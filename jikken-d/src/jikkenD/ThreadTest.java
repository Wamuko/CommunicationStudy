package jikkenD;

public class ThreadTest implements Runnable {
	Thread th;

	public ThreadTest() {
		th = new Thread(this);
		th.start();
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("In run(): i=" + i);
		}
	}

	public static void main(String[] args) {
		ThreadTest tt = new ThreadTest();

		for (int i = 0; i < 1000; i++) {
			System.out.println("main(): i=" + i);
		}
	}

}
