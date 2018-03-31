<?php
$return_arr = array();


// $username="root";
// $password="raspberry";
$server = "localhost";
$database = "application";

mysql_connect($server, "root", "asdqwe123");

mysql_select_db($database);

$query = "SELECT date(time) as time, COUNT(date(time)) as count  FROM movement GROUP BY date(time)";
$result = mysql_query($query);


while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
	$row_array['time'] = $row['time'];
    $row_array['count'] = $row['count'];
   

    array_push($return_arr, $row_array);
}
echo json_encode($return_arr);
?>
