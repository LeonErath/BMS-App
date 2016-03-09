package leon.bms;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
    int[] tabIcons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.anim.transition_enter);
            getWindow().setExitTransition(transition);
        }
        setContentView(R.layout.activity_main);

        tabIcons = new int[]{R.drawable.ic_done_white_24dp, R.drawable.ic_home_white_24dp, R.drawable.ic_schedule_white_24dp, R.drawable.ic_timeline_white_24dp};


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Highlights");

        drawerLayoutgesamt = (DrawerLayout) findViewById(R.id.drawerlayoutgesamt);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayoutgesamt, R.string.auf, R.string.zu);
        drawerLayoutgesamt.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Hightlights");

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText("Highlights");

        appBarLayout = (AppBarLayout) findViewById(R.id.AppBarLayout);


        //viewpager for tablayout
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapterMain viewPagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(), toolbar, viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout); //tabaylout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); //tabs fill the width
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayout(tabLayout);
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KursauswahlActivity.class));
            }
        });
        findViewById(R.id.tablayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AufgabenActivity.class));
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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final int position = intent.getIntExtra("position", 0);
            Log.d("TAG","TRIGGER");
            if (position!=0){
                viewPager.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position);
                    }
                }, 500);
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);


    }



    public void setupTabLayout(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[1]).getIcon().mutate().setColorFilter(Color.parseColor("#0d0d0d"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(tabIcons[2]).getIcon().mutate().setColorFilter(Color.parseColor("#0d0d0d"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(tabIcons[0]).getIcon().mutate().setColorFilter(Color.parseColor("#0d0d0d"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]).getIcon().mutate().setColorFilter(Color.parseColor("#0d0d0d"), PorterDuff.Mode.SRC_IN);
        int i = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(i).getIcon().clearColorFilter();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab.getIcon().clearColorFilter();

                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle("Highlights");
                        collapsingToolbarLayout.setTitle("Highlights");
                        textViewTitle.setText("Highlights");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Stundenplan");
                        collapsingToolbarLayout.setTitle("Stundenplan");
                        textViewTitle.setText("Stundenplan");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Aufgaben");
                        collapsingToolbarLayout.setTitle("Aufgaben");
                        textViewTitle.setText("Aufgaben");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("News");
                        collapsingToolbarLayout.setTitle("News");
                        textViewTitle.setText("News");
                        break;
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


                tab.getIcon().mutate().setColorFilter(Color.parseColor("#0d0d0d"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
                return true;
            case R.id.action_logout:
                dbUser user = new dbUser().getUser();
                user.loggedIn = false;
                user.save();
                Intent intent1 = new Intent(this, LogInActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.action_reset:
                deleteDB();
                Intent intent2 = new Intent(this, LogInActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_add:
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
                startActivity(new Intent(this, AufgabenActivity.class), activityOptionsCompat.toBundle());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
