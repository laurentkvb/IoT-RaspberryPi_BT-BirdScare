<?php
$return_arr = array();


// $username="root";
// $password="raspberry";
$server = "localhost";
$database = "application";

mysql_connect($server, "root", "asdqwe123");

mysql_select_db($database);

$query = "SELECT id, date(time) as date, time(time) AS time FROM movement ORDER BY id DESC";
$result = mysql_query($query);


while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
	$row_array['ide'] = $row['id'];
	$row_array['time'] = $row['time'];
    $row_array['date'] = $row['date'];
   

    array_push($return_arr, $row_array);
}

 //echo "{
  //\"data\": [";
echo json_encode($return_arr);
//echo "]}";
?>
