package sample;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private Pane main_pane;
    private HBox rectangles_pane;
    private int width = 100, height = 400, coordinate_index = 0;

    public static void main(String[] args) {
        launch(args);
    }

    private void drawLines(int result_index, ArrayList<Double[]> all_coordinates) {
        Double[] current_coordinates = all_coordinates.get(result_index);
        for (int i = 0; i < current_coordinates.length - 1; i++) {
            Node r = rectangles_pane.getChildren().get(i);
            Bounds b = r.localToScene(r.getBoundsInLocal());

            Line line = new Line();
            line.setStartX(b.getMinX());
            line.setStartY(b.getMinY() + height * current_coordinates[i]);
            line.setEndX(b.getMaxX());
            line.setEndY(b.getMinY() + height * current_coordinates[i + 1]);
            main_pane.getChildren().add(line);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        rectangles_pane = new HBox();
        rectangles_pane.setPadding(new Insets(80, 20, 20, 80));
        for (int i = 0; i < 10; i++) {
            Rectangle rectangle = new Rectangle(width, height);
            if (i < 5)
                rectangle.setFill(Color.BISQUE);
            else
                rectangle.setFill(Color.BURLYWOOD);
            rectangles_pane.getChildren().add(rectangle);
        }
        rectangles_pane.setAlignment(Pos.CENTER);

        // We force JavaFX to prerender the rectangles so that we know the exact position they
        // will occupy; otherwise, the first time we run drawLines() it will superpose all
        // rectangles and assume all x = 0
        rectangles_pane.applyCss();
        rectangles_pane.layout();

        main_pane = new Pane();
        main_pane.getChildren().add(rectangles_pane);

        Double[] espacio = {1.0, 1.0, 1.0, 1.0, 1.0, 1.55, 1.55, 1.55, 1.55, 1.55};
        ArrayList<Double[]> res = Algorithm.fermat(espacio, 0.20, 50);

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(res.size() - 1);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.setMinWidth(width * 10);
        slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            main_pane.getChildren().subList(4, main_pane.getChildren().size()).clear();
            drawLines(newValue.intValue(), res);
        });
        main_pane.getChildren().add(slider);

        drawLines(coordinate_index, res);

        Scene scene = new Scene(main_pane, 300, 250);
        primaryStage.setTitle("Fiber optic simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // The initial render is incorrect. Let's rerender it again.
        main_pane.getChildren().subList(4, main_pane.getChildren().size()).clear();
        drawLines(coordinate_index, res);
    }
}
