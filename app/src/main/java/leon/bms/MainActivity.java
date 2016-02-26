package leon.bms;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    Toolbar toolbar;
    TextView textViewHeaderName, textViewHeaderDatum;
    ViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;
    DrawerLayout drawerLayoutgesamt;
    ActionBarDrawerToggle drawerToggle;
    int[] tabIcons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabIcons = new int[]{R.drawable.ic_done_white_24dp, R.drawable.ic_home_white_24dp, R.drawable.ic_schedule_white_24dp, R.drawable.ic_timeline_white_24dp};


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Highlights");

        drawerLayoutgesamt = (DrawerLayout) findViewById(R.id.drawerlayoutgesamt);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayoutgesamt,R.string.auf, R.string.zu);
        drawerLayoutgesamt.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

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

        textViewHeaderName.setText(new dbUser().getUser().vorname +" "+new dbUser().getUser().nachname);
        textViewHeaderDatum.setText("Deine Stufe: "+new dbUser().getUser().stufe);

    }



    public void setupTabLayout(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[1]).getIcon().mutate().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(tabIcons[2]).getIcon().mutate().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(tabIcons[0]).getIcon().mutate().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]).getIcon().mutate().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        int i =tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(i).getIcon().clearColorFilter();



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab.getIcon().clearColorFilter();

                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle("Highlights");
                        ;
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Stundenplan");
                        ;
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Aufgaben");
                        ;
                        break;
                    case 3:
                        getSupportActionBar().setTitle("News");
                        ;
                        break;
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


                tab.getIcon().mutate().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);

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
        switch (id){
            case R.id.action_settings: return true;
            case R.id.action_logout:
                dbUser user = new dbUser().getUser();
                user.loggedIn=false;
                user.save();
                Intent intent1 = new Intent(this,LogInActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.action_reset:
                deleteDB();
                Intent intent2 = new Intent(this,LogInActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_add:
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this,AufgabenActivity.class);
                startActivity(intent3);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteDB(){
        Log.d(LogInActivity.class.getSimpleName(),"Datenbank wurde zurückgesetzt");
        dbUser.deleteAll(dbUser.class);
        dbAufgabe.deleteAll(dbAufgabe.class);
        dbKurs.deleteAll(dbKurs.class);
        dbSchulstunde.deleteAll(dbSchulstunde.class);
        dbMediaFile.deleteAll(dbMediaFile.class);
        dbLehrer.deleteAll(dbLehrer.class);
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
