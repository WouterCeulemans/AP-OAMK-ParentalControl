<?php
if (isset($_SESSION["LoggedIn"]))
    header("location: /index.php?");
?>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <link href="/themes/lightTheme.css" rel="stylesheet" type="text/css" />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        <script src="/js/sideMenu.js"></script>
        <script type="text/javascript" language="javascript">
        $(function() {
            $( "#dialog-message" ).dialog({
                modal: true,
                buttons: {
                    Ok: function() {
                        $( this ).dialog( "close" );
                    }
                }
            });
        });
        </script>
    </head>
    <body>
        <div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="/index.php" class="current">Home</a></li>
                <li><a href="/info.html">Info</a></li>
            </ul>
        </div>	
        <div id="divWrapper">
            <?php
            require_once('/php/recaptchalib.php');
            
            if (isset($_POST["UserName"]) && isset($_POST["Name"]) && isset($_POST["LastName"]) && isset($_POST["Pass"]) && isset($_POST["Pass2"]) && isset($_POST["Email"]) && isset($_POST["recaptcha_response_field"]))
            {
                if ($_POST["UserName"] != "" && $_POST["Name"] != "" && $_POST["LastName"] != "" && $_POST["Pass"] != "" && $_POST["Pass2"] != "" && $_POST["Email"] != "" )
                {
                    $resp = recaptcha_check_answer ($privatekey,
                        $_SERVER["REMOTE_ADDR"],
                        $_POST["recaptcha_challenge_field"],
                        $_POST["recaptcha_response_field"]);
                    if ($resp->is_valid)
                    {
                        $regex = "/^[a-zA-Z0-9_]{3,15}$/";
                        if (preg_match ( $regex, $_POST["UserName"]))
                        {
                            if ($_POST["Pass"] == $_POST["Pass2"])
                            {
                                $regex ="/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,13}$/";
                                if (preg_match ( $regex, $_POST["Pass"]))
                                {
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT * FROM users WHERE UserName='$_POST[UserName]'");
                                    if (mysql_num_rows ($result) != 0)
                                    {
                                        echo @"<div id='dialog-message' title='Register Error'><p>Username is already in use.</p></div>";
                                        $uname = false;
                                        mysql_close($dbhandle);
                                        goto end;
                                    }
                                    $result = mysql_query("SELECT * FROM users WHERE Email='$_POST[Email]'");
                                    if (mysql_num_rows ($result) != 0)
                                    {
                                        echo @"<div id='dialog-message' title='Register Error'><p>e-mail is already in use.</p></div>";
                                        $email = false;
                                        mysql_close($dbhandle);
                                        goto end;
                                    }
                                    session_start();
                                    $password = sha1( $_POST["Pass"], $raw_output = false );
                                    $result = mysql_query("INSERT INTO users (UserName,Pass,email,Name,LastName) Values('$_POST[UserName]','$password','$_POST[Email]','$_POST[Name]','$_POST[LastName]')");
                                    $result = mysql_query("SELECT ID  FROM users WHERE UserName='$_POST[UserName]'");
                                    $row = mysql_fetch_array($result);
                                    $_SESSION["UserName"] = $_POST["UserName"];
                                    $_SESSION["Name"]     = $_POST["Name"];
                                    $_SESSION["LastName"] = $_POST["LastName"];
                                    $_SESSION["ID"]       = $row{'ID'};
                                    $_SESSION["LoggedIn"] = "true";
                                    header("location: /index.php?Login");
                                    mysql_close($dbhandle);
                                    goto end;
                                }
                                $pass = false;
                                echo @"<div id='dialog-message' title='Register Error'><p>Password must contain an upper case, lower case and a numeric character. Length must be between 5 and 13 characters</p></div>";
                                goto end;
                            }
                            $pass = false;
                            echo @"<div id='dialog-message' title='Register Error'><p>Passwords do not match.</p></div>";
                            goto end;
                        }
                        $uname = false;
                        echo @"<div id='dialog-message' title='Register Error'><p>Username can only contain upercase, lowercase, numbers and underscore</p></div>";
                        goto end;	
                    }
                    echo @"<div id='dialog-message' title='Register Error'><p>Please fill in the words correctly.</p></div>";
                    goto end;
                }
                if ($_POST["UserName"] == '')
                    $uname = false;
                if ($_POST["Name"] == '')
                    $name = false;
                if ($_POST["LastName"] == '')
                    $lname = false;
                if ($_POST["Pass"] == '' || $_POST["Pass2"] == '')
                    $pass = false;
                if ($_POST["Email"] == '')
                    $email = false;
                echo @"<div id ='dialog-message' title='Register Error'><p>Please fill in all fields</p></div>";
                end:
            }
            ?>
            <div id="divHeader">
                <img src="/images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="/index.php">Home</a></li>
                            <li><a href="/info.html">info</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <form method="post" action="register.php" name="Register">
                        <table style="margin: 0 auto 0 auto;">
                            <tr><td>Username:		 </td><td><input type="text"     name="UserName" <?php if(isset($uname)): ?> style="border-color: red;" <?php endif ?> value="<?php if (isset($_POST["UserName"])) echo $_POST["UserName"]; ?>" /> <br /></td></tr>
                            <tr><td>Name:            </td><td><input type="text"     name="Name"     <?php if(isset($name)):  ?> style="border-color: red;" <?php endif ?> value="<?php if (isset($_POST["Name"]))     echo $_POST["Name"];     ?>" /> <br /></td></tr>
                            <tr><td>Last Name:       </td><td><input type="text"     name="LastName" <?php if(isset($lname)): ?> style="border-color: red;" <?php endif ?> value="<?php if (isset($_POST["LastName"])) echo $_POST["LastName"]; ?>" /> <br /></td></tr>
                            <tr><td>Password:		 </td><td><input type="password" name="Pass"     <?php if(isset($pass)):  ?> style="border-color: red;" <?php endif ?> /><br /></td></tr>
                            <tr><td>Repeat Password: </td><td><input type="password" name="Pass2"    <?php if(isset($pass)):  ?> style="border-color: red;" <?php endif ?> /><br /></td></tr>
                            <tr><td>E-Mail:          </td><td><input type="email"    name="Email"    <?php if(isset($email)): ?> style="border-color: red;" <?php endif ?> value="<?php if (isset($_POST["Email"]))    echo $_POST["Email"];    ?>" /> <br /></td></tr>
                            <tr><td colspan="2">
                            <?php
                            require_once('/php/recaptchalib.php');
                            echo recaptcha_get_html($publickey);
                            ?></td>
                            <tr><td></td>
                            <td><input type="submit" value="Register" class="right"/></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>