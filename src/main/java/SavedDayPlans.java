import javax.swing.*;

public class SavedDayPlans extends JFrame {
    public SavedDayPlans() {
        setTitle("Saved Day Plans");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(null);

        JLabel label = new JLabel("Here are your saved plans:");
        label.setBounds(50, 50, 300, 30);
        add(label);

        // You could later load and display saved data here

        setVisible(true);
    }
}