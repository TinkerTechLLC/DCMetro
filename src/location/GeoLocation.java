package location;

public class GeoLocation {
	double lat;
	double lon;
	public GeoLocation(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}	
}
