<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Untitled Document</title>
</head>

<?php 
	session_start();
	if(isset($_SESSION['id']))
	{
		//redirection to login page twitter or facebook 
		header("location: home.php");
	}
	if (array_key_exists("login", $_GET))
	{
		$oauth_provider = $_GET['oauth_provider'];
		if($oauth_provider == 'twitter')
		{
			header("Location : login-twitter.php");
		}
		else if ($oauth_provider == 'facebook')
		{
			header("Location : login-facebook.php");
		}
	}
?>
<a href="">Twitter_Login</a>
<a href="">Facebook</a>

<body>
</body>
</html>