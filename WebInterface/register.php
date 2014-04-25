<?php
if (isset($_POST["User"]))
	header("location: index.php?");
	
if (isset($_POST["User"]) && isset($_POST["Pass"]) && isset($_POST["Pass2"]) && isset($_POST["Email"]))
{
    if ($_POST["User"] != "" && $_POST["Pass"] != "" && $_POST["Pass2"] != "" && $_POST["Email"] != "" )
    {
        if ($_POST["Pass"] == $_POST["Pass2"]){
            include "dbconfig.php";
            $result = mysql_query("SELECT * FROM users WHERE Name='$_POST[User]'");
            if (mysql_num_rows ($result) != 0)
            {
                header("location: register.php?Error=30");
                mysql_close($dbhandle);
                exit();
            }
            $result = mysql_query("SELECT * FROM users WHERE Name='$_POST[Email]'");
            if (mysql_num_rows ($result) != 0)
            {
                header("location: register.php?Error=31");
                mysql_close($dbhandle);
                exit();
            }
            session_start();
            $password = sha1( $_POST["Pass"], $raw_output = false );
            $result = mysql_query("INSERT INTO users (Name,Pass,email) Values('$_POST[User]','$password','$_POST[Email]')");
            //header("location:index.php");
            exit();
        }
        header("location: register.php?Error=33");
        mysql_close($dbhandle);
        exit();
    }
    header("location: register.php?Error=32");
    mysql_close($dbhandle);
    exit();
}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="layout.css"  rel="stylesheet" type="text/css" />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        
        <script>
        $(function() {
            $( "#dialog-message" ).dialog({
                modal: false,
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
		<div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
            <div id="divLine"></div>
            <div id="divLinks">
                <div id="divNav">
                    <ul>
                        <li><a href="index.php" >Home</a></li>
                        <li><a href="info.html" >info</a></li>
                    </ul>
                </div>
            </div>
            <div id="divLine"></div>
        </div>
		<div class="divContent">
			<form method="post" action="register.php" name="Register">
				<table>
					<tr><td>Username: </td><td><input type="text"     name="User"  /> <br /></td></tr>
					<tr><td>Password: </td><td><input type="password" name="Pass"  /> <br /></td></tr>
					<tr><td>Repeat Password: </td><td><input type="password" name="Pass2"  /> <br /></td></tr>
					<tr><td>E-Mail  : </td><td><input type="email"    name="Email" /> <br /></td></tr>
				</table>
				<input type="submit" value="Register"/>
			</form>
			<?php
			if (isset($_GET["Error"]))
			{
				if ($_GET["Error"] == 30)
					echo @"<div id='dialog-message' title='Register Error'>
								<p>Username is already in use.</p>
							</div>";
				if ($_GET["Error"] == 31)
					echo @"<div id='dialog-message' title='Register Error'>
								<p>e-mail is already in use.</p>
							</div>";
				if ($_GET["Error"] == 32)
					echo @"<div id='dialog-message' title='Register Error'>
								<p>UPlease fill in all fields</p>
							</div>";
				if ($_GET["Error"] == 33)
					echo @"<div id='dialog-message' title='Register Error'>
								<p>passwords do not match.</p>
							</div>";
			}
			?>
			</div>
        <div id="divFooter" style="position:absolute; bottom:0; width:100%" ></div>
    </body>
</html>