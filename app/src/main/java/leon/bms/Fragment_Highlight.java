package leon.bms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/** @Fragment_Highlight ist ein Fragment , welches kompakt alles wichtiges anzeigen soll
 *  Es soll die aktuelle Stunde sowie wichtige Neuigkeiten und noch zu machen Hausaufgaben anzeigen
 */
public class Fragment_Highlight extends Fragment {

    TextView textViewNumber,textViewNumber2,textViewLehrer,textViewLehrer2,textViewStunde,textViewStunde2,textViewRaum,textViewRaum2;
    private static String TAG = Fragment_Highlight.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment__highlight, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewNumber = (TextView) view.findViewById(R.id.textViewNumber);
        textViewNumber2 = (TextView) view.findViewById(R.id.textViewNumber2);
        textViewLehrer = (TextView) view.findViewById(R.id.textViewLehrer);
        textViewLehrer2 = (TextView) view.findViewById(R.id.textViewLehrer2);
        textViewStunde = (TextView) view.findViewById(R.id.textViewStunde);
        textViewStunde2 = (TextView) view.findViewById(R.id.textViewStunde2);
        textViewRaum = (TextView) view.findViewById(R.id.textViewRaum);
        textViewRaum2 = (TextView) view.findViewById(R.id.textViewRaum2);

      setUpNächsteStunde();



    }
    public void setUpNächsteStunde(){
        Calendar calendar = Calendar.getInstance();
        Log.d(TAG,calendar.get(Calendar.YEAR)+" ");
        GregorianCalendar[] calendars = new GregorianCalendar[]{
                new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),8,0,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),8,45,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),9,35,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),10,45,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),11,35,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),12,25,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),13,30,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),14,15,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),15,0,0)
                ,new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),15,45,0)


        };
        Calendar date = GregorianCalendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int aktuelleStunde = 0;
        for (int i=calendars.length-1;i>=0;i--){
            Log.d(TAG,"Date: "+date+" CalendarDate: "+calendars[i]);
            if (date.before(calendars[i])){

                if (i==0){
                    aktuelleStunde =1;
                }else {
                    aktuelleStunde= i-1;
                }
                if (date.after(calendars[calendars.length-1])){
                    aktuelleStunde=99;
                }
            }
        }

        Log.d(TAG, "Aktuelle Stunde: " + aktuelleStunde + " wochentag: " + week);

        if (aktuelleStunde!=99 && week>0 && week<6){
            List<dbKurs> aktiveKurse = new dbKurs().getActiveKurse(0);
            aktiveKurse.addAll(new dbKurs().getActiveKurse(1));
            for (dbKurs kurs:aktiveKurse){
                List<dbSchulstunde> schulstundeList = kurs.getSchulStunden(kurs.getId());
                for (dbSchulstunde schulstunde:schulstundeList){

                    textViewStunde.setText("Kein Unterricht");
                    textViewRaum.setText("-");
                    textViewLehrer.setText(" ");
                    textViewNumber.setText(String.valueOf(aktuelleStunde));

                    if (schulstunde.wochentag == week){
                        if (schulstunde.beginnzeit == aktuelleStunde){
                            textViewStunde.setText(schulstunde.kurs.fach);
                            textViewRaum.setText(schulstunde.raum);
                            textViewLehrer.setText(schulstunde.lehrer.titel + " " + schulstunde.lehrer.name);
                            textViewNumber.setText(String.valueOf(schulstunde.beginnzeit));
                        }
                    }
                }
            }
        }

    }
    public List<stunden> convertSchulstundenZuStundeListe(List<dbSchulstunde> wochentagListe){
        // letzteStunde ist die letzte Schulstunde der WochentagListe
        if (wochentagListe.size()>0) {
            int letzteStunde = wochentagListe.get(wochentagListe.size() - 1).beginnzeit;
            // stundenplanListe ist nacher die fertige Liste mit alle Schulstunden und Freistunden
            List<stunden> stundenplanListe = new ArrayList<>();
            // geht alle Stunden von 1 bis zu letzten Stunde durch

            for (int i = 1; i <= letzteStunde; i++) {
                stunden stunden = new stunden();
                int l = 0;
                // überprüft ob die Stunden vorhanden ist
                for (int k = 0; k < wochentagListe.size(); k++) {
                    if (wochentagListe.get(k).getBeginnzeit() == i) {
                        // wenn die wochentagsListe die Zeit i beinhaltet wir l addiert;
                        l = k + 1;
                    }
                }
                // wenn l größer als 0 ist die Stunden vorhanden
                // wenn l nicht größer aks 0 ist wird eine Freistunde erstellt
                if (l > 0) {
                    Log.d("MAIN", "stunde erstellt");
                    dbSchulstunde schulstunde = wochentagListe.get(l - 1);
                    stunden.setActive(true);
                    stunden.setRaum(schulstunde.raum);
                    stunden.setStundenname(schulstunde.kurs.fach);
                    stunden.setLehrer(schulstunde.lehrer.titel + " " + schulstunde.lehrer.name);
                    stunden.setStunde(schulstunde.beginnzeit);
                } else {
                    Log.d("MAIN", "freistunde erstellt");
                    stunden.setStunde(i);
                    stunden.setActive(false);

                }
                stundenplanListe.add(stunden);

            }
            // gibt die fertige stundenplanListe zurück;
            return stundenplanListe;
        } else{
            return new ArrayList<>();
        }
    }
}
