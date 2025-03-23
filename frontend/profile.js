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
                'content-type': 'application/json',
                'ngrok-skip-browser-warning': 'true'
            },
            success: function (response) {
                $("#profileId").text(response.id);
                $("#chatId").text(response.chatId);
                $("#firstName").text(response.firstName);
                $("#lastName").text(response.lastName);
                $("#username").text(response.userName);
                $("#profileRoles").text(response.roles.join(', '));
                $("#userRoles").text(response.roles.join(', '));
                $("#userEmail").text(response.id);

                if (response.roles.includes("ROLE_ADMIN")) {
                    $("#adminPanelBtn").removeClass("d-none");
                }
            },
            error: function () {
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            }
        });
    }

    $("#editProfileBtn").click(function () {
        $("#editFirstName").val($("#firstName").text());
        $("#editLastName").val($("#lastName").text());
        $('#editProfileModal').modal('show');
    });

    $("#editProfileForm").submit(function (event) {
        event.preventDefault();

        const firstName = $("#editFirstName").val();
        const lastName = $("#editLastName").val();

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/user`,
            method: 'PUT',
            contentType: 'application/json',
            headers: { 'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            data: JSON.stringify({firstName, lastName }),
            success: function () {
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
            url: `${CONFIG.API_BASE_URL}/user`,
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            success: function () {
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
