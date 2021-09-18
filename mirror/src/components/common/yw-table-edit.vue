<template>
    <!-- 表格(可编辑类) -->
    <div class="yw-table-card">
        <el-card class="box-card"
                 style="margin-bottom: 5px;">
            <div slot="header">
                <span style="font-weight: bold">{{tableObj.title}}</span>
                <div class="clearfix fr">
                    <el-button v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.create"
                               class="btn-icons-wrap"
                               type="text"
                               icon="el-icon-plus"
                               @click="add()">新增</el-button>
                    <el-button v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.import"
                               type="text"
                               icon="el-icon-download"
                               @click="importIn()">导入</el-button>
                </div>
            </div>
            <el-table class="yw-el-table"
                      style="width:100%;height:100%;min-height:300px;"
                      max-height="500"
                      :data="tableObj.tableDatas"
                      stripe
                      border>
                <YwTableColumn :tableTitles="tableObj.tableTitles"
                               :tableDatas="tableObj.tableDatas"
                               :columns="tableObj.columns"
                               :validErrors="tableObj.tableChange.validErrors"
                               operatorType="update"
                               @changeTable="changeTable"></YwTableColumn>
                <el-table-column label="操作"
                                 fixed="right"
                                 align="center"
                                 width="100">
                    <template slot-scope="scope">
                        <div class="yw-table-operator">
                            <!-- <el-button v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.create"
                                       type="text"
                                       title="添加"
                                       icon="el-icon-circle-plus-outline"
                                       @click="add()"></el-button> -->
                            <el-button v-if="btnAuthority.permissions ==='*' || btnAuthority.btn.delete"
                                       type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="del(scope.row,scope.$index)"></el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- dialog -->
        <YwUpload v-if="uploadData.show"
                  :uploadData="uploadData"
                  @uploadEvent="uploadEvent"></YwUpload>
        <!-- dialog -->
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import rbCmdbReportService from 'src/services/cmdb/rb-cmdb-report-service.factory.js'
    export default {
        mixins: [CommonOption],
        props: ['tableObj', 'treeParams', 'condicationCode', 'module_id', 'queryParams', 'tableHeaderCode'],
        components: {
            YwTableColumn: () => import('src/components/common/yw-table-column.vue'),
            YwUpload: () => import('src/components/common/yw-upload.vue'),
        },
        data() {
            return {
                // 上传弹窗
                uploadData: {
                    show: false,// 是否显示弹出窗
                    actionUrl: `/v1/cmdb/process/importDataToView?importType=common&moduleId=${this.module_id}`,// 上传地址
                    actionParams: '',// 上传参数
                },
                // 删除的数据集合
                deleteDatas: []
            }
        },
        methods: {
            // 表格变更(数据、字段、校验规则等)
            changeTable(obj = {}) {
                this.$emit('changeTable', obj)
            },
            // 新增
            add() {
                if (!this.treeParams) {
                    this.$confirm('请先选择左边树数据', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return
                }
                let validErrors = false
                for (let key in this.tableObj.tableChange.validErrors) {
                    if (this.tableObj.tableChange.validErrors[key][0]) {
                        validErrors = true
                        break
                    }
                }
                if (validErrors) {
                    this.$confirm('请先填写首行数据再新增', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return
                }
                let obj = this.$utils.transformListToObj(this.tableObj.tableTitles)
                // this.tableObj.tableDatas.push(obj)
                this.tableObj.tableDatas = [obj].concat(this.tableObj.tableDatas)
            },
            // 删除
            del(rowData, index) {
                this.tableObj.tableDatas.splice(index, 1)
                if (rowData.id) {
                    // 保存有id的删除数据
                    this.deleteDatas.push(rowData)
                    this.$emit('changeTable', { deleteDatas: this.deleteDatas })
                }

            },
            // 导入
            importIn() {
                if (!this.treeParams) {
                    this.$confirm('请先选择左边树数据', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return
                }
                this.uploadData.show = true
                // this.uploadData.actionParams = Object.assign({
                //     importType: 'common',
                //     moduleId: this.module_id,
                // }, this.queryParams)
            },
            // 上传事件
            uploadEvent(obj = {}) {
                switch (obj.eventType) {
                    // 关闭弹窗
                    case 'closeDialog':
                        this.uploadData.show = false
                        break
                    // 下载模板
                    case 'downLoadTemp': {
                        this.showFullScreenLoading({ text: '正在下载模板, 请稍等...' })
                        let params = {
                            'module_id': this.module_id,
                            tableHeaderCode: this.tableHeaderCode

                        }
                        rbCmdbReportService.downloadTemplate(params).then((data) => {
                            this.exportFiles({
                                data: data,
                                fileType: 'application/msword',
                                fileName: '导入模板.xls'
                            })
                            this.$message.success('下载完成!')
                        }).catch((e) => {
                            this.$message.error('下载失败!' + '请重新选择')
                        }).finally(() => {
                            this.closeFullScreenLoading()
                        })
                        break
                    }
                    // 上传结束
                    case 'uploadFinish':
                        this.$message.success('导入成功')
                        this.tableObj.tableDatas = (obj.datas || []).concat(this.tableObj.tableDatas)
                        this.uploadData.show = false
                        this.$confirm('点击保存按钮才会进库', '提示', {
                            confirmButtonText: '了解了',
                            cancelButtonText: '关闭',
                            type: 'warning'
                        })

                        break

                    default:
                        break
                }
            },

        },
        created() {

        },
        mounted() {

        },
    }
</script>

<style lang="scss" scoped>
</style>
