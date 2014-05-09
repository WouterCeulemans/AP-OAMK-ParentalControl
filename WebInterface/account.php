<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: /index.php?error=99");
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Account Settings</title>
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <?php
        include "/php/dbconfig.php";
        $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
        $row    = mysql_fetch_assoc($result);
        echo "<link href='/themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        ?>
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        <script src="/js/settings.js"></script>
        <script src="/js/sideMenu.js"></script>
        <script language="javascript" type="text/javascript">
            $(function () {
                $("#dialog-message").dialog({
                    modal: true,
                    buttons: {
                        Ok: function () {
                            $(this).dialog("close");
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="/index.php">Home</a></li>
                <li><a href="/account.php" class="current">Account</a></li>
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
            </ul>
        </div>	
        <div id="divWrapper">
            <?php
            if (isset($_GET["error"]))
                switch ($_GET["error"]) {
                    case 30:
                        print @"<div id='dialog-message' title='Error'><p>No new device name given.</p></div>";
                        break;
                    case 50:
                        print @"<div id='dialog-message' title='Error'><p>The given device does not exist</p></div>";
                        break;
                    case 51:
                        print @"<div id='dialog-message' title='Error'><p>This link is no longer valid</p></div>";
                        break;
                    case 52:
                        print @"<div id='dialog-message' title='Error'><p>Please fill in the words correctly.</p></div>";
                        break;
                    case 53:
                        print @"<div id='dialog-message' title='Error'><p>No device is given.</p></div>";
                        break;
                    case 70:
                        print @"<div id='dialog-message' title='Error'><p>Passwords do not match.</p></div>";
                        break;
                    case 71:
                        print @"<div id='dialog-message' title='Error'><p>Password 's not strong enough.</p></div>";
                        break;
            		case 72:
            			print @"<div id='dialog-message' title='Error'><p>Current password not correct.</p></div>";
            			break;
                }
            if (isset($_GET["success"]))
                switch ($_GET["success"]) {
                    case 31:
                        print @"<div id='dialog-message' title='Info'><p>Device name changed.</p></div>";
                        break;
                    case 54:
                        print @"<div id='dialog-message' title='Info'><p>Device successfully added to your account.</p></div>";
                        break;
                    case 72:
                        print @"<div id='dialog-message' title='Info'><p>Passwords changed.</p></div>";
                        break;
                }
            ?>
            <div id="divHeader">
                <img src="/images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="/index.php">Home</a></li>
                            <li><a href="/account.php" class="current">Account</a></li>
                            <!--li><a href="/spybot.php">Spybot</a></li-->
                            <li><a href="/tracker.php">Tracker</a></li>
                            <li><a href="/php/logout.php">Log Out</a></li>
                        </ul>
                    </div>
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
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <div class="tabs">
                       <h3>Add Device</h3>
                        <form method="post" action="/php/addDevice.php">
                            <table>
                                <tr>
                                    <td>Device IMEI: </td>
                                    <td><input type="text" name="Device"/></td>
                                </tr>
                                <tr><td colspan="2">
                                <?php
                                require_once('/php/recaptchalib.php');
                                echo recaptcha_get_html($publickey);
                                ?></td>
                                </tr>
                                <tr><td></td>
                                <td><input  type='submit' class="right" value="Add Device"/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="tabs">
                        <h3>Change Device Name</h3>
                        <form method="post" action="/php/changeDevName.php" name="Change Name">
                            <table>
                                <tr>
                                    <td>Device Name: </td>
                                    <td><select name="devid">
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                        echo "<option value='$row[ID]'>$row[Name]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>New Name:</td>
                                    <td><input type="text" name="NewName"></td>
                                </tr>
                                <tr><td></td>
                                <td><input type="submit" class="right" value="Change Name"/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    
                    <div class="tabs">
                       <h3>Layout</h3>
                        <form method="post" action="/php/changeTheme.php">
                            <table>
                                <tr>
                                    <td>Theme </td>
                                    <td><select name="theme">
                                    <option value='light'>Light</option>
                                    <option value='dark' >Dark</option>
                                    </select></td>
                                    <td><input type='submit' class="right" value="Change Theme"/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    
                    <div class="tabs">
                       <h3>Change password</h3>
                        <form method="post" action="/php/changePass.php">
                            <table>
                                <tr><td>Old Password:	 </td><td><input type="password" name="cpass" /><br /></td></tr>
                                <tr><td>New Password:    </td><td><input type="password" name="pass" /><br /></td></tr>
                                <tr><td>Repeat Password: </td><td><input type="password" name="pass2"/><br /></td></tr>
                                <tr><td></td>
                                <td><input type='submit' class="right" value="Change Pass"/></td></tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>