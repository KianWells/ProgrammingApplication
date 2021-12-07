package com.kxw959.programmingapplication.JUNITSTUFF;

import org.junit.Assert;
import org.junit.Test;

public class jpa2021_Example_test {
    jpa2021_Example_start example = new jpa2021_Example_start();

    @Test
    public void task_one(){

        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, example.task1());
    }
}
