<?php
session_start();
if (isset($_SESSION["LoggedIn"]))
{
	if (isset($_POST["devid"]) && isset($_POST["NewName"]))
	{
		if ($_POST["NewName"] != "")
		{
			include "/dbconfig.php";
			$result = mysql_query("UPDATE devices SET NAME='$_POST[NewName]' WHERE ID='$_POST[devid]'");
			mysql_close($dbhandle);
			header ("location: /account.php?success=31");
			exit();
		}	
		header ("location: /account.php?error=30");
		exit();
	}
}
header("location: /404.php");
?>