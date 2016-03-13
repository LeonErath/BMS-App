package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    Date now;
    // URL für die Daten des Quizes
    private static String quizUrl = "http://app.marienschule.de/files/scripts/getQuizData.php";

    public QuizController(Context context) {
        this.context = context;
    }

    /**
     * @getQuizData stellt eine Anfrage an das Quiz mit der uizUrl
     */
    public void getQuizData() {
        now = new Date();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("", "");
        String params = builder.build().getEncodedQuery();
        atOnline atOnline = new atOnline(quizUrl, params, context);
        atOnline.setUpdateListener(new atOnline.OnUpdateListener() {
            @Override
            public void onUpdate(String result) {
                parseQuizData(result);

            }
        });
        atOnline.execute();

    }

    /**
     * @param result JSON Daten der Server Anfrage
     * @parseQuizData parsed die JSON Daten vom Server in die Datenbank und stellt die entsprechenden
     * Relationships zwischen kurs themenbereich frage und Antwort auf.
     */
    public void parseQuizData(String result) {

        //Momentag zu Testzwecken wird die Datenbank jedesmal zurückgesetzt
        dbThemenbereich.deleteAll(dbThemenbereich.class);
        dbFragen.deleteAll(dbFragen.class);
        dbAntworten.deleteAll(dbAntworten.class);

        try {
            //parsed die Quizdaten
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONArray jsonArrayThemenbereiche = jsonObjectAll.getJSONArray("themes");

            // parsed die Themenbereiche
            for (int i = 0; i < jsonArrayThemenbereiche.length(); i++) {
                JSONObject jsonObject = jsonArrayThemenbereiche.getJSONObject(i);
                dbThemenbereich themenbereich = new dbThemenbereich();
                themenbereich.serverid = jsonObject.getInt("id");
                if (jsonObject.getString("wInfos") != null) {
                    themenbereich.infos = jsonObject.getString("wInfos");
                }
                themenbereich.name = jsonObject.getString("titel");
                themenbereich.zuletztAktualisiert = jsonObject.getString("change");
                if (new dbThemenbereich().themenbereichVorhanden(themenbereich.serverid) == false) {
                    String kursID = jsonObject.getString("kursid");
                    if (new dbKurs().getKursWithKursid(kursID) != null) {
                        //relationships between kurs and themenbereich
                        dbKurs kurs = new dbKurs().getKursWithKursid(kursID);
                        themenbereich.kurs = kurs;
                        themenbereich.save();
                    }

                }
            }
            //parsed die Fragen und Antworten
            JSONArray jsonArrayFragen = jsonObjectAll.getJSONArray("frag");
            for (int i = 0; i < jsonArrayFragen.length(); i++) {
                JSONObject jsonObject = jsonArrayFragen.getJSONObject(i);
                dbFragen fragen = new dbFragen();
                if (new dbThemenbereich().getThemenbereich(jsonObject.getInt("themenbereich")) != null) {
                    fragen.frage = jsonObject.getString("frage");
                    fragen.schwirigkeit = jsonObject.getInt("schwierigkeit");
                    fragen.serverid = jsonObject.getInt("id");
                    fragen.stufe = jsonObject.getString("stufe");
                    if (fragen.stufe.equals(new dbUser().getUser().stufe)) {
                        if (new dbFragen().frageVorhanden(fragen.serverid) == false) {
                            dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(jsonObject.getInt("themenbereich"));
                            fragen.themenbereich = themenbereich;
                            String kursID = jsonObject.getString("kursid");
                            if (new dbKurs().getKursWithKursid(kursID) != null) {
                                dbKurs kurs = new dbKurs().getKursWithKursid(kursID);
                                //Stellt die Relationships ein
                                fragen.kurs = kurs;
                                fragen.save();

                                JSONArray answers = jsonObject.getJSONArray("answers");
                                for (int l = 0; l < answers.length(); l++) {
                                    JSONObject jsonObjectAnswer = answers.getJSONObject(l);
                                    dbAntworten antworten = new dbAntworten();
                                    antworten.serverid = jsonObjectAnswer.getInt("id");
                                    antworten.antwort = jsonObjectAnswer.getString("text");
                                    antworten.richtig = jsonObjectAnswer.getBoolean("truth");
                                    antworten.langfassung = jsonObjectAnswer.getString("description");
                                    //Stellt die Relationships ein
                                    antworten.fragen = fragen;
                                    antworten.save();

                                }

                            }


                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(context, now.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
        // uses Callbacks for the Fragment to notice that the parsing is finnished
        updateUI.updateUI(zuletztAktualisiert);

    }

    /**
     * @return List der quizkurse die einen Themenbereich haben werden zurückgegeben
     * @getQuizKurs lädt die Kurse mit Themenbereich für da Quiz und konvertiert diese mit den nötigen
     * Daten in quizkurse und gibt diese zurück
     */
    public List<quizkurs> getQuizKurse() {
        // lädt alle Kurse
        List<dbKurs> kursList = new dbKurs().getActiveKurse(0);
        kursList.addAll(new dbKurs().getActiveKurse(1));

        // lädt alle Themenbereich der Kurse
        List<dbThemenbereich> themenbereichList = new ArrayList<>();
        for (dbKurs kurs : kursList) {
            if (kurs.getThemenbereiche(kurs.getId()) != null) {
                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
            }
        }
        // lädt alle Fragen der Themenbereiche
        List<dbFragen> fragenList = new ArrayList<>();
        if (themenbereichList.size() > 0) {
            for (dbThemenbereich themenbereich : themenbereichList) {
                if (themenbereich.getFragen(themenbereich.getId()) != null) {
                    fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
                }
            }

        }
        //konvertiert die Daten in ein Quizkurs Objekt
        List<quizkurs> quizkursList = new ArrayList<>();
        if (fragenList.size() > 0) {
            for (dbKurs kurs : kursList) {
                if (kurs.getThemenbereiche(kurs.getId()) != null) {
                    quizkurs quizkurs = new quizkurs();
                    quizkurs.kursFach = kurs.fach;
                    quizkurs.kursId = kurs.name;
                    quizkurs.lehrer = kurs.lehrer.titel + " " + kurs.lehrer.name;
                    quizkurs.fragen = fragenList.size();
                    List<quizthemen> quizthemenList = new ArrayList<>();
                    int fragen = 0;
                    themenbereichList = sortListASC(themenbereichList);
                    quizkurs.datum = themenbereichList.get(0).zuletztAktualisiert;

                    for (dbThemenbereich themenbereich : themenbereichList) {
                        if (new dbThemenbereich().getFragen(themenbereich.getId()) != null) {
                            fragen += new dbThemenbereich().getFragen(themenbereich.getId()).size();
                        }
                    }
                    quizkurs.fragen = fragen;
                    quizkursList.add(quizkurs);
                }
            }


        }
        if (quizkursList.size() > 0) {
            //quizkurs liste wird zurückgegeben
            return quizkursList;
        } else {
            return null;
        }

    }

    /**
     * @param list ist die Liste die nachdem Datum sortiert werden soll
     * @return gibt die sortierte Liste zurüc
     * @sortListASC sortiert die Liste nachdem Datum absteigend
     */
    public List<dbThemenbereich> sortListASC(List<dbThemenbereich> list) {
        Collections.sort(list, new Comparator<dbThemenbereich>() {
            @Override
            public int compare(dbThemenbereich lhs, dbThemenbereich rhs) {
                return lhs.zuletztAktualisiert.compareTo(rhs.zuletztAktualisiert);
            }
        });
        return list;
    }

    /**
     * @param kursid ist die ID des Kurses dessen Themenbereiche konvertiert werden sollen
     * @return gibt die Themenbereich als quizthemen liste zurück
     * @getTehmenbereiche lädt alle Themenbereich eines bestimmten Kurses und kovertiert diese
     * zu quizthemen objekt , die zum anzeigen im recylcerview benutz werden können.
     */
    public List<quizthemen> getThemenbereiche(String kursid) {
        //lädt alle Kurse
        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursid);
        if (kursList.size() == 1) {
            dbKurs kurs = kursList.get(0);
            //lädt die Themenbereiche des Kurses
            List<dbThemenbereich> themenbereichList = new ArrayList<>();
            if (kurs.getThemenbereiche(kurs.getId()) != null) {
                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
            }
            //lädt die Fragen des Kurses
            List<dbFragen> fragenList = new ArrayList<>();
            if (themenbereichList.size() > 0) {
                for (dbThemenbereich themenbereich : themenbereichList) {
                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
                    }
                }
            }
            // erstellt die Quizthemen Objekte
            List<quizthemen> quizthemenList = new ArrayList<>();
            if (fragenList.size() > 0) {
                if (kurs.getThemenbereiche(kurs.getId()) != null) {
                    for (dbThemenbereich themenbereich : kurs.getThemenbereiche(kurs.getId())) {
                        int counterRichtig = 0;
                        int size;

                        quizthemen quizthemen = new quizthemen();
                        quizthemen.themenbereich = themenbereich.name;
                        quizthemen.id = themenbereich.getId();
                        quizthemen.datum = themenbereich.zuletztAktualisiert;
                        if (themenbereich.getFragen(themenbereich.getId()) != null) {
                            size = themenbereich.getFragen(themenbereich.getId()).size();
                            quizthemen.fragen = size;
                            for (dbFragen fragen : themenbereich.getFragen(themenbereich.getId())) {
                                if (fragen.richtigCounter > 0) {
                                    counterRichtig++;
                                    Log.d("QuizController", "counter: " + counterRichtig);
                                }
                            }
                            if (size != 0) {
                                quizthemen.richtig = round(((double) counterRichtig / (double) size) * 100, 2);
                                Log.d("QuizController", "richtig: " + round((counterRichtig / size) * 100, 2));
                            }
                            quizthemen.lehrer = kurs.lehrer.titel + " " + kurs.lehrer.name;
                            quizthemen.kursId = kurs.name;
                            quizthemenList.add(quizthemen);
                        }

                    }
                }
            }

            if (quizthemenList.size() > 0) {
                // gibt die Liste der Quizthemen zurück
                return quizthemenList;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<dbFragen> getAll(String kursID) {

        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursID);
        if (kursList.size() == 1) {
            dbKurs kurs = kursList.get(0);
            //lädt die Themenbereiche des Kurses
            List<dbThemenbereich> themenbereichList = new ArrayList<>();
            if (kurs.getThemenbereiche(kurs.getId()) != null) {
                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
            }
            //lädt die Fragen des Kurses
            List<dbFragen> fragenList = new ArrayList<>();
            if (themenbereichList.size() > 0) {
                for (dbThemenbereich themenbereich : themenbereichList) {
                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
                    }
                }

            }
            if (fragenList.size() > 0) {
                return fragenList;
            }

        } else {
            return null;
        }
        return null;
    }

    //Interface Callbacks
    public interface UpdateUI {
        public void updateUI(String datum);
    }

    public void setUpdateInterface(UpdateUI updateInterface) {
        this.updateUI = updateInterface;
    }

}
