package net.manbucy.lockwallpaper.model;

/**
 * Image
 * Created by yang on 2017/8/3.
 */

public class Image {
    private String title;
    private String content;
    private String imagePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (title != null ? !title.equals(image.title) : image.title != null) return false;
        if (content != null ? !content.equals(image.content) : image.content != null) return false;
        return imagePath != null ? imagePath.equals(image.imagePath) : image.imagePath == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        return result;
    }
}
