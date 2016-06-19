package leon.bms.activites.klausur;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import leon.bms.R;
import leon.bms.adapters.FehlerAdapter;
import leon.bms.adapters.KlausurAufsichtAdapter;
import leon.bms.adapters.KlausurInhaltAdapter;
import leon.bms.model.fehler;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbFehler;
import leon.bms.realm.dbKlausur;
import leon.bms.realm.dbKlausuraufsicht;
import leon.bms.realm.dbKlausurinhalt;
import leon.bms.realm.dbNote;

public class KlausurActivity extends AppCompatActivity implements View.OnClickListener, KlausurAufsichtAdapter.ViewHolder.ClickListener, KlausurInhaltAdapter.ViewHolder.ClickListener, FehlerAdapter.ViewHolder.ClickListener {

    TextView textViewKlausurname, textViewKursid, textViewDatum, textViewZeit, textViewRaum, textViewNotizen,
            textViewNote, textViewNotePunkte;
    ImageView imageViewExit;
    EditText editText;
    CardView cardViewCircle;
    RecyclerView recyclerViewKlausuraufsicht;
    RecyclerView recyclerViewKlausurinhalt;
    RecyclerView recyclerViewKlausurfehler;
    KlausurAufsichtAdapter aufsichtAdapter;
    dbKlausur klausur;
    KlausurInhaltAdapter inhaltAdapter;
    FehlerAdapter fehlerAdapter;
    RealmQueries realmQueries;
    String[] notenArray = {"5-", "5", "5+", "4-", "4", "4+", "3-", "3", "3+", "2-", "2", "2+", "1-", "1", "1+"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausur);

        textViewKlausurname = (TextView) findViewById(R.id.textViewKlausurname);
        textViewKursid = (TextView) findViewById(R.id.textViewKursID);
        textViewDatum = (TextView) findViewById(R.id.textViewDate);
        textViewNote = (TextView) findViewById(R.id.textViewNote);
        textViewNotePunkte = (TextView) findViewById(R.id.textViewNotePunkte);
        textViewZeit = (TextView) findViewById(R.id.textViewZeit);
        textViewRaum = (TextView) findViewById(R.id.textViewRaum);
        textViewNotizen = (TextView) findViewById(R.id.textViewNotizen);

        editText = (EditText) findViewById(R.id.editTextDurchschnitt);

        imageViewExit = (ImageView) findViewById(R.id.imageViewExit);
        cardViewCircle = (CardView) findViewById(R.id.cardViewCircle);

        cardViewCircle.setOnClickListener(this);
        recyclerViewKlausuraufsicht = (RecyclerView) findViewById(R.id.recyclerViewAufsicht);
        recyclerViewKlausurinhalt = (RecyclerView) findViewById(R.id.recyclerViewInhalt);
        recyclerViewKlausurfehler = (RecyclerView) findViewById(R.id.recyclerViewFehler);

        //die Activity lädt die Daten des speziellen Artikel
        if (getIntent() != null) {
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 100000000);
            if (id != 100000000) {
                realmQueries = new RealmQueries(this);
                if (realmQueries.getKlausur(id) != null) {
                    klausur =realmQueries.getKlausur(id);
                    textViewKlausurname.setText(klausur.getName());
                    textViewKursid.setText(klausur.getKurs().getName());
                    textViewNotizen.setText(klausur.getNotizen());
                    textViewDatum.setText(getDateString(klausur));
                    textViewZeit.setText(getZeitString(klausur));
                    textViewRaum.setText(klausur.getRaum().getName());

                    if (realmQueries.getNoteFromKlausur(klausur) == null) {
                        Log.d("changeNote", "keine Note vorhanden.");
                        textViewNote.setText("NA");
                        textViewNotePunkte.setText("Klick hier");
                    } else {
                        dbNote note = realmQueries.getNoteFromKlausur(klausur);
                        if (note.getPunkte() != null) {
                            textViewNotePunkte.setText(String.valueOf(note.getPunkte()) + " Punkte");
                            textViewNote.setText(notenArray[note.getPunkte() -1]);

                            Log.d("changeNote", "Note wurde erfolgreich geladen.");
                        }else {

                        }
                        if (note.getKlassendurchschnitt() != null){
                            editText.setText(String.valueOf(note.getKlassendurchschnitt()));
                            Log.d("changeNote", "Klassendurchschnitt wurde erfolgreich geladen.");
                        }
                    }

                    setUpAufsicht(klausur);
                    setUpInhalt(klausur);
                    setUpFehler(klausur);

                    imageViewExit.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            onBackPressed();
                            return false;
                        }
                    });

                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                    actionId == EditorInfo.IME_ACTION_DONE ||
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                if (Double.valueOf(v.getText().toString()) > 0.6 && Double.valueOf(v.getText().toString()) < 6.0) {
                                    if (realmQueries.getNoteFromKlausur(klausur) == null) {
                                        dbNote note = new dbNote();
                                        note.setKlassendurchschnitt(Double.valueOf(v.getText().toString()));
                                        note.setKlausur(klausur);
                                        save(note);
                                        View view = v.findFocus();
                                        if (view != null) {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            editText.clearFocus();
                                        }
                                        Log.d("changeNote2", "Klassendurchschnitt wurde eingespeichert");
                                    } else {
                                        dbNote note = realmQueries.getNoteFromKlausur(klausur);
                                        note.setKlassendurchschnitt(Double.valueOf(v.getText().toString()));
                                        save(note);
                                        View view = v.findFocus();
                                        if (view != null) {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            editText.clearFocus();
                                        }
                                        Log.d("changeNote", "Klassendurchschnitt wurde eingespeichert");
                                    }
                                    return true;
                                }
                            }
                            return false;
                        }
                    });


                }

            }
        }


    }
    private void save(final RealmObject object){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                bgrealm.copyToRealmOrUpdate(object);
                Log.d("Fragment_Kursauswahl","Saved Object");
            }
        });
    }
    private void setUpFehler(dbKlausur klausur) {
        fehlerAdapter = new FehlerAdapter(this, convertedList(),this);
        recyclerViewKlausurfehler.setAdapter(fehlerAdapter);
        recyclerViewKlausurfehler.setHasFixedSize(false);
        recyclerViewKlausurfehler.setItemAnimator(new DefaultItemAnimator());
        recyclerViewKlausurfehler.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<fehler> convertedList() {
        if (realmQueries.getFehlerFromKlausur(klausur) != null) {
            List<dbFehler> fehlerList = realmQueries.getFehlerFromKlausur(klausur);
            List<fehler> convertedFehlerList = new ArrayList<>();
            for (dbFehler dbfehler : fehlerList) {
                fehler fehler1 = new fehler();
                fehler1.setFehler(dbfehler);
                fehler1.setStatus(false);
                convertedFehlerList.add(fehler1);
            }
            fehler fehler1 = new fehler();
            fehler1.setStatus(true);
            convertedFehlerList.add(fehler1);
            return convertedFehlerList;
        } else {
            List<fehler> convertedFehlerList = new ArrayList<>();
            fehler fehler1 = new fehler();
            fehler1.setStatus(true);
            convertedFehlerList.add(fehler1);
            return convertedFehlerList;
        }
    }

    private void setUpInhalt(dbKlausur klausur) {
        if (realmQueries.getInhaltFromKlausur(klausur) != null) {
            List<dbKlausurinhalt> klausurinhaltList = realmQueries.getInhaltFromKlausur(klausur);
            inhaltAdapter = new KlausurInhaltAdapter(this, klausurinhaltList,this);
            recyclerViewKlausurinhalt.setAdapter(inhaltAdapter);
            recyclerViewKlausurinhalt.setHasFixedSize(true);
            recyclerViewKlausurinhalt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewKlausurinhalt.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void setUpAufsicht(dbKlausur klausur) {
        if (realmQueries.getAufsichtFromKlausur(klausur) != null) {
            List<dbKlausuraufsicht> aufsichtList =realmQueries.getAufsichtFromKlausur(klausur) ;
            aufsichtAdapter = new KlausurAufsichtAdapter(this, aufsichtList);
            recyclerViewKlausuraufsicht.setAdapter(aufsichtAdapter);
            recyclerViewKlausuraufsicht.setHasFixedSize(true);
            recyclerViewKlausuraufsicht.setItemAnimator(new DefaultItemAnimator());
            recyclerViewKlausuraufsicht.setLayoutManager(new LinearLayoutManager(this));
        }

    }


    public String getDateString(dbKlausur klausur) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(klausur.getDatum()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy ");

        return sdfmt.format(calendar.getTime());
    }

    public String getZeitString(dbKlausur klausur) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            calendar.setTime(myFormat.parse(klausur.getBeginn()));
            calendar2.setTime(myFormat.parse(klausur.getEnde()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("HH:mm");

        return sdfmt.format(calendar.getTime()) + " Uhr\n-" + sdfmt.format(calendar2.getTime()) + " Uhr";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewCircle:
                numberdialogshow();
                break;
            case R.id.imageViewExit:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void numberdialogshow() {
        final Dialog d = new Dialog(KlausurActivity.this,R.style.PauseDialog);
        d.setTitle("Wähle deine Punktzahl");

        d.setContentView(R.layout.numberdialog);
        final Button b1 = (Button) d.findViewById(R.id.button1);
        final Button b2 = (Button) d.findViewById(R.id.button2);
        final Button b3 = (Button) d.findViewById(R.id.button3);
        final Button b4 = (Button) d.findViewById(R.id.button4);
        final Button b5 = (Button) d.findViewById(R.id.button5);
        final Button b6 = (Button) d.findViewById(R.id.button6);
        final Button b7 = (Button) d.findViewById(R.id.button7);
        final Button b8 = (Button) d.findViewById(R.id.button8);
        final Button b9 = (Button) d.findViewById(R.id.button9);
        final Button b10 = (Button) d.findViewById(R.id.button10);
        final Button b11 = (Button) d.findViewById(R.id.button11);
        final Button b12 = (Button) d.findViewById(R.id.button12);
        final Button b13 = (Button) d.findViewById(R.id.button13);
        final Button b14 = (Button) d.findViewById(R.id.button14);
        final Button b15 = (Button) d.findViewById(R.id.button15);
        final Button bCancel = (Button) d.findViewById(R.id.button);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(15);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(14);
                d.dismiss();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(13);
                d.dismiss();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(12);
                d.dismiss();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(11);
                d.dismiss();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(10);
                d.dismiss();
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(9);
                d.dismiss();
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(8);
                d.dismiss();
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(7);
                d.dismiss();
            }
        });
        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(6);
                d.dismiss();
            }
        });
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(5);
                d.dismiss();
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(4);
                d.dismiss();
            }
        });
        b13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(3);
                d.dismiss();
            }
        });
        b14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(2);
                d.dismiss();
            }
        });
        b15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNote(1);
                d.dismiss();
            }
        });


        d.show();

    }

    private void changeNote(Integer punktzahl) {
        textViewNotePunkte.setText(punktzahl + " Punkte");
        textViewNote.setText(notenArray[punktzahl-1]);
        if (klausur != null) {
            dbNote note;
            if (realmQueries.getNoteFromKlausur(klausur) == null) {
                note = new dbNote();
                Log.d("changeNote", "neue Note wurde erstellt und gespeichert.");
            } else {
                note = realmQueries.getNoteFromKlausur(klausur);
                Log.d("changeNote", "alte Note wurde aktualisiert.");
            }
            note.setPunkte(punktzahl);
            note.setSchriftlich(true);
            note.setKlausur(klausur);
            note.setKurs(klausur.getKurs());

            save(note);
        }
    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    @Override
    public void onItemClicked2(int position) {
        inhaltAdapter.changeErledigt(position);
    }

    @Override
    public boolean onItemLongClicked2(int position) {
        inhaltAdapter.changeErledigt(position);
        return false;
    }


    @Override
    public void onItemClicked3(int position) {
        int alleFehler = fehlerAdapter.getItemCount() - 1;
        if (position == alleFehler) {
            stringdialogshow();
        } else {
            fehlerAdapter.changeErledigt(position);
        }

    }

    private void stringdialogshow() {
        Log.d("stringdialogshow", "show");
        final Dialog d = new Dialog(KlausurActivity.this);
        d.setTitle("Gebe deinen Fehler ein!");
        d.setContentView(R.layout.stringdialog);
        Button submit = (Button) d.findViewById(R.id.buttonsubmit);
        final Button cancel = (Button) d.findViewById(R.id.buttoncancel);
        final EditText ed = (EditText) d.findViewById(R.id.editText);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed.getText().toString().trim().equals("")) {
                    dbFehler fehler = new dbFehler();
                    fehler.setBeschreibung(ed.getText().toString());
                    fehler.setBearbeitet(false);
                    fehler.setKlausur(klausur);
                    save(fehler);

                    fehler fehler1 = new fehler();
                    fehler1.setFehler(fehler);
                    fehler1.setStatus(false);

                    fehlerAdapter.addAufgabe(fehler1);
                    recyclerViewKlausurfehler.smoothScrollToPosition(0);
                    d.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public boolean onItemLongClicked3(final int position) {
        int alleFehler = fehlerAdapter.getItemCount() - 1;
        if (position == alleFehler) {
            stringdialogshow();
        } else {
            if (fehlerAdapter.getFehler(position) != null) {
                final fehler fehler1 = fehlerAdapter.getFehler(position);
                String fehlerString = fehler1.getFehler().getBeschreibung();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Möchten sie den ausgewählten Fehler löschen?")
                        .setCancelable(false)
                        .setMessage("Fehler: " + fehlerString)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete(fehler1.getFehler());

                                //fehlerAdapter.deleteFehler(position); //TODO fix bug
                                fehlerAdapter.changeDataSet(convertedList());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        return false;
    }

    private void delete(final dbFehler fehler) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                fehler.deleteFromRealm();

            }
        });
    }
}
