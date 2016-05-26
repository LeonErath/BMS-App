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
 * @KursAdapter wird gebraucht für den recylcerview zum anzeigen der Kurse für die KurseActivity.
 */
public class KursAdapter extends RecyclerView.Adapter<KursAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<stunden> stundenplan;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener wird gebraucht um auf Click events zu reagieren
     * @param stundenList   ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KursAdapter(ViewHolder.ClickListener clickListener, List<stunden> stundenList) {
        super();
        this.clickListener = clickListener;
        this.stundenplan = stundenList;
        Log.d(TAG, stundenList.size() + " ");
    }

    public List<stunden> getStundenplan() {
        return stundenplan;
    }


    @Override
    public int getItemViewType(int position) {
        final stunden stunden = stundenplan.get(position);
        return stunden.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }

    /**
     * @param parent
     * @param viewType ist der anzuzeigen Type also hier item_kurs
     * @return
     * @onCreatViewHolder legt das design für ein "Item" fest
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_kurs;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stunden stunden = stundenplan.get(position);
        // TODO aktualisieren
        //Setting the Data to the Views
        //holder.textViewRaum.setText(stunden.getRaum());
        //holder.textViewWeek.setText(stunden.wochentag + ", " + stunden.stunde + ". Std.");
        //holder.textViewTime.setText(stunden.timeString);


    }

    /**
     * @return gibt die Größe der Liste zurück
     */
    @Override
    public int getItemCount() {
        return stundenplan.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewTime, textViewWeek, textViewRaum;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewTime = (TextView) itemView.findViewById(R.id.textViewZeit);
            textViewRaum = (TextView) itemView.findViewById(R.id.textViewRaum);
            textViewWeek = (TextView) itemView.findViewById(R.id.textViewWeek);


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