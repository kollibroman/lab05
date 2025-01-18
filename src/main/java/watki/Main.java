package watki;

import Threads.Cashier;
import Threads.Client;
import Threads.Worker;
import visualization.StartupPanel;
import visualization.VisualizationPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cafeteria Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        StartupPanel startupPanel = new StartupPanel();
        startupPanel.setStartAction(e -> {
            int numClients = startupPanel.getNumClients();
            int numWorkers = startupPanel.getNumWorkers();
            int numCashiers = startupPanel.getNumCashiers();
            int numSeats = startupPanel.getNumSeats();
            startSimulation(frame, numClients, numWorkers, numCashiers, numSeats);
        });

        frame.add(startupPanel);
        frame.setVisible(true);
    }

    private static void startSimulation(JFrame frame, int numClients, int numWorkers, int numCashiers, int numSeats) {
        int queueCapacity = 5;

        BlockingQueue<Client> entranceQueue = new ArrayBlockingQueue<>(numClients);
        BlockingQueue<Client>[] queuesToPoints = new ArrayBlockingQueue[numWorkers];
        BlockingQueue<Client>[] queuesToCashiers = new ArrayBlockingQueue[numCashiers];
        Semaphore seats = new Semaphore(numSeats);

        for (int i = 0; i < numWorkers; i++) {
            queuesToPoints[i] = new ArrayBlockingQueue<>(queueCapacity);
        }

        for (int i = 0; i < numCashiers; i++) {
            queuesToCashiers[i] = new ArrayBlockingQueue<>(queueCapacity);
        }

        VisualizationPanel visualizationPanel = new VisualizationPanel(entranceQueue, queuesToPoints, queuesToCashiers, seats);

        frame.getContentPane().removeAll();
        frame.add(visualizationPanel);
        frame.revalidate();
        frame.repaint();

        for (int i = 0; i < numClients; i++) {
            new Client(entranceQueue, queuesToPoints, queuesToCashiers, seats, visualizationPanel).start();
        }

        for (int i = 0; i < numWorkers; i++) {
            new Worker(queuesToPoints[i], queuesToCashiers, visualizationPanel).start();
        }

        for (int i = 0; i < numCashiers; i++) {
            new Cashier(queuesToCashiers[i], visualizationPanel).start();
        }
    }
}