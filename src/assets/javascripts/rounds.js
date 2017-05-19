function bindPreviousRoundSelection() {

    $("select").change(function () {
        console.log("bim");
        var previousRoundId = $("select option:selected").value()
        console.log(previousRoundId);
        var teams = getTeams(previousRoundId);
        console.log(teams);
    });
}

function getTeams(previousRoundId) {
    var data = {
        previousRoundId: previousRoundId
    };

    $.ajax({
        url: '/admin/rounds/' + previousRoundId + '/teams',
        type: 'post',
        dataType: 'json',
        data: data
    });
}

bindPreviousRoundSelection();