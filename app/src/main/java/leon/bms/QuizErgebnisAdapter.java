package leon.bms;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @QuizErgebnisAdapter Adapter der die beantworteten Fragen anzeigen sollen.
 */
public class QuizErgebnisAdapter extends RecyclerView.Adapter<QuizErgebnisAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<quizfragen> fragenList;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param fragenList    Liste der zu anzeigenden Fragen wird übergeben
     */
    public QuizErgebnisAdapter(ViewHolder.ClickListener clickListener, List<quizfragen> fragenList) {
        super();
        this.clickListener = clickListener;
        this.fragenList = fragenList;
        Log.d(TAG, fragenList.size() + " ");
    }

    public List<quizfragen> getFragenList() {
        return fragenList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_quizergebnis;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final quizfragen fragen = fragenList.get(position);

        //set the Data to the views
        holder.textViewFrage.setText(fragen.getFrage());
        holder.textViewFrageID.setText("Frage ID: "+String.valueOf(fragen.getFradeID()));
        if (fragen.richtigOderFalsch == true){
            holder.container.setBackgroundColor(Color.parseColor("#b7d167"));
            holder.imageView.setImageResource(R.drawable.ic_done_white_48dp);
            holder.imageView.setBorderColor(Color.parseColor("#ffffff"));
            holder.imageView.setBorderWidth(1);
        }else {
            holder.container.setBackgroundColor(Color.parseColor("#e30613"));
            holder.imageView.setImageResource(R.drawable.ic_clear_white_48dp);
            holder.imageView.setBorderColor(Color.parseColor("#ffffff"));
            holder.imageView.setBorderWidth(1);
        }

    }

    /**
     * @return gibt die Größe der fragenListe zurück
     */
    @Override
    public int getItemCount() {
        return fragenList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewFrage,textViewFrageID;
        CircleImageView imageView;
        CardView container;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial view
            textViewFrage= (TextView) itemView.findViewById(R.id.textViewFrage);
            textViewFrageID = (TextView) itemView.findViewById(R.id.textViewFrageID);
            imageView = (CircleImageView) itemView.findViewById(R.id.imageViewIcon);
            container = (CardView) itemView.findViewById(R.id.container);


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