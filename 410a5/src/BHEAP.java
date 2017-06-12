import java.util.Random;
import java.util.Scanner;

class BHEAPDriver {
	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		if (args.length == 0) {
			boolean done = false;
			BHEAP heap = new BHEAP();
			while (!done) {
				System.out.println("command (q,n,i,d,g,s,b,f,p,o)?");
				String command = s.nextLine();
				if (command.equals("q")) {
					System.exit(0);
				} else if (command.equals("n")) {
					heap = new BHEAP();
				} else if (command.equals("i")) {
					System.out.println("Enter an integer");
					int num = s.nextInt();
					s.nextLine();
					System.out.println("Enter a string");
					String data = s.nextLine();
					HeapElement element = new HeapElement(num, data);
					heap.insert(element);
				} else if (command.equals("d")) {
					heap.delMin();
				} else if (command.equals("g")) {
					System.out.println(heap.getMin().priority + ":"
							+ heap.getMin().data);
				} else if (command.equals("s")) {
					System.out.println(heap.size());
				} else if (command.equals("b")) {
					HeapElement[] vals = new HeapElement[100];
					for (int i = 0;; i++) {
						System.out.println("Enter an integer");
						int n = s.nextInt();
						s.nextLine();
						if (n == -1) {
							break;
						} else {
							System.out.println("Enter a string");
							String k = s.nextLine();
							HeapElement element = new HeapElement(n, k);
							vals[i] = element;
						}
					}
					heap = new BHEAP();
					heap.build(vals);
				} else if (command.equals("f")) {
					System.out
							.println("How many items would you like to generate?");
					int result = s.nextInt();
					s.nextLine();
					for (int i = 0; i < result; i++) {
						MyRandom random = new MyRandom();
						HeapElement element = new HeapElement(
								(int) (Math.random() * 100), random.nextString(
										6, 10));
						heap.insert(element);
					}
				} else if (command.equals("p")) {
					heap.print();
				} else if (command.equals("o")) {
					heap.sortPrint();
				} else {
				}
			}
		}
		// } else {
		// HeapElement[] arguments = new HeapElement[7];
		//
		// for (int i = 0; i < 7; i++) {
		// int n = s.nextInt();
		// String k = s.nextLine();
		// arguments[i] = new HeapElement(n, k);
		// }
		else {
			BHEAP heap = new BHEAP();
			HeapElement[] arguments = new HeapElement[100];
			int pri;
			String assocData;
			int na = args.length; // our test data will make this even
			for (int i = 0, c = 0; i < na; i += 2, c++) {
				pri = Integer.parseInt(args[i]);
				assocData = args[i + 1];
				HeapElement e = new HeapElement(pri, assocData);
				arguments[c] = e;
				heap.insert(e);
			}

			// HeapElement e1 = new HeapElement(17, "jones");
			// heap.insert(e1);
			// HeapElement e2 = new HeapElement(21, "adams");
			// heap.insert(e2);
			// HeapElement e3 = new HeapElement(3, "miller");
			// heap.insert(e3);
			// HeapElement e4 = new HeapElement(7, "smith");
			// heap.insert(e4);
			// HeapElement e5 = new HeapElement(13, "davis");
			// heap.insert(e5);
			// HeapElement e6 = new HeapElement(34, "carson");
			// heap.insert(e6);
			// HeapElement e7 = new HeapElement(9, "wilson");
			// heap.insert(e7);

			heap.print();

			BHEAP heap2 = new BHEAP();
			heap2.build(arguments);

			heap2.print();
			heap2.sortPrint();
		}
	}
}

class HeapElement {

	int priority;
	String data;

	public HeapElement(int priority, String data) {
		this.priority = priority;
		this.data = data;
	}
}

class BHEAP {

	int root;
	HeapElement[] elts;
	int last;

	public BHEAP() {
		elts = new HeapElement[100];
		root = 1;
		last = 0;
	}

	public int size() {

		return this.last;
	}

	public HeapElement getMin() {

		return this.elts[this.root];
	}

	public void insert(HeapElement element) {

		this.last++;
		this.elts[this.last] = element;
		int n = this.last;
		int p = (int) this.PN(n);
		HeapElement temp;
		while ((p != 0) && (element.priority < this.elts[p].priority)) {
			temp = this.elts[n];
			this.elts[n] = this.elts[p];
			this.elts[p] = temp;
			n = p;
			p = (int) this.PN(n);
		}
	}

	public void delMin() {

		if (this.size() == 0) {
			return;
		}
		if (this.size() == 1) {
			// this.elts[1].priority = Integer.MAX_VALUE;
			this.last--;
			return;
		}
		this.elts[this.root] = this.elts[this.last];
		// this.elts[this.last].priority = Integer.MAX_VALUE;
		this.last--;
		boolean done = false;
		HeapElement temp;
		int n = this.root;
		int c = 0;
		while (!done) {
			if (this.isLeaf(n)) {
				done = true;
			} else {
				if (this.hasOnlyLC(n)) {
					c = this.leftChild(n);
				} else {
					c = this.LCV(n).priority < this.RCV(n).priority ? this
							.leftChild(n) : this.rightChild(n);
				}
				if (this.elts[n].priority > this.elts[c].priority) {
					temp = this.elts[c];
					this.elts[c] = this.elts[n];
					this.elts[n] = temp;
					n = c;
				} else {
					done = true;
				}
			}
		}
	}

	public void build(HeapElement[] vals) {
		int i;
		for (i = 0; i < vals.length; i++) {
			if (vals[i] != null) {
			this.elts[i + 1] = vals[i];
			}
			else {
				break;
			}
		}
		this.last = i;
		int sn = (int) this.PN(this.last);
		for (int k = sn; k > 0; k--) {
			HeapElement temp;
			boolean done = false;
			int n = k;
			int c = 0;
			while (!done) {
				if (this.isLeaf(n)) {
					done = true;
				} else {
					if (this.hasOnlyLC(n)) {
						c = this.leftChild(n);
					} else {
						c = (this.LCV(n).priority < this.RCV(n).priority) ? this
								.leftChild(n) : this.rightChild(n);
					}
					if (this.elts[n].priority > this.elts[c].priority) {
						temp = this.elts[c];
						this.elts[c] = this.elts[n];
						this.elts[n] = temp;
						n = c;
					} else {
						done = true;
					}
				}

			}
		}
	}

	public int leftChild(int n) {
		return 2 * n;
	}

	public int rightChild(int n) {
		return (2 * n) + 1;
	}

	public double PN(double n) {
		return Math.floor(n / 2);
	}

	public HeapElement LCV(int n) {
		return this.elts[this.leftChild(n)];
	}

	public HeapElement RCV(int n) {
		return this.elts[this.rightChild(n)];
	}

	public HeapElement PNV(double n) {
		return this.elts[(int) this.PN(n)];
	}

	public void print() {
		for (int i = 1; i <= this.last; i++)
			if (i == this.last) {
				System.out.println(this.elts[i].priority + ":"
						+ this.elts[i].data);
			} else {
				System.out.println(this.elts[i].priority + ":"
						+ this.elts[i].data + ", ");
			}
	}

	public void sortPrint() {
		int count = this.size();
		for (int i = 0; i < count; i++) {
			if (i == this.last - 1) {
				System.out.println(this.getMin().priority + ":"
						+ this.getMin().data);
			} else {
				System.out.println(this.getMin().priority + ":"
						+ this.getMin().data + ", ");
			}
			this.delMin();
		}

	}

	public boolean isLeaf(int n) {
		return (this.leftChild(n) > this.last && this.rightChild(n) > this.last);
	}

	public boolean hasOnlyLC(int n) {
		return (this.rightChild(n) > this.last && this.leftChild(n) <= this.last);
	}
}

class MyRandom {

	private static Random rn = new Random();

	MyRandom() {
	}

	public static int rand(int lo, int hi) {
		int n = hi - lo + 1;
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}

	public static String nextString(int lo, int hi) {
		int n = rand(lo, hi);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) rand('a', 'z');
		return new String(b, 0);
	}

	public static String nextString() {
		return nextString(5, 25);
	}

}