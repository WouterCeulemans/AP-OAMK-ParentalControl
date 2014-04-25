﻿var map;
var markers = [];
var points = [];
var polyline = new google.maps.Polyline();

$(document).ready(function () {
    var latlng = new google.maps.LatLng(0, 0);
    var myOptions = {
        zoom: 10,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
});

function GetCoordinates() {
    $.ajax({
        type: "GET",
        url: "GetCoordinates.php?devid=" + document.getElementById('devid').value,
        success: callback
    });
};

function callback(data, status) {
    alert("callback");
    alert(data);
    var jsArr = JSON.parse(data);
    alert(data);
    alert(jsArr[0].x);
    clearOverlays();
    alert(jsArr[0].x);
    map.setCenter(new google.maps.LatLng(parseFloat(jsArr[0].x), parseFloat(jsArr[0].y)), 5);
    for (var i = 0; i < jsArr.length; i++) {
        markers[i] = new google.maps.Marker({
            position: new google.maps.LatLng(parseFloat(jsArr[i].x), parseFloat(jsArr[i].y)),
            map: map
        });
        points[i] = new google.maps.LatLng(parseFloat(jsArr[i].x), parseFloat(jsArr[i].y));
    }
    polyline = new google.maps.Polyline({
        path: points,
        geodesic: false,
        strokeColor: '#ff0000',
        strokeOpacity: 1,
        strokeWeight: 3
    });
    polyline.setMap(map);
}

function clearOverlays() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
    polyline.setMap(null);
}