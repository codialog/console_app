package com.example.console.app;

import com.example.console.app.controller.ClientController;
import com.example.console.app.controller.DealController;
import com.example.console.app.contstants.JsonProperties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;

@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static ClientController clientController;
    private static DealController dealController;

    public Application(ClientController clientController, DealController dealController) {
        Application.dealController = dealController;
        Application.clientController = clientController;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        JSONObject output = new JSONObject();
        String outputFilePath = System.getProperty("user.home") + "/Desktop" + "/output.json";
        try {

            String action = args[0];
            String inputFilePath = args[1];
            outputFilePath = args[2];

            File inputFile = new File(inputFilePath);
            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new Exception();
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(inputFilePath));

            switch (action) {
                case "search":
                    JSONArray result = new JSONArray();
                    JSONArray creterias = (JSONArray) jsonObject.get(JsonProperties.CRITERIAS);
                    for (int i = 0; i < creterias.size(); i++) {
                        JSONObject criteria = (JSONObject) creterias.get(i);
                        JSONObject inner = new JSONObject();
                        inner.put(JsonProperties.CRITERIA, criteria);
                        JSONArray clients = clientController.search(criteria);
                        inner.put(JsonProperties.RESULTS, clients);
                        if (clients.size() != 0) {
                            result.add(inner);
                        }
                    }
                    output.put(JsonProperties.TYPE, JsonProperties.SEARCH);
                    output.put(JsonProperties.RESULTS, result);
                    break;
                case "stat":
                    LocalDate startDate = LocalDate.parse(jsonObject.get(JsonProperties.START_DATE).toString());
                    LocalDate endDate = LocalDate.parse(jsonObject.get(JsonProperties.END_DATE).toString());
                    output = dealController.getJsonStat(startDate, endDate);
                    break;
                default:
                    throw new Exception();
            }

            logger.info("Success");
        } catch (Exception e){
            output = new JSONObject();
            output.put(JsonProperties.TYPE, JsonProperties.ERROR);
            output.put(JsonProperties.MESSAGE, "Неправильный формат даты");
            logger.error("Error, outputFile: " + outputFilePath);
        }finally {
            FileWriter outputFile = new FileWriter(outputFilePath);
            outputFile.write(output.toJSONString());
            outputFile.flush();
            outputFile.close();
        }














    }

}
