package com.bustracker;

import java.util.Random;

public class Util {

    public static int getRamdomTime(){
        Random random = new Random();

       return random.nextInt(4);

    }
}
