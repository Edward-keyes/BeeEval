# BeeEval - 智能座舱评测平台

BeeEval 是一个面向汽车智能座舱的综合评测平台，提供标准化评测体系、多维度车型对比、功能表现视频展示和数据可视化分析能力。

## 项目结构

```
BeeEval_All/
├── bee-eval_-front-end/       # 前端 - Next.js 应用
└── xai-lab-vehicle/           # 后端 - Spring Cloud 微服务
    ├── xai-vehicledata/       # 核心业务：车辆评测数据
    ├── xai-vehicle-operation-manager/      # 运营管理系统
    ├── xai-vehicle-operation-manager-api/  # 运营管理 API
    ├── xai-vehicle-feign/     # Feign 远程调用
    ├── xai-cloud-framework/   # 公共框架
    ├── xai-common/            # 公共工具
    ├── xai-member/            # 会员服务
    ├── xai-order/             # 订单服务
    ├── xai-forum/             # 论坛服务
    ├── xai-message/           # 消息服务
    ├── renren-fast/           # 后台管理
    └── sql/                   # 数据库脚本
```

## 技术栈

### 前端

| 技术 | 说明 |
|------|------|
| Next.js 14 | React 全栈框架 |
| TypeScript | 类型安全 |
| Tailwind CSS | 原子化样式 |
| Radix UI | 无障碍组件库 |
| Zustand | 状态管理 |
| Recharts | 数据可视化 |
| next-intl | 国际化 |
| next-auth | 身份认证 |

### 后端

| 技术 | 说明 |
|------|------|
| Java 21 | 运行环境 |
| Spring Boot 3.2 | 应用框架 |
| Spring Cloud 2023.0 | 微服务治理 |
| Spring Cloud Alibaba | Nacos 注册/配置中心 |
| MyBatis-Plus 3.5 | ORM 持久层 |
| Sa-Token | 权限认证 |
| Redisson | 分布式缓存与锁 |
| Knife4j | API 文档 |
| MySQL | 关系数据库 |
| Redis | 缓存 |
| 阿里云通义千问 | LLM 智能问答 |

## 环境要求

- **Java** 21+
- **Maven** 3.8+
- **Node.js** 18+
- **MySQL** 8.0+
- **Redis** 6+
- **Nacos** 2.x

## 快速开始

### 后端启动

```bash
cd xai-lab-vehicle

# 安装依赖
mvn clean install -DskipTests

# 启动核心服务（需先配置 Nacos 和数据库）
cd xai-vehicledata
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 前端启动

```bash
cd bee-eval_-front-end

# 安装依赖
npm install

# 配置环境变量
cp .env.example .env.local
# 编辑 .env.local，设置 API_BASE_URL

# 启动开发服务器
npm run dev
```

访问 http://localhost:3000 查看前端页面。

## 环境变量配置

### 前端 (.env.local)

```env
API_BASE_URL=http://localhost:8181/xai-vehicledata
```

### 后端

后端配置通过 Nacos 配置中心管理，本地开发使用 `bootstrap-dev.yml` 指定 Nacos 地址。

## 核心功能

- **评测体系** — 功能域 → 一级标签 → 二级标签 → 三级标签的多层级指标
- **车型对比** — 多车型评测数据横向对比与排行
- **视频评测** — 功能表现视频关联与分页展示
- **LLM 智能问答** — 基于通义千问的智能座舱知识问答
- **数据管理** — Excel 批量导入评测数据
- **用户行为分析** — 操作日志与行为监控
- **多云存储** — 支持阿里云 OSS / MinIO / 腾讯 COS / 七牛

## API 文档

后端启动后访问 Knife4j 接口文档：

```
http://localhost:8181/xai-vehicledata/doc.html
```

## 部署

### 后端 Docker 部署

```bash
cd xai-lab-vehicle/renren-fast
docker-compose up -d
```

### 前端构建

```bash
cd bee-eval_-front-end
npm run build
npm run start
```

## 注意事项

- 上传代码前请确保清除配置文件中的敏感信息（API Key、数据库密码等）
- 生产环境配置应通过环境变量或 Nacos 配置中心注入
- `uploads/` 目录为运行时生成，不纳入版本管理

## License

Private - All Rights Reserved
