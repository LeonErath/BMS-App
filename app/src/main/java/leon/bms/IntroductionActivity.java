package leon.bms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.franciscan.materialstepper.AbstractStep;
import com.github.franciscan.materialstepper.style.TabStepper;

/**
 * @IntroductionActivity ist die Activity die aufgerufen wird wenn der User zum aller ersten mal die
 * App öffnet. Hier kann er sich durch 4 Fragmente durch arbeiten um seine Konfiguration abzuschließen.
 * Diese activity ist extra dafür da den Einstieg in die App zu erleichtern.
 */
public class IntroductionActivity extends TabStepper {
    private int i = 1;
    AbstractStep fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setErrorTimeout(1500);
        setLinear(true);
        setAlternativeTab(false);
        setTitle("Einleitung");

        setTheme(R.style.AppThemeIntro);

        // Welcome
        addStep(createFragment(new Fragment_TabWelcome()));
        // Login
        fragment = new Fragment_TabLogin();
        addStep(createFragment(fragment));
        // Kursauswahl
        addStep(createFragment(new Fragment_TabKursauswahl()));
        // Danke
        addStep(createFragment(new Fragment_TabDanke()));


        super.onCreate(savedInstanceState);

    }


    @Override
    public void onNext() {
        AbstractStep step = mSteps.getCurrent();
        if (mSteps.current() == mSteps.total() - 1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onNext();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putInt("position", i++);
        fragment.setArguments(b);
        return fragment;
    }


}
