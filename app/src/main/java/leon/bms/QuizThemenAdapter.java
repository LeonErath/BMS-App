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
 * @QuizThemenAdapter Adapter zum Anzeigen die Themenbereiche des Quizes
 */
public class QuizThemenAdapter extends RecyclerView.Adapter<QuizThemenAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<quizthemen> quizthemenList;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param quizthemens   Liste der zu anzeigenden Themenbereich wird übergeben
     */
    public QuizThemenAdapter(ViewHolder.ClickListener clickListener, List<quizthemen> quizthemens) {
        super();
        this.clickListener = clickListener;
        this.quizthemenList = quizthemens;
        Log.d(TAG, quizthemenList.size() + " ");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_quiz_themenauswahl;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final quizthemen quizthemen = quizthemenList.get(position);

        //Set the Data to the views
        holder.textViewThemen.setText(quizthemen.themenbereich);
        holder.textViewLehrer.setText(quizthemen.lehrer);
        holder.textViewKursID.setText(quizthemen.kursId);
        holder.textViewFragen.setText(String.valueOf(quizthemen.fragen) + " F.");
        holder.textViewDatum.setText(quizthemen.datum);


    }

    /**
     * @return Gibt die Größe der Liste zurück
     */
    @Override
    public int getItemCount() {
        return quizthemenList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewThemen;
        TextView textViewLehrer;
        TextView textViewKursID;
        TextView textViewFragen;
        TextView textViewDatum;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewThemen = (TextView) itemView.findViewById(R.id.textViewThemen);
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
