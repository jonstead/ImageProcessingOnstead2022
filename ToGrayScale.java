import java.awt.Color;

public class ToGrayScale implements ColorToColor{
    @Override
    public Color toColor(Color color){
        int gray = (color.getRed() + color.getGreen() + color.getBlue())/3;
        return new Color(gray, gray, gray);
    }
    
}
