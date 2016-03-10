package leon.bms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @WebsiteArticleActivity ist die Activity zum Anzeigen der Webarticle der Webiste
 */
public class WebsiteArticleActivity extends AppCompatActivity {
    //views
    TextView textViewHeadline;
    TextView textViewAutor,textViewToolbar;
    WebView webView;
    ScrollView scrollView;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //die Activity lädt die Daten des speziellen Artikel
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        String url = intent.getStringExtra("url");
        String headline = intent.getStringExtra("headline");
        String autor = intent.getStringExtra("autor");
        setContentView(R.layout.activity_website_article);

        //stellt den Artikel in einem Webview dar
        // TODO Performance Problems and no Images
        String message ="<font color=\"" + "#5e5e5e" + "\">" +content+ "</font>";
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setDrawingCacheEnabled(false);
        webView = (WebView) findViewById(R.id.webView);
        webView.setDrawingCacheEnabled(false);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultFontSize(40);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        webView.loadDataWithBaseURL(url, message, "text/html", "utf-8", "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewToolbar = (TextView) findViewById(R.id.textViewToolbar);
        textViewAutor = (TextView) findViewById(R.id.textViewAutor);
        textViewHeadline = (TextView) findViewById(R.id.textViewHeadline);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_18dp);

        textViewAutor.setText(autor);
        textViewHeadline.setText(headline);
        textViewToolbar.setText("");
        getSupportActionBar().setTitle("");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //wenn home geclickt wird soll er die Activity schließen
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
