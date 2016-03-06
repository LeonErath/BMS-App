package leon.bms;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;


/**
 * @Fragment_Article ist ein Fragment welches die Website Artikel der Marienschule Webseite
 * darstellen soll . Die Artikel werden in einem RecyclerView angezeigt und das Laden sowie
 * verarbeiten der Artikel übernimmt der ArticleController
 */
public class Fragment_Article extends Fragment implements WebsiteArticleAdapter.ViewHolder.ClickListener, WebsiteArticleController.UpdateUI {

    // RecyclerView für das Anzeigen der einzelnen Artikel
    RecyclerView recyclerView;
    Snackbar snackbar;
    // Adapter für den RecyclerView
    WebsiteArticleAdapter websiteArticleAdapter;
    // WebsiteArticleController für das herunterladen und verarbeiten der Artikel
    WebsiteArticleController articleController;
    private static boolean m_iAmVisible;
    List<WebsiteArtikel> websiteArtikelList = new ArrayList<>();
    private static String TAG = Fragment_Article.class.getSimpleName();
    ViewPager viewPager;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;
    belka.us.androidtoggleswitch.widgets.ToggleSwitch toggleSwitch;
    int page = 1;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public Fragment_Article(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__article, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;
        if (m_iAmVisible) {
            articleController = new WebsiteArticleController(getActivity(), this);
            if (websiteArtikelList == null || websiteArtikelList.size() == 0) {
                progressDialog = ProgressDialog.show(getActivity(), "Load Articles", "Loading..", true, false);
                // das Interface des articleControllers wird Initialisiert
                articleController.getRecentPosts(page);
            } else {
                Log.d(TAG, "No update needed");
            }
            Log.d(TAG, "this fragment is now visible");

        } else {
            Log.d(TAG, "this fragment is now invisible");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articleController = new WebsiteArticleController(getActivity(), this);

        // sagt dem articleController ,dass er den Download der Artikel beginnen kann
        linearLayout = (LinearLayout) view.findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // setUp recylcerView
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        websiteArticleAdapter = new WebsiteArticleAdapter(this, websiteArtikelList);
        recyclerView.setAdapter(websiteArticleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            articleController = new WebsiteArticleController(getActivity(), Fragment_Article.this);
                            articleController.getRecentPosts(page);
                            snackbar = Snackbar.make(linearLayout, "Loading Articles..", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });

        toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toogleButton);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position) {
                if (position == 0) {
                    websiteArticleAdapter.changeDataSet(websiteArtikelList);
                    //alle artikel normal

                } else {
                    List<WebsiteArtikel> sortedList = new ArrayList<WebsiteArtikel>();
                    sortedList = sortForPersonalInterest(websiteArtikelList);
                    websiteArticleAdapter.changeDataSet(sortedList);
                    //personalisierte Artikel

                }
            }
        });


    }

    public List<WebsiteArtikel> sortForPersonalInterest(List<WebsiteArtikel> list) {
        for (WebsiteArtikel websiteArtikel : list){
            List<String> tagList = websiteArtikel.getTags();
            int relevanz = 0;
            int count = 0;
            int vorkommen = 0;
            String text = websiteArtikel.getExcerpt();
            text = text.toUpperCase();
            if (tagList != null && tagList.size() > 0) {
                for (String tag : tagList) {
                    tag = tag.toUpperCase();
                    if (new dbWebsiteTag().tagVorhanden(tag) != false) {
                        dbWebsiteTag websiteTag = new dbWebsiteTag().getWebsiteTag(tag);
                        vorkommen = websiteTag.vorkommen;
                        if (vorkommen != 0) {
                            count = countWord(websiteTag.websitetag, text);
                        }
                    }
                }
            }
            relevanz = vorkommen + count;
            websiteArtikel.relevanz = relevanz;
        }
        return sortListInteger(list);
    }
    public List<WebsiteArtikel> sortListInteger(List<WebsiteArtikel> list){
        Collections.sort(list, new Comparator<WebsiteArtikel>() {
            @Override
            public int compare(WebsiteArtikel lhs, WebsiteArtikel rhs) {
                return lhs.relevanz - rhs.relevanz;
            }
        });
        return list;
    }

    public int countWord(String word, String text) {
        int count = 0;
        for (int i=0;i<text.length();i++){
            if (i+word.length()<=text.length()) {
                String excerpt = text.substring(i, i + word.length());
                if (word.equals(excerpt)) {
                    count++;
                }
            }
        }
        Log.d(TAG,count+" COUNT");
        return count;
    }


    /**
     * Implementation des ClickListener um Aktionen bei einem Click auszuführen
     */
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), WebsiteArticleActivity.class);
        intent.putExtra("content", websiteArtikelList.get(position).contentArticle);
        intent.putExtra("headline", websiteArtikelList.get(position).title);
        intent.putExtra("autor", websiteArtikelList.get(position).author);
        intent.putExtra("url", websiteArtikelList.get(position).url);
        intent.putExtra("bild", websiteArtikelList.get(position).image);
        intent.putExtra("id", websiteArtikelList.get(position).id);

        getActivity().startActivity(intent);

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    @Override
    public void updateList(List<WebsiteArtikel> list) {
        //wenn das Interface ausgelöst wird bzw. der WebsiteController fertig ist
        // wird der RecyclerView aktualisiert sodass er die Artikel anzeigt
        Log.d(TAG,"POSITION "+toggleSwitch.getCheckedTogglePosition());
        if (toggleSwitch.getCheckedTogglePosition() == 0) {
            for (WebsiteArtikel websiteArtikel : list) {
                if (!websiteArtikelList.contains(websiteArtikel)) {
                    websiteArticleAdapter.addArticle(websiteArtikel);
                }
            }
        }else {
            list = sortForPersonalInterest(list);
            for (WebsiteArtikel websiteArtikel : list) {
                if (!websiteArtikelList.contains(websiteArtikel)) {
                    websiteArticleAdapter.addArticle(websiteArtikel);
                }
            }
        }
        Log.d(TAG, "list: " + list.size() + " website: " + websiteArtikelList.size());
        loading = true;
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        page++;
    }

    /** @updateList automatische Erstellung beim Implementieren des Interface
     */

}
