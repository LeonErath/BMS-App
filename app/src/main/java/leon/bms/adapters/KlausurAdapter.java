package leon.bms.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import leon.bms.R;
import leon.bms.model.klausurModel;
import leon.bms.realm.dbKlausur;

/**
 * Created by Leon E on 22.05.2016.
 */
public class KlausurAdapter extends UltimateViewAdapter<KlausurAdapter.ViewHolder> {

    Context context;
    List<klausurModel> klausurList;
    public ViewHolder.ClickListener clickListener;

    public KlausurAdapter(Context context, List<klausurModel> klausurList, ViewHolder.ClickListener listener) {
        this.context = context;
        this.klausurList = klausurList;
        this.clickListener = listener;

    }

    public klausurModel get(int position){
        if (position>=0&&position<getAdapterItemCount()){
            return klausurList.get(position);
        }
        return null;
    }

    @Override
    public KlausurAdapter.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public KlausurAdapter.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public KlausurAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_klausur, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public int getAdapterItemCount() {
        return klausurList.size();
    }

    public void changeDataSet(List<klausurModel> klausurList) {
        this.klausurList = klausurList;
        notifyDataSetChanged();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(KlausurAdapter.ViewHolder holder, int position) {
        //Set the data
        dbKlausur klausur = klausurList.get(position).klausur;
        klausurModel klausurModel = klausurList.get(position);
        holder.textViewKlausurname.setText(klausur.getName());
        holder.textViewZeit.setText(klausurModel.getZeitString());
        holder.textViewKlausurDatum.setText(klausurModel.getDateString());
        holder.textViewKlausurRaum.setText("Raum: "+klausur.getRaum().getName());
        holder.textViewAblaufdatum.setText(klausurModel.getAblaufdatum());
        holder.textViewAblaufdatum.setTextColor(Color.parseColor(klausurModel.getColor()));

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
        public TextView textViewKlausurname;
        public TextView textViewKlausurDatum;
        public TextView textViewKlausurRaum;
        public TextView textViewZeit;
        public TextView textViewAblaufdatum;


        public ViewHolder(View v, ClickListener clickListener) {
            super(v);

            textViewKlausurname = (TextView) v.findViewById(R.id.textViewKlausurname);
            textViewKlausurDatum = (TextView) v.findViewById(R.id.textViewKlausurDatum);
            textViewKlausurRaum = (TextView) v.findViewById(R.id.textViewKlausurRaum);
            textViewZeit = (TextView) v.findViewById(R.id.textViewKlausurZeit);
            textViewAblaufdatum = (TextView) v.findViewById(R.id.textViewAblaufdatum);


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
