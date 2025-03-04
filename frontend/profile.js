$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = "home.html";
        return;
    }

    function fetchProfile() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/user`,
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token,
                        'content-type': 'application/json'},
            success: function (response) {
                $("#profileId").text(response.id);
                $("#profileName").text(response.name);
                $("#profileEmail").text(response.email);
                $("#profileAge").text(response.age);
                $("#profileRoles").text(response.roles.join(', '));
                $("#userEmail").text(response.email);
                $("#userRoles").text(response.roles.join(', '));

                if (response.roles.includes("ROLE_ADMIN")) {
                    $("#adminPanelBtn").removeClass("d-none");
                }
            },
            error: function () {
                alert("Ошибка загрузки профиля.");
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            }
        });
    }

    $("#editProfileBtn").click(function () {
        $("#editName").val($("#profileName").text());
        $("#editAge").val($("#profileAge").text());
        $('#editProfileModal').modal('show');
    });

    $("#editProfileForm").submit(function (event) {
        event.preventDefault();

        const name = $("#editName").val();
        const password = $("#editPassword").val();
        const age = $("#editAge").val();

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/user/update`,
            method: 'POST',
            contentType: 'application/json',
            headers: { 'Authorization': 'Bearer ' + token },
            data: JSON.stringify({ name, password, age }),
            success: function () {
                alert("Профиль обновлён!");
                fetchProfile();
                $('#editProfileModal').modal('hide');
            },
            error: function (xhr) {
                $('#editProfileModal .modal-body .alert').remove();
                const errors = xhr.responseJSON;
                let errorMessages = '';
                if (errors) {
                    errors.forEach(error => {
                        errorMessages += `<p>${error.message}</p>`;
                    });
                }
                $('#editProfileModal .modal-body').append(`<div class="alert alert-danger mt-3">${errorMessages}</div>`);
            }
        });
    });

    $("#deleteAccountBtn").click(function () {
        if (!confirm("Вы уверены, что хотите удалить аккаунт?")) return;

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/user/delete`,
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token },
            success: function () {
                alert("Аккаунт удалён!");
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            },
            error: function () {
                alert("Ошибка при удалении аккаунта!");
            }
        });
    });

    $("#adminPanelBtn").click(function () {
        window.location.href = "admin.html";
    });

    $("#logoutBtn").click(function () {
        localStorage.removeItem('jwtToken');
        window.location.href = "home.html"
    })

    fetchProfile();
});
