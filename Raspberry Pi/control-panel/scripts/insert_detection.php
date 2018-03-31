<?php

$username = "root";
$password = "asdqwe123";
$hostname = "localhost"; 
$parameter = $_GET['parameter']; 

//connection to the database
$dbhandle = mysql_connect($hostname, $username, $password) 
 or die("Unable to connect to MySQL");
echo "Connected to MySQL<br>";

//select a database to work with
$selected = mysql_select_db("application",$dbhandle)
  or die("Could not select examples");

//execute the SQL query and return records
//$registrationIdsDB = mysql_query("INSERT INTO `movement`(`enabled`) VALUES ('".$parameter."')");
$registrationIdsDB = mysql_query("INSERT INTO `movement` VALUES ()");

   
//close the connection
mysql_close($dbhandle);// prep the bundle
echo "Succesfully inserted motion detection";

?>
