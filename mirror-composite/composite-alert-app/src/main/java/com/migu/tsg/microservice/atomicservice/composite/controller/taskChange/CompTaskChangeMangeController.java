package com.migu.tsg.microservice.atomicservice.composite.controller.taskChange;

import com.aspire.mirror.alert.api.dto.taskChange.TaskQueryRequest;
import com.aspire.mirror.alert.api.dto.taskChange.TaskRequest;
import com.aspire.mirror.composite.payload.taskChange.CompTaskDetail;
import com.aspire.mirror.composite.payload.taskChange.CompTaskQueryRequest;
import com.aspire.mirror.composite.payload.taskChange.CompTaskRequest;
import com.aspire.mirror.composite.service.taskChange.ICompTaskChangeManageService;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.taskChange.TaskChangeManageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.constant.AlertConfigConstants;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.UserHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.CompUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CompTaskChangeMangeController implements ICompTaskChangeManageService {

    @Autowired
    private TaskChangeManageServiceClient client;
    @Autowired
    private UserHelper userHelper;

    private String getUserName() {
        RequestAuthContext requestAuthContext = RequestAuthContext.currentRequestAuthContext();
        CompUserVo user = userHelper.findByLdapId(requestAuthContext.getUser().getUsername());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(user.getName()).append("(").append(user.getMobile()).append(")");
        return stringBuffer.toString();
    }

    @Override
    public ResponseEntity<String> addTask(@RequestBody CompTaskRequest compTaskRequest) {
        if (null == compTaskRequest) {
            log.error("[Task Change Manage] >>> addTask Request is null");
            throw new RuntimeException("addTask Request is null");
        }
        log.info("[Task Change Manage] >>> addTask Request is {}", compTaskRequest);
        compTaskRequest.setCreator(getUserName());
        String uuid = "";
        try {
            uuid = client.addTask(PayloadParseUtil.jacksonBaseParse(TaskRequest.class, compTaskRequest));
        } catch (Exception e) {
            log.error("[Task Change Manage] >>> addTask Error is {}",e);
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(uuid, HttpStatus.OK);
    }

    @Override
    public CompTaskDetail getTaskDetail(@RequestParam("uuid") String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("[Task Change Manage] >>> getTaskDetail Request UUID is null");
            throw new RuntimeException("getTaskDetail Request UUID is null");
        }
        return PayloadParseUtil.jacksonBaseParse(CompTaskDetail.class, client.getTaskDetail(uuid));
    }

    @Override
    public ResponseEntity<String> updateTask(@RequestBody CompTaskRequest compTaskRequest) {
        if (null == compTaskRequest) {
            log.error("[Task Change Manage] >>> updateTask Request is null");
            throw new RuntimeException("updateTask Request is null");
        }
        if (StringUtils.isEmpty(compTaskRequest.getUuid())) {
            log.error("[Task Change Manage] >>> updateTask Request UUID is null");
            throw new RuntimeException("updateTask Request UUID is null");
        }
        log.info("[Task Change Manage] >>> updateTask Request is {}", compTaskRequest);
        compTaskRequest.setUpdater(getUserName());
        client.updateTask(PayloadParseUtil.jacksonBaseParse(TaskRequest.class,compTaskRequest));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteTask(@RequestParam("uuid") String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("[Task Change Manage] >>> deleteTask Request is null");
            throw new RuntimeException("deleteTask Request is null");
        }
        client.deleteTask(getUserName(),uuid);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> editTaskMessage(@RequestBody Map<String, String> request) {
        if (StringUtils.isEmpty(request.get("uuid"))) {
            log.error("[Task Change Manage] >>> editTaskMessage Request UUID is null");
            throw new RuntimeException("editTaskMessage Request UUID is null");
        }
        if (StringUtils.isEmpty(request.get("content"))) {
            log.error("[Task Change Manage] >>> editTaskMessage Content is null");
            throw new RuntimeException("editTaskMessage Content is null");
        }
        log.info("[Task Change Manage] >>> editTaskMessage Request is {}", request);
        request.put("userName",getUserName());
        client.editTaskMessage(request);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> startTask(@RequestBody CompTaskRequest compTaskRequest) {
        if (null == compTaskRequest) {
            log.error("[Task Change Manage] >>> startTask Request is null");
            throw new RuntimeException("startTask Request is null");
        }
        if (StringUtils.isEmpty(compTaskRequest.getUuid())) {
            log.error("[Task Change Manage] >>> startTask Request UUID is null");
            throw new RuntimeException("startTask Request UUID is null");
        }
        log.info("[Task Change Manage] >>> startTask Request is {}", compTaskRequest);
        compTaskRequest.setUpdater(getUserName());
        client.startTask(PayloadParseUtil.jacksonBaseParse(TaskRequest.class,compTaskRequest));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> stopTask(@RequestBody Map<String, String> request) {
        if (StringUtils.isEmpty(request.get("uuid"))) {
            log.error("[Task Change Manage] >>> stopTask Request UUID is null");
            throw new RuntimeException("stopTask Request UUID is null");
        }
        request.put("userName",getUserName());
        client.stopTask(request);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public List<Map<String, Object>> getTaskActionList(@RequestParam("uuid") String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("[Task Change Manage] >>> getTaskActionList Request UUID is null");
            throw new RuntimeException("getTaskActionList Request UUID is null");
        }
        return client.getTaskActionList(uuid);
    }

    @Override
    public Map<String, Object> getTaskMessageList(@RequestParam("uuid") String uuid,
                                                  @RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("[Task Change Manage] >>> getTaskActionList Request UUID is null");
            throw new RuntimeException("getTaskActionList Request UUID is null");
        }
        return client.getTaskMessageList(uuid,pageNum,pageSize);
    }
//    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    @Override
    public void exportTask(@RequestBody Map<String, String> request, HttpServletResponse response) {
        try {
            log.info("[Task Change Manage] >>> exportTask Request is {}", request);
            List<Map<String, Object>> dataList = client.exportTask(request);
            String[] headerList = {"任务名称","操作类型","任务描述","资源池","计划执行开始时间","计划执行结束时间","任务状态","任务结果","失败原因","工单号","反馈意见","创建时间"};
            String[] keyList = {"task_name","task_type","task_description","idc_type","task_start_time","task_end_time","task_status","task_result","content","order_id","message_count","create_time"};
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String title = "计划列表_" + sDateFormat.format(new Date());
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
            book.write(os);
            os.flush();
            os.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<String> taskNotify(@RequestParam("uuid") String uuid) {
        return null;
    }

    @Override
    public Map<String, Object> getTaskList(@RequestBody CompTaskQueryRequest compTaskQueryRequest) {
        return client.getTaskList(PayloadParseUtil.jacksonBaseParse(TaskQueryRequest.class,compTaskQueryRequest));
    }

    @Override
    public void downloadTaskTemplate(HttpServletResponse response) {
        try {
            List<Map<String, Object>> dataList = Lists.newArrayList();
            String[] headerList = {"任务名称","操作类型","任务描述","资源池","计划执行开始时间","计划执行结束时间"};
            String[] keyList = {"task_name","task_type","task_description","idc_type","task_start_time","task_end_time"};
            String title = "任务模板";
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
            book.write(os);
            os.flush();
            os.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<String> importTaskTemplate(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (null == file) {
            log.error("文件不能为空！");
            throw new RuntimeException("文件不能为空！");
        }
        log.info("上传文件名称 -> {}", file.getOriginalFilename());
        if (!file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$") &&
                !file.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
            log.error("文件必须是excel格式！");
            throw new RuntimeException("文件必须是excel格式！");
        }
        log.info("上传文件大小 -> {}", file.getSize());
        if (file.getSize() == 0) {
            log.error("文件不能为空！");
            throw new RuntimeException("文件不能为空！");
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = file.getInputStream();
            if (file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //获取sheet
            Sheet sheet = wb.getSheetAt(0);
            //获取最大行数
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                log.error("Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                throw new RuntimeException("Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
            }
            //获取第一行
            Row row = sheet.getRow(0);
            //获取最大列数
            int col = row.getPhysicalNumberOfCells();
            Map<String, String> taskTemplate = AlertConfigConstants.TASK_TEMPLATE;
            for (int c=0;c<col;c++){
                String cell = row.getCell(c).toString();
                if (StringUtils.isEmpty(taskTemplate.get(cell))) {
                    log.error("Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                    throw new RuntimeException("Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                }
            }
            for (int i = 1; i<totalRows; i++) {
                Row cells = sheet.getRow(i);
                Map<String,String> map = Maps.newHashMap();
                if(cells !=null){
                    for (int j=0;j<col;j++){
                        log.info("cell >>> {}", row.getCell(j).toString());
                        String stringCellValue = getCellFormatValue(cells.getCell(j));
                        map.put(taskTemplate.get(row.getCell(j).toString()), stringCellValue);
                    }
                } else {
                    break;
                }
                map.put("creator",getUserName());
                log.info("map >>> {}", map);
                client.addTask(PayloadParseUtil.jacksonBaseParse(TaskRequest.class,map));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    private static String getCellFormatValue(Cell cell){
        String cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    if(DateUtil.isCellDateFormatted(cell)){
                        //用于转化为日期格式
                        Date d = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = sdf.format(d);
                    } else {
                        cellValue = String.valueOf((int)cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue =  String.valueOf(cell.getDateCellValue());
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
