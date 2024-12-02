package com.nhnacademy.heukbaekfrontend.common.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommonServiceTest {

    private CommonService commonService;

    @BeforeEach
    void setUp() {
        commonService = new CommonService();
    }

    @Test
    void testFormatPrice() {
        BigDecimal price = BigDecimal.valueOf(1234567.89);
        String formattedPrice = commonService.formatPrice(price);

        assertThat(formattedPrice).isEqualTo("1,234,567.89");
    }

    @Test
    void testCalculateTotalPrice() {
        BigDecimal salePrice = BigDecimal.valueOf(10000);
        int quantity = 3;
        BigDecimal totalPrice = commonService.calculateTotalPrice(salePrice, quantity);

        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(30000));
    }

    @Test
    void testCalculateTotalPriceAndFormat() {
        BigDecimal salePrice = BigDecimal.valueOf(10000);
        int quantity = 3;
        String formattedTotalPrice = commonService.calculateTotalPriceAndFormat(salePrice, quantity);

        assertThat(formattedTotalPrice).isEqualTo("30,000");
    }

    @Test
    void testCalculateAllTotalPrice() {
        List<Book> books = List.of(
                new Book(1L, "Book A", true, null, "10000", "9000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000),
                        "http://example.com/bookA.jpg", 2, "18,000", BigDecimal.valueOf(18000)),
                new Book(2L, "Book B", false, null, "20000", "18000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000),
                        "http://example.com/bookB.jpg", 1, "18,000", BigDecimal.valueOf(18000))
        );

        BigDecimal allTotalPrice = commonService.calculateAllTotalPrice(books);

        assertThat(allTotalPrice).isEqualTo(BigDecimal.valueOf(36000));
    }

    @Test
    void testCalculateAllTotalPriceAndFormat() {
        List<Book> books = List.of(
                new Book(1L, "Book A", true, null, "10000", "9000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000),
                        "http://example.com/bookA.jpg", 2, "18,000", BigDecimal.valueOf(18000)),
                new Book(2L, "Book B", false, null, "20000", "18000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000),
                        "http://example.com/bookB.jpg", 1, "18,000", BigDecimal.valueOf(18000))
        );

        String allTotalPriceFormatted = commonService.calculateAllTotalPriceAndFormat(books);

        assertThat(allTotalPriceFormatted).isEqualTo("36,000");
    }

    @Test
    void testCalculateAllTotalDiscountAndFormat() {
        List<Book> books = List.of(
                new Book(1L, "Book A", true, null, "10000", "9000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000),
                        "http://example.com/bookA.jpg", 2, "18,000", BigDecimal.valueOf(18000)),
                new Book(2L, "Book B", false, null, "20000", "18000",
                        BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000),
                        "http://example.com/bookB.jpg", 1, "18,000", BigDecimal.valueOf(18000))
        );

        String allTotalDiscountFormatted = commonService.calculateAllTotalDiscountAndFormat(books);

        assertThat(allTotalDiscountFormatted).isEqualTo("4,000");
    }
}
