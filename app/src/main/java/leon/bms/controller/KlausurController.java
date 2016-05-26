package leon.bms.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbKlausur;
import leon.bms.database.dbKlausuraufsicht;
import leon.bms.database.dbKlausurinhalt;
import leon.bms.database.dbKurs;
import leon.bms.database.dbLehrer;
import leon.bms.database.dbNote;
import leon.bms.database.dbRaum;
import leon.bms.model.klausurModel;

/**
 * Created by Leon E on 22.05.2016.
 */
public class KlausurController {
    OnUpdateListener listener;
    Context mainContext;
    List<dbKurs> kursList;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Boolean success = bundle.getBoolean("myKey");
            if (success) {
                listener.onSuccesss();
            }else {
                listener.onFailure();
            }
        }
    };

    public KlausurController(Context mainContext) {
        this.mainContext = mainContext;
    }

    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.EXAM_URL;
        kursList = new dbKurs().getAllActiveKurse();
        List<Integer> kursidList = new ArrayList<>();
        for (dbKurs kurs: kursList){
            kursidList.add(kurs.serverid);
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
                dbNote.deleteAll(dbNote.class);
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

    private void parseData(final String result2) {
        Runnable runnable = new Runnable() {
            public void run() {
                if (kursList != null) {
                    for (dbKurs kurs : kursList) {
                        try {
                            JSONObject jsonResult = new JSONObject(result2);
                            if (jsonResult.has(String.valueOf(kurs.serverid))){
                                JSONObject jsonKlausur = jsonResult.getJSONObject(String.valueOf(kurs.serverid));
                                dbKlausur klausur = new dbKlausur();
                                klausur.name = jsonKlausur.getString("name");
                                klausur.serverid = jsonKlausur.getInt("exam_id");
                                klausur.datum = jsonKlausur.getString("date");
                                klausur.beginn = jsonKlausur.getString("start");
                                klausur.ende = jsonKlausur.getString("end");
                                klausur.notizen = jsonKlausur.getString("note");
                                int roomid = jsonKlausur.getInt("room_id");
                                if (new dbRaum().getRaumWithId(roomid) != null) {
                                    dbRaum raum = new dbRaum().getRaumWithId(roomid);
                                    klausur.raum = raum;
                                } else {
                                    Log.d("parseData", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                                }
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
                                    inhalt.erledigt = false;
                                    inhalt.klausur = klausur;
                                    inhalt.save();
                                }
                                JSONArray ausichtArray = jsonKlausur.getJSONArray("supervisors");
                                for (int i=0;i<ausichtArray.length();i++){
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
                                        Log.d("parseData", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                    }
                                    aufsicht.save();
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
                }

            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public List<klausurModel> getAllKlausuren(){
        List<klausurModel> convertedKlausurlist = new ArrayList<>();
        if (new dbKlausur().getAllKLausur() != null){
            List<dbKlausur> klausurlist = new dbKlausur().getAllKLausur();
            List<klausurModel> klausurModelList = new ArrayList<>();
            List<klausurModel> klausurModelListinPast = new ArrayList<>();
            for (dbKlausur klausurObject:klausurlist){
                klausurModel klausurModel = new klausurModel();
                klausurModel.raum = klausurObject.raum;
                klausurModel.klausur = klausurObject;
                if (klausurModel.mathDate() < 0){
                    klausurModel.inThePast = true;
                    klausurModelListinPast.add(klausurModel);
                }else {
                    klausurModelList.add(klausurModel);
                }

            }
            klausurModelListinPast = sortListDate(klausurModelListinPast);
            klausurModelList = sortListDate(klausurModelList);
            convertedKlausurlist.addAll(klausurModelList);
            convertedKlausurlist.addAll(klausurModelListinPast);
            return convertedKlausurlist;
        }
        return null;
    }

    private List<klausurModel> sortListDate(List<klausurModel> klausurModelList) {
        Collections.sort(klausurModelList, new Comparator<klausurModel>() {
            public int compare(klausurModel kl1, klausurModel kl2) {
                return kl1.getDate().compareTo( kl2.getDate());
            }
        });
        Collections.reverse(klausurModelList);
        return klausurModelList;
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



