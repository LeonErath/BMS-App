package leon.bms.controller;

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

import leon.bms.atOnline;
import leon.bms.database.dbKurs;
import leon.bms.database.dbKursTagConnect;
import leon.bms.database.dbLehrer;
import leon.bms.database.dbSchulstunde;
import leon.bms.database.dbWebsiteTag;

/**
 * Created by Leon E on 21.12.2015.
 */

/**
 * @KursController ist die Klasse die alles bearbeitet was mit den Kursen zutun hat. Es erstellt alle Kurse sowie
 * Porjektkurs und AGs und lädt die richtige Bezeichnung nach.
 */
public class KurseController {
    Context mainContext;

    public KurseController(Context context) {
        mainContext = context;
    }

    /**
     * @erstelltKurse Methode um alle Kurse aus den Schulstunden zu erstellen und in der Datenbank entsprechend
     * zu speichern
     */
    public void erstelltKurse() {

        //lädt alle Schulstunden aus der Datenbank
        List<dbSchulstunde> schulstundeList = dbSchulstunde.listAll(dbSchulstunde.class);
        List<String> kursIDList = new ArrayList<>();

        for (int i = 0; i < schulstundeList.size(); i++) {
            kursIDList.add(i, schulstundeList.get(i).kursID);
        }

        //löscht alle doppelt vorkommenden kursIDs raus sodass eine Liste mit nur noch den Kursids vorhanden ist
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
        // speicher die Kursids als neue Kurse mit der entsprechenden kursart
        Log.d("COUNT", kursIDList.size() + "");
        for (int i = 0; i < kursIDList.size(); i++) {
            dbKurs kurs = new dbKurs();
            kurs.name = kursIDList.get(i);
            if (kurs.kursVorhanden(kurs) != true) {

                // LK 0 GK 1 PK 2 AG 3
                if (kursIDList.get(i).charAt(3) == 'L') {
                    kurs.kursart.gloablId = 0;
                } else {
                    kurs.kursart.gloablId = 1;
                }
                kurs.save();
            }
        }
    }

    /**
     * @connectKursStunden verbindet die Kurse mit den Schulstunden und lädt Daten des kurses nach.
     * Auch die Verbindung zu den Lehrer wird geschaffen.
     */
    public void connectKursStunden() {
        List<dbSchulstunde> schulstundeList = dbSchulstunde.listAll(dbSchulstunde.class);
        List<dbKurs> kurseList = dbKurs.listAll(dbKurs.class);


        for (int i = 0; i < kurseList.size(); i++) {
            dbKurs kurs1 = kurseList.get(i);
            for (int k = 0; k < schulstundeList.size(); k++) {
                dbSchulstunde schulstunde = schulstundeList.get(k);
                if (kurs1.name.equals(schulstunde.kursID)) {
                    // creating relationships
                    kurs1.lehrer = schulstunde.lehrer;
                    kurs1.hinzugefuegtAm = schulstunde.zuletztAktualisiert;
                    kurs1.save();
                    schulstunde.kurs = kurs1;
                    schulstunde.save();
                }
            }
        }
    }

    // überflüssig geworden erstmal
    public void downloadKuerzel() {
        String result = "";
        String url = "http://app.marienschule.de/files/scripts/getFachKuerzel.php";
        LogInController logInController = new LogInController(mainContext);
        String user = logInController.getUsername();
        String pass = logInController.getPass();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", user)
                .appendQueryParameter("pw", pass);
        String params = builder.build().getEncodedQuery();
        atOnline atOnline = new atOnline(url, params, mainContext);
        try {
            result = atOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result == "Error") {
            Toast.makeText(mainContext, "Download fehlgeschlagen..", Toast.LENGTH_SHORT).show();
        } else {
            connectKuerzel(result);
        }
    }

    /**
     * @param result beinhaltet zu jedem Kurs eine Kurz und langform
     * @connectKuerzel lädt mit den results die Langform der Kurse nach und speichert diese in den Kursen
     * Beispiel M L1 : Mathematik Leistungskurs 1
     */
    public void connectKuerzel(String result) {
        List<dbKurs> kurseList = dbKurs.listAll(dbKurs.class);

        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            //JSONObject jsonObjectDatum = jsonObjectAll.getJSONObject("zuletzt Aktualisiert");
            //Array mit allen Fach Kürzeln
            JSONArray jsonArrayKuerzel = jsonObjectAll.getJSONArray("facherkuerzel");

            for (int i = 0; i < jsonArrayKuerzel.length(); i++) {
                JSONObject jsonObject = jsonArrayKuerzel.getJSONObject(i);
                String mShort = jsonObject.optString("SHORT");
                for (int k = 0; k < kurseList.size(); k++) {
                    dbKurs kurs = kurseList.get(k);
                    //kurs wird anhand der Kursform rausgesucht und die Langform gespeichert
                    if (mShort.equals(kurs.name)) {
                        kurs.fach.name = jsonObject.optString("FULL");
                        kurs.save();
                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param result beinhaltet die JSON Daten der PK und AGs
     * @erstelltPKundAGs erstellt die PK und AGs. Hierbei muss besodners bei den kursarten aufgepasst
     * werden sowie das doppelt vorkommen da manche PK und AG die gleiche KursID besitzen.
     */
    public void erstelltPKundAGs(String result) {
        try {
            // parsed die JSON Daten
            JSONObject jsonObjectAll = new JSONObject(result);
            String datum = jsonObjectAll.getString("zuletzt Aktualisiert");
            JSONArray jsonArray = jsonObjectAll.getJSONArray("pkuags");
            Log.d("COUNT", jsonArray.length() + "");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectEintrag = jsonArray.getJSONObject(i);
                String name = jsonObjectEintrag.optString("BESCHREIBUNG");
                String kuerzel = jsonObjectEintrag.optString("KUERZEL");
                String lehrer = jsonObjectEintrag.optString("LEHRERKUERZEL");


                JSONArray Typ = jsonObjectEintrag.getJSONArray("TYP");

                for (int k = 0; k < Typ.length(); k++) {
                    //erstellt die Kurse
                    dbKurs kurs = new dbKurs();
                    kurs.name = kuerzel;
                    //Verbindung mit Lehrer schaffen
                    dbLehrer lehrer1 = new dbLehrer();
                    if (!lehrer.equals("")) {
                        kurs.lehrer = lehrer1.sucheLehrer(lehrer);
                    }
                    //speichert die richtige Kursart
                    // LK 0 GK 1 AG 2 PK 3
                    kurs.kursart.gloablId = 2;
                    if (Typ.getString(k).substring(0, 2).equals("PK")) {
                        kurs.kursart.gloablId = 3;
                    }
                    kurs.fach.name = name;
                    //überprüft ob Kurs vorhanden ist
                    if (new dbKurs().kursVorhanden(kurs) == false) {
                        JSONArray Tags = jsonObjectEintrag.getJSONArray("TAGS");
                        kurs.hinzugefuegtAm = datum;
                        kurs.save();
                        //speichert die Website TAGs der Kurse
                        for (int l = 0; l < Tags.length(); l++) {
                            String tag = Tags.getString(l);
                            tag = tag.toUpperCase();
                            dbKursTagConnect connect = new dbKursTagConnect();
                            if (new dbWebsiteTag().tagVorhanden(tag) == true) {
                                dbWebsiteTag websiteTag = new dbWebsiteTag().getWebsiteTag(tag);
                                websiteTag.kurs = kurs;
                                //websiteTag.hinzugefuegtAm = datum;
                                connect.kurs = kurs;
                                connect.tag = websiteTag;
                                websiteTag.save();
                                connect.save();
                            } else {
                                dbWebsiteTag websiteTag = new dbWebsiteTag();
                                websiteTag.websitetag = tag;
                                websiteTag.kurs = kurs;
                                connect.kurs = kurs;
                                connect.tag = websiteTag;
                                //websiteTag.hinzugefuegtAm = datum;
                                websiteTag.save();
                                connect.save();
                            }
                        }


                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public List<dbKurs> gibtKurseZurück() {
        List<dbKurs> kursList = dbKurs.listAll(dbKurs.class);
        return kursList;
    }

}

class ListComparator implements Comparator<String> {

    @Override
    public int compare(String lhs, String rhs) {
        return lhs.compareTo(rhs);
    }
}

