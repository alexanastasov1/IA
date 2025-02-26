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

    public CreateNewDayPlan() {
        ArrayList<String> attractions = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        System.out.print("Enter city name: ");
        String choice = input.nextLine();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            attractions.add(line);

            while (line != null) {
                System.out.println(line);
                line = br.readLine();
                attractions.add(line);
            }

            boolean notFound = true;
            if (notFound = true) {
                for (int i = 0; i < attractions.size(); i++) {
                    if (attractions.get(i) == choice);
                    notFound = false;
                }
            }
            else {
                System.out.print("Enter city name: ");
                choice = input.nextLine();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
