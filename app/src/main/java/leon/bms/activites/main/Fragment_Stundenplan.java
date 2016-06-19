package leon.bms.activites.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import leon.bms.R;
import leon.bms.activites.kurs.KursActivity;
import leon.bms.adapters.StundenplanAdapter;
import leon.bms.controller.VertretungsplanController;
import leon.bms.model.stunden;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbKurs;
import leon.bms.realm.dbSchulstunde;

/**
 * Created by Leon E on 18.11.2015.
 */

/**
 * @Fragment_Stundenplan ist ein Fragment, welches den Stundenplan anzeigt. Dazu verfügt
 * das Fragment über mehrer Methode zum erstellen des Stundenplannes. Der Stundenplan wird in
 * einem RecyclerView angezeigt und immer jeweils für einen Wochentag erstellt. Der User kann
 * den Wochentag durch einen Spinner wechseln.
 */
public class Fragment_Stundenplan extends Fragment implements StundenplanAdapter.ViewHolder.ClickListener, FragmentLifecycle {


    Spinner spinner;
    UltimateRecyclerView recyclerView;
    StundenplanAdapter stundenplanAdapter;
    VertretungsplanController vertretungsplanController;
    // Listen in den die einzelnen Stundenpläne für den Wochentag erstellt werden
    List<stunden> montag = new ArrayList<>();
    List<stunden> dienstag = new ArrayList<>();
    List<stunden> mittwoch = new ArrayList<>();
    List<stunden> freitag = new ArrayList<>();
    List<stunden> donnerstag = new ArrayList<>();
    RealmQueries realmQueries;

    ToggleSwitch toggleSwitch;
    TextView textViewDatum, textViewVertretungDatum, textViewStunden;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Boolean success = bundle.getBoolean("myKey");
            if (success) {
                aktualisiereDatum();
                setStundenplan(toggleSwitch.getCheckedTogglePosition());
            }
        }
    };

    public Fragment_Stundenplan() {
        //empty Constructor needed!
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stundenplan, container, false);
    }

    /**
     * @OnViewCreated hier wird Stundenplan zusammengestellt und an den recyclerView übergegeben
     * damit er den darstellen kann.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realmQueries = new RealmQueries(getActivity());
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recyclerview);
        toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toggleButton);

        textViewDatum = (TextView) view.findViewById(R.id.textViewDatum);
        textViewVertretungDatum = (TextView) view.findViewById(R.id.textViewVertretungDatum);
        textViewStunden = (TextView) view.findViewById(R.id.textViewStundenAnzahl);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Mo");
        labels.add("Di");
        labels.add("Mi");
        labels.add("Do");
        labels.add("Fr");
        toggleSwitch.setLabels(labels);

        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position) {
                setStundenplan(position);
                setUpDatum(position);
            }
        });
        toggleSwitch.setCheckedTogglePosition(getWochentag());
        // Methode zum erstllen des Stundenplanes
        //setUpStundenplan();
        setUpDatum(getWochentag());

        vertretungsplanController = new VertretungsplanController(getActivity());
        vertretungsplanController.setUpdateListener(new VertretungsplanController.OnUpdateListener() {
            @Override
            public void onSuccesss() {
                doInBackground();
            }

            @Override
            public void onFailure() {

            }
        });


        // Methode zum erstellen des Recyclerview
        setUpRecyclerView();
        setStundenplan(toggleSwitch.getCheckedTogglePosition());

        if (stundenplanAdapter != null && stundenplanAdapter.getAdapterItemCount() == 0) {
            vertretungsplanController.checkUpdate();
        } else {

        }
    }

    private void setStundenplan(int day) {
        int letzteStunde;
        switch (day) {

            case 0:
                if (montag != null) {
                    letzteStunde = montag.size();
                    textViewStunden.setText(letzteStunde + " Stunden");
                    stundenplanAdapter.changeStundenplan(montag);
                }

                break;
            case 1:
                if (dienstag != null) {
                    letzteStunde = dienstag.size();
                    textViewStunden.setText(letzteStunde + " Stunden");
                    stundenplanAdapter.changeStundenplan(dienstag);
                }
                break;
            case 2:
                if (mittwoch != null) {
                    letzteStunde = mittwoch.size();
                    textViewStunden.setText(letzteStunde + " Stunden");
                    stundenplanAdapter.changeStundenplan(mittwoch);
                }
                break;
            case 3:
                if (donnerstag != null) {
                    letzteStunde = donnerstag.size();
                    textViewStunden.setText(letzteStunde + " Stunden");
                    stundenplanAdapter.changeStundenplan(donnerstag);
                }
                break;
            case 4:
                if (freitag != null) {
                    letzteStunde = freitag.size();
                    textViewStunden.setText(letzteStunde + " Stunden");
                    stundenplanAdapter.changeStundenplan(freitag);
                }
                break;
        }

    }

    public void setUpDatum(int day) {
        Calendar calendar = Calendar.getInstance();
        int math = day - getWochentag();
        calendar.set(Calendar.DAY_OF_WEEK, getWochentag() + 2 + math);

        SimpleDateFormat sdfmt = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy ");

        String datum = sdfmt.format(calendar.getTime());
        textViewDatum.setText("Vertretung für " + datum);

    }

    public int getWochentag() {
        // aktueller Wochentag wird ausgerechnet
        Calendar calendar = Calendar.getInstance();
        //Sunday= 1 Monday = 2 ...
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day > 1 && day < 7) {
            return day - 2;
        } else {
            return 0;
        }
    }

    /**
     * @setUpRecyclerView erstellt den RecyclerView und erstellt die Anzeige des Stundenplans
     * wie er vorher in setUpStundenplan() erstellt worden ist
     */
    public void setUpRecyclerView() {
        List<stunden> stundenList = new ArrayList<>();
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        stundenplanAdapter = new StundenplanAdapter(this, stundenList);
        recyclerView.setAdapter(stundenplanAdapter);
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
                        vertretungsplanController.checkUpdate();
                        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                        recyclerView.setRefreshing(false);
                        //   ultimateRecyclerView.scrollBy(0, -50);
                        mLayoutManager.scrollToPosition(0);
                    }
                }, 1000);
            }
        });
    }

    /**
     * @setUpStundenplan erstellt die Stundenpläne für die verschiedenen Wochentage und stellt den
     * stundenplanAdapter , der für die Anzeige des Stundenplanes zuständig ist , den heutigen
     * Wochentag ein.
     */

    private void doInBackground() {
        Runnable runnable = new Runnable() {
            public void run() {

                montag = setUpStundenplan(0);
                dienstag = setUpStundenplan(1);
                mittwoch = setUpStundenplan(2);
                donnerstag = setUpStundenplan(3);
                freitag = setUpStundenplan(4);

                Message msg = handler.obtainMessage();
                boolean sucess = true;
                Bundle bundle = new Bundle();
                bundle.putBoolean("myKey", sucess);
                msg.setData(bundle);
                handler.sendMessage(msg);

            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    public List<stunden> setUpStundenplan(int day) {
        return convertSchulstundenZuStundeListe(realmQueries.getAktiveStundenWithDay(day));
    }

    private void aktualisiereDatum() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdfmt = new SimpleDateFormat("dd.MMMM.yyyy,  kk:mm");

        String datum = sdfmt.format(calendar.getTime()) + " Uhr";
        textViewVertretungDatum.setText("letzte Aktualisierung: " + datum);

    }


    /**
     * @convertSchulstundenZuStundeListe guckt wann die letzte Stunden ist und geht dann durch
     * ob zu jeder Stunde eine Schulstunde vorhanden ist
     * wenn nicht erstellt er eine Freistunde
     */
    public List<stunden> convertSchulstundenZuStundeListe(List<dbSchulstunde> wochentagListe) {
        // letzteStunde ist die letzte Schulstunde der WochentagListe
        if (wochentagListe.size() > 0) {
            int letzteStunde = wochentagListe.get(wochentagListe.size() - 1).getLesson();
            // stundenplanListe ist nacher die fertige Liste mit alle Schulstunden und Freistunden
            List<stunden> convertedList = new ArrayList<>();
            // geht alle Stunden von 1 bis zu letzten Stunde durch

            for (int i = 1; i <= letzteStunde; i++) {
                stunden stunden = new stunden();
                int l = 0;
                // überprüft ob die Stunden vorhanden ist
                for (int k = 0; k < wochentagListe.size(); k++) {
                    if (wochentagListe.get(k).getLesson() == i) {
                        // wenn die wochentagsListe die Zeit i beinhaltet wir l addiert;
                        l = k + 1;
                    }
                }
                // wenn l größer als 0 ist die Stunden vorhanden
                // wenn l nicht größer aks 0 ist wird eine Freistunde erstellt
                if (l > 0) {
                    dbSchulstunde schulstunde = wochentagListe.get(l - 1);
                    stunden.setActive(true);
                    stunden.setSchulstunde(schulstunde);
                    if (realmQueries.getVertretungFromStunde(schulstunde) != null) {
                        stunden.setVertretung(realmQueries.getVertretungFromStunde(schulstunde));
                        Log.d("Vertreung", i + " Position + stunde:" + schulstunde.getKurs().getName());
                    } else {
                        stunden.vertretung = null;
                    }
                    stunden.setStunde(i);
                } else {
                    stunden.setStunde(i);
                    stunden.setActive(false);

                }
                convertedList.add(stunden);

            }
            // gibt die fertige stundenplanListe zurück;
            return convertedList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Implementation des ClickListener um Aktionen bei einem Click auszuführen
     */
    @Override
    public void onItemClicked(int position) {
        stunden stunde = stundenplanAdapter.getStundenplan().get(position);
        if (stunde.active == true) {
            Intent intent = new Intent(getActivity(), KursActivity.class);
            intent.putExtra("id", stunde.getSchulstunde().getKurs().getInt_id());
            startActivity(intent);
        }

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
