package inside;

import java.util.*;

public class Config{

    /** Offline pattern, used when servers is offline */
    public String offlinePattern = "\u26A0 [scarlet]Offline";

    /**
     * Online count pattern, used in servers status.
     * @see arc.util.Strings#format(String, Object...)
     */
    public String onlinePattern = "\uE837 [accent]Online @";

    /** List with {@link HostData}. */
    public List<HostData> servers = Arrays.asList(
            new HostData(1000, 7, 22, 37, 200, 320, 200, 288),
            new HostData(2000, 7, 7, 22, 80, 200, 80, 168),
            new HostData(3000, 7, 22, 7, 200, 80, 200, 48),
            new HostData(4000, 7, 37, 23, 320, 200, 320, 168)
    );
}
