package com.sp.tools;

import com.sp.model.CardTemplate;

import java.util.HashMap;

public class RandomUtils {

    // Generate a random integer between a and b
    public static int randomInt(int a, int b) {
        return (int) (Math.random() * (b - a + 1) + a);
    }

    // return a callback of a random integer between a and b
    public static Callback<Integer> randomIntCallback(int a, int b) {
        return new Callback<Integer>() {
            @Override
            public Integer run() {
                return randomInt(a, b);
            }
        };
    }

    //Generate a random float between a and b
    public static float randomFloat(float a, float b) {
        return (float) (Math.random() * (b - a) + a);
    }

    // return a callback of a random float between a and b
    public static Callback<Float> randomFloatCallback(float a, float b) {
        return new Callback<Float>() {
            @Override
            public Float run() {
                return randomFloat(a, b);
            }
        };
    }


    public static CardTemplate randomFromMap(HashMap<CardTemplate, Float> cardTemplates) {
        // return a random CardTemplate from a map of CardTemplate and their probability
        float total = 0;
        for (CardTemplate cardTemplate : cardTemplates.keySet()) {
            total += cardTemplates.get(cardTemplate);
        }
        float random = randomFloat(0, total);
        float sum = 0;
        for (CardTemplate cardTemplate : cardTemplates.keySet()) {
            sum += cardTemplates.get(cardTemplate);
            if (random <= sum) {
                return cardTemplate;
            }
        }
        return null;
    }
}
