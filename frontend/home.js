$(document).ready(function () {

    $("#signinBtn").click(function () {
        $('#signinModal').modal('show');
    });

    $("#signupBtn").click(function () {
        $('#signupModal').modal('show');
    });

    $("#signinForm").submit(function (event) {
        event.preventDefault();
        const email = $("#signinEmail").val();
        const password = $("#signinPassword").val();

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/signin`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email, password }),
            success: function (response) {
                localStorage.setItem('jwtToken', response.token);
                window.location.href = "profile.html";
            },
            error: function (xhr) {
                $('#signinModal .modal-body .alert').remove();
                const errors = xhr.responseJSON;
                let errorMessages = '';

                if (errors) {
                    errors.forEach(error => {
                        errorMessages += `<p>${error.message}</p>`;
                    });
                } else {
                    errorMessages += `<p>Неправильно введены данные</p>`;
                }

                $('#signinModal .modal-body').append(`<div class="alert alert-danger mt-3">${errorMessages}</div>`);
            }
        });
    });

    $("#signupForm").submit(function (event) {
        event.preventDefault();
        const name = $("#signupName").val();
        const email = $("#signupEmail").val();
        const password = $("#signupPassword").val();
        const age = $("#signupAge").val();

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/signup`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ name, email, password, age }),
            success: function (response) {
                localStorage.setItem('jwtToken', response.token);
                window.location.href = "profile.html";
            },
            error: function (xhr) {
                $('#signupModal .modal-body .alert').remove();
                const errors = xhr.responseJSON;
                let errorMessages = '';

                if (errors) {
                    errors.forEach(error => {
                        errorMessages += `<p>${error.message}</p>`;
                    });
                }

                $('#signupModal .modal-body').append(`<div class="alert alert-danger mt-3">${errorMessages}</div>`);
            }
        });
    });
});
