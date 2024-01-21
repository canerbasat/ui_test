package com.gaugetest.methods;

import com.gaugetest.driver.DriverCreater;
import com.gaugetest.utils.ExcelReader;
import com.thoughtworks.gauge.Step;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import org.openqa.selenium.StaleElementReferenceException;
import com.gaugetest.helper.ElementHelper;
import com.gaugetest.helper.StoreHelper;
import com.gaugetest.model.ElementInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

public class Methods extends DriverCreater {

    /*
     * Bu class'ta Selenium, Assertion, Javascript odaklı metodlar kullanılarak fonksiyonlar geliştirilir.
     *
     * Bu class, 'pages' package'ındaki classları besler.
     *
     * Eger geliştirilen fonksiyonun içerisinde Assertion mevcut ise, aynı fonksiyonun polymorphism
     * mantığında 'assertionFail' mesajını parametre olarak alan versiyonunun da implemente edilmesi beklenmektedir.
     *
     * Not: fail mesajlarının türkçeye çevirilmesi gerekli
     * */


    public static int MAX_ITERATION_COUNT = 250;
    public static int MILLISECOND_WAIT_AMOUNT = 150;

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(Methods.class);

    private static String SAVED_ATTRIBUTE;

    private final Actions actions = new Actions(driver);

    public Methods(){
        PropertyConfigurator.configure(Methods.class.getClassLoader().getResource("log4j.properties"));
    }

    public void clickElement(String key) {

        findElement(key).click();
        logger.info("Elemente tıklandı.");
    }

    public WebElement findElement(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 0);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }



    private WebElement findElementIsClickable(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 0);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.elementToBeClickable(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public List<WebElement> findElements(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return driver.findElements(infoParam);
    }

    private void clickElementByKeyWithHover(WebElement element){
        element.click();
    }

    private void clickElementBy(String key){
        findElement(key).click();
    }

    public void hoverElement(WebElement element){
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key){
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key){
        findElement(key).sendKeys(Keys.ESCAPE);

    }
    public void sendKeyENTER(String key){
        findElement(key).sendKeys(Keys.ENTER);

    }

    private boolean isDisplayed(WebElement element){
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by){
        return driver.findElement(by).isDisplayed();
    }




    private String getPageSource(){
        return driver.switchTo().alert().getText();
    }

    public static String getSavedAttribute(){
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength){

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }

        return stringRandom;
    }

    public String getElementText(String key){
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute){
        return findElement(key).getAttribute(attribute);
    }

    public void printPageSource(){
        System.out.println(getPageSource());
    }

    public void javaScriptClicker(WebElement element){

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    @Step("<int> saniye bekle")
    public void waitBySeconds(int seconds){
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("<int> milisaniye bekle")
    public void waitByMilliseconds(long milliseconds){
        try {
            //      logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkElementExistsThenClick(String key){
        findElementByKeyWithLoop(key);
        clickElementByKeyWithHover(key);
    }

    public void clickElementByKeyWithHover(String key){
        WebElement element = findElement(key);
        hoverElement(element);
        waitByMilliseconds(500);
        clickElementByKeyWithHover(element);
        logger.info(key + " elementine tıklandı.");
    }


    public void hoverToElement(String key){
        WebElement element = findElement(key);
        hoverElement(element);
        waitByMilliseconds(500);
        logger.info(key + " elementinin üzerine gelindi.");
    }

    public void clickElementWithHover(WebElement element){
        hoverElement(element);
        waitByMilliseconds(1000);
        clickElementByKeyWithHover(element);
    }
    public void javaScriptScrollElementCenter(String byValue, String selectorType) {
        javascriptExecutor(getjsFindString(byValue, selectorType) + ".scrollIntoView();");
        javascriptExecutor(getjsFindString(byValue, selectorType) + ".focus();");
        javascriptExecutor(getjsFindString(byValue, selectorType) + ".scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})");
    }

    public  String getjsFindString(String byValue, String selectorType){

        String jsString = "";
        switch (selectorType){

            case "id":
                jsString ="document.getElementById(\""+ byValue +"\")";
                break;

            case "name":
                jsString ="document.getElementsByName(\""+ byValue +"\")[0]";
                break;

            case "class":
                jsString ="document.getElementsByClassName(\""+ byValue +"\")[0]";
                break;

            case "css":
                jsString ="document.querySelector(\""+ byValue +"\")";
                break;
            //querySelectorAll
            case "xpath":
                jsString = "arguments[0]";
                break;

            default:
                Assert.fail("HATA");
                break;
        }

        return jsString;
    }


    public void clickElementWithFocus(String key){
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile tıklandı.");
    }


    public WebElement findElementByKeyWithLoop(String key, String assertionFailMessage, int iteration,
                                               int millisecond){
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < iteration) {
            try {
                webElement = findElement(key);
                logger.info("Element:'" + key + "' bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliseconds(millisecond);
        }
        Assert.fail(assertionFailMessage);
        return null;
    }



    public WebElement findElementByKeyWithLoopWithoutAssert(String key,int iteration,
                                                            int millisecond){
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < iteration) {
            try {
                webElement = findElement(key);
                logger.info("Element:'" + key + "' bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliseconds(millisecond);
        }
        return null;
    }

    public WebElement findElementByKeyWithLoopWithoutAssert(String key){
        String defaultFailMessage = "Element: '" + key + "' bulunamadı.";
        return findElementByKeyWithLoopWithoutAssert(key);
    }


    public List<WebElement> findElementsByKeyWithLoop(String key, String assertionFailMessage, int iteration,
                                                      int millisecond){
        List<WebElement> webElements;
        int loopCount = 0;
        while (loopCount < iteration) {
            try {
                webElements = findElements(key);
                logger.info("Element:'" + key + "' bulundu.");
                return webElements;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliseconds(millisecond);
        }
        Assert.fail(assertionFailMessage);
        return null;
    }



    public WebElement findElementByKeyWithLoop(String key, String assertionFailMessage){
        return findElementByKeyWithLoop(key, assertionFailMessage, MAX_ITERATION_COUNT,
                MILLISECOND_WAIT_AMOUNT);
    }
    public WebElement findElementByKeyWithLoop(String key){
        String defaultFailMessage = "Element: '" + key + "' bulunamadı.";
        return findElementByKeyWithLoop(key, defaultFailMessage);
    }


    public void goToUrl(String url){
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    public WebElement findElementByCssWithLoop(String css, String assertionFailMessage){
        int loopCount = 0;
        List<WebElement> elements;
        while (loopCount < MAX_ITERATION_COUNT) {
            elements = driver.findElements(By.cssSelector(css));
            if (elements.size() > 0) {
                logger.info(css + " elementi bulundu.");
                return elements.get(0);
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
        return null;
    }

    public void findElementByCssWithLoop(String css){
        String defaultFailMessage = "Element: '" + css + "' doesn't exist.";
        findElementByCssWithLoop(css, defaultFailMessage);
    }

    public WebElement findElementByXpathWithLoop(String xpath, String assertionFailMessage){
        int loopCount = 0;
        List<WebElement> elements;
        while (loopCount < MAX_ITERATION_COUNT) {
            elements = driver.findElements(By.xpath(xpath));
            if (elements.size() > 0) {
                logger.info(xpath + " elementi bulundu.");
                return elements.get(0);
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
        return null;
    }

    public WebElement findElementByXpathWithLoop(String xpath){
        String defaultFailMessage = "Element: '" + xpath + "' bulunamadı.";
        return findElementByXpathWithLoop(xpath, defaultFailMessage);
    }

    public void waitElementToDisappear(String key, String assertionFailMessage){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void waitElementToDisappear(String key){
        String defaultFailMessage = "Element '" + key + "' halen mevcut.";
        waitElementToDisappear(key, defaultFailMessage);
    }

    public void uploadFile(String path, String key){
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyası " + key + " elementine yüklendi.");
    }

    public void writeValueToElement(String text, String key){
        findElement(key).sendKeys(text);
        logger.info(key + " elementine " + text + " texti yazıldı.");

    }

    public void javascriptClickerWithCss(String css, String assertionFailMessage){
        Assert.assertTrue(assertionFailMessage, isDisplayedBy(By.cssSelector(css)));
        javaScriptClicker(driver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " tıklandı.");
    }

    public void javascriptClickerWithCss(String css){
        String defaultFailMessage = "Element '" + css + "'bulunamadı";
        javascriptClickerWithCss(css, defaultFailMessage);
    }

    public void javascriptClickerWithXpath(String xpath, String assertionFailMessage){
        Assert.assertTrue(assertionFailMessage, isDisplayedBy(By.xpath(xpath)));
        javaScriptClicker(driver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " tıklandı.");
    }

    public void javascriptClickerWithXpath(String xpath){
        String defaultFailMessage = "Element '" + xpath + "' bulunamadı";
        javascriptClickerWithXpath(xpath, defaultFailMessage);
    }

    public void checkURLContains(String expectedURL, String assertionFailMessage){
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void checkURLContains(String expectedURL){
        String defaultFailMessage = "Actual URL doesn't match the expected.";
        checkURLContains(expectedURL, defaultFailMessage);
    }

    public void sendKeyToElementTAB(String key){
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yollandı.");
    }

    public void sendKeyToElementBACKSPACE(String key){
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandı.");
    }

    public void sendKeyToElementESCAPE(String key){
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yollandı.");
    }

    public void checkElementAttributeExists(String key, String attribute,
                                            String assertionFailMessage){
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void checkElementAttributeExists(String key, String attribute){
        String defaultFailMessage = "Element DOESN't have the attribute: '" + attribute + "'";
        checkElementAttributeExists(key, attribute, defaultFailMessage);
    }

    public void checkElementAttributeNotExists(String key, String attribute,
                                               String assertionFailMessage){
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void checkElementAttributeNotExists(String key, String attribute){
        String defaultFailMessage = "Element STILL have the attribute: '" + attribute + "'";
        checkElementAttributeNotExists(key, attribute, defaultFailMessage);
    }

    public void checkElementAttributeEquals(String key, String attribute, String expectedValue,
                                            String assertionFailMessage){
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void checkElementAttributeEquals(String key, String attribute, String expectedValue){
        String defaultFailMessage = "Element's attribute value doesn't match expected value";
        checkElementAttributeEquals(key, attribute, expectedValue, defaultFailMessage);
    }

    public void checkElementAttributeContains(String key, String attribute, String expectedValue,
                                              String assertionFailMessage){
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void checkElementAttributeContains(String key, String attribute, String expectedValue){
        String defaultFailMessage = "Element's attribute value doesn't contain expected value";
        checkElementAttributeContains(key, attribute, expectedValue, defaultFailMessage);
    }

    public void setElementAttribute(String value, String attributeName, String key){
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    public void setElementAttributeWithJavascript(String value, String attributeName, String key){
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
                webElement);
    }

    public void clearElement(String key){
        findElement(key).clear();
    }

    public void saveAttributeValueOfElement(String attribute, String key){
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    public void writeSavedAttributeToElement(String key){
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    public void checkElementContainsText(String key, String expectedText,
                                         String assertionFailMessage){
        Boolean containsText = findElement(key).getText().contains(expectedText);
        Assert.assertTrue(assertionFailMessage, containsText);

    }

    public void checkElementContainsText(String key, String expectedText){
        String defaultFailMessage = "Expected text is not contained";
        checkElementContainsText(key, expectedText, defaultFailMessage);
    }

    public void writeRandomValueToElement(String key){
        findElement(key).sendKeys(randomString(11));
    }

    public void writeRandomValueToElement(String key, String startingText){
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    public void printElementText(String css){
        System.out.println(driver.findElement(By.cssSelector(css)).getText());
    }

    public void sendKeysWithFocus(String text, String key){
        actions.moveToElement(findElement(key));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
    }

    public void refreshPage(){
        driver.navigate().refresh();
    }

    public void chromeZoomOut(String value){
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    public void chromeOpenNewTab(){
        ((JavascriptExecutor) driver).executeScript("window.open()");
    }

    public void chromeFocusTabWithNumber(int number){
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(number - 1));
    }

    public void chromeFocusLastTab(){
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    public void chromeFocusFrameWithNumber(String key){
        WebElement webElement = findElement(key);
        driver.switchTo().frame(webElement);
    }

    public void checkUrlForAppStart(){
        String urlName;
        boolean trueURL = false;

        while (trueURL==false){
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size()-1));
            urlName = driver.getCurrentUrl();
            if(!urlName.contains("flypgs")){
                driver.close();
            }else {
                trueURL=true;
            }
        }
    }


    public void chromeAlertPopupAccept(){
        driver.switchTo().alert().accept();
    }

    public void clickElementWithStaleException(String key, String assertionFailMessage){
        int loopCount = 0;
        while (loopCount < MAX_ITERATION_COUNT) {
            try {
                findElement(key).click();
                logger.info("Element:'" + key + "' StaleException ile tıklandı.");
                return;
            } catch (StaleElementReferenceException e) {
            }
            loopCount++;
            waitByMilliseconds(MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(assertionFailMessage);
    }

    public void clickElementWithStaleException(String key){
        String defaultFailMessage = "Element:'" + key + "' StaleException ile tıklanamadı.";
        clickElementWithStaleException(key, defaultFailMessage);
    }

    public void print(String message){
        System.out.println(message);
    }

    public int[] getTodayDateWithPlus(int plusDay, int plusMonth, int plusYear){
        LocalDate calculatedDate = LocalDate.now().plusDays(plusDay).plusMonths(plusMonth)
                .plusYears(plusYear);
        int day = calculatedDate.getDayOfMonth();
        int month = calculatedDate.getMonthValue();
        int year = calculatedDate.getYear();
        return new int[]{day, month, year};
    }

    public WebElement waitForElementVisibility(String key, int maxWaitTime){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        WebDriverWait wait = new WebDriverWait(driver, maxWaitTime);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    //*[text()='FİRMA LİSTELE']
    public WebElement waitForElementVisibility(WebElement webElement, int maxWaitTime){
        WebDriverWait wait = new WebDriverWait(driver, maxWaitTime);
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public boolean isElementVisible(String key){
        boolean isVisible;
        try {
            isVisible = findElement(key).isDisplayed();
        } catch (WebDriverException e) {
            logger.info(e.getMessage());
            return false;
        }
        return isVisible;
    }


    public boolean isElementClickable(String key){
        WebElement webElement = findElement(key);
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            return true;
        } catch (Exception e) {
            logger.info("Element '" + key + "' tıklanabilir değil.");
            return false;
        }
    }


    public void deneme(){
        if (findElement("").equals("")){
            List<WebElement> denemeList = findElements("");
            denemeList.get(0).click();

            WebDriverWait wait = new WebDriverWait(driver,50);
            wait.until(ExpectedConditions.elementToBeClickable(findElement((""))));

        }
    }


    public void javascriptExecutor(String script){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }

    public void javascriptExecutor(String script, WebElement webElement){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script, webElement);
    }

    public void scrollToElement(WebElement element){
        javascriptExecutor(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                element);
    }

    public int[] getDate(int plusDay, int plusMonth, int plusYear) {
        LocalDate calculatedDate = LocalDate.now().plusDays(plusDay).plusMonths(plusMonth).plusYears(plusYear);
        int day = calculatedDate.getDayOfMonth();
        int month = calculatedDate.getMonthValue();
        int year = calculatedDate.getYear();
        return new int[]{day, month, year};
    }

    public String createRandomPhoneNumber() {
        int secondAreaCodeNumber, thirdAreaCodeNumber;
        int numberBlock;
        Random generator = new Random();
        secondAreaCodeNumber = generator.nextInt(8);
        thirdAreaCodeNumber = generator.nextInt(8);
        numberBlock = generator.nextInt(8999999) + 1000000;
        return "5" + Integer.toString(secondAreaCodeNumber) + Integer.toString(thirdAreaCodeNumber) + " " +
                Integer.toString(numberBlock);
    }


    public double removePriceSuffixAndConvertDouble(String priceText) {
        String[] elementStringList = priceText.trim().split(" ");
        String elementString = elementStringList[0].replaceAll(",", "");
        return Double.parseDouble(elementString);
    }


    public String randomTCNo() {
        Vector<Integer> array = new Vector<Integer>();
        Random randomGenerator = new Random();

        array.add(new Integer(1 + randomGenerator.nextInt(9)));
        for (int i = 1; i < 9; i++) array.add(randomGenerator.nextInt(10));
        int t1 = 0;
        for (int i = 0; i < 9; i += 2) t1 += array.elementAt(i);
        int t2 = 0;
        for (int i = 1; i < 8; i += 2) t2 += array.elementAt(i);
        int x = (t1 * 7 - t2) % 10;
        array.add(new Integer(x));
        x = 0;
        for (int i = 0; i < 10; i++) x += array.elementAt(i);
        x = x % 10;
        array.add(new Integer(x));
        String TCNo = "";
        for (int i = 0; i < 11; i++) TCNo = TCNo + Integer.toString(array.elementAt(i));
        return TCNo;
    }

    public boolean priceControl(double firstPrice, double priceAtPage) {
        return firstPrice - 1.00 < priceAtPage && priceAtPage < firstPrice + 1.00;
    }

    public boolean doesElementHaveAttributeValue(WebElement webElement,String attribute,String value){

        int count = 0;
        while(true){
            count++;

            waitBySeconds(2);

            if(webElement.getAttribute(attribute).trim().equals(value)){
                return true;
            }
            if(count == 50){
                return false;
            }
        }
    }


    public void scrollToElement(String key){
        scrollToElement(findElement(key));
    }

    public void chooseByVisibleTextFromSelectElement(String key,String value) {
        Select select = new Select(findElement(key));
        select.selectByValue(value);
    }


    public void chooseByIndexFromSelectElement(String key,int index) {
        Select select = new Select(findElement(key));
        select.selectByIndex(index);
    }



    public void choosenIndexAtSelectElementContains(String key,int index) {
        Select select = new Select(findElement(key));
        select.selectByIndex(index-1);

        WebElement selectedOption = select.getFirstSelectedOption();

        String selectedOptionText = selectedOption.getText();
        logger.info("Seçili olan option: " + selectedOptionText);

        // Belirli bir metni içeriyor mu kontrol et ve logla
        if (selectedOptionText.contains(String.valueOf(index))) {
            logger.info("Seçili olan option belirli metni içeriyor.");
        } else {
            logger.info("Seçili olan option belirli metni içermiyor.");
        }

    }



}
