<template>
    <div>
        <el-row :gutter="10"
                type="flex"
                align="middle"
                class="recovery-container mtop10">
            <el-col :span="9">
                <!-- 左容器 -->
                <div class="border-container ptop0">
                    <div>
                        <el-tabs v-model="activeName"
                                 @tab-click="handleTabClick">
                            <el-tab-pane label="选择作业"
                                         name="first"></el-tab-pane>
                            <!-- <el-tab-pane label="选择脚本" name="second"></el-tab-pane> -->
                        </el-tabs>
                        <el-input v-model="searchWord"
                                  placeholder="请输入搜索内容">
                            <i slot="suffix"
                               class="el-input__icon el-icon-search"
                               @click="searchList"></i>
                        </el-input>
                    </div>
                    <div class="yw-el-table-wrap mtop20">
                        <el-table :data="dataListSource"
                                  ref="sourceTable"
                                  row-key="pipeline_name"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  height="370px"
                                  @selection-change="handleSelectionChange"
                                  v-loading="loading">
                            <el-table-column type="selection"
                                             :selectable="handleSelectable"
                                             :reserve-selection="true"
                                             width="42"></el-table-column>
                            <el-table-column prop="object_name"
                                             label="对象名称"
                                             min-width="130"
                                             show-overflow-tooltip></el-table-column>
                        </el-table>
                    </div>
                    <div class="yw-page-wrap">
                        <el-pagination @size-change="handleSizeChange"
                                       @current-change="handleCurrentChange"
                                       :current-page="currentPage"
                                       :page-sizes="pageSizes"
                                       :page-size="pageSize"
                                       :total="total"
                                       small
                                       layout="total, prev, pager, next"></el-pagination>
                    </div>
                </div>
            </el-col>
            <el-col :span="1">
                <!-- 箭头按钮 -->
                <div class="arrow-right"
                     @click="transferSelected">
                    <i class="el-icon-right"></i>
                </div>
            </el-col>
            <el-col :span="14">
                <!-- 右容器 -->
                <div class="border-container mleft5">
                    <el-button type="text"
                               class="mleft5"
                               @click="deleteAllRow">删除</el-button>
                    <div class="yw-el-table-wrap mtop20">
                        <el-table :data="dataListSelected"
                                  row-key="pipeline_name"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  height="450px"
                                  @selection-change="handleRightSelectionChange"
                                  v-loading="loading">
                            <el-table-column type="selection"
                                             width="45"></el-table-column>
                            <el-table-column prop="object_type"
                                             label="对象类型"
                                             width="90"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="object_name"
                                             label="对象名称"
                                             min-width="120"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="creater"
                                             label="操作"
                                             width="60">
                                <template slot-scope="scope">
                                    <el-button type="text"
                                               icon="el-icon-delete"
                                               @click="deleteRow(scope)"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-col>
        </el-row>
        <section class="btn-wrap mtop10">
            <el-button type="primary"
                       @click="addToSelectedData">确认</el-button>
            <el-button @click="cancel">取消</el-button>
        </section>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'

    export default {
        props: ['selectedData'],
        components: {
        },
        data() {
            return {
                codeList: [],
                activeName: 'first',
                boxShow: false,

                searchWord: '',
                multipleRightSelection: [], // 右侧勾选数据
                dataList: [], // 接口获取的原始列表数据
                dataListSource: [], // 尚未有已选的原始列表数据
                dataListSelected: [], // 已选的列表数据
                // dataListFiltered: [], // 过滤掉已选数据后的原始列表数据
                inspectionList: [
                    {
                        id: 1,
                        label: 'shell'
                    },
                    {
                        id: 2,
                        label: 'bat'
                    },
                    {
                        id: 3,
                        label: 'python'
                    },
                ],
            }
        },
        watch: {
            // 过滤初始数据
            dataListSource() {
                if (this.selectedData.length) {
                    this.dataListSource.forEach((row, index) => {
                        this.selectedData.forEach(item => {
                            if (row.object_name === item.object_name) {
                                this.dataListSource.splice(index, 1)
                            }
                        })
                    })
                }
            },
            // 已选数据变化，重新过滤
            // selectedData(val) {
            //   if(this.selectedData.length) {
            //     this.dataListSource.forEach((row,index) => {
            //       this.selectedData.forEach(item => {
            //         if(row.object_name === item.object_name) {
            //           this.dataListSource.splice(index, 1)
            //         }
            //       })
            //     })
            //   }
            // }
        },
        computed: {
            // 左侧容器去除已选行
            dataListFiltered() {
                let arr = this.dataListSelected.map((item) => {
                    return item.pipeline_name
                })
                let data = this.dataList.filter(item => {
                    return arr.indexOf(item.pipeline_name) === -1
                })
                return data
            },
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        methods: {
            // 切换tab
            handleTabClick() {
                // this.search()
            },
            // 右侧复选框 已勾选数据
            handleRightSelectionChange(val) {
                this.multipleRightSelection = val
            },
            // 确定选择
            addToSelectedData() {
                this.$emit('addToSelectedData', this.dataListSelected)
                this.multipleRightSelection = this.dataListSelected = []
            },
            cancel() {
                this.$emit('cancel')
            },
            search() {
                if (this.activeName === 'first') {
                    this.queryOpsPipelineList()
                } else {
                    this.queryOpsScriptList()
                }
            },
            handleListType(list) {
                list.map(item => {
                    if (this.activeName === 'first') {
                        item.object_type = 'pipeline'
                        item.object_name = item.pipeline_name
                        item.object_status = 1
                    } else {
                        item.object_type = 'script'
                        item.object_name = item.script_name
                        item.object_status = 1
                    }
                    item.object_id = item.pipeline_id
                })
                return list
            },
            // 查询作业列表
            queryOpsPipelineList(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pipeline_name_like: this.searchName,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                this.dataListSource = this.dataList = []
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataListSource = this.dataList = this.handleListType(res.dataList)
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 查询脚本列表
            queryOpsScriptList(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    scriptNameLike: this.searchName,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                this.dataListSource = this.dataList = []
                rbAutoOperationServicesFactory
                    .queryOpsScriptList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataListSource = this.dataList = this.handleListType(res.dataList)
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 过滤
            searchList() {
                let originList
                if (!this.dataListSelected.length) {
                    originList = this.dataList
                } else {
                    originList = this.dataListFiltered
                }
                if (this.searchWord) {
                    this.dataListSource = originList.filter(item => {
                        return Object.keys(item).some((key) => {
                            return String(item[key]).toLowerCase().indexOf(this.searchWord) > -1
                        })
                    })
                } else if (!this.dataListSelected.length) {
                    this.dataListSource = this.dataList
                } else {
                    this.dataListSource = this.dataListFiltered
                }
            },
            // 传递已选数据
            transferSelected() {
                if (!this.multipleSelection.length) {
                    this.$message.warning('请先选中数据！')
                    return
                }
                this.dataListSelected = [].concat(this.dataListSelected, JSON.parse(JSON.stringify(this.multipleSelection)))
                this.$refs.sourceTable.clearSelection()
                this.searchList()
            },
            // 删除单条已选数据
            deleteRow(scope) {
                this.dataListSelected.splice(scope.$index, 1)
                this.searchList()
            },
            // 删除已选数据
            deleteAllRow() {
                this.multipleRightSelection.forEach(item => {
                    this.dataListSelected.forEach((row, index) => {
                        if (item.pipeline_name === row.pipeline_name) {
                            this.dataListSelected.splice(index, 1)
                        }
                    })
                })
                this.multipleRightSelection = []
                this.searchList()
            },
        }
    }
</script>


<style lang="scss" scoped>
    // 故障自愈
    .border-container {
        border: 1px solid $color-border;
        padding: 10px 5px;
    }
    .ptop0 {
        padding-top: 0;
    }
    .arrow-right {
        width: 30px;
        height: 30px;
        line-height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        text-align: center;
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
    .recovery-container .el-input .el-input__icon {
        line-height: 26px;
    }
</style>
