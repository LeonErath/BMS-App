package leon.bms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * @Fragment_QuitErgebnis zeigt die Ergebnis des Quizes an. Dazu bekommt es den Themenbereich übergeben
 * welches der USer abgeschlossen hat. Anhand der Fragen wird das Ergebnis berechnet wie viele Richtig
 * oder Falsch waren sowie die Schwierigkeit mit einbezogen.
 */
public class Fragment_QuizErgebnis extends Fragment implements View.OnClickListener {

    //Listener für das zurück gehen
    private OnFragmentInteractionListener mListener;
    // views
    TextView textViewName, textViewStufe, textViewThemenbereich, textViewRichtig, textViewFalsch;
    // themenbereich den man abgeschlossen hat
    Long themenbereichID;
    ImageView imageViewCancel;
    List<dbFragen> fragenList;

    public Fragment_QuizErgebnis(long themenbereichID) {
        this.themenbereichID = themenbereichID;
    }

    public Fragment_QuizErgebnis() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__quiz_ergebnis, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewCancel = (ImageView) view.findViewById(R.id.imageViewCancel);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewStufe = (TextView) view.findViewById(R.id.textViewStufe);
        textViewThemenbereich = (TextView) view.findViewById(R.id.textViewThemenbereich);
        textViewRichtig = (TextView) view.findViewById(R.id.textViewFrageRichtig);
        textViewFalsch = (TextView) view.findViewById(R.id.textViewFrageFalsch);
        // zeigt das Ergebnis an
        setUpERGEBNIS();
        imageViewCancel.setOnClickListener(this);
    }

    /**
     * @setUpERGEBNIS zeigt das Ergebnis an und berechnet alles.
     */
    public void setUpERGEBNIS() {
        textViewName.setText("Name: " + new dbUser().getUser().vorname + " " + new dbUser().getUser().nachname);
        textViewStufe.setText("Stufe: " + new dbUser().getUser().stufe);


        if (new dbThemenbereich().getThemenbereich(themenbereichID) != null) {
            dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(themenbereichID);
            textViewThemenbereich.setText("Themenbereich: " + themenbereich.getName());
            if (themenbereich.getFragen(themenbereich.getId()) != null) {
                fragenList = themenbereich.getFragen(themenbereich.getId());
                if (fragenList.size() > 0) {
                    //berechnet die Ergebnis durch die Fragen
                    setUpFragen(fragenList);
                }
            }
        }
    }

    /**
     * @param fragen ein Liste mit alle beantworteten Fragen wird übergeben
     * @setUpfragen hier wird das Ergebnis ausgerechnet und angezeigt
     */
    public void setUpFragen(List<dbFragen> fragen) {
        double fragenRichtig = 0;
        double fragenFalsch = 0;
        // errechnet die falschen und richtigen Fragen
        for (dbFragen fragen1 : fragen) {
            if (fragen1.richtigCounter > 0) {
                fragenRichtig++;
            } else {
                fragenFalsch++;
            }
        }
        if ((fragenRichtig + fragenFalsch) == fragen.size()) {
            // für die Anzeige von Prozenten wird der Datentyp double verlangt
            double prozenRichtig = (fragenRichtig / fragen.size()) * 100;
            double prozenFalsch = (fragenFalsch / fragen.size()) * 100;
            // zeigt die Ergebnis an
            textViewRichtig.setText("Fragen richtig Beantwortet: " + fragenRichtig + " (" + prozenRichtig + "%)");
            textViewFalsch.setText("Fragen richtig Beantwortet: " + fragenFalsch + " (" + prozenFalsch + "%)");
        }
    }

    /**
     * initializes the Interface
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * onClick method to track clicks
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewCancel:
                // wenn das Bild angeclickt wird kommt man zurück zu den Themenbereichen
                mListener.Fragment_QuizErgebnisBACK();
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void Fragment_QuizErgebnisBACK();

        void Fragment_QuizErgebnisZuFrage(long themenbereichID, int FrageID);

    }

}
