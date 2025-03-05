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

    /* public boolean cityValidation(){
        Scanner input = new Scanner(System.in);

        System.out.print("Enter city name: ");
        String choice = input.nextLine();

        for(int i = 0; i < attractions.size(); i++) {
            if((attractions.get(0)).substring(0,6).equals(choice)) {
                return true;
            }
        }
        System.out.println("City not in database");
        System.out.print("Enter city name: ");
        choice = input.nextLine();

        System.out.println((attractions.get(0)).substring(0,6));
        return true;
    } */

    public boolean cityValidation() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter city name: ");
            String choice = input.nextLine();

            for (int i = 0; i < attractions.size(); i++) {
                String cityName = attractions.get(i).split(";")[0].trim(); // Extract city name
                System.out.println(cityName);
                if (cityName.equalsIgnoreCase(choice)) { // Case-insensitive comparison
                    return true; // Valid city found
                }
            }

            System.out.println("City not in database. Please try again.");
        }
    }
}
