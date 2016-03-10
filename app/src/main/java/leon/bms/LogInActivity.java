package leon.bms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
        findViewById(R.id.fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
            }
        });
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

