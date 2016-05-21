package leon.bms.activites.quiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import leon.bms.controller.QuizController;
import leon.bms.R;
import leon.bms.adapters.QuizKursAdapter;
import leon.bms.adapters.QuizThemenAdapter;
import leon.bms.database.dbFragen;
import leon.bms.model.quizthemen;


/**
 * @Fragment_ThemenAuswahl ist das Fragment welches die Themenbereiche anzeigt die ein bestimmter Kurs hat.
 * Dazu wird ein recyclerView verwendet. Wählt man ein Themenbereich aus wird das Fragment_QuizFrage gestartet
 * und der Themenbereich übergeben.
 **/
public class Fragment_QuizThemenAuswahl extends Fragment implements QuizKursAdapter.ViewHolder.ClickListener, QuizThemenAdapter.ViewHolder.ClickListener, View.OnClickListener {

    //views
    String kursID;
    private OnFragmentInteractionListener mListener;
    Button buttonBack, buttonQuit;
    RecyclerView recyclerView;
    CardView cardViewAll,cardViewNew,cardViewWrong;
    TextView textViewKurs;
    //Adapter für die Themenbereiche
    QuizThemenAdapter quizThemenAdapter;
    List<quizthemen> quizthemenList;


    public Fragment_QuizThemenAuswahl() {
        // Required empty public constructor
    }

    /**
     * @param outState
     * @onSaveInstanceState wird benötigt falls Fragment zerstört wird z.B.: bei ScreenRotations.
     * Speichert die aktuelle Frage .
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        String id = kursID;
        outState.putString("serverid", id);
        super.onSaveInstanceState(outState);
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

        Bundle bundle = this.getArguments();
        kursID = bundle.getString("kursid", null);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("serverid")) {
                //lädt Daten falls das Fragment wiederhergestellt wird nachdem es zerstört wurde
                String id = savedInstanceState.getString("serverid");
                this.kursID = id;
            }
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // bekommt alle themen durch den QuizController
        QuizController quizController = new QuizController(getActivity());
        quizthemenList = new ArrayList<>();
        if (quizController.getThemenbereiche(kursID) != null) {
            quizthemenList = quizController.getThemenbereiche(kursID);
        }
        //setUp recylcerView
        quizThemenAdapter = new QuizThemenAdapter(this, quizthemenList);
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

        cardViewAll = (CardView) view.findViewById(R.id.cardViewAll);
        cardViewNew = (CardView) view.findViewById(R.id.cardViewNew);
        cardViewWrong = (CardView) view.findViewById(R.id.cardViewWrong);

        cardViewAll.setOnClickListener(this);
        cardViewNew.setOnClickListener(this);
        cardViewWrong.setOnClickListener(this);

        textViewKurs = (TextView) view.findViewById(R.id.textViewKurs);
        textViewKurs.setText(kursID);

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
     * @param position
     * @onItemClicked wird aufgerufen wenn ein Themenbereich angeclickt wird.
     * startet das Fragment_QuizFrage mit der Themenbereich ID
     */
    @Override
    public void onItemClicked(int position) {
        quizthemen quizthemen = quizthemenList.get(position);
        mListener.FragmentQuizThemen_next(quizthemen.getId());
    }

    @Override
    public boolean onItemLongClicked(int position) {
        quizthemen quizthemen = quizthemenList.get(position);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardViewAll:
                if (new QuizController(getActivity()).getAll(kursID) != null) {
                    mListener.FragmentQuizThemen_nextSpecial(kursID, new QuizController(getActivity()).getAll(kursID));
                }else {
                    Toast.makeText(getActivity(), "Ein Fehler ist aufgetreten.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cardViewNew:
                if (new QuizController(getActivity()).getNewest(kursID) != null) {
                    mListener.FragmentQuizThemen_nextSpecial(kursID, new QuizController(getActivity()).getNewest(kursID));
                }else {
                    Toast.makeText(getActivity(), "Ein Fehler ist aufgetreten.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cardViewWrong:
                if (new QuizController(getActivity()).getWrong(kursID) != null) {
                    if (new QuizController(getActivity()).getWrong(kursID).size() >0){
                        mListener.FragmentQuizThemen_nextSpecial(kursID, new QuizController(getActivity()).getWrong(kursID));
                    }else {
                        Toast.makeText(getActivity(), "Du hast keine falschen Fragen.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Ein Fehler ist aufgetreten.", Toast.LENGTH_SHORT).show();
                }
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
        void backQuizThemenAuswwahl();

        void FragmentQuizThemen_next(Long themenbereichID);
        void FragmentQuizThemen_nextSpecial(String kursString,List<dbFragen> fragenList);
    }
}