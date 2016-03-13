package leon.bms;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.List;


/**
 * @Fragment_AufgabeÜbersicht ist ein Fragment welches die gemacht und nicht gemacht Aufgaben
 * darstellen soll . Dazu verwendet es einen RecyclerView und die in der Aufgaben vorhanden Suchabfragen
 * für die Aufgaben um diese anzuzeigen.
 */
public class Fragment_AufgabeÜbersicht extends Fragment implements AufgabentAdapter.ViewHolder.ClickListener {

    // definieren des recyclcerViews
    RecyclerView recyclerViewAufgaben;
    AufgabentAdapter aufgabeAdapter;
    private static boolean m_iAmVisible;
    private static String TAG = Fragment_Article.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__aufgabeuebersicht, container, false);
    }

    /**
     * @param isVisibleToUser gibt an ob das Fragment sichtbar ist oder nicht
     * @setUserVisibleHint prüft ob Fragment sichtbar ist oder nicht. Wenn das Fragment sichtbar
     * ist werden die Auggaben geladen und angezeigt.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;
        if (m_iAmVisible) {
            if (aufgabeAdapter != null) {
                List<dbAufgabe> unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                List<dbAufgabe> erledigtList = new dbAufgabe().getErledigtAufgabe();
                List<dbAufgabe> alleAufgaben = unerledigtList;
                alleAufgaben.addAll(erledigtList);
                if (alleAufgaben != null || alleAufgaben.size() != 0) {
                    aufgabeAdapter.changeDataSet(alleAufgaben);
                }
            }
            Log.d(TAG, "this fragment is now visible");

        } else {
            Log.d(TAG, "this fragment is now invisible");
        }

    }

    /**
     * @onResume aktualisiert das Fragment wenn es wieder aufgerufen wird
     */
    @Override
    public void onResume() {
        super.onResume();
        if (aufgabeAdapter != null) {
            List<dbAufgabe> unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
            List<dbAufgabe> erledigtList = new dbAufgabe().getErledigtAufgabe();
            List<dbAufgabe> alleAufgaben = unerledigtList;
            alleAufgaben.addAll(erledigtList);
            if (alleAufgaben != null || alleAufgaben.size() != 0) {
                for (dbAufgabe aufgabe : alleAufgaben) {
                    aufgabeAdapter.addAufgabe(aufgabe);
                }
            }
        }
    }

    /**
     * @onViewCreated hier wird alles erstellt und initalisiert
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewAufgaben = (RecyclerView) view.findViewById(R.id.recycler_view);

        List<dbAufgabe> unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
        List<dbAufgabe> erledigtList = new dbAufgabe().getErledigtAufgabe();
        List<dbAufgabe> alleAufgaben = unerledigtList;
        alleAufgaben.addAll(erledigtList);

        aufgabeAdapter = new AufgabentAdapter(this, alleAufgaben);
        recyclerViewAufgaben.setAdapter(aufgabeAdapter);
        recyclerViewAufgaben.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAufgaben.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    /**
     * @param aufgabe ist die Aufgabe auf die geclickt worden ist
     * @onItemClicked zeigt ein BottomSheet an für weiter Bediengung der Aufgaben
     */
    @Override
    public void onItemClicked(final dbAufgabe aufgabe) {
        //setUp BootonSheet
        if (aufgabe.checkIfErledigtAufgabe(aufgabe) != true) {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.beschreibung + "             Fach: " + aufgabe.kurs.name).sheet(R.menu.menu_nichtgemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<dbAufgabe> unerledigtList;
                    List<dbAufgabe> erledigtList;
                    List<dbAufgabe> alleAufgaben = new ArrayList<dbAufgabe>();
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die id der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("id", aufgabe.getId());
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.kurs.fach);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.beschreibung + "\n" + aufgabe.notizen + "\nAbgabe am:" + aufgabe.abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            // löscht die Aufgabe und updatet die Liste sowie den Adapter
                            alleAufgaben.clear();
                            aufgabe.delete();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.removeAufgabe(aufgabe);
                            break;
                        case R.id.erledigt:
                            // ändert die Aufgabe zu erledigt
                            alleAufgaben.clear();
                            aufgabe.erledigt = true;
                            aufgabe.save();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.changeAufgabe(aufgabe);
                            break;
                    }
                }
            }).show();
        } else {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.beschreibung + "            Fach: " + aufgabe.kurs.name).sheet(R.menu.menu_gemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<dbAufgabe> unerledigtList;
                    List<dbAufgabe> erledigtList;
                    List<dbAufgabe> alleAufgaben = new ArrayList<dbAufgabe>();
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die id der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("id", aufgabe.getId());
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.kurs.fach);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.beschreibung + "\n" + aufgabe.notizen + "\nAbgabe am:" + aufgabe.abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            // löscht die Aufgabe und updatet die Liste sowie den Adapter
                            alleAufgaben.clear();
                            aufgabe.delete();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.removeAufgabe(aufgabe);
                            break;
                        case R.id.cancel:
                            // ändert die Aufgabe zu nicht erledigt
                            alleAufgaben.clear();
                            aufgabe.erledigt = false;
                            aufgabe.save();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.changeAufgabe(aufgabe);
                            break;
                    }
                }
            }).show();
        }
    }

    /**
     * @param aufgabe ist die aufgabe die angeclickt wurde
     * @return nicht wichtig
     * @onItemLongClicked wird aufgerufen wenn ein Item lange geclickt wird
     */
    @Override
    public boolean onItemLongClicked(final dbAufgabe aufgabe) {
        if (aufgabe.checkIfErledigtAufgabe(aufgabe) != true) {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.beschreibung + "             Fach: " + aufgabe.kurs.name).sheet(R.menu.menu_nichtgemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<dbAufgabe> unerledigtList;
                    List<dbAufgabe> erledigtList;
                    List<dbAufgabe> alleAufgaben = new ArrayList<dbAufgabe>();
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die id der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("id", aufgabe.getId());
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.kurs.fach);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.beschreibung + "\n" + aufgabe.notizen + "\nAbgabe am:" + aufgabe.abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            // löscht die Aufgabe und updatet die Liste sowie den Adapter
                            alleAufgaben.clear();
                            aufgabe.delete();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.removeAufgabe(aufgabe);
                            break;
                        case R.id.erledigt:
                            // ändert die Aufgabe zu erledigt
                            alleAufgaben.clear();
                            aufgabe.erledigt = true;
                            aufgabe.save();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.changeAufgabe(aufgabe);
                            break;
                    }
                }
            }).show();
        } else {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.beschreibung + "            Fach: " + aufgabe.kurs.name).sheet(R.menu.menu_gemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<dbAufgabe> unerledigtList;
                    List<dbAufgabe> erledigtList;
                    List<dbAufgabe> alleAufgaben = new ArrayList<dbAufgabe>();
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die id der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("Beschreibung", aufgabe.beschreibung);
                            intent.putExtra("Notizen", aufgabe.notizen);
                            intent.putExtra("Fach", aufgabe.kurs.fach);
                            intent.putExtra("Datum", aufgabe.abgabeDatum);
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.kurs.fach);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.beschreibung + "\n" + aufgabe.notizen + "\nAbgabe am:" + aufgabe.abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            // löscht die Aufgabe und updatet die Liste sowie den Adapter
                            alleAufgaben.clear();
                            aufgabe.delete();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.changeDataSet(alleAufgaben);
                            break;
                        case R.id.cancel:
                            // ändert die Aufgabe zu nicht erledigt
                            alleAufgaben.clear();
                            aufgabe.erledigt = false;
                            aufgabe.save();
                            if (new dbAufgabe().getUnerledigtAufgabe().size() != 0) {
                                unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
                                alleAufgaben.addAll(unerledigtList);
                            }
                            if (new dbAufgabe().getErledigtAufgabe().size() != 0) {
                                erledigtList = new dbAufgabe().getErledigtAufgabe();
                                alleAufgaben.addAll(erledigtList);
                            }
                            aufgabeAdapter.changeAufgabe(aufgabe);
                            break;
                    }
                }
            }).show();
        }
        return false;
    }

}

