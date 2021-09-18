package com.aspire.ums.cmdb.common.handler;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: JSONTypeHandler
 * Author:   zhu.juwang
 * Date:     2019/3/12 18:49
 * Description: 处理数据库存放JSON类型字段
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class JSONTypeHandler<T extends Object> {
//    private static final ObjectMapper mapper = new ObjectMapper();
//    private Class<T> clazz;
//
//    public JSONTypeHandler(Class<T> clazz) {
//        if (clazz == null) throw new IllegalArgumentException("Type argument cannot be null");
//        this.clazz = clazz;
//    }
//
//    @Override
//    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
//        preparedStatement.setString(i, this.toJson(t));
//    }
//
//    @Override
//    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
//        return this.toObject(resultSet.getString(s), clazz);
//    }
//
//    @Override
//    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
//        return this.toObject(resultSet.getString(i), clazz);
//    }
//
//    @Override
//    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
//        return this.toObject(callableStatement.getString(i), clazz);
//    }
//
//    private String toJson(T object) {
//        try {
//            return mapper.writeValueAsString(object);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private T toObject(String content, Class<?> clazz) {
//        if (content != null && !content.isEmpty()) {
//            try {
//                return (T) mapper.readValue(content, clazz);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            return null;
//        }
//    }
//
//    static {
//        mapper.configure(Feature.WRITE_NULL_MAP_VALUES, false);
//        mapper.setSerializationInclusion(Inclusion.NON_NULL);
//    }
}
