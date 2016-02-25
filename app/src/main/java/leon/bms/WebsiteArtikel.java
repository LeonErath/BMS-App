package leon.bms;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Leon E on 13.02.2016.
 */
public class WebsiteArtikel{
    int id;
    String slug;
    String url;
    String title;
    String title_plain;
    String contentArticle;
    String excerpt;
    String date;
    String modified;
    String author;
    Bitmap image;
    String tag;


    public WebsiteArtikel() {
    }

    public WebsiteArtikel(int id, String slug, String url, String title, String title_plain, String contentArticle, String excerpt, String date, String modified, String author, Bitmap image, String tag) {
        this.id = id;
        this.slug = slug;
        this.url = url;
        this.title = title;
        this.title_plain = title_plain;
        this.contentArticle = contentArticle;
        this.excerpt = excerpt;
        this.date = date;
        this.modified = modified;
        this.author = author;
        this.image = image;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
