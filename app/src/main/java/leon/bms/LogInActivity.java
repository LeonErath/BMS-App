package leon.bms;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteDB();
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
            }
        });
    }
    public void deleteDB(){
        Log.d(LogInActivity.class.getSimpleName(),"Datenbank wurde zurückgesetzt");
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
    }

    }

