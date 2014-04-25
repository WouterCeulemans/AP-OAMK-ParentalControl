<?php
include dbconfig.php;

if (isset($_POST["User"]) && isset($_POST["Pass"]))
{
	echo "hello";
	if ($_POST["User"] != '')
	{
		$result = mysql_query("SELECT * FROM users WHERE Name='$_POST['User']';)";
		while ($row = mysql_fetch_array($result)) 
		{
		echo "2";
			if ($row{'Pass'} == sha1( $_POST["Pass"], $raw_output = false)
			{
				echo "Hello";
				$_SESSION["User"] = $_POST["User"];
				$_SESSION["ID"] = $row{'User_ID'};
				header("location:index.php");
			}
			else
				header("location:index.php?LoginError=20");
		}
	}
    print "<meta http-equiv='refresh' content='0;URL=index.php?LoginError=20'>";
}
else
    header("location:index.php?LoginError=99");
?>