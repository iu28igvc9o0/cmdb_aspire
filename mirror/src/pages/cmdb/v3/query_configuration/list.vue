<template>
    <div class="components-container"
         v-loading="loading"
         :element-loading-text="loadingText">
        <el-form class="components-condition yw-form query_configuration"
                 style="margin:0px 0px"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="配置地址">
                <el-input v-model="condicationName"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="配置类型">
                <el-select v-model="condicationType"
                           placeholder="请选择配置类型"
                           clearable
                           filterable>
                    <el-option v-for="item in conditionSettingTypeList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询</el-button>
            </section>
        </el-form>

        <el-form class="yw-form query_configuration"
                 style="margin:10px 0px">
            <el-form class="table_Button">
                <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="add()">新增</el-button>
            </el-form>

            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          ref="multipleTable"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="handleSelectionChange"
                          @row-dblclick="dblHandleCurrentChange($event)"
                          :header-cell-style="{background:'#DEE9FC',color:'#53607E'}"
                          height="calc(100vh - 300px)">
                    <el-table-column prop="condicationCode"
                                     label="配置编码"
                                     sortable
                                     :show-overflow-tooltip="true"
                                     width></el-table-column>
                    <el-table-column prop="condicationName"
                                     label="配置地址"
                                     sortable
                                     :show-overflow-tooltip="true"
                                     width></el-table-column>
                    <el-table-column prop="condicationType"
                                     label="配置类型"
                                     :show-overflow-tooltip="true"
                                     width></el-table-column>
                    <el-table-column prop="moduleId"
                                     label="模型名称"
                                     :show-overflow-tooltip="true"
                                     width>
                        <template slot-scope="scope">
                            <span>{{scope.row.simpleModule?scope.row.simpleModule.name:''}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="displayName"
                                     label="接入用户"
                                     :show-overflow-tooltip="true"
                                     width>
                        <template slot-scope="scope">
                            <span>{{scope.row.accessUser?scope.row.accessUser.displayName:''}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="settingRelationList"
                                     label="查询条件项"
                                     :show-overflow-tooltip="true"
                                     width>
                        <template slot-scope="scope">
                            <span v-for="(item,index) in scope.row.settingRelationList"
                                  :key=index>{{item.cmdbCode!=null?item.cmdbCode.filedName:''}}
                                <span v-if="index !== (scope.row.settingRelationList.length - 1) && item.cmdbCode!=null">,</span>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="returnRelationList"
                                     label="结果返回项"
                                     :show-overflow-tooltip="true"
                                     width>
                        <template slot-scope="scope">
                            <span v-for="(item,index) in scope.row.returnRelationList"
                                  :key=index>{{item.cmdbCode!=null?item.cmdbCode.filedName:''}}
                                <span v-if="index !== (scope.row.returnRelationList.length - 1) && item.cmdbCode!=null">,</span>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remark"
                                     label="备注"
                                     :show-overflow-tooltip="true"
                                     width></el-table-column>
                    <el-table-column fixed="right"
                                     label="操作"
                                     width="100">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="deleteRow(scope.row.condicationName,scope.row.id)">
                                <i style="color: #269BE0;"
                                   class="el-icon-delete"></i>
                            </el-button>&nbsp;&nbsp;&nbsp;
                            <el-button type="text"
                                       @click="Edit(scope.row)">
                                <i style="color: #269BE0;"
                                   class="el-icon-edit"></i>
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <div class="yw-page-wrap">
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </div>
        </el-form>

        <!-- dialog---弹窗 -->
        <el-dialog class="yw-dialog"
                   :title="dialogTitle"
                   width='1450px'
                   :visible.sync="addAlertNotifyConfig"
                   :close-on-click-modal="false"
                   @close="onCancel">
            <section class="yw-dialog-main">
                <el-form v-if="showDialog"
                         ref="form"
                         :model="form"
                         :rules="rules"
                         label-width="70px"
                         class="components-condition yw-form">

                    <el-row>
                        <el-col :span="5">
                            <el-form-item label="配置编码"
                                          prop="condicationCode"
                                          :rules="[
                                              { required: true, message: '请输入码表编码', trigger: ['blur', 'change'] },
                                              { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '请输入以英文字母开头,数字,下划线组合,', trigger: ['blur', 'change'] },
                                              { min: 1, max: 60, message: '最多不能超过60个字段', trigger: ['blur', 'change']},
                                              { validator: (rule, value, callback) => this.validDataUnique(rule, value, callback), trigger: 'blur' }
                                          ]">
                                <el-input placeholder="请输入配置编码"
                                          :clearable=clearableCodeName
                                          v-model="form.condicationCode"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="配置类型"
                                          prop="condicationType"
                                          :rules="{required: true, message: '配置类型不能为空', trigger: 'change'}">
                                <el-select v-model="form.condicationType"
                                           placeholder="请选择配置类型"
                                           :clearable=clearableCodeName
                                           filterable>
                                    <el-option v-for="item in conditionSettingTypeList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="配置地址"
                                          prop="condicationName"
                                          :rules="[
                                              { required: true, message: '配置地址不能为空', trigger: ['blur', 'change']}]">
                                <el-input placeholder="请输入配置地址"
                                          :clearable=clearableCodeName
                                          v-model="form.condicationName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="5">
                            <el-form-item label="模型名称"
                                          prop="moduleId">
                                <el-select clearable
                                           filterable
                                           v-model="moduleIds"
                                           placeholder="请选择"
                                           collapse-tags
                                           @change="selectChange"
                                           @clear="refModuleIdClear">
                                    <el-option :value="mineStatusValue"
                                               style="height: auto">
                                        <el-tree :data="reportData"
                                                 :filter-node-method="filterNode"
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
                        <el-col :span="5"
                                style=" display: inline-table;">
                            <el-form-item label="接入用户"
                                          prop="accessUserId">
                                <el-select v-model="form.accessUserId"
                                           placeholder="请选择接入用户"
                                           :clearable=clearableCodeName
                                           filterable>
                                    <el-option v-for="item in condTypeList"
                                               :key="item.id"
                                               :label="item.displayName"
                                               :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="5"
                                style="width:63.3%;display: inline-table;">
                            <el-form-item label="备注"
                                          prop="remark">
                                <el-input class="ctextarea"
                                          type="textarea"
                                          autosize
                                          placeholder="请输入内容"
                                          v-model="form.remark">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row class="diaTitle">查询条件配置</el-row>
                    <el-row v-for="(column, idxTable) in form.bindColumnsTable"
                            :key="idxTable">
                        <el-col :span="4"
                                class="form_item">
                            <el-form-item :prop="'bindColumnsTable.' + idxTable + '.codeId'"
                                          label="码表名称">
                                <el-select v-model="form.bindColumnsTable[idxTable].codeId"
                                           placeholder="请选码表名称"
                                           filterable
                                           clearable>
                                    <el-option v-for="item in codeTypeNameList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"
                                               @click.native="codeNameClick(form.bindColumnsTable[idxTable]['codeId'],idxTable)">
                                        <span style="float: left; width: 200px;">{{ item.label }}</span>
                                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.catalogName }}</span>

                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4"
                                class="form_item">
                            <el-form-item prop="defaultValue"
                                          label="默认值">
                                <el-input placeholder="请输入默认值"
                                          clearable
                                          v-model="form.bindColumnsTable[idxTable].defaultValue"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="3"
                                class="form_item3">
                            <el-form-item :prop="'bindColumnsTable.' + idxTable + '.operateType'"
                                          label="过滤条件">
                                <el-select v-model="form.bindColumnsTable[idxTable].operateType"
                                           placeholder="请选过滤条件"
                                           filterable
                                           clearable>
                                    <!-- filterCondition -->
                                    <el-option v-for="item in filterCondition"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4"
                                class="form_item"
                                style="width:240px;display: inline-table;">
                            <el-form-item :prop="'bindColumnsTable.' + idxTable + '.containCodeId'"
                                          label="包含字段">
                                <el-select v-model="form.bindColumnsTable[idxTable].containCodeId"
                                           placeholder="请选包含字段"
                                           filterable
                                           multiple
                                           class="contain-codeId"
                                           collapse-tags
                                           clearable>
                                    <el-option v-for="item in codeTypeNameList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"
                                               @click.native="containCodeNameClick(form.bindColumnsTable[idxTable]['containCodeId'],idxTable)">
                                        <span style="float: left; width: 200px;">{{ item.label }}</span>
                                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.catalogName }}</span>

                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5"
                                style="width:100px;display: inline-table;"
                                class="solit">
                            <el-form-item :prop="'bindColumnsTable.' + idxTable + '.sortIndex'"
                                          label="排序"
                                          label-width="50px"
                                          class="form_item2"
                                          :rules="[{required: true, message: '排序不能为空', trigger: 'change'},
                                                   { pattern: /^\+?[1-9][0-9]*$/, message: '必须是正 整数,', trigger: ['blur', 'change']}]">
                                <el-input placeholder="排序"
                                          clearable
                                          v-model="form.bindColumnsTable[idxTable].sortIndex"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="3">
                            <el-form-item prop="isRequire"
                                          label="是否必填"
                                          style="width:100%">
                                <el-switch v-model="form.bindColumnsTable[idxTable]['isRequire']"
                                           active-text="是"
                                           inactive-text="否"
                                           active-value="0"
                                           inactive-value="1"
                                           @change="switchClick(form.bindColumnsTable[idxTable]['isRequire'],idxTable)"></el-switch>&nbsp;&nbsp;&nbsp;
                            </el-form-item>
                        </el-col>
                        <el-col :span="3">
                            <el-form-item prop="showInput"
                                          label="手工输入"
                                          style="width:100%">
                                <el-switch v-model="form.bindColumnsTable[idxTable]['showInput']"
                                           active-text="是"
                                           inactive-text="否"
                                           active-value="1"
                                           inactive-value="0"
                                           @change="switchClick(form.bindColumnsTable[idxTable]['showInput'],idxTable)"></el-switch>&nbsp;&nbsp;&nbsp;
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-form-item prop="showOption"
                                          label="显示选项"
                                          style="width:100%">
                                <el-switch v-model="form.bindColumnsTable[idxTable]['showOption']"
                                           active-text="是"
                                           inactive-text="否"
                                           active-value="1"
                                           inactive-value="0"
                                           @change="switchClick(form.bindColumnsTable[idxTable]['showOption'],idxTable)"></el-switch>&nbsp;&nbsp;&nbsp;
                                <i class="el-icon-circle-plus-outline ICON"
                                   v-show="showPlusTable(idxTable)"
                                   @click="plusTable(idxTable)"></i>
                                <i class="el-icon-remove-outline ICON"
                                   v-show="showMouseTable(idxTable)"
                                   @click="mouseTable(idxTable)"></i>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row class="diaTitle">返回结果配置</el-row>
                    <el-row v-for="(column, idxTable) in form.bindColumnsReturn"
                            :key="idxTable">
                        <el-col :span="5">
                            <el-form-item :prop="'bindColumnsReturn.' + idxTable + '.codeId'"
                                          label="码表名称">
                                <el-select v-model="form.bindColumnsReturn[idxTable].codeId"
                                           placeholder="请选码表名称"
                                           filterable
                                           clearable>
                                    <el-option v-for="item in codeTypeNameList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"
                                               @click.native="codeNameClickReturn(form.bindColumnsReturn[idxTable]['codeId'],idxTable)">
                                        <span style="float: left; width: 200px;">{{ item.label }}</span>
                                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.catalogName }}</span>

                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <div class="mtop10">
                                <i class="el-icon-circle-plus-outline ICON"
                                   v-show="showPlusReturn(idxTable)"
                                   @click="plusReturn(idxTable)"></i>
                                <i class="el-icon-remove-outline ICON"
                                   v-show="showMouseReturn(idxTable)"
                                   @click="mouseReturn(idxTable)"></i>
                            </div>
                        </el-col>
                    </el-row>
                    <el-row class="diaTitle">结果排序配置</el-row>
                    <el-row v-for="(column, idxTable) in form.bindColumnsSort"
                            :key="idxTable">
                        <el-col :span="5">
                            <el-form-item :prop="'bindColumnsSort.' + idxTable + '.codeId'"
                                          label="码表名称">
                                <el-select v-model="form.bindColumnsSort[idxTable].codeId"
                                           placeholder="请选码表名称"
                                           filterable
                                           clearable>
                                    <el-option v-for="item in codeTypeNameList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value">
                                        <span style="float: left; width: 200px;">{{ item.label }}</span>
                                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.catalogName }}</span>
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item :prop="'bindColumnsSort.' + idxTable + '.sortType'"
                                          label="排序方式">
                                <el-select v-model="form.bindColumnsSort[idxTable].sortType"
                                           placeholder="请选排序方式"
                                           filterable
                                           :clearable=clearableCodeName>
                                    <el-option v-for="item in sortOrderList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8"
                                class="solit">
                            <el-form-item :prop="'bindColumnsSort.' + idxTable + '.sortIndex'"
                                          label="排序"
                                          :rules="[{required: true, message: '排序不能为空', trigger: 'change'},
                                                   { pattern: /^\+?[1-9][0-9]*$/, message: '必须是正 整数,', trigger: ['blur', 'change']}]">&nbsp;&nbsp;&nbsp;
                                <el-input placeholder="排序"
                                          clearable
                                          v-model="form.bindColumnsSort[idxTable].sortIndex"></el-input>
                                <i class="el-icon-circle-plus-outline ICON"
                                   v-show="showPlusSort(idxTable)"
                                   @click="plusSort(idxTable)"></i>
                                <i class="el-icon-remove-outline ICON"
                                   v-show="showMouseSort(idxTable)"
                                   @click="mouseSort(idxTable)"></i>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </section>
            <section class="btn-wrap"
                     style="left: calc(50% - 70px);">
                <el-button type="primary"
                           @click="onSubmite('form')">保存</el-button>
                <el-button @click="onCancel('form')">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import conditionManagement from 'src/services/condition_management/query_configuration.js'

    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        name: 'DynamicForm',
        data() {
            return {
                loadingText: '正在查询数据, 请稍等...',
                reportData: [], // 模型名称

                defaultProps: {
                    children: 'childModules',
                    label: 'name'
                },
                sortOrderList: [ // 排序方式
                    {
                        value: 'ASC',
                        label: 'ASC'
                    },
                    {
                        value: 'DESC',
                        label: 'DESC'

                    }
                ],
                codeLeh: '',
                clearableCodeName: true,
                codeindex: '0',
                codedata: '1',

                codeList: [],
                addAlertNotifyConfig: false,
                isRequireData: '',
                indexNum: '',
                dialogTitle: '编辑',

                currentPage: 1,
                pageSize: 50,
                total: 0,
                pageSizes: [10, 20, 50, 100],
                tableDataAmount: {},
                conditionSettingTypeList: [],
                condTypeList: [], // 配置类型
                codeTypeNameList: [], // 码表名称
                ID: '',
                uniqueCode: '',
                uniqueName: '',
                condicationName: '', // 配置名称
                condicationType: '', // 配置类型
                condition_setting_type: '', // 配置类型(取的字典值)
                remark: '', // 备注
                moduleIds: '',
                // 弹窗条件
                form: {
                    condicationCode: '',
                    condicationName: '',
                    condicationType: '',
                    remark: '', // 备注
                    moduleId: '',
                    accessUserId: '',
                    bindColumnsTable: [{
                        'codeId': '',
                        'defaultValue': '',
                        'sortIndex': '1', // 排序
                        'operateType': '', // 过滤条件
                        'containCodeId': '',
                        'showOption': '1',
                        'showInput': '0',
                        'isRequire': '1'
                    }],

                    bindColumnsReturn: [{
                        'codeId': ''
                    }],

                    bindColumnsSort: [{
                        'codeId': '',
                        'sortType': '',
                        'sortIndex': '1'
                    }]
                },
                condicationTypeList: [], // 配置地址
                filterCondition: [], // 过滤条件
                value: '',
                tableData: [], // 列表数据
                loading: false,
                showDialog: false,
                AAA: 1,
                BBB: 1,
                CCC: 1
            }
        },
        mounted() {
            this.search()
            this.Dictionaries()
        },
        watch: {
            moduleIds(val) {
                this.$refs.tree.filter(val)
            },

        },
        methods: {
            // 验证编码和地址的唯一性
            validDataUnique(rule, value, callback) {
                let uniqueValue = rule.field == 'condicationCode' ? this.uniqueCode : this.uniqueName
                console.info('before:' + uniqueValue)
                console.info('after:' + value)
                if (uniqueValue == value) {
                    return callback()
                }
                let params = rule.field == 'condicationCode' ? { code: value } : { name: value }
                conditionManagement.validDataUnique(params).then((res) => {
                    if (res.flag == 'false') {
                        return callback('值已存在,请重新编辑')
                    } else {
                        return callback()
                    }
                })
            },

            switchClick(data) {
                this.codedata = data
            },
            // 码表名称
            plusTable(idxTable) {
                this.form.bindColumnsTable.splice(idxTable + 1, 0, { 'codeId': '', 'defaultValue': '', 'sortIndex': '', 'operateType': '', 'showOption': '0', 'showInput': '0', 'isRequire': '1' })
                this.form.bindColumnsTable.map((item, index) => {
                    this.form.bindColumnsTable[index].sortIndex = index + 1
                    return Object.assign(item)
                })
            },
            showPlusTable() {
                return true
            },
            mouseTable(idxTable) {
                this.form.bindColumnsTable.splice(idxTable, 1)
            },
            showMouseTable(idxTable) {
                return (this.form.bindColumnsTable.length > 1 && idxTable === 0) || idxTable > 0
            },
            codeNameClick(data, index) {
                this.codeindex = index
                for (var i = 0; i < this.form.bindColumnsTable.length; i++) {
                    var item = this.form.bindColumnsTable[i]
                    if (item.codeId === '' && index === i) {
                        item.codeId = data
                    }
                }

            },
            containCodeNameClick(data, index) {
                console.log(data)
                this.codeindex = index
                for (var i = 0; i < this.form.bindColumnsTable.length; i++) {
                    var item = this.form.bindColumnsTable[i]
                    if ((item.containCodeId === '' || item.containCodeId.length < 1) && index === i) {
                        item.containCodeId = data
                    }
                }

            },
            // 返回结果配置
            plusReturn(idxTable) {
                this.form.bindColumnsReturn.splice(idxTable + 1, 0, { 'codeId': '' })
                this.form.bindColumnsReturn.map(item => {
                    return Object.assign(item)
                })
            },
            showPlusReturn() {
                return true
            },
            mouseReturn(idxTable) {
                this.form.bindColumnsReturn.splice(idxTable, 1)
            },
            showMouseReturn(idxTable) {
                return (this.form.bindColumnsReturn.length > 1 && idxTable === 0) || idxTable > 0
            },
            codeNameClickReturn(data, index) {
                this.codeindex = index
                for (var i = 0; i < this.form.bindColumnsReturn.length; i++) {
                    var item = this.form.bindColumnsReturn[i]
                    if (item.codeId === '' && index === i) {
                        item.codeId = data
                    }
                }
                // this.form.bindColumnsTable = this.form.bindColumnsTable
            },

            // 结果排序配置
            plusSort(idxTable) {
                this.form.bindColumnsSort.splice(idxTable + 1, 0, { 'codeId': '', 'sortType': '', 'sortIndex': '' })
                this.form.bindColumnsSort.map((item, index) => {
                    this.form.bindColumnsSort[index].sortIndex = index + 1
                    return Object.assign(item)
                })
            },
            showPlusSort() {
                return true
            },
            mouseSort(idxTable) {
                this.form.bindColumnsSort.splice(idxTable, 1)
            },
            showMouseSort(idxTable) {
                return (this.form.bindColumnsSort.length > 1 && idxTable === 0) || idxTable > 0
            },
            // 模型名称过滤
            filterNode(value, reportData) {
                if (!value) return true
                return reportData.name.indexOf(value) !== -1
            },
            // 模型名称
            handleNodeClick(e) {
                // 模型名称及联下的 码表名称（模型字段）
                this.form.moduleId = e.id
                this.moduleIds = e.name
                this.codeTypeNameList = []
                tableManagement.getModuleDetailList(e.id).then(res => {
                    for (let item in res) {
                        this.codeTypeNameList.push({
                            label: res[item].filedName,
                            value: res[item].codeId,
                            catalogName: res[item].catalogName
                        })
                    }
                })
                this.empty()
            },
            empty() {
                for (let index = 0; index < this.form.bindColumnsTable.length; index++) {
                    this.form.bindColumnsTable[index].codeId = ''
                }
                for (let index = 0; index < this.form.bindColumnsReturn.length; index++) {
                    this.form.bindColumnsReturn[index].codeId = ''
                }
                for (let index = 0; index < this.form.bindColumnsSort.length; index++) {
                    this.form.bindColumnsSort[index].codeId = ''
                }
                this.codeTypeNameList = []
            },
            refModuleIdClear() {
                this.empty()
                this.codeName()
            },
            codeName() {
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
                            catalogName: res[item].catalogName
                        })
                    }
                })
            },
            Dictionaries() {
                // 模型名称
                tableManagement.getTreeList('').then(res => {
                    this.reportData = res
                })
                // 配置类型
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'condition_setting_type' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.conditionSettingTypeList.push({
                            lable: res[item].id,
                            value: res[item].name
                        })
                    }
                })
                // 接入用户
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/v3/cmdb/access/user/list'
                }).then((res) => {
                    this.condTypeList = res
                })
                // 过滤条件
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'sql_operate' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.filterCondition.push({
                            lable: res[item].id,
                            value: res[item].name
                        })
                    }
                })
            },
            // 选择事件 得到选中的数据
            handleSelectionChange(data) {
                this.tableDataAmount = data
            },
            // table_编辑
            Edit(rows) {
                // this.codeTypeNameList = []
                // tableManagement.getModuleDetailList(rows.moduleId).then(res => {
                //     for (let item in res) {
                //         this.codeTypeNameList.push({
                //             label: res[item].filedName,
                //             value: res[item].codeId,
                //             catalogName: res[item].catalogName
                //         })
                //     }
                // })
                this.AAA = rows.settingRelationList.length
                this.BBB = rows.returnRelationList.length
                this.CCC = rows.sortRelationList.length
                this.addAlertNotifyConfig = true
                this.showDialog = true
                this.clearableCodeName = false
                rows.settingRelationList.forEach((item) => {
                    if (item.containCodeId) {
                        item.containCodeId = item.containCodeId.split(',')
                    }
                })
                this.form.bindColumnsTable = rows.settingRelationList.length > 0 ? rows.settingRelationList : [{
                    'codeId': '',
                    'defaultValue': '',
                    'sortIndex': '1', // 排序
                    'operateType': '', // 过滤条件
                    'containCodeId': '',
                    'showOption': '1',
                    'showInput': '0',
                    'isRequire': '1'
                }] // 筛选条件
                this.form.bindColumnsReturn = rows.returnRelationList.length > 0 ? rows.returnRelationList : [{
                    'codeId': ''
                }] // 返回结果配置
                this.form.bindColumnsSort = rows.sortRelationList.length > 0 ? rows.sortRelationList : [{
                    'codeId': '',
                    'sortType': '',
                    'sortIndex': '1'
                }] // 结果排序配置

                this.dialogTitle = '编辑'
                this.ID = rows.id
                this.moduleIds = rows.moduleId
                for (let index = 0; index < this.reportData.length; index++) {
                    if (rows.moduleId === this.reportData[index].id) {
                        this.moduleIds = this.reportData[index].name
                        this.form.moduleId = this.reportData[index].id
                    } else if (this.reportData[index].childModules.length > 0) {
                        for (let i = 0; i < this.reportData[index].childModules.length; i++) {
                            if (this.reportData[index].childModules[i].childModules.length > 0) {
                                for (let s = 0; s < this.reportData[index].childModules[i].childModules.length; s++) {
                                    if (rows.moduleId === this.reportData[index].childModules[i].childModules[s].id) {
                                        this.moduleIds = this.reportData[index].childModules[i].childModules[s].name
                                        this.form.moduleId = this.reportData[index].childModules[i].childModules[s].id
                                    } else if (this.reportData[index].childModules[i].childModules[s].childModules.length > 0) {
                                        for (let x = 0; x < this.reportData[index].childModules[i].childModules[s].childModules.length; x++) {
                                            if (rows.moduleId === this.reportData[index].childModules[i].childModules[s].childModules[x].id) {
                                                this.moduleIds = this.reportData[index].childModules[i].childModules[s].childModules[x].name
                                                this.form.moduleId = this.reportData[index].childModules[i].childModules[s].childModules[x].id
                                            }
                                        }

                                    }
                                }
                            } else if (rows.moduleId === this.reportData[index].childModules[i].id) {
                                this.moduleIds = this.reportData[index].childModules[i].name
                                this.form.moduleId = this.reportData[index].childModules[i].id
                            }
                        }
                    }
                }
                this.form.condicationName = rows.condicationName
                this.form.condicationType = rows.condicationType
                this.form.condicationCode = rows.condicationCode
                this.form.accessUserId = rows.accessUserId
                this.form.remark = rows.remark
                // 为之后
                this.uniqueCode = rows.condicationCode
                this.uniqueName = rows.condicationName
            },
            // 新增
            add() {
                // this.form = {}
                this.addAlertNotifyConfig = true

                this.form.bindColumnsTable = []
                this.form.bindColumnsReturn = []
                this.form.bindColumnsSort = []
                this.form.bindColumnsTable = [{
                    'codeId': '',
                    'defaultValue': '',
                    'sortIndex': '1', // 排序
                    'operateType': '', // 过滤条件
                    'showOption': '1',
                    'containCodeId': '',
                    'showInput': '0',
                    'isRequire': '1'
                }]
                this.form.bindColumnsReturn = [{
                    'codeId': ''
                }]

                this.form.bindColumnsSort = [{
                    'codeId': '',
                    'sortType': '',
                    'sortIndex': '1'
                }]
                this.form.condicationName = ''
                this.form.condicationType = ''
                this.form.remark = ''
                this.form.condicationCode = ''
                this.form.accessUserId = ''
                this.moduleIds = '' // 模型名称
                this.form.moduleId = '' // 模型名称

                this.showDialog = true
                this.dialogTitle = '新增'
            },

            // dialog---取消
            onCancel(formName) {
                this.codeTypeNameList = []
                this.addAlertNotifyConfig = false
                this.showDialog = false
                this.search()
                this.$refs[formName].resetFields()
                // 保证只有在更新的时候有值
                this.uniqueCode = ''
                this.uniqueName = ''
            },
            // dialog---查询接口
            QueryList() {
                this.loading = true
                if (this.dialogTitle === '新增') {
                    this.loadingText = '正在查询数据, 请稍等...'
                    this.form.bindColumnsTable.forEach((item) => {
                        if (item.containCodeId && item.containCodeId.length > 0) {
                            item.containCodeId = item.containCodeId.join(',')
                        } else {
                            item.containCodeId = ''
                        }
                    })
                    let page = {
                        remark: this.form.remark,
                        condicationName: this.form.condicationName,
                        condicationType: this.form.condicationType,
                        condicationCode: this.form.condicationCode,
                        moduleId: this.form.moduleId,
                        accessUserId: this.form.accessUserId,
                        settingRelationList: this.form.bindColumnsTable,
                        returnRelationList: this.form.bindColumnsReturn,
                        sortRelationList: this.form.bindColumnsSort
                    }
                    if (this.form.bindColumnsTable.length === 1 && this.form.bindColumnsTable[0].codeId === '') {
                        page.settingRelationList = []
                    }
                    if (this.form.bindColumnsReturn.length === 1 && this.form.bindColumnsReturn[0].codeId === '') {
                        page.returnRelationList = []
                    }
                    if (this.form.bindColumnsSort.length === 1 && this.form.bindColumnsSort[0].codeId === '') {
                        page.sortRelationList = []
                    }
                    conditionManagement.getCondicationSave(page).then(res => {
                        if (res.flag === 'error') {
                            this.$message.error(res.msg)
                            this.loading = false
                        } else {
                            this.tableData = res.data
                            this.total = res.totalSize
                            this.search()
                            this.loading = false
                            this.$message.success('新增成功')
                        }
                    })
                } else {
                    this.loadingText = '正在保存数据, 请稍等...'
                    this.form.bindColumnsTable.forEach((item) => {
                        if (item.containCodeId && item.containCodeId.length > 0) {
                            item.containCodeId = item.containCodeId.join(',')
                        } else {
                            item.containCodeId = ''
                        }
                    })
                    let page = {
                        id: this.ID,
                        remark: this.form.remark,
                        condicationName: this.form.condicationName,
                        condicationType: this.form.condicationType,
                        condicationCode: this.form.condicationCode,
                        moduleId: this.form.moduleId,
                        accessUserId: this.form.accessUserId,
                        settingRelationList: this.form.bindColumnsTable,
                        returnRelationList: this.form.bindColumnsReturn,
                        sortRelationList: this.form.bindColumnsSort
                    }
                    if (this.form.bindColumnsTable.length === 1 && this.form.bindColumnsTable[0].codeId === '') {
                        page.settingRelationList = []
                    }
                    if (this.form.bindColumnsReturn.length === 1 && this.form.bindColumnsReturn[0].codeId === '') {
                        page.returnRelationList = []
                    }
                    if (this.form.bindColumnsSort.length === 1 && this.form.bindColumnsSort[0].codeId === '') {
                        page.sortRelationList = []
                    }
                    conditionManagement.getCondicationUpdate(page).then(res => {
                        if (res.flag === 'error') {
                            this.$message.error(res.msg)
                            this.loading = false
                        } else {
                            this.tableData = res.data
                            this.total = res.totalSize
                            this.search()
                            this.loading = false
                            this.$message.success('编辑成功')
                        }
                    })
                }
            },
            onSubmite(formName) {
                this.$refs[formName].validate(valid => {
                    var newArr = []
                    var newArr1 = []
                    var newArr2 = []
                    if (this.form.bindColumnsTable.length > 0) {
                        for (let items in this.form.bindColumnsTable) {
                            if (this.form.bindColumnsTable[items].codeId !== '' && this.form.bindColumnsTable[items].operateType === '') {
                                this.$message.error('过滤条件不能为空')
                                return
                            }
                            if (this.form.bindColumnsTable[items].codeId !== '') {
                                newArr.push(this.form.bindColumnsTable[items].codeId)
                            }
                        }
                    }
                    if (this.form.bindColumnsReturn.length > 0) {
                        for (let items in this.form.bindColumnsReturn) {
                            if (this.form.bindColumnsReturn[items].codeId !== '') {
                                newArr1.push(this.form.bindColumnsReturn[items].codeId)
                            }
                        }
                    }
                    if (this.form.bindColumnsSort.length > 0) {
                        for (let items in this.form.bindColumnsSort) {
                            if (this.form.bindColumnsSort[items].codeId !== '' && this.form.bindColumnsSort[items].sortType === '') {
                                this.$message.error('排序方式不能为空')
                                return
                            }
                            if (this.form.bindColumnsSort[items].codeId !== '') {
                                newArr2.push(this.form.bindColumnsSort[items].codeId)
                            }
                        }
                    }
                    var nary = newArr.sort()
                    var nary1 = newArr1.sort()
                    var nary2 = newArr2.sort()
                    nary.pop()
                    nary1.pop()
                    nary2.pop()
                    if (new Set(nary).size !== nary.length) {
                        this.$message.error('查询条件配置～码表名称是唯一的不能重复,请重新选择')
                    } else if (new Set(nary1).size !== nary1.length) {
                        this.$message.error('返回结果配置～码表名称是唯一的不能重复,请重新选择')
                    } else if (new Set(nary2).size !== nary2.length) {
                        this.$message.error('结果排序配置～码表名称是唯一的不能重复,请重新选择')
                    } else if (valid) {
                        this.QueryList()
                        this.form.condicationName = ''
                        this.form.condicationType = ''
                        this.form.remark = ''
                        this.codeTypeNameList = []
                        this.addAlertNotifyConfig = false
                        this.showDialog = false
                    }
                })
                // 保证只有在更新的时候有值
                this.uniqueCode = ''
                this.uniqueName = ''
            },
            // 单行删除
            deleteRow(name, id) {
                let page = {
                    id: id
                }
                this.$confirm('删除配置名称' + name + '?', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    conditionManagement.getCondicationDELETE(page).then(res => {
                        console.log(res)
                        this.$message.success('删除成功')
                        this.search() // 重新查询数据
                    }).catch(() => {
                        this.$message.error('删除失败')
                    })
                })
            },
            search() {
                this.loading = true
                this.currentPage = 1
                let page = {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    condicationName: this.condicationName,
                    condicationType: this.condicationType
                }
                this.codeName()
                conditionManagement.getCondicationList(page).then(res => {
                    this.tableData = res.data
                    this.total = res.totalSize
                    this.loading = false
                })
                    .catch(() => {
                        this.$message.error('查询失败')
                        this.loading = false
                    })
            },
            // 分页
            handleSizeChange(val) {
                this.pageSize = val
                let page = {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    condicationName: this.condicationName,
                    condicationType: this.condicationType
                }
                conditionManagement.getCondicationList(page).then(res => {
                    this.tableData = res.data
                    this.total = res.totalSize
                })
                    .catch(() => {
                        this.$message.error('查询失败')
                    })
            },
            handleCurrentChange(val) {
                this.currentPage = val
                let page = {
                    currentPage: val,
                    pageSize: this.pageSize,
                    condicationName: this.condicationName,
                    condicationType: this.condicationType
                }
                conditionManagement.getCondicationList(page).then(res => {
                    this.tableData = res.data
                    this.total = res.totalSize
                })
                    .catch(() => {
                        this.$message.error('查询失败')
                    })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../query_configuration/queryList.scss";
    .form_item {
        .el-form-item {
            .el-input {
                width: 126px !important;
            }
            .el-select {
                /deep/ .el-input {
                    width: 140px !important;
                }
            }
        }
    }
    .form_item2 {
        .el-input {
            width: 84px !important;
        }
    }
    .form_item3 {
        .el-select {
            /deep/ .el-input {
                width: 100px !important;
            }
        }
    }
    .contain-codeId /deep/ .el-select__tags {
        max-width: 150px !important;
    }
</style>
