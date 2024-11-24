package me.theentropyshard.xkcdbot.xkcd;

import com.google.gson.annotations.SerializedName;

public class Comic {
    private String month;
    private int num;
    private String link;
    private String year;
    private String news;

    @SerializedName("safe_title")
    private String safeTitle;

    private String transcript;
    private String alt;
    private String img;
    private String title;
    private String day;

    public Comic() {

    }

    public String getMonth() {
        return this.month;
    }

    public int getNum() {
        return this.num;
    }

    public String getLink() {
        return this.link;
    }

    public String getYear() {
        return this.year;
    }

    public String getNews() {
        return this.news;
    }

    public String getSafeTitle() {
        return this.safeTitle;
    }

    public String getTranscript() {
        return this.transcript;
    }

    public String getAlt() {
        return this.alt;
    }

    public String getImg() {
        return this.img;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDay() {
        return this.day;
    }
}
