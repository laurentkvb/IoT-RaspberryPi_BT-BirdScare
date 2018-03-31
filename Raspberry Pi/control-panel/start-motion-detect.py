#!/usr/bin/python

import cgi
import cgitb
import RPi.GPIO as GPIO
import time
import datetime
import urllib
import os
cgitb.enable()

GPIO.setmode(GPIO.BCM)

PIR_PIN = 4
count = 1
start = time.time()
GPIO.setup(PIR_PIN, GPIO.IN)


def stopWatch(value):
    '''From seconds to Days;Hours:Minutes;Seconds'''

    valueD = (((value/365)/24)/60)
    Days = int (valueD)

    valueH = (valueD-Days)*365
    Hours = int(valueH)

    valueM = (valueH - Hours)*24
    Minutes = int(valueM)

    valueS = (valueM - Minutes)*60
    Seconds = int(valueS)


    print Days,";",Hours,":",Minutes,";",Seconds



try:
    print "Pir model tuse ctrl C to exit"
    time.sleep(2)
    print "ready"

    while True:
        if GPIO.input(PIR_PIN):
            count += 1
           # What in other posts is described is    
            end = time.time()               
            stopWatch(end-start)

            url = "http://localhost/control-panel/scripts/insert_detection.php";
            response = urllib.urlopen(url) #sends url request to another file responsible for sending GCM alerts
            os.system("python /home/pi/Desktop/send-weather_notification.py 1");
	    data = response.read()
            print "Motion detected {}".format(count)
        time.sleep(3)

except KeyboardInterrupt:
    print "Quit"
    GPIO.cleanup()

