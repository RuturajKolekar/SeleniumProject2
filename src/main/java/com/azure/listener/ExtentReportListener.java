package com.azure.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.azure.base.TestBase;
import com.azure.utils.Utils;





public class ExtentReportListener extends TestBase implements ITestListener {

	private static final String OUTPUT_FOLDER = "./reports/";
	private static  String FILE_NAME = "TestExecutionReport.html";

	private static ExtentReports extent = init();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static ExtentReports extentReports;
	public static String timestamp;
	public static ExtentTest extentTest;
	public static String path;
	

	private static ExtentReports init() {

		 timestamp= new SimpleDateFormat("YYYY.MM.dd-hh.mm.ss").format(new Date());
		 FILE_NAME = "TestExecutionReport "+timestamp+".html";
		Path path = Paths.get(OUTPUT_FOLDER);

		// if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}
		ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME );

		reporter.config().setReportName("Ruturaj Test Results");
		reporter.config().setDocumentTitle("Selenium Azure Project");
		reporter.config().setTheme(Theme.DARK);

		extentReports = new ExtentReports();
		extentReports.attachReporter(reporter);
		extentReports.setSystemInfo("System", "Windows");
		extentReports.setSystemInfo("Author", "Ruturaj Kolekar");
		extentReports.setSystemInfo("Build#", "1.1");
		extentReports.setSystemInfo("Team", "RahulShetty");
		extentReports.setSystemInfo("Customer Name", "AzureDevops");

		return extentReports;
	}
	
	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	@Override
	public synchronized void onStart(ITestContext context) {
		System.out.println("Test Suite started!");
		context.setAttribute("WebDriver", driver);
		log.info("I am in onStart method " + context.getName());
		
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		System.out.println(("Test Suite is ending!"));
		log.info("I am in onFinish method " + context.getName());
		extent.flush();
		test.remove();
	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);
		
		log.info(getTestMethodName(result) + " test is starting.");

		System.out.println(methodName + " started!");
		 extentTest = extent.createTest(result.getMethod().getMethodName(),
				result.getMethod().getDescription());

		extentTest.assignCategory(result.getTestContext().getSuite().getName());
		/*
		 * methodName = StringUtils.capitalize(StringUtils.join(StringUtils.
		 * splitByCharacterTypeCamelCase(methodName), StringUtils.SPACE));
		 */
		extentTest.assignCategory(className);
		
		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}

	public synchronized void onTestSuccess(ITestResult result) {
		log.info(getTestMethodName(result) + " test is succeed.");
		System.out.println((result.getMethod().getMethodName() + " passed!"));
		test.get().pass("Test passed");
		extentTest.log(Status.PASS,MarkupHelper.createLabel("Name of the Pass TestCase is : "+result.getName(), ExtentColor.GREEN) );

		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	public synchronized void onTestFailure(ITestResult result) {
		log.info(getTestMethodName(result) + " test is failed.");
		System.out.println((result.getMethod().getMethodName() + " failed!"));
		extentTest.log(Status.FAIL,MarkupHelper.createLabel("Name of the Fail TestCase is : "+result.getName(), ExtentColor.RED) );
		
		 path = System.getProperty("user.dir") + "/screenshot/" + result.getName()+result.getEndMillis()+ ".png";
		// Utils.getScreenshot(path);
		//extentTest.addScreenCaptureFromPath("path");		
		test.get().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(Utils.getScreenshot(path)).build());
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	public synchronized void onTestSkipped(ITestResult result) {
		log.info(getTestMethodName(result) + " test is skipped.");
		System.out.println((result.getMethod().getMethodName() + " skipped!"));
		extentTest.log(Status.SKIP,MarkupHelper.createLabel("Name of the Skip TestCase is : "+result.getName(), ExtentColor.YELLOW) );
		
		path = System.getProperty("user.dir") + "/screenshot/" + result.getName()+result.getEndMillis() + ".png";
//		Utils.getScreenshot(path);
//		extentTest.addScreenCaptureFromPath("path");
		
		test.get().skip(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(Utils.getScreenshot(path)).build());
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
		log.info("Test failed but it is in defined success ratio " + getTestMethodName(result));
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

}
