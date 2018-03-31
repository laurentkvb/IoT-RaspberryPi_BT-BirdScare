<?php
	switch ($_GET['action']) {
		case '0':
				restart();
			break;

		case '1':
				shutdown();
			break;

		case '2':
		echo ' stopped motion detection';
				stopmotiondetection();
			break;

		default:
			header('Location: ../index.php');
			break;
	}

	function restart(){

			?>
			<?php

			system('sudo /sbin/shutdown -r now');

	}

	function shutdown(){
			?>
			
			<?php

			system('sudo /sbin/shutdown -h now');
	}

	function stopmotiondetection(){	
		
		
			?>
		
			<?php
		system('pkill -9 -f start-motion-detect.py');
		echo system('ps -ef | grep python');

	}
?>
