package sample;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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

    private void drawLines(ArrayList<Tuple<Double, Double>> coords, boolean scaled) {
        Node r = rectangles_pane.getChildren().get(rectangles_pane.getChildren().size() / 2);
        Bounds b = r.localToScene(r.getBoundsInLocal());
        double start_x = b.getMinX();

        int mult = scaled ? 1 : 10;

        for (int i = 0; i < coords.size() - 1; i++) {
            Tuple<Double, Double> coord = coords.get(i);
            int r1 = coord.second.intValue();
            double x1 = coord.first * mult;
            Tuple<Double, Double> coord2 = coords.get(i + 1);
            int r2 = coord2.second.intValue();
            double x2 = coord2.first * mult;

            Node rp = rectangles_pane.getChildren().get(r1);
            Bounds bp = rp.localToScene(rp.getBoundsInLocal());
            Node rp2 = rectangles_pane.getChildren().get(r2);
            Bounds bp2 = rp2.localToScene(rp2.getBoundsInLocal());

            Line linep = new Line();
            linep.setStartX(start_x + x1);
            linep.setStartY(bp.getMinY());
            linep.setEndX(start_x + x2);
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

        ArrayList<Tuple<Double, Double>> coords = Algorithm.snell(20, 85, 1.47, 0.01, 2, 1000);
        for (Tuple<Double, Double> t : coords) {
            System.out.println(t.second.intValue() + " -> " + t.first);
        }
        drawLines(coords, true);

        /*
         * Buttons
         */
        VBox tf = new VBox();
        tf.setSpacing(12);
        tf.setPadding(new Insets(20, 20, 20, 20));

        TextField delta = new TextField();
        delta.setText("0.01");
        tf.getChildren().add(delta);
        TextField alpha = new TextField();
        alpha.setText("2");
        tf.getChildren().add(alpha);
        TextField angle = new TextField();
        angle.setText("85");
        tf.getChildren().add(angle);
        TextField n1 = new TextField();
        n1.setText("1.47");
        tf.getChildren().add(n1);
        CheckBox cb = new CheckBox("scaled");
        cb.setSelected(true);
        tf.getChildren().add(cb);

        Button render_button = new Button("Render");
        render_button.setOnAction((event) -> {
            int length = cb.isSelected() ? 1000 : 100;
            ArrayList<Tuple<Double, Double>> coords2 = Algorithm.snell(20, Double.parseDouble
                    (angle.getText()), Double.parseDouble(n1.getText()), Double.parseDouble(delta
                    .getText()), Integer.parseInt(alpha.getText()), length);
            main_pane.getChildren().subList(1, main_pane.getChildren().size()).clear();
            drawLines(coords2, cb.isSelected());
        });
        tf.getChildren().add(render_button);
        rectangles_pane.getChildren().add(tf);
        /*
         *
         */

        Scene scene = new Scene(main_pane, 300, 250);
        primaryStage.setTitle("Fiber optic simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // The initial render is incorrect. Let's rerender it again.
        main_pane.getChildren().subList(1, main_pane.getChildren().size()).clear();
        drawLines(coords, true);
    }
}
