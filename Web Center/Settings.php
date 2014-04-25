<?php
session_start();
if (!isset($_SESSION["User"]))
    header("location:index.php?LoginError=99");
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Settings</title>
<link href="Setting_php.css" rel="stylesheet" type="text/css">
</head>

<body>

<div id="divWrapper">
	<div id="divHeader">
    <img src="Images/Header.png" width="500" height="200"/>
    </div>
    </div>
<div id="divHome">



    <div id="divLinks">
    	<div id="divNav">
        	<ul>
            	<li><a href="Index.html" 					>Home</a></li>
                <li><a href="Settings.html"class="current"	>Settings</a></li>
                <li><a href="Spybot.html"					>Spybot</a></li>
                <li><a href="Tracker.html"					>Tracker</a></li>
            </ul>
    	</div>
    </div>
		<h5 id="Header5">Messaging Management</h5>

<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabs">
    	<tr><td>Amount of texts: </td><td><input type="text" name="text_message_limit"/></td></tr>
    	<tr><td>Block Number:</td><td><input type="text" name="text_num_lock"/></td></tr>
       	<tr><td>Block Name:</td><td><input type="text" name="text_name_lock"/></td></tr>        
        <tr><td>Unblock Number:</td><td><input type="text" name="text_num_unlock"/></td></tr>
        <tr><td>Unblock Name:</td><td><input type="text" name="text_name_unlock"/></td></tr>

    </table>
</form>
	<h5 id="Header5">Call Management</h5>

<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabs">
    	<tr><td>Amount of calling: </td><td><input type="text" name="call_call_limit"/></td></tr>
    	<tr><td>Block Number:</td><td><input type="text" name="call_num_lock"/></td></tr>
       	<tr><td>Block Name:</td><td><input type="text" name="call_name_lock"/></td></tr>
        
        <tr><td>Unblock Number:</td><td><input type="text" name="call_num_unlock"/></td></tr>
        <tr><td>Unblock Name:</td><td><input type="text" name="call_name_unlock"/></td></tr>

    </table>
</form>
	<h5 id="Header5">Application Management</h5>

<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabs">
    	<tr><td>Lock phone at:</td><td><input type="text" name="app_lock_phone"/></td></tr>
    	<tr><td>Unlock phone at:</td><td><input type="text" name="app_unlock_phone"/></td></tr>
       	<tr><td>Lock App (name):</td><td><input type="text" name="app_lock_app"/></td></tr>
        <tr><td>Unlock App(name):</td><td><input type="text" name="app_unlock_app"/></td></tr>
    </table>    
</form>
<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
<table>
	<tr><td>
    <button onClick="btnSave" id="btnOnclick">Save</button>
    </td></tr>
</table>
</form>
</div>
</body>
</html>