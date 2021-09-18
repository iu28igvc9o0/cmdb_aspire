<template>
    <div class="components-container yw-dashboard">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="70px">
            <el-form-item label="任务名称">
                <el-input v-model="name"
                          placeholder="请输入内容"></el-input>
            </el-form-item>
            <el-form-item label="开始时间">
                <el-date-picker v-model="time_range"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <!-- <el-button type="text"
                   icon="el-icon-plus"
                   @click="create">新增</el-button>
        <el-button type="text"
                   icon="el-icon-delete"
                   @click="deleteFilterBatch">清除</el-button> -->
            </div>
            <div class="yw-el-table-wrap">

                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 275px)"
                          @selection-change="handleSelectionChange"
                          :default-sort="{prop: 'create_time', order: 'descending'}">
                    <el-table-column prop="name"
                                     label="报表名称"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="task_type"
                                     label="报表类型"
                                     width="100px">
                    </el-table-column>
                    <el-table-column prop="create_time"
                                     label="巡检时间"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="result"
                                     label="巡检结果"
                                     width="500px"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <div v-html="scope.row.result"></div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     width="80px">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="详情"
                                       icon="el-icon-view"
                                       @click="observes(scope.row)"></el-button>
                            <el-button type="text"
                                       title="重新生成"
                                       icon="el-icon-refresh-right"
                                       @click="regenerate(scope.row)"></el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="手动报告"
                                     width="80px">
                        <template slot-scope="scope">
                            <el-upload class="upload-demo"
                                       :show-file-list="false"
                                       :file-list="fileList"
                                       :http-request="uploadReport"
                                       :data="scope.row"
                                       :on-remove="handleRemove">
                                <el-button type="text"
                                           title="上传"
                                           icon="el-icon-upload"></el-button>
                            </el-upload>
                            <el-button type="text"
                                       title="下载"
                                       icon="el-icon-download"
                                       v-show="scope.row.report_file_path"
                                       @click="downloadReport(scope.row)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="total">
                </el-pagination>
            </div>
        </el-form>

        <!-- dialog -->
        <!-- dialog -->
    </div>
</template>

<script>
    // import rbHttp from '../../../../assets/js/utility/rb-http.factory.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    export default {
        data() {
            return {
                fileList: [],
                name: '',
                time_range: [],
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 10,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                // 总共多少行数据
                total: 0
            }
        },
        computed: {
            isReadonly(row) {
                return row.report_file_path !== undefined
            }
        },
        methods: {
            regenerate(row) {
                let reportId = row.report_id
                rbProjectDataServiceFactory.regenerate(reportId).then(() => {
                    this.$message('重新生成完成')
                    this.search()
                })
            },
            downloadReport(row) {
                rbProjectDataServiceFactory.downloadReport({ file_path: row.report_file_path }).then(res => {
                    this.$message('开始下载')
                    let pathArr = row.report_file_path.split('/')
                    let filename = pathArr[pathArr.length - 1]
                    filename = row.name + filename.substring('.')
                    this.createDownloadFile(res, filename)
                })
            },
            createDownloadFile(res, filename) {
                // let element = document.createElement('a')
                // element.setAttribute('href', 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(res))
                // element.setAttribute('download', filename)
                // element.style.display = 'none'
                // document.body.appendChild(element)
                // element.click()
                // document.body.removeChild(element)
                let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                let objectUrl = URL.createObjectURL(blob)
                // window.location.href = objectUrl
                let downLoadElement = document.createElement('a')
                downLoadElement.href = objectUrl
                downLoadElement.download = filename
                document.body.appendChild(downLoadElement)
                downLoadElement.click()
                document.body.removeChild(downLoadElement)
                URL.revokeObjectURL(objectUrl)
            },
            uploadReport(param) {
                // let reportId = row.report_id
                const formData = new FormData()
                formData.append('file', param.file)
                formData.append('report_id', param.data.report_id)
                rbProjectDataServiceFactory
                    .uploadReport(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message('上传成功')
                            this.search()
                        }
                    })
            },
            // 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.search(1)
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search(1)
            },
            // 业务逻辑
            search(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                let obj = {
                    inspection_time_start: this.time_range ? formatDate(this.time_range[0]) : '',
                    inspection_time_end: this.time_range ? formatDate(this.time_range[1]) : '',
                    name: this.name,
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getTaskReport(obj).then((res) => {
                    this.tableData = this.packData(res.result)
                    this.total = res.count
                })
            },
            reset() {
                this.name = ''
                this.time_range = []
            },
            observes(obj) {
                this.$store.commit('reportDetail', obj.report_id)
                this.$router.push('report/reportDetails')
            },
            // 封装数据
            packData(arr) {
                arr.forEach((item) => {
                    item.create_time = formatDate(item.create_time)
                    item.task_type = rbMirrorCommonService.getTaskType(item.task_type)
                })
                return arr
            },
            // 数据来源
            getTableData() {
                let obj = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getTaskReport(obj).then((res) => {
                    this.tableData = this.packData(res.result)
                    this.total = res.count
                })
            }
        },
        mounted() {
            this.getTableData()
        }
    }
</script>

<style lang="scss" scoped>
    .upload-demo {
        display: inline-block;
    }
</style>
