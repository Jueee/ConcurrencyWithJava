package com.app.jueee.concurrency.chapter04.common;

import java.io.StringWriter;
import java.util.Date;

public class CommonInformationItem {

    // RSS 项的标题
    private String title;
    
    // RSS 项的日期
    private String txtDate;
    
    // RSS 项的日期
    private Date date;
    
    // RSS 项的链接
    private String link;
    
    // RSS 项的文本描述
    private StringBuffer description;
    
    // RSS 项的 ID
    private String id;
    
    // RSS 源的名称
    private String source;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommonInformationItem() {
        description=new StringBuffer();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTxtDate() {
        return txtDate;
    }
    
    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getDescription() {
        return description.toString();
    }
    
    public void setDescription(StringBuffer description) {
        this.description = description;
    }

    public void addDescripcion(String txt) {
        description.append(txt);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public String toXML() {
        StringWriter writer=new StringWriter();
        
        writer.append("<item>\n");
        writer.append("<ide>\n");
        writer.append(id);
        writer.append("\n</id>\n");
        writer.append("\n<title>\n");
        writer.append(title);
        writer.append("\n</title>\n");
        writer.append("\n<date>\n");
        writer.append(txtDate);
        writer.append("\n</date>\n");
        writer.append("\n<link>\n");
        writer.append(link);
        writer.append("\n</link>\n");
        writer.append("\n<description>\n");
        writer.append(description);
        writer.append("\n</description>\n");
        writer.append("\n</item>\n");

        return writer.toString();
    }
    
    public String getFileName() {
        StringWriter writer=new StringWriter();
        writer.append(source);
        writer.append("_");
        writer.append(String.valueOf(description.hashCode()));
        writer.append(".xml");
        
        return writer.toString();
        
    }
}
