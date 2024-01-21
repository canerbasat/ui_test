package com.gaugetest.driver;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class LocalBrowserExec {


    private static WebDriver driver;

    public static WebDriver LocalExec(String browser) throws Exception{

        SetBrowserForOS.setDriverPath(browser);

        switch (browser.toLowerCase(Locale.ENGLISH)) {
            case "chrome":
                driver = getChromeDriver();
                if (Boolean.parseBoolean(DriverCreater.ConfigurationProp.getString("chromeZoomCondition"))
                        && DriverCreater.ConfigurationProp.getString("chromeZoomPlatform")
                        .contains(DriverCreater.osName)) {
                    driver.get("chrome://settings/");
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
                    executor.executeScript(
                            "chrome.settingsPrivate.setDefaultZoom(" + DriverCreater.ConfigurationProp
                                    .getString("chromeZoomSize") + ");");
                }
                break;

            case "chromemobile":
                driver = getChromeDriverMobile();
                if (Boolean.parseBoolean(DriverCreater.ConfigurationProp.getString("chromeZoomCondition"))
                        && DriverCreater.ConfigurationProp.getString("chromeZoomPlatform")
                        .contains(DriverCreater.osName)) {
                    driver.get("chrome://settings/");
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
                    //executor.executeScript("chrome.settingsPrivate.setDefaultZoom(" + DriverCreater.ConfigurationProp.getString("chromeZoomSize") + ");");


                }
                break;

            case "firefox":
                driver = getFirefoxDriver();
                break;
            case "safari":
                driver = getSafariDriver();
                break;
            case "edge":
                driver = getEdgeDriver();
                break;
            case "ie":
                driver = getieDriver();
                break;
            case "opera":
                driver = getOperaDriver();
                break;
            default:
                throw new Exception("Hata");
        }

        if (DriverCreater.isFullScreen) {
            Thread.sleep(2000);
            driver.manage().window().fullscreen();
        } else if (DriverCreater.ConfigurationProp.getString("setSize").equalsIgnoreCase("true")) {
            String[] sizeValue = DriverCreater.ConfigurationProp.getString("setSizeValue").split(",");
            Thread.sleep(1000);
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window()
                    .setSize(new Dimension(Integer.parseInt(sizeValue[0]), Integer.parseInt(sizeValue[1])));
        }
        return driver;
    }

    private static ChromeDriver getChromeDriver(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--maximize");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.addArguments("ignore-certificate-errors");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("disable-plugins");
        chromeOptions.addArguments("--remote-allow-origins=*");


        chromeOptions.merge(capabilities);
        return new ChromeDriver(chromeOptions);
    }

    private static ChromeDriver getChromeDriverMobile(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 375);
        deviceMetrics.put("height", 812);
        deviceMetrics.put("pixelRatio", 3.0);
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--start-fullscreen");
        //chromeOptions.addArguments("--maximize");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.addArguments("ignore-certificate-errors");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("disable-plugins");
        chromeOptions.merge(capabilities);
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        chromeOptions.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(chromeOptions);
    }




    private static FirefoxDriver getFirefoxDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        if (Boolean.parseBoolean(DriverCreater.ConfigurationProp.getString("firefoxZoomCondition"))
                && DriverCreater.ConfigurationProp.getString("firefoxZoomPlatform")
                .contains(DriverCreater.osName)) {
            firefoxProfile.setPreference("layout.css.devPixelsPerPx",
                    DriverCreater.ConfigurationProp.getString("firefoxZoomSize"));
        }
        firefoxProfile.setPreference("dom.webnotifications.enabled", false);
        firefoxOptions.setProfile(firefoxProfile);
        firefoxOptions.merge(capabilities);
        firefoxOptions.addArguments("--remote-allow-origins=*");
        return new FirefoxDriver(firefoxOptions);
    }

    private static SafariDriver getSafariDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.merge(capabilities);
        return new SafariDriver(safariOptions);
    }

    private static EdgeDriver getEdgeDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.addArguments("ignore-certificate-errors");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("disable-plugins");
        chromeOptions.addArguments("--disable-local-storage");
        chromeOptions.setExperimentalOption("w3c", false);
        chromeOptions.setBinary(DriverCreater.ConfigurationProp.getString("edgeLocation"));
        chromeOptions.addArguments("--remote-allow-origins=*");
        EdgeOptions edgeOptions = new EdgeOptions().merge(chromeOptions);
        edgeOptions.merge(capabilities);
        return new EdgeDriver(edgeOptions);
    }


    private static InternetExplorerDriver getieDriver(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability("ignoreProtectedModeSettings", 1);
        capabilities.setCapability("nativeEvents", false);
        //capabilities.setCapability("requireWindowFocus","true");
        capabilities.setCapability("requireWindowFocus", true);
        capabilities.setCapability("enablePersistentHover", false);
        capabilities.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, true);


        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        internetExplorerOptions.introduceFlakinessByIgnoringSecurityDomains();

        internetExplorerOptions.merge(capabilities);
        return new InternetExplorerDriver(internetExplorerOptions);
    }

    private static OperaDriver getOperaDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.addArguments("--remote-allow-origins=*");
        operaOptions.merge(capabilities);
        return new OperaDriver(operaOptions);
    }


}