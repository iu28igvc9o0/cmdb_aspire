package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import com.migu.tsg.microservice.atomicservice.composite.common.KeyValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;


/**
* Controller分页工具类
* Project Name:composite-service
* File Name:PaginateUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller
* ClassName: PaginateUtil <br/>
* date: 2017年8月28日 上午10:58:49 <br/>
* Controller分页工具类
* @author pengguihua
* @version
* @since JDK 1.6
*/
public class PaginateUtil {
    /**
     * 从请求中提取分页信息, 返回pageNum和pageSize. <br/>
     *
     * 作者： pengguihua
     * @param authCtx
     * @return
     */
     public static KeyValue<Integer, Integer> resolveRequestPageInfo(RequestAuthContext authCtx) {
         int currPage = 1;    // default if the request has no "page" parameter
         int pageSize = 20;   // default if the request has no "page_size" parameter
         
         String paramPage = authCtx.getParameterSingleValue("page");
         paramPage = paramPage == null ? authCtx.getParameterSingleValue("pageno") : paramPage;
         String paramPageSize = authCtx.getParameterSingleValue("page_size");
         paramPageSize = paramPageSize == null ? authCtx.getParameterSingleValue("size") : paramPageSize;
         
         if (NumberUtils.isDigits(paramPage)) {
             currPage = Integer.parseInt(paramPage);
             //currPage = Integer.valueOf(paramPage);
         }
         if (NumberUtils.isDigits(paramPageSize)) {
             pageSize = Integer.parseInt(paramPageSize);
             //pageSize = Integer.valueOf(paramPageSize);
             pageSize = pageSize <= 0 ? -1 : pageSize;
         }
         return new KeyValue<Integer, Integer>(currPage, pageSize);
     }

     /**
     * 返回响应中分页结果的总页数
     *
     * 作者： pengguihua
     * @param totalCount
     * @return
     */
     public static int resolveResultPageCount(RequestAuthContext authCtx, int totalCount) {
         KeyValue<Integer, Integer> pageMeta = resolveRequestPageInfo(authCtx);
         int pageSize = pageMeta.getValue();
         return totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
     }

     /**
      * 获取分页数据.<br/>
      * 作者： pengguihua
      * @param authCtx
      * @param resList
      * @return
      */
      public static <T> List<T> getPaginateQuerySet(RequestAuthContext authCtx, List<T> resList) {
          if (resList == null || resList.isEmpty()) {
              return resList;
          }
          KeyValue<Integer, Integer> pageMeta = PaginateUtil.resolveRequestPageInfo(authCtx);
          int currPage = pageMeta.getKey();
          int pageSize = pageMeta.getValue();
          // pageSize小于等于0，表示数据不做分页处理
          if (pageSize <= 0) { 
              return resList;
          }
          int totalCount = resList.size();
          int startIdx = (currPage - 1) * pageSize;
          startIdx = startIdx > totalCount - 1 ? 0 : startIdx;
          int endIdx = startIdx + pageSize;
          endIdx = endIdx > totalCount ? totalCount : endIdx;

          return resList.subList(startIdx, endIdx);
      }
}
