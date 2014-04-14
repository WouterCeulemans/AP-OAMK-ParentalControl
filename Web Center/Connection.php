<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Connection</title>
</head>

<body>
<?php
//MySQL connect information 
$c_username = "root"; 				//databse username
$c_password = ""; 	// database password
$c_host = "localhost"; 				// host normal localhost
$c_database= "databasename"; 		// database name!


// connecting to the server 
$connection = mysql_connect($c_host, $c_username, $c_password) 
or die("it seems this site's database isn't responding.");
// connect to the database
mysql_select_db($c_database)
or die(" it seems this site's databse isn't responding.");



?>


</body>
</html>