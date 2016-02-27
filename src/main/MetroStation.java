package main;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import de.fhpotsdam.unfolding.geo.Location;

public class MetroStation {

	private JSONObject stationJSON;
	private String code;
	private String name;
	private String stationTogether1;
	private String stationTogether2;
	private String lineCode1;
	private String lineCode2;
	private String lineCode3;
	private String lineCode4;
	private double lat;
	private double lon;
	private Address address;
	public class Address{
		private String street;
		private String city;
		private String state;
		private int zip;
		
		public Address(String street, String city, String state, int zip){
			this.street = street;
			this.city = city;
			this.state = state;
			this.zip = zip;
		}
		
		public String getStreet() {
			return street;
		}
		public String getCity() {
			return city;
		}
		public String getState() {
			return state;
		}
		public int getZip() {
			return zip;
		}
		public void printInfo(){
			System.out.println("Address:\n" + street + "\n" + city + ", " + state + " " + zip);
		}
	}
	
	public static class LineColor{
		static String GREEN = "GR";
		static String BLUE = "BL";
		static String ORANGE = "OR";
		static String SILVER = "SV";
		static String YELLOW = "YL";
		static String RED = "RD";
	}
	
	public MetroStation(JSONObject station){
		this.stationJSON = station;
        this.code = station.get("Code").toString();
        this.name = station.get("Name").toString();
        this.stationTogether1 = (String) station.get("StationTogether1");
        this.stationTogether2 = (String) station.get("StationTogether2");
        this.lineCode1 = (String) station.get("LineCode1");
        this.lineCode2 = (String) station.get("LineCode2");
        this.lineCode3 = (String) station.get("LineCode3");
        this.lineCode4 = (String) station.get("LineCode4");
        this.lat = ((Number) station.get("Lat")).doubleValue();
        this.lon = ((Number) station.get("Lon")).doubleValue();
    	JSONObject address = (JSONObject) station.get("Address");
		String street = (String) address.get("Street");
		String city = (String) address.get("City");
		String state = (String) address.get("State");
		int zip = Integer.parseInt(address.get("Zip").toString());
		this.address = new Address(street, city, state, zip);
	}
	
	public MetroStation(String code, String name, String stationTogether1, String stationTogether2,
			String lineCode1, String lineCode2, String lineCode3, String lineCode4,
			double lat, double lon, String street, String city, String state, int zip){
		Address address = new Address(street, city, state, zip);
		init(code, name, stationTogether1, stationTogether2, lineCode1, lineCode2, lineCode3, lineCode4,
				lat, lon, address);
	}
	
	public MetroStation(String code, String name, String stationTogether1, String stationTogether2,
			String lineCode1, String lineCode2, String lineCode3, String lineCode4,
			double lat, double lon, Address address){
		init(code, name, stationTogether1, stationTogether2, lineCode1, lineCode2, lineCode3, lineCode4,
				lat, lon, address);
	}
	
	private void init(String code, String name, String stationTogether1, String stationTogether2,
			String lineCode1, String lineCode2, String lineCode3, String lineCode4,
			double lat, double lon, Address address){
		this.code = code;
		this.name = name;
		this.stationTogether1 = stationTogether1;
		this.stationTogether2 = stationTogether2;
		this.lineCode1 = lineCode1;
		this.lineCode2 = lineCode2;
		this.lineCode3 = lineCode3;
		this.lineCode4 = lineCode4;
		this.lat = lat;
		this.lon = lon;
		this.address = address;
	}
	
	public void printInfo(){
		System.out.println("Code: " + code);
		System.out.println("Name: " + name);
		System.out.println("StationTogether1: " + stationTogether1);
		System.out.println("StationTogether2: " + stationTogether2);
		System.out.println("lineCode1: " + lineCode1);
		System.out.println("lineCode2: " + lineCode2);
		System.out.println("LineCode3: " + lineCode3);
		System.out.println("lineCode4: " + lineCode4);
		System.out.println("Lat: " + lat);
		System.out.println("Lon: " + lon);
		this.address.printInfo();
	}
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getStationTogether1() {
		return stationTogether1;
	}
	public String getStationTogether2() {
		return stationTogether2;
	}
	public String getLineCode1() {
		return lineCode1;
	}
	public String getLineCode2() {
		return lineCode2;
	}
	public String getLineCode3() {
		return lineCode3;
	}
	public String getLineCode4() {
		return lineCode4;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public Address getAddress() {
		return address;
	}
	public Location getLocation(){
		return new Location(this.lat, this.lon);
	}
	@SuppressWarnings("unchecked")
	public JSONObject getGeoJSONObject(){
		
		JSONObject geoJSON = new JSONObject();
		JSONObject geometry = new JSONObject();
		JSONArray coordinates = new JSONArray();
		geoJSON.put("type", "Feature");
		geoJSON.put("properties", stationJSON);
		geometry.put("type", "Point");
		coordinates.put(this.lon);
		coordinates.put(this.lat);
		geometry.put("coordinates", coordinates);
		geoJSON.put("geometry", geometry);
		
		return geoJSON;
	}	
}