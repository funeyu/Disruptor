
public final class RingBuffer<E> {
	private static int INITIAL_VALLUE=0;
	private final int bufferSize;
	private final Object[] entries;
	private  final Sequence sequence;
	
	RingBuffer(EventFactory<E>factory,int size,Sequence sequence){
		this.bufferSize=size;
		this.sequence=sequence;
		entries=new Object[bufferSize];
		fill(factory);
	}
	

	private void fill(EventFactory<E> eventFactory)
    {
        for (int i = 0; i < entries.length; i++)
        {
            entries[i] = eventFactory.newInstance(i);
        }
    }
	

	public  long getCurrentCursor(){
		return sequence.get();
	}
	
	public Object getObject(long l){
		int index=(int) (l%bufferSize);
		return entries[index];
	}
	
	public void addJobs(long index,Url url){
		this.entries[(int) (index%bufferSize)]=url;
	}
	public Sequence getSequence(){
		return this.sequence;
	}
}
