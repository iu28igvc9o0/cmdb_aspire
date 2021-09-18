/**
 * 
 */
package com.aspire.ums.cmdb.sync.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.sync.entity.Dict;
import com.aspire.ums.cmdb.sync.entity.SyncEntity;
import com.aspire.ums.cmdb.sync.service.SyncService;

/**
 * @author lupeng
 *
 */
@RestController
@RequestMapping("/cmdb/sync")
public class SyncController {
	
	@Autowired
	private SyncService syncService;
	
	@RequestMapping("/syncEntities")
	public List<SyncEntity> syncEntities(){
		List<SyncEntity> entities = syncService.syncEntity();
		return entities;
	}
	
	@RequestMapping("/syncDicts")
	public List<Dict> syncDicts(){
		List<Dict> dicts = syncService.syncDicts();
		return dicts;
	}
	
	
	
}