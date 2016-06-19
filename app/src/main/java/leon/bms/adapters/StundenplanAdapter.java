package leon.bms.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

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
public class StundenplanAdapter extends UltimateViewAdapter<StundenplanAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

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
        Log.d(TAG, stundenList.size() + " ");
    }

    /**
     * @return gibt die stundenplan Liste zurück
     */
    public List<stunden> getStundenplan() {
        return stundenplan;
    }


    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stundenplan, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stunden stunden = stundenplan.get(position);

        //Sett the Data to the Views
        if (stunden.isActive() == true) {
            if (stunden.getVertretung() != null) {
                //dbVertretung vertretung = stunden.getSchulstunde().getVertretung(stunden.getSchulstunde().getId());
                //stunden.setVertretung(vertretung);
                if (stunden.getVertretung().getEva()) {
                    holder.textViewStunde.setText(String.valueOf(stunden.getStunde()));
                    holder.textViewFachname.setText(stunden.getSchulstunde().getKurs().getName());
                    holder.textViewFachname.setPaintFlags(holder.textViewFachname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    if (stunden.getVertretung().getRaum() != null) {
                        holder.textViewRaum.setText(stunden.getVertretung().getRaum().getName());
                        holder.textViewRaum.setBackgroundColor(Color.parseColor("#ff8002"));
                        holder.textViewRaum.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        holder.textViewRaum.setText(stunden.getSchulstunde().getRaum().getName());
                    }
                    if (stunden.getVertretung().getLehrer() != null) {
                        holder.textViewLehrer.setText(stunden.getVertretung().getLehrer().getTitle() + " "
                                + stunden.getVertretung().getLehrer().getLast_name());
                    } else {
                        holder.textViewLehrer.setText(stunden.getSchulstunde().getKurs().getLehrer().getTitle() + " "
                                + stunden.getSchulstunde().getKurs().getLehrer().getLast_name());
                    }

                } else {
                    if ((holder.textViewFachname.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        holder.textViewFachname.setPaintFlags( holder.textViewFachname.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    holder.textViewFachname.setText(stunden.getSchulstunde().getKurs().getName());
                    holder.textViewStunde.setText(String.valueOf(stunden.getStunde()));
                    if (stunden.getVertretung().getRaum()!= null) {
                        holder.textViewRaum.setText(stunden.getVertretung().getRaum().getName());
                        holder.textViewRaum.setBackgroundColor(Color.parseColor("#ff8002"));
                        holder.textViewRaum.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        holder.textViewRaum.setText(stunden.getSchulstunde().getRaum().getName());
                    }
                    if (stunden.getVertretung().getLehrer() != null) {
                        holder.textViewLehrer.setText(stunden.getVertretung().getLehrer().getTitle() + " "
                                + stunden.getVertretung().getLehrer().getLast_name());
                    } else {
                        holder.textViewLehrer.setText(stunden.getSchulstunde().getKurs().getLehrer().getTitle() + " "
                                + stunden.getSchulstunde().getKurs().getLehrer().getLast_name());
                    }

                }

            } else {
                if ((holder.textViewFachname.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                    holder.textViewFachname.setPaintFlags( holder.textViewFachname.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                holder.textViewRaum.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.textViewRaum.setTextColor(Color.parseColor("#000000"));
                holder.textViewStunde.setText(String.valueOf(stunden.getStunde()));
                holder.textViewFachname.setText(stunden.getSchulstunde().getKurs().getName());
                holder.textViewRaum.setText(stunden.getSchulstunde().getRaum().getName());
                holder.textViewLehrer.setText(stunden.getSchulstunde().getKurs().getLehrer().getTitle() + " "
                        + stunden.getSchulstunde().getKurs().getLehrer().getLast_name());

            }

        } else {
            if ((holder.textViewFachname.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                holder.textViewFachname.setPaintFlags( holder.textViewFachname.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
            holder.textViewRaum.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.textViewRaum.setTextColor(Color.parseColor("#000000"));
            holder.textViewStunde.setText(String.valueOf(stunden.getStunde()));
            holder.textViewFachname.setText("Freistunde");
            holder.textViewLehrer.setText("");
            holder.textViewRaum.setText("");
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return stundenplan.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    /**
     * changes the Data set for another List
     *
     * @param wochentagList ist die neue Liste die mit der alten ersetzt wird
     */
    public void changeStundenplan(List<stunden> wochentagList) {
        //stundenplan = wochentagList;
        //notifyDataSetChanged();

        int wochentagsize = wochentagList.size();
        int currentsize = stundenplan.size();

       for (int i=0;i<currentsize;i++){
           removeStunde(stundenplan.size()-1);
       }
        for (int i=0;i<wochentagsize;i++){
            addStunde(wochentagList.get(i));
        }
    }

    public void changeList(List<stunden> wochentagList){
        int wochentagsize = wochentagList.size();
        int currentsize = stundenplan.size();

        if ( (currentsize-wochentagsize) <0){
            for (int i=0;i<wochentagsize;i++){
                if (i < currentsize){
                    changeStunde(wochentagList.get(i),i);
                }else {
                    addStunde(wochentagList.get(i));
                }
            }
        }else {
            for (int i=0;i<currentsize;i++){
                if (i < wochentagsize){
                    changeStunde(wochentagList.get(i),i);
                }else {
                    removeStunde(stundenplan.size()-1);
                }
            }
        }
    }
    public void changeStunde(stunden stunden1,int position){
        stundenplan.remove(position);
        stundenplan.add(position,stunden1);
        notifyItemChanged(position);
    }
    public void addStunde(stunden stunden1){
        stundenplan.add(stunden1);
        notifyItemInserted(stundenplan.size()-1);
    }
    public void removeStunde(int position){
        stundenplan.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewFachname;
        TextView textViewLehrer;
        TextView textViewRaum;
        TextView textViewStunde;
        CardView cardView;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //intial Views
            textViewStunde = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewRaum = (TextView) itemView.findViewById(R.id.textViewRaum);
            textViewLehrer = (TextView) itemView.findViewById(R.id.textViewLehrer);
            textViewFachname = (TextView) itemView.findViewById(R.id.textViewStunde);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

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