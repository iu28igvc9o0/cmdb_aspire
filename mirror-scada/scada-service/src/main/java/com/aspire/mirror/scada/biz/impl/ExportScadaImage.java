package com.aspire.mirror.scada.biz.impl;

import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.biz.ScadaCanvasBiz;
import com.aspire.mirror.scada.config.FtpProperties;
import com.aspire.mirror.scada.config.FtpUtls;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringReader;
import java.util.Map;


@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ExportScadaImage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportScadaImage.class);

	@Autowired
	private ScadaCanvasBiz scadaCanvasBiz;

	@Autowired
	private FtpProperties ftpProperties;

	@Value("${exportScadaImage.path:E:\\test}")
	private String path;
	
	@Value("${exportScadaImage.http}")
	private String http;
	
	@Value("${exportScadaImage.scale:3}")
	private Integer scale;

	@Value("${exportScadaImage.remotePath:topo}")
	private String remotePath;

//	@Autowired
//	private FtpUtls ftpUtls;

	@Value("${ftp.host}")
	private String host;

	@Value("${ftp.port}")
	private Integer port;

	@Value("${ftp.username}")
	private String username;

	@Value("${ftp.password}")
	private String password;

	public void createScadaImage(ScadaCanvasDTO scadaCanvasDTO) {
    	System.out.println("---------------------开始生成所有拓扑图片------------------------");
        try {
	        File root = new File(path);  
	        if (!root.exists()) {//若路径不存在则创建此路径  
	        	root.mkdirs();
	        } 
	        
	        Document configDoc = stringToDoc(ConstantUtil.cssScada);

			createImage(configDoc, scadaCanvasDTO, path);
        } catch (Exception e) {
			LOGGER.error(e.toString());
		}
    	System.out.println("---------------------完成生成所有拓扑图片------------------------");
    }

	private void createImage(Document configDoc, ScadaCanvasDTO canvaDdto, String path) {
		String fileName = path +"/"+ canvaDdto.getId() + ".png";
//		String remote = remotePath +"/"+ canvaDdto.getName() + ".png";
		try {
			File pngFile = new File(fileName); 

			String content = canvaDdto.getContent();
			Document doc=stringToDoc(content.substring(1, content.length()-1));
			
			mxGraph graph = new mxGraph();
			mxCodec codec = new mxCodec(doc);
			mxCodec codeconfig = new mxCodec(configDoc);
			Element documentElement = doc.getDocumentElement();
			String rgba=documentElement.getAttribute("background");
			String pageWidth=documentElement.getAttribute("pageWidth");
			String pageHeight=documentElement.getAttribute("pageHeight");
//        		String scale=documentElement.getAttribute("scale");
			Color backgroug = null;
			if (rgba.indexOf("(") != -1 && rgba.indexOf(")") != -1) {
				String[] rgbas = rgba.substring(rgba.indexOf("(") + 1, rgba.indexOf(")")).split(",");
				backgroug = new Color(Integer.parseInt(rgbas[0]), Integer.parseInt(rgbas[1]), Integer.parseInt
						(rgbas[2]), Integer.parseInt(rgbas[3]));
			} else if (rgba.indexOf("#") != -1){
				int r = Integer.parseInt(rgba.substring(1, 3), 16);
				int g = Integer.parseInt(rgba.substring(3, 5), 16);
				int b = Integer.parseInt(rgba.substring(5, 7), 16);
				backgroug = new Color(r, g, b);
			}
			codec.decode(documentElement, graph.getModel());
			codeconfig.decode(configDoc.getDocumentElement(), graph.getStylesheet());
			mxRectangle graphBounds = graph.getGraphBounds();
			graphBounds.setWidth(Integer.parseInt(pageWidth)*scale);
			graphBounds.setHeight(Integer.parseInt(pageHeight)*scale);
			mxGraphModel  model= (mxGraphModel)graph.getModel();
			Map<String, Object> cells=model.getCells();
			for(Object o:cells.values()) {
				mxCell child=(mxCell)o;
				mxICell source = child.getSource();
				if(source!=null&&source==child.getTarget()) {
					child.removeFromParent();
				}
			}
//        		Font font=FontUtils.getDefinedFont(50);
			graph.selectAll();
		    graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, "宋体,simsun");
		    graph.selectCells(false, false);
			mxGraphics2DCanvas dd=new mxGraphics2DCanvas();
			BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 3, backgroug, true, graphBounds,dd);
			ImageIO.write(image, "png", pngFile);
			FtpUtls ftpUtls = new FtpUtls(host, port, username, password);
			ftpUtls.uploadFile(remotePath, canvaDdto.getId() + ".png" , fileName);
			System.out.println("--------------------------生成拓扑图片---------------"+pngFile);
		} catch (Exception e) {
			System.out.println("---------------------生成图片错误---------------"+fileName);
			e.printStackTrace();
			LOGGER.error(e.toString());
		}
	}
    /**
	 * 字符串转XML
	 */
	private Document stringToDoc(String xmlStr) {
        Document doc = null;   
        try {   
//            xmlStr = new String(xmlStr.getBytes(),"UTF-8").trim();   
            xmlStr=xmlStr.replace("\\\"", "\"").replace("stencils", http+"/stencils").replace(".svg", ".png");
            StringReader sr = new StringReader(xmlStr);   
            InputSource is = new InputSource(sr);   
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
            DocumentBuilder builder = factory.newDocumentBuilder();   
            doc = builder.parse(is);   
        }catch (Exception e) {    
			LOGGER.error(e.toString());
        	e.printStackTrace();
        }   
        return doc;   
    }

}
