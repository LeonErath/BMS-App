package leon.bms.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import leon.bms.realm.dbKlausur;
import leon.bms.realm.dbRaum;


/**
 * Created by Leon E on 23.05.2016.
 */
public class klausurModel {
    public dbKlausur klausur;
    public dbRaum raum;
    public boolean inThePast;

    public klausurModel() {
    }

    public klausurModel(dbKlausur klausur, dbRaum raum, boolean inThePast) {
        this.klausur = klausur;
        this.raum = raum;
        this.inThePast = inThePast;
    }

    public String getAblaufdatum() {
        if (mathDate() != 1000) {
            if (mathDate() > 0) {
                return "in " + mathDate() + " Tagen";
            } else {
                return "vor " + (mathDate() * -1) + " Tagen";
            }

        }
        return null;
    }

    public long mathDate() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = klausur.getDatum();

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = new Date();
            long diff = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) * -1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1000;
    }

    public String getDateString(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            calendar.setTime(myFormat.parse(klausur.getDatum()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("EEEE', 'dd. MMMM yyyy " );

        return sdfmt.format(calendar.getTime());
    }

    public String getZeitString(){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            calendar.setTime(myFormat.parse(klausur.getBeginn()));
            calendar2.setTime(myFormat.parse(klausur.getEnde()));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat sdfmt = new SimpleDateFormat("HH:mm" );

        return sdfmt.format(calendar.getTime())+"\n-\n"+sdfmt.format(calendar2.getTime());
    }

    public Date getDate(){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = klausur.getDatum();

        try {
            Date date1 = myFormat.parse(inputString1);
            return date1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getColor() {
        if (mathDate() != 1000) {
            if (mathDate() >= 8) {
                return "#408000";
            }
            if (mathDate() <= 7) {
                if (mathDate() <= 1) {
                    if (mathDate() < 0){
                        return "#a6a6a6";
                    }
                    return "#FF0606";
                }else {
                    return "#FF8000";
                }
            }


        }
        return null;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbRaum getRaum() {
        return raum;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
    }

    public boolean isInThePast() {
        return inThePast;
    }

    public void setInThePast(boolean inThePast) {
        this.inThePast = inThePast;
    }
}
