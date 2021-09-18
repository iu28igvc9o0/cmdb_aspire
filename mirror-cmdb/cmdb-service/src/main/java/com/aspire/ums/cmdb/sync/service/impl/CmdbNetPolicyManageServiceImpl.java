package com.aspire.ums.cmdb.sync.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.ums.cmdb.sync.mapper.CmdbNetPolicyManageMapper;
import com.aspire.ums.cmdb.sync.payload.PublicNetAndIntranetIPDTO;
import com.aspire.ums.cmdb.sync.service.ICmdbNetPolicyManageService;
import com.aspire.ums.cmdb.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CmdbNetPolicyManageServiceImpl implements ICmdbNetPolicyManageService {

	@Autowired
	private CmdbNetPolicyManageMapper mapper;

	@Override
	public void batchInsertAndDelOldData(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList) {
		// 分批次插入
		if (publicAndInnerIpList.size() > 1000) {
			List<PublicNetAndIntranetIPDTO> list = new ArrayList<>();
			for (int i = 0; i < publicAndInnerIpList.size(); i++) {
				list.add(publicAndInnerIpList.get(i));
				if (i % 1000 == 0) {
					mapper.batchInsert(list);
					list.clear();
				}
			}
			if (CollectionUtils.isNotEmpty(list)) {
				mapper.batchInsert(list);
			}
		} else {
			mapper.batchInsert(publicAndInnerIpList);
		}
		mapper.delOldData();
	}

	@Override
	public void updatePublicIPSurvivalStatus(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList) {

		// 根据公网ip查询公网ip地址库的id、首次存活时间、最近存活时间
		List<Map<String, String>> survivalInfolist = mapper.getIpRepositoryPublicIpSurvivalInfo(publicAndInnerIpList);
		if (CollectionUtils.isEmpty(survivalInfolist)) {
			log.info("ip地址库->公网ip没有匹配数据，此次公网ip同步不更新公网ip存活状态");
			return;
		}
		log.info("更新ip地址库->公网ip存活状态->数量为：{}", survivalInfolist.size());
		String currentTime = DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT);
		survivalInfolist.forEach(m -> {
			if (StringUtils.isEmpty(m.get("first_survival_time"))) {
				m.put("first_survival_time", currentTime);
			}
			m.put("latest_survival_time", currentTime);
		});
		// 更新公网ip地址库的存活状态
		mapper.updatePublicIpSurvivalStatus(survivalInfolist);
		// TODO 更新操作日志
	}

}
