package com.nhnacademy.heukbaekfrontend.book.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Pageable;

public record BookSearchRequest(
        String keyword,
        String searchCondition,
        String sortCondition
) {
    @JsonCreator
    public BookSearchRequest(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("searchCondition") String searchCondition,
            @JsonProperty("sortCondition") String sortCondition) {
        this.keyword = keyword != null ? keyword : "";
        this.searchCondition = searchCondition != null ? searchCondition : "TITLE";
        this.sortCondition = sortCondition != null ? sortCondition : "POPULARITY";
    }
}
