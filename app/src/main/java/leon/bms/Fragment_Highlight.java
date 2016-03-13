package leon.bms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * @Fragment_Highlight ist ein Fragment , welches kompakt alles wichtiges anzeigen soll
 * Es soll die aktuelle Stunde sowie wichtige Neuigkeiten und noch zu machen Hausaufgaben anzeigen
 */
public class Fragment_Highlight extends Fragment implements NachrichtenAdapter.ViewHolder.ClickListener {

    // views
    TextView textViewNumber, textViewNumber2, textViewLehrer, textViewLehrer2, textViewStunde, textViewStunde2, textViewRaum, textViewRaum2;
    RecyclerView recyclerView;
    Snackbar snackbar;
    LinearLayout linearLayout;
    List<nachrichten> nachrichtenList = new ArrayList<>();
    NachrichtenAdapter nachrichtenAdapter;

    private static String TAG = Fragment_Highlight.class.getSimpleName();
    // wichtig für das aktualiseren der Anzeige
    BroadcastReceiver broadcastReceiver;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__highlight, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initial views
        textViewNumber = (TextView) view.findViewById(R.id.textViewNumber);
        textViewNumber2 = (TextView) view.findViewById(R.id.textViewNumber2);
        textViewLehrer = (TextView) view.findViewById(R.id.textViewLehrer);
        textViewLehrer2 = (TextView) view.findViewById(R.id.textViewLehrer2);
        textViewStunde = (TextView) view.findViewById(R.id.textViewStunde);
        textViewStunde2 = (TextView) view.findViewById(R.id.textViewStunde2);
        textViewRaum = (TextView) view.findViewById(R.id.textViewRaum);
        textViewRaum2 = (TextView) view.findViewById(R.id.textViewRaum2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);

        snackbar = Snackbar.make(linearLayout, "Loading News..", Snackbar.LENGTH_LONG);
        snackbar.show();
        getNews();


        // setUp recylcerView
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        nachrichtenAdapter = new NachrichtenAdapter(this, nachrichtenList);
        recyclerView.setAdapter(nachrichtenAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);

        setUpNächsteStunde();


    }


    public void getNews() {
        // URL für die Server Anfrage
        String Url = "http://app.marienschule.de/files/scripts/nachrichten.php";
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", new dbUser().getUser().benutzername)
                .appendQueryParameter("pw", new LogInController(getActivity()).getPass())
                .appendQueryParameter("stufe", new dbUser().getUser().stufe);
        String params = builder.build().getEncodedQuery();

        atOnline atOnline2 = new atOnline(Url, params, getActivity());
        atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {
            @Override
            public void onUpdate(String result) {
                setUpNews(result);
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
        });
        atOnline2.execute();

    }

    public void setUpNews(String result) {
        if (result == "404") {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {

            try {
                JSONArray jsonArrayAll = new JSONArray(result);
                for (int i = 0; i < jsonArrayAll.length(); i++) {
                    JSONObject jsonObject = jsonArrayAll.getJSONObject(i);
                    nachrichten nachrichten = new nachrichten();
                    nachrichten.serverid = jsonObject.getInt("id");
                    nachrichten.titel = jsonObject.getString("titel");
                    nachrichten.nachricht = jsonObject.getString("nachricht");
                    nachrichten.hinzugefuegtAm = jsonObject.getString("hinzugefuegtAm");
                    nachrichten.geandertAm = jsonObject.getString("geaendertAm");
                    nachrichten.dateString = setUpDate(jsonObject.getString("geaendertAm"));
                    nachrichten.geloescht = jsonObject.getInt("geloescht");
                    if (!nachrichtenList.contains(nachrichten)) {
                        nachrichtenList.add(nachrichten);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nachrichtenAdapter.addList(sortListDate(nachrichtenList));

        }
    }

    private String setUpDate(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = calendar.getTime();
        //YEAR_IN_MILLIS wenn das Datum mehr als ein Jahr zurück liegt wird das ganze Datum angezeigt
        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(getActivity(), date1.getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0));
        return zuletztAktualisiert;
    }

    @Override
    public void onStart() {
        super.onStart();
        // updates view ever mintute
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    setUpNächsteStunde();

                }
            }
        };
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null)
            getActivity().unregisterReceiver(broadcastReceiver);
    }

    /**
     * @setUpNächsteStunde berechnet die jetztige und nächste Stunde und zeigt diese an
     */
    public void setUpNächsteStunde() {
        Log.d(TAG, "Updated Time");
        // Array mit den Schulstunden
        Calendar calendar = Calendar.getInstance();
        GregorianCalendar[] calendars = new GregorianCalendar[]{
                new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 8, 0, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 8, 45, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 9, 35, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 10, 45, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 11, 35, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 12, 25, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 13, 30, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 14, 15, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 15, 0, 0)
                , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 15, 45, 0)


        };

        // sucht die aktuelle Stunde heraus
        Calendar date = GregorianCalendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int aktuelleStunde = 0;
        for (int i = calendars.length - 1; i >= 0; i--) {
            //vergleich die aktuelle Zeit mit dem Array um zu gucken in welcher Schulstunde man sich begfindet
            if (date.before(calendars[i])) {
                aktuelleStunde = i;

            }
        }
        // wenn die aktuelle Stunde nach den Zeiten des Arrays ist wird die aktuelle Stunde auf 99 gesetzt
        if (date.after(calendars[calendars.length - 1])) {
            aktuelleStunde = 99;
        }

        Log.d(TAG, "Aktuelle Stunde: " + aktuelleStunde + " wochentag: " + week);
        // wenn der wochentag in der schulwoche ist

        if (week > 0 && week < 6) {
            // wenn die aktuelleStunde ein Schulstunde ist
            if (aktuelleStunde > 0 && aktuelleStunde < 10) {
                // holt sich alle Kurse
                List<dbKurs> aktiveKurse = new dbKurs().getActiveKurse(0);
                aktiveKurse.addAll(new dbKurs().getActiveKurse(1));

                List<dbSchulstunde> schulstundeList = new ArrayList<>();
                // holt sich von den Kursen die entsprechenden schulstunde für die Woche
                for (dbKurs kurs : aktiveKurse) {
                    if (kurs.getSchulstundeWithWeekAndKurs(kurs.getId(), week) != null) {
                        schulstundeList.addAll(kurs.getSchulstundeWithWeekAndKurs(kurs.getId(), week));
                    }
                }
                // wenn an dem Tag Unterricht ist also Schulstunden vorhanden sind
                if (schulstundeList.size() > 0) {
                    List<stunden> stundenList = convertSchulstundenZuStundeListe(sortListASC(schulstundeList));
                    int letzteStunde = stundenList.get(stundenList.size() - 1).stunde;
                    for (int i = 0; i < stundenList.size(); i++) {
                        stunden stunden = stundenList.get(i);
                        if (stunden.stunde == aktuelleStunde) {
                            if (aktuelleStunde != letzteStunde) {
                                if (stunden.active == true) {
                                    // wenn die aktuelle Stunde aktiv ist also keine Freistunde
                                    textViewStunde.setText(stunden.getStundenname());
                                    textViewRaum.setText(stunden.getRaum());
                                    textViewLehrer.setText(stunden.getLehrer());
                                    textViewNumber.setText(String.valueOf(stunden.getStunde()));
                                } else {
                                    // sonst Freistunde
                                    textViewStunde.setText("Freistunde");
                                    textViewRaum.setText("");
                                    textViewLehrer.setText("");
                                    textViewNumber.setText(String.valueOf(stunden.getStunde()));
                                }
                                stunden stunden2 = stundenList.get(i + 1);
                                if (stunden2.active == true) {
                                    textViewStunde2.setText(stunden2.getStundenname());
                                    textViewRaum2.setText(stunden2.getRaum());
                                    textViewLehrer2.setText(stunden2.getLehrer());
                                    textViewNumber2.setText(String.valueOf(stunden2.getStunde()));
                                } else {
                                    textViewStunde2.setText("Freistunde");
                                    textViewRaum2.setText("");
                                    textViewLehrer2.setText("");
                                    textViewNumber2.setText(String.valueOf(stunden2.getStunde()));
                                }
                            } else {
                                // wenn die aktuelle Stunde die letze Stunde ist
                                textViewStunde.setText(stunden.getStundenname());
                                textViewRaum.setText(stunden.getRaum());
                                textViewLehrer.setText(stunden.getLehrer());
                                textViewNumber.setText(String.valueOf(stunden.getStunde()));

                                textViewStunde2.setText("Danach kein Unterricht mehr.");
                                textViewRaum2.setText("");
                                textViewLehrer2.setText("Schule aus!");
                                textViewNumber2.setText(String.valueOf(""));
                            }
                        }
                    }

                }
            } else if (aktuelleStunde == 99) {
                // wenn es nachdem Unterrich ist
                textViewStunde.setText("Kein Unterricht mehr!");
                textViewRaum.setText("");
                textViewLehrer.setText("Denk an deine Hausaufgaben.");
                textViewNumber.setText(String.valueOf(""));
                textViewStunde2.setText("Kein Unterricht mehr!");
                textViewRaum2.setText("");
                textViewLehrer2.setText("");
                textViewNumber2.setText(String.valueOf(""));

            } else if (aktuelleStunde == 0) {
                // wenn es vor dem Unterricht ist
                textViewStunde.setText("Kein Unterricht!");
                textViewRaum.setText("");
                textViewLehrer.setText("Denk an deine Hausaufgaben.");
                textViewNumber.setText(String.valueOf(""));

                List<dbKurs> aktiveKurse = new dbKurs().getActiveKurse(0);
                aktiveKurse.addAll(new dbKurs().getActiveKurse(1));

                List<dbSchulstunde> schulstundeList = new ArrayList<>();
                for (dbKurs kurs : aktiveKurse) {
                    if (kurs.getSchulstundeWithWeekAndKurs(kurs.getId(), week) != null) {
                        schulstundeList.addAll(kurs.getSchulstundeWithWeekAndKurs(kurs.getId(), week));
                    }
                }
                // zeigt die erste Stunde an für den Tag
                if (schulstundeList.size() > 0) {
                    List<stunden> stundenList = convertSchulstundenZuStundeListe(sortListASC(schulstundeList));
                    stunden stunden = stundenList.get(0);
                    textViewStunde2.setText(stunden.getStundenname());
                    textViewRaum2.setText(stunden.getRaum());
                    textViewLehrer2.setText(stunden.getLehrer());
                    textViewNumber2.setText(String.valueOf(stunden.getStunde()));
                }
            }
        } else if (week == 6) {
            textViewStunde.setText("Heute ist Samstag ");
            textViewRaum.setText("");
            textViewLehrer.setText("Unterrichtsfrei");
            textViewNumber.setText("");

            textViewStunde2.setText("Heute ist Samstag ");
            textViewRaum2.setText("");
            textViewLehrer2.setText("Unterrichtsfrei");
            textViewNumber2.setText(" ");
        } else if (week == 0) {
            textViewStunde.setText("Heute ist Sonntag ");
            textViewRaum.setText("");
            textViewLehrer.setText("Unterrichtsfrei");
            textViewNumber.setText("");

            textViewStunde2.setText("Heute ist Sonntag ");
            textViewRaum2.setText("");
            textViewLehrer2.setText("Unterrichtsfrei");
            textViewNumber2.setText(" ");
        }

    }

    /**
     * @param list mit aktuallen Schulstunden wird pbergeben
     * @return sortierte List wird zurückgegeben
     * @sortListASC sortiert eine Schulstunde Liste nach der Zeit
     */
    public List<dbSchulstunde> sortListASC(List<dbSchulstunde> list) {
        Collections.sort(list, new Comparator<dbSchulstunde>() {
            @Override
            public int compare(dbSchulstunde lhs, dbSchulstunde rhs) {
                return lhs.getBeginnzeit().compareTo(rhs.getBeginnzeit());
            }
        });
        return list;
    }

    /**
     * @param wochentagListe Liste für einen Wochentag wird übergeben
     * @return gibt die kovertierte List zurück
     * @covertSchulstundenZuStundeListe konvertiert die Datenbank schulstunde zu normalen Stunden für einer einfachere Anzeige für
     * den AufgabenAdapter. Die konvertierte List enthält auch Freistunden.
     */
    public List<stunden> convertSchulstundenZuStundeListe(List<dbSchulstunde> wochentagListe) {
        // letzteStunde ist die letzte Schulstunde der WochentagListe
        if (wochentagListe.size() > 0) {
            int letzteStunde = wochentagListe.get(wochentagListe.size() - 1).beginnzeit;
            // stundenplanListe ist nacher die fertige Liste mit alle Schulstunden und Freistunden
            List<stunden> stundenplanListe = new ArrayList<>();
            // geht alle Stunden von 1 bis zu letzten Stunde durch

            for (int i = 1; i <= letzteStunde; i++) {
                stunden stunden = new stunden();
                int l = 0;
                // überprüft ob die Stunden vorhanden ist
                for (int k = 0; k < wochentagListe.size(); k++) {
                    if (wochentagListe.get(k).getBeginnzeit() == i) {
                        // wenn die wochentagsListe die Zeit i beinhaltet wir l addiert;
                        l = k + 1;
                    }
                }
                // wenn l größer als 0 ist die Stunden vorhanden
                // wenn l nicht größer aks 0 ist wird eine Freistunde erstellt
                if (l > 0) {
                    Log.d("MAIN", "stunde erstellt");
                    dbSchulstunde schulstunde = wochentagListe.get(l - 1);
                    stunden.setActive(true);
                    stunden.setRaum(schulstunde.raum);
                    stunden.setStundenname(schulstunde.kurs.fach);
                    stunden.setLehrer(schulstunde.lehrer.titel + " " + schulstunde.lehrer.name);
                    stunden.setStunde(schulstunde.beginnzeit);
                } else {
                    Log.d("MAIN", "freistunde erstellt");
                    stunden.setStunde(i);
                    stunden.setActive(false);

                }
                stundenplanListe.add(stunden);

            }
            // gibt die fertige stundenplanListe zurück;
            return stundenplanListe;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @param websiteArtikelList Liste die nach dem Datum sortiert werden soll
     * @return gibt die sortierte Liste zurück
     * @sortListDate sortiert die Liste nach ihrem Datum absteigen
     */
    private List<nachrichten> sortListDate(List<nachrichten> websiteArtikelList) {
        Collections.sort(nachrichtenList, new Comparator<nachrichten>() {
            public int compare(nachrichten nachrichten1, nachrichten nachrichten2) {
                return stringToCalander(nachrichten1.geandertAm).getTime().compareTo(stringToCalander(nachrichten2.geandertAm).getTime());
            }
        });
        Collections.reverse(websiteArtikelList);
        return nachrichtenList;
    }

    /**
     * @param date date in String
     * @return gibt Date in Calendar zurück
     * @stringToCalander parsed ein Datum vom Datentyp String zum Datentyp Calendar, dies
     * ist wichtig für das sortieren nach dem Datum
     */
    public Calendar stringToCalander(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }


    @Override
    public void onItemClicked(int position) {
        nachrichten nachrichten = nachrichtenAdapter.getArtikelList().get(position);
        Intent intent = new Intent(getActivity(), NachrichtenActivity.class);
        intent.putExtra("content", nachrichten.nachricht);
        intent.putExtra("titel", nachrichten.titel);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}
