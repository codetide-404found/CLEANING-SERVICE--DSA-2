public class Member4Demo {

    public static void main(String[] args) {

        System.out.println("================ PART 1: CustomQueue (routine FIFO dispatch) ================");
        partOneQueue();

        System.out.println();
        System.out.println("================ PART 2: CircularQueue (bounded holding buffer) =============");
        partTwoCircularQueue();

        System.out.println();
        System.out.println("================ PART 3: CustomDeque (urgent request insertion) =============");
        partThreeDeque();

        System.out.println();
        System.out.println("================ PART 4: CustomStack (undo / audit log) =====================");
        partFourStack();
    }

    private static void partOneQueue() {
        CustomQueue<CleaningRequest> queue = new CustomQueue<>();

        CleaningRequest r1 = new CleaningRequest(101, "Balme Library", "Routine Sweep", 2, "08:00");
        CleaningRequest r2 = new CleaningRequest(102, "Commonwealth Hall", "Washroom", 3, "08:05");
        CleaningRequest r3 = new CleaningRequest(103, "Volta Hall", "Routine Sweep", 1, "08:07");

        System.out.println("Enqueue " + r1);
        queue.enqueue(r1);
        System.out.println("  -> " + queue.printFrontToRear());

        System.out.println("Enqueue " + r2);
        queue.enqueue(r2);
        System.out.println("  -> " + queue.printFrontToRear());

        System.out.println("Enqueue " + r3);
        queue.enqueue(r3);
        System.out.println("  -> " + queue.printFrontToRear());

        System.out.println("Dequeue -> " + queue.dequeue() + "   (this is who the crew handles first)");
        System.out.println("  -> " + queue.printFrontToRear());

        System.out.println("Dequeue -> " + queue.dequeue());
        System.out.println("  -> " + queue.printFrontToRear());
    }

    private static void partTwoCircularQueue() {

        CircularQueue<CleaningRequest> board = new CircularQueue<>(4);

        CleaningRequest a = new CleaningRequest(201, "Akuafo Hall", "Routine Sweep", 1, "09:00");
        CleaningRequest b = new CleaningRequest(202, "Legon Hall", "Washroom", 2, "09:02");
        CleaningRequest c = new CleaningRequest(203, "N-Block", "Routine Sweep", 1, "09:03");
        CleaningRequest d = new CleaningRequest(204, "Diaspora Hall", "Washroom", 2, "09:05");
        CleaningRequest e = new CleaningRequest(205, "Bani Hall", "Routine Sweep", 1, "09:10");

        System.out.println("Enqueue " + a);
        board.enqueue(a);
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Enqueue " + b);
        board.enqueue(b);
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Enqueue " + c);
        board.enqueue(c);
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Enqueue " + d + "   (board is now full: 4/4)");
        board.enqueue(d);
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Attempt to enqueue " + e + " while full (invalid case)...");
        try {
            board.enqueue(e);
        } catch (StructureFullException ex) {
            System.out.println("  -> caught expected exception: " + ex.getMessage());
        }

        System.out.println("Dequeue -> " + board.dequeue() + "   (frees a slot)");
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Dequeue -> " + board.dequeue());
        System.out.println("  -> " + board.printTraceState());

        System.out.println("Enqueue " + e + "   (this is where the wrap-around happens: rear resets to index 0)");
        board.enqueue(e);
        System.out.println("  -> " + board.printTraceState());
    }

    private static void partThreeDeque() {
        CustomDeque<CleaningRequest> line = new CustomDeque<>();

        CleaningRequest routine1 = new CleaningRequest(301, "JQB", "Routine Sweep", 1, "10:00");
        CleaningRequest routine2 = new CleaningRequest(302, "Central Cafeteria", "Washroom", 2, "10:04");
        CleaningRequest urgent   = new CleaningRequest(303, "Chemistry Lab", "Hazardous Spill", 5, "10:06");

        System.out.println("addRear " + routine1 + "   (normal request joins the back)");
        line.addRear(routine1);
        System.out.println("  -> " + line.printFrontToRear());

        System.out.println("addRear " + routine2);
        line.addRear(routine2);
        System.out.println("  -> " + line.printFrontToRear());

        System.out.println("addFront " + urgent + "   (URGENT: spill jumps straight to the front)");
        line.addFront(urgent);
        System.out.println("  -> " + line.printFrontToRear());

        System.out.println("removeFront -> " + line.removeFront() + "   (crew deals with the spill immediately)");
        System.out.println("  -> " + line.printFrontToRear());

        System.out.println("removeFront -> " + line.removeFront() + "   (back to normal order)");
        System.out.println("  -> " + line.printFrontToRear());
    }

    private static void partFourStack() {
        CustomStack<String> auditLog = new CustomStack<>();

        System.out.println("Simulating a sequence of dispatch actions, each pushed onto the audit log:");

        String event1 = "09:00 - Req#201 ASSIGNED to Crew A (Akuafo Hall)";
        System.out.println("push: " + event1);
        auditLog.push(event1);
        System.out.println("  -> " + auditLog.printFromTop());

        String event2 = "09:12 - Req#201 COMPLETED";
        System.out.println("push: " + event2);
        auditLog.push(event2);
        System.out.println("  -> " + auditLog.printFromTop());

        String event3 = "09:15 - Req#303 ASSIGNED to Crew B (Chemistry Lab, urgent)";
        System.out.println("push: " + event3);
        auditLog.push(event3);
        System.out.println("  -> " + auditLog.printFromTop());

        System.out.println("peek (check most recent action without removing it) -> " + auditLog.peek());

        System.out.println("pop (supervisor undoes the most recent action) -> " + auditLog.pop());
        System.out.println("  -> " + auditLog.printFromTop());

        System.out.println("pop -> " + auditLog.pop());
        System.out.println("  -> " + auditLog.printFromTop());

        System.out.println("pop -> " + auditLog.pop());
        System.out.println("  -> " + auditLog.printFromTop() + "   (stack is empty again)");

        System.out.println("Attempt one more pop on an empty stack (invalid case)...");
        try {
            auditLog.pop();
        } catch (EmptyStructureException ex) {
            System.out.println("  -> caught expected exception: " + ex.getMessage());
        }
    }
}
