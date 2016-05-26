package leon.bms.adapters;

/**
 * Created by Leon E on 13.02.2016.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import leon.bms.R;
import leon.bms.model.nachrichten;


/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @WebsiteArticleAdapter Adapter für das Anzeigen der einzelnen artikel in einem RecylcerView.
 */
public class NachrichtenAdapter extends UltimateViewAdapter<NachrichtenAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();


    private List<nachrichten> nachrichtenList = new ArrayList<>();


    private ViewHolder.ClickListener clickListener;
    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param nachrichten   Liste der zu anzeigenden Artikel wird übergeben
     */
    public NachrichtenAdapter(ViewHolder.ClickListener clickListener, List<nachrichten> nachrichten) {
        super();
        this.clickListener = clickListener;
        this.nachrichtenList = nachrichten;
        Log.d(TAG, nachrichten.size() + " ");


    }

    public List<nachrichten> getArtikelList() {
        return nachrichtenList;
    }

    /**
     * @addArticle Methode zum hinzufügen eines Artikel zum Anzeigen wenn er nicht schon in der Liste
     * vorhande ist
     * @param nachrichten ist der zu hinzufügende Artikel
     */


    /**
     * @changeDataSet Methode zum Austausch der Daten
     * @param nachrichtens alte Liste wird duch die neue Komplett ersetzt
     */
    public void changeDataSet(List<nachrichten> nachrichtens) {

        int nachrichtenSize = nachrichtenList.size();
       for (int i= 0;i<nachrichtenSize;i++){
           removeArticle(nachrichtenList.size()-1);
       }
        for (int i= 0;i<nachrichtens.size();i++){
            addArticle(nachrichtens.get(i));
        }

    }
    public void addArticle(nachrichten nachrichten) {
        nachrichtenList.add(nachrichten);
        notifyItemInserted(nachrichtenList.size() - 1);
    }
    public void removeArticle(int position) {
        nachrichtenList.remove(position);
        notifyItemRemoved(position);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nachrichten, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(NachrichtenAdapter.ViewHolder holder, int position) {
        final nachrichten nachrichten =  nachrichtenList.get(position);

        //Set the Data to the Views
        holder.textViewHeadline.setText(nachrichten.titel);
        holder.textViewDate.setText("Zuletzt gändert am: "+nachrichten.dateString);
        holder.textViewContent.setText(nachrichten.nachricht);

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
        return nachrichtenList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }


    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewHeadline;
        TextView textViewDate;
        TextView textViewContent;



        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewHeadline = (TextView) itemView.findViewById(R.id.textViewTitel);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);


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
