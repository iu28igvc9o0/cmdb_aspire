package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: PdfWatermark
 * Author:   hangfang
 * Date:     2020/11/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class PdfWatermark  extends PdfPageEventHelper {

    private final String waterMarkText;

    public PdfWatermark(String waterMarkText){
        this.waterMarkText = waterMarkText;
    }
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try{
            float pageWidth = document.right() + document.left();// 获取pdf内容正文页面宽度
//
            float pageHeight = document.top() + document.bottom();// 获取pdf内容正文页面高度
            //设置水印字体格式
            BaseFont baseFont = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            Font waterMarkFont = new Font(baseFont, 16, Font.BOLD, BaseColor.LIGHT_GRAY);
            PdfContentByte waterMarkPdfContent = writer.getDirectContentUnder();
            PdfGState state = new PdfGState();
            state.setStrokeOpacity(0.5f);
            waterMarkPdfContent.setGState(state);
//            waterMarkPdfContent.beginText();
//            waterMarkPdfContent.setColorFill(BaseColor.LIGHT_GRAY);
//            waterMarkPdfContent.setFontAndSize(base, 100);
//            waterMarkPdfContent.setTextMatrix(70, 0);
//            int rise = 200;
//            for (int k = 0; k <waterMarkText.length(); k++) {
//                waterMarkPdfContent.setTextRise(rise);
//                char c = waterMarkText.charAt(k);
//                waterMarkPdfContent.showText(c + " ");
//                rise += 100;
//            }
//            waterMarkPdfContent.endText();
            Phrase phrase = new Phrase(waterMarkText, waterMarkFont);
            //两行三列
//            ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.1f,pageHeight*0.1f,45);
//            ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.25f,pageHeight*0.5f,45);
//            ColumnText.showTextAligned(waterMarkPdfContent, Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.25f,pageHeight*0.8f,45);
//            ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.65f,pageHeight*0.2f,45);
//            ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.65f,pageHeight*0.5f,45);
//            ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,
//                    pageWidth*0.65f,pageHeight*0.8f,45);
//            Rectangle pageRect = writer.getPageSize();
//            for (int height =  waterMarkText.length() *16; height < pageRect.getHeight()*2; height = height + waterMarkText.length() * 20) {
//                for (int width =  40; width < pageRect.getWidth()*1.5 + 40; width = width + 40 * 3) {
//                    // rotation:倾斜角度
//                    waterMarkPdfContent.showTextAligned(Element.ALIGN_LEFT, waterMarkText, 20, 40, 45);
//                }
//            }
//            float pageWidth = document.right() + document.left();// 获取pdf内容正文页面宽度
//
//            float pageHeight = document.top() + document.bottom();// 获取pdf内容正文页面高度
//
//            // 设置水印字体格式
//
//            BaseFont waterMarkFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
//            Font contentFont = new Font(waterMarkFont, (float) (16 * 0.75));
//            Phrase phrase = new Phrase(waterMarkText, contentFont);
            for(int i = 0 ; i < 100; i ++){
                for(int j = 0 ; j < 20 ; j ++ ){
                    ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER, phrase,pageWidth * 0.2f * j, pageHeight * 0.08f * i, 45);
                }
            }

        }catch(DocumentException de) {
            de.printStackTrace();
        }catch(IOException de) {
            de.printStackTrace();
        }
    }



}
