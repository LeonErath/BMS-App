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
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements Fragment_QuizStart.nextFragment,Fragment_QuizFachAuswahl.OnFragmentInteractionListener {

    FrameLayout frameLayout;
    int counter = 0;
    String kursid;
    String themenbereichID;



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
            case 2:
                if (kursid!=null){
                    Fragment_QuizThemenAuswahl fragment = new Fragment_QuizThemenAuswahl(kursid);
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction2.replace(R.id.fragemntContainer, fragment,"2");
                    transaction2.addToBackStack("2");
                    // Addd Custom Animations

                    //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                    // Commit the transaction
                    transaction2.commit();
                }else {
                    Toast.makeText(QuizActivity.this, "Ein Fehler ist aufgetreten. Versuche es später nochmal", Toast.LENGTH_SHORT).show();
                }
                break;
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
    public void next(String Kursid) {
        this.kursid = Kursid;
        next();
    }

    @Override
    public void back() {
        onBackPressed();
    }
}
