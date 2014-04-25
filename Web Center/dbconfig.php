<?php
$username = "root";
$password = "bx7QBSruC";
$hostname = "localhost"; 
$database = "parentcontrol";
    
$dbhandle = mysql_connect   ($hostname, $username, $password) or die("Unable to connect to MySQL");
$selected = mysql_select_db ($database,$dbhandle) or die("Could not select $database");
?>