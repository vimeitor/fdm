package sample;

public class Main_aux {
    public static void main(String[] args) {
        Algorithm a = new Algorithm();
        Double[] espacio = {1.0, 1.0, 1.0, 1.0, 1.0, 1.55, 1.55, 1.55, 1.55, 1.55};
        int n = 10;
        Double[] res = a.snell(espacio, 0.05);
        for (int i = 0; i < n + 1; ++i) {
            System.out.println(res[i]);
        }
    }
}
