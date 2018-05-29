package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
        for (int i = 0; i < 10; i++) {
            Rectangle rectangle = new Rectangle(width, height);
            if (i % 2 == 0)
                rectangle.setFill(Color.BISQUE);
            else
                rectangle.setFill(Color.BURLYWOOD);
            rectangles_pane.getChildren().add(rectangle);
        }
        rectangles_pane.applyCss();
        rectangles_pane.layout();

        main_pane = new Pane();
        main_pane.getChildren().add(rectangles_pane);

        HBox buttons_pane = new HBox();
        Button prev = new Button("<");
        Button next = new Button(">");
        buttons_pane.getChildren().add(prev);
        buttons_pane.getChildren().add(next);
        main_pane.getChildren().add(buttons_pane);

        Double[] espacio = {1.0, 1.0, 1.0, 1.0, 1.0, 1.55, 1.55, 1.55, 1.55, 1.55};
        int n = 10;
        ArrayList<Double[]> res = Algorithm.snell(espacio, 0.05, 50);
        drawLines(coordinate_index, res);

        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main_pane.getChildren().subList(4, main_pane.getChildren().size()).clear();
                coordinate_index = max(0, coordinate_index - 1);
                drawLines(coordinate_index,res);
            }
        });
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main_pane.getChildren().subList(4, main_pane.getChildren().size()).clear();
                coordinate_index = min(res.size() - 1, coordinate_index + 1);
                drawLines(coordinate_index,res);
            }
        });

        Scene scene = new Scene(main_pane, 300, 250);
        primaryStage.setTitle("Fiber optic simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
