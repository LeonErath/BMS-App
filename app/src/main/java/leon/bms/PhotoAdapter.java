package leon.bms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class PhotoAdapter extends SelectableAdapter<PhotoAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = PhotoAdapter.class.getSimpleName();



    private List<String> photoList;


    private ViewHolder.ClickListener clickListener;

    public PhotoAdapter(ViewHolder.ClickListener clickListener, List<String> photoList) {
        super();
        this.clickListener = clickListener;
        this.photoList = photoList;
        Log.d(TAG,photoList.size()+" ");


    }
    public void addPhoto(String path){
        photoList.add(path);
        notifyItemInserted(photoList.size()-1);
        Log.d(TAG,photoList.size()+"");
    }

    public void removeItem(int positions){
        if (photoList.size() == 1){
            photoList.clear();
            notifyItemRemoved(0);
            Log.d(TAG, photoList.size() + "");
        }else {
            photoList.remove(positions);
            notifyItemRemoved(positions);
            Log.d(TAG, photoList.size() + "");
        }
    }

    public void newData(List<String> paths){
        photoList.clear();
        photoList = paths;
        notifyDataSetChanged();
    }
    public List<String> getList(){
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


        Bitmap bitmap = setPic(position);
        holder.imageView.setImageBitmap(bitmap);
        //Setting text view title


        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
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
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

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
        ImageView imageView;
        View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
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