function bindValidationButton() {

    $("button").on('click', function (event) {
        var button = $(event.target);
        var roundId = button.data('id');
        generateMatches(roundId, button);
    });
}

function generateMatches(roundId, button) {
    $.ajax({
        url: '/admin/rounds/' + roundId + '/matches',
        type: 'POST'
    }).done(function () {
        console.log("Round " + roundId + ": Matches generated");
        button.hide(true);
    }).fail(function () {
        alert("error");
    });
}

bindValidationButton();