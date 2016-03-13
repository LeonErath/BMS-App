package leon.bms;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
 * @Fragment_QuizFrage zeigt die Fragen sowie die Antwortmöglichkeiten des Quizes an. Bekommt beim
 * erstellen des Frament einen Themenbereich. Geht alle Fragen des Themenbereiches durch und überprüft
 * bzw speichert die Ergebnis des Users. Wenn alle Fragen beanwortet worden werden die Ergebnis in
 * Fragment_QuizErgebnis angezeigt.
 */
public class Fragment_QuizFrage extends Fragment implements View.OnClickListener {

    //listener to communicate with the acitvity
    private OnFragmentInteractionListener mListener;
    // important for an Animation
    FrameLayout reveal4;
    // views
    TextView textViewThemenbereich, textViewFrageID, textViewFrage, textViewCounter;
    TextView textViewAntwort1, textViewAntwort2, textViewAntwort3, textViewAntwort4, textViewContinue;
    ImageView imageViewCancel;
    // important to check results
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
    boolean fromFragmentErgebnis = false;
    Boolean[] placeofAntwort = new Boolean[5];
    List<quizfragen> quizfragenList = new ArrayList<>();
    List<quizfragen> ergebnisQuizFrage;
    String kursString;
    List<dbFragen> fragenList2;
    int position;

    public Fragment_QuizFrage() {
        // Required empty public constructor
    }

    public Fragment_QuizFrage(Long themenbereichID) {
        this.themenbereichID = themenbereichID;
    }

    public Fragment_QuizFrage(String kursID, List<dbFragen> fragenList) {
        this.kursString = kursID;
        this.fragenList2 = fragenList;
    }

    public Fragment_QuizFrage(Long themenbereichID, List<quizfragen> quizfragenList, int position) {
        this.themenbereichID = themenbereichID;
        this.ergebnisQuizFrage = quizfragenList;
        this.position = position;
        counter = position + 1;
        fromFragmentErgebnis = true;
    }

    public Fragment_QuizFrage(String kursString, List<dbFragen> fragenList, List<quizfragen> quizfragenList, int position) {
        this.kursString = kursString;
        this.ergebnisQuizFrage = quizfragenList;
        this.position = position;
        this.fragenList2 = fragenList;
        counter = position + 1;
        fromFragmentErgebnis = true;
    }

    /**
     * @param outState
     * @onSaveInstanceState wird benötigt falls Fragment zerstört wird z.B.: bei ScreenRotations.
     * Speichert die aktuelle Frage .
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Long id = themenbereichID;
        outState.putLong("id", id);
        super.onSaveInstanceState(outState);
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
        imageViewCancel = (ImageView) view.findViewById(R.id.imageViewCancel);
        imageViewCancel.setOnClickListener(this);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardViewBottombar.setOnClickListener(this);

        if (savedInstanceState != null) {
            //loads Data wenn das Fragment wieder hergestellt wird
            if (savedInstanceState.containsKey("id")) {
                Long id = savedInstanceState.getLong("id");
                this.themenbereichID = id;
            }
        }

        setUpQuiz();
    }


    /**
     * @setUpQuiz lädt die Fragen und zeigt erste Elemente an.
     */
    public void setUpQuiz() {
        if (fragenList2 != null && fragenList2.size() > 0 && kursString != null) {
            fragenList = fragenList2;
            textViewThemenbereich.setText(kursString);
            textViewCounter.setText("Frage " + counter + " von " + fragenList.size());
            setUpFrage(counter, fragenList);
        } else {
            if (new dbThemenbereich().getThemenbereich(themenbereichID) != null) {
                // lädt den Themenbereich
                dbThemenbereich themenbereich = new dbThemenbereich().getThemenbereich(themenbereichID);
                textViewThemenbereich.setText(themenbereich.getName() + " " + themenbereich.kurs.getName());
                if (themenbereich.getFragen(themenbereich.getId()) != null) {
                    //lädt die Fragen des Themenbereiches
                    fragenList = themenbereich.getFragen(themenbereich.getId());
                    if (fragenList.size() > 0) {
                        textViewCounter.setText("Frage " + counter + " von " + fragenList.size());
                        setUpFrage(counter, fragenList);
                    }
                }
            }
        }
    }

    /**
     * @param counter    ist der Counter um zu bestimmen bei welcher Frage der User sich befindet
     * @param fragenList ist die Liste der Fragen des Themenbereiches
     * @setUpFrage zeigt die Frage an
     */
    public void setUpFrage(int counter, List<dbFragen> fragenList) {
        if (fromFragmentErgebnis == true) {
            if (ergebnisQuizFrage != null && ergebnisQuizFrage.size() > 0) {
                textViewFrage.setText(ergebnisQuizFrage.get(position).getFrage());
                textViewFrageID.setText("Frage ID: " + ergebnisQuizFrage.get(position).getFradeID());
                setResults(ergebnisQuizFrage.get(position).getQuizantwortenList());
                frage = fragenList.get(counter - 1);
            }
        } else if (counter > 0 && counter - 1 < fragenList.size()) {
            frage = fragenList.get(counter - 1);
            // zeigt die Frage an
            textViewFrage.setText(frage.getFrage());
            textViewFrageID.setText("Frage ID: " + frage.getServerid());
            setUpAntwort(frage.getId());
        } else {

        }
    }

    /**
     * @param id ist die Fragen id um die Antworten zu laden
     * @setUpAntwort zeigt alle Antworten zufällig an. Da die Antworten teilweise mulitpile choice sind werden
     * zuerst alle richtige Antworten angezeigt und dann die resltichen mit falschen aufgefüllt. Alle Antworten
     * werden immer an zufälligen Positionen angezigt. Die Position und ob die Frage richtig oder Falsch ist wird
     * im @placeofAntwort gepseichert um nacher die Lösung einfach anzuzeigen.
     */
    private void setUpAntwort(Long id) {
        if (new dbFragen().getAnworten(id) != null) {
            // lädt alle Antworten der Frage
            List<dbAntworten> antwortenList = new dbFragen().getAnworten(id);
            richtigeAntworten = new ArrayList<>();
            falscheAntworten = new ArrayList<>();
            //unterteilt die Antworten in richtig oder falsch
            for (dbAntworten antworten : antwortenList) {
                if (antworten.richtig == true) {
                    richtigeAntworten.add(antworten);
                } else {
                    falscheAntworten.add(antworten);
                }
            }
            // überprüft ob genug antworten vorhanden sind
            if (richtigeAntworten.size() > 0 && (richtigeAntworten.size() + falscheAntworten.size() >= 4)) {
                // mischt alle Antworten sowie Positionen zufällig
                ArrayList<Integer> randomOrder = generateRandom(4);
                Collections.shuffle(richtigeAntworten);
                Collections.shuffle(falscheAntworten);
                for (int i = 0; i < randomOrder.size(); i++) {
                    int randomPosition = randomOrder.get(i);
                    // befüllt erstmal alle richtige Antowrten
                    if (richtigeAntworten.size() >= i + 1) {
                        setUpCardView(randomPosition, richtigeAntworten.get(i));
                        Log.d(TAG, randomPosition + " richtig");
                        placeofAntwort[randomPosition] = true;
                    } else {
                        // dann alle falsche Antowrten
                        setUpCardView(randomPosition, falscheAntworten.get(i - richtigeAntworten.size()));
                        Log.d(TAG, randomPosition + " falsch");
                        placeofAntwort[randomPosition] = false;
                    }
                }
            }
        }
    }

    /**
     * @param randomPosition ist die Position der Antwort. Je nach Position wird das richtige CardView
     *                       ausgeählt und die Lösung anhezeigt
     * @param antworten      ist die Antwort die auf der entsprechenden Position angezeigt werden soll
     * @setUpCardView zeigt die Antwort auf dem CardView an
     */
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

    /**
     * @param maxZahl ist die Reichweite aus der eine Zufällige Kombination erzeugt werden soll
     * @return gibt ein Array mit der zufälligen Kombination zurück
     * @generateRandom erstellt zufällig eine Kombination von 1-maxZahl(hier 4) da es immer 4 Antworten gibt die
     * an verschiedenen Positionen angezeigt werden sollen.
     */
    public ArrayList<Integer> generateRandom(int maxZahl) {
        ArrayList<Integer> number = new ArrayList<Integer>();
        for (int i = 1; i <= maxZahl; ++i) number.add(i);
        Collections.shuffle(number);
        return number;
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
     * @param v ist der übergebene view
     * @onClick verwaltet die Click-Events und führt die dementsprechende Aktion aus. Bei den vier
     * Antworten wird eine Animation angezeigt je nachdem ob die Antwort ausgewählt worde oder nicht.
     * Wenn man das cardViewBottom angeclickt wird dann die Lösung angezeigt.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView:
                // animation wird angezeigt je nach Auswahl
                clickCardView(cardView1, textViewAntwort1, selected1, 1);
                break;
            case R.id.cardView2:
                // animation wird angezeigt je nach Auswahl
                clickCardView(cardView2, textViewAntwort2, selected2, 2);
                break;
            case R.id.cardView3:
                // animation wird angezeigt je nach Auswahl
                clickCardView(cardView3, textViewAntwort3, selected3, 3);
                break;
            case R.id.cardView4:
                // animation wird angezeigt je nach Auswahl
                clickCardView(cardView4, textViewAntwort4, selected4, 4);
                break;
            case R.id.imageViewCancel:
                mListener.Fragment_QuitFrageBACK();
                break;
            case R.id.cardViewbottomBar:
                // prüft in welchem Vorgang der User sich befindet
                if (textViewContinue.getText().toString().equals("Antwort anzeigen")) {
                    // wenn der User schon richtige Antworten gesehen hat und jetzt die Lösung sehen will
                    String richtigeAntwortenString = " ";
                    //überprüft ob ein oder Mehr Antworten ausgewält worden sind
                    if (richtigeAntworten.size() > 0) {
                        for (dbAntworten antworten : richtigeAntworten) {
                            richtigeAntwortenString += antworten.antwort + ", ";
                        }
                        String deineAntwort = " ";
                        if (selected1 == true) {
                            deineAntwort += textViewAntwort1.getText().toString() + ", ";
                        }
                        if (selected2 == true) {
                            deineAntwort += textViewAntwort2.getText().toString() + ", ";
                        }
                        if (selected3 == true) {
                            deineAntwort += textViewAntwort3.getText().toString() + ", ";
                        }
                        if (selected4 == true) {
                            deineAntwort += textViewAntwort4.getText().toString() + ", ";
                        }
                        // zeigt die Lösung an
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Frage: " + frage.frage)
                                .setCancelable(false)
                                .setMessage("Richtige Antworten: " + richtigeAntwortenString
                                                + "\n\nBeschreibung: " + richtigeAntworten.get(0).langfassung
                                                + "\n\nDeine Antwort: " + deineAntwort
                                )
                                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // lädt dann die nächste Frage
                                        if (fromFragmentErgebnis == true) {
                                            mListener.Fragment_QuitFrageBACK();
                                        } else {
                                            nextQuestion();
                                        }

                                    }
                                })
                                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // man kann sich nochmal die Frage und Antworten angucken
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {

                    showResults();
                }
                break;
            default:
                break;

        }
    }

    private void setResults(List<quizantworten> quizantwortenList) {
        List<dbAntworten> richtigeAntwortenList = new ArrayList<>();
        List<dbAntworten> falscheAntworten = new ArrayList<>();
        if (quizantwortenList != null && quizantwortenList.size() == 4) {
            for (quizantworten quizantworten : quizantwortenList) {
                switch (quizantworten.getPositionCard()) {
                    case 1:
                        placeofAntwort[1] = quizantworten.isRichtigOderFalsch();
                        if (placeofAntwort[1] == true) {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            richtigeAntwortenList.add(antworten);
                        } else {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            falscheAntworten.add(antworten);
                        }
                        selected1 = quizantworten.isSelection();
                        textViewAntwort1.setText(quizantworten.antwort);
                        if (selected1 == true) {
                            selected1 = false;
                            clickCardView(cardView1, textViewAntwort1, selected1, 1);
                        }
                        break;
                    case 2:
                        placeofAntwort[2] = quizantworten.isRichtigOderFalsch();
                        if (placeofAntwort[2] == true) {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            richtigeAntwortenList.add(antworten);
                        } else {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            falscheAntworten.add(antworten);
                        }
                        selected2 = quizantworten.isSelection();
                        textViewAntwort2.setText(quizantworten.antwort);
                        if (selected2 == true) {
                            selected2 = false;
                            clickCardView(cardView2, textViewAntwort2, selected2, 2);
                        }

                        break;
                    case 3:
                        placeofAntwort[3] = quizantworten.isRichtigOderFalsch();
                        if (placeofAntwort[3] == true) {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            richtigeAntwortenList.add(antworten);
                        } else {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            falscheAntworten.add(antworten);
                        }
                        selected3 = quizantworten.isSelection();
                        textViewAntwort3.setText(quizantworten.antwort);
                        if (selected3 == true) {
                            selected3 = false;
                            clickCardView(cardView3, textViewAntwort3, selected3, 3);
                        }
                        break;
                    case 4:
                        placeofAntwort[4] = quizantworten.isRichtigOderFalsch();
                        if (placeofAntwort[4] == true) {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            richtigeAntwortenList.add(antworten);
                        } else {
                            dbAntworten antworten = new dbAntworten();
                            antworten.antwort = quizantworten.antwort;
                            falscheAntworten.add(antworten);
                        }
                        selected4 = quizantworten.isSelection();
                        textViewAntwort4.setText(quizantworten.antwort);
                        if (selected4 == true) {
                            selected4 = false;
                            clickCardView(cardView4, textViewAntwort4, selected4, 4);
                        }
                        break;
                }
            }
        }
        richtigeAntworten = richtigeAntwortenList;
        showResults();
    }

    private void showResults() {
        // zeigt dem User an welche Antworten die richtigen gewesen wäre und ob seine Auswahl falsch oder richtig war
        if (selected1 || selected2 || selected3 || selected4) {
            textViewContinue.setText("Antwort anzeigen");
            // onClick events disabled
            disableCardView();
            // zeigt die Lösung farbig an
            generealCheck(cardView1, selected1, 1, textViewAntwort1);
            generealCheck(cardView2, selected2, 2, textViewAntwort2);
            generealCheck(cardView3, selected3, 3, textViewAntwort3);
            generealCheck(cardView4, selected4, 4, textViewAntwort4);
            // speichert die Lösung
            checkRight();
        }
    }

    /**
     * @checkRight speichert ob die Frage richtig oder Falsch beanwortet wurde
     */
    public void checkRight() {
        if (richtigOderFalsch == true) {
            Log.d(TAG, "Richtig!");
            dbFragen fragen = fragenList.get(counter - 1);
            fragen.richtigCounter++;
            fragen.save();
        } else {
            dbFragen fragen = fragenList.get(counter - 1);
            fragen.falschCounter++;
            fragen.save();
            Log.d(TAG, "Falsch!");
        }
    }

    /**
     * @nextQuestion lädt die nächste Frage wenn es eine gibt
     */
    public void nextQuestion() {
        counter++;
        quizfragenList.add(getQuizfragen());

        if (fragenList.size() >= counter) {

            // reset views
            richtigOderFalsch = true;
            enableCardView();
            textViewContinue.setText("Lösen");
            textViewCounter.setText("Frage " + counter + " von " + fragenList.size());
            animateViewRippleOUT(cardView1, textViewAntwort1);
            animateViewRippleOUT(cardView2, textViewAntwort2);
            animateViewRippleOUT(cardView3, textViewAntwort3);
            animateViewRippleOUT(cardView4, textViewAntwort4);
            selected1 = false;
            selected2 = false;
            selected3 = false;
            selected4 = false;

            setUpFrage(counter, fragenList);
        } else {
            // falls es keine Fragen mehr gibt wird das ergebnis angezeigt
            if (fragenList2 != null && fragenList2.size() > 0 && kursString != null) {
                mListener.Fragment_QuizFrageShowErgebnisSpecial(kursString, quizfragenList, fragenList);
            } else {
                mListener.Fragment_QuizFrageShowErgebnis(themenbereichID, quizfragenList);
            }


            Log.d(TAG, "Ergebnis");
        }

    }

    public quizfragen getQuizfragen() {
        //save results for Fragment_Ergebnis
        List<quizantworten> quizantwortenList = new ArrayList<>();
        quizantworten quizantworten1 = new quizantworten();
        quizantworten1.antwort = textViewAntwort1.getText().toString();
        quizantworten1.selection = selected1;
        quizantworten1.richtigOderFalsch = placeofAntwort[1];
        quizantworten1.positionCard = 1;
        quizantwortenList.add(quizantworten1);

        quizantworten quizantworten2 = new quizantworten();
        quizantworten2.antwort = textViewAntwort2.getText().toString();
        quizantworten2.selection = selected2;
        quizantworten2.richtigOderFalsch = placeofAntwort[2];
        quizantworten2.positionCard = 2;
        quizantwortenList.add(quizantworten2);

        quizantworten quizantworten3 = new quizantworten();
        quizantworten3.antwort = textViewAntwort3.getText().toString();
        quizantworten3.selection = selected3;
        quizantworten3.richtigOderFalsch = placeofAntwort[3];
        quizantworten3.positionCard = 3;
        quizantwortenList.add(quizantworten3);

        quizantworten quizantworten4 = new quizantworten();
        quizantworten4.antwort = textViewAntwort4.getText().toString();
        quizantworten4.selection = selected4;
        quizantworten4.richtigOderFalsch = placeofAntwort[4];
        quizantworten4.positionCard = 4;
        quizantwortenList.add(quizantworten4);

        quizfragen quizfragen = new quizfragen();
        quizfragen.frage = frage.frage;
        quizfragen.langfassung = frage.langfassung;
        quizfragen.fradeID = frage.getServerid();
        quizfragen.themenbereichFrage = frage.themenbereich.name;
        quizfragen.richtigOderFalsch = richtigOderFalsch;
        quizfragen.setQuizantwortenList(quizantwortenList);

        return quizfragen;
    }

    /**
     * @param cardView  ist das cardView das sich ausgewählt wurde
     * @param textView  ist der text der sich farbig verändert
     * @param Selection zur Bestimmung ob das CardView schon ausgeählt ist oder nicht
     * @param position  position des CardView
     * @clickCardView animiert die Auswahl und zeigt sie farbig an. Ändert dabei auch die textFarbe.
     */
    public void clickCardView(CardView cardView, TextView textView, Boolean Selection, int position) {
        if (Selection == false) {
            //falls das CardView nicht ausgewählt war
            //animate rippleeffect
            animateViewRippleIN(cardView);
            //change view color
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
            //falls das CardView  ausgewählt war
            //animate rippleeffect
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

    /**
     * @param cardView  welches je nach Lösung die Farbe verändert
     * @param selection die Auswahl des Users
     * @param position  die Position des CardView
     * @param textView  der TextView der seine TextFarbe ändert je nach Lösung
     * @generalCheck wird ausgeführt wenn die Lösung angezeigt wird. Überprüft die Auswahl mit der Lösung
     * wenn die Auswahl richtig ist dann wird das CardView grün gefärbt und wenn die Auswahl falsch ist rot.
     */
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

    /**
     * @disavleCardView disables the CardView Clicl-Events
     */
    public void disableCardView() {
        cardView1.setClickable(false);
        cardView2.setClickable(false);
        cardView3.setClickable(false);
        cardView4.setClickable(false);
    }

    /**
     * @enableCardView enables the CardView Clicl-Events
     */
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

    /**
     * applys the animation "in" . Wenn ein CardView ausgewählt wird
     *
     * @param view
     */
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

    /**
     * applys the animation "out" . Wenn ein CardView nicht mehr ausgewählt wird
     *
     * @param view
     */
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
                // ändert die Hintergrundfarbe und Textfarbe nachdem die Animation beendet ist
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
        void Fragment_QuizFrageShowErgebnis(long themenbereichID, List<quizfragen> quizfragenList);

        void Fragment_QuizFrageShowErgebnisSpecial(String kursid, List<quizfragen> quizfragenList, List<dbFragen> fragenList);

        void Fragment_QuitFrageBACK();
    }
}
