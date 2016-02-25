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


public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.ViewHolder> {
    // @TAG hier wird der Tag der Klasse gespeichert. Nacher wichtig für die Log-Datein
    private static final String TAG = AufgabentAdapter.class.getSimpleName();

    private List<Highlight> highlightList;
    // @clickListenere wird immmer bei einem click auf ein Item ausgelöst
    private ViewHolder.ClickListener clickListener;



    public HighlightAdapter(ViewHolder.ClickListener clickListener, List<Highlight> highlightList) {
        super();
        this.clickListener = clickListener;
        this.highlightList = highlightList;
        Log.d(TAG, highlightList.size() + " ");
    }




    @Override
    public int getItemViewType(int position) {
        final Highlight highlight = highlightList.get(position);
        return highlight.getType();
    }


    /** @onCreateViewHolder stellt die Verbindung zum item layout her
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;

        switch (viewType){
            case 0: layout = R.layout.item_aufgabenicht;break;
            case 1: layout = R.layout.item_websiteartikel;break;
            default: layout = R.layout.item_stundenplan_leer;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener, highlightList);
    }

    /** @onBindViewHolder ist die Verbindung von den Daten der Liste zu dem View also der Anzeige
     *  Die Daten werden aus der Liste geladen und an den ViewHolder und den entsprechenden
     *  Elemente übergeben , sodass nach die richtigen Daten an den richtigen "Views" sind
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Highlight highlight = highlightList.get(position);

        // Die Inhalte der Aufgabe werden angezeigt




    }

    /** @getItemCount() ist wichtig für die Funktionalität des RecyclerViews
     *  Gibt die größe der Liste zurück bzw. wie viele Items der RecylcerView beinhaltet
     */
    @Override
    public int getItemCount() {
        return highlightList.size();
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

        TextView textViewHeadline;
        TextView textViewContent;
        ImageView imageView;

        List<Highlight> highlightList;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener,List<Highlight> highlightList) {
            super(itemView);

            // Hier werden alle View intilisiert bzw ihre Verbindung zugewiesen
            textViewBeschreibung= (TextView) itemView.findViewById(R.id.textViewBeschreibung);
            textViewKurs= (TextView) itemView.findViewById(R.id.textViewKurs);
            textViewZeit= (TextView) itemView.findViewById(R.id.textViewZeit);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

            textViewHeadline = (TextView) itemView.findViewById(R.id.textViewHeadline);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewExcerpt);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            //listener wird initialisiert

            this.listener = listener;
            this.highlightList = highlightList;
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
                listener.onItemClicked(highlightList.get(getAdapterPosition()));
            }
        }
        //@onLongClick wird ausgelöst bei einem langen Click
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(highlightList.get(getAdapterPosition()));
            }

            return false;
        }
        /** @inteface ClickListener wird gebraucht um die clickEvents in die Klasse zu integrieren
         *  beinhalten zwei Click Methoden
         */
        public interface ClickListener {
            public void onItemClicked(Highlight highlight);
            public boolean onItemLongClicked(Highlight highlight);
        }
    }
}