package main;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import main.DCMetro.RouteInfo;
import processing.core.PApplet;


@SuppressWarnings("serial")
public class Main extends PApplet {
	
	static public void main(String args[]) {
		   PApplet.main(new String[] { "main.DCMetro" });
	}
	
	static String apiKey = "bdb04a8a84d844abb6958c07735c4d2e";
	
	// The map
	private UnfoldingMap map;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	// Stations for queue
	MetroStation station0;
	MetroStation station1;
	RouteInfo routeInfo;
	
    // The List you will populate with new SimplePointMarkers
    List<Marker> stationMarkers = new ArrayList<Marker>();
	
	public void setup(){     
		initColors();
		DCMetro.initalize();
		
		size(950, 600);
		map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		
	    map.zoomToLevel(5);
	    map.zoomAndPanTo(11, new Location(38.937676, -77.136483)); // Washington, DC
	    MapUtils.createDefaultEventDispatcher(this, map);	
	    
	    for(MetroStation thisStation : DCMetro.getStationList()){
	    	stationMarkers.add(createMarker(thisStation)); 
	    }
	    for(MetroLine thisLine : DCMetro.getLineList()){
	    	for(SimpleLinesMarker thisMarker : thisLine.getSegmentMarkers()){
	    		map.addMarker(thisMarker);
	    	}
	    }
	    map.addMarkers(stationMarkers);
    }  
	
	public void draw(){
	    background(10);
	    map.draw();	    
	    addKey();
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 250, 240);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Route Info", xbase+25, ybase+25);
		
		fill(150, 30, 30);


		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		String name0 = station0 == null ? "" : station0.getName();
		String name1 = station1 == null ? "" : station1.getName();
		
		// If two stations are currently selected, generate an API call
		String peak = "";
		String offPeak = "";
		String discount = "";
		String time = "";
		String distance = "";
		if(routeInfo != null){
			peak = routeInfo.getPeak();
			offPeak = routeInfo.getOffPeak();
			discount = routeInfo.getDiscount();
			time = routeInfo.getTime();
			distance = routeInfo.getDistance();
		}
		text("Station 1: " + name0, xbase+25, ybase+65);
		text("Station 2: " + name1, xbase+25, ybase+85);
		text("Peak fare: $" + peak, xbase+25, ybase+125);
		text("Off-peak fare: $" + offPeak, xbase+25, ybase+145);
		text("Discount fare: $" + discount, xbase+25, ybase+165);
		text("Est. travel time: " + time + " min", xbase+25, ybase+205);
		text("Distance: " + distance + " miles", xbase+25, ybase+225);	
	}
	
	private void updateRouteInfo(){
		System.out.println("Updating route info");
		if(routeInfo == null && station0 != null && station1 != null){
			if(station0 == station1){
				System.out.println("Can't route to same station");
				return;
			}
			System.out.println("New API call");
			routeInfo = DCMetro.getRouteInfo(station0, station1);
		}
		else if(station1 == null){
			System.out.println("Clearing route info");
			routeInfo = null;
		}
	}
	
	private void initColors(){
		int red = color(255, 0, 0);
		int yellow = color(255, 255, 0);
		int green = color(0, 255, 0);
		int blue = color(0, 0, 255);
		int silver = color(192, 192, 192);
		int orange = color(232, 120, 0);
		ColorPicker.init(red, yellow, green, blue, silver, orange);
	}
	
	private MetroMarker createMarker(MetroStation station)
	{	
		int rad = 10;
		
		MetroMarker thisMarker = new MetroMarker(station);
		if(station.getLineCode1().equals(MetroStation.LineColor.BLUE)){
			thisMarker.setColor(ColorPicker.BLUE);
			
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.GREEN)){
			thisMarker.setColor(ColorPicker.GREEN);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.ORANGE)){
			thisMarker.setColor(ColorPicker.ORANGE);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.RED)){
			thisMarker.setColor(ColorPicker.RED);
		}			
		else if(station.getLineCode1().equals(MetroStation.LineColor.SILVER)){
			thisMarker.setColor(ColorPicker.SILVER);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.YELLOW)){
			thisMarker.setColor(ColorPicker.YELLOW);
		}
		thisMarker.setRadius(rad);
		return thisMarker;
	}	
	

	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(stationMarkers);
	}
	
	@Override
	public void mouseClicked(){
		queueMarker(stationMarkers);
		updateRouteInfo();
	}
	
	private void queueMarker(List<Marker> markers){
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker) m;
			if (marker.isInside(map,  mouseX+200, mouseY+51)) {
				MetroMarker metroMarker = (MetroMarker) marker;
				if(station0 == null && station1 == null){
					station0 = metroMarker.getStation();
				}
				else if(station0 != null && station1 == null){
					station1 = metroMarker.getStation();
				}
				else if(station0 !=null && station1 != null){
					station0 = metroMarker.getStation();
					station1 = null;
				}
				return;
			}
		}
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker) m;
			if (marker.isInside(map,  mouseX+200, mouseY+51)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
}
