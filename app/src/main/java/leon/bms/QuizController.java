package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */
public class QuizController {
    UpdateUI updateUI;
    Context context;
    Date now;
    private static String quizUrl = "http://app.marienschule.de/files/scripts/getQuizData.php";

    public QuizController(Context context) {
        this.context = context;
    }

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

    public void parseQuizData(String result) {

        dbThemenbereich.deleteAll(dbThemenbereich.class);
        dbFragen.deleteAll(dbFragen.class);
        dbAntworten.deleteAll(dbAntworten.class);

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONArray jsonArrayThemenbereiche = jsonObjectAll.getJSONArray("themes");


            for (int i = 0; i < jsonArrayThemenbereiche.length(); i++) {
                JSONObject jsonObject = jsonArrayThemenbereiche.getJSONObject(i);
                dbThemenbereich themenbereich = new dbThemenbereich();
                themenbereich.serverid = jsonObject.getInt("id");
                themenbereich.name = jsonObject.getString("title");
                themenbereich.zuletztAktualisiert = jsonObject.getString("dat");
                if (new dbThemenbereich().themenbereichVorhanden(themenbereich.serverid) == false) {
                    String kursID = jsonObject.getString("kursid");
                    if (new dbKurs().getKursWithKursid(kursID) != null) {
                        dbKurs kurs = new dbKurs().getKursWithKursid(kursID);
                        themenbereich.kurs = kurs;
                        themenbereich.save();
                    }

                }
            }

            JSONArray jsonArrayFragen = jsonObjectAll.getJSONArray("frag");
            for (int i = 0; i < jsonArrayFragen.length(); i++) {
                JSONObject jsonObject = jsonArrayFragen.getJSONObject(i);
                dbFragen fragen = new dbFragen();
                if (new dbThemenbereich().getThemenbereich(jsonObject.getInt("themen")) != null) {
                    fragen.frage = jsonObject.getString("frage");
                    fragen.schwirigkeit = jsonObject.getInt("schw");
                    fragen.serverid = jsonObject.getInt("id");
                    ;
                    if (new dbFragen().frageVorhanden(fragen.serverid) == false) {
                        dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(jsonObject.getInt("themen"));
                        fragen.themenbereich = themenbereich;
                        String kursID = jsonObject.getString("kursid");
                        if (new dbKurs().getKursWithKursid(kursID) != null) {
                            dbKurs kurs = new dbKurs().getKursWithKursid(kursID);
                            fragen.kurs = kurs;
                            fragen.save();
                            themenbereich.save();
                        }


                    }
                }
            }

            JSONArray jsonArrayAntworten = jsonObjectAll.getJSONArray("antworten");
            for (int i = 0; i < jsonArrayAntworten.length(); i++) {
                JSONObject jsonObject = jsonArrayAntworten.getJSONObject(i);
                if (new dbFragen().getFrage(jsonObject.getInt("fragid")) != null) {
                    dbFragen frage = new dbFragen().getFrage(jsonObject.getInt("fragid"));
                    int id = jsonObject.optInt("id");
                    if (new dbAntworten().antwortVorhanden(id) == false) {

                        JSONArray jsonArrayFalscheAntworten = jsonObject.getJSONArray("antworten");
                        int richtig = jsonObject.getInt("truth");
                        for (int k = 0; k < jsonArrayFalscheAntworten.length(); k++) {
                            dbAntworten antworten = new dbAntworten();
                            if (richtig == k+1){
                                antworten.richtig = true;
                            }else {
                                antworten.richtig = false;
                            }
                            antworten.antwort = jsonArrayFalscheAntworten.getString(k);
                            antworten.serverid = id;
                            antworten.langfassung = jsonObject.getString("description");

                            antworten.fragen = frage;

                            antworten.save();
                            frage.save();
                        }




                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(context, now.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
        ;
        updateUI.updateUI(zuletztAktualisiert);

    }

    public List<quizkurs> getQuizKurse() {
        List<dbKurs> kursList = new dbKurs().getActiveKurse(0);
        kursList.addAll(new dbKurs().getActiveKurse(1));

        List<dbThemenbereich> themenbereichList = new ArrayList<>();
        for (dbKurs kurs : kursList) {
            if (kurs.getThemenbereiche(kurs.getId()) != null) {
                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
            }
        }
        List<dbFragen> fragenList = new ArrayList<>();
        if (themenbereichList.size() > 0) {
            for (dbThemenbereich themenbereich : themenbereichList) {
                if (themenbereich.getFragen(themenbereich.getId()) != null) {
                    fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
                }
            }

        }
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
                        fragen += new dbThemenbereich().getFragen(themenbereich.getId()).size();
                    }
                    quizkurs.fragen = fragen;

                    quizkursList.add(quizkurs);
                }
            }


        }
        if (quizkursList.size() > 0) {
            return quizkursList;
        } else {
            return null;
        }

    }

    public List<dbThemenbereich> sortListASC(List<dbThemenbereich> list) {
        Collections.sort(list, new Comparator<dbThemenbereich>() {
            @Override
            public int compare(dbThemenbereich lhs, dbThemenbereich rhs) {
                return lhs.zuletztAktualisiert.compareTo(rhs.zuletztAktualisiert);
            }
        });
        return list;
    }

    public List<quizthemen> getThemenbereiche(String kursid) {
        List<dbKurs> kursList = new dbKurs().find(dbKurs.class, "name = ?", kursid);
        if (kursList.size() == 1) {
            dbKurs kurs = kursList.get(0);

            List<dbThemenbereich> themenbereichList = new ArrayList<>();
            if (kurs.getThemenbereiche(kurs.getId()) != null) {
                themenbereichList.addAll(kurs.getThemenbereiche(kurs.getId()));
            }

            List<dbFragen> fragenList = new ArrayList<>();
            if (themenbereichList.size() > 0) {
                for (dbThemenbereich themenbereich : themenbereichList) {
                    if (themenbereich.getFragen(themenbereich.getId()) != null) {
                        fragenList.addAll(themenbereich.getFragen(themenbereich.getId()));
                    }
                }
            }
            List<quizthemen> quizthemenList = new ArrayList<>();
            if (fragenList.size() > 0) {
                if (kurs.getThemenbereiche(kurs.getId()) != null) {
                    for (dbThemenbereich themenbereich : kurs.getThemenbereiche(kurs.getId())) {
                        quizthemen quizthemen = new quizthemen();
                        quizthemen.themenbereich = themenbereich.name;
                        quizthemen.id = themenbereich.getId();
                        quizthemen.datum = themenbereich.zuletztAktualisiert;
                        quizthemen.fragen = themenbereich.getFragen(themenbereich.getId()).size();
                        quizthemen.lehrer = kurs.lehrer.titel + " " + kurs.lehrer.name;
                        quizthemen.kursId = kurs.name;
                        quizthemenList.add(quizthemen);
                    }
                }
            }

            if (quizthemenList.size() > 0) {
                return quizthemenList;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


    public interface UpdateUI {
        public void updateUI(String datum);
    }

    public void setUpdateInterface(UpdateUI updateInterface) {
        this.updateUI = updateInterface;
    }

}
