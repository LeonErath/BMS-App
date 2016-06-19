package leon.bms.adapters;

/**
 * Created by Leon E on 24.05.2016.
 */

import android.support.v7.widget.RecyclerView;
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
import leon.bms.model.mündlicheNoten;
import leon.bms.realm.dbNote;


public class MuendlichAdapter extends RecyclerView.Adapter<MuendlichAdapter.ViewHolder> {
    // @TAG hier wird der Tag der Klasse gespeichert. Nacher wichtig für die Log-Datein
    private static final String TAG = FehlerAdapter.class.getSimpleName();
    // Liste mit den nicht erledigten Aufgaben
    private List<mündlicheNoten> mündlicheNotenList;
    // @clickListenere wird immmer bei einem click auf ein Item ausgelöst
    private ViewHolder.ClickListener clickListener;
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;
    String[] notenArray = {"5-", "5", "5+", "4-", "4", "4+", "3-", "3", "3+", "2-", "2", "2+", "1-", "1", "1+"};
    /**
     * @Constructor benötigt einmal einen clickListener der in der Klasse implentiert sein muss
     * und die Liste der nichterledigten Aufgaben die angezeigt werden sollen.
     */
    public MuendlichAdapter(ViewHolder.ClickListener clickListener, List<mündlicheNoten> mündlicheNotenList) {
        super();
        this.clickListener = clickListener;
        this.mündlicheNotenList = mündlicheNotenList;
    }

    /**
     * @changeDataSet will be used not often only if the whole data was changed
     */
    public void changeDataSet(List<mündlicheNoten> mündlicheNotenList) {
        this.mündlicheNotenList = mündlicheNotenList;
        notifyDataSetChanged();
    }


    /**
     * @addAufgabe get called if you want to add a Aufgabe
     * checks if the adaoterList contains this Aufgabe
     * When not it will add the Aufgabe to the list
     * @notifyItemInsert for a nice Animation and updates the UI
     */
    public void addAufgabe(mündlicheNoten mündlicheNoten1) {
        boolean contains = false;
        for (mündlicheNoten münNoten : mündlicheNotenList) {
            if (mündlicheNoten1 == münNoten) {
                contains = true;
            }
        }
        if (contains == false) {
            if (mündlicheNotenList.size() != 1) {
                mündlicheNotenList.add(mündlicheNotenList.size() - 1, mündlicheNoten1);
                notifyItemInserted(mündlicheNotenList.size() - 1);
            } else {
                mündlicheNotenList.add(0, mündlicheNoten1);
                notifyItemInserted(0);
            }

        }
    }


    @Override
    public int getItemViewType(int position) {
        final mündlicheNoten münNote = mündlicheNotenList.get(position);
        return münNote.isKeinNote() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }


    /**
     * @onCreateViewHolder stellt die Verbindung zum item layout her
     * changes layout when the aufgabe is done or not done
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType == TYPE_INACTIVE ? R.layout.item_muendlich_note : R.layout.item_muendlich_add;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener, mündlicheNotenList);
    }

    /**
     * @onBindViewHolder ist die Verbindung von den Daten der Liste zu dem View also der Anzeige
     * Die Daten werden aus der Liste geladen und an den ViewHolder und den entsprechenden
     * Elemente übergeben , sodass nach die richtigen Daten an den richtigen "Views" sind
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final mündlicheNoten münNote = mündlicheNotenList.get(position);

        // Die Inhalte der Aufgabe werden angezeigt
        if (münNote.isKeinNote()) {

        } else {
            holder.textViewQuartal.setText(position+1+". Quartal");
            //holder.textViewDatum.setText(getDateString(klausur));
                dbNote note = münNote.getNote();
                holder.textViewDatum.setText(getDateString(note.getHinzugefuegtAm()));
                holder.textViewNotePunkte.setText(note.getPunkte()+" P.");
                holder.textViewNote.setText(notenArray[note.getPunkte()-1]);
                holder.textViewKeineNote.setVisibility(View.INVISIBLE);
                if (note.getPunkte() <= 4){
                    holder.imageViewAchtung.setVisibility(View.VISIBLE);
                }else {
                    holder.imageViewAchtung.setVisibility(View.INVISIBLE);
                }

        }

    }

    public String getDateString(String datum){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(datum));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy " );

        return sdfmt.format(calendar.getTime());
    }

    public void deleteFehler(int position) {
        if (position >= 0 && position < getItemCount()) {
            mündlicheNotenList.remove(position);
            notifyItemChanged(position);
        }
    }

    public mündlicheNoten getFehler(int position) {
        if (position >= 0 && position < getItemCount()) {
            return mündlicheNotenList.get(position);
        }
        return null;
    }


    /**
     * @getItemCount() ist wichtig für die Funktionalität des RecyclerViews
     * Gibt die größe der Liste zurück bzw. wie viele Items der RecylcerView beinhaltet
     */
    @Override
    public int getItemCount() {
        return mündlicheNotenList.size();
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
        TextView textViewQuartal, textViewDatum, textViewNote, textViewNotePunkte, textViewKeineNote;
        ImageView imageViewAchtung;
        List<mündlicheNoten> mündlicheNotenList;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener, List<mündlicheNoten> mündlicheNotenList) {
            super(itemView);

            // Hier werden alle View intilisiert bzw ihre Verbindung zugewiesen
            textViewQuartal = (TextView) itemView.findViewById(R.id.textViewQuartal);
            textViewDatum = (TextView) itemView.findViewById(R.id.textViewDatum);
            textViewNote = (TextView) itemView.findViewById(R.id.textViewNote);
            textViewKeineNote = (TextView) itemView.findViewById(R.id.textViewKeineNote);
            textViewNotePunkte = (TextView) itemView.findViewById(R.id.textViewNotePunkte);
            imageViewAchtung = (ImageView) itemView.findViewById(R.id.imageViewAchtung);

            //listener wird initialisiert

            this.listener = listener;
            this.mündlicheNotenList = mündlicheNotenList;
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
                listener.onItemClicked2(getAdapterPosition());
            }
        }

        //@onLongClick wird ausgelöst bei einem langen Click
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked2(getAdapterPosition());
            }

            return false;
        }

        /**
         * @inteface ClickListener wird gebraucht um die clickEvents in die Klasse zu integrieren
         * beinhalten zwei Click Methoden
         */
        public interface ClickListener {
            public void onItemClicked2(int position);

            public boolean onItemLongClicked2(int position);
        }
    }
}
