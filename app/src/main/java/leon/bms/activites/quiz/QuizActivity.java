package leon.bms.activites.quiz;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import leon.bms.R;
import leon.bms.model.quizfragen;

/**
 * @QuizActivity ist die Activity für das Verwalten des Quizes sowie das Anzeigen der Fragment für das Quiz.
 */
public class QuizActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    int counter = 0;
    String kursid;
    Long themenbereichID;
    List<quizfragen> quizfragenList;
    String kursString;
    //List<dbFragen> fragenList;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState == null) {
            // wenn Activity zum ersten mal gestartet wird wird das Fragment_QuizStar geladen
            //Fragment_QuizStart fragment = new Fragment_QuizStart();
            //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.add(R.id.fragemntContainer, fragment).commit();
        }

    }

    /**
     * @nextIF Methode die aufgerufen wird wenn das nächste Fragment angezeigt werden muss
     * erstellt automatisch das nächste Fragment und zerstört das alte. Je nach counter wird ein anderes
     * Fragment geladen.
     */
    public void next() {
        counter++;
        switch (counter) {
            case 1:break;
            default:break;
        }


    }

    /**
     * @onBackPressed wird aufgerufen wenn der zurück Button gedrückt wird. Automatisch wird das vorherige
     * Fragment geladen. Außer beim Ergebnis fragment dort wird man automatisch auf das Themenebreich Fragment
     * umgeleitet
     */
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        counter--;

        if (count == 0) {
            super.onBackPressed();
            if (counter == 3) {
                counter--;
                super.onBackPressed();
            }
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }




}
