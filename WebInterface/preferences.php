<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Preferences</title>
        <link href="./css/layout.css" rel="stylesheet" type="text/css" />
        <link href="./css/settings.css" rel="stylesheet" type="text/css" />
        <?php
        include "./php/dbconfig.php";
        $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
        $row = mysql_fetch_assoc($result);
        echo "<link href='./themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        ?>
        <script src="./js/sideMenu.js"></script>
    </head>

    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="index.php">Home</a></li>
                <li><a href="settings.php" >Settings</a></li>
                <li><a href="spybot.php"   >Spybot  </a></li>
                <li><a href="tracker.php"  >Tracker </a></li>
                <li><a href="preferences.php" class="current" >Preferences</a></li>
                <div class="Padding"></div>	
                <li><form action="./php/logout.php"><input type="submit" value="Log Out" /></form></li>
            </ul>
        </div>	
        <div id="divWrapper">
            <div id="divHeader">
                <img src="Images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="index.php" >Home</a></li>
                            <li><a href="settings.php" >Settings</a></li>
                            <li><a href="spybot.php"   >Spybot  </a></li>
                            <li><a href="tracker.php"  >Tracker </a></li>
                            <li><a href="preferences.php" class="current">Preferences</a></li>
                            <li><form action="./php/logout.php"><input type="submit" value="Log Out" /></form></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <div class="tabs">
                       <h3>Layout</h3>
                        <form method="post" action="./php/changeTheme.php">
                            <table>
                                <tr>
                                    <td>Theme </td>
                                    <td><select name="theme">
                                    <option value='light'>Light</option>
                                    <option value='dark'>Dark</option>
                                    </select></td>
                                    <td><input type='submit'/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>