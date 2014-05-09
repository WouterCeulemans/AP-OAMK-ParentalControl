<?php
session_start();

if (isset($_SESSION["LoggedIn"]))
	if(isset($_POST["bcontact"]) && isset($_POST["ucontact"]))
	{
		if ($_POST["bcontact"] == '' && $_POST["ucontact"] == '')
		{
			header("location: /devsettings.php?devid=$_POST[devid]&error=80");
			exit();
		}
		include "dbconfig.php";
		if(!$_POST["bcontact"] == '')	
		{
			$result = mysql_query("UPDATE contacts SET Blocked='1' where ID='$_POST[devid]' && Contact_ID='$_POST[bcontact]'");
		}
		if (!$_POST["ucontact"] == '')
		{
			$result = mysql_query("UPDATE contacts SET Blocked='0' where ID='$_POST[devid]' && Contact_ID='$_POST[ucontact]'");
		}
		mysql_close($dbhandle);
		header("location: /devsettings.php?devid=$_POST[devid]&success=81");
		exit();	
	}
header("location: /404.php");
?>