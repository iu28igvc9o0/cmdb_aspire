package com.migu.tsg.microservice.atomicservice.composite.controller.handler;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.DepartmentServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserPayload;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.DepartmentVO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.handler
 * 类名称:    UserExcelHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/4/29 17:32
 * 版本:      v1.0
 */
@Component
public class UserExcelHandler extends ExcelDataHandlerDefaultImpl<UserPayload> {
    Logger logger = LoggerFactory.getLogger(UserExcelHandler.class);

    public UserExcelHandler(DepartmentServiceClient departmentClient, UserServiceClient userClient) {
        this.departmentClient = departmentClient;
        this.userClient = userClient;
    }
    private final DepartmentServiceClient departmentClient;
    private final UserServiceClient userClient;
    @Override
    public Object importHandler(UserPayload user, String name, Object value) {
        logger.info("pending item : {}", value);
        if (name.equals("部门") && !StringUtils.isEmpty((String) value)) {
            DepartmentVO departmentVO = departmentClient.findByName((String) value);
            if (departmentVO != null) {
                value = departmentVO.getUuid();
            } else {
                value = "";
            }
        } else if (name.equals("关联人")) {
            if (user.getUserType().equals("1")) {
                value = "";
            } else {
                if (!StringUtils.isEmpty((String) value)) {
                    UserVO userVO = userClient.findByCode((String) value);
                    if (userVO != null) {
                        value = userVO.getUuid();
                    }
                }
            }
        }
        return super.importHandler(user, name, value);
    }
}
