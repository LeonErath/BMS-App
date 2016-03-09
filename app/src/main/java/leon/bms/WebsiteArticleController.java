package leon.bms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
                websiteArtikel.originalDate= einzelnerPost.optString("date");
                String date = einzelnerPost.optString("date");
                websiteArtikel.date = setUpDate(date);
                websiteArtikel.modified = einzelnerPost.optString("modified");
                JSONArray websiteTags = einzelnerPost.getJSONArray("tags");
                if (websiteTags.length() >0) {
                    List<String> tagList = new ArrayList<>();
                    for (int l = 0; l < websiteTags.length(); l++) {
                        JSONObject tagObject = websiteTags.getJSONObject(l);
                        tagList.add(tagObject.optString("title"));
                    }
                    if (tagList.size()>0){
                        websiteArtikel.tags = tagList;
                    }
                }

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
                            websiteArtikelList= sortListDate(websiteArtikelList);
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
        //updateUI.updateList(generalListForArticles);


    }

    private List<WebsiteArtikel> sortListDate(List<WebsiteArtikel> websiteArtikelList) {
        Collections.sort(websiteArtikelList, new Comparator<WebsiteArtikel>() {
            public int compare(WebsiteArtikel websiteArtikel, WebsiteArtikel websiteArtikel2) {
                return stringToCalander(websiteArtikel.originalDate).getTime().compareTo(stringToCalander(websiteArtikel2.originalDate).getTime());
            }
        });
        Collections.reverse(websiteArtikelList);
        return websiteArtikelList;
    }

    public Calendar stringToCalander(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    private String setUpDate(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        try {
            calendar.setTime(sdf2.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = calendar.getTime();
        String zuletztAktualisiert = String.valueOf(DateUtils.getRelativeDateTimeString(context, date1.getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0));
        return zuletztAktualisiert;
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

