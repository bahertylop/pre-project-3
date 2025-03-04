$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');

    function fetchUsers() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin`,
            method: 'GET',
            headers: {'Authorization': 'Bearer ' + token},
            success: function (response) {
                let tableBody = $("#userTableBody");
                tableBody.empty();

                response.forEach(user => {
                    tableBody.append(`
                    <tr data-id="${user.id}">
                        <td>${user.id}</td>
                        <td class="user-name">${user.name}</td>
                        <td class="user-email">${user.email}</td>
                        <td class="user-age">${user.age}</td>
                        <td class="user-roles">${user.roles.join(', ')}</td>
                        <td>
                            <button class="btn btn-warning btn-sm" onclick="editUser(${user.id})">Редактировать</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Удалить</button>
                        </td>
                    </tr>
                `);
                });
            },
            error: function () {
                alert("Ошибка загрузки пользователей.");
            }
        });
    }

    window.deleteUser = function (id) {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin/delete`,
            method: 'POST',
            data: {id},
            headers: {'Authorization': 'Bearer ' + token},
            success: function () {
                alert("Пользователь удалён!");
                fetchUsers();
            },
            error: function () {
                alert("Ошибка при удалении пользователя.");
            }
        });
    }

    window.editUser = function (id) {
        const row = $(`#userTableBody tr[data-id="${id}"]`);
        const name = row.find(".user-name").text();
        const email = row.find(".user-email").text();
        const age = row.find(".user-age").text();
        const roles = row.find(".user-roles").text();

        $("#editUserId").val(id);
        $("#editUserName").val(name);
        $("#editUserEmail").val(email);
        $("#editUserAge").val(age);

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

        const id = $("#editUserId").val();
        const name = $("#editUserName").val();
        let password = $("#editUserPassword").val();
        const age = $("#editUserAge").val();

        if (password === "") {
            password = null;
        }
        const roles = [];
        if ($("#roleUserEdit").prop("checked")) {
            roles.push("ROLE_USER");
        }
        if ($("#roleAdminEdit").prop("checked")) {
            roles.push("ROLE_ADMIN");
        }

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin/update`,
            method: 'POST',
            headers: {'Authorization': 'Bearer ' + token},
            contentType: 'application/json',
            data: JSON.stringify({id, name, password, age, roles}),
            success: function () {
                alert("Пользователь обновлён!");
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
            url: `${CONFIG.API_BASE_URL}/admin/create`,
            method: 'POST',
            headers: {'Authorization': 'Bearer ' + token},
            contentType: 'application/json',
            data: JSON.stringify({name, email, password, age, roles}),
            success: function () {
                alert("Пользователь создан!");
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
