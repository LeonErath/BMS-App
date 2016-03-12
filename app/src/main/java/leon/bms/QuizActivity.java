package leon.bms;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

/**
 * @QuizActivity ist die Activity für das Verwalten des Quizes sowie das Anzeigen der Fragment für das Quiz.
 */
public class QuizActivity extends AppCompatActivity implements Fragment_QuizStart.nextFragment, Fragment_QuizFachAuswahl.OnFragmentInteractionListener, Fragment_QuizThemenAuswahl.OnFragmentInteractionListener, Fragment_QuizFrage.OnFragmentInteractionListener, Fragment_QuizErgebnis.OnFragmentInteractionListener {

    FrameLayout frameLayout;
    int counter = 0;
    String kursid;
    Long themenbereichID;
    List<quizfragen> quizfragenList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState == null) {
            // wenn Activity zum ersten mal gestartet wird wird das Fragment_QuizStar geladen
            Fragment_QuizStart fragment = new Fragment_QuizStart();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragemntContainer, fragment).commit();
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
            case 1:
                //zuerst die Fach auswahl
                Fragment_QuizFachAuswahl fragment_quizFachAuswahl = new Fragment_QuizFachAuswahl();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragemntContainer, fragment_quizFachAuswahl, "1");
                transaction.addToBackStack("1");
                // Addd Custom Animations

                //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                // Commit the transaction
                transaction.commit();
                break;
            case 2:
                if (kursid != null) {
                    // als zweites die Themenbreich auswahl
                    Fragment_QuizThemenAuswahl fragment = new Fragment_QuizThemenAuswahl(kursid);
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction2.replace(R.id.fragemntContainer, fragment, "2");
                    transaction2.addToBackStack("2");
                    // Addd Custom Animations

                    //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                    // Commit the transaction
                    transaction2.commit();
                } else {
                    Toast.makeText(QuizActivity.this, "Ein Fehler ist aufgetreten. Versuche es später nochmal", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (themenbereichID != null) {
                    // dann wird das Quiz gestartet
                    Fragment_QuizFrage fragment = new Fragment_QuizFrage(themenbereichID);
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction2.replace(R.id.fragemntContainer, fragment, "3");
                    transaction2.addToBackStack("3");
                    // Addd Custom Animations

                    //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                    // Commit the transaction
                    transaction2.commit();
                }
                break;
            case 4:
                if (themenbereichID != null && quizfragenList != null && quizfragenList.size() != 0) {
                    // und nach dem Quiz wird das Ergebnis angezeigt
                    Fragment_QuizErgebnis fragment = new Fragment_QuizErgebnis(themenbereichID,quizfragenList);
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction2.replace(R.id.fragemntContainer, fragment, "4");
                    transaction2.addToBackStack("4");
                    // Addd Custom Animations

                    //transaction.setCustomAnimations(R.anim.transition_enter,R.anim.fadein);

                    // Commit the transaction
                    transaction2.commit();
                }
                break;
            case 5:
                break;
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

    // jetzt kommen jede Menge Interface Callbacks von den Fragmenten
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

    @Override
    public void backQuizThemenAuswwahl() {
        onBackPressed();
    }

    @Override
    public void FragmentQuizThemen_next(Long themenbereichID) {
        this.themenbereichID = themenbereichID;
        next();
    }


    @Override
    public void Fragment_QuizFrageShowErgebnis(long themenbereichID, List<quizfragen> quizfragenList) {
        this.themenbereichID = themenbereichID;
        this.quizfragenList = quizfragenList;
        next();

    }

    @Override
    public void Fragment_QuitFrageBACK() {
        onBackPressed();
    }


    @Override
    public void Fragment_QuizErgebnisBACK() {
        onBackPressed();
    }

    @Override
    public void Fragment_QuizErgebnisZuFrage(long themenbereichID, int FrageID) {

    }
}
