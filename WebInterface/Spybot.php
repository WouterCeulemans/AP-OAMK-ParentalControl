<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: /index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8" >
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <?php
        include "/php/dbconfig.php";
        $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
        $row = mysql_fetch_assoc($result);
        echo "<link href='/themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        ?>
        <script src="/js/sideMenu.js"></script>
    </head>
    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="/index.php">Home</a></li>
                <li><a href="/account.php">Account</a></li>
                <li><a href="/spybot.php" class="current">Spybot</a></li>
                <li><a href="/tracker.php">Tracker </a></li>
                <div class="Padding"></div>	
                <?php  
                include "/php/dbconfig.php";
                $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                while ($row = mysql_fetch_assoc($result)) 
                echo "<li><a href='/devsettings.php?devid=$row[ID]&devname=$row[Name]' >$row[Name]</a></li>";
                mysql_close($dbhandle);
                ?>
                <div class="Padding"></div>	
                <li><a href="/php/logout.php">Log Out</a></li>
            </ul>
        </div>	
        <div id="divWrapper">
            <div id="divHeader">
                <img src="/images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="/index.php">Home</a></li>
                            <li><a href="/account.php">Account</a></li>
                            <li><a href="/spybot.php" class="current">Spybot  </a></li>
                            <li><a href="/tracker.php">Tracker </a></li>
                            <li><a href="/php/logout.php">Log Out </a></li>
                        </ul>
                    </div>
                    <div id="divDev">
                        <ul>
                            <?php  
                            include "/php/dbconfig.php";
                            $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                            while ($row = mysql_fetch_assoc($result)) 
                            echo "<li><a href='/devsettings.php?devid=$row[ID]&devname=$row[Name]' >$row[Name]</a></li>";
                            mysql_close($dbhandle);
                            ?>
                        </ul>
                    </div>
                </div>
                
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <img class="contentImg" src="/images/BrowserIcon.png" width="90" height="90"/>
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
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <img class="contentImg" src="/images/MessageIcon.png" width="90" height="90"/>
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
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <img class="contentImg" src="/images/PhoneIcon.png" width="90" height="90"/>
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
        </div>
        <div id="divFooter"></div>    
    </body>
</html>
