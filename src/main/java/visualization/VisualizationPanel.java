package visualization;

import Threads.Client;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.ArrayList;

public class VisualizationPanel extends JPanel {
    private final BlockingQueue<Client> entranceQueue;
    private final BlockingQueue<Client>[] queuesToPoints;
    private final BlockingQueue<Client>[] queuesToCashiers;
    private final Semaphore seats;
    private final List<Client> seatedClients;
    private final JLabel[] seatLabels;

    private final JLabel entranceQueueLabel;
    private final JLabel[] pointsQueueLabels;
    private final JLabel[] cashiersQueueLabels;

    public VisualizationPanel(BlockingQueue<Client> entranceQueue, BlockingQueue<Client>[] queuesToPoints, BlockingQueue<Client>[] queuesToCashiers, Semaphore seats) {
        this.entranceQueue = entranceQueue;
        this.queuesToPoints = queuesToPoints;
        this.queuesToCashiers = queuesToCashiers;
        this.seats = seats;
        this.seatedClients = new ArrayList<>();
        this.seatLabels = new JLabel[seats.availablePermits()];

        setLayout(new GridLayout(queuesToPoints.length + queuesToCashiers.length + 4, 1));

        entranceQueueLabel = new JLabel();
        add(entranceQueueLabel);

        pointsQueueLabels = new JLabel[queuesToPoints.length];
        for (int i = 0; i < queuesToPoints.length; i++) {
            pointsQueueLabels[i] = new JLabel();
            add(pointsQueueLabels[i]);
        }

        cashiersQueueLabels = new JLabel[queuesToCashiers.length];
        for (int i = 0; i < queuesToCashiers.length; i++) {
            cashiersQueueLabels[i] = new JLabel();
            add(cashiersQueueLabels[i]);
        }

        JPanel seatsPanel = new JPanel(new GridLayout(2, seats.availablePermits() / 2, 5, 5));
        for (int i = 0; i < seats.availablePermits(); i++) {
            seatLabels[i] = new JLabel("*", SwingConstants.CENTER);
            seatLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            seatsPanel.add(seatLabels[i]);
        }
        add(seatsPanel);

        updateState();
    }

    public synchronized void updateQueueCount(String queueName, int size) {
        updateState();
    }

    public synchronized void updateSeats() {
        updateState();
    }

    public synchronized void seatClient(Client client) {
        if (seatedClients.size() < seats.availablePermits()) {
            animateSeating(client, true);
        }
    }

    public synchronized void unseatClient(Client client) {
        animateSeating(client, false);
    }

    private void animateSeating(Client client, boolean isSeating) {
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            if (isSeating) {
                seatedClients.add(client);
            } else {
                seatedClients.remove(client);
            }
            updateSeatsVisualization();
            timer.stop();
        });
        timer.start();
    }

    private void updateState() {
        entranceQueueLabel.setText("Entrance Queue: " + generateQueueVisualization(entranceQueue));
        for (int i = 0; i < queuesToPoints.length; i++) {
            pointsQueueLabels[i].setText("Points Queue " + (i + 1) + ": " + generateQueueVisualization(queuesToPoints[i]));
        }
        for (int i = 0; i < queuesToCashiers.length; i++) {
            cashiersQueueLabels[i].setText("Cashiers Queue " + (i + 1) + ": " + generateQueueVisualization(queuesToCashiers[i]));
        }
        updateSeatsVisualization();
    }

    private String generateQueueVisualization(BlockingQueue<Client> queue) {
        StringBuilder visualization = new StringBuilder();
        for (Client client : queue) {
            visualization.append(client.getLetter()).append(" ");
        }
        for (int i = queue.size(); i < 5; i++) {
            visualization.append("* ");
        }
        return visualization.toString().trim();
    }

    private void updateSeatsVisualization() {
        for (int i = 0; i < seatLabels.length; i++) {
            if (i < seatedClients.size()) {
                seatLabels[i].setText(String.valueOf(seatedClients.get(i).getLetter()));
            } else {
                seatLabels[i].setText("*");
            }
        }
    }
}