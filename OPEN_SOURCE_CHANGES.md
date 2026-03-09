# 开源版改造说明

## 📋 改造目标

将原商业版系统简化为开源版本，保留核心日志上传和下载功能，删除 AI 分析和商业功能。

---

## ✅ 主要改动

### 1. 模块结构调整

**删除的模块:**
- ❌ `incident-ai` - AI 分析模块（包含所有 AI 相关代码）

**保留的模块:**
- ✅ `incident-api` - API 接口层
- ✅ `incident-core` - 核心业务逻辑（简化版）
- ✅ `incident-infrastructure` - 基础设施层

### 2. 核心功能简化

#### IncidentServiceImpl 改造
- ❌ 删除 AI 分析调用 (`AIClient`)
- ❌ 删除复杂评估器 (`CompositeSeverityEvaluator`)
- ❌ 删除报告策略切换逻辑 (`ReportPolicy`)
- ✅ 保留基础规则分析 (`analyzeWithRules`)
- ✅ 保留日志预处理和根因提取
- ✅ 简化报告生成流程

#### 评估器简化
- **CompositeSeverityEvaluator**: 统一返回 P3 级别，不再使用多评分器机制
- **其他评分器**: 保留文件但不再使用（AISeverityScorer, BusinessExceptionScorer 等）

### 3. Controller 接口调整

**保留的接口:**
```java
POST /incidents/upload          // 上传日志文件
POST /incidents/analyze-text    // 文本日志分析
GET  /incidents/{id}/insight    // 获取洞察
GET  /incidents/{id}/report     // 获取报告视图
GET  /incidents/{id}/report/file // 下载 Markdown 报告
GET  /incidents/{id}/report.html // 下载 HTML 报告
GET  /incidents/{id}/report.pdf  // 下载 PDF 报告
```

**删除的接口:**
```java
POST /incidents/analyze         // 通用分析接口（支持预算控制）
POST /alerts/webhook            // 告警 Webhook（AlertManager 集成）
```

### 4. 配置文件简化

**application.yml 改动:**
```yaml
# 简化前
- 复杂的线程池配置
- 多环境 profile 切换
- JPA 严格验证模式

# 简化后
+ 基础数据库配置
+ JPA ddl-auto: update (宽松模式)
+ 标准端口 8080
+ 日志级别配置
```

### 5. 依赖清理

**pom.xml 改动:**
- ❌ 删除 `incident-ai` 模块依赖
- ❌ 删除私有 Nexus 仓库配置
- ❌ 删除商业版 distributionManagement 配置
- ✅ 保留阿里云公共仓库

### 6. 报告策略统一

**商业版:**
```java
switch (plan) {
    case PRO -> new ProReportPolicy();      // 专业版报告
    case BUSINESS -> new BusinessReportPolicy(); // 商业版报告
    default -> new FreeReportPolicy();      // 免费版报告
}
```

**开源版:**
```java
// 统一返回标准报告
ReportPlan.FREE
```

---

## 🎯 开源版功能清单

### 核心功能 ✅

1. **日志上传**
   - 文件上传分析
   - 文本输入分析
   
2. **基础分析**
   - 异常类型识别
   - 根因提取
   - 错误位置定位
   - 关键证据收集

3. **报告生成**
   - Markdown 格式
   - HTML 格式
   - PDF 格式

4. **数据存储**
   - PostgreSQL 数据库
   - 事故记录持久化

### 删除功能 ❌

1. **AI 智能分析**
   - OpenAI/自定义 AI 模型集成
   - Token 预算管理
   - 智能根因推荐

2. **告警集成**
   - AlertManager Webhook
   - 规则引擎触发
   - 异步通知

3. **商业特性**
   - 多版本报告策略
   - 严重度动态评估
   - 用户影响分析

---

## 📦 部署说明

### 环境要求
- Java 21+
- PostgreSQL 12+
- Maven 3.6+

### 快速启动

```bash
# 1. 创建数据库
createdb incident_db

# 2. 修改配置
vim incident-api/src/main/resources/application.yml

# 3. 构建项目
mvn clean package -DskipTests

# 4. 启动服务
java -jar incident-api/target/incident-api-1.0.0.jar

# 5. 访问 API
curl http://localhost:8080/api/incidents/upload \
  -F "file=@test.log" \
  -F "serviceName=test"
```

---

## 🔧 技术栈对比

| 组件 | 商业版 | 开源版 |
|------|--------|--------|
| 后端框架 | Spring Boot 3.x | Spring Boot 3.x ✅ |
| 数据库 | PostgreSQL | PostgreSQL ✅ |
| AI 集成 | OpenAI/Mock | 无 ❌ |
| 模板引擎 | Thymeleaf | Thymeleaf ✅ |
| PDF 生成 | OpenHTMLToPDF | OpenHTMLToPDF ✅ |
| 规则引擎 | DSL 规则引擎 | 简单规则 ✅ |

---

## 📝 后续优化建议

1. **前端界面**: 开发简单的 Web UI，方便用户上传日志和查看报告
2. **规则增强**: 添加可配置的规则文件，支持自定义异常模式
3. **性能优化**: 增加异步处理和批量分析能力
4. **监控集成**: 预留 Prometheus/Grafana 监控接口
5. **插件系统**: 设计插件架构，支持扩展分析能力

---

## 🚀 引流策略

### 功能对比表（用于 README）

| 功能 | 开源版 | 商业版 |
|------|--------|--------|
| 日志上传 | ✅ | ✅ |
| 基础分析 | ✅ | ✅ |
| Markdown 报告 | ✅ | ✅ |
| HTML/PDF导出 | ✅ | ✅ |
| AI 智能分析 | ❌ | ✅ |
| 告警集成 | ❌ | ✅ |
| 自定义策略 | ❌ | ✅ |
| 技术支持 | 社区 | 专业团队 |

### 升级引导

在开源版中埋入商业版推广信息:
- 启动日志显示："开源版 - 如需 AI 增强功能请访问 xxx"
- API 响应注释提示商业版特性
- README 中添加商业版链接

---

## ⚠️ 注意事项

1. **数据迁移**: 如已有商业版数据，需要编写迁移脚本
2. **API 兼容**: 保持部分 API 兼容，方便用户升级
3. **许可证**: 确保开源协议允许商用（建议使用 MIT/Apache 2.0）
4. **品牌标识**: 移除所有商业版 Logo 和商标信息

---

**改造完成时间**: 2026-03-09  
**版本**: v1.0.0-open-source
