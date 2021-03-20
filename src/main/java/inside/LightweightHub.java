package inside;

import arc.*;
import arc.files.Fi;
import arc.func.Func;
import arc.util.*;
import arc.util.io.Streams;
import com.google.gson.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.net.*;
import mindustry.world.Tile;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static mindustry.Vars.*;

public class LightweightHub extends Plugin{
    public static Config config;

    private final Interval interval = new Interval();
    private final AtomicInteger counter = new AtomicInteger();
    private final Func<Host, String> formatter = h -> config.onlinePattern.replace("%online%", Integer.toString(h.players)).replace("%map%", h.mapname);

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public void teleport(Player player){
        teleport(player, null);
    }

    public void teleport(final Player player, Tile tile){
        for(HostData h : config.servers){
            if(h.inDiapason(tile != null ? tile.x : player.tileX(), tile != null ? tile.y : player.tileY())){
                net.pingHost(h.ip, h.port, host -> {
                    // I'm not sure I need it yet
                    Log.debug("[@] @ --> @", player.uuid(), player.name, h.ip + ":" + h.port);
                    Call.connect(player.con, h.ip, h.port);
                }, e -> {});
            }
        }
    }

    @Override
    public void init(){

        Fi lobby = customMapDirectory.child("lobby.msav");
        if(!lobby.exists()){
            try{
                Streams.copy(Objects.requireNonNull(LightweightHub.class.getClassLoader().getResourceAsStream(lobby.name())), lobby.write(false));
            }catch(IOException e){
                Log.err("Failed to copy hub map. Skipping.");
                Log.err(e);
            }
        }

        Fi cfg = dataDirectory.child("config-hub.json");
        if(!cfg.exists()){
            cfg.writeString(gson.toJson(config = new Config()));
            Log.info("Config created...");
        }else{
            config = gson.fromJson(cfg.reader(), Config.class);
        }

        Events.on(ServerLoadEvent.class, event -> netServer.admins.addActionFilter(playerAction -> false));

        Events.on(TapEvent.class, event -> teleport(event.player, event.tile));

        Events.run(Trigger.update, () -> {
            if(interval.get(60 * 1.5f)){
                Groups.player.each(this::teleport);
            }
        });

        Events.on(PlayerJoin.class, event -> {
            NetConnection con = event.player.con();

            for(HostData h : config.servers){
                Call.label(con, h.title, 1100f, h.titleX, h.titleY);
                net.pingHost(h.ip, h.port, host -> {
                    Call.label(con, formatter.get(host), 10, h.labelX, h.labelY);
                }, e -> Call.label(con, config.offlinePattern, 10, h.labelX, h.labelY));
            }
        });

        Timer.schedule(() -> {
            for(HostData h : config.servers){
                net.pingHost(h.ip, h.port, host -> {
                    counter.addAndGet(host.players);
                    Call.label(formatter.get(host), 10, h.labelX, h.labelY);
                }, e -> Call.label(config.offlinePattern, 10, h.labelX, h.labelY));
            }

            Timer.schedule(() -> {
                Core.settings.put("totalPlayers", counter.get());
                counter.set(0);
            }, 3);
        }, 3, 10);
    }

    @Override
    public void registerServerCommands(CommandHandler handler){

        handler.register("reload-cfg", "Reload config.", args -> {
            config = gson.fromJson(dataDirectory.child("config-hub.json").readString(), Config.class);
            Log.info("Reloaded");
        });
    }
}
