import java.util.Scanner;

public class Menu {
    public int display() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("MAIN MENU:");
        System.out.println("---------------------------");
        System.out.println("1. Open Create New Day Plan");
        System.out.println("2. Open Saved Day Plans");
        System.out.println("3. Exit");

        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                if (choice >= 1 && choice <= 3) {
                    if (choice == 1) {
                        System.out.println("Opening Create New Day Plan...");
                    } else if (choice == 2) {
                        System.out.println("Opening Saved Day Plans...");
                    } else {
                        System.out.println("Exiting...");
                    }
                    return choice; // Return the valid choice
                }
            }
            scanner.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
        }
    }
}

