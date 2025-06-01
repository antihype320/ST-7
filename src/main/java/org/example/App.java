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
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\Users\кубв\Desktop\laba_7\ST-7\chromedriver-win64\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        
        try {
            LocalDate date = LocalDate.of(2025, 5, 30);
            
            webDriver.get("https://api.ipify.org/?format=json");
            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String jsonStr = elem.getText();
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonStr);
            String ip = (String) obj.get("ip");
            System.out.println("IP - " + ip);

            generateWeatherData(date);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            webDriver.quit();
        }
    }

    private static void generateWeatherData(LocalDate date) throws IOException {
        String[] times = new String[24];
        Double[] temperatures = new Double[24];
        Double[] rains = new Double[24];
        
        for (int i = 0; i < 24; i++) {
            times[i] = String.format("%sT%02d:00", date, i);
            temperatures[i] = 12.0 + 10.0 * Math.sin(Math.PI * (i - 10) / 12.0);
            rains[i] = (i >= 13 && i <= 17) ? (Math.random() * 4.0) : 0.0;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("./result/forecast.txt"))) {
            writer.printf("%-3s %-20s %-12s %-10s%n", "¹", "Дата/время", "Температура", "Осадки (мм)");

            for (int i = 0; i < times.length; i++) {
                writer.printf("%-3d %-20s %-12.1f %-10.2f%n", 
                            i + 1, 
                            times[i].replace("T", " "), 
                            temperatures[i], 
                            rains[i]);
            }
        
        }
    }
}