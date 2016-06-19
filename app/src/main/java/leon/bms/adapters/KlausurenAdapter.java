package leon.bms.adapters;

/**
 * Created by Leon E on 24.05.2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import leon.bms.R;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbKlausur;
import leon.bms.realm.dbNote;


public class KlausurenAdapter extends RecyclerView.Adapter<KlausurenAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KlausurenAdapter.class.getSimpleName();
    String[] notenArray = {"5-", "5", "5+", "4-", "4", "4+", "3-", "3", "3+", "2-", "2", "2+", "1-", "1", "1+"};

    private List<dbKlausur> klausurList;
    private ViewHolder.ClickListener clickListener;
    RealmQueries realmQueries;

    /**
     * @param clickListener wird gebraucht um auf Click events zu reagieren
     * @param klausurList   ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KlausurenAdapter(ViewHolder.ClickListener clickListener, List<dbKlausur> klausurList, Context context) {
        super();
        this.clickListener = clickListener;
        this.klausurList = klausurList;
        realmQueries = new RealmQueries(context);

    }

    public List<dbKlausur> getKlausurList() {
        return klausurList;
    }


    /**
     * @param parent
     * @param viewType ist der anzuzeigen Type also hier item_kurs
     * @return
     * @onCreatViewHolder legt das design für ein "Item" fest
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_klausuren;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final dbKlausur klausur = klausurList.get(position);
        holder.textViewKlausurenname.setText(klausur.getName());
        holder.textViewDatum.setText(getDateString(klausur));
        if (realmQueries.getNoteFromKlausur(klausur)!=null){
            dbNote note = realmQueries.getNoteFromKlausur(klausur);
            holder.textViewNotePunkte.setText(note.getPunkte()+" P.");
            holder.textViewNote.setText(notenArray[note.getPunkte()-1]);
            holder.textViewKeineNote.setVisibility(View.INVISIBLE);
            if (note.getPunkte() <= 4){
                holder.imageViewAchtung.setVisibility(View.VISIBLE);
            }else {
                holder.imageViewAchtung.setVisibility(View.INVISIBLE);
            }
        }else {
            holder.textViewKeineNote.setVisibility(View.VISIBLE);
        }
        //Setting the Data to the Views

    }


    public String getDateString(dbKlausur klausur){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(klausur.getDatum()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy " );

        return sdfmt.format(calendar.getTime());
    }

    public int getKlausurId(int position) {
        final dbKlausur klausur = klausurList.get(position);
       return klausur.getServerid();
    }




    /**
     * @return gibt die Größe der Liste zurück
     */
    @Override
    public int getItemCount() {
        return klausurList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        TextView textViewKlausurenname,textViewDatum,textViewNote,textViewNotePunkte,textViewKeineNote;
        ImageView imageViewAchtung;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial views
            textViewKlausurenname = (TextView) itemView.findViewById(R.id.textViewKlausurname);
            textViewDatum = (TextView) itemView.findViewById(R.id.textViewDatum);
            textViewNote = (TextView) itemView.findViewById(R.id.textViewNote);
            textViewKeineNote = (TextView) itemView.findViewById(R.id.textViewKeineNote);
            textViewNotePunkte = (TextView) itemView.findViewById(R.id.textViewNotePunkte);
            imageViewAchtung = (ImageView) itemView.findViewById(R.id.imageViewAchtung);

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

