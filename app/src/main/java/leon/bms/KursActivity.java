package leon.bms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @KursActivity zeigt alle Informationen zu einem bestimmten Kurs an. Es wird der Lehrer sowie eine
 * Möglichkeit diesen anzumailen angeboten. Außerdem werden noch die Stunden des Kurses in einem RecyclcerView
 * angezeigt.
 */
public class KursActivity extends AppCompatActivity implements View.OnTouchListener, KursAdapter.ViewHolder.ClickListener {
    // views
    TextView textViewLehrer, textViewEmail, textViewAufgabe, textViewDate, textViewKurs;
    RecyclerView recyclerView;
    dbKurs kurs;
    private static String TAG = KursActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurs);

        //sezUP Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);

        //initial view
        textViewKurs = (TextView) findViewById(R.id.textViewToolbar);
        textViewLehrer = (TextView) findViewById(R.id.textViewLehrer);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewAufgabe = (TextView) findViewById(R.id.textViewAufgaben);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        textViewEmail.setOnTouchListener(this);
        textViewAufgabe.setOnTouchListener(this);

        // get the Data fromt the other Activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Long id = intent.getLongExtra("id", 0);
            if (id != 0) {
                if (new dbKurs().getKursWithID(id) != null) {
                    // lädt den Kurs zum anzeigen
                    kurs = new dbKurs().getKursWithID(id);
                    //zeigt die einfachen Daten an
                    textViewKurs.setText(kurs.fach);
                    textViewLehrer.setText(kurs.lehrer.titel + " " + kurs.lehrer.name);
                    if (kurs.hinzugefuegtAm != null) {
                        textViewDate.setText("zuletzt geändert am: " + kurs.hinzugefuegtAm);
                    } else {
                        textViewDate.setText("zuletzt geändert am: kein Datum verfügbar.");
                    }

                } else {
                    Toast.makeText(KursActivity.this, "Kurs nicht vorhanden. ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(KursActivity.this, "Kein Kurs wurde übergeben. ", Toast.LENGTH_SHORT).show();
            }
        }
        setUpRecyclerView();


    }

    /**
     * @setUpRecyclerView zeigt alle Schulstunden des Kurses an
     */
    private void setUpRecyclerView() {
        List<stunden> stundenlist = new ArrayList<>();

        if (setupList(stundenlist) != null) {
            stundenlist = setupList(stundenlist);
        }
        KursAdapter kursadapter = new KursAdapter(this, stundenlist);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(kursadapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * @param stundenlist ist die Liste die alle Schulstunden enthält des Kurses
     * @return gibt die bearbeitete Liste zurück
     * @setUpList bereitet die Sundenlist für den RecylcerView vor in dem es die nötigen
     * Daten wie  Uhrzeit vorher berechnet und speichert
     */
    private List<stunden> setupList(List<stunden> stundenlist) {
        List<stunden> list = new ArrayList<>();
        // array zum Vergleich zu welcher Uhrzeit die Stunde ist
        String[] time = new String[]{"08.00 Uhr - 08.45 Uhr",
                "08.45 Uhr - 09.30 Uhr",
                "09.35 Uhr - 10.20 Uhr",
                "10.45 Uhr - 11.30 Uhr",
                "11.35 Uhr - 12.20 Uhr",
                "12.25 Uhr - 13.10 Uhr",
                "13.30 Uhr - 14.15 Uhr",
                "14.15 Uhr - 15.00 Uhr",
                "15.00 Uhr - 15.45 Uhr",
                "15.45 Uhr - 16.30 Uhr",};
        // überprüft an welchem Wochentag der Kurs ist
        List<dbSchulstunde> schulstundeList = kurs.getSchulStunden(kurs.getId());
        if (schulstundeList != null && schulstundeList.size() > 0) {
            for (dbSchulstunde schulstunde : schulstundeList) {
                stunden stunden = new stunden();
                stunden.raum = schulstunde.raum;
                String wochentag;
                switch (schulstunde.wochentag) {
                    case 1:
                        wochentag = "Montag";
                        break;
                    case 2:
                        wochentag = "Dienstag";
                        break;
                    case 3:
                        wochentag = "Mittwoch";
                        break;
                    case 4:
                        wochentag = "Donnerstag";
                        break;
                    case 5:
                        wochentag = "Freitag";
                        break;
                    default:
                        wochentag = "";
                }
                //speichert die neuen Daten
                stunden.wochentag = wochentag;
                stunden.stunde = schulstunde.beginnzeit;
                stunden.timeString = time[schulstunde.beginnzeit - 1];
                list.add(stunden);
            }
        }
        if (list.size() > 0) {
            //gibt die bearbeitete Liste zurück
            return list;
        } else {
            return null;
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

    /**
     * @param v     ist der View der angeclickt wurde
     * @param event nicht wichtig
     * @return true
     * @onTouch wird aufgerufen wenn entweden das Email Label oder das Aufgaben Label angeclickt wird
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.textViewEmail:
                Log.d(TAG, "email trigger");
                // startet den Email Intent zum verschicken einer Email an einen Lehrer
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", kurs.lehrer.email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hier der Betreff");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Sehr geehrte/r " + kurs.lehrer.titel + " " + kurs.lehrer.name + ",\n\n\n\nmit freundlichen Grüßen,\n" + new dbUser().getUser().vorname + " " + new dbUser().getUser().nachname);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                break;
            case R.id.textViewAufgaben:
                // startet die MainActivity und geht zu den Aufgaben Fragment
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}


