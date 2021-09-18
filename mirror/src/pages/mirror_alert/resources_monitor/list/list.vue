<!--  -->
<template>
    <div>
        <div class="components-container yw-dashboard">
            <div class="tree-main">
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
                                     @changeSelect="changeSelect"></YwCodeFrame>
                    </el-form-item>
                    <el-form-item label="日期">
                        <el-date-picker v-model="moduleData.monitorValueQuerydate"
                                        type="date"
                                        format="yyyy-MM-dd"
                                        value-format="yyyy-MM-dd"
                                        placeholder="选择日期">
                        </el-date-picker>

                    </el-form-item>

                    <div class="btn-wrap">
                        <el-button type="primary"
                                   @click="query()">查询</el-button>
                        <el-button @click="reset()">重置</el-button>
                        <el-button @click="exportBtn()">导出</el-button>
                    </div>
                </el-form>

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

                </section>

                <!-- table -->
                <component :is="currentTabComponent"
                           v-if="resetComponent"
                           @updatePageInfo="updatePageInfo"
                           :moduleData="moduleData"></component>
            </div>
        </div>
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'

    export default {
        name: 'ResourcesList',
        components: {
            ListCpu: () => import('./list-cpu.vue'),
            ListRam: () => import('./list-ram.vue'),
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
        },
        mixins: [CommonOption, YwCodeFrameOption],

        data() {
            return {
                value1: '',
                // 表单数据
                formData: {

                },

                // tab
                activeTab: 'cpu',
                activeTabObj: { label: 'CPU', data: '(0)', name: 'cpu', componentPage: 'ListCpu' },
                tabData: [
                    { label: 'CPU', data: '(0)', name: 'cpu', componentPage: 'ListCpu' },
                    { label: '内存', data: '(0)', name: 'memory', componentPage: 'ListRam' },
                ],

                // 内容
                currentTabComponent: 'ListCpu',
                moduleData: {
                    conditionParams: {},// 查询条件
                    tabParams: {},// tab
                    treeParams: {},// tree
                    monitorValueQuerydate: '',// 日期
                },
                pageInfo: {}

            }
        },

        methods: {
            updatePageInfo(data) {
                this.pageInfo = data
            },
            getTime() {
                var myDate = new Date()     // 获取当前年份(2位)
                var year = myDate.getFullYear()    // 获取完整的年份(4位,1970-????)
                var month = myDate.getMonth() + 1       // 获取当前月份(0-11,0代表1月)
                var day = myDate.getDate()        // 获取当前日(1-31)
                this.moduleData.monitorValueQuerydate = year + '-' + month + '-' + day
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

            // 重置
            reset() {
                this.resetCondition()
                this.moduleData.monitorValueQuerydate = ''
            },
            // 查询结果
            query() {
                if (this.moduleData.monitorValueQuerydate === '' || this.moduleData.monitorValueQuerydate === null || this.moduleData.monitorValueQuerydate === undefined) {
                    this.getTime()
                }
                // 更新参数
                this.moduleData.conditionParams = {
                    'idcType': this.getSelectValueByKey('idcType'),
                    'pod_name': this.getSelectValueByKey('pod_name'),
                    'roomId': this.getSelectValueByKey('roomId'),
                    'device_type': this.getSelectValueByKey('device_type'),
                    'department1': this.getSelectValueByKey('department1'),
                    'department2': this.getSelectValueByKey('department2'),
                    'bizSystem': this.getSelectValueByKey('bizSystem'),
                    'ip': this.getSelectValueByKey('ip'),
                }

                this.moduleData.tabParams = this.activeTabObj
                this.moduleData.treeParams = this.activeTreeObj

                // 更新组件
                this.currentTabComponent = this.activeTabObj.componentPage
                this.updateComponent()
            },
            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.query()
            },
            // 导出按钮
            exportBtn() {
                this.moduleData.conditionParams = {
                    'idcType': this.getSelectValueByKey('idcType'),
                    'pod_name': this.getSelectValueByKey('pod_name'),
                    'roomId': this.getSelectValueByKey('roomId'),
                    'device_type': this.getSelectValueByKey('device_type'),
                    'department1': this.getSelectValueByKey('department1'),
                    'department2': this.getSelectValueByKey('department2'),
                    'bizSystem': this.getSelectValueByKey('bizSystem'),
                    'ip': this.getSelectValueByKey('ip'),
                }
                let req = this.moduleData.conditionParams
                req.monitorValueQuerydate = this.moduleData.monitorValueQuerydate
                req.monitorValueQuerykpi = this.activeTab
                req.currentPage = this.pageInfo.currentPage
                req.pageSize = this.pageInfo.pageSize
                this.loading = true
                rbMonitorService.exportInstanceMonitorValueList(req).then(res => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '资源性能展示数据列表.xls'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                })
            }

        },
        created() {
            this.init()
            this.getTime()
        },
    }

</script>
<style lang='scss' scoped>
</style>