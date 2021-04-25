# Configuring

After loading the server with the plugin, the `config-hub.json` file should be created in directory `config`. <br>
The plugin configuration file uses the JSON format, see [json.org](https://www.json.org/json-en.html) site

### Available Fields

| Field           | Description                                                                                             |
| --------------- | ------------------------------------------------------------------------------------------------------- |
| offline-pattern | pattern used when servers are offline                                                                   |
| online-pattern  | placeholder based ([placeholders](#online-pattern-placeholders)) pattern, used when servers are online  |
| log-connects    | log player connections to hub's servers in `[player uuid] player name --> server ip:server port` format |
| servers         | hub's servers. ([servers](#servers))                                                                    |
| effects         | static effects that appear with a specified period. ([effects](#effects))                               |
| event-effects   | effects called on a specific event. ([event effects](#event-effects))                                   |

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
| %ping%        | server pint                                             |
| %port%        | server port                                             |

#### Servers

| Field      | Description                                                        |
| ---------- | ------------------------------------------------------------------ |
| ip         | server ip                                                          |
| port       | server port                                                        |
| size       | the teleport block size                                            |
| teleport-x | latter x coordinate of the teleport border. minimap coordinate     |
| teleport-y | latter y coordinate of the teleport border. minimap coordinate     |
| title      | server display name                                                |
| title-x    | latter x coordinate of the title signboard. minimap coordinate * 8 |
| title-y    | latter y coordinate of the title signboard. minimap coordinate * 8 |
| label-x    | latter x coordinate of the status label. minimap coordinate * 8    |
| label-y    | latter y coordinate of the status label. minimap coordinate * 8    |

#### Effects

| Field         | Description                                                                                                                                      |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| x             | effect x coordinate. minimap coordinate * 8                                                                                                      |
| x             | effect y coordinate. minimap coordinate * 8                                                                                                      |
| rotation      | effect rotation                                                                                                                                  |
| period-millis | interval in milliseconds after which the effect will spawn                                                                                       |
| color         | effect color in hex format                                                                                                                       |
| effect        | effect name. the name of one of the fields of the [Fx](https://github.com/Anuken/Mindustry/blob/master/core/src/mindustry/content/Fx.java) class |

#### Event Effects

> Event effects are such static effects, only without fields: <br>
> `x`, `y`, `period-millis`

Available names:

| Name  | Description                             |
| ----- | --------------------------------------- |
| move  | effect appearing when the player moves  |
| join  | effect appearing when the player joins  |
| leave | effect appearing when the player leaves |
