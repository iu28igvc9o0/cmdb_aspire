package com.aspire.mirror.alert.server.util;

import java.util.*;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class Criteria {
    protected String orderByClause;

    protected boolean distinct;

    protected String locale;

    protected List<Condition> oredCriteria;

    protected String exportField;


    /**
     * @Fields page : 分页查询的信息
     */
//    protected Page page;
    private Integer begin;
    private Integer pageSize;

    public Criteria() {
        oredCriteria = new ArrayList<Condition>();
    }

    /**
     * <p>
     * Title: 构造函数
     * </p>
     * <p>
     * Description: 带分页构造函数，tapetry分页grid用
     * </p>
     *
     * @param pageNum 分页参选
     */
    public Criteria(Integer pageNum, Integer pageSize) {
        oredCriteria = new ArrayList<Condition>();
        this.begin = begin;
        this.pageSize = pageSize;
    }


    public void or(final Condition condition) {
        oredCriteria.add(condition);
    }

    public Condition or() {
        final Condition condition = createConditionInternal();
        oredCriteria.add(condition);
        return condition;
    }

    public Condition createConditon() {
        final Condition condition = createConditionInternal();
        if (oredCriteria.isEmpty()) {
            oredCriteria.add(condition);
        }
        return condition;
    }

    protected Condition createConditionInternal() {
        final Condition condition = new Condition();
        return condition;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
        begin = null;
        pageSize = null;
    }

    public static class Condition {
        protected List<Criterion> criterions = new ArrayList<Criterion>();

        public boolean isValid() {
            return !criterions.isEmpty();
        }

        public List<Criterion> getCriterions() {
            return criterions;
        }

        protected void addCriterion(final String condition) {
            if (condition == null) {
                // throw new
                // RuntimeException("Value for condition cannot be null");
                return;
            }
            criterions.add(new Criterion(condition));
        }

        protected void addCriterion(final String condition, final Object value, final String property) {
            if (value == null) {
                // throw new RuntimeException("Value for " + property +
                // " cannot be null");
                return;
            }
            criterions.add(new Criterion(condition, value));
        }

        protected void addCriterion(final String condition, final Object value1, final Object value2, final String property) {
            if (value1 == null || value2 == null) {
                // throw new RuntimeException("Between values for " + property +
                // " cannot be null");
                return;
            }
            criterions.add(new Criterion(condition, value1, value2));
        }

        public Condition andIsNull(final String columnName) {
            addCriterion(columnName + " is null");
            return this;
        }

        public Condition andNoValue(final String condition) {
            addCriterion(condition);
            return this;
        }

        public Condition andIsNotNull(final String columnName) {
            addCriterion(columnName + " is not null");
            return this;
        }

        public Condition andEqualTo(final String columnName, final Object value) {
            addCriterion(columnName + " =", value, columnName);
            return this;
        }

        public Condition andNotEqualTo(final String columnName, final Object value) {
            addCriterion(columnName + " <>", value, columnName);
            return this;
        }

        public Condition andGreaterThan(final String columnName, final Object value) {
            addCriterion(columnName + " >", value, columnName);
            return this;
        }

        public Condition andGreaterThanOrEqualTo(final String columnName, final Object value) {
            addCriterion(columnName + " >=", value, columnName);
            return this;
        }

        public Condition andLessThan(final String columnName, final Object value) {
            addCriterion(columnName + " <", value, columnName);
            return this;
        }

        public Condition andLessThanOrEqualTo(final String columnName, final Object value) {
            addCriterion(columnName + " <=", value, columnName);
            return this;
        }

        public Condition andBetween(final String columnName, final Object value1, final Object value2) {
            addCriterion(columnName + " between", value1, value2, columnName);
            return this;
        }

        public Condition andNotBetween(final String columnName, final Object value1, final Object value2) {
            addCriterion(columnName + " not between", value1, value2, columnName);
            return this;
        }

        public Condition andIn(final String columnName, final List<Object> values) {
            if (values != null && !values.isEmpty()) {
                addCriterion(columnName + " in", values, columnName);
            } else {
                addCriterion("1 != 1");
            }
            return this;
        }

        public Condition andNotIn(final String columnName, final List<Object> values) {
            if (values != null && !values.isEmpty()) {
                addCriterion(columnName + " not in", values, columnName);
            }
            return this;
        }

        public Condition andLeftLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " like", value + "%", columnName);
            }
            return this;
        }

        public Condition andRightLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " like", "%" + value, columnName);
            }
            return this;
        }

        public Condition andLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " like", "%" + value + "%", columnName);
            }
            return this;
        }

        public Condition andLeftNotLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " not like", value + "%", columnName);
            }
            return this;
        }

        public Condition andRightNotLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " not like", "%" + value, columnName);
            }
            return this;
        }

        public Condition andNotLike(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion(columnName + " not like", "%" + value + "%", columnName);
            }
            return this;
        }

        public Condition andLikeInsensitive(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion("upper(" + columnName + ") like", value == null ? null : "%" + value.toUpperCase() + "%", columnName);
            }
            return this;
        }

        public Condition andLeftLikeInsensitive(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion("upper(" + columnName + ") like", value == null ? null : value.toUpperCase() + "%", columnName);
            }
            return this;
        }

        public Condition andRightLikeInsensitive(final String columnName, final String value) {
            if (StringUtils.isNotBlank(value)) {
                addCriterion("upper(" + columnName + ") like", value == null ? null : "%" + value.toUpperCase(), columnName);
            }
            return this;
        }
    }

    public static class Criterion {
        private final String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private boolean dateValue;

        private final String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public boolean isDateValue() {
            return dateValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(final String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(final String condition, final Object value, final String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else if (value instanceof Date) {
                this.dateValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(final String condition, final Object value) {
            this(condition, value, null);
        }

        protected Criterion(final String condition, final Object value, final Object secondValue, final String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(final String condition, final Object value, final Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
    private Map<String, Object> params = new HashMap<String, Object>();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}