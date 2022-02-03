package com.kxw959.programmingapplication.examples;

import org.junit.Assert;
import org.junit.Test;

public class ExampleTest {
    Example testObject = new Example();

    @Test
    public void test1(){
        Assert.assertEquals(testObject.task1(), 5);
    }

    @Test
    public void test2(){
        Assert.assertEquals(testObject.task2(5), "5");
    }
}
