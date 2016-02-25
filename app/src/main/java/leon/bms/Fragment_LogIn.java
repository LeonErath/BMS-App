package leon.bms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Leon E on 13.11.2015.
 */

/** @Fragment_LogIN ist ein Fragment , welches den LogIn behandelt sowie das nach potentiellen
 *  Daten updates zu kümmern. Dieses Fragment wird immer beim ersten Start der App gezeigt. Es sorgt
 *  für einen sicheren Login und leitet einen dann dementsprechend weiter ob der User schon seinen
 *  Stundenplan erstellt hat oder nicht.
 */
public class Fragment_LogIn extends Fragment {

    // defining the Views
    EditText user;
    EditText pass;
    EditText stufe;
    String User;
    String Pass;
    Button btnLogIn;
    String Stufe;
    String result;
    private static final String TAG =Fragment_LogIn.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }


    /** @onViewCreated beschäftigt sich mit allen Aufgaben des Framentes
     */
    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        user = (EditText) view.findViewById(R.id.editText);
        pass = (EditText) view.findViewById(R.id.editText2);
        stufe = (EditText) view.findViewById(R.id.editText3);
        btnLogIn = (Button) view.findViewById(R.id.LogIn);

        checkUser();
        /** @setOnClickListener wird ausgelöst wenn der User den LogIn Button clickt
         *  die eingegeben Daten des User werden an den LogInController weitergegeben der diese
         *  dann überprüft
         *      wenn der LogIn korrekt war wird man auf die nächste Activity weitergeleitet
         *      wenn der LogIn nicht korrekt war wird es dem User mitgeteilt sodass er darauf
         *          reagieren kann
         */
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"User login started");
                // erstmal überprüfen ob beide Textfelder für den Benutzername und das Passwort
                // nicht leer sind
                if (isEmpty(user)==false && isEmpty(pass)==false){
                    Log.d(TAG,"Textfelder nicht leer");
                    User = user.getText().toString();
                    Pass = pass.getText().toString();
                    Stufe = stufe.getText().toString();
                    //login website
                    LogInController logInController=new LogInController(getActivity());
                    // dem LogInConroller werden die Daten der Textfelder übermittelt
                    result = logInController.login(User, Pass, Stufe);
                    // Es wird überprüft ob der LogIn korrekt war
                    if (result != "Error"){
                        Log.d(TAG,"Login erfolgreich");
                        if (new dbUser().checkUser() == true){
                            Log.d(TAG,"User vorhanden");
                            dbUser vorhandenerUser = new dbUser().getUser();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (jsonObject != null){
                                String Benutzername = jsonObject.optString("username");
                                Log.d(TAG,"username: "+Benutzername+" "+vorhandenerUser.getBenutzername());
                                if (Benutzername.equals(vorhandenerUser.getBenutzername())){
                                    Log.d(TAG,"vorhandener User ist der gleiche wie der angemeldete User");
                                    if (vorhandenerUser.validData == true){
                                        Log.d(TAG,"ValidData = true -> MainActivity");
                                        Intent intent = new Intent(getActivity(),MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }else {
                                        Log.d(TAG,"ValidData = false -> KursauswahlActivity");
                                        Intent intent = new Intent(getActivity(),KursauswahlActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                } else {
                                    Log.d(TAG,"vorhandener User ist NICHT der gleiche wie der angemeldete");
                                    deleteDB();
                                    dbUser user2 = logInController.createUser(result);
                                    if (user2 != null){
                                        // das Passswort des Users wird gespeichert sodass er es nicht beim
                                        // nächsten Login eingeben muss
                                        logInController.savePass(Pass);
                                        StundenplanController stundenplanController = new StundenplanController(getActivity());
                                        // es wird geguckt ob neue Updates vorhanden sind
                                        stundenplanController.checkUpdate();
                                        // danach wird der User weitergeleitet auf die Kursauswahl
                                        Intent intent = new Intent(getActivity(),KursauswahlActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Log.d(TAG,"neuer User konnte nicht erstellt werden");
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG,"Kein User vorhanden");
                            dbUser user2 = logInController.createUser(result);
                            if (user2 != null){

                                // das Passswort des Users wird gespeichert sodass er es nicht beim
                                // nächsten Login eingeben muss
                                logInController.savePass(Pass);
                                StundenplanController stundenplanController = new StundenplanController(getActivity());
                                // es wird geguckt ob neue Updates vorhanden sind
                                stundenplanController.checkUpdate();
                                // danach wird der User weitergeleitet auf die Kursauswahl
                                Intent intent = new Intent(getActivity(),KursauswahlActivity.class);
                                startActivity(intent);

                            }else{
                                Log.d(TAG,"neuer User konnte nicht erstellt werden");
                            }
                        }
                    }else {
                        Log.d(TAG,"Login fehlgeschlagen");
                        Toast.makeText(getActivity(), "Login fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG,"Testfelder nicht korrekt");
                       // Benutzer oder User feld Leer
                    Toast.makeText(getActivity(),"Bitte Username und Passwort eingeben", Toast.LENGTH_SHORT).show();
                }
                }


        });


    }
    public void checkUser(){
        Log.d(TAG,"User-Check beginnt");
        if (new dbUser().checkUser()==true){
            Log.d(TAG,"User vorhanden");
            dbUser vorhandenerUser = new dbUser().getUser();
            if (vorhandenerUser.loggedIn == true){
                Log.d(TAG,"User ist schon angemeldet");
                if (vorhandenerUser.validData != null && vorhandenerUser.validData==true){
                    Log.d(TAG,"ValidData = true -> MainActivity");
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    Log.d(TAG,"ValidData = false -> KursauswahlActivity");
                    StundenplanController stundenplanController = new StundenplanController(getActivity());
                    stundenplanController.checkUpdate();
                    Intent intent = new Intent(getActivity(),KursauswahlActivity.class);
                    startActivity(intent);
                }
            }else {
                Log.d(TAG,"User hat sich vorher abgemeldet");
                pass.setText(new LogInController(getActivity()).getPass());
                user.setText(vorhandenerUser.getBenutzername());
                stufe.setText(vorhandenerUser.getStufe());
            }
        }else {
            Log.d(TAG,"Kein User vorhanden");
            Intent intent = new Intent(getActivity(),IntroductionActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


    }
    public void deleteDB(){
        Log.d(TAG,"Datenbank wurde zurückgesetzt");
        dbUser.deleteAll(dbUser.class);
        dbAufgabe.deleteAll(dbAufgabe.class);
        dbKurs.deleteAll(dbKurs.class);
        dbSchulstunde.deleteAll(dbSchulstunde.class);
        dbMediaFile.deleteAll(dbMediaFile.class);
        dbLehrer.deleteAll(dbLehrer.class);
    }


    /** @isEmpty überprüft ob das Textfeld leer ist
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }






}
