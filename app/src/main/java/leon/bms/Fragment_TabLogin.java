package leon.bms;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.franciscan.materialstepper.AbstractStep;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TabLogin extends AbstractStep  {
    private int i = 1;
    EditText user,pass,stufe;
    String Pass;
    String User;
    String Stufe;
    String result;
    private static final String TAG =Fragment_LogIn.class.getSimpleName();

    public Fragment_TabLogin() {
        // Required empty public constructor
    }

    @Override
    public String name() {
        return "Log in";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment__tab_login, container, false);
        user = (EditText) view.findViewById(R.id.editText);
        pass = (EditText) view.findViewById(R.id.editText2);
        stufe = (EditText) view.findViewById(R.id.editText3);
        return view;
    }
    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean nextIf() {
            User = user.getText().toString();
            Pass = pass.getText().toString();
            Stufe = stufe.getText().toString();
            //login website
            LogInController logInController = new LogInController(getActivity());
            // dem LogInConroller werden die Daten der Textfelder übermittelt
            result = logInController.login(User, Pass, Stufe);
            // Es wird überprüft ob der LogIn korrekt war
            if (result != "Error") {
                Log.d(TAG, "Login erfolgreich");
                dbUser user2 = logInController.createUser(result);

                if (user2 != null) {

                    // das Passswort des Users wird gespeichert sodass er es nicht beim
                    // nächsten Login eingeben muss
                    logInController.savePass(Pass);
                    StundenplanController stundenplanController = new StundenplanController(getActivity());
                    // es wird geguckt ob neue Updates vorhanden sind
                    stundenplanController.checkUpdate();
                    // danach wird der User weitergeleitet auf die Kursauswahl
                    i++;

                } else {
                    Log.d(TAG, "neuer User konnte nicht erstellt werden");
                }
            }

        return i > 1;
    }

    /** @isEmpty überprüft ob das Textfeld leer ist
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    @Override
    public String error() {
        return "<b>Login fehlgeschlagen! </b> <small>Versuche es erneut.</small>";
    }


}
