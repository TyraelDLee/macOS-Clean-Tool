package defaultPackage.GUI;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Background extends StackPane {
    public static final Color BRIGHT = new Color(1,1,1,0.9);
    public static final Color DARK = new Color(0.47,0.40,0.42,1);

    private double width;
    private double height;
    private Rectangle background = new Rectangle();
    public Background(double weight, double height){
        setSize(weight, height);
        this.getChildren().add(this.background);
        setFill(BRIGHT);
    }

    public Background(){
        this.getChildren().add(this.background);
        setFill(BRIGHT);
    }

    public void setSize(double weight, double height){
        this.height = height;
        this.width = weight;
        this.background.setHeight(this.height);
        this.background.setWidth(this.width);
    }

    public void setFill(Color paint){
        BoxBlur glass = new BoxBlur();
        glass.setHeight(10);
        glass.setWidth(10);
        this.background.setFill(paint);
        this.background.setEffect(new BoxBlur());
    }
}
