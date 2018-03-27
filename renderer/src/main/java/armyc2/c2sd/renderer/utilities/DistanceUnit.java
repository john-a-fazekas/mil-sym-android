package armyc2.c2sd.renderer.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DistanceUnit
{
    public final double conversionFactor;
    public final String label;

    public DistanceUnit(double conversionFactor, String label){
        this.conversionFactor = conversionFactor;
        this.label = label;
    }

    public static DistanceUnit parse(String distanceUnitText){
        if(distanceUnitText == null){
            return null;
        }
        String[] parts = distanceUnitText.split(",");
        if(parts.length != 2){
            return null;
        }
        double conversionFactor = Double.parseDouble(parts[0].trim());
        String label = parts[1].trim();

        return new DistanceUnit(conversionFactor, label);
    }

    public String toAttribute(){
        return conversionFactor + "," + label;
    }

    public static DistanceUnit METERS = new DistanceUnit(1, "m.");
}