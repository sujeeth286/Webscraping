package Sam;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class Naukriscraping 
{
	
	public static String url1="https://naukri.com/";
	public static String job_textfield_path="//input[@placeholder='Enter skills / designations / companies']";
	public static String searchbutton_path="//div[@class='qsbSubmit']";
	public static String pagination_path="//div[@class='fleft pages']/a";
	public static String jobs_path="//div[@class='info fleft']/a";
	public static String company_path="//div[@class='info fleft']/div/a[1]";
	public static String value= "api testing"; 
	public static WebDriver driver;
	public static String excel_path="C:\\Users\\sujee\\OneDrive\\Desktop\\Joblist.xlsx";
	public static String excel_sheet="Sheet1";
   
	@Test
	
	
	public static void openBrowser() throws InterruptedException, EncryptedDocumentException, IOException
	{
		System.setProperty("webdriver.chrome.driver", "./Software/chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get(url1);
		driver.findElement(By.xpath(job_textfield_path)).sendKeys(value);
		driver.findElement(By.xpath(searchbutton_path)).click();
		Thread.sleep(4000);
	}
		
	@Test(priority=1)
	public static void jobpages() throws InterruptedException, EncryptedDocumentException, IOException
	{

		FileInputStream fis = new FileInputStream(excel_path);
		Workbook book = WorkbookFactory.create(fis);
		
		List<WebElement> pagination = driver.findElements(By.xpath(pagination_path));
		for(int i=1;i<=2;i++)
		{
			Thread.sleep(4000);
			List<WebElement> jobs = driver.findElements(By.xpath(jobs_path));
			List<WebElement> companies = driver.findElements(By.xpath(company_path));
			Sheet sh = book.getSheet(excel_sheet);
			Thread.sleep(1000);
			FileOutputStream fout= new FileOutputStream(excel_path);
			
			for(int j=0;j<jobs.size();j++)
			{
				String job = jobs.get(j).getAttribute("title");
				
				String company = companies.get(j).getText();

				
				
				Row row = sh.createRow(j);
				Thread.sleep(1000);
				Cell cell = row.createCell(i);
				Thread.sleep(1000);
				
				cell.setCellValue(job + " and " + company);
				Thread.sleep(1000);
				
				Reporter.log("Page " + i + " jobs are " + j +" "+ job ,true);
				Reporter.log(company,true);
				
				book.write(fout);
				
			}
			
			
			
			
			String no = Integer.toString(i+1);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@class='fleft pages']/a[.='"+no+"']")).click();		
			Thread.sleep(10000);
	
		}
		driver.close();
		
	}
}


