package main;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;
import location.Address;

public class Station {
	static List<Station> list = new ArrayList<Station>();
	String code;	
	String name;	
	String stationTogether;
	String[] lineCode = new String[4];	
	Location geoLocation;
	Address address;
	
	public static class LineColor{
		final static public String 
			RED = "RD",
			YELLOW = "YL",
			GREEN = "GN",
			BLUE = "BL",
			ORANGE = "OR",
			SILVER = "SV";			
	}
	
	public Station(String code, String name, String stationTogether, String[] lineCode, Location geoLocation, Address address){
		this.code = code;
		this.name = name;
		this.stationTogether = stationTogether;
		this.lineCode = lineCode;
		this.geoLocation = geoLocation;
		this.address = address;
		list.add(this);
	}

	public static int getCount(){
		return list.size();
	}
	public static List<Station> getList(){
		return list;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getStationTogether() {
		return stationTogether;
	}

	public String[] getLineCode() {
		return lineCode;
	}

	public Location getLocation() {
		return geoLocation;
	}

	public Address getAddress() {
		return address;
	}
	public void printInfo(){
		System.out.println("Code: " + this.code);
		System.out.println("Name: " + this.name);
		System.out.println("Station Together: " + this.stationTogether);
		for(int i = 0; i < lineCode.length; i++){
			System.out.println("Line code " + i + ": " + this.lineCode[i]);
		}
		System.out.println("Geolocation -- Lat: " + this.geoLocation.getLat() + " Lon: " + this.geoLocation.getLon());
		this.address.printInfo();
	}	
	
}
