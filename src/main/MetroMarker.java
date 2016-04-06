package main;

import processing.core.PConstants;
import processing.core.PGraphics;

public class MetroMarker extends CommonMarker {

	MetroStation station;
	MetroMarker thisMarker;
	
	public MetroMarker(MetroStation station) {
		super(station.getLocation());
		this.station = station;
		thisMarker = this;
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = station.getName();
		String clicked = this.getClicked() == true ? "Clicked" : "Not clicked";
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(name) + 6, 20, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(name, x + 3 , y + 18);
		
		
		pg.popStyle();
		
	}
	
	public MetroStation getStation(){
		return station;
	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// TODO Auto-generated method stub
		
	}
}
