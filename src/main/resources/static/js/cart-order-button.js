function redirectToOrderPage() {
    // 체크된 책 ID 수집
    const selectedCheckboxes = document.querySelectorAll('.book-checkbox:checked');
    const bookIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.value);

    if (bookIds.length === 0) {
        alert("주문할 상품을 선택하여 주세요. ");
        return;
    }

    // `bookIds`를 콤마로 구분된 문자열로 변환
    const bookIdString = bookIds.join(',');

    // 서버로 이동
    window.location.href = `/order?bookIds=${bookIdString}`;
}