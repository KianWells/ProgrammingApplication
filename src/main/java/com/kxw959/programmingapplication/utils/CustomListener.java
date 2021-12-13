package com.kxw959.programmingapplication.utils;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class CustomListener extends RunListener
{
    public String output = "";

    /**
     * Called before any tests have been run.
     * */
    public void testRunStarted(Description description) throws java.lang.Exception
    {
        output+="Number of tests to execute : " + description.testCount()+"\n\n";
    }

    /**
     *  Called when all tests have finished
     * */
    public void testRunFinished(Result result) throws java.lang.Exception
    {
        output += "Number of tests executed : " + result.getRunCount()+"\n\n";
    }

    /**
     *  Called when an atomic test is about to be started.
     * */
    public void testStarted(Description description) throws java.lang.Exception
    {
        output += "Starting execution of test case : "+ description.getMethodName() +"\n\n";
    }

    /**
     *  Called when an atomic test has finished, whether the test succeeds or fails.
     * */
    public void testFinished(Description description) throws java.lang.Exception
    {
        output += "Finished execution of test case : "+ description.getMethodName() + "\n\n";
    }

    /**
     *  Called when an atomic test fails.
     * */
    public void testFailure(Failure failure) throws java.lang.Exception
    {
        output += "Execution of test case failed : "+ failure.getMessage() + "\n\n";
    }

    /**
     *  Called when a test will not be run, generally because a test method is annotated with Ignore.
     * */
    public void testIgnored(Description description) throws java.lang.Exception
    {
        output += "Execution of test case ignored : "+ description.getMethodName() +"\n\n";
    }
}