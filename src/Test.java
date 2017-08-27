import java.util.concurrent.locks.ReentrantLock;


public class Test {
	public static void main(String[]args){
		UrlFactory urlFactory=new UrlFactory();
		Sequence sequence=new Sequence(1024*32);
		RingBuffer ringBuffer=new RingBuffer<>(urlFactory, 1024*32, sequence);
		Sequence workSequence=new Sequence(0);
		Handler event=new Handler() {
			
			@Override
			public void onCall() {
				// TODO Auto-generated method stub
				System.out.println();
			}
		};
		ReentrantLock lock= new ReentrantLock();
		Consumer con1=new Consumer(ringBuffer, workSequence, 4, event,1,lock);
		Consumer con2=new Consumer(ringBuffer, workSequence, 4, event,2,lock);
		Consumer con3=new Consumer(ringBuffer, workSequence, 4, event,3,lock);
		ConsumerContainer.addConsumer(con1);
		ConsumerContainer.addConsumer(con2);
		ConsumerContainer.addConsumer(con3);
		new Thread(con1).start();
		new Thread(con2).start();
		new Thread(con3).start();
		Producer p=new Producer(sequence, 4, ringBuffer,lock);
		Producer p1=new Producer(sequence, 4, ringBuffer,lock);
		new Thread(p).start();
		new Thread(p1).start();
	}

}
