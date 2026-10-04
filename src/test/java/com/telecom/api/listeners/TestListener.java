package com.telecom.api.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.telecom.api.utilities.ExtentManager;

/**
 * TestNG listener to hook into test execution lifecycle and log results to ExtentReports.
 */
public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(testName, description != null ? description : testName);
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        testThreadLocal.set(test);
        System.out.println(">>> STARTING API TEST: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThreadLocal.get().log(Status.PASS, "API Test PASSED: " + result.getMethod().getMethodName());
        System.out.println(">>> API TEST PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        testThreadLocal.get().log(Status.FAIL, "API Test FAILED: " + testName);
        testThreadLocal.get().fail(result.getThrowable());
        System.out.println(">>> API TEST FAILED: " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String reason = (result.getThrowable() != null) ? result.getThrowable().getMessage() : "Skipped";
        testThreadLocal.get().log(Status.SKIP, "API Test SKIPPED: " + reason);
        System.out.println(">>> API TEST SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        System.out.println(">>> Extent report written to TestReport directory.");
    }
}
