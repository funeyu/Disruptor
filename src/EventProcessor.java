
public interface EventProcessor extends Runnable{
	void halt();
	Sequence getSequence();
	
}
