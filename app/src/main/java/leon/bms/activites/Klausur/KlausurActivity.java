package leon.bms.activites.Klausur;

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

import leon.bms.R;
import leon.bms.adapters.FehlerAdapter;
import leon.bms.adapters.KlausurAufsichtAdapter;
import leon.bms.adapters.KlausurInhaltAdapter;
import leon.bms.database.dbFehler;
import leon.bms.database.dbKlausur;
import leon.bms.database.dbKlausuraufsicht;
import leon.bms.database.dbKlausurinhalt;
import leon.bms.database.dbNote;
import leon.bms.model.fehler;

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
            Long id = intent.getLongExtra("id", 100000000);
            if (id != 100000000) {
                if (new dbKlausur().getKlausurWithId(id) != null) {
                    klausur = new dbKlausur().getKlausurWithId(id);
                    textViewKlausurname.setText(klausur.name);
                    textViewKursid.setText(klausur.kurs.name);
                    textViewNotizen.setText(klausur.notizen);
                    textViewDatum.setText(getDateString(klausur));
                    textViewZeit.setText(getZeitString(klausur));
                    textViewRaum.setText(klausur.raum.nummer);

                    if (klausur.getNoteWithId(klausur.getId()) == null) {
                        Log.d("changeNote", "keine Note vorhanden.");
                        textViewNote.setText("NA");
                        textViewNotePunkte.setText("Klick hier");
                    } else {
                        dbNote note = klausur.getNoteWithId(klausur.getId());
                        if (note.punkte != null) {
                            textViewNotePunkte.setText(String.valueOf(note.punkte) + " Punkte");
                            textViewNote.setText(notenArray[note.punkte]);

                            Log.d("changeNote", "Note wurde erfolgreich geladen.");
                        }else {

                        }
                        if (note.klassendurchschnitt != null){
                            editText.setText(String.valueOf(note.klassendurchschnitt));
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
                                    if (klausur.getNoteWithId(klausur.getId()) == null) {
                                        dbNote note = new dbNote();
                                        note.klassendurchschnitt = Double.valueOf(v.getText().toString());
                                        note.klausur = klausur;
                                        note.save();
                                        View view = v.findFocus();
                                        if (view != null) {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            editText.clearFocus();
                                        }
                                        Log.d("changeNote2", "Klassendurchschnitt wurde eingespeichert");
                                    } else {
                                        dbNote note = klausur.getNoteWithId(klausur.getId());
                                        note.klassendurchschnitt = Double.valueOf(v.getText().toString());
                                        note.save();
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

    private void setUpFehler(dbKlausur klausur) {
        fehlerAdapter = new FehlerAdapter(this, convertedList());
        recyclerViewKlausurfehler.setAdapter(fehlerAdapter);
        recyclerViewKlausurfehler.setHasFixedSize(false);
        recyclerViewKlausurfehler.setItemAnimator(new DefaultItemAnimator());
        recyclerViewKlausurfehler.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<fehler> convertedList() {
        if (klausur.getFehlerFromKlausur(klausur.getId()) != null) {
            List<dbFehler> fehlerList = klausur.getFehlerFromKlausur(klausur.getId());
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
        if (klausur.getInhaltFromKlausur(klausur.getId()) != null) {
            List<dbKlausurinhalt> klausurinhaltList = klausur.getInhaltFromKlausur(klausur.getId());
            inhaltAdapter = new KlausurInhaltAdapter(this, klausurinhaltList);
            recyclerViewKlausurinhalt.setAdapter(inhaltAdapter);
            recyclerViewKlausurinhalt.setHasFixedSize(true);
            recyclerViewKlausurinhalt.setItemAnimator(new DefaultItemAnimator());
            recyclerViewKlausurinhalt.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void setUpAufsicht(dbKlausur klausur) {
        if (klausur.getAufsichtFromKlausur(klausur.getId()) != null) {
            List<dbKlausuraufsicht> aufsichtList = klausur.getAufsichtFromKlausur(klausur.getId());
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
            calendar.setTime(myFormat.parse(klausur.datum));

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
            calendar.setTime(myFormat.parse(klausur.beginn));
            calendar2.setTime(myFormat.parse(klausur.ende));

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
        final Dialog d = new Dialog(KlausurActivity.this);
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
        textViewNote.setText(notenArray[punktzahl]);
        if (klausur != null) {
            dbNote note;
            if (klausur.getNoteWithId(klausur.getId()) == null) {
                note = new dbNote();
                Log.d("changeNote", "neue Note wurde erstellt und gespeichert.");
            } else {
                note = klausur.getNoteWithId(klausur.getId());
                Log.d("changeNote", "alte Note wurde aktualisiert.");
            }
            note.punkte = punktzahl;
            note.schriftlich = true;
            note.klausur = klausur;
            note.kurs = klausur.kurs;
            note.save();
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
                    fehler.beschreibung = ed.getText().toString();
                    fehler.bearbeitet = false;
                    fehler.klausur = klausur;
                    fehler.save();

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
                String fehlerString = fehler1.getFehler().beschreibung;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Möchten sie den ausgewählten Fehler löschen?")
                        .setCancelable(false)
                        .setMessage("Fehler: " + fehlerString)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbFehler.delete(fehler1.getFehler());
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
}
