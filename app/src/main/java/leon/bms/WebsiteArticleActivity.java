package leon.bms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class WebsiteArticleActivity extends AppCompatActivity {

    TextView textViewHeadline;
    TextView textViewContent;
    TextView textViewAutor;
    WebView webView;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_18dp);
        Intent intent = getIntent();
        content= intent.getStringExtra("content");
        String headline= intent.getStringExtra("headline");
        String autor= intent.getStringExtra("autor");


        textViewAutor = (TextView) findViewById(R.id.textViewAutor);
        textViewContent = (TextView) findViewById(R.id.textViewContent);
        textViewHeadline = (TextView) findViewById(R.id.textViewHeadline);

        //webView = (WebView) findViewById(R.id.webView);
        //webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webView.loadDataWithBaseURL("", content, "text/html", "utf-8", "");

        textViewContent.setText(Html.fromHtml(content));
        textViewHeadline.setText(headline);
        textViewAutor.setText(autor);
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
