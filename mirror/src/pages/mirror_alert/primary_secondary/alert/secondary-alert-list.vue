<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 label-width="85px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="收敛规则">
                <el-select v-model="formData.primarySecondaryId"
                           clearable>
                    <el-option v-for="(item, index) in primarySecondaryList"
                               :key="index"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-for="(itemA,indexA) in tileDatalist"
                          :key="indexA"
                          :label="itemA.queryParamName">
                <el-select v-if="itemA.code === 'biz_sys' && itemA.type === 'tree_in'"
                           clearable
                           filterable
                           v-model="itemA.value"
                           :placeholder="itemA.name"
                           collapse-tags>
                    <el-option :value="mineStatusValue"
                               style="height: auto">
                        <el-tree :data="itemA.listData"
                                 node-key="id"
                                 ref="tree"
                                 highlight-current
                                 :props="defaultProps"
                                 @node-click="handleNodeClick"></el-tree>
                    </el-option>
                </el-select>
                <el-input v-else-if="itemA.type === 'like' || itemA.type === 'and'"
                          v-model="itemA.value"
                          :placeholder="itemA.name"
                          :clearable="true">
                </el-input>
                <el-select v-else-if="itemA.type === 'in'"
                           v-model="itemA.valueList"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           @change="changePool()"
                           @clear="cleanPool()">
                    <el-option v-for="(item,index) in itemA.listData"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
                <el-select v-else-if="itemA.type === 'in_and'"
                           v-model="itemA.value"
                           placeholder="请选择"
                           clearable
                           filterable
                           @change="changePool()"
                           @clear="cleanPool()">
                    <el-option v-for="(item,index) in itemA.listData"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
                <el-date-picker v-else-if="itemA.type === 'date' || itemA.type === 'datetime'"
                                v-model="itemA.valueList"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
                <el-input v-else
                          v-model="itemA.value"
                          :placeholder="itemA.name"
                          :clearable="true">
                </el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="exportBatch()"
                           :disabled="exportDisabled">导出
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          v-loading="loading"
                          :data="result"
                          height="calc(100vh - 310px)"
                          stripe
                          border>
                    <el-table-column type="selection"
                                     width="40"></el-table-column>
                    <el-table-column label="规则名称"
                                     width="160">
                        <template slot-scope="scope">
                            <a>{{scope.row.primary_secondary_name}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column v-for="itemData in listShowList"
                                     :key="itemData.fieldCode"
                                     :label="itemData.listShowName"
                                     :width="((itemData.tableColumnWidth && itemData.tableColumnWidth > 0) ? itemData.tableColumnWidth : '180') + 'px'">
                        <template slot-scope="scope">
                            <label v-if="itemData.dataType === 'datetime'">
                                {{scope.row[itemData.fieldCode] | formatDate}}
                            </label>
                            <rb-mirror-alert-status v-else-if="itemData.fieldCode === 'alert_level'"
                                                    :mode="'list'"
                                                    :status="scope.row.alert_level">
                            </rb-mirror-alert-status>
                            <label v-else>
                                {{scope.row[itemData.fieldCode]}}
                            </label>
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
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertPrimarySecondaryServiceFactory from 'src/services/alert/rb-alert-primary-secondary-service.factory'
    import rbHttp from 'src/assets/js/utility/rb-http.factory'
    export default {
        name: 'MirrorAlertPrimarySecondaryAlert',
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            rbMirrorAlertStatus: () => import('src/pages/mirror_alert/common/rb-mirror-alert-status.vue'),
        },
        data() {
            return {
                listShowList: [],
                modelFieldDataS: [],
                tileDatalist: [],
                // 表单数据
                formData: {
                    isolateId: ''
                },
                // 查询条件
                queryParams: {
                },
                defaultProps: { // 业务系统树形下拉框
                    children: 'subList',
                    label: 'name'
                },
                // 查询结果
                result: [],
                primarySecondaryList: [],
                exportDisabled: false,
            }
        },
        created() {
            if (this.$route.query.id) {
                this.formData.primarySecondaryId = parseInt(this.$route.query.id)
            }
            rbHttp.sendRequest({
                method: 'GET',
                url: '/v2/alerts/model/field/listByTableName/alert_primary_secondary_alerts'
            }).then((res) => {
                // 列表头部展示数据
                let objList = res.filter((item) => {
                    return item.isListShow == 1
                })
                this.listShowList = objList
                // 过滤不需要的数据
                let obj = res.filter((item) => {
                    return item.isQueryParam == 1
                })
                this.modelFieldDataS = obj.sort(function (a, b) {
                    return a.queryParamSort - b.queryParamSort
                })
                this.modelFieldDataS.forEach(fieldCodeType => {
                    let OBJ = {}
                    OBJ.name = fieldCodeType.fieldName
                    OBJ.queryParamName = fieldCodeType.queryParamName
                    OBJ.code = fieldCodeType.fieldCode
                    OBJ.type = fieldCodeType.queryParamType
                    OBJ.valueList = ''
                    OBJ.queryParamSort = fieldCodeType.queryParamSort
                    OBJ.value = ''
                    if (fieldCodeType.fieldCode === 'device_mfrs') {
                        // 获取设备品牌
                        rbHttp.sendRequest({
                            method: 'POST',
                            params: { 'type': '生产供应商', 'orderBy': 'produce' },
                            url: '/v1/cmdb/maintenProduce/listProduceByPage'
                        }).then((res) => {
                            OBJ.listData = res.data.map((item) => {
                                return { 'name': item.produce, 'value': item.produce }
                            })
                        })
                    } else if (fieldCodeType.queryParamSource) {
                        if (fieldCodeType.queryParamSource.startsWith('[')) {
                            OBJ.listData = JSON.parse(fieldCodeType.queryParamSource)
                        } else {
                            rbHttp.sendRequest({
                                method: 'GET',
                                url: fieldCodeType.queryParamSource
                            }).then((resData) => {
                                OBJ.listData = resData
                            })
                        }
                    } else {
                        OBJ.listData = []
                    }
                    setTimeout(() => {

                        this.tileDatalist.push(OBJ)
                    }, 1000)
                })
            })
            this.queryIsolates()
            this.query()
        },
        methods: {
            handleNodeClick(e) {
                let array = this.tileDatalist
                array.forEach(item => {
                    if (item.code === 'biz_sys') {
                        item.value = e.name
                    }
                })
            },
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'list': [],
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize
                    }
                    if (this.formData.primarySecondaryId) {
                        this.queryParams.list.push({
                            'fieldName': 'primary_secondary_id',
                            'fieldType': 'and',
                            'fieldValue': this.formData.primarySecondaryId
                        })
                    }
                    this.tileDatalist.forEach(data => {
                        if (data.value !== '') {
                            let queryObj = {}
                            queryObj.fieldName = data.code
                            queryObj.fieldType = data.type
                            queryObj.fieldValue = data.value
                            this.queryParams.list.push(queryObj)
                        } else if (data.valueList.length > 0) {
                            let queryObj = {}
                            queryObj.fieldName = data.code
                            queryObj.fieldType = data.type
                            queryObj.fieldValue = data.valueList.join(',')
                            this.queryParams.list.push(queryObj)
                        }
                    })
                }

            },
            query(activePagination = false) {
                this.loading = true
                this.setParams(activePagination)
                rbAlertPrimarySecondaryServiceFactory.getprimarySecondaryAlertList(this.queryParams).then((data) => {
                    this.result = data.result
                    this.total = data.count
                }).finally(() => {
                    this.loading = false
                })
            },
            queryIsolates() {
                rbAlertPrimarySecondaryServiceFactory.getprimarySecondaryList({ 'pageSize': -1 }).then((data) => {
                    this.primarySecondaryList = data.result
                    this.$forceUpdate()
                })
            },
            reset() {
                for (let item in this.tileDatalist) {
                    var data = this.tileDatalist[item]
                    data.value = ''
                    data.valueList = []
                }
            },
            exportBatch() {
                this.loading = true
                this.exportDisabled = true
                this.setParams()
                rbAlertPrimarySecondaryServiceFactory.exportprimarySecondaryAlertList(this.queryParams).then((res) => {
                    if (res.code === '0000') {
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '次要告警记录.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.message)
                    }
                }).finally(() => {
                    this.loading = false
                    this.exportDisabled = false
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
