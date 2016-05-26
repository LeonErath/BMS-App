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

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbKurs;
import leon.bms.database.dbLehrer;
import leon.bms.database.dbRaum;
import leon.bms.database.dbSchulstunde;
import leon.bms.database.dbUser;
import leon.bms.database.dbVertretung;

/**
 * Created by Leon E on 25.05.2016.
 */
public class VertretungsplanController {

    OnUpdateListener listener;
    Context mainContext;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Boolean success = bundle.getBoolean("myKey");
            if (success) {
                listener.onSuccesss();
            } else {
                listener.onFailure();
            }
        }
    };

    public VertretungsplanController(Context context) {
        this.mainContext = context;
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
        List<dbKurs> alleKurse = new dbKurs().getAllActiveKurse();
        for (dbKurs k : alleKurse) {
            course_ids += k.serverid + ",";
        }


        String params = "username="+new LogInController(mainContext).getUsername()+"&password="+new LogInController(mainContext).getPass()+"&course_ids="+course_ids+"&last_refresh="; //TODO richtige Paramerter hiinzufügen
        Log.d("VERTREUNGSPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate", result2);
                dbVertretung.deleteAll(dbVertretung.class);
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
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    JSONArray jsonResult = new JSONArray(result2);
                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject jsonVertretung = jsonResult.getJSONObject(i);
                        dbVertretung vertretung = new dbVertretung();
                        vertretung.serverId = jsonVertretung.getInt("id");
                        if (!jsonVertretung.isNull("replacement_room")) {
                            int roomid = jsonVertretung.getInt("replacement_room");
                            int normalid = jsonVertretung.getInt("original_room");
                            if (roomid != normalid){
                                if (new dbRaum().getRaumWithId(roomid) != null) {
                                    dbRaum raum = new dbRaum().getRaumWithId(roomid);
                                    vertretung.raum = raum;
                                } else {
                                    Log.d("erstelleStundenplan", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                                }
                            }

                        }
                        if (!jsonVertretung.isNull("replacement_teacher")) {
                            int lehrerid = jsonVertretung.getInt("replacement_teacher");
                            int normalid = jsonVertretung.getInt("absence_teacher");
                            if (lehrerid != normalid){
                                if (new dbLehrer().getLehereWithId(lehrerid) != null) {
                                    dbLehrer lehrer = new dbLehrer().getLehereWithId(lehrerid);
                                    vertretung.lehrer = lehrer;
                                } else {
                                    Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                }
                            }

                        }

                        int eva = jsonVertretung.getInt("eva");
                        if (eva == 1) {
                            vertretung.eva = true;
                        } else {
                            vertretung.eva = false;
                        }
                        if (!jsonVertretung.isNull("text")) {
                            vertretung.notiz = jsonVertretung.getString("text");
                        }
                        int kursid = jsonVertretung.getInt("course_id");
                        int lesson = jsonVertretung.getInt("lesson");
                        String datum = jsonVertretung.getString("date");


                        if (new dbKurs().getKursWithServerId(kursid) != null) {
                            dbKurs kurs = new dbKurs().getKursWithServerId(kursid);
                            if (kurs.getSchulStunden(kurs.getId()) != null) {
                                List<dbSchulstunde> schulstundeList = kurs.getSchulStunden(kurs.getId());
                                if (getCalendarFromString(datum) != null) {
                                    Calendar cal = getCalendarFromString(datum);
                                    if (getWochentag(cal) != 100) {
                                        dbSchulstunde schulstunde = getSchulstundeWithWeekAndLesson(lesson,getWochentag(cal),schulstundeList);
                                        vertretung.schulstunde = schulstunde;
                                        Calendar calendarToday = Calendar.getInstance();
                                        calendarToday.set(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH-1);
                                        if (calendarToday.before(cal)){
                                            if (vertretung.raum != null || vertretung.lehrer != null || vertretung.eva.equals(1)){
                                                vertretung.save();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                    Message msg = handler.obtainMessage();
                    boolean sucess = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("myKey", sucess);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = handler.obtainMessage();
                    boolean sucess = false;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("myKey", sucess);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }


            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    public dbSchulstunde getSchulstundeWithWeekAndLesson(int lesson,int weekday,List<dbSchulstunde> schullist){
        for (dbSchulstunde schulstunde:schullist){
            if (schulstunde.beginnzeit == lesson && schulstunde.wochentag == weekday){
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
