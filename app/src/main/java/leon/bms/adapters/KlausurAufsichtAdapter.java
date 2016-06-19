package leon.bms.adapters;

/**
 * Created by Leon E on 24.05.2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import leon.bms.R;
import leon.bms.realm.dbKlausuraufsicht;


/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @KursAdapter wird gebraucht für den recylcerview zum anzeigen der Kurse für die KurseActivity.
 */
public class KlausurAufsichtAdapter extends RecyclerView.Adapter<KlausurAufsichtAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KlausurAufsichtAdapter.class.getSimpleName();


    private List<dbKlausuraufsicht> klausuraufsichtList;
    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener       wird gebraucht um auf Click events zu reagieren
     * @param klausuraufsichtList ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KlausurAufsichtAdapter(ViewHolder.ClickListener clickListener, List<dbKlausuraufsicht> klausuraufsichtList) {
        super();
        this.clickListener = clickListener;
        this.klausuraufsichtList = klausuraufsichtList;

    }

    public List<dbKlausuraufsicht> getKlausuraufsichtList() {
        return klausuraufsichtList;
    }


    /**
     * @param parent
     * @param viewType ist der anzuzeigen Type also hier item_kurs
     * @return
     * @onCreatViewHolder legt das design für ein "Item" fest
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_klausuraufsicht;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final dbKlausuraufsicht klausuraufsicht = klausuraufsichtList.get(position);

        //Setting the Data to the Views
        holder.textViewLehrer.setText(klausuraufsicht.getLehrer().getTitle()+" "+klausuraufsicht.getLehrer().getLast_name());
        holder.textViewZeit.setText(getZeitString(klausuraufsicht));

    }

    public String getZeitString(dbKlausuraufsicht klausuraufsicht){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            calendar.setTime(myFormat.parse(klausuraufsicht.getStart()));
            calendar2.setTime(myFormat.parse(klausuraufsicht.getEnd()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("HH:mm" );

        return sdfmt.format(calendar.getTime())+" Uhr\n"+sdfmt.format(calendar2.getTime())+" Uhr";
    }

    /**
     * @return gibt die Größe der Liste zurück
     */
    @Override
    public int getItemCount() {
        return klausuraufsichtList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewLehrer, textViewZeit;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewLehrer = (TextView) itemView.findViewById(R.id.textViewLehrer);
            textViewZeit = (TextView) itemView.findViewById(R.id.textViewZeit);

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
