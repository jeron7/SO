import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SharedPhilosopherStatesSemaphores extends SharedPhilosophersStates {

    private Semaphore mutex;
    private Semaphore[] sleepers;

    public SharedPhilosopherStatesSemaphores(int numberOfPhilosophers) {
        this.mutex = new Semaphore(1);
        initiateStates(numberOfPhilosophers);
        initiateSleepers(numberOfPhilosophers);
    }

    private void initiateSleepers(int numberOfPhilosophers) {
        this.sleepers = new Semaphore[numberOfPhilosophers];
        Arrays.fill(this.sleepers, new Semaphore(0));
    }

    @Override
    public void transitionToState(int position, PhilosopherState state) {
        switch(state) {
            case HUNGRY:
                transitionToHungry(position);
                break;
            case THINKING:
                transitionToThinking(position);
            break;
        }
    }

    private void transitionToHungry(int position) {
        try {
            // Verifies if was thinking
            if (getStateAtPos(position) == PhilosopherState.THINKING) {
                mutex.acquire();
                getStates()[position] = PhilosopherState.HUNGRY;

                // If he was hungry and fulfill the requirements, can transition to Eating
                if (canEat(position)) {
                    this.sleepers[position].release();
                    getStates()[position] = PhilosopherState.EATING;
                }
                mutex.release();
                this.sleepers[position].acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean canEat(int position) {
        PhilosopherState theOneOnMyRight = getStateAtPos(getRightPos(position));
        PhilosopherState theOneOnMyLeft = getStateAtPos(getLeftPos(position));
        return !isEating(theOneOnMyRight) && !isEating(theOneOnMyLeft);
    }

    private boolean isEating(PhilosopherState state) {
        return state == PhilosopherState.EATING;
    }

    private boolean isHungry(PhilosopherState state) {
        return state == PhilosopherState.HUNGRY;
    }

    private void transitionToThinking(int position) {
        try {
            // Verifies if was thinking
            if (isEating(getStates()[position])) {
                mutex.acquire();
                getStates()[position] = PhilosopherState.THINKING;

                PhilosopherState theOneOnMyRight = getStateAtPos(getRightPos(position));
                PhilosopherState theOneOnTheRightOfMyRight = getStateAtPos(getRightPos(getRightPos(position)));
                if (isHungry(theOneOnMyRight) &&
                        !isEating(theOneOnTheRightOfMyRight)) {
                    this.sleepers[getRightPos(position)].release();
                    getStates()[getRightPos(position)] = PhilosopherState.EATING;
                }

                PhilosopherState theOneOnMyLeft = getStateAtPos(getLeftPos(position));
                PhilosopherState theOneOnTheLeftOfMyLeft = getStateAtPos(getLeftPos(getLeftPos(position)));
                if (isHungry(theOneOnMyLeft) &&
                        !isEating(theOneOnTheLeftOfMyLeft)) {
                    this.sleepers[getLeftPos(position)].release();
                    getStates()[getLeftPos(position)] = PhilosopherState.EATING;
                }
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
