<template>
    <el-dialog :title="dialogName + '对象归属'"
               :visible.sync="visible"
               width="1000px"
               class="yw-dialog mod-dept-manage__dialog"
               :before-close="handleModDeptDialogClose">
        <section class="yw-dialog-main">
            <el-row :gutter="30">
                <el-col :span="14"
                        class="border_col">
                    <el-select style="width:200px;"
                               v-model="valueObject"
                               placeholder="请选择对象类型">
                        <el-option v-for="item in objectTypeOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                    <el-input class="yw-search-box"
                              v-model="searchText"
                              style="width: 36%"
                              placeholder="请输入对象名称">
                        <i slot="suffix"
                           class="el-input__icon el-icon-search"
                           @click="searchData"></i>
                    </el-input>
                    <section>
                        <el-table :data="personList"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  height="500px"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection"
                                             width="55">
                            </el-table-column>
                            <el-table-column prop="object_type_name"
                                             show-overflow-tooltip
                                             label="对象类型">
                            </el-table-column>
                            <el-table-column prop="object_name"
                                             show-overflow-tooltip
                                             label="对象名称">
                            </el-table-column>
                            <el-table-column prop="creater"
                                             show-overflow-tooltip
                                             label="创建人">
                            </el-table-column>
                            <el-table-column show-overflow-tooltip
                                             prop="create_time"
                                             label="创建时间">
                            </el-table-column>
                            <el-table-column prop="updater"
                                             show-overflow-tooltip
                                             label="最新修改人">
                            </el-table-column>
                            <el-table-column show-overflow-tooltip
                                             prop="update_time"
                                             label="最新修改时间">
                            </el-table-column>
                        </el-table>
                        <div class="yw-page-wrap">
                            <el-pagination @size-change="handleSizeChange"
                                           @current-change="handleCurrentChange"
                                           :current-page="currentPage"
                                           :page-size="pagesize"
                                           :page-sizes="[5, 10]"
                                           layout="total, sizes, prev, pager, next, jumper"
                                           :total="total">
                            </el-pagination>
                        </div>
                    </section>

                </el-col>
                <el-col :span="2"
                        style="height: 400px;">
                    <el-button @click="handlePersonList"
                               icon="el-icon-arrow-right"
                               size="small"
                               type="primary"
                               plain
                               style="position: absolute;left: 60%;top: 50%;"></el-button>
                </el-col>
                <el-col :span="8"
                        class="border_col">
                    <div class="choose-wrap">
                        当前分组名称：{{ groupData.group_name }}
                    </div>
                    <div v-if="dialogName == '修改'"
                         class="choose-wrap">
                        移除到目标分组：
                        <el-popover placement="bottom-start"
                                    trigger="click">
                            <comtree :data="departmentData"
                                     :props="departmentTreeDefault"
                                     :ex-control="true"
                                     node-key="group_id"
                                     :default-expand-all="true"
                                     :ex-control-opt="[]"
                                     @node-click="handleTreeClick">
                            </comtree>
                            <el-button slot="reference"
                                       type="primary">{{departmentText}}</el-button>
                        </el-popover>
                    </div>
                    <el-table :data="selectPersonList"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              height="500px">
                        <el-table-column prop="object_type_name"
                                         show-overflow-tooltip
                                         label="对象类型">
                        </el-table-column>
                        <el-table-column prop="object_name"
                                         show-overflow-tooltip
                                         label="对象名称">
                        </el-table-column>
                        <el-table-column prop="creater"
                                         show-overflow-tooltip
                                         label="创建人">
                        </el-table-column>
                        <el-table-column label="操作"
                                         width="50">
                            <template slot-scope="scope">
                                <div class="yw-table-operator">
                                    <el-button type="text"
                                               icon="el-icon-delete"
                                               @click="clearSelected(scope.row)">
                                    </el-button>
                                </div>

                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="selected">确定</el-button>
            <el-button @click="cancel">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import comtree from './tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'

    import _ from 'lodash'
    export default {
        components: {
            comtree
        },
        data() {
            return {
                objectTypeOptions: [
                    {
                        value: '',
                        label: '全部'
                    }, {
                        value: 'pipeline',
                        label: '作业'
                    }, {
                        value: 'script',
                        label: '脚本'
                    }, {
                        value: 'yum',
                        label: 'yum文件'
                    }, {
                        value: 'scenes',
                        label: '场景'
                    }, {
                        value: 'ap_scheme',
                        label: '自愈规则'
                    }, {
                        value: 'file',
                        label: '文件'
                    }],
                valueObject: '',
                departmentData: [],
                departmentTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                searchText: '',
                departmentDic: {},
                departmentText: '请选择分组',
                personList: [],
                selectPersonList: [],
                currentPage: 1,
                total: 1,
                pagesize: 10,
                multipleSelection: []
            }
        },
        props: {
            visible: {
                type: Boolean,
                default: false,
                required: true
            },
            dialogName: {
                type: String,
                default: null
            },
            groupData: {
                type: Object,
                default: null
            },
            groupListData: {
                type: Array,
                default: null
            }
        },
        watch: {
            visible(val) {
                // 隐藏时重置
                if (val) {
                    this.getDepartmentTree()
                    this.getPersonList('', this.pagesize, this.currentPage, this.searchText, this.valueObject)
                    this.selectPersonList = []
                    this.departmentData = []
                    this.pagesize = 10
                    this.currentPage = 1
                    this.searchText = ''
                    this.valueObject = ''
                }
            },
            searchText() {
                this.currentPage = 1
                this.getPersonList('', this.pagesize, this.currentPage, this.searchText, this.valueObject)
            },
            valueObject() {
                this.currentPage = 1
                this.getPersonList('', this.pagesize, this.currentPage, this.searchText, this.valueObject)
            }
        },
        methods: {
            searchData() {
                this.currentPage = 1
                this.getPersonList('', this.pagesize, this.currentPage, this.searchText, this.valueObject)
            },
            handleTreeClick(data) {
                this.departmentText = data.group_name
                this.departmentDic = data
            },
            // 获取资源
            getDepartmentTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.departmentData = res
                })
            },
            selected() {
                if (this.selectPersonList.length <= 0) {
                    this.$confirm('请先从左边选择一些对象到右边！', '提示', {
                        showConfirmButton: false,
                        type: 'warning'
                    })
                    return
                }
                const deleteRelationIds = []
                const selectPersonListIds = []
                if (this.dialogName == '修改') {
                    if (!this.departmentDic.group_id) {
                        this.$confirm('请先选择分组名称！', '提示', {
                            showConfirmButton: false,
                            type: 'warning'
                        })
                        return
                    }
                    this.selectPersonList.forEach(item => {
                        deleteRelationIds.push(item.group_relation_id)
                    })
                    this.selectPersonList.forEach(item => {
                        selectPersonListIds.push({ 'object_id': item.object_id, 'object_type': item.object_type, 'group_id': this.departmentDic.group_id })
                    })
                } else {
                    this.selectPersonList.forEach(item => {
                        selectPersonListIds.push({ 'object_id': item.object_id, 'object_type': item.object_type, 'group_id': this.groupData.group_id })
                    })
                }

                // alert(0)
                console.log(deleteRelationIds, selectPersonListIds, 0)
                const params = {
                    deleteRelationIds: deleteRelationIds,
                    groupRelationList: selectPersonListIds
                }
                groupDataService.saveBatchGroupRelation(params).then(() => {
                    let message = this.dialogName == '修改' ? '批量修改对象归属成功' : '批量新增对象归属成功'
                    this.$message({
                        message: message,
                        type: 'success'
                    })
                    this.selectPersonList = []
                    this.$emit('add-done')
                }).catch(() => {
                    this.$message({
                        message: '操作失败',
                        type: 'error'
                    })
                })
            },
            clearSelected(row) {
                this.selectPersonList = this.selectPersonList.del(row)
                // this.selectPersonList.splice(index, 1)
            },
            handlePersonList() {
                this.multipleSelection.forEach((item) => {
                    let userIdsList = _.map(this.selectPersonList, 'object_id')
                    if (!userIdsList.includes(item.object_id)) {
                        this.selectPersonList.push(item)
                    }
                })
            },
            handleCurrentChange(val) {
                // console.log(this.pagesize)
                if (this.searchText || this.valueObject) {
                    this.getPersonList('', this.pagesize, val, this.searchText, this.valueObject)
                } else {
                    this.getPersonList('', this.pagesize, val)
                }
            },
            handleSizeChange(val) {
                this.pagesize = val
                this.currentPage = 1
                // console.log(this.pagesize)
                this.getPersonList('', this.pagesize, this.currentPage, this.searchText, this.valueObject)
            },
            // queryObjectList
            getPersonList(id, page, val, search, selectType) {
                if (this.dialogName == '修改') {
                    groupDataService.querGroupRelationList(this.groupData.group_id, page, val, search, '', selectType).then((res) => {
                        if (res) {
                            this.currentPage = res.curPage
                            this.total = res.totalCount
                            this.personList = res.dataList
                            this.personList.forEach((item) => {
                                switch (item.object_type) {
                                    case 'pipeline':
                                        item.object_type_name = '作业'
                                        break
                                    case 'script':
                                        item.object_type_name = '脚本'
                                        break
                                    case 'yum':
                                        item.object_type_name = 'yum文件'
                                        break
                                    case 'scenes':
                                        item.object_type_name = '场景'
                                        break
                                    case 'ap_scheme':
                                        item.object_type_name = '自愈规则'
                                        break
                                    case 'file':
                                        item.object_type_name = '文件'
                                        break
                                }
                            })
                        }
                    })
                } else {
                    groupDataService.queryObjectList(id, page, val, search, selectType).then((res) => {
                        if (res) {
                            this.currentPage = res.curPage
                            this.total = res.totalCount
                            this.personList = res.dataList
                            this.personList.forEach((item) => {
                                switch (item.object_type) {
                                    case 'pipeline':
                                        item.object_type_name = '作业'
                                        break
                                    case 'script':
                                        item.object_type_name = '脚本'
                                        break
                                    case 'yum':
                                        item.object_type_name = 'yum文件'
                                        break
                                    case 'scenes':
                                        item.object_type_name = '场景'
                                        break
                                    case 'ap_scheme':
                                        item.object_type_name = '自愈规则'
                                        break
                                    case 'file':
                                        item.object_type_name = '文件'
                                        break
                                }
                            })
                        }
                    })
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleModDeptDialogClose() {
                this.selectPersonList = []
                this.$emit('dialog-close')
            },
            cancel() {
                this.selectPersonList = []
                this.$emit('dialog-close')
            }
        },
        mounted() {
            this.getDepartmentTree()
            // this.getPersonList('', this.pagesize, this.currentPage, this.searchText)
        }
    }
</script>

<style lang="scss" scoped>
    .choose-wrap {
        margin-bottom: 10px;
        font-size: $font-size;
    }
</style>
