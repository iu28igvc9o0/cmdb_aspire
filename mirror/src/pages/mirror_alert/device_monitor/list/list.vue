<template>
    <!-- 全设备展示页 -->
    <div class="components-container"
         style="padding:0;">
        <el-container>
            <el-aside width="260px"
                      style="overflow:hidden"
                      class="aside-tree">
                <YwTree @clickTree="clickTree"
                        :options="treeOptions"></YwTree>
            </el-aside>
            <el-main class="tree-main"
                     style="padding:0 0 0 10px;">
                <el-tabs v-model="activeName"
                         @tab-click="handleClick">
                    <el-tab-pane label="告警展示"
                                 name="first">
                        <div class="components-container yw-dashboard">
                            <!-- 查询条件 -->
                            <el-form class="yw-form components-condition"
                                     label-width="75px"
                                     :inline="true"
                                     :model="formData">
                                <el-form-item :label="labelItem.name"
                                              v-for="(labelItem,labelIndex) in conditionList"
                                              :key="labelIndex">
                                    <YwCodeFrame :frameDatas="labelItem.frameDatas"
                                                 v-if="labelItem.frameDatas.show"
                                                 :frameOptions="labelItem.frameOptions"
                                                 @clear="selfClear"
                                                 @changeSelect="changeSelect"></YwCodeFrame>
                                </el-form-item>
                                <div class="btn-wrap">
                                    <!-- <el-button type="primary"
                                   @click="goToIndex()">指标</el-button> -->
                                    <el-button type="primary"
                                               @click="query()">查询</el-button>
                                    <el-button @click="reset()">重置</el-button>
                                </div>
                            </el-form>
                            <!-- 查询条件 -->
                            <!-- tab -->
                            <section class="tab-wrap">
                                <el-tabs class="yw-tabs"
                                         v-model="activeTab"
                                         @tab-click="changeTab">
                                    <el-tab-pane :label="item.label"
                                                 tabindex="-1"
                                                 :name="item.name"
                                                 v-for="(item,index) in tabData"
                                                 :key="index">
                                    </el-tab-pane>
                                </el-tabs>
                                <div class="status-wrap">
                                    <YwStatusLamps :datas="statusDatas"></YwStatusLamps>
                                </div>

                            </section>
                            <!-- tab -->
                            <!-- table -->
                            <component :is="currentTabComponent"
                                       v-if="resetComponent"
                                       :moduleData="moduleData"></component>
                            <!-- table -->
                        </div>
                    </el-tab-pane>
                    <!-- <el-tab-pane label="连通性展示"
                                 name="second">
                        <div class="components-container yw-dashboard">
                            <el-form class="yw-form components-condition"
                                     label-width="75px"
                                     :inline="true"
                                     :model="formData">
                                <el-form-item :label="labelItem.name"
                                              v-for="(labelItem,labelIndex) in conditionList"
                                              :key="labelIndex">
                                    <YwCodeFrame :frameDatas="labelItem.frameDatas"
                                                 v-if="labelItem.frameDatas.show"
                                                 :frameOptions="labelItem.frameOptions"
                                                 :activeName="activeName"
                                                 @clear="selfClear"
                                                 @changeSelect="changeSelect"></YwCodeFrame>
                                </el-form-item>
                                <el-form-item label="设备类型">
                                    <el-select v-model="optionsParams.device_type"
                                               clearable
                                               placeholder="请选择">
                                        <el-option v-for="item in deviceOptions"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="管理网状态">
                                    <el-select v-model="optionsParams.status_manageIp"
                                               clearable
                                               placeholder="请选择">
                                        <el-option v-for="item in options"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="带外网状态">
                                    <el-select v-model="optionsParams.status_ipmiIp"
                                               clearable
                                               placeholder="请选择">
                                        <el-option v-for="item in options"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="业务网状态">
                                    <el-select v-model="optionsParams.status_serviceIp"
                                               clearable
                                               placeholder="请选择">
                                        <el-option v-for="item in options"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <div class="btn-wrap">
                                    <el-button type="primary"
                                               @click="query()">查询</el-button>
                                    <el-button @click="reset()">重置</el-button>
                                </div>
                            </el-form>
                            <section class="tab-wrap">
                                <el-tabs class="yw-tabs"
                                         v-model="activeTab"
                                         @tab-click="changeConnectTab">
                                    <el-tab-pane :label="item.label"
                                                 tabindex="-1"
                                                 :name="item.name"
                                                 v-for="(item,index) in tabConnecData"
                                                 :key="index">
                                    </el-tab-pane>
                                </el-tabs>
                                <div class="status-wrap">
                                    <YwStatusLamps :datas="statusDatas"></YwStatusLamps>
                                </div>

                            </section>
                            <component :is="currentConnectTabComponent"
                                       v-if="resetComponent"
                                       :moduleData="moduleData"
                                       :optionsParams="optionsParams"></component>
                        </div>
                    </el-tab-pane> -->
                </el-tabs>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    // import rbCustomServices from 'src/services/custom_pages/rb-services.js'

    export default {
        name: 'DeviceList',
        mixins: [CommonOption, YwCodeFrameOption],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            YwStatusLamps: () => import('src/components/common/yw-status-lamp-number.vue'),
            YwTree: () => import('src/components/common/yw-code-tree.vue'),
            ListAll: () => import('./list-all.vue'),
            ListLevel: () => import('./list-level.vue'),
            ListConnectAll: () => import('./list-connect-all.vue'),
            ListConnectLevel: () => import('./list-connect-level.vue'),
        },
        data() {
            return {
                activeName: 'first',
                // 表单数据
                formData: {

                },

                // tab
                activeTab: '-1',
                activeTabObj: { label: '全量', data: '(0)', name: '-1', componentPage: 'ListAll' },
                tabData: [
                    { label: '全量', data: '(0)', name: '-1', componentPage: 'ListAll' },
                    { label: '重大', data: '(0)', name: '5', componentPage: 'ListLevel' },
                    { label: '高', data: '(0)', name: '4', componentPage: 'ListLevel' },
                    { label: '中', data: '(0)', name: '3', componentPage: 'ListLevel' }
                ],
                activeConnectTabObj: { label: '全量', data: '(0)', name: '-1', componentPage: 'ListConnectAll' },
                tabConnecData: [
                    { label: '全量', data: '(0)', name: '-1', componentPage: 'ListConnectAll' },
                    { label: '重大', data: '(0)', name: '5', componentPage: 'ListConnectLevel' },
                    { label: '高', data: '(0)', name: '4', componentPage: 'ListConnectLevel' },
                    { label: '中', data: '(0)', name: '3', componentPage: 'ListConnectLevel' }
                ],
                statusDatas: {
                    total: '总数：0',
                    list: [
                        { status: 'red', num: 0 },
                        { status: 'orange', num: 0 },
                        { status: 'yellow', num: 0 },
                        { status: 'blue', num: 0 },
                    ]
                },

                // tree
                activeTreeObj: {},// 选中的树节点
                treeOptions: {
                    dataParams: {
                        treeCode: 'monitor_screen_idc_view',
                    },
                    search: {
                        show: true,
                    },
                    style: {
                        height: 'calc(100vh - 120px)'
                    }
                },

                // 内容
                currentTabComponent: 'ListAll',
                currentConnectTabComponent: 'ListConnectAll',
                moduleData: {
                    conditionParams: {},// 查询条件
                    tabParams: {},// tab
                    tabConnectParams: {},// tab
                    treeParams: {},// tree
                },
                // 级别类查询条件
                conditionParamsByLevel: {},

                options: [],
                optionsParams: {
                    status_manageIp: '',
                    status_ipmiIp: '',
                    status_serviceIp: '',
                    device_type: ''
                },
                deviceOptions: []
            }
        },

        methods: {
            // getInstanceStatistics() {
            //     let req = {
            //         // 该值取决于cmdb_sql_source表name字段, 由CMDB统一分配. 需要确保唯一.
            //         name: "Alert_API_getModuleListByDeviceType",
            //         // 根据实际的查询条件进行填写. 该值需要与cmdb_sql_source表中配置的<if> #{} ${}中配置的变量一致.
            //         params: {
            //             module_id: ''
            //         },
            //         // 如果接口需要返回单条记录, 则设置为map即可. 如需返回集合, 则可以不设置或设置为list. 不区分大小写. 系统默认为list.
            //         responseType: list
            //     }
            //     rbCustomServices
            //         .getInstanceStatistics(req)
            //         .then(res => {
            //             this.deviceOptions = res
            //             debugger
            //         })
            // },
            handleClick(tab, event) {
                // let filterList = JSON.parse(JSON.stringify(this.conditionList))
                // if (tab.name == 'second') {
                //     this.conditionList = filterList.filter(item => {
                //         return item.name !== '设备类型'
                //     })
                // } else {
                //     this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                // }
            },

            // tab切换
            changeTab(tab) {
                this.tabData.some((item) => {
                    if (item.name === tab.name) {
                        this.activeTabObj = item
                        return true
                    } else {
                        return false
                    }
                })

                this.query()
            },
            changeConnectTab(tab) {
                this.tabConnecData.some((item) => {
                    if (item.name === tab.name) {
                        this.activeConnectTabObj = item
                        return true
                    } else {
                        return false
                    }
                })

                this.query()
            },
            // 点击树
            clickTree(val = {}) {
                if (val.data.queryToData) {
                    // 方式一：改变右侧级联下拉框选中
                    this.conditionList.forEach((item) => {
                        item.frameDatas.select = ''
                        item.frameDatas.parentCode = ''
                        item.frameDatas.parentSelect = ''
                        this.freshCodeFrame(item)

                        this.conditionParamsByLevel[item.key] = ''
                    })

                    for (let key in val.data.queryToData) {
                        this.setSelectValue(key, { 'id': val.data.queryToData[key], 'name': val.data.queryInfo[key] })
                        this.conditionParamsByLevel[key] = val.data.queryInfo[key]
                    }
                }

                this.activeTreeObj = val.data
                this.query()

            },

            // 查询结果
            query() {
                // 更新参数
                // 默认，取id
                this.moduleData.conditionParams = {
                    'idcType': this.getSelectValueByKey('idcType'),
                    'pod_name': this.getSelectValueByKey('pod_name'),
                    'roomId': this.getSelectValueByKey('roomId'),
                    'device_type': this.getSelectValueByKey('device_type'),
                    'department1': this.getSelectValueByKey('department1'),
                    'department2': this.getSelectValueByKey('department2'),
                    'bizSystem': this.getSelectValueByKey('bizSystem'),
                    'ip': this.getSelectValueByKey('ip'),
                    // 'status_serviceIp': this.options.status_serviceIp,
                    // 'status_manageIp': this.options.status_manageIp,
                    // 'status_ipmiIp': this.options.status_ipmiIp
                }
                // 非全量，取value值
                if (this.activeTab !== '-1') {
                    this.moduleData.conditionParams = {
                        'idcType': this.conditionParamsByLevel['idcType'] ? this.conditionParamsByLevel['idcType'] : this.getSelectValueByKey('idcType', 'value'),
                        'pod_name': this.conditionParamsByLevel['pod_name'] ? this.conditionParamsByLevel['pod_name'] : this.getSelectValueByKey('pod_name', 'value'),
                        'roomId': this.conditionParamsByLevel['roomId'] ? this.conditionParamsByLevel['roomId'] : this.getSelectValueByKey('roomId', 'value'),
                        'device_type': this.conditionParamsByLevel['device_type'] ? this.conditionParamsByLevel['device_type'] : this.getSelectValueByKey('device_type', 'value'),
                        'department1': this.conditionParamsByLevel['department1'] ? this.conditionParamsByLevel['department1'] : this.getSelectValueByKey('department1', 'value'),
                        'department2': this.conditionParamsByLevel['department2'] ? this.conditionParamsByLevel['department2'] : this.getSelectValueByKey('department2', 'value'),
                        'bizSystem': this.conditionParamsByLevel['bizSystem'] ? this.conditionParamsByLevel['bizSystem'] : this.getSelectValueByKey('bizSystem', 'value'),
                        'ip': this.getSelectValueByKey('ip'),

                    }
                }
                // this.moduleData.conditionParams.status_serviceIp = this.options.status_serviceIp
                // this.moduleData.conditionParams.status_manageIp = this.options.status_manageIp
                // this.moduleData.conditionParams.status_ipmiIp = this.options.status_ipmiIp

                this.moduleData.tabParams = this.activeTabObj
                this.moduleData.tabConnectParams = this.activeConnectTabObj
                this.moduleData.treeParams = this.activeTreeObj

                // 查询状态
                this.getStatus()

                // 更新组件
                this.currentTabComponent = this.activeTabObj.componentPage
                this.currentConnectTabComponent = this.activeConnectTabObj.componentPage
                this.updateComponent()
            },

            // 跳转到设备指标页
            goToIndex() {
                this.$router.push({
                    path: '/mirror_alert/device_monitor/device_index',
                    query: {

                    }
                })
            },
            // 获得状态总数
            getStatus() {

                this.statusDatas = {
                    total: '总数：0',
                    list: [
                        { status: 'red', num: 0 },
                        { status: 'orange', num: 0 },
                        { status: 'yellow', num: 0 },
                        { status: 'blue', num: 0 },
                    ]
                }

                let params = {
                    'idcType': this.conditionParamsByLevel['idcType'] ? this.conditionParamsByLevel['idcType'] : this.getSelectValueByKey('idcType', 'value'),
                    'pod_name': this.conditionParamsByLevel['pod_name'] ? this.conditionParamsByLevel['pod_name'] : this.getSelectValueByKey('pod_name', 'value'),
                    'roomId': this.conditionParamsByLevel['roomId'] ? this.conditionParamsByLevel['roomId'] : this.getSelectValueByKey('roomId', 'value'),
                    'device_type': this.conditionParamsByLevel['device_type'] ? this.conditionParamsByLevel['device_type'] : this.getSelectValueByKey('device_type', 'value'),
                    'department1': this.conditionParamsByLevel['department1'] ? this.conditionParamsByLevel['department1'] : this.getSelectValueByKey('department1', 'value'),
                    'department2': this.conditionParamsByLevel['department2'] ? this.conditionParamsByLevel['department2'] : this.getSelectValueByKey('department2', 'value'),
                    'bizSystem': this.conditionParamsByLevel['bizSystem'] ? this.conditionParamsByLevel['bizSystem'] : this.getSelectValueByKey('bizSystem', 'value'),
                    'ip': this.getSelectValueByKey('ip'),
                }

                return rbMonitorService.deviceByAlert(params).then((res) => {
                    if (res) {
                        let total = 0
                        res.forEach((item) => {
                            let count = Number(item.count) || 0
                            let level = Number(item['alert_level'])
                            this.statusDatas.list[5 - level].num = count
                            total += count
                        })
                        this.statusDatas.total = `总数：${total}`

                    }

                    return res
                }).finally(() => {

                })
            },
            // 此页面自定义清空
            selfClear() {
                this.conditionList.forEach((item) => {
                    if (!item.frameDatas.select || Object.keys(item.frameDatas.select).length < 1) {
                        this.conditionParamsByLevel[item.key] = ''
                    }

                })

            },

            // 重置
            reset() {
                this.conditionList.forEach((item) => {
                    item.frameDatas.select = ''
                    item.frameDatas.parentCode = ''
                    item.frameDatas.parentSelect = ''
                    this.freshCodeFrame(item)
                    this.conditionParamsByLevel[item.key] = ''
                })
                this.query()
            },
            getQueryParams() {
                let req = {
                    type: 'whether'
                }
                return cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.options = res
                    })
            },
            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                await this.getQueryParams()
                this.query()
                // this.getInstanceStatistics()
            }

        },
        created() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
    .components-condition {
        padding-right: 225px;
    }
    .tab-wrap {
        position: relative;
        .status-wrap {
            position: absolute;
            bottom: 10px;
            right: 10px;
        }
    }
</style>
