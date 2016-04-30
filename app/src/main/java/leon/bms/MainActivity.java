package leon.bms;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

/**
 * @MainActivity beinhaltet alle Hauptfunktionen und ist der Hauptangelpunkt der App.
 * Hier wird alles verwaltet und koordiniert zwischen den Fragmenten. Die MainActivity beinhaltet Fragmente
 * die es als Tabs anzeigt : Highlight,Stundenplan,Aufgaben und WebsiteArtikel. Außerdem werden in einem
 * extra NavigationView alle zukünftigen Funktion eingetragen , sowie das Quiz.
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textViewHeaderName, textViewHeaderDatum;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView textViewTitle;
    NavigationView navigationView;
    AppBarLayout appBarLayout;
    DrawerLayout drawerLayoutgesamt;
    ActionBarDrawerToggle drawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AHBottomNavigation bottomNavigation;
    int[] tabIcons;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        tabIcons = new int[]{R.drawable.ic_done_white_24dp, R.drawable.ic_home_white_24dp, R.drawable.ic_schedule_white_24dp, R.drawable.ic_timeline_white_24dp};

        //setUp Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //setUP Drawerlayout
        drawerLayoutgesamt = (DrawerLayout) findViewById(R.id.drawerlayoutgesamt);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayoutgesamt, R.string.auf, R.string.zu);
        drawerLayoutgesamt.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        //applying CollapsingToolbar
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText("");

        appBarLayout = (AppBarLayout) findViewById(R.id.AppBarLayout);


        //viewpager for tablayout and to switch between the Fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapterMain viewPagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(), toolbar, viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KursauswahlActivity.class));
            }
        });
        findViewById(R.id.viewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebsiteArticleActivity.class));
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navView);
        View headerLayout = navigationView.getHeaderView(0);
        textViewHeaderName = (TextView) headerLayout.findViewById(R.id.headerName);
        textViewHeaderDatum = (TextView) headerLayout.findViewById(R.id.headerEmail);

        textViewHeaderName.setText(new dbUser().getUser().vorname + " " + new dbUser().getUser().nachname);
        textViewHeaderDatum.setText("Deine Stufe: " + new dbUser().getUser().stufe);

        //setUp NavigationView
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawerViewItemHome: {
                        break;
                    }

                    case R.id.drawerViewItemQuiz: {
                        Intent intent3 = new Intent(MainActivity.this, QuizActivity.class);
                        startActivity(intent3);
                        break;
                    }
                }
                drawerLayoutgesamt.closeDrawers();
                item.setChecked(true);

                return false;
            }
        });

        // Überprüft ob Daten von einer Anderen Activity übergeben worden ist
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final int position = intent.getIntExtra("position", 0);
            Log.d("TAG", "TRIGGER");
            if (position != 0) {
                viewPager.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position);
                    }
                }, 500);
            }
        }
        setUpBottombar();
    }

    private void setUpBottombar() {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Highlight", tabIcons[1], Color.parseColor("#4CAF50"));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Stundenplan", tabIcons[2], Color.parseColor("#009688"));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Aufgaben", tabIcons[0], Color.parseColor("#03A9F4"));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Website", tabIcons[3], Color.parseColor("#E91E63"));

        // Add items
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);

        bottomNavigation.addItems(bottomNavigationItems);


        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setColored(true);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setNotificationTextColor(Color.parseColor("#000000"));
        bottomNavigation.setCurrentItem(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(bottomNavigation.getItem(0).getColor(MainActivity.this)));


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(bottomNavigation.getItem(position).getColor(MainActivity.this)));
            }
        });

    }


    /**
     * Updates the navigationView if mainAcitivty get restored
     */
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     * @onOptinItemSelected ist das Menü um sich auszuloggen oder seine Daten zulöschen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id) {
            case R.id.action_settings:
                // TODO ADD Settings
                return true;
            case R.id.action_logout:
                // User wird augeloggt
                dbUser user = new dbUser().getUser();
                user.loggedIn = false;
                user.save();
                Intent intent1 = new Intent(this, LogInActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.action_reset:
                // daten werden gelöscht
                deleteDB();
                Intent intent2 = new Intent(this, LogInActivity.class);
                startActivity(intent2);
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @deleteDB Methode zum löschen aller Daten des Users
     */
    public void deleteDB() {
        Log.d(LogInActivity.class.getSimpleName(), "Datenbank wurde zurückgesetzt");
        dbUser.deleteAll(dbUser.class);
        dbAufgabe.deleteAll(dbAufgabe.class);
        dbKurs.deleteAll(dbKurs.class);
        dbSchulstunde.deleteAll(dbSchulstunde.class);
        dbMediaFile.deleteAll(dbMediaFile.class);
        dbLehrer.deleteAll(dbLehrer.class);
        dbWebsiteTag.deleteAll(dbWebsiteTag.class);
        dbFragen.deleteAll(dbFragen.class);
        dbThemenbereich.deleteAll(dbThemenbereich.class);
        dbAntworten.deleteAll(dbAntworten.class);
        dbKursTagConnect.deleteAll(dbKursTagConnect.class);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(new Configuration());
    }
}
