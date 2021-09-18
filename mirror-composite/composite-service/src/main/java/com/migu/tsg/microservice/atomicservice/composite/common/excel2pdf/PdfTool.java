package com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf;

import com.itextpdf.text.Document;

import java.io.OutputStream;


public class PdfTool {
    //
    protected Document document;
    //
    protected OutputStream os;
    
    public Document getDocument() {
        if (document == null) {
            document = new Document();
        }
        return document;
    }
}

