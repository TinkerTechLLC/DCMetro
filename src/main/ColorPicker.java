package main;

public class ColorPicker {
	
	public static int RED;
	public static int YELLOW;
	public static int GREEN;
	public static int BLUE;
	public static int SILVER;
	public static int ORANGE;
	
	public static int getColor(String colorCode){
		if(colorCode.equals("RD")){
			return RED;
		}
		else if(colorCode.equals("BL")){
			return BLUE;
		}
		else if(colorCode.equals("OR")){
			return ORANGE;
		}
		else if(colorCode.equals("GR")){
			return GREEN;
		}
		else if(colorCode.equals("SV")){
			return SILVER;
		}
		else if(colorCode.equals("YL")){
			return YELLOW;
		}
		else 
			return RED;
	}
	
	public static void init(int r, int y, int g, int b, int s, int o){
		RED = r;
		YELLOW = y;
		GREEN = g;
		BLUE = b;
		SILVER = s;
		ORANGE = o;
	}
	
}
