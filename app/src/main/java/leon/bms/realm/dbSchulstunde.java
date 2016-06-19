package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;



/**
 * Created by Leon E on 16.06.2016.
 */
public class dbSchulstunde  extends RealmObject {
    @PrimaryKey @Required
    private Integer lesson_id;
    private Double dauer;
    private Integer lesson;
    private String zuletztAktualisiert;
    private String kursID;
    @Index
    private Integer day;
    private int block_number;

    // defining a relationship
    private dbKurs kurs;
    private dbLehrer lehrer;
    private dbVertretung vertretung;
    private dbRaum raum;

    public dbSchulstunde() {
        // empty Constructor needed!
    }

    public Integer getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(Integer lesson_id) {
        this.lesson_id = lesson_id;
    }

    public Double getDauer() {
        return dauer;
    }

    public void setDauer(Double dauer) {
        this.dauer = dauer;
    }

    public Integer getLesson() {
        return lesson;
    }

    public void setLesson(Integer lesson) {
        this.lesson = lesson;
    }

    public String getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(String zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public String getKursID() {
        return kursID;
    }

    public void setKursID(String kursID) {
        this.kursID = kursID;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public int getBlock_number() {
        return block_number;
    }

    public void setBlock_number(int block_number) {
        this.block_number = block_number;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }

    public dbVertretung getVertretung() {
        return vertretung;
    }

    public void setVertretung(dbVertretung vertretung) {
        this.vertretung = vertretung;
    }

    public dbRaum getRaum() {
        return raum;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
    }
}
