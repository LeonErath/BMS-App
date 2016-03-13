package leon.bms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Created by Leon E on 06.12.2015.
 */

/**
 * Universelle Klasse atOnline ist dafür das die Daten vom Server zu laden
 * Um diese vom Server zu laden benutzt man ein AsyncTask der alles in einem seperaten Thread verarbeitet
 * und anschließen wenn er fertig ist durch ein Interface an die Activity oder Klasse weitergibt.
 * Der AsyncTask gibt die bekommenen Daten als String zurück.
 * WICHTIG: Um diese Klasse zu benutzen müssen vorher die "Permissions" im Manifest geändert werden , sodass
 * zugang zum Internet überhaupt gestattet wird
 */
public class atOnline extends AsyncTask<String, Void, String> {

    // @parsedString ist nacher der String der die Ergebnisse der Onlline-Abfrage enthält
    String parsedString;
    // @urlString ist der String der die URL beinhaltet von der die Daten geholt werden sollen
    // diese ist normalerweise app.marienschule/ ...
    private final String urlString;
    // @parameter dieser String enthält die Angaben für die POST Anfrage z.B.: Username , Password etc.
    private final String parameter;
    // @listener dieser Listener wird ausgelöst wenn die Daten Abfrage fertig ist
    OnUpdateListener listener;
    // @mainContext dieser Context ist für theoretische UI elemente da
    Context mainContext;


    /**
     * Constructor erhält alles was er für die Datenabfrage braucht.
     * Bekommen die URL , die Angaben für die POST Anfrage und den Context für potenntielle UI Interaktionen
     */
    public atOnline(String url, String params, Context context) {
        this.urlString = url;
        this.parameter = params;
        this.mainContext = context;
    }


    /**
     * Automatische generierte Methode um vorher Dinge vorzubereiten
     * Wird hier erstmal nicht gebraucht.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Wichtigste Methode !
     * Hier passiert alles was mit der Datenabfrage zutun hat.
     *
     * @doInBackground wird automatisch in einem seperate Thread ausgeführt
     * Stellt erst die Verbindung her und dann verarbeitet er die Daten weiter und gibt sie als String zurück
     */
    @Override
    protected String doInBackground(String... params) {

        // @parsedString wird erstmal initilisiert damit nacher NullPointerExeption auftreten
        parsedString = "";


        /** Sehr Wichtig!
         *  Überprüft erst ob die Internet Verbindung vorhanden ist bevor er die Datenanfrage sendet
         *  @isInternetOn return boolean / true : Internet Verbindung besteht / false : Internet Verbindung ist unterbrochen
         */
        if (isInternetOn(mainContext) == true) {
            URL url = null;
            try {
                //Verbindung wird aufgebaut bzw. Einstellungen werden festgelegt
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) connection;
                httpConn.setReadTimeout(15000);
                httpConn.setConnectTimeout(15000);
                //Wichtig hier das die Methode POST ist sonst ist die Anfrage auf app.marienschule nicht möglich
                httpConn.setRequestMethod("POST");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);

                OutputStream os = httpConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(parameter);
                writer.flush();
                writer.close();
                os.close();
                // Verbindung wird hergestellt
                httpConn.connect();

                /** @httpStatus gibt den Status an den der Server zurückgibt
                 *  Je nach seinem Status ist die Verbindung erfolgreich oder ein Fehler ist aufgetreten
                 *  Dieser ist nacher auch wichtig für die Fehlermeldung
                 */
                int httpStatus = httpConn.getResponseCode();

                /** Wenn der Status "Okay" ist werden die Daten übertragen und in einen String geparsed
                 *  @return parsedString
                 */
                if (httpStatus == httpConn.HTTP_OK) {
                    Log.d("LOGIN", "Verbindung hergestellt.. ");
                    InputStream is = httpConn.getInputStream();
                    parsedString = convertinputStreamToString(is);
                    return parsedString;
                } else {
                    // Falls die Internet Verbindung Fehlerhaft war wird erstmal Error zurückgegeben
                    parsedString = "404";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /** Falls keine Internet Verbindung vorhanden ist wird ein Toast an den User gesendet damit er
             *  gegebenfalls drauf reagieren kann .
             */

            return "Error";
        }
        // Falls die Anfrage nicht geklappt hat gibt er den leeren String zurück bzw. "Error"
        return parsedString;
    }

    /**
     * Methode um einen Input Stream in einen String zu verwandeln
     * Erstmal Vernachlässigbar
     */
    public String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }

            return sb.toString();
        } else {
            return "";
        }
    }


    /**
     * @onPostExecute wird aufgerufen wenn doInBackground fertig ist
     * Diese Methode hat wieder zugriff auf UI Elemente
     * Die Methode löst den listener aus der Die Daten weitergibt
     */
    @Override
    protected void onPostExecute(String s) {
        //listener muss vorher in der Klasse initilisiert sein
        if (listener != null) {
            listener.onUpdate(s);
        }

    }

    /**
     * Methode die die Internet Verbindung überprüft
     * Dafür Notwendig ist der Context
     *
     * @ConnectivityManager guckt ob Verbindung Vorhanden ist
     */
    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Test ob die Verbindung vorhanden ist / true : wenn ja / false : wenn nein
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Inteface onUpdateListener wird gebraucht um die Daten von dem AsnycTask in die Klasse zu holen
     * Beinhaltet nur eine Methode die , die Ergebnis @result übergeben
     */
    public interface OnUpdateListener {
        public void onUpdate(String result);
    }

    /**
     * Methode MUSS vorher in der Klasse aufgerufen werden
     * HIer wird das Interface "initlisiert"
     * Wenn dies nicht passiert ist stürtzt die Application ab
     */
    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }


}

