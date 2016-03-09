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
public class KursAdapter extends RecyclerView.Adapter<KursAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private List<stunden> stundenplan;


    private ViewHolder.ClickListener clickListener;

    public KursAdapter(ViewHolder.ClickListener clickListener, List<stunden> stundenList) {
        super();
        this.clickListener = clickListener;
        this.stundenplan = stundenList;
        Log.d(TAG, stundenList.size() + " ");
    }

    public List<stunden> getStundenplan() {
        return stundenplan;
    }


    @Override
    public int getItemViewType(int position) {
        final stunden stunden = stundenplan.get(position);
        return stunden.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType = R.layout.item_kurs;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stunden stunden = stundenplan.get(position);

        //Setting text view title
        holder.textViewRaum.setText(stunden.getRaum());
        holder.textViewWeek.setText(stunden.wochentag+", "+stunden.stunde+". Std.");
        holder.textViewTime.setText(stunden.timeString);


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
        TextView textViewTime, textViewWeek, textViewRaum;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            textViewTime = (TextView) itemView.findViewById(R.id.textViewZeit);
            textViewRaum = (TextView) itemView.findViewById(R.id.textViewRaum);
            textViewWeek = (TextView) itemView.findViewById(R.id.textViewWeek);


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