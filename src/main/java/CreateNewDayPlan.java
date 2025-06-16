import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class CreateNewDayPlan extends JFrame {
    private String fileName = "Database.txt";
    private String cityName;
    private double latitude;
    private double longitude;
    private double timeSpan;
    private String attractionType;
    private ArrayList<String> attractions = new ArrayList<>();

    // GUI components
    private JTextField cityField, latField, lonField, timeField;
    private JComboBox<String> typeBox;
    private JButton createButton;

    public CreateNewDayPlan() {
        loadAttractions();
        initGUI();
    }

    private void loadAttractions() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                attractions.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        setTitle("Create New Day Plan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(null);
        setResizable(false);

        add(new JLabel("City:")).setBounds(300, 50, 100, 30);
        cityField = new JTextField();
        cityField.setBounds(420, 50, 200, 30);
        add(cityField);

        add(new JLabel("Latitude:")).setBounds(300, 100, 100, 30);
        latField = new JTextField();
        latField.setBounds(420, 100, 200, 30);
        add(latField);

        add(new JLabel("Longitude:")).setBounds(300, 150, 100, 30);
        lonField = new JTextField();
        lonField.setBounds(420, 150, 200, 30);
        add(lonField);

        add(new JLabel("Time (hrs):")).setBounds(300, 200, 100, 30);
        timeField = new JTextField();
        timeField.setBounds(420, 200, 200, 30);
        add(timeField);

        add(new JLabel("Attraction Type:")).setBounds(300, 250, 150, 30);
        String[] types = { "Museum", "Historical Site", "Park", "Gallery", "Landmark",
                "Theme Park", "Zoo", "Theater", "Shopping", "Sports Venue" };
        typeBox = new JComboBox<>(types);
        typeBox.setBounds(420, 250, 200, 30);
        add(typeBox);

        createButton = new JButton("Create Plan");
        createButton.setBounds(375, 320, 150, 40);
        add(createButton);

        createButton.addActionListener(e -> handleCreate());

        setVisible(true);
    }

    private void handleCreate() {
        String cityInput = cityField.getText().trim();
        if (!validateCity(cityInput)) {
            JOptionPane.showMessageDialog(this, "City not in database.");
            return;
        }
        try {
            latitude = Double.parseDouble(latField.getText().trim());
            longitude = Double.parseDouble(lonField.getText().trim());
            timeSpan = Double.parseDouble(timeField.getText().trim());
            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180 || timeSpan <= 0 || timeSpan > 24) {
                throw new NumberFormatException();
            }
            attractionType = (String) typeBox.getSelectedItem();
            cityName = cityInput;

            CreatedDayPlan plan = new CreatedDayPlan(cityName, latitude, longitude, timeSpan, attractionType);
            plan.create();
            JOptionPane.showMessageDialog(this, "Plan created successfully!");
            // Optionally: dispose(); or hand off to next screen
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: please enter valid numbers.");
        }
    }

    private boolean validateCity(String inputCity) {
        for (String entry : attractions) {
            if (entry != null && !entry.isBlank()) {
                String city = entry.split(";")[0].trim();
                if (city.equalsIgnoreCase(inputCity)) return true;
            }
        }
        return false;
    }
}
