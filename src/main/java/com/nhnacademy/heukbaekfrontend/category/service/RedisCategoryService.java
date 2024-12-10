package com.nhnacademy.heukbaekfrontend.category.service;

import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public interface RedisCategoryService {

    List<CategorySummaryResponse> getCategories();
}