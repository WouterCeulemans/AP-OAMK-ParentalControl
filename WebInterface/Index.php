<?php session_start();?>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=500, user-scalable=0">
        <title>Parental Web Center</title>
        <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
    </head>

    <body>
        <?php
            if (isset($_GET["Logout"]))
            {
                session_start();
                session_destroy();
            }
            if (isset($_GET["LoginError"]))
                if ($_GET["LoginError"] == 99)
                    print @"<div id='dialog-message' title='Error'>
                                <p>You need to login before you can use this site.</p>
                            </div>";
                else if($_GET["LoginError"] == 20)
                    print @"<div id='dialog-message' title='Login Error'>
                                <p>Wrong username or password.</p>
                            </div>";
        ?>
        <div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
            <?php if (isset($_SESSION["LoggedIn"])): ?>
            <form action="index.php&Logout=1"><input type="submit" value="Log Out" /></form>
            <?php endif ?>
            <div id="divLine" ></div>
            <div id="divLinks">
                <div id="divNav">
                    <ul>
                        <li><a href="index.php" class="current">Home</a></li>
                        <?php if (isset($_SESSION["LoggedIn"])): ?>
                        <li><a href="settings.php" >Settings</a></li>
                        <li><a href="spybot.php"   >Spybot  </a></li>
                        <li><a href="tracker.php"  >Tracker </a></li>
                        <?php else: ?>
                        <li><a href="info.html" >Settings</a></li>
                        <?php endif ?>
                    </ul>
                </div>
            </div>
        </div>
        <div id="divLine" ></div>
        <?php if (!isset($_SESSION["LoggedIn"])): ?>
            <div id='divLogin'>
                <div class='divContent'>
                    <form  method='post' action='login.php' name='Login'>
                        <table>
                            <tr><td>Username: </td><td><input type='text'     name='User'  /> <br /></td></tr>
                            <tr><td>Password: </td><td><input type='password' name='Pass'  /> <br /></td></tr>
                            <tr><td><input type='submit' value='Login' /></td><td><a class='LinkButton' href='register.php'>Register</a></td></tr>
                        </table>
                    </form>
                </div>
            </div>
        <?php endif ?>
        <div class="divContent">
            <img class="contentImg" src="Images/UserIcon.png" width="100" height="100"/>
            <h5>Name</h5>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec malesuada accumsan lectus, a interdum metus tincidunt in. 
               Donec eu nisl nibh. Nullam pulvinar molestie libero quis mollis. Cras et magna risus. Donec vel fermentum mauris. Sed urna odio, 
               tempus eget condimentum vel, semper eget nisi. Nam nec fringilla nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
               Proin libero dui, euismod pharetra lacus nec, aliquam interdum lacus. Fusce imperdiet arcu egestas, vehicula orci id, semper nunc. 
               Nulla nec laoreet nibh. Suspendisse blandit nisi at odio elementum, posuere dictum ligula tempus. 
            </p>
        </div>
        
        <div class="divContent">
            <h3>Parental Control Product.
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
            </h3>
        </div>
        <div id="divFooter"></div>
    </body>
</html>