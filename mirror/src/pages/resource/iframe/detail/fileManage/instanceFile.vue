<template>
    <div v-loading="loading">
        <el-form class="yw-form">
            <el-form class="components-condition yw-form"
                     :inline="true"
                     label-width="60px"
                     v-model="queryForm">
                <el-form-item label="文件名称">
                    <el-input v-model="queryForm.fileName"></el-input>
                </el-form-item>
                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="getTableData">查询</el-button>
                    <el-button @click="resetForm">重置</el-button>
                </section>
            </el-form>
            <el-form class="yw-form"
                     style="padding:10px 0px">
                <div class="table-operate-wrap clearfix">
                    <el-button type="text"
                               icon="el-icon-plus"
                               @click="showUploadDialog">上传</el-button>
                </div>
                <div class="yw-el-table-wrap"
                     style="margin-top:10px">
                    <el-table class="yw-el-table"
                              empty-text="无数据"
                              :data="tableData"
                              height="calc(100vh - 302px)"
                              v-loading="loading"
                              stripe
                              border
                              size="mini">
                        <el-table-column prop="fileName"
                                         label="文件名称"
                                         min-width="100"></el-table-column>
                        <el-table-column prop="fileNameAlias"
                                         label="文件别名"
                                         min-width="100"></el-table-column>
                        <el-table-column label="操作"
                                         width="180">
                            <template slot-scope="scope">
                                <div class="table-operate-wrap">
                                    <el-button type="text"
                                               icon="el-icon-delete"
                                               @click="remove(scope.row.id)">删除</el-button>

                                    <el-button type="text"
                                               icon="el-icon-download"
                                               @click="download(scope.row)">下载</el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-form>
            <div class="yw-page-wrap">
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="pagination.currentPage"
                              :page-sizes="pagination.pageSizes"
                              :page-size="pagination.pageSize"
                              :total="pagination.total"></YwPagination>
            </div>
        </el-form>
        <DialogFileAdd v-if="addDialogMsg.dialogVisible"
                       :dialogMsg="addDialogMsg"
                       @refreshList="getTableData"></DialogFileAdd>

    </div>
</template>

<script>
    import rbDocManageServicesFactory from 'src/services/doc_manage/rb-doc-manage-services.factory.js'
    export default {
        components: {
            DialogFileAdd: () => import('src/pages/resource/iframe/detail/fileManage/addDialog.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                loading: false,
                instanceId: JSON.parse(this.$route.query.queryParams).instanceId,
                queryForm: {},
                tableData: [],
                addDialogMsg: {
                    dialogVisible: false,
                    data: {
                        title: '上传CI附件',
                        fileObject: this.instanceId,
                        fileType: '主机资源'
                    }
                },
                pagination: {
                    currentPage: 1, // 当前页
                    pageSize: 50, // 当前页多少条数据
                    pageSizes: [10, 20, 50, 100], // 改变每页条数
                    total: 0 // 总共多少条数据
                }
            }
        },
        mounted() {
            this.getTableData()

        },
        methods: {
            getTableData() {
                this.loading = true
                let data = {
                    fileType: '主机资源',
                    fileObject: this.instanceId,
                    fileName: this.queryForm.fileName,
                    pageNo: this.pagination.currentPage,
                    pageSize: this.pagination.pageSize
                }
                rbDocManageServicesFactory.queryFileList(data).then((res) => {
                    this.loading = false
                    this.tableData = res.data
                    this.pagination.total = res.totalSize
                }).finally(() => {
                    this.loading = false
                })
            },
            showUploadDialog() {
                this.addDialogMsg.dialogVisible = true
                this.addDialogMsg.data.fileObject = this.instanceId
            },
            refreshList() {
                this.getTableData()
            },
            remove(id) {
                this.$confirm('您确定要删除该文件吗？').then(() => {
                    this.loading = true
                    rbDocManageServicesFactory.deleteFile({ id: id }).then(res => {
                        if (res.flag === 'success') {
                            this.$message.success('删除成功')
                            this.getTableData()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                }).finally(() => {
                    this.loading = false
                })
            },
            // 下载源文件
            download(row) {
                this.loading = true
                rbDocManageServicesFactory.downloadFile(row.id).then(res => {
                    // this.$message.success("下载完成");
                    this.createDownloadFile(res, row.fileName)
                    this.loading = false
                })
            },
            createDownloadFile(res, filename) {
                var data = new Blob([res])
                let url = window.URL.createObjectURL(data)
                let element = document.createElement('a')
                element.setAttribute('href', url)
                element.setAttribute('download', filename)
                element.style.display = 'none'
                document.body.appendChild(element)
                element.click()
                document.body.removeChild(element)
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pagination.pageSize = val
                this.getTableData()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.pagination.currentPage = val
                this.getTableData()
            },
            // 重置查询
            resetForm() {
                this.queryForm = {}
            }
        }
    }
</script>

<style scoped>
</style>
