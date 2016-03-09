package leon.bms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Leon E on 22.12.2015.
 */
public class LogInController {
    SharedPreferences prefs;
    Context mainContext;
    String registrationUrl = "http://app.marienschule.de/files/scripts/login.php";


    public LogInController(Context context) {
        mainContext = context;
    }

    public String login(String user, String pass, String stufe) {
        String result = "";
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", user)
                .appendQueryParameter("pw", pass)
                .appendQueryParameter("stufe", stufe);
        String params = builder.build().getEncodedQuery();

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


    public dbUser createUser(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            // json to String
            String Benutzername = jsonObject.optString("login");
            String Firstname = jsonObject.optString("firstname");
            String Stufe = jsonObject.optString("stufe");
            String Lastname = jsonObject.optString("lastname");
            String lastAppLogin = jsonObject.optString("last_app_login");
            String lastLogin = jsonObject.optString("last_login");


            dbUser.deleteAll(dbUser.class);

            dbUser user = new dbUser();
            user.vorname = Firstname;
            user.nachname = Lastname;
            user.loggedIn = true;
            user.stufe = Stufe;
            user.lastAppOpen = lastAppLogin;
            user.lastServerConnection = lastLogin;
            user.benutzername = Benutzername;
            user.save();


            Toast.makeText(mainContext, "Herzlich Willkommen " + Firstname + " " + Lastname + " ", Toast.LENGTH_SHORT).show();
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "Irgendwas ist schief gelaufen...", Toast.LENGTH_SHORT).show();

        }
        return null;
    }

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

    public dbUser getActiveUser() {
        List<dbUser> userList = dbUser.find(dbUser.class, "logged_In = ?", "1");
        if (userList.size() == 1) {
            dbUser user = userList.get(0);
            return user;
        } else {
            return null;
        }
    }

    public String getUsername() {
        dbUser user = getActiveUser();
        if (user == null) {
            return null;
        } else {
            return user.benutzername;
        }
    }

    public void savePass(String pass) {
        prefs = mainContext.getSharedPreferences("leon.bms", Context.MODE_PRIVATE);
        prefs.edit().clear();
        prefs.edit().putString("PASS", pass).apply();
    }

    public String getPass() {
        prefs = mainContext.getSharedPreferences("leon.bms", Context.MODE_PRIVATE);
        return prefs.getString("PASS", "-");
    }

}
