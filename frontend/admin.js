$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = "home.html";
        return;
    }

    function fetchUsers() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin`,
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            success: function (response) {
                let tableBody = $("#userTableBody");
                tableBody.empty();

                response.forEach(user => {
                    let row = $(`
                    <tr data-id="${user.id}">
                        <td>${user.id}</td>
                        <td>${user.chatId}</td>
                        <td class="user-first-name">${user.firstName}</td>
                        <td class="user-last-name">${user.lastName}</td>
                        <td class="user-username">${user.userName}</td>                      
                        <td class="user-roles">${user.roles.join(', ')}</td>
                        <td>
                            <button class="btn btn-primary btn-sm" onclick="viewUserCars(${user.id})">Машины</button>
                            <button class="btn btn-warning btn-sm" onclick="editUser(${user.id})">Редактировать</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Удалить</button>
                        </td>
                    </tr>
                `);

                    tableBody.append(row);
                });
            },
            error: function () {
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            }
        });
    }

    window.viewUserCars = function (userId) {
        window.location.href = `admin_cars?userId=${userId}`;
    }


    window.deleteUser = function (id) {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin/${id}`,
            method: 'DELETE',
            headers: {'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            success: function () {
                fetchUsers();
            },
            error: function () {
                alert("Ошибка при удалении пользователя.");
            }
        });
    }

    window.editUser = function (id) {
        const row = $(`#userTableBody tr[data-id="${id}"]`);
        const firstName = row.find(".user-first-name").text();
        const lastName = row.find(".user-last-name").text();
        const username = row.find(".user-username").text();
        const roles = row.find(".user-roles").text();

        $("#editUserId").val(id);
        $("#editUserFirstName").val(firstName);
        $("#editUserLastName").val(lastName);


        if (roles.includes("ROLE_USER")) {
            $("#roleUserEdit").prop("checked", true);
        } else {
            $("#roleUserEdit").prop("checked", false);
        }

        if (roles.includes("ROLE_ADMIN")) {
            $("#roleAdminEdit").prop("checked", true);
        } else {
            $("#roleAdminEdit").prop("checked", false);
        }

        $('#editUserModal').modal('show');
    }

    $("#editUserForm").submit(function (event) {
        event.preventDefault();

        const id = $("#editUserId").val()
        const firstName = $("#editFirstName").val()
        const lastName = $("#editLastName").val()

        const roles = [];
        if ($("#roleUserEdit").prop("checked")) {
            roles.push("ROLE_USER");
        }
        if ($("#roleAdminEdit").prop("checked")) {
            roles.push("ROLE_ADMIN");
        }

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin`,
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            contentType: 'application/json',
            data: JSON.stringify({id, firstName, lastName, roles}),
            success: function () {
                $('#editUserModal').modal('hide');
                fetchUsers();
            },
            error: function (xhr) {
                $('#editUserModal .modal-body .alert').remove();
                const errors = xhr.responseJSON;
                let errorMessages = '';
                if (errors) {
                    errors.forEach(error => {
                        errorMessages += `<p>${error.message}</p>`;
                    });
                }
                $('#editUserModal .modal-body').append(`<div class="alert alert-danger mt-3">${errorMessages}</div>`);
            }
        });
    });

    $("#createUserForm").submit(function (event) {
        event.preventDefault();

        const name = $("#createUserName").val();
        const email = $("#createUserEmail").val();
        const age = $("#createUserAge").val();
        const password = $("#createUserPassword").val();

        const roles = [];
        if ($("#roleUserCreate").prop("checked")) {
            roles.push("ROLE_USER");
        }
        if ($("#roleAdminCreate").prop("checked")) {
            roles.push("ROLE_ADMIN");
        }
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin`,
            method: 'POST',
            headers: {'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            contentType: 'application/json',
            data: JSON.stringify({name, email, password, age, roles}),
            success: function () {
                $('#createUserModal').modal('hide');
                fetchUsers();
            },
            error: function (xhr) {
                $('#createUserModal .modal-body .alert').remove();
                const errors = xhr.responseJSON;
                let errorMessages = '';
                if (errors) {
                    errors.forEach(error => {
                        errorMessages += `<p>${error.message}</p>`;
                    });
                }
                $('#createUserModal .modal-body').append(`<div class="alert alert-danger mt-3">${errorMessages}</div>`);
            }
        });
    });

    $("#logoutBtn").click(function () {
        localStorage.removeItem('jwtToken');
        window.location.href = "home.html"
    })

    fetchUsers();
});
