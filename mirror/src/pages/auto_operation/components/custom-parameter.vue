<template>
    <div>
        <!-- <el-button type="primary"
                   @click="showDialog">
            自定义参数
        </el-button> -->
        <el-button type="primary"
                   v-if="showBtn!=='传参'"
                   @click="addListRow">
            增加
        </el-button>
        <!-- 已选中列表 -->
        <div class="yw-el-table-wrap">
            <el-table :data="selectedDataList"
                      class="yw-el-table"
                      stripe
                      tooltip-effect="dark"
                      border>
                <el-table-column prop="entity_param_code"
                                 label="参数编码">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.entity_param_code"></el-input>
                    </template>
                </el-table-column>
                <el-table-column prop="param_id"
                                 label="关联自定义参数">
                    <template slot-scope="scope">
                        <el-select v-model="scope.row.param_id"
                                   filterable
                                   @change="(val)=>{
                                    selectChanged(val,scope.row)
                                   }"
                                   placeholder="请选择">
                            <el-option v-for="item in dataList"
                                       :key="item.param_id"
                                       :label="item.param_name"
                                       :value="item.param_id">
                            </el-option>
                        </el-select>
                        <!-- slot-scope="scope" -->
                        <!-- <el-button type="primary"
                                   @click="showDialog">
                            自定义参数
                        </el-button> -->
                        <!-- <span v-if="scope.row.param_id">{{scope.row.param_id}}</span> -->
                    </template>
                </el-table-column>

                <el-table-column prop="param_value"
                                 label="参数值">
                    <template slot-scope="scope">
                        <el-input type="textarea"
                                  v-if="scope.row.auto_popup_flag==='N'"
                                  v-model="scope.row.param_value"></el-input>
                        <!-- <el-input type="textarea"
                                  v-if="scope.row.auto_popup_flag==='N'"
                                  v-model="scope.row.param_value"></el-input> -->
                    </template>
                </el-table-column>
                <el-table-column label="操作"
                                 width="60">
                    <template slot-scope="scope">
                        <div class="yw-table-operator">
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="deleteRow(scope.$index)"></el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 待选择数据列表 -->
        <!-- <el-dialog class="yw-dialog"
                   title="选择参数"
                   :visible.sync="dialogShow"
                   :append-to-body="true"
                   width="700px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main no-scroll">
                <el-table ref="dataTable"
                          :data="dataList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300"
                          v-loading="loading"
                          @select="handleSelectionChange"
                          @select-all="handleSelectionChange"
                          @selection-change="selectRow">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="param_code"
                                     label="参数编码"></el-table-column>
                    <el-table-column prop="param_name"
                                     label="参数名称"></el-table-column>
                    <el-table-column prop="param_type"
                                     label="参数类型">
                        <template slot-scope="scope">
                            <span v-if="scope.row.param_type === '1'">常量</span>
                            <span v-if="scope.row.param_type === '2'">密码变量</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToList">添加
                </el-button>
                <el-button @click="hideDialog">取消</el-button>
            </section>
        </el-dialog> -->
    </div>
</template>

<script>
    // import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    // import rbAutoOperationListChooseMixin from 'src/services/auto_operation/rb-auto-operation-list-choose-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    export default {
        name: 'CustomParamsComponent',
        components: {},
        props: ['paramCode', 'paramsType', 'showAddBtn', 'scriptParamList'],
        data() {
            return {
                dialogShow: false,
                dataList: [],
                selectedDataList: [],
                testList: [],
                showAdd: 'add',
                showBtn: '',
                showOption: false,
                saveList: []
            }
        },
        // mixins: [rbAutoOperationMixin, rbAutoOperationListChooseMixin],
        created() {
            this.search(1)
            if (this.paramsType) {
                this.showAdd = this.paramsType
            }
            if (this.showAddBtn) {
                this.showBtn = this.showAddBtn
            }

        },
        mounted() {
            this.$nextTick(() => {
                // console.log('paramCode', this.paramCode)
                // if (this.paramCode && this.paramCode.length > 0) {
                //     this.selectedDataList = []
                //     this.selectedDataList = this.paramCode
                // }
                // if (this.scriptParamList && this.scriptParamList.length > 0) {
                //     this.selectedDataList = []
                //     this.selectedDataList = this.scriptParamList
                // }
            })

        },
        methods: {
            addListRow() {
                this.selectedDataList.push({
                    entity_param_code: '', param_id: '', param_value: ''
                })
            },
            deleteRow(index) {
                this.selectedDataList.splice(index, 1)
            },
            selectChanged(val, row) {
                let obj = {}
                obj = this.dataList.find((item) => {// 这里的userRoleList就是上面遍历的数据源
                    return item.param_id === val// 筛选出匹配数据
                })
                // 原有数据
                let saveObj = {}
                saveObj = this.saveList.find((item) => {// 这里的userRoleList就是上面遍历的数据源
                    return item.param_id === val// 筛选出匹配数据
                })
                // debugger
                // obj.param_type_def.auto_popup_flag
                row.auto_popup_flag = obj.param_type_def.auto_popup_flag
                row.entity_param_code = obj.param_code
                row.param_type = obj.param_type
                if (saveObj && saveObj.param_value !== '') {
                    row.param_value = saveObj.param_value
                } else {
                    row.param_value = obj.param_default_value
                }
                this.$emit('setSelectedKey', row.param_type)
                console.log(row)
            },
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.initPage = true
                this.loading = true
                this.dataList = []
                rbAutoOperationServicesFactory
                    .queryCustomParamsList(req)
                    .then(res => {
                        this.loading = false
                        this.dataList = res
                        this.showVal()
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            showVal() {
                this.selectedDataList.forEach(item => {
                    let obj = {}
                    obj = this.dataList.find((items) => {// 这里的userRoleList就是上面遍历的数据源
                        return items.param_id === item.param_id// 筛选出匹配数据
                    })
                    item.auto_popup_flag = obj.param_type_def.auto_popup_flag
                    if (item.entity_param_code === '') {
                        item.entity_param_code = obj.param_code
                    }
                    // item.param_value = obj.param_default_value
                })
                this.selectedDataList = JSON.parse(JSON.stringify(this.selectedDataList))
            }
        },
        watch: {
            selectedDataList: {
                handler(val) {
                    this.$emit('custompParameter', val)
                },
                immediate: true,
                deep: true
            },
            paramCode: {
                handler(val) {
                    if (val) {
                        this.selectedDataList = []
                        this.selectedDataList = val
                        this.saveList = val
                        // this.selectedDataList.forEach(item => {
                        //     let obj = {}
                        //     obj = this.dataList.find((items) => {// 这里的userRoleList就是上面遍历的数据源
                        //         return item.param_id === items.param_id// 筛选出匹配数据
                        //     })
                        //     if (obj) {
                        //         item.auto_popup_flag = obj.param_type_def.auto_popup_flag
                        //         item.entity_param_code = obj.param_code
                        //     }
                        // })
                    }
                },
                immediate: true,
                deep: true
            },
            scriptParamList: {
                handler(val) {
                    if (val) {
                        this.selectedDataList = []
                        this.selectedDataList = val
                        this.saveList = val
                        this.showVal()
                    }
                },
                immediate: true,
                deep: true
            }
        }
    }
</script>
