package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_QuizErgebnis.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_QuizErgebnis extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    TextView textViewName,textViewStufe,textViewThemenbereich,textViewRichtig,textViewFalsch;
    Long themenbereichID;
    ImageView imageViewCancel;
    List<dbFragen> fragenList;

    public Fragment_QuizErgebnis(long themenbereichID) {
        // Required empty public constructor
        this.themenbereichID = themenbereichID;
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

        setUpERGEBNIS();
        imageViewCancel.setOnClickListener(this);
    }
    public void setUpERGEBNIS() {
        textViewName.setText("Name: "+new dbUser().getUser().vorname+" "+new dbUser().getUser().nachname);
        textViewStufe.setText("Stufe: "+new dbUser().getUser().stufe);


        if (new dbThemenbereich().getThemenbereich(themenbereichID) != null) {
            dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(themenbereichID);
            textViewThemenbereich.setText("Themenbereich: "+themenbereich.getName());
            if (themenbereich.getFragen(themenbereich.getId()) != null) {
                fragenList = themenbereich.getFragen(themenbereich.getId());
                if (fragenList.size() > 0) {
                setUpFragen(fragenList);
                }
            }
        }
    }
    public void setUpFragen(List<dbFragen> fragen){
        int fragenRichtig=0;
        int fragenFalsch=0;
        for (dbFragen fragen1: fragen){
            if (fragen1.richtigCounter>0){
                fragenRichtig++;
            }else {
                fragenFalsch++;
            }
        }
        if ((fragenRichtig+fragenFalsch)==fragen.size()){
            int prozenRichtig = (fragenRichtig/fragen.size())*100;
            int prozenFalsch = (fragenFalsch/fragen.size())*100;

            textViewRichtig.setText("Fragen richtig Beantwortet: "+fragenRichtig+" ("+prozenRichtig+"%)");
            textViewFalsch.setText("Fragen richtig Beantwortet: "+fragenFalsch+" ("+prozenFalsch+"%)");
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewCancel:
                mListener.Fragment_QuizErgebnisBACK();
                break;
            default:break;
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
        void Fragment_QuizErgebnisZuFrage(long themenbereichID,int FrageID);

    }

}
