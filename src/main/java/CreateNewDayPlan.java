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

        add(new JLabel("Start Latitude:")).setBounds(300, 100, 100, 30);
        latField = new JTextField();
        latField.setBounds(420, 100, 200, 30);
        add(latField);

        add(new JLabel("Start Longitude:")).setBounds(300, 150, 100, 30);
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

        createButton.addActionListener(e -> {
            try {
                String city = cityField.getText().trim();
                if (city.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "City name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if city exists in the database
                boolean cityExists = false;
                for (String line : attractions) {
                    String[] parts = line.split(";");

                    if (parts.length >= 1) {
                        String dbCity = parts[0].trim();

                        if (dbCity.equalsIgnoreCase(city)) {
                            cityExists = true;
                            break;
                        }
                    }
                }

                if (!cityExists) {
                    JOptionPane.showMessageDialog(this, "City not found in the database.", "City Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double lat = Double.parseDouble(latField.getText().trim());
                double lon = Double.parseDouble(lonField.getText().trim());
                double time = Double.parseDouble(timeField.getText().trim());
                String type = (String) typeBox.getSelectedItem();

                CreatedDayPlan plan = new CreatedDayPlan(city, lat, lon, time, type);
                plan.create();
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for latitude, longitude, and time.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}