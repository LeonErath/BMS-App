package leon.bms.activites.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import leon.bms.R;
import leon.bms.ViewPagerAdapterMain;
import leon.bms.activites.login.normal.LogInActivity;
import leon.bms.activites.quiz.QuizActivity;
import leon.bms.activites.website.Website;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbUser;


/**
 * @MainActivity beinhaltet alle Hauptfunktionen und ist der Hauptangelpunkt der App.
 * Hier wird alles verwaltet und koordiniert zwischen den Fragmenten. Die MainActivity beinhaltet Fragmente
 * die es als Tabs anzeigt : Highlight,Stundenplan,Aufgaben und websiteartikel. Außerdem werden in einem
 * extra NavigationView alle zukünftigen Funktion eingetragen , sowie das Quiz.
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textViewHeaderName, textViewHeaderDatum;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView textViewTitle;
    NavigationView navigationView;
    DrawerLayout drawerLayoutgesamt;
    ActionBarDrawerToggle drawerToggle;
    AHBottomNavigation bottomNavigation;
    RealmQueries realmQueries;
    int[] tabIcons;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        tabIcons = new int[]{R.drawable.ic_home_white_24dp, R.drawable.ic_schedule_white_24dp, R.drawable.ic_done_white_24dp, R.drawable.ic_class_white_24dp, R.drawable.ic_grade_white_24dp};

        //setUp Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realmQueries = new RealmQueries(this);

        //setUP Drawerlayout
        drawerLayoutgesamt = (DrawerLayout) findViewById(R.id.drawerlayoutgesamt);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayoutgesamt, R.string.auf, R.string.zu);
        drawerLayoutgesamt.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);


        //viewpager for tablayout and to switch between the Fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapterMain viewPagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(), toolbar, viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
                FragmentLifecycle fragmentToShow = (FragmentLifecycle) viewPagerAdapter.getItem(position);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) viewPagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        navigationView = (NavigationView) findViewById(R.id.navView);
        View headerLayout = navigationView.getHeaderView(0);
        textViewHeaderName = (TextView) headerLayout.findViewById(R.id.headerName);
        textViewHeaderDatum = (TextView) headerLayout.findViewById(R.id.headerEmail);

        dbUser user = realmQueries.getUser();
        textViewHeaderName.setText(user.getFirst_name() + " " +user.getLast_name());
        textViewHeaderDatum.setText("Deine Stufe: " + user.getGrade_string());

        //setUp NavigationView
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawerViewItemHome: {
                        break;
                    }
                    case R.id.drawerViewItemWebsite: {
                        Intent intent = new Intent(MainActivity.this, Website.class);
                        startActivity(intent);
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

    public static void setTaskBarColored(Activity context, String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    private void setUpBottombar() {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Highlight", tabIcons[0], Color.parseColor("#4CAF50"));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Stundenplan", tabIcons[1], Color.parseColor("#009688"));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Aufgaben", tabIcons[2], Color.parseColor("#03A9F4"));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Klausur", tabIcons[3], Color.parseColor("#3F51B5"));
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Note", tabIcons[4], Color.parseColor("#673AB7"));

        // Add items
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);
        bottomNavigationItems.add(item5);

        bottomNavigation.addItems(bottomNavigationItems);


        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setColored(true);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setNotificationTextColor(Color.parseColor("#000000"));
        bottomNavigation.setCurrentItem(0);
        textViewTitle.setText("Highlight");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(bottomNavigation.getItem(0).getColor(MainActivity.this)));
        setTaskBarColored(MainActivity.this, "#388E3C");

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(bottomNavigation.getItem(position).getColor(MainActivity.this)));
                switch (position) {
                    case 0:
                        setTaskBarColored(MainActivity.this, "#388E3C");
                        textViewTitle.setText("Highlight");
                        break;
                    case 1:
                        setTaskBarColored(MainActivity.this, "#00796B");
                        textViewTitle.setText("Stundenplan");
                        break;
                    case 2:
                        setTaskBarColored(MainActivity.this, "#0288D1");
                        textViewTitle.setText("Aufgaben");
                        break;
                    case 3:
                        setTaskBarColored(MainActivity.this, "#303F9F");
                        textViewTitle.setText("Klausur");
                        break;
                    case 4:
                        setTaskBarColored(MainActivity.this, "#512DA8");
                        textViewTitle.setText("Note:");
                        break;
                }
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
                dbUser user = realmQueries.getUser();
                user.setLoggedIn(false);


                save(user);
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
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save(final RealmObject object){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                bgrealm.copyToRealmOrUpdate(object);
                Log.d("Fragment_Kursauswahl","Saved Object");
            }
        });
    }

    /**
     * @deleteDB Methode zum löschen aller Daten des Users
     */
    public void deleteDB() {
        Log.d(LogInActivity.class.getSimpleName(), "Datenbank wurde zurückgesetzt");

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
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
