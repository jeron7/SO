public class Philosopher implements Runnable {

    private SharedPhilosophersStates states;
    private int position;
    private long eatTime;
    private long thinkTime;
    private int dinnersEaten;

    public Philosopher(SharedPhilosophersStates states, int positionAtTable, long eatTime, long thinkTime) {
        this.states = states;
        this.position = positionAtTable;
        this.eatTime = eatTime;
        this.thinkTime = thinkTime;

        new Thread((Runnable) this, "Philosopher - " + positionAtTable).start();
    }

    @Override
    public void run() {
        while (true) {
            this.think();
            this.getCutlery();
            this.eat();
            this.dropCutlery();
        }
    }

    private void getCutlery() {
        states.transitionToState(position, PhilosopherState.HUNGRY);
        System.out.println(String.format("The philosopher %s are waiting to eat", this.position));
    }

    private void dropCutlery() {
        states.transitionToState(position, PhilosopherState.THINKING);
    }

    private void eat() {
        try {
            System.out.println(String.format("The philosopher %s was starting eating.", this.position, this.dinnersEaten));
            Thread.sleep(this.eatTime);
            dinnersEaten++;
            System.out.println(String.format("The philosopher %s stopped eating. Has eaten %o times.", this.position, this.dinnersEaten));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void think() {
        try {
            System.out.println(String.format("The philosopher %s started thinking", this.position));
            Thread.sleep(this.thinkTime);
            System.out.println(String.format("The philosopher %s stopped thinking and was hungry now.", this.position));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
