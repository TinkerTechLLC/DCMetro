package main;

import java.util.List;

import org.json.simple.JSONObject;

import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

import java.util.ArrayList;

public class MetroLine {
	List<MetroStation> stations = new ArrayList<MetroStation>();
	List<SimpleLinesMarker> segments = new ArrayList<SimpleLinesMarker>();
	String code;
	
    String displayName;
    String endStationCode;
    String internalDestination1;
    String internalDestination2;
    String lineCode;
    String startStationCode;
	
	public MetroLine(JSONObject lineInfo){
		setLineInfo(lineInfo);
	}
	
	public void setLineInfo(JSONObject info){
		System.out.println(info.toString());
        this.displayName = info.get("DisplayName").toString();
        this.endStationCode = info.get("EndStationCode").toString();
        this.internalDestination1 = info.get("InternalDestination1").toString();
        this.internalDestination2 = info.get("InternalDestination2").toString();
        this.lineCode = info.get("LineCode").toString();
        this.startStationCode = info.get("StartStationCode").toString();
	}
	
	public void addStation(MetroStation station){
		stations.add(station);
		refreshSegments();
	}
	
	private void refreshSegments(){
		
	}
	
}
