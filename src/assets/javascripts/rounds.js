function bindPreviousRoundSelection() {

    $("select").change(function () {
        var previousRoundId = $(event.target).find('option:selected').val();
        getTeams(previousRoundId);
    });
}

function getTeams(previousRoundId) {
    var data = {
        previousRoundId: previousRoundId
    };

    $.ajax({
        url: '/admin/rounds/' + previousRoundId + '/teams',
        type: 'get',
        dataType: 'json',
        data: data
    }).done(function () {
        alert("success");
    }).fail(function () {
        alert("error");
    }).always(function () {
        alert("complete");
    });
}

bindPreviousRoundSelection();