import java.util.Arrays;

public class SharedPhilosophersStatesWorker implements SharedPhilosophersStates {

    private PhilosopherState[] states;

    public SharedPhilosophersStatesWorker(int numberOfPhilosophers) {
        initiateStates(numberOfPhilosophers);
    }

    private void initiateStates(int numberOfPhilosophers) {
        this.states = new PhilosopherState[numberOfPhilosophers];
        Arrays.fill(this.states, PhilosopherState.THINKING);
    }

    synchronized public void transitionToHungry(int position) {
        // Verifies if was thinking
        if (isThinking(getStateAtPos(position))) {
            this.states[position] = PhilosopherState.HUNGRY;

            // If he was hungry and fulfill the requirements, can transition to Eating
            if (!isEating(getStateAtPos(getRightPos(position))) &&
                    !isEating(getStateAtPos(getRightPos(getRightPos(position))))) {
                this.states[position] = PhilosopherState.EATING;
            }
        }
    }

    synchronized public void transitionToThinking(int position) {
        // Verifies if was thinking
        if (isEating(this.states[position])) {
            this.states[position] = PhilosopherState.THINKING;

            if (isHungry(getStateAtPos(position)) &&
                    !isEating(getStateAtPos(getRightPos(getRightPos(position))))) {
                states[getRightPos(position)] = PhilosopherState.EATING;
            }
            if (isHungry(getStateAtPos(getLeftPos(position))) &&
                    !isEating(getStateAtPos(getLeftPos(getLeftPos(position))))) {
                states[getLeftPos(position)] = PhilosopherState.EATING;
            }
        }
    }

    private boolean isHungry(PhilosopherState state) {
        return state == PhilosopherState.HUNGRY;
    }

    private boolean isEating(PhilosopherState state) {
        return state == PhilosopherState.EATING;
    }

    private boolean isThinking(PhilosopherState state) {
        return state == PhilosopherState.THINKING;
    }

    private PhilosopherState getStateAtPos(int position) {
        return this.states[position];
    }

    private int getLeftPos(int position) {
        return Math.floorMod(position - 1, this.states.length);
    }

    private int getRightPos(int position) {
        return Math.floorMod(position + 1, this.states.length);
    }
}
