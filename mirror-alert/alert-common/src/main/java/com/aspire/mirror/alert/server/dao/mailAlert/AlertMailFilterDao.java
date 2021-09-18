package com.aspire.mirror.alert.server.dao.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilter;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMailFilterDao {

    List<AlertMailFilter> selectFilterByReceiver(@Param(value = "receiver") String receiver);

    void insertMailFilter(AlertMailFilter mailFilter);

    void deleteMailFilter(@Param("ids") List<String> idList);

    AlertMailFilter selectMailFilterById(@Param("id") String id);
    
    AlertMailFilter selectMailFilterByRecipientId(@Param("recipientId") String recipientId,
    		@Param("name") String name,@Param("id") String id);

    List<AlertMailFilter> selectMailFilterList(Page page);
    int countMailFilterList(Page page);

    void updateMailFilter(AlertMailFilter mailFilter);
}
