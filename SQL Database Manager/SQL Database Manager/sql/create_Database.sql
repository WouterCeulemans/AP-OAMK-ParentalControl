CREATE TABLE `apps` (
  `ID` 			varchar(20) NOT NULL,
  `PackageName` varchar(50) NOT NULL,
  `Name` 		varchar(20) NOT NULL,
  `Visible` 	int 		NOT NULL,
  PRIMARY KEY (`ID`,`PackageName`)
);

CREATE TABLE `contacts` (
  `ID` 			varchar(20) NOT NULL,
  `Contact_ID` 	int 		NOT NULL,
  `SurName` 	varchar(20) NOT NULL,
  `Name` 		varchar(20) NOT NULL,
  `Number` 		varchar(13) NOT NULL,
  `TxtAmount` 	int,
  `TxtMax` 		int,
  `CallAmount` 	int,
  `CallMax` 	int,
  PRIMARY KEY (`ID`,`Contact_ID`)
);

CREATE TABLE `coordinates` (
  `ID` 			varchar(20) NOT NULL,
  `POS_ID` 		int 		NOT NULL,
  `LATITUDE` 	double 		NOT NULL,
  `LONGITUDE` 	double 		NOT NULL,
  PRIMARY KEY (`ID`,`POS_ID`)
);

CREATE TABLE `devices` (
  `ID` 			varchar(20) NOT NULL,
  `Name` 		varchar(20),
  `User_ID` 	int,
  PRIMARY KEY (`ID`)
);

CREATE TABLE `users` (
  `ID` 			int 		NOT NULL AUTO_INCREMENT,
  `username` 	varchar(20) NOT NULL,
  `password` 	varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
);
