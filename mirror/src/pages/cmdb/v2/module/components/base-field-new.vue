<template>
    <div id="addmodule">
        <div style="padding:0px 0px 10px 0px;text-align:center;"></div>
        <el-form :model="formDatas"
                 :rules="formRules"
                 ref="formDatas"
                 label-width="100px">
            <el-form-item label="模型分组:"
                          required>
                <treeselect class="yw-treeselect"
                            :disabled="['edit'].indexOf(commonObj.moduleStatus) > -1"
                            v-model="formDatas.catalogObj.id"
                            :options="moduleOptions"
                            :multiple="false"
                            :default-expand-level="1"
                            :auto-load-root-options="false"
                            :limit="1"
                            :clearable="false"
                            :flat="true"
                            noOptionsText="暂无数据"
                            @select="selectParentModule"
                            placeholder="请选择模型所属分组:">
                </treeselect>
            </el-form-item>
            <el-form-item label="引用模型:">
                <treeselect class="yw-treeselect"
                            v-model="formDatas.refModulesIds"
                            :options="moduleLists"
                            :multiple="true"
                            :auto-load-root-options="false"
                            :limit="1"
                            :flat="true"
                            noOptionsText="暂无数据"
                            placeholder="请选择引用模型">
                </treeselect>
            </el-form-item>
            <el-form-item label="模型编码:"
                          prop="code"
                          required>
                <el-input placeholder="支持英文字母、下划线"
                          style="width:178px;"
                          :disabled="['edit'].indexOf(commonObj.moduleStatus) > -1"
                          v-model="formDatas.code">
                </el-input>
            </el-form-item>
            <el-form-item label="模型标签:">
                <el-tag v-for="(item,index) in formDatas.tags"
                        :key="index"
                        :closable="true"
                        :close-transition="false"
                        @close="handleCloseTag(index)">{{item.tag}}
                </el-tag>

                <el-input class="input-new-tag"
                          v-if="inputVisible"
                          v-model="inputValue"
                          ref="saveTagInput"
                          size="mini"
                          @keyup.enter.native="handleInputConfirm"
                          @blur="handleInputConfirm">
                </el-input>
                <el-button v-else
                           class="button-new-tag"
                           size="small"
                           @click="showInput"
                           style="padding: 1px 20px;">+ 点击新增</el-button>
            </el-form-item>
            <el-form-item label="模型图标:"
                          prop="iconUrl"
                          required>
                <div class="btnIcon"
                     @click="selectIcon">
                    <span :class="{dis:icondisplay}">选择图标</span>
                    <div :class="{dis:!icondisplay}"
                         style="background:#1d6aa7;border-radius:3px;">
                        <img width="30"
                             height="30"
                             :src="formDatas.iconUrl" />
                        <div style="position: absolute;padding: 0 7px;height:20px;line-height:20px;top:43px;">修改图标</div>
                    </div>
                </div>
            </el-form-item>
            <!-- <el-form-item label="操作权限:">
                <el-checkbox-group v-model="formDatas.authsIds"
                                   @change="changeAuths">
                    <el-checkbox :label="item.id"
                                 v-for="(item,index) in checkDatas"
                                 :key="index">{{item.authName}}</el-checkbox>
                </el-checkbox-group>
            </el-form-item> -->
            <el-form-item label="附属模型:">
                <el-radio-group v-model="formDatas.isVice">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="是否需要审核:">
                <el-radio-group v-model="formDatas.enableApprove">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                </el-radio-group>
            </el-form-item>
            <div style="text-align: center;">
                <el-button @click="cancel">取消</el-button>
                <el-button @click="resetForm('formDatas')">重置</el-button>
                <el-button @click="stepTo(1,'next')"
                           type="primary">下一步</el-button>
            </div>
        </el-form>
        <el-dialog :visible.sync="iconDialog"
                   v-if="iconDialog"
                   class="iconDialog"
                   @close="iconDialogClose">
            <el-tabs v-model="activeName">
                <el-tab-pane label="系统图标库"
                             name="sysicon">
                    <div class="sysicon">
                        <li v-for="(item,index) in sysicons"
                            :key="index"
                            :class="{liactive:activeIcon==item.id}"
                            class="iconlist"
                            v-on:click="iconClick(item)">
                            <img width="30"
                                 height="30"
                                 :src="item.iconUrl" />
                            <i :class="{active:activeIcon==item.id,nactive:activeIcon!=item.id}"
                               class="fa fa-check-circle"></i>
                        </li>
                        <!--解决浮动之后无法撑开外层div的问题-->
                        <div style="clear:both;"></div>
                    </div>
                    <el-pagination style="clear:both;margin-top:20px;"
                                   :page-size="cuspage.pageSize"
                                   @current-change="getSysIcons"
                                   :current-page.sync="syspage.currPage"
                                   layout="prev, pager, next, jumper"
                                   :total="syspage.total">
                    </el-pagination>
                    <div style="text-align:center;margin-top:20px;">
                        <el-button type="primary"
                                   @click="iconcommit"
                                   :disabled="!btn_active">确定</el-button>
                        <el-button @click="iconCancel">取消</el-button>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-dialog>
    </div>
</template>

<script>
    import { mapState } from 'vuex'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import { filterVueTree } from 'src/assets/js/utility/rb-filters.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import CommonOption from 'src/utils/commonOption.js'
    import { isEnUnderLine } from 'src/utils/validate.js'

    export default {
        name: 'BaseFields',
        mixins: [CommonOption],
        props: ['parentForm'],
        components: {
            Treeselect
        },
        data() {
            return {
                // 选中的表单值
                formDatas: {
                    catalogObj: { id: null, catalogName: '', catalogCode: '' },// 模型分组
                    refModules: [],// 引用模型(格式：[{}])
                    refModulesIds: [],// 引用模型id集合
                    code: '',// 模型编码
                    tags: [],// 标签
                    iconUrl: '',// 图片地址
                    auths: [],// 权限
                    authsIds: [],// 权限ids集合
                    isVice: 0,// 附属模型
                    enableApprove: 0,// 是否需要审核
                },

                // 操作权限列表
                checkDatas: [],
                // 模型分组列表
                moduleOptions: [],
                // 引用模型列表
                moduleLists: [],

                activeName: 'sysicon',
                iconDialog: false,
                syspage: {
                    currPage: 1,
                    pageSize: 27,
                    total: 0
                },
                cuspage: {
                    currPage: 1,
                    pageSize: 27,
                    total: 0
                },
                inputVisible: false,
                inputValue: '', // 标签域输入值
                beforeValue: '', // 记录标签域上次输入值，以便校验
                sysicons: [], // 系统图标库
                customicons: [], // 自定义图标库
                currSelectIcon: '', // 当前选中的图标

                icondisplay: false,
                formRules: {
                    // catalogId: [
                    //   {
                    //     required: true,
                    //     message: '不能为空',
                    //   }
                    // ],
                    code: [
                        {
                            required: true,
                            message: '不能为空',
                        }, {
                            min: 1,
                            max: 30,
                            message: '长度在 1 到 30 个字符',
                            trigger: 'blur'
                        },
                        {
                            validator: isEnUnderLine,
                            trigger: 'blur'
                        }
                    ],
                    // iconUrl: [{
                    //   required: true,
                    //   message: '不能为空',
                    // }],

                },
                activeIcon: '',
                btn_active: false,
                headers: {
                }
            }
        },
        computed: {
            ...mapState({
                commonObj: state => state.model.commonObj,
                moduleObj: state => state.model.moduleObj,
            }),
        },
        methods: {
            // 权限修改
            changeAuths(val) {

                this.formDatas.auths = this.checkDatas.filter((item) => {
                    return val.indexOf(item.id) > -1
                })
            },
            // 查询模型详情
            getModuleDetails() {
                if (!this.moduleObj.id) {
                    return false
                }
                let params = {
                    moduleId: this.moduleObj.id
                }
                return rbCmdbModuleServiceFactory.getModuleDetail(params).then((data) => {
                    return data
                })

            },
            // 获得权限列表
            getCheckDatas() {
                let params = {
                    authOwner: '模型'
                }
                rbCmdbModuleServiceFactory.authModule(params).then((data) => {
                    this.checkDatas = data
                })

            },
            // 校验
            valid(obj) {
                let tip = true
                if (!obj.data) {
                    tip = false
                    this.$confirm(obj.tip, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                }
                return tip
            },
            // 下一步
            async stepTo(number, direction) {
                if (!this.valid({ data: this.formDatas.catalogObj.id, tip: '模型分组不能为空,如果模型列表没有可用数据，可去添加模型分组' })) {
                    return false
                }
                if (!this.valid({ data: this.formDatas.code, tip: '模型编码不能为空' })) {
                    return false
                }
                if (!this.valid({ data: this.formDatas.iconUrl, tip: '模型图标不能为空' })) {
                    return false
                }
                this.$refs['formDatas'].validate(async (valid) => {
                    if (valid) {
                        this.formDatas.refModules = [...new Set(this.formDatas.refModulesIds)].map((item) => {
                            return {
                                id: item
                            }
                        })
                        let obj = {
                            'catalogId': this.formDatas.catalogObj.id, // 分组id
                            'code': this.formDatas.code, // 模型编码
                            'iconUrl': this.formDatas.iconUrl, // 图标地址
                            'auths': this.formDatas.auths, // 权限列表
                            'refModules': this.formDatas.refModules, // 引用模型列表
                            'tags': this.formDatas.tags, // 标签列表
                            'name': this.formDatas.catalogObj.catalogName,
                            'isVice': this.formDatas.isVice,
                            'enableApprove': this.formDatas.enableApprove
                        }

                        this.$store.commit('setModuleObj', obj)
                        if (this.commonObj.moduleStatus === 'add') {
                            let valid = await this.validModule()
                            if (valid.success) {
                                this.$emit('stepTo', { number: number, direction: direction })
                            } else {
                                this.$message.error(valid.message)
                            }
                        } else {
                            this.$emit('stepTo', { number: number, direction: direction })
                        }
                    }
                })
            },

            // 取消
            cancel() {
                this.$emit('setShow', false)
            },

            // 获得模型分组数据
            getParentModule() {
                this.showFullScreenLoading()
                let params = {
                    catalogId: this.commonObj.topCatalogId
                }
                rbCmdbModuleServiceFactory.getAllTree(params).then((data) => {
                    this.moduleOptions = filterVueTree([data], { id: 'id', label: 'catalogName', children: 'child', isDisabled: (item) => { return item.hasModule }, autoAddRoot: true })[0].children
                    // 新增状态
                    if (this.commonObj.moduleStatus === 'add') {
                        // 默认选中第一条数据
                        // this.formDatas.catalogObj.id = this.moduleOptions[0].id;
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 选择模型分组数据
            selectParentModule(node) {
                if (node.selfDatas.hasModule) {
                    this.formDatas.catalogObj = { id: '', catalogName: '', catalogCode: '' }
                    return
                }
                this.formDatas.catalogObj = node.selfDatas
                // if (['copy'].indexOf(this.commonObj.moduleStatus) < 0) {
                //   this.getLeafModule();
                // }

            },
            // 取消选择引用模型数据
            deselectParentModule(node) {
                this.formDatas.catalogObj.splice(this.formDatas.catalogObj.findIndex(item => item.id === node.id), 1)
            },
            // 选择引用模型数据
            selectRefModule(node) {
                if (['edit'].indexOf(this.commonObj.moduleStatus) > -1 && node.id === this.moduleObj.id) {
                    this.$confirm('引用模型与自身重复，请重新选择', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {


                    })
                    this.formDatas.refModules.splice(this.formDatas.refModules.findIndex(item => item.id === node.id), 1)
                    return false
                }

                this.formDatas.refModules.push(node.selfDatas)
            },
            // 取消选择引用模型数据
            deselectRefModule(node) {
                this.formDatas.refModules.splice(this.formDatas.refModules.findIndex(item => item.id === node.id), 1)
            },
            // 获取默认引用模型
            getDefaultRefModules() {
                rbCmdbModuleServiceFactory.getModuleDetail({ moduleId: this.moduleObj.id }).then((res) => {
                    res.refModules.forEach(item => {
                        this.formDatas.refModulesIds.push(item.id)
                    })
                })
            },
            // 获得引用模型数据
            getLeafModule() {
                let params = {
                    'catalogId': this.commonObj.topCatalogId || '',
                }
                if (['copy'].indexOf(this.commonObj.moduleStatus) > -1) {
                    params.catalogId = ''
                }
                this.moduleLists = []
                rbCmdbModuleServiceFactory.getModuleTree(params).then((data) => {
                    if (data && data.length > 0) {
                        this.moduleLists = filterVueTree(data, { id: 'id', label: 'name', children: 'childModules', isDisabled: (item) => { return item.id === this.moduleObj.id }, autoAddRoot: true })[0].children
                    }

                })
            },

            selectIcon() {
                this.iconDialog = true
                this.getSysIcons()
                this.getCustomIcons()
            },
            // 获取系统图标
            getSysIcons() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = {
                    'pageNumber': this.syspage.currPage,
                    'pageSize': this.syspage.pageSize
                }
                let body = {
                    'iconcategory': 0 // 获取系统图标
                }
                rbCmdbModuleServiceFactory.getIcons(params, body).then((data) => {
                    if (data && data.dataList.length > 0) {
                        this.sysicons = data.dataList
                        if (this.formDatas.iconUrl !== '') {
                            this.sysicons.forEach((item) => {
                                if (item.iconUrl === this.formDatas.iconUrl) {
                                    this.activeIcon = item.id
                                }
                            })
                        }
                        this.syspage.currPage = data.pageNo
                        this.syspage.total = data.total
                    }
                }).catch(() => {
                    this.$notify({
                        title: '提示',
                        message: '加载图标失败',
                        type: 'error',
                        duration: 3000
                    })
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            sysCurrentChange(val) {
                this.syspage.currPage = val
                this.getSysIcons()
            },
            cusCurrentChange(val) {
                this.cuspage.currPage = val
                this.getCustomIcons()
            },
            // 获取自定义图标
            getCustomIcons() {
                let params = {
                    'pageNumber': this.syspage.currPage,
                    'pageSize': this.syspage.pageSize
                }
                let body = {
                    'iconcategory': 0 // 获取系统图标
                }
                rbCmdbModuleServiceFactory.getIcons(params, body).then((data) => {
                    if (data && data.dataList.length > 0) {
                        this.customicons = data.dataList
                        this.cuspage.currPage = data.pageNo
                        this.cuspage.total = data.total
                    }
                })
            },
            // 删除标签
            handleCloseTag(tag) {
                this.formDatas.tags.splice(this.formDatas.tags.indexOf(tag), 1)
            },
            showInput() {
                this.inputVisible = true
                this.$nextTick(() => {
                    this.$refs.saveTagInput.$refs.input.focus()
                })
            },
            // 标签名称校验
            handleInputConfirm() {
                let inputValue = this.inputValue
                let exist = this.formDatas.tags.some((item) => {
                    return item.tag === inputValue
                })
                if (this.formDatas.tags.length > 0 && exist) {
                    this.$notify({
                        title: '提示',
                        message: '标签名称已经存在',
                        type: 'warning',
                        duration: 3000
                    })
                    return
                }
                if (inputValue.trim().length > 20) {
                    this.$notify({
                        title: '提示',
                        message: '标签名称必须小于20个字符',
                        type: 'warning',
                        duration: 3000
                    })
                    return
                }
                if (inputValue) {
                    this.formDatas.tags.push({ tag: inputValue })
                }
                this.inputVisible = false
                this.inputValue = ''
            },
            uploadError() {
                this.$notify({
                    title: '提示',
                    message: '图标上传失败',
                    type: 'error',
                    duration: 3000
                })
            },
            uploadSuccess(response) {
                if (response.success) {
                    this.getCustomIcons()
                    this.$notify({
                        title: '提示',
                        message: '图标上传成功',
                        type: 'success',
                        duration: 3000
                    })
                } else {
                    this.$notify({
                        title: '提示',
                        message: '图标上传失败',
                        type: 'error',
                        duration: 3000
                    })
                }
            },
            iconClick(item) {
                this.activeIcon = item.id
                this.currSelectIcon = item.iconUrl
                this.btn_active = true
            },
            // 确定选择的图标
            iconcommit() {
                this.formDatas.iconUrl = this.currSelectIcon
                this.iconDialog = false
                this.icondisplay = true
            },
            // 图标选择Dialog 取消按钮
            iconCancel() {
                this.iconDialog = false
            },
            // iconDialog关闭时的
            iconDialogClose() {
                this.currSelectIcon = ''
                this.syspage.currPage = 1
                this.cuspage.currPage = 1
                this.activeIcon = null
                this.btn_active = false
            },
            validForm() {
                let flag = false
                this.$refs['formDatas'].validate((valid) => {
                    if (valid) {
                        flag = true
                    }
                })
                return flag
            },
            // 重置
            resetForm() {

                // this.$refs[formName].resetFields();
                this.formDatas.refModulesIds = []
                this.formDatas.iconUrl = ''
                this.formDatas.tags = []
                this.formDatas.auths = []
                this.formDatas.authsIds = []
                this.formDatas.enableApprove = 0
                this.formDatas.isVice = 0
                this.icondisplay = false
                this.btn_active = true


                if (['edit'].indexOf(this.commonObj.moduleStatus) < 0) {
                    this.formDatas.catalogObj.id = null
                    this.formDatas.code = ''
                }
            },
            // 模型是否存在校验
            validModule() {
                this.showFullScreenLoading({ text: '正在校验数据, 请稍等...' })
                let params = {
                    // parentCatalogId: '',
                    catalogId: this.moduleObj.catalogId,
                    moduleCode: this.moduleObj.code
                }

                return rbCmdbModuleServiceFactory.validModule(params).then((data) => {
                    return data
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 初始化
            async init() {
                this.$store.commit('setCommonObj', { rowDetails: '' })
                if (['edit', 'copy'].indexOf(this.commonObj.moduleStatus) > -1) {
                    let detailsDatas = await this.getModuleDetails()
                    this.$store.commit('setCommonObj', { rowDetails: detailsDatas })
                    this.formDatas.catalogObj = detailsDatas.moduleCatalog
                    this.formDatas.catalogObj.id = detailsDatas.catalogId
                    this.formDatas.refModules = detailsDatas.refModules
                    this.formDatas.refModulesIds = detailsDatas.refModules.map((item) => { return item.id })
                    this.formDatas.tags = detailsDatas.tags
                    this.formDatas.iconUrl = detailsDatas.iconUrl
                    this.formDatas.auths = detailsDatas.auths
                    this.formDatas.authsIds = detailsDatas.auths.map((item) => { return item.id })
                    this.formDatas.code = detailsDatas.code
                    this.icondisplay = true
                    this.btn_active = true
                    this.formDatas.enableApprove = detailsDatas.enableApprove
                    this.formDatas.isVice = detailsDatas.isVice
                }
                if (['copy'].indexOf(this.commonObj.moduleStatus) > -1) {
                    this.formDatas.catalogObj.id = null
                    this.formDatas.code = ''
                }
                this.getParentModule()
                this.getDefaultRefModules()
                this.getLeafModule()
                this.getCheckDatas()
            }
        },
        mounted: function () {
            this.init()
        }
    }
</script>
<style scoped>
    .el-form-item {
        margin-bottom: 10px;
    }
    #addmodule {
        width: 440px;
        margin: 0 auto;
    }

    #addmodule .el-tag {
        margin-left: 10px;
    }

    .input-new-tag {
        width: 78px;
        margin-left: 10px;
        vertical-align: bottom;
    }

    .button-new-tag {
        margin-left: 10px;
        height: 24px;
        line-height: 22px;
        padding-top: 0;
        padding-bottom: 0;
    }

    .dis {
        display: none;
    }

    .btnIcon {
        width: 60px;
        height: 60px;
        border: 1px dashed #d1dbe5;
        font-size: 12px;
        color: #48576a;
        line-height: 60px;
        text-align: center;
        border-radius: 2px;
        cursor: pointer;
    }

    .sysicon .active {
        display: block;
        color: #6adc71;
        float: right;
    }

    .sysicon .liactive {
        background: #1681c4;
        border-radius: 10px;
    }

    .sysicon .nactive {
        display: none;
    }

    .sysicon {
        background-color: #1d6aa7;
        border-radius: 5px;
        cursor: pointer;
        padding: 0px 0px 10px 0px;
    }

    .sysicon li:hover {
        border-radius: 10px;
        background: #1681c4;
    }

    .sysicon .iconlist {
        padding: 10px;
        float: left;
        width: 40px;
        height: 40px;
        list-style-type: none;
        margin: 10px 0px 0px 10px;
    }
</style>
