public class Member4Tests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Running Member 4 structure tests...\n");

        testCustomQueue();
        testCircularQueue();
        testCustomDeque();
        testCustomStack();

        System.out.println("\n----------------------------------------");
        System.out.println("Total: " + (passed + failed) + "   Passed: " + passed + "   Failed: " + failed);
    }

    private static void testCustomQueue() {
        System.out.println("-- CustomQueue --");

        CustomQueue<Integer> q = new CustomQueue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        check("normal: FIFO order", q.dequeue() == 1 && q.dequeue() == 2 && q.dequeue() == 3);

        CustomQueue<String> single = new CustomQueue<>();
        single.enqueue("only-one");
        check("boundary: size after one enqueue", single.size() == 1);
        single.dequeue();
        check("boundary: isEmpty after removing the only element", single.isEmpty());

        CustomQueue<Integer> empty = new CustomQueue<>();
        boolean threw = false;
        try {
            empty.dequeue();
        } catch (EmptyStructureException ex) {
            threw = true;
        }
        check("invalid: dequeue on empty queue throws EmptyStructureException", threw);
    }

    private static void testCircularQueue() {
        System.out.println("-- CircularQueue --");

        CircularQueue<Integer> cq = new CircularQueue<>(3);
        cq.enqueue(10);
        cq.enqueue(20);
        check("normal: front element correct", cq.peekFront() == 10);
        check("normal: dequeue returns front element", cq.dequeue() == 10);

        CircularQueue<Integer> cap2 = new CircularQueue<>(2);
        cap2.enqueue(1);
        cap2.enqueue(2);
        check("boundary: isFull at capacity", cap2.isFull());
        cap2.dequeue();
        cap2.enqueue(3);
        check("boundary: wrap-around keeps correct order", cap2.dequeue() == 2 && cap2.dequeue() == 3);

        CircularQueue<Integer> full = new CircularQueue<>(1);
        full.enqueue(99);
        boolean threw = false;
        try {
            full.enqueue(100);
        } catch (StructureFullException ex) {
            threw = true;
        }
        check("invalid: enqueue on full circular queue throws StructureFullException", threw);
    }

    private static void testCustomDeque() {
        System.out.println("-- CustomDeque --");

        CustomDeque<String> d = new CustomDeque<>();
        d.addRear("routine-1");
        d.addRear("routine-2");
        d.addFront("URGENT");
        check("normal: urgent request is at the front", d.peekFront().equals("URGENT"));
        check("normal: last routine request is still at the rear", d.peekRear().equals("routine-2"));

        CustomDeque<Integer> single = new CustomDeque<>();
        single.addFront(42);
        check("boundary: single element is both front and rear", single.peekFront() == 42 && single.peekRear() == 42);
        single.removeFront();
        check("boundary: empty after removing the only element", single.isEmpty());

        CustomDeque<Integer> empty = new CustomDeque<>();
        boolean threw = false;
        try {
            empty.removeRear();
        } catch (EmptyStructureException ex) {
            threw = true;
        }
        check("invalid: removeRear on empty deque throws EmptyStructureException", threw);
    }

    private static void testCustomStack() {
        System.out.println("-- CustomStack --");

        CustomStack<String> s = new CustomStack<>();
        s.push("first-event");
        s.push("second-event");
        s.push("third-event");
        check("normal: LIFO order on pop", s.pop().equals("third-event") && s.pop().equals("second-event"));

        CustomStack<Integer> single = new CustomStack<>();
        single.push(7);
        check("boundary: peek after single push", single.peek() == 7);
        single.pop();
        check("boundary: isEmpty after popping the only element", single.isEmpty());

        CustomStack<Integer> empty = new CustomStack<>();
        boolean threw = false;
        try {
            empty.pop();
        } catch (EmptyStructureException ex) {
            threw = true;
        }
        check("invalid: pop on empty stack throws EmptyStructureException", threw);
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }
}
