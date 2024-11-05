package com.nhnacademy.heukbaekfrontend.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {
    String title;
    String index;
    String description;
    String publication;
    String isbn;
    String thumbnailImageUrl;
    List<String> detailImageUrls;
    boolean isPackable;
    int stock;
    int standardPrice;
    float discountRate;
    String bookStatus;
    String publisher;
    List<String> categories;
    String authors;
    List<String> tags;
}
