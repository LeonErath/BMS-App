package leon.bms;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/** @Fragment_Article ist ein Fragment welches die Website Artikel der Marienschule Webseite
 *  darstellen soll . Die Artikel werden in einem RecyclerView angezeigt und das Laden sowie
 *  verarbeiten der Artikel übernimmt der ArticleController
 */
public class Fragment_Article extends Fragment  implements WebsiteArticleAdapter.ViewHolder.ClickListener, WebsiteArticleController.UpdateUI{

    // RecyclerView für das Anzeigen der einzelnen Artikel
    RecyclerView recyclerView;
    // Adapter für den RecyclerView
    WebsiteArticleAdapter websiteArticleAdapter;
    // WebsiteArticleController für das herunterladen und verarbeiten der Artikel
    WebsiteArticleController articleController;
    private static boolean m_iAmVisible;
    List<WebsiteArtikel> websiteArtikelList= new ArrayList<>();
    private static String TAG = Fragment_Article.class.getSimpleName();
    ViewPager viewPager;
    ProgressDialog progressDialog;

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
            articleController = new WebsiteArticleController(getActivity(),progressDialog);
            if (websiteArtikelList == null ||websiteArtikelList.size()==0){
                progressDialog = ProgressDialog.show(getActivity(), "Load Articles", "Loading..", true, false);
                // das Interface des articleControllers wird Initialisiert
                articleController.setUpdateInterface(new WebsiteArticleController.UpdateUI() {
                    @Override
                    public void updateList(List<WebsiteArtikel> list) {
                        //wenn das Interface ausgelöst wird bzw. der WebsiteController fertig ist
                        // wird der RecyclerView aktualisiert sodass er die Artikel anzeigt
                        websiteArtikelList = list;
                        websiteArticleAdapter.changeDataSet(websiteArtikelList);
                        progressDialog.dismiss();
                    }
                });
                articleController.getRecentPosts();
            }else{
                Log.d(TAG,"No update needed");
            }
            Log.d(TAG, "this fragment is now visible");

        } else {
            Log.d(TAG, "this fragment is now invisible");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articleController = new WebsiteArticleController(getActivity(),progressDialog);

        // sagt dem articleController ,dass er den Download der Artikel beginnen kann

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            // setUp recylcerView

            websiteArticleAdapter = new WebsiteArticleAdapter(this,websiteArtikelList);
            recyclerView.setAdapter(websiteArticleAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



    }



    /** Implementation des ClickListener um Aktionen bei einem Click auszuführen
     */
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(),WebsiteArticleActivity.class);
        intent.putExtra("content",websiteArtikelList.get(position).contentArticle);
        intent.putExtra("headline", websiteArtikelList.get(position).title);
        intent.putExtra("autor",websiteArtikelList.get(position).author);

        getActivity().startActivity(intent);

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    /** @updateList automatische Erstellung beim Implementieren des Interface
     */
    @Override
    public void updateList(List<WebsiteArtikel> list) {

    }
}
