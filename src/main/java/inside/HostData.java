package inside;

public class HostData{
    /** Server IP address. */
    public String ip = "localhost";
    /** Server port. */
    public int port;
    /** The teleport block size. */
    public int size;

    /** The last <b>X</b> coordinate of the teleport border. */
    public int teleportX;
    /** The last <b>Y</b> coordinate of the teleport border. */
    public int teleportY;

    /** The teleport title i.e. server display name. */
    public String title = "title";
    /** The last <b>X</b> coordinate of the title signboard. */
    public int titleX;
    /** The last <b>Y</b> coordinate of the title signboard. */
    public int titleY;

    /** The last <b>X</b> coordinate of the status label. */
    public int labelX;
    /** The last <b>Y</b> coordinate of the status label. */
    public int labelY;

    public HostData(int port, int size, int teleportX, int teleportY, int titleX, int titleY, int labelX, int labelY){
        this.port = port;
        this.size = size;
        this.teleportX = teleportX;
        this.teleportY = teleportY;
        this.titleX = titleX;
        this.titleY = titleY;
        this.labelX = labelX;
        this.labelY = labelY;
    }

    public boolean inDiapason(int x, int y){
        return x <= teleportX + size && x >= teleportX &&
               y >= teleportY && y <= teleportY + size;
    }
}
