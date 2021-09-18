<template>
    <div class="code-mirror-box">
        <el-form class="is-required"
                 ref="codeForm"
                 :model="codeInfo"
                 :rules="codeFormRules"
                 label-width="100px">
            <el-form-item label="脚本名称"
                          prop="codeName"
                          v-if="codeNameShow">
                <el-input v-model="codeInfo.codeName"
                          placeholder="请输入脚本名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="脚本分类"
                          prop="labelId"
                          v-if="!isFromWorkManage && !isPreview">
                <el-select v-model="codeInfo.labelId"
                           placeholder="请选择脚本分类"
                           filterable
                           clearable>
                    <el-option v-for="val in labelList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="自定义参数"
                          prop="is_public">
                <!-- <customParams :paramCode="currentCodeInfo.ops_param_code" -->
                <customParams :paramCode="codeInfo.ops_param_reference_list"
                              :paramsType="paramsType"
                              @setSelectedKey="setSelectedKey"
                              @custompParameter="custompParameter">
                </customParams>
            </el-form-item>
            <el-form-item label="打包密码"
                          v-if="paramTypes.includes('2')||paramTypes.includes('3')||paramTypes.includes('4')"
                          prop="package_password">
                <el-input v-model="codeInfo.package_password"
                          type="password"
                          placeholder="请输入打包密码"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="公共脚本"
                          prop="is_public">
                <el-radio-group v-model="codeInfo.is_public"
                                :disabled="readonly">
                    <el-radio :label="1"
                              name="type">是</el-radio>
                    <el-radio :label="0"
                              name="type">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="脚本来源"
                          prop="codeType">
                <el-radio-group v-model="codeInfo.codeType"
                                :disabled="readonly"
                                @change="changeCodeType">
                    <el-radio label="-1"
                              name="type">手工录入</el-radio>
                    <el-radio label="0"
                              name="type">脚本克隆</el-radio>
                    <el-radio label="1"
                              name="type">本地脚本</el-radio>
                    <el-radio label="2"
                              name="type"
                              v-if="publicCodeShow">公共脚本</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label>
                <div v-if="codeInfo.codeType === '0'">
                    脚本名称：
                    <el-select v-model="codeSelectedIndex"
                               :disabled="readonly"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="(val,index) in codeCloneList"
                                   :key="val.script_id"
                                   :label="val.script_name"
                                   :value="index"></el-option>
                    </el-select>
                </div>
                <div v-if="codeInfo.codeType === '1'">
                    <el-upload class="upload-demo"
                               :limit="1"
                               :http-request="UploadCodeFile">
                        <el-button size="small"
                                   type="primary">上传脚本</el-button>
                        <span>扩展名为：.sh、.bat、.py</span>
                    </el-upload>
                </div>
                <div v-if="codeInfo.codeType === '2'">
                    公共脚本：
                    <el-select v-model="codeSelectedIndex"
                               :disabled="readonly"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="(val,index) in publicCodeList"
                                   :key="val.script_id"
                                   :label="val.script_name"
                                   :value="index"></el-option>
                    </el-select>
                </div>
            </el-form-item>
            <el-form-item label="脚本内容"
                          prop="codeContent"
                          class="mtop10">
                <div class="lang-type">
                    <el-radio-group v-model="codeInfo.languageType"
                                    @change="changeLanguage"
                                    :disabled="readonly">
                        <el-radio :label="1"
                                  name="type">shell</el-radio>
                        <el-radio :label="2"
                                  name="type">bat</el-radio>
                        <!-- <el-radio :label="3" name="type">perl</el-radio> -->
                        <el-radio :label="3"
                                  name="type">python</el-radio>
                        <!-- <el-radio :label="5" name="type">powershell</el-radio> -->
                    </el-radio-group>
                    <el-tooltip class="item"
                                effect="dark"
                                content="请确保脚本为非交互式脚本，并且在本地调试通过。"
                                placement="top">
                        <span class="icon iconfont tool-tip">&#xe635;</span>
                    </el-tooltip>
                </div>
                <el-input type="textarea"
                          id="editor"
                          name="editor"
                          v-model="codeInfo.codeContent"
                          :rows="10"></el-input>
            </el-form-item>
            <el-form-item label="脚本使用说明"
                          v-if="isAddCode || codeInfo.script_use_desc"
                          prop="script_use_desc"
                          class="mtop10">
                <!-- 在脚本、作业新增及编辑页面  可编辑 -->
                <el-input type="textarea"
                          v-model="codeInfo.script_use_desc"
                          :disabled="readonly"
                          :rows="3"></el-input>
            </el-form-item>
            <el-form-item v-if="flag == '脚本'"
                          label="分组名称"
                          prop="groupTagids">
                <el-tag :key="tag.group_id"
                        style="margin-right: 5px;"
                        v-for="tag in codeInfo.groupTagids"
                        closable
                        :disable-transitions="false"
                        @close="handleClose(tag)"
                        size="small">
                    {{tag.group_name}}
                </el-tag>
                <el-popover placement="bottom-start"
                            trigger="click">
                    <comtree :ref="treeName"
                             :data="groupTreeData"
                             :props="gruopTreeDefault"
                             :exId="treeName"
                             :ex-control="true"
                             :ex-control-opt="customControl"
                             ex-show-search
                             @node-click="handleTreeClick">
                    </comtree>
                    <el-button slot="reference"
                               class="mod-btn"
                               size="small">请选择</el-button>
                </el-popover>
            </el-form-item>
        </el-form>
        <el-dialog class="yw-dialog"
                   title="新增子分组"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :append-to-body="true"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="groupAddForm"
                         :model="groupAddForm"
                         label-width="100px">
                    <el-form-item label="子分组名称"
                                  prop="name">
                        <el-input v-model="groupAddForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="子分组描述">
                        <el-input v-model="groupAddForm.descr"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleUpdateDepartment">确定</el-button>
                <el-button @click="departmentDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import customParams from './custom-parameter'
    import comtree from './tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'

    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    import * as CodeMirror from 'codemirror/lib/codemirror'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/monokai.css'

    import 'codemirror/mode/javascript/javascript'
    import 'codemirror/mode/shell/shell'
    // import "codemirror/mode/bat/bat"
    import 'codemirror/mode/perl/perl'
    import 'codemirror/mode/python/python'
    import 'codemirror/mode/powershell/powershell'
    let Base64 = require('js-base64').Base64

    export default {
        props: ['flag', 'currentCodeInfo', 'codeCloneList', 'codeNameShow', 'publicCodeShow', 'isFromWorkManage', 'isPreview', 'isAddCode', 'fromCodeList', 'paramType'],
        components: {
            comtree,
            customParams
        },
        data() {
            const that = this
            return {
                paramsType: '',
                publicCodeList: [],
                groupAddForm: {
                    parentid: '',
                    name: '',
                    descr: ''
                },
                treeName: 'gtree',
                // dynamicTags: [],
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                groupTreeData: [],
                // 分类标签
                labelList: [],
                // codemirror
                codeMirrorEditor: null,
                timer: null,
                codeSelectedIndex: '',
                languageTypeList: ['shell', 'bat', 'python'],
                codeInfo: {
                    codeName: '', // 脚本名称
                    labelId: '', // 脚本分类
                    codeType: '-1', // 脚本来源
                    languageType: 1, // 脚本语言类型
                    codeCloneSelected: '', // 已选中脚本克隆
                    publicCodeSelected: '', // 已选中公共脚本
                    codeContent: '',
                    is_public: 0,
                    script_use_desc: '',
                    groupTagids: [],
                    // 自定义参数code  多个逗号分隔
                    ops_param_code: '',
                    // 自定义参数产出密码
                    package_password: '',
                },
                departmentDialogVisible: false,
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: that.customGroupAdd
                    }
                ],
                codeFormRules: {
                    codeName: [
                        {
                            required: this.codeNameShow ? true : false,
                            message: '请输入脚本名称!',
                            trigger: 'blur'
                        },
                        {
                            min: 4,
                            max: 100,
                            message: '长度在 4 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    package_password: [
                        {
                            required: true,
                            message: '请输入打包密码!',
                            trigger: 'blur'
                        },
                        {
                            min: 6,
                            max: 100,
                            message: '长度在至少 6 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    codeType: [
                        {
                            required: true,
                            message: '请选择脚本录入方式!',
                            trigger: 'blur'
                        }
                    ],
                    codeContent: [
                        {
                            required: true,
                            message: '请输入脚本内容!',
                            trigger: 'blur'
                        }
                    ],
                    groupTagids: [
                        { required: true, message: '请选择分组名称', trigger: 'change' }
                    ]
                },
                // 已选中的自定义参数类型
                paramTypes: [],
                custompParameterList: []
            }
        },
        computed: {
            readonly() {
                return this.isPreview || this.fromCodeList
            }
        },
        watch: {
            // 当前脚本信息变更，更新脚本编辑框
            currentCodeInfo: {
                handler() {
                    this.setCodeContent()
                    console.log('codeInfo', this.codeInfo)
                },
                deep: true
            },
            // 当前克隆脚本变更，更新脚本编辑框
            codeSelectedIndex(val) {
                if (val !== '') {
                    let codeList = this.$utils.deepClone(this.codeCloneList)
                    // 公共脚本类型，设置已选中脚本
                    if (this.codeInfo.codeType === '2') {
                        codeList = this.$utils.deepClone(this.publicCodeList)
                    }
                    this.codeInfo.codeCloneSelected = codeList[val].script_id
                    this.codeInfo.codeContent = codeList[val].script_content
                    this.codeInfo.languageType = codeList[val].content_type
                    this.codeInfo.script_use_desc = codeList[val].script_use_desc
                    this.codeInfo.codeContent && this.codeMirrorEditor.setValue(Base64.decode(this.codeInfo.codeContent))
                } else {
                    this.codeInfo.codeCloneSelected = ''
                    this.codeMirrorEditor.setValue('')
                }
            },
            // 作业管理=>更新脚本信息到当前行
            codeInfo: {
                handler(newVal) {
                    // if(this.isFromWorkManage) {
                    this.$emit('passCodeContent', newVal)
                    // }
                },
                deep: true
            },
            readonly: {
                handler(newVal) {
                    this.$nextTick(() => {
                        if (newVal) {
                            // 该属性为驼峰 readOnly
                            this.codeMirrorEditor.setOption('readOnly', true)
                        } else {
                            this.codeMirrorEditor.setOption('readOnly', false)
                        }
                    })
                },
                immediate: true
            },
        },
        created() {
            this.paramsType = this.paramType

        },
        mounted() {
            this.initCodeMirror()
            this.getGroupTree()
            this.queryOpsLabelList()
            // 获取公共脚本
            if (this.publicCodeShow) {
                this.getPublicCodeList()
            }
        },
        methods: {
            custompParameter(val) {
                this.codeInfo.ops_param_reference_list = val

            },
            // 查询分类标签
            queryOpsLabelList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsLabelList()
                    .then(res => {
                        this.labelList = res
                    })
            },
            // 设置自定义参数code 
            // setSelectedKey(paramCodeStr, paramTypes) {
            //     this.codeInfo.ops_param_code = paramCodeStr
            //     this.paramTypes = paramTypes
            // },
            setSelectedKey(paramTypes) {
                console.log('paramTypes===', paramTypes)
                this.paramTypes = paramTypes
            },
            getPublicCodeList() {
                let req = {
                    is_public: 1,  // 公共脚本参数
                    page_no: 1,
                    page_size: 100
                }
                this.loading = true
                this.publicCodeList = []
                rbAutoOperationServicesFactory
                    .queryOpsScriptList(req)
                    .then(res => {
                        this.loading = false
                        this.publicCodeList = res.dataList
                    })
                    .catch(error => {
                        this.loading = false
                        this.$message({
                            message: error.message,
                            type: 'error'
                        })
                    })
            },
            // 快速新增分组
            customGroupAdd(node, data) {
                this.departmentDialogVisible = true
                this.groupAddForm.parentid = data.group_id
            },
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            // 新增分组
            handleUpdateDepartment() {
                this.$refs['groupAddForm'].validate((valid) => {
                    if (valid) {
                        const params = {
                            parent_id: this.groupAddForm.parentid,
                            group_name: this.groupAddForm.name,
                            group_desc: this.groupAddForm.descr
                        }
                        groupDataService.saveGroup(params).then(() => {
                            this.$message({
                                message: '新增分组成功',
                                type: 'success'
                            })
                            this.getGroupTree()
                            this.resetGroupAddForm()
                            this.handleDepartmentDialogClose()
                        }).catch(() => {
                            this.$message({
                                message: '操作失败',
                                type: 'error'
                            })
                        })
                    } else {
                        return false
                    }
                })
            },
            // 获取分组树
            getGroupTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.groupTreeData = res
                    // if (res.length > 0) {
                    //   this.deviceAuthDataExpanded = [res[0].group_id]
                    // }
                })
            },
            resetGroupAddForm() {
                this.groupAddForm = {
                    parentid: '',
                    name: '',
                    descr: ''
                }
            },
            handleClose(tag) {
                this.codeInfo.groupTagids.splice(this.codeInfo.groupTagids.indexOf(tag), 1)
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.codeInfo.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.codeInfo.groupTagids.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.treeVisible = false
            },
            setCodeContent() {
                this.codeInfo = JSON.parse(JSON.stringify(Object.assign(this.codeInfo, this.currentCodeInfo)))
                // 新建脚本，清空文本框
                if (!this.currentCodeInfo.codeContent) {
                    this.codeInfo.codeContent = ''
                    this.codeMirrorEditor.setValue('')
                }
                // 公共脚本类型，设置已选中脚本
                if (this.codeInfo.codeType === '2') {
                    this.publicCodeList.forEach((item, index) => {
                        if (item.script_id === this.codeInfo.codeId) {
                            this.codeSelectedIndex = index
                        }
                    })
                }
                // 编辑脚本，设置内容
                if (this.codeSelectedIndex === '') {
                    this.codeInfo.codeContent && this.codeMirrorEditor.setValue(Base64.decode(this.codeInfo.codeContent))
                }
            },
            changeLanguage(val) {
                this.codeMirrorEditor.setOption('mode', this.languageTypeList[val - 1])
            },
            // 当前脚本来源变更，更新脚本编辑框
            changeCodeType() {
                this.codeSelectedIndex = ''
                this.codeInfo.codeCloneSelected = ''
                this.codeInfo.script_use_desc = ''
                this.codeMirrorEditor.setValue('')
            },
            UploadCodeFile(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                rbAutoOperationServicesFactory
                    .readLocalScriptFile(formData)
                    .then(res => {
                        this.$message.success('上传成功')
                        if (res.flag) {
                            this.codeInfo.codeContent = res.biz_data.script_content
                            this.codeInfo.languageType = res.biz_data.content_type
                            this.codeInfo.codeContent && this.codeMirrorEditor.setValue(Base64.decode(this.codeInfo.codeContent))
                        }
                    })
            },
            initCodeMirror() {
                let _self = this
                let myTextarea = document.getElementById('editor')
                this.codeMirrorEditor = CodeMirror.fromTextArea(myTextarea, {
                    mode: 'shell', // 编辑器语言
                    theme: 'monokai', // 编辑器主题
                    extraKeys: { Ctrl: 'autocomplete' }, // ctrl可以弹出选择项
                    lineNumbers: true // 显示行号
                })

                this.setCodeContent()

                this.codeMirrorEditor.on('change', function () {
                    // 事件触发后更新脚本内容
                    if (_self.timer) {
                        clearTimeout(_self.timer)
                    }
                    _self.timer = setTimeout(() => {
                        let codeContent = _self.codeMirrorEditor.getValue()
                        _self.codeInfo.codeContent = Base64.encode(codeContent)
                        _self.$emit('passCodeContent', _self.codeInfo)
                        _self.$refs.codeForm && _self.$refs.codeForm.validateField('codeContent')
                    }, 500)
                })
            },
        }
    }
</script>


<style lang="scss" scoped>
    .code-mirror-box .el-textarea {
        width: 100% !important;
        max-height: 300px;
    }
    .code-mirror-box .el-textarea,
    .code-mirror-box .CodeMirror-code {
        font-size: 14px;
        line-height: 120%;
    }
    .lang-type {
        border: 1px solid $color-border;
        padding: 0 5px;
    }
    .pbottom10 {
        padding-bottom: 10px;
    }
    .mtop10 {
        margin-top: 10px;
    }
    .tool-tip {
        float: right;
        /* margin-top: 10px;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              padding: 2px 10px; */
    }
</style>
