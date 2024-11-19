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