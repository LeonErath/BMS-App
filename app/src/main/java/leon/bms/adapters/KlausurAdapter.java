package leon.bms.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

/**
 * Created by Leon E on 22.05.2016.
 */
public class KlausurAdapter  extends UltimateViewAdapter<KlausurAdapter.ViewHolder> {

    public KlausurAdapter(Context context) {
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
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(KlausurAdapter.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder  {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
