package inside;

import arc.graphics.Color;
import arc.util.Reflect;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Call;

public class EffectData{
    /** Effect <b>X</b> coordinate. */
    public float x;
    /** Effect <b>Y</b> coordinate. */
    public float y;
    /** Effect <b>rotation</b>. */
    public float rotation;
    /** Respawn period delay. Used with static effects, look {@link Config#effects}. */
    public long periodMillis;
    /** Effect color. In hex format. */
    private final String color;
    /** Name of one of {@link Fx} class fields. */
    private final String effect;

    public EffectData(float x, float y, float rotation, long periodMillis, String color, String effect){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.periodMillis = periodMillis;
        this.color = color;
        this.effect = effect;
    }

    public Color getColor(){
        return Color.valueOf(color);
    }

    public Effect getEffect(){
        return Reflect.get(Fx.class, effect);
    }

    public void spawn(){
        Call.effect(getEffect(), x, y, rotation, getColor());
    }

    public void spawn(float x, float y){
        Call.effect(getEffect(), x, y, rotation, getColor());
    }
}
