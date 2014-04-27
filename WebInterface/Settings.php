<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Settings</title>
        <link href="./css/layout.css" rel="stylesheet" type="text/css" />
        <link href="./css/settings.css" rel="stylesheet" type="text/css" />
        <?php
        include "./php/dbconfig.php";
        $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
        $row = mysql_fetch_assoc($result);
        echo "<link href='./themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        ?>
        <script src="./js/settings.js"></script>
        <script src="./js/sideMenu.js"></script>
    </head>
    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="index.php" >Home</a></li>
                <li><a href="settings.php" class="current">Settings</a></li>
                <li><a href="spybot.php"   >Spybot  </a></li>
                <li><a href="tracker.php"  >Tracker </a></li>
                <li><a href="preferences.php">Preferences </a></li>
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
                            <li><a href="index.php" 				  >Home	   </a></li>
                            <li><a href="settings.php" class="current">Settings</a></li>
                            <li><a href="spybot.php"				  >Spybot  </a></li>
                            <li><a href="tracker.php"			      >Tracker </a></li>
                            <li><a href="preferences.php"             >Preferences</a></li>
                            <li><form action="./php/logout.php"><input type="submit" value="Log Out" /></form></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <div class="tabs">
                       <h3>Add Device</h3>
                        <form method="post" action="./php/addDevice.php">
                            <table>
                                <tr>
                                    <td>Device IMEI: </td>
                                    <td><input type="text" name="Device"/></td>
                                    <td><input type='submit'/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="tabs">
                       <h3>Change Device Name</h3>
                        <form method="post" action="./php/changeDevName.php" name="Change Name">
                            <table>
                                <tr>
                                    <td>Device Name: </td>
                                    <td><select name="devid">
                                    <?php
                                    include "./php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    {
                                        echo "<option value='$row[ID]'>$row[Name]</option>";
                                    }
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>New Name:</td>
                                    <td><input type="text" name="NewName"></td>
                                </tr>
                            </table>
                            <input type="submit" value="Change Name"/>
                        </form>
                    </div>
                    <div class="tabs">
                        <h3 >Messaging Management</h3>
                        <form method="post" action="applyTexts.php">
                            <table>
                                <tr>
                                    <td>Amount of texts: </td>
                                    <td><input type="text" name="text_message_limit"/></td>
                                </tr>
                                <tr>
                                    <td>Block Number: </td>
                                    <td><input type="text" name="text_num_lock"/></td>
                                </tr>
                                <tr>
                                    <td>Block Name: </td>
                                    <td><input type="text" name="text_name_lock"/></td>
                                </tr>
                                <tr>
                                    <td>Unblock Number: </td>
                                    <td><input type="text" name="text_num_unlock"/></td>
                                </tr>
                                <tr>
                                    <td>Unblock Name: </td>
                                    <td><input type="text" name="text_name_unlock"/></td>
                                </tr>
                            </table>
                            <input type='submit' value='Save' />
                        </form>
                    </div>
            
                    <div class="tabs">
                        <h3 >Call Management</h3>
                        <form method="post" action="aplyCall.php">
                            <table>
                                <tr>
                                    <td>Amount of calling: </td>
                                    <td><input type="text" name="call_call_limit"/></td>
                                </tr>
                                <tr>
                                    <td>Block Number: </td>
                                    <td><input type="text" name="call_num_lock"/></td>
                                </tr>
                                <tr>
                                    <td>Block Name: </td>
                                    <td><input type="text" name="call_name_lock"/></td>
                                </tr>

                                <tr>
                                    <td>Unblock Number: </td>
                                    <td><input type="text" name="call_num_unlock"/></td>
                                </tr>
                                <tr>
                                    <td>Unblock Name: </td>
                                    <td><input type="text" name="call_name_unlock"/></td>
                                </tr>
                            </table>
                            <input type='submit' value='Save' />
                        </form>
                    </div>
        
                    <div class="tabs">
                        <h3 >Application Management</h3>
                        <form method="post" action="applyApp.php">
                            <table>
                                <tr>
                                    <td>Lock phone at: </td>
                                    <td><input type="text" name="app_lock_phone"/></td>
                                </tr>
                                <tr>
                                    <td>Unlock phone at: </td>
                                    <td><input type="text" name="app_unlock_phone"/></td>
                                </tr>
                                <tr>
                                    <td>Lock App (name): </td>
                                    <td><input type="text" name="app_lock_app"/></td>
                                </tr>
                                <tr>
                                    <td>Unlock App(name): </td>
                                    <td><input type="text" name="app_unlock_app"/></td>
                                </tr>
                            </table>
                            <input type='submit' value='Save' />
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>