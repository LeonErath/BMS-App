package leon.bms.activites.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

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

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import leon.bms.Constants;
import leon.bms.NachrichtenActivity;
import leon.bms.R;
import leon.bms.adapters.NachrichtenAdapter;
import leon.bms.atOnline;
import leon.bms.controller.KlausurController;
import leon.bms.controller.LogInController;
import leon.bms.controller.NachrichtenController;
import leon.bms.database.dbKurs;
import leon.bms.database.dbSchulstunde;
import leon.bms.database.dbUser;
import leon.bms.model.nachrichten;
import leon.bms.model.stunden;


/**
 * @Fragment_Highlight ist ein Fragment , welches kompakt alles wichtiges anzeigen soll
 * Es soll die aktuelle Stunde sowie wichtige Neuigkeiten und noch zu machen Hausaufgaben anzeigen
 */
public class Fragment_Highlight extends Fragment implements NachrichtenAdapter.ViewHolder.ClickListener, FragmentLifecycle {

    // views
    TextView textViewLehrer, textViewStunde, textViewRaum;
    UltimateRecyclerView recyclerView;
    CardView cardView;
    List<nachrichten> nachrichtenList = new ArrayList<>();
    NachrichtenAdapter nachrichtenAdapter;
    ImageView imageViewHeader;
    NachrichtenController nachrichtenController;

    private static String TAG = Fragment_Highlight.class.getSimpleName();
    // wichtig für das aktualiseren der Anzeige
    BroadcastReceiver broadcastReceiver;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__highlight, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //initial views
        textViewLehrer = (TextView) view.findViewById(R.id.textViewLehrer);
        textViewStunde = (TextView) view.findViewById(R.id.textViewStunde);
        textViewRaum = (TextView) view.findViewById(R.id.textViewRaum);
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycler_view);
        cardView = (CardView) view.findViewById(R.id.cardView);
        imageViewHeader = (ImageView) view.findViewById(R.id.imageViewHeader);
        final ViewPager vp= (ViewPager) getActivity().findViewById(R.id.viewpager);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              vp.setCurrentItem(1);
            }
        });

        setUpRecylcerView();


        setUpNächsteStunde();

        nachrichtenController = new NachrichtenController(getActivity());
        nachrichtenController.setUpdateListener(new NachrichtenController.OnUpdateListener() {
            @Override
            public void onSuccesss(List<nachrichten> nachrichtenList) {
                if (nachrichtenList != null && nachrichtenList.size()>0){
                    nachrichtenAdapter.changeDataSet(nachrichtenList);
                }
            }

            @Override
            public void onFailure() {

            }
        });
        if (nachrichtenAdapter != null && nachrichtenAdapter.getAdapterItemCount() == 0) {
            nachrichtenController.checkUpdate();
        }


    }

    private void setUpRecylcerView() {
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        nachrichtenAdapter = new NachrichtenAdapter(this, nachrichtenList);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(nachrichtenAdapter);
        LandingAnimator animator = new LandingAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerView.setItemAnimator(animator);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nachrichtenController.checkUpdate();
                        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                        recyclerView.setRefreshing(false);
                        //   ultimateRecyclerView.scrollBy(0, -50);
                        mLayoutManager.scrollToPosition(0);
                    }
                }, 1000);
            }
        });
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
                List<dbKurs> aktiveKurse = new dbKurs().getAllActiveKurse();

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
                                    textViewStunde.setText(stunden.getSchulstunde().kurs.name);
                                    textViewRaum.setText(stunden.getSchulstunde().raum.nummer);
                                    textViewLehrer.setText(stunden.getSchulstunde().lehrer.titel + " " + stunden.getSchulstunde().lehrer.name);
                                } else {
                                    // sonst Freistunde
                                    textViewStunde.setText("Freistunde");
                                    textViewRaum.setText("");
                                    textViewLehrer.setText("");
                                }

                            } else {
                                // wenn die aktuelle Stunde die letze Stunde ist
                                textViewStunde.setText(stunden.getSchulstunde().kurs.name);
                                textViewRaum.setText(stunden.getSchulstunde().raum.nummer);
                                textViewLehrer.setText(stunden.getSchulstunde().lehrer.titel + " " + stunden.getSchulstunde().lehrer.name);


                            }
                        }
                    }

                }
            } else if (aktuelleStunde == 99) {
                // wenn es nachdem Unterrich ist
                textViewStunde.setText("Kein Unterricht mehr!");
                textViewRaum.setText("");
                textViewLehrer.setText("Denk an deine Hausaufgaben.");

            } else if (aktuelleStunde == 0) {
                // wenn es vor dem Unterricht ist
                textViewStunde.setText("Kein Unterricht!");
                textViewRaum.setText("");
                textViewLehrer.setText("Denk an deine Hausaufgaben.");


            }
        } else if (week == 6) {
            textViewStunde.setText("Heute ist Samstag ");
            textViewRaum.setText("");
            textViewLehrer.setText("Unterrichtsfrei");

        } else if (week == 0) {
            textViewStunde.setText("Heute ist Sonntag ");
            textViewRaum.setText("");
            textViewLehrer.setText("Unterrichtsfrei");
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
                    stunden.setSchulstunde(schulstunde);
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

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
