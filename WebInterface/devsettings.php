<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: /index.php?error=99");
if (!isset($_GET["devid"]))
    header("location: /404.php");

include "/php/dbconfig.php";
$result = mysql_query("SELECT * FROM devices WHERE ID='$_GET[devid]' && User_ID='$_SESSION[ID]'");
if (mysql_num_rows ($result) != 1)
{
    header("location: /403.php");
    mysql_close($dbhandle);
}

?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Settings</title>
        <link rel="shortcut icon" href="/images/favico.ico" type="image/x-icon">
        <link href="/css/layout.css" rel="stylesheet" type="text/css" />
        <?php
        if (isset($_SESSION["LoggedIn"]))
        {
            include "/php/dbconfig.php";
            $result = mysql_query("SELECT Theme FROM users WHERE ID='$_SESSION[ID]'");
            $row = mysql_fetch_assoc($result);
            echo "<link href='/themes/$row[Theme]Theme.css' rel='stylesheet' type='text/css' />";
        }
        else
            echo "<link href='/themes/lightTheme.css' rel='stylesheet' type='text/css' />";
        ?>
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
                <li><a href="/account.php">Account</a></li>
                <!--li><a href="/spybot.php">Spybot</a></li-->
                <li><a href="/tracker.php">Tracker</a></li>
                <div class="Padding"></div>
                <?php  
                include "/php/dbconfig.php";
                $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                while ($row = mysql_fetch_assoc($result)) 
                {
                    $string = '';
                    $string = "<li><a href='/devsettings.php?devid=$row[ID]'";
                    if ($row["ID"] == $_GET["devid"])
                        $string .=" class=current";
                    $string .= ">$row[Name]</a></li>";
                    echo $string;
                }
                mysql_close($dbhandle);
                ?>
                <div class="Padding"></div>	
                <li><a href="/php/logout.php">Log Out</a></li>
            </ul>
        </div>	
        </div>	
        <div id="divWrapper">
            <?php
            if (isset($_GET["error"]))
                switch ($_GET["error"]) {
                    case 80:
                    case 100:
                    case 110:
                    case 120:
                        print @"<div id='dialog-message' title='Login Error'><p>Please fill in all fields.</p></div>";
                        break;
                }
            if (isset($_GET["success"]))
                switch ($_GET["success"]) {
                    case 81:
                        print @"<div id='dialog-message' title='Info'><p>Contact information changed.</p></div>";
                        break;
                    case 101:
                        print @"<div id='dialog-message' title='Info'><p>Texting information changed.</p></div>";
                        break;
                    case 111:
                        print @"<div id='dialog-message' title='Info'><p>Calling information changed.</p></div>";
                        break;
                    case 121:
                        print @"<div id='dialog-message' title='Info'><p>App information changed.</p></div>";
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
                            <li><a href="/account.php">Account</a></li>
                            <!--li><a href="/spybot.php">Spybot</a></li-->
                            <li><a href="/tracker.php">Tracker </a></li>
                            <li><a href="/php/logout.php">Log Out</a></li>
                        </ul>
                    </div>
                    <div id="divDev">
                        <ul>
                        <?php  
                        include "/php/dbconfig.php";
                        $result = mysql_query("SELECT Name, ID FROM devices WHERE User_ID='$_SESSION[ID]'");
                        while ($row = mysql_fetch_assoc($result)) 
                        {
                            $string = '';
                            $string = "<li><a href='/devsettings.php?devid=$row[ID]'";
                            if ($row["ID"] == $_GET["devid"])
                                $string .=" class=current";
                            $string .= ">$row[Name]</a></li>";
                            echo $string;
                        }
                        mysql_close($dbhandle);
                        ?>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="divContentWrapper"> 
                <div class="divContentHeader"> </div>
                <div class="divContent">
                    <div class="tabs">
                        <h3 >Contact Management</h3>
                        <form method="post" action="/php/applyContacts.php">
                            <input type="hidden" value='<?php echo $_GET["devid"]; ?>' name="devid"/>
                            <table>
                                <tr>
                                    <td>Block Contact: </td>
                                    <td><select name='bcontact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT FirstName, LastName, Contact_ID FROM contacts WHERE ID='$_GET[devid]' && blocked='0'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[Contact_ID]'>$row[FirstName] $row[LastName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Unblock Contact: </td>
                                    <td><select name='ucontact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT FirstName, LastName, Contact_ID FROM contacts WHERE ID='$_GET[devid]' && blocked='1'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[Contact_ID]'>$row[FirstName] $row[LastName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr><td></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    
                    <div class="tabs">
                        <h3 >Messaging Management</h3>
                        <form method="post" action="/php/applyTexts.php">
                            <input type="hidden" value='<?php echo $_GET["devid"]; ?>' name="devid"/>
                            <table>
                                <tr>
                                    <td>Contact: </td>
                                    <td><select name='contact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT FirstName, LastName, Contact_ID FROM contacts WHERE ID='$_GET[devid]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[Contact_ID]'>$row[FirstName] $row[LastName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Amount of texts: </td>
                                    <td><input type="text" name="txtLim"/></td>
                                </tr>
                                <tr><td><a id='middle' href="/php/reset.php?m=m&devid=<?php echo $_GET["devid"]; ?>">Rest</a></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
            
                    <div class="tabs">
                        <h3 >Call Management</h3>
                        <form method="post" action="/php/applyCall.php">
                            <input type="hidden" value='<?php echo $_GET["devid"]; ?>' name="devid"/>
                            <table>
                                <tr>
                                    <td>Contact: </td>
                                    <td><select name='contact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT FirstName, LastName, Contact_ID FROM contacts WHERE ID='$_GET[devid]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[Contact_ID]'>$row[FirstName] $row[LastName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>																																				
                                </tr>
                                <tr>
                                    <td>Amount of calling: </td>
                                    <td><input type="text" name="callLim"/></td>
                                </tr>
                                <tr><td><a id='middle' href="/php/reset.php?m=c&devid=<?php echo $_GET["devid"]; ?>">Rest</a></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
        
                    <div class="tabs">
                        <h3 >Application Management</h3>
                        <form method="post" action="/php/applyApp.php">
                            <input type="hidden" value='<?php echo $_GET["devid"]; ?>' name="devid"/>
                            <table>
                                <tr>
                                    <td>Lock phone at: </td>
                                    <td><input type="text" name="phoneLock"/></td>
                                </tr>
                                <tr>
                                    <td>Unlock phone at: </td>
                                    <td><input type="text" name="phoneUnlock"/></td>
                                </tr>
                                <tr>
                                    <td>Lock App: </td>
                                    <td><select name='lApp'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name,PackageName FROM apps WHERE ID='$_GET[devid]' && Visible='1'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[PackageName]'>$row[Name]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Unlock App: </td>
                                    <td><select name='uApp'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name,PackageName FROM apps WHERE ID='$_GET[devid]' && Visible='0'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[PackageName]'>$row[Name]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr><td></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="divFooter"></div>
    </body>
</html>