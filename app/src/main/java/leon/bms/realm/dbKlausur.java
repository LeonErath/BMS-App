package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Leon E on 17.06.2016.
 */
public class dbKlausur extends RealmObject{
    @PrimaryKey @Required
    private Integer serverid;
    @Required
    private String name;
    private String beginn;
    private String datum;
    private String ende;
    private String nachschreibertermin;
    private String notizen;
    private Boolean restkursFrei;

    // defining a relationship
    private dbKlausuraufsicht aufsicht;
    private dbKurs kurs;
    private dbNote note;
    private dbRaum raum;
    private dbFehler fehler;
    private dbKlausurinhalt klausurinhalt;

    public dbKlausur() {
        //empty Constructor needed!
    }

    public Integer getServerid() {
        return serverid;
    }

    public void setServerid(Integer serverid) {
        this.serverid = serverid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginn() {
        return beginn;
    }

    public void setBeginn(String beginn) {
        this.beginn = beginn;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getEnde() {
        return ende;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public String getNachschreibertermin() {
        return nachschreibertermin;
    }

    public void setNachschreibertermin(String nachschreibertermin) {
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

    public dbKlausuraufsicht getAufsicht() {
        return aufsicht;
    }

    public void setAufsicht(dbKlausuraufsicht aufsicht) {
        this.aufsicht = aufsicht;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbNote getNote() {
        return note;
    }

    public void setNote(dbNote note) {
        this.note = note;
    }

    public dbRaum getRaum() {
        return raum;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
    }

    public dbFehler getFehler() {
        return fehler;
    }

    public void setFehler(dbFehler fehler) {
        this.fehler = fehler;
    }

    public dbKlausurinhalt getKlausurinhalt() {
        return klausurinhalt;
    }

    public void setKlausurinhalt(dbKlausurinhalt klausurinhalt) {
        this.klausurinhalt = klausurinhalt;
    }
}
