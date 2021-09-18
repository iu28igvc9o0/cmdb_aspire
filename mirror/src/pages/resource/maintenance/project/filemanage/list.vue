<template>
    <el-dialog class="yw-dialog"
               width="1000px"
               top="10vh"
               title="维保合同列表"
               @close="handleClosed"
               :visible.sync="dialogMsg.dialogVisible">
        <div class="table-operate-wrap clearfix">
            <el-button type="text"
                       icon="el-icon-plus"
                       @click="createFile">上传</el-button>
        </div>
        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      style="margin-top:10px"
                      empty-text="无数据"
                      :data="tableData"
                      height="280px"
                      v-loading="loading"
                      stripe
                      border
                      size="mini">
                <el-table-column prop="fileName"
                                 label="文件名称"
                                 show-overflow-tooltip
                                 min-width="100"></el-table-column>
                <el-table-column prop="fileNameAlias"
                                 label="文件别名"
                                 show-overflow-tooltip
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
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div>
        </div>
    </el-dialog>
</template>

<script>
    import rbDocManageServicesFactory from 'src/services/doc_manage/rb-doc-manage-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        mixins: [rbAutoOperationMixin],
        props: ['dialogMsg'],
        components: {},
        data() {
            return {
                loading: false,
                pageLoading: false,
                tableData: [],
                fileAddDialogMsg: {
                    visable: false,
                    title: '文件新增'
                }
            }
        },
        watch: {
            dialogMsg: {
                handler() {
                    // this.query()
                },
                deep: true
            }
        },
        created() {
        },
        mounted() {
            console.info(this.dialogMsg.data)
            this.search()
        },
        methods: {
            // 上传文件，弹出新增拟态框进行处理
            createFile() {
                this.$emit('setMaintenAddFile', this.dialogMsg.data)
            },
            handleClosed() {
                this.$emit('setCloseHandler', true)
            },
            // 下载源文件
            download(row) {
                this.pageLoading = true
                rbDocManageServicesFactory.downloadFile(row.id).then(res => {
                    this.$message.success('下载完成')
                    this.createDownloadFile(res, row.fileName)
                    this.pageLoading = false
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
            // 删除文件
            remove(id) {
                this.$confirm('您确定要删除该文件吗？').then(() => {
                    this.loading = true
                    rbDocManageServicesFactory.deleteFile({ id: id }).then(res => {
                        if (res.flag === 'success') {
                            this.$message.success('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                })
            },
            // 查询文件列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    fileObject: this.dialogMsg.data,
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                }
                this.loading = true
                // 参数
                rbDocManageServicesFactory.queryFileList(req).then(res => {
                    this.loading = false
                    this.total = res.totalSize
                    this.tableData = res.data
                }).catch(() => {
                    this.loading = false
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
