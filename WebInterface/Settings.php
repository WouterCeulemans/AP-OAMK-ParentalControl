<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Settings</title>
        <link href="Layout.css" rel="stylesheet" type="text/css" />
        <link href="Settings.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
            <div id="divLine"></div>
            <div id="divLinks">
                <div id="divNav">
                    <ul>
                        <li><a href="Index.php" 				  >Home	   </a></li>
                        <li><a href="Settings.php" class="current">Settings</a></li>
                        <li><a href="Spybot.php"				  >Spybot  </a></li>
                        <li><a href="Tracker.php"			      >Tracker </a></li>
                    </ul>
                </div>
            </div>
            <div id="divLine"></div>
        </div>
        <div class="divContent">
        <div class="tabs">
           <h3>Add Device</h3>
            <form method="post" action="addDevice.php">
                <table>
                    <tr>
                        <td>Device: </td>
                        <td><input type="text" name="Device"/></td>
                        <td><input type='submit'/></td>
                    </tr>
                </table>
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
                    <input type='submit' value='Login' />
                </table>
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
                <input type='submit' value='Login' />
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
                <input type='submit' value='Login' />
            </form>
            </div>
        </div></div>
    </body>
</html>