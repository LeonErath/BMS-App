package leon.bms;

import android.graphics.Color;
import android.graphics.PorterDuff;
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

/**
 * Created by Leon E on 21.01.2016.
 */

/** Dieseer Adapter stellt die nicht erledigten Aufgaben in einer Liste da
 *  Er vermittel sozusagen die Daten an den RecyclerView
 *  Sie beinhaltet einer weitere Klasse ViewHolder die sich um den View kümmert ,also
 *  dem aussehen und die Verbindung zum Item layout
 */
public class AufgabentAdapter extends RecyclerView.Adapter<AufgabentAdapter.ViewHolder> {
    // @TAG hier wird der Tag der Klasse gespeichert. Nacher wichtig für die Log-Datein
    private static final String TAG = AufgabentAdapter.class.getSimpleName();
    // Liste mit den nicht erledigten Aufgaben
    private List<dbAufgabe> aufgabenList;
    // @clickListenere wird immmer bei einem click auf ein Item ausgelöst
    private ViewHolder.ClickListener clickListener;
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    /** @Constructor benötigt einmal einen clickListener der in der Klasse implentiert sein muss
     *  und die Liste der nichterledigten Aufgaben die angezeigt werden sollen.
     */
    public AufgabentAdapter(ViewHolder.ClickListener clickListener, List<dbAufgabe> aufgabeList) {
        super();
        this.clickListener = clickListener;
        this.aufgabenList = aufgabeList;
        Log.d(TAG, aufgabeList.size() + " ");
    }

    public void changeDataSet(List<dbAufgabe> dbAufgabeList){
        aufgabenList = dbAufgabeList;
        notifyDataSetChanged();
    }
    public void removeAufgabe(dbAufgabe aufgabe){
        if (aufgabenList.contains(aufgabe)){
            if (aufgabenList.size() == 1){
                aufgabenList.clear();
                notifyItemRemoved(0);
            }else {
                int position = aufgabenList.indexOf(aufgabe);
                aufgabenList.remove(aufgabe);
                notifyItemRemoved(position);
            }
        }
    }
    public void addAufgabe(dbAufgabe aufgabe){
        boolean contains = false;
        for (dbAufgabe aufgabe1:aufgabenList){
            if (aufgabe1.getId()==aufgabe.getId()){
                contains = true;
            }
        }
        if (contains == false) {
            aufgabenList.add(aufgabe);
            notifyItemInserted(aufgabenList.size() - 1);
        }
    }
    public void changeAufgabe(dbAufgabe aufgabe){
        if (aufgabenList.contains(aufgabe)){
            int index = aufgabenList.indexOf(aufgabe);
            aufgabenList.remove(index);
            aufgabenList.add(index,aufgabe);
            notifyItemChanged(index);
        }
    }


    @Override
    public int getItemViewType(int position) {
        final dbAufgabe aufgabe = aufgabenList.get(position);
        return aufgabe.erledigt ? TYPE_ACTIVE : TYPE_INACTIVE;
    }


    /** @onCreateViewHolder stellt die Verbindung zum item layout her
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType  == TYPE_INACTIVE ? R.layout.item_aufgabenicht : R.layout.item_aufgabeerledigt;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener, aufgabenList);
    }

    /** @onBindViewHolder ist die Verbindung von den Daten der Liste zu dem View also der Anzeige
     *  Die Daten werden aus der Liste geladen und an den ViewHolder und den entsprechenden
     *  Elemente übergeben , sodass nach die richtigen Daten an den richtigen "Views" sind
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final dbAufgabe aufgabe = aufgabenList.get(position);

        // Die Inhalte der Aufgabe werden angezeigt
        holder.textViewBeschreibung.setText(aufgabe.beschreibung);
        holder.textViewKurs.setText(aufgabe.kurs.fach);

        if (aufgabe.erledigt== false){
        // Anzeige des Countdown wie viele Tage man noch hat bis zur Abgabe der Hausaufgabe
        int restTage = berechneRestTage(aufgabe);
        // Überprüft ob noch Zeit für die Aufgaben besteht
        if (restTage >= 1){
            holder.textViewZeit.setText("Noch "+(restTage)+" Tage.");
        }else {
            // Wenn der Countdown abgelaufen ist wird der View verändert
            holder.textViewZeit.setTextColor(Color.RED);
            holder.textViewZeit.setText("Zeit überschritten!");
        }
        }else {
            holder.textViewZeit.setText("Erledigt.");
        }

    }
    /** @brechneRestTage ist eine Mtethode die von der übergebenen aufgabe die restliche
     *  Zeit brechnet bis zum Abgabe Termin in Tagen. Dazu wird das AbgabeDatum sowie das das Datum
     *  von Heute in Tagen umgerechnet und das AbgabeDatum minus das heutgie Datum gerechnet. Somit hat
     *  man den Unterschied in Tagen.
     */
    public int berechneRestTage(dbAufgabe aufgabe){

        Calendar abgabeDatum = Calendar.getInstance();
        Calendar heute = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM d yyyy");
        try {
            abgabeDatum.setTime(sdf.parse(aufgabe.abgabeDatum));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int heuteInt = heute.get(Calendar.DAY_OF_YEAR);
        int abgabe =abgabeDatum.get(Calendar.DAY_OF_YEAR);

        return  (abgabe - heuteInt);
    }
    /** @getItemCount() ist wichtig für die Funktionalität des RecyclerViews
     *  Gibt die größe der Liste zurück bzw. wie viele Items der RecylcerView beinhaltet
     */
    @Override
    public int getItemCount() {
        return aufgabenList.size();
    }


    /** @ViewHolder stellt die Verbindung zum layout her
     *  Hier werden die einzelnen Views definiert und initialisiert
     *  @View.OnClickListener wird implentiert um auf Item Clicks zu reagieren
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        // TAG wird wieder mit dem Namen der Klasse gespeichert für spätere Log-Datein
        private static final String TAG = ViewHolder.class.getSimpleName();
        // Hier werden alle Views des Layout definiert die nacher wichtig sind
        TextView textViewBeschreibung;
        TextView textViewKurs;
        TextView textViewZeit;
        ImageView imageViewIcon;
        List<dbAufgabe> aufgabeList;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener,List<dbAufgabe> aufgabeList) {
            super(itemView);

            // Hier werden alle View intilisiert bzw ihre Verbindung zugewiesen
            textViewBeschreibung= (TextView) itemView.findViewById(R.id.textViewBeschreibung);
            textViewKurs= (TextView) itemView.findViewById(R.id.textViewKurs);
            textViewZeit= (TextView) itemView.findViewById(R.id.textViewZeit);
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
                listener.onItemClicked(aufgabeList.get(getAdapterPosition()));
            }
        }
        //@onLongClick wird ausgelöst bei einem langen Click
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(aufgabeList.get(getAdapterPosition()));
            }

            return false;
        }
        /** @inteface ClickListener wird gebraucht um die clickEvents in die Klasse zu integrieren
         *  beinhalten zwei Click Methoden
         */
        public interface ClickListener {
            public void onItemClicked(dbAufgabe aufgabe);
            public boolean onItemLongClicked(dbAufgabe aufgabe);
        }
    }
}