package ch.unibe.inf.seg.mergeresolution.analyze;

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