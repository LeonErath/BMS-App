package leon.bms.activites.login.normal;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import leon.bms.R;
import leon.bms.database.dbAntworten;
import leon.bms.database.dbAufgabe;
import leon.bms.database.dbFragen;
import leon.bms.database.dbKurs;
import leon.bms.database.dbKursTagConnect;
import leon.bms.database.dbLehrer;
import leon.bms.database.dbMediaFile;
import leon.bms.database.dbSchulstunde;
import leon.bms.database.dbThemenbereich;
import leon.bms.database.dbUser;
import leon.bms.database.dbWebsiteTag;

/**
 * @LogInActivity zeigt das Fragment_Login und wird immer beim Start geladen.
 * Hier besteht zu Test Zwecken die Methode zum löschen der Datenbank
 */
public class LogInActivity extends AppCompatActivity {

    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    ;
    }

    /**
     * @deleteDB löscht alle Daten der Datenbank
     */
    public void deleteDB() {
        Log.d(LogInActivity.class.getSimpleName(), "Datenbank wurde zurückgesetzt");
        dbUser.deleteAll(dbUser.class);
        dbAufgabe.deleteAll(dbAufgabe.class);
        dbKurs.deleteAll(dbKurs.class);
        dbSchulstunde.deleteAll(dbSchulstunde.class);
        dbMediaFile.deleteAll(dbMediaFile.class);
        dbLehrer.deleteAll(dbLehrer.class);
        dbWebsiteTag.deleteAll(dbWebsiteTag.class);
        dbFragen.deleteAll(dbFragen.class);
        dbThemenbereich.deleteAll(dbThemenbereich.class);
        dbAntworten.deleteAll(dbAntworten.class);
        dbKursTagConnect.deleteAll(dbKursTagConnect.class);
    }


}

