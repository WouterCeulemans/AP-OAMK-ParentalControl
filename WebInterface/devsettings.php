<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: /index.php?LoginError=99");
if (!isset($_GET["devid"]) || !isset($_GET["devname"]))
    header("location: /404");
?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Settings</title>
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
                    $string = "<li><a href='/devsettings.php?devid=$row[ID]&devname=$row[Name]'";
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
                            $string = "<li><a href='/devsettings.php?devid=$row[ID]&devname=$row[Name]'";
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
                        <form method="post" action="applyContacts.php">
                            <table>
                                <tr>
                                    <td>Block Contact: </td>
                                    <td><select id='bcontact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, SurName FROM contacts WHERE ID='$_GET[devid]' && blocked='0'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Unblock Contact: </td>
                                    <td><select id='ucontact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, SurName FROM contacts WHERE ID='$_GET[devid]' && blocked='1'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
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
                        <form method="post" action="applyTexts.php">
                            <table>
                                <tr>
                                    <td>Contact: </td>
                                    <td><select id='contact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, SurName FROM contacts WHERE ID='$_GET[devid]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Amount of texts: </td>
                                    <td><input type="text" name="txt_limit"/></td>
                                </tr>
                                <tr><td></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
            
                    <div class="tabs">
                        <h3 >Call Management</h3>
                        <form method="post" action="aplyCall.php">
                            <table>
                                <tr>
                                    <td>Contact: </td>
                                    <td><select id='contact'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name, SurName FROM contacts WHERE ID='$_GET[devid]'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Amount of calling: </td>
                                    <td><input type="text" name="call_call_limit"/></td>
                                </tr>
                                <tr><td></td>
                                <td><input type='submit' class="right" value='Save' /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
        
                    <div class="tabs">
                        <h3 >Application Management</h3>
                        <form method="post" action="applyApp.php">
                            <table>
                                <tr>
                                    <td>Lock phone at: </td>
                                    <td><input type="text" name="app_lock_phone"/></td>
                                </tr>
                                <tr>
                                    <td>Unlock phone at: </td>
                                    <td><input type="text" name="app_unlock_phone"/></td>
                                </tr>
                                <tr>
                                    <td>Lock App: </td>
                                    <td><select id='lapp'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name FROM apps WHERE ID='$_GET[devid]' && Visible='0'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
                                    mysql_close($dbhandle);
                                    ?>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Unlock App: </td>
                                    <td><select id='uapp'>
                                    <option value=''></option>
                                    <?php
                                    include "/php/dbconfig.php";
                                    $result = mysql_query("SELECT Name FROM apps WHERE ID='$_GET[devid]' && Visible='1'");
                                    while ($row = mysql_fetch_assoc($result)) 
                                    echo "<option value='$row[ID]'>$row[Name] $row[SurName]</option>";
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