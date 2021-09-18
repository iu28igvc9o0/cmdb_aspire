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
            <div style="text-align: center;margin:10px"
                 v-if="Total==0">系统共检测到{{Total}}条记录符合查询条件</div>
            <div style="text-align: center;margin:10px"
                 v-else>系统共检测到{{Total}}条记录符合查询条件,是否确认更新?</div>

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
        <section class="section-content">
            <el-steps :active="stepNumber">
                <el-step title="选择修改配置项"></el-step>
                <el-step title="设置查询条件"></el-step>
                <el-step title="设置修正值"></el-step>
            </el-steps>
            <el-form ref="rulesFrom"
                     class="yw-form is-required"
                     :inline="true"
                     label-position="right"
                     label-width="100px"
                     :model="rulesFrom"
                     :rules="rules">
                <div class="content-cover">
                    <div class="content-between contentLeft">
                        <el-form-item prop="editModal"
                                      label="修改模型">
                            <!-- <el-select v-model="rulesFrom.editModal"
                                       placeholder="请选择模型"
                                       filterable
                                       clearable>
                                <el-option v-for="item in modalList"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.filedCode"></el-option>
                            </el-select> -->
                            <el-select v-model="rulesFrom.editModal"
                                       :disabled="currModuleId"
                                       @blur="blur"
                                       :filter-method="filterSelect"
                                       clearable
                                       filterable
                                       placeholder="请选择资源类型"
                                       collapse-tags
                                       @clear="refModuleIdClear">
                                <el-option style="height: auto"
                                           :value="valueTitle"
                                           :label="valueTitle">
                                    <el-tree :filter-node-method="filterNode"
                                             style="font-weight:400;"
                                             :data="reportData"
                                             node-key="id"
                                             ref="tree"
                                             highlight-current
                                             :props="defaultProps"
                                             @node-click="handleNodeClick"></el-tree>
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="configItems"
                                      label="修改配置项">
                            <!-- <el-select v-model="rulesFrom.configItems"
                                       placeholder="请选择配置项"
                                       filterable
                                       clearable>
                                <el-option v-for="item in configItemsList"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.filedCode"></el-option>
                            </el-select> -->
                            <el-select v-model="rulesFrom.configItems"
                                       multiple
                                       collapse-tags
                                       filterable
                                       clearable
                                       @change="leftChange"
                                       placeholder="请选择配置项">
                                <el-option v-for="(item, index)  in configItemsList"
                                           :key="index"
                                           :label="item.label"
                                           :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </div>
                    <div class="content-center">
                        <div v-show="centerShow">
                            <!-- 动态列表 -->
                            <div v-for="(item, index) in otherEditList"
                                 :key="index">
                                <el-form-item label="">
                                    <el-select v-model="item.department"
                                               class="letter-select"
                                               placeholder="请选择"
                                               filterable
                                               :disabled="true"
                                               clearable>
                                        <el-option v-for="item1 in departmentList"
                                                   :key="item1.value"
                                                   :label="item1.label"
                                                   :value="item1.value"></el-option>
                                    </el-select>
                                    <el-select v-model="item.judge"
                                               v-if="item.status == 0"
                                               class="letter-select letter-select-center"
                                               placeholder="请选择查询条件"
                                               disabled>
                                        <el-option v-for="item1  in judgeList"
                                                   :key="item1.value"
                                                   :label="item1.label"
                                                   :value="item1.value"></el-option>
                                    </el-select>
                                    <el-select v-model="item.judge"
                                               v-if="item.status == 1"
                                               class="letter-select letter-select-center"
                                               placeholder="请选择查询条件"
                                               disabled>
                                        <el-option v-for="item1  in judgeList"
                                                   :key="item1.value"
                                                   :label="item1.label"
                                                   :value="item1.value"></el-option>
                                    </el-select>
                                    <el-select v-model="item.judge"
                                               v-if="item.status == 2"
                                               class="letter-select letter-select-center"
                                               placeholder="请选择查询条件"
                                               filterable
                                               clearable>
                                        <el-option v-for="item1  in judgeListOther"
                                                   :key="item1.value"
                                                   :label="item1.label"
                                                   :value="item1.value"></el-option>
                                    </el-select>
                                    <el-select v-model="item.twoDepartment"
                                               v-if="item.status == 0"
                                               class="letter-select letter-select-right"
                                               placeholder="请选择"
                                               filterable
                                               clearable>
                                        <el-option v-for="item1 in item.twoDepartmentList"
                                                   :key="item1.id"
                                                   :label="item1.value"
                                                   :value="item1.value"></el-option>
                                    </el-select>
                                    <el-date-picker v-model="item.twoDepartment"
                                                    v-if="item.status == 1"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    clearable>
                                    </el-date-picker>
                                    <el-input v-model="item.twoDepartment"
                                              v-if="item.status == 2"
                                              placeholder="请输入"
                                              clearable>
                                    </el-input>
                                    <i class="el-icon-remove-outline icon-btn"
                                       @click="reduceOtherEdit(index)"></i>
                                </el-form-item>
                            </div>
                            <!-- 固定增加 -->
                            <div>
                                <el-form-item label="">
                                    <el-select v-model="rulesFrom.department"
                                               class="letter-select"
                                               placeholder="请选择查询条件"
                                               filterable
                                               clearable>
                                        <el-option v-for="item in departmentList"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="item.value"></el-option>
                                    </el-select>
                                    <i class="el-icon-circle-plus-outline icon-btn"
                                       @click="addOtherEdit"></i>
                                </el-form-item>
                            </div>
                        </div>
                    </div>
                    <div class="content-between">
                        <!-- <div class="right-list"
                             v-for="(item, index) in rightList"
                             :key="index">
                            <el-form-item :label="item.name">
                                <el-select v-model="item.value"
                                           :placeholder="'请选择'+item.name"
                                           filterable
                                           clearable>
                                    <el-option v-for="item1 in item.list"
                                               :key="item1.value"
                                               :label="item1.label"
                                               :value="item1.filedCode"></el-option>
                                </el-select>
                            </el-form-item>
                        </div> -->
                        <!-- 遍历右侧选择 -->
                        <div v-for="(item, index) in rightList"
                             :key="index">
                            <el-form-item :label="item.name">
                                <el-select v-model="item.twoDepartment"
                                           v-if="item.status == 0"
                                           class="letter-select"
                                           placeholder="请选择"
                                           @change="changeSingal($event,item.department)"
                                           filterable
                                           clearable>
                                    <el-option v-for="item1 in item.twoDepartmentList"
                                               :key="item1.id"
                                               :label="item1.value"
                                               :value="item1.id"></el-option>
                                </el-select>
                                <el-date-picker v-model="item.twoDepartment"
                                                v-if="item.status == 1"
                                                type="datetimerange"
                                                range-separator="至"
                                                value-format="yyyy-MM-dd HH:mm:ss"
                                                clearable>
                                </el-date-picker>
                                <el-input v-model="item.twoDepartment"
                                          v-if="item.status == 2"
                                          placeholder="请输入"
                                          clearable>
                                </el-input>
                            </el-form-item>
                        </div>

                        <!-- 新增下拉 -->
                        <!-- <div v-for="(item, index) in rightBottomList"
                             :key="index+'0'">
                            <el-form-item :label="item.name">
                                <el-select v-model="item.twoDepartment"
                                           v-if="item.status == 0"
                                           class="letter-select"
                                           placeholder="请选择"
                                           @change="changeSingal($event,2,item.department)"
                                           filterable
                                           clearable>
                                    <el-option v-for="item1 in item.twoDepartmentList"
                                               :key="item1.id"
                                               :label="item1.value"
                                               :value="item1.id"></el-option>
                                </el-select>
                                <el-date-picker v-model="item.twoDepartment"
                                                v-if="item.status == 1"
                                                type="daterange"
                                                range-separator="至"
                                                value-format="yyyy-MM-dd"
                                                clearable>
                                </el-date-picker>
                                <el-input v-model="item.twoDepartment"
                                          v-if="item.status == 2"
                                          placeholder="请输入"
                                          clearable>
                                </el-input>
                            </el-form-item>
                        </div> -->
                    </div>
                </div>
            </el-form>
            <div class="footer-cover">
                <span slot="footer"
                      class="dialog-footer">
                    <el-button @click="batchShow = false"
                               style="margin-right:30px;">取 消</el-button>
                    <el-button type="primary"
                               @click="submitRes('rulesFrom')">确 定</el-button>
                </span>
            </div>
        </section>
    </el-dialog>
</template>

<script>
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        name: 'BatchEdit',
        props: ['batchShowChild', 'currModuleId'],
        components: {
            BatchUpdateDialog: () => import('src/pages/resource/iframe/list/batch_update_dialog.vue')
        },
        data() {
            return {
                judgeListOther: [],
                Total: 0,
                innerVisible: false,
                judge1: '',
                judge2: '',
                moduleId: '',
                centerShow: false,
                loading: false,
                batchShow: this.batchShowChild,
                batchShowChildS: false,
                stepNumber: 0,
                valueTitle: '',
                rulesFrom: {
                    editModal: '',
                    configItems: '',
                    department: ''
                },
                rightList: [],
                rightBottomList: [],
                rules: {
                    editModal: [
                        { required: true, message: '请选择模型', trigger: 'change' }
                    ],
                    configItems: [
                        { required: true, message: '请选择配置项', trigger: 'change' }
                    ]
                },
                inputArr: ['=', 'between', ''],
                // 模型
                modalList: [],
                // 配置项
                configItemsList: [],
                // 组织
                departmentList: [],
                judgeList: [],
                twoDepartmentList: [],
                otherEditList: [
                    // {
                    //     department: '',
                    //     judge: '',
                    //     twoDepartment: ''
                    // }
                ],
                reportData: [], // 模型名称
                defaultProps: {
                    children: 'childModules',
                    label: 'name',
                    id: 'id'
                },
                controlType: '',
                currentRight: [],
                editList: null,
                processInfo: {
                    showProcess: false,
                    processId: ''
                }
            }
        },
        computed: {
            editModal() {
                return this.rulesFrom.editModal
            },
            configItems() {
                return this.rulesFrom.configItems
            }
        },
        mounted: function () {
            console.log(this.currModuleId)
            this.$nextTick(() => {
                this.$refs['rulesFrom'].clearValidate()
            })
            this.Dictionaries()
            this.getCenterIf()
        },
        watch: {
            editModal(newValue) {
                console.log(newValue)
                if (newValue != '') {
                    this.centerShow = true
                } else {
                    this.centerShow = false
                    this.stepNumber = 0
                }
            },
            configItems(newValue) {
                console.log(newValue)
                if (newValue != '') {
                    this.stepNumber = 1
                } else {
                    this.stepNumber = 0
                }
            },
            otherEditList(newValue) {
                console.log(newValue)
                if (this.rulesFrom.configItems != '' && newValue.length > 0) {
                    this.stepNumber = 2
                } else {
                    if (this.rulesFrom.configItems != '') {
                        this.stepNumber = 1
                    } else {
                        this.stepNumber = 0
                    }
                }
            },
        },
        methods: {
            submitLit() {
                if (this.Total > 0) {
                    this.loading = true
                    tableManagement.getBatchUpdateInstance(this.editList, this.moduleId).then(res => {
                        if (res.flag === 'true') {
                            this.processInfo.showProcess = true
                            this.processInfo.processId = res.processId
                            this.innerVisible = false
                            // this.batchShow = false
                            this.$message({
                                message: res.msg || res.message,
                                type: 'success'
                            })
                            this.$parent.query()
                        } else {
                            this.$message.error('启动批量更新失败！')
                        }
                    }).finally(() => {
                        this.loading = false
                    })
                } else {
                    this.innerVisible = false
                    this.batchShow = false
                }

            },
            changeSingal(e, id) {
                console.log(e)
                let arr = this.currentRight
                console.log(this.currentRight)
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].codeId == id) {
                        let newArr = arr[i].cascadeList
                        console.log(newArr)
                        for (let j = 0; j < newArr.length; j++) {
                            let sqlstr = newArr[j].sqlString
                            let sqlstrt = sqlstr.replace('?', e)
                            this.cascadeRes(sqlstrt, newArr[j].subCodeId)
                        }
                    }
                }
            },
            // 级联关系接口
            cascadeRes(sqlstr, id) {
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: { 'sql': sqlstr },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    console.log(res)
                    let arr = this.rightList
                    for (let i = 0; i < arr.length; i++) {
                        if (arr[i].department == id) {
                            this.$set(this.rightList, i, {
                                department: this.rightList[i].department,
                                name: this.rightList[i].name,
                                judge: '',
                                twoDepartment: '',
                                twoDepartmentList: res,
                                status: this.rightList[i].status,
                            })
                        }
                    }
                })
            },
            // 都非
            noneHasType(type, name, status, num, otherEditList) {
                this.rbHttp.sendRequest({
                    method: 'post',
                    data: { 'sql': type },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    console.log(res)
                    this.$set(this[otherEditList], num, {
                        department: name,
                        name: this[otherEditList][num].name,
                        judge: this.inputArr[0],
                        twoDepartment: '',
                        twoDepartmentList: res,
                        status: status
                    })
                })
            },
            // 数据字典
            hasModuleType(type, name, status, num, otherEditList) {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': type },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    console.log(res)
                    this.$set(this[otherEditList], num, {
                        department: name,
                        name: this[otherEditList][num].name,
                        judge: this.inputArr[0],
                        twoDepartment: '',
                        twoDepartmentList: res,
                        status: status
                    })
                })
            },
            // 引用模型
            handleRefModuleType(value, name, status, num, otherEditList) {
                console.log(status)
                // 处理引用模型类型
                let refModuleId = value.codeBindSource.refModuleId
                let refFiledCode = value.codeBindSource.showModuleCodeId
                let filedCode = value.filedCode
                if (!(refModuleId && refFiledCode)) {
                    this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                    return
                }
                let params = {
                    codeId: value.codeId
                }
                rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                    console.log(res)
                    this.$set(this[otherEditList], num, {
                        department: name,
                        name: this[otherEditList][num].name,
                        judge: this.inputArr[0],
                        twoDepartment: '',
                        twoDepartmentList: res,
                        status: status
                    })
                }))
            },
            // 查询条件
            getCenterIf() {
                // 查询条件
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'sql_operate' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        console.log(res[item].value)
                        if (res[item].value == '=') {
                            this.judge1 = res[item].value
                        } else if (res[item].value == 'between') {
                            this.judge2 = res[item].value
                        }
                        this.judgeList.push({
                            lable: res[item].id,
                            value: res[item].value
                        })
                        if (res[item].value == '=' || res[item].value == 'like') {
                            this.judgeListOther.push({
                                lable: res[item].id,
                                value: res[item].value
                            })
                        }
                    }
                })
            },
            leftChange(event, item) {
                console.log(event)
                console.log(item)
                this.rightList = []
                this.currentRight = []
                let status
                tableManagement.getCasParentCodes(event).then(res => {
                    this.currentRight = res
                    console.log(res)
                    if (res.length > 0) {
                        for (let i = 0; i < res.length; i++) {

                            if (res[i].controlType.controlCode == 'cascader' || res[i].controlType.controlCode == 'listSel') {
                                status = 0
                            } else if (res[i].controlType.controlCode == 'dateTime') {
                                status = 1
                            } else {
                                status = 2
                            }
                            if (res[i].isBindSource == '是') {
                                this.rightList.push({
                                    department: res[i].codeId,
                                    name: res[i].filedName,
                                    judge: '',
                                    twoDepartment: '',
                                    twoDepartmentList: [],
                                    status: status
                                })
                                if (res[i].codeBindSource.bindSourceType == '引用模型') {

                                    this.handleRefModuleType(res[i], res[i].codeId, status, i, 'rightList')
                                } else if (res[i].codeBindSource.bindSourceType == '数据字典') {
                                    this.hasModuleType(res[i].codeBindSource.dictSource, res[i].codeId, status, i, 'rightList')
                                } else {
                                    this.noneHasType(res[i].codeBindSource.tableSql, res[i].codeId, status, i, 'rightList')
                                }
                            } else {
                                this.rightList.push({
                                    department: res[i].codeId,
                                    name: res[i].filedName,
                                    judge: '',
                                    twoDepartment: '',
                                    twoDepartmentList: [],
                                    status: status
                                })
                            }
                        }
                        // console.log(newarr)
                        // this.rightBootmRes(event)

                    } else {
                        // this.rightBootmRes(event)
                    }

                })

            },
            blur() {
                this.$refs.tree.filter('')
            },
            havequchong(arr) {
                var obj = {}
                let newarr = arr.reduce(function (item, next) {
                    obj[next.value] ? '' : obj[next.value] = true && item.push(next)
                    return item
                }, [])
                return newarr
            },
            handleNodeClick(e) {
                this.departmentList = []
                this.configItemsList = []
                let configItemsList = []
                let departmentList = []
                this.rightList = []
                this.otherEditList = []
                if (this.rulesFrom.configItems != '') {
                    this.rulesFrom.configItems = ''
                }
                this.rulesFrom.department = ''
                this.rulesFrom.editModal = e.name
                this.moduleId = e.id
                tableManagement.getModuleDetailList(e.id).then(res => {
                    console.log(this.configItemsList)
                    for (let item in res) {
                        // 是否显示
                        if (res[item].updateReadOnly != 1) {
                            configItemsList.push({
                                label: res[item].filedName,
                                value: res[item].codeId,
                                filedCode: res[item].filedCode,
                                all: res[item]
                            })
                            departmentList.push({
                                label: res[item].filedName,
                                value: res[item].codeId,
                                filedCode: res[item].filedCode,
                                all: res[item]
                            })
                        } else {
                            departmentList.push({
                                label: res[item].filedName,
                                value: res[item].codeId,
                                filedCode: res[item].filedCode,
                                all: res[item]
                            })
                        }
                    }
                })
                this.configItemsList = configItemsList
                this.departmentList = departmentList
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
                        if (e != null) {
                            this.rulesFrom.editModal = e.name
                            this.handleNodeClick(e)
                        }
                    })
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            refModuleIdClear() {
                this.departmentList = []
                this.configItemsList = []
                this.rulesFrom.department = ''
            },
            filterSelect(value) {
                this.$refs.tree.filter(value)
            },
            // 提交按钮
            submitRes(formName) {
                console.log(formName)
                // console.log(this.otherEditList)
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.otherEditList.length > 0) {
                            let checkeefa = this.otherEditList.every(function (item) {
                                return item.judge != ''
                            })
                            if (!checkeefa) {
                                this.$message({
                                    message: '查询条件内容必须选择',
                                    type: 'warning'
                                })
                                return
                            }
                            let boolArray = false
                            for (let m = 0; m < this.otherEditList.length; m++) {
                                if (this.otherEditList[m].judge == 'between') {
                                    if (!Array.isArray(this.otherEditList[m].twoDepartment)) {
                                        boolArray = true
                                    }
                                }
                            }
                            if (boolArray) {
                                this.$message({
                                    message: '条件为between条件后内容必填',
                                    type: 'warning'
                                })
                                return
                            }
                            console.log('submit!')
                            console.log(this.otherEditList)
                            console.log(this.rightList)
                            let otherEditList = this.otherEditList
                            let rightList = this.rightList
                            let json = {
                                querys: [],
                                update: []
                            }
                            let departmentList = this.departmentList
                            console.log(departmentList)
                            for (let i = 0; i < otherEditList.length; i++) {
                                let other = otherEditList[i]
                                if (otherEditList[i].twoDepartmentList.length > 0) {
                                    console.log('2323')
                                    console.log(otherEditList[i].twoDepartmentList.length)
                                    let twoboolen = false
                                    for (let j = 0; j < other.twoDepartmentList.length; j++) {
                                        if (other.twoDepartmentList[j].value == other.twoDepartment) {
                                            twoboolen = true
                                            for (let k = 0; k < departmentList.length; k++) {
                                                if (departmentList[k].value == otherEditList[i].department) {
                                                    json.querys.push({
                                                        field: departmentList[k].filedCode,
                                                        operator: otherEditList[i].judge,
                                                        value: other.twoDepartmentList[j].id
                                                    })
                                                }
                                            }

                                        }
                                    }
                                    if (!twoboolen) {
                                        for (let k = 0; k < departmentList.length; k++) {
                                            if (departmentList[k].value == otherEditList[i].department) {
                                                json.querys.push({
                                                    field: departmentList[k].filedCode,
                                                    operator: otherEditList[i].judge,
                                                    value: ''
                                                })
                                            }
                                        }
                                    }

                                } else {
                                    if (typeof otherEditList[i].twoDepartment == 'string') {
                                        console.log('string')
                                        for (let k = 0; k < departmentList.length; k++) {
                                            if (departmentList[k].value == otherEditList[i].department) {
                                                json.querys.push({
                                                    field: departmentList[k].filedCode,
                                                    operator: otherEditList[i].judge,
                                                    value: otherEditList[i].twoDepartment
                                                })
                                            }
                                        }

                                    } else {
                                        console.log('object')
                                        for (let k = 0; k < departmentList.length; k++) {
                                            if (departmentList[k].value == otherEditList[i].department) {
                                                json.querys.push({
                                                    field: departmentList[k].filedCode,
                                                    operator: otherEditList[i].judge,
                                                    value: otherEditList[i].twoDepartment.join(',')
                                                })
                                            }
                                        }

                                    }
                                }

                            }
                            let currentRight = this.currentRight
                            console.log(this.currentRight)
                            for (let i = 0; i < rightList.length; i++) {
                                if (rightList[i].twoDepartmentList > 0) {
                                    if (rightList[i].twoDepartment != '') {
                                        for (let j = 0; j < currentRight.length; j++) {
                                            if (currentRight[j].codeId == rightList[i].department) {
                                                json.update.push({
                                                    codeId: rightList[i].department,
                                                    field: currentRight[j].filedCode,
                                                    value: rightList[i].twoDepartment
                                                })
                                            }
                                        }
                                    } else {
                                        for (let j = 0; j < currentRight.length; j++) {
                                            if (currentRight[j].codeId == rightList[i].department) {
                                                json.update.push({
                                                    codeId: rightList[i].department,
                                                    field: currentRight[j].filedCode,
                                                    value: rightList[i].twoDepartment
                                                })
                                            }
                                        }
                                    }
                                } else {
                                    for (let j = 0; j < currentRight.length; j++) {
                                        if (currentRight[j].codeId == rightList[i].department) {
                                            if (typeof rightList[i].twoDepartment == 'string') {
                                                json.update.push({
                                                    codeId: rightList[i].department,
                                                    field: currentRight[j].filedCode,
                                                    value: rightList[i].twoDepartment
                                                })
                                            } else {
                                                json.update.push({
                                                    codeId: rightList[i].department,
                                                    field: currentRight[j].filedCode,
                                                    value: rightList[i].twoDepartment.join(',')
                                                })
                                            }

                                        }
                                    }
                                }

                            }
                            console.log(json)

                            tableManagement.getBatchUpdateCount(json, this.moduleId).then(res => {
                                this.loading = false
                                this.Total = res
                                this.editList = json
                                this.innerVisible = true
                            }).catch((res) => {
                                this.loading = false
                                if (res.data.message) {
                                    this.$message.error(res.data.message)
                                } else {
                                    this.$message.error('服务器异常')
                                }

                            })
                        } else {
                            this.$message({
                                message: '请至少选择一条查询条件',
                                type: 'warning'
                            })
                        }

                    } else {
                        console.log('error submit!!')
                        return false
                    }
                })
            },
            reduceOtherEdit(index) {
                this.otherEditList.splice(index, 1)
            },

            // 新增动态列表
            addOtherEdit() {
                console.log(this.rulesFrom.department)
                console.log(this.otherEditList)
                if (this.rulesFrom.department == '') {
                    this.$message({
                        message: '请选择查询条件',
                        type: 'warning'
                    })
                } else {
                    let otherEditList = this.otherEditList
                    let departBool = false
                    for (let i = 0; i < otherEditList.length; i++) {
                        if (otherEditList[i].department == this.rulesFrom.department) {
                            departBool = true
                        }
                    }
                    if (departBool) {
                        this.$message({
                            message: '查询条件已选择，不能重复',
                            type: 'warning'
                        })
                    } else {
                        if (this.stepNumber == 1) {
                            this.stepNumber = 2
                        }
                        let departmentList = this.departmentList
                        console.log(this.controlType)
                        let status

                        let boolen = false
                        for (let i = 0; i < departmentList.length; i++) {
                            if (this.rulesFrom.department == departmentList[i].all.codeId) {
                                this.controlType = departmentList[i].all.controlType.controlCode
                                if (this.controlType == 'cascader' || this.controlType == 'listSel') {
                                    status = 0
                                } else if (this.controlType == 'dateTime') {
                                    status = 1
                                } else {
                                    status = 2
                                }
                                if (departmentList[i].all.isBindSource == '是') {
                                    boolen = true
                                    this.otherEditList.push({
                                        department: this.rulesFrom.department,
                                        name: departmentList[i].all.filedName,
                                        judge: '',
                                        twoDepartment: '',
                                        twoDepartmentList: [],
                                        status: status
                                    })
                                    if (departmentList[i].all.codeBindSource.bindSourceType == '引用模型') {

                                        this.handleRefModuleType(departmentList[i].all, this.rulesFrom.department, status, this.otherEditList.length - 1, 'otherEditList')
                                    } else if (departmentList[i].all.codeBindSource.bindSourceType == '数据字典') {
                                        this.hasModuleType(departmentList[i].all.codeBindSource.dictSource, this.rulesFrom.department, status, this.otherEditList.length - 1, 'otherEditList')
                                    } else {
                                        this.noneHasType(departmentList[i].all.codeBindSource.tableSql, this.rulesFrom.department, status, this.otherEditList.length - 1, 'otherEditList')
                                    }
                                }
                            }
                        }
                        if (!boolen) {
                            this.otherEditList.push({
                                department: this.rulesFrom.department,
                                judge: this.inputArr[status],
                                twoDepartment: '',
                                twoDepartmentList: [],
                                status: status
                            })
                        }
                    }
                }
            },
            // 取消
            resetDialog() {
                this.batchShow = false
                this.$emit('batchShowChildS', this.batchShow)
            },
            resetDialogLit() {
                this.innerVisible = false
            }
        }
    }
</script>
<style lang="scss" scoped>
    /deep/ div.el-select {
        width: 180px;
    }
    /deep/ .el-form-item__label {
        padding: 0;
    }
    /deep/ .el-select__tags {
        flex-wrap: initial;
    }
    /deep/ .yw-form .el-form-item__content > .el-input {
        width: 180px;
    }
    /deep/ .el-select-dropdown__item {
        padding: 0 !important;
    }
    /deep/ .el-select-dropdown__item.hover,
    .el-select-dropdown__item:hover {
        background-color: #fff !important;
    }
    /deep/ .el-date-editor.el-date-editor--datetimerange.el-input__inner {
        width: 180px;
    }
    /deep/ .el-date-editor.el-date-editor--datetimerange .el-range-input {
        width: 60px;
    }
    .footer-cover {
        width: 100%;
        position: relative;
        overflow: hidden;
        height: 30px;
        margin-top: 20px;
    }
    .dialog-footer {
        margin: 0 auto;
        display: inline-block;
        position: absolute;
        transform: translate(-50%, 0);
        left: 50%;
    }
    .section-content {
        max-height: 400px;
        overflow: auto;
    }
    /deep/ .content-center .letter-select {
        width: 140px;
    }
    /deep/ .content-center .letter-select-center {
        width: 100px;
    }
    /deep/ .content-center .letter-select-right {
        width: 180px;
    }
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
    .content-cover {
        width: 100%;
        display: flex;
        justify-content: center;
        .content-between {
            width: 28%;
            border-right: 1px solid #c0c4cc;
            box-sizing: border-box;
        }
        .content-between:last-child {
            border: none;
        }
        .content-center {
            flex: 1;
            border-right: 1px solid #c0c4cc;
            box-sizing: border-box;
            padding-left: 20px;
        }
        .icon-btn {
            font-size: 20px;
            color: #409eff;
            cursor: pointer;
        }
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
