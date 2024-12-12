package com.example.basaheroapp.Utilities;

public class Item {

    private String title;
    private String author;
    private String date;
    private String description;
    private String img;
    private int id;

    public Item(int id, String title, String author, String date, String description, String img) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;
        this.img = img;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
