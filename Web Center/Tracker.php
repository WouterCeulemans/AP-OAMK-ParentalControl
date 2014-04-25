<?php
session_start();
if (!isset($_SESSION["User"]))
    header("location:index.php?LoginError=99");
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Parental Web Center</title>
<link href="tracker.css" rel="stylesheet" type="text/css">
<link href="layout.css" rel="stylesheet" type="text/css">
</head>

<body>

<div id="divWrapper">
    <div id="divHeader">
    <img src="Images/Header.png" width="500" height="200"/>
    </div>
    
    <div id="divLine"></div>
    <div id="divLinks">
        <div id="divNav">
            <ul>
                <li><a href="Index.php" 					>Home</a></li>
                <li><a href="Settings.php"					>Settings</a></li>
                <li><a href="Spybot.php"					>Spybot</a></li>
                <li><a href="Tracker.php"class="current"	>Tracker</a></li>
            </ul>
        </div>
        <div id="divHome">
            <div id="divContent"> 
                <p>Google maps </p>
<!--                hier komt dan de link naar de google maps overonderstel ik ?
-->                <img scr="#" height="500" width="500"/>
                
            </div>
        </div>
    </div>
    <div id="divLine"></div>
    <div id="divFooter"></div>    
</div>

</body>
</html>
