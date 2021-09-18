/*
* 首页定制化
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class rbCustomServices {
    /**
     * 定制化页面
     */
    static async signIn(req) {
        return rbHttp.sendRequest({
            method: 'post',
            params: req,
            url: '/v1/desk/dutyCheckIn',
        }).then(function (data) {
            return data
        })
    }
    // 不同用户看到自己的定制化首页
    static async getPageViewByLdapId(req) {
        return rbHttp.sendRequest({
            method: 'get',
            params: req,
            url: '/v1/userClassify/findListByLdapId',
        }).then(function (data) {
            return data
        })
    }
    // 不同用户看到自己的视图列表
    static async getPagesList(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/moduleCustomizedView/select',
        }).then(function (data) {
            return data
        })
    }
    // 新增视图
    static async addPageView(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/moduleCustomizedView/insert',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 更新视图
    static async updatePageView(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/moduleCustomizedView/update',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 设计视图
    static async designPageView(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/moduleCustomizedView/design',
            params: req
        }).then(function (data) {
            return data
        })
    }
    // 删除视图
    static async deletePageView(id) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: `/v1/moduleCustomizedView/${id}`,
        }).then(function (data) {
            return data
        })
    }
    // 资源容量总览-统计接口
    static async getInstanceStatistics(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: req,
            url: '/v1/cmdb/restful/common/instance/statistics',
        }).then(function (data) {
            return data
        })
    }
    // 资源使用率
    static async getUserRateForZH(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/alerts/monReport/getUserRateForZH',
        }).then(function (data) {
            return data
        })
    }
    /*    1.  统计接口： /v1/cmdb / restful / common / instance / statistics接口
       接口定义: 根据不同的查询条件, 获取对应的统计数量信息
       变更为:
       URL: /v1/cmdb / restful / common / instance / statistics
       请求方式: POST
       请求参数: 如
       {
           "name": "bpm_department_index_vm_agent_statistic",  // 该值取决于cmdb_sql_source表name字段, 由CMDB统一分配. 需要确保唯一.
               "params": { //根据实际的查询条件进行填写. 该值需要与cmdb_sql_source表中配置的<if> #{} ${}中配置的变量一致.
               "department1": "中移信息公司",
                   "department2": "基础平台部",
                   ...
                   ...
           },
           "responseType": "map / list" // 如果接口需要返回单条记录, 则设置为map即可. 如需返回集合, 则可以不设置或设置为list. 不区分大小写. 系统默认为list.
       }
   
       返回值案列:
       BPM租户首页的虚拟机资源统计情况接口:
       name: bpm_department_index_vm_agent_statistic
       sql: select
       sum(case when department2 = '基础平台部' then 0 else 1 end) assign_count,
           sum(case when IFNULL(is_install_agent, '否') = '否' then 1 else 0 end) un_install_count,
               sum(case when insert_time like CONCAT(YEAR(CURDATE()), '-', '01') then 1 else 0 end) insert_count,
                   sum(1) total_count
       from(
           select * from cmdb_instance where 1 = 1
           <if test = "department1 != null and department1 != ''" >
               and department1 = #department1
               </if>
           <if test="department2 != null and department2 != ''">
               and department2 = #department2
               </if> 
               and is_delete = '0' and device_type = '云主机'
               ) tc
   
       则请求的接口为:
       URL: /v1/cmdb / restful / common / instance / statistics
       {
           "name": "bpm_department_index_vm_agent_statistic",
               "params": {
               "department1": "中移信息公司",
                   "department2": "基础平台部"
           },
           "responseType": "map"
       }
   
       返回值为:
       {
           "assign_count": 0,  //已分配数
               "un_install_count": 0, //未安装agent数量
                   "insert_count": 0,  // 本月新增数量
                       "cycle_count": 0,   // 本月回收数量
                           "total_count": 0    // 当前设备总数量
       }
   
   
       @佳讯, @鉴昌 请关注一下
       BPM需要的三个接口获取方式如上.
       虚拟机的入参 name 是: bpm_department_index_vm_agent_statistic
       物理机的入参 name 是: bpm_department_index_ph_agent_statistic
       GPU的入参 name 是: bpm_department_index_gpu_agent_statistic
   
       返回值如案列返回值.
   
   
           未安装agent比例 = un_install_count / total_count * 100
    */
}
