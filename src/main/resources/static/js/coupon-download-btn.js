// 다운로드 모달 열기
function openDownloadModal() {

    console.log("openDownloadModal 호출됨"); // 디버깅용 로그
    const modal = document.getElementById("downloadModal");
    modal.style.display = "block";
}

// 다운로드 모달 닫기
function closeDownloadModal() {
    const modal = document.getElementById("downloadModal");
    modal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    const downloadModal = document.getElementById("downloadModal");
    if (event.target === downloadModal) {
        closeDownloadModal();
    }
}

// 쿠폰 다운로드 기능
function downloadCoupon(button) {
    const couponId = button.getAttribute("data-id");

    fetch(`/members/coupons/${couponId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.text();
        })
        .then(message => {
            alert(message); // 정상 쿠폰 발급이 요청
        })
        .catch(error => {
            alert(error.message); // 예외 발생 시: "이미 발급 받은 쿠폰입니다", "발급 가능 기간이 아닙니다" 등
        });
}