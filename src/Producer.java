import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public class Producer implements EventProcessor {
    private final Sequence sequence;
    private final int batchSize;
    private final RingBuffer ringBuffer;
    private AtomicBoolean running = new AtomicBoolean(true);
    private ReentrantLock lock;
    private AtomicBoolean wait = new AtomicBoolean(false);

    public Producer(Sequence sequence, int batchSize, RingBuffer ringBuffer, ReentrantLock lock) {
        this.sequence = sequence;
        this.batchSize = batchSize;
        this.ringBuffer = ringBuffer;
        this.lock = lock;
    }

    @Override
    public void run() {
        long[] lanes = new long[batchSize];
        int i = 0;

        while (true) {
            long value = sequence.get();
            long min = ConsumerContainer.getMinium();
            if (running.get() && ((value + batchSize * 2 - 1024 * 32) < min)) {
                //lock.lock();
                lanes = sequence.laneSkip(batchSize, ringBuffer);

                for (int n = 0; n < batchSize; n++) {
                    i++;
                    Url url = new Url("url" + i);
                    ringBuffer.addJobs(lanes[n], url);
                }
                //lock.unlock();
            } else {
                try {
                    Thread.sleep(1);
                    System.out.println("producer sleep    " + value + "min" + min);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }

    }

    @Override
    public void halt() {
        // TODO Auto-generated method stub

    }

    @Override
    public Sequence getSequence() {
        // TODO Auto-generated method stub
        return null;
    }

}
