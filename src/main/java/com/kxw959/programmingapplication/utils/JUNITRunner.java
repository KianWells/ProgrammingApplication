package com.kxw959.programmingapplication.utils;

import com.kxw959.programmingapplication.HelloApplication;
import com.kxw959.programmingapplication.user.User;
import org.junit.internal.TextListener;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLClassLoader;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class JUNITRunner {
    public String runJunit(String className, URLClassLoader loader){
        try {
            Class<?> testClass = Class.forName("com.kxw959.programmingapplication.tasks."+ className, true, loader);
            if(User.JunitVersion == 4){
                CustomListener listener = new CustomListener();
                Result result = runJunit4(testClass, listener);
                System.out.println(result.getRunCount()-result.getFailureCount());
                return listener.output;
            }
            else if(User.JunitVersion == 5){
                TestExecutionSummary summary = runJunit5(testClass);
                System.out.println(summary.getTestsSucceededCount());
                StringWriter out    = new StringWriter();
                PrintWriter  writer = new PrintWriter(out);
                summary.printTo(writer);
                summary.printFailuresTo(writer);
                return out.toString();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public Result runJunit4(Class<?> junitClass, RunListener listener){
        JUnitCore junit = new JUnitCore();
        junit.addListener(listener);
        return junit.run(junitClass);
    }

    public TestExecutionSummary runJunit5(Class<?> junitClass){
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(junitClass))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        TestExecutionSummary summary = listener.getSummary();
        return summary;
    }
}
