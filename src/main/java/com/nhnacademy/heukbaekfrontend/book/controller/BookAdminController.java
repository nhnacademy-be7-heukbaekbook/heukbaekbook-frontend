package com.nhnacademy.heukbaekfrontend.book.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekfrontend.book.dto.response.*;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.category.service.CategoryService;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.heukbaekfrontend.tag.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;

@Controller
@RequestMapping("/admin")
public class BookAdminController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final CouponPolicyService couponPolicyService;

    public BookAdminController(BookService bookService, CategoryService categoryService, TagService tagService, CouponPolicyService couponPolicyService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.couponPolicyService = couponPolicyService;
    }

    @GetMapping("/aladin")
    public String searchBooks() {
        return "book/admin/searchBookFromAladin";
    }

    @PostMapping("/aladin")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<BookSearchResponse> books = bookService.searchBooks(title);
        model.addAttribute("responses", books);
        return "book/admin/searchBookFromAladin";
    }

    @PostMapping("/aladin/register")
    public String selectBookForRegistration(@ModelAttribute BookCreateRequest request, Model model) {
        model.addAttribute("bookCreateRequest", request);
        return "book/admin/registerBook";
    }

    @GetMapping("/books")
    public String viewAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String sort,
            Model model) {

        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        List<CouponPolicyResponse> couponPolicyResponses = couponPolicyService.getCouponPolicyList();
        Page<BookDetailResponse> books = bookService.getAllBooks(pageable);
        model.addAttribute("books", books);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("couponType", CouponType.BOOK);
        model.addAttribute("couponPolicyList", couponPolicyResponses);

        return "book/admin/viewAllBooks";
    }

    @GetMapping("/books/{book-id}")
    public String viewBook(@PathVariable(name = "book-id") Long bookId, Model model) {
        BookDetailResponse bookDetail = bookService.getBookById(bookId);
        model.addAttribute("book", bookDetail);
        return "book/admin/bookDetail";
    }

    @GetMapping("/books/register")
    public String showRegisterBookForm(Model model) {
        model.addAttribute("bookCreateRequest", new BookCreateRequest(
                "", "", "", "", "", "", false, 0, 0, 0.0f, "", "", ""
        ));
        return "book/admin/registerBook";
    }

    @PostMapping("/books/register")
    public String registerBook(@ModelAttribute BookCreateRequest request, Model model) {
        ResponseEntity<BookCreateResponse> response = bookService.registerBook(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "도서 등록에 실패했습니다.");
            model.addAttribute("bookCreateRequest", request);
        }
        return "book/admin/registerBook";
    }

    @GetMapping("/books/{book-id}/edit")
    public String updateBookForm(@PathVariable(name = "book-id") Long bookId, Model model) {
        BookDetailResponse bookDetail = bookService.getBookById(bookId);
        List<String> categories = categoryService.getCategoryPaths();
        List<String> tags = tagService.getTagList();
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(
                bookDetail.title(),
                bookDetail.index(),
                bookDetail.description(),
                bookDetail.publishedAt(),
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
        model.addAttribute("categoryPaths", categories);
        model.addAttribute("availableTags", tags);
        return "book/admin/updateBook";
    }

    @PutMapping("/books/{book-id}")
    public String updateBook(@PathVariable(name = "book-id") Long bookId,
                             @ModelAttribute BookUpdateRequest request,
                             Model model) {
        ResponseEntity<BookUpdateResponse> response = bookService.updateBook(bookId, request);
        List<String> categories = categoryService.getCategoryPaths();
        List<String> tags = tagService.getTagList();
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
            model.addAttribute("categoryPaths", categories);
            model.addAttribute("availableTags", tags);
        } else {
            model.addAttribute("error", "도서 수정에 실패했습니다.");
            model.addAttribute("bookUpdateRequest", request);
        }
        return "book/admin/updateBook";
    }

    @DeleteMapping("/books/{book-id}")
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

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/books");

        return "redirect:" + redirectUrl;
    }
}
