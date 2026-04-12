package utils;

import com.microsoft.playwright.*;
import config.ConfigReader;

import java.util.Arrays;

public class BrowserManager {
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private Playwright playwright;
    private ConfigReader configReader;

//    public Page initializeBrowser() {
//        playwright = Playwright.create();
//
//        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
//                .setChannel("chrome").setHeadless(true).setArgs(Arrays.asList("--start-maximized"));
//
//        browser = playwright.chromium().launch(launchOptions);
//        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
//        page = context.newPage();
//
//        return page;
//    }
    public BrowserManager(){
        this.configReader = new ConfigReader();
    }

    public Page initializeBrowser() {
        playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(configReader.isHeadlessBrowser())
                .setChannel("chrome")
                .setSlowMo(100)
                .setArgs(Arrays.asList(
                        "--disable-blink-features=AutomationControlled",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                ));

        browser = playwright.chromium().launch(launchOptions);

        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080)
                        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/146.0.0.0 Safari/537.36")
        );

        page = context.newPage();

        return page;
    }

    public void closeBrowser() {
        try {
            if (page != null) {
                page.close();
            }
            if (context != null) {
                context.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
            System.out.println("✓ Browser closed successfully");
        } catch (Exception e) {
            System.out.println("✗ Error closing browser: " + e.getMessage());
        }
    }
}
