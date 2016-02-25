package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

/**
 * Created by Leon E on 21.12.2015.
 */
public class KurseController {
    Context mainContext;

    public KurseController(Context context) {
        mainContext = context;
    }
    public void erstelltKurse() {

        List<dbSchulstunde> schulstundeList = dbSchulstunde.listAll(dbSchulstunde.class);
        List<String> kursIDList = new ArrayList<>();

        for (int i = 0; i < schulstundeList.size(); i++) {
            kursIDList.add(i, schulstundeList.get(i).kursID);
        }


        Set<String> sortetdSet = new TreeSet<String>(new ListComparator());
        for (String value : kursIDList) {
            if (!sortetdSet.add(value)) {
            //do nothing
            }
        }

            Iterator<String> iterator = sortetdSet.iterator();
            kursIDList.clear();

            while (iterator.hasNext()) {
                kursIDList.add(iterator.next());
            }
            Log.d("COUNT",kursIDList.size()+"");
            for (int i = 0; i < kursIDList.size(); i++) {
                dbKurs kurs = new dbKurs();
                kurs.name = kursIDList.get(i);
                if (kurs.kursVorhanden(kurs)!= true){

                    // LK 0 GK 1 PK 2 AG 3
                    if (kursIDList.get(i).charAt(3)=='L'){
                        kurs.kursart =0;
                    }else {
                        kurs.kursart=1;
                    }
                    kurs.save();
                }
            }
    }

    public void connectKursStunden(){
        List<dbSchulstunde> schulstundeList = dbSchulstunde.listAll(dbSchulstunde.class);
        List<dbKurs> kurseList = dbKurs.listAll(dbKurs.class);


        for (int i=0;i<kurseList.size();i++){
            dbKurs kurs1 = kurseList.get(i);
            for (int k=0;k<schulstundeList.size();k++){
                dbSchulstunde schulstunde =schulstundeList.get(k);
               if (kurs1.name.equals(schulstunde.kursID)){
                //kurs1.stunde =schulstunde;
                kurs1.lehrer = schulstunde.lehrer;
                kurs1.save();
                 schulstunde.kurs = kurs1;
                 schulstunde.save();
             }
            }
        }
    }

    public void downloadKuerzel(){
        String result="";
        String url = "http://app.marienschule.de/files/scripts/getFachKuerzel.php";
        LogInController logInController = new LogInController(mainContext);
        String user = logInController.getUsername();
        String pass = logInController.getPass();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", user)
                .appendQueryParameter("pw",pass);
        String params = builder.build().getEncodedQuery();
        atOnline atOnline = new atOnline(url,params,mainContext);
        try {
            result= atOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result=="Error"){
            Toast.makeText(mainContext, "Download fehlgeschlagen..", Toast.LENGTH_SHORT).show();
        }else {
           connectKuerzel(result);
        }
    }

    public void connectKuerzel(String result){
        List<dbKurs> kurseList = dbKurs.listAll(dbKurs.class);

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            //Array mit allen Fach Kürzeln
            JSONArray jsonArrayKuerzel = jsonObjectAll.getJSONArray("facherkuerzel");

            for (int i=0;i<jsonArrayKuerzel.length();i++){
                JSONObject jsonObject = jsonArrayKuerzel.getJSONObject(i);
                String mShort = jsonObject.optString("SHORT");
                for (int k=0;k<kurseList.size();k++){
                    dbKurs kurs = kurseList.get(k);
                    if (mShort.equals(kurs.name)){
                        kurs.fach= jsonObject.optString("FULL");
                        kurs.save();
                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void erstelltPKundAGs(String result){
        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            JSONArray jsonArray = jsonObjectAll.getJSONArray("pkuags");
            Log.d("COUNT",jsonArray.length()+"");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectEintrag = jsonArray.getJSONObject(i);
                String name = jsonObjectEintrag.optString("BESCHREIBUNG");
                String kuerzel = jsonObjectEintrag.optString("KUERZEL");
                String lehrer = jsonObjectEintrag.optString("LEHRERKUERZEL");
                JSONArray Typ = jsonObjectEintrag.getJSONArray("TYP");

                for (int k=0;k<Typ.length();k++){
                    dbKurs kurs = new dbKurs();
                    kurs.name = kuerzel;
                        //Verbindung mit Lehrer schaffen
                        dbLehrer lehrer1 = new dbLehrer();
                        if (!lehrer.equals("")){
                        kurs.lehrer = lehrer1.sucheLehrer(lehrer);
                        }
                        // LK 0 GK 1 AG 2 PK 3
                    kurs.kursart =2;
                        if (Typ.getString(k).substring(0,2).equals("PK")) {
                        kurs.kursart = 3;
                        }
                        kurs.fach = name;
                    if (new dbKurs().kursVorhanden(kurs)== false){
                        kurs.save();
                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public List<dbKurs> gibtKurseZurück(){
        List<dbKurs> kursList = dbKurs.listAll(dbKurs.class);
        return kursList;
    }

}
class ListComparator implements Comparator<String>{

    @Override
    public int compare(String lhs, String rhs) {
        return lhs.compareTo(rhs);
    }
}

