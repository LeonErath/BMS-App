package leon.bms.adapters;

/**
 * Created by Leon E on 24.05.2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import leon.bms.R;
import leon.bms.realm.dbKlausurinhalt;


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
    private Context context;

    /**
     * @param clickListener     wird gebraucht um auf Click events zu reagieren
     * @param klausurinhaltList ist die Liste die angezeigt werden soll
     * @KursAdapter ClickListener und anzuzeigende Liste wird übergeben.
     */
    public KlausurInhaltAdapter(ViewHolder.ClickListener clickListener, List<dbKlausurinhalt> klausurinhaltList, Context context) {
        super();
        this.clickListener = clickListener;
        this.klausurinhaltList = klausurinhaltList;
        this.context = context;
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
        holder.textViewInhalt.setText(klausurinhalt.getBeschreibung());
        if (klausurinhalt.isErledigt()) {
            holder.imageViewErledigt.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewErledigt.setVisibility(View.INVISIBLE);
        }

    }

    public void changeErledigt(int position) {
        final dbKlausurinhalt klausurinhalt = klausurinhaltList.get(position);
        if (klausurinhalt.isErledigt()) {
            klausurinhalt.setErledigt(false);
            save(klausurinhalt);
        } else {
            klausurinhalt.setErledigt(true);
            save(klausurinhalt);
        }
        notifyItemChanged(position);
    }

    private void save(final RealmObject object) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                bgrealm.copyToRealmOrUpdate(object);
                Log.d("Fragment_Kursauswahl", "Saved Object");
            }
        });
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
