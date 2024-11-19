package com.nhnacademy.heukbaekfrontend.book.dto.response;

import com.nhnacademy.heukbaekfrontend.contributor.dto.ContributorSummaryResponse;
import com.nhnacademy.heukbaekfrontend.publisher.dto.PublisherSummaryResponse;

import java.util.List;

public record BookViewResponse(Long id,
                               String title,
                               String index,
                               String description,
                               String publishedAt,
                               String isbn,
                               boolean isPackable,
                               String price,
                               String salePrice,
                               float discountRate,
                               int popularity,
                               String status,
                               String thumbnailUrl,
                               List<String> detailImageUrls,
                               List<ContributorSummaryResponse> contributors,
                               PublisherSummaryResponse publisher) {
}
