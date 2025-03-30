import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CreateNewDayPlan {
    private String fileName = "Database.txt";
    private String cityName;
    private int start_endPoint;
    private int timeSpan;
    private int attractionType;
    private ArrayList<String> attractions = new ArrayList<>();

    public CreateNewDayPlan() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            attractions.add(line);

            while (line != null) {
                line = br.readLine();
                attractions.add(line);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean cityValidation() {
        Scanner input = new Scanner(System.in);

        if (attractions.isEmpty()) {
            System.out.println("Error: No cities found in the database.");
            return false;
        }

        while (true) {
            System.out.print("Enter city name: ");
            String choice = input.nextLine();

            for (int i = 0; i < attractions.size(); i++) {
                String attraction = attractions.get(i);

                if (attraction != null && !attraction.isBlank()) { // Avoid null/blank entries
                    String city = attraction.split(";")[0].trim(); // Extract city name
                    if (city.equalsIgnoreCase(choice)) { // Case-insensitive match
                        System.out.println(city + " is in database");
                        this.cityName = city;
                        return true; // Valid city found
                    }
                }
            }

            System.out.println("City not in database. Please try again.");
        }
    }

    /* public void setStart_endPoint() {
    } */

    public void favouredAttractionType() {    //CHECK
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
                    attractionType = choice; // Store chosen attraction type
                    System.out.println("You selected: " + attractionTypes[choice - 1]);
                    return;
                }
            }
            System.out.println("Invalid choice. Please enter a valid number.");
            input.nextLine(); // Clear invalid input
        }
    }

    public double getTimeSpan() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the number of hours you plan to spend (e.g., 3.5): ");

            if (input.hasNextDouble()) {
                double time = input.nextDouble();
                if (time > 0 && time <= 24) {
                    timeSpan = (int) time; // Store as integer if needed, or use double
                    return time;
                }
            }
            input.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    public boolean create() {
        Scanner input = new Scanner(System.in);
        return true;
    }
}
