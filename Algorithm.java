package sample;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Jesus on 24/5/18.
 */



public class Algorithm {
    public Algorithm() {
    }

    Double[] snell(int n, Double[] space) { // parametros: numero de regiones del espacio y vector de indices de refraccion
        Double[] sep_coord = new Double[n+1]; // vector de coordenadas y(i)
        Double[] new_sep_coord = new Double[n+1];
        boolean stop = false;
        Double d = 0.05;
        for(int i = 0; i <= n; ++i) { //rellena con randoms el vector de coordenadas
            sep_coord[i] = ThreadLocalRandom.current().nextDouble(0.0,10.0);
        }
        Double nuevotiempo, alt;
        int reg;
        Double tiempo0 = calculo_tiempo_luz(n,space, sep_coord); // tiempo que tarad la luz con las coordenadas actuales
        int cont = 0;
        while(!stop) {
            reg = ThreadLocalRandom.current().nextInt(1, n-1);
            alt = ThreadLocalRandom.current().nextDouble(-d, d);; //coge unas nuevas coordenadas para un punto de interseccion aleatorio.
            new_sep_coord = sep_coord;
            new_sep_coord[reg] = sep_coord[reg] + alt;
            nuevotiempo = calculo_tiempo_luz(n,space, new_sep_coord);  //comprueba si con las nuevas coordenadas el tiempo es menor)
            if(nuevotiempo < tiempo0) {
                tiempo0 = nuevotiempo;
                sep_coord = new_sep_coord;
                cont = 0;
            }
            else{
                ++cont;
                if(cont == 50) {
                    stop = true;
                }
            }
        }
        return sep_coord;
    }

    Double calculo_tiempo_luz (int n, Double[] space, Double[] sep_coord) { //calculo del tiempo que tarda la luz en
        Double velocidad_luz = 1.0;									// atravesar el espacio con esa direccion
        Double tiempo_agg = 0.0;
        Double distancia= 0.0;
        Double velocidad_i = 0.0;
        Tuple<Double, Double> fin = new Tuple<>(0.0,0.0);
        Tuple<Double, Double> ini = new Tuple<>(0.0,0.0);
        Double pp= 0.0;
        Double tt = 0.0;
        for(double i = 0.0; i<n; i = i +1.0) {
            fin.first = i+1.0;
            fin.second = sep_coord[(int) i+1];
            ini.first = i;
            ini.second = sep_coord[(int) i];
            pp = Math.pow(fin.first-ini.first,2.0);
            tt = Math.pow(fin.second - ini.second, 2.0);
            distancia = Math.sqrt(pp+tt);
            velocidad_i = velocidad_luz / space[(int)i];
            tiempo_agg += distancia / velocidad_i;


        }
        return tiempo_agg;


    }



    Double[] snellfijado(int n, Double[] space) { // parametros: numero de regiones del espacio y vector de indices de refraccion
        Double[] sep_coord = new Double[n+1]; // vector de coordenadas y(i)
        Double[] new_sep_coord = new Double[n+1];
        boolean stop = false;
        Double d = 0.5;
        sep_coord[0] = 8.04;
        sep_coord[2] = 1.51;
        //for(int i = 1; i < n; ++i) { //rellena con randoms el vector de coordenadas
            sep_coord[1] = ThreadLocalRandom.current().nextDouble(0.0,10.0);
        //}
        Double nuevotiempo, alt;
        int reg;
        Double tiempo0 = calculo_tiempo_luz(n,space, sep_coord); // tiempo que tarad la luz con las coordenadas actuales
        int cont = 0;
        while(!stop) {
            reg = 1;
            alt = ThreadLocalRandom.current().nextDouble(-d, d);; //coge unas nuevas coordenadas para un punto de interseccion aleatorio.
            new_sep_coord = sep_coord;
            new_sep_coord[reg] = sep_coord[reg] + alt;
            nuevotiempo = calculo_tiempo_luz(n,space, new_sep_coord);  //comprueba si con las nuevas coordenadas el tiempo es menor)
            int o = 0;
            if(nuevotiempo < tiempo0) {
                tiempo0 = nuevotiempo;
                sep_coord = new_sep_coord;
                cont = 0;
            }
            else{
                ++cont;
                if(cont == 500) {
                    stop = true;
                }
            }
        }
        return sep_coord;
    }







}
