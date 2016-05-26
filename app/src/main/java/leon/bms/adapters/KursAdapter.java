package leon.bms.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    Calendar calendar = Calendar.getInstance();
    GregorianCalendar[] calendars = new GregorianCalendar[]{
            new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 8, 0, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 8, 45, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 9, 35, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 10, 45, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 11, 35, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 12, 25, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 13, 30, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 14, 15, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 15, 0, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 15, 45, 0)
            , new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 16, 30, 0)
    };


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

        holder.textViewRaum.setText(stunden.getSchulstunde().raum.nummer);
        holder.textViewWeek.setText(getWochentagString(stunden.getSchulstunde().wochentag)+ ", " + stunden.getSchulstunde().getBeginnzeit() + ". Std.");
        Calendar calendar = calendars[stunden.getSchulstunde().beginnzeit];
        Calendar calendar2 = calendars[stunden.getSchulstunde().beginnzeit+1];


        holder.textViewTime.setText(getDateString(calendar)+" - "+getDateString(calendar2));


    }

    public String getDateString(Calendar calendar){

        SimpleDateFormat sdfmt = new SimpleDateFormat("HH:mm" );
        return sdfmt.format(calendar.getTime())+" Uhr";
    }

    public String getWochentagString(int day){
        switch (day){
            case 1: return "Montag";
            case 2: return "Dienstag";
            case 3: return "Mittwoch";
            case 4: return "Donnerstag";
            case 5: return "Freitag";
        }
        return null;
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