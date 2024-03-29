# Configuring

After loading the server with the plugin, the `config-hub.json` file should be created in directory `config`. <br>
The plugin configuration file uses the JSON format, see [json.org](https://www.json.org/json-en.html) site

### Available Fields

| Field           | Type                                      | Description                                                                                             |
| --------------- | ----------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| offline-pattern | string                                    | pattern used when servers are offline                                                                   |
| online-pattern  | string                                    | placeholder based ([placeholders](#online-pattern-placeholders)) pattern, used when servers are online  |
| log-connects    | boolean                                   | log player connections to hub's servers in `[player uuid] player name --> server ip:server port` format |
| servers         | array of [server](#server-object) objects | hub's servers                                                                                           |
| effects         | array of [effect](#effect-object) objects | static effects that appear with a specified period                                                      |
| event-effects   | [event effects](#event-effect-object)     | effects called on a specific event                                                                      |

#### Online Pattern Placeholders

| Name          | Description                                             |
| ------------- | ------------------------------------------------------- |
| %address%     | server ip                                               |
| %mapname%     | current server map name                                 |
| %description% | server description                                      |
| %wave%        | current server map wave                                 |
| %players%     | server player count                                     |
| %playerLimit% | server player limit                                     |
| %version%     | server game version                                     |
| %versionType% | server game version type                                |
| %mode%        | server mode i.e. survival, sandbox, attack, pvp, editor |
| %modeName%    | server custom mode name, if absent, %mode% is used      |
| %ping%        | server ping                                             |
| %port%        | server port                                             |

### Server Object

###### Server Object structure

| Field      | Type    | Description                                                        |
| ---------- | ------- | ------------------------------------------------------------------ |
| ip         | string  | server ip                                                          |
| port       | integer | server port                                                        |
| size       | integer | the teleport block size                                            |
| teleport-x | integer | latter x coordinate of the teleport border. minimap coordinate     |
| teleport-y | integer | latter y coordinate of the teleport border. minimap coordinate     |
| title      | string  | server display name                                                |
| title-x    | float   | latter x coordinate of the title signboard. minimap coordinate * 8 |
| title-y    | float   | latter y coordinate of the title signboard. minimap coordinate * 8 |
| label-x    | float   | latter x coordinate of the status label. minimap coordinate * 8    |
| label-y    | float   | latter y coordinate of the status label. minimap coordinate * 8    |

###### Optimal Setup Formula

```
title-x = (teleport-x * 8) + 12
title-y = (teleport-y * 8) + 36

label-x = (teleport-x * 8) + 12
label-y = (teleport-y * 8) - 16
```

###### Example Server Object

```json
{
  "ip": "localhost",
  "port": 6567,
  "size": 7,
  "teleport-x": 22,
  "teleport-y": 37,
  "title": "title",
  "title-x": 200,
  "title-y": 320,
  "label-x": 200,
  "label-y": 288
}
```

### Effect Object

###### Effect Object structure

| Field         | Type    | Description                                                                                                                                      |
| ------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| x             | float   | effect x coordinate. minimap coordinate * 8                                                                                                      |
| y             | float   | effect y coordinate. minimap coordinate * 8                                                                                                      |
| rotation      | float   | effect rotation                                                                                                                                  |
| period-millis | integer | interval in milliseconds after which the effect will spawn                                                                                       |
| color         | string  | effect color in hex format                                                                                                                       |
| effect        | string  | effect name. the name of one of the fields of the [Fx](https://github.com/Anuken/Mindustry/blob/master/core/src/mindustry/content/Fx.java) class |

###### Example Effect Object

```json
{
  "x": 240.0,
  "y": 240.0,
  "rotation": 30.0,
  "period-millis": 2000,
  "color": "#ff0000ff",
  "effect": "explosion"
}
```

### Event Effect Object

###### Event Effect Object structure

> Event effects are such static effects, only without fields: <br>
> `x`, `y`, `period-millis`

| Name  | Description                             |
| ----- | --------------------------------------- |
| move  | effect appearing when the player moves  |
| join  | effect appearing when the player joins  |
| leave | effect appearing when the player leaves |

###### Example Event Effect Object

```json
{
  "rotation": 30.0,
  "color": "#4169e1ff",
  "effect": "fire"
}
```
