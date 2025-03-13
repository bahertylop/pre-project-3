$(document).ready(function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = "home.html";
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const carId = urlParams.get('id');

    if (!carId) {
        alert("ID автомобиля не указан!");
        window.location.href = "cars.html";
        return;
    }

    function fetchCarDetails() {
        $.ajax({
            url: `${CONFIG.API_BASE_URL}/cars/${carId}`,
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            success: function (car) {
                $("#carTitle").text(`${car.brand} ${car.model}`);
                $("#carYear").text(`${car.yearFrom || ""} - ${car.yearBefore || ""}`);
                $("#carMileage").text(`${car.mileageFrom || ""} - ${car.mileageBefore || ""}`);

                drawPriceChart(car.prices);
            },
            error: function (jqXHR) {
                if (jqXHR.status === 404) {
                    window.location.href = "cars.html";
                }
                else if (jqXHR.status === 403 || jqXHR.status === 401) {
                    localStorage.removeItem('jwtToken');
                    window.location.href = "home.html";
                }

            }
        });
    }

    function drawPriceChart(prices) {
        if (!prices || prices.length === 0) return;

        const ctx = document.getElementById('priceChart').getContext('2d');
        const labels = prices.map(p => p.date);
        const data = prices.map(p => p.price);

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Цена (₽)',
                    data: data,
                    borderColor: 'blue',
                    backgroundColor: 'rgba(0, 123, 255, 0.2)',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: { title: { display: true, text: 'Дата' } },
                    y: { title: { display: true, text: 'Цена (₽)' }, beginAtZero: false }
                }
            }
        });
    }

    fetchCarDetails();

    // Выход из аккаунта
    $("#logoutBtn").click(function () {
        localStorage.removeItem('jwtToken');
        window.location.href = "home.html";
    });
});
