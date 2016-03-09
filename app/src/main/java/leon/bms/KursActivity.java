package leon.bms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class KursActivity extends AppCompatActivity implements View.OnTouchListener {
    TextView textViewLehrer, textViewEmail, textViewAufgabe, textViewDate, textViewKurs;
    RecyclerView recyclerView;
    dbKurs kurs;
    private static String TAG = KursActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);

        textViewKurs = (TextView) findViewById(R.id.textViewToolbar);
        textViewLehrer = (TextView) findViewById(R.id.textViewLehrer);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewAufgabe = (TextView) findViewById(R.id.textViewAufgaben);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        textViewEmail.setOnTouchListener(this);
        textViewAufgabe.setOnTouchListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Long id = intent.getLongExtra("id", 0);
            if (id != 0) {
                if (new dbKurs().getKursWithID(id) != null) {
                    kurs = new dbKurs().getKursWithID(id);
                    textViewKurs.setText(kurs.fach);
                    textViewLehrer.setText(kurs.lehrer.titel+" "+kurs.lehrer.name);
                    if (kurs.hinzugefuegtAm != null) {
                        textViewDate.setText("zuletzt geändert am: "+kurs.hinzugefuegtAm);
                    }else {
                        textViewDate.setText("zuletzt geändert am: kein Datum verfügbar.");
                    }

                }else {
                    Toast.makeText(KursActivity.this, "Kurs nicht vorhanden. ", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(KursActivity.this, "Kein Kurs wurde übergeben. ", Toast.LENGTH_SHORT).show();
            }
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //wenn home geclickt wird soll er die Activity schließen
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.textViewEmail:
                Log.d(TAG, "email trigger");

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",kurs.lehrer.email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hier der Betreff");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Sehr geehrte/r "+kurs.lehrer.titel+" "+kurs.lehrer.name+",\n\n\n\nmit freundlichen Grüßen,\n"+new dbUser().getUser().vorname+" "+new dbUser().getUser().nachname);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                break;
            case R.id.textViewAufgaben:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                finish();
                break;
            default:break;
        }
        return true;
    }
}


