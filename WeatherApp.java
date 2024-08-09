/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.weatherapp;

/**
 *
 * @author hp
 */
import javax.swing.*; // Importing Swing library for GUI components
import java.awt.event.ActionEvent; // Importing ActionEvent for button actions
import java.awt.event.ActionListener; // Importing ActionListener for button actions
import java.io.BufferedReader; // Importing BufferedReader for reading data from URL
import java.io.InputStreamReader; // Importing InputStreamReader for reading data from URL
import java.net.HttpURLConnection; // Importing HttpURLConnection for making HTTP requests
import java.net.URL; // Importing URL for creating URL objects
import org.json.JSONObject; // Importing JSONObject for JSON parsing

public class WeatherApp {
    private JFrame frame; // Frame for the application window
    private JTextField cityTextField; // Text field for entering city name
    private JTextArea resultTextArea; // Text area for displaying weather data
    private static final String API_KEY = "83d6cad830f6e1c4015c6ffa0184e345"; // Replace with your OpenWeatherMap API key

    // Constructor for the WeatherApp class
    public WeatherApp() {
        frame = new JFrame("Weather Forecast App"); // Creating a new frame with title
        frame.setSize(400, 300); // Setting the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setting default close operation
        frame.setLayout(null); // Setting layout to null for custom layout

        JLabel cityLabel = new JLabel("Enter city:"); // Label for city input
        cityLabel.setBounds(50, 50, 100, 25); // Setting label position and size
        frame.add(cityLabel); // Adding label to the frame

        cityTextField = new JTextField(); // Text field for entering city name
        cityTextField.setBounds(150, 50, 150, 25); // Setting text field position and size
        frame.add(cityTextField); // Adding text field to the frame

        JButton getWeatherButton = new JButton("Get Weather"); // Button for fetching weather
        getWeatherButton.setBounds(150, 100, 150, 25); // Setting button position and size
        frame.add(getWeatherButton); // Adding button to the frame

        resultTextArea = new JTextArea(); // Text area for displaying weather data
        resultTextArea.setBounds(50, 150, 300, 100); // Setting text area position and size
        frame.add(resultTextArea); // Adding text area to the frame

        // ActionListener for the getWeatherButton
        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityTextField.getText(); // Getting the city name from the text field
                if (!city.isEmpty()) { // Checking if the city name is not empty
                    getWeatherData(city); // Fetching weather data for the entered city
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a city name"); // Showing error message if city name is empty
                }
            }
        });

        frame.setVisible(true); // Making the frame visible
    }

    // Method to fetch weather data from OpenWeatherMap API
    private void getWeatherData(String city) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric"; // Creating URL for API request
            URL url = new URL(urlString); // Creating URL object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Opening connection
            conn.setRequestMethod("GET"); // Setting request method
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); // Reading response
            String inputLine; // Variable for storing each line of response
            StringBuilder content = new StringBuilder(); // StringBuilder for storing complete response
            while ((inputLine = in.readLine()) != null) { // Reading response line by line
                content.append(inputLine); // Appending each line to StringBuilder
            }
            in.close(); // Closing BufferedReader
            conn.disconnect(); // Disconnecting HttpURLConnection
            parseAndDisplayWeatherData(content.toString()); // Parsing and displaying weather data
        } catch (Exception e) { // Catching any exception that might occur
            JOptionPane.showMessageDialog(frame, "Error fetching weather data: " + e.getMessage()); // Showing error message
        }
    }

    // Method to parse and display weather data
    private void parseAndDisplayWeatherData(String jsonData) {
        JSONObject obj = new JSONObject(jsonData); // Creating JSONObject from JSON string
        String cityName = obj.getString("name"); // Getting city name from JSON
        JSONObject main = obj.getJSONObject("main"); // Getting main weather information
        double temperature = main.getDouble("temp"); // Getting temperature
        int humidity = main.getInt("humidity"); // Getting humidity
        JSONObject weather = obj.getJSONArray("weather").getJSONObject(0); // Getting weather description
        String description = weather.getString("description"); // Getting description

        // Creating result string with weather information
        String result = "City: " + cityName + "\nTemperature: " + temperature + "°C\nHumidity: " + humidity + "%\nDescription: " + description;
        resultTextArea.setText(result); // Setting result string to text area
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherApp(); // Creating instance of WeatherApp
            }
        });
    }
}
