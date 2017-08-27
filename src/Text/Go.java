package Text;

public class Go {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Processors()).start();

	}

}
