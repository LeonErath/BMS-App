package leon.bms;

/**
 * Created by Leon E on 12.03.2016.
 */
public class quizantworten {
    int positionCard;
    String antwort;
    boolean selection;
    boolean richtigOderFalsch;

    public quizantworten() {
    }

    public quizantworten(int positionCard, String antwort, boolean selection, boolean richtigOderFalsch) {
        this.positionCard = positionCard;
        this.antwort = antwort;
        this.selection = selection;
        this.richtigOderFalsch = richtigOderFalsch;
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
