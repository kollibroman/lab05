package Threads;

import Threads.Client;
import visualization.VisualizationPanel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Cashier extends Thread {
    private final BlockingQueue<Client> queue;
    private final VisualizationPanel visualizationPanel;

    public Cashier(BlockingQueue<Client> queue, VisualizationPanel visualizationPanel) {
        this.queue = queue;
        this.visualizationPanel = visualizationPanel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = queue.take();
                processClient(client);
                synchronized (visualizationPanel) {
                    String queueName = getIndex(queue) < queue.size() / 4 ? "Cashiers Queue 1" :
                            getIndex(queue) < queue.size() / 2 ? "Cashiers Queue 2" :
                                    getIndex(queue) < 3 * queue.size() / 4 ? "Cashiers Queue 3" : "Cashiers Queue 4";
                    visualizationPanel.updateQueueCount(queueName, queue.size());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processClient(Client client) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
        synchronized (visualizationPanel) {
            visualizationPanel.updateSeats();
        }
    }

    private int getIndex(BlockingQueue<Client> queue) {
        return 0; // Placeholder implementation
    }
}