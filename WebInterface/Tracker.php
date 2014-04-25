﻿<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="tracker.css" rel="stylesheet" type="text/css" />
        <link href="layout.css"  rel="stylesheet" type="text/css" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        <script src="http://maps.google.com/maps/api/js?sensor=false"></script>  
        <script src="tracker.js"></script>
    </head>

    <body>
        <div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
            <?php if (isset($_SESSION["LoggedIn"])): ?>
            <form action="logout.php"><input type="submit" value="Log Out" /></form>
            <?php endif ?>
            <div id="divLine"></div>
            <div id="divLinks">
                <div id="divNav">
                    <ul>
                        <li><a href="index.php" 					>Home</a></li>
                        <li><a href="settings.php"					>Settings</a></li>
                        <li><a href="spybot.php"					>Spybot</a></li>
                        <li><a href="tracker.php" class="current"	>Tracker</a></li>
                    </ul>
                </div>
            </div>
            <div id="divLine"></div>
        </div>

        <div class="divContent"
            <table>
                <tr>
                    <td><select id='devid'>
                    <?php
                    include "dbconfig.php";
                    $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                    while ($row = mysql_fetch_assoc($result)) 
                    {
                        echo "<option value='$row[ID]'>$row[Name]</option>";
                    }
                    ?>
                    </select></td>
                    <td><input type="button" value="Send" style="margin: 0px;margin-bottom: 5px; margin-top: 5px;" onclick="GetCoordinates()"></td>
                </tr>
            </table>
            <p>Google maps </p>
            <div id="map_canvas"></div>
            </div>
        </div>
        <div id="divFooter"></div>
    <body>
</html>
