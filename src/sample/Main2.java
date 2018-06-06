package sample;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main2 extends Application {
    private Pane main_pane;
    private VBox rectangles_pane;
    private int width = 1000, height = 20;

    public static void main(String[] args) {
        launch(args);
    }

    private void drawLines(ArrayList<Tuple<Double, Double>> coords) {
        Node r = rectangles_pane.getChildren().get(rectangles_pane.getChildren().size() / 2);
        Bounds b = r.localToScene(r.getBoundsInLocal());

        Line line = new Line();
        line.setStartX(b.getMinY() - 20);
        line.setStartY((b.getMinX() + b.getMaxX()) / 2);
        line.setEndX(coords.get(0).second);
        line.setEndY(b.getMaxX());
        main_pane.getChildren().add(line);

        for (Tuple<Double, Double> coord : coords) {
//            int rectangle = coord.second;
//            double x = coord.first;
//
//            Node r = rectangles_pane.getChildren().get(i);
//            Bounds b = r.localToScene(r.getBoundsInLocal());
//
//            Line line = new Line();
//            line.setStartX(b.getMinX());
//            line.setStartY(b.getMinY() + height * current_coordinates[i]);
//            line.setEndX(b.getMaxX());
//            line.setEndY(b.getMinY() + height * current_coordinates[i + 1]);
//            main_pane.getChildren().add(line);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        rectangles_pane = new VBox();
        rectangles_pane.setPadding(new Insets(80, 20, 20, 80));
        for (int i = 0; i < 7; i++) {
            Rectangle rectangle = new Rectangle(width, height);
            if (i % 2 == 0)
                rectangle.setFill(Color.BISQUE);
            else
                rectangle.setFill(Color.BURLYWOOD);
            rectangles_pane.getChildren().add(rectangle);
        }

        // We force JavaFX to prerender the rectangles so that we know the exact position they
        // will occupy; otherwise, the first time we run drawLines() it will superpose all
        // rectangles and assume all x = 0
        rectangles_pane.applyCss();
        rectangles_pane.layout();

        main_pane = new Pane();
        main_pane.getChildren().add(rectangles_pane);

        ArrayList<Tuple<Double, Double>> coords = Algorithm.snell(20, 37.5, 1.0, 0.01, 2);
        drawLines(coords);

        Scene scene = new Scene(main_pane, 300, 250);
        primaryStage.setTitle("Fiber optic simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // The initial render is incorrect. Let's rerender it again.
        main_pane.getChildren().subList(1, main_pane.getChildren().size()).clear();
        drawLines(coords);
    }
}
