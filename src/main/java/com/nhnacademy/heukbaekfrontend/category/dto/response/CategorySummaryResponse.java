package com.nhnacademy.heukbaekfrontend.category.dto.response;

import java.util.List;

public record CategorySummaryResponse(Long id, String name, List<CategorySummaryResponse> subCategorySummaryResponses) {
}
