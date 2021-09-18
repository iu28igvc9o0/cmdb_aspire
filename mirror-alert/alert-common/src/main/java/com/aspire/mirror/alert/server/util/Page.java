/**
 * @Title: Page.java
 * @Package com.ailk.sysframe.dao.components
 * @Description: 分页组件的页面信息
 * @author linyl linyuliang.85@gmail.com
 * @version V1.0
 */
package com.aspire.mirror.alert.server.util;

/**
* @ClassName: Page
* @Description: 分页组件的页面信息
* @author baiwenping
*/
public class Page {
    /**
    * @Fields begin : 分页查询开始记录位置
    */
    private int begin;

    /**
     * @Fields end : 分页查看下结束位置
     */
    private int end;
    /**
     * @Fields length : 每页显示记录数
     */
    private int length;

    /**
     * @Fields count : 查询结果总记录数
     */
    private int count;

    /**
     * @Fields current : 当前页码
     */
    private int current;

    /**
     * @Fields total : 总共页数
     */
    private int total;

    /**
    * <p>Title: 默认构造函数</p>
    * <p>Description: 不建议使用</p>
    */
    public Page() {
    }

    /**
     * 不知道总记录数的构造函数
     * @param begin 分页查询开始记录位置
     * @param length 每页显示记录数
     */
    public Page(final int begin, final int length) {
        this.begin = begin;
        this.length = length;
        end = this.begin + this.length;
        current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
    }

    /**
     * 知道总记录数的构造函数
     * @param begin 分页查询开始记录位置
     * @param length 每页显示记录数
     * @param count 总记录数
     */
    public Page(final int begin, final int length, final int count) {
        this(begin, length);
        this.count = count;
    }

    /**
     * @return the begin
     */
    public int getBegin() {
        return begin;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(final int end) {
        this.end = end;
    }

    /**
     * @param begin
     *            the begin to set
     */
    public void setBegin(final int begin) {
        this.begin = begin;
        if (length != 0) {
            current = (int) Math.floor((this.begin * 1.0d) / length) + 1;
        }
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(final int length) {
        this.length = length;
        if (begin != 0) {
            current = (int) Math.floor((begin * 1.0d) / this.length) + 1;
        }
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(final int count) {
        this.count = count;
        total = (int) Math.floor((this.count * 1.0d) / length);
        if (this.count % length != 0) {
            total = total + 1;
        }
    }

    /**
     * @return the current
     */
    public int getCurrent() {
        return current;
    }

    /**
     * @param current
     *            the current to set
     */
    public void setCurrent(final int current) {
        this.current = current;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        if (total == 0) {
            return 1;
        }
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(final int total) {
        this.total = total;
    }

}
