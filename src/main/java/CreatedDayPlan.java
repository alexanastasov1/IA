import javax.swing.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

public class CreatedDayPlan extends JFrame {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;
    private String restaurantFile = "Restaurants.txt";

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

    // Loads vegan restaurants from Restaurants.txt ands stores it in a list
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

            //Debugging
            /* System.out.println("\n--- Iteration ---");
            System.out.println("Current location: " + currentLat + ", " + currentLon);
            System.out.println("Total time used: " + totalTime);

            if (bestPreferred != null) {
                System.out.println("Best preferred: " + bestPreferred.name + " (" + bestPreferred.type + "), Distance: " + minPreferredDist);
            } else {
                System.out.println("No preferred attractions within 1 km.");
            }
            System.out.println("Best fallback: " + bestFallback.name + " (" + bestFallback.type + "), Distance: " + minFallbackDist);
            */

            // Ensure bestPreferred is not null
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
        if (route.isEmpty()) {
            System.out.println("No attractions available for the route.");
            return;
        }

        List<Restaurant> allRestaurants = loadRestaurants();
        System.out.println("Planned route:");
        StringBuilder googleMapsUrl = new StringBuilder("https://www.google.com/maps/dir/");
        googleMapsUrl.append(latitude).append(",").append(longitude);

        for (int i = 0; i < route.size(); i++) {
            Attraction a = route.get(i);
            System.out.println("\n" + a.name + " (" + a.type + ") - Time: " + a.timeAdvised + " hours");

            // Find nearby vegan places
            List<Restaurant> nearby = new ArrayList<>();
            for (int j = 0; j < allRestaurants.size(); j++) {
                Restaurant r = allRestaurants.get(j);
                if (r.city.equalsIgnoreCase(cityName)) {
                    double dist = haversine(a.latitude, a.longitude, r.latitude, r.longitude);
                    if (dist <= 1.0) {
                        nearby.add(r);
                    }
                }
            }

            if (!nearby.isEmpty()) {
                System.out.println("Nearby vegan options (within 1 km):");
                for (int k = 0; k < nearby.size(); k++) {
                    Restaurant r = nearby.get(k);
                    System.out.println("  - " + r.name + " (" + r.type + ")");
                }
            } else {
                System.out.println("  No vegan restaurants or cafés nearby.");
            }

            googleMapsUrl.append("/").append(encodeForUrl(a.name));
        }

        System.out.println("\nGoogle Maps Route: " + googleMapsUrl);
    }

    private String encodeForUrl(String name) {
        try {
            // Encode using UTF-8 which is standard for URLs
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Fallback: replace spaces with + if encoding fails
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


