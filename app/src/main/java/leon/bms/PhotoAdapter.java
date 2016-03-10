package leon.bms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Leon E on 21.01.2016.
 */

/**
 * @PhotoAdapter ist ein Adapter zum Anzeigen von Photos in einer kleinen Übersicht.
 */
public class PhotoAdapter extends SelectableAdapter<PhotoAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = PhotoAdapter.class.getSimpleName();


    private List<String> photoList;


    private ViewHolder.ClickListener clickListener;

    /**
     * @param clickListener clickListener wird übergeben um auf Clickevents zu reagieren
     * @param photoList     Liste der zu anzeigenden Photos wird übergeben
     */
    public PhotoAdapter(ViewHolder.ClickListener clickListener, List<String> photoList) {
        super();
        this.clickListener = clickListener;
        this.photoList = photoList;
        Log.d(TAG, photoList.size() + " ");


    }

    /**
     * @param path Pfad des Photo welches hinzugefügt werden soll
     * @addPhoto methode zum hinzufügen von Photos
     */
    public void addPhoto(String path) {
        photoList.add(path);
        notifyItemInserted(photoList.size() - 1);
        Log.d(TAG, photoList.size() + "");
    }

    /**
     * @param positions position des Bildes in der Liste welches gelöscht werden soll
     * @removeItem methode zum löschen von Photos
     */
    public void removeItem(int positions) {
        if (photoList.size() == 1) {
            photoList.clear();
            notifyItemRemoved(0);
            Log.d(TAG, photoList.size() + "");
        } else {
            photoList.remove(positions);
            notifyItemRemoved(positions);
            Log.d(TAG, photoList.size() + "");
        }
    }

    /**
     * @param paths tauscht die Liste mit paths aus.
     */
    public void newData(List<String> paths) {
        photoList.clear();
        photoList = paths;
        notifyDataSetChanged();
    }

    /**
     * @return gibt die aktuelle Liste der Photos zurück
     */
    public List<String> getList() {
        return photoList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String photoPath = photoList.get(position);

        //set the Data to the views
        Bitmap bitmap = setPic(position);
        holder.imageView.setImageBitmap(bitmap);


        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /**
     * @param position Position des Bildes in der Liste welches verkleinert werden soll
     * @return gibt das Bild als Bitmap zurück
     * @setPic Methode zum runterskalieren der Biilder wobei auf die aspect ratio geachtet wird
     */
    private Bitmap setPic(int position) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoList.get(position), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoList.get(position), bmOptions);
        return bitmap;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private static final String TAG = ViewHolder.class.getSimpleName();
        //views
        ImageView imageView;
        View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            //initial view
            imageView = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

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