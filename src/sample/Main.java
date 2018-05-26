package sample;

import javafx.application.Application;
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

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int width = 100, height = 400;
        HBox rectangles_pane = new HBox();
        for (int i = 0; i < 10; i++) {
            Rectangle rectangle = new Rectangle(width, height);
            if (i % 2 == 0)
                rectangle.setFill(Color.BISQUE);
            else
                rectangle.setFill(Color.BURLYWOOD);
            rectangles_pane.getChildren().add(rectangle);
        }

        Pane main_pane = new Pane();
        main_pane.getChildren().add(rectangles_pane);

        int i = 4;
        Node r = rectangles_pane.getChildren().get(i);
        Bounds b = r.localToScene(r.getBoundsInLocal());

        Line line = new Line();
        line.setStartX(b.getMinX() + i * width);
        line.setStartY(b.getMinY());
        line.setEndX(b.getMaxX() + i * width);
        line.setEndY(b.getMaxY());
        main_pane.getChildren().add(line);

        HBox buttons_pane = new HBox();
        Button prev = new Button("<");
        Button next = new Button(">");
        buttons_pane.getChildren().add(prev);
        buttons_pane.getChildren().add(next);
        main_pane.getChildren().add(buttons_pane);

        Scene scene = new Scene(main_pane, 300, 250);
        primaryStage.setTitle("Fiber optic simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
