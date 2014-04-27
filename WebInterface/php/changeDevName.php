<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
	header("location: ../index.php?LoginError=99");

if (isset($_POST["devid"]) && isset($_POST["NewName"]))
{
	if ($_POST["NewName"] != "")
	{
		include "./dbconfig.php";
		$result = mysql_query("UPDATE devices SET NAME='$_POST[NewName]' WHERE ID='$_POST[devid]'");
		mysql_close($dbhandle);
		header ("location: ../settings.php?success");
	}	
}
?>