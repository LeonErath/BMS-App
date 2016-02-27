package leon.bms;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_QuizStart extends Fragment implements View.OnClickListener {

    Button buttonStart, buttonScore,buttonZurück;
    ProgressDialog progressDialog;
    TextView textViewDatum;
    nextFragment next;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            next = (nextFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__quiz_start, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonStart = (Button) view.findViewById(R.id.buttonStart);
        buttonScore = (Button) view.findViewById(R.id.buttonScore);
        buttonZurück = (Button) view.findViewById(R.id.buttonZurück);
        textViewDatum = (TextView) view.findViewById(R.id.textViewDate);

        buttonStart.setOnClickListener(this);
        buttonScore.setOnClickListener(this);
        buttonZurück.setOnClickListener(this);

        progressDialog = ProgressDialog.show(getActivity(), "Load Quiz", "Loading..", true, false);
        QuizController quizController = new QuizController(getActivity());
        quizController.setUpdateInterface(new QuizController.UpdateUI() {
            @Override
            public void updateUI(String datum) {
                progressDialog.dismiss();
                textViewDatum.setText("Letztes Update "+datum);
            }

        });
        quizController.getQuizData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStart:
                next.getNextFragment();
                break;
            case R.id.buttonScore:break;
            case R.id.buttonZurück:getActivity().onBackPressed(); break;
            default:break;

        }
    }

    public interface nextFragment {
        public void getNextFragment();
    }
    public void setUpInterface(nextFragment nextFragment){
        this.next = nextFragment;
    }

}
