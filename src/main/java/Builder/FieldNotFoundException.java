package Builder;

public class FieldNotFoundException extends Exception {
    private String s;
    public FieldNotFoundException(String s) {
        this.s=s;
    }
    public String getMessage() {
        return "Field not found : " + s;
    }
}