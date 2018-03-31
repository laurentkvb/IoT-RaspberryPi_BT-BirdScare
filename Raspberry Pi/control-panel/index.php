<?php
ob_start();


define(LANGUAGE, isset($_GET["language"]) ? $_GET["language"] : "dutch");

$icon_language = LANGUAGE == "dutch" ? "NL" : "EN";

$temp = shell_exec('cat /sys/class/thermal/thermal_zone*/temp');
$temp = round($temp / 1000, 1);

$clock = shell_exec('cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq');
$clock = round($clock / 1000);

$voltage = shell_exec('/opt/vc/bin/vcgencmd measure_volts');
$voltage = explode("=", $voltage);
$voltage = $voltage[1];
$voltage = substr($voltage, 0, -2);

$cpuusage = 100 - shell_exec("vmstat | tail -1 | awk '{print $15}'");

$uptimedata = shell_exec('uptime');
$uptime = explode(' up ', $uptimedata);
$uptime = explode(',', $uptime[1]);
$uptime = $uptime[0];

$uptimedata = shell_exec('uptime');
$amountOfUsers = explode(' up ', $uptimedata);
$amountOfUsers = explode(',', $amountOfUsers[1]);
$amountOfUsers = $amountOfUsers[1];
$motionDetectActivate = shell_exec('ps -ef | grep python');
$piDate = shell_exec('date');

include 'localization/' . LANGUAGE . '.lang.php';

?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="images/icon.png">

    <title>RPI control panel by Laurent</title>


    <script src="dist/js/raphael.2.1.0.min.js"></script>
    <script src="dist/js/justgage.1.0.1.min.js"></script>
    <script src="vendor/jquery/jquery.min.js"></script>

    <script src="dist/js/sb-admin-2.js"></script>
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="stylesheets/main.css">
    <link rel="stylesheet" href="stylesheets/button.css">
    <link href="dist/css/sb-admin-2.css" rel="stylesheet">
    <link href="vendor/morrisjs/morris.css" rel="stylesheet">

    <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">


</head>

<body>

<script>
    function checkAction(action, e) {
        if (confirm('<?php echo TXT_CONFIRM; ?> ' + action + '?')) {
            return true;
        }
        else {
            return false;
        }
    }

    window.onload = doLoad;

    function doLoad() {
        setTimeout("refresh()", 20 * 1000);
    }

    function refresh() {
        window.location.reload(false);
    }


</script>


<div id="wrapper">
    <?php include("menu.html"); ?>

    <div id="page-wrapper">
        <br>

        <div class="row">
            <!--components-->
            <div class="col-lg-15 col-md-3">

                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: center">
                        <h2><?php echo TXT_COMPONENTS; ?></h2>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="list-group"

                        <div id="container">

                            <?php if (isset($temp) && is_numeric($temp)) { ?>
                                <br>
                                <div id="tempgauge"></div>
                                <script>
                                    var t = new JustGage({
                                        id: "tempgauge",
                                        value: <?php echo $temp; ?>,
                                        min: 0,
                                        max: 100,
                                        title: "<?php echo TXT_TEMPERATURE; ?>",
                                        label: "°C"
                                    });
                                </script>
                            <?php } ?>

                            <?php if (isset($voltage) && is_numeric($voltage)) { ?>
                                <div id="voltgauge"></div>
                                <script>
                                    var v = new JustGage({
                                        id: "voltgauge",
                                        value: <?php echo $voltage; ?>,
                                        min: 0.8,
                                        max: 1.4,
                                        title: "<?php echo TXT_VOLTAGE; ?>",
                                        label: "V"
                                    });
                                </script>
                            <?php } ?>

                            <?php if (isset($cpuusage) && is_numeric($cpuusage)) { ?>
                                <div id="cpugauge"></div>
                                <script>
                                    var u = new JustGage({
                                        id: "cpugauge",
                                        value: <?php echo $cpuusage; ?>,
                                        min: 0,
                                        max: 100,
                                        title: "<?php echo TXT_USAGE; ?>",
                                        label: "%"
                                    });
                                </script>
                            <?php } ?>

                            <?php if (isset($clock) && is_numeric($clock)) { ?>
                                <div id="clockgauge"></div>
                                <script>
                                    var c = new JustGage({
                                        id: "clockgauge",
                                        value: <?php echo $clock; ?>,
                                        min: 0,
                                        max: 1200,
                                        title: "<?php echo TXT_CLOCK; ?>",
                                        label: "MHz"
                                    });
                                </script>
                            <?php } ?>

                        </div>


                    </div>


                </div>

            </div>
            <!--pi information-->
            <div class="col-lg-15 col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h1><?php echo TXT_PI_INFO; ?></h1>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="list-group">
                            <?php if (isset($uptime)) { ?>
                                <div id="uptime">
                                    <b><?php echo TXT_RUNTIME; ?></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<?php echo $uptime; ?>
                                    <span STYLE="font-size: 11px;">(hh:mm)</span>
                                </div>

                                <div id="uptime">
                                    <b><?php echo TXT_PI_USERS_ACTIVE; ?></b>&nbsp;&nbsp;&nbsp;<?php echo $amountOfUsers; ?>
                                </div>

                                <div id="uptime">
                                    <b><?php echo TXT_PI_CURRENT_TIME; ?></b>&nbsp;&nbsp;&nbsp;&nbsp;<?php echo $piDate; ?>
                                </div>
                            <?php } ?>
                        </div>
                    </div>
                </div>

            </div>
            <!--controls-->
            <div class="col-lg-15 col-md-8">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h1><?php echo TXT_CONTROLS; ?></h1>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="list-group">
                            <div id="controls" style="margin: auto; width: 100%;">
                                <h3>&nbsp;<?php echo TXT_CONTROLS_HEAD; ?></h3>

                                <a class="btn btn-danger btn-lg" href="scripts/sudo-pi.php?action=1"
                                   onclick="return checkAction('<?php echo TXT_SHUTDOWN_1; ?>');"><?php echo TXT_SHUTDOWN_2; ?></a>


                                <a class="btn btn-warning btn-lg" href="scripts/sudo-pi.php?action=0"
                                   onclick="return checkAction('<?php echo TXT_RESTART_1; ?>', 'test');"><?php echo TXT_RESTART_2; ?></a><br/>


                            </div>
                            <hr/>

                            <h3>&nbsp;<?php echo TXT_CONTROLS_PI; ?></h3>
                            <?php if (strpos($motionDetectActivate, 'start-motion-detect.py')) { ?>
                                <p>&nbsp; <?php echo TXT_CONTROLS_STATUS; ?> <span id="motiondetector"
                                                                                   class="label label-success"><?php echo TXT_CONTROLS_ACTIVE; ?></span>
                                </p>
                            <?php } else { ?>
                                <p>&nbsp; <?php echo TXT_CONTROLS_STATUS; ?> <span id="motiondetector"
                                                                                   class="label label-danger"><?php echo TXT_CONTROLS_INACTIVE; ?></span>
                                </p>
                            <?php } ?>

                            <button class="btn btn-success btn-lg"
                                    id="start_detect_btn"><?php echo TXT_CONTROLS_START_SENSOR; ?></button>
                            <button class="btn btn-danger btn-lg"
                                    id="stop_detect_btn"><?php echo TXT_CONTROLS_STOP_SENSOR; ?></button>


                        </div>
                    </div>
                </div>
                <!-- /.col-lg-4 -->
            </div>
            <!--motions detections table-->
            <div class="col-lg-15 col-md-11">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h1><?php echo TXT_MOTION_HEADER_TABLE; ?></h1>
                    </div>
                    <!-- /.panel-heading -->
                    <div class="panel-body">
                        <div class="list-group">

                            <div id="content">

                                <table id="motion-table" class="table table-striped table bordered" cellspacing="0"
                                       width="100%">
                                    <div class="table responsive">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th><?php echo TXT_MOTION_DATE; ?></th>
                                            <th><?php echo TXT_MOTION_TIME; ?></th>
                                        </tr>
                                        </thead>

                                    </div>

                            </div>
                            </table>

                        </div>
                    </div>
                </div>
                <!-- /.col-lg-4 -->
            </div>
            <!--motions detections frequency-->
            <div class="col-lg-13 ">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <i class="fa fa-bar-chart-o fa-fw"></i>
                        <h1><?php echo TXT_MOTION_HEADER_GRAPH; ?></h1>
                    </div>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-8">
                                <div id="morris-bar-chart"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>


        <script src="scripts/onclick.js"></script>


        <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
        <script src="vendor/metisMenu/metisMenu.min.js"></script>


        <script src="vendor/datatables/js/jquery.dataTables.min.js"></script>
        <script src="vendor/datatables-plugins/dataTables.bootstrap.min.js"></script>


        <script src="vendor/morrisjs/morris.min.js"></script>

        <script src="scripts/motion-detect_data.js"></script>

        <script>
            $(document).ready(function () {
                $('#motion-table').DataTable({
                    "order": [[0, "desc"]],
                    "language": {
                        "lengthMenu": "<?php echo TXT_TABLE_PER_PAGE; ?>",
                        "zeroRecords": "<?php echo TXT_TABLE_NOTHING_FOUND; ?>",
                        "info": "<?php echo TXT_TABLE_INFO; ?>",
                        "infoEmpty": "<?php echo TXT_TABLE_INFO_EMPTY; ?>",
                        "search": "<?php echo TXT_TABLE_SEARCH; ?>",
                        "infoFiltered": "<?php echo TXT_TABLE_INFO_FILTERED; ?>"
                    },
                    "ajax": {
                        "url": "/control-panel/scripts/get-all-movements.php",
                        "dataSrc": ""

                    }, "columns": [
                        {"data": "ide"},
                        {"data": "date"},
                        {"data": "time"}
                    ]

                });
            });


        </script>

</body>
</html>
