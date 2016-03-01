package leon.bms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Leon E on 13.02.2016.
 */

public class WebsiteArticleController {
Context context;

    UpdateUI mListener;


    Boolean finnish = false;
    List<WebsiteArtikel> websiteArtikelList = new ArrayList<>();

    public WebsiteArticleController(Context mainContext,Fragment_Article fragementContext) {
        this.context = mainContext;
        if (fragementContext instanceof UpdateUI) {
            mListener = (UpdateUI) fragementContext;
        } else {
            throw new RuntimeException(fragementContext.toString() + " must implement OnFragmentInteractionListener");
        }

    }



    public void getRecentPosts(int page){
                // DO YOUR STUFFS HERE
                websiteArtikelList.clear();
                String Url = "http://marienschule.de/?json=get_recent_posts&page="+page;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("", "");
                String params = builder.build().getEncodedQuery();
                atOnline atOnline2 = new atOnline(Url, params, context);
                atOnline2.setUpdateListener(new atOnline.OnUpdateListener() {
                    @Override
                    public void onUpdate(String result) {
                        parseRecentArticle(result);

                    }
                });
                atOnline2.execute();

    }

    public void parseRecentArticle(String result){


        try {
            JSONObject jsonObjectAll = new JSONObject(result);
            JSONArray posts = jsonObjectAll.getJSONArray("posts");
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            for (int i=0;i<posts.length();i++){
                JSONObject einzelnerPost = posts.getJSONObject(i);
                final WebsiteArtikel websiteArtikel = new WebsiteArtikel();

                websiteArtikel.id = einzelnerPost.optInt("id");
                websiteArtikel.slug = einzelnerPost.optString("slug");
                websiteArtikel.url = einzelnerPost.optString("url");
                websiteArtikel.title =  Html.fromHtml(einzelnerPost.optString("title")).toString();
                websiteArtikel.title_plain =  Html.fromHtml(einzelnerPost.optString("title_plain")).toString();
                websiteArtikel.contentArticle = Html.fromHtml(einzelnerPost.optString("content")).toString();
                websiteArtikel.excerpt =  Html.fromHtml(einzelnerPost.optString("excerpt")).toString();
                websiteArtikel.date = einzelnerPost.optString("date");
                websiteArtikel.modified = einzelnerPost.optString("modified");

                JSONObject custom_fields = einzelnerPost.getJSONObject("custom_fields");
                JSONArray autorArray = custom_fields.getJSONArray("Autor");
                for (int k=0;k<autorArray.length();k++){
                    websiteArtikel.author = autorArray.getString(0);
                }
                JSONArray imageArray = custom_fields.getJSONArray("leadimage");
                for (int k=0;k<imageArray.length();k++){
                    imageLoader.loadImage(imageArray.getString(0), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            websiteArtikel.image = loadedImage;
                            finnish = true;
                            websiteArtikelList.add(websiteArtikel);
                            mListener.updateList(websiteArtikelList);
                            Log.d("WebsiteArticleController","Image successfully loaded.");
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

                }

            }

            Log.d("WebsiteArticleController", "Liste size: " + websiteArtikelList.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //updateUI.updateList(websiteArtikelList);


    }

    public List<WebsiteArtikel> getWebsiteArtikelList() {
        return websiteArtikelList;
    }

    public void setWebsiteArtikelList(List<WebsiteArtikel> websiteArtikelList) {
        this.websiteArtikelList = websiteArtikelList;
    }
    public boolean LoadedWebsiteArticle(){
        return finnish;
    }

    public interface UpdateUI {
        public void updateList(List<WebsiteArtikel> list);
    }


}

