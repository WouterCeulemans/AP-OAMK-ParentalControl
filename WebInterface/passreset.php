<?php
session_start();
if (isset($_SESSION["LoggedIn"]))
    header("location: /404.php");
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>settings</title>
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <link href='/themes/lightTheme.css' rel='stylesheet' type='text/css' />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        <script src="/js/sideMenu.js"></script>
        <script language="javascript" type="text/javascript">
            $(function () {
                $("#dialog-message").dialog({
                    modal: true,
                    buttons: {
                        Ok: function () {
                            $(this).dialog("close");
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
                <li><a href="/index.php">Home</a></li>
                <li><a href="/info.html">Info</a></li>
            </ul>
        </div>	
        </div>	
        <div id="divWrapper">
            <?php
            if (isset($_GET["error"]))
                switch ($_GET["error"]) {
                    case 60:
                        print @"<div id='dialog-message' title='Error'><p>Please fill in the words correctly.</p></div>";
                        break;
                    case 61:
                        print @"<div id='dialog-message' title='Error'><p>No account exists on this e-mail.</p></div>";
                        break;
            }
            ?>
            <div id="divHeader">
                <img src="/images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="/index.php">Home</a></li>
                            <li><a href="/info.html">Info</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <div class="tabs">
                        <h3>Reset Password</h3>
                        <form method="post" action="/php/reset.php" name="Register">
                            <table style="margin: 0 auto 0 auto;">
                                <tr><td>E-Mail:</td><td><input type="email" name="Email"/><br/></td></tr>
                                <tr><td colspan="2">
                                <?php
                                require_once('/php/recaptchalib.php');
                                echo recaptcha_get_html($publickey);
                                ?></td>
                                <tr><td></td>
                                <td><input type="submit" value="Send recovery e-mail" class="right"/></td>
                                </tr>
                            </table>
                            <?php
                            require_once('/php/recaptchalib.php');
                            echo recaptcha_get_html($publickey);
                            ?>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>