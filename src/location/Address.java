package location;

public class Address {
	String street;
	String city;
	String state;
	int zip;
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
		System.out.println("Address: ");
		System.out.println("\tStreet: " + this.street);
		System.out.println("\tCity: " + this.city);
		System.out.println("\tState: " + this.state);
		System.out.println("\tZip: " + this.zip);
	}
}
