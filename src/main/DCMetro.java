package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
	
	private static List<MetroStation> stationList = new ArrayList<MetroStation>();
	
	public static void initalize() 
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.wmata.com/Rail.svc/json/jStations");

            builder.setParameter("LineCode", "");
            builder.setParameter("api_key", "bdb04a8a84d844abb6958c07735c4d2e");

            URI uri = builder.build();
            
            HttpGet request = new HttpGet(uri);
            
            JSONParser parser = new JSONParser();
           
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            
           
            
            // If information is available, create the list of metro station objects
            if (entity != null) 
            {
            	JSONArray stationArray = (JSONArray) ((JSONObject) parser.parse(EntityUtils.toString(entity))).get("Stations");
            	System.out.println("Stations retreived: " + stationArray.size());
            	
                for(int i = 0; i < stationArray.size(); i++){
                	JSONObject thisStation = (JSONObject) stationArray.get(i);
            		MetroStation newStation = new MetroStation(thisStation);
            		stationList.add(newStation);
                }
            }	
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        createGeoJSONFile();
    }

	public static List<MetroStation> getStationList(){
		return stationList;
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

}
