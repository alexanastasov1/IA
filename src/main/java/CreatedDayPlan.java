import java.io.*;
import java.util.*;

public class CreatedDayPlan {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;

    public CreatedDayPlan(String cityName, double start_endlatitude, double start_endlongitude, double timeSpan, String attractionType) {
        this.cityName = cityName;
        this.latitude = start_endlatitude;
        this.longitude = start_endlongitude;
        this.timeSpan = timeSpan;
        this.attractionType = attractionType;
    }

    // Main method to create the day plan route
    public void create() {
        List<Attraction> attractions = loadDatabase(); // Load all attractions from file
        List<Attraction> filteredAttractions = filterAttractions(attractions); // Filter based on city and type
        List<Attraction> route = planRoute(filteredAttractions); // Generate optimized route
        displayRoute(route); // Display the final route
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

    // Filters attractions based on the selected city and attraction type
    private List<Attraction> filterAttractions(List<Attraction> attractions) {
        List<Attraction> filtered = new ArrayList<>();
        for (int i = 0; i < attractions.size(); i++) {
            Attraction a = attractions.get(i);
            if (a.city.equals(cityName) && a.type.equals(attractionType)) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    // Plans the route by selecting the closest attraction within the given time limit
    private List<Attraction> planRoute(List<Attraction> attractions) {
        List<Attraction> route = new ArrayList<>();
        Set<String> visited = new HashSet<>(); // To keep track of visited attractions
        double totalTime = 0; // Total time spent visiting attractions
        double currentLat = latitude;
        double currentLon = longitude;

        while (!attractions.isEmpty()) {
            Attraction closest = findClosestAttraction(attractions, currentLat, currentLon, visited);
            if (closest == null || totalTime + closest.timeAdvised > timeSpan + 0.5) break; // Stop if time exceeds limit

            route.add(closest); // Add to route
            visited.add(closest.name); // Mark as visited
            totalTime += closest.timeAdvised; // Update total time used
            currentLat = closest.latitude; // Update current location
            currentLon = closest.longitude;

            for (int i = 0; i < attractions.size(); i++) {
                if (attractions.get(i).name.equals(closest.name)) {
                    attractions.remove(i);
                    break;
                }
            }
        }
        return route;
    }

    // Finds the closest attraction that hasn't been visited yet
    private Attraction findClosestAttraction(List<Attraction> attractions, double lat, double lon, Set<String> visited) {
        Attraction closest = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < attractions.size(); i++) {
            Attraction a = attractions.get(i);
            if (!visited.contains(a.name)) { // Check if attraction has already been visited
                double distance = haversine(lat, lon, a.latitude, a.longitude);
                if (distance < minDistance) { // Update the closest attraction if distance is shorter
                    minDistance = distance;
                    closest = a;
                }
            }
        }
        return closest;
    }

    // Uses the Haversine formula to calculate the distance between two coordinates
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Returns distance in km
    }

    // Displays the planned route
    private void displayRoute(List<Attraction> route) {
        System.out.println("Planned route:");
        for (int i = 0; i < route.size(); i++) {
            Attraction a = route.get(i);
            System.out.println(a.name + " (" + a.type + ") - Time: " + a.timeAdvised + " hours");
        }
    }

    //Attraction
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
}


