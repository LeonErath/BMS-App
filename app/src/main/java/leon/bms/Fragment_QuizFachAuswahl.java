package leon.bms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * @Fragment_QuizFachAuswahl zeigt die Fächer an die Themenbereich des Quizes enthalten an.
 * Es lässt sich alle Kurse übergeben die themenbereich besitzen und zeigt diese in einem RecyclerView an.
 */
public class Fragment_QuizFachAuswahl extends Fragment implements QuizKursAdapter.ViewHolder.ClickListener {

    private OnFragmentInteractionListener mListener;
    Button buttonBack, buttonQuit;
    RecyclerView recyclerView;
    QuizKursAdapter quizKursAdapter;
    List<quizkurs> quizkursList;

    public Fragment_QuizFachAuswahl() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__quiz_fach_auswahl, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // bekommt alle Kurse für die Anzeige vom quizController
        QuizController quizController = new QuizController(getActivity());
        quizkursList = new ArrayList<>();
        // überprüft ob Kurse vorhanden sind die Themenbereiche enthalten
        if (quizController.getQuizKurse() != null) {
            quizkursList = quizController.getQuizKurse();
        } else {
            Toast.makeText(getActivity(), "Keine Kurse vorhanden.", Toast.LENGTH_SHORT).show();
        }

        //setUp recyclerview
        quizKursAdapter = new QuizKursAdapter(this, quizkursList);
        recyclerView.setAdapter(quizKursAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setUp Buttons and onClick events
        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.back();
            }
        });
        buttonQuit = (Button) view.findViewById(R.id.buttonQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * @param position ist die Position welches von dem angeklicktem Item
     * @onITemClicked wird aufgerufen wenn ein Item des RecyclerView angeclickt wird
     */
    @Override
    public void onItemClicked(int position) {
        quizkurs quizkurs = quizkursList.get(position);
        mListener.next(quizkurs.kursId);
    }

    /**
     * @param position ist die Position welches von dem angeklicktem Item
     * @onITemLongClicked wird aufgerufen wenn ein Item des RecyclerView angeclickt wird
     */
    @Override
    public boolean onItemLongClicked(int position) {
        quizkurs quizkurs = quizkursList.get(position);
        mListener.next(quizkurs.kursId);
        return false;
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
        void next(String id);

        void back();
    }
}
