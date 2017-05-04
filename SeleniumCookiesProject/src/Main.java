import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
	static WebDriver driver;
	static WebDriverWait wait;
	BufferedReader br;
	static long startTime = System.currentTimeMillis();
	File f;

	public static void main(String[] args) throws Exception {
		{

			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\Administrator\\Desktop\\Selenium\\chromedriver.exe");

			driver = new ChromeDriver();
			// COOKIE
			File f = new File("browser.data");

			f.delete();
			f.createNewFile();
			BufferedWriter bos = new BufferedWriter(new FileWriter(f));

			for (Cookie ck : driver.manage().getCookies()) {
				bos.write((ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ ck.getExpiry() + ";" + ck.isSecure()));
				bos.newLine();
				System.out.println("Writing Cookies...");
				bos.close();

			}

			// LOGIN
			driver.get("https://www.reddit.com/login");
			String user = //redacted;
			String passwd = //redacted;

			WebElement userBox = driver.findElement(By.xpath("//*[@id=\"user_login\"]"));
			WebElement passwdBox = driver.findElement(By.xpath("//*[@id=\"passwd_login\"]"));
			WebElement saveBtn = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[5]/button"));

			userBox.sendKeys(user);
			passwdBox.sendKeys(passwd);
			saveBtn.click();

			driver.get("https://www.reddit.com/login");

			userBox = driver.findElement(By.xpath("//*[@id=\"user_login\"]"));
			passwdBox = driver.findElement(By.xpath("//*[@id=\"passwd_login\"]"));
			saveBtn = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[5]/button"));

			userBox.sendKeys(user);
			passwdBox.sendKeys(passwd);
			saveBtn.click();

			WebDriverWait wait = new WebDriverWait(driver, 5);
			boolean testElement = false;
			try {
				System.out.println("Logging In...");
				testElement = wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[@id=\"header-bottom-right\"]/form/a"), "logout"));
			} catch (Exception e) {
				e.printStackTrace();
				testElement = false;
			}

			if (testElement)
				System.out.println("You have logged in succesfully");
			else
				System.out.println("Failed to log in");

			System.out.println("Logging Out...");

			// LOG OUT
			WebElement link = driver.findElement(By.xpath("//*[@id=\"header-bottom-right\"]/form/a"));
			link.click();

			System.out.println("You have logged out succesfully");

		}

		{
			// COOKIE
			System.out.println("Loading cookies...");

			File f = new File("browser.data");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer str = new StringTokenizer(line, ";");
				while (str.hasMoreTokens()) {
					String name = str.nextToken();
					String value = str.nextToken();
					String domain = str.nextToken();
					String path = str.nextToken();
					Date expiry = null;
					String dt;

					if (!(dt = str.nextToken()).equals("null")) {
						expiry = new Date(dt);
					}
					boolean isSecure = new Boolean(str.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					driver.manage().addCookie(ck);
				}

			}

			System.out.println("Navigating to Reddit...");

			driver.get("https://www.reddit.com");
			// CHECKING LOGGED BACK IN AND TIMER.
			WebDriverWait wait = new WebDriverWait(driver, 5);
			boolean testElement = false;
			try {
				System.out.println("Logging In...");
				testElement = wait.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("//*[@id=\"header-bottom-right\"]/form/a"), "logout"));
			} catch (Exception e) {
				e.printStackTrace();
				testElement = false;
			}

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;

			if (testElement)
				System.out.println("You have logged in succesfully using the Cookie");
			else
				System.out.println("Failed to log in using the Cookie");

			System.out.println("This took: " + (totalTime / 1000) + " Seconds");
			System.out.println("Exiting...");
			driver.close();
			System.exit(0);

		}
	}
}
