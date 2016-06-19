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
import leon.bms.model.fehler;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * Dieseer Adapter stellt die nicht erledigten Aufgaben in einer Liste da
 * Er vermittel sozusagen die Daten an den RecyclerView
 * Sie beinhaltet einer weitere Klasse ViewHolder die sich um den View kümmert ,also
 * dem aussehen und die Verbindung zum Item layout
 */
public class FehlerAdapter extends RecyclerView.Adapter<FehlerAdapter.ViewHolder> {
    // @TAG hier wird der Tag der Klasse gespeichert. Nacher wichtig für die Log-Datein
    private static final String TAG = FehlerAdapter.class.getSimpleName();
    // Liste mit den nicht erledigten Aufgaben
    private List<fehler> fehlerList;
    // @clickListenere wird immmer bei einem click auf ein Item ausgelöst
    private ViewHolder.ClickListener clickListener;
    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;
    private Context context;

    /**
     * @Constructor benötigt einmal einen clickListener der in der Klasse implentiert sein muss
     * und die Liste der nichterledigten Aufgaben die angezeigt werden sollen.
     */
    public FehlerAdapter(ViewHolder.ClickListener clickListener, List<fehler> fehlerList,Context context) {
        super();
        this.clickListener = clickListener;
        this.fehlerList = fehlerList;
        this.context = context;
    }

    /**
     * @changeDataSet will be used not often only if the whole data was changed
     */
    public void changeDataSet(List<fehler> fehlerList) {
        this.fehlerList = fehlerList;
        notifyDataSetChanged();
    }


    /**
     * @addAufgabe get called if you want to add a Aufgabe
     * checks if the adaoterList contains this Aufgabe
     * When not it will add the Aufgabe to the list
     * @notifyItemInsert for a nice Animation and updates the UI
     */
    public void addAufgabe(fehler fehler1) {
        boolean contains = false;
        for (fehler fehlerInList : fehlerList) {
            if (fehler1 == fehlerInList) {
                contains = true;
            }
        }
        if (contains == false) {
            if (fehlerList.size() != 1){
                fehlerList.add(fehlerList.size()-1,fehler1);
                notifyItemInserted(fehlerList.size()-1);
            }else {
                fehlerList.add(0,fehler1);
                notifyItemInserted(0);
            }

        }
    }
    public void changeErledigt(int position) {
        final fehler fehler1 = fehlerList.get(position);
        if (fehler1.getFehler().getBearbeitet()) {
            fehler1.getFehler().setBearbeitet(false);
            save(fehler1.getFehler());
        } else {
            fehler1.getFehler().setBearbeitet(true);
            save(fehler1.getFehler());
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


    @Override
    public int getItemViewType(int position) {
        final fehler fehler1 = fehlerList.get(position);
        return fehler1.isStatus() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }


    /**
     * @onCreateViewHolder stellt die Verbindung zum item layout her
     * changes layout when the aufgabe is done or not done
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType == TYPE_INACTIVE ? R.layout.item_klausurfehler : R.layout.item_klausuraddfehler;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener, fehlerList);
    }

    /**
     * @onBindViewHolder ist die Verbindung von den Daten der Liste zu dem View also der Anzeige
     * Die Daten werden aus der Liste geladen und an den ViewHolder und den entsprechenden
     * Elemente übergeben , sodass nach die richtigen Daten an den richtigen "Views" sind
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final fehler fehler1 = fehlerList.get(position);

        // Die Inhalte der Aufgabe werden angezeigt
        if (fehler1.isStatus()){

        }else {
            holder.textViewFehler.setText(fehler1.getFehler().getBeschreibung());
            if (fehler1.getFehler().getBearbeitet()) {
                holder.imageViewErledigt.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewErledigt.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void deleteFehler(int position){
        if (position>=0 && position < getItemCount()){
            fehlerList.remove(position);
            notifyItemChanged(position);
        }
    }

    public fehler getFehler(int position){
        if (position>=0 && position < getItemCount()){
            return fehlerList.get(position);
        }
        return null;
    }


    /**
     * @getItemCount() ist wichtig für die Funktionalität des RecyclerViews
     * Gibt die größe der Liste zurück bzw. wie viele Items der RecylcerView beinhaltet
     */
    @Override
    public int getItemCount() {
        return fehlerList.size();
    }


    /**
     * @ViewHolder stellt die Verbindung zum layout her
     * Hier werden die einzelnen Views definiert und initialisiert
     * @View.OnClickListener wird implentiert um auf Item Clicks zu reagieren
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        // TAG wird wieder mit dem Namen der Klasse gespeichert für spätere Log-Datein
        private static final String TAG = ViewHolder.class.getSimpleName();
        // Hier werden alle Views des Layout definiert die nacher wichtig sind
        TextView textViewFehler;
        ImageView imageViewErledigt;
        List<fehler> fehlerList;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener, List<fehler> fehlerList) {
            super(itemView);

            // Hier werden alle View intilisiert bzw ihre Verbindung zugewiesen
            textViewFehler = (TextView) itemView.findViewById(R.id.textViewFehler);
            imageViewErledigt = (ImageView) itemView.findViewById(R.id.imageViewErledigt);

            //listener wird initialisiert

            this.listener = listener;
            this.fehlerList = fehlerList;
            // Methode des ClickListeners werden implentiert
            // @onClick wird ausgelöst bei einem kurzem Click
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // Methode des ClickListeners werden implentiert
        // @onClick wird ausgelöst bei einem kurzem Click
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked3(getAdapterPosition());
            }
        }

        //@onLongClick wird ausgelöst bei einem langen Click
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked3(getAdapterPosition());
            }

            return false;
        }

        /**
         * @inteface ClickListener wird gebraucht um die clickEvents in die Klasse zu integrieren
         * beinhalten zwei Click Methoden
         */
        public interface ClickListener {
            public void onItemClicked3(int position);

            public boolean onItemLongClicked3(int position);
        }
    }
}
