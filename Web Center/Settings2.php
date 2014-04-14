<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Settings</title>
<link href="Setting_php.css" rel="stylesheet" type="text/css">
</head>

<body>


<h1 id="Header1">Settings Menu</h1>
<div id="divHome">
	<h3 id="Header3">MM</h3>
		<h5 id="Header5">Messaging Management</h5>

<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabs">
    	<tr><td>Amount of texts: </td><td><input type="text" name="text_message_limit"/></td></tr>
    	<tr><td>Block Number:</td><td><input type="text" name="text_num_lock"/></td></tr>
       	<tr><td>Block Name:</td><td><input type="text" name="text_name_lock"/></td></tr>
        
        <tr><td>Unblock Number:</td><td><input type="text" name="text_num_unlock"/></td></tr>
        <tr><td height="32">Unblock Name:</td><td><input type="text" name="text_name_unlock"/></td></tr>

    </table>
</form>
<h3 id="Header3">CM</h3>
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
<h3 id="Header3">AM</h3>
	<h5 id="Header5">Application Management</h5>

<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabs">
    	<tr><td>Lock phone at:</td><td><input type="time" name="app_lock_phone"/></td></tr>
    	<tr><td>Unlock phone at:</td><td><input type="time" name="app_unlock_phone"/></td></tr>
       	<tr><td>Lock App (name):</td><td><input type="text" name="app_lock_app"/></td></tr>
        <tr><td>Unlock App(name):</td><td><input type="text" name="app_unlock_app"/></td></tr>

    </table>
    
</form>
</div>
</body>
</html>