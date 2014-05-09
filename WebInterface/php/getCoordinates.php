<?php
session_start();
if (isset($_SESSION["LoggedIn"]))
{
	if (isset($_GET["devid"])) {
		include "/dbconfig.php";
		$result = mysql_query("SELECT * FROM coordinates WHERE ID='$_GET[devid]'");
		$index  = 0;
		$c      = Array();
		while ($row = mysql_fetch_assoc($result)) 
		{
			$c[$index]["x"]  = $row["LATITUDE"];
			$c[$index]["y"]  = $row["LONGITUDE"];
			$c[$index]["id"] = $row["POS_ID"];
			$index++;
		}
		print json_encode($c);
		mysql_close($dbhandle);
		exit();
	}
}
header("location: /404.php");
?>