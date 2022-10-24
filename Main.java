import java.awt.Color;
// import java.awt.Graphics;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // new IP("download.jpg").onlyHighHue().save("downloadHighHue.png");
        AtomicInteger width = new AtomicInteger(-1);
        AtomicInteger height = new AtomicInteger(-1);

        new ICon("sheep.jpg")
        .exec(i->i.bitSlice(0b11011011))
        .save("sheepBitSlicing.png");

        new ICon("sheepBitSlicing.png")
        .setAsWidth(width)
        .setAsHeight(height)
        .addLayer("./Images/gradient.jpg")
        .setLayerAlpha(1f)
        .exec(l -> l.removeBlue())
        .setLayerBlendmode(BlendMode.Divide)
        .exec(ip -> ip.scaleLinear(width.get() / 255.0f, height.get() / 255.0f / 1.175f))
        .save("sheepBitSlicingBlueDarken.png");
        

        new IP("sheepBitSlicingBlueDarken.png").toHistogramHue(true).save("sheepBitSlicingBlueDarkenHistogramHue.png");
        new IP("sheepBitSlicingBlueDarken.png").toHistogramHueNullZero(true).save("sheepBitSlicingBlueDarkenHistogramHueNullZero.png");
        new IP("sheepBitSlicingBlueDarken.png").toHistogramValue(true).save("sheepBitSlicingBlueDarkenHistogramValue.png");
        
        new ICon("download.jpg")
        .exec(i->i.bitSlice(0b11011011))
        .save("downloadBitSlicing.png");

        new ICon("downloadBitSlicing.png")
        .setAsWidth(width)
        .setAsHeight(height)
        .addLayer("./Images/gradient.jpg")
        .setLayerAlpha(1f)
        .exec(l -> l.removeBlue())
        .setLayerBlendmode(BlendMode.Lighten)
        .exec(ip -> ip.scaleLinear(width.get() / 255.0f, height.get() / 255.0f / 1.175f))
        .save("downloadBitSlicingBlueDarken.png");
        
        new IP("downloadBitSlicingBlueDarken.png").toHistogramHue(true).save("sheepBitSlicingBlueDarkenHistogramHue.png");
        new IP("downloadBitSlicingBlueDarken.png").toHistogramHueNullZero(true).save("sheepBitSlicingBlueDarkenHistogramHueNullZero.png");
        new IP("downloadBitSlicingBlueDarken.png").toHistogramValue(true).save("downloadHistogramValue.png");
        
        new ICon("sheepBitSlicingBlueDarkenHistogramHueNullZero.png")
        .setAsWidth(width)
        .setAsHeight(height)
        .addToCanvasSize(0, 200)
        .addLayer("sheepBitSlicingBlueDarkenHistogramValue.png")
        .moveLayer(0, height.get())
        .exec(ip -> ip.scaleLinear(width.get() / 255.0f, 1))
        .setBackgroundColor(Color.WHITE)
        .save("sheepBitSlicingBlueDarkenHistogram.png"); 

        new ICon("sheepBitSlicingBlueDarken.png")
        .setAsWidth(width)
        .setAsHeight(height)
        .addToCanvasSize(0, 400)
        .addLayer("sheepBitSlicingBlueDarkenHistogram.png")
        .selectLayer(1)
        .moveLayer(0, height.get())
        .exec(ip -> ip.scaleLinear(width.get() / 360.0f, 1))
        .save("sheepBitSlicingBlueDarkenHistogram2.png"); 
}

    public static float interpolate(float one, float two, float percent){
        float value = (1 - percent) * one + (percent) * two;
        return value;
    }

    public static float[] rgbTohsv(int r, int g, int b){
        float cmax = Math.max(r, Math.max(g, b));
        float cmin = Math.min(r, Math.min(g, b));
        float diff = cmax - cmin;
    
        float h = 0;
    
        if (diff != 0) {
          if (cmax == r) {
            h = 0;
            h += 60 * (g - b) / diff;
          } else if (cmax == g) {
            h = 120;
            h += 60 * (b - r) / diff;
          } else {
            h = 240;
            h += 60 * (r - g) / diff;
          }
          while (h < 0)
            h += 360;
          h %= 360;
        }
    
        float s = 0;
    
        if (cmax > 0)
          s = diff / cmax * 255;
        float v = cmax;
    
        return new float[] { h, s, v };
      }
    
      public static float[] hsvToRgb(float hue, float saturation, float value) {
    
        saturation /= 255.0f;
        value /= 255.0f;
        int h = (int) (hue / 360.0f * 6);
        float f = hue / 360.0f * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);
    
        value *= 255.0f;
        f *= 255.0f;
        p *= 255.0f;
        q *= 255.0f;
        t *= 255.0f;
    
        switch (h) {
          case 0:
            return new float[] { value, t, p };
          case 1:
            return new float[] { q, value, p };
          case 2:
            return new float[] { p, value, t };
          case 3:
            return new float[] { p, q, value };
          case 4:
            return new float[] { t, p, value };
          case 5:
            return new float[] { value, p, q };
          default:
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", "
                + saturation + ", " + value);
        }
      }
    
      // public static void rgb_to_hsv(int r, int g, int b){
    
      //   float h = -1;
      //   float s = -1;
      //   float v = -1;
    
      //   v = Math.max(r, Math.max(g, b));
      //   int min = Math.min(r, Math.min(g, b));
      //   float diff = v - min;
    
      //   if (v == 0){
      //     s = 0;
      //   }
      //   else{
      //     s = diff/v;
      //   }
    
      //   s *= 255;
    
      //   if (r == v){
      //     h = 0;
      //     h += (g-b)/diff*60;
      //   }
      //   else if(g == v){
      //     h = 120;
      //     h += (b-r)/diff*60;
      //   }
      //   else{
      //     h = 240;
      //     h += (r-g)/diff*60;
      //   }
    
      //   System.out.println("h " + h + " s "+ s + " v " + v);
      // }
}