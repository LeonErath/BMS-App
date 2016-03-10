package leon.bms;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @QuizKursAdaoter Adapter zum Anzeigen der Kurs für das Quiz welche einen Themenbereich besitzen
 */
public class QuizKursAdapter extends RecyclerView.Adapter<QuizKursAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<quizkurs> quizkursList;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param quizkursList  Liste der zu anzeigenden Kursen wird übergeben
     */
    public QuizKursAdapter(ViewHolder.ClickListener clickListener, List<quizkurs> quizkursList) {
        super();
        this.clickListener = clickListener;
        this.quizkursList = quizkursList;
        Log.d(TAG, quizkursList.size() + " ");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_quiz_kursauswahl;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final quizkurs quizkurs = quizkursList.get(position);

        //set the Data to the Views
        if (quizkurs.kursFach != null && quizkurs.kursFach != "") {
            holder.textViewKurs.setText(quizkurs.kursFach);
        }
        if (quizkurs.lehrer != null && quizkurs.lehrer != "") {
            holder.textViewLehrer.setText(quizkurs.lehrer);
        }
        if (quizkurs.kursId != null && quizkurs.kursId != "") {
            holder.textViewKursID.setText(quizkurs.kursId);
        }
        holder.textViewFragen.setText(String.valueOf(quizkurs.fragen) + " F.");
        holder.textViewDatum.setText(quizkurs.datum);


    }

    /**
     * @return die Größe der quizkurs Liste
     */
    @Override
    public int getItemCount() {
        return quizkursList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewKurs;
        TextView textViewLehrer;
        TextView textViewKursID;
        TextView textViewFragen;
        TextView textViewDatum;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewKurs = (TextView) itemView.findViewById(R.id.textViewKurs);
            textViewLehrer = (TextView) itemView.findViewById(R.id.textViewLehrer);
            textViewKursID = (TextView) itemView.findViewById(R.id.textViewKursID);
            textViewFragen = (TextView) itemView.findViewById(R.id.textViewFragen);
            textViewDatum = (TextView) itemView.findViewById(R.id.textViewDatum);

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