public class Main {
    public static void main(String[] args) {
        //Bring up main menu on start
        Menu mainMenu = new Menu();

        //User to press a button
        int choice = mainMenu.display();
        if (choice == 1){
            //Open CreateNewDayPlan menu if button pressed
            CreateNewDayPlan createNewDayPlanMenu = new CreateNewDayPlan();
            createNewDayPlanMenu.cityValidation();
        } else if(choice == 2){
            //Open SavedDayPlans menu if button pressed
        } else if(choice == 3){
            //Exit program
        }
    }
}