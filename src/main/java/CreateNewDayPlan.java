import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CreateNewDayPlan {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;
    private ArrayList<String> attractions = new ArrayList<>();

    // Constructor to load the attractions database from file
    public CreateNewDayPlan() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            // Read all lines from the file and store them in the attractions list
            while (line != null) {
                attractions.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validates the city name input by the user
    public boolean cityValidation() {
        Scanner input = new Scanner(System.in);

        if (attractions.isEmpty()) {
            System.out.println("Error: No cities found in the database.");
            return false;
        }

        while (true) {
            System.out.print("Enter city name: ");
            String choice = input.nextLine();

            // Iterate through the stored attractions to find a matching city
            for (int i = 0; i < attractions.size(); i++) {
                String attraction = attractions.get(i);

                if (attraction != null && !attraction.isBlank()) { // Avoid null/blank entries
                    String city = attraction.split(";")[0].trim(); // Extract city name
                    if (city.equalsIgnoreCase(choice)) { // Case-insensitive match
                        System.out.println(city + " is in database.");
                        this.cityName = city;
                        return true; // Valid city found
                    }
                }
            }
            System.out.println("City not in database. Please try again.");
        }
    }

    // Sets the start and end point by allowing the user to input coordinates
    public void setStart_endPoint() {
        if (cityName == null || cityName.isEmpty()) {
            System.out.println("Error: City not set. Please validate a city first.");
            return;
        }

        // Generate Google Maps search URL for the selected city
        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + cityName.replace(" ", "+");
        System.out.println("Open the following link and right-click to select your start and end point on the map:");
        System.out.println(googleMapsUrl);

        Scanner input = new Scanner(System.in);

        // Prompt user for latitude and longitude
        while (true) {
            try {
                System.out.print("Enter the latitude and longitude of the selected point: ");
                String userInput = input.nextLine().trim();

                // Split input by comma or space
                String[] coordinates = userInput.split("[,\\s]+");

                if (coordinates.length == 2) {
                    latitude = Double.parseDouble(coordinates[0]);
                    longitude = Double.parseDouble(coordinates[1]);

                    // Validate latitude and longitude ranges
                    if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
                        break;
                    } else {
                        System.out.println("Invalid coordinates. Latitude must be between -90 and 90, and longitude between -180 and 180.");
                    }
                } else {
                    System.out.println("Invalid format. Please enter the coordinates in the format: latitude, longitude.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values.");
            }
        }

        // Display the set coordinates
        System.out.println("Start/end point set at: Latitude " + latitude + ", Longitude " + longitude);
    }

    // Allows the user to select their preferred attraction type
    public String favouredAttractionType() {
        Scanner input = new Scanner(System.in);
        String[] attractionTypes = {
                "Museum", "Historical Site", "Park", "Gallery", "Landmark",
                "Theme Park", "Zoo", "Theater", "Shopping", "Sports Venue"
        };

        System.out.println("Choose your preferred attraction type:");
        for (int i = 0; i < attractionTypes.length; i++) {
            System.out.println((i + 1) + ". " + attractionTypes[i]);
        }

        while (true) {
            System.out.print("Enter the number corresponding to your choice: ");
            if (input.hasNextInt()) {
                int choice = input.nextInt();
                if (choice >= 1 && choice <= attractionTypes.length) {
                    attractionType = attractionTypes[choice - 1]; // Store chosen attraction type
                    System.out.println("Preferred attraction type set as: " + attractionType);
                    return attractionType;
                }
            }
            System.out.println("Invalid choice. Please enter a valid number.");
            input.nextLine(); // Clear invalid input
        }
    }

    // Gets the time span the user wants to spend on the plan
    public double getTimeSpan() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the number of hours you plan to spend (e.g., 3.5): ");

            if (input.hasNextDouble()) {
                double time = input.nextDouble();
                if (time > 0 && time <= 24) {
                    timeSpan = time;
                    return timeSpan;
                }
            }
            input.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    // Creates a new day plan based on user input
    public CreatedDayPlan create() {
        System.out.println("Day plan successfully created!");
        return new CreatedDayPlan(cityName, latitude, longitude, timeSpan, attractionType);
    }
}
