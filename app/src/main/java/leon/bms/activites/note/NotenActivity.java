package leon.bms.activites.note;

import android.app.Dialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import leon.bms.R;
import leon.bms.activites.Klausur.KlausurActivity;
import leon.bms.adapters.KlausurenAdapter;
import leon.bms.adapters.MuendlichAdapter;
import leon.bms.database.dbKlausur;
import leon.bms.database.dbKurs;
import leon.bms.database.dbNote;
import leon.bms.model.mündlicheNoten;
import leon.bms.model.notenModel;

public class NotenActivity extends AppCompatActivity implements KlausurenAdapter.ViewHolder.ClickListener, MuendlichAdapter.ViewHolder.ClickListener, View.OnClickListener {

    TextView textViewKurs, textViewNote, textViewLehrer;
    RecyclerView recyclerViewKlausuren, recyclerViewMündlich;
    ImageView imageViewExit;
    KlausurenAdapter klausurenAdapter;
    MuendlichAdapter muendlichAdapter;
    CardView cardViewCircle;
    Boolean punkteAnzeige = true;

    dbKurs kurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noten);

        textViewKurs = (TextView) findViewById(R.id.textViewKursID);
        textViewLehrer = (TextView) findViewById(R.id.textViewLehrer);
        textViewNote = (TextView) findViewById(R.id.textViewNote);
        imageViewExit = (ImageView) findViewById(R.id.imageViewExit);

        cardViewCircle = (CardView) findViewById(R.id.cardViewCircle);
        cardViewCircle.setOnClickListener(this);

        recyclerViewKlausuren = (RecyclerView) findViewById(R.id.recyclerViewKlausuren);
        recyclerViewMündlich = (RecyclerView) findViewById(R.id.recyclerViewMündlich);

        //die Activity lädt die Daten des speziellen Artikel
        if (getIntent() != null) {
            Intent intent = getIntent();
            Long id = intent.getLongExtra("id", 100000000);
            if (id != 100000000) {
                if (new dbKurs().getKursWithID(id) != null) {
                    kurs = new dbKurs().getKursWithID(id);
                    textViewKurs.setText(kurs.name);
                    textViewLehrer.setText(kurs.lehrer.titel + " " + kurs.lehrer.name);
                    if (getNotenList(kurs).getDurchschnitt() == 0) {
                        textViewNote.setText("NA");
                    } else {
                        textViewNote.setText(String.valueOf(getNotenList(kurs).getDurchschnitt()));
                    }

                    setUpKlausuren(kurs);
                    setUpMündlich(kurs);

                }
            }
        }

        imageViewExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });


    }

    private notenModel getNotenList(dbKurs kurs) {
        notenModel notenModel1 = new notenModel();
        notenModel1.setKurs(kurs);
        if (kurs.getNoteWithId(kurs.getId(), 1) != null) {
            List<dbNote> schriftlich = kurs.getNoteWithId(kurs.getId(), 1);
            notenModel1.setSchriftlicheNoten(schriftlich);
        }
        if (kurs.getNoteWithId(kurs.getId(), 0) != null) {
            List<dbNote> mündlich = kurs.getNoteWithId(kurs.getId(), 0);
            notenModel1.setMündlicheNoten(mündlich);
        }
        notenModel1.setDurchschnitt(round(getNotenDurchschnitt(notenModel1), 1));

        return notenModel1;
    }

    private double getNotenDurchschnitt(notenModel notenModel1) {
            notenModel note = notenModel1;
            List<dbNote> alleNoten = new ArrayList<>();
            if (note.getMündlicheNoten() != null) {
                alleNoten.addAll(note.getMündlicheNoten());
            }
            if (note.getSchriftlicheNoten() != null) {
                alleNoten.addAll(note.getSchriftlicheNoten());
            }
            if (alleNoten.size() > 0) {
                Double newdurchschnitt = 0.0;
                for (dbNote note1 : alleNoten) {
                    newdurchschnitt += note1.punkte;
                }
                newdurchschnitt = newdurchschnitt / alleNoten.size();
                return newdurchschnitt;
            }
            return 0;

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setUpKlausuren(dbKurs kurs) {
        if (kurs.getKlausurWithId(kurs.getId()) != null) {
            List<dbKlausur> klausurList = kurs.getKlausurWithId(kurs.getId());
            klausurenAdapter = new KlausurenAdapter(this, klausurList);
            recyclerViewKlausuren.setAdapter(klausurenAdapter);
            recyclerViewKlausuren.setHasFixedSize(true);
            recyclerViewKlausuren.setItemAnimator(new DefaultItemAnimator());
            recyclerViewKlausuren.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private void setUpMündlich(dbKurs kurs) {
        muendlichAdapter = new MuendlichAdapter(this, convertedList());
        recyclerViewMündlich.setAdapter(muendlichAdapter);
        recyclerViewMündlich.setHasFixedSize(false);
        recyclerViewMündlich.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMündlich.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<mündlicheNoten> convertedList() {
        if (kurs.getNoteWithId(kurs.getId(), 0) != null) {
            List<dbNote> noteList = kurs.getNoteWithId(kurs.getId(), 0);
            List<mündlicheNoten> mündlicheNotenList = new ArrayList<>();
            for (dbNote note : noteList) {
                mündlicheNoten münNote = new mündlicheNoten();
                münNote.setNote(note);
                münNote.setKeinNote(false);
                mündlicheNotenList.add(münNote);
            }
            mündlicheNoten münNote = new mündlicheNoten();
            münNote.setKeinNote(true);
            mündlicheNotenList.add(münNote);
            return mündlicheNotenList;
        } else {
            List<mündlicheNoten> mündlicheNotenList = new ArrayList<>();
            mündlicheNoten münNote = new mündlicheNoten();
            münNote.setKeinNote(true);
            mündlicheNotenList.add(münNote);
            return mündlicheNotenList;
        }
    }

    private void numberdialogshow() {
        final Dialog d = new Dialog(NotenActivity.this);
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
        dbNote note = new dbNote();

        note.punkte = punktzahl;
        note.schriftlich = false;
        note.kurs = kurs;
        note.hinzugefuegtAm = getZeitString();
        note.save();

        mündlicheNoten münNote = new mündlicheNoten();
        münNote.setKeinNote(false);
        münNote.setNote(note);

        muendlichAdapter.addAufgabe(münNote);
        recyclerViewMündlich.smoothScrollToPosition(0);

        updateDurchschnitt();
    }

    private void updateDurchschnitt() {
        if (punkteAnzeige) {
            getRealDurchschnitt();
        } else {
            if (getNotenList(kurs).getDurchschnitt() == 0) {
                textViewNote.setText("NA");
            } else {
                textViewNote.setText(String.valueOf(getNotenList(kurs).getDurchschnitt()));
            }
        }

    }


    public String getZeitString() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd");
        return sdfmt.format(calendar.getTime());
    }


    @Override
    public void onItemClicked(int position) {
        if (position>=0){
            Log.d("Fragment_Klausur", "Item click");
            Intent intent = new Intent(this, KlausurActivity.class);
            intent.putExtra("id", klausurenAdapter.getKlausurId(position));
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpKlausuren(kurs);
        updateDurchschnitt();
    }

    @Override
    public boolean onItemLongClicked(int position) {

        return false;
    }

    @Override
    public void onItemClicked2(int position) {
        int alleMünNoten = muendlichAdapter.getItemCount() - 1;
        if (position == alleMünNoten) {
            numberdialogshow();
        } else {

        }
    }

    @Override
    public boolean onItemLongClicked2(int position) {
        int alleMünNoten = muendlichAdapter.getItemCount() - 1;
        if (position == alleMünNoten) {
            numberdialogshow();
        } else {
            if (muendlichAdapter.getFehler(position) != null) {
                final mündlicheNoten münNote = muendlichAdapter.getFehler(position);
                String fehlerString = position + 1 + ". Quartal, " + münNote.getNote().punkte + " Punkte";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Möchten sie die ausgewählte Note löschen?")
                        .setCancelable(false)
                        .setMessage(fehlerString)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbNote.delete(münNote.getNote());
                                //fehlerAdapter.deleteFehler(position); //TODO fix bug
                                muendlichAdapter.changeDataSet(convertedList());
                                updateDurchschnitt();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewCircle:
                if (punkteAnzeige) {
                    punkteAnzeige = false;
                } else {
                    punkteAnzeige = true;
                }
                updateDurchschnitt();
                break;
            default:
                break;
        }
    }

    private void getRealDurchschnitt() {
        if (getNotenList(kurs).getDurchschnitt() == 0) {
            textViewNote.setText("NA");
        } else {
            notenModel note = getNotenList(kurs);
            List<dbNote> alleNoten = new ArrayList<>();
            if (note.getMündlicheNoten() != null) {
                alleNoten.addAll(note.getMündlicheNoten());
            }
            if (note.getSchriftlicheNoten() != null) {
                alleNoten.addAll(note.getSchriftlicheNoten());
            }
            if (alleNoten.size() > 0) {
                Double[] notenRechner = {6.7777, 6.3333, 5.0, 4.7777, 4.3333, 4.0, 3.7777, 3.3333, 3.0, 2.7777, 2.3333, 2.0, 1.7777, 1.3333, 1.0, 0.7777};
                Double newdurchschnitt = 0.0;
                for (dbNote note1 : alleNoten) {
                    newdurchschnitt += notenRechner[note1.punkte];
                }
                newdurchschnitt = newdurchschnitt / alleNoten.size();
                textViewNote.setText(String.valueOf(round(newdurchschnitt, 1)));
            } else {
                textViewNote.setText("NA");
            }
        }

    }
}
