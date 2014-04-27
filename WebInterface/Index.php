<?php session_start();?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="./css/layout.css" rel="stylesheet" type="text/css" />
        <?php
        if (isset($_SESSION["LoggedIn"]))
        {
            include "./php/dbconfig.php";
            $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
            $row = mysql_fetch_assoc($result);
            echo "<link href='./themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        }
        else
            echo "<link href='./themes/lightTheme.css' rel='stylesheet' type='text/css' />";
        ?>
        <link href="./css/index.css" rel="stylesheet" type="text/css" />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
        <script src="./js/sideMenu.js"></script>
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
                <li><a href="index.php" class="current">Home</a></li>
                <?php if (isset($_SESSION["LoggedIn"])): ?>
                <li><a href="settings.php" >Settings</a></li>
                <li><a href="spybot.php"   >Spybot  </a></li>
                <li><a href="tracker.php"  >Tracker </a></li>
                <li><a href="preferences.php" >Preferences</a></li>
                <div class="Padding"></div>	
                <li><form action="./php/logout.php"><input type="submit" value="Log Out" /></form></li>
                <?php else: ?>
                <li><a href="info.html" >Info</a></li>
                <?php endif ?>
            </ul>
        </div>	
        <div id="divWrapper">
            <?php
                if (isset($_GET["LoginError"]))
                    if ($_GET["LoginError"] == 99)
                        print @"<div id='dialog-message' title='Error'><p>You need to login before you can use this site.</p></div>";
                    else if($_GET["LoginError"] == 20)
                        print @"<div id='dialog-message' title='Login Error'><p>Wrong username or password.</p></div>";
            ?>
            <div id="divHeader">
                <img src="Images/Header.png" width="500" height="200"/>
                <div id="divLinks">
                    <div onclick="onButtonClick()" id="MenuButton"></div>
                    <div id="divNav">
                        <ul>
                            <li><a href="index.php" class="current">Home</a></li>
                            <?php if (isset($_SESSION["LoggedIn"])): ?>
                            <li><a href="settings.php" >Settings</a></li>
                            <li><a href="spybot.php"   >Spybot  </a></li>
                            <li><a href="tracker.php"  >Tracker </a></li>
                            <li><a href="preferences.php">Preferences</a></li>
                            <li><form action="./php/logout.php"><input type="submit" value="Log Out" /></form></li>
                            <?php else: ?>
                            <li><a href="info.html" >Info</a></li>
                            <?php endif ?>
                        </ul>
                    </div>
                </div>
            </div>
        
            <?php if (!isset($_SESSION["LoggedIn"])): ?>
            <div id='divLogin'>
                <div class="divContentHeader"> </div>
                <div class='divContent'>
                    <form  method='post' action='./php/login.php' name='Login'>
                        <table>
                            <tr><td>Username: </td><td><input type='text'     name='UserName'  /> <br /></td></tr>
                            <tr><td>Password: </td><td><input type='password' name='Pass'  /> <br /></td></tr>
                            <tr><td><input type='submit' value='Login' /></td>   
                            <td><input type="Button" value="Register" onclick="window.location.href='register.php';" /></td></tr>
                        </table>
                    </form>
                </div>
            </div>
            <?php else: ?>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent" id="UserBox">
                    <img class="contentImg" src="Images/UserIcon.png" width="100" height="100"/>
                    <h3><?php echo "$_SESSION[Name] $_SESSION[LastName]" ?></h3>
                    <p>You have curentley these devices added to your account:</p>
                    <table id="DeviceTable">
                    <tr><th width="170px">Device ID</th><th>Friendley Name</th></tr>
                    <?php
                    include "./php/dbconfig.php";
                    $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                    while ($row = mysql_fetch_assoc($result)) 
                    {
                        echo "<tr><td>$row[ID]</td><td>$row[Name]</td></tr>";
                    }
                    mysql_close($dbhandle);
                    ?>
                    </table>
                </div>
                <?php endif ?>
                <div class="divContentHeader"> </div>
                    <div class="divContent">
                    <h3>Parental Control Product.</h3>
                    <p>Ever wondered what your child is doing on his phone all that time ?</p>
                    <p>Don't really trust you child with its smartphone ?</p>
                    <p>Want to keep your child safe from illigale websites?</p>
                    <p>Thinks he/She is seeing the wrong people ?</p>
                    <p>Never wonder why your child is so late at home ?</p>
                    <p>Are you sure at all time your child is safe when it goes home by bus or bycicle?</p>
                    <p>We can keep going about questions you ask yourself dayly that keeps you uneasy the whole day.</p>
                    <p>
                    We offer you an application that makes sure you can rest you mind at ease.
                    All you need or want to know is possible these days, you just need that cuting edge technology! which we offer you. 
                    Check out our Info Page if your are intrested in monitoring your childs cellphone to make sure, the child is doing what it is supose to do, or that your child is safe at least. Because , anything , can happen at anytime.... right?
                    </p>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>