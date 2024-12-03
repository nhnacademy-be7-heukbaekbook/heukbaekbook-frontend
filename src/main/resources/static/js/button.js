function decreaseQuantity(id) {
    const quantityInput = document.getElementById(`quantity-${id}`);
    const hiddenQuantityInput = document.getElementById(`hidden-quantity-${id}`);
    let currentQuantity = parseInt(quantityInput.value);
    if (currentQuantity > 1) {
        quantityInput.value = currentQuantity - 1;
        hiddenQuantityInput.value = currentQuantity - 1;
    }
}

function increaseQuantity(id) {
    const quantityInput = document.getElementById(`quantity-${id}`);
    const hiddenQuantityInput = document.getElementById(`hidden-quantity-${id}`);
    let currentQuantity = parseInt(quantityInput.value);
    quantityInput.value = currentQuantity + 1;
    hiddenQuantityInput.value = currentQuantity + 1;
}

function addToCart(bookId) {
    const quantity = document.getElementById(`quantity-${bookId}`).value;

    fetch('/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            bookId: bookId,
            quantity: parseInt(quantity)
        })
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Error adding to cart");
            }
        })
        .then(data => {
            alert("장바구니에 추가되었습니다.");
            // location.reload(); // 페이지 새로고침
        })
        .catch(error => {
            console.error("Error:", error);
            alert("장바구니에 추가하는 데 실패했습니다.");
        });
}

function maintainCategorySearch() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('categoryId');

    if (categoryId) {
        document.querySelector('input[name="categoryId"]').value = categoryId;
    }
}

// 페이지 로드 시 실행하여 검색 창에 categoryId를 유지
window.onload = maintainCategorySearch;


function handleEmptyKeyword() {
    const keywordInput = document.getElementById("keyword").value.trim();
    const categoryId = document.getElementById("categoryId").value;

    if (!keywordInput && categoryId) {
        const sortCondition = new URLSearchParams(window.location.search).get("sortCondition") || "POPULARITY";
        window.location.href = `/search?searchCondition=ALL&keyword=&categoryId=${categoryId}&sortCondition=${sortCondition}`;
        return false;
    }
    return true;
}

function sortBy(criteria) {
    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get("keyword") || "";
    const searchCondition = urlParams.get("searchCondition") || "ALL";
    const categoryId = urlParams.get("categoryId") || "";

    const searchUrl = `/search?searchCondition=${searchCondition}&keyword=${keyword}&categoryId=${categoryId}&sortCondition=${criteria}`;
    window.location.href = searchUrl;
}
