import scala.util.parsing.json._

object Testing extends App {
  println("lool")
 val parsed =  JSON.parseFull("""{
	"TITLE": "LENSIM17: Vaikea",
    "AirportName": "Heathrow",
    "Country": "Great-Britain",
    "City": "London",
    "Description": "Airport in London",
    "RushFactor": 0.2,
    
    "Runways": [
    	{"number": 1, "length": 3900, "crosses": [2,3]},
    	{"number": 2, "length": 3750, "crosses": [1]}, 
    	{"number": 3, "length": 2000, "crosses": [1]}  
    ],
    
    "Gates" : 30,
    
    "LandQueues": [
    	{"Runway": 1},
    	{"Runway": 2},
    	{"Runway": 3}
    ],
    
    "InAirQueues": [
    	{"Altitude": 1000},
    	{"Altitude": 2000},
    	{"Altitude": 2700},
    	{"Altitude": 3400},
    	{"Altitude": 4000}
    ]
    
    
    
}""")
  println(parsed.get)
  println("Supa testing app")
}