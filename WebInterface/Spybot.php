<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="layout.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
			<?php if (isset($_SESSION["LoggedIn"])): ?>
			<form action="index.php&Logout=1"><input type="submit" value="Log Out" /></form>
			<?php endif ?>
            <div id="divLine"></div>
            <div id="divLinks">
                <div id="divNav">
                    <ul>
                        <li><a href="index.php"					>Home</a></li>
                        <li><a href="settings.php" 				>Settings</a></li>
                        <li><a href="spybot.php" class="current">Spybot</a></li>
                        <li><a href="tracker.php"				>Tracker</a></li>
                    </ul>
                </div>
            </div>
            <div id="divLine"></div>
        </div>
        <div class="divContent">
            <img class="contentImg" src="Images/BrowserIcon.png" width="90" height="90"/>
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
        <div class="divContent">
            <img class="contentImg" src="Images/MessageIcon.png" width="90" height="90"/>
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
        <div class="divContent">
            <img class="contentImg" src="Images/PhoneIcon.png" width="90" height="90"/>
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
    <div id="divFooter"></div>    
</body>
</html>
