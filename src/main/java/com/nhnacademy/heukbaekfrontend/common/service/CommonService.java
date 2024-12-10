package com.nhnacademy.heukbaekfrontend.common.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class CommonService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");

    public String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price);
    }

    public BigDecimal calculateTotalPrice(BigDecimal salePrice, int quantity) {
        return salePrice.multiply(BigDecimal.valueOf(quantity));
    }


    public String calculateTotalPriceAndFormat(BigDecimal salePrice, int quantity) {
        BigDecimal totalPrice = calculateTotalPrice(salePrice, quantity);
        return formatPrice(totalPrice);
    }

    public BigDecimal calculateAllTotalPrice(List<Book> books) {
        BigDecimal allTotalPrice = BigDecimal.ZERO;
        for (Book book : books) {
            allTotalPrice = allTotalPrice.add(book.totalPrice());
        }

        return allTotalPrice;
    }

    public String calculateAllTotalPriceAndFormat(List<Book> books) {
        BigDecimal allTotalPrice = BigDecimal.ZERO;
        for (Book book : books) {
            allTotalPrice = allTotalPrice.add(book.totalPrice());
        }
        return formatPrice(allTotalPrice);
    }

    public String calculateAllTotalDiscountAndFormat(List<Book> books) {
        BigDecimal allTotalDiscount = BigDecimal.ZERO;
        for (Book book : books) {
            allTotalDiscount = allTotalDiscount.add(book.discountAmount().multiply(BigDecimal.valueOf(book.quantity())));
        }
        return formatPrice(allTotalDiscount);
    }
}
