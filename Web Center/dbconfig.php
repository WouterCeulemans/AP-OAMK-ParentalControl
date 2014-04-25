<?php
	define('DB_SERVER', 'localhost');
	define('DB_USERNAME', 'root');
	define('DB_PASSWORD', 'bx7QBSruC');
	define('DB_DATABASE', 'parentcontrol');
	
	$connection = mysql_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD or die(mysql_error));
	$database   = mysql_select_db(DB_DATABASE) or die(mysql_error());
?>