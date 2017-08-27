import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public class Consumer implements EventProcessor {
    private final RingBuffer ringBuffer;
    private final Sequence dragSequence
            = new Sequence();

    private final int batchSize;
    private final Sequence workSequence;
    private final Handler consumerHandler;
    private int self;
    private ReentrantLock lock;
    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicBoolean waiting = new AtomicBoolean(false);


    public Consumer(RingBuffer ringBuffer, Sequence workSequence, int size, Handler event, int self, ReentrantLock lock) {
        this.ringBuffer = ringBuffer;
        this.workSequence = workSequence;
        this.batchSize = size;
        this.consumerHandler = event;
        this.self = self;
        this.lock = lock;
    }

    @Override
    public void run() {
        Url event = null;
        long value;
        long cursor;
        while (true) {
            value = workSequence.get() + batchSize * 2 - 1;
            cursor = ringBuffer.getCurrentCursor();
            if (running.get() && (value < cursor)) {
                //		if(!waiting.get()){
//					lock.lock();

                long[] current = workSequence.skipAndGet(batchSize);
                dragSequence.set(current[0]);

                for (int i = 0; i < batchSize; i++) {
                    event = (Url) ringBuffer.getObject(current[i]);
                    event.print(this.self);

                }
                if ((current[batchSize - 1] - ConsumerContainer.getMinium()) > 1024 * 30) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //lock.unlock();
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


//			else{
//				try {
//					Thread.sleep(1);
//					System.out.println("consumer sleep value cursor"+value+"***"+cursor);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}

//		}
    }

    @Override
    public void halt() {
        running.set(false);
    }

    public void reStart() {
        if (!running.get()) {
            running.set(true);
        }
    }

    @Override
    public Sequence getSequence() {
        return dragSequence;
    }

}
