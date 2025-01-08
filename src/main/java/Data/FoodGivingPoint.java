package Data;

import Threads.Klient;
import lombok.Data;

import java.util.concurrent.BlockingQueue;

@Data
public class FoodGivingPoint
{
    private BlockingQueue<Klient> foodQueue;

    public FoodGivingPoint(BlockingQueue<Klient> foodQueue)
    {
        this.foodQueue = foodQueue;
    }
}
