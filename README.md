# Incident Community - 开源版

## 🎯 项目简介

**Incident Community** 是一个轻量级日志分析工具，专注于帮助开发者快速定位和分析系统异常。

### ✨ 核心功能

- 📤 **日志上传**: 支持文件上传和文本输入两种方式
- 🔍 **智能分析**: 基于规则引擎自动识别异常根因
- 📊 **可视化报告**: 生成结构化的事故分析报告
- 💾 **多格式导出**: 支持 Markdown、HTML、PDF 格式下载
- 🆓 **完全开源**: 免费使用，无商业限制

---

## 🚀 快速开始

### 环境要求

- Java 21+
- PostgreSQL 17+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/your-org/incident-community.git
cd incident-community
```

2. **配置数据库**
```sql
CREATE DATABASE incident_db;
```

3. **修改配置文件**
编辑 `incident-api/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/incident_db
    username: postgres
    password: your_password
```

4. **构建并运行**
```bash
mvn clean package -DskipTests
java -jar incident-api/target/incident-api-1.0.0.jar
```

5. **访问服务**
打开浏览器访问：http://localhost:8080/api

---

## 📖 API 使用指南

### 1. 上传日志文件

```bash
curl -X POST http://localhost:8080/api/incidents/upload \
  -F "file=@error.log" \
  -F "serviceName=my-service" \
  -F "env=production"
```

### 2. 文本日志分析

```bash
curl -X POST "http://localhost:8080/api/incidents/analyze-text?log=ERROR...&serviceName=test&env=dev"
```

### 3. 获取分析洞察

```bash
curl http://localhost:8080/api/incidents/{incidentId}/insight
```

### 4. 下载报告

**Markdown 格式:**
```bash
curl http://localhost:8080/api/incidents/{id}/report/file -o report.md
```

**HTML 格式:**
```bash
curl http://localhost:8080/api/incidents/{id}/report.html -o report.html
```

**PDF 格式:**
```bash
curl http://localhost:8080/api/incidents/{id}/report.pdf -o report.pdf
```

---

## 🛠️ 技术栈

- **后端框架**: Spring Boot 3.x
- **数据库**: PostgreSQL
- **构建工具**: Maven
- **模板引擎**: Thymeleaf
- **PDF 生成**: OpenHTMLToPDF

---

## 📝 功能对比

| 功能 | 开源版 | 商业版 |
|------|--------|--------|
| 日志上传分析 | ✅ | ✅ |
| 基础规则引擎 | ✅ | ✅ |
| Markdown 报告 | ✅ | ✅ |
| HTML/PDF导出 | ✅ | ✅ |
| AI 智能分析 | ❌ | ✅ |
| 告警集成 | ❌ | ✅ |
| 自定义报告策略 | ❌ | ✅ |
| 技术支持 | 社区 | 专业团队 |

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

---

## 📄 开源协议

MIT License

---

## 📧 联系方式

- 项目地址：https://github.com/LukeGitHub-xd/incident-community
- 问题反馈：https://github.com/LukeGitHub-xd/incident-community/issues

---

**🎉 开源版 - 让日志分析更简单！**