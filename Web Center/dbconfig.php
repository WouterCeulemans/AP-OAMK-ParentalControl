<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Untitled Document</title>
</head>

<?php
	define('DB_SERVER', 'localhost');
	define('DB_USERNAME', 'User Name');
	define('DB_PASSWORD', 'Password');
	define('DB_DATABASE', 'DATABSE');
	
	$connection = mysql_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD or die(mysql_error));
	$database = mysql_select_db(DB_DATABASE) or die(mysql_error());
?>

<body>
</body>
</html>