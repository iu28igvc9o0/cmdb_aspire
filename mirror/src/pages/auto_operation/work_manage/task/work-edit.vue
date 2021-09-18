<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- 作业基础信息 -->
        <el-form class="components-condition yw-form"
                 :model="basicForm"
                 :rules="basicFormRules"
                 ref="basicForm"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="作业名称"
                          prop="pipeline_name"
                          label-width="75px">
                <el-input v-if="!isRunWork"
                          v-model="basicForm.pipeline_name"
                          placeholder="请输入作业名称"
                          clearable></el-input>
                <span v-if="isRunWork">{{basicForm.pipeline_name}}</span>
            </el-form-item>
            <el-form-item label="分类标签"
                          prop="label_id">
                <el-select v-model="basicForm.label_id"
                           placeholder="请选择分类标签"
                           filterable
                           clearable>
                    <el-option v-for="val in labelList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="分组名称"
                          prop="groupTagids">
                <el-tag :key="tag.group_id"
                        style="margin-right: 5px;"
                        v-for="tag in basicForm.groupTagids"
                        closable
                        :disable-transitions="false"
                        @close="handleClose(tag)"
                        size="small">
                    {{tag.group_name}}
                </el-tag>
                <el-popover placement="bottom-start"
                            trigger="click">
                    <comtree :ref="treeName"
                             :isCustomEvent="departmentDialogVisible"
                             :data="groupTreeData"
                             :props="gruopTreeDefault"
                             :exId="treeName"
                             :ex-control="true"
                             :ex-control-opt="customControl"
                             ex-show-search
                             @node-click="handleTreeClick">
                    </comtree>
                    <el-button slot="reference"
                               class="mod-btn"
                               size="small">请选择</el-button>
                </el-popover>
            </el-form-item>
        </el-form>
        <!-- <el-button class="global-variable" disabled>全局变量设置</el-button> -->
        <!-- 作业步骤设置 -->
        <section class="mtop10 bgwhite"
                 v-for="(stepBlock, stepIndex) in stepBlockListHandle"
                 :key="stepIndex">
            <!-- // 操作类型  0 脚本  1 文件分发 -->
            <el-row v-if="stepBlock[0].ops_type === 0"
                    type="flex"
                    :gutter="15"
                    align="top"
                    justify="center">
                <el-col :span="1">
                    <div class="split-num">{{stepIndex+1}}</div>
                    <div class="split-line"
                         :class="{'last-child' : stepIndex+1 === stepBlockList.length}"></div>
                    <!-- 添加步骤按钮 -->
                    <el-dropdown trigger="click"
                                 @command="handleCommand"
                                 v-if="stepIndex+1 === stepBlockList.length">
                        <div class="split-num pointer">+</div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="item in stepTypes"
                                              :key="item.type"
                                              :command="item.type">{{item.label}}</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <!-- <div class="split-line" v-if="stepIndex+1 !== stepBlockList.length"></div> -->
                </el-col>
                <el-col :span="23">
                    <div class="step-block relative">
                        <div class="block-type">
                            <div>脚本</div>
                        </div>

                        <el-form @submit.native.prevent
                                 class="yw-form"
                                 :model="stepFormList[stepIndex]"
                                 :rules="stepFormRulesList[stepIndex]"
                                 :ref="'stepForm' + stepIndex"
                                 :inline="true"
                                 label-width="115px">
                            <div class="step-title-box relative">
                                <el-form-item label="步骤块名称："
                                              prop="block_name"
                                              label-width="115px">
                                    <el-input v-model="stepFormList[stepIndex].block_name"
                                              placeholder="请输入步骤块名称"
                                              clearable></el-input>
                                </el-form-item>
                                <el-button v-if="pageType !== 'detail'"
                                           type="text"
                                           class="global-variable"
                                           @click="deleteStep(stepIndex)">删除步骤</el-button>
                            </div>
                        </el-form>
                        <div class="mtop10">
                            <el-table :data="stepBlock"
                                      :ref="'stepTable' + stepIndex"
                                      :row-key="getRowKeys"
                                      :expand-row-keys="expands"
                                      @expand-change="expandSelect"
                                      class="yw-el-table"
                                      stripe
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column label="序号"
                                                 type="index"></el-table-column>
                                <el-table-column prop="name"
                                                 label="脚本名称">
                                    <template slot="header">
                                        <span class="required-option">脚本名称</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="name">
                                                <el-input v-model="scope.row.name"
                                                          placeholder="请输入脚本名称"
                                                          clearable></el-input>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_ops_user"
                                                 label="服务器账户">
                                    <template slot="header">
                                        <span class="required-option">服务器账户</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="target_ops_user">
                                                <el-select v-model="scope.row.target_ops_user"
                                                           placeholder="请选择服务器账户"
                                                           filterable
                                                           clearable>
                                                    <el-option v-for="val in accountList"
                                                               :key="val.accountName"
                                                               :label="val.accountName"
                                                               :value="val.accountName"></el-option>
                                                </el-select>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_hosts"
                                                 label="服务器数">
                                    <template slot="header">
                                        <span class="required-option">服务器数</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-input v-model="scope.row.target_hosts.length"
                                                  placeholder="请选择服务器"
                                                  readonly></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="script_param"
                                                 label="脚本参数"
                                                 min-width="110">
                                    <template slot-scope="scope">
                                        <el-input v-model="scope.row.script_param"
                                                  placeholder="请输入脚本参数"
                                                  clearable></el-input>
                                        <el-popover trigger="hover"
                                                    placement="top">
                                            <p v-show="scope.row.script_param"
                                               v-for="(item,index) in scope.row.script_param.split(' ')"
                                               :key="item">第{{index+1}}个参数：{{item}}</p>
                                            <div slot="reference">
                                                <i class="el-icon-question"></i>
                                            </div>
                                        </el-popover>

                                        <el-checkbox v-model="scope.row.param_sensive_flag">
                                            敏感参数
                                        </el-checkbox>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作"
                                                 min-width="90">
                                    <template slot-scope="scope">
                                        <div>
                                            <el-checkbox v-model="scope.row.pause_flag"
                                                         :disabled="basicForm.label_id === 'autoRepair' || basicForm.label_id === 'vulnerability'">完成后暂停</el-checkbox>
                                            <el-button type="text"
                                                       :ref="'expandBtn' + stepIndex + scope.$index"
                                                       @click="expandRow(scope, stepIndex)"
                                                       class="mleft10">
                                                <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                <span v-else>编辑更多</span>
                                            </el-button>
                                            <el-button v-if="pageType !== 'detail'"
                                                       type="text"
                                                       @click="deleteRow(scope, stepIndex)">删除</el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                                <!-- 展开更多信息 -->
                                <el-table-column type="expand"
                                                 width="1">
                                    <template slot-scope="props">
                                        <!-- 脚本 -->
                                        <step-info-script :isAutoRepair="basicForm.label_id === 'autoRepair' || basicForm.label_id === 'vulnerability'|| basicForm.label_id === 'vulnerability_goback'"
                                                          :stepMoreData="stepMoreData"
                                                          :rowData="props.row"
                                                          ref="scriptForm"
                                                          @updateScriptEditMoreValid="updateScriptEditMoreValid"
                                                          @updateCurrentRow="updateCurrentRow"
                                                          @upSelectedService="upSelectedService"></step-info-script>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <div v-if="pageType !== 'detail'"
                             class="t-center">
                            <el-button type="text"
                                       icon="el-icon-plus"
                                       @click="addScript(stepIndex)">新增节点</el-button>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row v-else-if="stepBlock[0].ops_type === 1"
                    type="flex"
                    :gutter="15"
                    align="top"
                    justify="center">
                <el-col :span="1">
                    <div class="split-num">{{stepIndex+1}}</div>
                    <div class="split-line"
                         :class="{'last-child' : stepIndex+1 === stepBlockList.length}"></div>
                    <!-- 添加步骤按钮 -->
                    <el-dropdown trigger="click"
                                 @command="handleCommand"
                                 v-if="stepIndex+1 === stepBlockList.length">
                        <div class="split-num pointer">+</div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="item in stepTypes"
                                              :key="item.type"
                                              :command="item.type">{{item.label}}</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <!-- <div class="split-line" v-if="stepIndex+1 !== stepBlockList.length"></div> -->
                </el-col>
                <el-col :span="23">
                    <div class="step-block relative">
                        <!-- // 操作类型  0 脚本  1 文件分发 -->
                        <div class="block-type file">
                            <div>文件分发</div>
                        </div>

                        <el-form @submit.native.prevent
                                 class="yw-form"
                                 :model="stepFormList[stepIndex]"
                                 :rules="stepFormRulesList[stepIndex]"
                                 :ref="'stepForm' + stepIndex"
                                 :inline="true"
                                 label-width="65px">
                            <div class="step-title-box relative">
                                <el-form-item label="步骤块名称："
                                              prop="block_name"
                                              label-width="115px">
                                    <el-input v-model="stepFormList[stepIndex].block_name"
                                              placeholder="请输入步骤块名称"
                                              clearable></el-input>
                                </el-form-item>
                                <el-button v-if="pageType !== 'detail'"
                                           type="text"
                                           class="global-variable"
                                           @click="deleteStep(stepIndex)">删除步骤</el-button>
                            </div>
                        </el-form>
                        <div class="mtop10">
                            <el-table :data="stepBlock"
                                      :ref="'stepTable' + stepIndex"
                                      :row-key="getRowKeys"
                                      :expand-row-keys="expands"
                                      @expand-change="expandSelect"
                                      class="yw-el-table"
                                      stripe
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column label="序号"
                                                 type="index"></el-table-column>
                                <el-table-column prop="name"
                                                 label="任务名称">
                                    <template slot="header">
                                        <span class="required-option">任务名称</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="name">
                                                <el-input v-model="scope.row.name"
                                                          placeholder="请输入任务名称"
                                                          clearable></el-input>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_ops_user"
                                                 label="服务器账户">
                                    <template slot="header">
                                        <span class="required-option">服务器账户</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="target_ops_user">
                                                <el-select v-model="scope.row.target_ops_user"
                                                           placeholder="请选择服务器账户"
                                                           filterable
                                                           clearable>
                                                    <el-option v-for="val in accountList"
                                                               :key="val.accountName"
                                                               :label="val.accountName"
                                                               :value="val.accountName"></el-option>
                                                </el-select>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_hosts"
                                                 label="服务器数">
                                    <template slot="header">
                                        <span class="required-option">服务器数</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-input v-model="scope.row.target_hosts.length"
                                                  placeholder="请选择服务器"
                                                  readonly></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="file_target_path"
                                                 label="目标路径"
                                                 min-width="110">
                                    <template slot="header">
                                        <span class="required-option">目标路径</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="file_target_path">
                                                <el-input v-model="scope.row.file_target_path"
                                                          placeholder="请输入目标路径"
                                                          clearable></el-input>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作"
                                                 min-width="90">
                                    <template slot-scope="scope">
                                        <div>
                                            <el-checkbox v-model="scope.row.pause_flag">完成后暂停</el-checkbox>
                                            <el-button type="text"
                                                       :ref="'expandBtn' + stepIndex + scope.$index"
                                                       @click="expandRow(scope, stepIndex)"
                                                       class="mleft10">
                                                <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                <span v-else>编辑更多</span>
                                            </el-button>
                                            <el-button v-if="pageType !== 'detail'"
                                                       type="text"
                                                       @click="deleteRow(scope, stepIndex)">删除</el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                                <!-- 展开更多信息 -->
                                <el-table-column type="expand"
                                                 width="1">
                                    <template slot-scope="props">
                                        <!-- 文件分发 -->
                                        <step-info-file :stepMoreData="stepMoreData"
                                                        :rowData="props.row"
                                                        :isFromWorkEdit="true"
                                                        ref="fileForm"
                                                        @updateFileEditMoreValid="updateFileEditMoreValid"
                                                        @updateCurrentRow="updateCurrentRow"></step-info-file>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <div v-if="pageType !== 'detail'"
                             class="t-center">
                            <el-button type="text"
                                       icon="el-icon-plus"
                                       @click="addScript(stepIndex)">新增节点</el-button>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row v-else-if="stepBlock[0].ops_type === 2"
                    type="flex"
                    :gutter="15"
                    align="top"
                    justify="center">
                <el-col :span="1">
                    <div class="split-num">{{stepIndex+1}}</div>
                    <div class="split-line"
                         :class="{'last-child' : stepIndex+1 === stepBlockList.length}"></div>
                    <!-- 添加步骤按钮 -->
                    <el-dropdown trigger="click"
                                 @command="handleCommand"
                                 v-if="stepIndex+1 === stepBlockList.length">
                        <div class="split-num pointer">+</div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="item in stepTypes"
                                              :key="item.type"
                                              :command="item.type">{{item.label}}</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <!-- <div class="split-line" v-if="stepIndex+1 !== stepBlockList.length"></div> -->
                </el-col>
                <el-col :span="23">
                    <div class="step-block relative">
                        <!-- // 操作类型  0 脚本  1 文件分发  2 结果文件保存 -->
                        <div class="block-type download">
                            <div>结果保存</div>
                        </div>

                        <el-form @submit.native.prevent
                                 class="yw-form"
                                 :model="stepFormList[stepIndex]"
                                 :rules="stepFormRulesList[stepIndex]"
                                 :ref="'stepForm' + stepIndex"
                                 :inline="true"
                                 label-width="65px">
                            <div class="step-title-box relative">
                                <el-form-item label="步骤块名称："
                                              prop="block_name"
                                              label-width="115px">
                                    <el-input v-model="stepFormList[stepIndex].block_name"
                                              placeholder="请输入步骤块名称"
                                              clearable></el-input>
                                </el-form-item>
                                <el-button v-if="pageType !== 'detail'"
                                           type="text"
                                           class="global-variable"
                                           @click="deleteStep(stepIndex)">删除步骤</el-button>
                            </div>
                        </el-form>
                        <div class="mtop10">
                            <el-table :data="stepBlock"
                                      :ref="'stepTable' + stepIndex"
                                      :row-key="getRowKeys"
                                      :expand-row-keys="expands"
                                      @expand-change="expandSelect"
                                      class="yw-el-table"
                                      stripe
                                      tooltip-effect="dark"
                                      border>
                                <el-table-column label="序号"
                                                 type="index"></el-table-column>
                                <el-table-column prop="name"
                                                 label="任务名称">
                                    <template slot="header">
                                        <span class="required-option">任务名称</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="name">
                                                <el-input v-model="scope.row.name"
                                                          placeholder="请输入任务名称"
                                                          clearable></el-input>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_ops_user"
                                                 label="服务器账户">
                                    <template slot="header">
                                        <span class="required-option">服务器账户</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-form @submit.native.prevent
                                                 :model="scope.row"
                                                 :rules="stepBlockRules"
                                                 :ref="'stepBlock' + stepIndex + scope.$index">
                                            <el-form-item prop="target_ops_user">
                                                <el-select v-model="scope.row.target_ops_user"
                                                           placeholder="请选择服务器账户"
                                                           filterable
                                                           clearable>
                                                    <el-option v-for="val in accountList"
                                                               :key="val.accountName"
                                                               :label="val.accountName"
                                                               :value="val.accountName"></el-option>
                                                </el-select>
                                            </el-form-item>
                                        </el-form>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="target_hosts"
                                                 label="服务器数">
                                    <template slot="header">
                                        <span class="required-option">服务器数</span>
                                    </template>
                                    <template slot-scope="scope">
                                        <el-input v-model="scope.row.target_hosts.length"
                                                  placeholder="请选择服务器"
                                                  readonly></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="file_store_converge_type"
                                                 label="汇聚类型"
                                                 min-width="110">
                                    <template slot-scope="scope">

                                        <el-select v-model="scope.row.file_store_converge_type"
                                                   placeholder="请选择汇聚类型"
                                                   filterable
                                                   style="width:180px;">
                                            <el-option v-for="val in convergeType"
                                                       :key="val.key"
                                                       :label="val.value"
                                                       :value="val.key"></el-option>
                                        </el-select>
                                        <el-checkbox v-model="scope.row.file_store_safety">是否安全</el-checkbox>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作"
                                                 min-width="90">
                                    <template slot-scope="scope">
                                        <div>
                                            <el-checkbox v-model="scope.row.pause_flag">完成后暂停</el-checkbox>
                                            <el-button type="text"
                                                       :ref="'expandBtn' + stepIndex + scope.$index"
                                                       @click="expandRow(scope, stepIndex)"
                                                       class="mleft10">
                                                <span v-if="activeStepIndex === stepIndex && activeRowIndex === scope.$index">收起更多</span>
                                                <span v-else>编辑更多</span>
                                            </el-button>
                                            <el-button v-if="pageType !== 'detail'"
                                                       type="text"
                                                       @click="deleteRow(scope, stepIndex)">删除</el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                                <!-- 展开更多信息 -->
                                <el-table-column type="expand"
                                                 width="1">
                                    <template slot-scope="props">
                                        <!-- 结果文件保存 -->
                                        <step-info-download :stepMoreData="stepMoreData"
                                                            :rowData="props.row"
                                                            :isFromWorkEdit="true"
                                                            ref="downloadForm"
                                                            @updateDownloadEditMoreValid="updateDownloadEditMoreValid"
                                                            @updateCurrentRow="updateCurrentRow"></step-info-download>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>

                        <div v-if="pageType !== 'detail'"
                             class="t-center">
                            <el-button type="text"
                                       icon="el-icon-plus"
                                       @click="addScript(stepIndex)">新增节点</el-button>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </section>
        <div v-if="pageType !== 'detail'"
             class="fixed-bottom-box t-center">
            <el-button @click="saveWork">保存</el-button>
            <el-button type="primary"
                       @click="toRunTask">去执行</el-button>
        </div>
        <el-dialog class="yw-dialog"
                   title="新增子分组"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :append-to-body="true"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form @submit.native.prevent
                         class="yw-form is-required"
                         ref="groupAddForm"
                         :model="groupAddForm"
                         label-width="100px">
                    <el-form-item label="子分组名称"
                                  prop="name">
                        <el-input v-model="groupAddForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="子分组描述">
                        <el-input v-model="groupAddForm.descr"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleUpdateDepartment">确定</el-button>
                <el-button @click="departmentDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import comtree from './../../components/tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'

    import stepInfoScript from './step-info-script.vue'
    import stepInfoFile from './step-info-file.vue'
    import stepInfoDownload from './step-info-download.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    export default {
        components: {
            stepInfoScript,
            stepInfoFile,
            stepInfoDownload,
            comtree
        },
        props: ['isRunWork'],
        data() {
            const that = this
            return {
                username: sessionStorage.username,
                groupAddForm: {
                    parentid: '',
                    name: '',
                    descr: ''
                },
                treeName: 'ggtree',
                // dynamicTags: [],
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                groupTreeData: [],
                departmentDialogVisible: false,
                convergeType: [
                    {
                        key: 0,
                        value: '无汇聚'
                    },
                    {
                        key: 1,
                        value: '追加汇聚'
                    },
                    {
                        key: 2,
                        value: '分类汇聚'
                    }
                ],
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: that.customGroupAdd
                    }
                ],
                // 步骤类型
                stepTypes: [
                    {
                        type: 'script',
                        label: '添加脚本'
                    },
                    {
                        type: 'file',
                        label: '添加文件分发'
                    },
                    {
                        type: 'download',
                        label: '结果文件保存'
                    },
                ],
                // 分类标签
                labelList: [],
                // 获取row的key值
                getRowKeys(row) {
                    return row.rowIndex
                },
                expands: [],

                // 作业基础信息
                basicForm: {
                    pipeline_name: '',
                    label_id: '',
                    groupTagids: []
                },
                // 基本信息校验规则
                basicFormRules: {
                    pipeline_name: [
                        {
                            required: true,
                            message: '请先输入作业名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    label_id: [
                        {
                            required: true,
                            message: '请选择分类标签!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    groupTagids: [
                        { required: true, message: '请选择分组名称', trigger: 'change' }
                    ]
                },
                // 步骤块名称校验规则
                stepFormRules: {
                    block_name: [
                        {
                            required: true,
                            message: '请先输入步骤块名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },
                // 步骤table校验规则
                stepBlockRules: {
                    name: [
                        {
                            required: true,
                            message: '请先输入脚本名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    target_ops_user: [
                        {
                            required: true,
                            message: '请先选择服务器账户!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    file_target_path: [
                        {
                            required: true,
                            message: '请先输入目标路径!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },

                // 步骤块名称
                stepFormList: [
                ],
                // 步骤块校验列表
                stepFormRulesList: [
                ],
                // 步骤块列表
                stepBlockList: [
                    [
                        {
                            ops_type: 0,
                            block_name: '',
                            name: '', // 脚本名称
                            target_ops_user: 'aspire', // 执行账户
                            target_hosts: [], // 目标服务器
                            embed_script: {},
                            script_param: '', // 脚本参数
                            param_sensive_flag: false, // 是否敏感参数
                            pause_flag: false,  // 是否完成后暂停
                            replace_attrs: [],   // 模版参数
                            ile_store_converge_type: 0,  // 无汇聚
                            file_store_safety: false, // 非安全
                        }
                    ],
                ],
                // 新增步骤参数
                defaultStepInfo: {
                    block_name: '', // 步骤块名称
                    name: '', // 脚本名称
                    target_ops_user: 'aspire', // 执行账户
                    target_hosts: [], // 目标服务器
                    script_param: '', // 脚本参数
                    param_sensive_flag: false, // 是否敏感参数
                    pause_flag: false, // 是否完成后暂停
                    replace_attrs: [],   // 模版参数
                    file_store_converge_type: 0,  // 无汇聚
                    file_store_safety: false, // 非安全
                },
                // 新增步骤类型
                stepType: 'script',
                // 更多脚本信息，传递到子组件
                stepMoreData: {
                    serverList: [],
                    allCodeList: [],
                },
                activeStepIndex: 0, // 当前激活步骤
                activeRowIndex: '', // 当前激活步骤下的激活脚本信息

                pageLoading: false,
                loading_text: '请稍候...',
                allCodeList: [],
                // 当前脚本内容
                currentCodeInfo: {
                    codeContent: '',
                    languageType: ''
                },
                isSensitivity: false, // 是否敏感参数
                addForm: {
                    name: '',
                    accountSelected: '',
                    codeParam: '',
                    codeTime: '30',
                    // 目标服务器
                    targetMachines: []
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请先输入脚本名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    accountSelected: [
                        {
                            required: true,
                            message: '请选择执行账户!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    targetMachines: [
                        {
                            required: true,
                            trigger: 'change',
                            validator: (rule, value, callback) => {
                                if (!value.length) {
                                    callback(new Error('请选择目标机器!'))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]
                },

                accountList: [], // 账户列表
                codeCloneList: [], // 脚本克隆列表
                publicCodeList: [], // 公共脚本列表
                codeInfo: {}, // 脚本弹框最新内容
                accountBoxShow: false, // 登记账户
                newAccountName: '', // 新登记账户名称
                chooseMachineType: 1, // 选择主机方式
                serverList: [], // 主机列表
                serversBoxShow: false, // 主机选择弹框
                resultBoxShow: false, // 执行结果弹框
                hasRunCode: false, // 显示执行结果按钮
                stepInstId: '', // 步骤实例ID
                searchWord: '',

                target_hosts: [], // 克隆主机列表

                isScriptEditMoreValid: true, // 脚本类型【编辑更多】信息已全部校验通过
                isFileEditMoreValid: true, // 文件分发类型【编辑更多】信息已全部校验通过
                isDownloadEditMoreValid: true, // 结果保存类型【编辑更多】信息已全部校验通过
                save_pipeline_id: '',  // 保存后，缓存作业id
                gotoRunWork: false, // 去执行作业
                remove_step_list: [], // 删除的步骤ids
                target_exec_object: []
            }
        },
        mixins: [rbAutoOperationMixin],
        watch: {
            // autoRepair类型不可勾选完成后暂停
            'basicForm.label_id'(val) {
                if (val === 'autoRepair' || val === 'vulnerability') {
                    this.stepBlockList.forEach((table) => {
                        table.forEach((row) => {
                            row.pause_flag = false
                        })
                    })
                }
            },
            routerQuery: {
                handler() {
                    this.initPageInfo()
                    this.getGroupTree()
                },
                deep: true,
                immediate: true
            }
        },
        computed: {
            // 目标主机列表，格式为 proxy_id:host_ip
            targetHostList() {
                return this.addForm.targetMachines.map(item => {
                    return `${item.proxy_id}:${item.agent_ip}`
                })
            },
            // 为每行增加rowIndex属性，新增行时需用到
            stepBlockListHandle() {
                this.stepBlockList.forEach((table, tableIndex) => {
                    // 步骤字段
                    console.log('table===', table)
                    let rowBlockName = this.stepFormList[tableIndex] && this.stepFormList[tableIndex].block_name
                    if (!rowBlockName) {
                        this.stepFormList.splice(
                            tableIndex,
                            1,
                            {
                                block_name: table[0].block_name || ''
                            }
                        )

                    }

                    // 步骤字段校验
                    this.stepFormRulesList.push(this.stepFormRules)

                    // 添加唯一 rowIndex
                    table.forEach((row, index) => {
                        console.log(row)
                        row.rowIndex = tableIndex + '' + index
                        if (this.pageType === 'clone') {
                            row.name = row.name + '_' + this.username
                        }
                    })
                })
                return this.stepBlockList
            },
            calcPipelineId() {
                return this.$route.query.pipeline_id || this.save_pipeline_id
            },
            pageType() {
                return this.$route.query.type || 'add'
            },
            // 编辑状态或已保存，带id，克隆不带
            hasSaved() {
                return this.pageType === 'edit' || this.save_pipeline_id
            },
            routerQuery() {
                return this.$route.query
            },
        },
        mounted() {
        },
        destroyed() {
        },
        methods: {
            // 快速新增分组
            customGroupAdd(node, data) {
                this.departmentDialogVisible = true
                this.groupAddForm.parentid = data.group_id
            },
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            // 新增分组
            handleUpdateDepartment() {
                this.$refs['groupAddForm'].validate((valid) => {
                    if (valid) {
                        const params = {
                            parent_id: this.groupAddForm.parentid,
                            group_name: this.groupAddForm.name,
                            group_desc: this.groupAddForm.descr
                        }
                        groupDataService.saveGroup(params).then(() => {
                            this.$message({
                                message: '新增分组成功',
                                type: 'success'
                            })
                            this.getGroupTree()
                            this.resetGroupAddForm()
                            this.handleDepartmentDialogClose()
                        }).catch((error) => {
                            this.showErrorTip(error)
                        })
                    } else {
                        return false
                    }
                })
            },
            // 获取分组树
            getGroupTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.groupTreeData = res
                    // if (res.length > 0) {
                    //   this.deviceAuthDataExpanded = [res[0].group_id]
                    // }
                })
            },
            resetGroupAddForm() {
                this.groupAddForm = {
                    parentid: '',
                    name: '',
                    descr: ''
                }
            },
            handleClose(tag) {
                this.basicForm.groupTagids.splice(this.basicForm.groupTagids.indexOf(tag), 1)
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.basicForm.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.basicForm.groupTagids.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.treeVisible = false
            },
            // 脚本【编辑更多】信息已全部校验通过
            updateScriptEditMoreValid() {
                this.isScriptEditMoreValid = true
            },
            // 文件分发【编辑更多】信息已全部校验通过
            updateFileEditMoreValid() {
                this.isFileEditMoreValid = true
            },
            // 结果文件保存【编辑更多】信息已全部校验通过
            updateDownloadEditMoreValid() {
                this.isDownloadEditMoreValid = true
            },
            // 新增步骤
            handleCommand(command) {
                if (this.pageType === 'detail') {
                    return
                }
                let stepType = command === 'script' ? 0 : command === 'file' ? 1 : 2
                let arr = []
                arr.push(this.handleNewStepData(stepType))
                this.stepBlockList.push(arr)

                this.triggerExpand(this.stepBlockList.length - 1, '0')
            },
            // 删除步骤
            deleteStep(stepIndex) {
                this.$confirm('确认删除该步骤？').then(() => {
                    if (this.stepBlockList.length > 1) {

                        // 把步骤里的所有节点step_id放进删除列表
                        this.stepBlockList[stepIndex].forEach(item => {
                            this.remove_step_list.push(item.step_id)
                        })

                        this.stepBlockList.splice(stepIndex, 1)
                        this.stepFormList.splice(stepIndex, 1)
                    } else {
                        this.$message.warning('至少有一个步骤！')
                    }
                })
            },
            // 初始化新增步骤数据
            handleNewStepData(stepType) {
                let data = JSON.parse(JSON.stringify(this.defaultStepInfo))
                data.ops_type = stepType

                if (data.ops_type === 0) {
                    data.embed_script = {
                        languageType: 1,  // 脚本语言类型
                        codeType: '-1', // 脚本来源
                    }
                } else if (data.ops_type === 1) {
                    data.file_source = []
                } else if (data.ops_type === 2) {
                    data.file_store_source = []
                }
                return data
            },
            upSelectedService(data) {
                this.target_exec_object = []
                this.target_exec_object = data
            },
            // 增加脚本
            addScript(stepIndex) {
                let curStepType = this.stepBlockList[stepIndex][0].ops_type
                let newStepData = this.handleNewStepData(curStepType)
                this.stepBlockList[stepIndex].push(newStepData)

                this.triggerExpand(stepIndex, this.stepBlockList[stepIndex].length - 1)
            },
            // 更新当前行的信息
            updateCurrentRow(target, key, value) {
                if (value === undefined) {
                    return
                }
                if (key === 'embed_script' && value.codeContent) {
                    let obj = {
                        content_type: value.languageType,
                        script_use_desc: value.script_use_desc,
                        script_content: value.codeContent,
                        base64_encode: true,
                    }
                    value = Object.assign(value, obj)
                }
                if (key === 'target_hosts') {
                    let arr = value.map(item => {
                        return `${item.proxy_id}:${item.agent_ip}`
                    })
                    value = arr
                }
                this.$set(target, key, value)
            },
            // 折叠面板每次只能展开一行
            expandSelect(row, expandedRows) {
                if (expandedRows.length) {
                    this.expands = []
                    if (row) {
                        this.expands.push(row.rowIndex)
                    }
                } else {
                    this.expands = []
                }
            },
            // 展开当前步骤
            expandRow(scope, stepIndex) {
                // 没有激活行 || 不同步骤 || 同一步骤下，不同行
                let needToSetIndex = this.activeRowIndex === '' || this.activeStepIndex !== stepIndex || (this.activeStepIndex === stepIndex && this.activeRowIndex !== scope.$index)
                if (needToSetIndex) {
                    this.activeRowIndex = scope.$index
                } else {
                    this.activeRowIndex = ''
                }
                this.activeStepIndex = stepIndex

                let row = scope.row
                this.target_hosts = row.target_hosts
                this.toggleRowExpansion(scope.row, stepIndex)

            },
            toggleRowExpansion(row, stepIndex) {
                this.$refs['stepTable' + stepIndex][0].toggleRowExpansion(this.handleTargetHostList(row))
            },
            handleTargetHostList(row) {
                let list = row.target_host_list
                if (list && list.length) {
                    row.target_host_list = list.map(item => {
                        if (item.agentIp) {
                            item.agent_ip = item.agentIp
                            item.proxy_id = item.proxyId
                        }
                        return item
                    })
                }
                return row
            },

            // 根据id查询脚本内容
            async queryOpsScriptById(row) {
                await rbAutoOperationServicesFactory.queryOpsScriptById(row.script_id).then(res => {
                    console.log('根据id查询脚本内容===', res)
                    row.embed_script = res
                })
            },
            // 根据脚本历史id查询脚本
            async queryOpsScriptHisById(row) {
                await rbAutoOperationServicesFactory.queryOpsScriptHisById(row.script_his_id).then(res => {
                    row.embed_script = res
                    console.log('根据脚本历史id查询脚本===', res)
                })
            },
            // 删除行
            deleteRow(scope, stepIndex) {
                this.$confirm('确定删除该行？').then(() => {
                    let length = this.stepBlockList[stepIndex].length
                    if (length > 1 && this.activeRowIndex === scope.$index && this.activeStepIndex === stepIndex) {
                        this.activeRowIndex = ''
                        this.toggleRowExpansion(scope.row, stepIndex)
                    } else if (length > 1 && this.activeRowIndex > scope.$index) {
                        this.activeRowIndex = this.activeRowIndex - 1
                    }
                    if (length > 1) {
                        this.remove_step_list.push(this.stepBlockList[stepIndex][scope.$index].step_id)
                        this.stepBlockList[stepIndex].splice(scope.$index, 1)
                    } else {
                        this.$message.warning('至少填写一条数据！')
                    }
                })
            },

            // 初始化页面信息
            initPageInfo() {
                // 来源：常用作业执行 => 编辑、克隆
                if (this.pageType === 'edit') {
                    this.getWorkDetail(this.calcPipelineId)
                } else if (this.pageType === 'clone' || this.pageType === 'detail') {
                    this.getWorkDetail(this.$route.query.cloneId)
                } else {
                    this.triggerExpand('0', '0')
                }

                this.queryOpsAccountList()
                this.getAllCodeList()
                this.queryOpsLabelList()

            },
            // 自动触发展开当前步骤第一行，暂时不启用
            triggerExpand(stepIndex, rowIndex) {
                console.log(stepIndex, rowIndex)
                // this.$nextTick(() => {
                //     this.$refs['expandBtn' + stepIndex + rowIndex][0].$emit('click')
                // })
            },
            // 获取作业详情
            getWorkDetail(workId) {
                if (this.$route.query.historyVal === '1') {
                    // 作业历史详情
                    rbAutoOperationServicesFactory
                        .queryOpsPipelineHisById(workId)
                        .then(res => {
                            console.log('作业历史详情===', res)
                            if (res.pipeline_id) {
                                if (this.pageType === 'edit' || this.pageType === 'detail') {
                                    this.basicForm.pipeline_name = res.pipeline_name
                                    this.basicForm.label_id = res.label_id
                                    this.basicForm.groupTagids = res.group_relation_list
                                }
                                this.getStepHisDetail(workId)

                            } else {
                                this.$message.error(res.error_tip)
                            }
                            this.pageLoading = false
                        })
                        .catch(error => {
                            this.pageLoading = false
                            this.showErrorTip(error)
                        })

                } else {
                    // 作业基本信息
                    rbAutoOperationWorkServicesFactory
                        .queryOpsPipelineById(workId)
                        .then(res => {
                            if (res.pipeline_id) {
                                if (this.pageType === 'edit' || this.pageType === 'detail') {
                                    this.basicForm.pipeline_name = res.pipeline_name
                                    this.basicForm.label_id = res.label_id
                                    this.basicForm.groupTagids = res.group_relation_list
                                }
                                this.getStepDetail(workId)

                            } else {
                                this.$message.error(res.error_tip)
                            }
                            this.pageLoading = false
                        })
                        .catch(error => {
                            this.pageLoading = false
                            this.showErrorTip(error)
                        })
                }
            },
            // 获取步骤详情
            getStepDetail(workId) {
                rbAutoOperationWorkServicesFactory
                    .queryOpsStepListByPipelineId(workId)
                    .then(res => {
                        if (res.length) {
                            this.handleList(res)
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 获取历史步骤详情
            getStepHisDetail(workId) {
                rbAutoOperationServicesFactory
                    .queryOpsStepHisListByPipelineHisId(workId)
                    .then(res => {
                        console.log('获取历史步骤详情===', res)
                        if (res.length) {
                            this.handleList(res)
                        } else {
                            this.$message.error(res.error_tip)
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            async handleList(list) {
                let arr = []
                for (let item of list) {
                    let stepOrd = item.block_ord - 1
                    arr[stepOrd] = arr[stepOrd] ? arr[stepOrd] : []
                    if (item.ops_type === 0) {
                        if (this.$route.query.historyVal === '1') {
                            await this.queryOpsScriptHisById(item)
                        } else {
                            await this.queryOpsScriptById(item)
                        }
                        item.param_sensive_flag = item.param_sensive_flag === 1 ? true : false
                        item.pause_flag = item.pause_flag === 1 ? true : false
                        arr[stepOrd].push(item)
                    } else {
                        item.pause_flag = item.pause_flag === 1 ? true : false
                        item.file_store_safety = item.file_store_safety === 1 ? true : false
                        arr[stepOrd].push(item)
                    }
                }
                // list.forEach((item) => {
                //     let stepOrd = item.block_ord - 1
                //     arr[stepOrd] = arr[stepOrd] ? arr[stepOrd] : []

                //     if (item.ops_type === 0) {
                //         this.queryOpsScriptById(item)
                //         item.param_sensive_flag = item.param_sensive_flag === 1 ? true : false
                //         item.pause_flag = item.pause_flag === 1 ? true : false
                //         arr[stepOrd].push(item)
                //     } else {
                //         item.pause_flag = item.pause_flag === 1 ? true : false
                //         arr[stepOrd].push(item)
                //     }
                // })
                this.stepBlockList = arr

                setTimeout(() => {
                    this.triggerExpand('0', '0')
                }, 0)
            },
            // 根据ops历史实例查询ops步骤实例列表
            queryStepInstListByPipelineInstId(pipelineInstanceId) {
                rbAutoOperationWorkServicesFactory.queryStepInstListByPipelineInstId(pipelineInstanceId).then(res => {
                    let cloneData = res[0]
                    this.currentCodeInfo.codeContent = cloneData.script_content
                    this.currentCodeInfo.languageType = cloneData.script_content_type
                    this.addForm.name = cloneData.name
                    this.addForm.accountSelected = cloneData.target_ops_user
                    // 初始化目标机器
                    this.target_hosts = cloneData.target_hosts

                })
            },
            // 登记账户
            registAccount() {
                if (!this.newAccountName) {
                    this.$message('请填写账户名称！')
                    return
                }
            },
            // 添加选中服务器/关闭弹框
            addToTargetMachines() {
                this.addForm.targetMachines = JSON.parse(
                    JSON.stringify(this.multipleSelection)
                )
                this.serversBoxShow = false
                this.$refs.addForm.validateField('targetMachines')
            },
            // 删除选中服务器
            clearMachine(index) {
                this.addForm.targetMachines.splice(index, 1)
            },
            // 传递最新脚本内容
            passCodeContent(data) {
                this.codeInfo = data
            },
            // 打开执行作业弹框
            toRunTask() {
                this.gotoRunWork = true
                this.validForm(this.checkScriptSensitivity)
            },
            // 保存作业
            saveWork() {
                this.gotoRunWork = false
                this.validForm(this.checkScriptSensitivity)
            },
            // 处理保存作业参数数据
            handleStepList() {
                let list = []
                let deepCloneList = JSON.parse(JSON.stringify(this.stepBlockListHandle))
                let ordCounter = 0
                deepCloneList.forEach((stepBlock, stepIndex) => {
                    stepBlock.forEach((item) => {
                        ordCounter++
                        if (this.type === 'clone') {
                            item.block_name = this.stepFormList[stepIndex].block_name + '_' + this.username
                        } else {
                            item.block_name = this.stepFormList[stepIndex].block_name
                        }
                        item.block_ord = stepIndex + 1  // 步骤块顺序
                        item.ord = ordCounter  // 脚本顺序

                        // 编辑状态或已保存，带id，克隆不带
                        item.step_id = this.hasSaved ? item.step_id : ''
                        item.script_id = this.hasSaved ? item.script_id : ''

                        // 脚本类型
                        if (item.ops_type === 0) {
                            item.ops_timeout = item.ops_timeout || '600'
                            item.embed_script.script_name = item.name
                            // 编辑状态带id，克隆不带
                            item.embed_script.script_id = this.hasSaved ? item.script_id : ''
                            // 是否敏感参数
                            item.param_sensive_flag = item.param_sensive_flag === true ? 1 : 0
                            item.file_store_safety = item.file_store_safety === true ? 1 : 0
                            // 文件分发类型
                        } else {
                            item.file_store_safety = item.file_store_safety === true ? 1 : 0
                            // item.file_source = item.file_source
                            item.param_sensive_flag = 0
                        }
                        item.pause_flag = item.pause_flag === true ? 1 : 0
                        item.target_exec_object = this.target_exec_object
                        list.push(item)
                    })
                })
                list.push
                return list
            },
            // 发起保存作业的请求
            sendSaveWorkReq() {
                /** 
                 * 20200907
                 * 增加作业审核状态：保存作业后，作业状态变成待审核 status: 8，不可执行，需要重新审核后才能执行
                 */
                // 点击执行作业，传递作业id获取作业详情，不做保存动作，避免状态变为待审核
                if (this.gotoRunWork && this.hasSaved) {
                    this.expands = []
                    this.activeRowIndex = ''
                    this.pageLoading = false
                    this.$bus.emit('showRunWork', this.calcPipelineId)
                } else {
                    this.pageLoading = true
                    // 获取分组id
                    const groupIdList = []
                    this.basicForm.groupTagids.forEach(item => {
                        groupIdList.push(item.group_id)
                    })
                    let req = {
                        step_count: this.stepBlockListHandle.length,
                        pipeline_id: this.hasSaved ? this.calcPipelineId : '',
                        pipeline_name: this.basicForm.pipeline_name,
                        label_id: this.basicForm.label_id,
                        // 需要保存的步骤列表
                        ops_step_list: this.handleStepList(),
                        remove_step_list: this.remove_step_list,  // 删除的步骤，只在修改作业且存在删除时传
                        group_id_list: groupIdList,
                    }
                    if (this.pageType === 'clone') {
                        rbAutoOperationWorkServicesFactory
                            .copyOpsPipeline(req)
                            .then(res => {
                                if (res.flag) {
                                    this.$message({
                                        message: '保存成功',
                                        type: 'success',
                                    })
                                    this.save_pipeline_id = res.biz_data
                                    this.getWorkDetail(this.save_pipeline_id)
                                } else if (!res.flag) {
                                    this.$message.error(res.error_tip || '保存失败')
                                }
                                this.pageLoading = false
                            })
                            .catch(error => {
                                this.pageLoading = false
                                this.showErrorTip(error)
                            })

                    } else {
                        rbAutoOperationWorkServicesFactory
                            .saveOpsPipeline(req)
                            .then(res => {
                                if (res.flag) {
                                    this.$message({
                                        message: '保存成功',
                                        type: 'success',
                                    })
                                    this.save_pipeline_id = res.biz_data
                                    this.getWorkDetail(this.save_pipeline_id)
                                } else if (!res.flag) {
                                    this.$message.error(res.error_tip || '保存失败')
                                }
                                this.pageLoading = false
                            })
                            .catch(error => {
                                this.pageLoading = false
                                this.showErrorTip(error)
                            })
                    }

                }
            },
            // 校验脚本是否包含敏感指令
            checkScriptSensitivity() {
                this.pageLoading = true
                let deepCloneList = JSON.parse(JSON.stringify(this.handleStepList()))
                let arr = []
                deepCloneList.forEach(item => {
                    if (item.ops_type === 0) {
                        item.embed_script.is_public = 0
                        item.embed_script.label_id = this.basicForm.label_id
                        arr.push(item.embed_script)
                    }
                })

                if (arr.length === 0) {
                    this.sendSaveWorkReq()
                    return
                }

                // 脚本类型步骤才进行校验
                rbAutoOperationServicesFactory
                    .checkScriptSensitivity({ script_list: arr })
                    .then(res => {
                        if (res.flag) {
                            this.sendSaveWorkReq()
                        } else if (!res.flag) {
                            this.$confirm(
                                `${res.error_tip}，确认继续操作吗？`,
                                '提示',
                                {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }
                            ).then(() => {
                                this.sendSaveWorkReq()
                            })
                                .catch(() => {
                                    this.$message({
                                        type: 'info',
                                        message: '已取消'
                                    })
                                    this.pageLoading = false
                                })
                        }
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                        this.pageLoading = false
                    })
            },
            // 校验表单
            validForm(callback) {
                let promiseArr = []
                // 基本信息-作业名称、标签
                const basicFormValid = new Promise((resolve, reject) => {
                    this.$refs.basicForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                promiseArr.push(basicFormValid)

                // 步骤名称
                this.stepFormList.forEach((item, index) => {
                    const stepFormValid = new Promise((resolve, reject) => {
                        this.$refs['stepForm' + index][0].validate(valid => {
                            if (valid) {
                                resolve()
                            } else {
                                reject()
                            }
                        })
                    })

                    // 步骤信息
                    this.stepBlockListHandle[index].forEach((item, blockIndex) => {
                        let blocks = this.$refs['stepBlock' + index + blockIndex]
                        blocks.forEach((block) => {
                            const stepBlockValid = new Promise((resolve, reject) => {
                                block.validate((valid) => {
                                    if (valid) {
                                        resolve()
                                    } else {
                                        reject()
                                    }
                                })
                            })
                            promiseArr.push(stepBlockValid)
                        })
                    })
                    promiseArr.push(stepFormValid)
                })
                Promise.all(promiseArr)
                    .then(() => {
                        this.validRowMore(callback)
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
            triggerValidMore(formName) {
                let self = this
                setTimeout(() => {
                    self.$refs[formName][0].validForm()
                }, 0)
            },
            // 校验每行的【编辑更多】信息
            validRowMore(callback) {
                let self = this
                self.isScriptEditMoreValid = true
                self.isFileEditMoreValid = true
                self.isDownloadEditMoreValid = true
                // self.stepBlockListHandle.forEach((stepBlock, stepIndex) => {
                for (let stepBlock of self.stepBlockListHandle) {
                    // stepBlock.some((block, blockIndex) => {
                    let scriptValid = true
                    for (let block of stepBlock) {
                        if (block.ops_type === 0 && (!block.target_hosts.length || !block.embed_script.script_content)) {
                            // 如果有脚本类型，且没有填写、设置为校验不通过。
                            scriptValid = false
                            self.isScriptEditMoreValid = false
                            self.expands = []
                            self.expands.push(block.rowIndex)

                            // 校验目标服务器、脚本内容
                            self.triggerValidMore('scriptForm')
                            break
                        } else if (block.ops_type === 1 && (!block.target_hosts.length || !block.file_source.length)) {
                            // 如果有文件分发类型，且没有填写、设置为校验不通过。
                            self.isFileEditMoreValid = false
                            self.expands = []
                            self.expands.push(block.rowIndex)

                            // 校验目标服务器、上传文件
                            self.triggerValidMore('fileForm')
                            break
                        } else if (block.ops_type === 2 && (!block.target_hosts.length || !block.file_store_source.length)) {
                            // 如果有结果下载类型，且没有填写、设置为校验不通过。
                            self.isDownloadEditMoreValid = false
                            self.expands = []
                            self.expands.push(block.rowIndex)

                            // 校验目标服务器、目标路径
                            self.triggerValidMore('downloadForm')
                            break
                        }
                    }
                    // 未完成校验，跳出整个循环
                    if (!scriptValid) {
                        break
                    }
                }
                setTimeout(() => {
                    if (self.isScriptEditMoreValid && self.isFileEditMoreValid && self.isDownloadEditMoreValid) {
                        callback && callback()
                    }
                }, 100)
            },
            // 发起执行脚本请求
            sendRunCodeReq() {
                this.hasRunCode = false
                this.pageLoading = true
                let req = {
                    run_name: this.addForm.name,
                    script: {
                        script_param: this.addForm.codeParam,
                        content_type: this.codeInfo.languageType,
                        script_content: this.codeInfo.codeContent,
                        timeout: this.addForm.codeTime,
                        base64_encode: true
                    },
                    target_host_list: this.targetHostList,
                    target_ops_user: this.addForm.accountSelected
                }

                rbAutoOperationWorkServicesFactory.realtimeScriptExecute(req).then(res => {
                    this.pageLoading = false
                    if (res.flag) {
                        this.$message('执行成功')
                        // 弹出执行结果弹框
                        this.stepInstId = res.biz_data
                        this.hasRunCode = true
                        this.resultBoxShow = true
                    }
                })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 查询分类标签
            queryOpsLabelList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsLabelList()
                    .then(res => {
                        this.labelList = res
                    })
            },
            // 查询执行账户列表
            queryOpsAccountList() {
                rbAutoOperationServicesFactory.queryOpsAccountList().then(res => {
                    this.accountList = res
                })
            },
            // 获取克隆脚本列表
            getAllCodeList() {
                let req = {
                    pageNum: 1,
                    pageSize: 200
                }
                rbAutoOperationServicesFactory.queryOpsScriptList(req).then(res => {
                    this.allCodeList = JSON.parse(JSON.stringify(res.dataList))
                    this.stepMoreData.allCodeList = this.allCodeList
                })
            }
        }
    }
</script>


<style lang="scss" scoped>
    .global-variable {
        position: absolute;
        top: 50%;
        right: 0;
        transform: translate(0, -50%);
    }

    .icon-input-add {
        font-size: 24px;
        vertical-align: middle;
    }
    .run-code-box {
        width: 70%;
        margin: 0 auto;
    }
    .account-box {
        padding: 15px;
        border: 1px solid $color-border;
        background: $color-bg;
        overflow: hidden;
    }
    .mleft10 {
        margin-left: 20px;
    }
    .red {
        color: $color-tip-red;
    }
    .split-box {
        padding: 10px;
    }
    .view-result-btn {
        cursor: pointer;
        width: 25px;
        padding: 5px;
        border: 1px solid $color-common;
        background: $color-bg;
        position: fixed;
        right: 10px;
        top: 45%;
    }
    .yw-dialog-main.no-scroll {
        overflow: hidden;
    }
    .drawer-close-btn {
        float: right;
        margin: -20px 10px 12px 0;
    }

    /deep/.el-popover__reference {
        display: inline;
    }
</style>
