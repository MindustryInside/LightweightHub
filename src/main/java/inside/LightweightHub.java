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

import java.io.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static mindustry.Vars.*;

public class LightweightHub extends Plugin{
    public static Config config;

    private final Interval interval = new Interval();
    private final AtomicInteger counter = new AtomicInteger();
    public final Func<Host, String> formatter = h -> config.onlinePattern.replace("%name%", h.name)
            .replace("%address%", h.address)
            .replace("%mapname%", h.mapname)
            .replace("%description%", h.description)
            .replace("%wave%", Integer.toString(h.wave))
            .replace("%players%", Integer.toString(h.players))
            .replace("%playerLimit%", Integer.toString(h.playerLimit))
            .replace("%version%", Integer.toString(h.version))
            .replace("%versionType%", h.versionType)
            .replace("%mode%", h.mode.name()) // NOTE: Gamemode#toString use localized string, but the server has no localization.
            .replace("%modeName%", h.modeName != null ? h.modeName : h.mode.name())
            .replace("%ping%", Integer.toString(h.ping))
            .replace("%port%", Integer.toString(h.port));

    public final Gson gson = new GsonBuilder()
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
                    if(config.logConnects){
                        Log.info("[@] @ --> @:@", player.uuid(), player.name, h.ip, h.port);
                    }
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
                InputStream stream = LightweightHub.class.getClassLoader().getResourceAsStream(lobby.name());
                Objects.requireNonNull(stream, "stream");
                Streams.copy(stream, lobby.write(false));
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
            // may be thrown exception if json has invalid format
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
                counter.addAndGet(Groups.player.size());
                Core.settings.put("totalPlayers", counter.get());
                counter.set(0);
            }, 3);
        }, 3, 10);
    }

    @Override
    public void registerServerCommands(CommandHandler handler){

        handler.register("reload-cfg", "Reload config.", args -> {
            try{
                config = gson.fromJson(dataDirectory.child("config-hub.json").readString(), Config.class);
                Log.info("Reloaded");
            }catch(Throwable t){
                Log.err("Failed to reload config.json.");
                Log.err(t);
            }
        });
    }
}
