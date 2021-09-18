<template>
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div class="managementRight">
            <!-- <Tab ref="networkTab"
                 @fatherMethodNetwork="newreFresh" />
            <Info /> -->
            <div class="managementRightUP"
                 v-if="reFresh">
                <div class="overview">
                    <div class="overviewContent uop"
                         v-show="overviewShow">
                        <div style="overflow:hidden"
                             v-show="oneStep.show">

                            <div :class="{equipmentBrandClass:pool,aLinePublic:aLinePublic1}"
                                 class="aLine">
                                <div class="content"
                                     style="display:flex;">
                                    <div class="name"
                                         style="display:inherit">
                                        {{oneStep1.name}}
                                    </div>
                                    <div style="flex:1;"
                                         v-show="!newonestep1">
                                        <!-- <p :class='queryForm.newidcType.includes(item.value)?"poolColor":""' -->
                                        <p :class='item.value == queryForm.idcType?"poolColor":""'
                                           @click="jilianclick(item.value,item.id,'idcType',idcTypeArr,onehead,item,false)"
                                           v-for="(item,index) in poolList"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                    <!-- 非级联 -->
                                    <div style="flex:1;"
                                         v-show="newonestep1">
                                        <p :class='queryForm.newidcType.includes(item.value)?"poolColor":""'
                                           @click="jilianclick(item.value,item.id,'idcType',idcTypeArr,onehead,item,true)"
                                           v-for="(item,index) in poolList"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                </div>
                                <div v-if='poolB'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="poolBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='poolUP'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="poolBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>

                            <div :class="{equipmentBrandClass:pool2,aLinePublic:aLinePublic2}"
                                 class="aLine">
                                <div class="content"
                                     style="display:flex;"
                                     :class="{boraderActive:boraderActive}">
                                    <div class="name"
                                         style="display:inherit;border-left: 1px solid #DEE9FC;">
                                        {{oneStep2.name}}
                                    </div>
                                    <!-- 工期 -->
                                    <div style="flex:1;"
                                         v-show="newonestep2">
                                        <p :class='queryForm.newdeviceProject.includes(item.value)?"poolColor":""'
                                           @click="jilianclick(item.value,item.id,'deviceProject',projectnameArr,twohead,item,true)"
                                           v-for="(item,index) in projectNameList"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                    <div style="flex:1;"
                                         v-show="!newonestep2">
                                        <p :class='item.value==queryForm.deviceProject?"poolColor":""'
                                           @click="jilianclick(item.value,item.id,'deviceProject',projectnameArr,twohead,item,false)"
                                           v-for="(item,index) in projectNameList"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                </div>

                                <div v-if='poolB2'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="poolBotton2">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='poolUP2'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="poolBotton2">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </div>
                        <!-- 单个一行资源 -->
                        <div :class="{equipmentBrandClass:pool}"
                             class="aLine"
                             v-show="oneStep1.show">
                            <div class="content"
                                 style="display:flex;width:calc(100% - 100px);;">
                                <div class="name"
                                     style="display:inherit">
                                    {{oneStep1.name}}
                                </div>
                                <div style="flex:1;"
                                     v-show="!newonestep1">
                                    <p :class='item.value == queryForm.idcType?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'idcType',idcTypeArr,onehead,item,false)"
                                       v-for="(item,index) in poolList"
                                       :key=index>
                                        <span>{{ item.value}}</span>
                                    </p>
                                </div>
                                <!-- 非级联 -->
                                <div style="flex:1;"
                                     v-show="newonestep1">
                                    <p :class='queryForm.newidcType.includes(item.value)?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'idcType',idcTypeArr,onehead,item,true)"
                                       v-for="(item,index) in poolList"
                                       :key=index>
                                        <span>{{ item.value}}</span>
                                    </p>
                                </div>
                            </div>
                            <div v-if='poolB'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="poolBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                            </div>
                            <div v-if='poolUP'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="poolBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                            </div>
                        </div>
                        <div :class="{equipmentBrandClass:pool2}"
                             class="aLine"
                             v-show="oneStep2.show">
                            <div class="content"
                                 style="display:flex;width:calc(100% - 100px);;">
                                <div class="name"
                                     style="display:inherit">
                                    {{oneStep2.name}}
                                </div>
                                <!-- 工期 -->
                                <div style="flex:1;"
                                     v-show="newonestep2">
                                    <p :class='queryForm.newdeviceProject.includes(item.value)?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'deviceProject',projectnameArr,twohead,item,true)"
                                       v-for="(item,index) in projectNameList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                                <div style="flex:1;"
                                     v-show="!newonestep2">
                                    <p :class='item.value==queryForm.deviceProject?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'deviceProject',projectnameArr,twohead,item,false)"
                                       v-for="(item,index) in projectNameList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                            </div>
                            <div v-if='poolB2'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="poolBotton2">更多<i class="el-icon-arrow-down"></i></el-button>
                            </div>
                            <div v-if='poolUP2'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="poolBotton2">收起<i class="el-icon-arrow-up"></i></el-button>
                            </div>
                        </div>

                        <div :class="{equipmentBrandClass:POD}"
                             class="aLine"
                             v-show="twoStep.show">
                            <div class="content"
                                 style="display:flex;width:calc(100% - 100px);">
                                <div class="name"
                                     style="display:inherit">
                                    {{twoStep.name}}
                                </div>
                                <div style="flex:1;"
                                     v-show="!newonestep3">
                                    <p :class='item.value==queryForm.pod?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'pod',podnameArr,threehead,item,false)"
                                       v-for="(item,index) in podList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                                <!-- 非级联 -->
                                <div style="flex:1;"
                                     v-show="newonestep3">
                                    <p :class='queryForm.newpod.includes(item.value)?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'pod',podnameArr,threehead,item,true)"
                                       v-for="(item,index) in podList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                            </div>
                            <div v-if='PODB'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="PODBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                            </div>
                            <div v-if='PODBUP'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="PODBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                            </div>
                        </div>
                        <div class="aLine"
                             :class="{equipmentBrandClass:pool3}"
                             v-show="threeStep.show">
                            <div class="content"
                                 style="display:flex;width:calc(100% - 100px);">
                                <div class="name"
                                     style="display:inherit">
                                    {{threeStep.name}}
                                </div>
                                <div style="flex:1;"
                                     v-show="!newonestep4">
                                    <p :class='item.value==queryForm.deviceClass?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'deviceClass',deviceTypeArr,fourhead,item,false)"
                                       v-for="(item,index) in deviceClassList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                                <!-- 非级联 -->
                                <div style="flex:1;"
                                     v-show="newonestep4">
                                    <p :class='queryForm.newdeviceClass.includes(item.value)?"poolColor":""'
                                       @click="jilianclick(item.value,item.id,'deviceClass',deviceTypeArr,fourhead,item,true)"
                                       v-for="(item,index) in deviceClassList"
                                       :key=index>
                                        <span>{{ item.value }}</span>
                                    </p>
                                </div>
                            </div>
                            <div v-if='deviceClassB'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="deviceClassBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                            </div>
                            <div v-if='deviceClassUp'
                                 style="float:right;margin: 4px 10px 0 0;">
                                <el-button @click="deviceClassBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                            </div>
                        </div>
                        <!-- <div :class="{equipmentBrandClass:equipmentBrand}" class="aLine brand" v-show="fourStep.show">
                    <div class="content" style="display:flex;min-height:60px;">
                        <div class="name" style="height: inherit;min-height:59px;line-height:60px;">
                            {{fourStep.name}}
                        </div>
                        <div style="flex:1;margin-right:10px;margin-left:20px">
                            <div class="iconimg" @click="Brand(item.produce,index,item)" v-for="(item,index) in brandList" :key = index >
                                <div @mouseover="mouseOverImg(item.produce,index)" @mouseleave="mouseLeaveImg(item.produce)" class="iconimgP" v-if = item.logoUrl>
                                    <div v-if="item.produce==queryForm.mfrFactory || imgIndex === index || ImgSelected === index" class="iconimgP poolColors">{{item.produce}}</div>
                                    <img v-else class="imgLogo" :src="item.logoUrl"/>
                                </div>
                                <div @mouseover="mouseOver(item.produce,index)" @mouseleave="mouseLeave(item.produce)" :class='(item.produce==queryForm.mfrFactory || imgIndex === index || ImgSelected === index)?"poolColors":""' class="iconimgP" v-else>{{item.produce}}</div>
                            </div>
                        </div>
                    </div> -->
                        <div :class="{equipmentBrandClass:equipmentBrand}"
                             class="aLine brand"
                             v-show="fourStep.show">
                            <div class="content"
                                 style="display:flex;min-height:60px;">
                                <div class="name"
                                     style="height: inherit;min-height:59px;line-height:60px;">
                                    {{fourStep.name}}
                                </div>
                                <div style="flex:1;margin-right:10px;margin-left:20px">
                                    <div class="iconimg"
                                         @click="Brand(item.name,index,item)"
                                         v-for="(item,index) in brandList"
                                         :key=index>
                                        <div @mouseover="mouseOverImg(item.name,index)"
                                             @mouseleave="mouseLeaveImg(item.name)"
                                             class="iconimgP"
                                             v-if=item.description>
                                            <div v-if="item.name==queryForm.mfrFactory || imgIndex === index || ImgSelected === index"
                                                 class="iconimgP poolColors">{{item.name}}</div>
                                            <img v-else
                                                 class="imgLogo"
                                                 :src="item.description" />
                                        </div>
                                        <div @mouseover="mouseOver(item.name,index)"
                                             @mouseleave="mouseLeave(item.name)"
                                             :class='(item.name==queryForm.mfrFactory || imgIndex === index || ImgSelected === index)?"poolColors":""'
                                             class="iconimgP"
                                             v-else>{{item.name}}</div>
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

                        <!-- 可显示遍历数组 -->
                        <!-- 级联框 -->
                        <template v-for="(item1,index1) in otherList">
                            <div class="aLine"
                                 v-if="item1.one.cmdbCode.controlType.controlCode == 'cascader'"
                                 :class="{equipmentBrandClass:item1[item1.id]}"
                                 :key="index1">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);;">
                                    <div class="name"
                                         style="display:inherit">
                                        {{item1.one.cmdbCode.filedName}}
                                    </div>
                                    <div style="flex:1;">
                                        <p :class='item.value==item1.color?"poolColor":""'
                                           ref="item.value"
                                           @click="commonsql(item.value,item.id,item1,index1,'1',item)"
                                           v-for="(item,index) in item1.two"
                                           :key=index>
                                            <span>{{item.value}}</span>
                                        </p>
                                    </div>
                                </div>
                                <div v-if='item1.popup'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMore(item1.id,index1)">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='item1.popdown'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMore(item1.id,index1)">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </template>
                        <!-- 非级联框 -->
                        <template v-for="(item1,index1) in otherList">
                            <div class="aLine"
                                 v-if="item1.one.cmdbCode.controlType.controlCode == 'listSel'"
                                 :class="{equipmentBrandClass:item1[item1.id]}"
                                 :key="index1">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);;">
                                    <div class="name"
                                         style="display:inherit">
                                        {{item1.one.cmdbCode.filedName}}
                                    </div>
                                    <div style="flex:1;">
                                        <p :class='item1.newcolor.includes(item.value)?"poolColor":""'
                                           ref="item.value"
                                           @click="commonsql(item.value,item.id,item1,index1,'1',item,'none')"
                                           v-for="(item,index) in item1.two"
                                           :key=index>
                                            <span>{{item.value}}</span>
                                        </p>
                                    </div>
                                </div>
                                <div v-if='item1.popup'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMore(item1.id,index1)">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='item1.popdown'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMore(item1.id,index1)">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </template>
                        <!-- 全输入框 -->
                        <!-- <div class="aLine"  v-for="(item1,index) in otherList" :key="index" v-if="item1.one.cmdbCode.controlType.controlCode != 'cascader' && item1.one.cmdbCode.controlType.controlCode != 'listSel' && item1.one.cmdbCode.controlType.controlCode != 'dateTime'">
                          <div class="aLineLeft" style="width: 100%;">
                            <div class="content">
                              <div class="name">
                                {{item1.one.cmdbCode.filedName}}
                              </div>
                              <p class="elInput iconLeft">
                                  <el-input v-model="item1.value" :placeholder="item1.one.cmdbCode.filedName"  @clear="changeQueryIP" :clearable = "true">
                                  </el-input>
                                   <i @click="IPClick(queryForm.ip)" class="el-icon-search elInputIcon"></i>
                              </p>
                            </div>
                          </div>
                        </div> -->
                        <!-- 半输入框 -->
                        <!-- <div style="overflow: hidden;"
                             v-if="newlastIndex<4"> -->
                        <div style="overflow: hidden;"
                             v-if="otherListCheck">
                            <template v-for="(item1,index) in otherList">
                                <div class="aLineLeft"
                                     :key="index"
                                     v-if="item1.one.cmdbCode.controlType.controlCode != 'cascader' && item1.one.cmdbCode.controlType.controlCode != 'listSel' && item1.one.cmdbCode.controlType.controlCode != 'dateTime'">
                                    <div class="content">
                                        <div class="name">
                                            {{item1.one.cmdbCode.filedName}}
                                        </div>
                                        <p class="elInput iconLeft">
                                            <el-input v-model="item1.value"
                                                      :placeholder="item1.one.cmdbCode.filedName"
                                                      @clear="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                                      :clearable="true">
                                            </el-input>
                                            <i @click="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                               class="el-icon-search elInputIcon"></i>
                                        </p>
                                    </div>
                                </div>
                            </template>
                            <!-- 新增 -->
                            <template v-for="(item1,index) in otherList">
                                <div class="aLineLeft"
                                     :key="index"
                                     v-if="item1.one.cmdbCode.controlType.controlCode == 'dateTime'">
                                    <div class="content">
                                        <div class="name">
                                            {{item1.one.cmdbCode.filedName}}
                                        </div>
                                        <p class="elInput iconLeft">
                                            <el-date-picker v-model="item1.value"
                                                            type="daterange"
                                                            range-separator="至"
                                                            :start-placeholder="item1.one.cmdbCode.filedName"
                                                            :end-placeholder="item1.one.cmdbCode.filedName"
                                                            value-format="yyyy-MM-dd"
                                                            @clear="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                                            :clearable="true"
                                                            @change="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)">
                                            </el-date-picker>
                                        </p>
                                    </div>
                                </div>
                            </template>

                            <div style="width: 50%;height: 32px;float: left;border-bottom: 1px solid #DEE9FC;"
                                 v-if="toplastIndex"></div>
                        </div>
                        <!-- 日期 -->
                        <!-- <div class="aLine"  v-for="(item1,index) in otherList" :key="index" v-if="item1.one.cmdbCode.controlType.controlCode == 'dateTime'">
                          <div class="aLineLeft">
                            <div class="content">
                              <div class="name">
                                {{item1.one.cmdbCode.filedName}}
                              </div>
                              <p class="elInput" style="margin-top:2px">
                                  <el-date-picker v-model="item1.value" type="daterange" range-separator="至"
                                                      start-placeholder="设备录入日期" end-placeholder="设备录入日期" value-format="yyyy-MM-dd" :clearable = "true" @change="inputFun(item1.value,item1.one.cmdbCode.filedCode)">
                                  </el-date-picker>
                              </p>
                            </div>
                          </div>
                        </div> -->

                    </div>

                    <div id="overviewContentID"
                         class="overviewContent"
                         v-if="showDown"
                         style="margin-top:-10px;border-top:none">
                        <!-- 后面遍历数组 -->
                        <template v-for="(item1,index1) in reducelist">
                            <div class="aLine"
                                 v-if="item1.one.cmdbCode.controlType.controlCode == 'cascader'"
                                 :class="{equipmentBrandClass:item1[item1.id]}"
                                 :key="index1">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);;">
                                    <div class="name"
                                         style="display:inherit">
                                        {{item1.one.cmdbCode.filedName}}
                                    </div>
                                    <div style="flex:1;">
                                        <p :class='item.value==item1.color?"poolColor":""'
                                           ref="item.value"
                                           @click="commonsql(item.value,item.id,item1,index1,'2',item)"
                                           v-for="(item,index) in item1.two"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                </div>
                                <div v-if='item1.popup'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMoreOther(item1.id,index1)">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='item1.popdown'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMoreOther(item1.id,index1)">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </template>

                        <!-- 拆分非级联下拉框 -->
                        <template v-for="(item1,index1) in reducelist">
                            <div class="aLine"
                                 v-if="item1.one.cmdbCode.controlType.controlCode == 'listSel'"
                                 :class="{equipmentBrandClass:item1[item1.id]}"
                                 :key="index1">
                                <div class="content"
                                     style="display:flex;width:calc(100% - 100px);;">
                                    <div class="name"
                                         style="display:inherit">
                                        {{item1.one.cmdbCode.filedName}}
                                    </div>
                                    <div style="flex:1;">
                                        <!-- <p :class='item.value==item1.color?"poolColor":""' -->
                                        <p :class='item1.newcolor.includes(item.value)?"poolColor":""'
                                           ref="item.value"
                                           @click="commonsql(item.value,item.id,item1,index1,'2',item,'none')"
                                           v-for="(item,index) in item1.two"
                                           :key=index>
                                            <span>{{ item.value }}</span>
                                        </p>
                                    </div>
                                </div>
                                <div v-if='item1.popup'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMoreOther(item1.id,index1)">更多<i class="el-icon-arrow-down"></i></el-button>
                                </div>
                                <div v-if='item1.popdown'
                                     style="float:right;margin: 4px 10px 0 0;">
                                    <el-button @click="commonMoreOther(item1.id,index1)">收起<i class="el-icon-arrow-up"></i></el-button>
                                </div>
                            </div>
                        </template>

                        <!-- 日期 -->
                        <!-- <div class="aLine"  v-for="(item1,index) in reducelist" :key="index" v-if="item1.one.cmdbCode.controlType.controlCode == 'dateTime'">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          {{item1.one.cmdbCode.filedName}}
                        </div>
                        <p class="elInput" style="margin-top:2px">
                            <el-date-picker v-model="item1.value" type="daterange" range-separator="至"
                                                start-placeholder="设备录入日期" end-placeholder="设备录入日期" value-format="yyyy-MM-dd" :clearable = "true" @change="inputFun(item1.value,item1.one.cmdbCode.filedCode)">
                            </el-date-picker>
                        </p>
                      </div>
                    </div>
                  </div> -->
                        <!-- 半输入框 -->
                        <div style="overflow: hidden;">
                            <template v-for="(item1,index) in reducelist">
                                <div class="aLineLeft"
                                     :key="index"
                                     v-if="item1.one.cmdbCode.controlType.controlCode != 'cascader' && item1.one.cmdbCode.controlType.controlCode != 'listSel' && item1.one.cmdbCode.controlType.controlCode != 'dateTime'">
                                    <div class="content">
                                        <div class="name">
                                            {{item1.one.cmdbCode.filedName}}
                                        </div>
                                        <p class="elInput iconLeft">
                                            <el-input v-model="item1.value"
                                                      @clear="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                                      :placeholder="item1.one.cmdbCode.filedName"
                                                      :clearable="true">
                                            </el-input>
                                            <i @click="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                               class="el-icon-search elInputIcon"></i>
                                        </p>
                                    </div>
                                </div>
                            </template>
                            <!-- 新增 -->
                            <template v-for="(item1,index) in reducelist">
                                <div class="aLineLeft"
                                     :key="index"
                                     v-if="item1.one.cmdbCode.controlType.controlCode == 'dateTime'">
                                    <div class="content">
                                        <div class="name">
                                            {{item1.one.cmdbCode.filedName}}
                                        </div>
                                        <p class="elInput iconLeft">
                                            <el-date-picker v-model="item1.value"
                                                            type="daterange"
                                                            range-separator="至"
                                                            :start-placeholder="item1.one.cmdbCode.filedName"
                                                            :end-placeholder="item1.one.cmdbCode.filedName"
                                                            value-format="yyyy-MM-dd"
                                                            :clearable="true"
                                                            @clear="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)"
                                                            @change="inputFun(item1.value,item1.one.cmdbCode.filedCode,item1)">
                                            </el-date-picker>
                                        </p>
                                    </div>
                                </div>
                            </template>
                            <div style="width: 50%;height: 32px;float: left;border-bottom: 1px solid #DEE9FC;"
                                 v-if="lastIndex"></div>
                        </div>

                        <!-- 全输入框 -->
                        <!-- <div class="aLine"  v-for="(item1,index) in reducelist" :key="index" v-if="item1.one.cmdbCode.controlType.controlCode != 'cascader' && item1.one.cmdbCode.controlType.controlCode != 'listSel' && item1.one.cmdbCode.controlType.controlCode != 'dateTime'">
                      <div class="aLineLeft" style="width: 100%;">
                        <div class="content">
                          <div class="name">
                            {{item1.one.cmdbCode.filedName}}
                          </div>
                          <p class="elInput iconLeft">
                              <el-input v-model="item1.value" :placeholder="item1.one.cmdbCode.filedName"  @clear="changeQueryIP" :clearable = "true">
                              </el-input>
                               <i @click="IPClick(queryForm.ip)" class="el-icon-search elInputIcon"></i>
                          </p>
                        </div>
                      </div>
                    </div> -->

                        <!-- <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          设备类型
                        </div>
                        <p class="elInput">
                            <el-select v-model="queryForm.deviceType" placeholder="设备类型" filterable :clearable = "true"  @clear="changeQueryDeviceType" @change="queryDeviceClass3">
                              <el-option @click.native="deviceTypeClick(queryForm.deviceType)" v-for="item in deviceTypeList" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                            </el-select>
                        </p>
                      </div>
                    </div>
                    <div class="aLineRight">
                      <div class="content">
                        <div class="name" style="border-left: 1px solid #DEE9FC;">
                          设备三级分类
                        </div>
                        <p class="elInput">
                            <el-select v-model="queryForm.deviceClass3" placeholder="设备三级分类" filterable :clearable = "true"  @clear="changeQueryDeviceClass3" @change="changeQuery">
                                <el-option @click.native="deviceClass3Click(queryForm.deviceClass3)" v-for="item in deviceClass3List" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                            </el-select>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          一级部门
                        </div>
                        <p class="elInput">
                            <el-select v-model="queryForm.department1" placeholder="一级部门" filterable :clearable = "true"  @clear="changeQueryDepartment1" @change="changeDepartment1" >
                              <el-option @click.native="department1Click(queryForm.department1)" v-for="item in department1List" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                            </el-select>
                        </p>
                      </div>
                    </div>
                    <div class="aLineRight">
                      <div class="content">
                        <div class="name" style="border-left: 1px solid #DEE9FC;">
                          二级部门
                        </div>
                        <p class="elInput">
                              <el-select v-model="queryForm.department2" placeholder="二级部门" filterable :clearable = "true" @clear="changeQueryDepartment2" @change="changeDepartment2">
                                <el-option  @click.native="department2Click(queryForm.department2)" v-for="item in department2List" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                              </el-select>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          业务系统
                        </div>
                        <p class="elInput">
                              <el-select v-model="queryForm.bizSystem" placeholder="业务系统" filterable @clear="changeQueryBizSystem" :clearable = "true" @change="changeQuery">
                                <el-option @click.native="bizSystemClick(queryForm.bizSystem)" v-for="item in bizSystemList" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                              </el-select>
                        </p>
                      </div>
                    </div>
                    <div class="aLineRight">
                      <div class="content">
                        <div class="name" style="border-left: 1px solid #DEE9FC;">
                          设备型号
                        </div>
                        <p class="elInput iconLeft">
                            <el-input v-model="queryForm.deviceModel" placeholder="设备型号" @clear="changeQueryDeviceModel"  :clearable = "true"></el-input>
                             <i  @click="deviceModelClick(queryForm.deviceModel)" class="el-icon-search elInputIcon"></i>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          机房位置
                        </div>
                          <p class="elInput">
                              <el-select v-model="queryForm.room" placeholder="机房位置" filterable :clearable = "true"  @clear="changeQueryRoom" @change="changeDepartment2">
                                <el-option @click.native="roomClick(queryForm.room)" v-for="item in roomList" :key="item.id" :label="item.name" :value="item.value" prefix-icon="el-icon-search"></el-option>
                              </el-select>
                          </p>
                      </div>
                    </div>
                    <div class="aLineRight">
                      <div class="content">
                        <div class="name"  style="border-left: 1px solid #DEE9FC;">
                          设备序列号
                        </div>
                        <p class="elInput iconLeft">
                            <el-input v-model="queryForm.deviceSN" placeholder="设备序列号" @clear="changeQueryDeviceSN" :clearable = "true" ></el-input>
                             <i  @click="deviceSNClick(queryForm.deviceSN)" class="el-icon-search elInputIcon"></i>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div :class="{equipmentBrandClass:status}" class="aLine">
                    <div class="content" style="display:flex;width:calc(100% - 100px);;">
                        <div class="name" style="display:inherit">
                        运行状态
                        </div>
                        <div style="flex:1">
                            <p :class='item.value==queryForm.deviceStatus?"poolColor":""'  @click="runningState(item.value)" v-for="(item,index) in deviceStatusList" :key = index>
                                <span>{{ item.name }}</span>
                            </p>
                        </div>
                    </div>
                    <div v-if = 'deviceStatusB' style="float:right;margin: 4px 10px 0 0;">
                      <el-button @click = "statusBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                    </div>
                    <div v-if = 'deviceStatusUP'  style="float:right;margin: 4px 10px 0 0;">
                      <el-button @click = "statusBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                    </div>
                  </div>
                  <div :class="{equipmentBrandClass:factory}" class="aLine">
                    <div class="content" style="display:flex;width:calc(100% - 100px);;">
                        <div class="name" style="display:inherit">
                            维保厂家
                        </div>
                        <div style="flex:1">
                            <p :class='item.value==queryForm.maintenanceFactory?"poolColor":""'  @click="mainFactory(item.value)" v-for="(item,index) in maintenanceFactoryList" :key = index>
                                <span>{{ item.name }}</span>
                            </p>
                        </div>
                    </div>
                    <div v-if = 'factoryB' style="float:right;margin: 4px 10px 0 0;">
                      <el-button @click = "factoryBotton">更多<i class="el-icon-arrow-down"></i></el-button>
                    </div>
                    <div v-if = 'factoryUP'  style="float:right;margin: 4px 10px 0 0;">
                      <el-button @click = "factoryBotton">收起<i class="el-icon-arrow-up"></i></el-button>
                    </div>
                  </div>
                  <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          维护部门
                        </div>
                        <p class="elInput iconLeft">
                            <el-input v-model="queryForm.deptOperation" placeholder="维护部门" @clear="changeQueryDeptOperation" :clearable = "true"></el-input>
                             <i  @click="deptOperationClick(queryForm.deptOperation)" class="el-icon-search elInputIcon"></i>
                        </p>
                      </div>
                    </div>
                    <div class="aLineRight">
                      <div class="content">
                        <div class="name" style="border-left: 1px solid #DEE9FC;">
                          维护人员
                        </div>
                        <p class="elInput iconLeft">
                            <el-input v-model="queryForm.ops" placeholder="维护人员" @clear="changeQueryOps" :clearable = "true"></el-input>
                             <i  @click="opsClick(queryForm.ops)" class="el-icon-search elInputIcon"></i>
                        </p>
                      </div>
                    </div>
                  </div>
                  <div class="aLine">
                    <div class="aLineLeft">
                      <div class="content">
                        <div class="name">
                          录入日期
                        </div>
                        <p class="elInput" style="margin-top:2px">
                            <el-date-picker v-model="insertTime" type="daterange" range-separator="至"
                                                start-placeholder="设备录入日期" end-placeholder="设备录入日期" value-format="yyyy-MM-dd" :clearable = "false" @clear="changeQuery" @change="insertTimeClick">
                            </el-date-picker>
                        </p>
                      </div>
                    </div>
                  </div> -->

                    </div>
                </div>
                <div class="querybotton">
                    <div class="showcCondition"
                         v-show="newMoreDown">
                        <div v-if="queryBotton">
                            <p v-if="queryBottonS"
                               @click='QueryBottonExhibition("down")'>
                                <i class="el-icon-arrow-down"></i>更多查询选项
                            </p>
                            <p v-else
                               @click='QueryBottonExhibition("down")'>
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
                <div class="selected"
                     v-show="selectedShow">
                    <div class="selectedTotal">共找到<span>&nbsp;{{total}}&nbsp;</span>条记录,已选</div> <!-- :disabled="true" -->
                    <div id="querySelector"
                         class="chosenFrame">

                        <div v-for="(item,index) in allcheckList"
                             style="display:inline-block;"
                             :key="index">
                            <div class="public"
                                 v-if="item.newson.length>0">
                                <span class="publicName">&nbsp;&nbsp;{{item.father.cmdbCode.filedName}}：&nbsp;&nbsp;&nbsp;</span>
                                <span class="publicContent"
                                      v-for="(item2,index) in item.newson"
                                      :key="index">{{item2.value}}<i v-if="index != item.newson.length-1"> , </i></span>
                                <span class="publicIcon">&nbsp;<i @click="deletechange(index)"
                                       class="el-icon-close"></i>&nbsp;</span>
                            </div>
                        </div>

                        <div class="public"
                             :class="{querySelectorActive:querySelectorClass}"
                             v-show="publicPool">
                            <span class="publicName">&nbsp;&nbsp;资源池：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputPool}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery1"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicPeriod">
                            <span class="publicName">&nbsp;&nbsp;工期：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputPeriod}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery2"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicPOD">
                            <span class="publicName">&nbsp;&nbsp;POD：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputPOD}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery3"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicClassification">
                            <span class="publicName">&nbsp;&nbsp;设备分类：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputClassification}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery4"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicBrand">
                            <span class="publicName">&nbsp;&nbsp;{{fourStep.name}}：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputBrand}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery5"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicState">
                            <span class="publicName">&nbsp;&nbsp;运行状态：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputState}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery6"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicManufactor">
                            <span class="publicName">&nbsp;&nbsp;维保厂家：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputManufactor}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery7"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicIP">
                            <span class="publicName">&nbsp;&nbsp;设备管理IP：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputIP}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery8"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceName">
                            <span class="publicName">&nbsp;&nbsp;设备名称：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceName}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery9"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceType">
                            <span class="publicName">&nbsp;&nbsp;设备类型：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceType}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery10"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceClass3">
                            <span class="publicName">&nbsp;&nbsp;设备三级分类：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceClass3}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery11"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDepartment1">
                            <span class="publicName">&nbsp;&nbsp;一级部门：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDepartment1}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery12"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDepartment2">
                            <span class="publicName">&nbsp;&nbsp;二级部门：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDepartment2}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery13"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceSN">
                            <span class="publicName">&nbsp;&nbsp;设备序列号：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceSN}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery14"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceModel">
                            <span class="publicName">&nbsp;&nbsp;设备型号：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceModel}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery15"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicRoom">
                            <span class="publicName">&nbsp;&nbsp;机房位置：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputRoom}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery16"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeviceProject">
                            <span class="publicName">&nbsp;&nbsp;项目名称：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeviceProject}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery17"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicDeptOperation">
                            <span class="publicName">&nbsp;&nbsp;维护部门：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputDeptOperation}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery18"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicOps">
                            <span class="publicName">&nbsp;&nbsp;维护人员：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputOps}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery19"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicInsertTime">
                            <span class="publicName">&nbsp;&nbsp;录入日期：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputInsertTime.toString()}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery20"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public"
                             v-show="publicBizSystem">
                            <span class="publicName">&nbsp;&nbsp;业务系统：&nbsp;&nbsp;&nbsp;</span>
                            <span class="publicContent">{{inputBizSystem}}</span>
                            <span class="publicIcon">&nbsp;<i @click="changeQuery21"
                                   class="el-icon-close"></i>&nbsp;</span>
                        </div>
                        <div class="public selectedDelete"
                             v-show="allcheckList.length>0">
                            <el-button @click="empty()"
                                       icon="el-icon-delete">清空选项</el-button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 查询结果 -->
            <el-form class="yw-form form"
                     style="padding-right:10px">
                <div class="table-operate-wrap clearfix">
                    <section class="fr">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-d-caret"
                                   v-popover:popover>选择列</el-button>
                        <el-popover ref="popover"
                                    @show="popShow"
                                    @hide="popHide"
                                    placement="top"
                                    trigger="click">
                            <div id="popover"
                                 class="choose-column-popover">
                                <el-checkbox-group v-model="selectColumns"
                                                   @change="handleCheckedColumnChange">
                                    <div v-for="(column,index) in checkColumns"
                                         :key="index">
                                        <el-checkbox :label="column">{{ column.filedName }}</el-checkbox>
                                    </div>
                                </el-checkbox-group>
                            </div>
                        </el-popover>
                    </section>
                    <div style="display:inline-block;vertical-align:middle;"
                         v-if="resetComponent && !this.queryForm.moduleId && buttonPermission.create">
                        <el-menu class="el-menu-button"
                                 menu-trigger='hover'
                                 mode="horizontal"
                                 style="margin-top:-5px">
                            <el-submenu index="1">
                                <template slot="title">
                                    <el-button type="primary"
                                               @click="updateComponent()">新增</el-button>
                                </template>
                                <div style="max-height: 150px;overflow-y: auto">
                                    <template v-for="item in moduleList">
                                        <el-submenu v-if="!item.isDisplay && item.isDisplay === 0"
                                                    :index="item.id"
                                                    :key="item.id">
                                            <template slot="title">
                                                <span class="menu-text">{{item.name}}</span>
                                            </template>
                                            <div style="max-height: 150px;overflow-y: auto">
                                                <template v-for="child in item.childModules">
                                                    <el-submenu v-if="!child.isDisplay && child.isDisplay === 0"
                                                                :index="child.id"
                                                                :key="child.id">
                                                        <template slot="title">
                                                            <span class="menu-text">{{child.name}}</span>
                                                        </template>
                                                        <div style="max-height: 150px;overflow-y: auto">
                                                            <template v-for="child2 in child.childModules">
                                                                <el-submenu v-if="!child2.isDisplay && child2.isDisplay === 0"
                                                                            :index="child2.id"
                                                                            :key="child2.id">
                                                                    <template slot="title">
                                                                        <span class="menu-text"
                                                                              @click="handleSelect(child,child2)">{{child2.name}}</span>
                                                                    </template>
                                                                </el-submenu>
                                                            </template>
                                                        </div>
                                                    </el-submenu>
                                                </template>
                                            </div>
                                        </el-submenu>
                                    </template>
                                </div>
                            </el-submenu>
                        </el-menu>
                    </div>
                    <el-button type="primary"
                               v-if="!(resetComponent && !this.queryForm.moduleId) && buttonPermission.create&&(condicationCode === 'ip_repository_inner' || condicationCode === 'search_ipv6_segment' || condicationCode === 'search_public_segment')"
                               @click="handEmptySelect()">新增</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.update"
                               @click="toUpdate()">编辑</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.assign && (condicationCode !== 'ip_repository_inner' && condicationCode !== 'search_ipv6_segment' && condicationCode !== 'search_public_segment')"
                               @click="distribution('1')">分配</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.unAssign && (condicationCode !== 'ip_repository_inner' && condicationCode !== 'search_ipv6_segment' && condicationCode !== 'search_public_segment')"
                               @click="distribution('2')">取消分配</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.batchUpdate"
                               @click="toBatchUpdate()">批量修正</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.delete&&(condicationCode === 'ip_repository_inner' ||condicationCode === 'search_ipv6_segment' || condicationCode === 'search_public_segment')"
                               @click="toDelete()">删除</el-button>
                    <el-button type="primary"
                               v-if="buttonPermission.import"
                               @click="importInstance()">导入</el-button>
                    <el-button @click="exportData()"
                               v-if="buttonPermission.export">导出</el-button>
                </div>
                <!-- <div class="yw-el-table-wrap"
                     style="height:calc(100vh - 350px);">
                    <el-table class="yw-el-table"
                              ref="viewTable"
                              :header-cell-style="{'height':'120px!import'}"
                              @sort-change="handleSortChange"
                              :data="instanceDataList"
                              v-loading="tableLoading"
                              @header-dragend="expandTable"
                              :default-sort="defaultSort"
                              max-height="80%"
                              style="width:100%;max-height:80%;"
                              stripe
                              border
                              @selection-change="changeCheckbox"> -->
                <div class="yw-el-table-wrap"
                     style="margin-top:10px;">
                    <el-table class="yw-el-table"
                              ref="viewTable"
                              @sort-change="handleSortChange"
                              :data="instanceDataList"
                              v-loading="tableLoading"
                              :default-sort="defaultSort"
                              @row-dblclick="toDetail"
                              :row-style="rowClass"
                              height="calc(100vh - 340px)"
                              style="width:100%"
                              border
                              @selection-change="changeCheckbox">
                        <el-table-column type="selection"
                                         width="50"></el-table-column>
                        <el-table-column label="IP地址"
                                         prop="ip"
                                         v-if="isHost('ip')"
                                         width="120"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <a class="table-link"
                                   @click="toDetail(scope.row)">{{scope.row.ip}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column label="IPV6"
                                         prop="ipv6"
                                         v-if="isHost('ipv6')"
                                         width="140"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <a class="table-link"
                                   @click="toDetail(scope.row)">{{scope.row.ipv6}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="ipv6_segment_owner"
                                         v-if="isHost('ipv6_segment_owner')"
                                         min-width="120"
                                         label="归属网段地址"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                {{scope.row.ipv6_segment_owner}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="device_name"
                                         v-if="isHost('device_name')"
                                         min-width="120"
                                         label="设备名称"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                {{scope.row.device_name}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="idcType"
                                         v-if="isHost('idcType')"
                                         min-width="120"
                                         label="资源池"
                                         sortable
                                         show-overflow-tooltip>
                            <!-- <template slot-scope="scope">
                                {{scope.row.idcType}}
                            </template> -->
                            <template slot-scope="scope">
                                {{refColumns['idcType'] ? scope.row[refColumns['idcType']] : scope.row['idcType'] }}
                            </template>

                        </el-table-column>
                        <el-table-column prop="vlan_number"
                                         v-if="isHost('vlan_number')"
                                         min-width="120"
                                         label="VLAN号"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                {{scope.row.vlan_number}}
                            </template>
                        </el-table-column>
                        <el-table-column v-for="(item,index) in selectColumns"
                                         :key="index"
                                         :prop="item.filedCode"
                                         :label="item.filedName"
                                         width="160"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <!-- <a :class="item.filedCode == 'network_segment_address' ? 'cursor-pointer':''">{{scope.row[item.filedCode]}}</a> -->
                                <!-- <a v-if="segment_address_list.indexOf(item.filedCode) > -1 && (condicationCode == 'ip_repository_inner' || condicationCode == 'search_ipv6_segment' || condicationCode == 'search_public_segment')"
                                   @click="toIpList(scope.row)">{{scope.row[item.filedCode]}}</a>
                                <span v-else>{{scope.row[item.filedCode]}}</span> -->
                                <a v-if="segment_address_list.indexOf(item.filedCode) > -1 && (condicationCode == 'ip_repository_inner' || condicationCode == 'search_ipv6_segment' || condicationCode == 'search_public_segment')"
                                   @click="toIpList(scope.row)"> {{refColumns[item.filedCode] ? scope.row[refColumns[item.filedCode]] : scope.row[item.filedCode] }}</a>
                                <span v-else> {{refColumns[item.filedCode] ? scope.row[refColumns[item.filedCode]] : scope.row[item.filedCode] }}</span>
                            </template>
                        </el-table-column>
                        <!-- <el-table-column prop="update_time"
                                         label="最后更新时间"
                                         min-width="160"
                                         sortable
                                         fixed="right"
                                         show-overflow-tooltip></el-table-column> -->
                        <!-- 变化一样 -->
                        <!--<el-table-column label="操作" width="80">-->
                        <!--<template slot-scope="scope">-->
                        <!--<div class="yw-table-operator">-->
                        <!--<el-button type="text"-->
                        <!--@click="toUpdate(scope.row.id)"-->
                        <!--title="编辑"-->
                        <!--icon="el-icon-edit"></el-button>-->
                        <!--</div>-->
                        <!--</template>-->
                        <!--</el-table-column>-->
                    </el-table>
                    <YwPagination @handleSizeChange="handleSizeChange"
                                  @handleCurrentChange="handleCurrentChange"
                                  :current-page="currentPage"
                                  :page-sizes="pageSizes"
                                  :page-size="pageSize"
                                  :total="total"></YwPagination>
                </div>
                <!-- <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination> -->
            </el-form>
            <!-- 查询结果 -->
            <importInstances ref="importInstances"
                             v-if="display.isImport"
                             :showImport="display.isImport"
                             @setImportDisplay="setImportDisplay"
                             :importType="importType"
                             :moduleType="moduleType"
                             @handleRefresh="handleRefresh"
                             :moduleId="moduleId"
                             :catalogId="catalogId"></importInstances>
        </div>
        <!-- :currModuleId="moduleId" -->
        <BatchEdit v-if="BatchEditDisplay"
                   :batchShowChild="BatchEditDisplay"
                   @batchShowChildS="toBatchUpdate1"></BatchEdit>
    </div>
</template>
<script>
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import updateComponent from 'src/utils/updateComponent.js'
    import conditionManagement from 'src/services/condition_management/query_configuration.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import { getUrlKey } from 'src/assets/js/utility/rb-filters.js'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'

    export default {
        mixins: [QueryObject, updateComponent],
        props: ['queryType', 'parentParams', 'moduleType', 'condicationCode', 'catalogId', 'moduleId', 'ipSingal', 'viewCodeName', 'routerName'],
        components: {
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            BatchEdit: () => import('../../list/batchEdit.vue'),
            // BatchEdit: () => import('../../list/batch_edit.vue'),
            // Tab: () => import('../tab/view'),
            // Info: () => import('../tab/info'),
        },
        data() {
            return {
                segment_address_list: ['network_segment_address', 'ipv6_segment_address', 'public_segment_address'],
                otherListCheck: false,
                newonestep1: false,
                newonestep2: false,
                newonestep3: false,
                newonestep4: false,
                reFresh: true,
                loading: false,
                tableLoading: false,
                loading_text: '正在加载数据...',
                downtshow: false,
                defaultSort: {},
                newCurrentOrder: null,
                fixedColumns: {},
                boraderActive: false,
                aLinePublic1: true,
                aLinePublic2: true,
                pool3: false,
                BatchEditDisplay: false,
                tableNewId: '',
                devicemfrsValue: '',
                initdata: [],
                onehead: null,
                twohead: null,
                threehead: null,
                fourhead: null,
                allcheckList: [],
                allbottomList: [],
                deviceClassB: false,
                deviceClassUp: false,
                pool2: false,
                poolB2: false,
                poolUP2: false,
                newlastIndex: 0,
                toplastIndex: false,
                lastIndex: false,
                reduceNumber: 0,
                newMoreDown: false,
                oneStep: {
                    show: false,
                    name: ''
                },
                oneStep1: {
                    show: false,
                    name: ''
                },
                oneStep2: {
                    show: false,
                    name: ''
                },
                twoStep: {
                    show: false,
                    name: ''
                },
                threeStep: {
                    show: false,
                    name: ''
                },
                fourStep: {
                    show: false,
                    name: ''
                },
                otherList: [],
                reducelist: [],
                //  入参
                queryhead: {
                    condication: {
                        id: '',
                        condicationType: '系统菜单'
                    },
                    queryParams: {
                        params: [
                            {
                                filed: 'idcType',
                                value: '全部'
                            }
                        ],
                        sort: [{  // 排序信息设置
                            filed: '排序字段',
                            type: '排序方式'
                        }]
                    },
                    moduleId: 'xxxxx',
                    pageSize: 50,
                    currentPage: 1
                },
                querySelectorClass: false,
                ImgSelected: '',
                imgIndex: '',
                overviewShow: true,
                poolID: null,
                PODID: null,
                selectedShow: false,

                publicPool: false,
                publicPeriod: false,
                publicPOD: false,
                publicClassification: false,
                publicBrand: false,
                publicState: false,
                publicManufactor: false,
                publicIP: false,
                publicDeviceName: false,
                publicDeviceType: false,
                publicDeviceClass3: false,
                publicDepartment1: false,
                publicDepartment2: false,
                publicDeviceSN: false,
                publicDeviceModel: false,
                publicRoom: false,
                publicDeviceProject: false,
                publicDeptOperation: false,
                publicOps: false,
                publicInsertTime: false,
                publicBizSystem: false,

                inputPool: '',
                inputPeriod: '',
                inputPOD: '',
                inputClassification: '',
                inputBrand: '',
                inputState: '',
                inputManufactor: '',
                inputIP: '',
                inputDeviceName: '',
                inputDeviceType: '',
                inputDeviceClass3: '',
                inputDepartment1: '',
                inputDepartment2: '',
                inputDeviceSN: '',
                inputDeviceModel: '',
                inputRoom: '',
                inputDeviceProject: '',
                inputDeptOperation: '',
                inputOps: '',
                inputInsertTime: '',
                inputBizSystem: '',

                pool: false,
                poolB: false,
                poolUP: false,
                POD: false,
                PODB: false,
                PODBUP: false,
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
                activeIndex: '1',
                queryParams: this.parentParams || {},
                queryKeys: ['moduleId', 'idcType', 'pod', 'deviceClass', 'deviceClass3', 'deviceType', 'deviceModel', 'department1',
                    'department2', 'bizSystem', 'ip', 'deviceName', 'deviceSN', 'room', 'deviceProject', 'deviceStatus',
                    'maintenanceFactory', 'mfrFactory', 'startInsertTime', 'endInsertTime', 'deptOperation', 'ops', 'projectName', 'moduleName'],
                pod: false,
                importType: 'ip_repository',
                display: {
                    isImport: false,
                    isEdit: false,
                    isSearchPlane: false,
                    isBatchUpdate: false
                },
                conditionShow: true,
                inputHome: false,
                inputShow: false,
                insertTime: '',
                queryForm: {
                    moduleId: this.moduleId,// '7e6a5f63a0204b92bd783508eed0818d',
                    idcType: '',
                    newidcType: ['全部'],
                    newdeviceProject: ['全部'],// 工期
                    projectName: '', // 工期
                    deviceStatus: '', // 运行状态
                    pod: '',
                    newpod: ['全部'],
                    newdeviceClass: ['全部'],
                    deviceClass: '',
                    deviceClass3: '',
                    deviceType: '',
                    deviceModel: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    ip: '',
                    deviceName: '',
                    deviceSN: '',
                    room: '',
                    deviceProject: '',
                    maintenanceFactory: '',
                    mfrFactory: '',
                    startInsertTime: '',
                    endInsertTime: '',
                    deptOperation: '',
                    ops: ''
                },
                refModuleFileds: {
                    'idcType': 'poolList', // 资源池列表
                    'project_name': 'projectNameList', // 工期
                    'pod_name': 'podList', // POD池列表
                    'bizSystem': 'bizSystemList',
                    'device_class': 'deviceClassList'
                },
                refModuleStorageFileds: {
                    'idcType': 'poolListStorage', // 资源池列表
                    'project_name': 'projectNameListStorage', // 工期
                    'pod_name': 'podListStorage', // POD池列表
                    'bizSystem': 'bizSystemListStorage',
                    'device_class': 'deviceClassListStorage'
                },
                allDictList: [], // 所有的设备字典
                poolList: [], // 资源池列表
                projectNameList: [], // 工期
                factoryList: [], // 维护厂家列表
                runningStateList: [], // 运行状态
                deviceClassList: [], // 设备分类列表
                deviceClass3List: [], // 设备三级分类列表
                deviceTypeList: [], // 设备类型列表
                podList: [], // POD池列表
                department1List: [], // 一级部门列表
                department2List: [], // 二级部门列表
                roomList: [], // 机房位置
                bizSystemList: [], // 业务系统列表
                deviceStatusList: [], // 设备运行状态
                maintenanceFactoryList: [], // 维保厂家
                mfrFactoryList: [], // 品牌
                brandList: [], // 设备品牌
                checkColumns: [], // 选择列-所有数据
                selectColumns: [], // 选择列-选中项
                moduleColumns: [], // 模型码表数据
                column_data: {},
                columnList: [],
                refColumns: {},
                dbFilterData: {}, // 数据中保存的选择列值
                instanceDataList: [], // 主机数据集合
                cloneData: [], // 主机数据集合克隆
                multipleSelection: [],
                multipleSelectionAll: [],
                paginationData: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 50,
                    selectPageSizes: [30, 50, 100],
                    sort: 'insert_time',
                    order: 'desc'
                },
                moduleList: [], // 模型列表
                currentModuleInfo: null,  // 当前模型信息
                parentModuleInfo: null, // 当前父模型信息
                buttonPermission: {}, // 按钮的操作权限
                selectRow: []
            }
        },
        computed: {

        },
        watch: {
            // checkColumns: {
            //     handler(newName, oldName) {
            //     },
            //     immediate: true,
            //     deep: true
            //   },
            moduleType(val) {
                if (val === 'business') {
                    this.fixedColumns = ['业务名称', '创建时间', '最后更新时间']
                } else if (val === 'dict') {
                    this.fixedColumns = ['创建时间', '最后更新时间']
                } else {
                    this.fixedColumns = ['管理IP地址', '设备名称', '创建时间', '最后更新时间']
                }
            },
            parentParams(val) {
                console.log(val, 'parentParams')
                console.log(this.reducelist)
                console.log(this.otherList)
                // 先清掉
                this.empty(true)
                this.allcheckList = []
                let isGo = false
                let reducelist = this.reducelist
                let otherList = this.otherList
                let typeFix = ['idcType', 'pod', 'deviceProject', 'deviceClass']
                if (val.all.type) {

                    if (val.all.type['idcType']) {
                        console.log(this.poolList, 'poolList')
                        let poolListnew = []
                        for (let i = 0; i < this.poolList.length; i++) {
                            poolListnew.push(this.poolList[i].value)
                        }
                        if (poolListnew.includes(val.all.type['idcType'])) {
                            console.log('进入232323')
                            isGo = true
                            this.jilianclick(val.all.type['idcType'], val.all.id, 'idcType', this.idcTypeArr, this.onehead, { id: val.all.id, key: val.all.type['idcType'], value: val.all.type['idcType'], valueId: val.all.tree.queryToData['idcType'] || '' }, false, 'treeover', 'isTreeClick')
                        } else {
                            this.allcheckList.push({ father: this.onehead, son: { id: val.all.id, key: val.all.type['idcType'], value: val.all.type['idcType'] }, newson: [{ id: val.all.id, key: val.all.type['idcType'], value: val.all.type['idcType'], valueId: val.all.tree.queryToData['idcType'] || '' }] })

                        }
                    }
                    if (val.all.type['deviceProject']) {
                        console.log(this.projectNameList)
                        let poolListnew = []
                        for (let i = 0; i < this.projectNameList.length; i++) {
                            poolListnew.push(this.projectNameList[i].value)
                        }
                        if (poolListnew.includes(val.all.type['deviceProject'])) {
                            isGo = true
                            this.jilianclick(val.all.type['deviceProject'], val.all.id, 'deviceProject', this.projectnameArr, this.twohead, { id: val.all.id, key: val.all.type['deviceProject'], value: val.all.type['deviceProject'], valueId: val.all.tree.queryToData['deviceProject'] || '' }, false, 'treeover', 'isTreeClick')
                        } else {
                            this.allcheckList.push({ father: this.twohead, son: { id: val.all.id, key: val.all.type['deviceProject'], value: val.all.type['deviceProject'] }, newson: [{ id: val.all.id, key: val.all.type['deviceProject'], value: val.all.type['deviceProject'], valueId: val.all.tree.queryToData['deviceProject'] || '' }] })
                        }
                    }
                    if (val.all.type['pod']) {
                        console.log(this.podList)
                        let poolListnew = []
                        for (let i = 0; i < this.podList.length; i++) {
                            poolListnew.push(this.podList[i].value)
                        }
                        if (poolListnew.includes(val.all.type['pod'])) {
                            isGo = true
                            this.jilianclick(val.all.type['pod'], val.all.id, 'pod', this.podnameArr, this.threehead, { id: val.all.id, key: val.all.type['pod'], value: val.all.type['pod'], valueId: val.all.tree.queryToData['pod'] || '' }, false, 'treeover', 'isTreeClick')
                        } else {
                            this.allcheckList.push({ father: this.threehead, son: { id: val.all.id, key: val.all.type['pod'], value: val.all.type['pod'] }, newson: [{ id: val.all.id, key: val.all.type['pod'], value: val.all.type['pod'], valueId: val.all.tree.queryToData['pod'] || '' }] })
                        }
                    }
                    if (val.all.type['deviceClass']) {
                        console.log(this.deviceClassList)
                        let poolListnew = []
                        for (let i = 0; i < this.deviceClassList.length; i++) {
                            poolListnew.push(this.deviceClassList[i].value)
                        }
                        if (poolListnew.includes(val.all.type['deviceClass'])) {
                            isGo = true
                            this.jilianclick(val.all.type['deviceClass'], val.all.id, 'deviceClass', this.deviceTypeArr, this.fourhead, { id: val.all.id, key: val.all.type['deviceClass'], value: val.all.type['deviceClass'], valueId: val.all.tree.queryToData['deviceClass'] || '' }, false, 'treeover', 'isTreeClick')
                        } else {
                            this.allcheckList.push({ father: this.fourhead, son: { id: val.all.id, key: val.all.type['deviceClass'], value: val.all.type['deviceClass'] }, newson: [{ id: val.all.id, key: val.all.type['deviceClass'], value: val.all.type['deviceClass'], valueId: val.all.tree.queryToData['deviceClass'] || '' }] })
                        }
                    }
                    console.log(this.allcheckList, otherList, reducelist, 'otherList')
                    for (let k in val.all.type) {
                        if (!typeFix.includes(k)) {
                            console.log('田')
                            for (let m = 0; m < otherList.length; m++) {
                                if (k == otherList[m].id) {
                                    isGo = true
                                    console.log('田小姐')
                                    let cuid = ''
                                    for (let n = 0; n < otherList[m].two.length; n++) {
                                        if (otherList[m].two[n].value == val.all.type[k]) {
                                            cuid = otherList[m].two[n].id
                                        }
                                    }
                                    console.log(cuid, val.all.tree.queryToData[k], 'cuid')
                                    if (otherList[m].one.cmdbCode.controlType.controlCode == 'cascader') {
                                        this.commonsql(val.all.type[k], cuid, otherList[m], m, '1', { id: cuid, key: val.all.type[k], value: val.all.type[k], valueId: val.all.tree.queryToData[k] || '' }, 'noneww', 'treeover', 'isTreeClick')
                                    } else {
                                        this.commonsql(val.all.type[k], cuid, otherList[m], m, '1', { id: cuid, key: val.all.type[k], value: val.all.type[k], valueId: val.all.tree.queryToData[k] || '' }, 'none', 'treeover', 'isTreeClick')
                                    }
                                }
                            }
                            for (let m = 0; m < reducelist.length; m++) {
                                if (k == reducelist[m].id) {
                                    if (k == reducelist[m].id) {
                                        console.log('高小姐')
                                        isGo = true
                                        let cuid = ''
                                        for (let n = 0; n < reducelist[m].two.length; n++) {
                                            if (reducelist[m].two[n].value == val.all.type[k]) {
                                                cuid = reducelist[m].two[n].id
                                            }
                                        }
                                        if (reducelist[m].one.cmdbCode.controlType.controlCode == 'cascader') {
                                            this.commonsql(val.all.type[k], cuid, reducelist[m], m, '2', { id: cuid, key: val.all.type[k], value: val.all.type[k], valueId: val.all.tree.queryToData[k] || '' }, 'noneww', 'treeover', 'isTreeClick')
                                        } else {
                                            this.commonsql(val.all.type[k], cuid, reducelist[m], m, '2', { id: cuid, key: val.all.type[k], value: val.all.type[k], valueId: val.all.tree.queryToData[k] || '' }, 'none', 'treeover', 'isTreeClick')
                                        }
                                    }
                                }
                            }
                            if (!isGo) {
                                console.log('近年来')
                                let objnew = {}
                                objnew.cmdbCode = {}
                                objnew.cmdbCode.filedCode = k
                                objnew.cmdbCode.filedName = k
                                this.allcheckList.push({ father: objnew, son: { id: val.all.id, key: val.all.type[k], value: val.all.type[k] }, newson: [{ id: val.all.id, key: val.all.type[k], value: val.all.type[k], valueId: val.all.tree.queryToData[k] || '' }] })
                            }

                        }

                    }
                    console.log(this.allcheckList, '9999')
                    if (this.allcheckList.length > 0) {
                        this.selectedShow = true
                    } else {
                        this.selectedShow = false
                    }
                    if (!isGo) {
                        // console.log('都没有')
                        // this.empty(false)
                        this.currentPage = 1
                        this.query()
                    } else {
                        this.currentPage = 1
                        this.query()
                    }
                    this.initParams()
                    if (val.moduleId) {
                        // this.queryForm.moduleId = val.moduleId
                        this.queryInstanceHeader()
                    }
                    this.multipleSelectionAll = []

                } else {
                    this.devicemfrsValue = val.mfrFactory
                    this.queryParams = val
                    if (val.department1 && val.department2 && val.bizSystem && val.deviceType && val.moduleId) {
                        this.publicDeviceType = true
                        this.publicBizSystem = true
                        this.publicDepartment2 = true
                        this.publicDepartment1 = true
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                        this.inputDepartment2 = val.department2
                        this.inputBizSystem = val.bizSystem
                        this.inputDeviceType = val.deviceType
                    } else if (val.department1 && val.department2 && val.bizSystem && val.deviceType === '') {
                        this.publicBizSystem = true
                        this.publicDepartment2 = true
                        this.publicDepartment1 = true
                        this.publicDeviceType = false
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                        this.inputDepartment2 = val.department2
                        this.inputBizSystem = val.bizSystem
                    } else if (val.department1 && val.department2 === '' && val.bizSystem && val.deviceType === '') {
                        this.queryDepartment2(val.department1)
                        this.publicBizSystem = true
                        this.publicDepartment2 = false
                        this.publicDepartment1 = true
                        this.publicDeviceType = false
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                        this.inputDepartment2 = val.department2
                        this.inputBizSystem = val.bizSystem
                    } else if (val.department1 && val.department2 === '' && val.bizSystem && val.deviceType) {
                        this.publicBizSystem = true
                        this.publicDepartment2 = false
                        this.publicDepartment1 = true
                        this.publicDeviceType = true
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                        this.inputDepartment2 = val.department2
                        this.inputBizSystem = val.bizSystem
                        this.inputDeviceType = val.deviceType
                    } else if (val.department1 && val.department2 && val.bizSystem === '') {
                        this.queryBizSystem(val.businessUuid2)
                        this.publicDepartment2 = true
                        this.publicDepartment1 = true
                        this.publicBizSystem = false
                        this.publicDeviceType = false
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                        this.inputDepartment2 = val.department2
                    } else if (val.department1 && val.department2 === '') {
                        this.queryDepartment2(val.businessUuid)
                        this.publicDepartment1 = true
                        this.publicDepartment2 = false
                        this.publicBizSystem = false
                        this.publicDeviceType = false
                        this.selectedShow = true
                        this.inputDepartment1 = val.department1
                    } else if (val.mfrFactory && val.deviceType) {
                        this.publicDeviceType = true
                        this.publicBrand = true
                        this.selectedShow = true
                        this.queryForm.deviceType = val.deviceType
                        this.inputDeviceType = val.deviceType
                        this.inputBrand = val.mfrFactory
                    } else if (val.mfrFactory) {
                        this.ImgSelected = ''
                        this.queryForm.mfrFactory = val.mfrFactory
                        this.publicBrand = true
                        this.publicDeviceType = false
                        this.selectedShow = true
                        this.inputBrand = val.mfrFactory
                    } else if (val.deviceType && val.moduleId && val.deviceClass) {
                        this.publicClassification = true
                        this.publicDeviceType = true
                        this.selectedShow = true
                        this.inputClassification = val.deviceClass
                        this.inputDeviceType = val.deviceType
                    } else if (val.deviceType && val.deviceClass === '') {
                        this.publicDeviceType = true
                        this.selectedShow = true
                        this.inputDeviceType = val.deviceType
                    } else if (val.deviceClass === '系统软件' || val.deviceClass === '安全设备' || val.deviceClass === '存储设备' || val.deviceClass === '网络设备' || val.deviceClass === '服务器' || val.deviceClass === '机房设备') {
                        this.publicClassification = true
                        this.publicDeviceType = false
                        this.selectedShow = true
                        // this.inputDeviceType = val.deviceClass
                        this.inputClassification = val.deviceClass
                    }

                    this.initParams()
                    if (val.moduleId) {
                        // this.queryForm.moduleId = val.moduleId
                        this.queryInstanceHeader()
                    }
                    this.multipleSelectionAll = []
                    this.currentPage = 1
                    this.query()
                }

            },
            queryParams(val) {
                this.queryDeviceClass3(val.deviceType)
                // this.queryDepartment2(val.department1)
                // this.queryBizSystem(val.department2 || val.department1)
            },
            multipleSelection(data) {
                this.selectRow = []
                if (data.length > 0) {
                    data.forEach((item) => {
                        this.selectRow.push(this.instanceDataList.indexOf(item))
                    })
                }
            }
        },

        mounted: function () {
            this.showLoading('正在加载数据，请稍等...')
            // 初始化参数
            this.initParams()
            // 设置请求地址中携带的参数信息
            this.initUrlParams()
            // 获取按钮权限
            this.initPermission()
            // 获取当前模型的过滤列
            this.getColumnFilter()
            if (this.$route.query.parentParams) {
                this.queryParams = this.$route.query.parentParams
            }
            // 获取表格数据信息
            // this.searchInstanceList()
            if (!this.queryForm.moduleId) {
                this.queryModuleList()
            }

            // 如果有模型ID信息
            this.queryForm.moduleId = this.moduleId// '7e6a5f63a0204b92bd783508eed0818d'
            console.log(this.queryForm.moduleId)
            if (this.queryForm.moduleId) {
                // 查询模型信息
                rbCmdbModuleServiceFactory.getParentInfo({ 'module_id': this.queryForm.moduleId }).then((data) => function () {
                    if (data) {
                        this.currentModuleInfo = {
                            module_id: data.module_id,
                            module_name: data.module_name
                        }
                        this.parentModuleInfo = {
                            module_id: data.parent_id,
                            module_name: data.parent_name
                        }
                    }
                })
            }
            this.headlist()
        },
        methods: {
            // 刷新组件
            // newreFresh() {
            //     console.log('刷新')
            //     this.reFresh = false
            //     this.$nextTick(() => {
            //         this.reFresh = true
            //     })
            // },
            initUrlParams() {
                if (getUrlKey('module_id')) {
                    this.queryForm.moduleId = getUrlKey('module_id')
                }
                if (getUrlKey('module_type')) {
                    this.moduleType = getUrlKey('module_type')
                }
                if (getUrlKey('condicationCode')) {
                    this.condicationCode = getUrlKey('condicationCode')
                }
                if (getUrlKey('catalogId')) {
                    this.catalogId = getUrlKey('catalogId')
                }
            },
            handleSortChange(val) {
                console.log(val)
                this.currentPage = 1
                this.newCurrentValue = val.prop

                if (val.order == 'ascending') {
                    this.newCurrentOrder = 'asc'
                    this.newCurrentOrderT = val.order
                    this.query(false)
                } else if (val.order == 'descending') {
                    this.newCurrentOrder = 'desc'
                    this.newCurrentOrderT = val.order
                    this.query(false)
                } else {
                    this.newCurrentOrderT = val.order
                    this.query(true)
                }
            },
            // BatchEditDisplayClick(data) {
            //     console.log('data', data)
            //     this.BatchEditDisplay = data
            // },
            // 设备品牌
            Brand(item, index, info) {
                console.log(info)
                console.log(item)
                console.log(index)
                // this.devicemfrsValue = info.type
                this.devicemfrsValue = item
                this.ImgSelected = index
                this.inputBrand = item
                this.queryForm.mfrFactory = item
                this.publicBrand = true
                this.selectedShow = true
                this.currentPage = 1
                // this.searchInstanceList()
                this.query()
            },
            query(activePagination = true) {
                console.log(this.tableNewId)
                console.log(this.allcheckList, 'allcheckListallcheckList')
                let allcheckList = this.allcheckList
                let resdata
                if (this.newCurrentOrderT == null) {
                    resdata = this.getQueryParams(activePagination)
                } else {
                    resdata = this.getQueryParams(false)
                }
                if (this.ipSingal == 'all') {
                    resdata = { ...resdata, network_segment_address: '' }
                } else if (this.ipSingal != '') {
                    resdata = { ...resdata, network_segment_address: this.ipSingal }
                }

                for (let i = 0; i < allcheckList.length; i++) {
                    let innerArr = allcheckList[i].newson
                    let innerStr = ''
                    for (let j = 0; j < innerArr.length; j++) {
                        if (j == 0) {
                            // innerStr = innerArr[j].value
                            innerStr = innerArr[j].valueId ? innerArr[j].valueId : innerArr[j].value
                        } else {
                            // innerStr = innerStr + ',' + innerArr[j].value
                            innerStr = innerStr + ',' + (innerArr[j].valueId ? innerArr[j].valueId : innerArr[j].value)
                        }
                    }
                    resdata[allcheckList[i].father.cmdbCode.filedCode] = innerStr
                }
                if (this.publicBrand) {
                    resdata['device_mfrs'] = this.devicemfrsValue
                }
                console.log(this.ipSingal)
                console.log(resdata)
                this.showLoading('正在加载数据，请稍等...')
                this.$emit('resdata', resdata)
                rbCmdbServiceFactory.getInstanceList(resdata).then((data) => {
                    this.total = data.totalSize
                    this.instanceDataList = data.data
                    // this.columnList = data.columnList
                    this.columnList = data.columns
                    for (const key in this.columnList) {
                        const item = this.columnList[key]
                        if (item.type === 'ref') {
                            this.$set(this.refColumns, item.filed_code, item.ref_name)
                        }
                    }
                    console.log(this.queryForm.idcType)
                }).finally(() => {
                    this.hideLoading()
                })
            },
            tablist() {
                console.log('tablist')
                console.log(this.allcheckList)
                let allcheckList = this.allcheckList
                let resdata = this.getQueryParams()
                for (let i = 0; i < allcheckList.length; i++) {
                    resdata[allcheckList[i].father.cmdbCode.filedCode] = allcheckList[i].son.valueId ? allcheckList[i].son.valueId : allcheckList[i].son.value
                }
                if (this.publicBrand) {
                    resdata['device_mfrs'] = this.devicemfrsValue
                }
                this.showLoading('正在加载数据，请稍等...')
                this.$emit('resdata', resdata)
                rbCmdbServiceFactory.getInstanceList(resdata).then((data) => {
                    this.total = data.totalSize
                    this.instanceDataList = data.data
                    // this.columnList = data.columnList
                    this.columnList = data.columns
                    for (const key in this.columnList) {
                        const item = this.columnList[key]
                        if (item.type === 'ref') {
                            this.$set(this.refColumns, item.filed_code, item.ref_name)
                        }
                    }
                }).finally(() => {
                    this.hideLoading()
                })
            },
            deletechange(index) {
                console.log(this.otherList)
                console.log(this.allcheckList)
                if (this.allcheckList[index].father.cmdbCode.filedCode == 'idcType') {
                    this.queryForm.idcType = '全部'
                    this.queryForm.newidcType = ['全部']
                } else if (this.allcheckList[index].father.cmdbCode.filedCode == 'project_name') {
                    this.queryForm.deviceProject = '全部'
                    this.queryForm.newdeviceProject = ['全部']
                } else if (this.allcheckList[index].father.cmdbCode.filedCode == 'pod_name') {
                    this.queryForm.pod = '全部'
                    this.queryForm.newpod = ['全部']
                } else if (this.allcheckList[index].father.cmdbCode.filedCode == 'device_class') {
                    this.queryForm.deviceClass = '全部'
                    this.queryForm.newdeviceClass = ['全部']
                } else {
                    for (let i = 0; i < this.otherList.length; i++) {
                        if (this.otherList[i].one.cmdbCode.filedCode == this.allcheckList[index].father.cmdbCode.filedCode) {
                            this.otherList[i].color = '全部'
                            this.otherList[i].newcolor = ['全部']
                            this.otherList[i].value = ''
                        }
                    }
                    for (let i = 0; i < this.reducelist.length; i++) {
                        if (this.reducelist[i].one.cmdbCode.filedCode == this.allcheckList[index].father.cmdbCode.filedCode) {
                            this.reducelist[i].color = '全部'
                            this.reducelist[i].newcolor = ['全部']
                            this.reducelist[i].value = ''
                        }
                    }
                }

                this.allcheckList.splice(index, 1)
                this.currentPage = 1
                this.query()
            },
            commonsql(value, id, item, index, type, info, ishas, treeCheck, clickType) {
                console.log(value, id, item, index, type, 'info:' + JSON.stringify(info), ishas, treeCheck, clickType, 'infoinfo')
                console.log(id)
                console.log(value)
                console.log(item)
                console.log(index)
                console.log(type)
                console.log(info)
                this.selectedShow = true
                let jsonfield = {}

                jsonfield.father = item.one
                jsonfield.son = info
                jsonfield.newson = []
                jsonfield.newson.push(info)
                let arr = item.one.cmdbCode.cascadeList
                console.log(arr)
                let allcheckList = this.allcheckList
                console.log(allcheckList)
                let checkboll = false
                let checkbollnew = false
                if (!info.valueId) {
                    info.valueId = info.id
                }
                for (let i = 0; i < allcheckList.length; i++) {
                    if (allcheckList[i].father.cmdbCode.filedCode == item.one.cmdbCode.filedCode) {
                        allcheckList[i].son = info
                        if (ishas == 'none') {
                            if (allcheckList[i].newson.length > 0) {
                                let tresarr = allcheckList[i].newson
                                for (let k = 0; k < tresarr.length; k++) {
                                    if (tresarr[k].value == info.value) {
                                        checkbollnew = true
                                        // eslint-disable-next-line no-empty
                                        if (item.one.cmdbCode.controlType.controlCode != 'cascader' && item.one.cmdbCode.controlType.controlCode != 'listSel') {

                                        } else {
                                            allcheckList[i].newson.splice(k, 1)
                                        }
                                    }
                                }
                                if (!checkbollnew) {
                                    allcheckList[i].newson.push(info)
                                }
                            } else {
                                allcheckList[i].newson.push(info)
                            }
                        } else {
                            allcheckList[i].newson = [info]
                        }

                        checkboll = true
                    } else {
                        console.log('')
                    }
                    if (arr.length > 0) {
                        for (let j = 0; j < arr.length; j++) {
                            console.log(allcheckList[i])
                            if (allcheckList[i] == undefined) {
                                console.log('')
                            } else {
                                if (arr[j].subFiledCode == allcheckList[i].father.cmdbCode.filedCode) {
                                    allcheckList.splice(i, 1)
                                }
                            }
                        }
                    }
                }
                if (!checkboll) {
                    this.allcheckList.push(jsonfield)
                } else {
                    this.allcheckList = allcheckList
                }
                for (let i = 0; i < this.allcheckList.length; i++) {
                    if (this.allcheckList[i].son.value == '全部') {
                        this.allcheckList.splice(i, 1)
                    }
                }
                // this.allcheckList = allcheckListnew
                // this.allcheckList.push(jsonfield)
                console.log(info)
                if (type == '1') {
                    if (ishas == 'none') {
                        if (value == '全部') {
                            this.otherList[index].newcolor = ['全部']
                        } else {
                            if (item.one.cmdbCode.controlType.controlCode != 'cascader' && item.one.cmdbCode.controlType.controlCode != 'listSel') {
                                console.log('已经进来')
                                this.otherList[index].value = value
                            } else {
                                if (this.otherList[index].newcolor.indexOf('全部') > -1) {
                                    this.otherList[index].newcolor.splice(this.otherList[index].newcolor.indexOf('全部'), 1)
                                }
                                if (this.otherList[index].newcolor.indexOf(value) > -1) {
                                    this.otherList[index].newcolor.splice(this.otherList[index].newcolor.indexOf(value), 1)
                                } else {
                                    this.otherList[index].newcolor.push(value)
                                }
                                console.log(this.otherList[index].newcolor)
                                if (this.otherList[index].newcolor.length == 0) {
                                    this.otherList[index].newcolor = ['全部']
                                }
                            }
                        }
                    } else {
                        this.otherList[index].color = value
                    }
                } else {
                    if (ishas == 'none') {
                        if (value == '全部') {
                            this.reducelist[index].newcolor = ['全部']
                        } else {
                            if (item.one.cmdbCode.controlType.controlCode != 'cascader' && item.one.cmdbCode.controlType.controlCode != 'listSel') {
                                console.log('已经进来')
                                this.reducelist[index].value = value
                            } else {
                                if (this.reducelist[index].newcolor.indexOf('全部') > -1) {
                                    this.reducelist[index].newcolor.splice(this.reducelist[index].newcolor.indexOf('全部'), 1)
                                }
                                if (this.reducelist[index].newcolor.indexOf(value) > -1) {
                                    this.reducelist[index].newcolor.splice(this.reducelist[index].newcolor.indexOf(value), 1)
                                } else {
                                    this.reducelist[index].newcolor.push(value)
                                }
                                console.log(this.reducelist[index].newcolor)
                                if (this.reducelist[index].newcolor.length == 0) {
                                    this.reducelist[index].newcolor = ['全部']
                                }
                            }
                        }
                    } else {
                        this.reducelist[index].color = value
                    }
                }
                // if (type == '1') {
                //     this.otherList[index].color = value
                // } else {
                //     this.reducelist[index].color = value
                // }
                let otherList = this.otherList
                let reducelist = this.reducelist
                console.log(this.otherList)
                console.log(this.reducelist)

                for (let i = 0; i < arr.length; i++) {
                    let sqlstr = arr[i].sqlString
                    sqlstr = sqlstr.replace('?', id)
                    let sigalArr = arr[i]

                    // idcType
                    if (sigalArr.subFiledCode == 'idcType') {
                        if (value == '全部---') {
                            this.poolList = this.fillSelectOption(this.poolListStorage, 'idcType')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {

                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.idcType = '全部'
                                    this.queryForm.newidcType = ['全部']
                                }
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.poolList = this.fillSelectOption(res, 'idcType')
                                } else {
                                    this.poolList = res
                                }

                                console.log(res)
                                // if (this.poolList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.poolB = true
                                // }
                            })
                        }

                    }
                    // project_name
                    if (sigalArr.subFiledCode == 'project_name') {
                        if (value == '全部---') {
                            this.projectNameList = this.fillSelectOption(this.projectNameListStorage, 'project_name')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {

                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.deviceProject = '全部'
                                    this.queryForm.newdeviceProject = ['全部']
                                }
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.projectNameList = this.fillSelectOption(res, 'project_name')
                                } else {
                                    this.projectNameList = res
                                }
                                console.log(res)
                                // if (this.projectNameList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.poolB2 = true
                                // }
                            })
                        }
                    }
                    // pod_name
                    if (sigalArr.subFiledCode == 'pod_name') {
                        if (value == '全部') {
                            this.podList = this.fillSelectOption(this.podListStorage, 'pod_name')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {

                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.pod = '全部'
                                    this.queryForm.newpod = ['全部']
                                }
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.podList = this.fillSelectOption(res, 'pod_name')
                                } else {
                                    this.podList = res
                                }
                                console.log(res)
                                // if (this.podList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.PODB = true
                                // }
                            })
                        }
                    }
                    // device_type
                    if (sigalArr.subFiledCode == 'device_class') {
                        if (value == '全部---') {
                            this.deviceClassList = this.fillSelectOption(this.deviceClassListStorage, 'device_class')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.deviceClass = '全部'
                                    this.queryForm.newdeviceClass = ['全部']
                                }
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.deviceClassList = this.fillSelectOption(res, 'device_class')
                                } else {
                                    this.deviceClassList = res
                                }
                                console.log(res)
                                // if (this.deviceClassList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.deviceClassB = true
                                // }
                            })
                        }
                    }
                    // device_mfrs
                    if (sigalArr.subFiledCode == 'device_mfrs') {
                        if (value == '全部') {
                            this.brandList = this.fillSelectOption(this.brandListStorage, 'device_mfrs')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.mfrFactory = ''
                                }
                                this.brandList = res
                                // if (this.brandList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.equipmentBrandB = true
                                // }
                            })
                        }
                    }
                    console.log(sigalArr.subFiledCode)
                    for (let j = 0; j < otherList.length; j++) {
                        if (otherList[j].id == sigalArr.subFiledCode) {
                            console.log(value)
                            if (value == '全部---') {
                                console.log('看是否进来')
                                // 修复全部没有复位
                                this.otherList[j].color = '全部'
                                this.otherList[j].two = this.fillSelectOption(this.otherList[j].three, sigalArr.subFiledCode)
                            } else {
                                console.log(sqlstr)
                                this.rbHttp.sendRequest({
                                    method: 'POST',
                                    data: { 'sql': sqlstr },
                                    url: '/v1/cmdb/module/queryTableData'
                                }).then((res) => {
                                    console.log(res)
                                    if (treeCheck !== 'treeover') {
                                        this.otherList[j].color = '全部'
                                    }
                                    // this.otherList[j].color = '全部'
                                    this.otherList[j].newcolor = ['全部']
                                    if (JSON.stringify(res) == '[]') {
                                        if (treeCheck !== 'treeover') {
                                            this.otherList[j].color = '全部'
                                        }
                                        // this.otherList[j].color = '全部'
                                        this.otherList[j].newcolor = ['全部']
                                    }
                                    let newHasAll = false
                                    for (let l = 0; l < res.length; l++) {
                                        if (res[l].value == '全部') {
                                            newHasAll = true
                                        }
                                    }
                                    console.log(newHasAll)
                                    if (!newHasAll) {
                                        this.otherList[j].two = this.fillSelectOption(res, sigalArr.subFiledCode)
                                    } else {
                                        this.otherList[j].two = res
                                    }

                                    // if (this.otherList[j].two.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                    //   this.otherList[j].popup = true
                                    // }
                                })
                            }
                        }
                    }
                    for (let j = 0; j < reducelist.length; j++) {
                        if (reducelist[j].id == sigalArr.subFiledCode) {
                            if (value == '全部---') {
                                this.reducelist[j].color = '全部'
                                this.reducelist[j].two = this.fillSelectOption(this.reducelist[j].three, sigalArr.subFiledCode)
                            } else {
                                this.rbHttp.sendRequest({
                                    method: 'POST',
                                    data: { 'sql': sqlstr },
                                    url: '/v1/cmdb/module/queryTableData'
                                }).then((res) => {
                                    if (treeCheck !== 'treeover') {
                                        this.otherList[j].color = '全部'
                                    }
                                    // this.reducelist[j].color = '全部'
                                    this.reducelist[j].newcolor = ['全部']
                                    if (JSON.stringify(res) == '[]') {
                                        if (treeCheck !== 'treeover') {
                                            this.otherList[j].color = '全部'
                                        }
                                        // this.reducelist[j].color = '全部'
                                        this.reducelist[j].newcolor = ['全部']
                                    }
                                    let newHasAll = false
                                    for (let l = 0; l < res.length; l++) {
                                        if (res[l].value == '全部') {
                                            newHasAll = true
                                        }
                                    }
                                    if (!newHasAll) {
                                        this.reducelist[j].two = this.fillSelectOption(res, sigalArr.subFiledCode)
                                    } else {
                                        this.reducelist[j].two = res
                                    }
                                    console.log(res)
                                    // if (this.reducelist[j].two.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                    //   this.reducelist[j].popup = true
                                    // }
                                })
                            }
                        }
                    }
                }
                console.log(this.allcheckList)
                if (!clickType) {
                    this.currentPage = 1
                    this.query()
                }

            },
            jilianclick(value, id, type, newarr, head, info, newMore, treeCheck, clickType) {
                console.log('value:' + value, 'id:' + id, 'type:' + type, 'newarr:' + newarr, 'head:' + head, 'info:' + JSON.stringify(info), 'newMore:' + newMore, 'treeCheck:' + treeCheck, 'clickType:' + clickType)
                console.log(this.allcheckList, '10000')
                // this.queryForm.idcType = value
                console.log(value)
                console.log(id)
                console.log(type)
                console.log(newarr)
                console.log(head)
                console.log(info)
                console.log(newMore)
                let arr = newarr
                this.selectedShow = true
                let jsonfield = {}
                jsonfield.father = head
                jsonfield.son = info
                jsonfield.newson = []
                jsonfield.newson.push(info)
                console.log(head)
                console.log(arr)
                let allcheckList = this.allcheckList
                console.log(allcheckList)
                let checkboll = false
                let checkbollnew = false
                if (!info.valueId) {
                    info.valueId = info.id
                }
                for (let i = 0; i < allcheckList.length; i++) {
                    if (allcheckList[i].father.cmdbCode.filedCode == head.cmdbCode.filedCode) {
                        allcheckList[i].son = info
                        if (newMore) {
                            if (allcheckList[i].newson.length > 0) {
                                let tresarr = allcheckList[i].newson
                                for (let k = 0; k < tresarr.length; k++) {
                                    if (tresarr[k].value == info.value) {
                                        checkbollnew = true
                                        allcheckList[i].newson.splice(k, 1)
                                        if (allcheckList[i].newson.length == 0) {
                                            this.allcheckList.splice(i, 1)
                                        }
                                    }

                                }
                                if (!checkbollnew) {
                                    // if (!treeCheck) {
                                    //     console.log(this.poolList)
                                    if (allcheckList[i].father.cmdbCode.filedCode == 'idcType') {
                                        if (!this.poolList.includes(allcheckList[i].newson[0])) {
                                            allcheckList[i].newson = [info]
                                        } else {
                                            allcheckList[i].newson.push(info)
                                        }
                                    }
                                    if (allcheckList[i].father.cmdbCode.filedCode == 'deviceProject') {
                                        if (!this.projectNameList.includes(allcheckList[i].newson[0])) {
                                            allcheckList[i].newson = [info]
                                        } else {
                                            allcheckList[i].newson.push(info)
                                        }
                                    }
                                    if (allcheckList[i].father.cmdbCode.filedCode == 'pod') {
                                        if (!this.podList.includes(allcheckList[i].newson[0])) {
                                            allcheckList[i].newson = [info]
                                        } else {
                                            allcheckList[i].newson.push(info)
                                        }
                                    }
                                    if (allcheckList[i].father.cmdbCode.filedCode == 'deviceClass') {
                                        if (!this.deviceClassList.includes(allcheckList[i].newson[0])) {
                                            allcheckList[i].newson = [info]
                                        } else {
                                            allcheckList[i].newson.push(info)
                                        }
                                    }
                                    // }
                                    // allcheckList[i].newson.push(info)
                                }
                            } else {
                                // if (!treeCheck) {
                                //     console.log(this.poolList)
                                if (allcheckList[i].father.cmdbCode.filedCode == 'idcType') {
                                    if (!this.poolList.includes(allcheckList[i].newson[0])) {
                                        allcheckList[i].newson = [info]
                                    } else {
                                        allcheckList[i].newson.push(info)
                                    }
                                }
                                if (allcheckList[i].father.cmdbCode.filedCode == 'deviceProject') {
                                    if (!this.projectNameList.includes(allcheckList[i].newson[0])) {
                                        allcheckList[i].newson = [info]
                                    } else {
                                        allcheckList[i].newson.push(info)
                                    }
                                }
                                if (allcheckList[i].father.cmdbCode.filedCode == 'pod') {
                                    if (!this.podList.includes(allcheckList[i].newson[0])) {
                                        allcheckList[i].newson = [info]
                                    } else {
                                        allcheckList[i].newson.push(info)
                                    }
                                }
                                if (allcheckList[i].father.cmdbCode.filedCode == 'deviceClass') {
                                    if (!this.deviceClassList.includes(allcheckList[i].newson[0])) {
                                        allcheckList[i].newson = [info]
                                    } else {
                                        allcheckList[i].newson.push(info)
                                    }
                                }
                                // }
                                // allcheckList[i].newson.push(info)
                            }
                        } else {
                            allcheckList[i].newson = [info]
                        }
                        checkboll = true
                    } else {
                        console.log('')
                    }
                    if (arr.length > 0) {
                        for (let j = 0; j < arr.length; j++) {
                            console.log(allcheckList[i])
                            if (allcheckList[i] == undefined) {
                                console.log('')
                            } else {
                                if (arr[j].subFiledCode == allcheckList[i].father.cmdbCode.filedCode) {
                                    allcheckList.splice(i, 1)
                                }
                            }
                        }
                    }
                }
                if (!checkboll) {
                    this.allcheckList.push(jsonfield)
                } else {
                    this.allcheckList = allcheckList
                }
                for (let i = 0; i < this.allcheckList.length; i++) {
                    if (this.allcheckList[i].son.value == '全部') {
                        this.allcheckList.splice(i, 1)
                    }
                }

                console.log(this.newonestep1)
                // 头部几种码表固定的
                // if (treeCheck == 'treeover') {
                //     this.queryForm[type] = [value]
                // } else {
                if (type == 'deviceProject') {
                    if (this.newonestep2) {
                        if (value == '全部') {
                            this.queryForm.newdeviceProject = ['全部']
                        } else {
                            if (this.queryForm.newdeviceProject.indexOf('全部') > -1) {
                                this.queryForm.newdeviceProject.splice(this.queryForm.newdeviceProject.indexOf('全部'), 1)
                            }
                            if (this.queryForm.newdeviceProject.indexOf(value) > -1) {
                                // eslint-disable-next-line no-empty
                                if (treeCheck == 'treeover') {

                                } else {
                                    this.queryForm.newdeviceProject.splice(this.queryForm.newdeviceProject.indexOf(value), 1)
                                }

                            } else {
                                if (treeCheck == 'treeover') {
                                    this.queryForm.newdeviceProject.splice(this.queryForm.newdeviceProject.length - 1, 1)
                                    this.queryForm.newdeviceProject.push(value)
                                } else {
                                    this.queryForm.newdeviceProject.push(value)
                                }
                            }
                            console.log(this.queryForm.newdeviceProject)
                            if (this.queryForm.newdeviceProject.length == 0) {
                                this.queryForm.newdeviceProject = ['全部']
                            }
                        }
                    } else {
                        this.queryForm[type] = value
                    }
                } else if (type == 'idcType') {

                    if (this.newonestep1) {
                        if (value == '全部') {
                            this.queryForm.newidcType = ['全部']
                        } else {
                            if (this.queryForm.newidcType.indexOf('全部') > -1) {
                                this.queryForm.newidcType.splice(this.queryForm.newidcType.indexOf('全部'), 1)
                            }
                            if (this.queryForm.newidcType.indexOf(value) > -1) {
                                // eslint-disable-next-line no-empty
                                if (treeCheck == 'treeover') {

                                } else {
                                    this.queryForm.newidcType.splice(this.queryForm.newidcType.indexOf(value), 1)
                                }

                            } else {
                                if (treeCheck == 'treeover') {
                                    this.queryForm.newidcType.splice(this.queryForm.newidcType.length - 1, 1)
                                    this.queryForm.newidcType.push(value)
                                } else {
                                    this.queryForm.newidcType.push(value)
                                }
                            }
                            console.log(this.queryForm.newidcType)
                            if (this.queryForm.newidcType.length == 0) {
                                this.queryForm.newidcType = ['全部']
                            }
                        }
                    } else {
                        this.queryForm[type] = value
                    }
                } else if (type == 'pod') {
                    if (this.newonestep3) {
                        if (value == '全部') {
                            this.queryForm.newpod = ['全部']
                        } else {
                            if (this.queryForm.newpod.indexOf('全部') > -1) {
                                this.queryForm.newpod.splice(this.queryForm.newpod.indexOf('全部'), 1)
                            }
                            if (this.queryForm.newpod.indexOf(value) > -1) {
                                // eslint-disable-next-line no-empty
                                if (treeCheck == 'treeover') {

                                } else {
                                    this.queryForm.newpod.splice(this.queryForm.newpod.indexOf(value), 1)
                                }

                            } else {
                                if (treeCheck == 'treeover') {
                                    this.queryForm.newpod.splice(this.queryForm.newpod.length - 1, 1)
                                    this.queryForm.newpod.push(value)
                                } else {
                                    this.queryForm.newpod.push(value)
                                }
                            }
                            console.log(this.queryForm.newpod)
                            if (this.queryForm.newpod.length == 0) {
                                this.queryForm.newpod = ['全部']
                            }
                        }
                    } else {
                        this.queryForm[type] = value
                    }
                } else if (type == 'deviceClass') {
                    if (this.newonestep4) {
                        if (value == '全部') {
                            this.queryForm.newdeviceClass = ['全部']
                        } else {
                            if (this.queryForm.newdeviceClass.indexOf('全部') > -1) {
                                this.queryForm.newdeviceClass.splice(this.queryForm.newdeviceClass.indexOf('全部'), 1)
                            }
                            if (this.queryForm.newdeviceClass.indexOf(value) > -1) {
                                // eslint-disable-next-line no-empty
                                if (treeCheck == 'treeover') {

                                } else {
                                    this.queryForm.newdeviceClass.splice(this.queryForm.newdeviceClass.indexOf(value), 1)
                                }

                            } else {
                                if (treeCheck == 'treeover') {
                                    this.queryForm.newdeviceClass.splice(this.queryForm.newdeviceClass.length - 1, 1)
                                    this.queryForm.newdeviceClass.push(value)
                                } else {
                                    this.queryForm.newdeviceClass.push(value)
                                }
                            }
                            console.log(this.queryForm.newdeviceClass)
                            if (this.queryForm.newdeviceClass.length == 0) {
                                this.queryForm.newdeviceClass = ['全部']
                            }
                        }
                    } else {
                        this.queryForm[type] = value
                    }
                }
                else {
                    this.queryForm[type] = value
                }
                // }

                // this.queryForm[type] = value
                console.log(this.allcheckList, '77777')
                console.log(id)
                // let arr = this.idcTypeArr

                let otherList = this.otherList
                let reducelist = this.reducelist
                console.log(arr)
                for (let i = 0; i < arr.length; i++) {
                    let sqlstr = arr[i].sqlString
                    sqlstr = sqlstr.replace('?', value)
                    let sigalArr = arr[i]
                    if (arr[i].subFiledCode == 'pod_name') {
                        if (value == '全部---') {
                            this.podList = this.fillSelectOption(this.podListStorage, 'pod_name')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {

                                this.queryForm.pod = '全部'
                                this.queryForm.newpod = ['全部']
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.podList = this.fillSelectOption(res, 'pod_name')
                                } else {
                                    this.podList = res
                                }
                                console.log(res)
                                // if (this.podList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.PODB = true
                                // }
                            })
                        }
                    } else if (arr[i].subFiledCode == 'idcType') {
                        if (value == '全部---') {
                            this.poolList = this.fillSelectOption(this.poolListStorage, 'idcType')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                this.queryForm.idcType = '全部'
                                this.queryForm.newidcType = ['全部']
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.poolList = this.fillSelectOption(res, 'idcType')
                                } else {
                                    this.poolList = res
                                }
                                console.log(res)
                                // if (this.poolList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.poolB = true
                                // }
                            })
                        }
                    } else if (arr[i].subFiledCode == 'project_name') {
                        if (value == '全部---') {
                            this.projectNameList = this.fillSelectOption(this.projectNameListStorage, 'project_name')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                this.queryForm.deviceProject = '全部'
                                this.queryForm.newdeviceProject = ['全部']
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.projectNameList = this.fillSelectOption(res, 'project_name')
                                } else {
                                    this.projectNameList = res
                                }
                                console.log(res)
                                // if (this.projectNameList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.poolB2 = true
                                // }
                            })
                        }
                    } else if (arr[i].subFiledCode == 'device_class') {
                        if (value == '全部---') {
                            this.deviceClassList = this.fillSelectOption(this.deviceClassListStorage, 'device_class')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                this.queryForm.deviceClass = '全部'
                                this.queryForm.newdeviceClass = ['全部']
                                let newHasAll = false
                                for (let l = 0; l < res.length; l++) {
                                    if (res[l].value == '全部') {
                                        newHasAll = true
                                    }
                                }
                                if (!newHasAll) {
                                    this.deviceClassList = this.fillSelectOption(res, 'device_class')
                                } else {
                                    this.deviceClassList = res
                                }
                                // if (this.brandList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.equipmentBrandB = true
                                // }
                            })
                        }
                    } else if (arr[i].subFiledCode == 'device_mfrs') {
                        if (value == '全部') {
                            this.brandList = this.fillSelectOption(this.brandListStorage, 'device_mfrs')
                        } else {
                            this.rbHttp.sendRequest({
                                method: 'POST',
                                data: { 'sql': sqlstr },
                                url: '/v1/cmdb/module/queryTableData'
                            }).then((res) => {
                                if (JSON.stringify(res) == '[]') {
                                    this.queryForm.mfrFactory = ''
                                }
                                this.brandList = res
                                console.log(res)
                                // if (this.brandList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                //   this.equipmentBrandB = true
                                // }
                            })
                        }
                    } else {
                        for (let j = 0; j < otherList.length; j++) {
                            if (otherList[j].id == sigalArr.subFiledCode) {
                                if (value == '全部---') {
                                    this.otherList[j].two = this.fillSelectOption(this.otherList[j].three, sigalArr.subFiledCode)
                                } else {
                                    this.rbHttp.sendRequest({
                                        method: 'POST',
                                        data: { 'sql': sqlstr },
                                        url: '/v1/cmdb/module/queryTableData'
                                    }).then((res) => {
                                        if (JSON.stringify(res) == '[]') {
                                            this.otherList[j].color = '全部'
                                            this.otherList[j].newcolor = ['全部']
                                        }
                                        let newHasAll = false
                                        for (let l = 0; l < res.length; l++) {
                                            if (res[l].value == '全部') {
                                                newHasAll = true
                                            }
                                        }
                                        console.log(newHasAll)
                                        if (!newHasAll) {
                                            this.otherList[j].two = this.fillSelectOption(res, sigalArr.subFiledCode)
                                        } else {
                                            this.otherList[j].two = res
                                        }
                                        console.log(res)
                                        // if (this.otherList[j].two.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                        //   this.otherList[j].popup = true
                                        // }
                                    })
                                }
                            }
                        }
                        for (let j = 0; j < reducelist.length; j++) {
                            if (reducelist[j].id == sigalArr.subFiledCode) {
                                if (value == '全部---') {
                                    this.reducelist[j].two = this.fillSelectOption(this.reducelist[j].three, sigalArr.subFiledCode)
                                } else {
                                    this.rbHttp.sendRequest({
                                        method: 'POST',
                                        data: { 'sql': sqlstr },
                                        url: '/v1/cmdb/module/queryTableData'
                                    }).then((res) => {
                                        if (JSON.stringify(res) == '[]') {
                                            this.reducelist[j].color = '全部'
                                            this.reducelist[j].newcolor = ['全部']
                                        }
                                        let newHasAll = false
                                        for (let l = 0; l < res.length; l++) {
                                            if (res[l].value == '全部') {
                                                newHasAll = true
                                            }
                                        }
                                        console.log(newHasAll)
                                        if (!newHasAll) {
                                            this.reducelist[j].two = this.fillSelectOption(res, sigalArr.subFiledCode)
                                        } else {
                                            this.reducelist[j].two = res
                                        }
                                        console.log(res)
                                        // if (this.reducelist[j].two.length * 100 >= document.body.offsetWidth / 2 - 150) {
                                        //   this.reducelist[j].popup = true
                                        // }
                                    })
                                }
                            }
                        }
                    }
                }
                if (!clickType) {
                    this.currentPage = 1
                    this.query()
                }

            },
            getQueryParams(value) {
                console.log(this.queryForm.moduleId)
                console.log(this.moduleId)
                if (!value) {
                    let resdata = {
                        'condicationCode': this.condicationCode,
                        'moduleType': this.moduleType,
                        'module_id': this.moduleId,
                        'pageSize': this.pageSize,
                        'currentPage': this.currentPage,
                        'sort': [{
                            'filed': this.newCurrentValue,
                            'type': this.newCurrentOrder
                        }]
                    }
                    return resdata
                } else {
                    let resdata = {
                        'condicationCode': this.condicationCode,
                        'moduleType': this.moduleType,
                        'module_id': this.moduleId,
                        'pageSize': this.pageSize,
                        'currentPage': this.currentPage
                    }
                    return resdata
                }
            },
            inputFun(value, id, item) {
                console.log(value, id, item, 'inputFun')
                if (Object.prototype.toString.call(value) == '[object Array]') {
                    value = value.join(',')
                }
                let json = {}
                let jsonbool = false
                if (value != '' && value != null) {
                    this.selectedShow = true
                    if (this.allcheckList.length < 1) {
                        this.allcheckList.push({
                            father: item.one,
                            son: { value: value },
                            newson: [{ value: value }]
                        })
                    } else {
                        for (let i = 0; i < this.allcheckList.length; i++) {
                            if (this.allcheckList[i].father.cmdbCode.filedCode == item.one.cmdbCode.filedCode) {
                                this.allcheckList[i].father = item.one
                                this.allcheckList[i].son.value = value
                                this.allcheckList[i].newson = [{ value: value }]
                                jsonbool = true
                            } else {
                                json = {
                                    father: item.one,
                                    son: { value: value },
                                    newson: [{ value: value }]
                                }
                            }
                        }
                        if (!jsonbool) {
                            this.allcheckList.push(json)
                        }
                    }

                } else {
                    for (let i = 0; i < this.allcheckList.length; i++) {
                        if (this.allcheckList[i].father.cmdbCode.filedCode == item.one.cmdbCode.filedCode) {
                            this.allcheckList.splice(i, 1)
                        }
                    }
                }
                console.log(this.allcheckList, 'inputFun-allcheckList')
                this.currentPage = 1
                let resdata = this.getQueryParams()
                let allcheckList = this.allcheckList
                for (let i = 0; i < allcheckList.length; i++) {
                    resdata[allcheckList[i].father.cmdbCode.filedCode] = allcheckList[i].son.valueId ? allcheckList[i].son.valueId : allcheckList[i].son.value
                }
                if (this.publicBrand) {
                    resdata['device_mfrs'] = this.devicemfrsValue
                }
                resdata[id] = value
                this.showLoading('正在加载数据，请稍等...')
                this.$emit('resdata', resdata)
                rbCmdbServiceFactory.getInstanceList(resdata).then((data) => {
                    this.total = data.totalSize
                    this.instanceDataList = data.data
                    // this.columnList = data.columnList
                    this.columnList = data.columns
                    for (const key in this.columnList) {
                        const item = this.columnList[key]
                        if (item.type === 'ref') {
                            this.$set(this.refColumns, item.filed_code, item.ref_name)
                        }
                    }
                }).finally(() => {
                    this.hideLoading()
                })
            },
            commonMore(value, index) {
                console.log(index)
                this.otherList[index][value] = !this.otherList[index][value]
                this.otherList[index].popup = !this.otherList[index].popup
                this.otherList[index].popdown = !this.otherList[index].popdown
            },
            commonMoreOther(value, index) {
                console.log(this.reducelist)
                console.log(value)
                console.log(index)
                this.reducelist[index][value] = !this.reducelist[index][value]
                this.reducelist[index].popup = !this.reducelist[index].popup
                this.reducelist[index].popdown = !this.reducelist[index].popdown
            },
            // 头部筛选条件加载
            headlist() {
                let queryheadNew = {
                    condication: {
                        condicationCode: this.condicationCode
                    },
                    queryParams: {
                        params: [
                        ]
                    },
                    moduleId: '',
                    pageSize: this.paginationData.pageSize,
                    currentPage: this.paginationData.currentPage
                }
                conditionManagement.getCondicationGet(queryheadNew.condication).then(res => {
                    this.tableNewId = res.id
                    console.log(this.tableNewId)
                    let headlist = res.settingRelationList
                    this.headlistqurey = res.settingRelationList
                    let a1 = false
                    let a2 = false
                    let a11 = false
                    let a22 = false
                    let otherList = []
                    let otherList1 = []
                    let indexNew = 0
                    let indexNumber = 0
                    let reduceNumber = 0
                    let lastIndex = 0
                    let toplastIndex = 0

                    for (let i = 0; i < headlist.length; i++) {
                        if (headlist[i].cmdbCode.filedCode == 'idcType') {
                            a11 = true
                            if (!a22) {
                                indexNew++
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'project_name') {
                            a22 = true
                            if (!a11) {
                                indexNew++
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'pod_name') {
                            indexNew++
                        } else if (headlist[i].cmdbCode.filedCode == 'device_class') {
                            indexNew++
                        } else if (headlist[i].cmdbCode.filedCode == 'device_mfrs') {
                            indexNew++
                        }
                        // if (headlist[i].cmdbCode.controlType.controlCode != 'cascader' && headlist[i].cmdbCode.controlType.controlCode != 'listSel' && headlist[i].cmdbCode.controlType.controlCode != 'dateTime') {
                        //     lastIndex++
                        // }
                    }
                    console.log(indexNew)
                    // 排序输入框放最后start
                    for (let i = 0; i < headlist.length; i++) {
                        if (headlist[i].cmdbCode.controlType.controlCode != 'cascader' && headlist[i].cmdbCode.controlType.controlCode != 'listSel' && headlist[i].cmdbCode.controlType.controlCode != 'dateTime') {
                            headlist.push(headlist[i])
                            headlist.splice(i, 1)
                        }
                    }
                    // end
                    // 新添加重新排序
                    // let oneNumber = headlist.length
                    // let headlistCurrent = headlist
                    // for (let i = 0; i < headlist.length; i++) {
                    //     if (headlist[i].cmdbCode.controlType.controlCode != 'cascader' && headlist[i].cmdbCode.controlType.controlCode != 'listSel' && headlist[i].cmdbCode.controlType.controlCode != 'dateTime') {
                    //         headlistCurrent = [...headlistCurrent, headlist[i]]
                    //     }
                    // }
                    // for (let i = 0; i < headlistCurrent.length; i++) {
                    //     if (i < oneNumber) {
                    //         if (headlistCurrent[i].cmdbCode.controlType.controlCode != 'cascader' && headlistCurrent[i].cmdbCode.controlType.controlCode != 'listSel' && headlistCurrent[i].cmdbCode.controlType.controlCode != 'dateTime') {
                    //             headlistCurrent.splice(i, 1)
                    //         }
                    //     }
                    // }
                    // headlist = headlistCurrent
                    // console.log(headlistCurrent)
                    for (let i = 0; i < headlist.length; i++) {
                        if (headlist[i].cmdbCode.filedCode == 'idcType') {
                            this.onehead = headlist[i]
                            this.idcTypeArr = headlist[i].cmdbCode.cascadeList
                            this.oneStep1.name = headlist[i].cmdbCode.filedName
                            // this.oneStep1.show = true
                            // this.onestepFun()
                            a1 = true
                            if (headlist[i].cmdbCode.isBindSource == '是') {
                                if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典' || headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.idcType = headlist[i].defaultValue
                                        this.queryForm.newidcType = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.idcType = '全部'
                                        this.queryForm.newidcType = ['全部']
                                    }
                                    switch (headlist[i].cmdbCode.codeBindSource.bindSourceType) {
                                        case '引用模型':
                                            // this.handleRefModuleType(headlist[i])
                                            this.onestepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 1)
                                            break
                                        case '数据字典':
                                            this.onestepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 2)
                                            break
                                    }
                                    // this.onestepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                } else {
                                    console.log('数据表')

                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.idcType = headlist[i].defaultValue
                                        this.queryForm.newidcType = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.idcType = '全部'
                                        this.queryForm.newidcType = ['全部']
                                    }
                                    this.onestepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                }
                            } else {
                                if (headlist[i].defaultValue != '') {
                                    this.queryForm.idcType = headlist[i].defaultValue
                                    this.queryForm.newidcType = [headlist[i].defaultValue]
                                } else {
                                    this.queryForm.idcType = '全部'
                                    this.queryForm.newidcType = ['全部']
                                }

                                this.poolList = this.fillSelectOption([], 'idcType')
                            }
                            this.allbottomList = [...this.poolList]
                            if (headlist[i].cmdbCode.controlType.controlCode == 'cascader') {
                                console.log('idc级联')
                                this.newonestep1 = false
                            } else {
                                console.log('idc非级联')
                                this.newonestep1 = true
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'project_name') {
                            this.twohead = headlist[i]
                            this.projectnameArr = headlist[i].cmdbCode.cascadeList
                            this.oneStep2.name = headlist[i].cmdbCode.filedName
                            // this.onestepFun1()
                            a2 = true
                            if (headlist[i].cmdbCode.isBindSource == '是') {
                                if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典' || headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.deviceProject = headlist[i].defaultValue
                                        this.queryForm.newdeviceProject = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.deviceProject = '全部'
                                        this.queryForm.newdeviceProject = ['全部']
                                    }
                                    // this.queryForm.deviceProject = '全部'
                                    switch (headlist[i].cmdbCode.codeBindSource.bindSourceType) {
                                        case '引用模型':
                                            // this.handleRefModuleType(headlist[i])
                                            this.onestepFun1(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 1)
                                            break
                                        case '数据字典':
                                            // this.onestepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                            this.onestepFun1(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 2)
                                            break
                                    }
                                    // this.onestepFun1(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                } else {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.deviceProject = headlist[i].defaultValue
                                        this.queryForm.newdeviceProject = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.deviceProject = '全部'
                                        this.queryForm.newdeviceProject = ['全部']
                                    }
                                    // this.queryForm.deviceProject = '全部'
                                    this.onestepFun1sql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                }

                            } else {
                                if (headlist[i].defaultValue != '') {
                                    this.queryForm.deviceProject = headlist[i].defaultValue
                                    this.queryForm.newdeviceProject = [headlist[i].defaultValue]
                                } else {
                                    this.queryForm.deviceProject = '全部'
                                    this.queryForm.newdeviceProject = ['全部']
                                }
                                // this.queryForm.deviceProject = '全部'
                                this.projectNameList = this.fillSelectOption([], 'project_name')
                            }
                            this.allbottomList = [...this.allbottomList, ...this.projectNameList]
                            if (headlist[i].cmdbCode.controlType.controlCode == 'cascader') {
                                this.newonestep2 = false
                            } else {
                                this.newonestep2 = true
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'pod_name') {
                            this.threehead = headlist[i]
                            this.podnameArr = headlist[i].cmdbCode.cascadeList
                            this.twoStep.name = headlist[i].cmdbCode.filedName
                            this.twoStep.show = true
                            // this.twoStepFun()
                            if (headlist[i].cmdbCode.isBindSource == '是') {
                                if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典' || headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.pod = headlist[i].defaultValue
                                        this.queryForm.newpod = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.pod = '全部'
                                        this.queryForm.newpod = ['全部']
                                    }
                                    // this.queryForm.pod = '全部'
                                    switch (headlist[i].cmdbCode.codeBindSource.bindSourceType) {
                                        case '引用模型':
                                            // this.handleRefModuleType(headlist[i])
                                            this.twoStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 1)
                                            break
                                        case '数据字典':
                                            // this.onestepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                            this.twoStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 2)
                                            break
                                    }
                                    // this.twoStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                } else {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.pod = headlist[i].defaultValue
                                        this.queryForm.newpod = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.pod = '全部'
                                        this.queryForm.newpod = ['全部']
                                    }
                                    // this.queryForm.pod = '全部'
                                    this.twoStepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                }
                            } else {
                                if (headlist[i].defaultValue != '') {
                                    this.queryForm.pod = headlist[i].defaultValue
                                    this.queryForm.newpod = [headlist[i].defaultValue]
                                } else {
                                    this.queryForm.pod = '全部'
                                    this.queryForm.newpod = ['全部']
                                }
                                // this.queryForm.pod = '全部'
                                this.podList = this.fillSelectOption([], 'pod_name')
                            }
                            console.log(this.podList)
                            this.allbottomList = [...this.allbottomList, ...this.podList]
                            if (headlist[i].cmdbCode.controlType.controlCode == 'cascader') {
                                this.newonestep3 = false
                            } else {
                                this.newonestep3 = true
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'device_class') {
                            this.fourhead = headlist[i]
                            // this.deviceTypeArr = headlist[i].cmdbCode.cascadeList
                            // this.threeStep.name = headlist[i].cmdbCode.filedName
                            // this.threeStep.show = true
                            // this.threeStepFun()
                            this.deviceTypeArr = headlist[i].cmdbCode.cascadeList
                            this.threeStep.name = headlist[i].cmdbCode.filedName
                            this.threeStep.show = true
                            if (headlist[i].cmdbCode.isBindSource == '是') {
                                if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典' || headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.deviceClass = headlist[i].defaultValue
                                        this.queryForm.newdeviceClass = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.deviceClass = '全部'
                                        this.queryForm.newdeviceClass = ['全部']
                                    }
                                    // this.queryForm.deviceClass = '全部'
                                    switch (headlist[i].cmdbCode.codeBindSource.bindSourceType) {
                                        case '引用模型':
                                            // this.handleRefModuleType(headlist[i])
                                            this.threeStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 1)
                                            break
                                        case '数据字典':
                                            // this.threeStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                            this.threeStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 2)
                                            break
                                    }
                                    // this.threeStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                } else {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.deviceClass = headlist[i].defaultValue
                                        this.queryForm.newdeviceClass = [headlist[i].defaultValue]
                                    } else {
                                        this.queryForm.deviceClass = '全部'
                                        this.queryForm.newdeviceClass = ['全部']
                                    }
                                    // this.queryForm.deviceClass = '全部'
                                    this.threeStepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                }
                            } else {
                                if (headlist[i].defaultValue != '') {
                                    this.queryForm.deviceClass = headlist[i].defaultValue
                                    this.queryForm.newdeviceClass = [headlist[i].defaultValue]
                                } else {
                                    this.queryForm.deviceClass = '全部'
                                    this.queryForm.newdeviceClass = ['全部']
                                }
                                // this.queryForm.deviceClass = '全部'
                                this.deviceClassList = this.fillSelectOption([], 'device_class')
                            }
                            console.log(this.deviceClassList)
                            if (headlist[i].cmdbCode.controlType.controlCode == 'cascader') {
                                this.newonestep4 = false
                            } else {
                                this.newonestep4 = true
                            }
                        } else if (headlist[i].cmdbCode.filedCode == 'device_mfrs') {
                            this.devicemfrsArr = headlist[i].cmdbCode.cascadeList
                            this.fourStep.name = headlist[i].cmdbCode.filedName
                            this.fourStep.show = true
                            if (headlist[i].cmdbCode.isBindSource == '是') {
                                if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典' || headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.mfrFactory = headlist[i].defaultValue
                                    } else {
                                        this.queryForm.mfrFactory = ''
                                    }
                                    switch (headlist[i].cmdbCode.codeBindSource.bindSourceType) {
                                        case '引用模型':
                                            // this.handleRefModuleType(headlist[i])
                                            this.fourStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 1)
                                            break
                                        case '数据字典':
                                            this.fourStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i], 2)
                                            break
                                    }
                                    // this.fourStepFun(headlist[i].cmdbCode.codeBindSource.dictSource, headlist[i])
                                } else {
                                    if (headlist[i].defaultValue != '') {
                                        this.queryForm.mfrFactory = headlist[i].defaultValue
                                    } else {
                                        this.queryForm.mfrFactory = ''
                                    }
                                    this.fourStepFunsql(headlist[i].cmdbCode.codeBindSource.tableSql, headlist[i])
                                }
                            } else {
                                this.deviceClassList = []
                            }
                            // this.fourStepFun(headlist[i].cmdbCode.dictSource)

                        } else {
                            // let resdata = headlist[i].cmdbCode.filedCode
                            let setf = this
                            indexNumber++
                            if ((indexNew + indexNumber) > 4) {
                                this.downtshow = false
                                this.newMoreDown = true
                                // setf.rbHttp.sendRequest({
                                //     method: 'GET',
                                //     params: { 'type': resdata},
                                //     url: `/v1/cmdb/configDict/getDictsByType`
                                // }).then((res) => {
                                //     let obj = {}
                                //     obj.one = headlist[i]
                                //     obj.two = res
                                //     obj.popup = true
                                //     obj.popdown = false
                                //     obj.value = ''
                                //     obj.id = headlist[i].cmdbCode.filedCode
                                //     obj[headlist[i].cmdbCode.filedCode] = false
                                //     otherList1.push(obj)
                                // })
                                if (headlist[i].cmdbCode.isBindSource == '是') {

                                    if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典') {
                                        let resdata = headlist[i].cmdbCode.codeBindSource.dictSource
                                        setf.rbHttp.sendRequest({
                                            method: 'GET',
                                            params: { 'type': resdata },
                                            url: '/v1/cmdb/configDict/getDictsByType'
                                        }).then((res) => {

                                            let obj = {}
                                            obj.one = headlist[i]
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }


                                            obj.three = res
                                            obj.popup = true
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                obj.value = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length > 1) {
                                                        console.log('')
                                                    } else {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            obj.popdown = false
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList1.push(obj)
                                        })
                                    } else if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                        // 处理引用模型类型
                                        let refModuleId = headlist[i].cmdbCode.codeBindSource.refModuleId
                                        let refFiledCode = headlist[i].cmdbCode.codeBindSource.showModuleCodeId
                                        let filedCode = headlist[i].cmdbCode.filedCode
                                        if (!(refModuleId && refFiledCode)) {
                                            this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                                            return
                                        }
                                        let params = {
                                            codeId: headlist[i].cmdbCode.codeId
                                        }
                                        rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                                            let obj = {}
                                            obj.one = headlist[i]
                                            obj.sortIndex = headlist[i].sortIndex
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }
                                            obj.three = res
                                            obj.popup = true
                                            obj.popdown = false
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length > 1) {
                                                        console.log('')
                                                    } else {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList1.push(obj)
                                        }))
                                    }
                                    else {
                                        let ressql = headlist[i].cmdbCode.codeBindSource.tableSql
                                        setf.rbHttp.sendRequest({
                                            method: 'POST',
                                            data: { 'sql': ressql },
                                            url: '/v1/cmdb/module/queryTableData'
                                        }).then((res) => {
                                            let obj = {}
                                            obj.one = headlist[i]
                                            obj.sortIndex = headlist[i].sortIndex
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }
                                            obj.three = res
                                            obj.popup = true
                                            obj.popdown = false
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length > 1) {
                                                        console.log('')
                                                    } else {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList1.push(obj)
                                        })
                                    }
                                } else {
                                    let obj = {}
                                    obj.one = headlist[i]
                                    obj.sortIndex = headlist[i].sortIndex
                                    // obj.two = []
                                    let newHasAll = false
                                    for (let l = 0; l < res.length; l++) {
                                        if (res[l].value == '全部') {
                                            newHasAll = true
                                        }
                                    }
                                    if (!newHasAll) {
                                        obj.two = this.fillSelectOption([], headlist[i].cmdbCode.filedCode)
                                    } else {
                                        obj.two = res
                                    }
                                    obj.three = []
                                    obj.popup = false
                                    obj.popdown = false
                                    if (headlist[i].defaultValue != '') {
                                        obj.color = headlist[i].defaultValue
                                        if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                            obj.value = headlist[i].defaultValue.split(',')
                                            if (obj.value.length > 1) {
                                                console.log('')
                                            } else {
                                                obj.value = [...obj.value, ...obj.value]
                                            }
                                            if (headlist[i].defaultValue != '') {
                                                this.allcheckList.push({
                                                    father: headlist[i],
                                                    son: { value: headlist[i].defaultValue }
                                                })
                                            }
                                        } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                            obj.value = headlist[i].defaultValue
                                            let bool = false
                                            for (let t = 0; t < res.length; t++) {
                                                if (res[t].value == headlist[i].defaultValue) {
                                                    this.selectedShow = true
                                                    this.allcheckList.push({
                                                        father: headlist[i],
                                                        son: res[t]
                                                    })
                                                    bool = true
                                                }
                                            }
                                            if (!bool) {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                        } else {
                                            obj.value = headlist[i].defaultValue
                                            if (headlist[i].defaultValue != '') {
                                                this.allcheckList.push({
                                                    father: headlist[i],
                                                    son: { value: headlist[i].defaultValue }
                                                })
                                            }
                                        }
                                    } else {
                                        // 新增非级联多选功能
                                        obj.newcolor = ['全部']
                                        obj.color = '全部'
                                        obj.value = ''
                                    }
                                    // obj.color = '全部'
                                    // obj.value = ''
                                    obj.id = headlist[i].cmdbCode.filedCode
                                    obj[headlist[i].cmdbCode.filedCode] = false
                                    otherList1.push(obj)
                                }
                            } else {
                                this.downtshow = true
                                reduceNumber++
                                this.newMoreDown = true
                                // setf.rbHttp.sendRequest({
                                //     method: 'GET',
                                //     params: { 'type': resdata},
                                //     url: `/v1/cmdb/configDict/getDictsByType`
                                // }).then((res) => {
                                //     let obj = {}
                                //     obj.one = headlist[i]
                                //     obj.two = res
                                //     obj.popup = true
                                //     obj.popdown = false
                                //     obj.value = ''
                                //     obj.id = headlist[i].cmdbCode.filedCode
                                //     obj[headlist[i].cmdbCode.filedCode] = false
                                //     otherList.push(obj)
                                // })
                                if (headlist[i].cmdbCode.isBindSource == '是') {
                                    console.log(headlist[i].cmdbCode.codeBindSource.bindSourceType)
                                    console.log(headlist[i].cmdbCode.filedName)
                                    if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '数据字典') {
                                        let resdata = headlist[i].cmdbCode.codeBindSource.dictSource
                                        setf.rbHttp.sendRequest({
                                            method: 'GET',
                                            params: { 'type': resdata },
                                            url: '/v1/cmdb/configDict/getDictsByType'
                                        }).then((res) => {
                                            let obj = {}
                                            obj.one = headlist[i]
                                            obj.sortIndex = headlist[i].sortIndex
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }
                                            obj.popup = true
                                            obj.popdown = false
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length > 1) {
                                                        console.log('')
                                                    } else {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList.push(obj)
                                        })
                                    } else if (headlist[i].cmdbCode.codeBindSource.bindSourceType == '引用模型') {
                                        let refModuleId = headlist[i].cmdbCode.codeBindSource.refModuleId
                                        let refFiledCode = headlist[i].cmdbCode.codeBindSource.showModuleCodeId
                                        let filedCode = headlist[i].cmdbCode.filedCode
                                        if (!(refModuleId && refFiledCode)) {
                                            this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                                            return
                                        }
                                        let params = {
                                            codeId: headlist[i].cmdbCode.codeId
                                        }
                                        rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                                            let obj = {}
                                            obj.one = headlist[i]
                                            obj.sortIndex = headlist[i].sortIndex
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }
                                            obj.three = res
                                            obj.popup = true
                                            obj.popdown = false
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length <= 1) {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList.push(obj)
                                        }))
                                    } else {
                                        let ressql = headlist[i].cmdbCode.codeBindSource.tableSql
                                        setf.rbHttp.sendRequest({
                                            method: 'POST',
                                            data: { 'sql': ressql },
                                            url: '/v1/cmdb/module/queryTableData'
                                        }).then((res) => {
                                            let obj = {}
                                            obj.one = headlist[i]
                                            obj.sortIndex = headlist[i].sortIndex
                                            // obj.two = res
                                            let newHasAll = false
                                            for (let l = 0; l < res.length; l++) {
                                                if (res[l].value == '全部') {
                                                    newHasAll = true
                                                }
                                            }
                                            if (!newHasAll) {
                                                obj.two = this.fillSelectOption(res, headlist[i].cmdbCode.filedCode)
                                            } else {
                                                obj.two = res
                                            }
                                            obj.three = res
                                            obj.popup = true
                                            obj.popdown = false
                                            if (headlist[i].defaultValue != '') {
                                                obj.color = headlist[i].defaultValue
                                                if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                                    obj.value = headlist[i].defaultValue.split(',')
                                                    if (obj.value.length <= 1) {
                                                        obj.value = [...obj.value, ...obj.value]
                                                    }
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                                    obj.value = headlist[i].defaultValue
                                                    let bool = false
                                                    for (let t = 0; t < res.length; t++) {
                                                        if (res[t].value == headlist[i].defaultValue) {
                                                            this.selectedShow = true
                                                            this.allcheckList.push({
                                                                father: headlist[i],
                                                                son: res[t]
                                                            })
                                                            bool = true
                                                        }
                                                    }
                                                    if (!bool) {
                                                        // 新增非级联多选功能
                                                        obj.newcolor = ['全部']
                                                        obj.color = '全部'
                                                        obj.value = ''
                                                    }
                                                } else {
                                                    obj.value = headlist[i].defaultValue
                                                    if (headlist[i].defaultValue != '') {
                                                        this.allcheckList.push({
                                                            father: headlist[i],
                                                            son: { value: headlist[i].defaultValue }
                                                        })
                                                    }
                                                }
                                            } else {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                            // obj.color = '全部'
                                            // obj.value = ''
                                            obj.id = headlist[i].cmdbCode.filedCode
                                            obj[headlist[i].cmdbCode.filedCode] = false
                                            otherList.push(obj)
                                        })
                                    }
                                } else {
                                    let obj = {}
                                    obj.one = headlist[i]
                                    obj.sortIndex = headlist[i].sortIndex
                                    // obj.two = []
                                    let newHasAll = false
                                    for (let l = 0; l < res.length; l++) {
                                        if (res[l].value == '全部') {
                                            newHasAll = true
                                        }
                                    }
                                    if (!newHasAll) {
                                        obj.two = this.fillSelectOption([], headlist[i].cmdbCode.filedCode)
                                    } else {
                                        obj.two = res
                                    }
                                    obj.three = []
                                    obj.popup = false
                                    obj.popdown = false
                                    if (headlist[i].defaultValue != '') {
                                        obj.color = headlist[i].defaultValue
                                        if (headlist[i].cmdbCode.controlType.controlCode == 'dateTime') {
                                            obj.value = headlist[i].defaultValue.split(',')
                                            if (obj.value.length > 1) {
                                                console.log('')
                                            } else {
                                                obj.value = [...obj.value, ...obj.value]
                                            }
                                            if (headlist[i].defaultValue != '') {
                                                this.allcheckList.push({
                                                    father: headlist[i],
                                                    son: { value: headlist[i].defaultValue }
                                                })
                                            }
                                        } else if (headlist[i].cmdbCode.controlType.controlCode == 'cascader' || headlist[i].cmdbCode.controlType.controlCode == 'listSel') {
                                            obj.value = headlist[i].defaultValue
                                            let bool = false
                                            for (let t = 0; t < res.length; t++) {
                                                if (res[t].value == headlist[i].defaultValue) {
                                                    this.selectedShow = true
                                                    this.allcheckList.push({
                                                        father: headlist[i],
                                                        son: res[t]
                                                    })
                                                    bool = true
                                                }
                                            }
                                            if (!bool) {
                                                // 新增非级联多选功能
                                                obj.newcolor = ['全部']
                                                obj.color = '全部'
                                                obj.value = ''
                                            }
                                        } else {
                                            obj.value = headlist[i].defaultValue
                                            if (headlist[i].defaultValue != '') {
                                                this.allcheckList.push({
                                                    father: headlist[i],
                                                    son: { value: headlist[i].defaultValue }
                                                })
                                            }
                                        }
                                    } else {
                                        // 新增非级联多选功能
                                        obj.newcolor = ['全部']
                                        obj.color = '全部'
                                        obj.value = ''
                                    }
                                    // obj.color = '全部'
                                    // obj.value = ''
                                    obj.id = headlist[i].cmdbCode.filedCode
                                    obj[headlist[i].cmdbCode.filedCode] = false
                                    otherList.push(obj)
                                }
                            }
                        }
                    }

                    console.log(this.reducelist)
                    this.reduceNumber = reduceNumber

                    let oiwe = [{ sortIndex: 2 }, { sortIndex: 7 }, { sortIndex: 6 }]
                    function sortBy(props) {
                        return function (a, b) {
                            return a[props] - b[props]
                        }
                    }
                    // this.otherList = otherList   arr1.sort(sortBy("value"))
                    this.otherList = otherList.sort(sortBy('sortIndex'))
                    for (let m = 0; m < this.otherList.length; m++) {
                        if (this.otherList[m].one.cmdbCode.controlType.controlCode != 'cascader' && this.otherList[m].one.cmdbCode.controlType.controlCode != 'listSel') {
                            this.otherListCheck = true
                            if (this.otherList[m].one.cmdbCode.controlType.controlCode != 'dateTime') {
                                toplastIndex++
                            }
                        }
                    }
                    if ((toplastIndex % 2) == 0) {
                        this.toplastIndex = false
                    } else {
                        this.toplastIndex = true
                    }
                    console.log(this.otherList)
                    console.log(oiwe.sort(sortBy('sortIndex')))
                    // this.reducelist = otherList1
                    this.reducelist = otherList1.sort(sortBy('sortIndex'))
                    console.log(this.reducelist)
                    for (let i = 0; i < this.reducelist.length; i++) {
                        if (this.reducelist[i].one.cmdbCode.controlType.controlCode != 'cascader' && this.reducelist[i].one.cmdbCode.controlType.controlCode != 'listSel' && this.reducelist[i].one.cmdbCode.controlType.controlCode != 'dateTime') {
                            lastIndex++
                        }
                    }
                    if ((lastIndex % 2) == 0) {
                        this.lastIndex = false
                    } else {
                        this.lastIndex = true
                    }
                    this.newlastIndex = (headlist.length) - lastIndex
                    this.allbottomList = [...this.allbottomList, ...this.reducelist, ...this.otherList]
                    console.log(this.allbottomList)
                    if (a1 && a2) {
                        this.oneStep.show = true
                        // this.onestepFun()
                        // this.onestepFun1()
                    } else {
                        this.oneStep.show = false
                        if (a1) {
                            this.oneStep1.show = true
                            // this.onestepFun()
                        }
                        if (a2) {
                            this.oneStep2.show = true
                            // this.onestepFun1()
                        }
                    }
                    setTimeout(() => {
                        this.query()
                    }, 2000)

                })
            },
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            // router() {
            //     // console.log('this.$route.query.name', this.$route.query.name,this.$route.query.seriesName)
            // },

            equipmentBrandBotton() {
                this.equipmentBrand = !this.equipmentBrand
                this.equipmentBrandB = !this.equipmentBrandB
                this.equipmentBrandBUP = !this.equipmentBrandBUP
            },
            poolBotton2() {

                this.aLinePublic1 = !this.aLinePublic1
                this.pool2 = !this.pool2
                this.poolB2 = !this.poolB2
                this.poolUP2 = !this.poolUP2
            },
            poolBotton() {
                this.boraderActive = !this.boraderActive
                this.aLinePublic2 = !this.aLinePublic2
                this.pool = !this.pool
                this.poolB = !this.poolB
                this.poolUP = !this.poolUP
            },
            deviceClassBotton() {
                this.pool3 = !this.pool3
                this.deviceClassUp = !this.deviceClassUp
                this.deviceClassB = !this.deviceClassB
            },
            PODBotton() {
                this.POD = !this.POD
                this.PODBUP = !this.PODBUP
                this.PODB = !this.PODB
            },
            factoryBotton() {
                this.factory = !this.factory
                this.factoryB = !this.factoryB
                this.factoryUP = !this.factoryUP
            },
            statusBotton() {
                this.status = !this.status
                this.deviceStatusB = !this.deviceStatusB
                this.deviceStatusUP = !this.deviceStatusUP
            },
            QueryBottonExhibition(data) {
                this.queryBottonS = true
                this.queryBotton = !this.queryBotton
                if (data === 'down') {
                    this.queryBottonA = false
                    this.queryBottonA1 = true
                    this.overviewShow = true
                    if (this.downtshow) {
                        this.showDown = false
                    } else {
                        this.showDown = true
                    }

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
            },
            // 清空按钮
            empty(data) {
                this.queryForm.idcType = '全部'
                this.queryForm.deviceProject = '全部'
                this.queryForm.pod = '全部'
                this.queryForm.deviceClass = '全部'
                this.queryForm.newidcType = ['全部']
                this.queryForm.newdeviceProject = ['全部']
                this.queryForm.newpod = ['全部']
                this.queryForm.newdeviceClass = ['全部']
                for (let i = 0; i < this.otherList.length; i++) {
                    this.otherList[i].color = '全部'
                    this.otherList[i].newcolor = ['全部']
                    this.otherList[i].value = ''
                }
                for (let i = 0; i < this.reducelist.length; i++) {
                    this.reducelist[i].color = '全部'
                    this.reducelist[i].newcolor = ['全部']
                    this.reducelist[i].value = ''
                }
                this.allcheckList = []
                this.queryDepartment2()
                this.queryBizSystem()
                // this.queryForm.moduleId = ''
                this.ImgSelected = ''
                this.selectedShow = false

                this.queryForm.deviceType = ''
                this.queryForm.deviceClass3 = ''
                this.queryForm.department1 = ''
                this.queryForm.department2 = ''
                this.queryForm.deviceSN = ''
                this.queryForm.room = ''
                this.queryForm.deptOperation = ''
                this.queryForm.deviceProject = ''
                this.queryForm.deviceModel = ''
                this.queryForm.bizSystem = ''
                this.queryForm.ops = ''
                this.queryForm.insertTime = ''
                this.queryForm.idcType = '全部'
                this.queryForm.projectName = ''
                this.queryForm.pod = '全部'
                this.queryForm.deviceClass = '全部'
                this.queryForm.mfrFactory = '全部'
                this.queryForm.deviceStatus = ''
                this.queryForm.maintenanceFactory = ''
                this.queryForm.ip = ''
                this.queryForm.deviceName = ''

                this.inputDeviceType = ''
                this.inputDeviceClass3 = ''
                this.inputDepartment1 = ''
                this.inputDepartment2 = ''
                this.inputDeviceSN = ''
                this.inputRoom = ''
                this.inputDeptOperation = ''
                this.inputDeviceProject = ''
                this.inputDeviceModel = ''
                this.inputBizSystem = ''
                this.inputOps = ''
                this.inputInsertTime = ''
                this.inputIdcType = ''
                this.inputProjectName = ''
                this.inputPod = ''
                this.inputDeviceClass = ''
                this.inputMfrFactory = ''
                this.inputDeviceStatus = ''
                this.inputMaintenanceFactory = ''
                this.inputIp = ''
                this.inputDeviceName = ''

                this.publicPool = false
                this.publicPeriod = false
                this.publicPOD = false
                this.publicClassification = false
                this.publicBrand = false
                this.publicState = false
                this.publicManufactor = false
                this.publicIP = false
                this.publicDeviceName = false
                this.publicDeviceType = false
                this.publicDeviceClass3 = false
                this.publicDepartment1 = false
                this.publicDepartment2 = false
                this.publicDeviceSN = false
                this.publicDeviceModel = false
                this.publicRoom = false
                this.publicDeviceProject = false
                this.publicDeptOperation = false
                this.publicOps = false
                this.publicInsertTime = false
                this.publicBizSystem = false

                // 恢复初始参数
                this.initParams()
                this.initUrlParams()
                this.queryForm.idcType = '全部'
                this.queryForm.deviceProject = '全部'
                this.queryForm.pod = '全部'
                this.queryForm.deviceClass = '全部'
                this.queryForm.moduleId = this.moduleId
                if (!data) {
                    this.currentPage = 1
                    this.query()
                }
            },
            initParams() {
                let _t = this
                this.queryKeys.forEach((item) => {
                    if (item !== 'moduleId') {
                        _t.queryForm[item] = ''
                    }
                })

                if (_t.queryParams && Object.keys(_t.queryParams).length > 0) {
                    Object.keys(_t.queryParams).forEach((item2) => {
                        _t.queryForm[item2] = _t.queryParams[item2]
                    })
                }
                if (_t.queryParams['startInsertTime'] && _t.queryParams['endInsertTime']) {
                    _t.insertTime = [_t.queryForm['startInsertTime'], _t.queryForm['endInsertTime']]
                }
            },
            // 获取模型列表
            queryModuleList() {
                let _t = this
                rbCmdbModuleServiceFactory.getModuleTree({ 'moduleType': this.moduleType }).then((data) => {
                    _t.moduleList = data
                })
            },

            // 获取表格头部信息
            queryInstanceHeader() {
                let _this = this
                this.checkColumns = []
                _this.selectColumns = []
                _this.moduleColumns = []
                rbCmdbServiceFactory.getInstanceHeader(this.queryForm.moduleId || '').then((item) => {
                    item.forEach(item2 => {
                        _this.moduleColumns.push(item2.filedCode)
                    })
                    // 不显示固定的几列
                    _this.checkColumns = item.filter((item2) => {
                        if (['ip', 'device_name', 'insert_time', 'update_time', 'vlan_number', 'idcType', 'id', 'module_id', 'ipv6', 'ipv6_segment_owner'].indexOf(item2.filedCode) < 0) {
                            return true
                        }
                    })

                    if (_this.dbFilterData.columnInfo) {
                        let ci = JSON.parse(_this.dbFilterData.columnInfo)
                        _this.checkColumns.forEach(item => {
                            if (ci[item['filedCode']]) {
                                _this.selectColumns.push(item)
                                _this.column_data[item['filedCode']] = ci[item['filedCode']]
                            }
                        })
                    }
                    this.layoutTable('viewTable')
                }).catch((item) => {
                    _this.$message.error('获取模型表头失败' + item)
                })
                console.log(this.selectColumns, 'selectColumns')
            },

            // 设置表格查询参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryForm['currentPage'] = this.currentPage
                    this.queryForm['pageSize'] = this.pageSize
                } else {
                    this.initPageChange(this.staticCurrentPage, this.staticPageSize)

                    this.queryForm.startInsertTime = this.insertTime[0] && this.insertTime[0] !== '' ? this.insertTime[0] : ''
                    this.queryForm.endInsertTime = this.insertTime[1] && this.insertTime[1] !== '' ? this.insertTime[1] : ''
                    this.queryForm.sortColumn = this.paginationData.sort
                    this.queryForm.sortType = this.paginationData.order
                    this.queryForm.currentPage = this.initPageChange()
                    this.queryForm.pageSize = this.pageSize
                }
            },
            // 带条件查询
            searchInstanceList: function () {
                this.query()
            },

            // 获取已保存的自定义列
            getColumnFilter() {
                let _this = this
                let _moduleId = ''
                if (this.queryForm.moduleId) {
                    _moduleId = this.queryForm.moduleId
                }
                rbCmdbServiceFactory.getColumnFilter({ menuType: 'CMDB_INSTANCE', moduleId: _moduleId }).then((data) => {
                    _this.dbFilterData = data
                    // 获取表格头信息
                    _this.queryInstanceHeader()
                })
            },

            // 跳转到IP列表
            toIpList(row) {
                let queryData = {
                    query: row,
                    check: false
                }
                this.$emit('tabCheck', queryData)
            },
            // 跳转到网段详情
            toDetail(row) {
                let queryParams = {
                    instanceId: row.id,
                    moduleId: this.queryForm.moduleId,
                    state: 'detail'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
            },

            // 批量修正
            toBatchUpdate() {
                this.BatchEditDisplay = true
            },
            toBatchUpdate1() {
                this.BatchEditDisplay = false
            },
            // 跳转修改页面
            toUpdate() {
                if (this.multipleSelection.length <= 0) {
                    this.$alert('请选择一条数据进行修改', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.multipleSelection.length > 1) {
                    this.$alert('只能对一条数据进行修改', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let queryParams = {
                    instanceId: this.multipleSelection[0].id,
                    moduleId: this.multipleSelection[0]['module_id'],
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'update'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams, viewCodeName: this.viewCodeName, routerName: this.routerName } })
            },
            // 删除功能
            toDelete() {
                if (this.multipleSelection.length <= 0) {
                    this.$alert('请选择需要删除的数据', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let _this = this
                this.$confirm('确定要删除选中的' + this.multipleSelection.length + '条记录吗?').then(() => {
                    rbCmdbServiceFactory.deleteInstance({ 'instanceList': _this.multipleSelection }).then((data) => {
                        if (data && data.success === true) {
                            this.$message({ type: 'success', message: data.message })
                            let queryDatas = {
                                type: 'CMDB_VIEW',
                                key: _this.viewCodeName
                            }
                            rbCmdbServiceFactory.refreshViewsTree(queryDatas).then((res) => {
                                if (res.success) {
                                    this.currentPage = 1
                                    this.query()
                                    this.$emit('handleRefresh')
                                    this.$parent.initTree()
                                } else {
                                    this.$message.error(res.message)
                                }
                            })

                        } else {
                            this.$message({ type: 'error', message: data.message })
                        }
                    })
                })
            },
            onestepFun(type, value, num) {
                // 获取资源池
                if (num == 1) {
                    // 处理引用模型类型
                    let refModuleId = value.cmdbCode.codeBindSource.refModuleId
                    let refFiledCode = value.cmdbCode.codeBindSource.showModuleCodeId
                    let filedCode = value.cmdbCode.filedCode
                    if (!(refModuleId && refFiledCode)) {
                        this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                        return
                    }
                    let params = {
                        codeId: value.cmdbCode.codeId
                    }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.poolList = this.fillSelectOption(res, 'idcType')
                        } else {
                            this.poolList = res
                        }

                        this.poolListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.idcType = '全部'
                            this.queryForm.newidcType = ['全部']
                        }
                        if (this.poolList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                            this.poolB = true
                        }
                    }))
                } else {
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': type },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.poolList = this.fillSelectOption(res, 'idcType')
                        } else {
                            this.poolList = res
                        }

                        this.poolListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.idcType = '全部'
                            this.queryForm.newidcType = ['全部']
                        }
                        if (this.poolList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                            this.poolB = true
                        }
                    })
                }

            },
            onestepFunsql(type, value) {
                // 获取资源池
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: { 'sql': type },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    let newHasAll = false
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            newHasAll = true
                        }
                    }
                    if (!newHasAll) {
                        this.poolList = this.fillSelectOption(res, 'idcType')
                    } else {
                        this.poolList = res
                    }
                    this.poolListStorage = res
                    let bool = false
                    if (value.defaultValue != '') {
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].value == value.defaultValue) {
                                this.selectedShow = true
                                this.allcheckList.push({
                                    father: value,
                                    son: res[i]
                                })
                                bool = true
                            }
                        }
                    }
                    if (!bool) {
                        this.queryForm.idcType = '全部'
                        this.queryForm.newidcType = ['全部']
                    }
                    if (this.poolList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                        this.poolB = true
                    }
                })
            },
            onestepFun1(type, value, num) {
                if (num == 1) {
                    // 处理引用模型类型
                    let refModuleId = value.cmdbCode.codeBindSource.refModuleId
                    let refFiledCode = value.cmdbCode.codeBindSource.showModuleCodeId
                    let filedCode = value.cmdbCode.filedCode
                    if (!(refModuleId && refFiledCode)) {
                        this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                        return
                    }
                    let params = {
                        codeId: value.cmdbCode.codeId
                    }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.projectNameList = this.fillSelectOption(res, 'project_name')
                        } else {
                            this.projectNameList = res
                        }
                        this.projectNameListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.deviceProject = '全部'
                            this.queryForm.newdeviceProject = ['全部']
                        }
                        if (this.projectNameList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                            this.poolB2 = true
                        }
                    }))
                } else {
                    // 获取工期
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': type },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.projectNameList = this.fillSelectOption(res, 'project_name')
                        } else {
                            this.projectNameList = res
                        }
                        this.projectNameListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.deviceProject = '全部'
                            this.queryForm.newdeviceProject = ['全部']
                        }
                        if (this.projectNameList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                            this.poolB2 = true
                        }
                    })
                }

            },
            onestepFun1sql(type, value) {
                // 获取工期
                this.rbHttp.sendRequest({
                    method: 'post',
                    data: { 'sql': type },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    let newHasAll = false
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            newHasAll = true
                        }
                    }
                    if (!newHasAll) {
                        this.projectNameList = this.fillSelectOption(res, 'project_name')
                    } else {
                        this.projectNameList = res
                    }
                    this.projectNameListStorage = res
                    let bool = false
                    if (value.defaultValue != '') {
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].value == value.defaultValue) {
                                this.selectedShow = true
                                this.allcheckList.push({
                                    father: value,
                                    son: res[i]
                                })
                                bool = true
                            }
                        }
                    }
                    if (!bool) {
                        this.queryForm.deviceProject = '全部'
                        this.queryForm.newdeviceProject = ['全部']
                    }
                    if (this.projectNameList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                        this.poolB2 = true
                    }
                })
            },
            twoStepFun(type, value, num) {
                if (num == 1) {
                    // 处理引用模型类型
                    let refModuleId = value.cmdbCode.codeBindSource.refModuleId
                    let refFiledCode = value.cmdbCode.codeBindSource.showModuleCodeId
                    let filedCode = value.cmdbCode.filedCode
                    if (!(refModuleId && refFiledCode)) {
                        this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                        return
                    }
                    let params = { codeId: value.cmdbCode.codeId }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.podList = this.fillSelectOption(res, 'pod_name')
                        } else {
                            this.podList = res
                        }
                        this.podListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.pod = '全部'
                            this.queryForm.newpod = ['全部']
                        }
                        if (this.podList.length * 80 >= document.body.offsetWidth - 200) {
                            this.PODB = true
                        }
                    }))
                } else {
                    // 获取POD池
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': type },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.podList = this.fillSelectOption(res, 'pod_name')
                        } else {
                            this.podList = res
                        }
                        this.podListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.pod = '全部'
                            this.queryForm.newpod = ['全部']
                        }
                        if (this.podList.length * 80 >= document.body.offsetWidth - 200) {
                            this.PODB = true
                        }
                    })
                }

            },
            twoStepFunsql(type, value) {
                // 获取POD池
                this.rbHttp.sendRequest({
                    method: 'post',
                    data: { 'sql': type },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    let newHasAll = false
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            newHasAll = true
                        }
                    }
                    if (!newHasAll) {
                        this.podList = this.fillSelectOption(res, 'pod_name')
                    } else {
                        this.podList = res
                    }
                    this.podListStorage = res
                    let bool = false
                    if (value.defaultValue != '') {
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].value == value.defaultValue) {
                                this.selectedShow = true
                                this.allcheckList.push({
                                    father: value,
                                    son: res[i]
                                })
                                bool = true
                            }
                        }
                    }
                    if (!bool) {
                        this.queryForm.pod = '全部'
                        this.queryForm.newpod = ['全部']
                    }
                    if (this.podList.length * 80 >= document.body.offsetWidth - 200) {
                        this.PODB = true
                    }
                })
            },
            threeStepFun(type, value, num) {
                if (num == 1) {
                    // 处理引用模型类型
                    let refModuleId = value.cmdbCode.codeBindSource.refModuleId
                    let refFiledCode = value.cmdbCode.codeBindSource.showModuleCodeId
                    let filedCode = value.cmdbCode.filedCode
                    if (!(refModuleId && refFiledCode)) {
                        this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                        return
                    }
                    let params = {
                        codeId: value.cmdbCode.codeId
                    }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.deviceClassList = this.fillSelectOption(res, 'device_class')
                        } else {
                            this.deviceClassList = res
                        }
                        this.deviceClassListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.deviceClass = '全部'
                            this.queryForm.newdeviceClass = ['全部']
                        }
                        if (this.deviceClassList.length * 80 >= document.body.offsetWidth - 200) {
                            this.deviceClassB = true
                        }
                    }))
                } else {
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': type },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        let newHasAll = false
                        for (let l = 0; l < res.length; l++) {
                            if (res[l].value == '全部') {
                                newHasAll = true
                            }
                        }
                        if (!newHasAll) {
                            this.deviceClassList = this.fillSelectOption(res, 'device_class')
                        } else {
                            this.deviceClassList = res
                        }
                        this.deviceClassListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {
                                    this.selectedShow = true
                                    this.allcheckList.push({
                                        father: value,
                                        son: res[i]
                                    })
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.deviceClass = '全部'
                            this.queryForm.newdeviceClass = ['全部']
                        }
                        if (this.deviceClassList.length * 80 >= document.body.offsetWidth - 200) {
                            this.deviceClassB = true
                        }
                    })
                }

            },
            threeStepFunsql(type, value) {
                this.rbHttp.sendRequest({
                    method: 'post',
                    data: { 'sql': type },
                    url: '/v1/cmdb/module/queryTableData'
                }).then((res) => {
                    let newHasAll = false
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            newHasAll = true
                        }
                    }
                    if (!newHasAll) {
                        this.deviceClassList = this.fillSelectOption(res, 'device_class')
                    } else {
                        this.deviceClassList = res
                    }
                    this.deviceClassListStorage = res
                    let bool = false
                    if (value.defaultValue != '') {
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].value == value.defaultValue) {
                                this.selectedShow = true
                                this.allcheckList.push({
                                    father: value,
                                    son: res[i]
                                })
                                bool = true
                            }
                        }
                    }
                    if (!bool) {
                        this.queryForm.deviceClass = '全部'
                        this.queryForm.newdeviceClass = ['全部']
                    }
                    if (this.deviceClassList.length * 80 >= document.body.offsetWidth - 200) {
                        this.deviceClassB = true
                    }
                })
            },
            // threeStepFun(){
            //       this.rbHttp.sendRequest({
            //           method: 'GET',
            //           url: `/v1/cmdb/configDict/getAll`
            //       }).then((res) => {
            //           const deviceClassOptions = []
            //           const deviceTypeOptions = []
            //           const deviceStatusOptions = []
            //           if (res) {
            //               this.allDictList = res
            //               res.forEach((item) => {
            //                   if (item.colName === 'device_class') {
            //                       deviceClassOptions.push(this.convertDictConfig(item))
            //                   }
            //                   if (item.colName === 'device_type') {
            //                       deviceTypeOptions.push(this.convertDictConfig(item))
            //                   }
            //                   if (item.colName === 'device_status') {
            //                       deviceStatusOptions.push(this.convertDictConfig(item))
            //                   }
            //               })
            //               this.deviceClassList = this.fillSelectOption(deviceClassOptions, 'device_class')
            //               this.deviceTypeList = deviceTypeOptions
            //           }
            //       })
            // },
            fourStepFun(type, value, num) {
                // 获取设备品牌
                // this.rbHttp.sendRequest({
                //     method: 'POST',
                //     params: { 'type': '生产供应商', 'orderBy': 'produce'},
                //     url: `/v1/cmdb/maintenProduce/listProduceByPage`
                // }).then((res) => {
                //     this.brandList = res.data
                // })
                if (num == 1) {
                    // 处理引用模型类型
                    let refModuleId = value.cmdbCode.codeBindSource.refModuleId
                    let refFiledCode = value.cmdbCode.codeBindSource.showModuleCodeId
                    let filedCode = value.cmdbCode.filedCode
                    if (!(refModuleId && refFiledCode)) {
                        this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                        return
                    }
                    let params = {
                        codeId: value.cmdbCode.codeId
                    }
                    rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                        console.log('ssss====>' + res)
                        this.brandList = res
                        this.brandListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {

                                    this.Brand(res[i].value, i, res[i])
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.mfrFactory = ''
                        }

                        if (this.brandList.length * 80 >= document.body.offsetWidth - 200) {
                            this.equipmentBrandB = true
                        }
                    }))
                } else {
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { params: { 'produce_type': '生产供应商' } },
                        url: '/v1/cmdb/module/module/data?moduleType=default_produce_module_id'
                    }).then((res) => {
                        console.log('ssss====>' + res)
                        this.brandList = res
                        this.brandListStorage = res
                        let bool = false
                        if (value.defaultValue != '') {
                            for (let i = 0; i < res.length; i++) {
                                if (res[i].value == value.defaultValue) {

                                    this.Brand(res[i].value, i, res[i])
                                    bool = true
                                }
                            }
                        }
                        if (!bool) {
                            this.queryForm.mfrFactory = ''
                        }

                        if (this.brandList.length * 80 >= document.body.offsetWidth - 200) {
                            this.equipmentBrandB = true
                        }
                    })
                }

            },
            fourStepFunsql(type, value) {
                console.log(type)
                this.rbHttp.sendRequest({
                    method: 'post',
                    params: { params: { 'produce_type': '维保供应商' } },
                    url: '/v1/cmdb/module/module/data?moduleType=default_produce_module_id'
                }).then((res) => {
                    let _brand = []
                    if (res) {
                        res.forEach(item => {
                            _brand.push({ 'name': item.produce_name, 'description': item.produce_logo })
                        })
                    }
                    let bool = false
                    if (value.defaultValue != '') {
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].value == value.defaultValue) {

                                this.Brand(res[i].value, i, _brand[i])
                                bool = true
                            }
                        }
                    }
                    if (!bool) {
                        this.queryForm.mfrFactory = ''
                    }
                    this.brandList = _brand
                    this.brandListStorage = _brand
                    if (this.brandList.length * 80 >= document.body.offsetWidth - 200) {
                        this.equipmentBrandB = true
                    }
                })
            },
            // 初始化字典值
            initDict() {
                // 获取维保厂家
                // this.rbHttp.sendRequest({
                //     method: 'GET',
                //     params: { 'type': 'mainten_factory' },
                //     url: `/v1/cmdb/configDict/getDictsByType`
                // }).then((res) => {
                //     this.maintenanceFactoryList = this.fillSelectOption(res, 'mainten_factory')
                // })

                // 获取运行状态
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'device_status' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            res.splice(l, 1)
                        }
                    }
                    this.deviceStatusList = this.fillSelectOption(res, 'device_status')
                })
                // // 获取品牌
                // this.rbHttp.sendRequest({
                //     method: 'GET',
                //     params: { 'type': 'device_mfrs' },
                //     url: `/v1/cmdb/configDict/getDictsByType`
                // }).then((res) => {
                //     this.mfrFactoryList = res
                // })
                // 获取机房位置
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'roomId', 'pid': this.PODID },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.roomList = res
                })

                // 加载一级部门
                this.queryDepartment1()
                // 加载二级部门
                this.queryDepartment2()
                // 加载业务系统
                this.queryBizSystem()
            },

            // 选择资源池
            poolClick(item, id) {
                this.queryForm.projectName = ''
                this.queryForm.pod = ''
                this.queryForm.room = ''
                this.publicPeriod = false
                this.publicRoom = false
                this.publicPOD = false
                this.querySelectorClass = true
                this.queryForm.idcType = item
                if (item !== '') {
                    this.publicPool = true
                    this.selectedShow = true
                } else {
                    this.publicPool = false
                }
                this.inputPool = item
                this.poolID = id
                this.searchInstanceList()

                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'project_name', 'pid': id },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let l = 0; l < res.length; l++) {
                        if (res[l].value == '全部') {
                            res.splice(l, 1)
                        }
                    }
                    this.projectNameList = this.fillSelectOption(res, 'project_name')
                })

                // 获取POD池
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'pod_name', 'pid': this.poolID },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.podList = this.fillSelectOption(res, 'pod_name')
                    if (this.podList.length * 100 >= document.body.offsetWidth / 2 - 150) {
                        this.PODB = true
                    } else {
                        this.PODB = false
                    }
                })

                // let arr = this.inputPool
                // function newArr(arr) {
                //     return Array.from(new Set(arr))
                // }
                // this.inputPool = newArr(arr)
                // this.inputPool = this.inputPool.toString()
            },
            // 工期
            periodClick(item) {
                if (item !== '') {
                    this.publicPeriod = true
                    this.selectedShow = true
                } else {
                    this.publicPeriod = false
                }
                this.querySelectorClass = true
                this.queryForm.deviceProject = item
                this.inputPeriod = item
                this.searchInstanceList()
            },
            // 选择POD
            selectPod(item, id) {
                if (item !== '') {
                    this.publicPOD = true
                    this.selectedShow = true
                } else {
                    this.publicPOD = false
                }
                this.queryForm.pod = item
                this.inputPOD = item
                this.PODID = id
                this.searchInstanceList()

                // 获取机房位置
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'roomId', 'pid': this.PODID },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.roomList = res
                })
            },
            // 维保厂家
            mainFactory(item) {
                if (item !== '') {
                    this.publicManufactor = true
                    this.selectedShow = true
                } else {
                    this.publicManufactor = false
                }
                this.queryForm.maintenanceFactory = item
                this.inputManufactor = item
                this.searchInstanceList()
            },
            // 运行状态
            runningState(item) {
                if (item !== '') {
                    this.publicState = true
                    this.selectedShow = true
                } else {
                    this.publicState = false
                }
                this.queryForm.deviceStatus = item
                this.inputState = item
                this.searchInstanceList()
            },
            mouseOver(item, index) {
                this.imgIndex = index
                // this.queryForm.mfrFactory = item
            },
            mouseLeave() {
                this.imgIndex = ''
                // this.queryForm.mfrFactory = ''
            },
            mouseLeaveImgS() {
                this.imgIndex = ''
            },
            mouseOverImg(item, index) {
                this.imgIndex = index
            },
            mouseLeaveImg() {
                this.imgIndex = ''
                // this.queryForm.mfrFactory = ''
            },

            // 选择设备分类
            equipment(item, dictId) {
                if (item !== '') {
                    this.publicClassification = true
                    this.selectedShow = true
                } else {
                    this.publicClassification = false
                }
                this.queryForm.deviceClass = item
                this.inputClassification = item
                this.searchInstanceList()
                if (dictId) {
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        params: { 'type': 'device_type', 'pid': dictId, 'pType': 'device_class' },
                        url: '/v1/cmdb/configDict/getDictsByType'
                    }).then((res) => {
                        this.deviceTypeList = res
                    })
                } else {
                    const deviceTypeOptions = []
                    this.allDictList.forEach((item) => {
                        if (item.colName === 'device_type') {
                            deviceTypeOptions.push(this.convertDictConfig(item))
                        }
                    })
                    this.deviceTypeList = deviceTypeOptions
                }
            },
            queryDeviceClass3(deviceType) {
                this.searchInstanceList()
                const params = { 'type': 'device_class_3', 'pValue': deviceType || '' }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: params,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.deviceClass3List = res
                })
            },
            // 获取一级部门
            queryDepartment1: function () {
                let setf = this
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'department1' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    console.log(res)
                    setf.department1List = res
                })
            },

            // 获取二级部门
            queryDepartment2: function (department1) {
                const params = { 'type': 'department2', 'pid': department1 || '' }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: params,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.department2List = res
                })
            },

            // 获取业务系统
            queryBizSystem: function (department) {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'bizSystem', 'pid': department || '' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.bizSystemList = res
                })
            },

            // 选择一级部门
            changeDepartment1(department) {
                let departmentId = ''
                this.queryForm.department2 = ''
                this.queryForm.bizSystem = ''
                this.department1List.forEach((item) => {
                    if (item.name === department) {
                        departmentId = item.id
                    }
                })
                this.searchInstanceList()
                this.queryDepartment2(departmentId)
                this.queryBizSystem(departmentId)
            },

            // 选择二级部门
            changeDepartment2(department) {
                let departmentId = ''
                this.queryForm.bizSystem = ''
                this.department2List.forEach((item) => {
                    if (item.name === department) {
                        departmentId = item.id
                    }
                })
                this.queryBizSystem(departmentId)
                this.searchInstanceList()
            },
            changeQuery1() {
                this.inputPool = ''
                this.queryForm.idcType = ''
                this.publicPool = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery2() {
                this.inputPeriod = ''
                this.queryForm.deviceProject = ''
                this.publicPeriod = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery3() {
                this.inputPOD = ''
                this.queryForm.pod = ''
                this.publicPOD = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery4() {
                this.inputClassification = ''
                this.queryForm.deviceClass = ''
                this.publicClassification = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery5() {
                this.ImgSelected = ''
                this.inputBrand = ''
                this.queryForm.mfrFactory = ''
                this.publicBrand = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery6() {
                this.inputState = ''
                this.queryForm.deviceStatus = ''
                this.publicState = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery7() {
                this.inputManufactor = ''
                this.queryForm.maintenanceFactory = ''
                this.publicManufactor = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery8() {
                this.inputIP = ''
                this.queryForm.ip = ''
                this.publicIP = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery9() {
                this.inputDeviceName = ''
                this.queryForm.deviceName = ''
                this.publicDeviceName = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery10() {
                this.inputDeviceType = ''
                this.queryForm.deviceType = ''
                this.publicDeviceType = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery11() {
                this.inputDeviceClass3 = ''
                this.queryForm.deviceClass3 = ''
                this.publicDeviceClass3 = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery12() {
                this.inputDepartment1 = ''
                this.queryForm.department1 = ''
                this.publicDepartment1 = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery13() {
                this.inputDepartment2 = ''
                this.queryForm.department2 = ''
                this.publicDepartment2 = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery14() {
                this.inputDeviceSN = ''
                this.queryForm.deviceSN = ''
                this.publicDeviceSN = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery15() {
                this.inputDeviceModel = ''
                this.queryForm.deviceModel = ''
                this.publicDeviceModel = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery16() {
                this.inputRoom = ''
                this.queryForm.room = ''
                this.publicRoom = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery17() {
                this.inputDeviceProject = ''
                this.queryForm.deviceProject = ''
                this.publicDeviceProject = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery18() {
                this.inputDeptOperation = ''
                this.queryForm.deptOperation = ''
                this.publicDeptOperation = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery19() {
                this.inputOps = ''
                this.queryForm.ops = ''
                this.publicOps = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery20() {
                this.insertTime = ''
                this.inputInsertTime = ''
                this.queryForm.insertTime = ''
                this.publicInsertTime = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQuery21() {
                this.inputBizSystem = ''
                this.queryForm.bizSystem = ''
                this.publicBizSystem = false
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
            },
            changeQueryIP() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicIP = false
                this.searchInstanceList()
            },
            IPClick(data) {
                if (data === '') {
                    this.publicIP = false
                } else {
                    this.selectedShow = true
                    this.publicIP = true
                }
                this.inputIP = data
                this.searchInstanceList()
            },
            changeQueryDeviceName() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
                this.publicDeviceName = false
            },
            deviceNameClick(data) {
                if (data === '') {
                    this.publicDeviceName = false
                } else {
                    this.publicDeviceName = true
                    this.selectedShow = true
                }
                this.inputDeviceName = data
                this.searchInstanceList()
            },
            changeQueryDeviceType() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeviceType = false
                this.searchInstanceList()
            },
            deviceTypeClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputDeviceType = data
                this.publicDeviceType = true
                this.searchInstanceList()
            },
            changeQueryDeviceClass3() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeviceClass3 = false
                this.searchInstanceList()
            },
            deviceClass3Click(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputDeviceClass3 = data
                this.publicDeviceClass3 = true
                this.searchInstanceList()
            },
            changeQueryDepartment1() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.searchInstanceList()
                this.publicDepartment2 = false
                this.publicBizSystem = false
                this.publicDepartment1 = false
            },
            department1Click(data) {
                this.publicDepartment2 = false
                this.publicBizSystem = false
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputDepartment1 = data
                this.publicDepartment1 = true
                this.searchInstanceList()
            },
            changeQueryDepartment2() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDepartment2 = false
                this.searchInstanceList()
            },
            department2Click(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputDepartment2 = data
                this.publicDepartment2 = true
                this.searchInstanceList()
            },
            changeQueryDeviceSN() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeviceSN = false
                this.searchInstanceList()
            },
            deviceSNClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                    this.publicDeviceSN = true
                } else {
                    this.publicDeviceSN = false
                }
                this.inputDeviceSN = data
                this.searchInstanceList()
            },
            changeQueryDeviceModel() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeviceModel = false
                this.searchInstanceList()
            },
            deviceModelClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                    this.publicDeviceModel = true
                } else {
                    this.publicDeviceModel = false
                }
                this.inputDeviceModel = data
                this.searchInstanceList()
            },
            changeQueryRoom() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicRoom = false
                this.searchInstanceList()
            },
            roomClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputRoom = data
                this.publicRoom = true
                this.searchInstanceList()
            },
            changeQueryDeviceProject() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeviceProject = false
                this.searchInstanceList()
            },
            deviceProjectClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                    this.publicDeviceProject = true
                } else {
                    this.publicDeviceProject = false
                }
                this.inputDeviceProject = data
                this.searchInstanceList()
            },
            changeQueryDeptOperation() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicDeptOperation = false
                this.searchInstanceList()
            },
            deptOperationClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                    this.publicDeptOperation = true
                } else {
                    this.publicDeptOperation = false
                }
                this.inputDeptOperation = data
                this.searchInstanceList()
            },
            changeQueryOps() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicOps = false
                this.searchInstanceList()
            },
            opsClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                    this.publicOps = true
                } else {
                    this.publicOps = false
                }
                this.inputOps = data
                this.searchInstanceList()
            },
            insertTimeClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputInsertTime = data
                this.publicInsertTime = true
                this.searchInstanceList()
            },
            changeQueryBizSystem() {
                let ID = document.getElementById('querySelector')
                if (ID.clientWidth < 400) {
                    this.selectedShow = false
                }
                this.publicBizSystem = false
                this.searchInstanceList()
            },
            bizSystemClick(data) {
                if (data !== '') {
                    this.selectedShow = true
                }
                this.inputBizSystem = data
                this.publicBizSystem = true
                this.searchInstanceList()
            },
            // 下拉框change事件触发 查询CI列表
            changeQuery() {
                this.searchInstanceList()
            },
            // 文本框blur离焦事件触发 查询CI列表
            blurQuery() {
                this.searchInstanceList()
            },
            // input框展示
            showInput() {
                this.inputShow = !this.inputShow
                this.inputHome = !this.inputHome
                this.conditionShow = !this.conditionShow
            },
            // 清空筛选
            emptyScreening(needSearch) {
                this.insertTime = ''
                Object.keys(this.queryForm).forEach((item) => {
                    this.queryForm[item] = ''
                })
                if (needSearch) {
                    this.searchInstanceList()
                }
            },

            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [{ id: '', value: '全部', type: selectType, name: '', pid: '' }]
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

            // 选中复选框时间
            handleSelectionChange(val) {
                this.multipleSelection = val
            },

            // 自定义表格列宽度
            customWidth: function (label) {
                return label.length * 30
            },

            // 导入功能
            importInstance() {
                this.setImportDisplay(true)
            },

            // 设置导入窗口是否显示 true:显示 false:不显示
            setImportDisplay(val) {
                this.display.isImport = val
            },
            showLoading(loading_text) {
                if (this.queryType && this.queryType !== '') {
                    this.$emit('showLoading', loading_text)
                } else {
                    this.loading = true
                    if (loading_text) {
                        this.loading_text = loading_text
                    }
                }
            },
            hideLoading() {
                if (this.queryType && this.queryType !== '') {
                    this.$emit('hideLoading')
                } else {
                    this.loading = false
                    this.loading_text = ''
                }
            },
            // 导出功能
            exportData() {
                this.showLoading('正在导出数据,由于数据量较大请耐心等待...')
                let allcheckList = this.allcheckList
                let resdata = this.getQueryParams()
                for (let i = 0; i < allcheckList.length; i++) {
                    resdata[allcheckList[i].father.cmdbCode.filedCode] = allcheckList[i].son.valueId ? allcheckList[i].son.valueId : allcheckList[i].son.value
                }
                if (this.publicBrand) {
                    resdata['device_mfrs'] = this.devicemfrsValue
                }
                resdata['pageSize'] = null
                resdata['currentPage'] = null
                rbConfigDictServiceFactory.getDictsByType({ type: 'export_type' }).then((res) => {
                    if (res[0].value !== 'stream') {
                        rbCmdbServiceFactory.exportInstanceList(resdata, this.moduleType).then((data) => {
                            if (data.code === 'success') {
                                let downLoadElement = document.createElement('a')
                                downLoadElement.href = data.path
                                // downLoadElement.download = '历史告警数据列表.xlsx'
                                downLoadElement.setAttribute('download', '资源列表.xlsx')
                                document.body.appendChild(downLoadElement)
                                downLoadElement.click()
                                document.body.removeChild(downLoadElement)
                            } else {
                                this.$message.error(data.message)
                            }
                        }).catch((e) => {
                            this.pageLoading = false
                            this.$message.error('导出数量过大，请重新选择!')
                        }).finally(() => {
                            this.hideLoading()
                        })
                    } else {
                        rbCmdbServiceFactory.exportInstanceListStream(resdata, this.moduleType).then((data) => {
                            let blob = new Blob([data], { type: 'application/msword' })
                            // 创建下载链接
                            let objectUrl = URL.createObjectURL(blob)
                            let downLoadElement = document.createElement('a')
                            downLoadElement.href = objectUrl
                            downLoadElement.download = '资源列表.xlsx'
                            document.body.appendChild(downLoadElement)
                            downLoadElement.click()
                            document.body.removeChild(downLoadElement)
                            URL.revokeObjectURL(objectUrl)
                        }).catch((e) => {
                            this.pageLoading = false
                            this.$message.error('导出数量过大，请重新选择!')
                        }).finally(() => {
                            this.hideLoading()
                        })
                    }
                })
            },
            // 新增
            handEmptySelect() {
                let queryParams = {
                    instanceId: '',
                    moduleId: this.queryForm.moduleId,
                    'device_class': {
                        id: this.parentModuleInfo && this.parentModuleInfo.module_id || '',
                        name: this.parentModuleInfo && this.parentModuleInfo.module_name || ''
                    },
                    'device_type': {
                        id: this.currentModuleInfo && this.currentModuleInfo.module_ud || '',
                        name: this.currentModuleInfo && this.currentModuleInfo.module_name || ''
                    },
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                if (this.condicationCode == 'ip_repository_inner' || this.condicationCode == 'search_ipv6_segment' || this.condicationCode == 'search_public_segment') {
                    this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams, currentTitle: '网段信息', viewCodeName: this.viewCodeName, routerName: this.routerName } })
                } else {
                    this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams, currentTitle: 'ip信息' } })
                }

            },
            // 新增
            handleSelect(parentItem, item) {
                let queryParams = {
                    instanceId: '',
                    moduleId: item.id,
                    'device_class': { id: parentItem.id, name: parentItem.name },
                    'device_type': { id: item.id, name: item.name },
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            // 选择列时，清空table数据，避免卡顿
            popShow() {
                this.tableLoading = true
                this.cloneData = this.$utils.deepClone(this.instanceDataList)
                this.instanceDataList = []
            },
            // 选择列完成后，回填数据
            popHide() {
                this.tableLoading = false
                this.instanceDataList = this.cloneData
            },
            // 选择列
            handleCheckedColumnChange() {
                this.layoutTable('viewTable')

                // 保存
                this.column_data = {}
                this.selectColumns.forEach((item) => {
                    this.column_data[item.filedCode] = true
                })

                let filter = this.dbFilterData
                filter.columnMap = this.column_data
                rbCmdbServiceFactory.updateColumnFilter(filter).then((data) => {
                    // this.query()
                    if (!data.success) {
                        this.$notify({
                            title: '提示',
                            message: data.msg,
                            type: 'error',
                            duration: 3000
                        })
                    }
                })
            },
            // 是否固定到左侧
            isFixedLeft(filedCode) {
                if (filedCode === 'idcType' || filedCode === 'network_segment_address' ||
                    filedCode === 'vlan_number' || filedCode === 'ip' || filedCode === 'device_name') {
                    return 'left'
                }
                return false
            },
            isHost(filedCode) {
                let flag = false
                if (this.moduleColumns.indexOf(filedCode) > 0) {
                    flag = true
                }
                return flag && this.moduleType !== 'business' && this.moduleType !== 'dict'
            },
            isBusiness(filedCode) {
                let flag = false
                if (this.moduleColumns.indexOf(filedCode) > 0) {
                    flag = true
                }
                return flag && this.moduleType !== 'business'
            },
            // 分配或者取消分配，1代表分配，2代表取消分配
            distribution(type) {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let title = type === '1' ? '是否分配列表中勾选的IP？' : '是否取消分配列表中勾选的IP？'
                    let __this = this
                    this.$confirm(title, 'IP分配', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let instanceId = this.multipleSelection.map(item => item.id)
                        let queryDatas = {
                            instanceId: instanceId.join(','),
                            assignStatus: type === '1' ? '已分配' : '未分配',
                            moduleId: this.multipleSelection[0].module_id
                        }
                        __this.updateDistribution(queryDatas)
                    }).catch(() => {
                        __this.$message({
                            type: 'info',
                            message: '已取消'
                        })

                    })
                }

            },
            updateDistribution(queryData) {
                this.loading = true
                console.log(queryData, 'queryData')
                rbCmdbServiceFactory.updateDistributionStatus(queryData).then((res) => {
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: res.msg
                        })
                    } else {
                        this.$message({
                            type: 'error',
                            message: res.msg
                        })
                    }
                    this.loading = false
                    this.currentPage = 1
                    this.query()
                })
            },
            // 初始化权限
            initPermission() {
                let permissions = sessionStorage.getItem('permissions')
                if (permissions === '*') {
                    this.buttonPermission = {
                        create: true,
                        update: true,
                        delete: true,
                        batchUpdate: true,
                        import: true,
                        export: true,
                        assign: true,
                        unAssign: true
                    }
                } else {
                    if (permissions && permissions.length > 0) {
                        permissions.split(',').forEach((item) => {
                            if (item.substr(0, 5) === 'cmdb:') {
                                this.buttonPermission[item.substr(5, item.length)] = true
                            }
                        })
                    }
                }
            },
            rowClass({ rowIndex }) {
                if (this.selectRow.includes(rowIndex)) {
                    return { 'background-color': '#ecf5ff' }
                }
            },
            getIpStatistics() {
                let allcheckList = this.allcheckList
                console.log(allcheckList, 'allcheckListallcheckList')
                let resdata = this.getQueryParams()
                for (let i = 0; i < allcheckList.length; i++) {
                    resdata[allcheckList[i].father.cmdbCode.filedCode] = allcheckList[i].son.valueId ? allcheckList[i].son.valueId : allcheckList[i].son.value
                }
                if (this.publicBrand) {
                    resdata['device_mfrs'] = this.devicemfrsValue
                }
                resdata['pageSize'] = null
                resdata['currentPage'] = null
                this.$emit('resdata', resdata)
            },
            handleRefresh() {
                this.$emit('handleRefresh')
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import "../../iframeHome/components/rem.scss";
    /deep/ .el-menu--popup {
        padding: 0;
    }
    .cursor-pointer {
        cursor: pointer;
    }
    .menu-text {
        display: inline-block;
        width: 100%;
        height: 100%;
        &:hover {
            color: #333;
        }
    }
    .resourceManagement {
        height: 100%;
        width: 100%;
        padding: 0 10px;
        .aLineLeft,
        .aLineRight {
            width: 50%;
            float: left;
            height: 33px;
            background: #fff;
            border-bottom: 1px solid #dee9fc;
            .name {
                z-index: 9;
                float: left;
                width: 150px;
                min-height: 31px;
                line-height: 32px;
                background: #fafbff;
                padding-left: 10px;
                box-sizing: border-box;
                border-right: 1px solid #dee9fc;
            }
            .content {
                float: left;
                overflow: hidden;
                height: 32px;
                width: calc(100% - 10px);
                .poolColor {
                    color: #54adff;
                }
                .elInput {
                    width: 250px;
                    position: relative;
                    .el-select {
                        width: 100%;
                    }
                    .elInputIcon {
                        position: absolute;
                        left: 213px;
                        top: 5px;
                        &:hover {
                            color: #2089da;
                        }
                    }
                }
                .iconimg {
                    display: inline-block;
                    padding: 0 0 10px 0px;
                    width: 100px;
                    height: 40px;
                    margin-top: 10px;
                    margin-left: -1px;
                    .iconimgP {
                        cursor: pointer;
                        text-align: center;
                        height: 40px;
                        line-height: 40px;
                        color: #666666;
                        width: 100px;
                        overflow: hidden; //隐藏文字
                        text-overflow: ellipsis; //显示 ...
                        white-space: nowrap; //不换行
                        border: 1px solid #dfe9fc;
                    }
                    .poolColors {
                        height: 40px;
                        color: #fff !important;
                        background: #3092dc !important;
                        border: 0 !important;
                    }
                    .imgLogo {
                        width: 100%;
                        height: 100%;
                    }
                }
                p {
                    float: left;
                    cursor: pointer;
                    padding-left: 10px;
                    box-sizing: border-box;
                    margin: 0 10px;
                    height: 30px;
                    line-height: 30px;
                }
            }
        }

        .managementRight {
            float: left;
            width: 100%;
            height: 100%;
            overflow: auto;
            .overview {
                background: #fff;
                box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
                border-radius: 6px;
                padding: 1px 0;
                margin-top: 10px;
                margin-right: 10px;
                .overviewContent {
                    border-left: 1px solid #dee9fc;
                    border-right: 1px solid #dee9fc;
                    border-top: 1px solid #dee9fc;
                    margin: 10px;

                    .aLine {
                        z-index: 999;
                        height: 33px;
                        width: 100%;
                        background: #fff;
                        border-bottom: 1px solid #dee9fc;
                        .aLineLeft,
                        .aLineRight {
                            width: 50%;
                            float: left;
                        }
                        .name {
                            z-index: 9;
                            float: left;
                            width: 150px;
                            min-height: 31px;
                            line-height: 32px;
                            background: #fafbff;
                            padding-left: 10px;
                            box-sizing: border-box;
                            border-right: 1px solid #dee9fc;
                        }
                        .content {
                            float: left;
                            overflow: hidden;
                            height: 32px;
                            width: calc(100% - 100px);
                            .poolColor {
                                color: #54adff;
                            }
                            .elInput {
                                width: 250px;
                                position: relative;
                                .el-select {
                                    width: 100%;
                                }
                                .elInputIcon {
                                    position: absolute;
                                    left: 230px;
                                    top: 5px;
                                    &:hover {
                                        color: #2089da;
                                    }
                                }
                            }
                            .iconimg {
                                display: inline-block;
                                padding: 0 0 10px 0px;
                                width: 100px;
                                height: 40px;
                                margin-top: 10px;
                                margin-left: -1px;
                                .iconimgP {
                                    cursor: pointer;
                                    text-align: center;
                                    height: 40px;
                                    line-height: 40px;
                                    color: #666666;
                                    width: 100px;
                                    overflow: hidden; //隐藏文字
                                    text-overflow: ellipsis; //显示 ...
                                    white-space: nowrap; //不换行
                                    border: 1px solid #dfe9fc;
                                }
                                .poolColors {
                                    height: 40px;
                                    color: #fff !important;
                                    background: #3092dc !important;
                                    border: 0 !important;
                                }
                                .imgLogo {
                                    width: 100%;
                                    height: 100%;
                                }
                            }
                            p {
                                float: left;
                                cursor: pointer;
                                padding-left: 10px;
                                box-sizing: border-box;
                                margin: 0 10px;
                                height: 30px;
                                line-height: 30px;
                            }
                        }
                    }
                    .aLinePublic {
                        float: left;
                        width: 50%;
                    }
                    .brand {
                        height: 60px;
                    }
                    .equipmentBrandClass {
                        height: auto;
                        overflow: hidden;
                        width: 100%;
                        .name {
                            height: auto !important;
                            overflow: hidden;
                        }
                        .content {
                            height: auto !important;
                        }
                    }
                }
            }
            .querybotton {
                overflow: hidden;
                .showcCondition {
                    display: contents;
                    height: 30px;
                    line-height: 30px;
                    div {
                        float: left;
                        cursor: pointer;
                        width: calc(100% - 130px);
                        text-align: center;
                        p {
                            width: 350px;
                            margin: 0 auto;
                            background: #fff;
                            border: 1px solid #dee9fc;
                            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
                        }
                    }
                    .botton {
                        float: right;
                        cursor: pointer;
                        margin-right: 25px;
                        color: #54adff;
                    }
                }
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
                    min-height: 25px;
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
        }

        .form {
            margin-top: 10px;
            margin-right: 10px;
            height: calc(100% - 245px);
            background: #fff;
            padding: 10px;
            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
            border-radius: 6px;
        }
    }

    .content .elInput .el-icon-search {
        font-size: 14px;
        margin-top: 3px;
    }
    .content .elInput .el-input .el-input__inner {
        margin-top: 3px;
    }
    .public .el-input__inner {
        padding-left: 0;
        width: 100%;
        color: #278bdc;
    }
    .public .el-input-group__prepend,
    .public .el-input__inner {
        border: aliceblue;
        background: #fff;
    }
    .public .el-input-group__prepend {
        padding-right: 0;
        color: #666666;
    }
    button.el-button.el-button--default:hover {
        border: 1px solid #2089da;
        border-radius: 2px;
    }
    button.el-button.el-button--default {
        border: 1px solid #2089da;
        border-radius: 2px;
    }
    .elInput button.el-button--default {
        height: 21px;
        margin-top: 3px;
        background: #fff;
        padding: 0px 14px;
        border: 0;
    }
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
    .boraderActive {
        border-bottom: 1px solid #dee9fc;
    }
    /deep/ .resourceManagement .yw-el-table .el-table__footer-wrapper,
    .resourceManagement .yw-el-table .el-table__header-wrapper {
        height: 120px !important;
        overflow: visible !important;
    }
</style>
