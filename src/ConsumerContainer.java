import java.util.Iterator;
import java.util.LinkedList;


public class ConsumerContainer {

    public static LinkedList<Consumer> consumers = new LinkedList<Consumer>();

    public static void addConsumer(Consumer consumer) {
        consumers.add(consumer);
    }

    public static long getMinium() {
        Consumer c;
        long minValue = Long.MAX_VALUE;
        Iterator<Consumer> it = consumers.iterator();
        while (it.hasNext()) {
            c = it.next();
            minValue = Math.min(minValue, c.getSequence().get());
        }
        return minValue;
    }
}
