import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    private JLabel labelOutput;
    private JTextField textUsername;
    private JButton buttonOK;
    private JList dropList;

    // private final int WIDGET_HEIGHT = 30; use constants for easier rearranging


    public GUI() {
        setTitle("Window title");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // quit the app when we close the window
        setSize(600, 400);
        setLayout(null);
        labelOutput = new JLabel("Hello World");
        labelOutput.setBounds(50,50, 150,30);


        textUsername = new JTextField();
        textUsername.setBounds(50, 90, 200, 30);

        dropList = new JList<>();
        dropList.setBounds(50, 200, 150, 50);

        buttonOK = new JButton("OK");
        buttonOK.setBounds(50, 130, 150, 50);
        buttonOK.addActionListener(this);

        add(labelOutput);
        add(textUsername);
        add(buttonOK);
        add(dropList);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
