package java8;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Bean.java to edit this template
 */

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author username
 */
public class BeanComponent {
    private int trigger = 0; // триггер, который будем изменять при возврате на главную страницу

    // Метод для вычисления произведения чисел больше М
    public String calculateProduct(int[] numbers, int M) {
    int product = 1;
    boolean found = false;
    for (int number : numbers) {
        if (number > M) {
            product *= number;
            found = true;
        }
    }
    if (found) {
        return String.valueOf(product);
    } else {
        return "Нет чисел больше M";
    }
}


    // Геттер и сеттер для триггера
    public int getTrigger() {
        return trigger;
    }

    public void setTrigger(int trigger) {
        this.trigger = trigger;
    }
}
