$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = "home.html";
        return;
    }

    $("#loading").hide();

    function fetchCars() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/cars`,
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token,
                        'content-type': 'application/json',
                'ngrok-skip-browser-warning': 'true'},
            success: function (response) {
                let tableBody = $("#carTableBody")
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
                        window.location.href = `car?id=${car.id}`;
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

    $("#addCarBtn").click(function () {
        fetchBrands();
        $("#addCarModal").modal('show');
    });

    $("#addCarForm").submit(function (e) {
        e.preventDefault();
        $("#loading").show();

        let carData = {
            brand: $("#brandSelect option:selected").text(),
            model: $("#modelSelect").val(),
            yearFrom: $("#yearFrom").val() || null,
            yearBefore: $("#yearBefore").val() || null,
            mileageFrom: $("#mileageFrom").val() || null,
            mileageBefore: $("#mileageBefore").val() || null
        };

        $.ajax({
            url: `${CONFIG.API_BASE_URL}/cars`,
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
                'ngrok-skip-browser-warning': 'true'
            },
            data: JSON.stringify(carData),
            complete: function () {
                $("#loading").hide();
                $("#addCarModal").modal('hide');
                fetchCars();
            }
        });
    });

    $("#logoutBtn").click(function () {
        localStorage.removeItem('jwtToken');
        window.location.href = "home.html"
    })

    fetchCars();
});
