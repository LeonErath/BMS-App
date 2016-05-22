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

import leon.bms.Constants;
import leon.bms.atOnline;
import leon.bms.database.dbUser;

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
    public dbUser createUser(String result) {
        Log.d("createUser",result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            // json to String
            String Firstname = jsonObject.optString("first_name");
            String Stufe = jsonObject.optString("grade_string");
            String Lastname = jsonObject.optString("last_name");


            dbUser.deleteAll(dbUser.class);

            dbUser user = new dbUser();
            user.vorname = Firstname;
            user.nachname = Lastname;
            user.loggedIn = true;
            user.stufe = Stufe;
            user.benutzername = Firstname+" "+Lastname;
            user.save();


            Toast.makeText(mainContext, "Herzlich Willkommen " + Firstname + " " + Lastname + " ", Toast.LENGTH_SHORT).show();
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
     * @return wenn kein user eingeloggt ist dann null sonst den aktuelle User
     * @getActiveUser gibt den aktuellenUser zurück der gerade eingeloggt ist.
     */
    public dbUser getActiveUser() {
        List<dbUser> userList = dbUser.find(dbUser.class, "logged_In = ?", "1");
        if (userList.size() == 1) {
            dbUser user = userList.get(0);
            return user;
        } else {
            return null;
        }
    }

    /**
     * @return gibt den aktuelle Usernamen des Users zurück
     */
    public String getUsername() {
        dbUser user = getActiveUser();
        if (user == null) {
            return null;
        } else {
            return user.benutzername;
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
