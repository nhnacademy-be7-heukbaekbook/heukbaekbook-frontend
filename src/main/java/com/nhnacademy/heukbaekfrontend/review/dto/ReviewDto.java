package com.nhnacademy.heukbaekfrontend.review.dto;

public class ReviewDto {
    private String title;
    private String content;
    private int score;
    private String bookTitle;

    public ReviewDto(String title, String content, int score, String bookTitle) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.bookTitle = bookTitle;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

}
