<?php
session_start();

if (!isset($_SESSION["LoggedIn"]))
{
	if (isset($_GET["hash"]))
	{
		include "dbconfig.php";
		$result = mysql_query("SELECT * FROM users WHERE ResetHash='$_GET[hash]' && ResetPending='1'");
		if (mysql_num_rows ($result) == 0)
		{
			mysql_close($dbhandle);
			header ("location: /index.php?error=40");
			exit();
		}
		while ($row = mysql_fetch_array($result)) 
		{
			$newPass =  GeneratePass (10);
			$from    = "noreply@parentalcontrol.noip.me";
			$subject = "New Password";
			$message = "Username: $row[UserName]\nNew Password: $newPass";
			$password = sha1( $newPass, $raw_output = false );
			$result = mysql_query("UPDATE users SET Pass='$password', ResetHash=NULL, ResetPending='0' WHERE ResetHash='$_GET[hash]'");
				
			mail($row["Email"],$subject,$message,"From: $from\n");
			mysql_close($dbhandle);
			header ("location: /index.php?success=41");
			exit();
		}
	}
}
header("location: /404.php");

function GeneratePass ($length)
{
	$regex ="/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,13}$/";
	$chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=~!@#$%^&*()_+,./<>?;:[]{}\|';
	do {
		$str = '';
		$max = strlen($chars) - 1;
		for ($i=0; $i < $length; $i++)
			$str .= $chars[mt_rand(0, $max)];
	} while (!preg_match ($regex, $str));
	return  "$str";
}
?>