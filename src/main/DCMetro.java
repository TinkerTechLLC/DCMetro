package main;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import location.Address;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;


@SuppressWarnings("serial")
public class DCMetro extends PApplet {
	static public void main(String args[]) {
		   PApplet.main(new String[] { "main.DCMetro" });
	}	
static String apiKey = "bdb04a8a84d844abb6958c07735c4d2e";
	
	// The map
	private UnfoldingMap map;
	
	public void setup(){        
		getStationData();	
		
		size(950, 600);
		map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		
	    map.zoomToLevel(5);
	    map.zoomAndPanTo(11, new Location(38.937676, -77.136483)); // Washington, DC
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();
	    
	    for(Station thisStation : Station.getList()){
	    	markers.add(createMarker(thisStation)); 
	    }
	    map.addMarkers(markers);
    }  
	
	public void draw(){
	    background(10);
	    map.draw();	    
	}
	
	private SimplePointMarker createMarker(Station station)
	{	
		int red = color(255, 0, 0);
		int yellow = color(255, 255, 0);
		int green = color(0, 255, 0);
		int blue = color(0, 0, 255);
		int silver = color(192, 192, 192);
		int orange = color(255, 140, 0);
		int rad = 10;
		
		SimplePointMarker thisMarker = new SimplePointMarker(station.getLocation());
		System.out.println("Color: " + station.getLineCode()[0]);
		if(station.getLineCode()[0].equals(Station.LineColor.BLUE)){
			thisMarker.setColor(blue);
			
		}
		else if(station.getLineCode()[0].equals(Station.LineColor.GREEN)){
			thisMarker.setColor(green);
		}
		else if(station.getLineCode()[0].equals(Station.LineColor.ORANGE)){
			thisMarker.setColor(orange);
		}
		else if(station.getLineCode()[0].equals(Station.LineColor.RED)){
			thisMarker.setColor(red);
		}			
		else if(station.getLineCode()[0].equals(Station.LineColor.SILVER)){
			thisMarker.setColor(silver);
		}
		else if(station.getLineCode()[0].equals(Station.LineColor.YELLOW)){
			thisMarker.setColor(yellow);
		}
		thisMarker.setRadius(rad);
		return thisMarker;
	}	
	
	private static void getStationData(){
		HttpClient httpclient = HttpClients.createDefault();
        String filePath = "";
        try
        {
            URIBuilder builder = new URIBuilder("https://api.wmata.com/Rail.svc/json/jStations");

            builder.setParameter("LineCode", "");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);

            // Request body
            request.setHeader("api_key", apiKey);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();          
            
            // Write the data to file
            InputStream is = entity.getContent();
            filePath = "stations.json";
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            int inByte;
            while((inByte = is.read()) != -1)
                 fos.write(inByte);
            is.close();
            fos.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        // Parse the saved data into a JSONObject     	
    	JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader(filePath));
			 System.out.println("Successfully parsed program object");
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		// Create the station objects from the JSON data
		if (jsonObject != null) 
		{
			JSONArray stations = (JSONArray) jsonObject.get("Stations");
			int count = stations.size();
			System.out.println(stations);
			System.out.println("Total DC stations: " + count);
		  
			for(int i = 0; i < count; i++){
			  	JSONObject thisStation = (JSONObject) stations.get(i);
			  	String code = thisStation.get("Code").toString();
				String name = thisStation.get("Name").toString();
				String stationTogether = thisStation.get("StationTogether1").toString();
				final int MAX_LINE_CODE = 4;
				String[] lineCode = {"", "", "", ""};
				for(int j = 0; j < MAX_LINE_CODE; j++){
					String key = "LineCode" + Integer.toString(j+1);
					System.out.println("Getting line code: " + key);
					try{
						lineCode[j] = thisStation.get(key).toString();
					}catch(NullPointerException e){
						break;
					}
				}
				Location location = new Location(((Number)thisStation.get("Lat")).doubleValue(), ((Number)thisStation.get("Lon")).doubleValue());
				JSONObject thisAddress = (JSONObject) thisStation.get("Address");
				String street = thisAddress.get("Street").toString();
				String city = thisAddress.get("City").toString();
				String state = thisAddress.get("State").toString();
				int zip = Integer.parseInt(thisAddress.get("Zip").toString());
				Address address = new Address(street, city, state, zip);
				new Station(code, name, stationTogether, lineCode, location, address);
			}
		}
		
		for(Station thisStation : Station.getList()){
			thisStation.printInfo();
			System.out.println("");
		}
	}
}
