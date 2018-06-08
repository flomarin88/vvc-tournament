$('.round-card').on('click', function (event) {
    var roundId = $(event.target).closest('.round-card').data('id');
    window.location = '/admin/rounds/' + roundId;
});