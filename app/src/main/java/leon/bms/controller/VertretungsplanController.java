package leon.bms.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbKlausur;
import leon.bms.realm.dbKlausuraufsicht;
import leon.bms.realm.dbKlausurinhalt;
import leon.bms.realm.dbKurs;
import leon.bms.realm.dbLehrer;
import leon.bms.realm.dbNote;
import leon.bms.realm.dbRaum;
import leon.bms.realm.dbSchulstunde;
import leon.bms.realm.dbVertretung;

/**
 * Created by Leon E on 25.05.2016.
 */
public class VertretungsplanController {

    OnUpdateListener listener;
    Context mainContext;
    RealmQueries realmQueries;


    public VertretungsplanController(Context context) {
        this.mainContext = context;
        realmQueries = new RealmQueries(context);
    }

    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.VERTRETUNGSPLAN_URL;
        LogInController logInController = new LogInController(mainContext);
        // parsed Datum
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
        String datum = sdf2.format(calendar.getTime());
        String course_ids = "";
        List<dbKurs> alleKurse = realmQueries.getAktiveKurse();
        for (dbKurs k : alleKurse) {
            course_ids += k.getInt_id() + ",";
        }


        String params = "username="+realmQueries.getUser().getBenutzername()+"&password="+new LogInController(mainContext).getPass()+"&course_ids="+course_ids+"&last_refresh="; //TODO richtige Paramerter hiinzufügen
        Log.d("VERTREUNGSPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate", result2);
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
                Realm.setDefaultConfiguration(realmConfig);
                // Get a Realm instance for this thread
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgrealm) {
                        bgrealm.delete(dbVertretung.class);
                    }
                });

                parseData(result2);
            }

            @Override
            public void onFailure(String result2) {
                listener.onFailure();
            }
        });
        atOnline2.execute();
    }

    private void parseData(final String result2) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                try {
                    JSONArray jsonResult = new JSONArray(result2);
                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject jsonVertretung = jsonResult.getJSONObject(i);
                        dbVertretung vertretung = new dbVertretung();
                        vertretung.setServerId(jsonVertretung.getInt("id"));
                        if (!jsonVertretung.isNull("replacement_room")) {
                            int roomid = jsonVertretung.getInt("replacement_room");
                            int normalid = jsonVertretung.getInt("original_room");
                            if (roomid != normalid) {
                                RealmResults<dbRaum> resultRaum = bgRealm.where(leon.bms.realm.dbRaum.class).equalTo("id", roomid).findAll();
                                if (resultRaum.size() == 1) {
                                    vertretung.setRaum(resultRaum.get(0));
                                } else {
                                    Log.d("erstelleStundenplan", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                                }
                            }

                        }
                        if (!jsonVertretung.isNull("replacement_teacher")) {
                            int lehrerid = jsonVertretung.getInt("replacement_teacher");
                            int normalid = jsonVertretung.getInt("absence_teacher");
                            if (lehrerid != normalid) {
                                RealmResults<dbLehrer> result2 = bgRealm.where(dbLehrer.class).equalTo("id", lehrerid).findAll();
                                if (result2.size() == 1) {
                                    vertretung.setLehrer(result2.get(0));
                                } else {
                                    Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                }
                            }

                        }

                        int eva = jsonVertretung.getInt("eva");
                        if (eva == 1) {
                            vertretung.setEva(true);
                        } else {
                            vertretung.setEva(false);
                        }
                        if (!jsonVertretung.isNull("text")) {
                            vertretung.setNotiz(jsonVertretung.getString("text"));
                        }
                        int kursid = jsonVertretung.getInt("course_id");
                        int lesson = jsonVertretung.getInt("lesson");
                        String datum = jsonVertretung.getString("date");


                        if (realmQueries.getKurs(kursid) != null) {
                            dbKurs kurs = realmQueries.getKurs(kursid);
                            if (realmQueries.getStunden(kurs) != null) {
                                List<dbSchulstunde> schulstundeList =realmQueries.getStunden(kurs) ;
                                if (getCalendarFromString(datum) != null) {
                                    Calendar cal = getCalendarFromString(datum);
                                    if (getWochentag(cal) != 100) {
                                        dbSchulstunde schulstunde = getSchulstundeWithWeekAndLesson(lesson, getWochentag(cal), schulstundeList);
                                        vertretung.setSchulstunde(schulstunde);
                                        Calendar calendarToday = Calendar.getInstance();
                                        calendarToday.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH - 1);
                                        if (calendarToday.before(cal)) {
                                            if (vertretung.getRaum() != null || vertretung.getLehrer() != null || vertretung.getEva() == true) {
                                                bgRealm.copyToRealm(vertretung);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                listener.onSuccesss();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                listener.onFailure();
            }
        });





    }

    public dbSchulstunde getSchulstundeWithWeekAndLesson(int lesson,int weekday,List<dbSchulstunde> schullist){
        for (dbSchulstunde schulstunde:schullist){
            if (schulstunde.getLesson() == lesson && schulstunde.getDay() == weekday){
                return schulstunde;
            }
        }
        return null;
    }

    public int getWochentag(Calendar cal) {
        // aktueller Wochentag wird ausgerechnet
        Calendar calendar = cal;
        //Sunday= 1 Monday = 2 ...
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day > 1 && day < 7) {
            return day - 1;
        } else {
            return 100;
        }
    }

    public Calendar getCalendarFromString(String calString) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(calString));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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
