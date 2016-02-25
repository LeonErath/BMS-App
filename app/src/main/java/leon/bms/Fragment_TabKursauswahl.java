package leon.bms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.franciscan.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Fragment_TabKursauswahl extends AbstractStep implements KursauswahlAdapter.ViewHolder.ClickListener {
     private int i = 1;
    RecyclerView recyclerView;
    List<dbKurs> kurseList;
    String TAG = "Fragment_TabKursauswahl";
    private KursauswahlAdapter kursauswahlAdapter;
    private static boolean m_iAmVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment__tab_kursauswahl, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public List<dbKurs> sortierteListe(){
        List<dbKurs> sortiereListe = new ArrayList<>();

        if (new dbKurs().kursartListe(0).size() > 0){
        List<dbKurs> lkList = sortListASC(new dbKurs().kursartListe(0));
        Log.d(TAG, lkList.size()+"");
            sortiereListe.addAll(lkList);

        }
        if (new dbKurs().kursartListe(1).size() > 0){
        List<dbKurs> gkList =  sortListASC(new dbKurs().kursartListe(1));
        Log.d(TAG, gkList.size()+"");
            sortiereListe.addAll(gkList);

        }
        if (new dbKurs().kursartListe(2).size() > 0){
        List<dbKurs> pkList =  sortListASC(new dbKurs().kursartListe(2));
        Log.d(TAG, pkList.size()+"");
            sortiereListe.addAll(pkList);

        }
        if (new dbKurs().kursartListe(3).size() > 0){
        List<dbKurs> agList =  sortListASC(new dbKurs().kursartListe(3));
        Log.d(TAG, agList.size()+"");
            sortiereListe.addAll(agList);

        }



        // gibt die sortierteListe zurück
        return sortiereListe;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            // kurseList wird durch die Methode sortierteListe() mit allen verfügbaren Kursen gefüllt
            kurseList = sortierteListe();
            // Adapter bekommt die Kurseliste für die Anzeige der Kurse
            kursauswahlAdapter = new KursauswahlAdapter(this,kurseList);
            // setUp RecyclerView
            recyclerView.setAdapter(kursauswahlAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            Log.d(TAG, "this fragment is now visible");

        } else {
            Log.d(TAG, "this fragment is now invisible");
        }
    }

    /** @sortiereListASC sortiert die Liste nach ihrem Name von A bis Z durch
     */
    public List<dbKurs> sortListASC(List<dbKurs> list){
        Collections.sort(list, new Comparator<dbKurs>() {
            @Override
            public int compare(dbKurs lhs, dbKurs rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return list;
    }
    @Override
    public String name() {
        return "Kursauswahl";
    }

    @Override
    public boolean nextIf() {

        //prüft ob die Möglichkeit der Kurse möglich ist
        //fragt den User ob er fertig ist mit seiner Auswahl
        //speichert die Einstellung und geht zur MainActivity
        final List<dbKurs> alleKurse = new ArrayList<>();
        alleKurse.addAll(kursauswahlAdapter.getSchriftlichList());
        alleKurse.addAll(kursauswahlAdapter.getMündlichList());

        // kontrolliert ob die Kombination möglich ist
        if (checkKurseKombination(alleKurse)==true){
            // wenn ja wird final gefragt ob der User wirklich fertig ist
            for (dbKurs kurs : kursauswahlAdapter.getSchriftlichList()) {
                kurs.aktiv = true;
                kurs.schriftlich = true;
                kurs.save();
            }
            for (dbKurs kurs : kursauswahlAdapter.getMündlichList()) {
                kurs.aktiv = true;
                kurs.schriftlich = false;
                kurs.save();
            }
            alleKurse.clear();

            dbUser user = new dbUser().getUser();
            user.validData = true;
            user.save();

            i++;

        }
        return i>1;
    }

    @Override
    public String error() {
        return "<b>Kursauswahl fehlgeschlagen! </b>";
    }

    @Override
    public void onItemClicked(final int position) {
        /**  überprüft ob der Kurs schon ausgewählt worden ist
         *      wenn ja dann wird er makiert und in die entsprechende Liste eingespeichert
         *      wenn nein wird die auswahl aufgehoben
         */
        if (kursauswahlAdapter.isSelected(position)!= true) {
            // überprüft ob das Fach ein GK ist
            // bei LK wird es automatisch auf schriftlich gesetzt
            // bei PK und AG wird es automatisch auf mündlich gesetzt
            if (kurseList.get(position).kursart == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Kurswahl")
                        .setCancelable(false)
                        .setMessage("Wähle ob du den Kurs schriftlich oder mündlich hast.")
                        .setPositiveButton("schriftlich", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                kursauswahlAdapter.switchMS(position, true);
                            }
                        })
                        .setNegativeButton("mündlich", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                kursauswahlAdapter.switchMS(position, false);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (kurseList.get(position).kursart == 0){
                kursauswahlAdapter.switchMS(position,true);
            } else if (kurseList.get(position).kursart > 1){
                kursauswahlAdapter.switchMS(position,false);
            }
        }else {
            kursauswahlAdapter.removeMS(position);
        }
        // toggleSelection ändert die Auswahl in der Anzeige
        toggleSelection(position);
    }
    private void toggleSelection(int position) {
        // ändert die Makierung
        kursauswahlAdapter.toggleSelection(position);
        // count ist die Anzahl der ausgewählten Kurse
        int count = kursauswahlAdapter.getSelectedItemCount();

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }


    /** @checkKurseKombination kontrolliert ob die Kombination der Kurse möglich ist
     *  dazu überprüft er ob die Schulstunden Zeiten der LK oder GK sich überschneiden
     *  Da die PKs und AGs kein Zeiten beinhalten müssen diese nicht kontrolliert werden
     *  @param kursList ist die Liste der ausgewählten Kurse
     */
    public Boolean checkKurseKombination(List<dbKurs> kursList){
        List<dbKurs> lkList = new ArrayList<>();
        List<dbKurs> gkList =  new ArrayList<>();
        List<dbKurs> pkList =  new ArrayList<>();
        List<dbKurs> agList =  new ArrayList<>();

        // Liste wird in die einzelnen Kursarten unterteilt
        for (int i=0;i<kursList.size();i++) {
            switch (kursList.get(i).getKursart()){
                case 0: lkList.add(kursList.get(i));break;
                case 1: gkList.add(kursList.get(i));break;
                case 2: agList.add(kursList.get(i));break;
                case 3: pkList.add(kursList.get(i));break;
                default:break;
            }
        }
        /** Überprüft die LK : man MUSS 2 LKS haben und die Schulstunden dürfen nicht gleich
         *  sein
         */
        if (new dbUser().getUser().stufe.equals("Q2")||new dbUser().getUser().stufe.equals("Q1")){
            if (lkList.size()==2){
                List<dbSchulstunde>stundenList = lkList.get(0).getSchulStunden(lkList.get(0).getId());
                List<dbSchulstunde>stundenList2 = lkList.get(1).getSchulStunden(lkList.get(1).getId());

                if (stundenList.get(0).wochentag == stundenList2.get(0).wochentag && stundenList.get(0).beginnzeit == stundenList2.get(0).beginnzeit){
                    Log.d("CHECK", "LKs funktionieren nicht zusammen");
                    return false;
                }
            }else {
            Log.d("CHECK","LKs sind zu wenige oder zu viele");
            return false;
            }
        }

        Log.d("SIZE","GKLISTE: "+gkList.size());
        List<dbSchulstunde>stundenList = new ArrayList<>();
        List<dbSchulstunde>stundenList2 = new ArrayList<>();
        /** stundenList beinhaltet jeweils die Schulstunden der GK Liste
         *  stundenList2 beihaltet immer die Schulstunden der anderen Kursen
         *  wenn die Zeiten und Wochentage der schulstunden unterschiedlich sind wird
         *      true zurüclgegeben / wenn nicht dann false
         */
        for (int i=0; i<gkList.size()-1;i++){
            stundenList = gkList.get(i).getSchulStunden(gkList.get(i).getId());
            dbSchulstunde schulstunde = stundenList.get(0);

            for (int k=i+1; k<gkList.size();k++){
                stundenList2 = gkList.get(k).getSchulStunden(gkList.get(k).getId());
                dbSchulstunde schulstunde2 = stundenList2.get(0);

                if (schulstunde2.wochentag == schulstunde.wochentag && schulstunde.beginnzeit == schulstunde2.beginnzeit){
                    Log.d("CHECK","Kurse gheen nicht zusammen "+schulstunde.kursID+" "+schulstunde2.kursID+" "+schulstunde2.wochentag+" "+schulstunde.wochentag+" "+schulstunde.beginnzeit +" "+ schulstunde2.beginnzeit);
                    return false;
                }
            }
        }

        return true;
    }
}
