public class PhilosophersDinnerWithWorkers {

    static long THINK_TIME = 100;
    static long EAT_TIME = 100;

    public static void main(String[] args) {
        int numberOfPhilosophers = 6;

        SharedPhilosophersStates states = new SharedPhilosophersStatesWorker(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            new OtherPhilosopher(states, i, EAT_TIME, THINK_TIME);
        }
    }
}
