package com.asura.javcourse.jvm.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassCode {
    private double money;
    private int age;

    public void lifeAndCreateMoney() {
        ClassCode demo = new ClassCode();
        demo.money = 10;
        demo.age = 28;
        List<Integer> lifes = new ArrayList<>();
        for (int i = 1; i > 6; i++) {
            if (new Random().nextInt(5) > 3) {
                lifes.add(i * 3);
            } else {
                lifes.add(i * 5);
            }
        }
        calcFinalMoney(lifes);
    }
    private void calcFinalMoney(List<Integer> lifes) {
        lifes.stream().forEach(life -> {
            age += life;
            money =  money * (1 + new Random().nextInt(10) % 10) * life * 12;
        });
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "ClassCode{" +
                "money=" + money +
                ", age=" + age +
                '}';
    }
}
