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

import java.util.List;

import leon.bms.R;
import leon.bms.database.dbKlausuraufsicht;
import leon.bms.database.dbKlausurinhalt;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @KursAdapter wird gebraucht für den recylcerview zum anzeigen der Kurse für die KurseActivity.
 */
public class KlausurInhaltAdapter extends RecyclerView.Adapter<KlausurInhaltAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KlausurAufsichtAdapter.class.getSimpleName();


    private List<dbKlausurinhalt> klausurinhaltList;
    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener     wird gebraucht um auf Click events zu reagieren
     * @param klausurinhaltList ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KlausurInhaltAdapter(ViewHolder.ClickListener clickListener, List<dbKlausurinhalt> klausurinhaltList) {
        super();
        this.clickListener = clickListener;
        this.klausurinhaltList = klausurinhaltList;

    }

    public List<dbKlausurinhalt> getKlausuraufsichtList() {
        return klausurinhaltList;
    }


    /**
     * @param parent
     * @param viewType ist der anzuzeigen Type also hier item_kurs
     * @return
     * @onCreatViewHolder legt das design für ein "Item" fest
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_klausurinhalt;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final dbKlausurinhalt klausurinhalt = klausurinhaltList.get(position);

        //Setting the Data to the Views
        holder.textViewInhalt.setText(klausurinhalt.beschreibung);
        if (klausurinhalt.erledigt) {
            holder.imageViewErledigt.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewErledigt.setVisibility(View.INVISIBLE);
        }

    }

    public void changeErledigt(int position) {
        final dbKlausurinhalt klausurinhalt = klausurinhaltList.get(position);
        if (klausurinhalt.erledigt) {
            klausurinhalt.erledigt = false;
            klausurinhalt.save();
        } else {
            klausurinhalt.erledigt = true;
            klausurinhalt.save();
        }
        notifyItemChanged(position);
    }


    /**
     * @return gibt die Größe der Liste zurück
     */
    @Override
    public int getItemCount() {
        return klausurinhaltList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewInhalt;
        ImageView imageViewErledigt;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewInhalt = (TextView) itemView.findViewById(R.id.textViewInhalt);
            imageViewErledigt = (ImageView) itemView.findViewById(R.id.imageViewErledigt);

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
                listener.onItemClicked2(getAdapterPosition());
            }
        }

        /**
         * @param v
         * @onLongClick wird aufgerufen wenn ein Item angeclickt wird
         */
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked2(getAdapterPosition());
            }

            return false;
        }

        /**
         * Interface for Click Callbacks
         */
        public interface ClickListener {
            public void onItemClicked2(int position);

            public boolean onItemLongClicked2(int position);
        }
    }
}
