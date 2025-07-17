import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class SavedDayPlans extends JFrame {
    private List<String> planTitles = new ArrayList<>();
    private List<String> planContents = new ArrayList<>();
    private JTextArea displayArea;
    private int selectedPlanIndex = -1;

    public SavedDayPlans() {
        setTitle("Saved Day Plans");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        // Left: Buttons for each plan
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        buttonScroll.setPreferredSize(new Dimension(200, getHeight()));

        // Right: Display area + Delete button
        JPanel rightPanel = new JPanel(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane displayScroll = new JScrollPane(displayArea);
        rightPanel.add(displayScroll, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Plan");
        deleteButton.addActionListener(e -> deleteSelectedPlan());
        rightPanel.add(deleteButton, BorderLayout.SOUTH);

        // Load plans from file
        loadPlans();

        // Create a button for each saved plan
        for (int i = 0; i < planTitles.size(); i++) {
            final int index = i;
            JButton planButton = new JButton(planTitles.get(i));
            planButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            planButton.addActionListener(e -> {
                displayArea.setText(planContents.get(index));
                selectedPlanIndex = index;
            });
            buttonPanel.add(planButton);
            buttonPanel.add(Box.createVerticalStrut(5));
        }

        add(buttonScroll, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadPlans() {
        planTitles.clear();
        planContents.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("SavedPlans.txt"))) {
            String line;
            String currentCity = null;
            StringBuilder currentPlan = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#CITY:")) {
                    currentCity = line.substring(6).trim();
                    currentPlan.setLength(0);
                    currentPlan.append(line).append("\n");
                } else if (line.startsWith("#END")) {
                    if (currentCity != null && currentPlan.length() > 0) {
                        String finalCurrentCity = currentCity;
                        long count = planTitles.stream().filter(title -> title.startsWith(finalCurrentCity)).count();
                        String label = currentCity + (count > 0 ? " (Plan " + (count + 1) + ")" : "");
                        planTitles.add(label);
                        planContents.add(currentPlan.toString());
                    }
                    currentCity = null;
                } else if (currentCity != null) {
                    currentPlan.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read saved plans.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedPlan() {
        if (selectedPlanIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a plan to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this plan?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Remove from memory
        planTitles.remove(selectedPlanIndex);
        String contentToRemove = planContents.remove(selectedPlanIndex);
        selectedPlanIndex = -1;
        displayArea.setText("");

        // Re-write the file without the deleted plan
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SavedPlans.txt"))) {
            for (String plan : planContents) {
                writer.write(plan);
                writer.write("\n"); // Extra spacing between plans
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to update saved file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Refresh the window
        dispose();
        new SavedDayPlans(); // Re-open the window with updated data
    }
}
