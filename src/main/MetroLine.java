package main;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

import java.util.ArrayList;

public class MetroLine {
	List<MetroStation> stations = new ArrayList<MetroStation>();
	List<MetroSegment> segments = new ArrayList<MetroSegment>();
	List<SimpleLinesMarker> segmentMarkers = new ArrayList<SimpleLinesMarker>();
	
	
	String code;
	
    String displayName;
    String endStationCode;
    String internalDestination1;
    String internalDestination2;
    String lineCode;
    String startStationCode;
    
    public class MetroSegment{
        private String distanceToPrev;
        private String lineCode;
        private int seqNum;
        private String stationCode;
        private String stationName;
        
        private MetroSegment(JSONObject info){
        	this.distanceToPrev = info.get("DistanceToPrev").toString();
        	this.lineCode = info.get("LineCode").toString();
        	this.seqNum = Integer.parseInt(info.get("SeqNum").toString());
        	this.stationCode = info.get("StationCode").toString();
        	this.stationName = info.get("StationName").toString();
        }

		public String getDistanceToPrev() {
			return distanceToPrev;
		}

		public String getLineCode() {
			return lineCode;
		}

		public int getSeqNum() {
			return seqNum;
		}

		public String getStationCode() {
			return stationCode;
		}

		public String getStationName() {
			return stationName;
		}
    }
	
	public MetroLine(JSONObject lineInfo){
		setLineInfo(lineInfo);
	}
	
	public void setSegmentInfo(JSONArray info){
		System.out.println("Segments for this line: " + info.size());
		for(int i = 0; i < info.size(); i++){
			segments.add(new MetroSegment((JSONObject)info.get(i)));
		}
		initSegmentMarkers();
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
	
	private void initSegmentMarkers(){
		for(int i = 0; i < segments.size()-1; i++){
			
			String code0 = segments.get(i).getStationCode();
			String code1 = segments.get(i+1).getStationCode();
			MetroStation s0 = null;
			MetroStation s1 = null;
			for(int j = 0; j < DCMetro.getStationList().size(); j++){
				// Stop searching once both stations are found
				if(s0 != null && s1 != null){
					break;
				}
				MetroStation thisStation = DCMetro.getStationList().get(j);
				if(thisStation.getCode().equals(code0)){
					s0 = thisStation;
					continue;
				}
				else if(thisStation.getCode().equals(code1)){
					s1 = thisStation;
					continue;
				}
			}
			SimpleLinesMarker thisSegMarker = new SimpleLinesMarker(s0.getLocation(), s1.getLocation());
			thisSegMarker.setColor(ColorPicker.getColor(this.getLineCode()));
			thisSegMarker.setStrokeWeight(3);
			segmentMarkers.add(thisSegMarker);
		}
	}
	
	public void addStation(MetroStation station){
		stations.add(station);
		refreshSegments();
	}
	
	private void refreshSegments(){
		
	}

	public List<MetroSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<MetroSegment> segments) {
		this.segments = segments;
	}

	public List<MetroStation> getStations() {
		return stations;
	}
	
	public List<SimpleLinesMarker> getSegmentMarkers(){
		return segmentMarkers;
	}

	public String getCode() {
		return code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEndStationCode() {
		return endStationCode;
	}

	public String getInternalDestination1() {
		return internalDestination1;
	}

	public String getInternalDestination2() {
		return internalDestination2;
	}

	public String getLineCode() {
		return lineCode;
	}

	public String getStartStationCode() {
		return startStationCode;
	}
	
}
