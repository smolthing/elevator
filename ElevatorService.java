import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ElevatorService {
    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController();

        Scanner scanner = new Scanner(System.in);

        // Print a cute passenger from Spirted Away
        printCutePassengerInElevator();

        // Get the number of requests
        System.out.print("Enter the number of requests: ");
        int numberOfRequests = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Gather requests
        for (int i = 0; i < numberOfRequests; i++) {
            System.out.print("Enter request " + i + " (from,to): ");
            String input = scanner.nextLine();

            // Parse the input
            String[] parts = input.split(",");
            if (parts.length == 2) {
                try {
                    int fromFloor = Integer.parseInt(parts[0].trim());
                    int toFloor = Integer.parseInt(parts[1].trim());
                    controller.request(fromFloor, toFloor);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter numbers separated by a comma.");
                    i--; // Decrement i to re-enter the current request
                }
            } else {
                System.out.println("Invalid input. Please enter two numbers separated by a comma.");
                i--; // Decrement i to re-enter the current request
            }
        }

        controller.processRequest();
    }

    private static void printCutePassengerInElevator() {
        System.out.println("　　　|＼___＿____|＿＿＿|＿＿＿＿＿＿＿／|");
        System.out.println("　　　|　 |　 　 ／＿＿＿_＼.　　　　　|  |");
        System.out.println("　　　|　 |　　￣ ‘,,,,.'￣＼　　　　　|  |");
        System.out.println("　　　|　 | 　 _/ 　 　 　　＼_　　　　|  |");
        System.out.println("　　　|　 | ／;'　 　 /\"\"ヽ　　　　　　|  | 　_");
        System.out.println("　　　|. ／　:i　　 ノ　　:、 /ヽ|　　 |　|　|△|");
        System.out.println("　　　|（,,,.-ｰi,,,,／__,..--､_ ＼　|　|　|　|▽|");
        System.out.println("　　　|　 | 　|　~\"\"　 　 　ﾞﾞﾞﾞ.　|:l||  |   -");
        System.out.println("　　　|γ´wヽ 　　　　　　　　 ﾉﾉ 　　　|  |");
        System.out.println("　　　|'iq|゜_ ゜|`ｰ-､.,_______,..-ｰﾌ　|  |");
        System.out.println("　　　|j;(つ⊂)､ 　 |＿＿_|　 ノμ|　　　|  |");
        System.out.println("　　　| _)__(__(　ヽ,,,,,,,,八,,,,,,,,/|　|");
        System.out.println("　　　|∠三三:三三 三三:三三三三三三三三＼ |");
    }
}

public class ElevatorController {
    private final Elevator elevator;
    private final PriorityQueue<Integer> moveUpQueue;
    private final PriorityQueue<Integer> moveDownQueue;
    private static final int MAX_FLOOR = 10;
    private static final int MIN_FLOOR = 1;

    public ElevatorController() {
        elevator = new Elevator(MIN_FLOOR, MAX_FLOOR);
        moveUpQueue = new PriorityQueue<>();
        moveDownQueue = new PriorityQueue<>(Comparator.reverseOrder());
    }

    private boolean isValidRange(int floor) {
        return floor >= MIN_FLOOR && floor <= MAX_FLOOR;
    }

    public void request(int from, int to) {
        // validate request
        if (from == to || !isValidRange(from) || !isValidRange(to)) {
            return;
        }

        if (from < to) {
            addMoveUpQueue(from, to);
        } else {
            addMoveDownQueue(from, to);
        }
    }

    public void addMoveUpQueue(int from, int to) {
        moveUpQueue.add(from);
        moveUpQueue.add(to);
    }

    public void addMoveDownQueue(int from, int to) {
        moveDownQueue.add(from);
        moveDownQueue.add(to);
    }

    public void processRequest() {
        boolean isDirectionUp = true; 
        while(!moveUpQueue.isEmpty() || !moveDownQueue.isEmpty()) {
            if (isDirectionUp) {
                processRequestsInDirection(moveUpQueue, Direction.UP);
            } else {
                processRequestsInDirection(moveDownQueue, Direction.DOWN);
            }

            // Toggle the direction
            isDirectionUp = !isDirectionUp;
        }
    }

    private void processRequestsInDirection(PriorityQueue<Integer> requestQueue, Direction direction) {
        while (!requestQueue.isEmpty()) {
            int currentFloor = elevator.getCurrentFloor();
            int requestFloor = requestQueue.poll();

            while (currentFloor != requestFloor) {
                elevator.move(direction);
                currentFloor = elevator.getCurrentFloor();
            }

            elevator.stop(requestFloor);
        }
    }
}

public class Elevator {
    private final int maxNoOfFloor;
    private final int MOVE_DELAY_MS = 1000; // 1 second
    private final int STOP_DELAY_MS = 2000; // 2 second
    private int currentFloor;
    private Direction currentDirection = Direction.NONE;

    public Elevator(int initialFloor, int maxNoOfFloor) {
        this.maxNoOfFloor = maxNoOfFloor;
        this.currentFloor = initialFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getMaxFloor() {
        return maxNoOfFloor;
    }

    public void move(Direction direction) {
        currentFloor += direction.getValue();
        System.out.println("Elevator moves to " + currentFloor);
        addDelay(MOVE_DELAY_MS);
    }

    public void stop(int floor) {
        System.out.println("====================");
        System.out.println("Elevator stops at " + floor);
        System.out.println("====================");
        addDelay(STOP_DELAY_MS);
    }

    private void addDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public enum Direction {
    NONE(0),
    UP(1),
    DOWN(-1);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
