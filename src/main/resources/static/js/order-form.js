// 배송 정보의 이름과 휴대폰 정보를 주문자 정보에 복사하는 함수
document.querySelectorAll('input[name="customerOption"]').forEach(option => {
    option.addEventListener('change', function () {
        if (this.value === '배송정보와 동일') {
            // 배송 정보 가져오기
            const recipientName = document.getElementById('recipientName').value;
            const recipientPhone1 = document.getElementById('recipientPhone1').value;
            const recipientPhone2 = document.getElementById('recipientPhone2').value;
            const recipientPhone3 = document.getElementById('recipientPhone3').value;

            // 주문 고객 정보에 복사
            document.getElementById('customerName').value = recipientName;
            document.getElementById('customerPhone1').value = recipientPhone1;
            document.getElementById('customerPhone2').value = recipientPhone2;
            document.getElementById('customerPhone3').value = recipientPhone3;

            // 필드 비활성화
            document.getElementById('customerName').readOnly = true;
            document.getElementById('customerPhone1').readOnly = true;
            document.getElementById('customerPhone2').readOnly = true;
            document.getElementById('customerPhone3').readOnly = true;
        } else {
            // 새로 입력 옵션 선택 시 필드 활성화 및 초기화
            document.getElementById('customerName').readOnly = false;
            document.getElementById('customerPhone1').readOnly = false;
            document.getElementById('customerPhone2').readOnly = false;
            document.getElementById('customerPhone3').readOnly = false;

            document.getElementById('customerName').value = '';
            document.getElementById('customerPhone1').value = '';
            document.getElementById('customerPhone2').value = '';
            document.getElementById('customerPhone3').value = '';
        }
    });
});