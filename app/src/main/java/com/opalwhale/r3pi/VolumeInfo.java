package com.opalwhale.r3pi;

import java.util.List;

/**
 * Created by Seba on 30-Jan-18.
 */

class VolumeInfo {

    ImageLinks imageLinks;
    String title;
    String subtitle;
    List<String> authors;
    String publisher;
    String publishedDate;
    String description;
    int pageCount;

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPageCount() {
        return pageCount;
    }



}

class ImageLinks {


    String extraLarge;
    String smallThumbnail;
    String thumbnail;
    String small;
    String medium;
    String large;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public String getExtraLarge() {
        return extraLarge;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getLarge() {
        return large;
    }



}