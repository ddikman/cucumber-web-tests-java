package cucumberwebtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import java.io.File;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinitions {

  private WebDriver driver;
  private Scenario scenario;

  @Before
  public void initialization(Scenario scenario) {
    this.scenario = scenario;

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    driver = new ChromeDriver(options);
  }

  @After
  public void tearDown() {
    if (driver != null) {
      takeScreenshot(scenario.getName());
      driver.quit();
    }
  }

  private void takeScreenshot(String filename) {
    try {
      File screenshot =
        ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(
        screenshot,
        new File("target/screenshots/test/" + filename + ".png")
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private WebElement getSearchInput() {
    return driver.findElement(By.name("q"));
  }

  public void enterSearch(String keyword) {
    getSearchInput().sendKeys(keyword);
  }

  @Given("the user is on google top page")
  public void the_user_is_on_google_top_page() {
    driver.get("https://www.google.com");
  }

  @When("entering {string}")
  public void entering(String keyword) {
    getSearchInput().sendKeys(keyword);
  }

  @When("tapping I'm lucky")
  public void tapping_i_m_lucky() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement element = wait.until(
      ExpectedConditions.elementToBeClickable(By.name("btnI"))
    );
    element.click();
  }

  @Then("navigates to {string}")
  public void navigates_to(String expectedUrl) {
    assertEquals(expectedUrl, driver.getCurrentUrl());
  }

  @When("searching for {string}")
  public void searching_for(String keyword) {
    enterSearch(keyword);
    getSearchInput().sendKeys(Keys.ENTER);
  }

  @Then("the first match is {string}")
  public void the_first_match_is(String expectedTitle) {
    WebElement element = driver.findElement(By.cssSelector("#search div h3"));
    assertEquals(element.getText(), expectedTitle);
  }
}
