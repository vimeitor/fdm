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
        double start_x = b.getMinX();

//        Line line = new Line();
//        line.setStartX(b.getMinX());
//        line.setStartY((b.getMinY() + b.getMaxY()) / 2);
//        line.setEndX(start_x + coords.get(0).second * 10);
//        line.setEndY(b.getMaxY());
//        main_pane.getChildren().add(line);

        for (int i = 0; i < coords.size() - 1; i++) {
            Tuple<Double, Double> coord = coords.get(i);
            int r1 = coord.second.intValue();
            double x1 = coord.first;
            Tuple<Double, Double> coord2 = coords.get(i + 1);
            int r2 = coord2.second.intValue();
            double x2 = coord2.first;

            Node rp = rectangles_pane.getChildren().get(r1);
            Bounds bp = rp.localToScene(rp.getBoundsInLocal());
            Node rp2 = rectangles_pane.getChildren().get(r2);
            Bounds bp2 = rp2.localToScene(rp2.getBoundsInLocal());

            Line linep = new Line();
            linep.setStartX(start_x + x1 * 10);
            linep.setStartY(bp.getMinY());
            linep.setEndX(start_x + x2 * 10);
            linep.setEndY(bp2.getMinY());
            main_pane.getChildren().add(linep);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        rectangles_pane = new VBox();
        rectangles_pane.setPadding(new Insets(80, 20, 20, 80));
        for (int i = 0; i < 20; i++) {
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

        ArrayList<Tuple<Double, Double>> coords = Algorithm.snell(20, 82, 1.47, 0.01, 2);
        for (Tuple<Double, Double> t : coords) {
            System.out.println(t.second.intValue() + " -> " + t.first);
        }
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
