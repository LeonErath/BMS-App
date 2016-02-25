package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Leon E on 21.12.2015.
 */
public class StundenplanController {

    String registrationUrl = "http://app.marienschule.de/files/scripts/getStundenplan.php";
    Context mainContext;

    public StundenplanController(Context context) {
        Log.d("CONSTRUCTOR", " StundenlanController erstellt");
        mainContext=context;
    }

    public String downloadStundenplan() {
        String result="";
        Log.d("DOWNLOAD", " downloadStundenplan()");
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", "erath")
                .appendQueryParameter("pw", "Ardaturan99");
        String params = builder.build().getEncodedQuery();
        atOnline atOnline = new atOnline(registrationUrl, params, mainContext);
        try {
            result= atOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void erstelleStundenplan(String result){
        try {

            JSONObject jsonObjectAll = new JSONObject(result);
            String aktualisierungsDatum = jsonObjectAll.optString("zuletzt Aktualisiert");
            //Array mit allen Stundenplan Informationen
            JSONArray jsonArrayStundenplan = jsonObjectAll.getJSONArray("stundenPlan");
            //Array sortieren

            for (int i=0;i<jsonArrayStundenplan.length();i++){
                JSONObject jsonObject = jsonArrayStundenplan.getJSONObject(i);
                dbSchulstunde schulstunde = new dbSchulstunde();
                String LKZ = jsonObject.optString("LKZ");
                String KURSID = jsonObject.optString("KURSID");
                Integer WOCHENTAG = jsonObject.optInt("WOCHENTAG");
                Integer STUNDE = jsonObject.optInt("STUNDE");
                Integer BN = jsonObject.optInt("BN");
                String RAUM = jsonObject.optString("RAUM");

                dbLehrer lehrer1 = new dbLehrer();
                if (!LKZ.equals("")){
                    lehrer1 = lehrer1.getLehrer("kuerzel",LKZ);
                }else {

                }
                schulstunde.lehrer = lehrer1;
                //List<dbRaum> raum = dbRaum.find(dbRaum.class, "nummer = ?", RAUM);
                //dbRaum raum1 = raum.get(0);


                schulstunde.wochentag = WOCHENTAG;
                schulstunde.beginnzeit = STUNDE;
                schulstunde.kursID = KURSID;

                schulstunde.raum = RAUM;
                //schulstunde.raum = raum1;
                schulstunde.save();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // nimmt das getAllData.php und guckt welche Daten aktuallisiert werden müssen
    // in folgeneder Reihenfolge:
    // 1 Lehrerliste
    // 2 PK und AGs
    // 3 Stundenplan
    // 4 Kürzel
    // Die einzelnen Daten sollten immer in ihren eigen Catch-Block stehen , sodass bei fehlern andere Daten noch
    // altualisiert werden können
    public void checkUpdate(){
        String Url = "http://app.marienschule.de/files/scripts/NEW/getAllDataN.php";
        String result="";
        LogInController logInController = new LogInController(mainContext);



        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", logInController.getUsername())
                .appendQueryParameter("pw", logInController.getPass())
                .appendQueryParameter("date","");
        String params = builder.build().getEncodedQuery();

        atOnline atOnline = new atOnline(Url, params, mainContext);
        try {
            result= atOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("RESULT", result);




        //Hier die Lehrerliste
        if (new dbLehrer().countLehrer()==0) {
            try {
                JSONObject jsonObjectAll = new JSONObject(result);
                JSONObject leherliste = jsonObjectAll.getJSONObject("lehrerliste");
                erstelleLehrerListe(String.valueOf(leherliste));
                Log.d("leherliste", String.valueOf(leherliste));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Hier die AGs und Projektkurse
        if (new dbKurs().countKurse()==0) {
            try {
                JSONObject jsonObjectAll = new JSONObject(result);
                JSONObject pkuags = jsonObjectAll.getJSONObject("pkuags");

                KurseController kurseController = new KurseController(mainContext);
                Log.d("pkuags", String.valueOf(pkuags));
                kurseController.erstelltPKundAGs(String.valueOf(pkuags));
                Log.d("COUNT", new dbKurs().countKurse() + " ");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        //Hier der Stundenplan und die Kurse
        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONObject stundenplan = jsonObjectAll.getJSONObject("timetable");

            erstelleStundenplan(String.valueOf(stundenplan));
            Log.d("STUNDENPLAN", "Stundenplan wurde erstellt.");

            KurseController kurseController = new KurseController(mainContext);
            kurseController.erstelltKurse();
            Log.d("KURSE", "Kurse wurden erstellt.");
            kurseController.connectKursStunden();
            Log.d("stundenplan", String.valueOf(stundenplan));



        } catch (JSONException e) {
            e.printStackTrace();
        }
        }


        //Hier die Fachkuerzel
        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONObject fachkz = jsonObjectAll.getJSONObject("fachkuerzel");

            KurseController kurseController = new KurseController(mainContext);
            kurseController.connectKuerzel(String.valueOf(fachkz));
            Log.d("fachkz", String.valueOf(fachkz));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void erstelleLehrerListe(String result){
        try {
            JSONObject jsonObjectLehrerliste = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            JSONArray jsonArrayLehrer = jsonObjectLehrerliste.getJSONArray("lehrerliste");
            for (int i=0;i<jsonArrayLehrer.length();i++){
                dbLehrer lehrer = new dbLehrer();
                JSONObject lehrerDaten = jsonArrayLehrer.getJSONObject(i);
                lehrer.name = lehrerDaten.optString("NAME");
                lehrer.email = lehrerDaten.optString("MAIL");
                lehrer.titel = lehrerDaten.optString("TITEL");
                lehrer.kuerzel = lehrerDaten.optString("KUERZEL");
                lehrer.Vorname = lehrerDaten.optString("VORNAME");
                JSONArray lehrerFaecher =lehrerDaten.getJSONArray("faecher");
                String faecher = "";
                for (int k=0;k<lehrerFaecher.length();k++){
                    faecher+= lehrerFaecher.getString(k)+",";
                }
                lehrer.faecher = faecher;
                lehrer.save();



            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }




}
