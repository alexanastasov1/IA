import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    private JLabel labelOutput;
    private JButton buttonCNDP;
    private JButton buttonSDP;
    private JComboBox dropList;

    public GUI() {
        setTitle("Day Trip Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // quit the app when window closed
        setSize(600, 400);
        setLayout(null);
        labelOutput = new JLabel("Day Trip Planner");
        labelOutput.setBounds(225,50, 150,30);

        /* dropList = new JComboBox<>();
        dropList.setBounds(50, 200, 150, 50); */

        buttonCNDP = new JButton("Create new day plan");
        buttonCNDP.setBounds(225, 100, 150, 50);
        buttonCNDP.addActionListener(this);

        buttonSDP = new JButton("Saved day plan(s)");
        buttonSDP.setBounds(225,175,150,50);
        buttonSDP.addActionListener(this);

        add(labelOutput);
        add(buttonCNDP);
        add(buttonSDP);
        /* add(dropList); */
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
