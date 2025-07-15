import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    private JLabel labelOutput;
    private JButton buttonCNDP;
    private JButton buttonSDP;

    public GUI() {
        setTitle("Day Trip Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // quit the app when window closed
        setSize(900, 600);
        setLayout(null);
        setResizable(false);
        labelOutput = new JLabel("Day Trip Planner");
        labelOutput.setBounds(375,50, 150,30);

        buttonCNDP = new JButton("Create new day plan");
        buttonCNDP.setBounds(375, 100, 150, 50);
        buttonCNDP.addActionListener(this);

        buttonSDP = new JButton("Saved day plan(s)");
        buttonSDP.setBounds(375,175,150,50);
        buttonSDP.addActionListener(this);

        add(labelOutput);
        add(buttonCNDP);
        add(buttonSDP);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCNDP) {
            new CreateNewDayPlan();
            // Opens window for creating new day plan
        } else if (e.getSource() == buttonSDP) {
            new SavedDayPlans();  // Opens window for saved day plans
        }
    }
}
