package leon.bms.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import leon.bms.R;
import leon.bms.model.stunden;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @StundenplanAdapter ist dafür da den Stundenplan anzuzeigen. Hier bei wird zwischen zwei viewtypes
 * unterschieden einmal für normale stunden und einaml für freistunden. Deshalb wird für jedes Item der
 * ViewType überprüft.
 */
public class StundenplanAdapter extends RecyclerView.Adapter<StundenplanAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<stunden> stundenplan;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param stundenList   Liste der zu anzeigenden stunden wird übergeben
     */
    public StundenplanAdapter(ViewHolder.ClickListener clickListener, List<stunden> stundenList) {
        super();
        this.clickListener = clickListener;
        this.stundenplan = stundenList;
        Log.d(TAG,stundenList.size()+" ");
    }

    /**
     * @return gibt die stundenplan Liste zurück
     */
    public List<stunden> getStundenplan(){
        return stundenplan;
    }


    /**
     * @param position Position der Stunde in der stundenplan liste
     * @return gibt an ob die Stunde ein Freistunde oder normale Stunde ist
     */
    @Override
    public int getItemViewType(int position) {
        final stunden stunden = stundenplan.get(position);
        return stunden.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }

    /**
     * Hier wird zwischen zwei layouts gewechselt falls die Stunden keine normale sondern ein frei
     * Stunde ist
     * @param parent
     * @param viewType wechselt je nach TYPE_INACTIVE
     * @return das layout füur das item zurück
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType  == TYPE_INACTIVE ? R.layout.item_stundenplan_leer : R.layout.item_stundenplan;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stunden stunden = stundenplan.get(position);

        //Sett the Data to the Views
        if (stunden.isActive()==true){
            holder.textViewFachname.setText(stunden.getStundenname());
            holder.textViewStunde.setText(stunden.getStunde());
            holder.textViewLehrer.setText(stunden.getLehrer());
            holder.textViewRaum.setText(stunden.getRaum());
        }else {
            holder.textViewStunde.setText(stunden.getStunde());

        }



    }

    /**
     * @return gibt die Größe der stundenplan Liste zurück
     */
    @Override
    public int getItemCount() {
        return stundenplan.size();
    }

    /**
     * changes the Data set for another List
     * @param finalMontagList ist die neue Liste die mit der alten ersetzt wird
     */
    public void changeWeekDay(List<stunden> finalMontagList) {
        stundenplan = finalMontagList;
       notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewFachname;
        TextView textViewLehrer;
        TextView textViewRaum;
        TextView textViewStunde;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //intial Views
            textViewStunde = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewRaum= (TextView) itemView.findViewById(R.id.textViewRaum);
            textViewLehrer= (TextView) itemView.findViewById(R.id.textViewLehrer);
            textViewFachname= (TextView) itemView.findViewById(R.id.textViewStunde);

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