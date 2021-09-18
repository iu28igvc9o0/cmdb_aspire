/* eslint-disable no-dupe-keys */
<template>
    <div>
        <div class="alertListQyery">
            <div class="overview">
                <div class="overviewContent uop"
                     v-show="overviewShow">
                    <div style="overflow:hidden">
                        <div :class="{equipmentBrandClass:Condition}"
                             class="aLine FILE"
                             style="border:0">
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
                                           :class='item.value==Filecondition1?"poolColor":""'
                                           v-for="(item,index) in dataList.conditionList"
                                           :key=index>
                                            <span>{{item.name}}</span>
                                        </p>
                                    </div>
                                    <div style="width:calc100% - 100px);position: relative;overflow:hidden;padding-right:100px;">
                                        <p @click="conditionClick2(item.sceneName,item.id,'过滤器2',item.condition)"
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
                        <div :class="{equipmentBrandClass:pool}"
                             class="aLine aLinePublic">
                            <div class="content">
                                <div class="name"
                                     style="">
                                    设备IP
                                </div>
                                <div>
                                    <p class="elInput iconLeft">
                                        <el-input v-model="deviceIp"
                                                  placeholder="请输入设备IP查询"
                                                  @clear="changeQueryIP"
                                                  :clearable="true"></el-input>
                                        <i @click="deviceDeviceClick(deviceIp,'设备IP')"
                                           class="el-icon-search elInputIcon"></i>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="aLine aLinePublic">
                            <div class="content"
                                 style="display:flex;margin-top:-1px;">
                                <div class="name"
                                     style="display:inherit;border-left: 1px solid #DEE9FC;"
                                     :class="{NAMEBOrder:nameBorderAttribute}">
                                    告警级别
                                </div>
                                <div style="flex:1;">
                                    <p :class='item.value==alertLevelS?"poolColor":""'
                                       @click="alertLevelClick(item.label,item.id)"
                                       v-for="(item,index) in dataList.alertLevelList"
                                       :key=index>
                                        <span>{{ item.label }}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="content"
                             style="display:flex;width:calc(100% - 100px);;">
                            <div class="name"
                                 style="display:inherit">
                                资源池
                            </div>
                            <div style="flex:1;">
                                <p :class='item.value==queryForm.idcType?"poolColor":""'
                                   @click="poolClick(item.value,item.id,'资源池')"
                                   v-for="(item,index) in dataList.poolList"
                                   :key=index>
                                    <span>{{ item.name }}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="content">
                            <div class="name">
                                设备分类
                            </div>
                            <p :class='item.value==queryForm.deviceClass?"poolColor":""'
                               @click="deviceClassClick(item.value,item.id,'设备分类')"
                               v-for="(item,index) in dataList.deviceClassList"
                               :key=index>
                                <span>{{ item.name }} </span>
                            </p>
                        </div>
                    </div>
                    <div :class="{equipmentBrandClass:equipmentBrand}"
                         class="aLine brand">
                        <div class="content"
                             style="display:flex;min-height:60px;">
                            <div class="name"
                                 style="height: inherit;min-height:59px;line-height:60px;">
                                设备品牌
                            </div>
                            <div class="brandListFalst"
                                 style="flex:1;margin-right:10px;margin-left:20px">
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
                <div id="overviewContentID"
                     class="overviewContent"
                     v-if="showDown"
                     style="margin-top:-10px;border-top:none">
                    <div class="aLine"
                         :class="{equipmentBrandClass:BizSys}">
                        <div class="content"
                             style="position: relative;display:flex;width:100%;">
                            <div class="name"
                                 style="display:inherit">
                                业务系统
                            </div>
                            <div style="flex:1;">
                                <div style="overflow: hidden;padding-right:100px;"
                                     :class="{nameBorder:BizSysdepartmen1}">
                                    <p :class='item.value==bizSys0?"poolColor":""'
                                       @click="bizSysClick(item.name,item.id,'业务系统')"
                                       v-for="(item,index) in dataList.bizSysList"
                                       :key=index>
                                        <span>{{ item.name }}</span>
                                    </p>
                                </div>
                                <div v-show="BizSys"
                                     style="overflow: hidden;"
                                     :class="{nameBorder:BizSysdepartmen2}">
                                    <p :class='item.value==bizSys1?"poolColor":""'
                                       @click="bizSysClick1(item.name,item.id,'业务系统')"
                                       v-for="(item,index) in dataList.bizSysList1"
                                       :key=index>
                                        <span>{{ item.name }}</span>
                                    </p>
                                </div>
                                <div v-show="BizSys"
                                     style="overflow: hidden;">
                                    <p :class='item.value==queryForm.bizSys?"poolColor":""'
                                       @click="bizSysClick2(item.name,item.id,'业务系统')"
                                       v-for="(item,index) in dataList.bizSysList2"
                                       :key=index>
                                        <span>{{ item.name }}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div v-if='BizSysB'
                             class="BOTTON BOTTONBIZ"
                             style="position: absolute;right:15px">
                            <el-button @click="MonitItemsBotton('业务系统down')">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div v-if='BizSysUP'
                             class="BOTTON BOTTONBIZ"
                             style="position: absolute;right:15px">
                            <el-button @click="MonitItemsBotton('业务系统up')">收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="aLine"
                         :class="{equipmentBrandClass:Source}">
                        <div class="content"
                             style="display:flex;">
                            <div class="name"
                                 style="display:inherit">
                                告警来源
                            </div>
                            <div style="flex:1;">
                                <p :class='item.value==queryForm.source?"poolColor":""'
                                   @click="sourceClick(item.name,item.id,'告警来源')"
                                   v-for="(item,index) in dataList.sourceList"
                                   :key=index>
                                    <span>{{ item.name }}</span>
                                </p>
                            </div>
                        </div>
                        <div v-if='SourceB'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('告警来源down')">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div v-if='SourceUP'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('告警来源up')">收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="aLine"
                         :class="{equipmentBrandClass:DeviceType}">
                        <div class="content"
                             style="display:flex;">
                            <div class="name"
                                 style="display:inherit">
                                设备类型
                            </div>
                            <div style="flex:1;">
                                <p :class='item.value==queryForm.deviceType?"poolColor":""'
                                   @click="deviceTypeClick(item.name,item.id,'设备类型')"
                                   v-for="(item,index) in dataList.deviceTypeList"
                                   :key=index>
                                    <span>{{ item.name }}</span>
                                </p>
                            </div>
                        </div>
                        <div v-if='DeviceTypeB'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('设备类型down')">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div v-if='DeviceTypeUP'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('设备类型up')">收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="aLineLeft">
                            <div class="content"
                                 style="width:100%">
                                <div class="name">
                                    告警内容
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="monitDesc"
                                              placeholder="请输入告警内容查询"
                                              @clear="clearQuery('告警内容')"
                                              :clearable="true"></el-input>
                                    <i @click="queryFormClick(monitDesc, '告警内容')"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
                            </div>
                        </div>
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    告警类型
                                </div>
                                <p :class='item.value==objectTypeS?"poolColor":""'
                                   @click="alert_typeClick(item.label,item.id,'告警类型')"
                                   v-for="(item,index) in dataList.objectTypeList"
                                   :key=index>
                                    <span>{{ item.label }}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name">
                                    告警开始时间
                                </div>
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="alarmCurrentTime"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery"
                                                    @change="insertTimeClick('告警开始时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    当前告警时间
                                </div>
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="alarmBeginTime"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery"
                                                    @change="insertTimeClick('当前告警时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine"
                         :class="{equipmentBrandClass:SourceRoom}">
                        <div class="content"
                             style="display:flex;">
                            <div class="name"
                                 style="display:inherit">
                                机房位置
                            </div>
                            <div style="flex:1;">
                                <p :class='item.value==queryForm.sourceRoom?"poolColor":""'
                                   @click="sourceRoomClick(item.name,item.id,'机房位置')"
                                   v-for="(item,index) in dataList.sourceRoomList"
                                   :key=index>
                                    <span>{{ item.name }}</span>
                                </p>
                            </div>
                        </div>
                        <div v-if='SourceRoomB'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('机房位置down')">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div v-if='SourceRoomUP'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('机房位置up')">收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    设备型号
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="deviceModel"
                                              placeholder="请输入设备型号查询"
                                              @clear="changeQuerydeviceModel"
                                              :clearable="true"></el-input>
                                    <i @click="device_modelClick(deviceModel)"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    主机名称
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="hostName"
                                              placeholder="请输入主机名称查询"
                                              @clear="clearQuery('主机名称')"
                                              :clearable="true"></el-input>
                                    <i @click="queryFormClick(hostName, '主机名称')"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine"
                         :class="{equipmentBrandClass:MonitItems}">
                        <div class="content"
                             style="display:flex;">
                            <div class="name"
                                 style="display:inherit">
                                监控对象
                            </div>
                            <div style="flex:1;">
                                <p :class='item.value==queryForm.monitItems?"poolColor":""'
                                   @click="monitItemsClick(item.name,item.id,'监控对象')"
                                   v-for="(item,index) in dataList.monitItemsList"
                                   :key=index>
                                    <span>{{ item.name }}</span>
                                </p>
                            </div>
                        </div>
                        <div v-if='MonitItemsB'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('监控对象down')">更多<i class="el-icon-arrow-down"></i></el-button>
                        </div>
                        <div v-if='MonitItemsUP'
                             class="BOTTON">
                            <el-button @click="MonitItemsBotton('监控对象up')">收起<i class="el-icon-arrow-up"></i></el-button>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    通知方式
                                </div>
                                <div style="">
                                    <p :class='item.value==notifyTypeS?"poolColor":""'
                                       @click="notifyTypeClick(item.label,item,id)"
                                       v-for="(item,index) in dataList.notifyTypeList"
                                       :key=index>
                                        <span>{{ item.label }}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    通知状态
                                </div>
                                <div style="">
                                    <p :class='item.value==notifyStatusS?"poolColor":""'
                                       @click="noticeStatusClick(item.label,item.id,'通知状态')"
                                       v-for="(item,index) in dataList.noticeStatusList"
                                       :key=index>
                                        <span>{{ item.label }}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="aLine">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    通知操作时间
                                </div>
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="operationTime"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery"
                                                    @change="insertTimeClick('通知操作时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    转派时间
                                </div><!-- daterange -->
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="TransferTime"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery('转派时间')"
                                                    @change="insertTimeClick('转派时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine FILE">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    转派操作人
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="transferStaff"
                                              placeholder="请输入转派操作人查询"
                                              @clear="clearQuery('转派操作人')"
                                              :clearable="true"></el-input>
                                    <i @click="queryFormClick(transferStaff, '转派操作人')"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    待确认人
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="toConfirmStaff"
                                              placeholder="请输入待确认人查询"
                                              @clear="clearQuery('待确认人')"
                                              :clearable="true"></el-input>
                                    <i @click="queryFormClick(toConfirmStaff, '待确认人')"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine HISquery">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    派单操作时间
                                </div>
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="deliverTimeHIS"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery"
                                                    @change="insertTimeClick('派单操作时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    清除操作时间
                                </div><!-- daterange -->
                                <p class="elInput"
                                   style="margin-top:2px">
                                    <el-date-picker v-model="clearTimeHIS"
                                                    type="datetimerange"
                                                    :picker-options="pickerOptions"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间"
                                                    value-format="yyyy-MM-dd HH:mm:ss"
                                                    @clear="clearQuery('清除操作时间')"
                                                    @change="insertTimeClick('清除操作时间')">
                                    </el-date-picker>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="aLine HISquery">
                        <div class="aLineLeft">
                            <div class="content">
                                <div class="name">
                                    清除类型
                                </div>
                                <p :class='item.value==isClearHIS?"poolColor":""'
                                   @click="scavenging_typeClick(item.label,item,id)"
                                   v-for="(item,index) in dataList.scavenging_typeList"
                                   :key=index>
                                    <span>{{ item.label }}</span>
                                </p>
                            </div>
                        </div>
                        <div class="aLineRight">
                            <div class="content">
                                <div class="name"
                                     style="border-left: 1px solid #DEE9FC;">
                                    派单人员
                                </div>
                                <p class="elInput iconLeft">
                                    <el-input v-model="deliverHIS"
                                              placeholder="请输入派单人员查询"
                                              @clear="clearQuery('派单人员')"
                                              :clearable="true"></el-input>
                                    <i @click="queryFormClick(deliverHIS, '派单人员')"
                                       class="el-icon-search elInputIcon"></i>
                                </p>
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
                <div class="public"
                     v-show="publiccondition">
                    <span class="publicName">&nbsp;&nbsp;过滤器:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{FileconditionName}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('过滤器2')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicDeviceDevice">
                    <span class="publicName">&nbsp;&nbsp;设备IP:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{deviceIp}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('设备IP')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicPool">
                    <span class="publicName">&nbsp;&nbsp;资源池:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.idcType}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('资源池')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicDeviceClass">
                    <span class="publicName">&nbsp;&nbsp;设备分类:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.deviceClass}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('设备分类')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicBrand">
                    <span class="publicName">&nbsp;&nbsp;设备品牌:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.deviceMfrs}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('设备品牌')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicnotice_status">
                    <span class="publicName">&nbsp;&nbsp;通知状态:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{notifyStatusS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('通知状态')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicdevice_model">
                    <span class="publicName">&nbsp;&nbsp;设备型号:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{deviceModel}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('设备型号')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicalertLevel">
                    <span class="publicName">&nbsp;&nbsp;告警级别:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{alertLevelS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('告警级别')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicbizSys">
                    <span class="publicName">&nbsp;&nbsp;业务系统:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.bizSys}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('业务系统')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicsource">
                    <span class="publicName">&nbsp;&nbsp;告警来源:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.source}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('告警来源')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicdeviceType">
                    <span class="publicName">&nbsp;&nbsp;设备类型:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.deviceType}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('设备类型')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicmonitItems">
                    <span class="publicName">&nbsp;&nbsp;监控对象:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.monitItems}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('监控对象')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicalarmBeginTime">
                    <span class="publicName">&nbsp;&nbsp;告警开始时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{alarmCurrentTime?alarmCurrentTime.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('告警开始时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicalarmCurrentTime">
                    <span class="publicName">&nbsp;&nbsp;当前告警时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{alarmBeginTime?alarmBeginTime.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('当前告警时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicoperationTime">
                    <span class="publicName">&nbsp;&nbsp;通知操作时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{operationTime?operationTime.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('通知操作时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicTransferTime">
                    <span class="publicName">&nbsp;&nbsp;转派时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{TransferTime?TransferTime.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('转派时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicsourceRoom">
                    <span class="publicName">&nbsp;&nbsp;机房位置:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{queryForm.sourceRoom}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('机房位置')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicnotifyType">
                    <span class="publicName">&nbsp;&nbsp;通知方式:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{notifyTypeS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('通知方式')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publictoConfirmStaff">
                    <span class="publicName">&nbsp;&nbsp;待确认人:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{toConfirmStaff}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('待确认人')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publictransferStaff">
                    <span class="publicName">&nbsp;&nbsp;转派操作人:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{transferStaff}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('转派操作人')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publichostName">
                    <span class="publicName">&nbsp;&nbsp;主机名称:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{hostName}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('主机名称')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicmonitDesc">
                    <span class="publicName">&nbsp;&nbsp;告警内容:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{monitDesc}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('告警内容')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicalert_type">
                    <span class="publicName">&nbsp;&nbsp;告警类型:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{objectTypeS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('告警类型')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <!-- 历史告警-----清除类型 -->
                <div class="public"
                     v-show="publicscavenging_type">
                    <span class="publicName">&nbsp;&nbsp;清除类型:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{isClearHIS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('清除类型')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicdeliverHIS">
                    <span class="publicName">&nbsp;&nbsp;派单人员:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{deliverHIS}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('派单人员')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicdeliverTimeHIS">
                    <span class="publicName">&nbsp;&nbsp;派单操作时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{deliverTimeHIS?deliverTimeHIS.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('派单操作时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>
                <div class="public"
                     v-show="publicclearTimeHIS">
                    <span class="publicName">&nbsp;&nbsp;清除操作时间:&nbsp;&nbsp;&nbsp;</span>
                    <span class="publicContent">{{clearTimeHIS?clearTimeHIS.toString():''}}</span>
                    <span class="publicIcon">&nbsp;<i @click="changeQueryDelete('清除操作时间')"
                           class="el-icon-close"></i>&nbsp;</span>
                </div>

                <div class="public selectedDelete">
                    <el-button @click="empty()"
                               icon="el-icon-delete">清空选项</el-button>
                </div>
            </div>
        </div>

        <div class="alertListTable">
            <AlertTable :queryDatas='queryForm'
                        :queryDatasFile='queryFormFile'
                        :alertType='alertType'
                        @sendTotal='formSendTotal' />
        </div>
    </div>
</template>

<script>

    import { alert_level, alert_type, alert_notice_types, notice_status, scavenging_type } from '../../alert_his/config/options.js'
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
                publicPool: false,
                publicDeviceClass: false,
                publicBrand: false,
                publicDeviceDevice: false,
                publicdevice_model: false,
                publicnotice_status: false,
                publiccondition: false,
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

                alarmBeginTime: [], // 当前告警时间
                alarmCurrentTime: [], // 告警开始时间
                operationTime: [], // 通知操作时间
                TransferTime: [], // 转派时间

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
                deviceIp: '',
                bizSys1: '',
                bizSys0: '',
                alertLevelS: '',
                isClearHIS: '', // 历史告警-----清除类型
                deliverHIS: '', // 历史告警-----派单人员
                objectTypeS: '',
                notifyTypeS: '',
                notifyStatusS: '',
                queryForm: {
                    alertCreateTimeRangeEnd: '', // 告警生效时间
                    alertCreateTimeRangeStart: '', // 告警生效时间
                    alertLevel: '', // 告警级别
                    bizSys: '', // 业务线，多个逗号分隔
                    curMoniTimeFrom: '', // 当前监控时间
                    curMoniTimeTo: '', // 当前监控时间
                    deviceClass: '', // 设备分类
                    deviceIp: '', // 设备IP
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
                        // if (this.pickerMinDate !== '') {
                        // }
                        return time.getTime() > Date.now()
                    }
                },
                dataList: {
                    conditionList: [], // 过滤器
                    conditionList2: [], // 过滤器
                    poolList: [], // 资源池
                    brandList: [], // 设备品牌
                    deviceClassList: [], // 设备分类
                    alertLevelList: [], // 告警级别
                    bizSysList: [], // 业务系统
                    bizSysList1: [], // 业务系统
                    bizSysList2: [], // 业务系统
                    sourceList: [], // 告警来源
                    deviceTypeList: [], // 设备类型
                    monitItemsList: [], // 监控对象
                    sourceRoomList: [], // 机房位置
                    notifyTypeList: [], // 通知方式
                    noticeStatusList: [], // 通知状态
                    objectTypeList: [], // 告警分类（告警类型）

                    scavenging_typeList: [] // 历史告警清除类型

                }
            }
        },
        mounted() {
            this.initDict()
            this.fillSelectOption()
        },
        methods: {
            changeQuerydeviceModel() {
                this.publicdevice_model = false
                this.Eliminate()
            },
            changeQueryIP() {
                this.queryForm.deviceIp = ''
                this.publicDeviceDevice = false
                this.Eliminate()
            },
            addFile() {
                this.$router.push({ path: '/mirror_alert/filter/list/' })
            },
            MonitItemsBotton(data) {
                if (data === '监控对象down') {
                    this.MonitItems = true
                    this.MonitItemsB = false
                    this.MonitItemsUP = true
                } else if (data === '监控对象up') {
                    this.MonitItems = false
                    this.MonitItemsB = true
                    this.MonitItemsUP = false
                } else if (data === '机房位置down') {
                    this.SourceRoom = true
                    this.SourceRoomB = false
                    this.SourceRoomUP = true
                } else if (data === '机房位置up') {
                    this.SourceRoom = false
                    this.SourceRoomB = true
                    this.SourceRoomUP = false
                } else if (data === '设备类型down') {
                    this.DeviceType = true
                    this.DeviceTypeB = false
                    this.DeviceTypeUP = true
                } else if (data === '设备类型up') {
                    this.DeviceType = false
                    this.DeviceTypeB = true
                    this.DeviceTypeUP = false
                } else if (data === '告警来源down') {
                    this.Source = true
                    this.SourceB = false
                    this.SourceUP = true
                } else if (data === '告警来源up') {
                    this.Source = false
                    this.SourceB = true
                    this.SourceUP = false
                } else if (data === '业务系统down') {
                    this.BizSys = true
                    this.BizSysB = false
                    this.BizSysUP = true
                } else if (data === '业务系统up') {
                    this.BizSys = false
                    this.BizSysB = true
                    this.BizSysUP = false
                } else if (data === '过滤器2down') {
                    this.Condition = true
                    this.ConditionB = false
                    this.ConditionUP = true
                } else if (data === '过滤器2up') {
                    this.Condition = false
                    this.ConditionB = true
                    this.ConditionUP = false
                }
            },
            insertTimeClick(data) {
                if (data === '当前告警时间') {
                    if (this.alarmBeginTime) {
                        this.queryForm.curMoniTimeFrom = this.alarmBeginTime[0]  // 告警生效时间
                        this.queryForm.curMoniTimeTo = this.alarmBeginTime[1] // 告警生效时间
                        this.publicalarmCurrentTime = true
                    } else {
                        this.EliminateTime()
                        this.selectedShow = false
                        this.queryForm.curMoniTimeFrom = ''  // 告警生效时间
                        this.queryForm.curMoniTimeTo = '' // 告警生效时间
                        this.publicalarmCurrentTime = false
                    }
                    this.selectedShow = true
                } else if (data === '告警开始时间') {
                    if (this.alarmCurrentTime) {
                        this.queryForm.alertCreateTimeRangeStart = this.alarmCurrentTime[0]  // 告警生效时间
                        this.queryForm.alertCreateTimeRangeEnd = this.alarmCurrentTime[1] // 告警生效时间
                        this.publicalarmBeginTime = true
                    } else {
                        this.EliminateTime()
                        this.queryForm.alertCreateTimeRangeStart = ''  // 告警生效时间
                        this.queryForm.alertCreateTimeRangeEnd = '' // 告警生效时间
                        this.publicalarmBeginTime = false
                    }
                    this.selectedShow = true
                } else if (data === '通知操作时间') {
                    if (this.operationTime) {
                        this.queryForm.notifyTimeRangeStart = this.operationTime[0]  // 告警生效时间
                        this.queryForm.notifyTimeRangeEnd = this.operationTime[1] // 告警生效时间
                        this.publicoperationTime = true
                    } else {
                        this.EliminateTime()
                        this.queryForm.notifyTimeRangeStart = ''  // 告警生效时间
                        this.queryForm.notifyTimeRangeEnd = '' // 告警生效时间
                        this.publicoperationTime = false
                    }
                    this.selectedShow = true
                } else if (data === '转派时间') {
                    if (this.TransferTime) {
                        this.queryForm.transferTimeRangeStart = this.TransferTime[0]  // 告警生效时间
                        this.queryForm.transferTimeRangeEnd = this.TransferTime[1] // 告警生效时间
                        this.publicTransferTime = true
                    } else {
                        this.EliminateTime()
                        this.queryForm.transferTimeRangeStart = ''  // 告警生效时间
                        this.queryForm.transferTimeRangeEnd = '' // 告警生效时间
                        this.publicTransferTime = false
                    }
                    this.selectedShow = true
                } else if (data === '派单操作时间') {
                    if (this.deliverTimeHIS) {
                        this.queryForm.deliverTimeRangeStart = this.deliverTimeHIS[0]
                        this.queryForm.deliverTimeRangeEnd = this.deliverTimeHIS[1]
                        this.publicdeliverTimeHIS = true
                    } else {
                        this.EliminateTime()
                        this.queryForm.deliverTimeRangeStart = ''
                        this.queryForm.deliverTimeRangeEnd = ''
                        this.publicdeliverTimeHIS = false
                    }
                    this.selectedShow = true
                } else if (data === '清除操作时间') {
                    if (this.clearTimeHIS) {
                        this.queryForm.clearTimeRangeStart = this.clearTimeHIS[0]
                        this.queryForm.clearTimeRangeEnd = this.clearTimeHIS[1]
                        this.publicclearTimeHIS = true
                    } else {
                        this.EliminateTime()
                        this.queryForm.clearTimeRangeStart = ''
                        this.queryForm.clearTimeRangeEnd = ''
                        this.publicclearTimeHIS = false
                    }
                    this.selectedShow = true
                }
            },
            queryFormFileClear() {
                this.queryFormFile.condition = ''
                this.FileconditionName = ''
                this.Filecondition1 = ''
                this.Filter = false
            },
            empty() {
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
                this.deviceIp = ''
                this.bizSys0 = ''
                this.bizSys1 = ''
                this.alertLevelS = ''
                this.objectTypeS = ''
                this.notifyTypeS = ''
                this.isClearHIS = '' // 历史告警------清除类型
                this.deliverHIS = ''// 历史告警-------派单人员
                this.clearTimeHIS = [] // 历史告警--------清除操作时间时间
                    .deliverTimeHIS = [] // 历史告警--------清除操作时间时间

                        .queryForm.page_no = 1
                this.queryForm.page_size = 50

                this.queryForm.order = 'DESC' // 排序方式，默认desc
                this.queryForm.queryType = '0' // 查询类型，待确认-0，已确认-1
                this.queryForm.alertLevel = ''
                this.FileconditionName = ''
                this.Filecondition1 = ''
                this.selectedShow = false
                this.Filter = false
                this.alarmBeginTime = []
                this.alarmCurrentTime = []
                this.operationTime = []
                this.TransferTime = []

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
                // this.queryForm.page_no = val2
                // this.queryForm.page_size = val3
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
                this.dataList.alertLevelList = alert_level
                this.dataList.notifyTypeList = alert_notice_types
                this.dataList.noticeStatusList = notice_status
                this.dataList.objectTypeList = alert_type
                this.dataList.scavenging_typeList = scavenging_type

                // 获取过滤器/v1/alerts/alerts_filter/selectAll?filterFlag=true
                this.rbHttp.sendRequest({
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
                    this.dataList.conditionList = this.fillSelectOption1(newarrey, 'idcType')
                })
                // 获取资源池
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.dataList.poolList = this.fillSelectOption(res, 'idcType')
                })
                // 获取设备品牌
                this.rbHttp.sendRequest({
                    method: 'POST',
                    params: { 'type': '生产供应商', 'orderBy': 'produce' },
                    url: '/v1/cmdb/maintenProduce/listProduceByPage'
                }).then((res) => {
                    this.brandList = res.data
                    // this.brandList = this.fillSelectOption(res.data, 'idcType')
                })
                // 获取设备分类
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/configDict/getAll'
                }).then((res) => {
                    const deviceClassOptions = []
                    if (res) {
                        res.forEach((item) => {
                            if (item.colName === 'device_class') {
                                deviceClassOptions.push(this.convertDictConfig(item))
                            }
                        })
                        this.dataList.deviceClassList = this.fillSelectOption(deviceClassOptions, 'idcType')
                    }
                })
                // 获取业务系统GET /v1/cmdb/orgManager/loadTreeDepBiz    /v1/cmdb/configDict/getDictsByType
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'department1' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.dataList.bizSysList = this.fillSelectOption(res, 'idcType')
                })
                // 告警来源：GET /v1/cmdb/configDict/getDictsByType?type=alert_from
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'alert_from' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    let Arrey = []
                    for (let item in res) {
                        Arrey.push({
                            name: res[item].value,
                            value: res[item].value
                        })
                    }
                    this.dataList.sourceList = this.fillSelectOption(Arrey, 'alert_from')
                    if (this.dataList.sourceList.length * 80 >= document.body.offsetWidth - 200) {
                        this.SourceB = true
                    } else {
                        this.false = true
                    }
                })
                // 设备类型： GET /v1/cmdb/configDict/getDictsByType?type=device_type
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'device_type' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.dataList.deviceTypeList = this.fillSelectOption(res, 'device_type')
                })
                // 监控对象：GET /v1/alerts/statistic/monit-obj-list
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/alerts/statistic/monit-obj-list'
                }).then((res) => {
                    let newArray = []
                    for (let item in res) {
                        newArray.push({
                            name: res[item],
                            value: res[item]
                        })
                    }
                    this.dataList.monitItemsList = this.fillSelectOption(newArray, 'device_type')
                })
                // 机房：GET /v1/cmdb/configDict/getDictsByType?type=roomId
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'roomId' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.dataList.sourceRoomList = this.fillSelectOption(res, 'roomId')
                })
            },
            clearQuery(data) {
                if (data === '待确认人') {
                    this.publictoConfirmStaff = false
                    this.queryForm.toConfirmStaff = ''
                } else if (data === '转派操作人') {
                    this.publictransferStaff = false
                    this.queryForm.transferStaff = ''
                } else if (data === '主机名称') {
                    this.publichostName = false
                    this.queryForm.hostName = ''
                } else if (data === '告警内容') {
                    this.publicmonitDesc = false
                    this.queryForm.monitDesc = ''
                } else if (data === '派单人员') {
                    this.publicdeliverHIS = false
                    this.queryForm.deliver = ''
                }
                this.Eliminate()
            },
            queryFormClick(item, data) {
                if (data === '待确认人') {
                    this.publictoConfirmStaff = true
                    this.queryForm.toConfirmStaff = item
                } else if (data === '转派操作人') {
                    this.publictransferStaff = true
                    this.queryForm.transferStaff = item
                } else if (data === '主机名称') {
                    this.publichostName = true
                    this.queryForm.hostName = item
                } else if (data === '告警内容') {
                    this.publicmonitDesc = true
                    this.queryForm.monitDesc = item
                } else if (data === '派单人员') {
                    this.publicdeliverHIS = true
                    this.queryForm.deliver = item
                }
                this.selectedShow = true
            },
            // 选择过滤器场景GET /v1/alerts/alerts_filter/filter/{filter_id}
            conditionClick(item, id) {
                if (item === '') {
                    this.Condition = false
                    this.Filecondition1 = item
                    this.Filter = false
                    this.nameBorderAttribute = false
                    this.ConditionB = false
                    this.ConditionUP = false
                } else {
                    this.Filecondition1 = item
                    this.Filter = true
                    this.nameBorderAttribute = true
                }
                this.FileconditionName = ''
                this.publiccondition = false
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
                    this.selectedShow = true
                    this.publiccondition = true
                }
            },
            // 选择设备IP
            deviceDeviceClick(item) {
                if (item === '') {
                    this.publicDeviceDevice = false
                    this.queryForm.deviceIp = ''
                    this.Eliminate()
                } else {
                    this.queryForm.deviceIp = item
                    this.selectedShow = true
                    this.publicDeviceDevice = true
                }
            },
            // 选择资源池
            poolClick(item) {
                this.queryFormFileClear()
                if (item === '') {
                    this.publicPool = false
                    this.queryForm.idcType = ''
                    this.Eliminate()
                } else {
                    this.queryForm.idcType = item
                    this.selectedShow = true
                    this.publicPool = true
                }
            },
            // 通知方式
            notifyTypeClick(item, id) {
                if (item === '全部') {
                    this.publicnotifyType = false
                    this.queryForm.notifyType = ''
                    this.notifyTypeS = ''
                    this.Eliminate()
                } else {
                    this.queryForm.notifyType = id.id
                    this.notifyTypeS = item
                    this.selectedShow = true
                    this.publicnotifyType = true
                }
            },
            // 机房位置
            sourceRoomClick(item) {
                if (item === '全部') {
                    this.publicsourceRoom = false
                    this.queryForm.sourceRoom = ''
                    this.Eliminate()
                } else {
                    this.queryForm.sourceRoom = item
                    this.selectedShow = true
                    this.publicsourceRoom = true
                }
            },
            // 告警类型
            alert_typeClick(item, id) {
                if (item === '全部') {
                    this.publicalert_type = false
                    this.queryForm.objectType = ''
                    this.objectTypeS = ''
                    this.Eliminate()
                } else {
                    this.objectTypeS = item
                    this.queryForm.objectType = id
                    this.selectedShow = true
                    this.publicalert_type = true
                }
            },
            // 选择设备分类
            deviceClassClick(item) {
                if (item === '') {
                    this.publicDeviceClass = false
                    this.queryForm.deviceClass = ''
                    this.Eliminate()
                } else {
                    this.queryForm.deviceClass = item
                    this.selectedShow = true
                    this.publicDeviceClass = true
                }
            },
            // 选择告警级别
            alertLevelClick(item, id) {
                if (item === '全部') {
                    this.publicalertLevel = false
                    this.queryForm.alertLevel = ''
                    this.alertLevelS = ''
                    this.Eliminate()
                } else {
                    this.alertLevelS = item
                    this.queryForm.alertLevel = id
                    this.selectedShow = true
                    this.publicalertLevel = true
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
            // 选择业务系统
            bizSysClick(item, id) {
                this.dataList.bizSysList2 = []
                this.BizSysdepartmen2 = false
                this.bizSys0 = ''
                this.bizSys1 = ''
                if (item === '全部') {
                    this.publicbizSys = false
                    this.queryForm.bizSys = ''
                    this.Eliminate()
                } else {
                    this.bizSys0 = item
                    this.BizSys = true
                    this.BizSysdepartmen1 = true
                    this.BizSysB = false
                    this.BizSysUP = true
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': 'department2', 'pid': id },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        this.dataList.bizSysList1 = this.fillSelectOption(res, 'idcType')
                    })
                }
            },
            bizSysClick1(item, id) {
                this.queryForm.bizSys = ''
                if (item === '全部') {
                    this.publicbizSys = false
                    this.bizSys1 = ''
                    this.Eliminate()
                } else {
                    this.bizSys1 = item
                    this.BizSys = true
                    this.BizSysdepartmen2 = true

                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': 'bizSystem', 'pid': id },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        this.dataList.bizSysList2 = this.fillSelectOption(res, 'idcType')
                    })
                }
            },
            bizSysClick2(item) {
                if (item === '全部') {
                    this.publicbizSys = false
                    this.queryForm.bizSys = ''
                    this.Eliminate()
                } else {
                    this.queryForm.bizSys = item
                    this.selectedShow = true
                    this.publicbizSys = true
                }
            },
            // 选择告警涞源
            sourceClick(item) {
                if (item === '全部') {
                    this.publicsource = false
                    this.queryForm.source = ''
                    this.Eliminate()
                } else {
                    this.queryForm.source = item
                    this.selectedShow = true
                    this.publicsource = true
                }
            },
            // 选择设备类型
            deviceTypeClick(item) {
                if (item === '全部') {
                    this.publicdeviceType = false
                    this.queryForm.deviceType = ''
                    this.Eliminate()
                } else {
                    this.queryForm.deviceType = item
                    this.selectedShow = true
                    this.publicdeviceType = true
                }
            },
            // 选择监控对象
            monitItemsClick(item) {
                if (item === '全部') {
                    this.publicmonitItems = false
                    this.queryForm.monitItems = ''
                    this.Eliminate()
                } else {
                    this.queryForm.monitItems = item
                    this.selectedShow = true
                    this.publicmonitItems = true
                }
            },
            // 选择通知状态
            noticeStatusClick(item, id) {
                if (item === '全部') {
                    this.publicnotice_status = false
                    this.queryForm.notifyStatus = ''
                    this.notifyStatusS = ''
                    this.Eliminate()
                } else {
                    this.queryForm.notifyStatus = id
                    this.notifyStatusS = item
                    this.selectedShow = true
                    this.publicnotice_status = true
                }
            },
            // 选择设备型号
            device_modelClick(item) {
                this.queryForm.deviceModel = item
                this.publicdevice_model = true
                this.selectedShow = true
            },
            // 设备品牌
            Brand(item, index) {
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
            changeQueryDelete(data) {
                if (data === '资源池') {
                    this.queryForm.idcType = ''
                    this.publicPool = false
                } else if (data === '设备分类') {
                    this.publicDeviceClass = false
                    this.queryForm.deviceClass = ''
                } else if (data === '设备品牌') {
                    this.publicBrand = false
                    this.queryForm.deviceMfrs = ''
                    this.ImgSelected = ''
                    this.imgIndex = ''
                } else if (data === '设备IP') {
                    this.publicDeviceDevice = false
                    this.queryForm.deviceIp = ''
                    this.deviceIp = ''
                } else if (data === '设备型号') {
                    this.publicdevice_model = false
                    this.queryForm.deviceModel = ''
                    this.deviceModel = ''
                } else if (data === '通知状态') {
                    this.publicnotice_status = false
                    this.queryForm.notice_status = ''
                    this.notifyStatusS = ''
                } else if (data === '告警级别') {
                    this.publicalertLevel = false
                    this.queryForm.alertLevel = ''
                    this.alertLevelS = ''
                } else if (data === '过滤器2') {
                    this.publiccondition = false
                    this.FileconditionName = ''
                    this.Filecondition1 = ''
                    this.Filter = false
                } else if (data === '业务系统') {
                    this.publicbizSys = false
                    this.bizSys0 = ''
                    this.bizSys1 = ''
                    this.queryForm.bizSys = ''
                } else if (data === '告警来源') {
                    this.publicsource = false
                    this.queryForm.source = '全部'
                } else if (data === '设备类型') {
                    this.publicdeviceType = false
                    this.queryForm.deviceType = ''
                } else if (data === '当前告警时间') {
                    this.publicalarmCurrentTime = false
                    this.alarmBeginTime = ''
                    this.queryForm.curMoniTimeFrom = ''  // 告警生效时间
                    this.queryForm.curMoniTimeTo = '' // 告警生效时间
                    this.EliminateTime()
                } else if (data === '监控对象') {
                    this.publicmonitItems = false
                    this.queryForm.monitItems = ''
                } else if (data === '告警开始时间') {
                    this.publicalarmBeginTime = false
                    this.alarmCurrentTime = ''
                    this.queryForm.alertCreateTimeRangeStart = ''  // 告警生效时间
                    this.queryForm.alertCreateTimeRangeEnd = '' // 告警生效时间
                    this.EliminateTime()
                } else if (data === '通知操作时间') {
                    this.publicoperationTime = false
                    this.operationTime = ''
                    this.queryForm.notifyTimeRangeStart = ''  // 告警生效时间
                    this.queryForm.notifyTimeRangeEnd = '' // 告警生效时间
                    this.EliminateTime()
                } else if (data === '转派时间') {
                    this.publicTransferTime = false
                    this.TransferTime = ''
                    this.queryForm.transferTimeRangeStart = '' // 告警生效时间
                    this.queryForm.transferTimeRangeEnd = '' // 告警生效时间
                    this.EliminateTime()
                } else if (data === '机房位置') {
                    this.publicsourceRoom = false
                    this.queryForm.sourceRoom = ''
                } else if (data === '通知方式') {
                    this.publicnotifyType = false
                    this.queryForm.notifyType = ''
                    this.notifyTypeS = ''
                } else if (data === '待确认人') {
                    this.publictoConfirmStaff = false
                    this.queryForm.toConfirmStaff = ''
                    this.toConfirmStaff = ''
                } else if (data === '转派操作人') {
                    this.publictransferStaff = false
                    this.queryForm.transferStaff = ''
                    this.transferStaff = ''
                } else if (data === '主机名称') {
                    this.publichostName = false
                    this.queryForm.hostName = ''
                    this.hostName = ''
                } else if (data === '告警内容') {
                    this.publicmonitDesc = false
                    this.queryForm.monitDesc = ''
                    this.monitDesc = ''
                } else if (data === '告警类型') {
                    this.publicalert_type = false
                    this.queryForm.alert_type = ''
                    this.objectTypeS = ''
                } else if (data === '清除类型') {
                    this.publicscavenging_type = false
                    this.queryForm.isClear = ''
                    this.isClearHIS = ''
                } else if (data === '派单人员') {
                    this.publicdeliverHIS = false
                    this.queryForm.deliver = ''
                    this.deliverHIS = ''
                } else if (data === '派单操作时间') {
                    this.publicdeliverTimeHIS = false
                    this.deliverTimeHIS = ''
                    this.queryForm.deliverTimeRangeStart = ''
                    this.queryForm.deliverTimeRangeEnd = ''
                    this.EliminateTime()
                } else if (data === '清除操作时间') {
                    this.publicclearTimeHIS = false
                    this.clearTimeHIS = ''
                    this.queryForm.clearTimeRangeStart = ''
                    this.queryForm.clearTimeRangeEnd = ''
                    this.EliminateTime()
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
            alertType: {
                handler(val) {
                    if (val === 'activity') {
                        this.queryForm.queryType = '0'
                    } else if (val === 'confirm') {
                        this.queryForm.queryType = 1
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
</style>
<style lang="stylus">
    .el-input-group__append {
        color: #6F7EA2;
        vertical-align: middle;
        display: table-cell;
        position: relative;
        border: 1px solid #DCDFE6;
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
        border: 1px solid #2089DA;
    }

    button.el-button.el-button--default {
        border: 1px solid #2089DA;
        padding: 5px 15px;
    }
</style>

