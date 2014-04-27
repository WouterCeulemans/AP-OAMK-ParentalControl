<?php
if (isset($_SESSION["LoggedIn"]))
	header("location: index.php?");
?>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="./css/layout.css" rel="stylesheet" type="text/css" />
        <link href="./themes/lightTheme.css" rel="stylesheet" type="text/css" />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
		<script src="./js/sideMenu.js"></script>
		<script type="text/javascript" language="javascript">
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
		<div id="SideMenu">
            <div class="Padding"></div>	
            <ul>
                <h5 class="_356">Links</h5>
                <li><a href="index.php" class="current">Home</a></li>
                <li><a href="info.html" >Info</a></li>
            </ul>
        </div>	
		<div id="divWrapper">
			<?php	
			if (isset($_POST["UserName"]) && isset($_POST["Name"]) && isset($_POST["LastName"]) && isset($_POST["Pass"]) && isset($_POST["Pass2"]) && isset($_POST["Email"]))
			{
				if ($_POST["UserName"] != "" && $_POST["Name"] != "" && $_POST["LastName"] != "" && $_POST["Pass"] != "" && $_POST["Pass2"] != "" && $_POST["Email"] != "" )
				{
					$regex ="/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,13}$/";
					if (preg_match ( $regex, $_POST["Pass"]))
					{
						if ($_POST["Pass"] == $_POST["Pass2"])
						{
							include "./php/dbconfig.php";
							$result = mysql_query("SELECT * FROM users WHERE Name='$_POST[User]'");
							if (mysql_num_rows ($result) != 0)
							{
								echo @"<div id='dialog-message' title='Register Error'><p>Username is already in use.</p></div>";
								mysql_close($dbhandle);
								goto end;
							}
							$result = mysql_query("SELECT * FROM users WHERE Name='$_POST[Email]'");
							if (mysql_num_rows ($result) != 0)
							{
								echo @"<div id='dialog-message' title='Register Error'><p>e-mail is already in use.</p></div>";
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
							header("location: index.php?Login");
							mysql_close($dbhandle);
							goto end;
						}
						echo @"<div id='dialog-message' title='Register Error'><p>Passwords do not match.</p></div>";
						goto end;
					}
					echo @"<div id='dialog-message' title='Register Error'><p>Password must contain an upper case, lower case and a numeric character. Length must be between 5 and 13 characters</p></div>";
					goto end;
				}
				echo @"<div id='dialog-message' title='Register Error'><p>Please fill in all fields</p></div>";
				end:
			}
			?>
			<div id="divHeader">
				<img src="Images/Header.png" width="500" height="200"/>
				<div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
					<div id="divNav">
						<ul>
							<li><a href="index.php" >Home</a></li>
							<li><a href="info.html" >info</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div id="divContentWrapper"> 
				<div class="divContentHeader"> </div>
				<div class="divContent">
					<form method="post" action="register.php" name="Register">
						<table>
							<tr><td>Username:		 </td><td><input type="text"     name="UserName" value="<?php if (isset($_POST["UserName"])) echo $_POST["UserName"]; ?>" /> <br /></td></tr>
							<tr><td>Name:            </td><td><input type="text"     name="Name"     value="<?php if (isset($_POST["Name"]))     echo $_POST["Name"];     ?>" /> <br /></td></tr>
							<tr><td>Last Name:       </td><td><input type="text"     name="LastName" value="<?php if (isset($_POST["LastName"])) echo $_POST["LastName"]; ?>" /> <br /></td></tr>
							<tr><td>Password:		 </td><td><input type="password" name="Pass"  /> <br /></td></tr>
							<tr><td>Repeat Password: </td><td><input type="password" name="Pass2" /> <br /></td></tr>
							<tr><td>E-Mail:          </td><td><input type="email"    name="Email"    value="<?php if (isset($_POST["Email"]))    echo $_POST["Email"];    ?>" /> <br /></td></tr>
						</table>
						<input type="submit" value="Register"/>
					</form>
				</div>
			</div>
		</div>
        <div id="divFooter"></div>
    </body>
</html>