package com.nhnacademy.heukbaekfrontend.book.service;

import com.nhnacademy.heukbaekfrontend.book.dto.redis.BookInfo;

import java.util.List;

public interface RedisBookService {

    void createBooks(List<BookInfo> bookInfos);

    List<BookInfo> getBookInfos(List<Long> bookIds);
}
