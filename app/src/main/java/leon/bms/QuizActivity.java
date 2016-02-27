package leon.bms;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class QuizActivity extends AppCompatActivity implements Fragment_QuizStart.nextFragment,Fragment_QuizFachAuswahl.OnFragmentInteractionListener {

    FrameLayout frameLayout;
    int counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        Fragment_QuizStart fragment = new Fragment_QuizStart();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragemntContainer,fragment).commit();

    }

    public void next(){
        counter++;
        switch (counter){
            case 1:
                Fragment_QuizFachAuswahl fragment_quizFachAuswahl = new Fragment_QuizFachAuswahl();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragemntContainer, fragment_quizFachAuswahl,"1");
                transaction.addToBackStack("1");
                // Addd Custom Animations

                //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                // Commit the transaction
                transaction.commit();
                break;
            case 2:break;
            case 3:break;
            case 4:break;
            case 5:break;
        }


    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        counter--;

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {

            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void getNextFragment() {
        next();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
