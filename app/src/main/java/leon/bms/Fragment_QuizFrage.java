package leon.bms;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView textViewThemenbereich, textViewFrageID, textViewFrage;
    TextView textViewAntwort1, textViewAntwort2, textViewAntwort3, textViewAntwort4, textViewContinue;
    ImageView imageViewCancel;
    boolean selected1 = false, selected2 = false, selected3 = false, selected4 = false;
    CardView cardView1, cardView2, cardView3, cardView4, cardViewBottombar;
    private static String TAG = Fragment_QuizFrage.class.getSimpleName();


    public Fragment_QuizFrage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__quiz_frage, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewThemenbereich = (TextView) view.findViewById(R.id.textViewThemenbereich);
        textViewFrageID = (TextView) view.findViewById(R.id.textViewFrageID);
        textViewFrage = (TextView) view.findViewById(R.id.textViewFrage);
        imageViewCancel = (ImageView) view.findViewById(R.id.imageViewCancel);
        textViewAntwort1 = (TextView) view.findViewById(R.id.textViewAntwort1);
        textViewAntwort2 = (TextView) view.findViewById(R.id.textViewAntwort2);
        textViewAntwort3 = (TextView) view.findViewById(R.id.textViewAntwort3);
        textViewAntwort4 = (TextView) view.findViewById(R.id.textViewAntwort4);
        textViewContinue = (TextView) view.findViewById(R.id.textViewContinue);
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
                Log.d(TAG, "Clicked 1");
                if (selected1 == false) {
                    animateViewRippleIN(cardView1);
                    cardView1.setCardBackgroundColor(Color.parseColor("#00b9ee"));
                    textViewAntwort1.setTextColor(Color.parseColor("#ffffff"));
                    selected1 = true;
                } else {
                    animateViewRippleOUT(cardView1,textViewAntwort1);
                    selected1 = false;
                }
                break;
            case R.id.cardView2:
                Log.d(TAG, "Clicked 2");
                if (selected2 == false) {
                    animateViewRippleIN(cardView2);
                    cardView2.setCardBackgroundColor(Color.parseColor("#00b9ee"));
                    textViewAntwort2.setTextColor(Color.parseColor("#ffffff"));
                    selected2 = true;
                } else {
                    animateViewRippleOUT(cardView2,textViewAntwort2);
                    selected2 = false;
                }
                break;
            case R.id.cardView3:
                Log.d(TAG, "Clicked 3");
                if (selected3 == false) {
                    animateViewRippleIN(cardView3);
                    cardView3.setCardBackgroundColor(Color.parseColor("#00b9ee"));
                    textViewAntwort3.setTextColor(Color.parseColor("#ffffff"));
                    selected3 = true;
                }else {
                    animateViewRippleOUT(cardView3,textViewAntwort3);
                    selected3 = false;
                }
                break;
            case R.id.cardView4:
                Log.d(TAG, "Clicked 4");
                if (selected4 == false) {
                    animateViewRippleIN(cardView4);
                    cardView4.setCardBackgroundColor(Color.parseColor("#00b9ee"));
                    textViewAntwort4.setTextColor(Color.parseColor("#ffffff"));
                    selected4 = true;
                } else {
                    animateViewRippleOUT(cardView4,textViewAntwort4);
                    selected4 = false;
                }
                break;
            case R.id.cardViewbottomBar:
                Log.d(TAG, "Clicked 4");
                textViewContinue.setText("Antwort anzeigen.");
                break;
            default:
                break;

        }
    }
    public void animateViewRippleIN(final CardView view){
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


    public void animateViewRippleOUT(final CardView view, final TextView tv){
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;


        // get the final radius for the clipping circle
        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        animator= animator.reverse();
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                Log.d(TAG, "trigger");
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
        void onFragmentInteraction(Uri uri);
    }
}
