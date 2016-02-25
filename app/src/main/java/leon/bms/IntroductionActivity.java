package leon.bms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.franciscan.materialstepper.AbstractStep;
import com.github.franciscan.materialstepper.style.TabStepper;

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
            Intent intent = new Intent(this,MainActivity.class);
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
