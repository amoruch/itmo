
import java.time.Instant;
import java.util.Date;

public class App {

    public static void main(String[] args) throws Exception {
        Instant moment = Instant.now();
        Date now = Date.from(moment);
        System.out.println(now);
    }
}
