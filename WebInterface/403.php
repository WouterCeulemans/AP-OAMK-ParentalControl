<?php
session_start();
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>settings</title>
        <link rel="shortcut icon" href="/images/favico.ico" type="image/x-icon">
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <?php
        if (isset($_SESSION["LoggedIn"]))
        {
            include "/php/dbconfig.php";
            $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
            $row = mysql_fetch_assoc($result);
            echo "<link href='/themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        }
        else
            echo "<link href='/themes/lightTheme.css' rel='stylesheet' type='text/css' />";
        ?>
        <script src="/js/sideMenu.js"></script>
    </head>

    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="/index.php">Home</a></li>
                <?php if (isset($_SESSION["LoggedIn"])): ?>
                <li><a href="/account.php">Account</a></li>
                <!--li><a href="/spybot.php">Spybot</a></li-->
                <li><a href="/tracker.php">Tracker</a></li>
                <div class="Padding"></div>	
                <?php  
                include "/php/dbconfig.php";
                $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                while ($row = mysql_fetch_assoc($result)) 
                echo "<li><a href='/devsettings.php?devid=$row[ID]'>$row[Name]</a></li>";
                mysql_close($dbhandle);
                ?>
                <div class="Padding"></div>	
                <li><a href="/php/logout.php">Log Out</a></li>
                <?php else: ?>
                <li><a href="/info.html">Info</a></li>
                <?php endif ?>
            </ul>
        </div>
        </div>	
        <div id="divWrapper">
            <div id="divHeader">
                <img src="/images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="/index.php">Home</a></li>
                            <?php if (isset($_SESSION["LoggedIn"])): ?>
                            <li><a href="/account.php">Account</a></li>
                            <!--li><a href="/spybot.php">Spybot</a></li-->
                            <li><a href="/tracker.php">Tracker</a></li>
                            <li><a href="/php/logout.php">Log Out</a></li>
                            <?php else: ?>
                            <li><a href="/info.html">Info</a></li>
                            <?php endif ?>
                        </ul>
                    </div>
                    <?php if (isset($_SESSION["LoggedIn"])): ?>
                    <div id="divDev">
                        <ul>
                            <?php  
                            include "/php/dbconfig.php";
                            $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                            while ($row = mysql_fetch_assoc($result)) 
                            echo "<li><a href='/devsettings.php?devid=$row[ID]' >$row[Name]</a></li>";
                            mysql_close($dbhandle);
                            ?>
                        </ul>
                    </div>
                    <?php endif ?>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <p>403</p>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>