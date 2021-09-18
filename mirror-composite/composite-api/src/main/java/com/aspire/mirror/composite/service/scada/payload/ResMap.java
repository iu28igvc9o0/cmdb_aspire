package com.aspire.mirror.composite.service.scada.payload;



/**
 * @ClassName: ResMap
 * @Description:接口返回结果
 * @author: JinSu
 * @date: 2018年3月19日 下午4:00:00
 * @Copyright: 2018 http://www.ttfs-edu.com
 */
public class ResMap
{
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回结果对象
     */
    private Object data;

    public ResMap()
    {
    }

    public ResMap(Integer code, String msg, Object data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResMap(Integer code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    /**
     * @author: JinSu
     * @date: 2018年3月22日 下午4:20:33
     * @Description: 成功
     * @param: @return
     */
    public static ResMap success()
    {
        return new ResMap(ResErrorEnum.SUCCESS.getCode(), ResErrorEnum.SUCCESS.getMsg(), null);
    }

    /**
     * @author: JinSu
     * @date: 2018年3月22日 下午4:20:46
     * @Description: 成功
     * @param: @param
     * data 返回数据
     * @param: @return
     */
    public static ResMap success(Object data)
    {
        return new ResMap(ResErrorEnum.SUCCESS.getCode(), ResErrorEnum.SUCCESS.getMsg(), data);
    }

    public static ResMap success(String msg, Object data)
    {
        return new ResMap(ResErrorEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * @author: JinSu
     * @date: 2018年3月22日 下午4:21:02
     * @Description: 失败
     */
    public static ResMap error()
    {
        return new ResMap(ResErrorEnum.ERROR.getCode(), ResErrorEnum.ERROR.getMsg(), null);
    }

    /**
     * @author: JinSu
     * @date: 2018年3月22日 下午4:21:02
     * @Description: 失败
     */
    public static ResMap error(Object data)
    {
        return new ResMap(ResErrorEnum.ERROR.getCode(), ResErrorEnum.ERROR.getMsg(), data);
    }

    /**
     * @author: JinSu
     * @date: 2018年3月22日 下午4:21:13
     * @Description: 失败
     */
    public static ResMap error(ResErrorEnum errorEnum, Object data)
    {
        return new ResMap(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    /**
     * @author: JinSu
     * @date: 2018年4月2日 上午10:15:06
     * @Description: 自定义错误信息
     */
    public static ResMap error(String msg, Object data)
    {
        return new ResMap(ResErrorEnum.ERROR.getCode(), msg, data);
    }

    /**
     * @author: JinSu
     * @date: 2018年4月2日 上午10:14:31
     * @Description: 返回null
     */
    public static ResMap nulls()
    {
        return new ResMap(ResErrorEnum.NULL.getCode(), ResErrorEnum.NULL.getMsg(), null);
    }

    public static ResMap notice(String msg, Object data)
    {
    return new ResMap(ResErrorEnum.NOTICE.getCode(), msg, data);
}

    public static ResMap warn(String msg, Object data)
    {
        return new ResMap(ResErrorEnum.WARN.getCode(), msg, data);
    }

    public static ResMap warn(Object data)
    {
        return new ResMap(ResErrorEnum.WARN.getCode(), ResErrorEnum.WARN.getMsg(), data);
    }

    public static ResMap lossParam()
    {
        return new ResMap(ResErrorEnum.NOTICE.getCode(), "参数缺省", null);
    }


    public static ResMap businessNotice(String msg, Object data)
    {
        return new ResMap(ResErrorEnum.BUSINESSNOTICE.getCode(), msg, data);
    }
}
