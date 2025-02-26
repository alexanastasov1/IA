import java.util.Scanner;

public class Menu {
    public int display() {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Choose an option:");
            System.out.println("1. Open Create New Day Plan");
            System.out.println("2. Open Saved Day Plans");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Opening Create New Day Plan");
                return 1;
            } else if (choice == 2) {
                System.out.println("Opening Saved Day Plans");
                return 2;
            } else if (choice == 3) {
                System.out.println("Exiting");
                return 3;
            }
        }
    }
}
