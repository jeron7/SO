import java.util.Arrays;

public class SharedPhilosophersStatesWorker extends SharedPhilosophersStates {

    public SharedPhilosophersStatesWorker(int numberOfPhilosophers) {
        initiateStates(numberOfPhilosophers);
    }

    synchronized public void transitionToState(int position, PhilosopherState state) {
        getStates()[position] = state;
    }
}
