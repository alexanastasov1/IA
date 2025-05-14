import java.io.*;
import java.net.URLEncoder;
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

    private List<Attraction> filterByCity(List<Attraction> attractions) {
        List<Attraction> filtered = new ArrayList<>();
        for (Attraction a : attractions) {
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

            for (Attraction a : attractions) {
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

            Attraction next = (bestPreferred != null) ? bestPreferred : bestFallback;

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

    private Attraction findBestAttraction(List<Attraction> attractions, double lat, double lon, Set<String> visited) {
        Attraction bestPreferred = null;
        Attraction bestFallback = null;
        double minPreferredDistance = Double.MAX_VALUE;
        double minFallbackDistance = Double.MAX_VALUE;

        for (Attraction a : attractions) {
            if (visited.contains(a.name)) continue;

            double distance = haversine(lat, lon, a.latitude, a.longitude);

            if (a.type.equals(attractionType)) {
                if (distance < minPreferredDistance) {
                    minPreferredDistance = distance;
                    bestPreferred = a;
                }
            }

            if (distance < minFallbackDistance) {
                minFallbackDistance = distance;
                bestFallback = a;
            }
        }

        if (bestPreferred != null && minPreferredDistance <= 1.0) {
            return bestPreferred;
        } else {
            return bestFallback;
        }
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

        System.out.println("Planned route:");
        StringBuilder googleMapsUrl = new StringBuilder("https://www.google.com/maps/dir/");
        googleMapsUrl.append(latitude).append(",").append(longitude);

        for (Attraction a : route) {
            System.out.println(a.name + " (" + a.type + ") - Time: " + a.timeAdvised + " hours");
            googleMapsUrl.append("/").append(encodeForUrl(a.name));
        }

        System.out.println("Google Maps Route: " + googleMapsUrl);
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


