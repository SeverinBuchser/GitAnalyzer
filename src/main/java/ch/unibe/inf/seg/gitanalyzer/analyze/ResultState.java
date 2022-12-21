package ch.unibe.inf.seg.gitanalyzer.analyze;

/**
 * Enumerator for the result state.
 */
public enum ResultState {
    OK("OK"),
    SKIP("SKIP"),
    FAIL("FAIL");

    private final String stateString;

    ResultState(String stateString) {
        this.stateString = stateString;
    }

    @Override
    public String toString() {
        return this.stateString;
    }
}