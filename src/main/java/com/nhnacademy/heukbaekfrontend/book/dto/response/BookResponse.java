package com.nhnacademy.heukbaekfrontend.book.dto.response;

import com.nhnacademy.heukbaekfrontend.contributor.dto.ContributorSummaryResponse;
import com.nhnacademy.heukbaekfrontend.image.dto.ImageSummaryResponse;
import com.nhnacademy.heukbaekfrontend.publisher.dto.PublisherSummaryResponse;

import java.util.List;

public record BookResponse(Long id,
                           String title,
                           String publishedAt,
                           String salePrice,
                           float discountRate,
                           String thumbnailUrl,
                           List<ContributorSummaryResponse> contributors,
                           PublisherSummaryResponse publisher) {
}
