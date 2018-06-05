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

    static ArrayList<Tuple<Double, Double>> snell(int num_regions, double initial_angle, double
            n1, double delta, double alpha, int radius) {
        double n2 = Math.sqrt(n1 - delta * n1);

        Double[] indices = new Double[num_regions];
        indices[0] = n2;
        indices[num_regions - 1] = n2;

        int core_index = (num_regions - 1) / 2;
        indices[core_index] = n1;
        for (int i = 1; i < core_index; i++) {
            indices[core_index + i] = n1 * Math.sqrt(1 - 2 * delta * Math.pow(i / radius, alpha));
            indices[core_index - i] = n1 * Math.sqrt(1 - 2 * delta * Math.pow(i / radius, alpha));
        }

        ArrayList<Tuple<Double, Double>> coords = new ArrayList<>();
        Tuple<Double, Double> current = new Tuple();

        current.second = (double) num_regions / 2.0;
        coords[0] = current;
        double angle = initial_angle;
        boolean reflects = true;
        boolean upwards = true;
        int current_region = core_index;
        int next_region = core_index + 1;
        double current_angle = angle.toradians();
        double next_angle;
        int i = 0;
        coords[i] = current;
        double critical_angle;
        double new_x = 0.0;
        while (actual.first < 100) {
            // Check if beam is inside fiber optic
            if (next_region > num_regions - 1 || next_region < 0) {
                break;
            }

            // actual = calculate_new_coords(current,direction,indices[currentregion],
            // indices[next_region],angle); // TODO: calcular nuevas coordenadas

            critical_angle = Math.asin(indices[next_region] / indices[current_region]);
            if (current_angle > critical_angle) {
                reflects = true;
                upwards = !upwards;
                if (upwards) {
                    next_region = next_region + 2;
                    current.second = current.second + 1;
                } else {
                    next_region = next_region - 2;
                    current.second = current.second - 1;
                }
            } else {
                reflects = false;
                current_angle = Math.asin(indices[current_region] / indices[next_region] * Math.sin
                        (current_angle));
                if (upwards) {
                    current_region = current_region + 1;
                    next_region = next_region + 1;
                    current.second = current.second + 1;
                } else {
                    current_region = current_region - 1;
                    next_region = next_region - 1;
                    current.second = current.second - 1;
                }
            }
            new_x = 1.0 / Math.tan(current_angle);
            current.first = current.first + new_x;

            coords.set(i, current);
            i++;
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
