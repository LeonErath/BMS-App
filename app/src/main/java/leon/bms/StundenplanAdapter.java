package leon.bms;

import android.support.v4.app.FragmentActivity;
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
public class StundenplanAdapter extends RecyclerView.Adapter<StundenplanAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<stunden> stundenplan;


    private ViewHolder.ClickListener clickListener;

    public StundenplanAdapter(ViewHolder.ClickListener clickListener, List<stunden> stundenList) {
        super();
        this.clickListener = clickListener;
        this.stundenplan = stundenList;
        Log.d(TAG,stundenList.size()+" ");
    }




    @Override
    public int getItemViewType(int position) {
        final stunden stunden = stundenplan.get(position);
        return stunden.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType  == TYPE_INACTIVE ? R.layout.item_stundenplan_leer : R.layout.item_stundenplan;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stunden stunden = stundenplan.get(position);

        //Setting text view title
        if (stunden.isActive()==true){
            holder.textViewFachname.setText(stunden.getStundenname());
            holder.textViewStunde.setText(stunden.getStunde());
            holder.textViewLehrer.setText(stunden.getLehrer());
            holder.textViewRaum.setText(stunden.getRaum());
        }else {
            holder.textViewStunde.setText(stunden.getStunde());

        }



    }

    @Override
    public int getItemCount() {
        return stundenplan.size();
    }

    public void changeWeekDay(List<stunden> finalMontagList) {
        stundenplan = finalMontagList;
       notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        TextView textViewFachname;
        TextView textViewLehrer;
        TextView textViewRaum;
        TextView textViewStunde;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            textViewStunde = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewRaum= (TextView) itemView.findViewById(R.id.textViewRaum);
            textViewLehrer= (TextView) itemView.findViewById(R.id.textViewLehrer);
            textViewFachname= (TextView) itemView.findViewById(R.id.textViewStunde);

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