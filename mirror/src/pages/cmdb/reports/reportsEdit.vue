<template>
    <!-- 上报：编辑页面 -->
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
            <section class="btn-wrap"
                     style="margin-top:30px;">
                <el-button type="primary"
                           icon="el-icon-search"
                           @click="query()">查询</el-button>
                <el-button @click="save()"
                           v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.save">保存</el-button>
            </section>
        </el-form>
        <!-- 表格 -->
        <el-form class="yw-form">
            <YwTableEdit :tableObj="tableObj"
                         :treeParams="treeParams"
                         :condicationCode="condicationCode"
                         :module_id="module_id"
                         :queryParams="getConditionListForm"
                         :tableHeaderCode="tableHeaderCode"
                         @changeTable="changeTable"></YwTableEdit>
        </el-form>
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbReportService from 'src/services/cmdb/rb-cmdb-report-service.factory.js'
    export default {
        mixins: [CommonOption, YwCodeFrameOption],
        props: ['treeParams', 'condicationCode', 'module_id', 'tableHeaderCode'],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            YwTableEdit: () => import('src/components/common/yw-table-edit.vue'),
        },
        data() {
            return {
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
                        // 校验规则未通过集合
                        validErrors: {
                            // 表头key(即数据id)：{第几行:报错信息}
                            // 'node1': {2:'不能为空',3:'大于6'}
                        },
                        // 删除的数据集合
                        deleteDatas: []
                    }
                }
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
                let params = Object.assign({
                    condicationCode: this.condicationCode,
                    module_id: this.module_id
                    // currentPage: 1,
                    // pageSize: 20
                }, this.getConditionListForm)
                return rbCmdbReportService.getTableDatas(params).then((res) => {
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
            // 获得数据校验结果
            validResult() {
                let valid = true
                if (!this.tableObj.tableDatas || this.tableObj.tableDatas.length < 1) {
                    valid = true
                    return valid
                }
                for (let nodeItem in this.tableObj.tableChange.validErrors) {
                    if (Object.keys(this.tableObj.tableChange.validErrors[nodeItem]).length > 0) {
                        valid = false
                        break
                    }

                }

                return valid
            },
            // 查询按钮
            query() {
                this.getTableObj()
            },
            // 保存按钮
            save() {
                if (!this.treeParams) {
                    this.$confirm('请先选择左边树数据', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return
                }
                if (!this.validResult()) {
                    this.$confirm('数据填报不正确，请先检查数据格式!', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return
                }

                this.$confirm('确定保存吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                    let params = Object.assign({
                        add: this.tableObj.tableDatas,
                        delete: this.tableObj.tableChange.deleteDatas,
                        'module_id': this.module_id,
                    }, this.getConditionListForm)
                    rbCmdbReportService.saveDatas(params).then((res) => {
                        if (res.flag) {
                            this.$message.success(res.msg)
                            this.query()
                        } else {
                            this.$message.error(res.msg)
                        }

                    }).finally(() => {
                        this.closeFullScreenLoading()
                    })
                })

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
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>