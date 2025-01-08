package watki;

import Data.CashRegister;
import Data.FoodGivingPoint;
import Threads.Klient;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadHandler
{
    public void startSimulation()
    {
        var rnd = new Random();

        var randomKlientNumber = rnd.nextInt(20);

        var entranceQueue = new LinkedBlockingDeque<Klient>(randomKlientNumber);

        var foodStall1 = new FoodGivingPoint(new LinkedBlockingQueue<Klient>(randomKlientNumber));
        var foodStall2 = new FoodGivingPoint(new LinkedBlockingQueue<Klient>(randomKlientNumber));

        var cashRegister1 = new CashRegister(new LinkedBlockingQueue<Klient>(randomKlientNumber));
        var cashRegister2 = new CashRegister(new LinkedBlockingQueue<Klient>(randomKlientNumber));
        var cashRegister3 = new CashRegister(new LinkedBlockingQueue<Klient>(randomKlientNumber));
        var cashRegister4 = new CashRegister(new LinkedBlockingQueue<Klient>(randomKlientNumber));


    }
}
