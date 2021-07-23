import java.util.Arrays;

public abstract class SharedPhilosophersStates {

    private PhilosopherState[] states;

    public abstract void transitionToState(int position, PhilosopherState state);

    protected void initiateStates(int numberOfPhilosophers) {
        this.states = new PhilosopherState[numberOfPhilosophers];
        Arrays.fill(this.states, PhilosopherState.THINKING);
    }

    protected PhilosopherState[] getStates() {
        return states;
    }

    public PhilosopherState getStateAtPos(int position) {
        return this.states[position];
    }

    public int getLeftPos(int position) {
        return Math.floorMod(position - 1, this.states.length);
    }

    public int getRightPos(int position) {
        return Math.floorMod(position + 1, this.states.length);
    }
}
