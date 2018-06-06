package src;

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
        total_coords.add(coords.clone());

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
                total_coords.add(new_coords.clone());
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
            n1, double delta, double alpha) {
        double radius = (num_regions-2.0)/2.0;
        double n2 = n1 - delta * n1;

        Double[] indices = new Double[num_regions];
        indices[0] = n2;
        indices[num_regions - 1] = n2;

        int core_index1 = num_regions / 2;
        int core_index2 = core_index1-1;
        indices[core_index1] = n1;
        indices[core_index2] = n1;
        for (int i = 1; i < core_index2; i++) {
            indices[core_index1 + i] = n1 * Math.sqrt(1 - 2 * delta * Math.pow( (double)i / radius, alpha));
            indices[core_index2 - i] = n1 * Math.sqrt(1 - 2 * delta * Math.pow((double)i / radius, alpha));
        }

        ArrayList<Tuple<Double, Double>> coords = new ArrayList<>();
        Tuple<Double, Double> current = new Tuple<>();
        current.first = 0.0;
        current.second = num_regions / 2.0;
        coords.add(0,current.clone());
        double angle = initial_angle;
        boolean reflects = true;
        boolean upwards = true;
        int current_region = core_index1;
        int next_region = core_index1 + 1;
        double current_angle = Math.toRadians(angle);
        double next_angle;
        double critical_angle;
        double new_x = 0.0;
        new_x = Math.tan(current_angle);
        current.first = current.first + new_x;
        current.second = current.second + 1.0;
        coords.add(1, current.clone());
        int i = 2;
        while (current.first < 100) {
            // Check if beam is inside fiber optic
            if (next_region > num_regions - 2 || next_region < 1) {
                current_angle = Math.asin(indices[current_region] / indices[next_region] * Math.sin(current_angle));
                new_x = Math.tan(current_angle);
                current.first = current.first + new_x;

                if (upwards) {
                    current.second = current.second + 1;
                } else {
                    current.second = current.second - 1;
                }
                coords.add(i, current.clone());
                break;

            }

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
                current_angle = Math.asin(indices[current_region] / indices[next_region] * Math.sin(current_angle));
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
            new_x = Math.tan(current_angle);
            current.first = current.first + new_x;
            coords.add(i, current.clone());
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

    static Double[] getangles(Double[] space, Double[] y_coords) {
        int n = space.length;
        Double[] angles = new Double[n];
        Double[] anglesRefr = new Double[n];
        anglesRefr[0] = 0.0;
        double angulo;
        double angulorefr = 45;
        double angulorefrrad;
        double anguloRadianes;
        double yp;
        double y;
        for (int i = 0; i < n; ++i) {
            yp = y_coords[i + 1];
            y = y_coords[i];
            double aux = Math.abs(yp - y);
            anguloRadianes = Math.atan(aux);
            angulo = Math.toDegrees(anguloRadianes);
            angles[i] = angulo;
            if (i < n - 1) {
                angulorefrrad = Math.asin(space[i] * Math.sin(anguloRadianes) / space[i + 1]);
                angulorefr = Math.toDegrees(angulorefrrad);
                anglesRefr[i + 1] = angulorefr;
            }
        }

        for(int i = 0; i < angles.length; ++i) {
            System.out.print("angulo:"+angles[i]);
            System.out.println(".   angulo refr:" + anglesRefr[i]);
        }


        return angles;
    }

}
