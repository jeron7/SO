public class OtherPhilosopher implements Runnable {

    private SharedPhilosophersStates states;
    private int position;
    private long eatTime;
    private long thinkTime;
    private int dinnersEaten;

    public OtherPhilosopher(SharedPhilosophersStates states, int positionAtTable, long eatTime, long thinkTime) {
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

        PhilosopherState state = states.getStateAtPos(position);
        synchronized (state) {
            PhilosopherState theOneOnMyRight = states.getStateAtPos(states.getRightPos(position));
            PhilosopherState theOneOnMyLeft = states.getStateAtPos(states.getLeftPos(position));
            while (theOneOnMyRight == PhilosopherState.EATING ||
                    theOneOnMyLeft == PhilosopherState.EATING) {
                try {
                    state.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        states.transitionToState(position, PhilosopherState.EATING);
        System.out.println(String.format("The philosopher %s are waiting to eat", this.position));
    }

    private void dropCutlery() {
        states.transitionToState(position, PhilosopherState.THINKING);

        PhilosopherState theOneOnMyRight = states.getStateAtPos(states.getRightPos(position));
        synchronized (theOneOnMyRight) {
            PhilosopherState theOneOnTheRightOfMyRight = states.getStateAtPos(states.getRightPos(states.getRightPos(position)));
            if (theOneOnMyRight == PhilosopherState.HUNGRY && theOneOnTheRightOfMyRight != PhilosopherState.EATING) {
                theOneOnMyRight.notify();
            }
        }

        PhilosopherState theOneOnMyLeft = states.getStateAtPos(states.getLeftPos(position));
        synchronized (theOneOnMyLeft) {
            PhilosopherState theOneOnTheLeftOfMyLeft = states.getStateAtPos(states.getLeftPos(states.getLeftPos(position)));
            if (theOneOnMyLeft == PhilosopherState.HUNGRY && theOneOnTheLeftOfMyLeft != PhilosopherState.EATING) {
                theOneOnMyLeft.notify();
            }
        }
    }

    private void eat() {
        try {
            System.out.println(String.format("The philosopher %s was starting eating.", this.position, this.dinnersEaten));
            Thread.sleep(this.eatTime);
            this.dinnersEaten++;
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
