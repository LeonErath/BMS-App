package leon.bms.model;

/**
 * Created by Leon E on 12.03.2016.
 */
public class quizantworten {
    public  int positionCard;
    public String antwort;
    public boolean selection;
    public  boolean ausgewählt;
    public  boolean richtigOderFalsch;

    public quizantworten() {
    }

    public quizantworten(int positionCard, String antwort, boolean selection, boolean ausgewählt, boolean richtigOderFalsch) {
        this.positionCard = positionCard;
        this.antwort = antwort;
        this.selection = selection;
        this.ausgewählt = ausgewählt;
        this.richtigOderFalsch = richtigOderFalsch;
    }

    public boolean isAusgewählt() {
        return ausgewählt;
    }

    public void setAusgewählt(boolean ausgewählt) {
        this.ausgewählt = ausgewählt;
    }

    public int getPositionCard() {
        return positionCard;
    }

    public void setPositionCard(int positionCard) {
        this.positionCard = positionCard;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isRichtigOderFalsch() {
        return richtigOderFalsch;
    }

    public void setRichtigOderFalsch(boolean richtigOderFalsch) {
        this.richtigOderFalsch = richtigOderFalsch;
    }
}
