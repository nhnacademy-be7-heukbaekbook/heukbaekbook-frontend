package com.nhnacademy.heukbaekfrontend.book.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
    String publishedAt;
    String isbn;
    String thumbnailImageUrl;
    List<String> detailImageUrls;
    boolean isPackable;
    @Min(0) int stock;
    @Min(0) int standardPrice;
    @DecimalMin(value = "0.0")float discountRate;
    String bookStatus;
    String publisher;
    List<String> categories;
    String authors;
    List<String> tags;
}
