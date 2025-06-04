import javax.swing.*;
public class CreateNewDayPlanGUI extends JFrame {
    public CreateNewDayPlanGUI() {
        setTitle("Create New Day Plan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(null);

        JLabel label = new JLabel("Enter your trip preferences:");
        label.setBounds(50, 50, 300, 30);
        add(label);

        setVisible(true);
    }
}
