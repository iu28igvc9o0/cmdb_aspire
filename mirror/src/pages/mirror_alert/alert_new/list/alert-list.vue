
<template>
    <div>
        <div class="alertListQyery">
            <div class="overview">
                <div class="overviewContent uop"
                     v-show="overviewShow">
                    <div style="overflow:hidden">
                        <div :class="{equipmentBrandClass:Condition}"
                             class="aLine FILE"
                             style="border:0;height: auto;">
                            <div class="content"
                                 style="display:flex;width:100%"
                                 :class="{activeFilter:Filter}">
                                <div class="name nameBorder"
                                     style="display:inherit;"
                                     :class="{nameBorderActive:nameBorderAttribute}">
                                    过滤器
                                    <svg @click="addFile"
                                         class="svg-icon svg-icon-24 addIcon"
                                         aria-hidden="true">
                                        <use xlink:href="#iconaddNew"></use>
                                    </svg>
                                </div>
                                <div style="flex:1;">
                                    <div style="overflow: hidden;width:100%;height:32px;border-bottom: 1px solid #DEE9FC;">
                                        <p @click="conditionClick(item.value,item.id,'过滤器')"
                                           :class='item.value==Filecondition1 ? "poolColor":""'
                                           v-for="(item,index) in dataList.conditionList"
                                           :key=index>
                                            <span>{{item.name}}</span>
                                        </p>
                                    </div>
                                    <div style="width:calc100% - 100px);position: relative;overflow:hidden;padding-right:100px;">
                                        <p @click="conditionClick2(item.sceneName,item.sceneId,'过滤器2',item.condition)"
                                           :class='item.value==FileconditionName?"poolColor":""'
                                           v-for="(item,index) in dataList.conditionList2"
                                           :key=index>
                                            <span>{{item.sceneName}}</span>
                                        </p>
                                    </div>
                                    <div v-if='ConditionB'
                                         v-show="Filter"
                                         class="BOTTON"
                                         style="position: absolute;right: 15px;top: 48px;">
                                        <el-button @click="MonitItemsBotton('过滤器2down')">更多<i class="el-icon-arrow-down"></i></el-button>
                                    </div>
                                    <div v-if='ConditionUP'
                                         class="BOTTON"
                                         style="position: absolute;right: 15px;top: 48px;">
                                        <el-button @click="MonitItemsBotton('过滤器2up')">收起<i class="el-icon-arrow-up"></i></el-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 动态生成的查询条件集合 -->
                        <div v-for="(itemA,indexA) in tileDatalist"
                             :key="indexA">
                            <div class="aLine halfALine"
                                 v-show="showDown || indexA < 5"
                                 v-if="itemA.type === 'like' || itemA.type === 'and' || itemA.type == null"
                                 :class="itemA.queryParamType === 'like' || itemA.queryParamType === 'and' || itemA.queryParamType == null">
                                <div class="content">
                                    <div class="name"
                                         style="display:inherit">
                                        {{itemA.name}}
                                    </div>
                                    <p class="elInput iconLeft">
                                        <el-input v-model="itemA.valueList"
                                                  :placeholder="itemA.name"
                                                  @clear="changeQueryIP(indexA)"
                                                  :clearable="true"
                                                  @change="inputChange(itemA.valueList,itemA.code,itemA.type,itemA,indexA)"></el-input>
                                        <i @click="deviceDeviceClick(itemA.valueList,itemA.code,itemA.type,itemA,indexA)"
                                           class="el-icon-search elInputIcon"></i>
                                    </p>
                                </div>
                            </div>
                            <!-- 时间框 -->
                            <div v-show="showDown || indexA < 5"
                                 class="aLine halfALine"
                                 v-if="itemA.type === 'date' || itemA.type === 'datetime'">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);">
                                    <div class="name"
                                         style="display:inherit">
                                        {{itemA.name}}
                                    </div>
                                    <p class="elInput">
                                        <el-date-picker v-model="itemA.valueList"
                                                        type="datetimerange"
                                                        range-separator="至"
                                                        :start-placeholder="itemA.name"
                                                        :end-placeholder="itemA.name"
                                                        value-format="yyyy-MM-dd HH:mm:ss"
                                                        @clear="clearQuery(indexA)"
                                                        @change="insertTimeClick('',itemA.valueList,itemA.code,itemA.type,indexA)">
                                        </el-date-picker>
                                    </p>
                                </div>
                            </div>
                            <!-- 业务系统树形结构 halfALine style="border-right: 1px solid #DEE9FC;" -->
                            <div v-show="showDown || indexA < 5"
                                 class="aLine halfALine"
                                 v-else-if="itemA.code === 'biz_sys' || itemA.type === 'tree_in'">
                                <div class="content"
                                     style="waith:50%">
                                    <div class="name"
                                         style="display:inherit">
                                        {{itemA.name}}
                                    </div>
                                    <p class="iconLeft treestyle">
                                        <el-select v-model="filterText"
                                                   @blur="blur"
                                                   :filter-method="filterSelect"
                                                   clearable
                                                   filterable
                                                   :placeholder="itemA.name"
                                                   collapse-tags
                                                   @change="selectChange"
                                                   @clear="refModuleIdClear(indexA)">
                                            <el-option :value="mineStatusValue"
                                                       style="height: auto">
                                                <el-tree :filter-node-method="filterNode"
                                                         :data="itemA.listData"
                                                         node-key="id"
                                                         ref="tree"
                                                         highlight-current
                                                         :props="defaultProps"
                                                         @check-change="handleCheckChange"
                                                         @node-click="handleNodeClick"></el-tree>
                                            </el-option>
                                        </el-select>
                                    </p>
                                </div>
                            </div>
                            <!-- 下拉框平铺数据 -->
                            <div v-show="showDown || indexA < 5"
                                 :class="{equipmentBrandClass:itemA.popdown,halfALine:itemA.listData.length<6?true:false}"
                                 class="aLine"
                                 v-else-if="(itemA.type === 'in' || itemA.type === 'in_and') && itemA.code !== 'device_mfrs'">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);">
                                    <div class="name"
                                         style="display:inherit;height:33px;border-solid:1px solid #DEE9FC">
                                        {{itemA.name}}
                                    </div>
                                    <div style="flex:1;">
                                        <!-- 资源池 -->
                                        <div v-if="itemA.code !== 'alert_level' && itemA.code !== 'object_type'">
                                            <p :class='item.value==itemA.valueList?"poolColor":""'
                                               @click="tiLingClick(item.value,indexA,itemA.code,itemA.type)"
                                               v-for="(item,index) in itemA.listData"
                                               :key=index>
                                                <span>{{ item.name }}</span>
                                            </p>
                                        </div>
                                        <div v-if="itemA.code === 'alert_level'">
                                            <p :class='item.name==itemA.valueList?"poolColor":""'
                                               @click="tiLingClick(item.value,indexA,itemA.code,itemA.type)"
                                               v-for="(item,index) in itemA.listData"
                                               :key=index>
                                                <span>{{ item.name }}</span>
                                            </p>
                                        </div>
                                        <div v-if="itemA.code === 'object_type'">
                                            <p :class='item.name==itemA.valueList?"poolColor":""'
                                               @click="tiLingClick(item.value,indexA,itemA.code,itemA.type)"
                                               v-for="(item,index) in itemA.listData"
                                               :key=index>
                                                <span>{{ item.name }}</span>
                                            </p>
                                        </div>
                                    </div>
                                    <div v-if='itemA.popup'
                                         style="float:right;margin: 4.5px 10px 0 0;">
                                        <el-button @click="tiLingBotton(indexA)">更多<i class="el-icon-arrow-down"></i></el-button>
                                    </div>
                                    <div v-if='itemA.popdown'
                                         style="float:right;margin: 4.5px 10px 0 0;">
                                        <el-button @click="tiLingBotton(indexA)">收起<i class="el-icon-arrow-up"></i></el-button>
                                    </div>
                                </div>
                            </div>
                            <!-- 设备厂商 -->
                            <div v-show="showDown || indexA < 5"
                                 v-if="itemA.code === 'device_mfrs'"
                                 :class="{equipmentBrandClass:equipmentBrand}"
                                 class="aLine brand">
                                <div class="content"
                                     style="display:flex;min-height:60px;width:calc(100% - 100px) !important;">
                                    <div class="name"
                                         style="height: inherit;min-height:59px;line-height:60px;">
                                        {{itemA.name}}
                                    </div>
                                    <div style="flex:1;margin-right:10px;margin-left:20px">
                                        <div class="iconimg"
                                             @click="Brand(item.produce,index)"
                                             v-for="(item,index) in brandList"
                                             :key=index>
                                            <div @mouseover="mouseOverImg(item.produce,index)"
                                                 @mouseleave="mouseLeaveImg(item.produce)"
                                                 class="iconimgP"
                                                 v-if=item.logoUrl>
                                                <div v-if="item.produce==queryForm.deviceMfrs || imgIndex === index || ImgSelected === index"
                                                     class="iconimgP poolColors">{{item.produce}}</div>
                                                <img v-else
                                                     class="imgLogo"
                                                     :src="item.logoUrl" />
                                            </div>
                                            <div @mouseover="mouseOver(item.produce,index)"
                                                 @mouseleave="mouseLeave(item.produce)"
                                                 :class='(item.produce==queryForm.deviceMfrs || imgIndex === index || ImgSelected === index)?"poolColors":""'
                                                 class="iconimgP"
                                                 v-else>{{item.produce}}</div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if='equipmentBrandB'
                                     style="float:right;margin: 17px 10px 0 0;">
                                    <el-button @click="equipmentBrandBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='equipmentBrandBUP'
                                     style="float:right;margin: 17px 10px 0 0;">
                                    <el-button @click="equipmentBrandBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="querybotton">
                <div class="showcCondition">
                    <div v-if="queryBotton">
                        <p v-if="queryBottonS"
                           @click='QueryBottonExhibition("down")'>
                            <i class="el-icon-arrow-down"></i>更多查询选项（业务系统，告警来源，告警内容等）
                        </p>
                        <p v-else
                           @click='QueryBottonExhibition("downS")'>
                            <i class="el-icon-arrow-down"></i>点击显示查询选项
                        </p>
                    </div>
                    <div v-else>
                        <p @click='QueryBottonExhibition("up")'>
                            <i class="el-icon-arrow-up"></i>点击收起更多查询选项
                        </p>
                    </div>
                </div>
                <div class="showcCondition">
                    <p v-if="queryBottonA"
                       @click='QueryBottonHide("down")'
                       class="botton">展开全部查询选项</p>
                    <p v-if="queryBottonA1"
                       @click='QueryBottonHide("up")'
                       class="botton">隐藏全部查询选项</p>
                </div>
            </div>
        </div>
        <div class="selected"
             v-show="selectedShow">
            <div class="selectedTotal FILE">共找到<span>&nbsp;{{sendTotal}}&nbsp;</span>条数据,已选</div>
            <div class="selectedTotal HISquery">共找到<span>&nbsp;{{sendTotalHIS}}&nbsp;</span>条数据,已选</div>
            <div id="querySelector">
                <!-- 循环生成的以选中条件 -->
                <div class="public"
                     v-for="(item,index) in tileDatalist"
                     :key="index"
                     v-show="item.allSelected">
                    <span class="publicName">&nbsp;&nbsp;{{item.name}}：&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{item.valueList}}</span>
                    <span class="publicIcon">&nbsp;<i @click="deletechange(index,item.code)"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publiccondition">
                    <span class="publicName">&nbsp;&nbsp;过滤器:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{FileconditionName}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('过滤器2')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>

                <div class="public"
                     v-show="publicBrand">
                    <span class="publicName">&nbsp;&nbsp;设备厂商:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.deviceMfrs}}</span>
                    <span class="publicIcon">&nbsp;<i @click="BrandDelete('设备厂商')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicbizSys">
                    <span class="publicName">&nbsp;&nbsp;业务系统:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{filterText}}</span>
                    <span class="publicIcon">&nbsp;<i @click="bizSysDelete('业务系统')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>

                <div class="public selectedDelete">
                    <el-button @click="empty()"
                               icon="el-icon-delete">清空选项</el-button>
                </div>
            </div>
        </div>

        <div class="alertListTable">
            <!-- :queryDatas='queryForm' -->
            <AlertTable :queryDatas='queryFormNew'
                        :modelFieldData='listShowList'
                        :queryDatasFile='queryFormFile'
                        :alertType='alertType'
                        @sendTotal='formSendTotal' />
        </div>
    </div>
</template>

<script>

    // import {alert_level, alert_type, relation, alert_from, alert_notice_types, notice_status, scavenging_type} from '../list/config/config.js'
    import QueryObject from 'src/utils/queryObject.js'
    import AlertTable from '../list/alert-table'
    export default {
        mixins: [QueryObject],
        props: ['alertType', 'sendTotalHis', 'sendcurrentPagelHis', 'sendPageSizeHis'],
        name: 'MirrorAlertAlertList',
        components: {
            AlertTable
        },
        data() {
            return {
                mineStatusValue: '',
                filterText: '',
                modelFieldDataS: [], // 模型字段接口数据
                modelFieldDataInput: [], // 模型字段接口数据 input框
                modelFieldDataSelect: [], // 模型字段接口数据 select 下拉框
                modelFieldDataDate: [], // 模型字段接口数据 时间框

                tileDatalist: [], // 模型字段接口数据
                listShowList: [], // 列表头部是否展示

                bizSysList: [],

                selected: -1,
                selected1: -1,

                moduleIds: '',
                defaultProps: { // 业务系统树形下拉框
                    children: 'subList',
                    label: 'name',
                    id: 'id'
                },

                sendTotal: 0,
                sendTotalHIS: 0,

                ImgSelected: '',
                imgIndex: '',
                Filter: false,

                nameBorderAttribute: false,
                BizSysdepartmen1: false,
                BizSysdepartmen2: false,

                MonitItems: false,
                MonitItemsB: true,
                MonitItemsUP: false,
                SourceRoom: false,
                SourceRoomB: true,
                SourceRoomUP: false,
                DeviceType: false,
                DeviceTypeB: true,
                DeviceTypeUP: false,
                Source: false,
                SourceB: false,
                SourceUP: false,
                BizSys: false,
                BizSysB: true,
                BizSysUP: false,
                Condition: false,
                ConditionB: false,
                ConditionUP: false,

                factory: false,
                factoryB: true,
                factoryUP: false,
                status: false,
                deviceStatusB: true,
                deviceStatusUP: false,
                equipmentBrand: false,
                equipmentBrandB: true,
                equipmentBrandBUP: false,
                queryBotton: true,
                queryBottonA: false,
                queryBottonS: true,
                queryBottonA1: true,
                showDown: false,
                overviewShow: true,

                // 点击条件生成的框
                selectedShow: false,
                publiccondition: false,
                publicBrand: false,
                publicPool: false,
                publicDeviceClass: false,
                publicDeviceDevice: false,
                publicdevice_model: false,
                publicnotice_status: false,
                publicalertLevel: false,
                publicbizSys: false,
                publicsource: false,
                publicdeviceType: false,
                publicmonitItems: false,
                publicalarmBeginTime: false,
                publicalarmCurrentTime: false,
                publicTransferTime: false,
                publicoperationTime: false,
                publicsourceRoom: false,
                publicnotifyType: false,
                publictoConfirmStaff: false,
                publichostName: false,
                publicmonitDesc: false,
                publicalert_type: false,

                publicscavenging_type: false, // 历史告警清除类型
                publicdeliverHIS: false,
                publicdeliverTimeHIS: false,
                publicclearTimeHIS: false,

                operationTime: [], // 通知操作时间

                clearTimeHIS: [], // 历史告警--------清除操作时间时间
                deliverTimeHIS: [], // 历史告警--------清除操作时间时间

                FileconditionName: '',
                Filecondition1: '',
                queryFormFile: {
                    condition: '', // 过滤器二级
                    page_no: 1, // 页码
                    page_size: 50 // 每页数量
                },
                transferStaff: '',
                toConfirmStaff: '',
                deviceModel: '',
                hostName: '',
                monitDesc: '',
                bizSys1: '',
                bizSys0: '',
                alertLevelS: '',
                isClearHIS: '', // 历史告警-----清除类型
                deliverHIS: '', // 历史告警-----派单人员
                objectTypeS: '',
                notifyTypeS: '',
                notifyStatusS: '',
                queryFormNew: { // 动态列表接口
                    page_no: 1,
                    page_size: 50,
                    sort_name: '',
                    list: [
                        {
                            fieldName: 'operate_status', // 条件名称
                            fieldType: 'and', // 类型
                            fieldValue: '0' // 选中的条件 0-待确认,1-已确认,4-待观察
                        }
                    ]
                },

                queryForm: {
                    deviceMfrs: '', // 设备厂商
                    deviceModel: '', // 设备型号
                    deviceType: '', // 设备类型
                    hostName: '', // 主机名称
                    idcType: '', // 资源池
                    instanceId: '', // 设备实例号
                    monitDesc: '', // 告警描述
                    monitItems: '', // 监控对象，多个以逗号分隔
                    notifyStatus: '', // 通知状态
                    notifyTimeRangeEnd: '', // 通知操作时间
                    notifyTimeRangeStart: '', // 通知操作时间
                    notifyType: '', // 通知方式，多个逗号分隔
                    objectType: '', // 告警分类
                    operateStatus: '', // 操作状态
                    order: 'DESC', // 排序方式，默认desc
                    queryType: 0, // 查询类型，待确认-0，已确认-1
                    page_no: 1, // 页码
                    page_size: 50, // 每页数量

                    sort_name: '', // 排序字段
                    source: '', // 告警来源
                    sourceRoom: '', // 机房
                    toConfirmStaff: '', // 待确认人
                    transferStaff: '', // 转派操作人
                    transferTimeRangeEnd: '', // 转派时间
                    transferTimeRangeStart: '', // 转派时间

                    // 历史告警 比当前告警多的几个条件 //
                    isClear: '',  // 清除类型
                    deliverTimeRangeStart: '', // 派单时间
                    deliverTimeRangeEnd: '', // 派单时间
                    deliver: '', // 派单人员
                    clearTimeRangeStart: '', // 清除时间
                    clearTimeRangeEnd: '' // 清除时间
                },
                // 时间不能选择超过今天的时间
                pickerMinDate: '',
                pickerOptions: {
                    onPick: ({ maxDate, minDate }) => {
                        this.pickerMinDate = minDate.getTime()
                        if (maxDate) {
                            this.pickerMinDate = ''
                        }
                    },
                    disabledDate: (time) => {
                        if (this.pickerMinDate !== '') {
                            console.log('')
                        }
                        return time.getTime() > Date.now()
                    }
                },
                dataList: {
                    conditionList: [], // 过滤器
                    conditionList2: [], // 过滤器

                    poolList: [], // 资源池 2            idc_type
                    brandList: [], // 设备厂商 4          device_mfrs
                    deviceClassList: [], // 设备分类 3     device_class
                    alertLevelList: [], // 告警级别 1     alert_level
                    bizSysList: [], // 业务系统 5         biz_sys
                    bizSysList1: [], // 业务系统
                    bizSysList2: [], // 业务系统
                    sourceList: [], // 告警来源 6        source
                    deviceTypeList: [], // 设备类型 7     device_type
                    monitItemsList: [], // 监控对象 10    item_key ？？？
                    sourceRoomList: [], // 机房位置 9    source_room
                    notifyTypeList: [], // 通知方式 11    不清楚
                    noticeStatusList: [], // 通知状态 12  不清楚
                    objectTypeList: [], // 告警分类（告警类型）8   object_type
                    scavenging_typeList: [] // {只有历史告警有}清除类型

                }
            }
        },
        mounted() {
            this.initDict()
            this.fillSelectOption()
            this.setQueryParams()
        },
        methods: {
            setQueryParams() {
                let query = this.$route.query
                console.log(query)
                // 在这里添加
                if (query.kgalert) {
                    let queryObj = query.parentParams
                    for (let i in queryObj) {
                        this.queryFormNew.list.push({
                            fieldName: i,
                            fieldType: 'in_and',
                            fieldValue: queryObj[i]
                        })
                    }
                }
                if (query.biz_sys) {
                    this.queryFormNew.list.push({
                        fieldName: 'biz_sys',
                        fieldType: 'tree_in',
                        fieldValue: query.biz_sys,
                    })
                    this.$refs.tree.filter(query.biz_sys)
                }
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            filterSelect(value) {
                this.$refs.tree[0].filter(value)
            },
            blur() {
                this.$refs.tree[0].filter('')
            },
            // 过滤器条件和其他条件冲突不能共存
            FILE() {
                this.publiccondition = false
                this.Condition = false
                this.Filecondition1 = ''
                this.Filter = false
                this.nameBorderAttribute = false
                this.ConditionB = false
                this.ConditionUP = false
                this.queryFormFile.condition = ''
                this.queryFormFile.operationStatus = ''
            },
            // input框change事件
            deviceDeviceClick(value, code, type, item, index) {

                this.queryFormNew.page_no = 1
                this.FILE()

                this.selectedShow = true
                this.tileDatalist[index].valueList = value
                this.tileDatalist[index].allSelected = true

                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== code
                })
                this.queryFormNew.list = objTiLing

                if (value) {
                    this.queryFormNew.list.push({
                        fieldName: code,
                        fieldType: type,
                        fieldValue: value,
                    })
                } else {
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== code
                    })
                    this.queryFormNew.list = objTiLing
                    this.tileDatalist[index].allSelected = false
                    this.Eliminate()
                }
            },
            inputChange(value, code, type, item, index) {

                if (!value) {
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== code
                    })
                    this.queryFormNew.list = objTiLing
                    this.tileDatalist[index].allSelected = false
                    this.Eliminate()
                }
            },
            tiLingClick(value, index, code, type) {

                // this.FILE()
                this.queryFormNew.page_no = 1
                if (code === 'alert_level' && value == '') {
                    this.tileDatalist[index].valueList = '全部'
                } else if (code === 'alert_level' && value == 2) {
                    this.tileDatalist[index].valueList = '低'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else if (code === 'alert_level' && value == 3) {
                    this.tileDatalist[index].valueList = '中'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else if (code === 'alert_level' && value == 4) {
                    this.tileDatalist[index].valueList = '高'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else if (code === 'alert_level' && value == 5) {
                    this.tileDatalist[index].valueList = '重大'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else if (code === 'object_type' && value == '') {
                    this.tileDatalist[index].valueList = '全部'
                } else if (code === 'object_type' && value == 1) {
                    this.tileDatalist[index].valueList = '设备'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else if (code === 'object_type' && value == 2) {
                    this.tileDatalist[index].valueList = '业务系统'
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                } else {
                    this.tileDatalist[index].valueList = value
                    this.selectedShow = true
                    this.tileDatalist[index].allSelected = true
                }


                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== code
                })
                this.queryFormNew.list = objTiLing
                if (value) {
                    this.queryFormNew.list.push({
                        fieldName: code,
                        fieldType: type,
                        fieldValue: value,
                    })
                } else {
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== code
                    })
                    this.queryFormNew.list = objTiLing
                    this.tileDatalist[index].allSelected = false
                    this.Eliminate()
                }
            },
            // 时间框框
            insertTimeClick(data, value, code, type, index) {

                this.FILE()

                this.queryFormNew.page_no = 1
                this.tileDatalist[index].valueList = value
                this.selectedShow = true
                this.tileDatalist[index].allSelected = true

                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== code
                })
                this.queryFormNew.list = objTiLing

                if (value) {
                    this.queryFormNew.list.push({
                        fieldName: code,
                        fieldType: type,
                        fieldValue: value.toString(),
                    })
                } else {

                    this.tileDatalist[index].allSelected = false
                    this.EliminateTime()
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== code
                    })
                    this.queryFormNew.list = objTiLing
                }
            },
            refModuleIdClear() {
                this.publicbizSys = false
                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'biz_sys'
                })
                this.queryFormNew.list = objTiLing
                this.Eliminate()
            },
            handleNodeClick(e) {
                this.FILE()
                this.queryFormNew.page_no = 1
                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'biz_sys'
                })
                this.queryFormNew.list = objTiLing

                this.filterText = e.name
                if (e.type === 'bizSystem') {
                    this.queryFormNew.list.push({
                        fieldName: 'biz_sys',
                        fieldType: 'tree_in',
                        fieldValue: e.name,
                    })
                    this.selectedShow = true
                    this.publicbizSys = true
                    this.filterText = e.name
                }
            },
            changeQuerydeviceModel() {
                this.publicdevice_model = false
                this.Eliminate()
            },
            clearQuery(index) {
                this.tileDatalist[index].allSelected = false
                this.EliminateTime()
            },
            changeQueryIP(index) {
                this.tileDatalist[index].allSelected = false
                this.Eliminate()
            },
            // 跳转到过滤器详情的页面
            addFile() {
                this.$router.push({ path: '/mirror_alert/filter/list/' })
            },
            // 过滤器更多收起按钮
            MonitItemsBotton(data) {
                if (data === '过滤器2down') {
                    this.Condition = true
                    this.ConditionB = false
                    this.ConditionUP = true
                } else if (data === '过滤器2up') {
                    this.Condition = false
                    this.ConditionB = true
                    this.ConditionUP = false
                }
            },
            queryFormFileClear() {
                this.queryFormFile.condition = ''
                this.queryFormFile.operationStatus = ''
                this.FileconditionName = ''
                this.Filecondition1 = ''
                this.Filter = false
            },
            deletechange(index, code) {
                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== code
                })
                this.queryFormNew.list = objTiLing
                if (code === 'alert_level') {
                    this.tileDatalist[index].valueList = '全部'
                } else if (code === 'object_type') {
                    this.tileDatalist[index].valueList = '全部'
                } else {
                    this.tileDatalist[index].valueList = ''
                }
                this.tileDatalist[index].allSelected = false
                this.Eliminate()
            },
            empty() {
                for (let item in this.tileDatalist) {
                    this.tileDatalist[item].valueList = ''
                    this.tileDatalist[item].allSelected = false
                    if (this.tileDatalist[item].code === 'alert_level') {
                        this.tileDatalist[item].valueList = '全部'
                    } else if (this.tileDatalist[item].code === 'object_type') {
                        this.tileDatalist[item].valueList = '全部'
                    }
                }
                this.queryFormNew = {}
                this.queryFormNew = { // 动态列表接口
                    page_no: 1,
                    page_size: 50,
                    sort_name: '',
                    list: [
                        {
                            fieldName: 'operate_status', // 条件名称
                            fieldType: 'and', // 类型
                            fieldValue: this.getOperationStatus() // 选中的条件 0-待确认,1-已确认
                        }
                    ]
                }

                for (let item in this.queryForm) {
                    this.queryForm[item] = ''
                    this.ImgSelected = ''
                    this.imgIndex = ''
                }

                this.nameBorderAttribute = false
                this.transferStaff = ''
                this.toConfirmStaff = ''
                this.deviceModel = ''
                this.hostName = ''
                this.monitDesc = ''
                this.bizSys0 = ''
                this.bizSys1 = ''
                this.alertLevelS = ''
                this.objectTypeS = ''
                this.notifyTypeS = ''
                this.isClearHIS = '' // 历史告警------清除类型
                this.deliverHIS = ''// 历史告警-------派单人员
                this.clearTimeHIS = [] // 历史告警--------清除操作时间时间
                this.deliverTimeHIS = [] // 历史告警--------清除操作时间时间

                this.queryForm.page_no = 1
                this.queryForm.page_size = 50

                this.queryForm.order = 'DESC' // 排序方式，默认desc
                this.queryForm.queryType = '0' // 查询类型，待确认-0，已确认-1
                this.FileconditionName = ''
                this.Filecondition1 = ''
                this.selectedShow = false
                this.Filter = false

                this.selectedShow = false
                this.publicPool = false
                this.publicDeviceClass = false
                this.publicBrand = false
                this.publicDeviceDevice = false
                this.publicdevice_model = false
                this.publicnotice_status = false
                this.publiccondition = false
                this.publicalertLevel = false
                this.publicbizSys = false
                this.publicsource = false
                this.publicdeviceType = false
                this.publicmonitItems = false
                this.publicalarmBeginTime = false
                this.publicalarmCurrentTime = false
                this.publicTransferTime = false
                this.publicsourceRoom = false
                this.publicnotifyType = false
                this.publictoConfirmStaff = false
                this.publichostName = false
                this.publicalert_type = false
                this.publictransferStaff = false
                this.publicoperationTime = false

                this.publicscavenging_type = false // 历史告警清除类型
                this.publicdeliverHIS = false
            },
            Eliminate() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 370) {
                    this.selectedShow = false
                }
            },
            EliminateTime() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 600) {
                    this.selectedShow = false
                }
            },
            formSendTotal(val1, val2, val3) {
                this.sendTotal = val1
                if (this.sendTotal > 50) {
                    this.queryForm.page_no = val2
                    this.queryForm.page_size = val3
                } else {
                    this.queryForm.page_no = 1
                    this.queryForm.page_size = val3
                }
            },
            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [{ id: '', name: '全部', parentName: '', produce: '全部', type: selectType, value: '', pid: '' }]
                if (options) {
                    options.forEach((item) => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },
            fillSelectOption1(options, selectType) {
                const newOptions = [{ id: '', name: '暂不选择', sceneName: '暂不选择', type: selectType, value: '', pid: '' }]
                if (options) {
                    options.forEach((item) => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },

            // 将获取到的字典值转化为可识别的json对象
            convertDictConfig(dict) {
                return {
                    id: dict.dictId,
                    type: dict.colName,
                    name: dict.dictCode,
                    value: dict.dictNote,
                    pid: dict.upDict
                }
            },
            // 初始化
            initDict() {
                let _this = this
                // 获取模型字段接口
                _this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v2/alerts/model/field/listByTableName/alert_alerts'
                }).then((res) => {
                    // 列表头部展示数据
                    let objList = res.filter((item) => {
                        return item.isListShow == 1
                    })
                    _this.listShowList = objList
                    // 过滤不需要的数据
                    let obj = res.filter((item) => {
                        return item.isQueryParam == 1
                    })
                    this.modelFieldDataS = obj.sort(function (a, b) {
                        return a.queryParamSort - b.queryParamSort
                    })

                    _this.tileDatalist = []
                    let promiseList = []
                    this.modelFieldDataS.forEach(fieldCodeType => {
                        let OBJ = {}
                        OBJ.name = fieldCodeType.fieldName
                        OBJ.queryParamName = fieldCodeType.queryParamName
                        OBJ.code = fieldCodeType.fieldCode
                        OBJ.type = fieldCodeType.queryParamType
                        OBJ.popdown = false
                        OBJ.allSelected = false
                        OBJ.popup = false
                        OBJ.valueList = ''
                        OBJ.queryParamSort = fieldCodeType.queryParamSort
                        OBJ.listData = []
                        if (fieldCodeType.fieldCode === 'device_mfrs') {
                            // 获取设备品牌
                            let promiseItem = _this.rbHttp.sendRequest({
                                method: 'POST',
                                params: { 'type': '生产供应商', 'orderBy': 'produce' },
                                url: '/v1/cmdb/maintenProduce/listProduceByPage'
                            }).then((res) => {
                                _this.brandList = res.data
                                OBJ.listData = res.data.map((item) => {
                                    return { 'name': item.produce, 'value': item.produce }
                                })
                                return res
                            })
                            promiseList.push(promiseItem)
                        } else if (fieldCodeType.queryParamSource) {
                            if (fieldCodeType.queryParamSource.startsWith('[')) {
                                OBJ.listData = JSON.parse(fieldCodeType.queryParamSource)
                            } else {
                                let promiseItem = _this.rbHttp.sendRequest({
                                    method: 'GET',
                                    url: fieldCodeType.queryParamSource ? fieldCodeType.queryParamSource : '/v1/cmdb/configDict/getDictsByType?type=alert_level'
                                }).then((resData) => {
                                    OBJ.listData = fieldCodeType.queryParamType !== 'tree_in' ? _this.fillSelectOption(resData, fieldCodeType.fieldCode) : resData
                                    if (fieldCodeType.fieldCode === 'alert_level') {
                                        OBJ.valueList = '全部'
                                    }
                                    return resData
                                })
                                promiseList.push(promiseItem)
                            }
                        }
                        setTimeout(() => {
                            this.tileDatalist.push(OBJ)
                            this.tileDatalist.forEach(itemB => {
                                itemB.popup = itemB.listData.length > 8 ? true : false
                                if (itemB.code === 'object_type') {
                                    OBJ.valueList = '全部'
                                } else if (itemB.code === 'source_room') {
                                    OBJ.valueList = ''
                                } else if (itemB.code === 'moni_object') {
                                    OBJ.valueList = ''
                                }
                            })
                        }, 500)

                    })

                    // 初始选中赋值
                    Promise.all(promiseList).then(() => {
                        // url参数
                        let filter = JSON.parse(this.$route.query.filter)

                        filter.forEach((filterItem) => {

                            let index = 0
                            this.tileDatalist.some((item, itemIndex) => {
                                if (item.code === filterItem.code) {
                                    index = itemIndex
                                    item.listData.some((subItem) => {
                                        if (subItem.name === filterItem.name) {
                                            return true
                                        }
                                    })
                                    return true
                                }
                            })

                            this.tiLingClick(filterItem.name, index, filterItem.code, filterItem.type || 'in_and')
                        })

                    })



                })
                // 获取过滤器/v1/alerts/alerts_filter/selectAll?filterFlag=true
                _this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'filterFlag': 'true' },
                    url: '/v1/alerts/alerts_filter/selectAll'
                }).then((res) => {
                    let newarrey = []
                    for (let item in res) {
                        newarrey.push(
                            { value: res[item].name, id: res[item].id, name: res[item].name }
                        )
                    }
                    _this.dataList.conditionList = _this.fillSelectOption1(newarrey, 'idcType')
                })
            },
            // 选择过滤器场景GET /v1/alerts/alerts_filter/filter/{filter_id}
            conditionClick(item, id) {
                if (item === '暂不选择' || item === '') {
                    this.Condition = false
                    this.Filecondition1 = item
                    this.Filter = false
                    this.nameBorderAttribute = false
                    this.ConditionB = false
                    this.ConditionUP = false

                    this.publiccondition = false
                    this.FileconditionName = ''
                    this.Eliminate()
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== 'operate_status'
                    })
                    if (objTiLing && objTiLing.length > 0) {
                        this.queryFormNew.list = objTiLing
                    } else {
                        let newArr = [
                            {
                                fieldName: 'operate_status', // 条件名称
                                fieldType: 'in', // 类型
                                fieldValue: this.getOperationStatus() // 选中的条件 0-待确认,1-已确认
                            }
                        ]
                        this.queryFormNew.list = newArr
                    }

                    this.queryFormNew.filter_scene_id = ''




                } else {
                    this.Filecondition1 = item
                    this.Filter = true
                    this.nameBorderAttribute = true
                    // this.tileDatalist.forEach((itemF) => { // 点击过滤器其余的任何查询条件都不成立
                    //     itemF.valueList = ''
                    //     itemF.allSelected = false
                    // })
                    this.filterText = ''
                    this.publicbizSys = false

                    // this.queryFormNew = { // 动态列表接口
                    //     page_no: 1,
                    //     page_size: 50,
                    //     filter_scene_id: id,
                    //     sort_name: '',
                    //     list: [
                    //         {
                    //             fieldName: 'operate_status', // 条件名称
                    //             fieldType: 'and', // 类型
                    //             fieldValue: this.getOperationStatus() // 选中的条件 0-待确认,1-已确认
                    //         }
                    //     ]
                    // }

                    this.FileconditionName = '' //  业务系统
                    this.publiccondition = false
                    this.queryForm.deviceMfrs = '' // 设备厂商
                    this.publicBrand = false
                    this.imgIndex = ''
                    this.ImgSelected = ''
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        url: '/v1/alerts/alerts_filter/filter/' + id
                    }).then((res) => {
                        let newArrey = []
                        for (let item in res) {
                            newArrey.push({
                                sceneName: res[item].sceneName,
                                count: res[item].count,
                                sceneId: res[item].sceneId,
                                value: res[item].sceneName,
                                condition: res[item].condition
                            })
                        }
                        this.dataList.conditionList2 = this.fillSelectOption1(newArrey, 'filterFlag')
                        if (this.dataList.conditionList2.length * 90 >= document.body.offsetWidth - 200) {
                            this.ConditionB = true
                            this.ConditionUP = false
                        } else {
                            this.ConditionB = false
                            this.ConditionUP = false
                        }
                        this.Eliminate()
                    })
                }

            },
            getOperationStatus() {
                let operationStatus = ''
                if (this.alertType == 'activity') {
                    operationStatus = '0'
                } else if (this.alertType == 'observe') {
                    operationStatus = '4'
                } else if (this.alertType == 'confirm') {
                    operationStatus = '1'
                } else if (this.alertType == 'biz_alert') {
                    operationStatus = '5'
                } else if (this.alertType == 'exception') {
                    operationStatus = '6'
                }
                return operationStatus
            },
            // 过滤器2
            conditionClick2(item, id, name, condition) {
                if (item === '暂不选择') {
                    this.publiccondition = false
                    this.FileconditionName = ''
                    this.Eliminate()
                } else {
                    this.FileconditionName = item
                    this.queryFormFile.condition = condition
                    this.queryFormFile.operationStatus = this.getOperationStatus()
                    this.selectedShow = true
                    this.publiccondition = true

                    this.queryFormNew = { // 动态列表接口
                        page_no: 1,
                        page_size: 50,
                        filter_scene_id: id,
                        sort_name: '',
                        list: [
                            {
                                fieldName: 'operate_status', // 条件名称
                                fieldType: 'in', // 类型
                                fieldValue: this.getOperationStatus() // 选中的条件 0-待确认,1-已确认
                            }
                        ]
                    }

                }
            },
            // 历史告警--------清除类型
            scavenging_typeClick(item, id) {
                if (item === '全部') {
                    this.publicscavenging_type = false
                    this.queryForm.isClear = ''
                    this.isClearHIS = ''
                    this.Eliminate()
                } else {
                    this.isClearHIS = item
                    this.queryForm.isClear = id.id
                    this.selectedShow = true
                    this.publicscavenging_type = true
                }
            },
            // 设备厂商
            Brand(item, index) {
                this.FILE()
                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'device_mfrs'
                })
                this.queryFormNew.list = objTiLing

                if (item) {
                    this.queryFormNew.list.push({
                        fieldName: 'device_mfrs',
                        fieldType: 'in_and',
                        fieldValue: item,
                    })
                } else {
                    let objTiLing = this.queryFormNew.list.filter((item) => {
                        return item.fieldName !== 'device_mfrs'
                    })
                    this.queryFormNew.list = objTiLing
                }
                this.ImgSelected = index
                this.inputBrand = item
                this.queryForm.deviceMfrs = item
                this.publicBrand = true
                this.selectedShow = true
            },
            mouseOver(item, index) {
                this.imgIndex = index
            },
            mouseLeave() {
                this.imgIndex = ''
            },
            mouseOverImg(item, index) {
                this.imgIndex = index
            },
            mouseLeaveImg() {
                this.imgIndex = ''
            },

            equipmentBrandBotton() {
                this.equipmentBrand = !this.equipmentBrand
                this.equipmentBrandB = !this.equipmentBrandB
                this.equipmentBrandBUP = !this.equipmentBrandBUP
            },
            tiLingBotton(index) {
                // this.equipmentBrand = !this.equipmentBrand
                this.tileDatalist[index].popup = !this.tileDatalist[index].popup
                this.tileDatalist[index].popdown = !this.tileDatalist[index].popdown
            },
            // 设备品牌清除小按钮
            BrandDelete() {

                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'device_mfrs'
                })
                this.queryFormNew.list = objTiLing

                this.publicBrand = false
                this.queryForm.deviceMfrs = ''
                this.ImgSelected = ''
                this.imgIndex = ''
                this.Eliminate()
            },

            bizSysDelete() {

                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'biz_sys'
                })
                this.queryFormNew.list = objTiLing

                this.publicbizSys = false
                this.bizSys0 = ''
                this.bizSys1 = ''
                this.filterText = ''
                this.Eliminate()
            },
            changeQueryDelete(data) {
                let objTiLing = this.queryFormNew.list.filter((item) => {
                    return item.fieldName !== 'operate_status'
                })
                if (objTiLing && objTiLing.length > 0) {
                    this.queryFormNew.list = objTiLing
                } else {
                    let newArr = [
                        {
                            fieldName: 'operate_status', // 条件名称
                            fieldType: 'in', // 类型
                            fieldValue: this.getOperationStatus() // 选中的条件 0-待确认,1-已确认
                        }
                    ]
                    this.queryFormNew.list = newArr
                }

                this.queryFormNew.filter_scene_id = ''

                if (data === '过滤器2') {
                    this.publiccondition = false
                    this.FileconditionName = ''
                    this.Filecondition1 = ''
                    this.Filter = false
                }

                this.Eliminate()
            },
            // 隐藏展开更多选项
            QueryBottonExhibition(data) {
                this.queryBottonS = true
                this.queryBotton = !this.queryBotton
                if (data === 'down') {
                    this.queryBottonA = false
                    this.queryBottonA1 = true
                    this.overviewShow = true
                    this.showDown = true
                } if (data === 'downS') {
                    this.queryBotton = true
                    this.queryBottonA = false
                    this.queryBottonA1 = true
                    this.overviewShow = true
                    this.showDown = false
                } else if (data === 'up') {
                    this.queryBottonA = false
                    this.queryBottonA1 = true
                    this.overviewShow = true
                    this.showDown = false
                }
            },
            QueryBottonHide(data) {
                this.queryBottonS = false
                this.queryBottonA = false
                this.queryBottonA1 = true
                if (data === 'down') {
                    this.queryBotton = false
                    this.overviewShow = true
                    this.showDown = true
                } else if (data === 'up') {
                    this.queryBottonA1 = false
                    this.queryBotton = true
                    this.overviewShow = false
                    this.showDown = false
                }
            }

        },

        computed: {
            queryFormHis() {
                return this.queryForm
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val)
            },
            alertType: {
                handler(val) {
                    if (val === 'activity') {
                        this.queryForm.queryType = '0'
                        this.queryFormNew.list[0].fieldValue = '0'
                    } else if (val === 'confirm') {
                        this.queryForm.queryType = '1'
                        this.queryFormNew.list[0].fieldValue = '1'
                    } else if (val === 'observe') {
                        this.queryForm.queryType = '4'
                        this.queryFormNew.list[0].fieldValue = '4'
                    } else if (val === 'biz_alert') {
                        this.queryForm.queryType = '5'
                        this.queryFormNew.list[0].fieldValue = '5'
                    } else if (val === 'exception') {
                        this.queryForm.queryType = '6'
                        this.queryFormNew.list[0].fieldValue = '6'
                    }
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            queryFormHis: {
                handler(val) {
                    this.$emit('queryDatasHis', val)
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            sendTotalHis: {
                handler(val) {
                    this.sendTotalHIS = val
                },
                deep: true // 深度监听
            },
            sendcurrentPagelHis: {
                handler(val) {
                    this.queryForm.page_no = val
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            },
            sendPageSizeHis: {
                handler(val) {
                    this.queryForm.page_size = val
                },
                immediate: true, // 初始化传值
                deep: true // 深度监听
            }
        }

    }
</script>

<style lang="scss" scoped>
    @import "../list/alert.scss";
    .activeFilter {
        height: 65px !important;
        border-bottom: 1px solid #dee9fc;
        width: 100% !important;
    }
    .selected {
        position: relative;
        overflow: hidden;
        display: flex;
        width: 98%;
        margin-top: 10px;
        .selectedTotal {
            float: left;
            text-align: right;
            margin-right: 10px;
            color: #999999;
            min-width: 130px;
            line-height: 35px;
        }
        .public {
            display: inline-block;
            min-width: 100px;
            margin: 5px;
            height: 25px;
            border: 1px solid #a4aaae;
            line-height: 25px;
            border-radius: 2px;
            .publicName {
                color: #666666;
            }
            .publicContent {
                color: #2089da;
            }
            .publicIcon {
                cursor: pointer;
                &:hover {
                    color: #278bdc;
                }
            }
        }
        .querySelectorActive {
            color: #278bdc;
        }
        .selectedDelete {
            border: none;
            margin: 6px 0 0 30px;
        }
    }
    .HISquery {
        display: none;
    }
    .halfALine {
        width: 50% !important;
        float: left;
        border-left: 1px solid #dee9fc;
    }
    .aLine {
        width: 100%;
        border-left: 1px solid #dee9fc;
    }
    .content {
        width: 100% !important;
    }
</style>
<style lang="scss">
    .el-input-group__append {
        color: #6f7ea2;
        vertical-align: middle;
        display: table-cell;
        position: relative;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        padding: 0px 10px;
        width: 1px;
        white-space: nowrap;
        top: 3px;
    }
    .iconLeft .el-input__suffix {
        margin-right: 10px;
    }
    button.el-button.el-button--default:hover {
        border: 1px solid #2089da;
    }

    button.el-button.el-button--default {
        border: 1px solid #2089da;
        padding: 5px 15px;
    }
    .treestyle div.el-select {
        width: 260px;
    }
    .treestyle .el-tree {
        margin-left: 100px;
    }
    .yw-form .clearfix {
        padding-bottom: 10px;
    }
</style>

