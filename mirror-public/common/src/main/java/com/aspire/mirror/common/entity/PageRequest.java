package com.aspire.mirror.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.entity
 * 类名称:    PageRequest.java
 * 类描述:    保存条数、第几页、排序规则、查询的起始时间和结束时间等分页请求信息
 * 创建人:    JinSu
 * 创建时间:  2018/7/28 16:03
 * 版本:      v1.0
 */
    public class PageRequest implements Serializable {

        private static final long serialVersionUID = 4285267921806851030L;

        /** 每页条数 */
        @JsonProperty("num_per_page")
        private Integer pageSize = 20;

        /** 第几页 */
        @JsonProperty("page")
        private int pageNo = 1;

        /** 排序的字段 */
        private String sortName;

        /** 排序类型 */
        private String order;

        /** 组合排序字段,会加 order dir 后 */
        private String assembleOrderBy;

        /** 起始时间 */
        private Date startTime;

        /** 结束时间 */
        private Date endTime;

        private Map<String, Object> dynamicQueryFields = new HashMap<String, Object>();

        /**
         * @param fieldName fieldName
         * @param fieldValue fieldValue
         */
        public void addFields(final String fieldName, final Object fieldValue) {
            dynamicQueryFields.put(fieldName, fieldValue);
        }


        /**
         * @return Integer
         */
        public Integer getPageSize() {
            return pageSize;
        }

        /**
         * @param pageSize pageSize
         */
        public void setPageSize(Integer pageSize) {
            if (null != pageSize) {
                this.pageSize = pageSize;
            }
        }

        /**
         * @return Integer
         */
        public Integer getPageNo() {
            return pageNo;
        }

        /**
         * @param pageNo pageNo
         */
        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        /**
         * @return Date
         */
        public Date getStartTime() {
            if (this.startTime == null) {
                return null;
            } else {
                return (Date) startTime.clone();
            }
        }

        /**
         * @param startTime startTime
         */
        public void setStartTime(Date startTime) {
            this.startTime = (Date) startTime.clone();
        }

        /**
         * @return endTime endTime
         */
        public Date getEndTime() {
            if (this.endTime == null) {
                return null;
            } else {
                return (Date) endTime.clone();
            }
        }

        /**
         * @param endTime endTime
         */
        public void setEndTime(Date endTime) {
            this.endTime = (Date) endTime.clone();
        }
        /**
         * @return Map<String, Object>
         */
        public Map<String, Object> getDynamicQueryFields() {
            return dynamicQueryFields;
        }
        /**
         * @return String
         */
        public String getSortName() {
            return sortName;
        }

        /**
         * @param sortName sortName
         */
        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        /**
         * @return String
         */
        public String getOrder() {
            return order;
        }

        /**
         * @param order order
         */
        public void setOrder(String order) {
            this.order = order;
        }

        /**
         * @return String
         */
        public String getAssembleOrderBy() {
            return assembleOrderBy;
        }

        /**
         * @param assembleOrderBy assembleOrderBy
         */
        public void setAssembleOrderBy(String assembleOrderBy) {
            this.assembleOrderBy = assembleOrderBy;
        }

        /**
         * 降序
         * @param sort  排序字段
         * @return PageRequest
         */
        public PageRequest desc(String sort) {
            this.descSet(sort);
            return this;
        }

        /**
         * 降序
         * @param sort
         */
        protected void descSet(String sort) {
            if (!StringUtils.isEmpty(sort)) {
                this.sortName = sort;
                this.order = "DESC";
            }
        }

        /**
         * 升序
         * @param sort 排序字段
         * @return PageRequest
         */
        public PageRequest asc(String sort) {
            this.ascSet(sort);
            return this;
        }

        /**
         * @param sort 排序字段
         */
        protected void ascSet(String sort) {

            if (!StringUtils.isEmpty(sort)) {
                this.sortName = sort;
                this.order = "ASC";
            }
        }

        /**
         * 设置第几页
         * @param sPageNo 第几页
         * @return PageRequest
         */
        public PageRequest stringPageNo(String sPageNo) {
            this.pageNoSet(sPageNo);
            return this;
        }

        /**
         * 设置第几页
         * @param sPageNo 第几页
         */
        protected void pageNoSet(String sPageNo) {
            try {
                this.pageNo = Integer.parseInt(sPageNo);
            } catch (Exception e) {
                // do nothing
                this.pageNo = 1;
            }
        }

        /**
         * 设置第几页和按字段{sort}进行降序
         * @param sort  排序字段
         * @param sPageNo 第几页
         * @return PageRequest
         */
        public PageRequest pageNoAndDesc(final String sort, final String sPageNo) {
            this.descSet(sort);
            this.pageNoSet(sPageNo);
            return this;
        }

        /**
         * 设置第几页和按字段{sort}进行升序
         * @param sort 排序字段
         * @param sPageNo 第几页
         * @return PageRequest
         */
        public PageRequest pageNoAndAsc(final String sort, final String sPageNo) {
            this.ascSet(sort);
            this.pageNoSet(sPageNo);
            return this;
        }
}
