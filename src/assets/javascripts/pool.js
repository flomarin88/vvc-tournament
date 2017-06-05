function bindMatchesButton() {

    $(".match-button-js").on('click', function (event) {
        var row = $(event.target).parents('tr');
        var matchId = row.data('id');
        var teamScore1 = row.find('input[name=teamScore1]').val();
        var teamScore2 = row.find('input[name=teamScore2]').val();
        updateScore(matchId, teamScore1, teamScore2);
    });
}

function updateScore(matchId, teamScore1, teamScore2) {
    var data = {
        teamScore1: teamScore1,
        teamScore2: teamScore2
    };

    $.ajax({
        url: '/admin/matches/' + matchId,
        type: 'PUT',
        dataType: 'JSON',
        data: data
    }).done(function () {
        console.log("Match sauvegardé");
    }).fail(function (error) {
        console.log(error);//to show what error happended
        alert("error");
    });
}

bindMatchesButton();