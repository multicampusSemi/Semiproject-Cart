document.addEventListener("DOMContentLoaded", function () {

  // 탭 전환 로직
  const findIdTab = document.getElementById("find-id-tab");
  const findPwTab = document.getElementById("find-pw-tab");
  const findIdForm = document.getElementById("find-id-form");
  const findPwForm = document.getElementById("find-pw-form");

  findIdTab.addEventListener("click", () => {
    findIdTab.classList.add("active");
    findPwTab.classList.remove("active");
    findIdForm.classList.remove("hidden");
    findPwForm.classList.add("hidden");
  });

  findPwTab.addEventListener("click", () => {
    findPwTab.classList.add("active");
    findIdTab.classList.remove("active");
    findPwForm.classList.remove("hidden");
    findIdForm.classList.add("hidden");
  });

  const urlParams = new URLSearchParams(window.location.search);
  const tab = urlParams.get('tab');
  if (tab === 'password') {
    findPwTab.click(); // 비밀번호 찾기 탭 활성화
  }

  // jQuery AJAX 로직
  $("#find-id-btn").on("click", function (e) {
    e.preventDefault(); // 폼 제출 방지

    const name = $("#name").val();
    const email = $("#email-search").val();

    $.ajax({
      url: "/user/find-id",
      method: "POST",
      data: { name: name, email: email },
      success: function (response) {
        $("#message").text("아이디: " + response.unmaskedId)
                      .removeClass("error")
                      .addClass("success");
      },
      error: function (xhr) {
        if (xhr.status === 404) {
          $("#message").text("아이디를 찾을 수 없습니다.")
                       .removeClass("success")
                       .addClass("error");
        } else {
          $("#message").text("오류가 발생했습니다. 다시 시도해주세요.")
                       .removeClass("success")
                       .addClass("error");
        }
      },
    });
  });

  // 이메일 인증 요청
  $("#send-code-btn").on("click", function () {
    const email = $("#email-pw").val();
    if (!email) {
      alert("이메일을 입력해주세요.");
      return;
    }

    $.ajax({
      url: "/email/send-code",
      method: "POST",
      data: { email: email },
      success: function (response) {
        alert(response); 
      },
      error: function () {
        alert("이메일 전송 중 오류가 발생했습니다. 다시 시도해주세요.");
      },
    });
  });

  // 이메일 인증 확인
  let isEmailVerified = false;

  $("#verify-code-btn").on("click", function () {
    const email = $("#email-pw").val();
    const code = $("#email-code-pw").val();

    if (!code) {
      alert("인증번호를 입력해주세요.");
      return;
    }

    $.ajax({
      url: "/email/verify-code",
      method: "POST",
      data: { email: email, code: code },
      success: function (response) {
        alert("인증이 완료되었습니다.");
        isEmailVerified = true; // 이메일 인증 성공
      },
      error: function () {
        alert("인증번호가 잘못되었거나 유효 시간이 초과되었습니다.");
        isEmailVerified = false; // 인증 실패
      },
    });
  });

  // 모달 열기 전에 이메일 인증 여부 확인
  $("#reset-password-btn").on("click", function () {
    if (!isEmailVerified) {
      alert("이메일 인증을 완료해야 합니다.");
      return;
    }

    const email = $("#email-pw").val();
    const username = $("#username-pw").val();
    const name = $("#name-pw").val();

    if (!email || !username || !name) {
      alert("이메일, 아이디, 이름을 모두 입력해주세요.");
      return;
    }

    // 사용자 정보 확인
    $.ajax({
      url: "/user/verify-info",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        email: email,
        username: username,
        name: name,
      }),
      success: function (response) {
        if (response.verified === true) {
          $("#password-reset-modal").removeClass("hidden");
        } else {
          alert("입력하신 정보와 일치하는 사용자가 없습니다.");
        }
      },
      error: function () {
        alert("사용자 정보 확인 중 오류가 발생했습니다.");
      },
    });
  });

  // 모달 닫기
  $("#close-modal-btn").on("click", function () {
    $("#password-reset-modal").addClass("hidden");
  });

  // 실시간 비밀번호 유효성 검사
  $("#new-password").on("input", function () {
    const password = $(this).val();
    const feedback = $("#password-feedback");
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

    if (!regex.test(password)) {
      feedback.show();
    } else {
      feedback.hide();
    }
  });

  $("#confirm-password").on("input", function () {
    const confirmPassword = $(this).val();
    const newPassword = $("#new-password").val();
    const feedback = $("#confirm-feedback");

    if (newPassword !== confirmPassword) {
      feedback.show();
    } else {
      feedback.hide();
    }
  });

  // 비밀번호 변경 폼 제출
  $("#reset-password-form").on("submit", function (e) {
    e.preventDefault();

    const newPassword = $("#new-password").val();
    const confirmPassword = $("#confirm-password").val();

    if (newPassword !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    $.ajax({
      url: "/user/reset-password",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        email: $("#email-pw").val(),
        password: $("#new-password").val(),
      }),
      success: function () {
        alert("비밀번호가 성공적으로 변경되었습니다.");
        $("#password-reset-modal").addClass("hidden");
        window.location.href = "/web/loginMain";
      },
      error: function (xhr) {
        alert("비밀번호 변경 중 오류가 발생했습니다.");
      },
    });

  });
});
