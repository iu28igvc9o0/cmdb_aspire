<template>
    <div class="alert-field">
        <div>
            <div class="div-header">
                <p class="p-text">基本信息</p>
            </div>
            <div>
                <el-form :inline="true"
                         label-position="right"
                         label-width="100px"
                         ref="alertFieldForm"
                         :model="alertFieldForm"
                         :rules="alertFieldFormRules"
                         :disabled="detailDisabled">
                    <el-form-item label="字段名称"
                                  prop="fieldName">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.fieldName"
                                  :disabled="updateDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="字段编码"
                                  prop="fieldCode">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.fieldCode"
                                  :disabled="updateDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="数据类型"
                                  prop="JDBCType">
                        <!--<el-input size="mini" v-model="alertFieldForm.JDBCType" :disabled="updateDisabled"></el-input>-->
                        <el-select style="width:178px"
                                   size="mini"
                                   v-model="alertFieldForm.JDBCType"
                                   :disabled="updateDisabled">
                            <el-option v-for="item in dataSourceType"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="字段类型"
                                  prop="fieldType">
                        <el-radio-group v-model="alertFieldForm.fieldType"
                                        @change="fieldTypeChange()"
                                        :disabled="this.alertFieldForm.fieldType === '1' && this.type === 'update'">
                            <el-radio label="1">基础字段</el-radio>
                            <el-radio label="2">扩展字段</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="长度">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.JDBCLength"
                                  :disabled="this.alertFieldForm.fieldType === '1' && this.type === 'update'"></el-input>
                    </el-form-item>
                    <el-form-item label="提示信息">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.JDBCTip"
                                  :disabled="this.alertFieldForm.fieldType === '1' && this.type === 'update'"></el-input>
                    </el-form-item>
                    <el-form-item label="是否锁定">
                        <el-radio-group v-model="alertFieldForm.isLock"
                                        :disabled="(this.alertFieldForm.fieldType === '1' && this.type === 'update')">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div>
            <div class="div-header">
                <p class="p-text">CI信息</p>
            </div>
            <div>
                <el-form :inline="true"
                         label-position="right"
                         label-width="100px"
                         ref="alertFieldForm"
                         :model="alertFieldForm"
                         :disabled="detailDisabled">
                    <el-form-item label="是否CI参数">
                        <el-radio-group v-model="alertFieldForm.isCiParams">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                        <!--<el-radio v-model="alertFieldForm.isCiParams" label="1">是</el-radio>-->
                        <!--<el-radio v-model="alertFieldForm.isCiParams" label="0">否</el-radio>-->
                    </el-form-item>
                    <el-form-item label="参数名称"
                                  style="padding-left: 75px;">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.paramsName"></el-input>
                    </el-form-item>
                    <el-form-item label="CI码表字段">
                        <!--<el-input size="mini" v-model="alertFieldForm.paramCode"></el-input>-->
                        <el-select style="width:178px"
                                   size="mini"
                                   v-model="alertFieldForm.paramCode"
                                   @change="ciCodeChange()"
                                   @clear="ciCodeClear()"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="val in ciCodeList"
                                       :key="val.filedName"
                                       :label="val.filedName"
                                       :value="val.filedName"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="CI码表编码">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.CICode"
                                  disabled></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div>
            <div class="div-header">
                <p class="p-text">展示信息</p>
            </div>
            <div>
                <el-form :inline="true"
                         label-position="right"
                         label-width="100px"
                         ref="alertFieldForm"
                         :model="alertFieldForm"
                         :disabled="detailDisabled">
                    <el-form-item label="是否列表展示">
                        <!--<el-radio v-model="alertFieldForm.isListShow" label="1">是</el-radio>-->
                        <!--<el-radio v-model="alertFieldForm.isListShow" label="0">否</el-radio>-->
                        <el-radio-group v-model="alertFieldForm.isListShow">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="展示顺序"
                                  style="padding-left: 75px;">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model.number="alertFieldForm.listShowSort"></el-input>
                    </el-form-item>
                    <el-form-item label="展示名称">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.listShowName"></el-input>
                    </el-form-item>
                    <el-form-item label="展示格式">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.listShowPattern"></el-input>
                    </el-form-item>
                    <el-form-item label="表列宽度">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.tableColumnWidth"></el-input>
                    </el-form-item>
                    <br />
                    <el-form-item label="是否详情展示">
                        <!--<el-radio v-model="alertFieldForm.isDetailShow" label="1">是</el-radio>-->
                        <!--<el-radio v-model="alertFieldForm.isDetailShow" label="0">否</el-radio>-->
                        <el-radio-group v-model="alertFieldForm.isDetailShow">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="展示顺序"
                                  style="padding-left: 75px;">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model.number="alertFieldForm.detailShowSort"></el-input>
                    </el-form-item>
                    <el-form-item label="展示名称">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.detailShowName"></el-input>
                    </el-form-item>
                    <el-form-item label="展示格式">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.detailShowPattern"></el-input>
                    </el-form-item>
                    <el-form-item label="是否查询条件">
                        <!--<el-radio v-model="alertFieldForm.isQueryParam" label="1">是</el-radio>-->
                        <!--<el-radio v-model="alertFieldForm.isQueryParam" label="0">否</el-radio>-->
                        <el-radio-group v-model="alertFieldForm.isQueryParam">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="展示顺序"
                                  style="padding-left: 75px;">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model.number="alertFieldForm.queryParamSort"></el-input>
                    </el-form-item>
                    <el-form-item label="展示名称">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.queryParamName"></el-input>
                    </el-form-item>
                    <el-form-item label="查询格式">
                        <!--<el-input size="mini" v-model="alertFieldForm.queryParamType"></el-input>-->
                        <el-select style="width:178px"
                                   size="mini"
                                   v-model="alertFieldForm.queryParamType">
                            <el-option v-for="item in queryType"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据来源">
                        <el-input size="mini"
                                  style="width:178px"
                                  v-model="alertFieldForm.queryParamSource"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div v-if="this.tableName === 'alert_alerts'">
            <div class="div-header">
                <p class="p-text">衍生告警(告警模型独有)</p>
            </div>
            <div>
                <el-form :inline="true"
                         ref="alertFieldForm"
                         label-width="100px"
                         label-position="right"
                         :model="alertFieldForm"
                         :disabled="detailDisabled ">
                    <el-form-item label="是否衍生字段">
                        <!--<el-radio v-model="alertFieldForm.isDeriveField" label="1">是</el-radio>-->
                        <!--<el-radio v-model="alertFieldForm.isDeriveField" label="0">否</el-radio>-->
                        <el-radio-group v-model="alertFieldForm.isDeriveField">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="展示顺序"
                                  style="padding-left: 75px;">
                        <el-input size="mini"
                                  v-model.number="alertFieldForm.deriveFieldSort"></el-input>
                    </el-form-item>
                    <el-form-item label="展示名称">
                        <el-input size="mini"
                                  v-model="alertFieldForm.deriveFieldName"></el-input>
                    </el-form-item>
                    <el-form-item label="查询格式">
                        <!--<el-input size="mini" v-model="alertFieldForm.deriveFieldType"></el-input>-->
                        <el-select size="mini"
                                   v-model="alertFieldForm.deriveFieldType">
                            <el-option v-for="item in queryType"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据来源">
                        <el-input size="mini"
                                  v-model="alertFieldForm.deriveFieldSource"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </div>

    </div>
</template>

<script>
    import { query_type, jdbc_type } from 'src/pages/mirror_alert/alert/config/options.js'
    import cmdbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory'
    export default {
        components: {
        },
        props: [
            'type', 'alertFieldDetail', 'tableName'
        ],
        data() {
            return {
                collapseName: ['1', '2', '3', '4'],
                alertFieldForm: {
                    id: '',
                    fieldName: '',
                    fieldCode: '',
                    fieldType: '2',
                    JDBCType: '',
                    JDBCLength: '',
                    JDBCTip: '',
                    isLock: '0',
                    isCiParams: '0',
                    paramsName: '',
                    paramCode: '',
                    CICode: '',
                    isListShow: '0',
                    listShowSort: 0,
                    listShowName: '',
                    listShowPattern: '',
                    tableColumnWidth: '',
                    isDetailShow: '0',
                    detailShowSort: 0,
                    detailShowName: '',
                    detailShowPattern: '',
                    isQueryParam: '0',
                    queryParamSort: 0,
                    queryParamName: '',
                    queryParamType: '',
                    queryParamSource: '',
                    isDeriveField: '0',
                    deriveFieldSort: 0,
                    deriveFieldName: '',
                    deriveFieldType: '',
                    deriveFieldSource: '',
                },
                alertFieldFormRules: {
                    fieldName: [
                        { required: true, message: '请输入告警模型字段名称!', trigger: 'blur' },
                        { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                    ],
                    fieldCode: [
                        { required: true, message: '请输入告警模型字段代码!', trigger: 'blur' }
                    ],
                    JDBCType: [
                        { required: true, message: '请输入告警模型字段数据类型!', trigger: 'blur' }
                    ],
                },
                isLockDisabled: false,
                detailDisabled: false,
                updateDisabled: false,
                queryType: query_type,
                ciCodeList: [],
                dataSourceType: jdbc_type,
            }
        },
        mounted() {
            this.queryCodeList()
        },
        methods: {
            fieldTypeChange() {
                if (this.alertFieldForm.fieldType === '1') {
                    this.isLockDisabled = true
                    this.alertFieldForm.isLock = '1'
                } else {
                    this.isLockDisabled = false
                    this.alertFieldForm.isLock = '2'
                }
            },
            // 获取码表列表
            queryCodeList() {
                let queryObject = {}
                queryObject.limitStart = 0
                queryObject.limitEnd = 1000
                cmdbCodeService.queryCodeList(queryObject).then((data) => {
                    this.ciCodeList = data.data
                }).catch(() => {
                    this.$message.error('查询码表信息失败')
                }).finally(() => {
                    this.loading = false
                })
            },
            ciCodeChange() {
                this.ciCodeList.forEach((item) => {
                    if (this.alertFieldForm.paramCode === item.filedName) {
                        this.alertFieldForm.CICode = item.filedCode
                    }
                })
            },
            ciCodeClear() {
                this.alertFieldForm.CICode = ''
            },
        },
        watch: {
            alertFieldDetail: {
                handler: function () {
                    if (this.alertFieldDetail) {
                        this.alertFieldForm.id = this.alertFieldDetail.id
                        this.alertFieldForm.fieldName = this.alertFieldDetail.fieldName
                        this.alertFieldForm.fieldCode = this.alertFieldDetail.fieldCode
                        this.alertFieldForm.fieldType = this.alertFieldDetail.fieldType
                        this.alertFieldForm.JDBCType = this.alertFieldDetail.dataType
                        this.alertFieldForm.JDBCLength = this.alertFieldDetail.dataLength
                        this.alertFieldForm.JDBCTip = this.alertFieldDetail.dataTip
                        this.alertFieldForm.isLock = this.alertFieldDetail.isLock
                        this.alertFieldForm.isCiParams = this.alertFieldDetail.isCiParams
                        this.alertFieldForm.paramsName = this.alertFieldDetail.paramsName
                        this.alertFieldForm.paramCode = this.alertFieldDetail.paramCode
                        this.alertFieldForm.CICode = this.alertFieldDetail.ciCode
                        this.alertFieldForm.isListShow = this.alertFieldDetail.isListShow
                        this.alertFieldForm.listShowSort = this.alertFieldDetail.listShowSort
                        this.alertFieldForm.listShowName = this.alertFieldDetail.listShowName
                        this.alertFieldForm.listShowPattern = this.alertFieldDetail.listShowPattern
                        this.alertFieldForm.tableColumnWidth = this.alertFieldDetail.tableColumnWidth
                        this.alertFieldForm.isDetailShow = this.alertFieldDetail.isDetailShow
                        this.alertFieldForm.detailShowSort = this.alertFieldDetail.detailShowSort
                        this.alertFieldForm.detailShowName = this.alertFieldDetail.detailShowName
                        this.alertFieldForm.detailShowPattern = this.alertFieldDetail.detailShowPattern
                        this.alertFieldForm.isQueryParam = this.alertFieldDetail.isQueryParam
                        this.alertFieldForm.queryParamSort = this.alertFieldDetail.queryParamSort
                        this.alertFieldForm.queryParamName = this.alertFieldDetail.queryParamName
                        this.alertFieldForm.queryParamType = this.alertFieldDetail.queryParamType
                        this.alertFieldForm.queryParamSource = this.alertFieldDetail.queryParamSource
                        this.alertFieldForm.isDeriveField = this.alertFieldDetail.isDeriveField
                        this.alertFieldForm.deriveFieldSort = this.alertFieldDetail.deriveFieldSort
                        this.alertFieldForm.deriveFieldName = this.alertFieldDetail.deriveFieldName
                        this.alertFieldForm.deriveFieldType = this.alertFieldDetail.deriveFieldType
                        this.alertFieldForm.deriveFieldSource = this.alertFieldDetail.deriveFieldSource
                    }
                },
                immediate: true
            },
            type: {
                handler: function () {
                    if (this.type === 'detail') {
                        this.detailDisabled = true
                    } else if (this.type === 'update') {
                        this.updateDisabled = true
                    }
                },
                immediate: true
            },
        }
    }
</script>


<style lang="scss" scoped>
    .alert-field {
        .div-header {
            background-color: #d6e1e5;
            width: 100%;
            height: 24px;
            font-size: 12px;
        }
        .p-text {
            padding-top: 3px;
            padding-left: 10px;
            font-size: 12px;
        }
        /deep/ .el-form-item__label {
            font-size: 12px;
        }
        /deep/ .el-form-item {
            margin: 1px;
            padding-right: 20px;
            // height: 50px;
        }
    }
</style>
