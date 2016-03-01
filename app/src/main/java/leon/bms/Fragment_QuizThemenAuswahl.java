package leon.bms;

import android.content.Context;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_QuizThemenAuswahl.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_QuizThemenAuswahl extends Fragment implements QuizKursAdapter.ViewHolder.ClickListener, QuizThemenAdapter.ViewHolder.ClickListener {

    String kursID;
    private OnFragmentInteractionListener mListener;
    Button buttonBack,buttonQuit;
    RecyclerView recyclerView;
    QuizThemenAdapter quizThemenAdapter;
    List<quizthemen> quizthemenList;


    public Fragment_QuizThemenAuswahl(String kursID) {
        // Required empty public constructor
        this.kursID = kursID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__quiz_themen_auswahl, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        QuizController quizController = new QuizController(getActivity());
        quizthemenList = new ArrayList<>();
        if (quizController.getThemenbereiche(kursID)!=null){
                quizthemenList = quizController.getThemenbereiche(kursID);
        }
        quizThemenAdapter = new QuizThemenAdapter(this,quizthemenList);
        recyclerView.setAdapter(quizThemenAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backQuizThemenAuswwahl();
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
    public void onItemClicked(int position) {
        quizthemen quizthemen = quizthemenList.get(position);
        mListener.FragmentQuizThemen_next();
    }

    @Override
    public boolean onItemLongClicked(int position) {
        quizthemen quizthemen = quizthemenList.get(position);
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
        void backQuizThemenAuswwahl();
        void FragmentQuizThemen_next();
        void onFragmentInteraction(Long themenbereichID);
    }
}
