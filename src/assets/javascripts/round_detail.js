function bindSwitchCheckboxButton() {
    $('button.switch-team-js').on('click', function (event) {
        var teams = $('.switch-team-js:checked');
        if (teams.length !== 2) {
            alert("2 équipes uniquement");
        }
        var roundId = $('.row.switch-class-js').data('round-id');
        var pool1 = $(teams[0]).parents('table').data('pool-id');
        var team1 = $(teams[0]).data('team-id');
        var pool2 = $(teams[1]).parents('table').data('pool-id');
        var team2 = $(teams[1]).data('team-id');
        switchTeams(roundId, pool1, team1, pool2, team2);
    });

}

function switchTeams(roundId, pool1, team1, pool2, team2) {
    var data = {
        pool1Id: pool1,
        team1Id: team1,
        pool2Id: pool2,
        team2Id: team2
    };

    $.ajax({
        url: '/admin/rounds/' + roundId + '/switch',
        type: 'PUT',
        dataType: 'json',
        data: data
    }).done(function (data) {
        if (data.redirect) {
            window.location.href = data.redirect;
        }
        else {
            alert("Erreur")
        }
    });
}

bindSwitchCheckboxButton();