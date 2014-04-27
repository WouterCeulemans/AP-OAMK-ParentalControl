function ChangeName() {
    var e = document.getElementById('devid');
    var devid = e.options[e.selectedIndex].value;
    var newName = document.getElementById('NewName').value;

    $.ajax({
        type: "GET",
        url: "getCoordinates.php?devid=" + devid + "&newName=" + newName,
        success: NameCallback
    });
}

function NameCallback(data, status) {
    alert("ok");
}