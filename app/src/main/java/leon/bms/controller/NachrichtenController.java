package leon.bms.controller;

/**
 * Created by Leon E on 25.05.2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import leon.bms.database.dbKurs;
import leon.bms.database.dbUser;
import leon.bms.model.nachrichten;

;

/**
 * Created by Leon E on 25.05.2016.
 */
public class NachrichtenController {

    OnUpdateListener listener;

    Context mainContext;


    public NachrichtenController(Context context) {
        this.mainContext = context;
    }

    public void checkUpdate() {
        // WICHTIGE Url für die Anfrage an den Server
        String Url = Constants.NACHRICHTEN_URL;
        LogInController logInController = new LogInController(mainContext);
        // parsed Datum
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
        String datum = sdf2.format(calendar.getTime());
        String course_ids = "";
        List<dbKurs> alleKurse = new dbKurs().getAllActiveKurse();
        for (dbKurs k : alleKurse) {
            course_ids += k.serverid + ",";
        }


        String params ="username="+new LogInController(mainContext).getUsername()+"&password="+new LogInController(mainContext).getPass()+"&last_refresh=" + datum + "&course_ids=" + course_ids; //TODO richtige Paramerter hiinzufügen
        Log.d("VERTREUNGSPLAN", params);

        atOnline atOnline2 = new atOnline(Url, params, mainContext);
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {

            @Override
            public void onSuccesss(String result2) {
                Log.d("checkUpdate", result2);
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

        try {
            List<nachrichten> nachrichtenList = new ArrayList<>();
            JSONArray jsonResult = new JSONArray(result2);
            for (int i = 0; i < jsonResult.length(); i++) {
                JSONObject jsonObject = jsonResult.getJSONObject(i);
                nachrichten nachrichten = new nachrichten();
                nachrichten.serverid = jsonObject.getInt("id");
                nachrichten.titel = jsonObject.getString("title");
                nachrichten.nachricht = jsonObject.getString("message");
                nachrichten.hinzugefuegtAm = jsonObject.getString("added_date");
                nachrichten.geandertAm = jsonObject.getString("last_change");
                nachrichten.dateString = setUpDate(jsonObject.getString("last_change"));
                nachrichten.geloescht = jsonObject.getInt("deleted");
                nachrichtenList.add(nachrichten);
            }
            if (nachrichtenList != null && nachrichtenList.size() > 0) {
                listener.onSuccesss(nachrichtenList);
            }

        } catch (JSONException e) {

        }


    }

    private String setUpDate(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = calendar.getTime();
        //YEAR_IN_MILLIS wenn das Datum mehr als ein Jahr zurück liegt wird das ganze Datum angezeigt
        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(mainContext, date1.getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0));
        return zuletztAktualisiert;
    }

    /**
     * @param websiteArtikelList Liste die nach dem Datum sortiert werden soll
     * @return gibt die sortierte Liste zurück
     * @sortListDate sortiert die Liste nach ihrem Datum absteigen
     */
    private List<nachrichten> sortListDate(List<nachrichten> websiteArtikelList) {
        Collections.sort(websiteArtikelList, new Comparator<nachrichten>() {
            public int compare(nachrichten nachrichten1, nachrichten nachrichten2) {
                return stringToCalander(nachrichten1.geandertAm).getTime().compareTo(stringToCalander(nachrichten2.geandertAm).getTime());
            }
        });
        Collections.reverse(websiteArtikelList);
        return websiteArtikelList;
    }

    /**
     * @param date date in String
     * @return gibt Date in Calendar zurück
     * @stringToCalander parsed ein Datum vom Datentyp String zum Datentyp Calendar, dies
     * ist wichtig für das sortieren nach dem Datum
     */
    public Calendar stringToCalander(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }


    public Calendar getCalendarFromString(String calString) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(calString));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface OnUpdateListener {
        public void onSuccesss(List<nachrichten> nachrichtenList);

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
