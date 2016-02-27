package main;

import java.util.List;

import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

import java.util.ArrayList;

public class MetroLine {
	List<MetroStation> stations = new ArrayList<MetroStation>();
	List<SimpleLinesMarker> segments = new ArrayList<SimpleLinesMarker>();
	String code;
	
	MetroLine(String code){
		this.code = code;
	}
	
	public void addStation(MetroStation station){
		stations.add(station);
		refreshSegments();
	}
	
	private void refreshSegments(){
		
	}
	
}
