function bindToggleButtons() {

    $('input').change(function (event) {
        var team = $(event.target);
        var teamId = team.data('id');
        var isPresent = team.prop('checked');
        checkin(teamId, isPresent);
    });
}

function checkin(teamId, isPresent) {
    var data = {
        isPresent: isPresent
    };
    var diff = 1;
    if (!isPresent) {
        diff = -1;
    }

    $.ajax({
        url: '/admin/teams/' + teamId + '/checkin',
        type: 'POST',
        dataType: 'json',
        data: data
    }).done(function () {
        console.log("Checkin done");
        var absenceCountLabel = $('.label');
        var current = absenceCountLabel.value();
        absenceCountLabel.value(current + diff);
    });
}

bindToggleButtons();