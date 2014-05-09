<?php
session_start();
require_once('/recaptchalib.php');

if (!isset($_SESSION["LoggedIn"]))
{
	if (isset($_POST["Email"]) && isset($_POST["recaptcha_response_field"]))
	{
		$resp = recaptcha_check_answer ($privatekey,
			$_SERVER["REMOTE_ADDR"],
			$_POST["recaptcha_challenge_field"],
			$_POST["recaptcha_response_field"]);
		if ($resp->is_valid)
		{
			include "dbconfig.php";
			$result = mysql_query("SELECT * FROM users WHERE Email='$_POST[Email]'");
			if (mysql_num_rows ($result) == 0)
			{
				mysql_close($dbhandle);
				header ("location: /passreset.php?error=61");
				exit();
			}
			while ($row = mysql_fetch_array($result)) 
			{
				$string = $row["ID"].$row["UserName"].$row["Pass"].$row["Email"].$row["Name"].$row["LastName"].date ("dmYHisu");
				$hash = sha1( $string, $raw_output = false );
				$result = mysql_query("UPDATE users SET ResetHash='$hash', ResetPending='1' WHERE Email='$_POST[Email]'");
				
				$from = "noreply@parentalcontrol.noip.me";
				$subject = "Password reset";
				$message = "http://parentalcontrol.noip.me:8080/php/confirmReset.php?hash=$hash";
				
				mail($_POST["Email"],$subject,$message,"From: $from\n");
				
				mysql_close($dbhandle);
				header ("location: /index.php?success=62");
				exit();
			}
		} else {
			header ("location: /passreset.php?error=60");
			exit();
		}
	}
}
header("location: /404.php");
?>