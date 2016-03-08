package leon.bms;

/**
 * Created by Leon E on 13.02.2016.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leon E on 21.01.2016.
 */
public class WebsiteArticleAdapter extends RecyclerView.Adapter<WebsiteArticleAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = KursauswahlAdapter.class.getSimpleName();


    private List<WebsiteArtikel> artikelList = new ArrayList<>();


    private ViewHolder.ClickListener clickListener;

    public WebsiteArticleAdapter(ViewHolder.ClickListener clickListener, List<WebsiteArtikel> artikelList) {
        super();
        this.clickListener = clickListener;
        this.artikelList = artikelList;
        Log.d(TAG, artikelList.size() + " ");


    }

    public List<WebsiteArtikel> getArtikelList() {
        return artikelList;
    }

    public void addArticle(WebsiteArtikel websiteArtikel) {
        if (!artikelList.contains(websiteArtikel)) {
            artikelList.add(websiteArtikel);
            notifyItemInserted(artikelList.size() - 1);
        }
    }

    public void changeDataSet(List<WebsiteArtikel> websiteArtikelList) {
        for (int i = 0; i < websiteArtikelList.size(); i++) {
            WebsiteArtikel websiteArtikel = websiteArtikelList.get(i);
            artikelList.remove(i);
            artikelList.add(i, websiteArtikel);
            notifyItemChanged(i);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_websiteartikel, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WebsiteArtikel websiteArtikel = artikelList.get(position);

        //Setting text view title
        holder.imageView.setImageBitmap(websiteArtikel.getImage());
        holder.textViewHeadline.setText(websiteArtikel.getTitle());
        holder.textViewContent.setText(websiteArtikel.getExcerpt());
        holder.textViewDate.setText(websiteArtikel.getDate());


    }

    @Override
    public int getItemCount() {
        return artikelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        TextView textViewHeadline;
        TextView textViewContent;
        TextView textViewDate;
        ImageView imageView;


        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            textViewHeadline = (TextView) itemView.findViewById(R.id.textViewHeadline);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewExcerpt);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);


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
