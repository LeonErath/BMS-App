package leon.bms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Leon E on 18.11.2015.
 */

/** @Fragment_Stundenplan ist ein Fragment, welches den Stundenplan anzeigt. Dazu verfügt
 *  das Fragment über mehrer Methode zum erstellen des Stundenplannes. Der Stundenplan wird in
 *  einem RecyclerView angezeigt und immer jeweils für einen Wochentag erstellt. Der User kann
 *  den Wochentag durch einen Spinner wechseln.
 */
public class Fragment_Stundenplan extends Fragment implements StundenplanAdapter.ViewHolder.ClickListener{


    Spinner spinner;
    RecyclerView recyclerView;
    StundenplanAdapter stundenplanAdapter;
    // Listen in den die einzelnen Stundenpläne für den Wochentag erstellt werden
    List<dbSchulstunde> montagList = new ArrayList<>();
    List<dbSchulstunde> dienstagList = new ArrayList<>();
    List<dbSchulstunde> mittwochList = new ArrayList<>();
    List<dbSchulstunde> donnerstagList = new ArrayList<>();
    List<dbSchulstunde> freitagList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stundenplan,container,false);
    }
    /** @OnViewCreated hier wird Stundenplan zusammengestellt und an den recyclerView übergegeben
     *  damit er den darstellen kann.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // Methode zum erstllen des Stundenplanes
        setUpStundenplan();
        // Methode zum erstellen des Recyclerview
        setUpRecyclerView(view);

        // Liste sind jetzt befüllt mit den Stunden
        final List<dbSchulstunde> finalMontagList = montagList;
        final List<dbSchulstunde> finalDienstagList = dienstagList;
        final List<dbSchulstunde> finalMittwochList = mittwochList;
        final List<dbSchulstunde> finalDonnerstagList = donnerstagList;
        final List<dbSchulstunde> finalFreitagList = freitagList;

        // @setOnItemSelectedListener wird ausgelöst wenn der User einen anderen Wochentag auswählen will
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // ändert die Anzeige über den stundenplanAdapter nach dem ausgewählten Wochentag
            switch (position){
                case 0:stundenplanAdapter.changeWeekDay(convertSchulstundenZuStundeListe(finalMontagList));
                    break;
                case 1:stundenplanAdapter.changeWeekDay(convertSchulstundenZuStundeListe(finalDienstagList));
                    break;
                case 2:stundenplanAdapter.changeWeekDay(convertSchulstundenZuStundeListe(finalMittwochList));
                    break;
                case 3:stundenplanAdapter.changeWeekDay(convertSchulstundenZuStundeListe(finalDonnerstagList));
                    break;
                case 4:stundenplanAdapter.changeWeekDay(convertSchulstundenZuStundeListe(finalFreitagList));
                    break;
                default:break;

            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    });

    }

    /** @setUpRecyclerView erstellt den RecyclerView und erstellt die Anzeige des Stundenplans
     *  wie er vorher in setUpStundenplan() erstellt worden ist
     */
    public void setUpRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(stundenplanAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /** @setUpStundenplan erstellt die Stundenpläne für die verschiedenen Wochentage und stellt den
     *  stundenplanAdapter , der für die Anzeige des Stundenplanes zuständig ist , den heutigen
     *  Wochentag ein.
     */
    public void setUpStundenplan(){

        // zuerst werden alle GK und LK Kurse in eine Liste gespeichert
        List<dbKurs> kursList = new dbKurs().getActiveKurse(0);
        kursList.addAll(new dbKurs().getActiveKurse(1));

        // alle Schulstunden der Kurse werden in die Liste dbSchulstundeList geladen
        List<dbSchulstunde> dbSchulstundeList = new ArrayList<>();
        for (dbKurs kurs:kursList){
            dbSchulstundeList.addAll(kurs.getSchulStunden(kurs.getId()));

        }

        // Wochentagslisten werden initialisiert
        montagList = new ArrayList<>();
        dienstagList = new ArrayList<>();
        mittwochList = new ArrayList<>();
        donnerstagList = new ArrayList<>();
        freitagList = new ArrayList<>();

        // jeder Schulstunde wird in nachdem Wochentag in die jeweilige Wochentagsliste sortiert
        for (dbSchulstunde schulstunde:dbSchulstundeList){
            switch (schulstunde.wochentag){
                case 1: montagList.add(schulstunde);
                    break;
                case 2: dienstagList.add(schulstunde);
                    break;
                case 3: mittwochList.add(schulstunde);
                    break;
                case 4: donnerstagList.add(schulstunde);
                    break;
                case 5: freitagList.add(schulstunde);
                    break;
                default:
                    break;
            }
        }

        // alle Wochentagslisten werden Stunden mäßig sortiert
        montagList = sortListASC(montagList);
        dienstagList = sortListASC(dienstagList);
        mittwochList = sortListASC(mittwochList);
        donnerstagList = sortListASC(donnerstagList);
        freitagList = sortListASC(freitagList);


        // aktueller Wochentag wird ausgerechnet
        Calendar calendar = Calendar.getInstance();
        //Sunday= 1 Monday = 2 ...
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day > 1 && day < 7){
            spinner.setSelection(day-2);
        }else {
            spinner.setSelection(0);
        }
        /** Je nachdem Wochentag wird der Adapter auf diesen eingestellt
         *  die sortierten Wochentagslisten werden zu Stundenplan listern convertiert , sodass in
         *  der Liste auch Freistunden vorhanden sind und nicht nur die Stunden die man hat. Dazu
         *  wird die Methode converSchulstundenZuStundeListe() benutzt
         */
        switch (day){
            case 1:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(montagList));break;
            case 2:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(montagList));break;
            case 3:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(dienstagList));break;
            case 4:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(mittwochList)); break;
            case 5: stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(donnerstagList));break;
            case 6:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(freitagList)); break;
            case 7:stundenplanAdapter = new StundenplanAdapter(this,convertSchulstundenZuStundeListe(montagList));break;
            default:break;
        }

    }
    /** @sortListASC sortiert die Übergegebene Schulstunde-Liste nach den Stunden
     */
    public List<dbSchulstunde> sortListASC(List<dbSchulstunde> list){
        Collections.sort(list, new Comparator<dbSchulstunde>() {
            @Override
            public int compare(dbSchulstunde lhs, dbSchulstunde rhs) {
                return lhs.getBeginnzeit().compareTo(rhs.getBeginnzeit());
            }
        });
        return list;
    }

    /** @convertSchulstundenZuStundeListe guckt wann die letzte Stunden ist und geht dann durch
     *  ob zu jeder Stunde eine Schulstunde vorhanden ist
     *      wenn nicht erstellt er eine Freistunde
     */
    public List<stunden> convertSchulstundenZuStundeListe(List<dbSchulstunde> wochentagListe){
        // letzteStunde ist die letzte Schulstunde der WochentagListe
        if (wochentagListe.size()>0) {
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
        } else{
            return new ArrayList<>();
        }
    }
    /** Implementation des ClickListener um Aktionen bei einem Click auszuführen
     */
    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}
