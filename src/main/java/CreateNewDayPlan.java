import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class CreateNewDayPlan extends JFrame {
    private String fileName = "Database.txt";
    private ArrayList<String> attractions = new ArrayList<>();

    // GUI components
    private JTextField cityField, latField, lonField, timeField;
    private JComboBox<String> typeBox;
    private JButton createButton;
    private boolean closedByUser = true;

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (closedByUser) {
                    SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
                }
            }
        });

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
                    if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(city)) {
                        cityExists = true;
                        break;
                    }
                }

                if (!cityExists) {
                    JOptionPane.showMessageDialog(this, "City not found in the database.", "City Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse and validate latitude
                double lat = Double.parseDouble(latField.getText().trim());
                if (lat < -90 || lat > 90) {
                    JOptionPane.showMessageDialog(this, "Latitude must be between -90 and 90 degrees.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse and validate longitude
                double lon = Double.parseDouble(lonField.getText().trim());
                if (lon < -180 || lon > 180) {
                    JOptionPane.showMessageDialog(this, "Longitude must be between -180 and 180 degrees.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse and validate time
                double time = Double.parseDouble(timeField.getText().trim());
                if (time <= 0 || time > 16) {
                    JOptionPane.showMessageDialog(this, "Time must be between 0.1 and 16 hours.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String type = (String) typeBox.getSelectedItem();

                CreatedDayPlan plan = new CreatedDayPlan(city, lat, lon, time, type);
                plan.create();

                closedByUser = false;
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