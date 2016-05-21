package leon.bms.activites.login.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import leon.bms.R;
import leon.bms.activites.main.MainActivity;
import leon.bms.adapters.KursauswahlAdapter;
import leon.bms.database.dbKurs;
import leon.bms.database.dbKursTagConnect;
import leon.bms.database.dbSchulstunde;
import leon.bms.database.dbUser;
import leon.bms.database.dbWebsiteTag;

/**
 * @KursauswahlActivity zeigt alle verfügbaren Kurse in einem RecyclerView an und lässt den User
 * seine Kurse auswählen. Der User bekommt alle Kurse zu seiner Stufe angezeigt. Ist die Kursauswahl
 * fertig wird überprüft ob diese Kombination der Kurse überhaupt möglich ist. Wenn alles korrekt ist
 * wird der User auf die MainActivity weitergeleitet.
 */
public class KursauswahlActivity extends AppCompatActivity implements KursauswahlAdapter.ViewHolder.ClickListener {
    // Tag für die Log-Datein
    private static final String TAG = KursauswahlActivity.class.getSimpleName();
    // kursauswahlAdapter für die Anzeige der Kurse
    private KursauswahlAdapter kursauswahlAdapter;
    /**
     * ActionModeCallback und ActionMode ist für die veränderun der Toolbar bei einer Auswahl der
     * der Kurse. Über die "neue" Toolbar kann der User sein Auswahl abschließen oder noch weiter
     * informationen zu den Auswahl bekommen.
     */
    private ActionModeCallback actionModeCallback = new ActionModeCallback(this);
    private ActionMode actionMode;
    // Liste in den nacher die Kurse gespeichert sind
    List<dbKurs> kurseList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kursauswahl);

        // kurseList wird durch die Methode sortierteListe() mit allen verfügbaren Kursen gefüllt
        kurseList = sortierteListe();
        Log.d(TAG, kurseList.size() + "");
        // Adapter bekommt die Kurseliste für die Anzeige der Kurse
        kursauswahlAdapter = new KursauswahlAdapter(this, kurseList);

        // setUp Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wähle deine Kurse!");

        // setUp RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(kursauswahlAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * @sortierteListe lässt sich die kurse der verschiedenen Kursarten zurückgeben und sortiert
     * diese nach ihren Namen sodass sie nacher bei der Anzeige sortiert angezeigt werden
     */
    public List<dbKurs> sortierteListe() {
        List<dbKurs> sortiereListe = new ArrayList<>();

        if (new dbKurs().kursartListe(0).size() > 0) {
            List<dbKurs> lkList = sortListASC(new dbKurs().kursartListe(0));
            Log.d(TAG, lkList.size() + "");
            sortiereListe.addAll(lkList);

        }
        if (new dbKurs().kursartListe(1).size() > 0) {
            List<dbKurs> gkList = sortListASC(new dbKurs().kursartListe(1));
            Log.d(TAG, gkList.size() + "");
            sortiereListe.addAll(gkList);

        }
        if (new dbKurs().kursartListe(2).size() > 0) {
            List<dbKurs> pkList = sortListASC(new dbKurs().kursartListe(2));
            Log.d(TAG, pkList.size() + "");
            sortiereListe.addAll(pkList);

        }
        if (new dbKurs().kursartListe(3).size() > 0) {
            List<dbKurs> agList = sortListASC(new dbKurs().kursartListe(3));
            Log.d(TAG, agList.size() + "");
            sortiereListe.addAll(agList);

        }


        // gibt die sortierteListe zurück
        return sortiereListe;

    }

    /**
     * @sortiereListASC sortiert die Liste nach ihrem Name von A bis Z durch
     */
    public List<dbKurs> sortListASC(List<dbKurs> list) {
        Collections.sort(list, new Comparator<dbKurs>() {
            @Override
            public int compare(dbKurs lhs, dbKurs rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        return list;
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
        /** übeprüft ob der ActionMode schon vorhanden ist sonst wird er gestartet
         */
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        /**  überprüft ob der Kurs schon ausgewählt worden ist
         *      wenn ja dann wird er makiert und in die entsprechende Liste eingespeichert
         *      wenn nein wird die auswahl aufgehoben
         */
        if (kursauswahlAdapter.isSelected(position) != true) {
            // überprüft ob das Fach ein GK ist
            // bei LK wird es automatisch auf schriftlich gesetzt
            // bei PK und AG wird es automatisch auf mündlich gesetzt
            if (kurseList.get(position).kursart.gloablId == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            } else if (kurseList.get(position).kursart.gloablId == 0) {
                kursauswahlAdapter.switchMS(position, true);
            } else if (kurseList.get(position).kursart.gloablId > 1) {
                kursauswahlAdapter.switchMS(position, false);
            }
        } else {
            kursauswahlAdapter.removeMS(position);
        }
        // toggleSelection ändert die Auswahl in der Anzeige
        toggleSelection(position);
    }

    // Methode wird aufegrufen wenn ein Item "lange" geclicked wird
    // prüft ob das Item schon makiert (selected) ist
    // wenn ja dann wird es wird entmarkeirt
    // wenn eni fragt es den User ob schriftlich oder mündlich
    // speichert die Eingabe in der entsprechenden Liste
    @Override
    public boolean onItemLongClicked(final int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        if (kursauswahlAdapter.isSelected(position) != true) {
            // überprüft ob das Fach ein GK ist
            // bei LK wird es automatisch auf schriftlich gesetzt
            // bei PK und AG wird es automatisch auf mündlich gesetzt
            if (kurseList.get(position).kursart.gloablId == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Make a Decision")
                        .setCancelable(false)
                        .setMessage("Wähle zwischen schriftlich und mündlich.")
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
            } else if (kurseList.get(position).kursart.gloablId == 0) {
                kursauswahlAdapter.switchMS(position, true);
            } else if (kurseList.get(position).kursart.gloablId > 1) {
                kursauswahlAdapter.switchMS(position, false);
            }
        } else {
            kursauswahlAdapter.removeMS(position);
        }
        toggleSelection(position);
        return true;
    }

    /**
     * Ändert die Makierung (Selection) in das entgegengesetze
     * <p/>
     * Wenn es das letzte Item in der Liste ist wird der actionMode beendet
     * Darf nur aufgerufen werden wenn der ActionMode nicht null ist
     *
     * @param position Position von dem Item
     */
    private void toggleSelection(int position) {
        // ändert die Makierung
        kursauswahlAdapter.toggleSelection(position);
        // count ist die Anzahl der ausgewählten Kurse
        int count = kursauswahlAdapter.getSelectedItemCount();

        // wenn kein Item mehr ausgewählt ist wird der actionMode beendet
        if (count == 0) {
            actionMode.finish();
        } else {
            // zählt im Titel mit wie viele Kurse ausgewählt worden sind
            actionMode.setTitle(String.valueOf(count) + " Selected");
            actionMode.invalidate();
        }
    }

    /**
     * @ActionModeCallback ist für die "neue" Toolbar zuständig sowie die Fertigstellung
     * der Kursauswahl wenn der User fertig ist.
     */
    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")

        private Context context;
        // Tag für die Log-Datein
        private final String TAG = ActionModeCallback.class.getSimpleName();

        public ActionModeCallback(Context context) {
            // Context für Anzeigen in der UI
            this.context = context;
        }

        // Hier wird das Menü erstellt selected_menu.xml
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // selected_menu ist ein spezielles Menü das angezeigt werden soll
            // findet sich unter res -> menu
            mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * @onACtionItemClicked wird ausgelöst wenn eins der Menü Items gecklickt wird
         * je nach Menü Item wird entweder
         * die Auswahl fertig gestellt
         * Informationen zur Kursauswahl gegeben
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_finnish:
                    //prüft ob die Möglichkeit der Kurse möglich ist
                    //fragt den User ob er fertig ist mit seiner Auswahl
                    //speichert die Einstellung und geht zur MainActivity
                    final List<dbKurs> alleKurse = new ArrayList<>();
                    alleKurse.addAll(kursauswahlAdapter.getSchriftlichList());
                    alleKurse.addAll(kursauswahlAdapter.getMündlichList());

                    // kontrolliert ob die Kombination möglich ist
                    if (checkKurseKombination(alleKurse) == true) {
                        // wenn ja wird final gefragt ob der User wirklich fertig ist
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Finnish!")
                                .setMessage("Bist du fertig mit der Auswahl deiner Kurse?")
                                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // wenn ja werden die Kurse als aktiv gespeichert
                                        // und ob er sie schriftlich oder mündlich hat
                                        for (dbKurs kurs : kursauswahlAdapter.getSchriftlichList()) {
                                            kurs.aktiv = true;
                                            kurs.schriftlich = true;
                                            if (new dbKursTagConnect().getTags(kurs.getId()) != null) {
                                                List<dbWebsiteTag> websiteTags = new dbKursTagConnect().getTags(kurs.getId());
                                                for (int i = 0; i < websiteTags.size(); i++) {
                                                    dbWebsiteTag websiteTag = websiteTags.get(i);
                                                    websiteTag.vorkommen++;
                                                    websiteTag.save();
                                                }
                                            }
                                            kurs.save();
                                        }
                                        Log.d(TAG, kursauswahlAdapter.getMündlichList().size() + "  SIZE MÜNDLICH");
                                        for (dbKurs kurs : kursauswahlAdapter.getMündlichList()) {
                                            kurs.aktiv = true;
                                            kurs.schriftlich = false;
                                            if (new dbKursTagConnect().getTags(kurs.getId()) != null) {
                                                List<dbWebsiteTag> websiteTags = new dbKursTagConnect().getTags(kurs.getId());
                                                for (int i = 0; i < websiteTags.size(); i++) {
                                                    dbWebsiteTag websiteTag = websiteTags.get(i);
                                                    websiteTag.vorkommen++;
                                                    Log.d(TAG, websiteTag.websitetag + " kommt: " + websiteTag.vorkommen + " vor");
                                                    websiteTag.save();
                                                }
                                            }

                                            kurs.save();
                                        }
                                        alleKurse.clear();

                                        dbUser user = new dbUser().getUser();
                                        user.validData = true;
                                        user.save();

                                        // leitet den User auf die MainActivity weiter
                                        Intent intent = new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // wennn der User nicht einverstanden ist soll der dialog geschlossen werden
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        /** wenn die Kombination der Kurse nicht funktioniert wird dem User das
                         *  mitgeteilt
                         */
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Fehler")
                                .setMessage("Diese Kombination ist nicht möglich!")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                    //mode.finish(); Nur wenn die Auswahl aufgehobe werden soll
                    return true;
                // gibt informationen zu der Kursauswahl an
                case R.id.menu_info:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setTitle("Info")
                            .setMessage("Wähle Hier deine Kurse, AGs und Projektkurse aus. Wenn du ein Fach ausgewählt " +
                                    "hast gib uns noch an ob du dieses Fach schriftlich oder mündlich hast.\n\n" +
                                    "LK Fächer werden automatisch als schriftliches Fach gespeichert sowie AG und PK als mündliches Fach.\n\n" +
                                    "Wenn du fertig bist kannst du oben rechts auf den Hacken klicken.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();
                    return true;

                default:
                    return false;
            }
        }

        /**
         * @onDestroyActionMode wird ausgelöst wenn der User die Auswahl aufheben möchte
         * gibt an kursauswahlAdapter an die Auswahl aufzulösen
         * und der ActionMode wird beendet
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            kursauswahlAdapter.clearSelection();
            actionMode = null;
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
                switch (kursList.get(i).getKursart().gloablId) {
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
            if (new dbUser().getUser().stufe.equals("Q2") || new dbUser().getUser().stufe.equals("Q1")) {
                if (lkList.size() == 2) {
                    List<dbSchulstunde> stundenList = lkList.get(0).getSchulStunden(lkList.get(0).getId());
                    List<dbSchulstunde> stundenList2 = lkList.get(1).getSchulStunden(lkList.get(1).getId());

                    if (stundenList.get(0).wochentag == stundenList2.get(0).wochentag && stundenList.get(0).beginnzeit == stundenList2.get(0).beginnzeit) {
                        Log.d("CHECK", "LKs funktionieren nicht zusammen");
                        return false;
                    }
                } else {
                    Log.d("CHECK", "LKs sind zu wenige oder zu viele");
                    return false;
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
                stundenList = gkList.get(i).getSchulStunden(gkList.get(i).getId());
                dbSchulstunde schulstunde = stundenList.get(0);

                for (int k = i + 1; k < gkList.size(); k++) {
                    stundenList2 = gkList.get(k).getSchulStunden(gkList.get(k).getId());
                    dbSchulstunde schulstunde2 = stundenList2.get(0);

                    if (schulstunde2.wochentag == schulstunde.wochentag && schulstunde.beginnzeit == schulstunde2.beginnzeit) {
                        Log.d("CHECK", "Kurse gheen nicht zusammen " + schulstunde.kursID + " " + schulstunde2.kursID + " " + schulstunde2.wochentag + " " + schulstunde.wochentag + " " + schulstunde.beginnzeit + " " + schulstunde2.beginnzeit);
                        return false;
                    }
                }
            }

            return true;
        }
    }


}
