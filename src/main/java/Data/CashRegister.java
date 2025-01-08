package Data;

import Threads.Klient;
import lombok.Data;

import java.util.concurrent.BlockingQueue;

@Data
public class CashRegister
{
    private BlockingQueue<Klient> cashRegisterQueue;

    public CashRegister(BlockingQueue<Klient> cashRegisterQueue)
    {
        this.cashRegisterQueue = cashRegisterQueue;
    }
}
