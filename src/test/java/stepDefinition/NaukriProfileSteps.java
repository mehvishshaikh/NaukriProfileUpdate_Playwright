package stepDefinition;

import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import utils.BrowserManager;
import com.microsoft.playwright.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NaukriProfileSteps {

    private BrowserManager browserManager;
    private Page page;
    private ConfigReader configReader;

    @Before
    public void setUp() {
        this.browserManager = new BrowserManager();
        this.configReader = new ConfigReader();
        System.out.println("========== Test Setup Started ==========");
    }

    @After
    public void tearDown() {
        System.out.println("========== Test Teardown Started ==========");
        browserManager.closeBrowser();
    }

    @Given("I launch the Naukri application")
    public void launchNaukriApplication() {
        page = browserManager.initializeBrowser();
        page.navigate(configReader.getNaukriURL());

        // Wait for page readiness (instead of NETWORKIDLE)
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        System.out.println("✓ Navigated to: " + configReader.getNaukriURL());
    }

    @Given("I wait for the page to load")
    public void waitForPageLoad() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        System.out.println("✓ Page loaded successfully");
    }

    // 🔹 Login button inside login form (submit button)
    @When("I click the login button")
    public void clickLoginButton() {

        if (System.getenv("CI") != null) {
            System.out.println("⚠ Skipping login click in CI");
            return;
        }

        try {
            Locator loginBtn = page.locator("button[type='submit']");
            loginBtn.waitFor();
            loginBtn.click();

            System.out.println("✓ Login button clicked");
        } catch (Exception e) {
            System.out.println("✗ Error clicking login button: " + e.getMessage());
            throw e;
        }
    }

    // 🔹 Home page login button (top navbar)
    @Then("I Click on the Login button")
    public void iClickOnTheLoginButton() {

        if (System.getenv("CI") != null) {
            System.out.println("Skipping login in CI (using saved session)");
            return;
        }

        try {
            Locator loginLink = page.locator("#login_Layer");
            loginLink.waitFor();
            loginLink.click();

            System.out.println("✓ Login button clicked from home page");

        } catch (Exception e) {
            System.out.println("✗ Error clicking Login button: " + e.getMessage());
            page.screenshot(
                    new Page.ScreenshotOptions().setPath(
                            java.nio.file.Paths.get("screenshots/login_error.png")
                    )
            );
            throw e;
        }
    }

    @When("I enter valid credentials")
    public void enterValidCredentials() {

        if (System.getenv("CI") != null) {
            System.out.println("Skipping login in CI (using saved session)");
            return;
        }

        String username = configReader.getUsername();
        String password = configReader.getPassword();

        page.getByPlaceholder("Enter your active Email ID / Username").fill(username);
        page.locator("input[type='password']").fill(password);

        System.out.println("✓ Valid credentials entered");
    }

    @Then("I should be redirected to the dashboard")
    public void verifyDashboardRedirect() {
        try {
            page.waitForURL("**/mnjuser/homepage");

            assertThat(page)
                    .hasURL(Pattern.compile(".*/mnjuser/homepage"));
            System.out.println("✓ Redirected to dashboard");

            // SAVE SESSION (ONLY LOCAL)
            if (System.getenv("CI") == null) {
                page.context().storageState(
                        new BrowserContext.StorageStateOptions()
                                .setPath(Paths.get("auth.json"))
                );
                System.out.println("✓ Session saved to auth.json");
            }
        } catch (Exception e) {
            System.out.println("✗ Error verifying redirect: " + e.getMessage());
            throw e;
        }
    }

    @And("I click on the view profile button")
    public void iClickOnTheViewProfileButton() {
        try {
            Locator viewProfile = page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("View profile"));

            viewProfile.waitFor();
            viewProfile.click();

            page.waitForURL("**/mnjuser/profile");

            System.out.println("✓ View profile button clicked");
        } catch (Exception e) {
            System.out.println("✗ Error clicking view profile button: " + e.getMessage());
            throw e;
        }
    }

    @Then("I edit the resume headline")
    public void iEditTheResumeHeadline() {
        try {
            Locator editIcon = page.locator("//div[@class='widgetHead']//span[text()='editOneTheme']");
            editIcon.waitFor();
            editIcon.click();

            System.out.println("✓ Headline edit button clicked");
        } catch (Exception e) {
            System.out.println("✗ Error editing headline: " + e.getMessage());
            throw e;
        }
    }

    @And("I save the changes")
    public void iSaveTheChanges() {
        try {
            Locator saveBtn = page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Save"));

            saveBtn.waitFor();
            saveBtn.click();

            System.out.println("✓ Changes saved successfully");
        } catch (Exception e) {
            System.out.println("✗ Error saving changes: " + e.getMessage());
            throw e;
        }
    }

    @Then("I should see a confirmation message for the profile update")
    public void iShouldSeeAConfirmationMessageForTheProfileUpdate() {

        assertThat(
                page.getByText(
                        Pattern.compile("Profile updated successfully", Pattern.CASE_INSENSITIVE)
                )
        ).isVisible();

        System.out.println("✓ Confirmation message verified");
    }
}