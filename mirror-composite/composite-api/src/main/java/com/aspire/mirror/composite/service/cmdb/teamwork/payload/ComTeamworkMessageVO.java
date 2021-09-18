package com.aspire.mirror.composite.service.cmdb.teamwork.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:13
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTeamworkMessageVO {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String twTeamworkId;
    /**
     * teamwork_user_manager ID
     */
    private String twSendUserAccount;
    /**
     * 
     */
    private String twSendTime;
    /**
     * 
     */
    private String twLabelId;
    /**
     * 
     */
    private String twSendContent;
    /**
     * 消息类型(@圈圈/普通/引用, @圈圈只有指定人查看)
     */
    private String twMessageType;
    /**
     * 引用消息ID
     */
    private String twRefMessageId;
    /**
     * 内容类型(文本/图片/视频等
     */
    private String twContentType;
    /**
     * 
     */
    private String contentUrl;
    /**
     * 
     */
    private int isDelete;
}