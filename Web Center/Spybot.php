﻿<?php
session_start();
if (!isset($_SESSION["User"]))
    header("location:index.php?LoginError=99");
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Parental Web Center</title>
<link href="spybot.css" rel="stylesheet" type="text/css">
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
            	<li><a href="Index.php"					>Home</a></li>
                <li><a href="settings.php" 				>Settings</a></li>
                <li><a href="Spybot.php" class="current"	>Spybot</a></li>
                <li><a href="Tracker.php"					>Tracker</a></li>
            </ul>
    	</div>
    </div>
    <div id="divLine"></div>
    <div id="divHome">
    	<div id="divContent">
        	<img src="Images/BrowserIcon.png" width="90" height="90"/>
        	<h5>Webbrowsing</h5>
            <p>
            Klik on the icon if you wish to see what webbrowsing activities have occured on the cellphone.  
            </p>
            <p>
            <b><u>if you notice any sort of delay or lag then</b></u>
            Servers can be under maintenance<br>
            Check you internet connection<br>
            Contact your network service provider			
            </p>
        </div>
    </div>
    
        <div id="divLine"></div>
    <div id="divHome">
    	<div id="divContent">
        	<img src="Images/MessageIcon.png" width="90" height="90"/>
        	<h5>Messaging</h5>
            <p>
            Klik on the icon if you wish to see what Message activities have occured on the cellphone.  
            </p>
            <p>
            <b><u>if you notice any sort of delay or lag then</b></u>
            Servers can be under maintenance<br>
            Check you internet connection<br>
            Contact your network service provider			
            </p>
        </div>
    </div>

<div id="divLine"></div>
    <div id="divHome">
    	<div id="divContent">
        	<img src="Images/PhoneIcon.png" width="90" height="90"/>
        	<h5>CallActivity</h5>
            <p>
            Klik on the icon if you wish to see what CallActivity has occured on the cellphone recently.  
            </p>
            <p>
            <b><u>if you notice any sort of delay or lag then</b></u>
            Servers can be under maintenance<br>
            Check you internet connection<br>
            Contact your network service provider			
            </p>
        </div>
    </div>
    <div id="divFooter"></div>    
</div>


</body>
</html>
