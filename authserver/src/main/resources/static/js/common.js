function postURLEnc() {
    $.ajax({
        type: 'POST',
        url:'http://localhost:8086/url-enc',
        data: {
            'url': document.getElementById("url-to-enc").value,
        },
    }).done(function(res) {
        printResult(res, true);
    }).fail(function(err) {
        printResult(err.responseJSON, false);
    });
}