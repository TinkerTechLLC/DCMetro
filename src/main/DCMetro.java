package main;

import java.io.FileWriter;
import java.io.IOException;
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DCMetro 
{		

	static String JSONFileLocation = "../data/station-data.json";
	static JSONParser parser = new JSONParser();
	
	private static List<MetroStation> stationList = new ArrayList<MetroStation>();
	private static List<MetroLine> lineList = new ArrayList<MetroLine>();
	
	public static void initalize() 
    {
		initLines();
		initStations();
		initPaths();
    }
	
	private static void initLines(){
		JSONArray lineArray = apiCall("https://api.wmata.com/Rail.svc/json/jLines", null, "Lines");

    	System.out.println("Lines retreived: " + lineArray.size());
        for(int i = 0; i < lineArray.size(); i++){
        	JSONObject lineInfo = (JSONObject) lineArray.get(i);
    		MetroLine newLine = new MetroLine(lineInfo);
    		lineList.add(newLine);
        }
	}
	
	private static void initStations(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LineCode", "");
        JSONArray stationArray = apiCall("https://api.wmata.com/Rail.svc/json/jStations", params, "Stations");
        
        // If information is available, create the list of metro station objects
        System.out.println("Stations retreived: " + stationArray.size());
        for(int i = 0; i < stationArray.size(); i++){
        	JSONObject thisStation = (JSONObject) stationArray.get(i);
    		MetroStation newStation = new MetroStation(thisStation);
    		stationList.add(newStation);
        }
        createGeoJSONFile();
    }
	
	private static void initPaths(){
		for(MetroLine line : lineList){
			String fromCode = line.getStartStationCode();
			String toCode = line.getEndStationCode();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("FromStationCode", fromCode);
			params.put("ToStationCode", toCode);
			JSONArray pathArray = apiCall("https://api.wmata.com/Rail.svc/json/jPath", params, "Path");
			line.setSegmentInfo(pathArray);
		}
		
	}
	
	private static JSONArray apiCall(String url, HashMap<String,String> params, String arrayName) {
		System.out.println("Performaing an API call!");
		JSONArray fetchedArray = null;
		HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(url);

            if(params != null){
	            for(int i = 0; i < params.size(); i++){
	            	String key = (String) params.keySet().toArray()[i];
	            	String value = (String) params.values().toArray()[i];
	            	System.out.println("Key: " + key + " val: " + value);
	            	builder.setParameter(key, value);
	            }
            }
            builder.setParameter("api_key", "bdb04a8a84d844abb6958c07735c4d2e");

            URI uri = builder.build();
            
            HttpGet request = new HttpGet(uri);
           
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            
	        if (entity != null) 
	        {
				try {
					fetchedArray = (JSONArray) ((JSONObject) parser.parse(EntityUtils.toString(entity))).get(arrayName);
				} catch (ParseException | org.json.simple.parser.ParseException | IOException e) {
					e.printStackTrace();
				}
	        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fetchedArray;
	}
	
	public static List<MetroStation> getStationList(){
		return stationList;
	}
	
	public static List<MetroLine> getLineList(){
		return lineList;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getGeoJSONObject(){
		JSONObject stationListObject = new JSONObject();
		JSONArray stationCollection = new JSONArray();
		stationListObject.put("type", "FeatureCollection");
		for(int i = 0; i < stationList.size(); i++){
			MetroStation thisStation = stationList.get(i);
			stationCollection.add(thisStation.getGeoJSONObject());
		}
		stationListObject.put("features", stationCollection);
		return stationListObject;
	}
	
	private static void createGeoJSONFile(){
		try {
			FileWriter file = new FileWriter(JSONFileLocation);
			file.write(DCMetro.getGeoJSONObject().toJSONString());
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getGeoJSONFileLocation(){
		createGeoJSONFile();
		return "metroStations.json";
	}
	
	public static RouteInfo getRouteInfo(MetroStation s0, MetroStation s1){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("FromStationCode", s0.getCode());
		params.put("ToStationCode", s1.getCode());
		JSONArray infoArray = DCMetro.apiCall("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo", 
				params, "StationToStationInfos");
		return new RouteInfo(infoArray);
	}
	
	public static class RouteInfo{
		String distance;
		String destination;
		String source;
		String peak;
		String offPeak;
		String discount;
		String time;
		
		RouteInfo(JSONArray infoArray){
			JSONObject info = (JSONObject) infoArray.get(0);
			this.distance = info.get("CompositeMiles").toString();
			this.destination = info.get("DestinationStation").toString();
			this.source = info.get("SourceStation").toString();
			this.time = info.get("RailTime").toString();
			JSONObject fares = (JSONObject) info.get("RailFare");
			this.offPeak = fares.get("OffPeakTime").toString();
			this.peak = fares.get("PeakTime").toString();
			this.discount = fares.get("SeniorDisabled").toString();
		}

		public String getDistance() {
			return distance;
		}

		public String getDestination() {
			return destination;
		}

		public String getSource() {
			return source;
		}

		public String getPeak() {
			return peak;
		}

		public String getOffPeak() {
			return offPeak;
		}

		public String getDiscount() {
			return discount;
		}

		public String getTime() {
			return time;
		}
	}

}
