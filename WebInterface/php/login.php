<?php
if (isset($_POST["UserName"]) && isset($_POST["Pass"]))
{
    if ($_POST["UserName"] == '')
        print "<meta http-equiv='refresh' content='0;URL=/index.php?LoginError=20'>";
    else
    {
        include "dbconfig.php";
        $result = mysql_query("SELECT * FROM users WHERE UserName='$_POST[UserName]'");
        if (mysql_num_rows ($result) != 1)
        {
            header("location: /index.php?LoginError=20");
            mysql_close($dbhandle);
            exit();
        }
        while ($row = mysql_fetch_array($result)) 
            if ($row{'Pass'} == sha1( $_POST["Pass"], $raw_output = false))
            {
                session_start();
                $_SESSION["UserName"] = $_POST["UserName"];
                $_SESSION["Name"]     = $row{'Name'};
                $_SESSION["LastName"] = $row{'LastName'};
                $_SESSION["ID"]       = $row{'ID'};
                $_SESSION["LoggedIn"] = "true";
                header("location: /index.php?Login");
                mysql_close($dbhandle);
                exit();
            }
            else
            {
                header("location: /index.php?LoginError=20");
                mysql_close($dbhandle);
                exit();
            }
    }
}
header("location: /404");
?>