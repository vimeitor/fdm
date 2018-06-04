package sample;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
    public Algorithm() {
    }

    /**
     * @param space     Refraction indices list
     * @param delta     Variability of the new y(i) coordinate with respect to the old y(i)
     *                  The higher the value, the faster the convergence speed
     * @param max_tries The amount of times a new random y(i) will be generated for a given y(i)
     *                  The higher the value, the faster the convergence, but the lower the
     *                  performance
     * @return List of lists of all coordinates that the light will go through
     */
    static ArrayList<Double[]> fermat(Double[] space, double delta, int max_tries) {
        int num_regions = space.length;

        Double[] coords = new Double[num_regions + 1];
        for (int i = 0; i <= num_regions; i++) {
            coords[i] = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
        }

        ArrayList<Double[]> total_coords = new ArrayList<>();
        total_coords.add(coords);

        Double time = calculate_light_time(space, coords);
        int current_tries = 0;
        while (true) {
            int region = ThreadLocalRandom.current().nextInt(1, num_regions);
            Double alt = ThreadLocalRandom.current().nextDouble(-delta, delta);

            Double[] new_coords = coords.clone();
            new_coords[region] = coords[region] + alt;

            Double new_time = calculate_light_time(space, new_coords);
            if (new_time < time) {
                time = new_time;
                coords = new_coords;
                current_tries = 0;
                total_coords.add(new_coords);
            } else {
                current_tries++;
                if (current_tries == max_tries) {
                    break;
                }
            }
        }
        return total_coords;
    }

    static Double[] snell(int num_regions, double initial_angle, double n1, double n2,
                                  double alpha, int radius) {
        double delta = (n1 - n2) / n1;

        // Populate the indices from each layer assuming it's a GRIN fiber optic
        Double[] indices = new Double[num_regions + 1];
        indices[0] = n1;
        indices[1] = n2;
        for (int i = 2; i < indices.length; i++) {
            indices[i] = n1 * Math.sqrt(1 - 2 * delta * Math.pow(i / radius, alpha));
        }

        Double[] coords = new Double[num_regions + 1];
        coords[0] = initial_angle; // FIXME: Temporarily using it as initial x position
        for (int i = 1; i < num_regions; i++) {
            double region_height = radius / num_regions;
            double in_angle = Math.atan(region_height / (coords[i - 1] / coords[i - 2]));
            double out_angle = Math.asin(indices[i - 1] * Math.sin(in_angle) / indices[i]);
            double x = region_height / Math.tan(out_angle);
            coords[i] = coords[i - 1] + x;
        }
        return coords;
    }

    /**
     * @param space    Refraction indices list
     * @param y_coords y(i) coordinates list
     * @return Time it takes for light to go through the given space
     */
    static private Double calculate_light_time(Double[] space, Double[] y_coords) {
        Double time = 0.0;
        Tuple<Double, Double> start = new Tuple<>(0.0, 0.0);
        Tuple<Double, Double> end = new Tuple<>(0.0, 0.0);
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
