<?php
session_start();

if (isset($_SESSION["LoggedIn"]))
	if(isset($_GET["m"]) && isset($_GET["devid"]))
	{
		include "dbconfig.php";
		if(!$_GET["m"] == 'm')	
			$result = mysql_query("UPDATE contacts SET TxtMax='0', TxtAmount='0' where ID='$_GET[devid]' && Contact_ID='$_GET[contact]'");

		if (!$_GET["m"] == 'c')
			$result = mysql_query("UPDATE contacts SET CallMax='0', CallAmount='0' where ID='$_GET[devid]' && Contact_ID='$_GET[contact]'");

		mysql_close($dbhandle);
		header("location: /devsettings.php?devid=$_GET[devid]&success=130");
		exit();	
	}
header("location: /404.php");
?>