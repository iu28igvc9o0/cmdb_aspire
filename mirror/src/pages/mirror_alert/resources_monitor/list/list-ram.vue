<!--  -->
<template>
    <div class="components-container">
        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="tableDatas"
                          height="calc(100vh - 340px)"
                          stripe
                          border>
                    <el-table-column label="设备类型"
                                     prop="device_type"
                                     width="160">
                        <template slot-scope="scope">
                            {{ scope.row['device_type'] }}
                        </template>
                    </el-table-column>
                    <el-table-column label="设备名称"
                                     prop="device_name"
                                     width="160"
                                     sortable>
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="goToDevice(scope.row)">{{ scope.row['device_name'] }}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="资源池"
                                     width="110"
                                     prop="idcType">
                        <template slot-scope="scope">
                            {{ scope.row.idcType }}
                        </template>
                    </el-table-column>
                    <el-table-column label="POD"
                                     prop="pod_name"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.pod_name }}
                        </template>
                    </el-table-column>
                    <el-table-column label="IP"
                                     prop="ip"
                                     sortable
                                     width="120">
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="goToDevice(scope.row)">{{ scope.row.ip }}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="一级部门"
                                     prop="department1"
                                     width="100"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.department1 }}
                        </template>
                    </el-table-column>
                    <el-table-column label="二级部门"
                                     prop="department2"
                                     width="100"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.department2 }}
                        </template>
                    </el-table-column>
                    <el-table-column label="业务系统"
                                     prop="bizSystem"
                                     width="120"
                                     sortable>
                        <template slot-scope="scope">
                            {{ scope.row.bizSystem }}
                        </template>
                    </el-table-column>
                    <el-table-column label="联系人"
                                     prop="business_concat">
                        <template slot-scope="scope">
                            {{ scope.row.business_concat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="联系电话"
                                     width="100"
                                     prop="business_concat_phone">
                        <template slot-scope="scope">
                            {{ scope.row.business_concat_phone }}
                        </template>
                    </el-table-column>
                    <template v-for="(timeNum,index) in time">
                        <el-table-column :label="timeNum.label"
                                         prop="memory"
                                         :key="timeNum.label">
                            <template slot-scope="scope"
                                      v-if="scope.row.resourceValueList">
                                {{ scope.row.resourceValueList[index].value }}
                            </template>
                        </el-table-column>
                    </template>
                </el-table>
            </div>
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import rbMonitorService from 'src/services/monitor/rb-monitor-service.factory.js'

    export default {
        mixins: [CommonOption, YwPaginationOption],
        props: ['moduleData'],

        data() {
            return {
                tableTitles: [],
                tableDatas: [],
                time: [],
                timeVal: [],
                queryParams: {
                    'pageSize': '10',
                    'currentPage': '1'
                }
            }
        },
        components: {
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
        },

        methods: {
            setParams(activePagination) {// 设置参数b

                if (activePagination) {
                    this.queryParams['pageSize'] = this.pageSize
                    this.queryParams['currentPage'] = this.currentPage
                } else {

                    this.queryParams = {
                        'currentPage': this.initPageChange(),
                        'pageSize': this.pageSize,
                        // 'monitorValueQuerydate': '2020-08-31',
                        'monitorValueQuerydate': this.moduleData.monitorValueQuerydate,
                        'monitorValueQuerykpi': 'memory',

                        'idcType': this.moduleData.conditionParams['idcType'],
                        'pod_name': this.moduleData.conditionParams['pod_name'],
                        'roomId': this.moduleData.conditionParams['roomId'],
                        'device_type': this.moduleData.conditionParams['device_type'],
                        'department1': this.moduleData.conditionParams['department1'],
                        'department2': this.moduleData.conditionParams['department2'],
                        'bizSystem': this.moduleData.conditionParams['bizSystem'],
                        'ip': this.moduleData.conditionParams['ip'],

                        // 'alertLevel': this.moduleData.tabParams['name'],

                    }
                }

            },
            /** 查询
        * activePagination:分页活动下保持先前的查询条件
        */
            query(activePagination = false) {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                this.setParams(activePagination)
                this.$emit('updatePageInfo', {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize
                })

                let params = this.queryParams

                return rbMonitorService.getInstanceMonitorValueList(params).then((res) => {
                    this.cmdbColumnsConvert = res.columns || {}
                    this.total = res.totalSize || 0
                    this.tableDatas = res.data.map((item) => {
                        return {
                            'device_name': this.convertCmdb(item, 'device_name'),
                            'idcType': this.convertCmdb(item, 'idcType'),
                            'pod_name': this.convertCmdb(item, 'pod_name'),
                            'device_class': this.convertCmdb(item, 'device_class'),
                            'device_type': this.convertCmdb(item, 'device_type'),
                            'ip': this.convertCmdb(item, 'ip'),
                            'department1': this.convertCmdb(item, 'department1'),
                            'department2': this.convertCmdb(item, 'department2'),
                            'bizSystem': this.convertCmdb(item, 'bizSystem'),
                            'business_concat': item['business_concat'],
                            'business_concat_phone': item['business_concat_phone'],
                            'resourceValueList': item['resourceValueList']



                        }
                    })
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },



            timeSlot(step) {   //  step = 间隔的分钟
                var date = new Date()
                date.setHours(0)    // 时分秒设置从零点开始
                date.setSeconds(0)
                date.setUTCMinutes(0)

                var timeArr = []
                var slotNum = 24 * 60 / step   // 算出多少个间隔
                for (var f = 0; f < slotNum; f++) {   //  stepM * f = 24H*60M
                    // arr.push(new Date(Number(date.getTime()) + Number(step*60*1000*f)))   //  标准时间数组
                    var time = new Date(Number(date.getTime()) + Number(step * 60 * 1000 * f))  // 获取：零点的时间 + 每次递增的时间
                    var hour = '', sec = ''
                    time.getHours() < 10 ? hour = '0' + time.getHours() : hour = time.getHours()  // 获取小时
                    time.getMinutes() < 10 ? sec = '0' + time.getMinutes() : sec = time.getMinutes()   // 获取分钟
                    timeArr.push(hour + ':' + sec)
                }
                timeArr.forEach(element => {
                    let obj = {}
                    obj.label = element
                    this.time.push(obj)
                })
            },
            // 跳转到设备分析页
            goToDevice(row) {
                let path = ''
                if (row['device_class'] === '服务器' || row['device_class'] === '存储设备' || row['device_class'] === '安全设备') {
                    path = '/mirror_alert/device_monitor/device_server'
                } else {
                    path = '/mirror_alert/device_monitor/device_network'
                }
                // let path = '/mirror_alert/device_monitor/device_network'

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
            // 跳转到设备指标页
            goToIndex(row) {
                this.$router.push({
                    // path: '/mirror_alert/device_monitor/device_index',
                    path: '/mirror_alert/device_monitor/device_server',
                    query: {
                        row: row
                    }
                })
            }

        },
        created() {
        },
        mounted() {
            this.query()
            this.timeSlot(5)

        },

    }

</script>
<style lang='scss' scoped>
</style>