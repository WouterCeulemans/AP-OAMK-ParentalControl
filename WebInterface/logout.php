<?php
session_start();
if (isset($_SESSION[LoggedIn]))
{
	session_destroy();
	header("location: index.php");
}
?>