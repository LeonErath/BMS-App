package leon.bms;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKlausur ist eine Tabelle der Datenbank für die Klausuren
 * Sie verwaltet alle Einträge der Klausur und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbKlausur extends SugarRecord {

    //Datensätze der Klausur
    Double dauer;
    Date nachschreibertermin;
    String notizen;
    Boolean restkursFrei;
    Date zeit;

    // defining a relationship
    dbKlausuraufsicht aufsicht;
    dbKurs kurs;
    dbNote note;
    dbRaum raum;

    public dbKlausur() {
        //empty Constructor needed!
    }

    public dbKlausur(Double dauer, Date nachschreibertermin, String notizen, Boolean restkursFrei, Date zeit) {

        this.dauer = dauer;
        this.nachschreibertermin = nachschreibertermin;
        this.notizen = notizen;
        this.restkursFrei = restkursFrei;
        this.zeit = zeit;
    }

    public Double getDauer() {
        return dauer;
    }

    public void setDauer(Double dauer) {
        this.dauer = dauer;
    }

    public Date getNachschreibertermin() {
        return nachschreibertermin;
    }

    public void setNachschreibertermin(Date nachschreibertermin) {
        this.nachschreibertermin = nachschreibertermin;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(String notizen) {
        this.notizen = notizen;
    }

    public Boolean getRestkursFrei() {
        return restkursFrei;
    }

    public void setRestkursFrei(Boolean restkursFrei) {
        this.restkursFrei = restkursFrei;
    }

    public Date getZeit() {
        return zeit;
    }

    public void setZeit(Date zeit) {
        this.zeit = zeit;
    }


}
