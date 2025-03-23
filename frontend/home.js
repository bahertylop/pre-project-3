function onTelegramAuth(user) {
    console.log(user);

    $.ajax({
        url: `${CONFIG.API_BASE_URL}/auth/tg_auth`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            telegramId: user.id,
            firstName: user.first_name,
            lastName: user.last_name,
            username: user.username
        }),
        success: function (response) {
            localStorage.setItem('jwtToken', response.access);
            window.location.href = "profile.html"
        },
        error: function(xhr) {
            console.error('Ошибка авторизации через Telegram:', xhr.responseJSON);
        }
    });


    // alert('Logged in as ' + user.first_name + ' ' + user.last_name + ' (' + user.id + (user.username ? ', @' + user.username : '') + ')');
}

$(document).ready(function () {

});
