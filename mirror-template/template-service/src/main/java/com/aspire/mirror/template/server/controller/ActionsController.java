package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.ActionsCreateRequest;
import com.aspire.mirror.template.api.dto.ActionsCreateResponse;
import com.aspire.mirror.template.api.dto.ActionsUpdateRequest;
import com.aspire.mirror.template.api.dto.ActionsUpdateResponse;
import com.aspire.mirror.template.api.dto.model.ActionsDTO;
import com.aspire.mirror.template.api.dto.vo.ActionsVO;
import com.aspire.mirror.template.api.service.ActionsService;
import com.aspire.mirror.template.server.biz.ActionsBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 动作控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   ActionsController.java
 * 类描述:   动作业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "ActionsCache")
public class ActionsController implements ActionsService {

    /**
     * 创建动作信息
     *
     * @param actionsCreateRequest 动作创建请求对象
     * @return ActionsCreateResponse 动作创建响应对象
     */
    public ActionsCreateResponse createdActions(@RequestBody final ActionsCreateRequest actionsCreateRequest) {
        if (null == actionsCreateRequest) {
            LOGGER.error("created param actionsCreateRequest is null");
            throw new RuntimeException("actionsCreateRequest is null");
        }
        ActionsDTO actionsDTO = new ActionsDTO();
        BeanUtils.copyProperties(actionsCreateRequest, actionsDTO);
        String actionId = actionsBiz.insert(actionsDTO);
        ActionsCreateResponse actionsCreateResponse = new ActionsCreateResponse();
        BeanUtils.copyProperties(actionsDTO, actionsCreateResponse);
        actionsCreateResponse.setActionId(actionId);
        return actionsCreateResponse;
    }

    /**
     * 根据主键删除单条动作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("action_id") final String actionId) {
        try {
            actionsBiz.deleteByPrimaryKey(actionId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键删除多条动作信息
     *
     * @param actionIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("action_ids") final String actionIds) {
        try {
            if (StringUtils.isEmpty(actionIds)) {
                throw new RuntimeException("actionIds is null");
            }
            String[] actionIdArrays = actionIds.split(",");
            actionsBiz.deleteByPrimaryKeyArrays(actionIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除错误！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改动作信息
     *
     * @param actionsUpdateRequest 动作修改请求对象
     * @return ActionsUpdateResponse 动作修改响应对象
     */
    public ActionsUpdateResponse modifyByPrimaryKey(@PathVariable("action_id") final String actionId, @RequestBody
    final ActionsUpdateRequest actionsUpdateRequest) {
        ActionsDTO actionsdTO = new ActionsDTO();
        BeanUtils.copyProperties(actionsUpdateRequest, actionsdTO);
        actionsdTO.setActionId(actionId);
        actionsBiz.updateByPrimaryKey(actionsdTO);
        ActionsDTO findActionsDTO = actionsBiz.selectByPrimaryKey(actionId);
        ActionsUpdateResponse actionsUpdateResponse = new ActionsUpdateResponse();
        BeanUtils.copyProperties(findActionsDTO, actionsUpdateResponse);

        return actionsUpdateResponse;
    }

    /**
     * 根据主键查找动作详情信息
     *
     * @param actionId 动作主键
     * @return ActionsVO 动作详情响应对象
     */
    public ActionsVO findByPrimaryKey(@PathVariable("action_id") final String actionId) {
        if (StringUtils.isEmpty(actionId)) {
            LOGGER.warn("findByPrimaryKey param actionId is null");
            return null;
        }
        ActionsDTO actionsDTO = actionsBiz.selectByPrimaryKey(actionId);
        ActionsVO actionsVO = new ActionsVO();
        if (null != actionsDTO) {
            BeanUtils.copyProperties(actionsDTO, actionsVO);
        }

        return actionsVO;
    }

    /**
     * 根据主键查询动作集合信息
     *
     * @param actionIds 动作主键
     * @return ActionsVO 动作查询响应对象
     */
    public List<ActionsVO> listByPrimaryKeyArrays(@PathVariable("action_id") final String actionIds) {
        if (StringUtils.isEmpty(actionIds)) {
            LOGGER.error("listByPrimaryKeyArrays param actionIds is null");
            return Lists.newArrayList();
        }
        String[] actionIdArrays = actionIds.split(",");
        List<ActionsDTO> listActionsDTO = actionsBiz.selectByPrimaryKeyArrays(actionIdArrays);
        List<ActionsVO> listActionsVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listActionsDTO)) {
            for (ActionsDTO actionsDTO : listActionsDTO) {
                ActionsVO actionsVO = new ActionsVO();
                BeanUtils.copyProperties(actionsDTO, actionsVO);
                listActionsVO.add(actionsVO);
            }
        }
        return listActionsVO;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsController.class);

    @Autowired
    private ActionsBiz actionsBiz;

}