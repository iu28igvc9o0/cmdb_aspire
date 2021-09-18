package com.aspire.ums.cmdb.code.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:38
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCodeGroup {
    private String groupName;
    private List<CmdbCode> codeList;
}