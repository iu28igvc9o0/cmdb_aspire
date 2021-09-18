<template>
    <div>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <section class="fr">
                    <el-button class="btn-icons-wrap"
                               v-show="lockScgreen"
                               @click="lock">锁屏</el-button>
                    <el-button class="btn-icons-wrap"
                               v-show="goScgreen"
                               @click="go">刷屏</el-button>
                    <!-- <el-button class="btn-icons-wrap" v-popover:popover>选择列&nbsp;&nbsp;&nbsp;<i class="el-icon-arrow-down"></i></el-button> -->
                    <el-popover ref="popover"
                                placement="top"
                                trigger="click">
                        <div id="popover"
                             style="max-height: 300px;overflow-y: auto;">
                            <el-checkbox-group v-model="test"
                                               @change="handleCheckedColumnChange">
                                <div v-for="(column,index) in checkColumns"
                                     :key="index">
                                    <el-checkbox :label="column.keyCode">{{ column.keyName }}</el-checkbox>
                                </div>
                            </el-checkbox-group>
                        </div>
                    </el-popover>
                </section>
                <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="transferDialog"
                           v-if="alertType === 'activity' && !isHidden">转派</el-button>
                <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="confirmDialog"
                           v-if="alertType === 'activity' || alertType === 'observe' ">确认</el-button>
                <!-- <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="alertObserve()"
                           v-if="alertType === 'activity'">待观察</el-button> -->
                <el-button class="btn-icons-wrap"
                           type="primary"
                           @click="sendDialog"
                           v-if="alertType === 'activity' && !isHidden">派单</el-button>
                <el-button class="btn-icons-wrap"
                           @click="cleanDialog">清除</el-button>
                <el-button class="btn-icons-wrap"
                           v-if="!isHidden"
                           @click="noticeDialog">通知</el-button>
                <el-button class="btn-icons-wrap"
                           @click="notifyDialog"
                           v-if="alertType !== 'exception' && !isHidden">已通知</el-button>
                <el-button class="btn-icons-wrap"
                           @click="alertVoiceNotifyDialog()"
                           v-if="alertType === 'activity' && !isHidden">语音通知</el-button>
                <el-button class="btn-icons-wrap"
                           @click="resourceExport"
                           :disabled="exportDisabled">导出</el-button>
                <el-button class="btn-icons-wrap"
                           v-if="!isHidden"
                           @click="addRules">通知订阅</el-button>
                <!-- 演示 -->
                <el-button class="btn-icons-wrap"
                           @click="demo=true">根因分析</el-button>
            </div>
            <!-- 告警列表 -->
            <div class="yw-el-table-wrap ywTabBorder">
                <el-table border
                          class="yw-el-table auto-height"
                          :data="activityAlertData"
                          style="cursor: pointer;"
                          stripe
                          tooltip-effect="dark"
                          v-loading="loading"
                          element-loading-text="正在查询数据, 请稍等..."
                          @selection-change="handleSelectionChange"
                          @row-dblclick="dblhandleCurrentChange($event)"
                          :header-cell-style="{background:'#DEE9FC',color:'#53607E'}">
                    <el-table-column type="selection"
                                     width="40px"></el-table-column>

                    <el-table-column v-for="(itemData, index) in modelFieldDataList"
                                     :key="itemData.fieldCode"
                                     :label="itemData.listShowName"
                                     :prop="itemData.fieldCode"
                                     sortable
                                     :fixed='index < 2'
                                     :show-overflow-tooltip="true"
                                     :width="((itemData.tableColumnWidth && itemData.tableColumnWidth > 0) ? itemData.tableColumnWidth : '200') + 'px'">
                        <template slot-scope="scope">
                            <rb-mirror-alert-status width="100px"
                                                    v-if="itemData.fieldCode === 'alert_level'"
                                                    :mode="'list'"
                                                    :status="scope.row.alert_level"></rb-mirror-alert-status>
                            <span v-else-if="itemData.fieldCode === 'order_id'"
                                  @click="gotoBPM(scope.row.order_id)"
                                  style="color:#0060DF">{{scope.row.order_id}}</span>
                            <span v-else>{{scope.row[itemData.fieldCode]}}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>

        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   title="确认"
                   :visible.sync="confirm.confirmDialog"
                   width="460px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="84px"
                         v-if="confirm.confirmDialog">
                    <el-form-item label="告警确认">
                        <span>
                            <i class="el-icon-info"></i>告警数据将移至已确认看板
                        </span>
                    </el-form-item>
                    <el-form-item label="确认操作">
                        <el-radio-group v-model="confirm.operation">
                            <el-radio :label="1">本次直接确认</el-radio>
                            <el-radio :label="2">后续自动确认</el-radio>
                        </el-radio-group>
                        <el-date-picker v-model="confirm.dateTimeRange"
                                        v-if="confirm.operation === 2"
                                        style="width:320px;"
                                        type="datetimerange"
                                        align="right"
                                        range-separator="至"
                                        start-placeholder="开始时间"
                                        end-placeholder="结束时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="告警确认内容">
                        <el-input type="textarea"
                                  :rows="5"
                                  style="width:320px;"
                                  v-model="confirm.confirmDialogTextArea"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="confirmElert">确定</el-button>
                <el-button size="small"
                           @click="cancel('confirm')">取消</el-button>
            </section>
        </el-dialog>

        <!-- <el-dialog class="yw-dialog"
                   title="待观察"
                   :visible.sync="observe.observeDialog"
                   width="460px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="84px"
                         v-if="observe.observeDialog">
                    <el-form-item label="告警待观察">
                        <span>
                            <i class="el-icon-info"></i>告警数据将移至已待观察看板
                        </span>
                    </el-form-item>
                    <el-form-item label="操作">
                        <el-radio-group v-model="observe.operation">
                            <el-radio :label="1">本次直接确认</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="observeSubmit()">确定</el-button>
                <el-button size="small"
                           @click="observe.observeDialog = false">取消</el-button>
            </section>
        </el-dialog> -->

        <el-dialog class="yw-dialog"
                   v-if="notice.noticeDialog"
                   title="告警通知"
                   :visible.sync="notice.noticeDialog"
                   width="900px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <rb-mirror-alert-use-list ref="rbMirrorAlertUseList"></rb-mirror-alert-use-list>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="sendSMS()">发送短信</el-button>
                <el-button type="primary"
                           size="small"
                           @click="sendEmail()">发送邮件</el-button>
                <el-button size="small"
                           @click="cancel('notice')">取消</el-button>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   :visible.sync="transfer.transferDialog"
                   width="570px"
                   title="转派"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="70px">
                    <el-form-item label="告警转派">
                        <span>
                            <i class="el-icon-info"></i>告警数据将转派到某个人进行后续处理
                        </span>
                    </el-form-item>
                    <el-form-item label="转派操作">直接转派</el-form-item>
                    <el-form-item label="转派人员">
                        <el-checkbox v-model="transfer.transferTeamChecked">个人</el-checkbox>
                        <el-input v-model="transfer.transferPersonalInput"></el-input>
                        <el-button type="text"
                                   @click="transfer.dialogChoose=true">个人选择器</el-button>
                    </el-form-item>
                    <el-form-item label>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      :data="transfer.transferTableData"
                                      height="200px"
                                      stripe
                                      border
                                      tooltip-effect="dark">
                                <el-table-column prop="transfer_info"
                                                 label="转派对象信息"></el-table-column>
                                <el-table-column prop="operation"
                                                 label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="primary"
                                                   size="small"
                                                   @click="deleteUser(scope.row.id)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="turnSend">转派</el-button>
                <el-button size="small"
                           @click="cancel('transfer')">取消</el-button>
            </section>
        </el-dialog>
        <div class="alertNotice">
            <el-dialog class="yw-dialog"
                       title="选择用户"
                       :visible.sync="transfer.dialogChoose"
                       width="700px"
                       :close-on-click-modal="false">
                <rb-mirror-alert-use-list ref="rbMirrorAlertUseList"></rb-mirror-alert-use-list>
                <section class="btn-wrap">
                    <el-button type="primary"
                               size="small"
                               @click="choose">确认选择</el-button>
                    <el-button size="small"
                               @click="cancel('choose')">返回</el-button>
                </section>
            </el-dialog>
        </div>
        <el-dialog class="yw-dialog"
                   :visible.sync="send.sendDialog"
                   width="446px"
                   title="派单"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="70px">
                    <el-form-item label="告警派单">
                        <el-radio-group v-model="orderType">
                            <el-radio :label="1">告警工单</el-radio>
                            <el-radio :label="2">故障工单</el-radio>
                            <el-radio :label="3">维保工单</el-radio>
                            <el-radio :label="4">调优工单</el-radio>
                        </el-radio-group>
                        <!-- <span><i class="el-icon-info"></i>告警数据派单到对应工单，进行工单处理</span> -->
                    </el-form-item>
                    <el-form-item label="派单操作">直接后台派单</el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="sendbpm">确定</el-button>
                <el-button size="small"
                           @click="cancel('send')">取消</el-button>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   :visible.sync="clean.cleanDialog"
                   title="清除"
                   width="446px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="clean"
                         ref="formClean"
                         :rules="cleanRules"
                         label-width="80px">
                    <el-form-item label="告警清除">
                        <span>
                            <i class="el-icon-info"></i>告警数据将移至历史告警看板
                        </span>
                    </el-form-item>
                    <el-form-item label="确认清除">
                        <el-radio-group v-model="clean.operation">
                            <el-radio :label="1">本次直接清除</el-radio>
                            <el-radio :label="2">后续自动清除</el-radio>
                        </el-radio-group>
                        <el-date-picker v-model="clean.dateTimeRange"
                                        v-if="clean.operation === 2"
                                        style="width:300px;"
                                        type="datetimerange"
                                        align="right"
                                        range-separator="至"
                                        start-placeholder="开始时间"
                                        end-placeholder="结束时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="清除原因"
                                  prop="cleanDialogTextArea">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="clean.cleanDialogTextArea"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="goRemove('formClean')">确定</el-button>
                <el-button size="small"
                           @click="cancel('clean')">取消</el-button>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   title="高级检索"
                   :visible.sync="search.searchDialog"
                   width="500px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         label-width="84px">
                    <el-form-item label="主机名称">
                        <el-input v-model="search.host_name"></el-input>
                    </el-form-item>
                    <el-form-item label="设备类型">
                        <el-select v-model="search.device_type"
                                   placeholder="请选择"
                                   clearable
                                   filterable>
                            <el-option v-for="item in device_typess"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="告警生效时间">
                        <el-date-picker v-model="search.alert_date_from"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>-
                        <el-date-picker v-model="search.alert_date_to"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="当前监控时间">
                        <el-date-picker v-model="search.cur_moni_time_from"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>-
                        <el-date-picker v-model="search.cur_moni_time_to"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="告警分类">
                        <el-select v-model="search.alert_type"
                                   placeholder="请选择"
                                   clearable
                                   filterable>
                            <el-option v-for="item in search.alert_types"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="监控对象">
                        <el-select v-model="monitor_value"
                                   placeholder="请选择"
                                   multiple
                                   filterable
                                   collapse-tags>
                            <el-option v-for="item in monitor_values"
                                       :key="item"
                                       :label="item"
                                       :value="item"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="机房">
                        <el-select v-model="search.device_room"
                                   placeholder="请选择"
                                   clearable
                                   filterable>
                            <el-option v-for="item in idc_locations"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="告警描述">
                        <el-input v-model="search.alert_description"></el-input>
                    </el-form-item>

                    <el-form-item label="设备型号">
                        <el-input v-model="search.device_model"></el-input>
                    </el-form-item>
                    <el-form-item label="通知方式">
                        <el-checkbox-group v-model="search.noticeType">
                            <el-checkbox v-for="type in notice.noticeTypes"
                                         :label="type.id"
                                         :key="type.id">{{type.name}}</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                    <el-form-item label="通知状态">
                        <el-radio v-model="search.notice_status"
                                  label="1">成功</el-radio>
                        <el-radio v-model="search.notice_status"
                                  label="0">失败</el-radio>
                    </el-form-item>
                    <el-form-item label="通知操作时间">
                        <el-date-picker v-model="search.notice_date_from"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>-
                        <el-date-picker v-model="search.notice_date_to"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="转派时间">
                        <el-date-picker v-model="search.transfer_date_from"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>-
                        <el-date-picker v-model="search.transfer_date_to"
                                        type="datetime"
                                        placeholder="选择日期时间"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        size="mini"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="转派操作人">
                        <el-input v-model="search.transfer_operator"></el-input>
                    </el-form-item>
                    <el-form-item label="待确认人">
                        <el-input v-model="search.confirm_operator"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <!-- <section class="btn-wrap">
            <el-button type="primary" size="small" @click="searchData">查询</el-button>
            <el-button size="small" @click="cancelHighSearch">取消</el-button>
      </section> -->
        </el-dialog>
        <!-- dialog 语音通知 -->
        <el-dialog class="yw-dialog"
                   v-if="voiceNotifyDialog"
                   title="语音通知"
                   :visible.sync="voiceNotifyDialog"
                   width="500px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <section class="yw-dialog-main">
                <rb-mirror-alert-voice-notify ref="rbMirrorAlertVoiceNotify"
                                              v-if="voiceNotifyDialog"
                                              :alertVoiceNotifyDetail="alertVoiceNotifyDetail"></rb-mirror-alert-voice-notify>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="createVoiceNotify()">确认</el-button>
                <el-button size="small"
                           @click="cancel('voice')">取消</el-button>
            </section>
        </el-dialog>

        <!-- 新增规则 -->
        <alertSubscribeRulesAdd :addDialog="addDialog"
                                @closeDialog="closeDialog"
                                v-if="addDialog.dialogVisible"></alertSubscribeRulesAdd>

        <!-- 演示 -->
        <el-dialog class="yw-dialog"
                   v-if="demo"
                   title="根因分析"
                   :visible.sync="demo"
                   width="800px"
                   height="500px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <section class="yw-dialog-main">
                <h3 style="text-align:center">故障原因</h3>
                <table class="bordered mtop20">
                    <tr>
                        <td width="130">IT运维原始告警：</td>
                        <td>192.168.1.119 IP不可达</td>
                    </tr>
                    <tr>
                        <td width="130">匹配动环告警：</td>
                        <td>ABBUPS输出列头柜1/24，UPS输出屏中断告警</td>
                    </tr>
                    <tr>
                        <td width="130">故障根因：</td>
                        <td>列头柜掉电，引起对应机柜内IT设备断电宕机，对应告警为“ABBUPS输出列头柜1/24，UPS输出屏中断告警”</td>
                    </tr>
                </table>
                <h3 style="text-align:center;margin-top:10px">匹配详情</h3>
                <div class="ywTab">
                    <el-tabs v-model="demoName"
                             @tab-click="demoClick">
                        <el-tab-pane label="IT运维告警"
                                     name="first">
                            <table class="bordered mtop20">
                                <tr>
                                    <td width="130">告警内容：</td>
                                    <td>192.168.1.1 IP不可达对应范围</td>
                                </tr>
                                <tr>
                                    <td width="130">告警时间：</td>
                                    <td>2020/12/1 19:17:25</td>
                                </tr>
                                <tr>
                                    <td width="130">资源池：</td>
                                    <td>南方基地</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机房：</td>
                                    <td>广州南方基地数据中心B座502机房</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A01</td>
                                </tr>
                                <tr>
                                    <td width="130">所属业务：</td>
                                    <td>一级业务支撑</td>
                                </tr>
                            </table>
                        </el-tab-pane>
                        <el-tab-pane label="动环告警"
                                     name="second">
                            <table class="bordered mtop20">
                                <tr>
                                    <td width="130">告警名称：</td>
                                    <td>ABBUPS输出列头柜1/24，UPS输出屏中断告警</td>
                                </tr>
                                <tr>
                                    <td width="130">告警时间：</td>
                                    <td>2020/12/1 19:17:25</td>
                                </tr>
                                <tr>
                                    <td width="130">资源池：</td>
                                    <td>南方基地</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机房：</td>
                                    <td>广州南方基地数据中心B座502机房</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A01</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A02</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A03</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A04</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A05</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A06</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A07</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A08</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A09</td>
                                </tr>
                                <tr>
                                    <td width="130">业务机柜：</td>
                                    <td>广州南方基地数据中心B座502机房-A10</td>
                                </tr>
                                <tr>
                                    <td width="130">所属业务：</td>
                                    <td>一级业务支撑</td>
                                </tr>
                            </table>
                        </el-tab-pane>
                    </el-tabs>

                </div>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import { alert_level, alert_type, relation } from '../list/config/config.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import rbMirrorAlertStatus from 'src/pages/mirror_alert/common/rb-mirror-alert-status.vue'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import rbMirrorAlertUseList from 'src/pages/mirror_alert/common/rb-mirror-alert-use-list.vue'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbMirrorAlertNum from 'src/pages/mirror_alert/common/rb-mirror-alert-num.vue'
    import rbAlertFilterServicesFactory from 'src/services/alert/rb-alertFilter-services.factory.js'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import rbMirrorAlertVoiceNotify from 'src/pages/mirror_alert/common/rb-mirror-alert-voice-notify.vue'
    import rbAlertVoiceNotifyServicesFactory from 'src/services/alert/rb-alert-voice-notify-services.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    export default {
        mixins: [QueryObject],
        name: 'MirrorAlertAlertList',
        components: {
            rbMirrorAlertStatus,
            rbMirrorAlertNum,
            rbMirrorAlertUseList,
            Treeselect,
            rbMirrorAlertVoiceNotify,
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            alertSubscribeRulesAdd: () => import('./alert-subscribeRules-add.vue')

        },
        props: ['alertType', 'alertLevel_default', 'queryDatas', 'queryDatasFile', 'modelFieldData'],
        data() {
            return {
                addDialog: {// 新增规则
                    dialogVisible: false,
                    id: []
                },
                queryDatasList: {},
                modelFieldDataList: [], // 动态生成列 头部
                queryDataParameterFile: {},
                queryDataParameter: {},
                cleanRules: {
                    cleanDialogTextArea: [
                        { required: true, message: '不能为空', trigger: 'blur' }
                    ]
                },
                loading: true,
                bizSysTree: null,
                bizSysDepChecked: [],
                bizDepTreeSelOpts: [],
                sceneId: '',
                filterFlag: false,
                filterCondition: '',
                // filterData: [],
                filterId: '',
                filterSceneData: [],
                test: [],
                checkColumns: [
                    { keyCode: 'source_room', keyName: '机房位置', keyIsSelected: false },
                    { keyCode: 'device_mfrs', keyName: '设备厂商', keyIsSelected: false },
                    { keyCode: 'device_model', keyName: '设备型号', keyIsSelected: false },
                    { keyCode: 'host_name', keyName: '主机名称', keyIsSelected: false },
                    { keyCode: 'moni_object', keyName: '监控对象', keyIsSelected: false },
                    { keyCode: 'object_type', keyName: '告警类型', keyIsSelected: false },
                    { keyCode: 'alert_count', keyName: '告警数量', keyIsSelected: false },
                    { keyCode: 'report_type', keyName: '通知方式', keyIsSelected: false },
                    { keyCode: 'report_status', keyName: '通知状态', keyIsSelected: false },
                    { keyCode: 'report_time', keyName: '通知时间', keyIsSelected: false },
                    { keyCode: 'trans_status', keyName: '转派状态', keyIsSelected: false },
                    { keyCode: 'trans_user', keyName: '转派人', keyIsSelected: false },
                    { keyCode: 'trans_time', keyName: '转派时间', keyIsSelected: false },
                    { keyCode: 'to_confirm_user', keyName: '待确认人', keyIsSelected: false },
                    { keyCode: 'order_type', keyName: '派单类型', keyIsSelected: false },
                    { keyCode: 'order_status', keyName: '派单状态', keyIsSelected: false },
                    { keyCode: 'deliver_time', keyName: '派单时间', keyIsSelected: false },
                    { keyCode: 'confirmed_user', keyName: '确认人', keyIsSelected: false },
                    { keyCode: 'confirmed_time', keyName: '确认时间', keyIsSelected: false },
                    { keyCode: 'confirmed_content', keyName: '确认内容', keyIsSelected: false }
                ],
                columnFilter: {
                    source_room: false,
                    device_mfrs: false,
                    device_model: false,
                    host_name: false,
                    biz_sys: false,
                    moni_object: false,
                    object_type: false,
                    alert_count: false,
                    report_type: false,
                    report_status: false,
                    report_time: false,
                    trans_status: false,
                    trans_user: false,
                    trans_time: false,
                    to_confirm_user: false,
                    order_type: false,
                    order_status: false,
                    deliver_time: false
                },
                classA: 'classA',
                classB: 'classB',
                alertList: [],
                // 选择列存放的值
                filterForm: {},
                columnInfo: '',
                checkedcolumns: [],
                moduleId: '12345XYUEFKSLDDLDKFJAL',
                column_data: {},
                columnList: [],
                detailObjId: '',
                detailOrderStatus: '',
                // 多选框模板存放的值
                multipleSelection: [],
                activityAlertData: [], // 活动告警数据数组
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                deviceList: [], // 查询出的设备
                lockScgreen: true,
                goScgreen: false,
                //  device_relation_value: '',
                //  monitor_relation_value: '',
                device_device: this.$route.query.deviceIp || '', // 包含的设备
                monitor_device: '', // 监控项的设备
                monitor_value: [],
                monitor_values: [],
                // 告警级别
                alert_level_value: '',
                alert_level: '',
                // 告警类型
                object_type: '',
                queryType: '',
                // 设备关系
                relation: '',
                // 业务系统
                bizSysList: [],
                // 资源池
                resourcePoors: [],
                // 机房
                idc_locations: [],
                // 设备类型
                device_types: [],
                // 设备分类
                device_classs: [],
                device_typess: [],
                alert_froms: [],
                device_mfrss: [],
                // 告警派单
                send: {
                    send_type: '', // 派单类型
                    sendDialog: false
                },
                // 用户列表
                userList: [],
                userResult: [],
                // 告警清除
                clean: {
                    cleanDialog: false,
                    cleanDialogTextArea: '',
                    operation: 1,
                    dateTimeRange: []
                },
                // 告警确认
                confirm: {
                    confirmDialog: false, // 弹出框
                    confirmDialogTextArea: '', // 告警确认弹出框文本域
                    operation: 1,
                    dateTimeRange: []
                },
                // 告警通知
                notice: {
                    noticeTypes: [
                        {
                            id: '0',
                            name: '短信'
                        },
                        {
                            id: '1',
                            name: '邮件'
                        }
                    ],
                    noticeDialog: false, // 弹出框
                    noticeTeamChecked: false, // 团队
                    noticeTeamInput: '',
                    noticeTeamType: [],
                    noticePersonalChecked: false, // 个人
                    noticePersonalInput: '',
                    noticePersonalType: [],
                    noticeTableData: [],
                    userList: [],
                    userResult: []
                },
                // 告警转派
                transfer: {
                    dialogChoose: false,
                    transferDialog: false,
                    transferTeamChecked: false, // 团队
                    transferTeamInput: '',
                    choose_user: '', // 已选择的用户
                    transferPersonalChecked: false, // 个人
                    transferPersonalInput: '',
                    transferTableData: []
                },
                // 高级检索
                search: {
                    searchDialog: false,
                    alert_date_from: '', // 告警开始时间
                    alert_date_to: '', // 告警结束时间
                    cur_moni_time_from: '',
                    cur_moni_time_to: '',
                    alert_type: '', // 告警分类
                    alert_types: [],
                    device_type: '', // 设备类型
                    device_class: '', // 设备分类
                    system: '', // 业务系统
                    alert_description: '', // 告警描述
                    resource_pool: this.$route.query.idcType || '', // 资源池
                    device_room: '', // 机房
                    notice_date_from: '', // 通知开始时间
                    notice_date_to: '', // 通知结束时间
                    noticeType: [], // 通知方式
                    notice_status: '', // 通知状态
                    // confirm_date_from: '', // 确认开始时间
                    // confirm_date_to: '', // 确认结束时间
                    transfer_date_from: '', // 转派开始时间
                    transfer_date_to: '', // 转派结束时间
                    // work_date_from: '', // 派单开始时间
                    // work_date_to: '', // 派单结束时间
                    transfer_operator: '', // 转派操作人
                    confirm_operator: '', // 待确认人
                    operate_status: '', // 操作状态
                    alert_from: [], // 告警来源
                    device_mfrs: '', // 设备厂商
                    device_model: '', // 设备型号
                    host_name: ''
                },
                dicts: {},
                timer_frequency: 60000,
                exportDisabled: false,
                voiceNotifyDialog: false,
                alertVoiceNotifyDetail: {},
                orderType: 1,
                // 告警确认
                observe: {
                    observeDialog: false, // 弹出框
                    operation: 1,
                },
                demo: false,
                demoName: 'first'
            }
        },
        computed: {
            isHidden() {
                return window.cityName === 'guangxi'
            }
        },
        mounted() {
            this.initAlertSum()
            this.init()
            // this.initFilter()
        },
        destroyed() {
            clearInterval(this.timer)
            clearInterval(this.voiceTimer)
            this.timer = null
            this.voiceTimer = null
        },
        methods: {
            getFilterData(condition, sceneId) {
                this.sceneId = sceneId
                this.filterFlag = true
                this.filterCondition = condition
                this.searchFilterData()
            },
            searchFilterData(valFile) {
                rbAlertFilterServicesFactory.getFilterData(valFile).then(res => {
                    this.activityAlertData = this.parseTableData(
                        this.packData(res.result)
                    )
                    this.total = res.count
                    this.$emit('sendTotal', this.total, this.currentPage, this.pageSize)
                    let sceneId = this.sceneId
                    if (this.filterSceneData.length > 0) {
                        let currentScene = this.filterSceneData.find(function (o) {
                            return o.sceneId === sceneId
                        })
                        if (currentScene !== undefined && currentScene !== null) {
                            currentScene.count = this.total
                            this.$forceUpdate()
                        }
                    }
                })
                    .catch(() => {
                        this.$message.error('查询失败')
                    })
            },
            initFilter() {
                // rbAlertFilterServicesFactory.getAll(true).then(res => {
                //     this.filterData = res
                // })
            },
            filterChange(val) {
                if (val === '' || val === null) {
                    this.filterSceneData = []
                } else {
                    rbAlertFilterServicesFactory.getFilterScene(val).then(res => {
                        this.filterSceneData = res
                    })
                }
            },
            initAlertSum() {
            },
            // 1 element相关方法 多选框
            handleSelectionChange(val) {
                this.alertList = []
                this.addDialog.id = []
                this.multipleSelection = val
                console.log(val)
                val.forEach(item => {
                    this.alertList.push({ alertId: item.alertId })
                    this.addDialog.id.push(item.alert_id)
                })

            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                // this.searchDataGo()
                if (this.queryDataParameterFile.condition) {
                    this.queryDataParameterFile.page_no = val
                    this.searchFilterData(this.queryDataParameterFile)
                } else {
                    this.queryDatasList.page_size = val
                    this.getTableData(this.queryDatasList)  // 分页新数据集合
                }
            },
            searchDataGo() {
                if (this.filterFlag) {
                    this.searchFilterData(this.queryDataParameterFile)
                } else {
                    // this.searchData(1)
                }
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                // this.searchDataGo()
                if (this.queryDataParameterFile.condition) {
                    this.queryDataParameterFile.page_no = val
                    this.searchFilterData(this.queryDataParameterFile)
                } else {
                    this.queryDatasList.page_no = val
                    this.getTableData(this.queryDatasList)
                }
            },
            // 对话框
            handleClose(done) {
                done()
            },
            changePool() {
                let _this = this
                _this.bizDepTreeSelOpts = []
                rbAlertKanBanServiceFactory
                    .bizSysTreeOptionByResourcePollSel(this.search.resource_pool || '')
                    .then(function (poolDeps) {
                        let sysDepts =
                            _this.bizSysTree && _this.bizSysTree.length > 0 && _this.bizSysTree[0].children && _this.bizSysTree[0].children.length > 0
                                ? _this.bizSysTree[0].children
                                : []
                        _.forEach(poolDeps, poolDep => {
                            if (poolDep) {
                                _(sysDepts)
                                    .filter(sysdep => {
                                        return poolDep === sysdep.id
                                    })
                                    .forEach(subdep => {
                                        _this.bizDepTreeSelOpts.push(subdep)
                                    })
                            }
                        })
                    })
            },
            cleanPool() {
                this.bizSysList = []
                this.search.system = ''
            },
            // 获取用户列表
            // getUserList() {
            //     const namespace = sessionStorage.getItem('namespace')
            //     let arr = [
            //         namespace,
            //         {
            //             order_by: '-createTime',
            //             page_size: -1
            //         }
            //     ]
            //     rbProjectDataServiceFactory.getUserList(arr).then(res => {
            //         let arr = []
            //         res.result.forEach(item => {
            //             let obj = {
            //                 label: item.name,
            //                 key: item.username
            //             }
            //             arr.push(obj)
            //         })
            //         this.userList = arr
            //     })
            // },
            // 获取设备
            querySearchDevice() { },

            // 当前告警列表新街口
            getTableData(obj) {
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: obj,
                    url: '/v2/alerts/alert/query'
                }).then((res) => {
                    this.total = res.count
                    // this.activityAlertData = res.result
                    this.activityAlertData = this.parseTableData(this.packData(res.result))

                    this.activityAlertData.forEach(item => {
                        if (item.notify_status === '0') {
                            item.notify_status = '未通知'
                        } else if (item.notify_status === '1') {
                            item.notify_status = '已通知'
                        } else {
                            item.notify_status = '失败'
                        }
                        return this.activityAlertData
                    })

                    this.$emit('sendTotal', this.total)
                    this.loading = false
                })

            },
            // 获取列表数据相关方法
            // getTableData(obj) {
            //     rbAlertKanBanServiceFactory
            //     .getSearchAlertList(obj)
            //         .then(res => {
            //             this.activityAlertData = this.parseTableData(
            //             this.packData(res.result)
            //         )
            //             this.total = res.count

            //             this.$emit('sendTotal', this.total, this.currentPage, this.pageSize)
            //         })
            //     .finally(() => {
            //         this.loading = false
            //     })
            // },
            parseTableData(obj) {
                let list = []
                obj.forEach(item => {
                    let idcType = item.idc_type ? item.idc_type + ' ' : ''
                    let projectName = item.project_name ? item.project_name + ' ' : ''
                    let podName = item.pod_name ? item.pod_name : ''
                    let deviceClass = item.device_class ? item.device_class + '-' : ''
                    let deviceType = item.device_type ? item.device_type : ''
                    item.idc_type = idcType + projectName + podName
                    item.device_class = deviceClass + deviceType
                    list.push(item)
                })
                return list
            },
            // 封装得到的数据
            packData(arr) {
                // 列表数据封装
                if (arr.forEach) {
                    arr.forEach(item => {
                        //            item.order_type = item.order_status === '1' ? '' : '告警工单'
                        item.order_type = rbMirrorCommonService.common(
                            'ORDERTYPE',
                            '1',
                            item.order_type
                        )
                        item.order_status = rbMirrorCommonService.common(
                            'ORDERSTATUS',
                            '1',
                            item.order_status
                        )
                        item.object_type = rbMirrorCommonService.common(
                            'OBJECTTYPE',
                            '1',
                            item.object_type
                        )
                        item.report_status = rbMirrorCommonService.common(
                            'REPORTSTATUS',
                            '1',
                            item.report_status + ''
                        )
                        item.trans_status = rbMirrorCommonService.common(
                            'TRANSSTATUS',
                            '1',
                            item.trans_status
                        )
                        item.report_type = rbMirrorCommonService.common(
                            'REPORTTYPE',
                            '1',
                            item.report_type + ''
                        )

                        item.cur_moni_time = formatDate(item.cur_moni_time)
                        item.alert_start_time = formatDate(item.alert_start_time)
                        item.alert_end_time = formatDate(item.alert_end_time)
                        item.deliver_time = formatDate(item.deliver_time)
                        item.confirmed_time = formatDate(item.confirmed_time)
                        item.report_time = formatDate(item.report_time)
                        item.trans_time = formatDate(item.trans_time)
                        item.create_time = formatDate(item.create_time)
                        item.clear_time = formatDate(item.clear_time)
                    })
                    return arr
                } else {
                    // 详情数据封装
                    //          arr.order_type = arr.order_status === '1' ? '' : '告警工单'
                    arr.order_type = rbMirrorCommonService.common(
                        'ORDERTYPE',
                        '1',
                        arr.order_type
                    )
                    arr.order_status = rbMirrorCommonService.common(
                        'ORDERSTATUS',
                        '1',
                        arr.order_status
                    )
                    arr.object_type = rbMirrorCommonService.common(
                        'OBJECTTYPE',
                        '1',
                        arr.object_type
                    )
                    arr.report_status = rbMirrorCommonService.common(
                        'REPORTSTATUS',
                        '1',
                        arr.report_status + ''
                    )
                    arr.trans_status = rbMirrorCommonService.common(
                        'TRANSSTATUS',
                        '1',
                        arr.trans_status
                    )
                    arr.report_type = rbMirrorCommonService.common(
                        'REPORTTYPE',
                        '1',
                        arr.report_type + ''
                    )
                    arr.cur_moni_time = formatDate(arr.cur_moni_time)
                    arr.alert_start_time = formatDate(arr.alert_start_time)
                    arr.deliver_time = formatDate(arr.deliver_time)
                    arr.confirmed_time = formatDate(arr.confirmed_time)
                    arr.report_time = formatDate(arr.report_time)
                    arr.trans_time = formatDate(arr.trans_time)
                    return arr
                }
            },
            // 封装时间戳
            packTime(str) {
                return formatDate(str)
            },
            // 告警时长
            packTimePoint(num) {
                let date = new Date().getTime()
                var total = (date - num) / 1000
                var day = parseInt(total / (24 * 60 * 60)) //  计算整数天数
                var afterDay = total - day * 24 * 60 * 60 //  取得算出天数后剩余的秒数
                var hour = parseInt(afterDay / (60 * 60)) //  计算整数小时数
                var afterHour = total - day * 24 * 60 * 60 - hour * 60 * 60 //  取得算出小时数后剩余的秒数
                var min = parseInt(afterHour / 60) //  计算整数分
                var second = parseInt(
                    total - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
                ) //  取得算出分后剩余的秒数
                return `${day}d ${hour}h ${min}m ${second}s`
            },
            // 查询
            // searchData(num) {
            //     this.filterFlag = false
            //     if (num !== 1) {
            // // 搜索前将当前页置为1
            //         this.currentPage = 1
            //     }
            //     this.getTableData(this.queryDataParameter)
            //     if (this.search.searchDialog === true) {
            //         this.search.searchDialog = false
            //     }
            // },
            cancelSearch() {
                this.search.searchDialog = false
                this.search.alert_date_from = ''
                this.search.alert_date_to = ''
                this.search.cur_moni_time_from = ''
                this.search.cur_moni_time_to = ''
                this.search.alert_type = ''
                this.monitor_value = []
                this.search.alert_description = ''
                this.search.device_room = ''
                this.search.device_mfrs = ''
                this.search.device_model = ''
                this.search.host_name = ''
                this.search.noticeType = []
                this.search.notice_status = ''
                this.search.notice_date_from = ''
                this.search.notice_date_to = ''
                this.search.transfer_date_from = ''
                this.search.transfer_date_to = ''
                this.search.transfer_operator = ''
                this.search.confirm_operator = ''
                // this.search.alert_from = ''
                this.queryType = ''
                this.bizSysDepChecked = []
                this.bizDepTreeSelOpts = this.bizSysTree
            },
            // 重置
            reset() {
                // this.device_relation_value = ''
                // this.monitor_relation_value = ''
                this.device_device = ''
                this.search.device_class = ''
                this.alert_level_value = ''
                this.search.system = ''
                this.search.resource_pool = ''
                // this.queryType = ''
                this.search.alert_from = []
                this.cancelSearch()
            },
            // 转派
            choose() {
                this.transfer.dialogChoose = false
                let transferUser = []
                let username = []
                if (this.transfer.transferTableData) {
                    transferUser = this.transfer.transferTableData
                }
                let userUuids = []
                transferUser.forEach(item => {
                    userUuids.push(item.id)
                })
                let users = this.$refs.rbMirrorAlertUseList.multipleSelection
                users.forEach(item => {
                    let obj = {
                        id: item.uuid,
                        transfer_obj_type: '个人',
                        transfer_info: item.name,
                        transfer_user: item.code
                    }
                    if (userUuids.indexOf(item.uuid) === -1) {
                        transferUser.push(obj)
                    }
                })
                this.transfer.transferTableData = []
                transferUser.forEach(item => {
                    this.transfer.transferTableData.push(item)
                    username.push(item.transfer_info)
                })
                this.transfer.transferPersonalInput = username.toString()
            },
            deleteUser(id) {
                if (this.transfer.transferTableData.length > 0) {
                    for (let i = 0; i < this.transfer.transferTableData.length; i++) {
                        if (this.transfer.transferTableData[i].id === id) {
                            this.transfer.transferTableData.splice(i, 1)
                        }
                    }
                    let user = []
                    this.transfer.transferTableData.forEach(item => {
                        user.push(item.transfer_info)
                    })
                    this.transfer.transferPersonalInput = user.toString()
                }
            },
            transferDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行转派', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.transfer.transferDialog = true
                }
            },
            turnSend() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('告警列表已刷新，请重新选择告警', '警告', {
                        confirmButtonText: '确定'
                    })
                    this.transfer.transferDialog = false
                    return
                }
                if (!this.transfer.transferTeamChecked) {
                    this.$alert('请先确认是否已选择用户类型(个人)', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                const namespace = sessionStorage.getItem('namespace')
                let alertIds = ''
                let userNames = ''
                this.multipleSelection.forEach(item => {
                    alertIds += item.alert_id + ','
                })
                alertIds = alertIds.slice(0, -1)
                if (this.transfer.transferTableData.length < 1) {
                    this.$alert('请选择通知用户', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.transfer.transferTableData.forEach(item => {
                    userNames += item.transfer_user + ','
                })
                userNames = userNames.slice(0, -1)
                let obj = {
                    namespace: namespace,
                    alert_ids: alertIds,
                    user_names: userNames
                }
                rbAlertKanBanServiceFactory
                    .alertTransfer(obj)
                    .then(() => {
                        this.$message({
                            message: '转派成功',
                            type: 'success'
                        })
                    })
                    .catch(() => {
                        this.$message.error('转派失败')
                    })
                this.transfer.transferDialog = false
                this.cancel('transfer')
            },
            // // 待观察
            // alertObserve() {
            //     if (this.multipleSelection.length < 1) {
            //         this.$alert('请选择告警进行告警转待观察', '警告', {
            //             confirmButtonText: '确定'
            //         })
            //     } else {
            //         this.observe.observeDialog = true
            //     }
            // },
            // observeSubmit() {
            //     if (this.multipleSelection.length < 1) {
            //         this.$alert('告警列表已刷新，请重新选择告警', '警告', {
            //             confirmButtonText: '确定'
            //         })
            //         this.observe.observeDialog = false
            //         return
            //     }
            //     let str = ''
            //     if (this.detailObjId) {
            //         str = this.detailObjId
            //     } else {
            //         this.multipleSelection.forEach(item => {
            //             str += item.alert_id + ','
            //         })
            //         str = str.slice(0, -1)
            //     }
            //     let obj = {
            //         alertIds: str,
            //     }
            //     rbAlertKanBanServiceFactory
            //         .alertObserve(obj)
            //         .then(() => {
            //             this.$message({
            //                 message: '操作成功',
            //                 type: 'success'
            //             })
            //             this.detailObjId = ''
            //         })
            //         .catch(() => {
            //             this.$message.error('操作失败')
            //         })
            //     this.observe.observeDialog = false
            //     this.getTableData(this.queryDataParameter)
            // },
            // 确认
            confirmDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行确认', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.confirm.confirmDialog = true
                }
            },
            confirmElert() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('告警列表已刷新，请重新选择告警', '警告', {
                        confirmButtonText: '确定'
                    })
                    this.confirm.confirmDialog = false
                    return
                }
                const namespace = sessionStorage.getItem('namespace')
                let str = ''
                if (this.detailObjId) {
                    str = this.detailObjId
                } else {
                    this.multipleSelection.forEach(item => {
                        str += item.alert_id + ','
                    })
                    str = str.slice(0, -1)
                }
                if (!this.confirm.confirmDialogTextArea) {
                    this.$alert('告警确认原因不能为空,请填写!', '警告', {
                        confirmButtonText: '确定'
                    })
                    this.confirm.confirmDialog = false
                    return
                }
                let obj = {
                    namespace: namespace,
                    alert_ids: str,
                    content: this.confirm.confirmDialogTextArea,
                    auto_type: this.confirm.operation === 2 ? 1 : -1,
                    start_time:
                        this.confirm.operation === 2 ? this.confirm.dateTimeRange[0] : '',
                    end_time:
                        this.confirm.operation === 2 ? this.confirm.dateTimeRange[1] : ''
                }
                rbAlertKanBanServiceFactory
                    .alertConfirm(obj)
                    .then(() => {
                        this.$message({
                            message: '确认成功',
                            type: 'success'
                        })
                        this.detailObjId = ''
                    })
                    .catch(() => {
                        this.$message.error('确认失败')
                    })
                this.confirm.confirmDialog = false
                this.cancel('confirm')
                this.getTableData(this.queryDataParameter)
            },
            // 派单
            sendDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行派单', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.send.sendDialog = true
                }
            },
            sendbpm() {
                let bool = true
                if (bool) {
                    const namespace = sessionStorage.getItem('username')
                    let str = ''
                    if (this.detailObjId) {
                        str = this.detailObjId
                    } else {
                        this.multipleSelection.forEach(item => {
                            str += item.alert_id + ','
                        })
                        str = str.slice(0, -1)
                    }
                    let obj = {
                        namespace: namespace,
                        order_type: this.orderType,
                        alert_ids: str
                    }
                    rbAlertKanBanServiceFactory
                        .alertHandle(obj)
                        .then(res => {
                            let successNum = res.substring(8)
                            let message = ''
                            let type = ''
                            if (successNum != 0) {
                                message = '派单成功: ' + successNum
                                type = 'success'
                            } else {
                                message = '派单失败'
                                type = 'error'
                            }
                            this.$message({
                                message: message,
                                type: type
                            })
                            this.detailObjId = ''
                            this.detailOrderStatus = ''
                        })
                        .catch(() => {
                            this.$message.error('派单失败')
                        })
                } else {
                    this.$alert('只有未派单的告警才能派单', '警告', {
                        confirmButtonText: '确定'
                    })
                }
                this.send.sendDialog = false
                this.getTableData(this.queryDataParameter)
            },
            // 删除告警
            cleanDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行清除', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.clean.cleanDialogTextArea = ''
                    this.clean.cleanDialog = true
                }
            },
            goRemove(formName) {
                if (this.clean.operation === 2 && this.clean.dateTimeRange.length === 0) {
                    this.$alert('自动清除时间不能为空,请填写!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        let bool = true
                        if (bool) {
                            if (this.multipleSelection.length < 1) {
                                this.$alert('告警列表已刷新，请重新选择告警', '警告', {
                                    confirmButtonText: '确定'
                                })
                                this.clean.cleanDialog = false
                                return
                            }
                            const namespace = sessionStorage.getItem('namespace')
                            let str = ''
                            if (this.detailObjId) {
                                str = this.detailObjId
                            } else {
                                this.multipleSelection.forEach(item => {
                                    str += item.alert_id + ','
                                })
                                str = str.slice(0, -1)
                            }
                            let obj = {
                                namespace: namespace,
                                alert_ids: str,
                                content: this.clean.cleanDialogTextArea,
                                auto_type: this.clean.operation === 2 ? 2 : -1,
                                start_time:
                                    this.clean.operation === 2 ? this.clean.dateTimeRange[0] : '',
                                end_time:
                                    this.clean.operation === 2 ? this.clean.dateTimeRange[1] : ''
                            }
                            rbAlertKanBanServiceFactory
                                .deleteAlert(obj)
                                .then(res => {
                                    if (res) {
                                        this.$message({
                                            message: '删除成功',
                                            type: 'success'
                                        })
                                        this.getTableData(this.queryDataParameter)
                                    }
                                    this.detailObjId = ''
                                    this.detailOrderStatus = ''
                                })
                                .catch(() => {
                                    this.$message.error('删除失败')
                                })
                        } else {
                            this.$alert('工单状态中的告警不能清除', '警告', {
                                confirmButtonText: '确定'
                            })
                        }
                        this.clean.cleanDialog = false
                        this.cancel('clean')
                        this.getTableData(this.queryDataParameter)
                    } else {
                        return false
                    }
                })
            },
            // 告警通知
            noticeDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行通知', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.notice.noticeDialog = true
                    // this.getUserList()
                }
            },
            // 告警已通知通知
            notifyDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择告警进行操作', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let ids = ''
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    let item = this.multipleSelection[i]
                    ids += item.alert_id + ','
                }
                ids = ids.slice(0, -1)
                this.$confirm('是否标记为已通知状态?').then(() => {
                    this.rbHttp.sendRequest({
                        method: 'PUT',
                        data: { 'alertIds': ids },
                        url: '/v2/alerts/alert/notify/status'
                    }).then(() => {
                        this.getTableData(this.queryDataParameter)
                    })
                })
            },
            // 发送短信
            sendSMS() {
                let notifyUser = this.$refs.rbMirrorAlertUseList.multipleSelection
                if (notifyUser.length < 1) {
                    this.$alert('请选择通知用户', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length < 1) {
                    this.$alert('告警列表已刷新，请重新选择告警', '警告', {
                        confirmButtonText: '确定'
                    })
                    this.notice.noticeDialog = false
                } else {
                    this.$confirm('确认发送？').then(() => {
                        this.notice.noticeDialog = false
                        let mobiles = []
                        notifyUser.forEach(item => {
                            mobiles.push(item.mobile)
                        })
                        let alertIds = ''
                        this.multipleSelection.forEach(item => {
                            alertIds += item.alert_id + ','
                        })
                        alertIds = alertIds.slice(0, -1)
                        let obj = {
                            mobiles: mobiles,
                            alertIds: alertIds
                        }
                        // this.getUserList()
                        this.sms(obj)
                    })
                }
            },
            sms(obj) {
                rbProjectDataServiceFactory
                    .alertSMSNotify(obj)
                    .then(() => {
                        this.$message({
                            message: '发送成功',
                            type: 'success'
                        })
                    })
                    .catch(() => {
                        this.$message.error('发送失败')
                    })
            },
            // 发送邮件
            sendEmail() {
                let notifyUser = this.$refs.rbMirrorAlertUseList.multipleSelection
                if (notifyUser.length < 1) {
                    this.$alert('请选择通知用户', '警告', {
                        confirmButtonText: '确定'
                    })
                } else if (this.multipleSelection.length < 1) {
                    this.$alert('告警列表已刷新，请重新选择告警', '警告', {
                        confirmButtonText: '确定'
                    })
                    this.notice.noticeDialog = false
                } else {
                    this.$confirm('确认发送？').then(() => {
                        this.notice.noticeDialog = false
                        let emails = []
                        notifyUser.forEach(item => {
                            emails.push(item.mail)
                        })
                        let alertIds = ''
                        this.multipleSelection.forEach(item => {
                            alertIds += item.alert_id + ','
                        })
                        alertIds = alertIds.slice(0, -1)
                        let obj = {
                            emails: emails,
                            alertIds: alertIds
                        }
                        // this.getUserList()
                        this.email(obj)
                    })
                }
            },
            email(obj) {
                rbProjectDataServiceFactory
                    .alertNotify(obj)
                    .then(() => {
                        this.$message({
                            message: '发送成功',
                            type: 'success'
                        })
                    })
                    .catch(() => {
                        this.$message.error('发送失败')
                    })
            },
            // 锁屏
            lock() {
                clearInterval(this.timer)
                this.lockScgreen = false
                this.goScgreen = true
            },
            // 刷屏
            go() {
                this.timer = setInterval(() => {
                    if (this.filterFlag) {
                        this.searchFilterData(this.queryDataParameterFile)
                    } else {
                        this.getTableData(this.queryDataParameter)
                    }
                }, this.timer_frequency)
                this.goScgreen = false
                this.lockScgreen = true
            },

            //   新导出接口 /v2/alerts/alert/export  this.queryDatasList
            resourceExport() {
                this.loading = true
                this.exportDisabled = true
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: this.queryDatasList,
                    url: '/v2/alerts/alert/export'
                }).then((res) => {
                    if (res.code === '0000') {
                        this.loading = false
                        this.exportDisabled = false
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '当前告警数据列表.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.loading = false
                        this.exportDisabled = false
                        this.$message.error(res.message)
                    }
                })
            },

            // 选择列
            // 1.获取选择列表的初始值
            // getColumnFilter() {
            //     rbCmdbServiceFactory
            //     .getColumnFilter({ menuType: 'alert', moduleId: this.moduleId }).then(data => {
            //         this.filterForm = data
            //         if (data.columnInfo === null) {
            //             this.checkColumns.forEach(item => {
            //                 this.columnFilter[item.keyCode] = false
            //             })
            //         } else {
            //             this.columnFilter = JSON.parse(data.columnInfo)
            //         }
            //         this.queryFields = []
            //         this.test = []
            //         this.checkColumns.forEach(item => {
            //             if (this.columnFilter[item.keyCode]) {
            //                 item.keyIsSelected = true
            //                 this.test.push(item.keyCode)
            //                 this.queryFields.push(item)
            //             } else {
            //                 item.keyIsSelected = false
            //             }
            //         })
            //     })
            // },
            // 2.更新选择的变化
            handleCheckedColumnChange(val) {
                this.checkColumns.forEach(item => {
                    this.columnFilter[item.keyCode] = false
                })
                val.forEach(item => {
                    this.columnFilter[item] = true
                })
                let data = this.filterForm
                data.columnInfo = JSON.stringify(this.columnFilter)
                data.columnMap = this.columnFilter
                rbCmdbServiceFactory.updateColumnFilter(data).then().then(() => {
                    // this.getColumnFilter()
                    this.getTableData(this.queryDataParameter)
                })
            },
            // 操作
            // 查看告警详情
            goDetail(alert_id) {
                this.$router.push({
                    path: '/mirror_alert/alert_new/detail',
                    query: {
                        alert_id: alert_id
                    }
                })
            },
            // 派单单条告警
            sendOne(alert_id, order_status) {
                this.send.sendDialog = true
                this.detailObjId = alert_id
                this.detailOrderStatus = order_status
            },
            // 确认单条告警
            confirmElertOne(obj) {
                this.confirm.confirmDialog = true
                this.detailObjId = obj
            },
            // 删除单条告警
            goRemoveOne(alert_id, order_status) {
                this.clean.cleanDialog = true
                this.detailObjId = alert_id
                this.detailOrderStatus = order_status
            },
            cancel(obj) {
                if (obj === 'transfer') {
                    this.transfer.transferDialog = false
                    this.transfer.transferPersonalChecked = false
                    this.transfer.transferPersonalInput = ''
                    this.userList = []
                    this.userResult = []
                    this.transfer.transferTableData = []
                    // this.getUserList()
                } else if (obj === 'send') {
                    this.send.sendDialog = false
                    this.send.send_type = '0'
                } else if (obj === 'clean') {
                    this.clean.cleanDialog = false
                    this.clean.cleanDialogTextArea = ''
                    this.clean.operation = 1
                    this.clean.dateTimeRange = []
                } else if (obj === 'notice') {
                    this.notice.noticeDialog = false
                    this.userList = []
                    this.userResult = []
                } else if (obj === 'choose') {
                    this.transfer.dialogChoose = false
                    this.userList = []
                    this.userResult = []
                    // this.getUserList()
                } else if (obj === 'confirm') {
                    this.confirm.confirmDialog = false
                    this.confirm.confirmDialogTextArea = ''
                    this.confirm.operation = 1
                    this.confirm.dateTimeRange = []
                } else if (obj === 'voice') {
                    this.voiceNotifyDialog = false
                }
            },
            cancelHighSearch() {
                this.search.searchDialog = false
                this.search.alert_date_from = ''
                this.search.alert_date_to = ''
                this.search.cur_moni_time_from = ''
                this.search.cur_moni_time_to = ''
                this.search.alert_type = ''
                this.search.device_type = ''
                this.monitor_value = []
                this.search.system = ''
                this.search.alert_description = ''
                this.search.resource_pool = ''
                this.search.device_room = ''
                this.search.noticeType = []
                this.search.notice_status = ''
                this.search.notice_date_from = ''
                this.search.notice_date_to = ''
                this.search.transfer_date_from = ''
                this.search.transfer_date_to = ''
                this.search.transfer_operator = ''
                this.search.confirm_operator = ''
                this.queryType = ''
            },
            getTimerFrequency() {
                let obj = {
                    type: 'alert_timer_frequency'
                }
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    if (res) {
                        this.timer_frequency = res[0].value
                    }
                })
            },
            initParam() {
                if (this.alertType === 'activity') {
                    this.queryType = 0
                } else if (this.alertType === 'confirm') {
                    this.queryType = 1
                } else if (this.alertType === 'observe') {
                    this.queryType = 4
                }
            },
            init() {
                console.log(this.$route.query)
                let json = JSON.parse(this.$route.query.filter)
                let arr = [{
                    fieldName: 'operate_status', // 条件名称
                    fieldType: 'in', // 类型
                    fieldValue: '0,1,3' // 选中的条件 0-待确认,1-已确认,4-待观察
                }]
                for (let i = 0; i < json.length; i++) {
                    if (json[i].code == 'cur_moni_time') {
                        arr.push({
                            fieldName: json[i].code,
                            fieldType: 'datetime',
                            fieldValue: json[i].name
                        })
                    } else {
                        arr.push({
                            fieldName: json[i].code,
                            fieldType: 'in_and',
                            fieldValue: json[i].name
                        })
                    }

                }
                if (this.$route.query.filter) {
                    this.queryDataParameter.list = arr
                }
                this.initParam()
                this.alert_level = alert_level
                this.search.alert_types = alert_type
                this.relation = relation
                if (this.alertLevel_default) {
                    this.alert_level_value = this.alertLevel_default.toString()
                }
                // this.getColumnFilter()
                // this.getTableData(this.queryDataParameter)
                this.getTimerFrequency()
                if (this.timer) {
                    clearInterval(this.timer)
                } else {
                    this.timer = setInterval(() => {
                        this.getTableData(this.queryDataParameter)
                        this.getTimerFrequency()
                    }, this.timer_frequency)
                }
                // this.getMonitorValue()
                // this.getUserList()
                if (this.alertType === 'activity') {
                    this.voiceNotifyInit()
                }
            },
            // 自定义表头列宽
            flexColumnWidth(str) {
                let flexWidth = 150
                if (str === 'moni_index') {
                    flexWidth += 130
                } else if (str === 'alert_start_time') {
                    flexWidth += 35
                } else if (str === 'alert_end_time') {
                    flexWidth += 35
                }
                return flexWidth + 'px'
            },
            // 跳转告警详情页面==============================================================================================================
            dblhandleCurrentChange(column) {
                console.log(this.modelFieldDataList)
                // let idcParams = ''
                // if (column.idc_type.indexOf(' ') > -1) {
                //     console.log(1111)
                // } else {
                //     console.log(2222)
                // }
                this.$router.push({
                    path: '/mirror_alert/alert_new/detail',
                    query: {
                        alertId: column.alert_id,
                        deviceId: column.device_id,
                        keyComment: column.key_comment,
                        roomId: column.source_room,
                        detailType: 'alert',
                        // modeNameList: this.modelFieldDataList
                    }
                })
            },
            sortByAlertLevel(obj1, obj2) {
                let val1 = obj1.alert_level
                let val2 = obj2.alert_level
                return val1 - val2
            },
            alertVoiceNotifyDialog() {
                this.getAlertVoiceNotify()
                this.voiceNotifyDialog = true
            },
            getAlertVoiceNotify() {
                rbAlertVoiceNotifyServicesFactory.getAlertVoiceNotify().then(res => {
                    this.alertVoiceNotifyDetail = res
                })
            },
            validateAddRequest() {
                let addForm = this.$refs.rbMirrorAlertVoiceNotify.AlertVoiceNotifyForm
                let obj = {}
                obj.addForm = addForm
                obj.flag = true
                if (!addForm.alertFilterId) {
                    obj.flag = false
                    obj.msg = '请先选择告警筛选器!'
                    return obj
                } else if (!addForm.alertFilterSceneId) {
                    obj.flag = false
                    obj.msg = '请先选择告警场景!'
                    return obj
                } else if (!addForm.alertExistTime) {
                    obj.flag = false
                    obj.msg = '请先填写告警持续时长!'
                    return obj
                } else {
                    return obj
                }
            },
            getAlertVoiceNotifyRequest(obj) {
                let request = {}
                request.uuid = obj.uuid
                request.language = obj.language
                request.voiceContent = obj.voiceContent
                request.isOpen = parseInt(obj.isOpen)
                request.alertFilterId = parseInt(obj.alertFilterId)
                request.alertFilterSceneId = parseInt(obj.alertFilterSceneId)
                request.content = obj.content.toString()
                request.alertExistTime = parseInt(obj.alertExistTime)
                request.voiceAlertId = this.alertVoiceNotifyDetail.voiceAlertId
                return request
            },
            createVoiceNotify() {
                let obj = this.validateAddRequest()
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertVoiceNotifyServicesFactory
                    .createdAlertVoiceNotify(this.getAlertVoiceNotifyRequest(obj.addForm))
                    .then(res => {
                        let content = this.alertVoiceNotifyDetail.uuid ? '更新' : '创建'
                        if (res === 'success') {
                            this.$message({
                                message: content + '成功!',
                                type: 'success'
                            })
                            this.voiceNotifyDialog = false
                            window.speechSynthesis.cancel()
                            if (obj.addForm.isOpen === 1) {
                                this.voiceNotifyInit()
                            } else {
                                clearInterval(this.voiceTimer)
                            }
                        } else {
                            this.voiceNotifyDialog = false
                            this.$message.error(content + '失败!')
                        }
                    })
            },
            voiceNotify() {
                rbAlertVoiceNotifyServicesFactory.getAlertVoiceNotify().then(result => {
                    if (result && result.isOpen === 1) {
                        rbAlertVoiceNotifyServicesFactory
                            .getAlertVoiceNotifyContent(result)
                            .then(res => {
                                if (res && res !== 'error') {
                                    let msg = new SpeechSynthesisUtterance()
                                    msg.text = res
                                    window.speechSynthesis.speak(msg)
                                }
                            })
                    }
                })
            },
            voiceNotifyInit() {
                this.voiceNotify()
                if (!this.voiceTimer) {
                    this.voiceTimer = setInterval(() => {
                        this.voiceNotify()
                    }, 60 * 1000)
                }
            },
            gotoBPM: function (bpm_id) {
                const url = `${sessionStorage.getItem(
                    'X7_SERVER_URL'
                )}/front/#/inst/${bpm_id}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
            // 新增弹窗
            // 关闭弹窗
            closeDialog() {
                this.addDialog.dialogVisible = false
            },
            addRules() {
                // this.addDialog.id = ''
                if (this.addDialog.id.length < 1) {
                    this.$alert('请选择告警进行新增', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.addDialog.dialogVisible = true
                    this.addDialog.id = this.addDialog.id.join(',')
                }
            },
            // 演示
            demoClick(tab, event) { }
        },
        watch: {
            queryDatas: {
                handler(val) {
                    console.log(val)
                    this.queryDatasList = val
                    if (val.queryType != 3) {
                        this.queryDataParameterFile = {}
                        this.queryDataParameter = val
                        this.getTableData(val)
                    }
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            modelFieldData: {
                handler(val) {
                    this.modelFieldDataList = val
                }
            },
            queryDatasFile: {
                handler(valFile) {
                    this.queryDataParameterFile = valFile
                    if (valFile.condition) {
                        this.searchFilterData(valFile)
                    }
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            filterFlag: function () {
                clearInterval(this.timer)
                this.go()
            }
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .components-condition {
        padding-right: 250px;
    }
    .alertNumContent {
        display: inline-block;
        vertical-align: middle;
        margin-right: 10px;
        &:first-of-type {
            margin-left: 20px;
        }
        .total {
            display: inline-block;
            vertical-align: middle;
        }
    }

    .yw-form {
        /deep/ {
            .el-form__item {
                white-space: nowrap;
            }
        }
    }
</style>

<style>
    .ywTabBorder .el-table__body-wrapper {
        height: calc(100% - 29px) !important;
    }
    .ywTabBorder .el-table__fixed-body-wrapper {
        top: 29px !important;
        height: calc(100% -29px) !important;
    }

    .ywTab .el-tabs .el-tabs__header {
        width: 160px;
        margin: 0 auto;
    }
</style>

