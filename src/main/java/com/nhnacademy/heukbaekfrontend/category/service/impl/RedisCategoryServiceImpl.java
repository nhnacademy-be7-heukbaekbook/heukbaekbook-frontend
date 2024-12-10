package com.nhnacademy.heukbaekfrontend.category.service.impl;

import com.nhnacademy.heukbaekfrontend.category.client.CategoryClient;
import com.nhnacademy.heukbaekfrontend.category.dto.response.CategorySummaryResponse;
import com.nhnacademy.heukbaekfrontend.category.service.RedisCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisCategoryServiceImpl implements RedisCategoryService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final CategoryClient categoryClient;

    private static final String CATEGORY_CACHE_KEY = "categories";

    @Override
    public List<CategorySummaryResponse> getCategories() {
        // 1. Redis에서 카테고리 정보를 조회
        List<CategorySummaryResponse> cachedCategories = getCachedCategories();

        // 2. Redis에 데이터가 없으면 db에서 가져오기
        if (cachedCategories.isEmpty()) {
            cachedCategories = categoryClient.getCategories();

            // 3. Redis에 데이터를 캐싱
            cacheCategories(cachedCategories);
        }

        return cachedCategories;
    }

    private List<CategorySummaryResponse> getCachedCategories() {
        // Redis에서 캐싱된 데이터 조회
        Object cachedData = redisTemplate.opsForValue().get(CATEGORY_CACHE_KEY);

        if (cachedData instanceof List<?>) {
            // 캐싱된 데이터를 List<CategorySummaryResponse>로 변환
            return (List<CategorySummaryResponse>) cachedData;
        }

        return List.of();
    }

    private void cacheCategories(List<CategorySummaryResponse> categories) {
        // Redis에 데이터를 캐싱 (TTL 설정 가능)
        redisTemplate.opsForValue().set(CATEGORY_CACHE_KEY, categories, Duration.ofMinutes(30));
    }
}
