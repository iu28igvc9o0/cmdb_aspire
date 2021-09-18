/**
 * 
 */
package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.Dict;
import com.aspire.ums.cmdb.sync.entity.SyncEntity;

/**
 * @author lupeng
 *
 */
public interface SyncService {
	
	List<SyncEntity> syncEntity();
	
	
	List<Dict> syncDicts();
	
}
