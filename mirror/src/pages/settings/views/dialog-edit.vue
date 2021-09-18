<template>
    <!-- 绑定模型事件 -->
    <el-dialog class="yw-dialog"
               :close-on-click-modal="false"
               v-if="dialogMsg.dialogVisible"
               width="1100px"
               :title="dialogMsg.data.title"
               @close="cancel"
               :visible.sync="dialogMsg.dialogVisible">
        <section class="yw-dialog-main"
                 style="max-height:800px;">
            <!-- 查询条件 -->
            <el-form class="yw-form"
                     label-width="70px"
                     :inline="true"
                     :rules="rules"
                     ref="ruleForm"
                     :model="formData">
                <el-form-item label="视图编码"
                              prop="code">
                    <el-input v-model="formData.code"
                              placeholder=""></el-input>
                </el-form-item>
                <el-form-item label="视图名称"
                              prop="name">
                    <el-input v-model="formData.name"
                              placeholder=""></el-input>
                </el-form-item>
                <el-form-item label="模型分组"
                              prop="moduleGroup">
                    <el-select v-model="formData.moduleGroup"
                               @change="changeModuleGroup"
                               filterable
                               placeholder="请选择">
                        <el-option v-for="item in moduleGroupOptions"
                                   :key="item.id"
                                   :label="item.catalogName"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="模型名称"
                              prop="moduleName"
                              class="is-required">
                    <treeselect class="yw-treeselect"
                                v-model="formData.moduleName"
                                :options="moduleNameOptions"
                                :limit="1"
                                :clearable="false"
                                @select="changeModuleName"
                                noOptionsText="暂无数据"
                                placeholder="请选择">
                    </treeselect>
                    <p style="color: #F56C6C"
                       v-if="myRules.moduleName[0].show">{{myRules.moduleName[0].message}}</p>
                </el-form-item>
                <!-- <section class="btn-wrap">
                    <el-button type="primary"
                               @click="query()">查询</el-button>
                    <el-button @click="add()">创建新视图</el-button>
                </section> -->
            </el-form>
            <!-- 查询条件 -->

            <!-- tabs -->
            <el-tabs class="yw-tabs">
                <el-tab-pane label="节点配置">
                </el-tab-pane>
            </el-tabs>
            <!-- tabs -->

            <!-- 内容 -->
            <el-container style="height:400px;">
                <el-aside width="300px"
                          style="border-right:1px solid #eee;">
                    <el-tree :data="treeDatas"
                             node-key="id"
                             :current-node-key="treeDatas && treeDatas.length>0 ? treeDatas[0].id :''"
                             :highlight-current="true"
                             :expand-on-click-node="false"
                             :props="defaultProps"
                             :check-on-click-node="true"
                             default-expand-all>
                        <span class="custom-tree-node"
                              slot-scope="{data}">
                            <span>{{ data.nodeName }}</span>
                            <span class="tree-btn-wrap">
                                <el-button type="text"
                                           @click="()=>createTree(data)"
                                           title="新增"
                                           icon="el-icon-plus"></el-button>
                                <el-button type="text"
                                           @click="()=>editTree(data)"
                                           title="编辑"
                                           icon="el-icon-edit"></el-button>
                                <el-button type="text"
                                           @click="()=>delTree(data)"
                                           title="删除"
                                           icon="el-icon-delete"></el-button>
                            </span>
                        </span>
                    </el-tree>
                </el-aside>
                <el-main style="position:relative;">
                    <el-form class="yw-form is-required"
                             :rules="rules"
                             ref="ruleForm2"
                             label-width="120px"
                             :inline="true"
                             :model="formData">
                        <el-form-item label="节点名称"
                                      class="is-required"
                                      prop="nodeName">
                            <el-input v-model="formData.nodeName"
                                      placeholder=""></el-input>
                        </el-form-item>
                        <el-form-item label="父节点名称">
                            <el-input v-model="formData.parentNodeName"
                                      :disabled="true"
                                      placeholder=""></el-input>
                        </el-form-item>
                        <el-form-item label="是否动态节点">
                            <el-checkbox v-model="formData.showActiveNode"
                                         @change="changeActiveNode"
                                         style="width:178px;"></el-checkbox>
                        </el-form-item>
                        <el-form-item label="节点图标">
                            <el-select style="width:178px"
                                       v-model="formData.nodeImg"
                                       @change="changeNodeImg"
                                       filterable
                                       placeholder="请选择">
                                <el-option v-for="item in nodeImgOptions"
                                           :key="item.id"
                                           :label="item.name"
                                           :value="item.id">
                                    <span style="float: left">{{ item.name }}</span>
                                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <div v-if="formData.showActiveNode">
                            <el-form-item label="使用配置项">
                                <el-checkbox v-model="formData.configChecked"
                                             @change="changeConfigChecked"></el-checkbox>
                            </el-form-item>
                            <br />
                            <el-form-item label="对应实例属性"
                                          class="is-required"
                                          prop="viewsAttr">
                                <el-input v-model="formData.viewsAttr"
                                          placeholder=""></el-input>
                            </el-form-item>
                            <br />
                            <el-form-item label="配置项名称"
                                          prop="configName"
                                          v-if="formData.configChecked">
                                <el-select v-model="formData.configName"
                                           @change="changeConfigName"
                                           filterable
                                           placeholder="请选择">
                                    <el-option v-for="item in configNameOptions"
                                               :key="item.codeId"
                                               :label="item.filedName"
                                               :value="item.codeId">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <br />
                            <el-form-item label="SQL配置"
                                          class="is-required"
                                          prop="configSQL"
                                          v-if="!formData.configChecked">
                                <el-input type="textarea"
                                          :rows="3"
                                          placeholder=""
                                          v-model="formData.configSQL">
                                </el-input>

                            </el-form-item>
                        </div>

                        <div v-if="formData.showActiveNode">
                            <el-form-item label="显示子节点数量">
                                <el-checkbox v-model="formData.showChildNode"
                                             style="width:178px;"></el-checkbox>
                            </el-form-item>
                            <el-form-item label="多数据显示分隔符">
                                <el-input v-model="formData.dataSplit"
                                          placeholder=""></el-input>
                            </el-form-item>
                            <div v-if="formData.showChildNode">
                                <section v-for="(sqlItem,sqlIndex) in formData.sqlConfig"
                                         :key="sqlIndex">
                                    <el-form-item label="显示前缀1">
                                        <el-input v-model="sqlItem.showPrefix"
                                                  placeholder=""></el-input>
                                    </el-form-item>
                                    <el-form-item label="SQL配置1">
                                        <el-input v-model="sqlItem.showSql"
                                                  placeholder=""></el-input>
                                    </el-form-item>
                                    <el-form-item label=""
                                                  class="yw-table-operator">
                                        <el-button type="text"
                                                   title="添加"
                                                   icon="el-icon-circle-plus-outline"
                                                   @click="addSQL()"></el-button>
                                        <el-button type="text"
                                                   title="删除"
                                                   icon="el-icon-delete"
                                                   v-if="formData.sqlConfig && formData.sqlConfig.length>1"
                                                   @click="delSQL(sqlIndex)"></el-button>
                                    </el-form-item>
                                </section>
                            </div>

                        </div>
                    </el-form>
                    <!-- 保存 -->
                    <!-- <section class="btn-wrap">
                        <el-button type="primary"
                                   @click="save()">保存</el-button>
                        <el-button @click="cancel()">取消</el-button>
                    </section> -->
                    <!-- 保存 -->
                </el-main>
            </el-container>
            <!-- 内容 -->
        </section>

        <!-- 确定 -->
        <section class="btn-wrap"
                 style="left:350px;">
            <el-button type="primary"
                       @click="save()">保存</el-button>
            <el-button @click="cancel()">关闭</el-button>
        </section>
        <!-- 确定 -->
    </el-dialog>

</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import CommonOption from 'src/utils/commonOption.js'
    import { isEnUnderLine } from 'src/utils/validate.js'
    import { filterVueTree } from 'src/assets/js/utility/rb-filters.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbCmdbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory'
    import rbConfigDictService from 'src/services/cmdb/rb-configDict-service.factory'

    export default {
        mixins: [updateComponent, CommonOption],
        props: ['dialogMsg'],
        components: {
            Treeselect
        },
        data() {
            return {
                // 树
                defaultProps: {
                    children: 'subNodeList',
                    label: 'nodeName'
                },
                // 模型名称
                moduleNameProps: {
                    children: 'childModules',
                    label: 'name'
                },

                // 表单数据
                formData: {
                    id: '',// 视图id
                    code: '',// 视图编码
                    name: '',// 视图名称
                    moduleGroup: '',// 模型分组
                    moduleName: null,// 模型名称

                    nodeId: '',// 节点id
                    nodeName: '',// 节点名称
                    parentNodeId: '',// 父节点id
                    parentNodeName: '',// 父节点名称
                    showActiveNode: true,// 是否动态节点
                    nodeImg: '',// 节点图标
                    configChecked: true,// 使用配置项
                    viewsAttr: '',// 实例属性
                    configName: '',// 配置项名称
                    configSQL: '',// sql配置
                    showChildNode: false,// 显示子节点数量
                    dataSplit: '',// 多数据显示分隔符
                    sqlConfig: [{ showPrefix: '', showSql: '' }],// sql配置
                },
                // 初始表单数据
                initFormData: {},

                // 模型分组
                moduleGroupOptions: [],
                // 模型名称
                moduleNameOptions: [],
                // 配置项名称
                configNameOptions: [],
                // 节点图标
                nodeImgOptions: [],
                // 树
                treeDatas: [],
                // 当前树节点
                activeTreeNode: {},
                // 自定义规则
                myRules: {
                    // 模型名称
                    moduleName: [{ show: false, required: true, message: '不能为空', trigger: 'change' }]
                },
                // 规则
                rules: {
                    code: [
                        { required: true, message: '不能为空', trigger: 'change' },
                        { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'change' },
                        {
                            validator: isEnUnderLine,
                            trigger: 'change'
                        }],
                    name: [
                        { required: true, message: '不能为空', trigger: 'change' },
                        { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'change' }],
                    // moduleName: [{ required: true, message: '不能为空', trigger: 'change' }],
                    moduleGroup: [{ required: true, message: '不能为空', trigger: 'change' }],
                    nodeName: [{ required: true, message: '不能为空', trigger: 'change' }],
                    viewsAttr: [
                        { required: true, message: '不能为空', trigger: 'change' },
                        {
                            validator: isEnUnderLine,
                            trigger: 'change'
                        }],
                    configName: [{ required: true, message: '不能为空', trigger: 'change' }],
                    configSQL: [{ required: true, message: '不能为空', trigger: 'change' }],
                }
            }
        },
        methods: {
            // 获得模型分组
            getModuleGroup() {

                let params = {
                    // viewCode: this.dialogMsg.data.item.viewCode,
                    // viewName: this.dialogMsg.data.item.viewName,
                    // catalogId: this.dialogMsg.data.item.catalogId,
                    // pageSize: 6,
                    // currentPage: 1,
                }
                return rbCmdbModuleService.getRootTree(params).then((res) => {
                    this.moduleGroupOptions = res
                    return res

                }).finally(() => {

                })
            },

            // 获得模型名称
            getModuleName() {

                this.moduleNameOptions = []
                let params = {
                    'catalogId': this.formData.moduleGroup,
                }

                rbCmdbModuleService.getModuleTree(params).then((res) => {
                    this.moduleNameOptions = filterVueTree(res, { id: 'id', label: 'name', children: 'childModules', })
                })
            },

            // 获得树
            getTree() {
                let params = {
                    id: this.formData.id,
                }
                return rbCmdbService.getViewsStructure(params).then((res) => {
                    this.treeDatas = res.viewNodeList

                    this.editTree(this.treeDatas[0])
                    this.formData.code = res.viewCode
                    this.formData.name = res.viewName
                    this.formData.moduleGroup = res.catalogId
                    this.formData.moduleName = res.moduleId

                    return res

                }).finally(() => {

                })
            },
            // 获得配置项名称
            getConfigName() {

                this.configNameOptions = []

                let params = this.formData.moduleName
                return rbCmdbCodeService.getModuleCodes(params).then((res) => {
                    this.configNameOptions = res
                }).finally(() => {

                })
            },
            // 获得节点图标
            getNodeImg() {
                let params = {
                    type: 'cmdb_view_icon'
                }
                return rbConfigDictService.getDictsByType(params).then((res) => {
                    this.nodeImgOptions = res
                }).finally(() => {

                })
            },
            // 切换模型分组
            changeModuleGroup() {
                this.formData.moduleName = null
                this.getModuleName()
            },
            // 切换模型名称
            changeModuleName(node) {
                this.formData.moduleName = node.id
                this.myRules.moduleName[0].show = false
                this.formData.configName = ''
                this.getConfigName()
            },
            // 切换树
            changeTree() {

            },
            // 切换配置项名称
            changeConfigName() {
                this.configNameOptions.some((item) => {
                    if (this.formData.configName === item.codeId) {
                        this.formData.viewsAttr = item.filedCode
                        this.formData.nodeName = item.filedName
                        return true
                    }
                })
            },
            // 切换配置项
            changeConfigChecked() {
                //
            },
            // 切换节点图标
            changeNodeImg() {

            },
            // 切换动态节点
            changeActiveNode() {
                // 方法
            },
            // SQL配置校验
            validSQL() {

            },
            // 子节点SQL配置校验
            validSubSQL() {

            },
            createTree(node) {
                this.reset()

                let obj = {
                    nodeId: '',
                    parentNodeId: node.id,
                    parentNodeName: node.nodeName
                }
                this.query(obj)

            },
            editTree(node) {
                this.reset()
                let obj = {}
                if (node) {
                    obj = {
                        nodeId: node.id,
                        nodeName: node.nodeName,
                        parentNodeId: node.parentNodeId,
                        parentNodeName: node.parentNodeName,
                        showActiveNode: node.enableQuery ? true : false,
                        nodeImg: node.iconDictId,
                        configChecked: node.enableUseCode ? true : false,
                        viewsAttr: node.toQueryFiled,
                        configName: node.useCodeId,
                        showChildNode: node.enableShowCount ? true : false,
                        dataSplit: node.showSep,
                        sqlConfig: node.nodeShowList,
                        configSQL: node.configSql,
                    }
                }


                this.query(obj)
            },
            delTree(node) {
                this.$confirm('确定删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        id: node.id
                    }
                    return rbCmdbService.deleteViewsStructure(params).then((res) => {
                        if (res.success) {
                            this.$message.success(res.message)
                            this.getTree()
                        } else {
                            this.$message.error(res.message)
                        }

                    }).finally(() => {

                    })
                })

            },
            addSQL() {
                this.formData.sqlConfig.push({
                    showPrefix: '',
                    showSql: ''
                })
            },
            delSQL(index) {
                this.formData.sqlConfig.splice(index, 1)
            },
            // 确定
            submit() {

                this.$emit('closeDialog', '')
            },
            // 保存
            save() {

                this.$confirm('确定保存吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.saveDatas()
                })

            },
            saveDatas() {
                let validList = []
                let rules = ['ruleForm', 'ruleForm2']
                rules.forEach((item) => {
                    this.$refs[item].validate((valid) => {
                        validList.push(valid)
                    })
                })
                // 模型名称自定义校验
                if (this.formData.moduleName) {
                    this.myRules.moduleName[0].show = false
                } else {
                    this.myRules.moduleName[0].show = true
                }
                validList.push(this.formData.moduleName ? true : false)
                let validAll = validList.every((item) => { return item })

                if (validAll) {
                    this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                    let params = {
                        id: this.formData.id,
                        viewCode: this.formData.code,
                        viewName: this.formData.name,
                        catalogId: this.formData.moduleGroup,
                        moduleId: this.formData.moduleName,
                        viewNodeList: [{
                            id: this.formData.nodeId,
                            nodeName: this.formData.nodeName,
                            // icon: '业务系统',
                            iconDictId: this.formData.nodeImg,
                            parentNodeId: this.formData.parentNodeId,
                            parentNodeName: this.formData.parentNodeName,
                            enableUseCode: this.formData.configChecked ? 1 : 0,
                            enableQuery: this.formData.showActiveNode ? 1 : 0,
                            useCodeId: this.formData.configName,
                            toQueryFiled: this.formData.viewsAttr,
                            enableShowCount: this.formData.showChildNode ? 1 : 0,
                            configSql: this.formData.configSQL,
                            showSep: this.formData.dataSplit,
                            nodeShowList: this.formData.sqlConfig,
                        }]
                    }
                    return rbCmdbService.updateViewsStructure(params).then((res) => {
                        if (res.success) {
                            this.formData.id = res.id
                            this.$message.success(res.message)
                            this.getTree()

                        } else {
                            this.$message.error(res.message)
                        }

                    }).finally(() => {
                        this.closeFullScreenLoading()
                    })
                } else {
                    this.$confirm('字段值不符合规范,请检查后再提交', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return false
                }
            },
            // 取消
            cancel() {
                this.$emit('closeDialog', 'update')
            },
            // 重置节点信息
            reset() {
                for (let key in this.initFormData) {
                    if (['code', 'name', 'moduleGroup', 'moduleName', 'id'].indexOf(key) < 0) {
                        this.formData[key] = ''
                    }
                }
            },
            // 节点信息
            query(data = {}) {

                let allKeys = Object.keys(this.formData)
                for (let key in data) {
                    if (allKeys.indexOf(key) > -1) {
                        this.formData[key] = data[key]
                    }
                }
                if (!this.formData.sqlConfig || this.formData.sqlConfig.length < 1) {
                    this.formData.sqlConfig = [{ showPrefix: '', showSql: '' }]
                }
            },
            async init() {

                this.initFormData = JSON.parse(JSON.stringify(this.formData))

                this.formData.id = ''
                if (this.dialogMsg.data.type === 'edit') {
                    this.formData.id = this.dialogMsg.data.item.id
                    await this.getTree()
                    this.getModuleName()
                    this.getConfigName()
                }
                this.getModuleGroup()
                this.getNodeImg()

            },
        },
        mounted() {
            this.init()
        }
    }

</script>
<style lang="scss" scoped>
    .tree-btn-wrap {
        margin-left: 10px;
    }
    .btn-wrap {
        position: absolute;
        bottom: 0;
    }
</style>
