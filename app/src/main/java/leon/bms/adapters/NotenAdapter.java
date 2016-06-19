package leon.bms.adapters;

/**
 * Created by Leon E on 24.05.2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import leon.bms.R;
import leon.bms.model.notenModel;

/**
 * Created by Leon E on 22.05.2016.
 */
public class NotenAdapter extends UltimateViewAdapter<NotenAdapter.ViewHolder> {

    Context context;
    List<notenModel> notenModelList;
    public ViewHolder.ClickListener clickListener;

    public NotenAdapter(Context context, List<notenModel> notenModelList, ViewHolder.ClickListener listener) {
        this.context = context;
        this.notenModelList = notenModelList;
        this.clickListener = listener;

    }

    public notenModel get(int position) {
        if (position >= 0 && position < getAdapterItemCount()) {
            return notenModelList.get(position);
        }
        return null;
    }

    @Override
    public NotenAdapter.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public NotenAdapter.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public NotenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noten, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public int getAdapterItemCount() {
        return notenModelList.size();
    }

    public void changeDataSet(List<notenModel> notenModelList) {
        this.notenModelList = notenModelList;
        notifyDataSetChanged();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(NotenAdapter.ViewHolder holder, int position) {
        //Set the data
        notenModel notenModel1 = notenModelList.get(position);
        holder.textViewKurs.setText(notenModel1.getKurs().getName());
        holder.textViewLehrer.setText(notenModel1.getKurs().getLehrer().getTitle()+" "+notenModel1.getKurs().getLehrer().getLast_name());
        int schriftlichSize = 0;
        if (notenModel1.getSchriftlicheNoten() != null){
                schriftlichSize = notenModel1.getSchriftlicheNoten().size();
        }
        int mündlichSize = 0;
        if (notenModel1.getMündlicheNoten() != null){
            mündlichSize = notenModel1.getMündlicheNoten().size();
        }
        holder.textViewKlausuren.setText(schriftlichSize+" Klausuren +"+mündlichSize +" mündliche Noten");
        if (notenModel1.getDurchschnitt() != 0){
            holder.textViewNote.setText(String.valueOf(notenModel1.getDurchschnitt()));
        }else {
            holder.textViewNote.setText("NA");
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ClickListener listener;

        public TextView textViewKurs;
        public TextView textViewLehrer;
        public TextView textViewKlausuren;
        public TextView textViewNote;



        public ViewHolder(View v, ClickListener clickListener) {
            super(v);

            textViewKurs = (TextView) v.findViewById(R.id.textViewKurs);
            textViewLehrer = (TextView) v.findViewById(R.id.textViewLehrer);
            textViewKlausuren = (TextView) v.findViewById(R.id.textViewKlausuren);
            textViewNote = (TextView) v.findViewById(R.id.textViewNote);


            this.listener = clickListener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
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
                listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);

            public boolean onItemLongClicked(int position);
        }
    }


}

