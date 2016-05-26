package leon.bms.activites.main;


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
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.sergiocasero.revealfab.RevealFAB;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import leon.bms.activites.website.Fragment_Article;
import leon.bms.R;
import leon.bms.activites.aufgabe.AufgabenActivity;
import leon.bms.adapters.AufgabentAdapter;
import leon.bms.database.dbAufgabe;
import leon.bms.model.aufgabenModel;


/**
 * @Fragment_AufgabeÜbersicht ist ein Fragment welches die gemacht und nicht gemacht Aufgaben
 * darstellen soll . Dazu verwendet es einen RecyclerView und die in der Aufgaben vorhanden Suchabfragen
 * für die Aufgaben um diese anzuzeigen.
 */
public class Fragment_AufgabeUebersicht extends Fragment implements AufgabentAdapter.ViewHolder.ClickListener,FragmentLifecycle {

    // definieren des recyclcerViews
    RecyclerView recyclerViewAufgaben;
    AufgabentAdapter aufgabeAdapter;
    TextView textViewAnzahl;
    RevealFAB revealFAB;
    private static String TAG = Fragment_Article.class.getSimpleName();
    List<aufgabenModel> aufgabenModelList = new ArrayList<>();


    public Fragment_AufgabeUebersicht() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__aufgabeuebersicht, container, false);
    }



    /**
     * @onResume aktualisiert das Fragment wenn es wieder aufgerufen wird
     */
    @Override
    public void onResume() {
        super.onResume();
        if (aufgabeAdapter != null) {
            if (getListForAdapter() != null){
                aufgabeAdapter.changeDataSet(getListForAdapter());
            }
        }
        revealFAB.onResume();
    }

    /**
     * @onViewCreated hier wird alles erstellt und initalisiert
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerViewAufgaben = (RecyclerView) view.findViewById(R.id.recycler_view);
        textViewAnzahl = (TextView) view.findViewById(R.id.textViewAnzahl);




        aufgabeAdapter = new AufgabentAdapter(this, aufgabenModelList);
        recyclerViewAufgaben.setAdapter(aufgabeAdapter);
        OvershootInLeftAnimator animator = new OvershootInLeftAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerViewAufgaben.setItemAnimator(animator);
        recyclerViewAufgaben.setLayoutManager(new LinearLayoutManager(getActivity()));

        revealFAB = (RevealFAB) view.findViewById(R.id.reveal_fab);

        Intent intent = new Intent(getActivity(), AufgabenActivity.class);
        revealFAB.setIntent(intent);
        revealFAB.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });

        if (aufgabeAdapter != null && aufgabeAdapter.getItemCount() ==0){
            if (getListForAdapter() != null){
                aufgabeAdapter.changeDataSet(getListForAdapter());
            }
        }

    }


    public List<aufgabenModel> getListForAdapter(){
        List<aufgabenModel> convertedList = new ArrayList<>();

        List<dbAufgabe> unerledigtList = new dbAufgabe().getUnerledigtAufgabe();
        List<dbAufgabe> erledigtList = new dbAufgabe().getErledigtAufgabe();
        int unerledigt=0;
        int erledigt=0;

        if (unerledigtList != null && unerledigtList.size()>0){
            aufgabenModel aufgabenModelHead = new aufgabenModel();
            aufgabenModelHead.setStatus(1);
            aufgabenModelHead.setTextHeader("ausstehend");
            convertedList.add(aufgabenModelHead);
            for (dbAufgabe aufgabe:unerledigtList){
                aufgabenModel aufgabenModel1 = new aufgabenModel();
                aufgabenModel1.setStatus(0);
                aufgabenModel1.setAufgabe(aufgabe);
                convertedList.add(aufgabenModel1);
            }
            unerledigt = unerledigtList.size();
        }
        if (erledigtList != null && erledigtList.size()>0){
            aufgabenModel aufgabenModelHead = new aufgabenModel();
            aufgabenModelHead.setStatus(1);
            aufgabenModelHead.setTextHeader("erledigt");
            convertedList.add(aufgabenModelHead);
            for (dbAufgabe aufgabe:erledigtList){
                aufgabenModel aufgabenModel1 = new aufgabenModel();
                aufgabenModel1.setStatus(0);
                aufgabenModel1.setAufgabe(aufgabe);
                convertedList.add(aufgabenModel1);
            }
           erledigt = erledigtList.size();
        }

       textViewAnzahl.setText(unerledigt+" unerledigt - "+erledigt+" erledigt");
        if (convertedList != null && convertedList.size()>0){
            return convertedList;
        }
        return null;
    }




    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


    @Override
    public void onItemClicked(int position, aufgabenModel aufgabe) {
        click(position,aufgabe);
    }
    @Override
    public boolean onItemLongClicked(int position, aufgabenModel aufgabe) {
        return false;
    }

    private void click(int position, final aufgabenModel aufgabe) {
        if (aufgabe.getAufgabe().erledigt != true) {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.getAufgabe().beschreibung
                    + ", Fach: " + aufgabe.getAufgabe().kurs.name).sheet(R.menu.menu_nichtgemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die serverid der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("id", aufgabe.getAufgabe().getId());
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.getAufgabe().kurs.fachnew.name);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.getAufgabe().beschreibung + "\n" + aufgabe.getAufgabe().notizen
                                    + "\nAbgabe am:" + aufgabe.getAufgabe().abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            dbAufgabe.delete(aufgabe.getAufgabe());
                            if (getListForAdapter() != null){
                                aufgabeAdapter.changeDataSet(getListForAdapter());
                            }
                            break;
                        case R.id.erledigt:
                            // ändert die Aufgabe zu erledigt
                            aufgabe.getAufgabe().erledigt = true;
                            aufgabe.getAufgabe().save();
                            if (getListForAdapter() != null){
                                aufgabeAdapter.changeDataSet(getListForAdapter());
                            }
                            break;
                    }
                }
            }).show();
        } else {
            new BottomSheet.Builder(getActivity()).title("Aufgabe " + aufgabe.getAufgabe().beschreibung
                    + ", Fach: " + aufgabe.getAufgabe().kurs.name).sheet(R.menu.menu_gemachteaufgaben).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<dbAufgabe> unerledigtList;
                    List<dbAufgabe> erledigtList;
                    List<dbAufgabe> alleAufgaben = new ArrayList<dbAufgabe>();
                    switch (which) {
                        case R.id.change:
                            // ruft für die Änderung der Aufgabe die AufgabenActivity auf und übergibt die serverid der Aufgabe
                            Intent intent = new Intent(getActivity(), AufgabenActivity.class);
                            intent.putExtra("id", aufgabe.getAufgabe().getId());
                            startActivity(intent);
                            break;
                        case R.id.share:
                            // teilt die Aufgabe
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aufgabe.getAufgabe().kurs.untisId);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, aufgabe.getAufgabe().beschreibung + "\n" + aufgabe.getAufgabe().notizen
                                    + "\nAbgabe am:" + aufgabe.getAufgabe().abgabeDatum);
                            startActivity(Intent.createChooser(sharingIntent, "Share.."));
                            break;
                        case R.id.delete:
                            // löscht die Aufgabe und updatet die Liste sowie den Adapter
                            dbAufgabe.delete(aufgabe.getAufgabe());
                            if (getListForAdapter() != null){
                                aufgabeAdapter.changeDataSet(getListForAdapter());
                            }
                            break;
                        case R.id.cancel:
                            // ändert die Aufgabe zu nicht erledigt
                            aufgabe.getAufgabe().erledigt = false;
                            aufgabe.getAufgabe().save();
                            if (getListForAdapter() != null){
                                aufgabeAdapter.changeDataSet(getListForAdapter());
                            }
                            break;
                    }
                }
            }).show();
        }


    }

}

