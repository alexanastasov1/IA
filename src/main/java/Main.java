import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.TravelMode;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

public class Main {
    public static void main(String[] args) {
        //Bring up main menu on start
        Menu mainMenu = new Menu();

        //User to press a button
        int choice = mainMenu.display();
        while (choice != 3) {
            if (choice == 1){ //Open CreateNewDayPlan menu if button pressed
                CreateNewDayPlan createNewDayPlanMenu = new CreateNewDayPlan();
                createNewDayPlanMenu.cityValidation();
                createNewDayPlanMenu.getTimeSpan();
                createNewDayPlanMenu.setStart_endPoint();
                createNewDayPlanMenu.favouredAttractionType();
            }
            else if(choice == 2) { //Open SavedDayPlans menu if button pressed

            }
            choice = mainMenu.display();
        }
        System.out.println("Application terminated");

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAa8eDPb8bpJadi3seJxapjhJvy8bkGv88")
                .build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, "Berlin, Germany").await();
            System.out.println(results[0].formattedAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}