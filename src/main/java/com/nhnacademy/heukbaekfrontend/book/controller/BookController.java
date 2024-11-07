package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Admin
    @GetMapping("/admins/aladin")
    public String searchBooks() {
        return "admin/searchBookFromAladin";
    }

    @Admin
    @PostMapping("/admins/aladin")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<BookSearchResponse> books = bookService.searchBooks(title);
        model.addAttribute("responses", books);
        return "admin/searchBookFromAladin";
    }

    @Admin
    @PostMapping("/admins/aladin/register")
    public String selectBookForRegistration(@ModelAttribute BookCreateRequest request, Model model) {
        model.addAttribute("bookCreateRequest", request);
        return "admin/registerBook";
    }

    @GetMapping("/books/{book-id}")
    public String viewBook(@PathVariable(name = "book-id") Long bookId, Model model) {
        BookDetailResponse bookDetail = bookService.getBookById(bookId);
        model.addAttribute("book", bookDetail);
        return "admin/bookDetail";
    }

    @Admin
    @GetMapping("/admins/books")
    public String viewAllBooks(
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {
        Page<BookDetailResponse> books = bookService.getAllBooks(pageable);
        model.addAttribute("books", books);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sort", pageable.getSort().toString());
        return "admin/viewAllBooks";
    }

    @Admin
    @GetMapping("/admins/books/register")
    public String showRegisterBookForm(Model model) {
        model.addAttribute("bookCreateRequest", new BookCreateRequest(
                "", "", "", "", "", "", false, 0, 0, 0.0f, "", "", ""
        ));
        return "admin/registerBook";
    }

    @Admin
    @PostMapping("/admins/books/register")
    public String registerBook(@ModelAttribute BookCreateRequest request, Model model) {
        ResponseEntity<BookCreateResponse> response = bookService.registerBook(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "도서 등록에 실패했습니다.");
            model.addAttribute("bookCreateRequest", request);
        }
        return "admin/registerBook";
    }

    @Admin
    @GetMapping("/admins/books/{book-id}")
    public String updateBookForm(@PathVariable(name = "book-id") Long bookId, Model model) {
        BookDetailResponse bookDetail = bookService.getBookById(bookId);

        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(
                bookDetail.title(),
                bookDetail.index(),
                bookDetail.description(),
                bookDetail.publication(),
                bookDetail.isbn(),
                bookDetail.thumbnailImageUrl(),
                bookDetail.detailImageUrls(),
                bookDetail.isPackable(),
                bookDetail.stock(),
                bookDetail.standardPrice(),
                bookDetail.discountRate(),
                bookDetail.bookStatus(),
                bookDetail.publisher(),
                bookDetail.categories(),
                String.join(", ", bookDetail.authors()),
                bookDetail.tags()
        );

        model.addAttribute("bookUpdateRequest", bookUpdateRequest);
        model.addAttribute("bookId", bookId);
        return "admin/updateBook";
    }

    @Admin
    @PutMapping("/admins/books/{book-id}")
    public String updateBook(@PathVariable(name = "book-id") Long bookId,
                             @ModelAttribute BookUpdateRequest request,
                             Model model) {
        ResponseEntity<BookUpdateResponse> response = bookService.updateBook(bookId, request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "도서 수정에 실패했습니다.");
            model.addAttribute("bookUpdateRequest", request);
        }
        return "admin/updateBook";
    }

    @Admin
    @DeleteMapping("/admins/books/{book-id}")
    public String deleteBook(
            @PathVariable(name = "book-id") Long bookId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<BookDeleteResponse> response = bookService.deleteBook(bookId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "도서 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admins/books");

        return "redirect:" + redirectUrl;
    }

}
