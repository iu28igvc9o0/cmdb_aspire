<template>
    <el-dialog title="批量修正"
               class="yw-dialog"
               :visible.sync="batchShow"
               @close="resetDialog"
               width="1150px"
               v-loading="loading"
               element-loading-text="正在查询数据, 请稍等...">

        <el-dialog width="30%"
                   class="yw-dialog"
                   title="提示"
                   :visible.sync="innerVisible"
                   @close="resetDialogLit"
                   v-loading="loading"
                   element-loading-text="正在查询数据, 请稍等..."
                   append-to-body>
            <div style="text-align: center;margin:10px">系统共检测到{{Total}}条记录符合查询条件,是否确认更新?</div>

            <section class="btn-wrap"
                     style="left: calc(50% - 70px);">
                <el-button type="primary"
                           @click="submitLit()">确定</el-button>
                <el-button @click="resetDialogLit()">取消</el-button>
            </section>
        </el-dialog>
        <batch-update-dialog v-if="processInfo.showProcess"
                             :processInfo="processInfo">
        </batch-update-dialog>
        <section class="yw-dialog-main">
            <el-form ref="rulesFrom"
                     class="yw-form is-required"
                     :inline="true"
                     label-position="right"
                     label-width="100px"
                     v-model="editList"
                     :rules="rules">
                <el-row class="diaTitle">筛选条件</el-row>
                <el-row>
                    <el-col :span="12"
                            style="width:33.3%;display: inline-table;">
                        <el-form-item label="资源类型">
                            <!-- <el-select clearable filterable v-model="filterText" placeholder="请选择" collapse-tags @change="selectChange" @clear="refModuleIdClear">
                                <el-option :value="mineStatusValue" style="height: auto">
                                    <el-tree :data="reportData" node-key="id" ref="tree" highlight-current :props="defaultProps" @check-change="handleCheckChange"  @node-click="handleNodeClick"></el-tree>
                                </el-option>
                            </el-select> -->

                            <el-select v-model="filterText"
                                       :disabled="currModuleId"
                                       @blur="blur"
                                       :filter-method="filterSelect"
                                       clearable
                                       filterable
                                       placeholder="请选择资源类型"
                                       collapse-tags
                                       @change="selectChange"
                                       @clear="refModuleIdClear">
                                <el-option :value="mineStatusValue"
                                           style="height: auto">
                                    <el-tree :filter-node-method="filterNode"
                                             :data="reportData"
                                             node-key="id"
                                             ref="tree"
                                             highlight-current
                                             :props="defaultProps"
                                             @check-change="handleCheckChange"
                                             @node-click="handleNodeClick"></el-tree>
                                </el-option>
                            </el-select>

                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-for="(column, idx) in bindColumns"
                        :key="idx">
                    <el-col :span="12"
                            style="width:33.3%;">
                        <el-form-item prop="filed"
                                      label="码表名称">
                            <el-select v-model="bindColumns[idx]['filed']"
                                       placeholder="请选码表名称"
                                       filterable
                                       clearable>
                                <el-option v-for="item in codeTypeNameList"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.filedCode"
                                           @click.native="codeNameClick(bindColumns[idx].filed,idx)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            style="width:33.3%;">
                        <el-form-item prop="operator"
                                      label="查询条件">
                            <el-select v-model="bindColumns[idx]['operator']"
                                       placeholder="请选查询条件"
                                       filterable
                                       clearable>
                                <el-option v-for="item in conditionSettingTypeList"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.value"
                                           @click.native="sqLClick(bindColumns[idx].operator,idx,item)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12"
                            style="width:33.3%;"
                            class="queryStyle">
                        <el-form-item prop="value"
                                      label="查询值">
                            <el-input clearable
                                      :placeholder="bindColumns[idx].placeholderOperator"
                                      @click.native="valueClick(bindColumns[idx].value,idx)"
                                      v-model="bindColumns[idx]['value']"></el-input>
                            <i class="el-icon-circle-plus-outline ICON"
                               v-show="showPlus()"
                               @click="plus(idx)"></i>
                            <i class="el-icon-remove-outline ICON"
                               v-show="showMouse(idx)"
                               @click="mouse(idx)"></i>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row class="diaTitle">修正配置</el-row>
                <el-row>
                    <el-col :span="12"
                            style="width:33.3%;">
                        <el-form-item prop="filed"
                                      label="码表名称">
                            <el-select v-model="editList.update.codeId"
                                       placeholder="请选择码表名称"
                                       filterable
                                       clearable>
                                <template v-for="item in codeTypeNameList">
                                    <el-option v-if="item.updateReadOnly != 1"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"
                                               @click.native="filedClick(item)"></el-option>
                                </template>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            style="width: 66.7%;">
                        <el-form-item prop="value"
                                      label="修正值">
                            <el-input type="textarea"
                                      clearable
                                      placeholder="请输入修正值"
                                      v-model="editList.update.value"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </section>
        <section class="btn-wrap"
                 style="left: calc(50% - 70px);">
            <el-button type="primary"
                       @click="submit()">确定</el-button>
            <el-button @click="resetDialog('rulesFrom')">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        name: 'BatchEdit',
        props: ['batchShowChild', 'currModuleId'],
        components: {
            BatchUpdateDialog: () => import('src/pages/resource/iframe/list/batch_update_dialog.vue')
        },
        data() {
            return {
                loading: false,
                filterText: '',
                Total: 0,
                innerVisible: false,
                bindColumns: [{
                    'filed': '', // 码表名称
                    'operator': '', // 查询条件
                    'value': '',
                    'placeholderOperator': '请输入默认值'
                }],
                reportData: [], // 模型名称
                defaultProps: {
                    children: 'childModules',
                    label: 'name',
                    id: 'id'
                },
                currentKey: '',
                batchShow: this.batchShowChild,
                batchShowChildS: false,
                codeTypeNameList: [], // 码表名称
                codeTypeNameListUpdata: [], // 码表名称
                conditionSettingTypeList: [], // 查询条件
                moduleId: '',
                editList: {
                    querys: [
                        {
                            'filed': '', // 筛选条件-码表名称
                            'operator': '', // 筛选条件-查询条件
                            'value': '' // 筛选条件-查询值
                        }
                    ],
                    update: {
                        codeId: '', // 修正配置-码表id
                        filed: '', // 修正配置-码表名称
                        value: '' // 修正配置-修正值
                    }
                },
                processInfo: {
                    showProcess: false,
                    processId: ''
                }

            }
        },

        mounted: function () {

            this.Dictionaries()
        },
        watch: {
            batchShowChild: {
                handler(newVal) {
                    this.batchShow = newVal
                },
                immediate: true,
                deep: true
            },
            filterText(val) {
                this.$refs.tree.filter(val)
            }

        },
        methods: {
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            filterSelect(value) {
                this.$refs.tree.filter(value)
            },
            blur() {
                this.$refs.tree.filter('')
            },
            handleNodeClick(e) {
                this.codeTypeNameList = []
                this.filterText = e.name
                this.moduleId = e.id
                this.editList.update.codeId = ''
                for (let i = 0; i < this.bindColumns.length; i++) {
                    this.bindColumns[i].filed = ''
                }
                tableManagement.getModuleDetailList(e.id).then(res => {
                    for (let item in res) {
                        this.codeTypeNameList.push({
                            label: res[item].filedName,
                            value: res[item].codeId,
                            filedCode: res[item].filedCode
                        })
                    }
                })
            },
            filedClick(data) {
                this.editList.update.filed = data.filedCode
            },

            refModuleIdClear() {
                this.codeTypeNameList = []
                this.editList.update.codeId = ''
                for (let i = 0; i < this.bindColumns.length; i++) {
                    this.bindColumns[i].filed = ''
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/code/getDistinctCodeList'
                }).then((res) => {
                    for (let item in res) {
                        this.codeTypeNameList.push({
                            label: res[item].filedName,
                            value: res[item].codeId,
                            filedCode: res[item].filedCode
                        })
                    }
                })
            },
            Dictionaries() {
                console.log(this.currModuleId)
                // 模型名称
                tableManagement.getTreeList('').then(res => {
                    this.reportData = res
                    this.currentKey = this.currModuleId
                    this.$nextTick(() => {
                        this.$refs.tree.setCurrentKey(this.currModuleId)
                        let e = this.$refs.tree.getCurrentNode()
                        console.log(e)
                        this.filterText = e.name
                        this.handleNodeClick(e)
                    })


                })
                // 码表名称
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/code/getDistinctCodeList'
                }).then((res) => {
                    this.codeTypeNameList = []
                    for (let item in res) {
                        this.codeTypeNameList.push({
                            label: res[item].filedName,
                            value: res[item].codeId,
                            filedCode: res[item].filedCode
                        })
                    }
                })
                // 查询条件
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'sql_operater' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.conditionSettingTypeList.push({
                            lable: res[item].id,
                            value: res[item].name
                        })
                    }
                })
            },

            plus(idx) {
                this.bindColumns.splice(idx + 1, 0, { 'filed': '', 'operator': '', 'value': '' })
                // eslint-disable-next-line no-unused-vars
                let arr1 = this.bindColumns.map(item => {
                    return Object.assign(item)
                })
                this.bindColumns[idx + 1].placeholderOperator = '请输入默认值'
                // this.placeholderOperator = '请输入默认值'
                this.editList.querys = this.bindColumns
            },
            showPlus() {
                return true
            },
            mouse(idx) {
                this.bindColumns.splice(idx, 1)
            },
            showMouse(idx) {
                return (this.bindColumns.length > 1 && idx === 0) || idx > 0
            },
            codeNameClick(data, index) {
                for (var i = 0; i < this.bindColumns.length; i++) {
                    var item = this.bindColumns[i]
                    if (item.filed === '' && index === i) {
                        item.filed = data
                    }
                }
                this.editList.querys = this.bindColumns
            },

            sqLClick(data, index, im) {
                console.log(index, im)
                if (im.value === 'in' || im.value === 'not in' || im.value === 'bewteen') {
                    this.bindColumns[index].placeholderOperator = '多个查询值之间用英文逗号隔开'
                    this.bindColumns[index].value = '0'
                    this.bindColumns[index].value = ''
                } else {
                    this.bindColumns[index].value = '0'
                    this.bindColumns[index].value = ''
                    this.bindColumns[index].placeholderOperator = '请输入默认值'
                }

                for (var i = 0; i < this.bindColumns.length; i++) {
                    var item = this.bindColumns[i]
                    if (item.operator === '' && index === i) {
                        item.operator = data
                    }
                }
                this.editList.querys = this.bindColumns
            },
            valueClick(data, index) {
                for (var i = 0; i < this.bindColumns.length; i++) {
                    var item = this.bindColumns[i]
                    if (item.value === '' && index === i) {
                        item.value = data
                    }
                }
                this.editList.querys = this.bindColumns
            },
            // 保存
            submit() {
                for (let item in this.editList.querys) {
                    let itemQuery = this.editList.querys[item]

                    var newArr = []
                    if (this.bindColumns.length > 0) {
                        for (let items in this.bindColumns) {
                            if (this.bindColumns[items].filed !== '') {
                                newArr.push(this.bindColumns[items].filed)
                            }
                        }
                    }

                    var nary = newArr.sort()
                    nary.pop()

                    if (new Set(nary).size !== nary.length) {
                        this.$message.error('码表名称是唯一的不能重复,请重新选择')
                        return
                    } else if (itemQuery.filed && itemQuery.operator && this.filterText !== '') {
                        this.loading = true
                        tableManagement.getBatchUpdateCount(this.editList, this.moduleId).then(res => {
                            this.loading = false
                            this.Total = res
                            this.innerVisible = true
                        }).catch((res) => {
                            this.loading = false
                            this.$message.error(res.data.message)
                        })
                        return
                    } else if (this.filterText === '') {
                        this.$message.error('资源类型不能为空')
                        return
                    } else if (this.editList.querys[0].filed === '' || this.editList.querys[0].operator === '') {
                        this.$message.error('筛选条件必须大于或者等于一条, 并且"码表名称"～"查询条件"不能为空')
                        return
                    }
                }
            },
            // 取消
            resetDialog() {
                this.batchShow = false
                this.$emit('batchShowChildS', this.batchShow)
            },
            // 内层确定
            submitLit() {
                this.loading = true
                tableManagement.getBatchUpdateInstance(this.editList, this.moduleId).then(res => {
                    if (res.flag === 'true') {
                        this.processInfo.showProcess = true
                        this.processInfo.processId = res.processId
                        this.innerVisible = false
                        this.$parent.query()
                    } else {
                        this.$message.error('启动批量更新失败！')
                    }
                    // if (res.success === true) {
                    //     this.loading = false
                    //     this.innerVisible = false
                    // this.batchShow = false
                    // this.$message.success('已批量录入配置审核！')
                    // } else {
                    //     this.loading = false
                    // this.$message.error(res.message)
                    // }
                }).finally(() => {
                    this.loading = false
                })
            },
            resetDialogLit() {
                this.innerVisible = false
            }

        }
    }
</script>
<style lang="scss" scoped>
    .diaTitle {
        height: 30px;
        line-height: 30px;
        padding-left: 10px;
        color: #000;
        font-weight: 500;
    }
    .ICON {
        cursor: pointer;
        color: #409eff;
        font-size: 15px;
    }
</style>
<style lang="stylus">
    .queryStyle .el-form-item__label {
        width: 70px !important;
    }

    .queryStyle .el-form-item__content > .el-input {
        width: 220px;
    }
</style>
