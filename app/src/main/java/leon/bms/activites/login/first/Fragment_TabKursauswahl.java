package leon.bms.activites.login.first;

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

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import leon.bms.R;
import leon.bms.adapters.KursauswahlAdapter;
import leon.bms.model.kursauswahl;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbFach;
import leon.bms.realm.dbKurs;
import leon.bms.realm.dbSchulstunde;
import leon.bms.realm.dbUser;
import leon.bms.realm.dbWebsiteTag;


/**
 * @Fragment_TabKursauswahl zeigt die Kursauswahl in der Introduction Acitvity an. Ist für die Auswahl der
 * Kurs der User verantwortlich und speichert die Kurs sowie deren TAGs die nacher wichtig für die Website sind.
 */
public class Fragment_TabKursauswahl extends AbstractStep implements KursauswahlAdapter.ViewHolder.ClickListener {
    private int i = 1;
    //recylcer view for the Kurs
    RecyclerView recyclerView;
    List<kursauswahl> kurseList;
    String TAG = "Fragment_TabKursauswahl";
    private KursauswahlAdapter kursauswahlAdapter;
    private static boolean m_iAmVisible;
    int counter = 0;
    RealmQueries realmQueries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__tab_kursauswahl, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        realmQueries = new RealmQueries(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * @return gibt die soriterte Liste zurück
     * @sortierteList sortiert die Listen nach LK GK AG und PK und alphabetisch jeweils
     */
    public List<kursauswahl> sortierteListe() {
        List<kursauswahl> sortiereListe = new ArrayList<>();
        if (realmQueries.getAllFaecher() != null) {
            RealmResults<dbFach> fachList =realmQueries.getAllFaecher();
            fachList.sort("description", Sort.ASCENDING);
            for (dbFach fach : fachList) {
                if (realmQueries.getKurseFromFach(fach) != null) {
                    kursauswahl kurswahl = new kursauswahl();
                    kurswahl.headline = fach.getDescription();
                    kurswahl.headlineOrKurs = true;
                    sortiereListe.add(kurswahl);

                    List<dbKurs> kursList = realmQueries.getKurseFromFach(fach);
                    List<dbKurs> lklist = new ArrayList<>();
                    List<dbKurs> gklist = new ArrayList<>();
                    List<dbKurs> aglist = new ArrayList<>();
                    List<dbKurs> pklist = new ArrayList<>();

                    for (dbKurs kurs : kursList) {
                        switch (kurs.getKursart().getGloablId()) {
                            case 0:
                                lklist.add(kurs);
                                break;
                            case 1:
                                gklist.add(kurs);
                                break;
                            case 2:
                                aglist.add(kurs);
                                break;
                            case 3:
                                pklist.add(kurs);
                                break;
                        }
                    }

                    lklist = sortListASCKurs(lklist);
                    gklist = sortListASCKurs(gklist);
                    aglist = sortListASCKurs(aglist);
                    pklist = sortListASCKurs(pklist);

                    kursList = lklist;
                    kursList.addAll(gklist);
                    kursList.addAll(aglist);
                    kursList.addAll(pklist);

                    for (dbKurs kurs : kursList) {
                        kursauswahl kurswahl2 = new kursauswahl();
                        kurswahl2.kurs = kurs;
                        kurswahl2.headlineOrKurs = false;
                        sortiereListe.add(kurswahl2);
                    }
                }


            }

        }


        // gibt die sortierteListe zurück
        return sortiereListe;

    }

    /**
     * @param isVisibleToUser gibt an ob das Fragment für den User sichtbar ist oder nicht
     * @setUserVisbileHint erstellt den recylcerview und zeigt die Kurse an. Anzeige geschieht aber
     * nur wenn das Fragment wirklich für den User sichtbar ist.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            // wenn sichtbar
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            // kurseList wird durch die Methode sortierteListe() mit allen verfügbaren Kursen gefüllt
            if (sortierteListe() != null && sortierteListe().size() != 0) {
                kurseList = sortierteListe();

                // Adapter bekommt die Kurseliste für die Anzeige der Kurse
                kursauswahlAdapter = new KursauswahlAdapter(this, kurseList);
                // setUp RecyclerView
                recyclerView.setAdapter(kursauswahlAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                Log.d(TAG, "this fragment is now visible");
            }


        } else {
            Log.d(TAG, "this fragment is now invisible");
        }
    }

    /**
     * @sortiereListASC sortiert die Liste nach ihrem Name von A bis Z durch
     */
    public List<dbKurs> sortListASCKurs(List<dbKurs> list) {
        Collections.sort(list, new Comparator<dbKurs>() {
            @Override
            public int compare(dbKurs lhs, dbKurs rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        return list;
    }


    /**
     * @return "Kursauswahl"
     * @name gibt den Name des Fragments an
     */
    @Override
    public String name() {
        return "Kursauswahl";
    }

    /**
     * Methode wird aufgerufen wenn der User die Kursauswahl beendet
     * Hier wird geguckt ob die Kombination der Kurse möglich ist .
     * Wenn die Kombination möglich ist werden die Kurs als aktiv und je nachdem auch als schriftlich
     * gespeichert. Danach werden noch zu den Kursen die Website TAGs gespeichert.
     * Das Attribut validData des User wird jetzt auf true gesetzt, da der User alle Schritte der Konfiguration
     * durchlaufen hat. Danach wird noch das Fragment_TabDanke angezeigt
     */
    public void setUpKurse() {
        Log.d(TAG, "NEXT IF TRIGGGER __________________");
        //prüft ob die Möglichkeit der Kurse möglich ist
        //fragt den User ob er fertig ist mit seiner Auswahl
        //speichert die Einstellung und geht zur MainActivity
        final List<dbKurs> alleKurse = new ArrayList<>();
        alleKurse.addAll(kursauswahlAdapter.getSchriftlichList());
        alleKurse.addAll(kursauswahlAdapter.getMündlichList());

        // kontrolliert ob die Kombination möglich ist
        if (checkKurseKombination(alleKurse) == true) {
            // all schriftliche Kurse werden gespeichert

            // TODO Website Tag Methode
            for (final dbKurs kurs : kursauswahlAdapter.getSchriftlichList()) {
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity()).build();
                Realm.setDefaultConfiguration(realmConfig);
                // Get a Realm instance for this thread
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgrealm) {
                        kurs.setAktiv(true);
                        kurs.setSchriftlich(true);
               /* if (new dbKursTagConnect().getTags(kurs.getId()) != null) {
                    List<dbWebsiteTag> websiteTags = new dbKursTagConnect().getTags(kurs.getId());
                    for (int i = 0; i < websiteTags.size(); i++) {
                        //website TAGs werden gespeichert
                        dbWebsiteTag websiteTag = websiteTags.get(i);
                        websiteTag.vorkommen++;
                        websiteTag.save();
                    }
                }*/
                        bgrealm.copyToRealmOrUpdate(kurs);
                        Log.d("Fragment_Kursauswahl","Saved Object");
                    }
                });



            }

            //alle Mündlichen Kurse werden gespeichert
            Log.d(TAG, kursauswahlAdapter.getMündlichList().size() + "  SIZE MÜNDLICH");
            for (final dbKurs kurs : kursauswahlAdapter.getMündlichList()) {
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity()).build();
                Realm.setDefaultConfiguration(realmConfig);
                // Get a Realm instance for this thread
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgrealm) {
                        kurs.setAktiv(true);
                        kurs.setSchriftlich(false);
               /* if (new dbKursTagConnect().getTags(kurs.getId()) != null) {
                    List<dbWebsiteTag> websiteTags = new dbKursTagConnect().getTags(kurs.getId());
                    for (int i = 0; i < websiteTags.size(); i++) {
                        //website TAGs werden gespeichert
                        dbWebsiteTag websiteTag = websiteTags.get(i);
                        websiteTag.vorkommen++;
                        websiteTag.save();
                    }
                }*/
                        bgrealm.copyToRealmOrUpdate(kurs);
                        Log.d("Fragment_Kursauswahl","Saved Object");
                    }
                });
            }
            alleKurse.clear();

            final dbUser user = realmQueries.getUser();
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(getActivity()).build();
            Realm.setDefaultConfiguration(realmConfig);
            // Get a Realm instance for this thread
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {
                    user.setValidData(true);

                    bgrealm.copyToRealmOrUpdate(user);
                    Log.d("Fragment_Kursauswahl","Saved Object");
                }
            });

            i++;

        }
    }


    /**
     * @return
     * @nextIF wird aufgerufen wenn der User auf den "Continue" Button clickt. Je nachdem ob i > 1 ist
     * wird das nächste Fragment geladen
     */
    @Override
    public boolean nextIf() {
        if (counter == 0) {
            setUpKurse();
        }
        return i > 1;
    }

    /**
     * @error wird aufgerufen wenn die Kombination der Kurs nicht möglich ist
     */
    @Override
    public String error() {
        return "<b>Kursauswahl fehlgeschlagen! </b>";
    }

    /**
     * Methode wird aufegrufen wenn ein Item geclicked wird
     * prüft ob das Item schon makiert (selected) ist
     * wenn ja dann wird es wird entmarkeirt
     * wenn es ein GK ist fragt es den User ob schriftlich oder mündlich
     * speichert die Eingabe in der entsprechenden Liste
     */
    @Override
    public void onItemClicked(final int position) {
        if (kurseList.get(position).headlineOrKurs == false) {

            /**  überprüft ob der Kurs schon ausgewählt worden ist
             *      wenn ja dann wird er makiert und in die entsprechende Liste eingespeichert
             *      wenn nein wird die auswahl aufgehoben
             */
            if (kursauswahlAdapter.isSelected(position) != true) {
                // überprüft ob das Fach ein GK ist
                // bei LK wird es automatisch auf schriftlich gesetzt
                // bei PK und AG wird es automatisch auf mündlich gesetzt
                if (kurseList.get(position).kurs.getKursart().getGloablId() == 1) {
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
                } else if (kurseList.get(position).kurs.getKursart().getGloablId() == 0) {
                    kursauswahlAdapter.switchMS(position, true);
                } else if (kurseList.get(position).kurs.getKursart().getGloablId() > 1) {
                    kursauswahlAdapter.switchMS(position, false);
                }
            } else {
                kursauswahlAdapter.removeMS(position);
            }
            // toggleSelection ändert die Auswahl in der Anzeige
            toggleSelection(position);
        }
    }

    private void toggleSelection(int position) {
        // ändert die Makierung
        kursauswahlAdapter.toggleSelection(position);
        // count ist die Anzahl der ausgewählten Kurse
        int count = kursauswahlAdapter.getSelectedItemCount();

    }

    // Methode wird aufegrufen wenn ein Item "lange" geclicked wird
    // prüft ob das Item schon makiert (selected) ist
    // wenn ja dann wird es wird entmarkeirt
    // wenn eni fragt es den User ob schriftlich oder mündlich
    // speichert die Eingabe in der entsprechenden Liste
    @Override
    public boolean onItemLongClicked(final int position) {
        if (kurseList.get(position).headlineOrKurs == false) {

            /**  überprüft ob der Kurs schon ausgewählt worden ist
             *      wenn ja dann wird er makiert und in die entsprechende Liste eingespeichert
             *      wenn nein wird die auswahl aufgehoben
             */
            if (kursauswahlAdapter.isSelected(position) != true) {
                // überprüft ob das Fach ein GK ist
                // bei LK wird es automatisch auf schriftlich gesetzt
                // bei PK und AG wird es automatisch auf mündlich gesetzt
                if (kurseList.get(position).kurs.getKursart().getGloablId()== 1) {
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
                } else if (kurseList.get(position).kurs.getKursart().getGloablId()== 0) {
                    kursauswahlAdapter.switchMS(position, true);
                } else if (kurseList.get(position).kurs.getKursart().getGloablId() > 1) {
                    kursauswahlAdapter.switchMS(position, false);
                }
            } else {
                kursauswahlAdapter.removeMS(position);
            }
            // toggleSelection ändert die Auswahl in der Anzeige
            toggleSelection(position);
        }
        return false;
    }


    /**
     * @param kursList ist die Liste der ausgewählten Kurse
     * @checkKurseKombination kontrolliert ob die Kombination der Kurse möglich ist
     * dazu überprüft er ob die Schulstunden Zeiten der LK oder GK sich überschneiden
     * Da die PKs und AGs kein Zeiten beinhalten müssen diese nicht kontrolliert werden
     */
    public Boolean checkKurseKombination(List<dbKurs> kursList) {
        List<dbKurs> lkList = new ArrayList<>();
        List<dbKurs> gkList = new ArrayList<>();
        List<dbKurs> pkList = new ArrayList<>();
        List<dbKurs> agList = new ArrayList<>();

        // Liste wird in die einzelnen Kursarten unterteilt
        for (int i = 0; i < kursList.size(); i++) {
            switch (kursList.get(i).getKursart().getGloablId()) {
                case 0:
                    lkList.add(kursList.get(i));
                    break;
                case 1:
                    gkList.add(kursList.get(i));
                    break;
                case 2:
                    agList.add(kursList.get(i));
                    break;
                case 3:
                    pkList.add(kursList.get(i));
                    break;
                default:
                    break;
            }
        }
        /** Überprüft die LK : man MUSS 2 LKS haben und die Schulstunden dürfen nicht gleich
         *  sein
         */


        if (realmQueries.getUser().getGrade_string().equals("Q2") || realmQueries.getUser().getGrade_string().equals("Q1")) {
            if (lkList.size() == 2) {
                List<dbSchulstunde> stundenList = realmQueries.getStunden(lkList.get(0));
                List<dbSchulstunde> stundenList2 = realmQueries.getStunden(lkList.get(1));

                if (stundenList.get(0).getDay() == stundenList2.get(0).getDay() && stundenList.get(0).getLesson() == stundenList2.get(0).getLesson()) {
                    Log.d("CHECK", "LKs funktionieren nicht zusammen");
                    return false;
                }
            } else {
                Log.d("CHECK", "LKs sind zu wenige oder zu viele");
                return true;
            }
        }

        Log.d("SIZE", "GKLISTE: " + gkList.size());
        List<dbSchulstunde> stundenList = new ArrayList<>();
        List<dbSchulstunde> stundenList2 = new ArrayList<>();
        /** stundenList beinhaltet jeweils die Schulstunden der GK Liste
         *  stundenList2 beihaltet immer die Schulstunden der anderen Kursen
         *  wenn die Zeiten und Wochentage der schulstunden unterschiedlich sind wird
         *      true zurüclgegeben / wenn nicht dann false
         */
        for (int i = 0; i < gkList.size() - 1; i++) {
            stundenList = realmQueries.getStunden(gkList.get(i));
            dbSchulstunde schulstunde = stundenList.get(0);

            for (int k = i + 1; k < gkList.size(); k++) {
                stundenList2 =realmQueries.getStunden(gkList.get(k));
                dbSchulstunde schulstunde2 = stundenList2.get(0);

                if (schulstunde2.getDay() == schulstunde.getDay() && schulstunde.getLesson() == schulstunde2.getLesson()) {
                    Log.d("CHECK", "Kurse gheen nicht zusammen "
                            + schulstunde.getKursID() + " " + schulstunde2.getKursID() + " "
                            + schulstunde2.getDay() + " " + schulstunde.getDay() + " "
                            + schulstunde.getLesson() + " " + schulstunde2.getLesson());
                    return false;
                }
            }
        }

        return true;
    }
}
