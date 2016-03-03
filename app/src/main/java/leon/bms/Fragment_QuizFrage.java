package leon.bms;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_QuizFrage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_QuizFrage extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    FrameLayout reveal4;
    TextView textViewThemenbereich, textViewFrageID, textViewFrage, textViewCounter;
    TextView textViewAntwort1, textViewAntwort2, textViewAntwort3, textViewAntwort4, textViewContinue;
    ImageView imageViewCancel;
    boolean selected1 = false, selected2 = false, selected3 = false, selected4 = false;
    CardView cardView1, cardView2, cardView3, cardView4, cardViewBottombar;
    private static String TAG = Fragment_QuizFrage.class.getSimpleName();
    Long themenbereichID;
    dbFragen frage;
    boolean richtigOderFalsch = true;
    List<dbFragen> fragenList;
    List<dbAntworten> falscheAntworten;
    List<dbAntworten> richtigeAntworten;
    int counter = 1;
    boolean mAlreadyLoaded=false;
    Boolean[] placeofAntwort = new Boolean[5];


    public Fragment_QuizFrage(Long themenbereichID) {
        // Required empty public constructor
        this.themenbereichID = themenbereichID;
    }

    public Fragment_QuizFrage(Long themenbereichID, int position) {
        // Required empty public constructor
        this.themenbereichID = themenbereichID;
        this.counter = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__quiz_frage, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            // Do this code only first time, not after rotation or reuse fragment from backstack
        textViewThemenbereich = (TextView) view.findViewById(R.id.textViewThemenbereich);
        textViewFrageID = (TextView) view.findViewById(R.id.textViewFrageID);
        textViewFrage = (TextView) view.findViewById(R.id.textViewFrage);
        imageViewCancel = (ImageView) view.findViewById(R.id.imageViewCancel);
        textViewAntwort1 = (TextView) view.findViewById(R.id.textViewAntwort1);
        textViewAntwort2 = (TextView) view.findViewById(R.id.textViewAntwort2);
        textViewAntwort3 = (TextView) view.findViewById(R.id.textViewAntwort3);
        textViewAntwort4 = (TextView) view.findViewById(R.id.textViewAntwort4);
        textViewContinue = (TextView) view.findViewById(R.id.textViewContinue);
        textViewCounter = (TextView) view.findViewById(R.id.textViewCounter);
        cardView1 = (CardView) view.findViewById(R.id.cardView);
        cardView2 = (CardView) view.findViewById(R.id.cardView2);
        cardView3 = (CardView) view.findViewById(R.id.cardView3);
        cardView4 = (CardView) view.findViewById(R.id.cardView4);
        cardViewBottombar = (CardView) view.findViewById(R.id.cardViewbottomBar);
        reveal4 = (FrameLayout) view.findViewById(R.id.reveal4);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardViewBottombar.setOnClickListener(this);

        setUpQuiz();
    }



    public void setUpQuiz() {
        if (new dbThemenbereich().getThemenbereich(themenbereichID) != null) {
            dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(themenbereichID);
            textViewThemenbereich.setText(themenbereich.getName() + " " + themenbereich.kurs.getName());
            if (themenbereich.getFragen(themenbereich.getId()) != null) {
                fragenList = themenbereich.getFragen(themenbereich.getId());
                if (fragenList.size() > 0) {
                    textViewCounter.setText("Frage " + counter + " von " + fragenList.size());
                    setUpFrage(counter, fragenList);
                }
            }
        }
    }

    public void setUpFrage(int counter, List<dbFragen> fragenList) {
        if (counter > 0 && counter-1< fragenList.size()) {
            frage = fragenList.get(counter - 1);
            textViewFrage.setText(frage.getFrage());
            setUpAntwort(frage.getId());
        }else {

        }
    }

    private void setUpAntwort(Long id) {
        if (new dbFragen().getAnworten(id) != null) {
            List<dbAntworten> antwortenList = new dbFragen().getAnworten(id);
            richtigeAntworten = new ArrayList<>();
            falscheAntworten = new ArrayList<>();
            for (dbAntworten antworten : antwortenList) {
                if (antworten.richtig == true) {
                    richtigeAntworten.add(antworten);
                } else {
                    falscheAntworten.add(antworten);
                }
            }
            if (richtigeAntworten.size() > 0 && (richtigeAntworten.size() + falscheAntworten.size() >= 4)) {
                ArrayList<Integer> randomOrder = generateRandom(4);
                Collections.shuffle(richtigeAntworten);
                Collections.shuffle(falscheAntworten);
                for (int i = 0; i < randomOrder.size(); i++) {
                    int randomPosition = randomOrder.get(i);
                    if (richtigeAntworten.size() >= i + 1) {
                        setUpCardView(randomPosition, richtigeAntworten.get(i));
                        Log.d(TAG, randomPosition + " richtig");
                        placeofAntwort[randomPosition] = true;
                    } else {
                        setUpCardView(randomPosition, falscheAntworten.get(i - richtigeAntworten.size()));
                        Log.d(TAG, randomPosition + " falsch");
                        placeofAntwort[randomPosition] = false;
                    }
                }
            }
        }
    }

    public void setUpCardView(int randomPosition, dbAntworten antworten) {
        switch (randomPosition) {
            case 1:
                textViewAntwort1.setText(antworten.antwort);
                break;
            case 2:
                textViewAntwort2.setText(antworten.antwort);
                break;
            case 3:
                textViewAntwort3.setText(antworten.antwort);
                break;
            case 4:
                textViewAntwort4.setText(antworten.antwort);
                break;
            default:
                break;
        }
    }


    public ArrayList<Integer> generateRandom(int maxZahl) {
        ArrayList<Integer> number = new ArrayList<Integer>();
        for (int i = 1; i <= maxZahl; ++i) number.add(i);
        Collections.shuffle(number);
        return number;
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
        switch (v.getId()) {
            case R.id.cardView:
                clickCardView(cardView1, textViewAntwort1, selected1, 1);
                break;
            case R.id.cardView2:
                clickCardView(cardView2, textViewAntwort2, selected2, 2);
                break;
            case R.id.cardView3:
                clickCardView(cardView3, textViewAntwort3, selected3, 3);
                break;
            case R.id.cardView4:
                clickCardView(cardView4, textViewAntwort4, selected4, 4);
                break;
            case R.id.cardViewbottomBar:
                if (textViewContinue.getText().toString().equals("Antwort anzeigen")) {
                    String richtigeAntwortenString = " ";
                    if (richtigeAntworten.size() > 0) {
                        for (dbAntworten antworten : richtigeAntworten) {
                            richtigeAntwortenString += antworten.antwort + ", ";
                        }
                        String deineAntwort=" ";
                        if (selected1 == true){
                            deineAntwort+=textViewAntwort1.getText().toString()+", ";
                        }
                        if (selected2 == true){
                            deineAntwort+=textViewAntwort2.getText().toString()+", ";
                        }
                        if (selected3 == true){
                            deineAntwort+=textViewAntwort3.getText().toString()+", ";
                        }
                        if (selected4 == true){
                            deineAntwort+=textViewAntwort4.getText().toString()+", ";
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Frage: " + frage.frage)
                                .setCancelable(false)
                                .setMessage("Richtige Antworten: " + richtigeAntwortenString
                                                + "\n\nBeschreibung: " + richtigeAntworten.get(0).langfassung
                                                + "\n\nDeine Antwort: " + deineAntwort
                                )
                                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        nextQuestion();
                                    }
                                })
                                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                    if (selected1 || selected2 || selected3 || selected4) {
                        textViewContinue.setText("Antwort anzeigen");
                        disableCardView();
                        generealCheck(cardView1, selected1, 1, textViewAntwort1);
                        generealCheck(cardView2, selected2, 2, textViewAntwort2);
                        generealCheck(cardView3, selected3, 3, textViewAntwort3);
                        generealCheck(cardView4, selected4, 4, textViewAntwort4);
                        checkRight();
                    }
                }
                break;
            default:
                break;

        }
    }
    public void checkRight(){
        if (richtigOderFalsch == true){
            Log.d(TAG,"Richtig!");
            dbFragen fragen = fragenList.get(counter-1);
            fragen.richtigCounter++;
            fragen.save();
        }else {
            Log.d(TAG,"Falsch!");
        }
    }

    public void nextQuestion(){
        counter++;
        if (fragenList.size()>=counter){
            richtigOderFalsch = true;
            enableCardView();
            textViewContinue.setText("Lösen");
            textViewCounter.setText("Frage " + counter + " von " + fragenList.size());
            setUpFrage(counter,fragenList);
        }else {
            mListener.Fragment_QuizFrageShowErgebnis(themenbereichID);

            Log.d(TAG,"Ergebnis");
        }

    }

    public void clickCardView(CardView cardView, TextView textView, Boolean Selection, int position) {
        if (Selection == false) {
            animateViewRippleIN(cardView);
            cardView.setCardBackgroundColor(Color.parseColor("#00b9ee"));
            textView.setTextColor(Color.parseColor("#ffffff"));
            switch (position) {
                case 1:
                    selected1 = true;
                    break;
                case 2:
                    selected2 = true;
                    break;
                case 3:
                    selected3 = true;
                    break;
                case 4:
                    selected4 = true;
                    break;
            }
        } else {
            animateViewRippleOUT(cardView, textView);
            switch (position) {
                case 1:
                    selected1 = false;
                    break;
                case 2:
                    selected2 = false;
                    break;
                case 3:
                    selected3 = false;
                    break;
                case 4:
                    selected4 = false;
                    break;
            }
        }
    }

    public void generealCheck(CardView cardView, Boolean selection, int position, TextView textView) {
        if (selection == true) {
            if (selection == placeofAntwort[position]) {
                animateViewRippleIN(cardView);
                cardView.setCardBackgroundColor(Color.parseColor("#b7d167"));
                textView.setTextColor(Color.parseColor("#ffffff"));
            } else {
                richtigOderFalsch = false;
                animateViewRippleIN(cardView);
                cardView.setCardBackgroundColor(Color.parseColor("#e30613"));
                textView.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            if (selection != placeofAntwort[position]) {
                if (placeofAntwort[position] == true) {
                    richtigOderFalsch = false;
                    animateViewRippleIN(cardView);
                    cardView.setCardBackgroundColor(Color.parseColor("#b7d167"));
                    textView.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    richtigOderFalsch = false;
                    Log.d(TAG, "false " + placeofAntwort[position]);
                    animateViewRippleIN(cardView);
                    cardView.setCardBackgroundColor(Color.parseColor("#e30613"));
                    textView.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        }
    }


    public void disableCardView() {
        cardView1.setClickable(false);
        cardView2.setClickable(false);
        cardView3.setClickable(false);
        cardView4.setClickable(false);
    }

    public void enableCardView() {
        cardView1.setCardBackgroundColor(Color.parseColor("#ffffff"));
        cardView2.setCardBackgroundColor(Color.parseColor("#ffffff"));
        cardView3.setCardBackgroundColor(Color.parseColor("#ffffff"));
        cardView4.setCardBackgroundColor(Color.parseColor("#ffffff"));

        cardView1.setClickable(true);
        cardView2.setClickable(true);
        cardView3.setClickable(true);
        cardView4.setClickable(true);
    }

    public void animateViewRippleIN(final CardView view) {
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;


        // get the final radius for the clipping circle
        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();

    }


    public void animateViewRippleOUT(final CardView view, final TextView tv) {
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;


        // get the final radius for the clipping circle
        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        animator = animator.reverse();
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                view.setCardBackgroundColor(Color.parseColor("#ffffff"));
                tv.setTextColor(Color.parseColor("#000000"));
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();

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
        void Fragment_QuizFrageShowErgebnis(long themenbereichID);
        void Fragment_QuitFrageBACK();
    }
}
