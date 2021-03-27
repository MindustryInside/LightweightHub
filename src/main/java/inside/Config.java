package inside;

import mindustry.net.Host;

import java.util.*;

public class Config{

    /** Offline pattern, used when servers is offline. */
    public String offlinePattern = "\u26A0 [scarlet]Offline";

    /**
     * Online count pattern, used in servers status.
     * This is placeholder based patter, {@link LightweightHub#formatter}.
     * <p>
     * Syntax: <b>%fieldName%</b> - where fieldName is one of
     * a fields is {@link Host} class.
     */
    public String onlinePattern = "\uE837 [accent]Online: %players%\n\uE827 Map: %mapname%";

    /** If {@code true}, player connection logging will be enabled. */
    public boolean logConnects = false;

    /** List with {@link HostData}. */
    public List<HostData> servers = Arrays.asList(
            new HostData(1000, 7, 22, 37, 200, 320, 200, 288),
            new HostData(2000, 7, 7, 22, 80, 200, 80, 168),
            new HostData(3000, 7, 22, 7, 200, 80, 200, 48),
            new HostData(4000, 7, 37, 23, 320, 200, 320, 168)
    );
}
