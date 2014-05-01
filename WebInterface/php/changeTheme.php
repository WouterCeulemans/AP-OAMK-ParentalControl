<?php
session_start();
if (isset($_SESSION["LoggedIn"]))
{
	if (isset($_POST["theme"])){
		if ($_POST["theme"] == 'light' || $_POST["theme"] == 'dark')
		{
			include "dbconfig.php";
			$result = mysql_query("UPDATE users SET Theme='$_POST[theme]' WHERE ID='$_SESSION[ID]'");	
			header("location: /account.php?success=$_POST[theme]");
			mysql_close($dbhandle);
			exit();
		}
		header("location: /account.php");
		exit();
	}
}
header("location: /404");
?>
