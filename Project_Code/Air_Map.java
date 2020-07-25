package module6;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PApplet;

public class AirportMap extends PApplet {
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	public void setup() {
		size(800,600, OPENGL);
		map = new UnfoldingMap(this, 150, 50, 950, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			if(Float.parseFloat(feature.getProperty("altitude").toString())>4000){
				airportList.add(m);
				airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			}	
		}
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			routeList.add(sl);
		}
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
	}
	public void draw() {
		background(0);
		map.draw();
		addKey();
	}
	private void addKey() 
	{	
		fill(153);
		rect(10, 250, 150, 150);
		textSize(18);
		fill(0, 102, 153, 51);
		text("Airport key", 15, 270);
		fill(color(255,0,0));
		ellipse(30, 290, 18, 18);
		textSize(12);
		text("4000+ magnitude", 50, 290);
		fill(color(255, 255, 0));
		rect(20, 320, 12, 12);
		textSize(12);
		text("5000+ magnitude", 50, 330);
		fill(color(0,0,255));
		triangle(25, 360, 30, 350,35,360);
		textSize(12);
		text("6000+ magnitude", 50, 360);
	}
	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}
	}

}