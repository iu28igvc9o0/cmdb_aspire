<template>
  <div class="container">
    <el-popover ref="popover" placement="bottom-start" trigger="hover">
    <div id="popover">
     <el-checkbox-group v-model="checkedColumns"  @change="handleCheckedColumnChange">
         <div v-for="column in columnInitData">
             <el-checkbox :label="column.code">{{ column.name }}</el-checkbox>
         </div>
     </el-checkbox-group>

    </div>
   </el-popover>

        <el-row style="padding:15px 15px 15px;">

                     <el-col :span="1" style="padding-right:10px;">

                                <div  style="width:41px;float:right;margin-left:5px;white-space:nowrap;">
                                         <el-input icon="search" style="width:200px;padding:8px 5px 5px 0px;"  v-model="keyword" :maxlength="50" placeholder="请输入内容">
                                        </el-input>
                                        <el-button @click="hideDetailSearch = !hideDetailSearch" style="padding:8px 16px 5px 16px;" icon="search">高级搜索</el-button>
                                        <el-tooltip class="item" effect="dark" content="显示字段" placement="top">
                                             <el-button v-popover:popover type="primary" style="padding:8px 16px 5px 11px;"><i class="fa fa-filter" aria-hidden="true"></i></el-button>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="dark" content="从Excel新增或更新数据" placement="top">
                                             <el-button style="padding:8px 16px 5px 16px;" type="primary"  @click="isImport=true"><i class="fa fa-upload" aria-hidden="true"></i></el-button>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="dark" content="将数据导出为Excel" placement="top">
                                             <el-button style="padding:8px 16px 5px 16px;" type="primary"  @click="isExport=true"><i class="fa fa-download" aria-hidden="true"></i></el-button>
                                        </el-tooltip>

                                        <el-tooltip class="item" effect="dark" content="移交" placement="top">
                                        <el-button style="padding:8px 16px 5px 16px;" type="primary"  @click="handUp"><i class="fa fa-exchange" aria-hidden="true"></i></el-button>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="dark" content="删除" placement="top">
                                        <el-button style="padding:8px 16px 5px 16px;" type="primary" icon="delete" @click="multipleDelete"></el-button>
                                        </el-tooltip>
                                        <el-button style="padding:8px 16px 5px 16px;" type="primary" icon="plus" @click="addInstance">新增配置</el-button>


                                </div>

                    </el-col>
            </el-row>

    <el-row >

      <el-form :label-position="labelPosition" class="demo-form-inline"  label-width="70px" :inline="true" :model="formData2" ref="formData2" >
          <div class="detail-search" v-show="!hideDetailSearch" >
          <el-row>
                 <el-form-item label="配置类型"    prop="moduleId"  >
	              <el-select  placeholder="过滤条件" style="width:200px;"  v-model="formData2.moduleId">
				    <el-option-group
				      v-for="group in selectData"
				      :key="group.id"
				      :label="group.name">
				      <el-option
				        v-for="item in group.item"
				        :key="item.id"
				        :label="item.name"
				        :value="item.id">
				      </el-option>
				    </el-option-group>
				  </el-select>
	             </el-form-item>

               <el-form-item label="标签" prop="tag">
                  <el-input  style="width:200px;" v-model="formData2.tag" placeholder=""></el-input>
              </el-form-item>

                    <el-form-item label="创建日期" prop="insertTime">

                    <el-date-picker  :editable="false"   style="width:200px;" v-model="formData2.insertTime"  type="datetimerange" range-separator="~"  format="yyyy-MM-dd HH:mm:ss" placeholder="时间范围" align="left">
                    </el-date-picker>

                      </el-form-item>
                    <el-form-item label="更新日期" prop="updateTime">

                    <el-date-picker style="width:200px;"  v-model="formData2.updateTime" :editable="false"   type="datetimerange" range-separator="~"  format="yyyy-MM-dd HH:mm:ss" placeholder="时间范围" align="left">
                    </el-date-picker>

                      </el-form-item>

           </el-row>


            <el-row type="flex" >
			  <el-col :span="24"></el-col>
			  <el-col :span="7">
			            <el-form-item >
                            <el-button  @click="getPageData()" type="primary" class="submit-btn">查询</el-button>
                        </el-form-item>
                        <el-form-item >
                            <el-button  @click="clearAllParam" class="submit-btn">清空</el-button>
                        </el-form-item>
              </el-col>
			</el-row>
          </div>
        </el-form>
    </el-row>

    <el-row >
      <el-table v-if="isInit" :data="tableData"   row-key="id" ref="table"
                          stripe class="margin-top-12"  @sort-change="handleRemoteSort"  @selection-change="handleSelectionChange"
                          v-loading="loading"    element-loading-text="拼命加载中">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column   prop="name"  fixed  label="名称"  align="center" v-if="column_data.name == true" sortable="custom"    show-overflow-tooltip>
                        <template scope="scope">
				            <el-button style="text-decoration:underline" type="text" @click="doubleView(scope.row,scope.event)">
				                {{scope.row.name}}
				            </el-button>
				        </template>
                </el-table-column>
                <el-table-column   prop="moduleName" label="配置类型" align="center" v-if="column_data.moduleName == true"   sortable="custom"  show-overflow-tooltip></el-table-column>

                <el-table-column prop="tagName" label="标签" align="center" v-if="column_data.tagName == true"  sortable="custom" show-overflow-tooltip>
                      <template scope="scope">
        <el-popover trigger="click" placement="top" >

											 <el-tag
											  :key="tag"
											  v-for="tag in scope.row.tags"
											  :closable="true"
											  :close-transition="false"
											  @close="handleClose(tag,scope.row)"
											>
											{{tag}}
											</el-tag>
													<el-input
													  class="input-new-tag"
													  v-if="inputVisible"
													  v-model="inputValue"
													  ref="saveTagInput"
													  size="mini"
													  @keyup.enter.native="handleInputConfirm(scope.row)"
													  @blur="handleInputConfirm(scope.row)"
													>
													</el-input>
											<el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新增</el-button>
									  <div style="text-align: right; margin: 0">
									   <el-button type="primary" size="mini" @click="updateTagName(scope.row)">确定</el-button>
									  </div>

          <div slot="reference" class="name-wrapper">
            {{ scope.row.tagName }}<i class="el-icon-edit"></i>
          </div>
        </el-popover>


				      </template>
                </el-table-column>
                <el-table-column prop="insertTime" label="创建时间" align="center" v-if="column_data.insertTime == true"  sortable="custom" show-overflow-tooltip></el-table-column>
                <el-table-column prop="updateTime" label="更新时间" align="center" v-if="column_data.updateTime == true"  sortable="custom" show-overflow-tooltip></el-table-column>

               <div v-for="(item, index) in columnList">
                <el-table-column  :prop="item.code" :label="item.name" align="center"  v-if="column_data[item.code] == true"    show-overflow-tooltip></el-table-column>
                </div>

                <el-table-column label="操作"  align='center' >
                    <template scope="scope">
                      <el-dropdown @command="handleCommand">
                          <span class="el-dropdown-link"><i class="el-icon-caret-bottom el-icon--right"></i></span>
                          <el-dropdown-menu  slot="dropdown">
                              <el-dropdown-item :command="'editRelation'+','+ scope.row.id+','+scope.row.moduleId+','+scope.row.name">编辑关系</el-dropdown-item>
                              <el-dropdown-item :command="'edit'+','+ scope.row.id+','+scope.row.name+','+scope.row.moduleId">编辑</el-dropdown-item>
                              <el-dropdown-item :command="'delete'+','+ scope.row.id">删除</el-dropdown-item>
                          </el-dropdown-menu>
                      </el-dropdown>
                      </template>
                </el-table-column>
          </el-table>

        <div class="block">
        <el-pagination class="text-right padding-right-10" ref="taskPage" align='right'
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="paginationData.currentPage"
          :page-sizes="[10,20,30, 40]"
          :page-size="paginationData.pageSize"
          :total="paginationData.total"
           layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>
    </el-row>
         <el-dialog title="" v-if="isAdd" v-model="isAdd"   @close="addClose">
            <addInstance :circleId="circleId" ref="addInstance" v-on:childBindAdd="closeDialog"></addInstance>
        </el-dialog>
        <el-dialog title="移交" v-if="transforInstanceDialog" v-model="transforInstanceDialog"  size="small" @close="addClose">
            <transforInstance :multipleSelection="multipleSelection" :circleId="circleId" ref="transforInstance" v-on:childBindAdd="closeDialog"></transforInstance>
        </el-dialog>
        <el-dialog title="导入Excel新增或更新数据" v-if="isImport" v-model="isImport"  size="small" :before-close="addClose">
            <importInstances :circleId="circleId" ref="importInstances" v-on:childBindAdd="closeDialog"></importInstances>
        </el-dialog>
         <el-dialog title="将数据导出为Excel" v-if="isExport" v-model="isExport"  size="small" @close="addClose">
            <exportInstances ref="exportInstances" v-on:childBindAdd="closeDialog"></exportInstances>
        </el-dialog>
        <el-dialog title="" v-if="isEdit" v-model="isEdit"  @close="addClose">
            <editInstance ref="editInstance"    :instanceId="instanceId"  :instanceName="instanceName"  :moduleId="moduleId" :isOnlyRead="false" v-on:childBindAdd="closeDialog"></editInstance>
        </el-dialog>
        <el-dialog title="" v-if="isEditRelation" v-model="isEditRelation" size="full" @close="addClose">
            <editMaintainRelation ref="editMaintainRelation"    :instanceId="instanceId"  :moduleId="moduleId"  :circleId="circleId" :instanceName="instanceName" v-on:childBindAdd="closeDialog"></editMaintainRelation>
        </el-dialog>
        <el-dialog title=""  v-if="isView" v-model="isView"  size="full" @close="closeView">
            <enterViewInstance ref="editViewInstance"   type="maintain" :instanceId="instanceId"  :instanceName="instanceName"  :moduleId="moduleId" listType="maintain" v-on:enterView="enterView"></enterViewInstance>
        </el-dialog>
  </div>
</template>

<script>
import router from '../router';
import transforInstance from './transforInstance.vue';
import importInstances from './importInstances.vue';
import exportInstances from './exportInstances.vue';
import editInstance from './editInstance.vue';
import editMaintainRelation from './editMaintainRelation.vue';
import enterViewInstance from './enterViewInstance.vue';
import addInstance from './addInstance.vue';

  export default {
    props: ['viewId','defaultView','circleId'],
    components: {
        transforInstance,
        importInstances,
        exportInstances,
        editInstance,
        editMaintainRelation,
        enterViewInstance,
        addInstance,
    },
    name: 'instanseList',
     data() {

        return {
             labelPosition: 'right',
            // 是否隐藏筛选层
            hideDetailSearch: true,
            keyword: '',
            /**多选框*/
            formData2: {
                name: '',
                moduleId: '',
                tag: '',
                insertTime: [],
                updateTime: [],
            },

            tableData:[],
            columnInitData : [],
            column_data :  {},
            checkedColumns:[],
            columnList:[],
            loading:true,
             paginationData: {
                currentPage: 1,
                total: 0,
                pageSize: 20,
                selectPageSizes: [10, 20, 30, 40],
                sort: 'insertTime',
                order:'desc',
            },
            selectData: [],
            multipleSelection: [],
            transforInstanceDialog: false,
            isInit:false,
            isImport:false,
            isExport:false,
            isShowUpdate:false,
            inputVisible: false,
            inputValue: '',
            isEdit:false,
            isEditRelation:false,
            isView:false,
            isAdd:false,
            currentRow:{},

            instanceId: "",
            instanceName: "",
            moduleId: "",
            isOnlyRead: false,
            columnFilter:{},
            isFilter:'',
      }
    },
 watch: {
            viewId(curVal,oldVal){
            this.initData();
            debugger;
            this.queryData();
　　　　　　　  　},
            keyword(curVal,oldVal){
            debugger;
            this.paginationData.currentPage=1;
            this.getPageData();
　　　　　　　　},
            isView(curVal,oldVal){
                if(!curVal){
                this.$refs.table.toggleRowSelection(this.currentRow,false) ;
                }
　　　　　　　  　},

    },
    mounted: function () {

    this.initData();
    this.queryData();
    this.readonlyDatePicker();
    },
    methods:{
        readonlyDatePicker(){
                var explorer = window.navigator.userAgent;
                if ((!$.support.style) && (!$.support.opacity)) {
                    //alert('IE');
                    function f(){
                       $(".el-date-range-picker__time-header input").attr("readonly","readonly");
                        setTimeout(f,1000);
                    }
                    setTimeout(f,1000);
                    }
            },
        resetForm: function (formName) {
            this.$refs[formName].resetFields();
        },

        queryData: function () {
          // 改变this.currentPage 即触发翻页
          if (this.paginationData.currentPage != 1) {
            this.paginationData.currentPage = 1;
          }else{

          }

          this.isInit = false;
          this.loading = true;
          var insertStartTime='';
          var insertEndTime='';
           if(this.formData2.insertTime.length>0){
           insertStartTime = this.formData2.insertTime[0].format("yyyy-MM-dd HH:mm:ss");
           insertEndTime = this.formData2.insertTime[1].format("yyyy-MM-dd HH:mm:ss");
           }
          var updateStartTime='';
          var updateEndTime='';
           if(this.formData2.updateTime.length>0){
           updateStartTime = this.formData2.updateTime[0].format("yyyy-MM-dd HH:mm:ss");
           updateEndTime = this.formData2.updateTime[1].format("yyyy-MM-dd HH:mm:ss");
           }

           debugger;
            var query_data = {
                "pageNumber": this.paginationData.currentPage,
                "pageSize": this.paginationData.pageSize,
                "sort": this.paginationData.sort,
                "order":this.paginationData.order,
                "defaultView":this.defaultView,
                "viewId": this.viewId,
                "name": this.keyword,
                "moduleId": this.formData2.moduleId,
                "tag": this.formData2.tag,
                'updateStartTime':  updateStartTime,
                'updateEndTime': updateEndTime,
                'insertStartTime':  insertStartTime,
                'insertEndTime': insertEndTime,
                'circleId':this.circleId,

            };
            var sysCodeUrl = '/cmdb/circle/getInstanceByView';
            $.ajax({
                url: sysCodeUrl,
                type: "POST",
                cache: false,
                async: false,
                dataType: "json",
                data: query_data,
            }).done(function(data) {
            debugger;
                     this.isInit = true;
                     this.loading = false;
                this.paginationData.total = data.total;
                this.tableData = data.dataList;
                for (var i = 0; i < this.tableData.length; i++) {
                      this.tableData[i].tags = [];
                      if(this.tableData[i].tagName !== null && this.tableData[i].tagName !== undefined){
                      this.tableData[i].tags = this.tableData[i].tagName.split(",");
                      }
                }
                  console.log(this.tableData);

                this.columnList = data.columList;

                this.columnInitData =[ {code:'name',name:'名称'},{code:'moduleName',name:'配置类型'}, {code:'tagName',name:'标签'}, {code:'insertTime',name:'创建时间'},{code:'updateTime',name:'更新时间'} ];
                this.column_data= {'name':true,'moduleName':true,'tagName':true,'insertTime':true,'updateTime':true };
                for(var i = 0; i < data.columList.length; i++){
                var cu = new Object();
                cu.code = data.columList[i].code;
                cu.name = data.columList[i].name;
                 this.columnInitData.push(cu);
                 this.column_data[data.columList[i].code] = false;
                }

                        this.checkedColumns = [];
                        if(data.columFilter!=null && data.columFilter!='' && data.columFilter!=undefined && data.columFilter.columnInfo !=null && data.columFilter.columnInfo !=''){
                                var obj = JSON.parse(data.columFilter.columnInfo);
                                debugger;
		                        for(var x in obj){
		                            var cc =  obj[x];
		                            var dd = this.column_data[x];
								   this.column_data[x] = obj[x];
								   if(obj[x]){
								   this.checkedColumns.push(x);
                                        }
								}

								debugger;
                        }else{
                        this.checkedColumns = ['name','moduleName','tagName','insertTime','updateTime'];
                        }
                        this.columnFilter = data.columFilter;
                        this.isFilter = data.isFilter;
                           debugger;
            }.bind(this));
        },

        getPageData: function () {

                debugger;
          this.loading = true;
          var insertStartTime='';
          var insertEndTime='';
           if(this.formData2.insertTime.length>0){
           insertStartTime = this.formData2.insertTime[0].format("yyyy-MM-dd HH:mm:ss");
           insertEndTime = this.formData2.insertTime[1].format("yyyy-MM-dd HH:mm:ss");
           }
          var updateStartTime='';
          var updateEndTime='';
           if(this.formData2.updateTime.length>0){
           updateStartTime = this.formData2.updateTime[0].format("yyyy-MM-dd HH:mm:ss");
           updateEndTime = this.formData2.updateTime[1].format("yyyy-MM-dd HH:mm:ss");
           }

           debugger;
            var query_data = {
                "pageNumber": this.paginationData.currentPage,
                "pageSize": this.paginationData.pageSize,
                "sort": this.paginationData.sort,
                "order":this.paginationData.order,
                "defaultView":this.defaultView,
                "viewId": this.viewId,
                "name": this.keyword,
                "moduleId": this.formData2.moduleId,
                "tag": this.formData2.tag,
                'updateStartTime':  updateStartTime,
                'updateEndTime': updateEndTime,
                'insertStartTime':  insertStartTime,
                'insertEndTime': insertEndTime,
                'circleId':this.circleId,

            };
            var sysCodeUrl = '/cmdb/circle/getInstanceByView';
            $.ajax({
                url: sysCodeUrl,
                type: "POST",
                cache: false,
                async: false,
                dataType: "json",
                data: query_data,
            }).done(function(data) {
            debugger;
                this.loading = false;
                this.paginationData.total = data.total;
                this.tableData = data.dataList;
                for (var i = 0; i < this.tableData.length; i++) {
	                      this.tableData[i].tags = [];
	                      if(this.tableData[i].tagName !== null && this.tableData[i].tagName !== undefined){
	                      this.tableData[i].tags = this.tableData[i].tagName.split(",");
	                      }
                }
                if(!this.hideDetailSearch){
               this.hideDetailSearch = !this.hideDetailSearch;
               }
            }.bind(this));
        },

        handleCommand(command) {
        debugger;
        var kk = command.split(",");
          if (kk[0] == 'editRelation') {
          this.isEditRelation = true;
          this.instanceId = kk[1];
          this.moduleId = kk[2];
          this.instanceName=kk[3];

          }else if (kk[0] == 'edit') {

                   this.instanceId=kk[1];
                 this.instanceName=kk[2];
                 this.moduleId= kk[3];
                 this.isOnlyRead=false;
                   this.isEdit = true;

          }else if(kk[0] == 'delete'){

                    this.$confirm('是否确认删除？', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var val = kk[1];

                    debugger;
                    var multipleDeleteUrl="/cmdb/circle/deleteInstance";
                    $.ajax({
                        url: multipleDeleteUrl,
                        type: "POST",
                        data: {'ids': val},
                        dataType: "json",
                        success: function(json, textStatus) {
                            if (json.success) {
                                this.$notify({
                                    title: '提示',
                                    message: '删除成功!',
                                    type: 'success',
                                    duration:3000
                                });
                                this.getPageData();
                            } else {
                                this.$notify({
                                    title: '提示',
                                    message: '删除失败!',
                                    type: 'error',
                                    duration:3000
                                });
                            }
                        }.bind(this),
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            this.$notify({
                                title: '提示',
                                message: '操作失败!',
                                type: 'error',
                                duration:3000
                            });
                        }.bind(this)
                    });
            }).catch(() => {
                this.$notify({
                    title: '提示',
                    message: '已取消删除',
                    type: 'info',
                    duration:3000
                });
            });


            }
        },

       saveOrUpdateTableColumn(){

       },

        //当表格的排序条件发生变化的时候会触发该事件
        handleRemoteSort(column, prop, order){
              this.paginationData.sort = column.prop;
              if(column.order == "ascending"){
                    this.paginationData.order = 'asc';
              }
              if(column.order == "descending"){
                    this.paginationData.order = 'desc';
              }
              if(this.paginationData.sort != null && this.paginationData.sort != '' && this.paginationData.order != null && this.paginationData.order != ''){
              this.getPageData();
              }
        },
        clearAllParam(){
          this.formData2.moduleId = '';
          this.keyword='';
          this.resetForm('formData2');
        },

                //选择每页显示记录数
        handleSizeChange(size) {
            this.paginationData.pageSize = size;
            var maxPage = Math.ceil(this.paginationData.total / size);
            if (this.paginationData.currentPage > maxPage) {
                this.paginationData.currentPage = maxPage;
            } else {
                this.getPageData();
            }
        },
       //分页
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getPageData();
        },
     initData() {
            var sysCodeUrl = '/cmdb/circle/getModuleTree';
            $.ajax({
                url: sysCodeUrl,
                type: "POST",
                cache: false,
                async: true,
                dataType: "json",
                data: "",
            }).done(function(data) {
            debugger;
                this.selectData = data;
            }.bind(this));
        },
      //多选删除
        multipleDelete() {
        debugger;
            if (this.multipleSelection.length == 0) {
                this.$notify({
                    title: '提示',
                    message: '至少选择一行！',
                    type: 'warning',
                    duration:3000
                });
                return;
            }
            this.$confirm('是否确认删除？', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                debugger;
                    var val = "",closeNum=0;
                    for ( var i = 0; i < this.multipleSelection.length; i++) {
                            closeNum++;
                            val +=this.multipleSelection[i].id + ",";
                    }
                    if(closeNum == 0){
                        return false;
                    }
                    debugger;
                    var multipleDeleteUrl="/cmdb/circle/deleteInstance";
                    $.ajax({
                        url: multipleDeleteUrl,
                        type: "POST",
                        data: {'ids': val},
                        dataType: "json",
                        async: false,
                        success: function(json, textStatus) {
                            if (json.success) {
                                this.$notify({
                                    title: '提示',
                                    message: '删除成功!',
                                    type: 'success',
                                    duration:3000
                                });
                              this.isView=false;
			                  this.getPageData();
                            } else {
                                this.$notify({
                                    title: '提示',
                                    message: '删除失败!',
                                    type: 'error',
                                    duration:3000
                                });
                            }
                        }.bind(this),
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            this.$notify({
                                title: '提示',
                                message: '操作失败!',
                                type: 'error',
                                duration:3000
                            });
                        }.bind(this)
                    });
            }).catch(() => {
                this.$notify({
                    title: '提示',
                    message: '已取消删除',
                    type: 'info',
                    duration:3000
                });
            });
        },
      handleSelectionChange(val) {
            this.multipleSelection = val;
        },

     addInstance() {
     this.isAdd=true;
        },
     addClose(done){
     debugger;
            this.getPageData();
            done();
         },

    handUp() {

                if (this.multipleSelection.length == 0) {
                this.$notify({
                    title: '提示',
                    message: '至少选择一行！',
                    type: 'warning',
                    duration:3000
                });
                return;
               }
               this.transforInstanceDialog = true;


       },
    closeDialog: function() {

               this.transforInstanceDialog = false;
               this.isImport = false;
               this.exImport = false;
               this.isEdit = false;
               this.isEditRelation = false;
               this.isAdd = false;
               this.isView = false;
               this.getPageData();
           },

  doubleView: function(row, event){
                this.currentRow = row;
                this.$refs.table.toggleRowSelection(this.currentRow,true) ;
                   this.instanceId=row.id;
                 this.instanceName=row.name;
                 this.moduleId=row.moduleId;
                 this.isOnlyRead=true;
                     this.isView = true;


},

 handleClose(tag,row) {
 debugger;

        row.tags.splice(row.tags.indexOf(tag), 1);
         this.inputVisible = true;
         this.inputVisible = false;
        this.inputValue = '';
       console.log(row);
      },

      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },

      handleInputConfirm(row) {
        let inputValue = this.inputValue;
        if (inputValue) {
          row.tags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      } ,
     updateTagName(row) {
                           var paramData = {
                                "instanceId": row.id,
                                "tags": row.tags,
                                "circleId":this.circleId,
                                "name":row.name,
                                "moduleName":row.moduleName,
                            }
                            debugger;
                            var submitUrl = "/cmdb/circle/updateTagName";
                            jQuery.ajax({
                                url: submitUrl,
                                type: "POST",
                                cache: false,
                                async: false,
                                traditional: true,
                                data: paramData,
                                success: function (json, textStatus) {
                                    if (json.success == true) {
                                        this.getPageData();
                                    } else {
                                    }
                                }.bind(this),
                                error: function (XMLHttpRequest, textStatus, errorThrown) {
                                }.bind(this),
                            });
      },
      enterView : function(type) {
			      if(type == 'edit'){
			      this.isEdit=true;
			      }
			     else if(type == 'handup'){
			      this.transforInstanceDialog=true;
			      }else if(type =='delete'){
			      this.multipleDelete();

			                      }
           },
     closeView : function() {

           },
     handleCheckedColumnChange(value){
                   debugger;
                    for (var x in this.column_data) {
                        this.column_data[x] = false;
                    }
                    for (var i = 0; i < value.length; i++) {
                        var option = value[i];
                        this.column_data[option] = true;
                    }
                     if(this.isFilter  == 'true'){
                              this.columnFilter.columnMap = this.column_data;
                              var submitUrl = "/cmdb/repertryInstance/updateColumnFilter";
                              jQuery.ajax({
                                  url: submitUrl,
                                  type: "POST",
                                  contentType: 'application/json;charset=utf-8', //设置请求头信息
                                  cache: false,
                                  async: false,
                                  traditional: true,
                                  data: JSON.stringify(this.columnFilter),
                                  dataType: "json",
                                  success: function (json, textStatus) {
                                      if (json.success == true) {


                                      } else {
                                          this.$notify({
                                              title: '提示',
                                              message: json.msg,
                                              type: 'error',
                                              duration: 3000
                                          });
                                      }
                                  }.bind(this),
                                  error: function (XMLHttpRequest, textStatus, errorThrown) {

                                  }.bind(this),
                              });
                   }
                    debugger;
     }
    }
}
</script>

<style scoped>

@keyframes myfirst
{
0%   {left:100%;top:0px;}
100% {left:55%; top:0px;}

}


.right-dialog{
    width:45%;
    height:100%;
    left: 55%;
    animation:myfirst 0.8s;
    top:0%;
    background: #fff;
    border-radius: 2px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, .3);
    box-sizing: border-box;
}


.dialog-fade-leave-active {
    animation: dialog-fade-out .3s
}

 .box {
    width: 400px;

    .top {
      text-align: center;
    }

    .left {
      float: left;
      width: 60px;
    }

    .right {
      float: right;
      width: 60px;
    }

    .bottom {
      clear: both;
      text-align: center;
    }

    .item {
      margin: 4px;
    }

    .left .el-tooltip__popper,
    .right .el-tooltip__popper {
      padding: 8px 10px;
    }
  }

    .el-row {
    margin-bottom: 1px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .row-bg {
    padding: 1px 0;
    background-color: #f9fafc;
  }

  /* 高级搜索 */

.detail-search {
    padding: 20px 30px 0px 30px;
    clear: both;
    box-shadow: 0 3px 2px 0 #e6e7e8;
    position: absolute;
    box-sizing:border-box;
    width:100%;
    left:0;
    top: 0px;
    z-index: 999;
    background-color: #FFF;
}

</style>
