package com.aspire.ums.cmdb.sync.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/6/12 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbSyncModuleCondition {

    private String moduleType;

    private String moduleKey;

    private String moduleFieldType;

    private String moduleFiledKey;
}
