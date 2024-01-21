package com.gaugetest.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TxtWriter {

    private static Logger logger = LoggerFactory.getLogger(TxtWriter.class);
    public static void writeToTxt(String text){

        try {
            String filePath = "testDataFiles/response.txt";
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text+"\n");
            bufferedWriter.close();
           logger.info("Text succesfully writed to txt file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
