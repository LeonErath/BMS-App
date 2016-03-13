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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    List<WebsiteArtikel> generalListForArticles = new ArrayList<>();
    private static String TAG = Fragment_Article.class.getSimpleName();
    ViewPager viewPager;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;
    // toogleSwitch zum anzeigen der personalisierten Webseiten
    belka.us.androidtoggleswitch.widgets.ToggleSwitch toggleSwitch;
    int page = 1;
    //@sortedList enthält alle Article nach dem Datum sortiert
    List<WebsiteArtikel> sortedList = new ArrayList<>();
    private boolean loading = true;
    // wichtige Daten zum ermitteln ob der RecyclerView neue Article laden muss
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public Fragment_Article(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__article, container, false);
    }

    /**
     * @param isVisibleToUser gibt an ob das Fragment sichtbar ist oder nicht
     * @setUserVisibleHint prüft ob Fragment sichtbar ist oder nicht. Wenn nicht und keine Article bereits
     * heruntergeladen worden sind werden neue Artikel geladen.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;
        if (m_iAmVisible) {
            articleController = new WebsiteArticleController(getActivity(), this);
            if (generalListForArticles == null || generalListForArticles.size() == 0) {
                progressDialog = ProgressDialog.show(getActivity(), "Load Articles", "Loading..", true, false);
                progressDialog.setCancelable(false);
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
        websiteArticleAdapter = new WebsiteArticleAdapter(this, generalListForArticles);
        recyclerView.setAdapter(websiteArticleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);

        // guckt wie weit man scrollt
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        // wenn das Ende erreicht wird werden neue Artikel heruntergeladen
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
        // initiate toggleSwitch
        toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toogleButton);
        /** wenn die position des toogleSwitch nicht 0 ist werden die personalisierten Artikel angezeigt.
         *  wird wieder auf 0 geschalten werden wieder die normalen Artikel angezeigt.
         *  bei jeder Änderung wird der recyclerView wieder nach oben scrolllen
         */
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position) {
                if (position == 0) {
                    // zeigt wieder alle Artikel nach dem Datum sortiert an
                    Log.d(TAG, "trigger");
                    websiteArticleAdapter.changeDataSet(sortedList);
                    recyclerView.smoothScrollToPosition(0);
                    //alle artikel normal

                } else {
                    // sortiert die Artikel nach persönlichen Vorlieben
                    List<WebsiteArtikel> sortedList = new ArrayList<WebsiteArtikel>();
                    for (int i = 0; i < websiteArticleAdapter.getArtikelList().size() / 10; i++) {
                        List<WebsiteArtikel> tempList = websiteArticleAdapter.getArtikelList().subList(i * 10, (i * 10) + 10);
                        tempList = sortForPersonalInterest(tempList);
                        sortedList.addAll(tempList);
                    }
                    if (sortedList.size() > 0) {
                        websiteArticleAdapter.changeDataSet(sortedList);
                        recyclerView.smoothScrollToPosition(0);
                    }
                    //personalisierte Artikel

                }
            }
        });


    }

    /**
     * @param list ist die Liste mit Artikel die sortiert werden soll
     * @return gibt die sortierte List zurück
     * @sortForPersonalInterest sortiert Website Listen nach der persönlichen Relevanz ,dazu werden
     * die TAGs des Artikel mit den persönlichen TAGs verglichen. Außerdem wird geguckt wie oft die
     * eigenen TAGs im Artikel vorkommen sowie wie oft der eigene Name in dem Artikel vorkommt.
     */
    public List<WebsiteArtikel> sortForPersonalInterest(List<WebsiteArtikel> list) {
        for (int i = 0; i < list.size(); i++) {
            WebsiteArtikel websiteArtikel = list.get(i);
            // sucht alle WebsiteTAGs raus
            List<String> tagList = websiteArtikel.getTags();
            int relevanz = 0;
            int count = 0;
            int vorkommen = 0;
            String text = websiteArtikel.getExcerpt();
            text = text.toUpperCase();
            if (tagList != null && tagList.size() > 0) {
                for (String tag : tagList) {
                    tag = tag.toUpperCase();
                    // wenn der TAG gleich ist mit dem Artikel TAG wird die relevanz erhöht
                    if (new dbWebsiteTag().tagVorhanden(tag) != false) {
                        dbWebsiteTag websiteTag = new dbWebsiteTag().getWebsiteTag(tag);
                        vorkommen += websiteTag.vorkommen;
                        if (websiteTag.vorkommen != 0) {
                            // wenn der TAG in dem Artikel vorkommt wird die relevanz um 1 erhöht
                            count += countWord(websiteTag.websitetag, text);
                            // wenn der Name in dem Artikel vorkommt wird die relevanz um 10 erhöht
                            count += countWord(new dbUser().getUser().vorname, text) * 10;
                        }

                    }
                }
            }
            // relevanz wird berechnet
            relevanz += vorkommen + count;
            websiteArtikel.relevanz = relevanz;
        }
        // sortiert die Liste nach der Relavanz aufsteigend
        Collections.sort(list, new Comparator<WebsiteArtikel>() {
            @Override
            public int compare(WebsiteArtikel lhs, WebsiteArtikel rhs) {
                return lhs.relevanz - rhs.relevanz;
            }
        });
        // kehrt die Liste um sodass die relevantesten Artikel oben stehen
        Collections.reverse(list);
        return list;
    }

    /**
     * @param word ist das Wort welches nach Häufigkeit überprüft werden soll
     * @param text ist der Text in dem das Wort vorkommen soll
     * @return gibt die Anzahl zurück wie oft ein Wort vorkommt
     * @countWord zählt wie oft ein Wort in einem Text vorkommt
     */
    public int countWord(String word, String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (i + word.length() <= text.length()) {
                String excerpt = text.substring(i, i + word.length());
                if (word.equals(excerpt)) {
                    count++;
                }
            }
        }
        Log.d(TAG, count + " COUNT");
        return count;
    }


    /**
     * Implementation des ClickListener um Aktionen bei einem Click auszuführen
     * wenn ein Artikel angeclickt wird soll sich die WebsiteArticleActivity öffnen die den
     * Artikel anzeigt.
     */
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), WebsiteArticleActivity.class);
        intent.putExtra("content", generalListForArticles.get(position).contentArticle);
        intent.putExtra("headline", generalListForArticles.get(position).title);
        intent.putExtra("autor", generalListForArticles.get(position).author);
        intent.putExtra("url", generalListForArticles.get(position).url);
        intent.putExtra("bild", generalListForArticles.get(position).image);
        intent.putExtra("id", generalListForArticles.get(position).id);

        getActivity().startActivity(intent);

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    /**
     * @param list ist die heruntergeladen Liste
     * @updateList automatische Erstellung beim Implementieren des Interface
     * @updateList wird aufgerufen wenn der WebsiteArticleController alle Artikel geladen hat
     * dann werden die Artikel in sortedList geladen , die immer alle Artikel enthält. Wenn der User
     * im moment sich seiner personlisierten Artikel anguckt wird die List auch nach den persönlichen
     * Interesse sortiert und dem Adapter hinzugefügt. Artikel werden nur geladen wenn 10 Artikel fertig
     * sind.
     */
    @Override
    public void updateList(List<WebsiteArtikel> list) {
        //wenn das Interface ausgelöst wird bzw. der WebsiteController fertig ist
        // wird der RecyclerView aktualisiert sodass er die Artikel anzeigt

        if (list.size() == 10) {
            Log.d(TAG, String.valueOf(list));
            sortedList.addAll(list);
            if (toggleSwitch.getCheckedTogglePosition() == 1) {
                list = sortForPersonalInterest(list);
            }
            for (WebsiteArtikel websiteArtikel : list) {
                websiteArticleAdapter.addArticle(websiteArtikel);
            }


            loading = true;
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }

            page++;

        }
    }

    @Override
    public void error() {
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }


}
