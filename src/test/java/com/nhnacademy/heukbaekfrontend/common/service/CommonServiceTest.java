//package com.nhnacademy.heukbaekfrontend.common.service;
//
//import com.nhnacademy.heukbaekfrontend.book.domain.Book;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class CommonServiceTest {
//
//    private CommonService commonService;
//
//    @BeforeEach
//    void setUp() {
//        commonService = new CommonService();
//    }
//
//    @Test
//    void testCalculateTotalPrice() {
//        BigDecimal salePrice = new BigDecimal("15000");
//        int quantity = 3;
//
//        BigDecimal totalPrice = commonService.calculateTotalPrice(salePrice, quantity);
//
//        assertThat(totalPrice).isEqualTo(new BigDecimal("45000"));
//    }
//
//    @Test
//    void testCalculateTotalPriceAndFormat() {
//        BigDecimal salePrice = new BigDecimal("15000");
//        int quantity = 2;
//
//        String totalPriceFormatted = commonService.calculateTotalPriceAndFormat(salePrice, quantity);
//
//        assertThat(totalPriceFormatted).isEqualTo("30,000");
//    }
//
//    @Test
//    void testCalculateAllTotalPrice() {
//        Book book1 = new Book(1L, "Book1", true, "20000", "15000", BigDecimal.valueOf(0.25), BigDecimal.valueOf(5000), "url1", 2, null, BigDecimal.valueOf(30000));
//        Book book2 = new Book(2L, "Book2", false, "30000", "25000", BigDecimal.valueOf(0.20), BigDecimal.valueOf(5000), "url2", 1, null, BigDecimal.valueOf(25000));
//
//        List<Book> books = Arrays.asList(book1, book2);
//
//        BigDecimal allTotalPrice = commonService.calculateAllTotalPrice(books);
//
//        assertThat(allTotalPrice).isEqualTo(new BigDecimal("55000"));
//    }
//
//    @Test
//    void testCalculateAllTotalPriceAndFormat() {
//        Book book1 = new Book(1L, "Book1", true, "20000", "15000", BigDecimal.valueOf(0.25), BigDecimal.valueOf(5000), "url1", 2, null, BigDecimal.valueOf(30000));
//        Book book2 = new Book(2L, "Book2", false, "30000", "25000", BigDecimal.valueOf(0.20), BigDecimal.valueOf(5000), "url2", 1, null, BigDecimal.valueOf(25000));
//
//        List<Book> books = Arrays.asList(book1, book2);
//
//        String allTotalPriceFormatted = commonService.calculateAllTotalPriceAndFormat(books);
//
//        assertThat(allTotalPriceFormatted).isEqualTo("55,000");
//    }
//
//    @Test
//    void testCalculateAllTotalDiscountAndFormat() {
//        Book book1 = new Book(1L, "Book1", true, "20000", "15000", BigDecimal.valueOf(0.25), BigDecimal.valueOf(5000), "url1", 2, null, BigDecimal.valueOf(30000));
//        Book book2 = new Book(2L, "Book2", false, "30000", "25000", BigDecimal.valueOf(0.20), BigDecimal.valueOf(5000), "url2", 1, null, BigDecimal.valueOf(25000));
//
//        List<Book> books = Arrays.asList(book1, book2);
//
//        String allTotalDiscountFormatted = commonService.calculateAllTotalDiscountAndFormat(books);
//
//        BigDecimal expectedDiscount = book1.discountAmount().multiply(BigDecimal.valueOf(book1.quantity()))
//                .add(book2.discountAmount().multiply(BigDecimal.valueOf(book2.quantity())));
//
//        assertThat(allTotalDiscountFormatted).isEqualTo(commonService.formatPrice(expectedDiscount));
//    }
//}
