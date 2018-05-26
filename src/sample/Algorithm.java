package sample;

import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
    public Algorithm() {
    }

    /**
     * @param space Refraction index list
     * @param d TODO
     * @return List of updated y(i) coordinates
     */
    Double[] snell(Double[] space, double d) {
        int num_regions = space.length;
        Double[] y_coords = new Double[num_regions + 1];
        for (int i = 0; i <= num_regions; i++) {
            y_coords[i] = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
        }
        Double time = calculate_light_time(space, y_coords);
        int cont = 0;
        while (true) {
            int region = ThreadLocalRandom.current().nextInt(1, num_regions - 1);
            Double alt = ThreadLocalRandom.current().nextDouble(-d, d);
            Double[] new_y_coords = y_coords.clone();
            new_y_coords[region] = y_coords[region] + alt;
            Double new_time = calculate_light_time(space, new_y_coords);
            if (new_time < time) {
                time = new_time;
                y_coords = new_y_coords;
                cont = 0;
            } else {
                cont++;
                if (cont == 50) {
                    break;
                }
            }
        }
        return y_coords;
    }

    /**
     * @param space    Refraction index list
     * @param y_coords y(i) coordinates list
     * @return Time it takes for light to go through the given space
     */
    private Double calculate_light_time(Double[] space, Double[] y_coords) {
        Double time = 0.0;
        Tuple<Double, Double> end = new Tuple<>(0.0, 0.0);
        Tuple<Double, Double> start = new Tuple<>(0.0, 0.0);
        for (int i = 0; i < space.length; i++) {
            end.first = i + 1.0;
            end.second = y_coords[i + 1];
            start.first = (double) i;
            start.second = y_coords[i];
            Double pp = Math.pow(end.first - start.first, 2.0);
            Double tt = Math.pow(end.second - start.second, 2.0);
            Double distance = Math.sqrt(pp + tt);
            Double velocity = 1.0 / space[i];
            time += distance / velocity;
        }
        return time;
    }
}
