<template>
    <div class="component-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="60px">
            <el-form-item label="任务名称">
                <el-input v-model="name"
                          placeholder="请输入内容"
                          class="input-tem-name1"></el-input>
            </el-form-item>
            <el-form-item label="巡检模板">
                <el-select v-model="moni_template_items_value"
                           placeholder="请选择"
                           class="list-sel"
                           clearable>
                    <el-option v-for="item in moni_template_items"
                               :key="item.template_id"
                               :label="item.name"
                               :value="item.template_id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="任务类型">
                <el-select v-model="type"
                           placeholder="请选择"
                           class="list-sel"
                           clearable>
                    <el-option v-for="item in task_type"
                               :key="item"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="运行时间">
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
                           @click="search()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">

            <!--<el-button type="primary" @click="goAdd">新增</el-button>-->
            <!--<el-button type="primary" @click="update">修改</el-button>-->
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="goAdd()">新增
                </el-button>
                <el-button type="text"
                           icon=""
                           @click="execute"
                           ref="execBtn">立即执行
                </el-button>
                <el-button type="text"
                           icon=""
                           @click="addSchedu">添加调度
                </el-button>
                <el-button type="text"
                           icon=""
                           @click="stopSchedu">停止调度
                </el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="del">删除
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          border
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          height="calc(100vh - 270px)"
                          @selection-change="handleSelectionChange"
                          :default-sort="{prop: 'create_time', order: 'descending'}">
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column prop="name"
                                     label="任务名称"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="template_names"
                                     label="巡检模板">
                    </el-table-column>
                    <el-table-column prop="type"
                                     width="80px"
                                     label="任务类型">
                    </el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="range"
                                     label="巡检范围"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column prop="exec_time"
                                     label="执行时间">
                    </el-table-column>
                    <el-table-column prop="recent_run_time"
                                     label="最近运行时间"
                                     sortable>
                    </el-table-column>
                    <el-table-column prop="sta"
                                     label="运行状态"
                                     width="80px">
                        <template slot-scope="scope">
                            <span v-if="scope.row.sta === '执行中'"
                                  style="color: #67c23a">{{scope.row.sta}}</span>
                            <span v-if="scope.row.sta === '已完成'"
                                  style="color: #67c23a">{{scope.row.sta}}</span>
                            <span v-else-if="scope.row.sta== '调度中'"
                                  style="color: #67c23a">{{scope.row.sta}}</span>
                            <span v-else-if="scope.row.sta== '未执行'"
                                  style="color: #409eff">{{scope.row.sta}}</span>
                            <span v-else-if="scope.row.sta== '未调度'"
                                  style="color: #409eff">{{scope.row.sta}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="operation"
                                     label="操作"
                                     width="120px">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="详情"
                                       icon="el-icon-view"
                                       @click="goDetails(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       @click="update(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="巡检结果"
                                       icon="el-icon-document"
                                       @click="goResult(scope.row)">
                            </el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="delSingle(scope.row)">
                            </el-button>
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
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="taskEditorShow"
                   width="1050px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <task-editor ref="taskEditor"
                             :currentTaskInfo="currentTaskInfo"></task-editor>
            </section>
            <section class="btn-wrap"
                     v-if="isEdit">
                <el-button type="primary"
                           @click="saveTask()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
        <!-- dialog -->
    </div>
</template>

<script>
    // import rbHttp from 'src/assets/js/utility/rb-http.factory.js'
    import { task_type } from './config/options.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import taskEditor from './edit/task-editor.vue'
    import _ from 'lodash'
    export default {
        data() {
            return {
                isEdit: false,
                taskEditorShow: false,
                pageLoading: false,
                dialogName: '',
                currentTaskInfo: {},
                task_type: '',
                type: '',
                name: '',
                zhou: '',
                yue: '',
                moni_template_items: '',
                moni_template_items_value: '',
                time_range: [],
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 20,
                // 每页多少行数组
                pageSizes: [10, 20, 50, 100],
                // 总共多少行数据
                total: 0,
                time: null
            }
        },
        components: {
            taskEditor
        },
        methods: {
            // 封装方法
            packTime() {
                if (this.currentTaskInfo.cycle === '月') {
                    let index = formatDate(this.currentTaskInfo.exec_time).indexOf(' ')
                    let str1 = formatDate(this.currentTaskInfo.exec_time).slice(index + 1, -3)
                    let yue = this.currentTaskInfo.yue.slice(0, -1)
                    let str = yue + '+' + str1
                    return str
                } else if (this.currentTaskInfo.cycle === '周') {
                    let index = formatDate(this.currentTaskInfo.exec_time).indexOf(' ')
                    let str1 = formatDate(this.currentTaskInfo.exec_time).slice(index + 1, -3)
                    let zhou = rbMirrorCommonService.getDaoZhou(this.currentTaskInfo.zhou)
                    let str = zhou + '+' + str1
                    return str
                } else if (this.currentTaskInfo.cycle === '日') {
                    let index = formatDate(this.currentTaskInfo.exec_time).indexOf(' ')
                    let str = formatDate(this.currentTaskInfo.exec_time).slice(index + 1, -3)
                    return str
                } else if (this.currentTaskInfo.cycle === '分钟') {
                    return this.currentTaskInfo.exec_time
                } else if (this.currentTaskInfo.cycle === '小时') {
                    return this.currentTaskInfo.exec_time
                } else if (this.currentTaskInfo.cycle === '自定义') {
                    return this.currentTaskInfo.exec_time
                }
            },
            saveTask() {
                this.$refs.taskEditor.$refs.taskForm.validate((valid) => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    } else {
                        var noObjectId = false
                        this.currentTaskInfo.object_list.forEach(item => {
                            if (item.object_ids == '') {
                                noObjectId = true
                            }
                        })
                        if (noObjectId) {
                            this.$message('请选择监控设备')
                            return
                        }
                    }

                    this.pageLoading = true
                    let req = {
                        cycle: rbMirrorCommonService.getDaoCycleType(this.currentTaskInfo.cycle),
                        // exec_time: this.currentTaskInfo.exec_time,
                        exec_time: this.packTime(),
                        name: this.currentTaskInfo.name,
                        object_list: JSON.parse(JSON.stringify(this.currentTaskInfo.object_list)),
                        receivers: this.currentTaskInfo.receivers,
                        status: this.currentTaskInfo.status,
                        type: rbMirrorCommonService.getDaoTaskType1(this.currentTaskInfo.type),
                        task_id: this.currentTaskInfo.task_id
                    }
                    if (this.currentTaskInfo.task_id == '') {
                        rbProjectDataServiceFactory.creatTask(req).then((res) => {
                            if (res.task_id) {
                                this.$message.success('创建成功')
                            }
                            this.pageLoading = false
                            this.taskEditorShow = false
                            this.search()
                        }).catch(() => {
                            this.$message.error('创建失败')
                        })
                    } else {
                        let arr = [req, req.task_id]
                        rbProjectDataServiceFactory.updateTask(arr).then((res) => {
                            if (res.task_id) {
                                this.$message({
                                    message: '修改成功',
                                    type: 'success'
                                })
                            }
                            this.pageLoading = false
                            this.taskEditorShow = false
                            this.search()
                        }).catch(() => {
                            this.$message.error('修改失败')
                        })
                    }
                    this.pageLoading = false
                })
            },
            addCancel() {
                this.taskEditorShow = false
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
            // 封装数据
            packData(arr, obj) {
                console.log(obj)
                arr.forEach((item) => {
                    // if (item.type == 1 && item.status == 'ON') {
                    //     if (this.time) {
                    //         clearTimeout(this.time)
                    //     }
                    //     this.time = setTimeout(() => {
                    //         this.getTaskList(obj)
                    //     }, 1500)
                    //     sessionStorage.setItem('taskTimeStorage', this.time)
                    // }
                    item.type = rbMirrorCommonService.getTaskType(item.type)
                    item.exec_time = rbMirrorCommonService.getListExecTime(item.cycle, item.exec_time)
                    if (item.create_time) {
                        item.create_time = formatDate(item.create_time)
                    }
                    if (item.recent_run_time) {
                        item.recent_run_time = formatDate(item.recent_run_time)
                    }
                    item.sta = item.status === 'ON' ? (item.type === '手动' ? '执行中' : '调度中') : (item.type === '手动' ? '未执行' : '未调度')
                    if (item.sta === '未执行' && item.recent_run_time) {
                        item.sta = '已完成'
                    }
                    // res.result.forEach(item => {
                    // 手动

                    // })
                })
                return arr
            },
            // 数据来源
            getTableData(obj) {
                let obj1 = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                if (obj) {
                    obj = Object.assign(obj, obj1)
                } else {
                    obj = obj1
                }
                this.getTaskList(obj)

            },
            getTaskList(obj) {
                rbProjectDataServiceFactory.getTaskList(obj).then((res) => {
                    this.tableData = this.packData(res.result, obj)
                    this.total = res.count
                    // res.result.forEach(item => {
                    //     // 手动
                    //     if (item.type == 1 && item.status == 'ON') {
                    //
                    //     }
                    // })
                })
            },
            // 模板数据来源
            templateData() {
                rbProjectDataServiceFactory.getList().then((res) => {
                    this.moni_template_items = res.result
                })
            },
            // 业务逻辑
            reset() {
                this.type = ''
                this.name = ''
                this.moni_template_items_value = ''
                this.time_range = []
            },
            // 任务的增删改查
            goAdd() {
                this.dialogName = '新建巡检任务'
                this.isEdit = true
                this.currentTaskInfo = {
                    'cycle': '',
                    'exec_time': '',
                    'name': '',
                    'object_list': [],
                    'receivers': '',
                    'status': '',
                    'type': '',
                    'zhou': '',
                    'yue': '',
                    'task_id': ''
                }
                this.taskEditorShow = true
            },
            update(row) {
                if (row.status === 'ON') {
                    this.$alert('已被调度的任务不能修改', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.pageLoading = true
                    // this.$root.$children[0].$children[0].reload()
                    // this.$store.commit('updateDetail', this.multipleSelection[0].task_id)
                    // this.$router.push('update')
                    rbProjectDataServiceFactory.getTaskDetail(row.task_id).then(res => {
                        if (res) {
                            this.dialogName = '编辑巡检任务'
                            this.isEdit = true
                            if (!res.object_list) {
                                res.object_list = []
                            }
                            res.object_list.forEach(item => {
                                item.object_ids = _.map(item.device_instance_list, 'object_id').join(',')
                                item.object_type = '3'
                            })
                            let recArray = []
                            res.receivers.split(',').forEach(rec => {
                                recArray.push(rec.substring(rec.indexOf('/') + 1))
                            })
                            res.receivers = recArray.join(',')
                            this.currentTaskInfo = {
                                'cycle': rbMirrorCommonService.getCycleType(res.cycle),
                                'exec_time': res.exec_time,
                                'name': res.name,
                                'object_list': JSON.parse(JSON.stringify(res.object_list)),
                                'receivers': res.receivers,
                                'status': res.status,
                                'type': rbMirrorCommonService.getTaskType1(res.type),
                                'task_id': res.task_id
                            }
                            this.initTime(res.exec_time, res.cycle)
                            this.taskEditorShow = true
                        }
                        this.pageLoading = false
                    }).catch(error => {
                        this.$message.error(error.data.errors[0].code)
                        this.pageLoading = false
                    })
                }
            },
            initTime(str, str1) {
                if (str1 === 'MIN' || str1 === 'HOUR' || str1 === 'DEFINE') {
                    this.currentTaskInfo.exec_time = str
                } else if (str1 === 'MON') {
                    let index = str.indexOf('+')
                    this.currentTaskInfo.yue = str.slice(0, index) + '号'
                    this.currentTaskInfo.exec_time = '2018-08-20 ' + str.slice(index + 1) + ':10'
                } else if (str1 === 'WEEK') {
                    let index = str.indexOf('+')
                    this.currentTaskInfo.zhou = rbMirrorCommonService.getZhou(str.slice(0, index))
                    this.currentTaskInfo.exec_time = '2018-08-20 ' + str.slice(index + 1) + ':10'
                } else if (str1 === 'DAY') {
                    this.currentTaskInfo.exec_time = '2018-08-20 ' + str + ':10'
                } else if (str1 === '') {
                    this.currentTaskInfo.cycle = ''
                    this.currentTaskInfo.exec_time = ''
                }
            },
            goDetails(row) {
                rbProjectDataServiceFactory.getTaskDetail(row.task_id).then(res => {
                    if (res) {
                        this.dialogName = '编辑巡检详情'
                        this.isEdit = false
                        if (!res.object_list) {
                            res.object_list = []
                        }
                        res.object_list.forEach(item => {
                            item.object_ids = _.map(item.device_instance_list, 'object_id').join(',')
                            item.object_type = '3'
                        })
                        let recArray = []
                        res.receivers.split(',').forEach(rec => {
                            recArray.push(rec.substring(rec.indexOf('/') + 1))
                        })
                        res.receivers = recArray.join(',')
                        this.currentTaskInfo = {
                            'cycle': rbMirrorCommonService.getCycleType(res.cycle),
                            'exec_time': res.exec_time,
                            'name': res.name,
                            'object_list': JSON.parse(JSON.stringify(res.object_list)),
                            'receivers': res.receivers,
                            'status': res.status,
                            'type': rbMirrorCommonService.getTaskType1(res.type),
                            'task_id': res.task_id
                        }
                        this.taskEditorShow = true
                    }
                })
            },
            addSchedu() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请先选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length >= 2) {
                    this.$alert('只能选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    if (this.multipleSelection[0].type === '手动') {
                        this.$alert('只有自动类型才能调度', '警告', {
                            confirmButtonText: '确定'
                        })
                    } else {
                        if (this.multipleSelection[0].status === 'ON') {
                            this.$alert('此任务已被调度', '警告', {
                                confirmButtonText: '确定'
                            })
                        } else if (this.multipleSelection[0].status === 'OFF') {
                            let str = this.multipleSelection[0].task_id
                            rbProjectDataServiceFactory.addScheduling(str).then((res) => {
                                if (res) {
                                    this.getTableData()
                                    this.$message({
                                        message: '调度成功',
                                        type: 'success'
                                    })
                                }
                            }).catch(() => {
                                this.getTableData()
                                this.$message.error('调度失败')
                            })
                        }
                    }
                }
            },
            stopSchedu() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请先选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length >= 2) {
                    this.$alert('只能选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    if (this.multipleSelection[0].type === '手动') {
                        this.$alert('只有自动类型才能调度', '警告', {
                            confirmButtonText: '确定'
                        })
                    } else {
                        if (this.multipleSelection[0].status === 'OFF') {
                            this.$alert('此任务未被调度', '警告', {
                                confirmButtonText: '确定'
                            })
                        } else if (this.multipleSelection[0].status === 'ON') {
                            let str = this.multipleSelection[0].task_id
                            rbProjectDataServiceFactory.stopScheduling(str).then((res) => {
                                if (res) {
                                    this.getTableData()
                                    this.$message({
                                        message: '停止调度成功',
                                        type: 'success'
                                    })
                                }
                            }).catch(() => {
                                this.getTableData()
                                this.$message.error('停止调度失败')
                            })
                        }
                    }
                }
            },
            execute() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请先选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length >= 2) {
                    this.$alert('只能选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    if (this.multipleSelection[0].type === '自动') {
                        this.$alert('只有手动类型才能立即执行', '警告', {
                            confirmButtonText: '确定'
                        })
                    } else {
                        if (this.multipleSelection[0].status === 'ON') {
                            this.$alert('此任务已被执行', '警告', {
                                confirmButtonText: '确定'
                            })
                        } else {
                            let str = this.multipleSelection[0].task_id
                            rbProjectDataServiceFactory.execute(str).then((res) => {
                                if (res) {
                                    this.getTableData()
                                    this.$message({
                                        message: '立即执行成功',
                                        type: 'success'
                                    })
                                }
                            }).catch(() => {
                                this.getTableData()
                                this.$message.error('立即执行失败')
                            })
                        }
                    }
                }
            },
            // 查看任务详情
            goResult(row) {
                // let str = this.multipleSelection[0].task_id
                // this.$store.commit('taskDetail', str)
                // this.$store.commit('taskState', true)
                this.$router.push({ path: '/auto_operation/inspection/exec', query: { task_name: row.name } })
            },
            search(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                let obj = {
                    name: this.name,
                    type: rbMirrorCommonService.getDaoTaskType(this.type),
                    exec_time_start: this.time_range ? this.time_range[0] : '',
                    exec_time_end: this.time_range ? this.time_range[1] : '',
                    template_id: this.moni_template_items_value
                }
                if (rbMirrorCommonService.getDaoTaskType(this.type) === '0') {
                    obj.type = ''
                }
                this.getTableData(obj)
            },
            delSingle(row) {
                if (row.status === 'ON') {
                    this.$alert('已被调度的任务不能删除', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.$confirm('确认删除？').then(() => {
                        rbProjectDataServiceFactory.deleteTask(row.task_id).then(() => {
                            this.getTableData()
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            })
                        }).catch(() => {
                            this.$message.error('删除失败')
                        })
                    }).catch(() => {
                    })
                }
            },
            del() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请先选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length >= 2) {
                    this.$alert('只能选择一个任务', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length === 1) {
                    if (this.multipleSelection[0].status === 'ON') {
                        if (this.multipleSelection[0].type === '自动') {
                            this.$alert('已被调度的任务不能删除', '警告', {
                                confirmButtonText: '确定'
                            })
                        } else if (this.multipleSelection[0].type === '手动') {
                            this.$alert('已被执行的任务不能删除', '警告', {
                                confirmButtonText: '确定'
                            })
                        }
                    } else {
                        this.$confirm('确认删除？').then(() => {
                            rbProjectDataServiceFactory.deleteTask(this.multipleSelection[0].task_id).then(() => {
                                this.getTableData()
                                this.$message({
                                    message: '删除成功',
                                    type: 'success'
                                })
                            }).catch(() => {
                                this.$message.error('删除失败')
                            })
                        }).catch(() => {
                        })
                    }
                }
            }
        },
        mounted() {
            this.getTableData()
            this.templateData()
            this.task_type = task_type
        }
    }
</script>

<style lang="scss" scoped>
    .component-container {
      header {
        height: 140px;
        border: 2px solid #f0f0f0;
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        .head {
          width: 100%;
          text-align: left;
          .task-name {
            display: inline-block;
            margin-left: 4.7%;
          }
          .head-type {
            margin-left: 7.8%;
            display: inline-block;
          }
          .input-tem-name1 {
            width: 150px;
          }
          .list-sel {
            width: 180px;
            display: inline-block;
          }
        }
        .dataTime {
          width: 100%;
          text-align: left;
          display: block;
          div {
            display: inline-block;
          }
          .time-range {
            height: 34px;
            line-height: 32px;
            padding: 0 15px;
          }
          .creat-time {
            margin-left: 4.7%;
          }
          .task-list-button {
            margin-left: 2%;
          }
        }
      }
      .body-container {
        //border:2px solid #dfdfdf;
        border: 2px solid #f0f0f0;
        margin-top: 10px;
        padding: 10px 5px;
        .el-button {
          height: 30px;
          width: 80px;
          padding: 8px 15px;
        }
        .block {
          margin-top: 30px;
          height: 50px;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
</style>
