<?php
session_start();

if (isset($_SESSION["LoggedIn"]))
	if(isset($_POST["txtLim"]) && isset($_POST["contact"]))
	{
		if ($_POST["contact"] == '' || $_POST["txtLim"] == '')
		{
			header("location: /devsettings.php?devid=$_POST[devid]&error=100");
			exit();
		}
		include "dbconfig.php";
		$result = mysql_query("UPDATE contacts SET TxtMax='$_POST[txtLim]' where ID='$_POST[devid]' && Contact_ID='$_POST[contact]'");
		mysql_close($dbhandle);
		header("location: /devsettings.php?devid=$_POST[devid]&success=101");
		exit();	
	}
header("location: /404.php");
?>