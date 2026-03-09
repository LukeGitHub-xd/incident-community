package cn.jc.incident.core.util;

import java.util.regex.Pattern;

/**
 * 日志屏蔽器
 *
 * @author xuedongdong
 * @date 2026/03/08
 */
public class LogMasker {
    // 手机号
    private static final Pattern PHONE =
            Pattern.compile("\\b1[3-9]\\d{9}\\b");

    // IPv4/IPv6
    private static final Pattern IPV4 =
            Pattern.compile("\\b\\d{1,3}(\\.\\d{1,3}){3}\\b");
    private static final Pattern IPV6 =
            Pattern.compile("([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}");

    // Email
    private static final Pattern EMAIL =
            Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+");

    // 身份证号
    private static final Pattern ID_CARD =
            Pattern.compile("\\b\\d{15}(\\d{2}[0-9xX])?\\b");

    // 银行卡号（Luhn 校验可选）
    private static final Pattern BANK_CARD =
            Pattern.compile("\\b\\d{12,19}\\b");

    // 密码 / pwd / pass
    private static final Pattern PASSWORD =
            Pattern.compile("(password|pwd|pass)[=:]\\s*[^\\s]+",
                    Pattern.CASE_INSENSITIVE);

    // Token / AccessToken / Auth
    private static final Pattern TOKEN =
            Pattern.compile("(Bearer|Token|access[_-]?token)[=:]\\s*[A-Za-z0-9._-]+",
                    Pattern.CASE_INSENSITIVE);

    // Cookie / Session
    private static final Pattern COOKIE =
            Pattern.compile("Cookie:\\s*[^\\n]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern SESSION_ID =
            Pattern.compile("(sessionId|JSESSIONID)[=:]\\s*[A-Za-z0-9-]+",
                    Pattern.CASE_INSENSITIVE);

    // TraceId / SpanId / RequestId
    private static final Pattern TRACE_ID =
            Pattern.compile("(traceId|spanId|requestId)[=:]\\s*[A-Za-z0-9-]+",
                    Pattern.CASE_INSENSITIVE);

    // URL / DB / JDBC / Redis
    private static final Pattern DB_URL =
            Pattern.compile("jdbc:[^\\s]+");
    private static final Pattern REDIS_URL =
            Pattern.compile("redis://[^\\s]+");
    private static final Pattern HTTP_URL =
            Pattern.compile("(http|https)://[^\\s]+");

    // 内部IP / 私网IP
    private static final Pattern INTERNAL_IP =
            Pattern.compile("\\b10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b|\\b192\\.168\\.\\d{1,3}\\.\\d{1,3}\\b");

    // MAC地址
    private static final Pattern MAC =
            Pattern.compile("([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}");

    // UUID
    private static final Pattern UUID =
            Pattern.compile("\\b[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\b");

    // IC卡号 / SWIFT / IBAN / 税号
    private static final Pattern IBAN =
            Pattern.compile("\\b[A-Z]{2}\\d{2}[A-Z0-9]{1,30}\\b");
    private static final Pattern SWIFT =
            Pattern.compile("\\b[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?\\b");
    private static final Pattern TAX_ID =
            Pattern.compile("\\b\\d{15,20}\\b");

    // IP:PORT
    private static final Pattern IP_PORT =
            Pattern.compile("\\b(\\d{1,3}(\\.\\d{1,3}){3}):(\\d{1,5})\\b");

    // MAC / DeviceId / HWID
    private static final Pattern DEVICE_ID =
            Pattern.compile("(deviceId|hwid)[=:]\\s*[A-Za-z0-9-]+", Pattern.CASE_INSENSITIVE);

    // 其它数字长ID（订单号、用户号等）
    private static final Pattern LONG_ID =
            Pattern.compile("\\b\\d{10,20}\\b");

    // 各类敏感关键字
    private static final Pattern API_KEY =
            Pattern.compile("api[_-]?key[=:]\\s*[^\\s]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern SECRET =
            Pattern.compile("(secret|client[_-]?secret)[=:]\\s*[^\\s]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern PRIVATE_KEY =
            Pattern.compile("-----BEGIN [A-Z ]*PRIVATE KEY-----[\\s\\S]+?-----END [A-Z ]*PRIVATE KEY-----");

    // OAuth / JWT
    private static final Pattern JWT =
            Pattern.compile("eyJ[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+");

    // IP范围 / CIDR
    private static final Pattern CIDR =
            Pattern.compile("\\b\\d{1,3}(\\.\\d{1,3}){3}/\\d{1,2}\\b");

    // 全部Pattern集合
    private static final Pattern[] MASK_PATTERNS = new Pattern[]{
            PHONE, IPV4, IPV6, INTERNAL_IP, MAC, EMAIL, ID_CARD, BANK_CARD,
            PASSWORD, TOKEN, COOKIE, SESSION_ID, TRACE_ID, DB_URL, REDIS_URL,
            HTTP_URL, UUID, IBAN, SWIFT, TAX_ID,
            IP_PORT, DEVICE_ID, LONG_ID, API_KEY, SECRET, PRIVATE_KEY, JWT, CIDR
    };

    public static String mask(String log) {

        if (log == null || log.isBlank()) return "";

        String masked = log;

        for (Pattern p : MASK_PATTERNS) {
            masked = p.matcher(masked).replaceAll(m -> {
                String name = "[" + p.pattern().split("\\|")[0] + "]";
                return name;
            });
        }

        return masked;
    }
}