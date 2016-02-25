package leon.bms;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon E on 21.01.2016.
 */
public class KursauswahlAdapter extends SelectableAdapter<KursauswahlAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();



    private List<dbKurs> kurse;
    private List<dbKurs> schriftlichList;
    private List<dbKurs> mündlichList;

    private ViewHolder.ClickListener clickListener;

    public KursauswahlAdapter(ViewHolder.ClickListener clickListener, List<dbKurs> kursList) {
        super();
        this.clickListener = clickListener;
        this.kurse = kursList;
        Log.d(TAG,kursList.size()+" ");

        schriftlichList = new ArrayList<>();
        mündlichList = new ArrayList<>();

    }



    public void switchMS(int position,boolean schriftlich){
        dbKurs kurs = kurse.get(position);


                if (schriftlich == true) {
                    if (!schriftlichList.contains(kurs)) {
                        schriftlichList.add(kurs);
                        Log.d(TAG, "schriftlichList.add " + kurs.name);
                    }
                    if (mündlichList.contains(kurs)){
                        mündlichList.remove(mündlichList.indexOf(kurs));
                        Log.d(TAG, "Changed " + kurs.name);
                    }
                } else {
                    if (!mündlichList.contains(kurs)) {
                        mündlichList.add(kurs);
                        Log.d(TAG, "mündlichList.add " + kurs.name);
                    }
                    if (schriftlichList.contains(kurs)){
                        schriftlichList.remove(schriftlichList.indexOf(kurs));
                        Log.d(TAG, "Changed " + kurs.name);
                    }

                }
    }
    public List<dbKurs> getSchriftlichList(){
        return schriftlichList;
    }
    public List<dbKurs> getMündlichList(){
        return mündlichList;
    }


    public void removeMS (int position){
        dbKurs kurs = kurse.get(position);
        if (schriftlichList.contains(kurs)) {
            schriftlichList.remove(kurs);
        }
        if (mündlichList.contains(kurs)) {
            mündlichList.remove(kurs);
        }
        Log.d(TAG, "Removed "+kurs.name);
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kursauswahl, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final dbKurs kurs = kurse.get(position);

        //Setting text view title
        holder.textViewFachname.setText(kurs.fach);
        holder.textViewFachid.setText(kurs.name);

        String lehrerString="";

        if (kurs.lehrer!=null){
            if (kurs.lehrer.name!= null){
                if (kurs.lehrer.titel!=null){
                    lehrerString+= kurs.lehrer.titel+" ";
                }
                lehrerString+= kurs.lehrer.name+" ";
            }
        }
        holder.textViewLehrer.setText(lehrerString);
        switch (kurs.kursart){
            case 0:holder.textViewArt.setText("LK");break;
            case 1:holder.textViewArt.setText("GK");break;
            case 2:holder.textViewArt.setText("AG");break;
            case 3:holder.textViewArt.setText("PK");break;
        }

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return kurse.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        TextView textViewFachname;
        TextView textViewFachid;
        TextView textViewLehrer;
        TextView textViewArt;
        View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            textViewFachname = (TextView) itemView.findViewById(R.id.textViewFachname);
            textViewFachid = (TextView) itemView.findViewById(R.id.textViewFachid);
            textViewLehrer= (TextView) itemView.findViewById(R.id.textViewFachlehrer);
            textViewArt= (TextView) itemView.findViewById(R.id.textViewArt);
            selectedOverlay =  itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }

            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }
    }
}