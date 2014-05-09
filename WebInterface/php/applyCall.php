<?php
session_start();

if (isset($_SESSION["LoggedIn"]))
	if(isset($_POST["callLim"]) && isset($_POST["contact"]))
	{
		if ($_POST["contact"] == '' || $_POST["callLim"] == '')
		{
			header("location: /devsettings.php?devid=$_POST[devid]&error=110");
			exit();
		}
		include "dbconfig.php";
		$result = mysql_query("UPDATE contacts SET CallMax='$_POST[callLim]' where ID='$_POST[devid]' && Contact_ID='$_POST[contact]'");
		mysql_close($dbhandle);
		header("location: /devsettings.php?devid=$_POST[devid]&success=111");
		exit();	
	}
header("location: /404.php");
?>