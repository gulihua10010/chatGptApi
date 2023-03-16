package cn.jianwoo.chatgpt.api.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Code;
import org.commonmark.node.Document;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * @author gulihua
 * @Description
 * @date 2022-12-26 14:49
 */
public class MarkdownToHtmlUtils
{
    public static String markdownToHtml(String markdown)
    {
        Parser parser = Parser.builder().build();
        Node paragraphs = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(paragraphs);
    }


    public static String markdownToHtmlExtensions(String markdown)
    {
        // h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        // 转换table的HTML
        List<Extension> tableExtension = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(tableExtension).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(headingAnchorExtensions).extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context)
                    {
                        return new CustomAttributeProvider();
                    }
                }).build();
        String content = renderer.render(document).replaceAll("\n", "<br/>");
        if (content.endsWith("<br/>"))
        {
            content = content.substring(0, content.length() - 5);
        }
//        content = content.replaceAll()
        return content;
    }

    static class CustomAttributeProvider implements AttributeProvider
    {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes)
        {
            // 改变a标签的target属性为_blank
            if (node instanceof Link)
            {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock)
            {
                attributes.put("class", "ui celled table");
            }
            if ("pre".equalsIgnoreCase(tagName))
            {
                attributes.put("style",
                        "padding: 2px;background-color: #202123;" + "color: #eee;"
                                + "display: inline-block; overflow: auto;" + "white-space: pre;" + "padding: 10rpx;"
                                + "border-radius: 5px;" + "width: 95%;word-wrap: break-word;word-break: break-all");
            }
        }
    }

}
