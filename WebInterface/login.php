﻿<?php
if (isset($_POST["User"]) && isset($_POST["Pass"]))
{
    if ($_POST["User"] == '')
    {
        print "<meta http-equiv='refresh' content='0;URL=index.php?LoginError=20'>";
    }
    else
    {
        include "dbconfig.php";
        $result     = mysql_query("SELECT * FROM users WHERE Name='$_POST[User]'");
        while ($row = mysql_fetch_array($result)) 
        {
            if ($row{'Pass'} == sha1( $_POST["Pass"], $raw_output = false))
            {
                session_start();
                $_SESSION["User"] = $_POST["User"];
                $_SESSION["ID"]   = $row{'ID'};
                $_SESSION["LoggedIn"] = "true";
                header("location: index.php?Login");
                mysql_close($dbhandle);
                exit();
            }
            else
            {
                header("location: index.php?LoginError=20");
                mysql_close($dbhandle);
                exit();
            }
        }
    }
}
else
{
    header("location: index.php?LoginError=99");
}
?>