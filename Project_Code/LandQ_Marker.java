package module6;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

public class LandQuakeMarker extends EarthquakeMarker {
	public LandQuakeMarker(PointFeature quake) {
		super(quake);
		isOnLand = true;
	}
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.ellipse(x, y, 2*radius, 2*radius);
	}
	public String getCountry() {
		return (String) getProperty("country");
	}	
}