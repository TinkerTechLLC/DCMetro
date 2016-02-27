package main;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
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
	
    // The List you will populate with new SimplePointMarkers
    List<Marker> stationMarkers = new ArrayList<Marker>();
	
	public void setup(){        
		DCMetro.initalize();
		
		size(950, 600);
		map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		
	    map.zoomToLevel(5);
	    map.zoomAndPanTo(11, new Location(38.937676, -77.136483)); // Washington, DC
	    MapUtils.createDefaultEventDispatcher(this, map);	
	    
	    for(MetroStation thisStation : DCMetro.getStationList()){
	    	stationMarkers.add(createMarker(thisStation)); 
	    }
	    map.addMarkers(stationMarkers);
    }  
	
	public void draw(){
	    background(10);
	    map.draw();	    
	}
	
	private MetroMarker createMarker(MetroStation station)
	{	
		int red = color(255, 0, 0);
		int yellow = color(255, 255, 0);
		int green = color(0, 255, 0);
		int blue = color(0, 0, 255);
		int silver = color(192, 192, 192);
		int orange = color(255, 140, 0);
		int rad = 10;
		
		MetroMarker thisMarker = new MetroMarker(station);
		System.out.println("Color: " + station.getLineCode1());
		if(station.getLineCode1().equals(MetroStation.LineColor.BLUE)){
			thisMarker.setColor(blue);
			
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.GREEN)){
			thisMarker.setColor(green);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.ORANGE)){
			thisMarker.setColor(orange);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.RED)){
			thisMarker.setColor(red);
		}			
		else if(station.getLineCode1().equals(MetroStation.LineColor.SILVER)){
			thisMarker.setColor(silver);
		}
		else if(station.getLineCode1().equals(MetroStation.LineColor.YELLOW)){
			thisMarker.setColor(yellow);
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
		System.out.println("X: " + mouseX + " Y: " + mouseY);
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
