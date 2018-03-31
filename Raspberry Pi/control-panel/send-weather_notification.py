import requests
import pyowm
import os

owm = pyowm.OWM('430b20589b2b9e1b39345453656036dc') 
def email_alert(first, second, third):
    report = {}
    report["value1"] = first
    report["value2"] = second
    report["value3"] = third
    requests.post("https://maker.ifttt.com/trigger/motion_detected/with/key/bvAWkEgGIN343juXcALFpW", data=report)    

# Search for current weather in London (UK)
observation = owm.weather_at_place('DenHelder,netherlands')
w = observation.get_weather()
print(w)                      # <Weather - reference time=2013-12-18 09:20,
                              # status=Clouds>

# Weather details
w.get_wind()                  # {'speed': 4.6, 'deg': 330}
w.get_humidity()              # 87
w.get_temperature('celsius')
a = w.get_temperature('celsius')
b = "c"
c = "c"

forecast = owm.daily_forecast("DenHelder,Netherlands")
tomorrow = pyowm.timeutils.tomorrow()
b = forecast.will_be_sunny_at(tomorrow)

email_alert(str(a['temp']) + " degree", b, c)
