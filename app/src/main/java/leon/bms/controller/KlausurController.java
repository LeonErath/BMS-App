package leon.bms.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbKlausur;
import leon.bms.database.dbKlausuraufsicht;
import leon.bms.database.dbKlausurinhalt;
import leon.bms.database.dbKurs;
import leon.bms.database.dbLehrer;
import leon.bms.database.klausur;

/**
 * Created by Leon E on 22.05.2016.
 */
public class KlausurController {
    OnUpdateListener listener;
    Context mainContext;
    List<dbKurs> kursList;

    public KlausurController(Context mainContext) {
        this.mainContext = mainContext;
    }

    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.EXAM_URL;
        kursList = new dbKurs().getAllActiveKurse();
        List<Integer> kursidList = new ArrayList<>();
        for (dbKurs kurs: kursList){
            kursidList.add(kurs.serverId);
        }
        String kurse="";
        for (Integer id: kursidList){
            kurse+=id+",";
        }

        String params = "username=erath&password=Ardaturan99&course_ids="+kurse+"&last_refresh="; //TODO richtige Paramerter hiinzufügen
        Log.d("params", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate",result2);
                dbKlausur.deleteAll(dbKlausur.class);
                dbKlausuraufsicht.deleteAll(dbKlausur.class);
                dbKlausurinhalt.deleteAll(dbKlausur.class);
                parseData(result2);
            }

            @Override
            public void onFailure(String result2) {
                listener.onFailure();
            }
        });
        atOnline2.execute();
    }

    private void parseData(String result2) {
        if (kursList != null) {
            for (dbKurs kurs : kursList) {
                try {
                    JSONObject jsonResult = new JSONObject(result2);
                    if (jsonResult.has(String.valueOf(kurs.serverId))){
                        JSONObject jsonKlausur = jsonResult.getJSONObject(String.valueOf(kurs.serverId));
                        dbKlausur klausur = new dbKlausur();
                        klausur.name = jsonKlausur.getString("name");
                        klausur.serverid = jsonKlausur.getInt("exam_id");
                        klausur.datum = jsonKlausur.getString("date");
                        klausur.beginn = jsonKlausur.getString("start");
                        klausur.ende = jsonKlausur.getString("end");
                        klausur.notizen = jsonKlausur.getString("note");
                        switch (jsonKlausur.getInt("rest_course_eva")){
                            case 0:klausur.restkursFrei = true;break;
                            case 1:klausur.restkursFrei = false;break;
                            default:break;
                        }

                        klausur.kurs = kurs;
                        klausur.save();
                        JSONArray contentArray = jsonKlausur.getJSONArray("content");
                        for (int i=0;i<contentArray.length();i++){
                            JSONObject jsonContent = contentArray.getJSONObject(i);
                            dbKlausurinhalt inhalt = new dbKlausurinhalt();
                            inhalt.inhaltIndex = jsonContent.getInt("subject_material_number");
                            inhalt.beschreibung = jsonContent.getString("material_description");
                            inhalt.klausur = klausur;
                            inhalt.save();
                        }
                        JSONArray ausichtArray = jsonKlausur.getJSONArray("supervisors");
                        for (int i=0;i<contentArray.length();i++){
                            JSONObject jsonAufsicht = ausichtArray.getJSONObject(i);
                            dbKlausuraufsicht aufsicht = new dbKlausuraufsicht();
                            aufsicht.start = jsonAufsicht.getString("start");
                            aufsicht.end = jsonAufsicht.getString("end");
                            aufsicht.klausur = klausur;
                            int lehrerid = jsonAufsicht.getInt("teacher_id");
                            if (new dbLehrer().getLehereWithId(lehrerid) != null) {
                                dbLehrer lehrer = new dbLehrer().getLehereWithId(lehrerid);
                                aufsicht.lehrer = lehrer;
                            } else {
                                Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                            }
                            aufsicht.save();
                        }

                    }
                    listener.onSuccesss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
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



