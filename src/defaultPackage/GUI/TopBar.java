package defaultPackage.GUI;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class TopBar extends StackPane {
    private double height = 0;
    private double width = 0;
    private String title = "";
    private Circle close = new Circle(6);

    public TopBar(){}

    public TopBar(double width, double height){
        this.width = width;
        this.height = height;
    }

    public TopBar(double width, double height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public double getBarWidth(){
        return this.width;
    }
    public double getBarHeight() {
        return this.height;
    }
}
