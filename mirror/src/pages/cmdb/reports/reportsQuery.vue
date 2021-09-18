<template>
    <!-- 上报：查询页面 -->
    <div class="components-container yw-dashboard">
        <!-- 查询条件 -->
        <el-form class="yw-form components-condition"
                 label-width="85px"
                 :inline="true">
            <el-form-item :label="labelItem.name"
                          v-for="(labelItem,labelIndex) in conditionList"
                          :key="labelIndex">
                <YwCodeFrame :frameDatas="labelItem.frameDatas"
                             v-if="labelItem.frameDatas.show"
                             :frameOptions="labelItem.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           icon="el-icon-search"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <!-- 表格 -->
        <el-form class="yw-form">
            <div class="yw-table-card">
                <el-card class="box-card"
                         style="margin-bottom: 5px;">
                    <div slot="header">
                        <span style="font-weight: bold">{{tableObj.title}}</span>
                        <div class="clearfix fr">
                            <el-button v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.export"
                                       type="text"
                                       icon="el-icon-download"
                                       @click="exportInstance()">导出</el-button>
                        </div>
                    </div>
                    <el-table class="yw-el-table"
                              style="width:100%;height:100%;min-height:300px;"
                              :data="tableObj.tableDatas"
                              max-height="500"
                              stripe
                              border>
                        <YwTableColumn :tableTitles="tableObj.tableTitles"
                                       :tableDatas="tableObj.tableDatas"
                                       :columns="tableObj.columns"
                                       :validErrors="tableObj.tableChange.validErrors"
                                       operatorType="query"
                                       @changeTable="changeTable"></YwTableColumn>
                        <el-table-column label=""
                                         width="0">
                        </el-table-column>
                    </el-table>
                    <YwPagination @handleSizeChange="handleSizeChange"
                                  @handleCurrentChange="handleCurrentChange"
                                  :current-page="currentPage"
                                  :page-sizes="pageSizes"
                                  :page-size="pageSize"
                                  :total="total"></YwPagination>
                </el-card>
            </div>

        </el-form>
        <!-- 导出结果 -->
        <exportInstances ref="exportInstances"
                         v-if="isExportShow"
                         :showExport="isExportShow"
                         @setExportDisplay="setExportDisplay"
                         :moduleId="module_id"
                         :queryParams="queryParamsAll"></exportInstances>
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import exportByProcess from 'src/utils/exportByProcess.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbReportService from 'src/services/cmdb/rb-cmdb-report-service.factory.js'
    export default {
        mixins: [CommonOption, exportByProcess, YwPaginationOption, YwCodeFrameOption],
        props: ['treeParams', 'condicationCode', 'module_id', 'tableHeaderCode'],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
            YwTableColumn: () => import('src/components/common/yw-table-column.vue'),
            exportInstances: () => import('src/pages/cmdb/v2/instance/export/export-instance.vue'),
        },
        data() {
            return {
                // 分页标签
                activePagination: false,
                // 表格
                tableObj: {
                    // 表标题
                    title: '',
                    // 表头
                    tableTitles: [
                        // {
                        //     key: 'node1',
                        //     name: '1级',
                        //     controlType: 'input',
                        //     rules: [
                        //         { validType: 'fromConfig', required: true, message: '不能为空', },// 静态判断
                        //         { validType: 'fromFun' }// 接口动态判断
                        //     ]
                        // },
                        // {
                        //     key: 'node2',
                        //     name: '2级',
                        //     children: [
                        //         {
                        //             key: 'node2-1', name: '2级-1', controlType: 'select', rules: [
                        //                 { validType: 'fromConfig', required: true, message: '不能为空', },// 静态判断
                        //                 { validType: 'fromFun' }// 接口动态判断
                        //             ]
                        //         },
                        //         { key: 'node2-2', name: '2级-2', controlType: 'datetime' }
                        //     ]
                        // },
                        // {
                        //     key: 'node3',
                        //     name: '3级',
                        //     children: [
                        //         { key: 'node3-1', name: '3级-1', children: [{ key: 'node3-1-1', name: '3级-1-1', controlType: 'number' }, { key: 'node3-1-2', name: '3级-1-2' }] },
                        //         { key: 'node3-2', name: '3级-2', children: [{ key: 'node3-2-1', name: '3级-2-1' }, { key: 'node3-2-2', name: '3级-2-2' }] }
                        //     ]
                        // }
                    ],
                    // 表数据
                    tableDatas: [
                        // { node1: '123', 'node2-2': 444, 'node3-1-1': 888, 'node3-1-2': 777 },
                        // {}
                    ],
                    // 字段列属性
                    columns: {
                        // bizSystem: {
                        //     code_id: '4feabaeba38949139f10049c4782665b',
                        //     filed_code: 'bizSystem',
                        //     filed_name: '归属业务名称',
                        //     ref_name: 'bizSystem_bizSystem_name_name',
                        //     type: 'ref'
                        // }
                    },
                    // 表格变更对象(校验规则、其他数据等)
                    tableChange: {
                        validErrors: {}
                    }
                }
            }
        },
        computed: {
            // 查询条件
            queryParamsAll() {
                let obj = {
                    'condicationCode': this.condicationCode,
                    'module_id': this.module_id,
                }
                if (this.activePagination) {
                    obj['currentPage'] = this.currentPage
                    obj['pageSize'] = this.pageSize
                } else {
                    obj['currentPage'] = this.initPageChange()
                    obj['pageSize'] = this.pageSize
                    obj = Object.assign(obj, this.getConditionListForm)
                }

                return obj
            }
        },
        methods: {
            // 获得表头
            getTableTitles() {
                this.tableObj.title = this.treeParams && this.treeParams.nodeName || ''
                let params = {
                    moduleId: this.module_id,
                    tableHeaderCode: this.tableHeaderCode
                }
                return rbCmdbReportService.getTableHeader(params).then((res) => {
                    this.tableObj.tableTitles = res.filter(function (item) {
                        return !item.display

                    })
                    return res
                })

            },
            // 获得表数据
            getTableDatas() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = this.queryParamsAll
                return rbCmdbReportService.getTableDatas(params).then((res) => {
                    this.total = res.totalSize || 0
                    this.tableObj.tableDatas = res.data || []
                    this.tableObj.columns = res.columns || {}
                    return res
                }).finally(() => {
                    this.closeFullScreenLoading()
                })

            },
            // 获得表格
            getTableObj() {
                this.getTableTitles()
                this.getTableDatas()
            },
            // 表格变更(数据、字段、校验规则等)
            changeTable(obj = {}) {
                let allKeys = Object.keys(obj)
                for (let key in this.tableObj.tableChange) {
                    if (allKeys.indexOf(key) > -1) {
                        this.tableObj.tableChange[key] = obj[key]
                    }
                }
            },
            // 重置
            reset() {
                this.resetCondition()
                this.query()
            },
            // 设置参数
            setParams(activePagination) {
                this.activePagination = activePagination
            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.setParams(activePagination)
                this.getTableObj()
            },
            // 左侧树的条件赋值到右侧查询条件
            treeToCondition() {
                for (let treeKey in this.treeParams.queryToData) {
                    this.setSelectValue(treeKey, this.treeParams.queryToData[treeKey])
                }
            },

            // 初始化
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: this.condicationCode })
                this.treeToCondition()
                this.query()
            }
        },
        mounted() {
            this.pageSize = 20
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>