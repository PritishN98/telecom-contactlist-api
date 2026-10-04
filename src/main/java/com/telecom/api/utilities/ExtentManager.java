package com.telecom.api.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Manages the ExtentReports instance for API execution reporting.
 */
public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir = System.getProperty("user.dir") + File.separator + "TestReport";
        File dir = new File(reportDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String reportPath = reportDir + File.separator + "Telecom_API_Report_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Telecom Domain - Contact List API Test Report");
        sparkReporter.config().setReportName("Contact List API Automation Execution Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(sparkReporter);
        reports.setSystemInfo("Project", "Telecom Domain - Contact List API Testing");
        reports.setSystemInfo("Application", "thinking-tester-contact-list.herokuapp.com");
        reports.setSystemInfo("Tester", System.getProperty("user.name"));
        reports.setSystemInfo("Environment", "QA");
        reports.setSystemInfo("OS", System.getProperty("os.name"));
        reports.setSystemInfo("Java Version", System.getProperty("java.version"));

        return reports;
    }
}
