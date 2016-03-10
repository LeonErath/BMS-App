package leon.bms;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Leon E on 13.02.2016.
 */

/**
 * @WebsiteArtikel ist eine Klasse die alle Daten für ein Artikel Objekt enthält und somit perfekt zum
 * anzeigen in einem RecyclerView ist.
 */
public class WebsiteArtikel {
    int id;
    String slug;
    String url;
    String title;
    String title_plain;
    String contentArticle;
    String excerpt;
    String date;
    String originalDate;
    String modified;
    String author;
    Bitmap image;
    int relevanz;
    List<String> tags;


    public WebsiteArtikel() {
        //empty Constructor needed!
        this.relevanz = 0;
    }

    public WebsiteArtikel(int id, String slug, String url, String title, String title_plain, String contentArticle, String excerpt, String date, String originalDate, String modified, String author, Bitmap image, int relevanz, List<String> tags) {
        this.id = id;
        this.slug = slug;
        this.url = url;
        this.title = title;
        this.title_plain = title_plain;
        this.contentArticle = contentArticle;
        this.excerpt = excerpt;
        this.date = date;
        this.originalDate = originalDate;
        this.modified = modified;
        this.author = author;
        this.image = image;
        this.relevanz = relevanz;
        this.tags = tags;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public int getRelevanz() {
        return relevanz;
    }

    public void setRelevanz(int relevanz) {
        this.relevanz = relevanz;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getContentArticle() {
        return contentArticle;
    }

    public void setContentArticle(String contentArticle) {
        this.contentArticle = contentArticle;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }


}
