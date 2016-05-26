package leon.bms.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbFach;
import leon.bms.database.dbKurs;
import leon.bms.database.dbKursart;
import leon.bms.database.dbLehrer;
import leon.bms.database.dbRaum;
import leon.bms.database.dbSchulstunde;

/**
 * Created by Leon E on 21.12.2015.
 */

/**
 * @StundenplanController ist die Klasse die alles bearbeitet was mit dem Stundenplan zutun hat. Es erstellt den Stundenplan und arbeitet
 * mit dem Kurs Controller zusammen. Es lädt alle Updates herunter für die Datenbank und parsed die Datein richtig in die Datenbank.
 */
public class StundenplanController {


    Context mainContext;
    String result = "";
    ProgressDialog progressDialog;
    OnUpdateListener listener;

    public StundenplanController(Context context) {
        Log.d("CONSTRUCTOR", " StundenlanController erstellt");
        mainContext = context;
    }


    /**
     * @checkUpdate macht eine Server Anfrage mit dem Datum des letzten Login des Users und überprüft
     * ob sich Daten verändert haben. Alle Daten werden in einer bestimmten Reihenfolge in die Datenbank
     * geparsed . Jede große Datenblock wird einzeln geparsed und auf verschiedene Methoden verteilt, sodass
     * man einen Überblick behält.
     */
    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.GET_ALL_DATA_URL;
        LogInController logInController = new LogInController(mainContext);
        // parsed Datum
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            calendar.setTime(sdf2.parse("1960-02-22 01:30:22"));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);

        String params = "username=erath&password=Ardaturan99"; //TODO richtige Paramerter hiinzufügen
        Log.d("STUNDENPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate",result2);
                parseData(result2);
            }

            @Override
            public void onFailure(String result2) {
                listener.onFailure();
            }
        });
        atOnline2.execute();
    }


    public void parseData(String result) {

            // nimmt das getAllData.php und guckt welche Daten aktuallisiert werden müssen
            // in folgeneder Reihenfolge:
            // 1 Lehrerliste
            // 2 Raum
            // 3 PK und AGs
            // 4 Stundenplan

            // Die einzelnen Daten sollten immer in ihren eigen Catch-Block stehen , sodass bei fehlern andere Daten noch
            // altualisiert werden können

            //Hier die Lehrerliste
            if (new dbLehrer().countLehrer() == 0) {
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONObject leherliste = jsonObjectAll.getJSONObject("teacher_data");
                    boolean leherercheck = saveLehrerliste(String.valueOf(leherliste));
                    Log.d("leherliste", String.valueOf(leherliste));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new dbRaum().countRaum() == 0) {
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONArray raumliste = jsonObjectAll.getJSONArray("room_data");
                    boolean roomcheck = saveRaumliste(raumliste);
                    Log.d("raum", String.valueOf(raumliste));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Hier die AGs und Projektkurse
            if (new dbKurs().countKurse() == 0) {
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONObject pkuags = jsonObjectAll.getJSONObject("pkuags");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Hier der Stundenplan und die Kurse
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONArray stundenplan = jsonObjectAll.getJSONArray("timetable_data");

                    erstelleStundenplan(stundenplan);
                    Log.d("stundenplan", String.valueOf(stundenplan));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            listener.onSuccesss();
    }

    private boolean saveRaumliste(JSONArray jsonArrayRaum) {
        try {
            for (int i = 0; i < jsonArrayRaum.length(); i++) {
                JSONObject jsonObjectRaum = jsonArrayRaum.getJSONObject(i);
                dbRaum raum = new dbRaum();
                raum.serverid = jsonObjectRaum.getInt("id");
                raum.nummer = jsonObjectRaum.getString("name");
                raum.save();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }

    /**
     * @param result JSON Daten des Server für die Lehrer
     * @erstelltLehrerListe parsed die Daten für die Lehrer in die Datenbank
     */
    public boolean saveLehrerliste(String result) {
        try {
            JSONObject jsonObjectLehrerliste = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            JSONArray jsonArrayLehrer = jsonObjectLehrerliste.getJSONArray("teachers");
            for (int i = 0; i < jsonArrayLehrer.length(); i++) {
                dbLehrer lehrer = new dbLehrer();
                JSONObject lehrerDaten = jsonArrayLehrer.getJSONObject(i);
                lehrer.serverid = lehrerDaten.optInt("id");
                lehrer.name = lehrerDaten.optString("last_name");
                lehrer.email = lehrerDaten.optString("email");
                lehrer.titel = lehrerDaten.optString("title");
                lehrer.kuerzel = lehrerDaten.optString("abbreviation");
                lehrer.Vorname = lehrerDaten.optString("first_name");
                lehrer.save();


            }


        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    // Methode nur für Testzwecke
    public void erstelleStundenplan(JSONArray timetablearray) {
        try {

            for (int i = 0; i < timetablearray.length(); i++) {
                JSONObject jsonObject = timetablearray.getJSONObject(i);
                dbFach fach = new dbFach();
                if (jsonObject.isNull("id")==false){
                    fach.serverId = jsonObject.optInt("id");
                }
               if (jsonObject.isNull("abbreviation")== false){
                   fach.kuerzel = jsonObject.optString("abbreviation");
               }else {
                   fach.kuerzel = "";
               }
                if (jsonObject.isNull("description")== false){
                    fach.name = jsonObject.optString("description");
                }else {
                    fach.name = "";
                }
                fach.save();
                JSONArray kurstyparray = jsonObject.getJSONArray("course_types");
                for (int k = 0; k < kurstyparray.length(); k++) {
                    JSONObject jsonObjectKursart = kurstyparray.getJSONObject(k);
                    dbKursart kursart = new dbKursart();
                    kursart.gloablId = jsonObjectKursart.getInt("global_id"); //TODO global id ist im moment immer null
                    kursart.abkuerzung = jsonObjectKursart.getString("abbreviation");
                    kursart.name = jsonObjectKursart.getString("name");
                    kursart.save();
                    JSONArray jsonArrayKurse = jsonObjectKursart.getJSONArray("courses");
                    for (int l = 0; l < jsonArrayKurse.length(); l++) {
                        JSONObject jsonObjectKurse = jsonArrayKurse.getJSONObject(l);
                        dbKurs kurs = new dbKurs();
                        kurs.serverid = jsonObjectKurse.getInt("int_id");
                        kurs.untisId = jsonObjectKurse.getString("id");
                        kurs.name = jsonObjectKurse.getString("name");
                        int lehrerid = jsonObjectKurse.getInt("teacher_id");
                        if (new dbLehrer().getLehereWithId(lehrerid) != null) {
                            dbLehrer lehrer = new dbLehrer().getLehereWithId(lehrerid);
                            kurs.lehrer = lehrer;
                        } else {
                            Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                        }
                        kurs.kursart = kursart;
                        kurs.fachnew = fach;
                        kurs.save();

                        JSONArray jsonArraySchulstunde = jsonObjectKurse.getJSONArray("lessons");
                        for (int m = 0; m < jsonArraySchulstunde.length(); m++) {
                            JSONObject jsonObjectSchulstunde = jsonArraySchulstunde.getJSONObject(m);
                            dbSchulstunde schulstunde = new dbSchulstunde();
                            schulstunde.beginnzeit = jsonObjectSchulstunde.getInt("lesson");
                            schulstunde.wochentag = jsonObjectSchulstunde.getInt("day");
                            schulstunde.blocknummer = jsonObjectSchulstunde.getInt("block_number");
                            schulstunde.serverId = jsonObjectSchulstunde.getInt("lesson_id");
                            int roomid = jsonObjectSchulstunde.getInt("room_id");
                            if (new dbLehrer().getLehereWithId(lehrerid) != null) {
                                dbLehrer lehrer = new dbLehrer().getLehereWithId(lehrerid);
                                schulstunde.lehrer = lehrer;
                            } else {
                                Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                            }
                            if (new dbRaum().getRaumWithId(roomid) != null) {
                                dbRaum raum = new dbRaum().getRaumWithId(roomid);
                                schulstunde.raum = raum;
                            } else {
                                Log.d("erstelleStundenplan", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                            }
                            schulstunde.kurs = kurs;
                            schulstunde.save();
                        }


                    }
                }


            }
            Log.d("erstelleStundenplan", "Success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnUpdateListener {
        public void onSuccesss();

        public void onFailure();
    }

    /**
     * Methode MUSS vorher in der Klasse aufgerufen werden
     * HIer wird das Interface "initlisiert"
     * Wenn dies nicht passiert ist stürtzt die Application ab
     */
    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }


}
