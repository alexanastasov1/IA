public class CreatedDayPlan {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;

    public CreatedDayPlan(String cityName, double start_endlatitude, double start_endlongtidue, double timeSpan, String attractionType) {
        this.cityName = cityName;
        latitude = start_endlatitude;
        longitude = start_endlongtidue;
        this.timeSpan = timeSpan;
        this.attractionType = attractionType;
    }
}
