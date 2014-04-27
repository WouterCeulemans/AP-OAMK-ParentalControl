i = 0;
function onButtonClick () {
    if (i >= 256)
        hide_loop();
    else
        show_loop();

}

function show_loop() {
    i += 8;
    document.getElementById("divWrapper").style.left = i;
    document.getElementById("SideMenu").style.left = i - 256;
    if (i < 256)
        setTimeout(show_loop, 1);
};

function hide_loop() {
    i-=8;
    document.getElementById("divWrapper").style.left = i;
    document.getElementById("SideMenu").style.left = i - 256;
    if (i > 0)
        setTimeout(hide_loop, 1);
};