import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SharedPhilosopherStatesSemaphores implements SharedPhilosophersStates {

    private Semaphore mutex;
    private PhilosopherState[] states;
    private Semaphore[] sleepers;

    public SharedPhilosopherStatesSemaphores(int numberOfPhilosophers) {
        this.mutex = new Semaphore(1);
        initiateStates(numberOfPhilosophers);
        initiateSleepers(numberOfPhilosophers);
    }

    private void initiateStates(int numberOfPhilosophers) {
        this.states = new PhilosopherState[numberOfPhilosophers];
        Arrays.fill(this.states, PhilosopherState.THINKING);
    }

    private void initiateSleepers(int numberOfPhilosophers) {
        this.sleepers = new Semaphore[numberOfPhilosophers];
        Arrays.fill(this.sleepers, new Semaphore(0));
    }

    public void transitionToHungry(int position) {
        try {
            // Verifies if was thinking
            if (isThinking(getStateAtPos(position))) {
                mutex.acquire();
                this.states[position] = PhilosopherState.HUNGRY;

                // If he was hungry and fulfill the requirements, can transition to Eating
                if (!isEating(getStateAtPos(getRightPos(position))) &&
                        !isEating(getStateAtPos(getRightPos(getRightPos(position))))) {
                    this.sleepers[position].release();
                    this.states[position] = PhilosopherState.EATING;
                }
                mutex.release();
                this.sleepers[position].acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void transitionToThinking(int position) {
        try {
            // Verifies if was thinking
            if (isEating(this.states[position])) {
                mutex.acquire();
                this.states[position] = PhilosopherState.THINKING;

                if (isHungry(getStateAtPos(position)) &&
                        !isEating(getStateAtPos(getRightPos(getRightPos(position))))) {
                    this.sleepers[getRightPos(position)].release();
                    states[getRightPos(position)] = PhilosopherState.EATING;
                }
                if (isHungry(getStateAtPos(getLeftPos(position))) &&
                        !isEating(getStateAtPos(getLeftPos(getLeftPos(position))))) {
                    this.sleepers[getLeftPos(position)].release();
                    states[getLeftPos(position)] = PhilosopherState.EATING;
                }
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
