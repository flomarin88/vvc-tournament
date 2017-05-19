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

    $.ajax({
        url: '/admin/teams/' + teamId + '/checkin',
        type: 'post',
        dataType: 'json',
        data: data
    });
}

bindToggleButtons();