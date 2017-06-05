$(".clickable-row").on('click', function (event) {
    var roundId = $(event.target).parents('tr').data('id');
    window.location = '/admin/rounds/' + roundId;
});