<template>
    <!-- 网络设备 -->
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
            <ModuleData v-for="(item) in chartDatas.slice(0,5)"
                        :key="item.id"
                        style="width:24.5%;height:200px;"
                        :moduleData="item"></ModuleData>

            <!-- 趋势类 -->
            <TrendErrorIn style="width:49.7%;height:400px;"
                          :moduleData="{instanceId:formData.ipId}"></TrendErrorIn>
            <TrendErrorOut style="width:49.7%;height:400px;"
                           :moduleData="{instanceId:formData.ipId}"></TrendErrorOut>
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
            TrendErrorIn: () => import('src/pages/mirror_alert/device_monitor/device_network/trend-error-in.vue'),
            TrendErrorOut: () => import('src/pages/mirror_alert/device_monitor/device_network/trend-error-out.vue'),
            TrendBandWidthIn: () => import('src/pages/mirror_alert/device_monitor/device_network/trend-bandWidth-in.vue'),
            TrendBandWidthOut: () => import('src/pages/mirror_alert/device_monitor/device_network/trend-bandWidth-out.vue'),
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
                    { id: 0, title: '运行时间', data: 'no data' },
                    { id: 1, title: '指标总数', data: 'no data' },
                    { id: 2, title: '接口数量', data: 'no data' },
                    { id: 3, title: 'Ping耗时', data: 'no data' },
                    // { id: 4, title: '采集耗时', data: 'no data' },
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
            queryNetInfo() {
                let params = {
                    'instanceId': this.formData.ipId,
                    deviceClass: '网络设备',
                }
                return this.$api.queryNetInfo(params).then((res) => {
                    if (res) {
                        this.chartDatas[0].data = res.RunningTime ? res.RunningTime : 'no data'
                        this.chartDatas[1].data = [undefined, null].indexOf(eval(res.itemCount)) > -1 ? 'no data' : res.itemCount
                        this.chartDatas[2].data = [undefined, null].indexOf(eval(res.interfaceCount)) > -1 ? 'no data' : res.interfaceCount
                        this.chartDatas[3].data = res.pingTime ? res.pingTime : 'no data'
                        // this.chartDatas[4].data = res.pingTime || 'no data'
                    }
                    return res
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

                this.queryNetInfo()

                // 更新结果组件
                this.updateComponent()

            },

            // 重置
            reset() {
                this.resetCondition()
                this.query()
            },
            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                let deviceIp = JSON.parse(this.$route.query.queryParams).ip || sessionStorage.getItem('deviceIP')
                let deviceIdByIp = JSON.parse(this.$route.query.queryParams).ip || sessionStorage.getItem('deviceIdByIp')
                //  let deviceIp = this.$route.query.deviceIP || sessionStorage.getItem('deviceIP')
                //   let deviceIdByIp = this.$route.query.deviceIdByIp || sessionStorage.getItem('deviceIdByIp')
                console.log('deviceIp==', deviceIp)
                console.log('deviceIp==', deviceIdByIp)
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
