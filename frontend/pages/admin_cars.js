$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = "home.html";
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('userId');

    if (!userId) {
        alert("ID пользователя не указан!");
        window.location.href = "admin.html";
        return;
    }

    function fetchCars() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/admin/cars?userId=${userId}`,
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token,
                'content-type': 'application/json',
                'ngrok-skip-browser-warning': 'true'
            },
            success: function (response) {
                let tableBody = $("#userCarTableBody")
                tableBody.empty();

                response.forEach(car => {
                    let row = $(`
                    <tr data-id="${car.id}" class="clickable-row">
                        <td>${car.id}</td>
                        <td>${car.brand}</td>
                        <td>${car.model}</td>
                        <td>${car.yearFrom || '-'}</td>
                        <td>${car.yearBefore || '-'}</td>
                        <td>${car.mileageFrom || '-'}</td>
                        <td>${car.mileageBefore || '-'}</td>
                    </tr>
                    `);

                    row.click(function () {
                        window.location.href = `admin_car?id=${car.id}&userId=${userId}`;
                    });

                    tableBody.append(row);
                })
            },
            error: function () {
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html"
            }
        })
    }

    function fetchBrands() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/brands`,
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            success: function (brands) {
                let brandSelect = $("#brandSelect");
                brandSelect.append(brands.map(b => `<option value="${b.id}">${b.name}</option>`));
            },
            error: function () {
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            }
        });
    }

    $("#brandSelect").change(function () {
        let brandId = $(this).val();
        let modelSelect = $("#modelSelect");
        modelSelect.prop('disabled', true).empty().append(`<option>Загрузка...</option>`);

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/models?brandId=${brandId}`,
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token,
                'ngrok-skip-browser-warning': 'true'
            },
            success: function (models) {
                modelSelect.prop('disabled', false).empty()
                    .append(`<option value="">Выберите модель</option>`)
                    .append(models.map(m => `<option value="${m.name}">${m.name}</option>`));
            },
            error: function () {
                localStorage.removeItem('jwtToken');
                window.location.href = "home.html";
            }
        });
    });

    $("#logoutBtn").click(function () {
        localStorage.removeItem('jwtToken');
        window.location.href = "home.html"
    })

    fetchCars();
});
