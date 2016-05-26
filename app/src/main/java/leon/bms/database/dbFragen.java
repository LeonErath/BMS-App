package leon.bms.database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */

/**
 * @dbFragen ist eine Tabelle der Datenbank für die Fragen des Quizes.
 * Sie verwaltet alle Fragen des Quizes und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbFragen extends SugarRecord {

    /**
     * @richtigCounter wichtig für die Anzeige alle richtigen bzw. falschen Fragen
     * @schwierigkeit wichtig für die Errechnung des Scores für den User
     * @serverid wichtig um die Beziehung mit den Anworten herzustellen sowie zum raussuchen
     * von Fragen am Server
     * @langfassung Erklärung der Frage . Nacher für die Anzeige der richtigen Antwort wichtig.
     */
    public int anzahlFalschtBeantwortet;
    public int anzahlRichtigBeantwortet;
    public String dateLetzteRichtigeAntwort;
    public String erklaerung;
    public String frage;
    public int globaleid;
    public String hinzugefuegtAm;
    public String imageURL;
    public int punkzahl;
    public int schwirigkeit;
    public int stufe;
    public String zuletztAktualisiert;


    public dbKurs kurs;
    public dbAntworten antworten;
    public dbThemenbereich themenbereich;
    public dbQuiz quiz;
    public int serverid;


    public dbFragen() {
        //empty Constructor needed!
    }

    public dbFragen(int anzahlFalschtBeantwortet, int anzahlRichtigBeantwortet, String dateLetzteRichtigeAntwort, String erklaerung, String frage, int globaleid, String hinzugefuegtAm, String imageURL, int punkzahl, int schwirigkeit, int stufe, String zuletztAktualisiert, dbKurs kurs, dbAntworten antworten, dbThemenbereich themenbereich, dbQuiz quiz, int serverid) {
        this.anzahlFalschtBeantwortet = anzahlFalschtBeantwortet;
        this.anzahlRichtigBeantwortet = anzahlRichtigBeantwortet;
        this.dateLetzteRichtigeAntwort = dateLetzteRichtigeAntwort;
        this.erklaerung = erklaerung;
        this.frage = frage;
        this.globaleid = globaleid;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.imageURL = imageURL;
        this.punkzahl = punkzahl;
        this.schwirigkeit = schwirigkeit;
        this.stufe = stufe;
        this.zuletztAktualisiert = zuletztAktualisiert;
        this.kurs = kurs;
        this.antworten = antworten;
        this.themenbereich = themenbereich;
        this.quiz = quiz;
        this.serverid = serverid;
    }

    public int getAnzahlFalschtBeantwortet() {
        return anzahlFalschtBeantwortet;
    }

    public void setAnzahlFalschtBeantwortet(int anzahlFalschtBeantwortet) {
        this.anzahlFalschtBeantwortet = anzahlFalschtBeantwortet;
    }

    public int getAnzahlRichtigBeantwortet() {
        return anzahlRichtigBeantwortet;
    }

    public void setAnzahlRichtigBeantwortet(int anzahlRichtigBeantwortet) {
        this.anzahlRichtigBeantwortet = anzahlRichtigBeantwortet;
    }

    public String getDateLetzteRichtigeAntwort() {
        return dateLetzteRichtigeAntwort;
    }

    public void setDateLetzteRichtigeAntwort(String dateLetzteRichtigeAntwort) {
        this.dateLetzteRichtigeAntwort = dateLetzteRichtigeAntwort;
    }

    public String getErklaerung() {
        return erklaerung;
    }

    public void setErklaerung(String erklaerung) {
        this.erklaerung = erklaerung;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public int getGlobaleid() {
        return globaleid;
    }

    public void setGlobaleid(int globaleid) {
        this.globaleid = globaleid;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPunkzahl() {
        return punkzahl;
    }

    public void setPunkzahl(int punkzahl) {
        this.punkzahl = punkzahl;
    }

    public int getSchwirigkeit() {
        return schwirigkeit;
    }

    public void setSchwirigkeit(int schwirigkeit) {
        this.schwirigkeit = schwirigkeit;
    }

    public int getStufe() {
        return stufe;
    }

    public void setStufe(int stufe) {
        this.stufe = stufe;
    }

    public String getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(String zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbAntworten getAntworten() {
        return antworten;
    }

    public void setAntworten(dbAntworten antworten) {
        this.antworten = antworten;
    }

    public dbThemenbereich getThemenbereich() {
        return themenbereich;
    }

    public void setThemenbereich(dbThemenbereich themenbereich) {
        this.themenbereich = themenbereich;
    }

    public dbQuiz getQuiz() {
        return quiz;
    }

    public void setQuiz(dbQuiz quiz) {
        this.quiz = quiz;
    }
}
