# SimpRTPAddons 运行机制

SimpRTPAddons 是事件驱动的可选扩展。它不能单独完成随机传送，而是硬依赖 SimpRTP，并把自己的子指令注册到 `/srtp` 指令树中。

## 启动流程

1. Folia 先加载 SimpRTP。
2. SimpRTPAddons 读取自己的 `config.yml` 和语言文件。
3. 逐项检查每个扩展的 `Enabled` 开关。
4. 只为已启用的扩展注册事件、指令、菜单或 SQLite 数据库。
5. 注册 `/srtp addons`，用于查看扩展列表、帮助和版本。

执行主插件重载事件时，附属插件会卸载旧监听器并重新加载配置，避免同一监听器重复注册。

## 内置扩展

| 扩展 | 默认状态 | 工作方式 |
| --- | --- | --- |
| Logger | 开启 | 监听 SimpRTP 指令事件，将操作记录到 SQLite，并可输出到控制台。 |
| Flashback | 关闭 | 传送成功后保存旧位置，倒计时结束再将玩家送回；离线状态通过数据库恢复。 |
| Portals | 开启 | 用两个角点定义传送门区域；玩家进入区域后调用主插件的随机传送流程。 |
| ExtraEffects | 开启 | 在传送前事件中调整目标高度，并临时处理落地伤害。默认 Y 偏移为 50。 |
| MagicStick | 关闭 | 识别配置的物品，玩家右键时发起随机传送，可按配置消耗物品。 |
| Commands | 关闭 | 监听传送或取消事件，以控制台身份执行配置的指令。 |
| Parties | 开启 | 管理邀请、接受、离开、踢出和准备状态，让队伍成员协同传送。 |
| RTPMenu | 关闭 | 将世界列表显示为物品栏菜单，点击后调用相应世界的随机传送。 |

## 传送门

管理员使用 `/srtp portals loc1` 和 `/srtp portals loc2` 选择两个角，再执行创建指令。传送门区域保存在 SQLite 中，启动时读入内存缓存。玩家进入缓存区域后，附属插件以 `ADDON_PORTAL` 类型调用主插件，因此位置生成和最终传送仍由 SimpRTP 负责。

传送门预览使用 Bukkit 原生方块变更发送接口，只对查看者显示，不会修改真实世界方块。

## SQLite 数据

Logger、Flashback 和 Portals 使用附属插件 `data` 目录中的 SQLite 文件，当前表名是 `SimpRTP_Data`。该名称不兼容旧版 `BRTP_Data`，升级前应自行备份或迁移数据。

## 性能注意事项

- 不使用的扩展应将 `Enabled` 设为 `false`，这样不会注册对应监听器或数据库。
- `RTPMenu.AutoRefresh: 0` 表示不周期刷新菜单，通常性能最好。
- Flashback、Logger 和 Portals 会产生数据库读写；不需要时关闭。
- Commands 会以控制台权限执行配置内容，只能填写可信指令。
- ExtraEffects 会改变目标 Y 坐标，可能延长玩家下落时间；不需要视觉效果时关闭。
