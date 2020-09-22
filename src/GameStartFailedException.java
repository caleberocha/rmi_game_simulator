public class GameStartFailedException extends Exception {
    private static final long serialVersionUID = 1L;

    public GameStartFailedException(int errorCode) {
        super(getErrorMessage(errorCode));
    }

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case -1:
                return "Maximum number of players have been reached";
            case -2:
                return "Address already in use";
            default:
                return String.format("%d", errorCode);
        }
    }
}
