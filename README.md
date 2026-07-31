<p align="center">
  <b><a>Welcome to SimpRTP's repository!</a></b>
</p>

## Where's the Lang files?/Want to Contribute translating?  
All language files are located [here](src/main/resources/lang)
feel free to fork one of the language files and help translate!

## 玩家自选语言

SimpRTP 默认使用简体中文。玩家可使用以下指令单独选择消息语言，选择结果会持久保存：

- `/srtp language`：查看当前语言及可用语言
- `/srtp language <语言代码>`：选择语言，例如 `chs`、`cht`、`en`
- `/srtp language auto`：跟随 Minecraft 客户端语言
- `/srtp language default`：恢复服务器默认语言

所需权限为 `simprtp.language`，默认向所有玩家开放。管理员仍可通过 `config.yml` 中的 `Language-File` 设置服务器默认语言。

## Libraries
SimpRTP uses and is compiled with the following libraries:

- [Folia API](https://github.com/PaperMC/Folia) (provided) - Native scheduler, asynchronous teleport and chunk-loading APIs.

SimpRTP no longer bundles cross-version scheduler, particle, or packet compatibility libraries.

Builds targeting Folia 26.1.2+ require Java 25 or newer.

The main plugin and SimpRTPAddons target the Folia 26.1.2 build 8 API and declare `api-version: '26.1'`. CI also boots the shaded SimpRTP jar on that exact stable Folia build and runs plugin version and configuration smoke checks.

## Build instructions on Ubuntu

mvn clean install

The file will be in the Target file.

## Where's the Wiki?  
The wiki is available [here](../../wiki)!
    
<p align="center">
  <b>Chat with us on Discord</b><br/>
  <a href="https://discord.gg/8Kt4wKm"><img src="https://img.shields.io/discord/182633513474850818.svg?longCache=true&style=flat-square&label=Discord" alt="Discord" /></a><br/>
  <b>Have a Suggestion? Make an issue!</b><br/>
  <a href="../../issues"><img src="https://img.shields.io/github/issues-raw/Minecraft0122/SimpRTP.svg?longCache=true&style=flat-square&label=Issues" alt="GitHub issues" /></a><br/>
  <br/>
  <a href="https://www.spigotmc.org/resources/36081/">Thank you for viewing the Wiki for SimpRTP!</a><br/>
  <i><a>Did this wiki help you out? Please give it a <b>Star</b> so I know it's getting use!</a></i><br/>
  <br/>
  <b><i><a href="https://www.spigotmc.org/resources/authors/superronancraft.13025/">Check out my other plugins!</a></i></b>
</p>
