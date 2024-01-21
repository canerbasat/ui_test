package com.gaugetest.steps;

import com.gaugetest.driver.DriverCreater;
import com.gaugetest.helper.ElementHelper;
import com.gaugetest.helper.StoreHelper;
import com.gaugetest.methods.Methods;
import com.gaugetest.model.ElementInfo;
import com.gaugetest.utils.ExcelReader;
import com.gaugetest.utils.TxtWriter;
import com.thoughtworks.gauge.Step;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Implementation extends DriverCreater {


    private Logger logger = LoggerFactory.getLogger(getClass());

    Methods methods = new Methods();
    JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
    String highlightJavascript = "arguments[0].setAttribute('style', 'border: 4px solid red;');";


    @Step("<key> li elementi bul ve değerini <saveKey> olarak sakla")
    public void saveTextByKey(String key, String saveKey) throws InterruptedException {
        Thread.sleep(1000);
        StoreHelper.INSTANCE.saveValue(saveKey, methods.findElementByKeyWithLoop(key).getText());
        Thread.sleep(2000);
    }


    @Step("<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır ve değişiklik oldugunu dogrula")
    public void equalsSaveTextByKeyNotequal(String key, String saveKey) {
        Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), methods.findElement(key).getText());
    }

    @Step("<key> elementine tıklanır")
    public void clickElementByKeyWithHover(String key) {
        WebElement element = methods.findElement(key);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        methods.waitByMilliseconds(500);
        methods.hoverElement(element);
        methods.waitByMilliseconds(500);
        jsDriver.executeScript(highlightJavascript, element);
        methods.clickElementByKeyWithHover(key);
        logger.info(key + " elementine tıklandı.");
    }

    @Step("<key> elementine js ile tıklanır")
    public void clickElementByKeyWitJS(String key) {
        WebElement element = methods.findElement(key);
        methods.waitByMilliseconds(500);
        methods.hoverElement(element);
        methods.waitByMilliseconds(500);
        jsDriver.executeScript(highlightJavascript, element);
        methods.javaScriptClicker(element);
        logger.info(key + " elementine tıklandı.");
    }

    @Step("<key> elementinin uzerine gelinir")
    public void hoverToElement(String key) {
        WebElement element = methods.findElement(key);
        methods.waitByMilliseconds(500);
        methods.hoverElement(element);
        methods.waitByMilliseconds(1000);
        logger.info(key + " elementinin üzerine gelindi.");
    }

    @Step("<key> elementine <text> değeri yazilir")
    public void writeValueToElement(String key, String text) {
        methods.findElement(key).sendKeys(text);
        logger.info(key + " elementine " + text + " texti yazıldı.");
    }

    @Step("<key> elementine excelden <int> satirdaki <int> sutundaki değer yazilir")
    public void writeValueToElementFromExcel(String key, int row,int cell) {

        String readedValue = ExcelReader.readFieldFromExcel(row,cell);
        methods.findElement(key).sendKeys(readedValue);
        logger.info(key + " elementine " + readedValue + " texti yazıldı.");
    }




    @Step("<key> elementi temizlenir")
    public void clearElement(String key) throws InterruptedException {
        methods.findElement(key).clear();
        Thread.sleep(10000);
        logger.info(key + " elementi temizlendi.");
    }


    @Step("<key> elementinde klavyeden enter gonderilir")
    public void sendKeyEnter(String key) throws InterruptedException {
        methods.sendKeyENTER(key);
        Thread.sleep(1000);
        logger.info(key + " elementinde klavyeden enter gonderildi.");
    }


    @Step("<key> elementi <expectedText> değerini içermektedir")
    public void checkElementContainsText(String key, String expectedText) {
        String defaultFailMessage = "Element bu değeri içermemektedir.";
        methods.checkElementContainsText(key, expectedText, defaultFailMessage);
    }

    @Step("<key> elementi görünene kadar beklenir")
    public WebElement waitForElementVisibility(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    @Step("<key> elementin görünmemesi beklenir")
    public void waitForElementNotFound(String key) throws Exception {
        List<WebElement> elementList = methods.findElements(key);
        if (elementList.size() == 0) {
            throw new Exception("Element görünmemesi gerekirken göründü.");
        } else {
            logger.info("Element başarılı görülmedi.");
        }
    }


    @Step("Mevcut sayfanin <urlS> olması beklenir")
    public  void checkURLContains(String expectedURL) {
        methods.waitByMilliseconds(3);
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < methods.MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            methods.waitByMilliseconds(methods.MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(expectedURL + " gelmesi beklenirken gelen url : " + actualURL);
    }

    @Step("<key> elementinin <attiribute> degeri <localVar> degiskenine kaydedilir")
    public void saveSpesAttiributeOrText(String key, String attiribute, String localVar) {
        List<WebElement> element = methods.findElements(key);
        if(attiribute.equals("text")){
            String val = element.get(0).getText();
            logger.info("KAYDET");
            logger.info(val);
            logger.info(val);
            logger.info(val);
            StoreHelper.INSTANCE.saveValue(localVar, val);
        }else{
            String val = element.get(0).getAttribute(attiribute);
            StoreHelper.INSTANCE.saveValue(localVar, val);
        }

    }

    @Step("<key> elementi listeye atilir ve rastgele birine tiklanir")
    public void clicRandomFromList(String key) {
        List<WebElement> elementList = methods.findElements(key);
        int randomIndex = new Random().nextInt(elementList.size());
        WebElement selectedElement = elementList.get(randomIndex);
        selectedElement.click();
    }


    @Step("<key> elementinin degeri txt dosyasına kaydedilir")
    public void checkElementContainsValue(String key) {
        List<WebElement> elements = methods.findElements(key);
        if (!elements.isEmpty()) {
            String textValue = elements.get(0).getText();

            if (textValue.matches("\\d.*")) {
                // Sayı ile başlıyorsa virgül sonrasındaki tüm karakterleri kaldırma
                textValue = textValue.replaceAll("\\s+|TL", "");
                textValue = textValue.replaceAll(",.*", "");
            } else {
            }

            TxtWriter.writeToTxt(textValue);
            logger.info(key + " elementinde bulunan " + textValue + " değeri txt dosyasına yazıldı.");
        } else {
            logger.error("Belirtilen key'e sahip element bulunamadı.");
        }
    }

    @Step("<key> elementinin <elementValue> degeri <value> degerini icermektedir")
    public void checkElementContainsValue(String key, String elementValue, String value) {
        List<WebElement> element = methods.findElements(key);
        String valOFelement = element.get(0).getAttribute(elementValue);
        Assert.assertTrue("Element beklenen degeri icermiyor", valOFelement.contains(value));

    }


    @Step("Mevcut url <localVar> degiskenine kaydedilir")
    public void saveUrlToLocalVar(String localVar) {
        StoreHelper.INSTANCE.saveValue(localVar, driver.getCurrentUrl());
    }

    @Step("<saveKey1> ve <saveKey2> değişkenlerinin değerleri birbirini icerir")
    public void assertTwoLocalVarEq(String saveKey1, String saveKey2) {
        String x = StoreHelper.INSTANCE.getValue(saveKey1);
        x = x.replaceAll("\\s+|TL", "");
        x = x.replaceAll(",.*", "");
        logger.info(x);

        String xx = StoreHelper.INSTANCE.getValue(saveKey2);
        xx = xx.replaceAll("\\s+|TL", "");
        xx = xx.replaceAll(",.*", "");
        logger.info(xx);

        Assert.assertTrue("Degerler birbirini icermiyor.", x.contains(xx));
    }

    @Step("<key> alanına rastgele <type> yazilir")
    public void randomMailGenerator(String key, String type) {
        if (type.equals("email")) {
            String word = methods.randomString(11) + "@gmail.com";
            methods.findElement(key).sendKeys(word);
            logger.info(key + " elementine " + word + " texti yazıldı.");
        } else {
            methods.writeRandomValueToElement(key);
        }
    }



    @Step("<key> listesinden <text> degeri secilir ve adet degisikligi kontrol edilir")
    public void chooseByVisibleTextFromSelectElementAndControl(String key,int value) throws InterruptedException {
        methods.choosenIndexAtSelectElementContains(key,value);
        Thread.sleep(5000);
    }







}