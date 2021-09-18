<template>
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
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <div class="status-wrap fr">
                    <YwStatusLamps :datas="statusDatas"
                                   @changeLamp="changeLamp"></YwStatusLamps>
                </div>
                <!-- <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-upload2"
                           @click="exportOut">导出</el-button> -->
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="tableDatas"
                          height="calc(100vh - 220px)"
                          stripe
                          border>
                    <el-table-column label="严重等级"
                                     prop="alert_level"
                                     sortable
                                     width="100">
                        <template slot-scope="scope">
                            <YwStatusLampsSingle :alertLevel="scope.row['alert_level']"
                                                 v-if="scope.row['alert_level']"></YwStatusLampsSingle>

                        </template>
                    </el-table-column>
                    <el-table-column label="设备IP"
                                     prop="ip"
                                     width="160"
                                     sortable>
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="goToDevice(scope.row)">{{ scope.row.ip }}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="设备分类"
                                     prop="deviceClass"
                                     width="100"
                                     sortable>
                        <template>
                            {{$route.query.row['device_class']}}
                        </template>
                    </el-table-column>
                    <el-table-column label="设备类型"
                                     prop="deviceType"
                                     width="140"
                                     sortable>
                        {{$route.query.row['device_type']}}
                    </el-table-column>
                    <el-table-column label="监控对象"
                                     prop="moniObject"
                                     sortable>
                        <template slot-scope="scope">
                            <span class="text-ellipse"
                                  :title="scope.row.moniObject">{{ scope.row.moniObject }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="监控项名称"
                                     prop="key"
                                     sortable>
                        <template slot-scope="scope">
                            <span class="text-ellipse"
                                  :title="scope.row.key"> {{ scope.row.key }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="监控周期（S）"
                                     prop="delay"
                                     width="140"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.delay }}
                        </template>
                    </el-table-column>
                    <el-table-column label="监控状态"
                                     prop="status"
                                     width="100"
                                     sortable>
                        <template slot-scope="scope">
                            <!-- '0':正常 其他：停用 -->
                            {{ scope.row.status === '0' ? '启用':'停用' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="阈值"
                                     prop="threshold"
                                     width="120"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.threshold }}
                        </template>
                    </el-table-column>
                    <el-table-column label="最新值"
                                     prop="value"
                                     width="120"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.value }}
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>
        <!-- 查询条件 -->
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'

    export default {
        mixins: [CommonOption, YwCodeFrameOption, YwPaginationOption],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
            YwStatusLamps: () => import('src/components/common/yw-status-lamp-level.vue'),
            YwStatusLampsSingle: () => import('src/components/common/yw-status-lamp-level-single.vue'),
        },
        data() {
            return {

                // 表单数据
                formData: {
                    level: '',// 状态灯级别
                },
                // 表格数据
                tableDatas: [],
                statusDatas: {
                    total: '严重等级',
                    list: [
                        { status: 'serious', name: '重大' },
                        { status: 'high', name: '高' },
                        { status: 'middle', name: '中' },
                        { status: 'low', name: '低' },
                    ]
                },

            }
        },

        methods: {
            // 状态灯切换
            changeLamp(val) {
                this.formData.level = val.status
                // this.query()

                this.conditionList
            },

            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNum'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'pageNum': this.initPageChange(),
                        'pageSize': this.pageSize,

                        'resourceId': this.$route.query.row['id'],
                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {

                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                this.setParams(activePagination)

                let params = this.queryParams

                return rbMonitorService.zabbixList(params).then((res) => {
                    this.total = res.count || 0
                    this.tableDatas = res.result
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 跳转到设备分析页面
            goToDevice(row) {
                let path = ''
                if (row['device_class_id'] === '9564' || row['device_class'] === '网络设备') {
                    // 网络设备
                    path = '/mirror_alert/device_monitor/device_network'
                } else {
                    // 服务器
                    path = '/mirror_alert/device_monitor/device_server'
                }

                if (path) {
                    this.$router.push({
                        path: path,
                        query: {
                            deviceIP: row.ip,
                            deviceIdByIp: row.id
                        }
                    })
                }
            },

            // 重置
            reset() {
                this.resetCondition()

            },

            // 导出
            exportOut() {

                let params = {
                    'pageNum': -1,
                    'pageSize': 50,

                    'resourceId': this.$route.query.row['id'],
                }

                this.rbHttp.sendRequest({
                    method: 'GET',
                    data: params,
                    binary: true,
                    url: '/v1/alerts/zabbix/itemByResourceId'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '指标列表.xls'
                    })
                    return res
                })
            },
            // 编辑
            setConditionValues() {
                if (this.$route.query.row) {
                    for (let key in this.$route.query.row) {
                        this.setSelectValue(key, this.$route.query.row[key])
                    }

                }

            },
            // 初始化
            async init() {
                // 查询级联下拉框字段
                // await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                // this.setConditionValues()
                this.query()
            }

        },
        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
    .components-condition {
        padding-right: 225px;
    }
</style>
