package leon.bms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

/**
 * Created by Leon E on 24.01.2016.
 */

/**
 * @ViewPagerAdapterMain ist für die MainActivity sodass die Fragmente automatisch geladen werden so
 * wie man mit "swipe" Gesten zwischen den Fragmenten hin und her "swipen" kann
 */
public class ViewPagerAdapterMain extends FragmentPagerAdapter {
    //int icons[] = {R.drawable.ic_done_white_24dp,R.drawable.ic_home_white_24dp,R.drawable.ic_schedule_white_24dp,R.drawable.ic_timeline_white_24dp};
    String[] tabtitlearray = {"", "", "", ""};
    Toolbar toolbar;
    ViewPager viewPager;

    public ViewPagerAdapterMain(FragmentManager fm, Toolbar toolbar, ViewPager viewPager) {
        super(fm);
        this.toolbar = toolbar;
        this.viewPager = viewPager;
    }

    /**
     * @param position ist die Position des Tabs. Am Anfang immer 0;
     * @return gibt das Fragment zurück für die ausgewählte Position
     * @getItem erstellt je nach Position das entsprechende Fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new Fragment_Highlight();
            case 1:
                return new Fragment_Stundenplan();
            case 2:
                return new Fragment_AufgabeÜbersicht();
            case 3:
                return new Fragment_Article(viewPager);
        }
        return null;
    }

    // Wichtige Methode damit die Fragmente immer aktualisiert werden
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * @return gibt die Größe des ViewPager zurück
     */
    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Drawable drawable =;
        return tabtitlearray[position];
    }
}
