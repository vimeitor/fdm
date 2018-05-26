package sample;

/**
 * Created by Jesus on 24/5/18.
 */
public class Main_aux {
    public static void main(String[] args) {
        Algorithm a = new Algorithm();
        Double[] espacio = {1.0,1.55};
        int n  = 2;
        Double[] res = a.snellfijado(n,espacio);
        for(int i = 0; i < n+1; ++i) {
            System.out.println(res[i]);
        }
    }
}
