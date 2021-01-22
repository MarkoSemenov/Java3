import java.io.Serializable;

public class ErrorCommandDate implements Serializable {

    private final String errorMsg;

    public ErrorCommandDate(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
