import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CreatedDayPlan extends JFrame {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;
    private String restaurantFile = "Restaurants.txt";

    static {
        Platform.setImplicitExit(false);
        new JFXPanel();
    }

    public CreatedDayPlan(String cityName, double start_endlatitude, double start_endlongitude, double timeSpan, String attractionType) {
        this.cityName = cityName;
        this.latitude = start_endlatitude;
        this.longitude = start_endlongitude;
        this.timeSpan = timeSpan;
        this.attractionType = attractionType;
    }

    // Main method to create the day plan route
    public void create() {
        List<Attraction> allAttractions = loadDatabase(); // Load all attractions
        List<Attraction> cityAttractions = filterByCity(allAttractions); // Only filter by city
        List<Attraction> route = planRoute(cityAttractions); // Generate optimized route
        displayRoute(route); // Display final route
    }

    // Loads the attraction data from the file and stores it in a list
    private List<Attraction> loadDatabase() {
        List<Attraction> attractions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("; ");
                if (data.length == 6) {
                    String city = data[0];
                    String name = data[1];
                    String type = data[2];
                    double timeAdvised = Double.parseDouble(data[3]);
                    double latitude = Double.parseDouble(data[4]);
                    double longitude = Double.parseDouble(data[5]);

                    attractions.add(new Attraction(city, name, type, timeAdvised, latitude, longitude));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attractions;
    }

    // Loads vegan restaurants from Restaurants.txt
    private List<Restaurant> loadRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(restaurantFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("; ");
                if (data.length == 6) {
                    String city = data[0];
                    String name = data[1];
                    String type = data[2];
                    double timeAdvised = Double.parseDouble(data[3]);
                    double latitude = Double.parseDouble(data[4]);
                    double longitude = Double.parseDouble(data[5]);

                    restaurants.add(new Restaurant(city, name, type, timeAdvised, latitude, longitude));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    //Filter database for selected city
    private List<Attraction> filterByCity(List<Attraction> attractions) {
        List<Attraction> filtered = new ArrayList<>();
        for (int i = 0; i < attractions.size(); i++) {
            Attraction a = attractions.get(i);
            if (a.city.equalsIgnoreCase(cityName)) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    // Plans the route by selecting the closest attraction within the given time limit
    private List<Attraction> planRoute(List<Attraction> attractions) {
        List<Attraction> route = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        double totalTime = 0;
        double currentLat = latitude;
        double currentLon = longitude;

        while (totalTime <= timeSpan + 0.5) {
            Attraction bestPreferred = null;
            Attraction bestFallback = null;
            double minPreferredDist = Double.MAX_VALUE;
            double minFallbackDist = Double.MAX_VALUE;

            for (int i = 0; i < attractions.size(); i++) {
                Attraction a = attractions.get(i);
                if (visited.contains(a.name)) continue;

                double distance = haversine(currentLat, currentLon, a.latitude, a.longitude);

                // If it's the preferred type and within 1km
                if (a.type.equalsIgnoreCase(attractionType) && distance <= 1.0 && distance < minPreferredDist) {
                    bestPreferred = a;
                    minPreferredDist = distance;
                }

                // Always track the closest of any type as fallback
                if (distance < minFallbackDist) {
                    bestFallback = a;
                    minFallbackDist = distance;
                }
            }

            Attraction next;
            if (bestPreferred != null && minPreferredDist <= 1.0) {
                next = bestPreferred;
            } else {
                next = bestFallback;
            }

            // End if there's no option or not enough time
            if (next == null || totalTime + next.timeAdvised > timeSpan + 0.5) break;

            route.add(next);
            visited.add(next.name);
            totalTime += next.timeAdvised;
            currentLat = next.latitude;
            currentLon = next.longitude;
        }
        return route;
    }

    // Uses the Haversine formula to calculate the distance between two coordinates
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Displays the planned route
    private void displayRoute(List<Attraction> route) {
        JFrame frame = new JFrame("Created Day Plan for " + cityName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Route building
        StringBuilder output = new StringBuilder();
        StringBuilder googleMapsUrl = new StringBuilder("https://www.google.com/maps/dir/");
        googleMapsUrl.append(latitude).append(",").append(longitude);
        googleMapsUrl.append("/data=!4m2!4m1!3e2"); // set walking mode

        if (route.isEmpty()) {
            output.append("No attractions available for the route.\n");
        } else {
            List<Restaurant> allRestaurants = loadRestaurants();
            output.append("PLANNED ROUTE:\n");

            for (Attraction a : route) {
                output.append("\n").append(a.name).append(" (").append(a.type).append(") - Time: ")
                        .append(a.timeAdvised).append(" hours\n");

                List<Restaurant> nearby = new ArrayList<>();
                for (Restaurant r : allRestaurants) {
                    if (r.city.equalsIgnoreCase(cityName)) {
                        double dist = haversine(a.latitude, a.longitude, r.latitude, r.longitude);
                        if (dist <= 1.0) {
                            nearby.add(r);
                        }
                    }
                }

                if (!nearby.isEmpty()) {
                    output.append("Nearby vegan options (within 1 km):\n");
                    for (Restaurant r : nearby) {
                        output.append("  - ").append(r.name).append(" (").append(r.type).append(")\n");
                    }
                } else {
                    output.append("  No vegan restaurants or cafés nearby.\n");
                }

                googleMapsUrl.append("/").append(encodeForUrl(a.name));
            }
        }

        outputArea.setText(output.toString());

        // JavaFX map in a JFXPanel
        JFXPanel fxPanel = new JFXPanel();  // Always create new

        Platform.runLater(() -> {
            try {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                List<String> coords = new ArrayList<>();
                coords.add("{ lat: " + latitude + ", lng: " + longitude + " }");
                for (Attraction a : route) {
                    coords.add("{ lat: " + a.latitude + ", lng: " + a.longitude + " }");
                }
                String waypointArray = "[" + String.join(", ", coords) + "]";
                String htmlContent = loadHtmlWithMap(waypointArray, "AIzaSyAa8eDPb8bpJadi3seJxapjhJvy8bkGv88");

                Scene scene = new Scene(webView);
                fxPanel.setScene(scene);
                webEngine.loadContent(htmlContent, "text/html");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Save Plan button
        JButton saveButton = new JButton("Save Plan");
        final boolean[] alreadySaved = {false};  // Flag to prevent multiple saves

        saveButton.addActionListener(e -> {
            if (alreadySaved[0]) {
                JOptionPane.showMessageDialog(frame, "Day plan already saved!");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("SavedPlans.txt", true))) {
                writer.write("#CITY: " + cityName + "\n");
                writer.write(outputArea.getText());
                writer.write("#END\n\n");
                JOptionPane.showMessageDialog(frame, "Plan saved successfully to SavedPlans.txt.");
                alreadySaved[0] = true;  // Set the flag to prevent future saves
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to save plan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(saveButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, fxPanel);
        splitPane.setResizeWeight(0.22);
        frame.add(splitPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private String loadHtmlWithMap(String waypointsArray, String apiKey) {
        StringBuilder html = new StringBuilder();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("map_template.html")) {
            if (in == null) throw new FileNotFoundException("map_template.html not found in resources.");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    html.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "<html><body><h2>Error loading map</h2></body></html>";
        }

        return html.toString()
                .replace("%WAYPOINTS%", waypointsArray)
                .replace("%API_KEY%", apiKey);
    }

    private String encodeForUrl(String name) {
        try {
            // Encode using UTF-8
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return name.replace(" ", "+");
        }
    }

    //Attractions
    private static class Attraction {
        String city;
        String name;
        String type;
        double timeAdvised;
        double latitude;
        double longitude;

        Attraction(String city, String name, String type, double timeAdvised, double latitude, double longitude) {
            this.city = city;
            this.name = name;
            this.type = type;
            this.timeAdvised = timeAdvised;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    //Restaurants/cafes
    private static class Restaurant {
        String city;
        String name;
        String type;
        double timeAdvised;
        double latitude;
        double longitude;

        Restaurant(String city, String name, String type, double timeAdvised, double latitude, double longitude) {
            this.city = city;
            this.name = name;
            this.type = type;
            this.timeAdvised = timeAdvised;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}