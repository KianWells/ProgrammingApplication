package com.kxw959.programmingapplication.examples;

import org.junit.Assert;
import org.junit.Test;

public class ExampleTest {
    Example example = new Example();

    @Test
    public void test1(){
        Assert.assertEquals(example.task1(3).length, 3);
    }

    @Test
    public void test2(){
        Assert.assertEquals(example.task2(5).length(), 5);
    }
}
