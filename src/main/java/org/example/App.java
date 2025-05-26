package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class App {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\egorm\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        try {
            //first task
            webDriver.get("https://api.ipify.org/?format=json");

            WebElement elem = webDriver.findElement(By.tagName("pre"));

            String jsonStr = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonStr);

            String ip = (String) obj.get("ip");

            System.out.println("IP - "+ip);

            //second task
            String url = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";
            webDriver.get(url);

            elem = webDriver.findElement(By.tagName("pre"));
            jsonStr = elem.getText();

            parser = new JSONParser();
            obj = (JSONObject) parser.parse(jsonStr);

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            PrintWriter writer = getPrintWriter(times, temperatures, rains);

            writer.close();
        } catch (Exception e) {
            System.err.println("Error: "+ e.getMessage());
        }
    }

    private static PrintWriter getPrintWriter(JSONArray times, JSONArray temperatures, JSONArray rains) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("./result/forecast.txt"));
        writer.printf("%-3s %-20s %-12s %-10s%n", "№", "Дата/время", "Температура", "Осадки (мм)");

        for (int i = 0; i < times.size(); i++) {
            String time = (String) times.get(i);
            double temp = (Double) temperatures.get(i);
            double rain = (Double) rains.get(i);
            writer.printf("%-3d %-20s %-12.1f %-10.2f%n", i + 1, time, temp, rain);
        }
        return writer;
    }
}