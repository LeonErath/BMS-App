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

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.model.klausurModel;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbKlausur;
import leon.bms.realm.dbKlausuraufsicht;
import leon.bms.realm.dbKlausurinhalt;
import leon.bms.realm.dbKurs;
import leon.bms.realm.dbLehrer;
import leon.bms.realm.dbNote;
import leon.bms.realm.dbRaum;

/**
 * Created by Leon E on 22.05.2016.
 */
public class KlausurController {
    OnUpdateListener listener;
    Context mainContext;
    List<dbKurs> kursList;
    RealmQueries realmQueries;
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

    public KlausurController(Context mainContext) {
        this.mainContext = mainContext;
        realmQueries = new RealmQueries(mainContext);
    }

    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.EXAM_URL;
        kursList = realmQueries.getSchriftlicheKurse();
        List<Integer> kursidList = new ArrayList<>();
        for (dbKurs kurs : kursList) {
            kursidList.add(kurs.getInt_id());
        }
        String kurse = "";
        for (Integer id : kursidList) {
            kurse += id + ",";
        }

        final String params = "username=" + realmQueries.getUser().getBenutzername() + "&password=" + new LogInController(mainContext).getPass() + "&course_ids=" + kurse + "&last_refresh="; //TODO richtige Paramerter hiinzufügen
        Log.d("params", params);

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
                        bgrealm.delete(dbNote.class);
                        bgrealm.delete(dbKlausur.class);
                        bgrealm.delete(dbKlausuraufsicht.class);
                        bgrealm.delete(dbKlausurinhalt.class);
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

    private void save(final RealmObject object) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                bgrealm.copyToRealmOrUpdate(object);
                Log.d("Fragment_Kursauswahl", "Saved Object");
            }
        });
    }


    private void parseData(final String result2) {
        if (kursList != null) {
            // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
            Realm.setDefaultConfiguration(realmConfig);
            // Get a Realm instance for this thread
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    for (dbKurs kurs : kursList) {
                        try {
                            JSONObject jsonResult = new JSONObject(result2);
                            if (jsonResult.has(String.valueOf(kurs.getInt_id()))) {
                                JSONObject jsonKlausur = jsonResult.getJSONObject(String.valueOf(kurs.getInt_id()));
                                dbKlausur klausur = new dbKlausur();

                                klausur.setName(jsonKlausur.getString("name"));
                                klausur.setDatum(jsonKlausur.getString("date"));
                                klausur.setBeginn(jsonKlausur.getString("start"));
                                klausur.setServerid(jsonKlausur.getInt("exam_id"));
                                klausur.setNotizen(jsonKlausur.getString("note"));

                                int roomid = jsonKlausur.getInt("room_id");
                                RealmResults<dbRaum> resultRaum = bgRealm.where(leon.bms.realm.dbRaum.class).equalTo("id", roomid).findAll();
                                if (resultRaum.size() == 1) {
                                    klausur.setRaum(resultRaum.get(0));
                                } else {
                                    Log.d("erstelleStundenplan", "Raum konnte nicht anhand der Serverid herausgefunden werden + id:" + roomid);
                                }
                                if (jsonKlausur.getInt("rest_course_eva") == 0) {
                                    klausur.setRestkursFrei(true);
                                } else {
                                    klausur.setRestkursFrei(false);
                                }

                                klausur.setKurs(kurs);
                                bgRealm.copyToRealmOrUpdate(klausur);

                                JSONArray contentArray = jsonKlausur.getJSONArray("content");
                                for (int i = 0; i < contentArray.length(); i++) {
                                    JSONObject jsonContent = contentArray.getJSONObject(i);
                                    dbKlausurinhalt inhalt = new dbKlausurinhalt();
                                    inhalt.setBeschreibung(jsonContent.getString("material_description"));
                                    inhalt.setExam_id(jsonContent.getInt("subject_material_number"));
                                    inhalt.setErledigt(false);
                                    inhalt.setKlausur(klausur);
                                    bgRealm.copyToRealmOrUpdate(inhalt);
                                }
                                JSONArray ausichtArray = jsonKlausur.getJSONArray("supervisors");
                                for (int i = 0; i < ausichtArray.length(); i++) {
                                    JSONObject jsonAufsicht = ausichtArray.getJSONObject(i);
                                    dbKlausuraufsicht aufsicht = new dbKlausuraufsicht();
                                    aufsicht.setStart(jsonAufsicht.getString("start"));
                                    aufsicht.setEnd(jsonAufsicht.getString("end"));
                                    aufsicht.setKlausur(klausur);

                                    int lehrerid = jsonAufsicht.getInt("teacher_id");
                                    RealmResults<dbLehrer> result2 = bgRealm.where(dbLehrer.class).equalTo("id", lehrerid).findAll();
                                    if (result2.size() == 1) {
                                        aufsicht.setLehrer(result2.get(0));
                                    } else {
                                        Log.d("erstelleStundenplan", "Lehrer konnte nicht anhand der Serverid herausgefunden werden + id:" + lehrerid);
                                    }
                                    bgRealm.copyToRealm(aufsicht);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                }
            });


        }
    }


    public List<klausurModel> getAllKlausuren() {
        List<klausurModel> convertedKlausurlist = new ArrayList<>();
        if (realmQueries.getKlausuren() != null) {
            List<dbKlausur> klausurlist =realmQueries.getKlausuren();
            List<klausurModel> klausurModelList = new ArrayList<>();
            List<klausurModel> klausurModelListinPast = new ArrayList<>();
            for (dbKlausur klausurObject : klausurlist) {
                klausurModel klausurModel = new klausurModel();
                klausurModel.raum = klausurObject.getRaum();
                klausurModel.klausur = klausurObject;
                if (klausurModel.mathDate() < 0) {
                    klausurModel.inThePast = true;
                    klausurModelListinPast.add(klausurModel);
                } else {
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
                return kl1.getDate().compareTo(kl2.getDate());
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



