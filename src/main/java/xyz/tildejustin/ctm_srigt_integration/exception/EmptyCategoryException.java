package xyz.tildejustin.ctm_srigt_integration.exception;

public class EmptyCategoryException extends Exception {
    public EmptyCategoryException() {
        super();
    }

    public EmptyCategoryException(String s) {
        super(s);
    }
}
