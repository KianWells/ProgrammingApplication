package com.kxw959.programmingapplication.user;

import javafx.util.Pair;

public class Task {
    public String taskID;
    public Pair<String, String> start;
    public Pair<String, String> test;
    public Pair<String, String> instructions;
    public Pair<String, String> quiz;
    public int junitVersion = 4;
    public int testsPassed = 0;
    public int totalTests = 0;
    public boolean completed = false;
}
