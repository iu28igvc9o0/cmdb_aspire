package com.aspire.mirror.scada.controller;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.aspire.mirror.scada.api.dto.Node;
import com.aspire.mirror.scada.api.dto.PathRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasPageRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasReq;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.api.service.ScadaCanvasService;
import com.aspire.mirror.scada.biz.ScadaCanvasBiz;
import com.aspire.mirror.scada.common.entity.ResMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.scada.controller
 * 类名称:     ScadaCanvasController
 * 类描述:     监控视图API
 * 创建时间:     2019-06-11 11:32:23
 *
 * @author JinSu
 */
@RestController
@CacheConfig(cacheNames = "ScadaCanvasCache")
public class ScadaCanvasController implements ScadaCanvasService {
    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScadaCanvasController.class);

    /**
     * FTP服务器端口
     */
    private static String port = "21";
    /**
     * 文件最大限制
     */
    private static String size = "300000";

    private static String host103 = "10.1.5.103";
    /**
     * FTP登录账号
     */
    private static String user103 = "sudoroot";
    /**
     * FTP登录密码
     */
    private static String password103 = "spider+999";
    /**
     * 文件映射路径   103的存放路径为"/opt/aspire/product/scada/uploadFile/"、
     * 62的存放路径为"/mnt/file/"、
     */
    private static String resourceHandler = "/file/";
    private static String resourceLocations103 = "/opt/aspire/product/scada/uploadFile/";

    private static String host62 = "10.12.70.62";
    private static String user62 = "sudoroot";
    private static String password62 = "Opstest+789";
    private static String resourceLocations62 = "/mnt/file/";

    @Autowired
    private ScadaCanvasBiz scadaCanvasBiz;

    /**
     * 根据主键删除视图
     *
     * @param scadaCanvasId 主键
     * @@return Result 返回结果
     */
    @Override
    public ResMap deleteByPrimaryKey(@PathVariable("id") final String scadaCanvasId) {
        scadaCanvasBiz.deleteByPrimaryKey(scadaCanvasId);
        return ResMap.success();
    }

    /**
     * 根据主键修改视图
     *
     * @param scadaCanvasReq 动作创建请求对象
     * @return CanvasCreateResponse 动作创建响应对象
     */
    @Override
    public ResMap modifyByPrimaryKey(@RequestBody final ScadaCanvasReq scadaCanvasReq) {
        if (StringUtils.isEmpty(scadaCanvasReq.getId())) {
            LOGGER.error("created param scadaCanvasId is null");
            return ResMap.notice("scadaCanvasId is null", null);
        }
        //判断是否有图片类型和网页类型
//        ResMap resMap = checkPictureAndPageType(scadaCanvasReq);
//        if (!resMap.getCode().equals(ResErrorEnum.SUCCESS.getCode())) {
//            return resMap;
//        }
        //根据监控对象id和图片类型查询是否是修改绑定对象且原来已存在画布，若有，则删除原来的画布
//        ResMap result = getScadaCanvasInfoByPrecinctId(scadaCanvasReq.getPrecinctId(),scadaCanvasReq.getPictureType
// ());
//        if (result.getCode().equals(ResErrorEnum.SUCCESS.getCode())&&result.getData()!=null){
//            ScadaCanvasDTO old = (ScadaCanvasDTO) result.getData();
//            if (!old.getId().equals(scadaCanvasReq.getId())){
//                scadaCanvasBiz.deleteByPrimaryKey(old.getId());
//            }
//        }
        ScadaCanvasDTO findCanvasDTO = scadaCanvasBiz.selectByPrimaryKey(scadaCanvasReq.getId());
        if (null == findCanvasDTO) {
            return ResMap.notice("scadaCanvas is not found", null);
        }
        //检测是否是修改绑定对象，若已修改，则将A复制给B，且保留A
//        if(!findCanvasDTO.getPrecinctId().equals(scadaCanvasReq.getPrecinctId())){
//            ScadaCanvasDTO newCanvasDTO = new ScadaCanvasDTO();
//            BeanUtils.copyProperties(scadaCanvasReq, newCanvasDTO);
//            scadaCanvasBiz.insert(newCanvasDTO);
//        }else {
        ScadaCanvasDTO scadaCanvasDTO = new ScadaCanvasDTO();
        BeanUtils.copyProperties(scadaCanvasReq, scadaCanvasDTO);
        scadaCanvasBiz.updateByPrimaryKey(scadaCanvasDTO);
//        }
        return ResMap.success(scadaCanvasReq);
    }

//    private ResMap checkPictureAndPageType(ScadaCanvasReq scadaCanvasReq) {
//        if (StringUtils.isEmpty(scadaCanvasReq.getPictureType())||StringUtils.isEmpty(scadaCanvasReq.getPageType())){
//            return ResMap.notice("pictureType or pageType is null",null);
//        }
//        if (!scadaCanvasReq.getPageType().equals(PageTypeEnum.ORDINARY_PAGE.getCode())&&
//                !scadaCanvasReq.getPageType().equals(PageTypeEnum.TEMPLATE.getCode()) ){
//            LOGGER.error("created param pageType is wrong");
//            return ResMap.notice("pageType is wrong",null);
//        }
//        return ResMap.success();
//    }

    /**
     * 根据主键查找动作详情信息
     *
     * @param scadaCanvasId 主键
     * @return CanvasVO 详情响应对象
     */
    @Override
    public ScadaCanvasDTO findByPrimaryKey(@RequestParam("id") final String scadaCanvasId) {
        if (StringUtils.isEmpty(scadaCanvasId)) {
            LOGGER.warn("findByPrimaryKey param scadaCanvasId is null");
            return null;
        }
        ScadaCanvasDTO scadaCanvasDTO = scadaCanvasBiz.selectByPrimaryKey(scadaCanvasId);
        return scadaCanvasDTO;
    }

//    @Override
//    public ResMap pictureTransformatting(@RequestParam("picture") final MultipartFile picture) {
//        String data = null;
//        if (picture != null) {
//            BASE64Encoder encoder = new BASE64Encoder();
//            // 通过base64来转化图片
//            try {
//                data = encoder.encode(picture.getBytes());
//            } catch (IOException e) {
//            }
//        }
//        return ResMap.success(data);
//    }

    @Override
    public ResMap creatScadaCanvas(@RequestBody final ScadaCanvasReq scadaCanvasReq) {
        //根据监控对象id和背景图片类型查询是否已存在视图，若存在，则删除
        ScadaCanvasDTO canvasDTO = null;
        if (!StringUtils.isEmpty(scadaCanvasReq.getPod()) && !StringUtils.isEmpty(scadaCanvasReq.getIdc()) &&
                !StringUtils.isEmpty(scadaCanvasReq.getPictureType())) {
            ScadaCanvasDTO scadaCanvasDTO = new ScadaCanvasDTO();
            BeanUtils.copyProperties(scadaCanvasReq, scadaCanvasDTO);

            //设置PictureType,只保留第一位数字 (add by zhujiahao)
//            scadaCanvasDTO.setPictureType(Integer.parseInt(Integer.toString(scadaCanvasReq.getPictureType()).substring(0,
//                    1)));
			// 创建图片
            String canvasId = scadaCanvasBiz.insert(scadaCanvasDTO);
            canvasDTO = findByPrimaryKey(canvasId);
        }

        return ResMap.success(canvasDTO);
    }

//    @Override
//    public ResMap getScadaCanvasInfoByPrecinctId(String precinctId, Integer pictureType) {
//        if (StringUtils.isEmpty(precinctId) || StringUtils.isEmpty(pictureType)) {
//            LOGGER.warn("param precinctId/pictureType is null");
//            return ResMap.notice("param precinctId/pictureType is null", null);
//        }
//        return scadaCanvasBiz.getScadaCanvasInfoByPrecinctId(precinctId, pictureType);
//    }

//    @Override
//    public ResMap upload(HttpServletRequest request, @RequestParam(value = "files") final MultipartFile file) throws
//            Exception {
//        InputStream is = null;
//        String result = null;
//        //获取配置文件中的上传图标文件的最大大小
//        int ftpPort = Integer.valueOf(port);
//        Long iconMaxsize = Long.valueOf(size);
//        String fileName = file.getOriginalFilename();
//        System.out.println(file.getSize());
//        if (file.getSize() > iconMaxsize) {
//            return ResMap.notice("上传的文件过大", null);
//        }
//        if (!file.isEmpty()) {
//            is = file.getInputStream();
//            //根据本机IP选择要上传的服务器
//            InetAddress inetAddress = InetAddress.getLocalHost();
//            //取得服务器端口
//            int serverPort = request.getLocalPort();
//            String ia = inetAddress.getHostAddress().toString();
//            if (host62.equals(ia)) {
//                result = FtpUtls.uploadFile(host62, ftpPort, user62, password62, serverPort,
//                        resourceLocations62, resourceHandler, fileName, is);
//            }
////              else if (host103.equals(ia)){
//            else {
////                result = FtpUtls.uploadFile(host103, ftpPort, user103, password103,serverPort,
////                        resourceLocations103, resourceHandler,fileName, is);
//
//                result = FtpUtls.uploadFile(host62, ftpPort, user62, password62, serverPort,
//                        resourceLocations62, resourceHandler, fileName, is);
//            }
//            if (null != result && !"".equals(result)) {
//            } else {
//                return ResMap.notice("文件[\"+fileName+\"]上传失败", null);
//            }
//        }
//        return ResMap.success(result);
//    }

//    @Override
//    public void getFile(String remoteFilePath, HttpServletResponse response) throws IOException {
//        //读取路径下面的文件
//        File file = new File(remoteFilePath);
//        File picFile = null;
//        for (File f : file.listFiles()) {
//            //根据路径获取文件
//            picFile = new File(f.getPath());
//            //获取文件后缀名格式
//            String ext = picFile.getName().substring(picFile.getName().indexOf("."));
//            //判断图片格式,设置相应的输出文件格式
//            if (ext.equals("jpg")) {
//                response.setContentType("image/jpeg");
//            } else if (ext.equals("JPG")) {
//                response.setContentType("image/jpeg");
//            } else if (ext.equals("png")) {
//                response.setContentType("image/png");
//            } else if (ext.equals("PNG")) {
//                response.setContentType("image/png");
//            }
//        }
//        //读取指定路径下面的文件
//        InputStream in = new FileInputStream(picFile);
//        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//        //创建存放文件内容的数组
//        byte[] buff = new byte[1024];
//        //所读取的内容使用n来接收
//        int n;
//        //当没有读取完时,继续读取,循环
//        while ((n = in.read(buff)) != -1) {
//            //将字节数组的数据全部写入到输出流中
//            outputStream.write(buff, 0, n);
//        }
//        //强制将缓存区的数据进行输出
//        outputStream.flush();
//        //关流
//        outputStream.close();
//        in.close();
//    }

//    @Override
//    public ResMap uploadByByte(HttpServletRequest request, byte[] buffer, String fileName) {
//        InputStream inputStream = new ByteArrayInputStream(buffer);
//        String result = null;
//        //获取配置文件中的上传图标文件的最大大小
//        int ftpPort = Integer.valueOf(port);
//        //根据本机IP选择要上传的服务器
//        InetAddress inetAddress = null;
//        try {
//            inetAddress = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        //取得服务器端口
//        int serverPort = request.getLocalPort();
//        String ia = inetAddress.getHostAddress().toString();
//        if (host62.equals(ia)) {
//            result = FtpUtls.uploadFile(host62, ftpPort, user62, password62, serverPort,
//                    resourceLocations62, resourceHandler, fileName, inputStream);
//        }
////              else if (host103.equals(ia)){
//        else {
//            result = FtpUtls.uploadFile(host103, ftpPort, user103, password103, serverPort,
//                    resourceLocations103, resourceHandler, fileName, inputStream);
//        }
//        if (null != result && !"".equals(result)) {
//        } else {
//            return ResMap.notice("文件[\"+fileName+\"]上传失败", null);
//        }
//        return ResMap.success(result);
//    }

    @Override
    public ResMap findScadaCanvasList(@RequestBody(required=false) ScadaCanvasReq scadaCanvasReq) {
        ScadaCanvasDTO scadaCanvasDTO = new ScadaCanvasDTO();
        BeanUtils.copyProperties(scadaCanvasReq, scadaCanvasDTO);
        if (!StringUtils.isEmpty(scadaCanvasReq.getBizSystem())) {
            scadaCanvasDTO.setBizSystemList(Arrays.asList(scadaCanvasReq.getBizSystem().split(",")));
        }
        List<ScadaCanvasDTO> scadaCanvasList = scadaCanvasBiz.findScadaCanvasList(scadaCanvasDTO);
        return ResMap.success(scadaCanvasList);
    }


//===========================特殊任务==========================================================//


    ////特殊任务，将文件名和节点id关联
//    @Override
//    public String batchFindPrecinctId() {
//        String result = scadaCanvasBiz.batchFindPrecinctId();
//        return result;
//    }


    //特殊任务，视图批量插入数据库
//    @Override
//    public String batchSaveScadaCanvas() {
//        String result = null;
//        try {
//            result = scadaCanvasBiz.batchSaveScadaCanvas();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    @Override
    public ScadaCanvasDTO findScadaCanvasByName(@RequestParam("name") String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.warn("findScadaCanvasByName param name is null");
            return null;
        }
        ScadaCanvasDTO scadaCanvasDTO = scadaCanvasBiz.findScadaCanvasByName(name);
        return scadaCanvasDTO;
    }

    @Override
    public PageResponse<ScadaCanvasDTO> pageList(@RequestBody ScadaCanvasPageRequest scadaCanvasPageRequest) {
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(scadaCanvasPageRequest, page);
        Map<String, Object> map = FieldUtil.getFiledMap(scadaCanvasPageRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<ScadaCanvasDTO> pageResult =scadaCanvasBiz.pageList(page);
        return pageResult;
    }
    
    @Override
    public ResMap getPath(@RequestBody PathRequest pathRequest) throws Exception{
    	 if (StringUtils.isEmpty(pathRequest.getContent()) || StringUtils.isEmpty(pathRequest.getDestIp()) 
    			 ||StringUtils.isEmpty(pathRequest.getSourceIp())) {
             LOGGER.warn("getPath param error:{}",pathRequest);
             return new ResMap(5,"请求异常");
         }
    	 String sourceId = "";
      	String destId = "";
      	String str = pathRequest.getContent().substring(1, pathRequest.getContent().length()-1);
      	str = str.replace("\\\"", "\"");
         SAXReader reader = new SAXReader();
         Document document;
         Map<String, Node> nodeMap = Maps.newHashMap();
         Map<String, String> ipMap = Maps.newHashMap();
         List<String> pathList = Lists.newArrayList();
         try {
             document = reader.read(new ByteArrayInputStream(str
            	     .getBytes("UTF-8")));
             LOGGER.info("***************************开始解析拓扑图***************************");
             //获取所有拥有autoSaveNode属性的mxCell节点
             //---------------------------方案一-----------------------------------------------------
             Element rootElt = document.getRootElement(); // 获取根节点
             Element rootjd = rootElt.element("root");
             Iterator rootiter = rootjd.elementIterator("mxCell"); // 获取根节点下的子节点mxCell
             while (rootiter.hasNext()) {
                 Element recordEle = (Element) rootiter.next();
                 String edge = recordEle.attributeValue("edge");
                 String id = recordEle.attributeValue("id");
                 if (edge != null) {
                     String aa = recordEle.attributeValue("source");
                     String bb = recordEle.attributeValue("target");
                     if (!StringUtils.isEmpty(aa) && !StringUtils.isEmpty(bb)) {
                         addNodeMap(nodeMap, aa, bb);
                         addNodeMap(nodeMap, bb, aa);
                         if(!ipMap.containsKey(aa+bb)) {
                         	ipMap.put(aa+bb, id);
                         }
                         
                     }
                 }else {
                	String value = recordEle.attributeValue("value");
                	if(null!= value && value.equals(pathRequest.getSourceIp())) {
                		sourceId =  id;
                	}
                	if(null!= value &&value.equals(pathRequest.getDestIp())) {
                		destId =  id;
                	}
                 }
             }
             
             LOGGER.info("***************************解析拓扑图结束***************************");
			if (sourceId == "" && destId == "") {
				return new ResMap(4, "源ip和目标ip不存在");
			} else if (sourceId == "") {
				return new ResMap(4, "源ip不存在");
			} else if (destId == "") {
				return new ResMap(4, "目标ip不存在");
			}
			 LOGGER.info("源id:{},目标id:{}",sourceId,destId);
			//每次循环的node id
			 List<String> startList = Lists.newArrayList();
	            startList.add(sourceId);
	            
	            List<String> nodeList = Lists.newArrayList();
	            //保存key value
	            Map<String,List<String>> map = Maps.newHashMap();
	            List<String> pList = Lists.newArrayList();
	            pList.add(sourceId);
	            map.put(sourceId, pList);
	            //每次循环的nodeid 对应的key
	            Map<String,List<String>> keyMap = Maps.newHashMap();
	            List<String> keyList = Lists.newArrayList();
	            keyList.add(sourceId);
	            keyMap.put(sourceId, keyList);
	            LOGGER.info("***************************开始检索链路***************************");
	           boolean flag =  getValueNew(nodeMap,startList,destId,nodeList,map,keyMap);
	           LOGGER.info("***************************检索链路结束,返回:{}",flag);
	            if(!flag) {
	            	return ResMap.success(pathList);
	            }
	            for(Entry<String,List<String>> en:map.entrySet()) {
	            	List<String> list = en.getValue();
	            	if(list.contains(destId)) {
	            		getId(ipMap,pathList,list);
	            	}
	            }
	            LOGGER.info("***************************返回链路：{}",pathList);
         } catch (Exception e) {
        	 LOGGER.error("解析topo图报错",e);
        	 throw e;
         }
         return ResMap.success(pathList);
		/*
		 * if(sourceId == "" && destId =="") { return new ResMap(4,"源ip和目标ip不存在"); }else
		 * if(sourceId == "" ) { return new ResMap(4,"源ip不存在"); }else if(destId == "" )
		 * { return new ResMap(4,"目标ip不存在"); } HashMap<String,List<String>> pathMap =
		 * Maps.newHashMap(); pathMap.put(sourceId, Lists.newArrayList()); List<String>
		 * noneList = Lists.newArrayList();//不通向目标节点的节点 try {
		 * getPathValue(nodeMap.get(sourceId),nodeMap.get(destId), nodeMap, pathMap,
		 * sourceId,noneList); }catch(Exception e) {
		 * 
		 * }
		 * 
		 * 
		 * List<String> ipList = Lists.newArrayList(); //过滤非目标链路
		 * for(Entry<String,List<String>> path:pathMap.entrySet()) { List<String> list =
		 * path.getValue(); if(list.contains(destId)) {
		 * 
		 * System.out.println(list); getId(ipMap,ipList,list); } }
		 * //System.out.println(ipList); LOGGER.info("目标链路id:{}",ipList); return
		 * ResMap.success(ipList);
		 */
    }
    

	//获取链路id
    private static void getId(Map<String, String> ipMap, List<String> ipList, List<String> list) {
		for(int i=0;i<list.size();i++) {
			if(list.size()<(i+2)) {
				break;
			}
			String aa = list.get(i);
			String bb = list.get(i+1);
			String ip1 = ipMap.get(aa+bb);
			String ip2 = ipMap.get(bb+aa);
			if(ip1!=null && !ipList.contains(ip1)) {
				ipList.add(ip1);
			}
			if(ip2!=null && !ipList.contains(ip2)) {
				ipList.add(ip2);
			}
		}
		
	}
   /***
    * 
    * @param nodeMap  node关系map
    * @param startList 当前需要循环的node id
    * @param end 目标节点
    * @param pathedList 解析过的node id
    * @param map key 和节点
    * @param keyMap 当前循环的node id和key的关系
    * @return
    */
    public static boolean getValueNew(Map<String, Node> nodeMap,List<String> startList,String end,List<String> pathedList
    		,Map<String,List<String>> map,Map<String,List<String>> keyMap) {
    	//System.out.println(startList);
    	boolean flag = false;
    	List<String> tempList = Lists.newArrayList();
    	Map<String,List<String>> tempMap = Maps.newHashMap();//存放id和key映射
    	for(int j=0;j<startList.size();j++) {
    		Node startNode = nodeMap.get(startList.get(j));
    		String startId = startNode.getCellId();
        	List<Node> list =  startNode.getRelationNodeList();
        	if(!pathedList.contains(startId)) {
    			pathedList.add(startId);
    		}
        
        	for(int i=0;i<list.size();i++) {
        		Node temp = list.get(i);
        		String tempId= temp.getCellId();
        		if(pathedList.contains(tempId)) {
        			continue;
        		}
        		
        		if(!tempList.contains(tempId)) {
        			tempList.add(tempId);
        		}
        		
        		List<String> keyTempList  = keyMap.get(startId);
        		List<String> newKeyTempList  = Lists.newArrayList();
        		for(String k:keyTempList) {
        			String key =  k+"oo_oo"+tempId;
        			List pathListTemp = Lists.newArrayList();
            		
        			List pathList = map.get(k);
        			pathListTemp.addAll(pathList);
        			if(!pathListTemp.contains(tempId)) {
            			pathListTemp.add(tempId);
            		}
            		if(!map.containsKey(key)) {
            			map.put(key, pathListTemp);
            		}
            		newKeyTempList.add(key);
            		
        		}
        		tempMap.put(tempId, newKeyTempList);
        		
        		if(tempId.equals(end)) {
        			flag = true;
        		}
        	}
        	
    	}
    	if(flag) {
    		return true;
    	}else {
    		if(tempList.size()==0) {
    			return false;
    		}
    		return getValueNew(nodeMap,tempList,end,pathedList,map,tempMap);
    	}
    	
    	//return false;
    }
    //获取链路
 public  boolean getPathValue(Node startNode, Node endNode, Map<String, Node> nodeMap,Map<String,List<String>> pathMap
		 ,String key,List<String> noneList) throws Exception {
	 
	// LOGGER.error("查找链路：{},:{}",startNode,key);
    	String startId = startNode.getCellId();
    	String endId = endNode.getCellId();
    	pathMap.get(key).add(startId);
    	if(startId == endId) {
    		//aaa.get(key).add("true");
    		return true;
    		
    	}else {
    		List<Node> list =  startNode.getRelationNodeList();
    		for(int i=0;i<list.size();i++) {
    			Node n = list.get(i);
    			//判断是否目标节点
        		if(n.getCellId().equals(endNode.getCellId())) {
        			if(endNode.getRelationNodeList().size() ==1) {
                		String key1 = key+i;
                		List copy = Lists.newArrayList();
                		copy.addAll(pathMap.get(key));
                		copy.add(n.getCellId());
                		pathMap.put(key1, copy);
        				return true;
        			}
        		}
    		}
        	for(int i=0;i<list.size();i++) {
        		Node n = list.get(i);
        		//返回链路中的某个节点,不能往下走了
        		if(pathMap.get(key).contains(n.getCellId())) {
        			continue;
        		}
				
				
        		Node s = nodeMap.get(n.getCellId());
        		
        		List<String> tempList = Lists.newArrayList();
        		if(noneList.contains(s.getCellId())) {
        			continue;
        		}
				
				if (!getValue(nodeMap, s.getCellId(), endNode.getCellId(), tempList)) {
					noneList.addAll(tempList);
					continue;
				}
				
        		String key1 = key+i;
        		List copy = Lists.newArrayList();
        		copy.addAll(pathMap.get(key));
        		pathMap.put(key1, copy);
        		//aaa.get(key1).add(n.getCellId());
        		getPathValue(s,endNode,nodeMap,pathMap,key1,noneList);
        		
        	}
    	}
    	return false;
    }
 //获取链路各节点关联
    private static void addNodeMap(Map<String, Node> nodeMap, String aa, String bb) {

        if (nodeMap.get(aa) == null) {
            Node node = new Node();
            node.setCellId(aa);
            List<Node> value = Lists.newArrayList();
            if (!bb.equals(aa)) {
                Node bbNode = new Node();
                bbNode.setCellId(bb);
                bbNode.setRelationNodeList(Lists.newArrayList());
                value.add(bbNode);
                node.setRelationNodeList(value);
                nodeMap.put(aa, node);
            }
        } else {
            if (!bb.equals(aa)) {
                Node node = nodeMap.get(aa);
                List<Node> value = node.getRelationNodeList();
                Set<String> nCellIdSet = value.stream().map(Node::getCellId).collect(Collectors.toSet());
                if (!nCellIdSet.contains(bb)) {
                    Node bbNode = new Node();
                    bbNode.setCellId(bb);
                    bbNode.setRelationNodeList(Lists.newArrayList());
                    value.add(bbNode);
                }
            }
        }
    }
    
    
    public static boolean getValue(Map<String, Node> nodeMap,String start,String end,List<String> noneList) {
    	//System.out.println("***********开始了***********");
    	if(noneList.size()==0) {
    		noneList = Lists.newArrayList();
    	}
    	if(noneList.contains(start)) {
    		return false;
    	}
    	
    	if(start.equals(end)) {
    		return true;
    	}
    	noneList.add(start);
    	Node s = nodeMap.get(start);
    	if(s.getRelationNodeList().size()>0) {
    		for(Node n:s.getRelationNodeList()) {
    			if(getValue(nodeMap, n.getCellId(), end, noneList)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
}
