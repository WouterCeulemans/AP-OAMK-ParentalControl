<?php
session_start();
if (!isset($_SESSION["LoggedIn"]))
    header("location: index.php?LoginError=99");
?>
<html>
	<head>
		<meta charset="utf-8" />
		<title>Parental Web Center</title>
		<link href="tracker.css" rel="stylesheet" type="text/css" />
		<link href="layout.css"  rel="stylesheet" type="text/css" />
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.js"></script>
		<script src="http://maps.google.com/maps/api/js?sensor=false"></script>  
		<script src="Tracker.js"></script>
	</head>

	<body>
        <div id="divHeader">
            <img src="Images/Header.png" width="500" height="200"/>
			<div id="divLine"></div>
			<div id="divLinks">
				<div id="divNav">
					<ul>
						<li><a href="Index.php" 					>Home</a></li>
						<li><a href="Settings.php"					>Settings</a></li>
						<li><a href="Spybot.php"					>Spybot</a></li>
						<li><a href="Tracker.php" class="current"	>Tracker</a></li>
					</ul>
				</div>
			</div>
			<div id="divLine"></div>
        </div>

        <div class="divContent"
            <table>
                <tr>
                    <td><input type="text" id='devid'/></td>
                    <td><input type="button" value="Send" style="margin: 0px;margin-bottom: 5px; margin-top: 5px;" onclick="GetCoordinates()"></td>
                </tr>
            </table>
            <p>Google maps </p>
            <div id="map_canvas"></div>
            </div>
        </div>
        <div id="divLine"></div>
        <div id="divFooter"></div>
	<body>
</html>
