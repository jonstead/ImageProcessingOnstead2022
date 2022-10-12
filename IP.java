import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.Font;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class IP {

    BufferedImage bufferedImage;

    public IP(String filename){
        try {
            this.bufferedImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IP(BufferedImage bi) {
        super();
        this.bufferedImage = bi;
    }

    public IP clone(){
        BufferedImage bi = deepCopy(bufferedImage);
        return new IP(bi);
    }

    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public IP doubleLoop(ColorToColor lambda){
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));

                Color newColor = lambda.toColor(color);

                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return this;
    }

    public IP toGrayScale2(){
        return doubleLoop(new ToGrayScale());
    }

    public IP toGrayscale(){
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                int average = (color.getRed() + color.getGreen() + color.getBlue())/3;
                Color newColor = new Color(average, average, average);

                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return this;
    }

    public IP toSepia(){
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                int R = color.getRed();
                int G = color.getGreen();
                int B = color.getBlue();
                int newRed = (int)(0.393*R + 0.769*G + 0.189*B);
                int newGreen = (int)(0.349*R + 0.686*G + 0.168*B);
                int newBlue = (int)(0.272*R + 0.534*G + 0.131*B);
                if(newRed > 255) newRed = 255;
                if(newGreen > 255) newGreen = 255;
                if(newBlue > 255) newBlue = 255;
                Color newColor = new Color(newRed, newGreen, newBlue);

                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return this;
    }

    public IP toNegative(){
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                int R = 255 - color.getRed();
                int G = 255 - color.getGreen();
                int B = 255 - color.getBlue();
                Color newColor = new Color(R, G, B);

                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return this;
    }

    public IP mirrorImage(){
        int bw = bufferedImage.getWidth();
        int bh = bufferedImage.getHeight();
        BufferedImage intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bh; y++){
            for(int x = 0; x < bw; x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                intermediate.setRGB(bw - x - 1, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP flipSide(){
        int bw = bufferedImage.getWidth();
        int bh = bufferedImage.getHeight();
        BufferedImage intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bh; y++){
            for(int x = 0; x < bw; x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                intermediate.setRGB(x, bh - y - 1, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP rotate180(){
        BufferedImage intermediate = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                
                // int halfWidth = bufferedImage.getWidth()/2;
                // int halfHeight = bufferedImage.getHeight()/2;

                int x1 = bufferedImage.getWidth() - x - 1;
                int y1 = bufferedImage.getHeight() - y - 1;

                
                intermediate.setRGB(x1, y1, color.getRGB());
            }
        }

        bufferedImage = intermediate;
        return this;
    }

    public IP rotate90(){
        BufferedImage intermediate = new BufferedImage(bufferedImage.getHeight(), bufferedImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                
                int y1 = bufferedImage.getWidth() - x - 1;
                int x1 = bufferedImage.getHeight() - y - 1;

                
                intermediate.setRGB(x1, y1, color.getRGB());
            }
        }

        bufferedImage = intermediate;
        return this;
    }

    public IP translateForward(int dx, int dy){
        int bw = bufferedImage.getWidth();
        int bh = bufferedImage.getHeight();
        BufferedImage intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);


        Graphics g = intermediate.getGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0,0,bw,bh);
        
        for(int y = 0; y < bh; y++){
            for(int x = 0; x < bw; x++){
               
                Color color = new Color(bufferedImage.getRGB(x, y));
                
                int y1 = y + dy;
                int x1 = x + dx;

                if( x1 >= bw || y1 >= bh || x1 < 0 || y1 < 0)
                    continue;

                
                intermediate.setRGB(x1, y1, color.getRGB());
            }
        }

        bufferedImage = intermediate;
        return this;
    }

    public IP translate(int dx, int dy){
        int bw = bufferedImage.getWidth();
        int bh = bufferedImage.getHeight();
        BufferedImage intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Graphics g = intermediate.getGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0,0,bw,bh);
        
        for(int y = 0; y < bh; y++){
            for(int x = 0; x < bw; x++){
                int originalX = x - dx;
                int originalY = y - dy;
               

                Color color;
                if( originalX >= bw || originalY >= bh || originalX < 0 || originalY < 0)
                    color = Color.MAGENTA;

                else
                    color = new Color(bufferedImage.getRGB(originalX, originalY));
                
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP scale(float scale){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var nw = (int)(bw * scale + .5);
        var nh = (int)(bh * scale + .5);
        var intermediate = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        
        for(var y = 0; y < nh; y++){
            for(var x = 0; x < nw; x++){
                float originalX = (x / scale);
                float originalY = (y / scale);
                

                
                Color color;
                if( originalX >= bw || originalY >= bh || originalX < 0 || originalY < 0)
                    color = Color.MAGENTA;

                else{
                    var color00 = new Color(bufferedImage.getRGB((int)originalX, (int)originalY));
                    Color color10;
                    Color color01;
                    Color color11;
                    if (originalX + 1 < bw)
                        color10 = new Color(bufferedImage.getRGB((int)originalX + 1, (int)originalY));
                    else
                        color10 = Color.WHITE;    
                    if (originalY + 1 < bh)
                        color01 = new Color(bufferedImage.getRGB((int)originalX, (int)originalY + 1));
                    else
                        color01 = Color.WHITE;
                    if (originalX + 1 < bw && originalY + 1 < bh)
                        color11 = new Color(bufferedImage.getRGB((int)originalX + 1, (int)originalY + 1));
                    else
                        color11 = Color.WHITE;
                    float percentX = originalX - (int)originalX;
                    float percentY = originalY - (int)originalY;
                    
                    var interpolationX1 = interpolate(color00, color10, percentX);
                    var interpolationX2 = interpolate(color01, color11, percentX);

                    var interpolationY = interpolate(interpolationX1, interpolationX2, percentY);
                    color = interpolationY;
                }
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP scaleLinear(float scale) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var nw = (int) (bw * scale + .5);
        var nh = (int) (bh * scale + .5);
        var intermediate = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < nh; y++) {
            for (var x = 0; x < nw; x++) {

                float originalX = (x / scale);
                float originalY = (y / scale);

                Color color;
                if (originalX >= bw || originalY >= bh || originalX < 0 || originalY < 0)
                    color = Color.MAGENTA;

                else {
                    var color00 = new Color(bufferedImage.getRGB((int) originalX, (int) originalY));
                    Color color10;
                    Color color01;
                    Color color11;

                    if(originalX + 1 <bw)
                        color10 = new Color(bufferedImage.getRGB((int) originalX + 1, (int) originalY));
                    else
                        color10 = Color.WHITE;
                    if(originalY +1 < bh){
                        color01 = new Color(bufferedImage.getRGB((int) originalX, (int) originalY + 1));
                    }
                    else
                        color01 = Color.WHITE;

                    if(originalX + 1< bw && originalY +1 < bh)
                        color11 = new Color(bufferedImage.getRGB((int) originalX + 1, (int) originalY + 1));
                    else{
                        color11 = Color.WHITE;
                    }

                    float percentX = originalX - (int) originalX;
                    float percentY = originalY - (int) originalY;

                    var interpolationX1 = interpolate(color00, color10, percentX);
                    var interpolationX2 = interpolate(color01, color11, percentX);

                    var interpolationY = interpolate(interpolationX1, interpolationX2, percentY);
                    color = interpolationY;
                }
                intermediate.setRGB(x, y, color.getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP scaleLinear(float xs, float ys) {

        var _bw = bufferedImage.getWidth();
        var _bh = bufferedImage.getHeight();
        var nw = (int) (_bw * xs + .5);
        var nh = (int) (_bh * ys + .5);
        return updateImageAndSize(nw, nh, (bi, bw, bh, x, y) -> {

            float originalX = (x / xs);
            float originalY = (y / ys);

            Color color;
            if (!MyMath.inBounds(bw, bh, (int) originalX, (int) originalY))
                color = Color.MAGENTA;
            else {
                color = getBilinear(bufferedImage, originalX, originalY, Color.WHITE);
            }
            return color;
        });
    }

    protected Color getBilinear(BufferedImage inImage, float x, float y, Color border) {
        var color00 = new Color(bufferedImage.getRGB((int) x, (int) y));
        Color color10;
        Color color01;
        Color color11;
    
        if ((int) x + 1 < inImage.getWidth())
          color10 = new Color(bufferedImage.getRGB((int) x + 1, (int) y));
        else
          color10 = Color.WHITE;
        if (y + 1 < inImage.getHeight()) {
          color01 = new Color(bufferedImage.getRGB((int) x, (int) y + 1));
        } else
          color01 = Color.WHITE;
    
        if (x + 1 < inImage.getWidth() && y + 1 < inImage.getHeight())
          color11 = new Color(bufferedImage.getRGB((int) x + 1, (int) y + 1));
        else {
          color11 = Color.WHITE;
        }
    
        float percentX = x - (int) x;
        float percentY = y - (int) y;
    
        var interpolationX1 = interpolate(color00, color10, percentX);
        var interpolationX2 = interpolate(color01, color11, percentX);
    
        var interpolationY = interpolate(interpolationX1, interpolationX2, percentY);
        return interpolationY;
      }

    public IP updateImageAndSize(int nw, int nh, IUpdateLambda lambda) {
        BufferedImage intermediate = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < nh; y++) {
          for (int x = 0; x < nw; x++) {
            Color color = lambda.toColor(bufferedImage, nw, nh, x, y);
    
            intermediate.setRGB(x, y, color.getRGB());
          }
        }
    
        bufferedImage = intermediate;
        return new IP(this);
    }

    public IP(IP base) {
        super();
        this.bufferedImage = base.bufferedImage;
    }

    public IP scaleNN(float scale) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var nw = (int) (bw * scale + .5);
        var nh = (int) (bh * scale + .5);
        var intermediate = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < nh; y++) {
            for (var x = 0; x < nw; x++) {

                var originalX = (int) (x / scale);
                var originalY = (int) (y / scale);

                Color color;
                if (originalX >= bw || originalY >= bh || originalX < 0 || originalY < 0)
                    color = Color.MAGENTA;

                else
                    color = new Color(bufferedImage.getRGB(originalX, originalY));

                intermediate.setRGB(x, y, color.getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    private float length(float x, float y){
        return (float)(Math.sqrt(x*x+y*y));
    }

    private float getAngle(float x, float y){
        return (float)Math.atan2(y, x);
    }

    private float getX(float distance, float angle){
        return (float)Math.cos(angle) * distance;
    }
    
    private float getY(float distance, float angle){
        return (float)Math.sin(angle) * distance;
    }
    
    // public IP rotate(float degrees, boolean linearInterpolation) {
    //     var bw = bufferedImage.getWidth();
    //     var bh = bufferedImage.getHeight();
    //     var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

    //     for (var y = 0; y < bh; y++) {
    //         for (var x = 0; x < bw; x++) {

    //             // Rotations
    //             /*
    //              * 1 - Radians or degrees? Degrees
    //              * 2 - What are we rotating about? Upper left-hand
    //              * 3 - Rotate up or down? Down (e.g. positive rotations are clockwise)
    //              */

    //             float radians = (float) (degrees / 360 * Math.PI * 2);
    //             float r = length(x, y);
    //             float theta = getAngle(x, y);

    //             float newAngle = theta - radians;
    //             float newX = getX(r, newAngle);
    //             float newY = getY(r, newAngle);

    //             Color color;
    //             if (newX >= bw || newY >= bh || newX < 0 || newY < 0)
    //                 color = Color.MAGENTA;
    //             else {
    //                 var interpolationY = getBilinear(bufferedImage, newX, newY, Color.WHITE);
    //                 if (linearInterpolation)
    //                     color = interpolationY;
    //                 else
    //                     color = getNN(bufferedImage, (int) newX, (int) newY, Color.WHITE);
    //             }
    //             intermediate.setRGB(x, y, color.getRGB());
    //         }
    //     }

    //     bufferedImage = intermediate;

    //     return this;
    // }

    


    public IP rotateCenter(float degrees){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                /*
                Rotations
                Radians or degrees? - Degrees
                What are we rotating about? - Default is center, but start at upper left hand
                Rotate up or down? - Down - Positive = Clockwise
                */

                float radians = (float)(degrees / 360 * 2 * Math.PI);
                
                float offsetX = x - bw/2;
                float offsetY = y - bh/2;
                
                float radius = length(offsetX, offsetY);
                float theta = getAngle(offsetX, offsetY);

                float newAngle = theta + radians;
                float newX = getX(radius, newAngle) + bw/2;
                float newY = getY(radius, newAngle) + bh/2;

                Color color;
                if (newX >= bw || newY >= bh || newX < 0 || newY < 0){
                    Random obj = new Random();
                    int rand_num = obj.nextInt(0xffffff + 1);
                    String colorCode = String.format("#%06x", rand_num);
                    color = Color.decode(colorCode);
                }
                else {
                    var color00 = new Color(bufferedImage.getRGB((int) newX, (int) newY));
                    Color color10;
                    Color color01;
                    Color color11;

                    if(newX + 1 <bw)
                        color10 = new Color(bufferedImage.getRGB((int) newX + 1, (int) newY));
                    else
                        color10 = Color.WHITE;
                    if(newY +1 < bh){
                        color01 = new Color(bufferedImage.getRGB((int) newX, (int) newY + 1));
                    }
                    else
                        color01 = Color.WHITE;

                    if(newX + 1< bw && newY +1 < bh)
                        color11 = new Color(bufferedImage.getRGB((int) newX + 1, (int) newY + 1));
                    else{
                        color11 = Color.WHITE;
                    }

                    float percentX = newX - (int) newX;
                    float percentY = newY - (int) newY;

                    var interpolationX1 = interpolate(color00, color10, percentX);
                    var interpolationX2 = interpolate(color01, color11, percentX);

                    var interpolationY = interpolate(interpolationX1, interpolationX2, percentY);
                    color = interpolationY;
                }
                intermediate.setRGB(x, y, color.getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP rotateGrayScale(float degrees){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                /*
                Rotations
                Radians or degrees? - Degrees
                What are we rotating about? - Default is center, but start at upper left hand
                Rotate up or down? - Down - Positive = Clockwise
                */

                float radians = (float)(-degrees / 360 * 2 * Math.PI);
                float radius = length(x, y);
                float theta = getAngle(x, y);

                float newAngle = theta + radians;
                float newX = getX(radius, newAngle);
                float newY = getY(radius, newAngle);

                Color color;
                if (newX >= bw || newY >= bh || newX < 0 || newY < 0){
                    Random obj = new Random();
                    int rand_num = obj.nextInt(0xffffff + 1);
                    String colorCode = String.format("#%06x", rand_num);
                    color = Color.decode(colorCode);
                    int average = (color.getRed() + color.getGreen() + color.getBlue())/3;
                    Color newColor = new Color(average, average, average);
                    color = newColor;
                }
                else {
                    var color00 = new Color(bufferedImage.getRGB((int) newX, (int) newY));
                    Color color10;
                    Color color01;
                    Color color11;

                    if(newX + 1 <bw)
                        color10 = new Color(bufferedImage.getRGB((int) newX + 1, (int) newY));
                    else
                        color10 = Color.WHITE;
                    if(newY +1 < bh){
                        color01 = new Color(bufferedImage.getRGB((int) newX, (int) newY + 1));
                    }
                    else
                        color01 = Color.WHITE;

                    if(newX + 1< bw && newY +1 < bh)
                        color11 = new Color(bufferedImage.getRGB((int) newX + 1, (int) newY + 1));
                    else{
                        color11 = Color.WHITE;
                    }

                    float percentX = newX - (int) newX;
                    float percentY = newY - (int) newY;

                    var interpolationX1 = interpolate(color00, color10, percentX);
                    var interpolationX2 = interpolate(color01, color11, percentX);

                    var interpolationY = interpolate(interpolationX1, interpolationX2, percentY);
                    color = interpolationY;
                }
                intermediate.setRGB(x, y, color.getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP save(String filename){
        try {
            ImageIO.write(bufferedImage, "PNG", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            return this;
        }

    }

    // public IP addParts(){
    //     Graphics g = bufferedImage.getGraphics();
    //     Random obj = new Random();
    //     int rand_num = obj.nextInt(0xffffff + 1);
    //     String colorCode = String.format("#%06x", rand_num);
    //     g.setColor(Color.decode(colorCode));
    //     g.drawRect(10, 10, 100, 100);
    //     rand_num = obj.nextInt(0xffffff + 1);
    //     colorCode = String.format("#%06x", rand_num);
    //     g.setColor(Color.decode(colorCode));
    //     g.drawRect(110, 110, 100, 100);
    //     rand_num = obj.nextInt(0xffffff + 1);
    //     colorCode = String.format("#%06x", rand_num);
    //     g.setColor(Color.decode(colorCode));
    //     g.drawRect(210, 210, 100, 100);
    //     return this;
    // }

    public IP randomImage(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Random obj = new Random();
        Color color;
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                int rand_num = obj.nextInt(0xffffff + 1);
                String colorCode = String.format("#%06x", rand_num);
                color = Color.decode(colorCode);
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP removeRed(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Color color;
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                color = new Color(bufferedImage.getRGB(x, y));
                color = new Color(0, color.getGreen(), color.getBlue());
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP removeGreen(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Color color;
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                color = new Color(bufferedImage.getRGB(x, y));
                color = new Color(color.getRed(), 0, color.getBlue());
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP removeBlue(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Color color;
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                color = new Color(bufferedImage.getRGB(x, y));
                color = new Color(color.getRed(), color.getGreen(), 0);
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP removeRandom(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Random obj = new Random();
        
        Color color;
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {
                color = new Color(bufferedImage.getRGB(x, y));
                int rand_num = obj.nextInt(2 + 1);
                if (rand_num == 1){
                    color = new Color(color.getRed(), color.getGreen(), 0);
                }
                else if(rand_num == 2){
                    color = new Color(color.getRed(), 0, color.getBlue());
                }
                else{
                    color = new Color(0, color.getGreen(), color.getBlue());
                }
                intermediate.setRGB(x, y, color.getRGB());
            }
        }
        bufferedImage = intermediate;
        return this;
    }

    public IP onlyHighHue(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        
        float[] histogram = new float[361];
        for (var i = 0; i < 361; i++) {
            histogram[0] = 0;
        }

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                histogram[(int) hsv[0]]++;

            }
        }
        float max = 0;
        float maxi = 0;
        for (int i = 0; i < 361; i++) {
            if (histogram[i] > max) {
                max = histogram[i];
                maxi = i;
            }
        }
        
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());
                if(hsv[0] == maxi){
                    intermediate.setRGB(x, y, original.getRGB());
                }
                else{
                    int average = (original.getRed() + original.getGreen() + original.getBlue())/3;
                    Color newColor = new Color(average, average, average);
    
                    intermediate.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        
        bufferedImage = intermediate;
        return this;
    }

    public IP onlyHighHueBlur(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        
        float[] histogram = new float[361];
        for (var i = 0; i < 361; i++) {
            histogram[0] = 0;
        }

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                histogram[(int) hsv[0]]++;

            }
        }
        float max = 0;
        float maxi = 0;
        for (int i = 0; i < 361; i++) {
            if (histogram[i] > max) {
                max = histogram[i];
                maxi = i;
            }
        }
        Color highHue = new Color(0,0,0);
        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());
                if(hsv[0] == maxi){
                    intermediate.setRGB(x, y, original.getRGB());
                    highHue = original;
                }
                else{
                    intermediate.setRGB(x, y, highHue.getRGB());
                }
            }
        }
        
        bufferedImage = intermediate;
        return this;
    }

    public IP waterMark(){
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(intermediate, 0, 0, null);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.setColor(new Color(0, 0, 0, 255));
        String watermark = "Image Altered by Jadon Onstead";
        graphics.drawString(watermark, 0, bh);
        graphics.dispose();
  
        return this;
    }

    private Color interpolate(Color one, Color two, float percent){
        float r = (1 - percent) * one.getRed() + (percent) * two.getRed();
        float g = (1 - percent) * one.getGreen() + (percent) * two.getGreen();
        float b = (1 - percent) * one.getBlue() + (percent) * two.getBlue();

        return new Color((int)r,(int)g,(int)b);
    }

    public IP changeHue(int degrees) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                hsv[0] += degrees;
                while (hsv[0] < 0) {
                    hsv[0] += 360;
                }
                hsv[0] %= 360;

                float[] rgb = Main.hsvToRgb(hsv[0], hsv[1], hsv[2]);

                intermediate.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]).getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP changeSaturation(int amount) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                hsv[1] += amount;
                if (hsv[1] < 0) {
                    hsv[1] = 0;
                }
                if (hsv[1] > 255) {
                    hsv[1] = 255;
                }

                float[] rgb = Main.hsvToRgb(hsv[0], hsv[1], hsv[2]);

                intermediate.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]).getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP changeValue(int amount) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();
        var intermediate = new BufferedImage(bw, bh, BufferedImage.TYPE_INT_ARGB);

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                hsv[2] += amount;
                if (hsv[2] < 0) {
                    hsv[2] = 0;
                }
                if (hsv[2] > 255) {
                    hsv[2] = 255;
                }

                float[] rgb = Main.hsvToRgb(hsv[0], hsv[1], hsv[2]);

                intermediate.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]).getRGB());
            }
        }

        bufferedImage = intermediate;

        return this;
    }

    public IP toHistogramValue(boolean alter) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();

        int height = 200;
        var intermediate = new BufferedImage(255, height, BufferedImage.TYPE_INT_ARGB);

        float[] histogram = new float[256];
        for (var i = 0; i < 256; i++) {
            histogram[0] = 0;
        }

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                //System.out.println("R " + original.getRed() + " G "+ original.getGreen() + " B " + original.getBlue());
                histogram[(int) hsv[2]]++;

            }
    }

        // Normalize
        float max = 0;
        for (int i = 0; i < 256; i++) {
            if (histogram[i] > max) {
                max = histogram[i];
            }
        }

        for (int i = 0; i < 256; i++) {
            histogram[i] /= max;
        }

        Graphics g = intermediate.getGraphics();

        if(alter == true){
            g.setColor(Color.ORANGE);
        }
        else{
            g.setColor(Color.WHITE);
        }
        g.fillRect(0, 0, 255, height);
        // Random obj = new Random();
        for (int i = 0; i < 256; i++) {
            if(alter == true){
                // int rand_num = obj.nextInt(0xffffff + 1);
                // String colorCode = String.format("#%06x", rand_num);
                // Color color2 = Color.decode(colorCode);
                // float[] hsv = Main.rgbTohsv(color2.getRed(), color2.getGreen(), color2.getBlue());
                // hsv[2] = i;
                // float[] rgb = Main.hsvToRgb(hsv[0], hsv[1], hsv[2]);
                Color color2;
                // if(i <= 10){
                //     color2 = new Color(10, 10, 10);
                // }
                // else if(i >= 200){
                //     color2 = new Color(200, 200, 200);
                // }
                // else{
                    color2 = new Color(i, i, i);
                // }
                g.setColor(color2);
            }
            else{
                g.setColor(Color.BLACK);
            }
            g.fillRect(i, 0, 1, (int) ((1 - histogram[i]) * height));
        }

        g.dispose();

        // intermediate.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int)
        // rgb[2]).getRGB());
        bufferedImage = intermediate;

        return this;
    }

    public IP toHistogramHue(boolean alter) {
        var bw = bufferedImage.getWidth();
        var bh = bufferedImage.getHeight();

        int height = 200;
        var intermediate = new BufferedImage(360, height, BufferedImage.TYPE_INT_ARGB);

        float[] histogram = new float[361];
        for (var i = 0; i < 361; i++) {
            histogram[0] = 0;
        }

        for (var y = 0; y < bh; y++) {
            for (var x = 0; x < bw; x++) {

                Color original = new Color(bufferedImage.getRGB(x, y));

                float[] hsv = Main.rgbTohsv(original.getRed(), original.getGreen(), original.getBlue());

                histogram[(int) hsv[0]]++;

            }
        }

        // Normalize
        float max = 0;
        for (int i = 0; i < 361; i++) {
            if (histogram[i] > max) {
                max = histogram[i];
            }
            if(histogram[i] >= 20000){
                // System.out.println(i + ": " + histogram[i]);
            }
        }

        for (int i = 0; i < 361; i++) {
            histogram[i] /= max;

        }

        Graphics g = intermediate.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 360, height);
        Random obj = new Random();
        for (int i = 0; i < 361; i++) {
            if(alter == true){
                int rand_num = obj.nextInt(0xffffff + 1);
                String colorCode = String.format("#%06x", rand_num);
                Color color2 = Color.decode(colorCode);
                float[] hsv = Main.rgbTohsv(color2.getRed(), color2.getGreen(), color2.getBlue());
                hsv[0] = i%360;
                float[] rgb = Main.hsvToRgb(hsv[0], hsv[1], hsv[2]);
                color2 = new Color(((int)rgb[0]), ((int)rgb[1]), ((int)rgb[2]));
                g.setColor(color2);
            }
            else{
                g.setColor(Color.BLUE);
            }
            g.fillRect(i, 0, 1, (int) ((1 - histogram[i]) * height));
        }

        g.dispose();

        // intermediate.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int)
        // rgb[2]).getRGB());
        bufferedImage = intermediate;

        return this;
    }

    public boolean isValidCoordinate(int x, int y){
        return x >= 0 && y >= 0 && x < bufferedImage.getWidth() && y < bufferedImage.getHeight();
    
      }
}