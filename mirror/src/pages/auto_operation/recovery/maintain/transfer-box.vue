<template>
    <el-row :gutter="10"
            type="flex"
            align="middle"
            class="recovery-container mtop10">
        <el-col :span="8">
            <!-- 左容器 -->
            <div class="border-container">
                <el-row :gutter="10"
                        type="flex"
                        align="middle"
                        class="recovery-container">
                    <el-col :span="12">
                        故障告警指标项
                    </el-col>
                    <el-col :span="12">
                        <el-input v-model="searchWord"
                                  placeholder="请输入搜索内容">
                            <i slot="suffix"
                               class="el-input__icon el-icon-search"
                               @click="searchList"></i>
                        </el-input>
                    </el-col>
                </el-row>
                <div class="yw-el-table-wrap mtop20">
                    <el-table :data="dataListSource"
                              ref="sourceTable"
                              row-key="command"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              height="200px"
                              @selection-change="handleSelectionChange"
                              v-loading="loading">
                        <el-table-column type="selection"
                                         :selectable="handleSelectable"
                                         :reserve-selection="true"
                                         width="42"></el-table-column>
                        <el-table-column type="index"
                                         label="序号"
                                         width="45"></el-table-column>
                        <el-table-column prop="command"
                                         label="告警指标"
                                         min-width="130"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="creater"
                                         label="指标来源"
                                         show-overflow-tooltip></el-table-column>
                    </el-table>
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
        <el-col :span="15">
            <!-- 右容器 -->
            <div class="border-container">
                <el-row :gutter="10"
                        type="flex"
                        align="middle"
                        class="recovery-container">
                    <el-col :span="12">
                        已选
                        <el-button type="text"
                                   class="mleft5"
                                   @click="deleteAllRow">清空已选</el-button>
                    </el-col>
                    <el-col :span="12">
                        <el-input v-model="searchWord"
                                  placeholder="请输入搜索内容">
                            <i slot="suffix"
                               class="el-input__icon el-icon-search"
                               @click="searchList"></i>
                        </el-input>
                    </el-col>
                </el-row>
                <div class="yw-el-table-wrap mtop20">
                    <el-table :data="dataListSelected"
                              row-key="command"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              height="200px"
                              v-loading="loading">
                        <el-table-column type="index"
                                         label="序号"
                                         width="45"></el-table-column>
                        <el-table-column prop="command"
                                         label="脚本名称"
                                         min-width="130"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="creater"
                                         label="指标来源"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="creater"
                                         label="巡检项名称"
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <el-select v-model="scope.row.inspection"
                                           placeholder="请选择"
                                           filterable
                                           clearable>
                                    <el-option v-for="val in inspectionList"
                                               :key="val.id"
                                               :label="val.label"
                                               :value="val.id"></el-option>
                                </el-select>
                            </template>
                        </el-table-column>
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
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        props: ['dataList'],
        components: {
        },
        data() {
            return {
                searchWord: '',
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
        },
        computed: {
            dataListFiltered() {
                let arr = this.dataListSelected.map((item) => {
                    return item.command
                })
                let data = this.dataList.filter(item => {
                    return arr.indexOf(item.command) === -1
                })
                console.log('data===', data)
                return data
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.dataListSource = this.dataList
        },
        methods: {
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
            // 删除所有已选数据
            deleteAllRow() {
                this.dataListSelected = []
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
