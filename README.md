# 学生快递代取系统

一个基于微信小程序的校园快递代取系统，帮助高校学生解决取件难问题，同时为有空闲时间的学生提供勤工俭学机会。

---

## 项目简介

随着电子商务的快速发展，高校学生网购数量激增，取件难成为普遍问题。本系统通过微信小程序平台，实现快递代取需求的线上对接，为学生提供便捷的代取服务。

### 核心功能

- **用户端**：注册登录、快递下单、订单管理、个人中心、充值等
- **骑手端**：骑手认证、接单管理、订单配送、收益管理等
- **管理端**：用户管理、骑手管理、订单管理、基础数据管理、通知公告等

---

## 技术栈

### 前端（微信小程序）

| 技术 | 版本/说明 |
|------|----------|
| 微信小程序 | 原生开发 |
| 开发工具 | 微信开发者工具 |
| 页面技术 | WXML、WXSS、JavaScript |

### 后端（Spring Boot）

| 技术 | 版本 |
|------|------|
| Spring Boot | 3.5.9 |
| MyBatis | 3.0.3 |
| MySQL | 8.2.0 |
| Druid | 1.2.23 |
| PageHelper | 2.1.0 |
| JWT | 0.11.5 |
| JDK | 17 |

### 管理后台（Vue）

| 技术 | 说明 |
|------|------|
| Vue.js | 前端框架 |
| Element UI | UI组件库 |
| Axios | HTTP客户端 |

---

## 项目结构

```
fast/
├── pages/                    # 微信小程序页面
│   ├── index/               # 首页
│   ├── login/               # 登录页
│   ├── register/            # 注册页
│   ├── create-order/        # 创建订单
│   ├── order/               # 订单列表
│   ├── my/                  # 个人中心
│   ├── rider-home/          # 骑手首页
│   ├── admin-home/          # 管理后台首页
│   ├── admin-user/          # 用户管理
│   ├── admin-rider/         # 骑手管理
│   ├── admin-order/         # 订单管理
│   ├── admin-size/          # 包裹规格管理
│   ├── admin-station/       # 快递站点管理
│   ├── admin-building/      # 宿舍楼管理
│   ├── admin-notice/        # 通知管理
│   ├── forgot-password/     # 忘记密码
│   ├── price-rule/          # 计费规则
│   └── order-guide/         # 下单须知
├── utils/                    # 工具类
├── springboot/               # Spring Boot后端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/fast/
│   │       │   ├── system/    # 系统模块（用户、权限等）
│   │       │   └── take/      # 快递代取业务模块
│   │       └── resources/
│   │           ├── mapper/     # MyBatis映射文件
│   │           └── application.yml
│   └── pom.xml
├── vue/                      # Vue管理后台
├── fast.sql                  # 数据库初始化脚本
├── app.json                  # 小程序配置
└── project.config.json       # 小程序项目配置
```

---

## 快速开始

### 环境要求

- **JDK**: 17+
- **Maven**: 3.x
- **MySQL**: 8.x
- **微信开发者工具**: 最新版本

### 1. 数据库配置

1. 创建数据库：

```sql
CREATE DATABASE `package-take` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 导入数据脚本：

```bash
mysql -u root -p package-take < fast.sql
```

### 2. 后端配置

1. 修改 `springboot/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/package-take?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: 你的密码

server:
  port: 8080
```

2. 启动后端服务：

```bash
cd springboot
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

### 3. 小程序配置

1. 修改 `utils/request.js` 中的后端地址：

```javascript
const baseUrl = 'http://localhost:8080';  // 本地开发
// 或使用内网穿透地址
```

2. 使用微信开发者工具打开项目目录

3. 配置小程序 AppID（在 `project.config.json` 中）

### 4. 内网穿透（可选）

如果需要在真机上测试，可以使用内网穿透工具：

**使用 cpolar：**

```bash
# 下载并安装 cpolar
# 注册账号并获取token
cpolar http 8080
```

将生成的公网地址配置到 `utils/request.js` 中。

---

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | user1 | 123456 |
| 骑手 | rider1 | 123456 |

---

## 功能模块

### 普通用户功能

| 功能 | 说明 |
|------|------|
| 注册登录 | 用户注册、登录、忘记密码 |
| 快递下单 | 选择快递站点、宿舍楼、包裹规格下单 |
| 订单管理 | 查看订单列表、订单详情、取消订单 |
| 个人中心 | 查看个人信息、修改信息、账户充值 |
| 查看通知 | 查看平台通知公告 |
| 申请骑手 | 提交骑手认证申请 |

### 骑手功能

| 功能 | 说明 |
|------|------|
| 接单管理 | 查看待接单列表、接单 |
| 订单配送 | 查看配送中订单、确认送达 |
| 收益管理 | 查看收益明细、余额 |
| 个人信息 | 查看和修改个人信息 |

### 管理员功能

| 功能 | 说明 |
|------|------|
| 用户管理 | 查看用户列表、管理用户余额 |
| 骑手管理 | 骑手认证审核、禁用/启用骑手 |
| 订单管理 | 查看所有订单、管理订单状态 |
| 基础数据 | 管理快递站点、宿舍楼、包裹规格 |
| 通知管理 | 发布、编辑、删除通知公告 |
| 数据统计 | 订单统计、用户统计、收益统计 |

---

## 数据库表说明

| 表名 | 说明 |
|------|------|
| sys_user | 用户表 |
| `order` | 订单表 |
| rider | 骑手表 |
| station | 快递站点表 |
| building | 宿舍楼表 |
| size | 包裹规格表 |
| notice | 通知表 |
| sys_role | 角色表 |
| sys_menu | 菜单表 |

---

## 接口文档

### 用户相关接口

| 方法 | 接口 | 说明 |
|------|------|------|
| POST | /login | 用户登录 |
| POST | /system/user/register | 用户注册 |
| POST | /system/user/forgotPwd | 忘记密码 |
| GET | /getInfo | 获取当前用户信息 |

### 订单相关接口

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | /take/order/list | 查询订单列表 |
| GET | /take/order/selectMyOrderList | 查询我的订单 |
| POST | /take/order | 创建订单 |
| PUT | /take/order/cancelOrder/{orderId} | 取消订单 |
| PUT | /take/order/accept/{orderId} | 接单 |
| PUT | /take/order/receive/{orderId} | 确认送达 |

### 通知相关接口

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | /take/notice/list | 查询通知列表 |
| GET | /take/notice/{id} | 查询通知详情 |

### 基础数据接口

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | /take/station/list | 查询快递站点列表 |
| GET | /take/building/list | 查询宿舍楼列表 |
| GET | /take/size/list | 查询包裹规格列表 |

---

## 系统截图

> 可在此处添加系统运行截图

---

## 部署说明

### 后端部署

1. 打包项目：

```bash
cd springboot
mvn clean package
```

2. 将生成的 jar 包上传到服务器

3. 运行：

```bash
java -jar springboot.jar
```

或使用 nohup 后台运行：

```bash
nohup java -jar springboot.jar > app.log 2>&1 &
```

### 小程序部署

1. 在微信开发者工具中点击"上传"
2. 登录微信公众平台提交审核
3. 审核通过后发布上线

---

## 常见问题

### 1. 数据库连接失败

检查 `application.yml` 中的数据库地址、用户名、密码是否正确。

### 2. 小程序无法请求后端

- 检查后端服务是否启动
- 检查 `utils/request.js` 中的 baseUrl 是否正确
- 如果是真机调试，需要使用内网穿透地址

### 3. 跨域问题

后端已配置 CORS，如仍有问题，检查 `SecurityConfig.java` 配置。

---

## 后续优化方向

- [ ] 增加在线支付功能（微信支付）
- [ ] 增加评价系统
- [ ] 增加消息推送（订阅消息）
- [ ] 增加数据可视化图表
- [ ] 优化性能，引入 Redis 缓存
- [ ] 增加物流追踪功能
- [ ] 增加智能调度算法
- [ ] 使用 HTTPS 协议
- [ ] 密码使用 BCrypt 加密

---

## 技术交流

如有问题，欢迎提交 Issue 或 Pull Request。

---

## 许可证

本项目仅供学习交流使用。

---

## 致谢

感谢所有为本项目做出贡献的开发者！

