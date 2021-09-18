<template>
    <el-form class="yw-form is-required"
             ref="templateForm"
             :model="templateInfo"
             :rules="templateFormRules"
             label-width="70px"
             label-position="left"
             :inline="true">
        <el-form-item label="模板名称"
                      prop="name">
            <el-input v-model="templateInfo.name"
                      placeholder="请输入名称"
                      clearable
                      :disabled="!templateInfo.isEdit"></el-input>
        </el-form-item>
        <el-form-item label="模板分类"
                      prop="type">
            <el-select v-model="templateInfo.type"
                       placeholder="请选择"
                       class="list-sel add-con"
                       clearable>
                <el-option v-for="item in typeOptions"
                           :key="item.value"
                           :label="item.label"
                           :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="模板描述"
                      prop="description">
            <el-input v-model="templateInfo.description"
                      placeholder="请输入描述"
                      clearable></el-input>
        </el-form-item>
        <el-form-item style="width: 100%">
            <div class="clearfix">
                <!-- 可选择 -->
                <!--<section class="search-right">-->
                <!--<YwSearch :searchParams="searchParams" style="width:140px;"-->
                <!--@changeSearch="changeSearch"></YwSearch>-->
                <!--</section>-->
                <section class="search-right fl"
                         style="width: 37%">
                    <div class="search-right-top">
                        <div class="name">
                            <h4>请选择</h4>
                        </div>
                        <div class="selectConfig">
                            <el-tag :key="tag.group_id"
                                    style="margin-right: 5px;"
                                    v-for="tag in basicForm.groupTagids"
                                    closable
                                    :disable-transitions="false"
                                    @close="handleClose(tag)"
                                    size="small">
                                {{tag.group_name}}
                            </el-tag>
                            <el-popover placement="bottom-start"
                                        trigger="click">
                                <comtree :ref="treeName"
                                         :isCustomEvent="departmentDialogVisible"
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

                            <!-- <YwSearch :searchParams="searchParams0"
                                      style="width:130px;"
                                      @changeSearch="changeSearch0"></YwSearch> -->
                            <YwSearch :searchParams="searchParams"
                                      style="width:130px;"
                                      @changeSearch="changeSearch"></YwSearch>
                        </div>
                    </div>
                    <div class="yw-el-table-wrap">
                        <!--<div style="text-align: left">-->
                        <!--<h4>请选择巡检项</h4>-->
                        <!--</div>-->
                        <el-table class="yw-el-table"
                                  height="290"
                                  stripe
                                  :data="scriptList"
                                  @selection-change="handleSelectionChange"
                                  style="width: 100%">
                            <el-table-column type="selection"
                                             width="30">
                            </el-table-column>
                            <el-table-column label="序号"
                                             width="50"
                                             type="index"
                                             align="left">
                            </el-table-column>
                            <el-table-column label="分组"
                                             width="100"
                                             show-overflow-tooltip
                                             align="left">
                                <template slot-scope="scope">
                                    {{ scope.row.group_name }}
                                </template>
                            </el-table-column>
                            <el-table-column label="脚本名称"
                                             width="180"
                                             show-overflow-tooltip
                                             align="left">
                                <template slot-scope="scope">
                                    {{ scope.row.script_name }}
                                </template>
                            </el-table-column>
                            <el-table-column label="脚本类型"
                                             width="100"
                                             show-overflow-tooltip
                                             align="left">
                                <template slot-scope="scope">
                                    {{ scope.row.content_type_desc }}
                                </template>
                            </el-table-column>
                        </el-table>
                        <YwPagination @handleSizeChange="handleSizeChange"
                                      @handleCurrentChange="handleCurrentChange"
                                      :current-page="currentPage"
                                      :page-sizes="pageSizes"
                                      :page-size="pageSize"
                                      layout="total, prev, pager, next, jumper"
                                      :total="total"></YwPagination>
                    </div>
                </section>
                <!-- 选择按钮 -->

                <section class="search-arrow fl"
                         @click="addItems()"
                         style="width: 3%">
                    <i class="el-icon-right"></i>
                </section>

                <!-- 选择结果 -->
                <section class="search-right  search-result fl"
                         style="width: 56%">
                    <div class="search-right-top">
                        <div class="name">
                            <el-tooltip placement="top"
                                        effect="light"
                                        popper-class="thematicData-debug-help">
                                <div slot="content">注意：判断符和匹配值生成表达式为异常状态判断，表达式为真结果状态为异常，反之为正常</div>
                                <i class='el-icon-question'
                                   style="color: #93D0F7;font-size:16px;vertical-align: text-top;"></i>
                            </el-tooltip>
                            <h4>已选</h4>
                            <a class="yw-table-link"
                               @click="empty()">清空已选</a>
                        </div>
                        <div class="selectConfig">
                            <YwSearch :searchParams="searchParams2"
                                      style="width:160px;"
                                      @changeSearch="changeSearch2"></YwSearch>
                        </div>
                    </div>
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-el-table"
                                  height="290"
                                  stripe
                                  :data="selectItemList"
                                  style="width: 100%">
                            <el-table-column label="序号"
                                             type="index"
                                             align="left">
                            </el-table-column>
                            <el-table-column label="脚本名称"
                                             show-overflow-tooltip
                                             width="70"
                                             align="left">
                                <template slot-scope="scope">
                                    {{scope.row.script_name}}
                                </template>
                            </el-table-column>
                            <el-table-column label="脚本类型"
                                             show-overflow-tooltip
                                             width="70"
                                             align="left">
                                <template slot-scope="scope">
                                    {{ scope.row.content_type_desc }}
                                </template>
                            </el-table-column>
                            <el-table-column label="分类"
                                             show-overflow-tooltip
                                             width="70"
                                             align="left">
                                <template slot-scope="scope">
                                    {{ scope.row.biz_group }}
                                </template>
                            </el-table-column>
                            <el-table-column label="优先级"
                                             show-overflow-tooltip
                                             width="80"
                                             align="left">
                                <template slot-scope="scope">
                                    <el-select v-model="scope.row.item_group">
                                        <el-option v-for="item in itemGroupList"
                                                   :key="item.key"
                                                   :label="item.value"
                                                   :value="item.key">
                                        </el-option>
                                    </el-select>
                                </template>
                            </el-table-column>
                            <el-table-column label="指标名称"
                                             show-overflow-tooltip
                                             width="80"
                                             align="left">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.name"
                                              auto-complete="off"
                                              class="input2"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="判断符号"
                                             prop="expression">
                                <template slot-scope="scope">
                                    <el-select v-model="scope.row.expression"
                                               placeholder=""
                                               class="input2">
                                        <el-option v-for="item in expressionList1"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="item.value">
                                        </el-option>
                                    </el-select>
                                </template>
                            </el-table-column>
                            <el-table-column label="匹配值"
                                             prop="match">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.match"
                                              auto-complete="off"
                                              class="input2"></el-input>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作"
                                             align="left"
                                             width="70">
                                <template slot-scope="scope">
                                    <a class="yw-table-link"
                                       @click="del(scope.row,scope.$index)"><i class="el-icon-delete"></i></a>
                                    <a class="yw-table-link"
                                       @click="deliveryData(scope.row)">传参</a>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                </section>
                <!-- 确认结果 -->
            </div>
        </el-form-item>
        <el-dialog class="yw-dialog"
                   title="新增子分组"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :append-to-body="true"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form @submit.native.prevent
                         class="yw-form is-required"
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
        <el-dialog class="yw-dialog"
                   title="传参"
                   append-to-body
                   :visible.sync="parameterDialogVisible"
                   width="900px">
            <section class="yw-dialog-main">
                <el-form class="yw-form components-condition">
                    <el-form-item label="脚本参数"
                                  prop="script_param">
                        <el-input v-model="script_param"
                                  :type="isSensitivity?'password':'text'"
                                  clearable></el-input>
                        <el-checkbox v-model="isSensitivity">
                            <el-tooltip class="item"
                                        effect="dark"
                                        :content="tipContent"
                                        placement="right">
                                <i class="el-icon-question"></i>
                            </el-tooltip>
                            *敏感参数
                        </el-checkbox>
                    </el-form-item>
                    <el-form-item label="自定义参数"
                                  prop="is_public">
                        <customParams @custompParameter="custompParameter"
                                      :scriptParamList="scriptParamList"
                                      :scriptParam="script_param"
                                      :showAddBtn="showAddBtn"
                                      :showEdit="showEdit">
                        </customParams>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="parameterBtn()">确认</el-button>
                <el-button @click="parameterDialogVisible=false">返回</el-button>
            </section>

        </el-dialog>

    </el-form>

</template>

<script>
    import { typeOptions, expressionList } from '../config/options.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import _ from 'lodash'
    import comtree from 'src/pages/auto_operation/components/tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'
    import customParams from '../../../components/custom-parameter.vue'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'

    export default {
        name: 'TemplateEditor',
        mixins: [QueryObject],
        components: {
            comtree,
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            customParams
        },
        props: ['currentTemplateInfo'],
        watch: {
            script_param: {
                handler(val) {
                    if (val) {
                        let newVal = val.split(' ')
                        let num = 0
                        this.tipContent = ''
                        newVal.forEach(item => {
                            num++
                            this.tipContent += '第' + num + '个参数：' + item + ';'
                        })
                    }
                },
                deep: true,
                immediate: true

            },
            currentTemplateInfo() {
                console.log('currentTemplateInfo', this.currentTemplateInfo)
                this.templateInfo = this.currentTemplateInfo
                this.selectItemList = this.currentTemplateInfo.item_list
            },
            routerQuery: {
                handler() {
                    this.getGroupTree()
                },
                deep: true,
                immediate: true
            }

        },
        data() {
            return {
                // 传参
                parameterDialogVisible: false,
                isSensitivity: false, // 是否敏感参数
                tipContent: '',
                script_param: '',
                custompParameterList: [],
                showAddBtn: '',
                showEdit: true,
                scriptParamList: [],
                // 作业基础信息
                basicForm: {
                    pipeline_name: '',
                    label_id: '',
                    groupTagids: [],
                    groupTagid: ''
                },
                groupAddForm: {
                    parentid: '',
                    name: '',
                    descr: ''
                },
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                treeName: 'ggtree',
                departmentDialogVisible: false,
                groupTreeData: [],
                groupIdList: [],
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: this.customGroupAdd
                    }
                ],

                searchParams0: {
                    keyword: '',
                    desc: {
                        placeholder: '请选择分组名称',
                        bgcolor: ''
                    }
                },
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '请选择脚本名称',
                        bgcolor: ''
                    }
                },
                searchParams2: {
                    keyword: '',
                    desc: {
                        placeholder: '请选择脚本名称',
                        bgcolor: ''
                    }
                },
                itemGroupList: [],
                templateInfo: this.currentTemplateInfo,
                selectItemList: this.currentTemplateInfo.item_list,
                expressionList1: expressionList,
                scriptList: [],
                tempList: [],
                // scriptNameLike: '',
                multipleSelection: [],
                templateFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入模板名称!',
                            trigger: 'blur'
                        },
                        {
                            min: 4,
                            max: 100,
                            message: '长度在 4 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    type: [
                        {
                            required: true,
                            message: '请选择模板分类!',
                            trigger: 'blur'
                        }
                    ],
                    description: [
                        {
                            max: 200,
                            message: '最大200个字符!',
                            trigger: 'blur'
                        }
                    ],
                },
                typeOptions: typeOptions
            }
        },
        mounted() {
            let itemGroup = this.$store.state.homeStore.dictObj.item_group
            if (itemGroup) {
                Object.keys(itemGroup).forEach(item => {
                    this.itemGroupList.push({ 'key': item, 'value': itemGroup[item].name })
                })
            }
            this.searchScriptList()
        },
        methods: {
            // 传参
            parameterBtn() {
                let item_ext = {}
                item_ext.script_param = this.script_param
                item_ext.customize_param = JSON.stringify(this.custompParameterList)
                this.$emit('customParam', item_ext)
                this.parameterDialogVisible = false
            },
            deliveryData(row) {
                this.showAddBtn = '传参'
                this.$nextTick(() => {
                    console.log(row)
                    if (row.item_ext && row.item_ext.customize_param && row.item_ext.customize_param !== null) {
                        this.scriptParamList = []
                        this.script_param = ''
                        this.scriptParamList = JSON.parse(row.item_ext.customize_param)
                        this.script_param = row.item_ext.script_param

                    } else {
                        rbAutoOperationServicesFactory.queryOpsScriptById(row.key).then(res => {
                            this.scriptParamList = []
                            this.scriptParamList = res.ops_param_reference_list
                        })
                    }
                    this.parameterDialogVisible = true
                })
            },
            changeSearch2(val) {
                this.searchParams2.keyword = val
                this.searchFilter()
            },
            custompParameter(val) {
                this.custompParameterList = val
            },
            searchFilter() {
                if (this.tempList.length == 0) {
                    this.tempList = JSON.parse(JSON.stringify(this.selectItemList))
                }
                if (this.searchParams2.keyword != '') {
                    this.selectItemList = _.filter(this.selectItemList, (item) => {
                        return item.script_name.includes(this.searchParams2.keyword)
                    })
                } else {
                    this.selectItemList = JSON.parse(JSON.stringify(this.tempList))
                }
            },
            changeSearch(val) {
                this.searchParams.keyword = val
                this.searchScriptList()
            },
            changeSearch0(val) {
                this.searchParams0.keyword = val
                this.searchScriptList()
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.searchScriptList()

            },
            handleSelectionChange(val) {
                this.multipleSelection = val
                console.log('this.multipleSelection', this.multipleSelection)
            },
            del(row, index) {
                if (this.currentTemplateInfo.isEdit) {
                    this.selectItemList.splice(index, 1)
                } else {
                    this.$message('关联任务的模板指标不允许删除！')
                }
            },
            // 添加到已选栏中
            addItems() {
                if (this.currentTemplateInfo.isEdit) {
                    var id = this.currentTemplateInfo.template_id
                    var arr = [].concat(this.multipleSelection)
                    console.log(arr)
                    arr.forEach(item => {
                        if (!(_.map(this.selectItemList, 'key').includes(item.script_id))) {
                            let obj = {}
                            obj.template_id = id
                            obj.value_type = 'LOG'
                            obj.sys_type = 'SCRIPT'
                            obj.key = item.script_id
                            obj.name = item.script_name
                            obj.script_name = item.script_name
                            obj.content_type_desc = item.content_type_desc
                            obj.biz_group = item.group_name
                            obj.ops_param_reference_list = item.ops_param_reference_list
                            obj.item_group = '1'
                            this.selectItemList.push(obj)
                        }
                    })
                } else {
                    this.$message('关联任务的模板指标不允许添加！')
                }
                // this.templateInfo.item_list = this.selectItemList
            },
            // 清空已选
            empty() {
                if (this.currentTemplateInfo.isEdit) {
                    this.selectItemList = []
                } else {
                    this.$message('关联任务的模板指标不允许删除！')
                }
            },
            searchScriptList() {
                if (this.basicForm.groupTagids) {
                    this.groupIdList = ''
                    this.basicForm.groupTagids.forEach(item => {
                        console.log('item===', item)
                        if (this.groupIdList) {
                            this.groupIdList += ',' + item.group_id
                        } else {
                            this.groupIdList += item.group_id
                        }
                    })
                }
                let obj = {
                    scriptNameLike: this.searchParams.keyword,
                    groupNameLike: this.searchParams0.keyword,
                    group_ids: this.groupIdList,
                    label_id: 'cruisecheck',
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getScriptList(obj).then((res) => {
                    this.total = res.totalCount
                    res.dataList.forEach((item) => {
                        item.content_type_desc = rbMirrorCommonService.common('CONTENT_TYPE', '1', item.content_type)
                        if (item.group_relation_list) {
                            item.group_name = _.map(item.group_relation_list, 'group_name').join(',')
                        }
                    })
                    this.scriptList = res.dataList
                })
            },
            //
            handleClose(tag) {
                this.basicForm.groupTagids.splice(this.basicForm.groupTagids.indexOf(tag), 1)
                this.searchScriptList()
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
            routerQuery() {
                return this.$route.query
            },

            // 快速新增分组
            customGroupAdd(node, data) {
                this.departmentDialogVisible = true
                this.groupAddForm.parentid = data.group_id
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.basicForm.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.basicForm.groupTagids.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.searchScriptList()
                this.treeVisible = false
            },
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            resetGroupAddForm() {
                this.groupAddForm = {
                    parentid: '',
                    name: '',
                    descr: ''
                }
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
                        }).catch((error) => {
                            // this.showErrorTip(error)
                            this.$message.error(error)
                        })
                    } else {
                        return false
                    }
                })
            },
            onInputBlur() {
                this.searchScriptList()
            }
        }
    }
</script>

<style lang="scss" scoped>
    .choose-wrap {
        display: inline-block;
    }

    .search-arrow {
        width: 30px;
        height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        margin: 170px 15px 0 15px;
        text-align: center;
        line-height: 30px;
        cursor: pointer;
        .el-icon-right {
            font-size: 18px;
        }
        &:hover {
            border: 1px solid #46bafe;
            .el-icon-right {
                color: #46bafe;
            }
        }
    }

    .search-right {
        width: 360px;
        border: 1px solid #dcdfe6;
    }

    .search-right-top {
        .name {
            float: left;
            padding-right: 10px;
            padding-left: 10px;
            h4 {
                color: #606266cc;
                float: left;
                padding-right: 10px;
            }
        }
        .selectConfig {
            text-align: right;
            padding-right: 10px;
        }
    }

    .clearfix {
        padding-top: 10px;
    }

    /deep/ .el-input__inner {
        border: 1px solid #dcdfe6 !important;
        background-color: #fff !important;
        border-radius: 6px !important;
    }

    /deep/ .el-icon-search {
        top: 6px !important;
    }

    /deep/ .search-box-wrap {
        /*border: 1px solid #DCDFE6 !important;*/
        line-height: 26px;
    }

    .yw-el-table-wrap {
        /*border: 1px solid rgba(220, 223, 230, 1);*/
        /*border-radius: 8px;*/
        margin-top: 15px;
        height: 300px;
        padding: 2px;
    }

    .yw-search-wrap {
        display: inline-block;
    }

    .iconLeft .el-input__suffix {
        margin-right: 10px;
    }
</style>
