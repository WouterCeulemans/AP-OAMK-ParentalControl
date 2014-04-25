<?php
include "dbconfig.php";
session_start();

if (isset($_POST["Device"]))
{
	if ($_POST["Device"] == '')
	{
		header("location: index.php?LoginError=99");
		mysql_close($dbhandle);
		exit();
	}
	else
	{
		$count = mysql_num_rows(mysql_query("SELECT * FROM devices WHERE ID='$_POST[Device]'"));
		if ($count == 0)
		{
			header("location: settings.php?Error=50");
			mysql_close($dbhandle);
			exit();
		}
		$result = mysql_query("UPDATE devices SET USER_ID='$_SESSION[ID]' WHERE ID='$_POST[Device]'");	
		header("location: settings.php?success=$_POST[Device]");
		mysql_close($dbhandle);
		exit();
	}
}
else
{
	header("location: setting.php");
	mysql_close($dbhandle);
	exit();
}
?>