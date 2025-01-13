package Threads;

import visualization.VisualizationPanel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Client extends Thread {
    private static char nextLetter = 'A';
    private final char letter;
    private final BlockingQueue<Client> entranceQueue;
    private final BlockingQueue<Client>[] queuesToPoints;
    private final BlockingQueue<Client>[] queuesToCashiers;
    private final Semaphore seats;
    private final VisualizationPanel visualizationPanel;

    public Client(BlockingQueue<Client> entranceQueue, BlockingQueue<Client>[] queuesToPoints, BlockingQueue<Client>[] queuesToCashiers, Semaphore seats, VisualizationPanel visualizationPanel) {
        this.entranceQueue = entranceQueue;
        this.queuesToPoints = queuesToPoints;
        this.queuesToCashiers = queuesToCashiers;
        this.seats = seats;
        this.visualizationPanel = visualizationPanel;
        synchronized (Client.class) {
            this.letter = nextLetter++;
        }
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                enterQueue();
                getMeal();
                payForMeal();
                eatMeal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enterQueue() throws InterruptedException {
        entranceQueue.put(this);
        synchronized (visualizationPanel) {
            visualizationPanel.updateQueueCount("Entrance Queue", entranceQueue.size());
        }
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
    }

    private void getMeal() throws InterruptedException {
        entranceQueue.take();
        BlockingQueue<Client> queue = getShortestQueue(queuesToPoints);
        queue.put(this);
        synchronized (visualizationPanel) {
            String queueName = getIndex(queue, queuesToPoints) < queuesToPoints.length / 2 ? "Points Queue 1" : "Points Queue 2";
            visualizationPanel.updateQueueCount(queueName, queue.size());
        }
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
    }

    private void payForMeal() throws InterruptedException {
        BlockingQueue<Client> queue = getShortestQueue(queuesToCashiers);
        queue.put(this);
        synchronized (visualizationPanel) {
            String queueName = getIndex(queue, queuesToCashiers) < queuesToCashiers.length / 4 ? "Cashiers Queue 1" :
                    getIndex(queue, queuesToCashiers) < queuesToCashiers.length / 2 ? "Cashiers Queue 2" :
                            getIndex(queue, queuesToCashiers) < 3 * queuesToCashiers.length / 4 ? "Cashiers Queue 3" : "Cashiers Queue 4";
            visualizationPanel.updateQueueCount(queueName, queue.size());
        }
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
    }

    private void eatMeal() throws InterruptedException {
        seats.acquire();
        synchronized (visualizationPanel) {
            visualizationPanel.updateSeats();
        }

        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));

        synchronized (visualizationPanel) {
            visualizationPanel.updateSeats();
        }
        seats.release();
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

    private int getIndex(BlockingQueue<Client> queue, BlockingQueue<Client>[] queues) {
        for (int i = 0; i < queues.length; i++) {
            if (queues[i] == queue) {
                return i;
            }
        }
        return -1;
    }
}