package com.nhnacademy.heukbaekfrontend.book.dto.response;

import com.nhnacademy.heukbaekfrontend.image.dto.ImageSummaryResponse;

import java.util.List;

public record BookCartResponse(Long id, String title, String price, String salePrice, double discountRate, String thumbnailUrl) {}
