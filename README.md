# 🧭 PlayerTracker 插件（适用于 Spigot/Paper 1.19.1）
🚀 **PlayerTracker** 是一款 **Minecraft 1.19.1** 服务器插件，支持管理员查询在线玩家 **坐标和状态**.

🚀 **PlayerTracker** is an esay-to-use plugin for **1.19.1 Minecraft servers**. Server ops are able to get to know **the coordinary and status** of online players.

📌 **当前版本：`v2.1.0`**  
📌 **兼容 Minecraft 版本：`1.19.1`**  
📌 **支持服务端：`Spigot` `Paper`**

---

## 📥 下载 & 安装
### 1️⃣ 下载插件
🔹 [点击下载 PlayerTracker](https://github.com/X-xiaohan521/PlayerTracker/releases/)

### 2️⃣ 安装步骤
1. **下载** `.jar` 文件。
2. 将插件文件放入服务器的 **`plugins`** 文件夹。
3. 重启服务器。
4. 在游戏内使用 `/track` 命令进行测试。

---

## 📝 使用方法
### 🔹 主要指令
| 指令            | 功能描述               | 权限       |
|-----------------|-----------------------|-----------|
| `/track`    | 查询所有在线玩家坐标及状态    | `playertracker.use` |
| `/track <player>` | 查询指定玩家坐标及状态  | `playertracker.use` |
| `/tracklog <on/off>` | 开启/关闭日志记录  | `playertracker.admin` |

### 🔹 配置文件 `config.yml`
> 📌配置文件会在插件首次启动时自动生成在 `.\plugins\PlayerTracker` 路径下
```yml
plugin:
  enabled: true   #控制插件开启/关闭

log:
  enabled: true   #控制日志记录开启/关闭
  schedule: 300   #控制日志自动记录间隔（秒）
```
### 🔹 触发自动记录的事件
> 📌以下事件发生时，将会自动记录（当日志开启时）
- 玩家进入/离开游戏
- 玩家切换飞行状态
- 玩家进入/退出载具
- 玩家受伤
- 玩家使用末影珍珠
- 玩家攻击
- 玩家打开箱子

### 🔹 可被记录的状态
- 睡觉中
- 乘坐载具
- 游泳
- 滑翔中
- 疾跑
- 潜行
- 闲逛
---

## 🔧 常见问题
### 暂无

---

## 📜 更新日志
> 📌此处只显示最新版本，更早版本详见 `CHANGELOG.md`

---

## 💡 参与贡献
### 如果你想帮助改进这个插件，请通过 GitHub 提交 Issue 或 Pull Request。
- 提交问题：[🔗 GitHub Issues](https://github.com/X-xiaohan521/PlayerTracker/issues)
- 提交功能建议: [🔗 GitHub Issues](https://github.com/X-xiaohan521/PlayerTracker/issues)
- 插件讨论：[🔗 discussions](https://github.com/X-xiaohan521/PlayerTracker/discussions/)
- 提交代码：Fork 代码仓库并发起 [🔗 Pull Request](https://github.com/X-xiaohan521/PlayerTracker/pulls)

---
## 📜 许可证
### 本插件基于 Unlicense 开源发布。
任何人都可以以及任何途径自由复制、修改、发布、使用、编译、出售或以源代码形式或编译后的形式分发此软件，并用于任何目的（商业或非商业）。

---

## 📞 联系方式
### 📧 Email: unimilk891@gmail.com
### 🌐 GitHub: [X-xiaohan521](https://github.com/X-xiaohan521/)


---

## 📌 结语
### 感谢使用 PlayerTracker！如果你觉得插件好用，请 Star ⭐ 这个 GitHub 项目，或在评论区留言反馈！🚀