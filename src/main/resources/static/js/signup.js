

let isAvailableLoginID = false;
let isAvailableEmail = false;

function checkDuplicate(type) {
    const value = document.getElementById(type === 'loginId' ? 'loginId' : 'email').value;
    if (!value) {
        alert(type === 'loginId' ? '아이디를 입력해주세요' : '이메일을 입력해주세요');
        return;
    }

    fetch(`/signup/check-duplicate/${type}`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: value
    })
        .then(response => response.json())
        .then(isDuplicate => {
            const messageElement = document.getElementById(type === 'loginId' ? 'checkMessage-loginId' : "checkMessage-email");
            if (isDuplicate) {
                messageElement.innerText = '이미 사용중입니다.';
                if (type === 'loginId') isAvailableLoginID = false;
                if (type === 'email') isAvailableEmail = false;
            } else {
                messageElement.innerText = '사용 가능합니다.';
                messageElement.style.color = 'green';
                if (type === 'loginId') isAvailableLoginID = true;
                if (type === 'email') isAvailableEmail = true;
            }
        })
        .catch(error => {
            console.error(error);
            alert(error);
            // alert('중복 확인 중 오류가 발생했습니다.');
        })
}

function findAddressKakaoAPI() {
    new daum.Postcode({
        oncomplete: function (data) {
            var address = !data.userSelectedType ? "" : data.roadAddress;
            document.getElementById('postalCode').value = data.zonecode;
            document.getElementById("roadNameAddress").value = address;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

function validateForm() {
    if (!isAvailableLoginID) {
        alert("아이디 중복 확인을 완료해주세요.");
        return false;
    }
    if (!isAvailableEmail) {
        alert("이메일 중복 확인을 완료해주세요.");
        return false;
    }
    return true;
}