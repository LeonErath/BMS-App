package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void getQuizData(){
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
    public void parseQuizData(String result){

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONArray jsonArrayThemenbereiche = jsonObjectAll.getJSONArray("themes");


            for (int i=0;i<jsonArrayThemenbereiche.length();i++){
                JSONObject jsonObject = jsonArrayThemenbereiche.getJSONObject(i);
                dbThemenbereich themenbereich = new dbThemenbereich();
                themenbereich.serverid = jsonObject.getInt("id");
                themenbereich.name = jsonObject.getString("title");
                themenbereich.zuletztAktualisiert = jsonObject.getString("dat");
                if (new dbThemenbereich().themenbereichVorhanden(themenbereich.serverid)==false){
                themenbereich.save();}
            }

            JSONArray jsonArrayFragen = jsonObjectAll.getJSONArray("frag");
            for (int i=0;i<jsonArrayFragen.length();i++){
                JSONObject jsonObject = jsonArrayFragen.getJSONObject(i);
                dbFragen fragen = new dbFragen();
                if (new dbThemenbereich().getThemenbereich(jsonObject.getInt("themen"))!=null){
                    fragen.frage = jsonObject.getString("frage");
                    fragen.schwirigkeit = jsonObject.getInt("schw");
                    fragen.serverid = 1;
                    if (new dbFragen().frageVorhanden(fragen.serverid)==false){
                        dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(jsonObject.getInt("themen"));
                        themenbereich.fragen = fragen;
                        themenbereich.save();

                        fragen.save();
                    }
                }
            }

            JSONArray jsonArrayAntworten = jsonObjectAll.getJSONArray("antworten");
            for (int i=0;i<jsonArrayAntworten.length();i++){
                JSONObject jsonObject = jsonArrayAntworten.getJSONObject(i);
                if (new dbFragen().getFrage(jsonObject.getInt("fragid"))!=null) {
                    dbFragen frage = new dbFragen().getFrage(jsonObject.getInt("fragid"));
                    int id = jsonObject.optInt("id");
                    if (new dbAntworten().antwortVorhanden(id)==false){

                    JSONArray jsonArrayFalscheAntworten = jsonObject.getJSONArray("antworten");
                    for (int k = 0; k < jsonArrayFalscheAntworten.length(); k++) {
                        dbAntworten antworten = new dbAntworten();
                        antworten.richtig = false;
                        antworten.antwort = jsonArrayFalscheAntworten.getString(k);
                        antworten.serverid = id;


                            frage.antworten = antworten;
                            frage.save();

                            antworten.save();

                    }
                    dbAntworten antworten = new dbAntworten();
                    antworten.richtig = true;
                    antworten.antwort = jsonObject.getString("truth");
                    antworten.serverid = id;

                        frage.antworten = antworten;
                        frage.save();

                        antworten.save();
                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(context, now.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));;
        updateUI.updateUI(zuletztAktualisiert);

    }
    public interface UpdateUI {
        public void updateUI(String datum);
    }
    public void setUpdateInterface(UpdateUI updateInterface){
        this.updateUI = updateInterface;
    }

}
