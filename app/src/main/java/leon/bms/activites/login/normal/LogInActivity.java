package leon.bms.activites.login.normal;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import leon.bms.R;


/**
 * @LogInActivity zeigt das Fragment_Login und wird immer beim Start geladen.
 * Hier besteht zu Test Zwecken die Methode zum löschen der Datenbank
 */
public class LogInActivity extends AppCompatActivity {

    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteDB();
        setContentView(R.layout.activity_log_in);

    }

    /**
     * @deleteDB löscht alle Daten der Datenbank
     */
    public void deleteDB() {
        Log.d(LogInActivity.class.getSimpleName(), "Datenbank wurde zurückgesetzt");

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }


}

