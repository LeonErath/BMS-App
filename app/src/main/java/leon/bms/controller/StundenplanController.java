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

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbLehrer;
import leon.bms.realm.dbSchulstunde;

/**
 * Created by Leon E on 21.12.2015.
 */

/**
 * @StundenplanController ist die Klasse die alles bearbeitet was mit dem Stundenplan zutun hat. Es erstellt den Stundenplan und arbeitet
 * mit dem Kurs Controller zusammen. Es lädt alle Updates herunter für die Datenbank und parsed die Datein richtig in die Datenbank.
 */
public class StundenplanController {

    boolean lehrerDownload, raumDownload, timetable;
    Context mainContext;
    String result = "";
    ProgressDialog progressDialog;
    OnUpdateListener listener;
    RealmQueries realmQueries;

    public StundenplanController(Context context) {
        Log.d("CONSTRUCTOR", " StundenlanController erstellt");
        mainContext = context;
        realmQueries = new RealmQueries(context);
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

        String params = "username=" + realmQueries.getUser().getBenutzername() + "&password=" + new LogInController(mainContext).getPass(); //TODO richtige Paramerter hiinzufügen
        Log.d("STUNDENPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate", result2);
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

        this.result = result;

        lehrerDownload = false;
        raumDownload = false;
        timetable = false;

        // nimmt das getAllData.php und guckt welche Daten aktuallisiert werden müssen
        // in folgeneder Reihenfolge:
        // 1 Lehrerliste
        // 2 Raum
        // 3 PK und AGs
        // 4 Stundenplan

        // Die einzelnen Daten sollten immer in ihren eigen Catch-Block stehen , sodass bei fehlern andere Daten noch
        // altualisiert werden können

        //Hier die Lehrerliste

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONObject leherliste = jsonObjectAll.getJSONObject("teacher_data");
            //saveLehrerliste(String.valueOf(leherliste));
            Log.d("leherliste", String.valueOf(leherliste));
            saveLehrerRealm(String.valueOf(leherliste));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONArray raumliste = jsonObjectAll.getJSONArray("room_data");
            //saveRaumliste(raumliste);
            saveRaumRealm(raumliste);
            Log.d("raum", String.valueOf(raumliste));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Hier die AGs und Projektkurse

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONObject pkuags = jsonObjectAll.getJSONObject("pkuags");


        } catch (JSONException e) {
            e.printStackTrace();
        }




        //listener.onSuccesss();
    }


    private void saveRaumRealm(final JSONArray jsonArrayRaum) {
        Log.d("Stundenplan", "Started Transaction");
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                try {
                    for (int i = 0; i < jsonArrayRaum.length(); i++) {
                        JSONObject jsonObjectRaum = jsonArrayRaum.getJSONObject(i);
                        final leon.bms.realm.dbRaum raum1 = new leon.bms.realm.dbRaum();
                        raum1.setId(jsonObjectRaum.getInt("id"));
                        raum1.setName(jsonObjectRaum.getString("name"));
                        bgRealm.copyToRealmOrUpdate(raum1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                // Transaction was a success.
                //Realm realm = Realm.getDefaultInstance();
                //Log.d("StundenplanController2","Size: "+realm.where(leon.bms.realm.dbLehrer.class).findAll().size());
                Log.d("Stundenplan", "Raum : Successful Transaction");
                raumDownload = true;
                update();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.

                Log.d("Stundenplan", "Raum : No successful Transaction");
                raumDownload = true;
                update();

            }
        });

    }

    /**
     * @param result JSON Daten des Server für die Lehrer
     * @erstelltLehrerListe parsed die Daten für die Lehrer in die Datenbank
     */


    public void saveLehrerRealm(final String result) {
        Log.d("Stundenplan", "Started Realm Transaction");
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                try {
                    JSONObject jsonObjectLehrerliste = new JSONObject(result);
                    JSONArray jsonArrayLehrer = jsonObjectLehrerliste.getJSONArray("teachers");

                    for (int i = 0; i < jsonArrayLehrer.length(); i++) {
                        JSONObject lehrerDaten = jsonArrayLehrer.getJSONObject(i);

                        dbLehrer lehrer2 = new dbLehrer();
                        lehrer2.setId(lehrerDaten.optInt("id"));
                        lehrer2.setLast_name(lehrerDaten.optString("last_name"));
                        lehrer2.setEmail(lehrerDaten.optString("email"));
                        lehrer2.setTitle(lehrerDaten.optString("title"));
                        lehrer2.setFirst_name(lehrerDaten.optString("first_name"));
                        lehrer2.setAbbreviation(lehrerDaten.optString("abbreviation"));
                        Log.d("Stundenplan", "Lehrer: "+lehrer2.getId());
                        bgRealm.copyToRealmOrUpdate(lehrer2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                // Transaction was a success.
                //Realm realm = Realm.getDefaultInstance();
                //Log.d("StundenplanController2","Size: "+realm.where(leon.bms.realm.dbLehrer.class).findAll().size());

                Log.d("Stundenplan", "Lehrer : Successful Transaction");
                lehrerDownload = true;
                update();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.

                Log.d("Stundenplan", "Lehrer : No successful Transaction");
                lehrerDownload = true;
                update();

            }
        });


    }





    public void saveTimetableRealm(final JSONArray timetablearray) {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                try {
                    for (int i = 0; i < timetablearray.length(); i++) {
                        JSONObject jsonObject = timetablearray.getJSONObject(i);
                        leon.bms.realm.dbFach fach1 = new leon.bms.realm.dbFach();
                        if (jsonObject.isNull("id") == false) {
                            fach1.setId(jsonObject.optInt("id"));
                            if (jsonObject.isNull("abbreviation") == false) {
                                fach1.setAbbreviation(jsonObject.optString("abbreviation"));
                            } else {
                                fach1.setAbbreviation("");
                            }
                            if (jsonObject.isNull("description") == false) {
                                fach1.setDescription(jsonObject.optString("description"));
                            } else {
                                fach1.setDescription("");

                            }
                            bgRealm.copyToRealm(fach1);
                            JSONArray kurstyparray = jsonObject.getJSONArray("course_types");
                            for (int k = 0; k < kurstyparray.length(); k++) {
                                JSONObject jsonObjectKursart = kurstyparray.getJSONObject(k);

                                final leon.bms.realm.dbKursart kursart1 = new leon.bms.realm.dbKursart();
                                kursart1.setGloablId(jsonObjectKursart.getInt("global_id"));
                                kursart1.setAbbreviation(jsonObjectKursart.getString("abbreviation"));
                                kursart1.setName(jsonObjectKursart.getString("name"));
                                bgRealm.copyToRealm(kursart1);

                                final JSONArray jsonArrayKurse = jsonObjectKursart.getJSONArray("courses");
                                for (int l = 0; l < jsonArrayKurse.length(); l++) {
                                    JSONObject jsonObjectKurse = jsonArrayKurse.getJSONObject(l);

                                    final leon.bms.realm.dbKurs kurs1 = new leon.bms.realm.dbKurs();
                                    kurs1.setInt_id(jsonObjectKurse.optInt("int_id"));
                                    kurs1.setId(jsonObjectKurse.optString("id"));
                                    kurs1.setName(jsonObjectKurse.optString("name"));
                                    int lehrerid = jsonObjectKurse.optInt("teacher_id");
                                    RealmResults<dbLehrer> result2 = bgRealm.where(dbLehrer.class).equalTo("id", lehrerid).findAll();
                                    if (result2.size() == 1) {
                                        kurs1.setLehrer(result2.get(0));
                                    } else {
                                        Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                    }
                                    kurs1.setKursart(kursart1);
                                    kurs1.setFach(fach1);
                                    bgRealm.copyToRealmOrUpdate(kurs1);

                                    final JSONArray jsonArraySchulstunde = jsonObjectKurse.getJSONArray("lessons");
                                    for (int m = 0; m < jsonArraySchulstunde.length(); m++) {
                                        JSONObject jsonObjectSchulstunde = jsonArraySchulstunde.getJSONObject(m);

                                        final dbSchulstunde schulstunde1 = new dbSchulstunde();
                                        schulstunde1.setLesson_id(jsonObjectSchulstunde.optInt("lesson_id"));
                                        schulstunde1.setDay(jsonObjectSchulstunde.optInt("day"));
                                        schulstunde1.setLesson(jsonObjectSchulstunde.optInt("lesson"));
                                        schulstunde1.setBlock_number(jsonObjectSchulstunde.optInt("block_number"));
                                        int roomid = jsonObjectSchulstunde.optInt("room_id");

                                        RealmResults<leon.bms.realm.dbRaum> resultRaum = bgRealm.where(leon.bms.realm.dbRaum.class).equalTo("id", roomid).findAll();
                                        if (resultRaum.size() == 1) {
                                            schulstunde1.setRaum(resultRaum.get(0));
                                        } else {
                                            Log.d("erstelleStundenplan", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                                        }
                                        if (result2.size() == 1) {
                                            schulstunde1.setLehrer(result2.get(0));
                                        } else {
                                            Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                        }
                                        schulstunde1.setKurs(kurs1);
                                        bgRealm.copyToRealmOrUpdate(schulstunde1);

                                    }


                                }

                            }
                        }

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("Stundenplan", "Stundenplan : Successful Transaction");
                timetable = true;
                update();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("Stundenplan", "Stundenplan : No successful Transaction " + error);
                Log.d("Stundenplan", "Error" + error.getLocalizedMessage());
                Log.d("Stundenplan", "Error" + error.getMessage());
                Log.d("Stundenplan", "Error" + error.getCause());
                timetable = true;
                update();
            }
        });


    }


    public void update() {
        if (lehrerDownload && raumDownload && !timetable){
            //Hier der Stundenplan und die Kurse
            try {
                JSONObject jsonObjectAll = new JSONObject(result);
                JSONArray stundenplan = jsonObjectAll.getJSONArray("timetable_data");

                //erstelleStundenplan(stundenplan);
                saveTimetableRealm(stundenplan);
                Log.d("stundenplan", String.valueOf(stundenplan));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        if (lehrerDownload && raumDownload && timetable) {
            listener.onSuccesss();
            Log.d("StundenplanController", "Download Completed");
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
