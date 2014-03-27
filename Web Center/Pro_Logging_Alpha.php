<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Loging</title>
<link href="Login.css" rel="stylesheet" type="text/css">
</head>

<?
session_start();

	include('/class.login.php');
	
	$login = new Login();
	
	if($login->isLoggedIn())
	echo"Succes!";
	else
	$login->showErrors();

$token = $_SESSION['token'] = md5(uniqid(mt_rand(),true));
?>


<form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
	<table id="tabInlog">
    	<tr><td>Username: </td><td><input type="text" name="username"/></td></tr>
    	<tr><td>Password: </td><td><input type="password" name="password"/></td></tr>
    </table>
    
    <input type="hidden" name="token" value="<?=$token;?>"
    <input type="submit" name="login" value="Log In"/>
    
    
</form>


<body>
</body>
</html>