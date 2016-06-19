package leon.bms.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.realm.dbUser;


/**
 * Created by Leon E on 22.12.2015.
 */

/**
 * @LoginController ist dafür zuständig um den Login zu vollziehen sowie nach erfolgreichem Login den User
 * zu aktualiseren bzw zu speichern. Der LoginController enthält alle notwendigen Methoden für den
 * Login.
 */
public class LogInController {
    SharedPreferences prefs;
    Context mainContext;
    // URL für den LOGIN
    String registrationUrl = Constants.LOGIN_URL;


    public LogInController(Context context) {
        mainContext = context;
    }

    /**
     * @param user  ist der Username der , der User in einem textfeld eingibt
     * @param pass  ist das Passwort des Users
     * @param stufe ist die Stufe des Users
     * @return gibt das Resultat der Anfrage zurück. Wenn die Anfrage nicht erfolgreich war
     * kriegt man "Error" zurück.
     * @login stellt ein Login Anfrage an den Server durch die atOnline Klasse
     */
    public String login(String user, String pass, String stufe) {
        String result = "";
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", user)
                .appendQueryParameter("password", pass);
        String params = builder.build().getEncodedQuery();
        Log.d("A",params);

        atOnline atOnline = new atOnline(registrationUrl, params, mainContext);
        try {
            result = atOnline.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return result;


    }

    /**
     * @param result beinhaltet die JSON Daten des User
     * @return gibt den erstellten User zurück
     * @createUser erstellt den user anhand der JSON Daten die man vom Server bekommt
     * Speichert den username,vorname,nachname,stufe,zuletztaktualisiert in die Datenbank.
     */
    public dbUser createUser(String result, final String User) {
        Log.d("createUser",result);
        try {
            final JSONObject jsonObject = new JSONObject(result);
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(mainContext).build();
            Realm.setDefaultConfiguration(realmConfig);
            // Get a Realm instance for this thread
            Realm realm = Realm.getDefaultInstance();

            final leon.bms.realm.dbUser user = new dbUser();
            user.setFirst_name(jsonObject.optString("first_name"));
            user.setGrade_string(jsonObject.optString("grade_string"));
            user.setLast_name(jsonObject.optString("last_name"));
            user.setSession_hash(jsonObject.optString("session_hash"));
            user.setLoggedIn(true);
            user.setBenutzername(User);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(user);
                }
            });
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            //error message beim parsen
            Toast.makeText(mainContext, "Irgendwas ist schief gelaufen...", Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    /**
     * @param stringDate ist das Datum als String
     * @return gibt den String als Datum zurück
     * @fromStringtoDate Methode zum parsen vom String zu einem Date
     */
    public Date fromStringtoDate(String stringDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss''");
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * @param pass ist das Passwort als String
     * @savePass wird benötigt um das Passswort zuspeichern um es nicht als Klartext im der Datenbank zu speichern, wird es
     * hier über die savepreferences gespeichert
     */
    public void savePass(String pass) {
        prefs = mainContext.getSharedPreferences("leon.bms", Context.MODE_PRIVATE);
        prefs.edit().clear();
        prefs.edit().putString("PASS", pass).apply();
    }

    /**
     * @return lädt das passwort
     */
    public String getPass() {
        prefs = mainContext.getSharedPreferences("leon.bms", Context.MODE_PRIVATE);
        return prefs.getString("PASS", "-");
    }

}
