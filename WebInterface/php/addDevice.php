<?php
session_start();
require_once('/recaptchalib.php');

if (isset($_SESSION["LoggedIn"]))
	if (isset($_POST["Device"]) && isset($_POST["recaptcha_response_field"]))
	{
		$resp = recaptcha_check_answer ($privatekey,
			$_SERVER["REMOTE_ADDR"],
			$_POST["recaptcha_challenge_field"],
			$_POST["recaptcha_response_field"]);

		if ($_POST["Device"] == '')
		{
			header("location: /account.php?error=53");
			exit();
		} else {
			if ($resp->is_valid) {
				include "dbconfig.php";
				$result = mysql_query("SELECT User_ID FROM devices WHERE ID='$_POST[Device]'");
				$row    = mysql_fetch_assoc($result);
				if ($row["User_ID"] != null) {
					header("location: /account.php?error=51");
					mysql_close($dbhandle);
					exit();
				}	
				$count = mysql_num_rows(mysql_query("SELECT * FROM devices WHERE ID='$_POST[Device]'"));
				if ($count == 0) {
					header("location: /account.php?error=50");
					mysql_close($dbhandle);
					exit();
				}
				$result = mysql_query("UPDATE devices SET USER_ID='$_SESSION[ID]' WHERE ID='$_POST[Device]'");	
				header("location: /account.php?success=54");
				mysql_close($dbhandle);
				exit();
			} else {
				header("location: /account.php?error=52");
				
				die ("The reCAPTCHA wasn't entered correctly. Go back and try it again." .
					"(reCAPTCHA said: " . $resp->error . ")");exit();
			}
		}
	}
header("location: /404.php");
?>