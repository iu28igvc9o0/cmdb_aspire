<!--  -->
<template>
    <div>
        <el-dialog class="yw-dialog"
                   title="修改通知订阅规则"
                   width="1200px"
                   :visible.sync="addDialog.dialogVisible"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="addForm"
                         :rules="addFormRules"
                         ref="addForm"
                         label-width="100px">
                    <el-form-item label="订阅规则"
                                  prop="subscribeRules">
                        <el-input placeholder="请输入订阅规则名称"
                                  :disabled="showWrite"
                                  v-model="addForm.subscribeRules"
                                  style="width:526px"></el-input>
                    </el-form-item>
                    <el-form-item label="维护用户"
                                  prop="defensetor">
                        <el-checkbox v-model="currentUserFlag"
                                     :disabled="showWrite"
                                     @change="selectCurrent()">当前用户</el-checkbox>
                        <el-checkbox label="其他用户"
                                     :disabled="showWrite"
                                     v-model="otherUserFlag"
                                     @change="selectOther()"></el-checkbox>
                        <el-input v-model="addForm.defensetor.join(',')"
                                  placeholder=""
                                  :disabled="true"></el-input>
                        <el-button type="primary"
                                   :disabled="!otherUserFlag"
                                   @click="getUser()">选择
                        </el-button>

                    </el-form-item>
                    <el-form-item label="触发类型"
                                  prop="notifyAlertType">
                        <el-radio v-model="addForm.notifyAlertType"
                                  :disabled="showWrite"
                                  label="0">告警生成</el-radio>
                        <el-radio v-model="addForm.notifyAlertType"
                                  :disabled="showWrite"
                                  label="1">告警清除/解散</el-radio>

                    </el-form-item>
                    <el-form-item label="通知对象">
                        <div class="transfer-wrap">
                            <section class="transfer-left">
                                <div class="tree-wrap">
                                    <div class="search-box-wrap">
                                        <el-input placeholder="请输入查询部门"
                                                  class="search-box"
                                                  v-model="filterText"></el-input>
                                    </div>
                                    <div class="yw-el-tree"
                                         style="max-height: 240px;">
                                        <el-tree ref="departmentTree"
                                                 :data="data"
                                                 :filter-node-method="filterNode"
                                                 node-key="uuid"
                                                 :props="defaultProps"
                                                 :default-checked-keys="defaultCheckedKeys"
                                                 :lazy="true"
                                                 :load="loadHandler"
                                                 :default-expanded-keys="[0]"
                                                 @node-click="handleNodeClick">
                                        </el-tree>

                                    </div>
                                </div>
                                <div class="table-wrap">
                                    <div class="search-box-wrap">
                                        <el-input placeholder="请输入姓名、手机号、邮箱查询"
                                                  v-model="searchText"></el-input>
                                        <i class="el-icon-search"
                                           @click="searchPersionList"></i>
                                    </div>
                                    <div class="yw-el-table-wrap">

                                        <el-table class="yw-el-table"
                                                  style="cursor:pointer;width:340px"
                                                  :data="userData"
                                                  @selection-change="handleSelectionChange"
                                                  stripe
                                                  tooltip-effect="dark"
                                                  border
                                                  height="calc(100vh - 370px)"
                                                  size="samll"
                                                  v-loading="loading">
                                            <el-table-column type="selection"
                                                             width="40px"></el-table-column>
                                            <el-table-column prop="name"
                                                             width="90px"
                                                             label="姓名"></el-table-column>
                                            <el-table-column prop="mobile"
                                                             label="手机号"></el-table-column>
                                            <el-table-column prop="mail"
                                                             label="邮箱">
                                                <template slot-scope="scope">
                                                    <p class="table-word">{{ scope.row.mail }}</p>
                                                </template>
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                </div>
                            </section>

                            <section class="search-arrow fl"
                                     @click="addItems()"
                                     style="width: 3%">
                                <i class="el-icon-right"></i>
                            </section>

                            <section class="transfer-right">
                                <div class="transfer-right-top">
                                    <span>已选({{selectItemList.length}})</span>
                                    <!-- <div class="search-box-wrap">
                                        <el-input placeholder="请输入姓名、手机号、邮箱查询" v-model="searchWord"></el-input>
                                        <i class="el-icon-search"></i>
                                    </div> -->
                                </div>
                                <div class="right-table-box">
                                    <el-table class="yw-el-table"
                                              style="cursor:pointer"
                                              :data="selectItemList"
                                              tooltip-effect="dark"
                                              border
                                              height="calc(100vh - 370px)"
                                              size="samll"
                                              v-loading="loading">
                                        <el-table-column prop="notifyObjInfo"
                                                         width="90px"
                                                         label="姓名"></el-table-column>
                                        <el-table-column prop="telephone"
                                                         label="手机号"></el-table-column>
                                        <el-table-column prop="email"
                                                         label="邮箱">
                                            <template slot-scope="scope">
                                                <p class="table-word">{{ scope.row.email }}</p>
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="操作"
                                                         width="50px">
                                            <template slot-scope="scope">
                                                <a class="yw-table-link"
                                                   @click="del(scope.row,scope.$index)">删除</a>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </div>
                            </section>
                        </div>

                    </el-form-item>
                    <el-form-item label="通知方式"
                                  prop="notifyType">
                        <el-checkbox label="短信"
                                     :disabled="showWrite"
                                     v-model="checkMsg"
                                     :true-label="2"></el-checkbox>
                        <el-checkbox label="邮件"
                                     :disabled="showWrite"
                                     v-model="checkMail"
                                     :true-label="1"></el-checkbox>
                    </el-form-item>
                    <el-form-item label="通知模板">
                        <div class="draggable-wrap">
                            <div class="draggable-col-left">
                                <h4>通知内容项</h4>
                                <draggable class="dragArea list-group"
                                           :list="list1"
                                           :group="{ name: 'people', pull: 'clone', put: false }"
                                           @change="log">
                                    <div class="list-group-item"
                                         v-for="element in list1"
                                         :key="element.name">
                                        {{ element.name.split(':')[0] }}
                                    </div>
                                </draggable>
                            </div>

                            <div class="draggable-col-right">
                                <el-tabs v-model="activeName"
                                         type="card"
                                         @tab-click="handleClick">
                                    <el-tab-pane label="短信模板"
                                                 :disabled="subDraggable.msgAble"
                                                 name="first">
                                        <div class="tabs-wrap">
                                            <draggable class="dragArea list-group"
                                                       :list="list2"
                                                       group="people"
                                                       @change="log">
                                                <p>模板</p>
                                                <div class="list-group-item"
                                                     style="display:none"
                                                     v-for="element in list2"
                                                     :key="element.name">
                                                    【{{ element.name }}:】
                                                </div>
                                                <el-input type="textarea"
                                                          :rows="8"
                                                          placeholder="请输入内容"
                                                          v-model="notifyParmas.emails">
                                                </el-input>

                                            </draggable>
                                            <div class="list-group">
                                                <el-button type="text"
                                                           @click="goNotify()">预览
                                                </el-button>

                                                <p>{{readData.smsContent}}</p>
                                            </div>
                                        </div>
                                    </el-tab-pane>
                                    <el-tab-pane label="邮箱模板"
                                                 :disabled="subDraggable.mailAble"
                                                 name="second">
                                        <div class="tabs-wrap">
                                            <draggable class="dragArea list-group"
                                                       :list="list2"
                                                       group="people"
                                                       @change="log">
                                                <p>模板</p>
                                                <div class="mail-item">
                                                    <p>标题</p>
                                                    <div class="item-box">
                                                        <div class="list-group-item"
                                                             style="display:none"
                                                             v-for="element in list2"
                                                             :key="element.name">
                                                            【{{ element.name }}】
                                                        </div>
                                                        <el-input type="textarea"
                                                                  :rows="1"
                                                                  placeholder="请输入内容"
                                                                  v-model="notifyParmas.subject">
                                                        </el-input>

                                                    </div>
                                                </div>
                                                <el-divider></el-divider>
                                                <div class="mail-item">
                                                    <p>内容</p>
                                                    <div class="item-box">
                                                        <div class="list-group-item"
                                                             style="display:none"
                                                             v-for="element in list2"
                                                             :key="element.name">
                                                            【{{ element.name }}】
                                                        </div>
                                                        <el-input type="textarea"
                                                                  :rows="6"
                                                                  placeholder="请输入内容"
                                                                  v-model="notifyParmas.emails"
                                                                  style="margin-top:4px">
                                                        </el-input>

                                                    </div>
                                                </div>
                                            </draggable>
                                            <div class="list-group">
                                                <el-button type="text"
                                                           @click="goNotify()">预览
                                                </el-button>

                                                <div class="mail-item">
                                                    <p style="width:100%">标题 {{readData.subject}}</p>
                                                </div>
                                                <el-divider></el-divider>
                                                <div class="mail-item">
                                                    <p>内容</p>
                                                    <p>{{readData.emails}}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </el-tab-pane>
                                </el-tabs>
                            </div>
                        </div>

                    </el-form-item>
                    <template v-if="this.checkMail===1">
                        <el-form-item label="邮件发送方式">
                            <el-radio v-model="addForm.emailType"
                                      :label="1">合并</el-radio>
                            <el-radio v-model="addForm.emailType"
                                      :label="2">单条</el-radio>
                        </el-form-item>
                    </template>
                    <el-form-item label="是否重发">
                        <el-switch v-model="addForm.isRecurrenceInterval"
                                   :disabled="showWrite"
                                   :active-value="1"
                                   :inactive-value="0"
                                   active-text="是"
                                   inactive-text="否">
                        </el-switch>
                    </el-form-item>
                    <div v-show="repeatShow">
                        <el-form-item label="重发间隔">
                            <el-input placeholder="请输入重发间隔"
                                      :disabled="showWrite"
                                      v-model="addForm.recurrenceInterval"></el-input>
                            <el-select v-model="addForm.recurrenceIntervalUtil"
                                       :disabled="showWrite"
                                       style="width:86px"
                                       placeholder="请选择">
                                <el-option value="m"
                                           label="分"></el-option>
                                <el-option value="h"
                                           label="时"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="重发次数">
                            <el-radio v-model="repass"
                                      :disabled="showWrite"
                                      label="-1">未解除，持续发送</el-radio>
                            <el-radio v-model="repass"
                                      :disabled="showWrite"
                                      label="1">
                                自定义次数
                                <el-input-number style="width:180px;margin-left: 10px"
                                                 v-model="customVal"
                                                 :disabled="showWrite"
                                                 @change="handleChange"></el-input-number>
                            </el-radio>
                        </el-form-item>
                    </div>
                    <el-form-item label="发送时段">
                        <el-radio v-model="addForm.period"
                                  :disabled="showWrite"
                                  label="0">全天</el-radio>
                        <el-radio v-model="addForm.period"
                                  :disabled="showWrite"
                                  label="1">自定义时段
                            <template v-if="addForm.period==='1'">
                                <el-select v-model="addForm.startPeriod"
                                           placeholder=""
                                           :disabled="showWrite"
                                           style="width: 60px;">
                                    <el-option v-for="item in period_option"
                                               :key="item"
                                               :label="item"
                                               :value="item">
                                    </el-option>
                                </el-select>
                                <span>-</span>
                                <el-select v-model="addForm.endPeriod"
                                           placeholder=""
                                           :disabled="showWrite"
                                           style="width: 60px;">
                                    <el-option v-for="item in period_option"
                                               :key="item"
                                               :label="item"
                                               :value="item">
                                    </el-option>
                                </el-select>

                            </template>

                        </el-radio>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button size="small"
                           @click="dialogCancel">取消</el-button>
                <el-button type="primary"
                           size="small"
                           @click="goAdd">保存</el-button>
            </section>
        </el-dialog>

        <DialogUser :dialogUser="dialogUser"
                    @closeDialogUser="closeDialogUser"
                    v-if="dialogUser.dialogVisible"></DialogUser>

    </div>
</template>

<script>
    import { period_option } from 'src/pages/mirror_alert/alert/config/options.js'
    import subscribeFactory from 'src/services/alert/rb-alert-subscribe-service.factory.js'

    import personDataService from 'src/services/sys/person-services.js'
    import personDataMixin from 'src/services/sys/person-mixin.js'
    import draggable from 'vuedraggable'

    export default {
        name: 'Clone',
        display: 'Clone',
        order: 2,
        props: ['addDialog'],
        mixins: [personDataMixin],
        data() {
            return {
                showWrite: false,
                period_option: period_option,
                rules: {}, // 规则
                ruleForm: {},// 绑定的新增规则表单
                radio: '1', // 新增维护用户
                checkMsg: '', // 新增通知方式
                checkMail: '',
                repeatShow: false, // 是否重发
                value1: false,// 新增是否重发
                num: 1,// 新增自定义次数
                updateFlag: false,
                currentUserFlag: false,
                otherUserFlag: false,
                myuser: sessionStorage.getItem('username'),
                dialogUser: {// 用户列表dialog
                    dialogVisible: false,
                    date: [] // 数据
                },
                openSel: [{
                    value: '1',
                    label: '开启'
                }, {
                    value: '0',
                    label: '关闭'
                }],
                alertId: '',
                addForm: {
                    creator: '',//    创建人
                    alertIds: '',// 用户选择的告警id
                    defensetor: [],// 维护人
                    subscribeRules: '',// 告警配置名称
                    isOpen: '',// 是否启用
                    notifyAlertType: '',// 告警通知类型
                    notifyType: '',// 通知类型
                    reciverList: '',// 通知对象
                    recurrenceInterval: '',// 重发间隔
                    recurrenceIntervalUtil: '',// 重发间隔单位
                    isRecurrenceInterval: '',// 是否重发
                    recurrenceCount: '',// 重发次数
                    emailType: '',// 发送方式,
                    period: '0',// 执行时间段
                    startPeriod: '',// 配置起始执行时间
                    endPeriod: '',// 配置执行结束时间
                    emialContent: '',// 邮件的内容
                    subject: '',// 主题
                    uuid: '',
                    smsContent: ''// 短信内容
                },
                rulesName: [],// 已有配置规则
                userList: [],// 用户菜单
                reciverList: [],// 通知对象
                repass: '',
                customVal: '',
                subDraggable: {
                    msgAble: '',
                    mailAble: ''
                },
                options: {
                    titles: ['不显示字段', '显示字段'],
                    fieldMatch: {// 对应字段匹配
                        title: 'name',// 分类标题
                        list: 'codeList',// 分类列表
                        name: 'filedName',// 显示名称
                        display: 'display'// 是否显示
                    }
                },
                addFormRules: {
                    subscribeRules: [
                        { required: true, message: '请先输入订阅规则名称!', trigger: 'blur' },
                        { min: 4, max: 20, message: '长度在 4 到 20 个字符!', trigger: 'blur' }
                    ],
                    defensetor: [
                        { required: true, message: '请先选择维护用户!' }
                    ],
                    notifyAlertType: [
                        { required: true, message: '请先选择触发类型!' }
                    ],
                    reciverList: [
                        { required: true, message: '请先选择通知对象!' }
                    ],
                },

                // 通知对象
                loading: false,
                subscribeData: [],  // 列表数据
                usersList: [],
                data: [],
                defaultCheckedKeys: [],
                deviceAuthDataExpanded: [],
                defaultProps: {
                    children: 'childList',
                    label: 'name'
                },
                userData: [],
                multipleSelection: [], // 多选框模板存放的值
                selectItemList: [],
                filterText: '',
                searchText: '',
                selectedDepartmentId: '',

                // 通知模板
                msgAble: true,// 信息禁用
                mailAble: true,// 邮箱禁用
                activeName: 'first',// 分页标签
                list1: [
                    { name: '资源池:@{idc_type}' },
                    { name: '业务系统:@{object_type}' },
                    { name: '设备分类:@{device_class}' },
                    { name: '设备类型:@{device_type}' },
                    { name: '工程期数:@{project_name}' },
                    { name: '设备厂商:@{device_mfrs}' },
                    { name: '设备型号:@{device_model}' },
                    { name: '告警开始时间:@{alert_start_time}' },
                    { name: '告警设备IP:@{device_ip}' },
                    { name: '告警内容:@{moni_index}' },
                    { name: '告警名称:@{key_comment}' },
                    { name: '告警值:@{cur_moni_value}' },
                    { name: '告警等级:@{alert_level}' }
                ],
                list2: [],
                notifyParmas: {
                    emails: '',
                    alertIds: '',
                    subject: ''
                },
                rulesForm: [],
                readData: {
                    emails: '',
                    subject: ''
                },
                aleartidList: []

            }
        },
        components: {
            DialogUser: () => import('src/components/common/yw-dialog-user.vue'),
            draggable
        },

        methods: {
            queryDetail(id) {
                subscribeFactory.getSubscribeRulesById(id).then(res => {
                    if (res) {
                        this.ruleForm = res
                        this.addForm = res.alertSubscribeRulesDetailShowDtoDetail
                        this.addForm.defensetor = res.alertSubscribeRulesDetailShowDtoDetail.defensetor.split(',')
                        this.notifyParmas.emails = res.alertSubscribeRulesDetailShowDtoDetail.smsContent
                        if (res.alertSubscribeRulesDetailShowDtoDetail.notifyType === '0') {
                            this.addForm.notifyType = 0
                            this.checkMsg = 2
                            this.checkMail = 1
                            this.notifyParmas.emails = res.alertSubscribeRulesDetailShowDtoDetail.smsContent
                            this.notifyParmas.emails = res.alertSubscribeRulesDetailShowDtoDetail.emialContent
                            this.notifyParmas.subject = res.alertSubscribeRulesDetailShowDtoDetail.emialSubject
                        } else if (res.alertSubscribeRulesDetailShowDtoDetail.notifyType === '1') {
                            this.activeName = 'second'
                            this.addForm.notifyType = 1
                            this.checkMail = 1
                            this.notifyParmas.emails = res.alertSubscribeRulesDetailShowDtoDetail.emialContent
                            this.notifyParmas.subject = res.alertSubscribeRulesDetailShowDtoDetail.emialSubject
                            this.addForm.emailType = res.alertSubscribeRulesDetailShowDtoDetail.emailType
                        } else if (res.alertSubscribeRulesDetailShowDtoDetail.notifyType === '2') {
                            this.addForm.notifyType = 2
                            this.checkMsg = 2
                            this.notifyParmas.emails = res.alertSubscribeRulesDetailShowDtoDetail.smsContent
                        }
                        if (res.reciverList) {
                            this.selectItemList = res.reciverList
                        }
                        if (res.alertSubscribeRulesDetailShowDtoDetail.recurrenceCount === -1) {
                            this.repass = '-1'
                        } else {
                            this.repass = '1'
                            this.customVal = res.alertSubscribeRulesDetailShowDtoDetail.recurrenceCount
                        }
                        this.addForm.defensetor.forEach((user) => {
                            if (user === this.myuser) {
                                this.currentUserFlag = true
                            } else {
                                this.dialogUser.date.push(user)
                                this.otherUserFlag = true
                            }
                        })
                        res.alertSubscribeRulesManagementResponeList.forEach(item => {
                            this.aleartidList.push(item.alertId)
                        })
                        this.notifyParmas.alertIds = this.aleartidList.join(',')
                        this.addForm.alertIds = this.aleartidList.join(',')
                    }
                })
            },
            // 用户信息
            getUser() {
                console.log('dialogUser', this.dialogUser.date)
                this.dialogUser.dialogVisible = true
            },
            selectOther() {
                if (!this.otherUserFlag) {
                    this.dialogUser.date = []
                    this.addForm.defensetor = []
                    this.initUser()
                }
            },
            initUser() {
                if (this.currentUserFlag) {
                    this.addForm.defensetor = []
                    this.addForm.defensetor = [this.myuser]
                }
            },
            // 关闭用户弹窗
            closeDialogUser(date) {
                /* this.dialogUser.dialogVisible = false
                if (date && date.type === 'update') {
                    console.log('确认的操作')
                    this.dialogUser.date = date.userResult
                    this.addForm.defensetor = Array.from(new Set([this.addForm.defensetor, date.userResult]))
                } */
                this.dialogUser.dialogVisible = false
                if (date && date.type === 'update') {
                    // 确认的操作
                    console.log('date.userResult===', date.userResult)
                    this.dialogUser.date = []
                    if (this.currentUserFlag) {
                        let a = []
                        a.push(this.myuser)
                        console.log(a)
                        this.addForm.defensetor = []
                        this.addForm.defensetor = a.concat(date.userResult)
                    } else {
                        this.addForm.defensetor = []
                        this.addForm.defensetor = date.userResult
                    }
                    this.dialogUser.date = date.userResult
                    // this.addForm.defensetor = Array.from(new Set([this.addForm.defensetor, date.userResult]))
                }

            },
            // 选择用户
            selectCurrent() {
                if (this.currentUserFlag) {
                    let containFlag = false
                    for (let i = 0; i < this.addForm.defensetor.length; i++) {
                        if (this.addForm.defensetor[i] === this.myuser) {
                            containFlag = true
                            break
                        }
                    }
                    if (!containFlag) {
                        this.addForm.defensetor.unshift(this.myuser)
                    }
                } else {
                    for (let i = 0; i < this.addForm.defensetor.length; i++) {
                        if (this.addForm.defensetor[i] === this.myuser) {
                            this.addForm.defensetor.splice(i, 1)
                        }
                    }
                }
            },
            searchPersionList() {
                if (!this.selectedDepartmentId) {
                    this.$alert('请先选择部门!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                personDataService.getPersonList(this.selectedDepartmentId, 10000, 1, this.searchText).then((res) => {
                    if (res) {
                        this.userData = res.result
                    }
                })
            },

            // 修改确定
            goAdd() {
                if (this.checkMail && !this.checkMsg) {
                    this.addForm.notifyType = 1
                    this.addForm.emialContent = this.notifyParmas.emails
                    this.addForm.subject = this.notifyParmas.subject
                    this.addForm.smsContent = ''
                } else if (!this.checkMail && this.checkMsg) {
                    this.addForm.notifyType = 2
                    this.addForm.smsContent = this.notifyParmas.emails
                    this.addForm.subject = ''
                    this.addForm.emialContent = ''
                    this.addForm.emailType = ''
                } else if (this.checkMail && this.checkMsg) {
                    this.addForm.notifyType = 0
                    this.addForm.smsContent = this.notifyParmas.emails
                    this.addForm.emialContent = this.notifyParmas.emails
                    this.addForm.subject = this.notifyParmas.subject
                }
                this.addForm.uuid = this.alertId
                this.addForm.defensetor = this.addForm.defensetor.toString()
                this.addForm.reciverList = this.selectItemList
                console.log('ssssssss', this.addForm)
                if (!this.addForm.subscribeRules) {
                    this.$alert('请选择订阅规则', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (!this.addForm.defensetor) {
                    if (!this.otherUserFlag && !this.currentUserFlag) {
                        this.$alert('请选择维护用户', '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                } else if (!this.addForm.notifyAlertType) {
                    this.$alert('请选择触发类型', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.addForm.notifyType === null || this.addForm.notifyType === undefined || this.addForm.notifyType === '') {
                    this.$alert('请选择通知方式', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (!this.addForm.reciverList) {
                    this.$alert('请选择通知对象', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.addForm.notifyType == 2 && !this.addForm.smsContent) {
                    this.$alert('请填写通知模板', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.addForm.notifyType == 1 && !this.addForm.emialContent) {
                    this.$alert('请填写通知模板', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.checkMail === 1 && this.addForm.emailType === '') {
                    this.$alert('请填写邮件发送方式', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                if (this.addForm.defensetor.indexOf(sessionStorage.username) !== -1) {
                    subscribeFactory.updateSubscribeService(this.addForm).then((res) => {
                        if (res === 'success') {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            })
                            this.addDialog.dialogVisible = false
                        } else {
                            this.$message.error('修改失败!')
                        }
                    })

                } else {
                    this.$alert('您不是维护用户', '警告', {
                        confirmButtonText: '确定'
                    })
                }
            },
            // 新增自定义次数
            handleChange(value) {
                if (this.addForm.recurrenceCount !== '-1') {
                    this.addForm.recurrenceCount = value
                }

            },
            dialogCancel() {
                this.addDialog.dialogVisible = false
                this.$refs.addForm.resetAddTableData()
            },

            // 通知对象
            // 添加到已选栏中
            addItems() {
                var arr = [].concat(this.multipleSelection)
                arr.forEach(item => {
                    if (!(_.map(this.selectItemList, 'uuid').includes(item.uuid))) {
                        let obj = {}
                        obj.notifyObjInfo = item.name
                        obj.name = item.name
                        obj.email = item.mail
                        obj.telephone = item.mobile
                        obj.uuid = item.uuid
                        this.selectItemList.push(obj)
                    }
                })
            },
            del(row, index) {
                let that = this
                that.selectItemList.splice(index, 1)
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            getDepartment() {
                personDataService.getDepartmentTree().then((res) => {
                    // 返回部门id用作获取默认用户列表
                    if (res.length > 0) {
                        this.deviceAuthDataExpanded = [res[0].uuid]
                        this.selectedDepartmentId = res[0].uuid
                        this.getPersionList(res[0].uuid)
                    }
                })
            },
            filterNode(value, data, node) {
                if (!value) return true
                return node.label.indexOf(value) !== -1
            },
            handleNodeClick(data) {
                this.selectedDepartmentId = data.uuid
                this.getPersionList(data.uuid)
            },
            getPersionList(deptId) {
                personDataService.getPersonList(deptId, 10000, 1, '').then((res) => {
                    if (res) {
                        this.userData = res.result
                    }
                })
            },

            // 通知模板
            // 拖拽
            log(evt) {
                this.notifyParmas.emails += '【' + evt.added.element.name + '】'

            },
            // 分页标签
            handleClick(tab, event) {
                console.log(tab, event)
            },
            // 发送模板
            goNotify() {
                if (this.addForm.defensetor.indexOf(sessionStorage.username) !== -1) {

                    subscribeFactory.emailNotify(this.notifyParmas).then((res) => {
                        this.readData.emails = res[0].emails
                        this.readData.subject = res[0].subject
                    })
                } else {
                    this.$alert('您不是维护用户', '警告', {
                        confirmButtonText: '确定'
                    })
                }

            },
            unique(arr) {
                const res = new Map()
                return arr.filter((arr) => !res.has(arr.name) && res.set(arr.name, 1))
            }

        },
        created() {
        },
        mounted() {
            this.getDepartment()
            //  this.initUser()
        },
        watch: {
            'addForm.isRecurrenceInterval'(val) {
                if (val == 1) {
                    this.repeatShow = true
                } else {
                    this.repeatShow = false
                }
            },
            /* 'dialogUser.date'(val) {
                console.log('watch', val)
                this.addForm.defensetor = []
                if (this.currentUserFlag) {
                    var userArr = [this.myuser]
                    this.addForm.defensetor = userArr.push(...val.split(','))
                    console.log('watchdefensetor', this.addForm.defensetor)
                }
            }, */
            checkMsg(val) {
                if (val === 2) {
                    this.addForm.notifyType = 2
                    this.subDraggable.msgAble = false
                } else {
                    this.subDraggable.msgAble = true
                }

            },
            checkMail(val) {
                if (val === 1) {
                    this.addForm.notifyType = 1
                    this.addForm.emailType = 1
                    this.subDraggable.mailAble = false
                } else {
                    this.subDraggable.mailAble = true
                    this.addForm.emailType = ''
                }
            },

            repass(val) {
                if (val == -1) {
                    this.addForm.recurrenceCount = -1
                } else {
                    this.addForm.recurrenceCount = this.ruleForm.alertSubscribeRulesDetailShowDtoDetail.recurrenceCount
                }
            },
            addDialog: {
                handler(val) {
                    this.queryDetail({ id: val.data.id })
                    this.alertId = val.data.id
                },
                immediate: true
            },
            filterText(val) {
                this.$refs['departmentTree'].filter(val)
            },

        }

    }

</script>
<style lang='scss' scoped>
    .transfer-wrap {
      display: flex;
      justify-content: space-around;

      .transfer-left,
      .transfer-right {
        width: 400px;
        height: 300px;
        border: 1px solid #eee;
        padding: 10px;

        .yw-el-table {
          height: 300px;
        }
      }

      .transfer-left {
        display: flex;
        justify-content: space-around;

        .search-box-wrap {
          position: relative;

          .el-icon-search {
            position: absolute;
            top: 12px;
            right: 8px;
            font-size: 16px;
            color: #46bafe;
            cursor: pointer;
            &:hover {
              color: #086297;
            }
          }
        }

        .tree-wrap {
          width: 37%;
        }

        .el-table {
          width: 230px;
          height: 240px !important;
        }
      }

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

      .transfer-right {
        display: flex;
        flex-direction: column;

        .transfer-right-top {
          display: flex;
          justify-content: space-between;

          .search-box-wrap {
            position: relative;
            .el-input {
              width: 240px;
            }

            .el-icon-search {
              position: absolute;
              top: 12px;
              right: 8px;
              font-size: 16px;
              color: #46bafe;
              cursor: pointer;
              &:hover {
                color: #086297;
              }
            }
          }
        }

        .el-table {
          height: 240px !important;
        }
      }
    }

    .draggable-wrap {
      width: 880px;
      height: 300px;
      border: 1px solid #eee;
      display: flex;
      padding: 10px;

      .draggable-col-left {
        width: 300px;
        margin-right: 20px;
        border-right: 1px solid #eee;
        .list-group {
          display: flex;
          //justify-content: space-around;
          flex-wrap: wrap;
          .list-group-item {
            background: #eee;
            padding: 0 10px;
            margin: 4px;
          }
        }
      }

      .draggable-col-left::after {
        content: '';
        width: 1px;
        background: #eee;
      }

      .draggable-col-right {
        flex: 1;

        .tabs-wrap {
          display: flex;
          justify-content: space-between;

          span {
            display: block;
          }
          .list-group {
            //display: flex;
            //justify-content: space-around;
            //flex-wrap: wrap;
            width: 266px;
            height: 220px;
            border: 1px solid #eee;
            padding: 0 4px;

            .el-divider--horizontal {
              margin: 0;
            }

            .list-group-item {
              float: left;
              padding-right: 4px;
            }

            .mail-item {
              display: flex;
              flex-wrap: wrap;

              p {
                padding-right: 10px;
              }

              .item-box {
                width: 220px;
              }

              .list-group-item::after {
                content: '';
              }
            }

            .list-group-item::after {
              content: ',';
            }

            .list-group-item:last-child::after {
              content: '';
            }
          }
        }
      }
    }

    .transfer-wrap {
      .transfer-left {
        width: 600px;
      }
    }
    .table-word {
      width: 100px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
</style>