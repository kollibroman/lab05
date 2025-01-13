package Threads;

import visualization.VisualizationPanel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Worker extends Thread {
    private final BlockingQueue<Client> queue;
    private final BlockingQueue<Client>[] queuesToCashiers;
    private final VisualizationPanel visualizationPanel;

    public Worker(BlockingQueue<Client> queue, BlockingQueue<Client>[] queuesToCashiers, VisualizationPanel visualizationPanel) {
        this.queue = queue;
        this.queuesToCashiers = queuesToCashiers;
        this.visualizationPanel = visualizationPanel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = queue.take();
                processClient(client);
                synchronized (visualizationPanel) {
                    String queueName = getIndex(queue) < queue.size() / 2 ? "Points Queue 1" : "Points Queue 2";
                    visualizationPanel.updateQueueCount(queueName, queue.size());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processClient(Client client) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
        BlockingQueue<Client> nextQueue = getShortestQueue(queuesToCashiers);
        nextQueue.put(client);
        synchronized (visualizationPanel) {
            String queueName = getIndex(nextQueue) < nextQueue.size() / 4 ? "Cashiers Queue 1" :
                    getIndex(nextQueue) < nextQueue.size() / 2 ? "Cashiers Queue 2" :
                            getIndex(nextQueue) < 3 * nextQueue.size() / 4 ? "Cashiers Queue 3" : "Cashiers Queue 4";
            visualizationPanel.updateQueueCount(queueName, nextQueue.size());
        }
    }

    private BlockingQueue<Client> getShortestQueue(BlockingQueue<Client>[] queues) {
        BlockingQueue<Client> shortestQueue = queues[0];
        for (BlockingQueue<Client> queue : queues) {
            if (queue.size() < shortestQueue.size()) {
                shortestQueue = queue;
            }
        }
        return shortestQueue;
    }

    private int getIndex(BlockingQueue<Client> queue) {
        return 0; // Placeholder implementation
    }
}