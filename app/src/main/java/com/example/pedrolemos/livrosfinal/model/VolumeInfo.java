package com.example.pedrolemos.livrosfinal.model;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VolumeInfo {
    @SerializedName("title")
    private String title;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("publishedDate")
    private String publishedDate;
    @SerializedName("description")
    private String description;
    @SerializedName("pageCount")
    private int pageCount;
    @SerializedName("authors")
    private List<String> authors = new ArrayList<String>();
    @SerializedName("categories")
    private List<String> categories = new ArrayList<String>();
    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    public VolumeInfo(String title, String publisher, String publishedDate, String description, int pageCount, List<String> authors, List<String> categories, ImageLinks imageLinks) {
        this.title = title;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.authors = authors;
        this.categories = categories;
        this.imageLinks = imageLinks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {

        return description;


    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }
}
