<?php
session_start();

if (isset($_SESSION["LoggedIn"]))
	if(isset($_POST["lApp"]) && isset($_POST["uApp"]))
	{
		if ($_POST["uApp"] == '' && $_POST["lApp"] == '')
		{
			echo $_POST["uApp"];
			echo $_POST["lApp"];
			header("location: /devsettings.php?devid=$_POST[devid]&error=120");
			exit();
		}
		
		include "dbconfig.php";
		if(!$_POST["lApp"] == '')	
		{
			$result = mysql_query("UPDATE apps SET Visible='0' where ID='$_POST[devid]' && PackageName='$_POST[lApp]'");
		}
		if (!$_POST["uApp"] == '')
		{
			$result = mysql_query("UPDATE apps SET Visible='1' where ID='$_POST[devid]' && PackageName='$_POST[uApp]'");
		}
		header("location: /devsettings.php?devid=$_POST[devid]&success=121");
		exit();	
	}
//header("location: /404.php");
?>