﻿<?php
session_start();
if (isset($_SESSION["LoggedIn"]))
    if (isset($_POST["pass"]) && isset($_POST["pass2"]))
        if ($_POST["pass"] == $_POST["pass2"])
        {
            $regex ="/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,13}$/";
            if (preg_match ( $regex, $_POST["pass"]))
            {
                include "/dbconfig.php";
                $password = sha1( $_POST["pass"], $raw_output = false );
                $result   = mysql_query("UPDATE users SET Pass='$password' WHERE ID='$_SESSION[ID]'");
                mysql_close($dbhandle);
                header ("location: /account.php?success=72");
                exit();
            }
            header ("location: /account.php?error=71");
            exit();
        }
        header ("location: /account.php?error=70");
        exit();
header("location: /404");
?>