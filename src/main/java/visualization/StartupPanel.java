package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartupPanel extends JPanel {
    private final JTextField numClientsField;
    private final JTextField numWorkersField;
    private final JTextField numCashiersField;
    private final JTextField numSeatsField;
    private final JButton startButton;

    public StartupPanel() {
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Number of Clients:"));
        numClientsField = new JTextField("20");
        add(numClientsField);

        add(new JLabel("Number of Workers:"));
        numWorkersField = new JTextField("2");
        add(numWorkersField);

        add(new JLabel("Number of Cashiers:"));
        numCashiersField = new JTextField("4");
        add(numCashiersField);

        add(new JLabel("Number of Seats:"));
        numSeatsField = new JTextField("8");
        add(numSeatsField);

        startButton = new JButton("Start Simulation");
        add(startButton);
    }

    public void setStartAction(ActionListener startAction) {
        startButton.addActionListener(startAction);
    }

    public int getNumClients() {
        return Integer.parseInt(numClientsField.getText());
    }

    public int getNumWorkers() {
        return Integer.parseInt(numWorkersField.getText());
    }

    public int getNumCashiers() {
        return Integer.parseInt(numCashiersField.getText());
    }

    public int getNumSeats() {
        return Integer.parseInt(numSeatsField.getText());
    }
}