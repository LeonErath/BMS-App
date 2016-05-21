package leon.bms.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbKurs;
import leon.bms.database.dbLehrer;
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


    // Methode nur für Testzwecke
    public void erstelleStundenplan(String result) {
        try {

            JSONObject jsonObjectAll = new JSONObject(result);
            String aktualisierungsDatum = jsonObjectAll.optString("zuletzt Aktualisiert");
            //Array mit allen Stundenplan Informationen
            JSONArray jsonArrayStundenplan = jsonObjectAll.getJSONArray("stundenPlan");
            //Array sortieren

            for (int i = 0; i < jsonArrayStundenplan.length(); i++) {
                JSONObject jsonObject = jsonArrayStundenplan.getJSONObject(i);
                dbSchulstunde schulstunde = new dbSchulstunde();
                String LKZ = jsonObject.optString("LKZ");
                String KURSID = jsonObject.optString("KURSID");
                Integer WOCHENTAG = jsonObject.optInt("WOCHENTAG");
                Integer STUNDE = jsonObject.optInt("STUNDE");
                Integer BN = jsonObject.optInt("BN");
                String RAUM = jsonObject.optString("RAUM");

                dbLehrer lehrer1 = new dbLehrer();
                if (!LKZ.equals("")) {
                    lehrer1 = lehrer1.getLehrer("kuerzel", LKZ);
                } else {

                }
                schulstunde.lehrer = lehrer1;
                //List<dbRaum> raum = dbRaum.find(dbRaum.class, "nummer = ?", RAUM);
                //dbRaum raum1 = raum.get(0);

                schulstunde.zuletztAktualisiert = aktualisierungsDatum;
                schulstunde.wochentag = WOCHENTAG;
                schulstunde.beginnzeit = STUNDE;
                schulstunde.kursID = KURSID;

                schulstunde.raum.nummer = RAUM;
                //schulstunde.raum = raum1;
                schulstunde.save();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        String date = calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE);

        String params = "pw=" + logInController.getPass() + "&username=" + logInController.getUsername(); //TODO Datum später hinzufügen
        Log.d("STUNDENPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                parseData(result2);
            }

            @Override
            public void onFailure(String result2) {
                onFailure(result2);
            }
        });
        atOnline2.execute();
    }
    public void parseData(String result){
        Log.d("RESULT", result);
        if (!result.equals("")) {

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
                    JSONObject leherliste = jsonObjectAll.getJSONObject("lehrerliste");
                    erstelleLehrerListe(String.valueOf(leherliste));
                    Log.d("leherliste", String.valueOf(leherliste));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Hier die AGs und Projektkurse
            if (new dbKurs().countKurse() == 0) {
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONObject pkuags = jsonObjectAll.getJSONObject("pkuags");

                    KurseController kurseController = new KurseController(mainContext);
                    Log.d("pkuags", String.valueOf(pkuags));
                    kurseController.erstelltPKundAGs(String.valueOf(pkuags));
                    Log.d("COUNT", new dbKurs().countKurse() + " ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Hier der Stundenplan und die Kurse
                try {
                    JSONObject jsonObjectAll = new JSONObject(result);
                    JSONObject stundenplan = jsonObjectAll.getJSONObject("timetable");

                    erstelleStundenplan(String.valueOf(stundenplan));
                    Log.d("STUNDENPLAN", "Stundenplan wurde erstellt.");

                    KurseController kurseController = new KurseController(mainContext);
                    kurseController.erstelltKurse();
                    Log.d("KURSE", "Kurse wurden erstellt.");
                    kurseController.connectKursStunden();
                    Log.d("stundenplan", String.valueOf(stundenplan));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




            listener.onSuccesss();
        }else {
            // wenn es keine Results gibt
            listener.onFailure();
        }
    }

    /**
     * @param result JSON Daten des Server für die Lehrer
     * @erstelltLehrerListe parsed die Daten für die Lehrer in die Datenbank
     */
    public void erstelleLehrerListe(String result) {
        try {
            JSONObject jsonObjectLehrerliste = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            JSONArray jsonArrayLehrer = jsonObjectLehrerliste.getJSONArray("lehrerliste");
            for (int i = 0; i < jsonArrayLehrer.length(); i++) {
                dbLehrer lehrer = new dbLehrer();
                JSONObject lehrerDaten = jsonArrayLehrer.getJSONObject(i);
                lehrer.name = lehrerDaten.optString("NAME");
                lehrer.email = lehrerDaten.optString("MAIL");
                lehrer.titel = lehrerDaten.optString("TITEL");
                lehrer.kuerzel = lehrerDaten.optString("KUERZEL");
                lehrer.Vorname = lehrerDaten.optString("VORNAME");
                JSONArray lehrerFaecher = lehrerDaten.getJSONArray("faecher");
                String faecher = "";
                for (int k = 0; k < lehrerFaecher.length(); k++) {
                    faecher += lehrerFaecher.getString(k) + ",";
                }
                lehrer.faecher = faecher;
                lehrer.save();


            }


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
