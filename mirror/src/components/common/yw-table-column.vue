<template>
    <!-- 表格列(动态多级表头类型)【ps.考虑到性能问题,数据校验时机:1、新增数据行；2：编辑数据框时】 -->
    <div>
        <el-table-column v-for="(tableColumn,tableColumnIndex) in tableTitles"
                         :key="tableColumnIndex"
                         :label="tableColumn.name"
                         :prop="tableColumn.key"
                         :fixed="tableColumn.fixed ? tableColumn.fixed : false"
                         min-width="200"
                         align="center">
            <div v-if="tableColumn.children && tableColumn.children.length>0 && !tableColumn.display">
                <YwTableColumn :tableTitles="tableColumn.children"
                               :tableDatas="tableDatas"
                               :columns="columns"
                               :validErrors="validErrors"
                               :operatorType="operatorType"
                               @changeTable="changeTable"></YwTableColumn>
            </div>
            <div v-if="!(tableColumn.children && tableColumn.children.length>0 && tableColumn.display)"
                 slot-scope="scope">
                <!-- 文本 -->
                <div v-if="operatorType === 'query'">
                    {{columns[tableColumn.key] && columns[tableColumn.key].type==='ref' ? scope.row[columns[tableColumn.key].ref_name] : scope.row[tableColumn.key]}}
                </div>
                <!-- 下拉框 -->
                <el-select v-else-if="selectList.includes(tableColumn.controlType)"
                           :clearable="true"
                           :filterable="true"
                           v-model="scope.row[tableColumn.key]"
                           @change="validRules(tableColumn,scope.row[tableColumn.key],scope.$index)"
                           placeholder="请选择">
                    <el-option v-for="(selectItem,selectIndex) in selectOptions[tableColumn.key]"
                               :key="selectIndex"
                               :label="selectItem.key"
                               :value="selectItem.id">
                    </el-option>
                </el-select>
                <!-- 数字框 -->
                <el-input-number v-else-if="numberList.includes(tableColumn.controlType)"
                                 v-model="scope.row[tableColumn.key]"
                                 :min="0"
                                 @change="validRules(tableColumn,scope.row[tableColumn.key],scope.$index)"
                                 label="描述文字"></el-input-number>
                <!-- 时间框 -->
                <el-date-picker v-else-if="dateList.includes(tableColumn.controlType)"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                v-model="scope.row[tableColumn.key]"
                                type="datetime"
                                style="width:100%"
                                @change="validRules(tableColumn,scope.row[tableColumn.key],scope.$index)"
                                placeholder="选择日期时间">
                </el-date-picker>
                <!-- 输入框 -->
                <input v-else
                       class="edit-input"
                       type="text"
                       @blur="validRules(tableColumn,scope.row[tableColumn.key],scope.$index)"
                       v-model="scope.row[tableColumn.key]"
                       placeholder="" />
                <!-- 校验提示 -->
                <div v-if="validErrors[tableColumn.key] && validErrors[tableColumn.key][scope.$index]"
                     class="text-ellipse"
                     style="font-size:12px;color:red;">
                    {{validErrors[tableColumn.key][scope.$index]}}
                </div>
                <!-- 校验提示 -->
            </div>
        </el-table-column>

    </div>
</template>

<script>
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbReportService from 'src/services/cmdb/rb-cmdb-report-service.factory.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    export default {
        mixins: [YwCodeFrameOption],
        props: ['tableTitles', 'tableDatas', 'columns', 'validErrors', 'operatorType'],
        components: {
            YwTableColumn: () => import('src/components/common/yw-table-column.vue'),
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
        },
        data() {
            return {
                // 输入框
                inputList: ['ip', 'singleRowText'],
                // 数字框
                numberList: ['int'],
                // 下拉框
                selectList: ['listSel', 'cascader'],
                // 时间框
                dateList: ['dateTime'],
                // select下拉框数据集
                selectOptions: {
                    // 表头key(即数据id)：数据
                    // 'node2-1': [{
                    //     id: '选项1',
                    //     key: '黄金糕',
                    //     value: '黄金糕'
                    // }]
                },
                // demo：校验规则未通过集合
                // validErrors: {
                // 表头key(即数据id)：{第几行:报错信息}
                // 'node1': {2:'不能为空',3:'大于6'}
                // },

                // demo：表头
                // tableTitles: [
                //     {
                //         key: 'node1',
                //         name: '1级',
                // controlType: 'input',
                //             rules: [
                //                 { required: true, message: '请输入活动名称', trigger: 'blur' },
                //                 { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                //             ]
                //     },
                //     {
                //         key: 'node2',
                //         name: '2级',
                //         children: [
                //             { key: 'node2-1', name: '2级-1' }, { key: 'node2-2', name: '2级-2' }
                //         ]
                //     },
                //     {
                //         key: 'node3',
                //         name: '3级',
                //         children: [
                //             { key: 'node3-1', name: '3级-1', children: [{ key: 'node3-1-1', name: '3级-1-1' }, { key: 'node3-1-2', name: '3级-1-2' }] },
                //             { key: 'node3-2', name: '3级-2', children: [{ key: 'node3-2-1', name: '3级-2-1' }, { key: 'node3-2-2', name: '3级-2-2' }] }
                //         ]
                //     }
                // ],

            }
        },
        watch: {
            'tableDatas.length': {
                handler(newVal, oldVal) {
                    // 新增时，去校验规则
                    // if (newVal > oldVal && this.operatorType === 'update') {
                    //     // 新增在最后一行
                    //     this.rowValid(this.tableTitles, newVal - 1)

                    // }
                    if (this.operatorType === 'update') {
                        // 新增在第一行
                        this.rowValid(this.tableTitles, 0)
                    }

                }
            }
        },
        methods: {
            // 获得下拉框列表
            getSelectList(tableTitles = []) {
                tableTitles.forEach((tableColumn) => {

                    if (tableColumn.children && tableColumn.children.length > 0) {
                        this.getSelectList(tableColumn)
                    } else if (this.selectTypes.includes(tableColumn.controlType)) {
                        let params = {
                            codeId: tableColumn.codeId
                        }
                        rbCmdbModuleService.getRefModuleDict({ params: params }).then((data) => {
                            this.$set(this.selectOptions, tableColumn.key, data)
                        })
                    }
                })
            },
            // 一行数据校验
            rowValid(tableTitles = [], rowIndex = 0) {
                tableTitles.forEach((tableColumn) => {

                    if (tableColumn.children && tableColumn.children.length > 0) {
                        this.rowValid(tableColumn.children, rowIndex)
                    } else {

                        this.validRules(tableColumn, this.tableDatas[rowIndex][tableColumn.key], rowIndex)

                    }
                })

            },

            // 校验规则(列属性,值,行数)
            validRules(column, val = '', rowIndex = '') {
                if (!this.validErrors[column.key]) {
                    this.$set(this.validErrors, column.key, {})
                }

                if (column.rules && column.rules.length > 0) {
                    column.rules.some(async (ruleItem) => {
                        let valid = await this.validRulesDetails(column, val, ruleItem)
                        if (!valid.status) {
                            this.$set(this.validErrors[column.key], rowIndex, valid.message)
                            this.changeTable()
                            return true
                        } else {
                            if (rowIndex in this.validErrors[column.key]) {
                                this.$delete(this.validErrors[column.key], rowIndex)
                                this.changeTable()
                            }
                            return false
                        }
                    })
                }
            },
            // 详细校验规则
            async validRulesDetails(column, val, ruleItem) {
                let valid = { status: true }
                switch (ruleItem.validType) {
                    case 'fromConfig':
                        valid = this.validRulesByConfig(column, val, ruleItem)
                        break
                    case 'fromFun':
                        valid = await this.validRulesByFun(column, val, ruleItem)
                        break
                }
                return valid
            },
            // 前端判断
            validRulesByConfig(column, val, ruleItem) {
                if (val && val.trim()) {
                    return { status: true }
                } else {
                    return { status: false, message: ruleItem.message || '数据格式错误' }
                }
            },
            // 接口动态判断
            validRulesByFun(column, val, ruleItem) {
                if (!column.codeId) {
                    return { status: true }
                }
                let params = {

                }
                params[column.codeId] = val
                return rbCmdbReportService.validDatas(params).then((res) => {
                    if (res[column.codeId] && res[column.codeId].flag !== 'success') {
                        return { status: false, message: res[column.codeId].msg || '数据格式错误' }
                    } else {
                        return { status: true }
                    }

                })

            },
            // 表格变更(数据、字段、校验规则等)
            changeTable() {
                this.$emit('changeTable', { validErrors: this.validErrors })
            },
            // 初始化
            init() {
                this.getSelectList(this.tableTitles)
            }
        },
        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>
