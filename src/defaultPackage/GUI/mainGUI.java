package defaultPackage.GUI;

import defaultPackage.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainGUI extends Application implements Observer {
    private static final double WINDOW_HEIGHT = 420;
    private static final double WINDOW_WIDTH = 720;
    private static double ZOOMFACTOR = 1;
    private Number mainStageHeight = WINDOW_HEIGHT, mainStageWidth = WINDOW_WIDTH;
    private final Group root = new Group();
    private mainProgress executor = new mainProgress(true);
    private Background background = new Background();

    private void setZoomFactor(Number mainStageWidth, Number mainStageHeight) {
        double width = mainStageWidth.doubleValue() / WINDOW_WIDTH, height = mainStageHeight.doubleValue() / WINDOW_HEIGHT;
        if (width < height) ZOOMFACTOR = Math.round(width * 100.0) / 100.0;
        else ZOOMFACTOR = Math.round(height * 100.0) / 100.0;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        executor.reg(this);
        primaryStage.setTitle("fuxk Time machine!");
        primaryStage.setMinHeight(420*0.7);
        primaryStage.setMinWidth(720*0.7);
        Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);
        scene.setFill(Color.WHITESMOKE.deriveColor(
                0, 1, 1, 0.01
        ));

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mainStageHeight = newValue;
            setZoomFactor(mainStageWidth, mainStageHeight);
            resize();
        });
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mainStageWidth = newValue;
            setZoomFactor(mainStageWidth, mainStageHeight);
            resize();
        });
        resize();
        //background.setFill(Background.BRIGHT);
        root.getChildren().addAll();
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void resize(){
        background.setSize(mainStageWidth.doubleValue(),mainStageHeight.doubleValue());
    }

    @Override
    public void update(String context) {

    }
}
