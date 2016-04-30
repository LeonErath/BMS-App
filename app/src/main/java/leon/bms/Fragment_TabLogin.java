package leon.bms;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.franciscan.materialstepper.AbstractStep;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TabLogin extends AbstractStep {
    private int i = 1;
    EditText user, pass, stufe;
    String Pass;
    String User;
    String Stufe;
    String result;
    ProgressDialog progressDialog;
    private static final String TAG = Fragment_LogIn.class.getSimpleName();

    public Fragment_TabLogin() {
        // Required empty public constructor
    }

    /**
     * @return "Log in"
     * @name gibt den Namen des Fragments zurück
     */
    @Override
    public String name() {
        return "Log in";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment__tab_login, container, false);
        user = (EditText) view.findViewById(R.id.editText);
        pass = (EditText) view.findViewById(R.id.editText2);
        stufe = (EditText) view.findViewById(R.id.editText3);
        return view;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    /**
     * @return
     * @nextIF wird ausgeführt wenn auf dem "continue" Button geclickt wird.
     * Es wird überprüft ob der Login korrekt ist. Wenn ja wird ein User in der Datenbank
     * erstellt und alle Daten für den User werden heruntergeladen. Danach wird der User auf die
     * Fragment_TabKursauswahl weitergeleitet.
     */
    @Override
    public boolean nextIf() {
        if (i== 1) {
            User = user.getText().toString();
            Pass = pass.getText().toString();
            Stufe = stufe.getText().toString();
            //login website
            LogInController logInController = new LogInController(getActivity());
            // dem LogInConroller werden die Daten der Textfelder übermittelt
            result = logInController.login(User, Pass, Stufe);
            // Es wird überprüft ob der LogIn korrekt war
            if (result != "Error") {
                // login erfolgreich
                Log.d(TAG, "Login erfolgreich");
                dbUser user2 = logInController.createUser(result);

                if (user2 != null) {

                    // das Passswort des Users wird gespeichert sodass er es nicht beim
                    // nächsten Login eingeben muss
                    logInController.savePass(Pass);
                    StundenplanController stundenplanController = new StundenplanController(getActivity());
                    stundenplanController.setUpdateListener(new StundenplanController.OnUpdateListener() {
                        @Override
                        public void onSuccesss() {
                            i++;
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            nextIf();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getActivity(), "Es ist ein Fehler aufgetreten", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // es wird geguckt ob neue Updates vorhanden sind
                    stundenplanController.checkUpdate();
                    progressDialog = ProgressDialog.show(getActivity(), "Loading..", "Check for Updates..", true, false);
                    progressDialog.setCancelable(false);
                    // danach wird der User weitergeleitet auf die Kursauswahl


                } else {
                    Log.d(TAG, "neuer User konnte nicht erstellt werden");
                }
            }
        }
        return i > 1;
    }

    /**
     * @isEmpty überprüft ob das Textfeld leer ist
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    /**
     * @error wird angezeigt wenn der Login nicht erfolgreich war
     */
    public String error() {
        return "<b>Login fehlgeschlagen! </b> <small>Versuche es erneut.</small>";
    }


}
