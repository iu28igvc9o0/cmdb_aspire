<template>
    <div class="component-container">
        <el-form class="yw-form is-required"
                 ref="taskForm"
                 :model="taskInfo"
                 :rules="taskFormRules"
                 label-width="100px"
                 :inline="true">
            <el-form-item label="任务名称"
                          prop="name"
                          style="width: 40%;">
                <el-input v-model="taskInfo.name"
                          placeholder="请输入名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="巡检结果发送"
                          prop="receivers"
                          style="width: 45%;">
                <el-input style="width:172px;"
                          v-model="taskInfo.receivers"
                          placeholder="请输入内容"
                          class="input-tem-name1"
                          readonly></el-input>
                <el-button class="sel-user"
                           type="primary"
                           @click="selUser">选择用户</el-button>
            </el-form-item>
            <el-form-item prop="type"
                          label="任务类型"
                          class="add-tit"
                          style="width: 40%;">
                <el-select v-model="taskInfo.type"
                           placeholder="请选择"
                           class="list-sel">
                    <el-option v-for="item in task_type"
                               :key="item"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="cycle"
                          label="周期类型"
                          class="add-tit"
                          style="width: 40%;">
                <el-select v-model="taskInfo.cycle"
                           placeholder="请选择"
                           class="list-sel"
                           :disabled="selected">
                    <el-option v-for="item in cycle_type"
                               :key="item"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="exec_time"
                          label="执行时间"
                          class="add-tit"
                          style="width: 100%;">
                <div v-if='!taskInfo.cycle || taskInfo.cycle == "" '>
                    <el-input placeholder="请输入内容"
                              disabled
                              prefix-icon="el-icon-time"
                              key="1"></el-input>
                </div>

                <div v-if=' taskInfo.cycle == "自定义" '
                     style="position:relative">
                    <el-input v-model="taskInfo.exec_time"
                              placeholder="请输入cron表达式"
                              @change="cronVal"
                              ref="cron"
                              suffix-icon="el-icon-warning"
                              style="width: 178px;"></el-input>
                    <span class="cron"
                          ref="cronText">请输入6位数正确的cron表达式</span>
                </div>

                <div v-if=' taskInfo.cycle == "分钟" '>
                    <el-input-number v-model="taskInfo.exec_time"
                                     controls-position="right"
                                     :min="1"
                                     :max="60"></el-input-number>
                </div>

                <div v-if=' taskInfo.cycle == "小时" '>
                    <el-input-number v-model="taskInfo.exec_time"
                                     controls-position="right"
                                     :min="1"
                                     :max="60"
                                     size="small"></el-input-number>
                </div>

                <div v-if=' taskInfo.cycle == "日" '>
                    <el-time-picker v-model="taskInfo.exec_time"
                                    placeholder="选择日期时间"
                                    class="input-creat-time data-inp1"
                                    format="HH:mm">
                    </el-time-picker>
                </div>

                <div v-if=' taskInfo.cycle == "周" '>
                    <el-select v-model="taskInfo.zhou"
                               placeholder="请选择"
                               class="list-sel"
                               style="width:100px">
                        <el-option v-for="item in zhou_type"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                    </el-select>
                    <el-time-picker v-model="taskInfo.exec_time"
                                    placeholder="选择时间"
                                    class="input-creat-time data-inp1"
                                    format="HH:mm"
                                    style="width:136px">
                    </el-time-picker>
                </div>

                <div v-if=' taskInfo.cycle == "月" '>
                    <el-select v-model="taskInfo.yue"
                               placeholder="请选择"
                               class="list-sel"
                               style="width:100px">
                        <el-option v-for="item in yue_type"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                    </el-select>
                    <el-time-picker v-model="taskInfo.exec_time"
                                    placeholder="选择时间"
                                    class="input-creat-time data-inp1"
                                    format="HH:mm"
                                    style="width:136px">
                    </el-time-picker>
                </div>
            </el-form-item>
            <el-form-item>
                <div class="clearfix">
                    <!-- 可选择 -->
                    <section class="search-right fl"
                             style="width: 380px">
                        <div class="search-right-top">
                            <div class="name">
                                <h4>请选择巡检模板</h4>
                            </div>
                            <div class="selectConfig">
                                <YwSearch :searchParams="searchParams"
                                          style="width:160px;"
                                          @changeSearch="changeSearch"></YwSearch>
                            </div>
                        </div>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      height="290"
                                      :data="templateList"
                                      @selection-change="handleSelectionChange"
                                      style="width: 100%">
                                <el-table-column type="selection"
                                                 width="30">
                                </el-table-column>
                                <el-table-column label="序号"
                                                 type="index"
                                                 align="left">
                                </el-table-column>
                                <el-table-column label="模板名称"
                                                 show-overflow-tooltip
                                                 width="80"
                                                 align="left">
                                    <template slot-scope="scope">
                                        {{ scope.row.name }}
                                    </template>
                                </el-table-column>
                                <el-table-column label="监控类型"
                                                 show-overflow-tooltip
                                                 width="80"
                                                 align="left">
                                    <template slot-scope="scope">
                                        {{ scope.row.mon_type }}
                                    </template>
                                </el-table-column>
                                <el-table-column label="巡检类型"
                                                 show-overflow-tooltip
                                                 width="80"
                                                 align="left">
                                    <template slot-scope="scope">
                                        {{ scope.row.type}}
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                        <YwPagination @handleSizeChange="handleSizeChange"
                                      @handleCurrentChange="handleCurrentChange"
                                      :current-page="currentPage"
                                      :page-sizes="pageSizes"
                                      :page-size="pageSize"
                                      layout="total, prev, pager, next, jumper"
                                      :total="total"></YwPagination>

                    </section>
                    <!-- 选择按钮 -->

                    <section class="search-arrow fl"
                             @click="addItems()">
                        <i class="el-icon-right"></i>
                    </section>

                    <!-- 选择结果 -->
                    <section class="search-right  search-result fl"
                             style="width: 420px;height: 380px;">
                        <div class="search-right-top">
                            <div class="name">
                                <h4>已选</h4>
                                <a class="yw-table-link"
                                   @click="empty()">清空已选</a>
                            </div>
                            <div class="selectConfig">
                                <YwSearch :searchParams="searchParams2"
                                          style="width:160px;"
                                          @changeSearch="changeSearch2"></YwSearch>
                                <!--<YwSearch :searchParams="searchParams" style="width:160px;"-->
                                <!--@changeSearch="changeSearch"></YwSearch>-->
                            </div>

                        </div>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      height="330"
                                      :data="selectTemplateList"
                                      style="width: 100%">
                                <el-table-column label="序号"
                                                 type="index"
                                                 align="left">
                                </el-table-column>
                                <el-table-column label="模板名称"
                                                 show-overflow-tooltip
                                                 width="80"
                                                 align="left">
                                    <template slot-scope="scope">
                                        {{scope.row.template_name}}
                                    </template>
                                </el-table-column>
                                <el-table-column label="绑定设备"
                                                 show-overflow-tooltip
                                                 width="145"
                                                 align="left">
                                    <template slot-scope="scope">
                                        {{ scope.row.object_ids }}
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作"
                                                 align="left"
                                                 width="150">
                                    <template slot-scope="scope">
                                        <a class="yw-table-link"
                                           @click="bindDevice(scope.row,scope.$index)">绑定设备</a>
                                        <a class="yw-table-link"
                                           @click="del(scope.row,scope.$index)">删除</a>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                    </section>
                    <!-- 确认结果 -->
                </div>
            </el-form-item>
        </el-form>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   append-to-body
                   :title="dialogName"
                   :visible.sync="deviceEditorShow"
                   width="950px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <device-editor ref="deviceEditor"
                               :currentTemplateObj="currentTemplateObj"></device-editor>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addDevice()">确定</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>

        <!-- cmdb类型目标主机 -->
        <selectServers @addCMDB="addCMDB"
                       @close="close"
                       from="inspectionTask"
                       :inspectionTaskCmdbBoxShow="serversCmdbBoxShow"
                       :targetMachines="[]"></selectServers>

        <DialogUser :dialogUser="dialogUser"
                    @closeDialogUser="closeDialogUser"
                    v-if="dialogUser.dialogVisible"></DialogUser>
    </div>
</template>


<script>
    import { task_type1, cycle_type, zhou_type, yue_type } from '../config/options.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { cronValidate } from 'src/assets/js/utility/cron.js'
    import _ from 'lodash'
    import deviceEditor from './device-editor.vue'
    import selectServers from '../../../components/select-servers.vue'
    export default {
        name: 'TaskEditor',
        props: ['currentTaskInfo'],
        components: {
            deviceEditor,
            selectServers,
            YwSearch: () => import('src/components/common/yw-search.vue'),
            DialogUser: () => import('src/components/common/yw-dialog-user.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        mixins: [QueryObject],
        watch: {
            'taskInfo.type'(newValue) {
                if (newValue === '自动' || newValue === '全部') {
                    this.selected = false
                } else if (newValue === '手动') {
                    this.taskInfo.cycle = ''
                    this.selected = true
                }
            },
            currentTaskInfo() {
                this.taskInfo = this.currentTaskInfo
            },
            'currentTaskInfo.object_list'(val) {
                this.selectTemplateList = val
            }
        },
        data() {
            return {
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入模板名称',
                        bgcolor: ''
                    }
                },
                searchParams2: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入模板名称',
                        bgcolor: ''
                    }
                },
                taskFormRules: {
                    name: [
                        { required: true, message: '请输入任务名称', trigger: 'blur' },
                        { min: 1, max: 33, message: '长度在 1 到 33 个字符', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '请选择任务类型', trigger: 'change' },
                        { min: 1, max: 33, message: '长度在 1 到 33 个字符', trigger: 'change' }
                    ],
                    receivers: [
                        { required: true, message: '请选择接收人', trigger: 'change' },
                        { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'change' }
                    ]
                },
                dialogName: '绑定设备',
                currentTemplateObj: {},
                dialogUser: {
                    dialogVisible: false,
                    data: [] // 数据
                },
                selected: this.currentTaskInfo.type === '手动' ? true : false,
                taskInfo: this.currentTaskInfo,
                templateList: [],
                selectTemplateList: this.currentTaskInfo.object_list,
                task_type: task_type1,
                cycle_type: cycle_type,
                zhou: '',
                yue: '',
                zhou_type: zhou_type,
                yue_type: yue_type,
                tempList: [],
                // 多选框模板存放的值
                multipleSelection: [],
                selectDeviceList: [],
                deviceEditorShow: false,
                serversCmdbBoxShow: false,
                templateNameLike: null
            }
        },
        mounted() {
            this.searchTemplateList()
        },
        methods: {
            close(type) {
                if (type) {
                    this.serversCmdbBoxShow = false
                }
            },
            addCMDB(data) {
                console.log(data)
                this.close(true)
                // this.addForm.targetMachines = JSON.parse(
                //     JSON.stringify(data)
                // )
                // this.$refs.addForm.validateField('targetMachines')
                var objectIdsList = []
                if (this.currentTemplateObj['object_ids'] != '') {
                    objectIdsList = this.currentTemplateObj['object_ids'].split(',')
                }
                data.forEach(item => {
                    if (!objectIdsList.includes(item.proxy_id + ':' + item.agent_ip)) {
                        objectIdsList.push(item.proxy_id + ':' + item.agent_ip)
                    }
                })
                this.currentTemplateObj['object_ids'] = objectIdsList.join(',')
            },
            changeSearch(val) {
                this.searchParams.keyword = val
                this.searchTemplateList()
            },
            changeSearch2(val) {
                this.searchParams2.keyword = val
                this.searchFilter()
            },
            searchFilter() {
                if (this.tempList.length == 0) {
                    this.tempList = JSON.parse(JSON.stringify(this.selectItemList))
                }
                if (this.searchParams2.keyword != '') {
                    this.selectTemplateList = _.filter(this.selectTemplateList, (item) => {
                        return item.template_name.includes(this.searchParams2.keyword)
                    })
                } else {
                    this.selectTemplateList = JSON.parse(JSON.stringify(this.tempList))
                }
            },
            addDevice() {
                this.deviceEditorShow = false
            },
            addCancel() {
                this.deviceEditorShow = false
            },
            bindDevice(row) {

                if (sessionStorage.getItem('ops_agent_loadfrom') && sessionStorage.getItem('ops_agent_loadfrom') == 'cmdb') {
                    this.serversCmdbBoxShow = true
                } else {
                    this.deviceEditorShow = true
                }
                this.currentTemplateObj = row
            },
            cronVal() {
                if (!cronValidate(this.exec_time)) {
                    let inp = this.$refs.cron.$el.getElementsByClassName('el-input__inner')[0]
                    let span = this.$refs.cron.$el.getElementsByClassName('el-input__suffix')[0]
                    inp.style.border = '1px solid #f56c6c'
                    span.style.display = 'inline-block'
                    this.$refs.cronText.style.display = 'block'
                } else {
                    let inp = this.$refs.cron.$el.getElementsByClassName('el-input__inner')[0]
                    let span = this.$refs.cron.$el.getElementsByClassName('el-input__suffix')[0]
                    inp.style.border = '1px solid #dcdfe6'
                    span.style.display = 'none'
                    this.$refs.cronText.style.display = 'none'
                }
            },
            closeDialogUser(date) {
                this.dialogUser.dialogVisible = false
                if (date && date.type === 'update') {
                    this.dialogUser.data = _.map(date.userResult, function (user) {
                        return user.indexOf('/') !== -1 ? user.substring(user.indexOf('/') + 1) : user
                    })
                    this.taskInfo.receivers = this.dialogUser.data.join(',')
                    // this.formData.user = Array.from(new Set([this.formData.user, date.userResult]))
                }
            },
            // goUser(userList) {
            //     this.dialogVisible = false
            //     let str = ''
            //     userList.forEach((res) => {
            //         str += res.username + ','
            //     })
            //     str = str.slice(0, -1)
            //     this.taskInfo.receivers = str
            // },
            selUser() {
                this.dialogUser.dialogVisible = true
            },
            // cancelUser() {
            //     this.dialogVisible = false
            // },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            del(row, index) {
                this.selectTemplateList.splice(index, 1)
            },
            // 添加到已选栏中
            addItems() {
                // var id = this.currentTaskInfo.task_id
                var arr = [].concat(this.multipleSelection)
                arr.forEach(item => {
                    if (!(_.map(this.selectTemplateList, 'template_id').includes(item.template_id))) {
                        let obj = {}
                        obj.template_id = item.template_id
                        obj.template_name = item.name
                        obj.object_type = '3'
                        obj.object_ids = ''
                        this.selectTemplateList.push(obj)
                    }
                })
                // this.templateInfo.item_list = this.selectItemList
            },
            // 清空已选
            empty() {
                this.selectTemplateList = []
            },
            searchTemplateList() {
                let obj = {
                    name: this.searchParams.keyword,
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    fun_type: '2'
                }
                rbProjectDataServiceFactory.getList(obj).then((res) => {
                    this.templateList = this.packData(res.result)
                    this.total = res.count
                })
            },
            packData(arr) {
                arr.forEach((item) => {
                    item.type = rbMirrorCommonService.getType(item.type)
                    item.mon_type = rbMirrorCommonService.common('MONTYPE', '1', item.mon_type)
                })
                return arr
            }
        }

    }
</script>

<style lang="scss" scoped>
    .choose-wrap {
      display: inline-block;
    }

    /deep/ .el-input-number__increase {
      right: 1px !important;
      line-height: 9.5px !important;
    }
    /deep/ .el-input-number__decrease {
      line-height: 9.5px !important;
    }
    /*.search-right {*/
    /*width: 360px;*/
    /*&.search-result {*/
    /*// margin-top: 30px;*/
    /*}*/
    /*}*/

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
</style>