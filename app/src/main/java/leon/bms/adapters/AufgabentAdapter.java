package leon.bms.adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import leon.bms.R;
import leon.bms.database.dbAufgabe;
import leon.bms.model.aufgabenModel;
import leon.bms.model.nachrichten;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * Dieseer Adapter stellt die nicht erledigten Aufgaben in einer Liste da
 * Er vermittel sozusagen die Daten an den RecyclerView
 * Sie beinhaltet einer weitere Klasse ViewHolder die sich um den View kümmert ,also
 * dem aussehen und die Verbindung zum Item layout
 */
public class AufgabentAdapter extends RecyclerView.Adapter<AufgabentAdapter.ViewHolder> {
    // @TAG hier wird der Tag der Klasse gespeichert. Nacher wichtig für die Log-Datein
    private static final String TAG = AufgabentAdapter.class.getSimpleName();
    // Liste mit den nicht erledigten Aufgaben
    private List<aufgabenModel> aufgabenList;
    // @clickListenere wird immmer bei einem click auf ein Item ausgelöst
    private ViewHolder.ClickListener clickListener;
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    /**
     * @Constructor benötigt einmal einen clickListener der in der Klasse implentiert sein muss
     * und die Liste der nichterledigten Aufgaben die angezeigt werden sollen.
     */
    public AufgabentAdapter(ViewHolder.ClickListener clickListener, List<aufgabenModel> aufgabeList) {
        super();
        this.clickListener = clickListener;
        this.aufgabenList = aufgabeList;
        Log.d(TAG, aufgabeList.size() + " ");
    }

    /**
     * @changeDataSet Methode zum Austausch der Daten
     * @param aufgabenModelList alte Liste wird duch die neue Komplett ersetzt
     */
    public void changeDataSet(List<aufgabenModel> aufgabenModelList) {

        int aufgabenSize = aufgabenList.size();
        for (int i= 0;i<aufgabenSize;i++){
            removeArticle(aufgabenList.size()-1);
        }
        for (int i= 0;i<aufgabenModelList.size();i++){
            addArticle(aufgabenModelList.get(i));
        }

    }
    public void addArticle(aufgabenModel aufgabenModel) {
        aufgabenList.add(aufgabenModel);
        notifyItemInserted(aufgabenList.size() - 1);
    }
    public void removeArticle(int position) {
        aufgabenList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * @changeAufgabe get called when a Aufgabe gets changed
     * seaches for the aufgabe and changes it with the new aufgabe
     * @notifyItemChanged for a nice Animation and updates the UI
     */
    public void changeAufgabe(dbAufgabe aufgabeNew) {

    }




    @Override
    public int getItemViewType(int position) {
        final aufgabenModel aufgabe = aufgabenList.get(position);
        return aufgabe.getStatus();
    }


    /**
     * @onCreateViewHolder stellt die Verbindung zum item layout her
     * changes layout when the aufgabe is done or not done
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout;
        switch (viewType){
            case 0:   layout= R.layout.item_aufgabe;
                break;
            case 1: layout= R.layout.item_aufgabe_header;
                break;
            default:layout= R.layout.item_aufgabe;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener, aufgabenList);
    }

    /**
     * @onBindViewHolder ist die Verbindung von den Daten der Liste zu dem View also der Anzeige
     * Die Daten werden aus der Liste geladen und an den ViewHolder und den entsprechenden
     * Elemente übergeben , sodass nach die richtigen Daten an den richtigen "Views" sind
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final aufgabenModel aufgabe = aufgabenList.get(position);

        // Die Inhalte der Aufgabe werden angezeigt
        if (aufgabe.getStatus() == 1){
            holder.textViewHeader.setText(aufgabe.getTextHeader());
        }else {
            holder.textViewBeschreibung.setText(aufgabe.getAufgabe().beschreibung);
            holder.textViewKursZeit.setText(aufgabe.getAufgabe().abgabeDatum+", "+aufgabe.getAufgabe().kurs.untisId);
            if (aufgabe.getAufgabe().erledigt){
                holder.imageViewIcon.setImageResource(R.drawable.ic_done_black_48dp);
            }else{
                holder.imageViewIcon.setImageResource(R.drawable.ic_clear_black_48dp);
            }
        }

    }

    /**
     * @brechneRestTage ist eine Mtethode die von der übergebenen aufgabe die restliche
     * Zeit brechnet bis zum Abgabe Termin in Tagen. Dazu wird das AbgabeDatum sowie das das Datum
     * von Heute in Tagen umgerechnet und das AbgabeDatum minus das heutgie Datum gerechnet. Somit hat
     * man den Unterschied in Tagen.
     */
    public int berechneRestTage(dbAufgabe aufgabe) {

        Calendar abgabeDatum = Calendar.getInstance();
        Calendar heute = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM d yyyy");
        try {
            abgabeDatum.setTime(sdf.parse(aufgabe.abgabeDatum));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int heuteInt = heute.get(Calendar.DAY_OF_YEAR);
        int abgabe = abgabeDatum.get(Calendar.DAY_OF_YEAR);

        return (abgabe - heuteInt);
    }

    /**
     * @getItemCount() ist wichtig für die Funktionalität des RecyclerViews
     * Gibt die größe der Liste zurück bzw. wie viele Items der RecylcerView beinhaltet
     */
    @Override
    public int getItemCount() {
        return aufgabenList.size();
    }


    /**
     * @ViewHolder stellt die Verbindung zum layout her
     * Hier werden die einzelnen Views definiert und initialisiert
     * @View.OnClickListener wird implentiert um auf Item Clicks zu reagieren
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        // TAG wird wieder mit dem Namen der Klasse gespeichert für spätere Log-Datein
        private static final String TAG = ViewHolder.class.getSimpleName();
        // Hier werden alle Views des Layout definiert die nacher wichtig sind
        TextView textViewBeschreibung;
        TextView textViewKursZeit;
        TextView textViewHeader;
        ImageView imageViewIcon;
        List<aufgabenModel> aufgabeList;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener, List<aufgabenModel> aufgabeList) {
            super(itemView);

            // Hier werden alle View intilisiert bzw ihre Verbindung zugewiesen
            textViewBeschreibung = (TextView) itemView.findViewById(R.id.textViewBeschreibung);
            textViewKursZeit = (TextView) itemView.findViewById(R.id.textViewKursZeit);
            textViewHeader = (TextView) itemView.findViewById(R.id.textViewHeader);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

            //listener wird initialisiert

            this.listener = listener;
            this.aufgabeList = aufgabeList;
            // Methode des ClickListeners werden implentiert
            // @onClick wird ausgelöst bei einem kurzem Click
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // Methode des ClickListeners werden implentiert
        // @onClick wird ausgelöst bei einem kurzem Click
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition(),aufgabeList.get(getAdapterPosition()));
            }
        }

        //@onLongClick wird ausgelöst bei einem langen Click
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition(),aufgabeList.get(getAdapterPosition()));
            }

            return false;
        }

        /**
         * @inteface ClickListener wird gebraucht um die clickEvents in die Klasse zu integrieren
         * beinhalten zwei Click Methoden
         */
        public interface ClickListener {
            public void onItemClicked(int position,aufgabenModel aufgabe);

            public boolean onItemLongClicked(int position,aufgabenModel aufgabe);
        }
    }
}