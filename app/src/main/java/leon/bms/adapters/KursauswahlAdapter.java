package leon.bms.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leon.bms.R;
import leon.bms.database.dbAufgabe;
import leon.bms.database.dbKurs;
import leon.bms.model.kursauswahl;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @KursauswahlAdapter wird benötigt für das Anzeigen der Kursauswahl bzw der Kurse für die Kursauswahl.
 */
public class KursauswahlAdapter extends SelectableAdapter<KursauswahlAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<kursauswahl> kurse;
    private List<dbKurs> schriftlichList;
    private List<dbKurs> mündlichList;

    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener wird gebraucht um auf Click events zu reagieren
     * @param kursList      ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KursauswahlAdapter(ViewHolder.ClickListener clickListener, List<kursauswahl> kursList) {
        super();
        this.clickListener = clickListener;
        this.kurse = kursList;
        Log.d(TAG, kursList.size() + " ");

        schriftlichList = new ArrayList<>();
        mündlichList = new ArrayList<>();

    }


    /**
     * @param position    ist die Position des Items welches die Liste wechselt
     * @param schriftlich ist die Angabe zu welcher Liste es wechselt entweder zu schriftlich oder mündlich
     * @switchMS Ändert den Kurs von einer Liste zur anderen.
     * Speichert den Kurs in der entsprechende Liste für Schriftlich oder Mündlich
     */
    public void switchMS(int position, boolean schriftlich) {
        dbKurs kurs = kurse.get(position).kurs;


        if (schriftlich == true) {
            if (!schriftlichList.contains(kurs)) {
                schriftlichList.add(kurs);
                Log.d(TAG, "schriftlichList.add " + kurs.name);
            }
            if (mündlichList.contains(kurs)) {
                mündlichList.remove(mündlichList.indexOf(kurs));
                Log.d(TAG, "Changed " + kurs.name);
            }
        } else {
            if (!mündlichList.contains(kurs)) {
                mündlichList.add(kurs);
                Log.d(TAG, "mündlichList.add " + kurs.name);
            }
            if (schriftlichList.contains(kurs)) {
                schriftlichList.remove(schriftlichList.indexOf(kurs));
                Log.d(TAG, "Changed " + kurs.name);
            }

        }
    }

    /**
     * @return gibt alle schriftliche Kurse zurück bzw die schriftlichList
     */
    public List<dbKurs> getSchriftlichList() {
        return schriftlichList;
    }

    /**
     * @return gibt alle mündliche Kurse zurück bzw die mündlichLIst
     */
    public List<dbKurs> getMündlichList() {
        return mündlichList;
    }

    /**
     * @param position ist die Position von dem Kurs der entfernt werden soll
     * @removeMS entfernt einen Kurs von der Schriftlich oder Mündlich List
     */
    public void removeMS(int position) {
        dbKurs kurs = kurse.get(position).kurs;
        if (schriftlichList.contains(kurs)) {
            schriftlichList.remove(kurs);
        }
        if (mündlichList.contains(kurs)) {
            mündlichList.remove(kurs);
        }
        Log.d(TAG, "Removed " + kurs.name);
    }

    @Override
    public int getItemViewType(int position) {
        return kurse.get(position).headlineOrKurs ? TYPE_ACTIVE : TYPE_INACTIVE;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType == TYPE_INACTIVE ? R.layout.item_kursauswahl: R.layout.item_kursauswahl_headline;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final kursauswahl kurswahl = kurse.get(position);

        if (kurswahl.headlineOrKurs == true){
            holder.textViewHeadline.setText(kurswahl.headline);
        }else {
            dbKurs kurs = kurswahl.kurs;
            holder.textViewFachid.setText(kurs.name);

            String lehrerString = "";

            if (kurs.lehrer != null) {
                if (kurs.lehrer.name != null) {
                    if (kurs.lehrer.titel != null) {
                        //zeigt den Lehrer nur an wenn er vorhanden ist
                        lehrerString += kurs.lehrer.titel + " ";
                    }
                    lehrerString += kurs.lehrer.name + " ";
                }
            }
            holder.textViewLehrer.setText(lehrerString);
            switch (kurs.kursart.gloablId) {
                case 0:
                    holder.textViewArt.setText("LK");
                    break;
                case 1:
                    holder.textViewArt.setText("GK");
                    break;
                case 2:
                    holder.textViewArt.setText("AG");
                    break;
                case 3:
                    holder.textViewArt.setText("PK");
                    break;
            }

            // Highlight the item if it's selected
            holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return kurse.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewFachid;
        TextView textViewLehrer;
        TextView textViewArt;
        TextView textViewHeadline;
        View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial the Views
            textViewHeadline = (TextView) itemView.findViewById(R.id.textViewHeadline);
            textViewFachid = (TextView) itemView.findViewById(R.id.textViewFachid);
            textViewLehrer = (TextView) itemView.findViewById(R.id.textViewFachlehrer);
            textViewArt = (TextView) itemView.findViewById(R.id.textViewArt);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /**
         * @param v
         * @onClick wird aufgerufen wenn ein Item angeclickt wird
         */
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        /**
         * @param v
         * @onLongClick wird aufgerufen wenn ein Item angeclickt wird
         */
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }

            return false;
        }

        /**
         * Interface for Click Callbacks
         */
        public interface ClickListener {
            public void onItemClicked(int position);

            public boolean onItemLongClicked(int position);
        }
    }
}