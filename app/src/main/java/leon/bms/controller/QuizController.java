package leon.bms.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.model.quizthemen;
import leon.bms.realm.dbKurs;

/**
 * Created by Leon E on 27.02.2016.
 */

/**
 * @QuizController enthält alle Methode für die Verwaltung der QuizDaten sowie das herunterladen neuer Quiz daten.
 * Der QuizController wird von allen QuizFragementen benutzt und gibt für jedes Fragment die notwendigen Daten zurück.
 */
public class QuizController {
    UpdateUI updateUI;
    Context context;
    List<dbKurs> kursList;
    Date now;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Boolean success = bundle.getBoolean("myKey");
            if (success) {
                updateUI.success();
            } else {
                updateUI.failure();
            }
        }
    };

    // URL für die Daten des Quizes
    private static String quizUrl = Constants.QUIZ_URL;

    public QuizController(Context context) {
        this.context = context;
    }

//    /**
//     * @getQuizData stellt eine Anfrage an das Quiz mit der uizUrl
//     */
//    public void checkUpdate() {
//        // WICHTIGE Url für die Anfrage an den Server
//        String Url = Constants.EXAM_URL;
//        kursList = new dbKurs().getAllActiveKurse();
//        List<Integer> kursidList = new ArrayList<>();
//        for (dbKurs kurs : kursList) {
//            kursidList.add(kurs.serverid);
//        }
//        String kurse = "";
//        for (Integer id : kursidList) {
//            kurse += id + ",";
//        }
//
//        String params = "username="+new LogInController(context).getUsername() +"&password="+new LogInController(context).getPass()+"&course_ids="+kurse+"&last_refresh="; //TODO richtige Paramerter hiinzufügen
//        Log.d("params", params);
//
//        atOnline atOnline2 = new atOnline(Url, params, context);
//        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {
//
//            @Override
//            public void onSuccesss(String result2) {
//                Log.d("checkUpdate", result2);
//                dbNote.deleteAll(dbNote.class);
//                dbKlausur.deleteAll(dbKlausur.class);
//                dbKlausuraufsicht.deleteAll(dbKlausur.class);
//                dbKlausurinhalt.deleteAll(dbKlausur.class);
//                parseData(result2);
//            }
//
//            @Override
//            public void onFailure(String result2) {
//                updateUI.failure();
//            }
//        });
//        atOnline2.execute();
//    }
//
//    private void parseData(final String result2) {
//        Runnable runnable = new Runnable() {
//            public void run() {
//                try {
//                    JSONArray jsonarray = new JSONArray(result2);
//
//                    for (int i = 0; i < jsonarray.length(); i++) {
//                        JSONObject jsonQuiz = jsonarray.getJSONObject(i);
//                        dbQuiz quiz = new dbQuiz();
//                        quiz.intergerid = jsonQuiz.getInt("id");
//                        quiz.name = jsonQuiz.getString("name");
//                        quiz.beschreibung = jsonQuiz.getString("description");
//                        quiz.erstelltAm = jsonQuiz.getString("creation_date");
//                        quiz.geandertAm = jsonQuiz.getString("last_change");
//                        quiz.gesamtePunkte = jsonQuiz.getInt("total_points");
//                        quiz.schwierigkeitsgrad = jsonQuiz.getInt("level_of_difficulty");
//                        int kursid = jsonQuiz.getInt("course_id");
//                        if (new dbKurs().getKursWithServerId(kursid) != null) {
//                            dbKurs kurs = new dbKurs().getKursWithServerId(kursid);
//                            quiz.kurs = kurs;
//                        }
//                        if (!jsonQuiz.isNull("global_id")) {
//                            quiz.globaleid = jsonQuiz.getInt("global_id");
//                        }
//                        quiz.save();
//                        JSONArray jsonFragenArray = jsonQuiz.getJSONArray("questions");
//                        for (int k = 0; k < jsonFragenArray.length(); k++) {
//                            JSONObject jsonFragen = jsonFragenArray.getJSONObject(k);
//                            dbFragen fragen = new dbFragen();
//                            fragen.serverid = jsonFragen.getInt("id");
//
//
//                        }
//
//
//                    }
//
//
//                    Message msg = handler.obtainMessage();
//                    boolean sucess = true;
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("myKey", sucess);
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                } catch (JSONException e) {
//                    Message msg = handler.obtainMessage();
//                    boolean sucess = false;
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("myKey", sucess);
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                    e.printStackTrace();
//                }
//
//
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();
//    }
//
//
//    /**
//     * @param list ist die Liste die nachdem Datum sortiert werden soll
//     * @return gibt die sortierte Liste zurüc
//     * @sortListASC sortiert die Liste nachdem Datum absteigend
//     */
//    public List<dbThemenbereich> sortListASC(List<dbThemenbereich> list) {
//        Collections.sort(list, new Comparator<dbThemenbereich>() {
//            @Override
//            public int compare(dbThemenbereich lhs, dbThemenbereich rhs) {
//                return lhs.zuletztAktualisiert.compareTo(rhs.zuletztAktualisiert);
//            }
//        });
//        return list;
//    }
//
//    /**
//     * @param kursid ist die ID des Kurses dessen Themenbereiche konvertiert werden sollen
//     * @return gibt die Themenbereich als quizthemen liste zurück
//     * @getTehmenbereiche lädt alle Themenbereich eines bestimmten Kurses und kovertiert diese
//     * zu quizthemen objekt , die zum anzeigen im recylcerview benutz werden können.
//     */
//    public List<quizthemen> getThemenbereiche(String kursid) {
//        //lädt alle Kurse
//        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursid);
//        if (kursList.size() == 1) {
//            dbKurs kurs = kursList.get(0);
//            //lädt die Themenbereiche des Kurses
//            List<dbThemenbereich> themenbereichList = new ArrayList<>();
//            if (kurs.getThemenbereiche(kurs.getId()) != null) {
//                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
//            }
//            //lädt die Fragen des Kurses
//            List<dbFragen> fragenList = new ArrayList<>();
//            if (themenbereichList.size() > 0) {
//                for (dbThemenbereich themenbereich : themenbereichList) {
//                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
//                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
//                    }
//                }
//            }
//            // erstellt die Quizthemen Objekte
//            List<quizthemen> quizthemenList = new ArrayList<>();
//            if (fragenList.size() > 0) {
//                if (kurs.getThemenbereiche(kurs.getId()) != null) {
//                    for (dbThemenbereich themenbereich : kurs.getThemenbereiche(kurs.getId())) {
//                        int counterRichtig = 0;
//                        int size;
//
//                        quizthemen quizthemen = new quizthemen();
//                        quizthemen.themenbereich = themenbereich.name;
//                        quizthemen.id = themenbereich.getId();
//                        quizthemen.datum = themenbereich.zuletztAktualisiert;
//                        if (themenbereich.getFragen(themenbereich.getId()) != null) {
//                            size = themenbereich.getFragen(themenbereich.getId()).size();
//                            quizthemen.fragen = size;
//                            for (dbFragen fragen : themenbereich.getFragen(themenbereich.getId())) {
//                                if (fragen.getAnzahlRichtigBeantwortet() > 0) {
//                                    counterRichtig++;
//                                    Log.d("QuizController", "counter: " + counterRichtig);
//                                }
//                            }
//                            if (size != 0) {
//                                quizthemen.richtig = round(((double) counterRichtig / (double) size) * 100, 2);
//                                Log.d("QuizController", "richtig: " + round((counterRichtig / size) * 100, 2));
//                            }
//                            quizthemen.lehrer = kurs.lehrer.titel + " " + kurs.lehrer.name;
//                            quizthemen.kursId = kurs.name;
//                            quizthemenList.add(quizthemen);
//                        }
//
//                    }
//                }
//            }
//
//            if (quizthemenList.size() > 0) {
//                // gibt die Liste der Quizthemen zurück
//                return quizthemenList;
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }
//
//    }
//
//    public static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
//
//    public List<dbFragen> getAll(String kursID) {
//
//        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursID);
//        if (kursList.size() == 1) {
//            dbKurs kurs = kursList.get(0);
//            //lädt die Themenbereiche des Kurses
//            List<dbThemenbereich> themenbereichList = new ArrayList<>();
//            if (kurs.getThemenbereiche(kurs.getId()) != null) {
//                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
//            }
//            //lädt die Fragen des Kurses
//            List<dbFragen> fragenList = new ArrayList<>();
//            if (themenbereichList.size() > 0) {
//                for (dbThemenbereich themenbereich : themenbereichList) {
//                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
//                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
//                    }
//                }
//
//            }
//            if (fragenList.size() > 0) {
//                return fragenList;
//            }
//
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    public List<dbFragen> getNewest(String kursID) {
//
//        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursID);
//        if (kursList.size() == 1) {
//            dbKurs kurs = kursList.get(0);
//            //lädt die Themenbereiche des Kurses
//            List<dbThemenbereich> themenbereichList = new ArrayList<>();
//            if (kurs.getThemenbereiche(kurs.getId()) != null) {
//                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
//            }
//            //lädt die Fragen des Kurses
//            List<dbFragen> fragenList = new ArrayList<>();
//            if (themenbereichList.size() > 0) {
//                for (dbThemenbereich themenbereich : themenbereichList) {
//                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
//                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
//                    }
//                }
//
//            }
//            if (fragenList.size() > 0) {
//
//                return sortListDate(fragenList);
//            }
//
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    /**
//     * @param fragenList Liste die nach dem Datum sortiert werden soll
//     * @return gibt die sortierte Liste zurück
//     * @sortListDate sortiert die Liste nach ihrem Datum absteigen
//     */
//    private List<dbFragen> sortListDate(List<dbFragen> fragenList) {
//        Collections.sort(fragenList, new Comparator<dbFragen>() {
//            public int compare(dbFragen fragen1, dbFragen fragen2) {
//                return stringToCalander(fragen1.getDateLetzteRichtigeAntwort()).getTime().compareTo(stringToCalander(fragen2.getDateLetzteRichtigeAntwort()).getTime());
//            }
//        });
//        Collections.reverse(fragenList);
//        return fragenList;
//    }
//
//    /**
//     * @param date date in String
//     * @return gibt Date in Calendar zurück
//     * @stringToCalander parsed ein Datum vom Datentyp String zum Datentyp Calendar, dies
//     * ist wichtig für das sortieren nach dem Datum
//     */
//    public Calendar stringToCalander(String date) {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            calendar.setTime(sdf2.parse(date));// all done
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return calendar;
//    }
//
//    public List<dbFragen> getWrong(String kursID) {
//
//        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursID);
//        if (kursList.size() == 1) {
//            dbKurs kurs = kursList.get(0);
//            //lädt die Themenbereiche des Kurses
//            List<dbThemenbereich> themenbereichList = new ArrayList<>();
//            if (kurs.getThemenbereiche(kurs.getId()) != null) {
//                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
//            }
//            //lädt die Fragen des Kurses
//            List<dbFragen> fragenList = new ArrayList<>();
//            if (themenbereichList.size() > 0) {
//                for (dbThemenbereich themenbereich : themenbereichList) {
//                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
//                        List<dbFragen> fragenListAlle = themenbereich.getFragen(themenbereich.getId());
//                        for (dbFragen fragen : fragenListAlle) {
//                            if (fragen.getAnzahlFalschtBeantwortet() > fragen.getAnzahlRichtigBeantwortet()) {
//                                Log.d("QuizController", "falsch: " + fragen.getAnzahlFalschtBeantwortet() + " richtig: " + fragen.getAnzahlRichtigBeantwortet());
//                                fragenList.add(fragen);
//                            }
//                        }
//
//                    }
//                }
//                return fragenList;
//            }
//        }
//        return null;
//
//    }

    //Interface Callbacks
    public interface UpdateUI {
        public void updateUI(String datum);

        public void success();

        public void failure();
    }

    public void setUpdateInterface(UpdateUI updateInterface) {
        this.updateUI = updateInterface;
    }

}
