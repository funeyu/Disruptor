import sun.misc.Unsafe;


public class Sequence {
    static final long INITIAL_VALUE = -1L;
    private static final Unsafe UNSAFE;
    private static final long VALUE_OFFSET;

    static {
        UNSAFE = Util.getUnsafe();
        final int base = UNSAFE.arrayBaseOffset(long[].class);
        final int scale = UNSAFE.arrayIndexScale(long[].class);
        VALUE_OFFSET = base + (scale * 7);
    }

    private final long[] paddedValue = new long[15];


    public Sequence() {
        this(INITIAL_VALUE);
    }


    public Sequence(final long initialValue) {
        UNSAFE.putOrderedLong(paddedValue, VALUE_OFFSET, initialValue);
    }


    public long get() {
        return UNSAFE.getLongVolatile(paddedValue, VALUE_OFFSET);
    }

    public boolean compareAndSet(final long expectedValue, final long newValue) {
        return UNSAFE.compareAndSwapLong(paddedValue, VALUE_OFFSET, expectedValue, newValue);
    }

    public void set(final long value) {
        UNSAFE.putOrderedLong(paddedValue, VALUE_OFFSET, value);
    }

    public long[] skipAndGet() {
        return skipAndGet(1);
    }

    public long[] skipAndGet(final int times) {
        long[] tracks = new long[times];

        for (int i = 0; i < times; i++) {
            long currentValue = get();
            long newValue = currentValue;
            do {
                currentValue = currentValue + 1;
            } while (!compareAndSet(currentValue - 1, currentValue));
            tracks[i] = currentValue - 1;
        }
        return tracks;
    }


    public long[] laneSkip(final int times, final RingBuffer ringBuffer) {
        long[] lanes = new long[times];

        for (int i = 0; i < times; i++) {
            long currentValue = get();
            long newValue = currentValue;
            do {
                currentValue = currentValue + 1;
            } while (!compareAndSet(currentValue - 1, currentValue));
            lanes[i] = currentValue - 1;
        }
        return lanes;
    }
}