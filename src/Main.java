import logic.Users;
import sql.DatabaseHandler;

public class Main {
    public static final int PORT_WORK = 2525;

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer(PORT_WORK);
        new Thread(server).start();
    }
}
