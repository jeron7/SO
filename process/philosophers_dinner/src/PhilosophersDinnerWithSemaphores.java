public class PhilosophersDinnerWithSemaphores {

    static long THINK_TIME = 100;
    static long EAT_TIME = 100;

    public static void main(String[] args) {
        int numberOfPhilosophers = 6;

        SharedPhilosophersStates states =  new SharedPhilosopherStatesSemaphores(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            new Philosopher(states, i, EAT_TIME, THINK_TIME);
        }
    }
}
