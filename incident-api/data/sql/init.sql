-- ================================
-- SaaS 标准双ID模型
-- PostgreSQL 17.8
-- ================================

-- 必须启用 JSONB 扩展（默认已有）
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- ================================
-- 1. incident_report（核心表）
-- ================================
CREATE TABLE incident_report
(
    id BIGSERIAL PRIMARY KEY,

    report_code VARCHAR(40) UNIQUE NOT NULL, -- INC-20260221-000123

    service     VARCHAR(100),
    env         VARCHAR(50),

    severity    VARCHAR(10),
    confidence  DOUBLE PRECISION,

    tokens_used INT,
    cost_amount NUMERIC(10, 4),

    report_json JSONB,

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_incident_created_at ON incident_report (created_at);
CREATE INDEX idx_incident_code ON incident_report (report_code);
CREATE INDEX idx_incident_json ON incident_report USING GIN (report_json);
CREATE TABLE incident
(
    id             varchar(64) PRIMARY KEY,
    service_name   varchar(100),
    env            varchar(50),
    occur_time     timestamp,
    summary        text,
    change_summary text,
    raw_log        text,

    confirmed_root_causes jsonb,
    suspected_root_causes jsonb,
    uncertainties jsonb,
    recommendations jsonb,
    severity_decisions jsonb,

    severity_level varchar(20),
    user_impact    boolean,
    token_usage    int,
    created_at     timestamp
);

create table public.license_usage
(
    month    varchar(7) not null
        primary key,
    used     integer,
    checksum varchar(64)
);

alter table public.license_usage
    owner to inclined;



