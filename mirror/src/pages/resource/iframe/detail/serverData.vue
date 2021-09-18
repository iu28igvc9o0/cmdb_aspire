<template>
    <!-- 服务器设备 -->
    <div class="components-container yw-dashboard">
        <!-- 查询条件 -->
        <!-- <el-form class="yw-form components-condition"
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
            <div class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </div>
        </el-form> -->
        <!-- 查询条件 -->

        <!-- 查询结果 -->
        <div class="chart-wrap"
             v-if="resetComponent">
            <!-- 总数、占比类 -->
            <ModuleData style="width:16.1%;height:200px;"
                        :moduleData="chartDatas[0]"></ModuleData>
            <div class="chart-part-vertical"
                 style="width:16.1%;height:200px;">
                <ModuleData v-for="(item) in chartDatas.slice(1,3)"
                            :key="item.id"
                            style="width:100%;height:47%;"
                            :moduleData="item"></ModuleData>
            </div>
            <ModuleGauge v-for="(item) in chartDatas.slice(3,5)"
                         :key="item.id"
                         style="width:16.1%;height:200px;"
                         :moduleData="item"></ModuleGauge>
            <ModuleData v-for="(item) in chartDatas.slice(5,6)"
                        :key="item.id"
                        style="width:16.1%;height:200px;"
                        :moduleData="item"></ModuleData>
            <div class="chart-part-vertical"
                 style="width:16.1%;height:200px;">
                <ModuleData style="width:100%;height:35%;"
                            :moduleData="chartDatas[6]"></ModuleData>
                <ModuleData style="width:100%;height:59%;"
                            :moduleData="chartDatas[7]"
                            :option="{style:{fontSize:'14px'}}"></ModuleData>
            </div>

            <!-- 趋势类 -->
            <TrendCpu style="width:49.7%;height:400px;"
                      :moduleData="{instanceId:formData.ipId}"></TrendCpu>
            <TrendDisk style="width:49.7%;height:400px;"
                       :moduleData="{instanceId:formData.ipId}"></TrendDisk>
            <TrendMemory style="width:49.7%;height:400px;"
                         :moduleData="{instanceId:formData.ipId}"></TrendMemory>
            <TrendSysLoad style="width:49.7%;height:400px;"
                          :moduleData="{instanceId:formData.ipId}"></TrendSysLoad>
            <TrendBandWidthIn style="width:49.7%;height:400px;"
                              :moduleData="{instanceId:formData.ipId}"></TrendBandWidthIn>
            <TrendBandWidthOut style="width:49.7%;height:400px;"
                               :moduleData="{instanceId:formData.ipId}"></TrendBandWidthOut>
        </div>
        <!-- 查询结果 -->
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'

    export default {
        name: 'DeviceServer',
        mixins: [CommonOption, YwCodeFrameOption],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            ModuleData: () => import('src/pages/mirror_alert/device_monitor/module-data.vue'),
            ModuleGauge: () => import('src/pages/mirror_alert/device_monitor/module-gauge.vue'),
            TrendCpu: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-cpu.vue'),
            TrendDisk: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-disk.vue'),
            TrendMemory: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-memory.vue'),
            TrendSysLoad: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-sysLoad.vue'),
            TrendBandWidthIn: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-bandWidth-in.vue'),
            TrendBandWidthOut: () => import('src/pages/mirror_alert/device_monitor/device_server/trend-bandWidth-out.vue'),
        },
        data() {
            return {
                // 表单数据
                formData: {
                    // 设备Ip的id
                    'ipId': ''
                },

                // 图表数据
                chartDatas: [
                    { id: 0, title: '系统运行时间', data: 'no data' },
                    { id: 1, title: 'CPU 核数', data: 'no data' },
                    { id: 2, title: '内存总量', data: 'no data' },
                    {
                        id: 3, title: 'CPU使用率（5m）',
                        data: [{
                            show: true, id: 'gauge-1', chartOption: 'gauge-option', chartDatas: { name: '使用率', rate: 0 },
                            details: {
                                seriesName: { xLabel: 'rate' }
                            }
                        }],
                    },
                    {
                        id: 4, title: '内存使用率',
                        data: [{
                            show: true, id: 'gauge-2', chartOption: 'gauge-option', chartDatas: { name: '使用率', rate: 0 },
                            details: {
                                seriesName: { xLabel: 'rate' }
                            }
                        }],
                    },
                    { id: 5, title: '系统平均负载（5m）', data: 'no data' },
                    { id: 6, title: 'ip地址', data: 'no data' },
                    { id: 7, title: '主机名', data: 'no data' },
                ],

            }
        },

        methods: {
            // 根据Ip查询id
            queryIdByIp() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = {
                    'token': '5245ed1b-6345-11e',
                    'condicationCode': 'cond_monitor_screen_view',

                    'idcType': this.getSelectValueByKey('idcType'),
                    'pod_name': this.getSelectValueByKey('pod_name'),
                    'roomId': this.getSelectValueByKey('roomId'),
                    'device_type': this.getSelectValueByKey('device_type'),
                    'department1': this.getSelectValueByKey('department1'),
                    'department2': this.getSelectValueByKey('department2'),
                    'bizSystem': this.getSelectValueByKey('bizSystem'),
                    'ip': this.getSelectValueByKey('ip'),

                }
                return this.$api.queryIdByIp(params).then((res) => {
                    if (res) {
                        this.formData.ipId = res.id
                    }
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 设备总量
            queryServerInfo() {
                let params = {
                    'instanceId': this.formData.ipId,
                    itermType: 'sysTime',
                    deviceClass: '服务器'
                }
                return this.$api.queryServerInfo(params).then((res) => {
                    if (res) {
                        this.chartDatas[0].data = res.RunningTime ? res.RunningTime : 'no data'
                        this.chartDatas[1].data = [undefined, null].indexOf(eval(res.cpuCount)) > -1 ? 'no data' : res.cpuCount
                        this.chartDatas[2].data = [undefined, null].indexOf(eval(res.memoryCount)) > -1 ? 'no data' : res.memoryCount + 'GB'
                        this.chartDatas[6].data = res.ip ? res.ip : 'no data'
                        this.chartDatas[7].data = res.deviceName ? res.deviceName : 'no data'
                    }
                    return res
                })
            },
            // 设备趋势
            queryServerData() {
                ['cpuUse', 'memoryUse', 'processLoad'].forEach((item) => {
                    let params = {
                        'instanceId': this.formData.ipId,
                        itermType: item,
                        deviceClass: '服务器'
                    }
                    return this.$api.queryServerData(params).then((res) => {
                        switch (item) {
                            case 'cpuUse':
                                if (res && res.series && res.series.length > 0 && res.series[0].data) {
                                    this.chartDatas[3].data[0].chartDatas.rate = res.series[0].data[res.series[0].data.length - 1] || 0
                                } else {
                                    this.chartDatas[3].data[0].chartDatas.rate = 0
                                }

                                this.chartDatas[3].data[0].show = false
                                this.$nextTick(() => {
                                    this.chartDatas[3].data[0].show = true
                                })
                                break
                            case 'memoryUse':
                                if (res && res.series && res.series.length > 0 && res.series[0].data) {
                                    this.chartDatas[4].data[0].chartDatas.rate = res.series[0].data[res.series[0].data.length - 1] || 0
                                } else {
                                    this.chartDatas[4].data[0].chartDatas.rate = 0
                                }
                                this.chartDatas[4].data[0].show = false
                                this.$nextTick(() => {
                                    this.chartDatas[4].data[0].show = true
                                })
                                break
                            case 'processLoad':
                                if (res && res.series && res.series.length > 0 && res.series[0].data) {
                                    this.chartDatas[5].data = res.series[0].data[res.series[0].data.length - 1] || 0
                                } else {
                                    this.chartDatas[5].data = 'no data'
                                }
                                break
                        }
                        return res
                    })
                })

            },

            // 查询
            async query() {

                if (!this.getSelectValueByKey('ip')) {
                    this.$confirm('请输入设备管理IP', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }

                if (this.getSelectValueByKey('ip') !== sessionStorage.getItem('deviceIP')) {
                    // 非运营月报跳转来源，查询Ip的id
                    await this.queryIdByIp()
                    if (!this.formData['ipId']) {
                        this.$confirm('无效的ip,请重新输入ip', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {

                        })
                        return false
                    }
                } else {
                    this.formData.ipId = sessionStorage.getItem('deviceIdByIp')
                    if (!this.formData.ipId) {
                        this.$confirm('此设备无性能数据', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {

                        })
                        return false
                    }
                }
                this.queryServerInfo()
                this.queryServerData()

                // 更新结果组件
                this.updateComponent()

            },

            // 重置
            reset() {
                // 重置级联查询条件
                this.resetCondition()
                this.query()
            },
            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                // let deviceIp = this.$route.query.deviceIP || sessionStorage.getItem('deviceIP')
                // let deviceIdByIp = this.$route.query.deviceIdByIp || sessionStorage.getItem('deviceIdByIp')
                let deviceIp = JSON.parse(this.$route.query.queryParams).ip || sessionStorage.getItem('deviceIP')
                let deviceIdByIp = JSON.parse(this.$route.query.queryParams).ip || sessionStorage.getItem('deviceIdByIp')
                this.setSelectValue('ip', deviceIp)
                this.formData.ipId = deviceIdByIp
                this.query()
            }

        },
        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
    @import "src/pages/mirror_alert/device_monitor/device-monitor.scss";
</style>
