package cn.jc.incident.core.report.impl;

import cn.jc.incident.core.report.PdfRenderService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfRenderServiceImpl implements PdfRenderService {

    @Override
    public byte[] renderPdf(String html) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // 1️⃣ HTML → XHTML（关键）
            String xhtml = toXhtml(html);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(xhtml, null);

            // 2️⃣ 中文字体
            // === 核心：从 classpath 加载字体 ===
            builder.useFont(
                    () -> getClass().getResourceAsStream("/fonts/SimHei.ttf"),
                    "SimHei"
            );
//            InputStream fontStream = getClass()
//                    .getResourceAsStream("/fonts/SimHei.ttf");
//            if (fontStream == null) {
//                throw new IllegalStateException("SimHei.ttf not found in classpath:/fonts");
//            }
//
//            builder.useFont(() -> fontStream, "SimHei");
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF render failed", e);
        }
    }

    /**
     * 把“浏览器友好 HTML”
     * 转成“XML 100% 合法 XHTML”
     */
    private String toXhtml(String html) {
        Document doc = Jsoup.parse(html);

        doc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)   // 强制 XHTML
                .escapeMode(Entities.EscapeMode.xhtml)        // 转义 &, <, >
                .charset("UTF-8");

        // 额外兜底（防止模板里残留）
        return doc.html()
                .replace("&nbsp;", " ");
    }
}

