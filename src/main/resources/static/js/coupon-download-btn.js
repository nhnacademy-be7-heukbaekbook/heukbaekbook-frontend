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
    // 서버로 다운로드 요청 보내기
    fetch(`/download/coupon/${couponId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.blob();
            }
            throw new Error('Network response was not ok.');
        })
        .then(blob => {
            // 다운로드 트리거
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'coupon.pdf'; // 서버에서 제공하는 파일 이름을 사용
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            alert('쿠폰 다운로드에 실패했습니다.');
            console.error('There was a problem with the download operation:', error);
        });
}
