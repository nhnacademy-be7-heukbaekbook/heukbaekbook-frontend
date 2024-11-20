function redirectToOrderPage(bookId) {
    // 수량 가져오기
    const quantityInput = document.getElementById(`quantity-${bookId}`);
    const quantity = quantityInput ? parseInt(quantityInput.value, 10) : 1;

    // 페이지 이동
    window.location.href = `/order?bookIds=${bookId}&quantity=${quantity}`;
}