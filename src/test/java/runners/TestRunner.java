package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinition"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/report.html",
                "json:target/cucumber-reports/report.json"
        },
        monochrome = true,
        dryRun = false,
        tags = "@smoke"
)
public class TestRunner extends AbstractTestNGCucumberTests {
        @BeforeClass
        public void setUp() {
                // Set system properties for headless mode in CI/CD
                if (System.getenv("BROWSER_HEADLESS") != null) {
                        System.setProperty("browser.headless", System.getenv("BROWSER_HEADLESS"));
                }
        }
}

