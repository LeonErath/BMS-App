package leon.bms;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKlausuraufsicht ist eine Tabelle der Datenbank für die Klausuraufsicht
 * Sie verwaltet alle Einträge der Klausuraufsicht und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbKlausuraufsicht extends SugarRecord<dbKlausuraufsicht> {

    //Datensätze der Klausuraufsicht
    Date fromDate;
    Date toDate;

    // defining a relationship
    dbKlausur klausur;
    dbLehrer lehrer;

    public dbKlausuraufsicht() {
        //empty Constructor needed!
    }

    public dbKlausuraufsicht(Date fromDate, Date toDate) {

        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }


}
