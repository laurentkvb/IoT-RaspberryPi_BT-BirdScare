var startDetectBtn = document.getElementById("start_detect_btn");
var stopDetectBtn = document.getElementById("stop_detect_btn");


var activeBtn = document.getElementById("motiondetector");

startDetectBtn.addEventListener("click", onStartDetectBtn);
stopDetectBtn.addEventListener("click", onStopDetectBtn);

function alertFunction()
{
		alert("Motion detection has started");
		console.log("calling amethod");
}



//Functions
function onStopDetectBtn() {
    $.ajax({
        type: "GET",
        url: "scripts/sudo-pi.php?action=2" ,
        success : function() {
			
			activeBtn.className = "label label-danger";
			activeBtn.innerHTML = "Inactive"
		
			
            console.log("Succesfully stopped detection");
        },
        error: function () {
            console.log("Fail");
        }
    });

}
function onStartDetectBtn() {

	var isRunning = "<?php echo $checking; ?>"   
            console.log("Succesfully started detection " + isRunning);
            
    $.ajax({
        type: "GET",
        url: "/cgi-bin/start-motion-detect.py" ,
        timeout: 1000,
        success : function() {
			alertFunction();
            console.log("Succesfully started detection");
        },
        error: function () {
			
			activeBtn.className = "label label-success";
			activeBtn.innerHTML = "Active"

            console.log("Succesfully started detection");        },
        
    });
}


