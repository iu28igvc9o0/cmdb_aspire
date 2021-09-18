<template>
<div class="container">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules"  label-position="top" label-width="80px"  class="demo-ruleForm" style="white-space:nowrap;">
        <el-form-item label="请定义限制条件"    prop="restriction"  align="center" required>
		  <el-radio-group  v-model="ruleForm.restriction">
		  <el-row >
		  <el-col :span="6">
		  <el-radio-button label="OneToOne" class="classbtn">一对一</el-radio-button>
		  </el-col>
		  <el-col :span="6">
		  <el-radio-button label="OneToMany" class="classbtn">一对多</el-radio-button>
		  </el-col>
		  <el-col :span="6">
		  <el-radio-button label="ManyToOne"  class="classbtn">多对一</el-radio-button>
		  </el-col>
		  <el-col :span="6">
		  <el-radio-button label="ManyToMany" class="classbtn">多对多</el-radio-button>
		  </el-col>
		  </el-row>
		</el-radio-group> 
	     </el-form-item>
    <el-form-item align="center">
          <el-alert style="padding:0px 0px 0px 0px;width: 100px;" align="center"
		    :title="ruleForm.dec"
		    type="info"
		    :closable="false">
		  </el-alert>
    </el-form-item>
        
    <el-row>
    <el-col :span="6" style="border-right:1px solid #d1dbe5;height:100%;padding:10px 10px 0px 10px;text-align:center;overflow:auto;">

		  <el-form-item label="请选择关系类型"    prop="relationType" align="left" required>
			<div id='d1' style='height:220px;overflow:auto;'>  
				 <el-radio-group   v-model="ruleForm.relationType" >
								 <div v-for="(item, index) in ruleForm.relationTypeList">
						             <el-row>
								      <el-radio-button class="radiobtn" type="text" style="width:80px;"  :label="item.id">{{item.name}}</el-radio-button>
								      <el-tag 
								           v-if="item.builtin == 'false' && ruleForm.relationType == item.id"
										  :key="item.id"
										  :closable="true"
										  :close-transition="false"
										  @close="delType(item.name)"
										>
										</el-tag>
								      </el-row>
								  </div>    
						          <el-form :model="addForm" ref="addForm" :rules="addRules">
								  			  			 <el-popover
														  ref="popover5"
														  placement="top"
														  width="80"
														  v-model="isShowAdd">
														      <el-form-item prop="newType" label="添加关系">
														 <el-input size="small" placeholder="请输入关系名称" v-model="addForm.newType"></el-input>
														  </el-form-item>
														  <div style="text-align: right; margin: 0">		    
														    <el-button type="primary" size="mini" @click="addType()">确定</el-button>
														    <el-button size="mini" type="text" @click="isShowAdd = false">取消</el-button>
														  </div>
														</el-popover>
														<el-row>
														<el-button class="abt" type="text" v-popover:popover5 icon="plus" size="small">添加关系</el-button>
														</el-row>
								</el-form>
								

					
				</el-radio-group>  
		    </div>
		 </el-form-item>
    </el-col>
    <el-col :span="16" style="height:100%;padding:10px 20px 0px 20px;">
        <div style=" position:relative; height:230px;">
             <el-form-item label="可添加多个模型"  align="left"    prop="checkboxModules" required>
				 <div class="block">
				     <el-checkbox-group v-model="ruleForm.checkboxModules"    > 
				         <div v-for="row in ruleForm.moduleList" >    
							 <el-row :gutter="1">
							 <div v-for="col in row.cols"   style="text-overflow:ellipsis;overflow:hidden;  display:inline; " :title='col.name'>
							  <el-col :span="7" ><el-checkbox-button  :label="col.id" :key="col.id" >{{col.name}}</el-checkbox-button></el-col>
							  </div>
							 </el-row>
						 </div>	 
				      </el-checkbox-group>
		         </div>
			 </el-form-item>
			 <div style=" left:20%;height:10px; position:absolute; bottom:0px;"  >
			 		    <el-pagination small   v-if="ruleForm.moduleList.length > 0" align="center" 
					      @size-change="handleSizeChange"
					      @current-change="handleCurrentChange"
					      :current-page.sync="paginationData.currentPage"
					      :page-size="paginationData.pageSize"
					      layout="prev, pager, next"
					      :total="paginationData.total">
					    </el-pagination>
		     </div>		
	  	</div>	     	    
     </el-col>
 </el-row> 

        <el-form-item align="center" style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >确定</el-button>
            <el-button @click="closeDialog">取消</el-button>
        </el-form-item>
    </el-form>
</div>
</template>
<style>
 .text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  .clearfix:after {
      clear: both
  }

  .box-card {
    width: 150px;
  }
.el-row {
    margin-bottom: 1px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 1px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 5px 0;
    background-color: #f9fafc;
  }
.el-checkbox-button__inner{
width:100px;
text-overflow:ellipsis;overflow:hidden;float:left;  display:inline;
}  
.el-radio-button__inner{
width:130px;

text-align:center;
}
.abt{
width:130px;
}
  
  
.el-radio-button:first-child .el-radio-button__inner{
border-left:0px solid #bfcbd9;

}
.el-radio-button__inner{
border-left:0px solid #bfcbd9;
border-right:0px;
border-top:0px;
border-bottom:0px;
border-radius:0 0 0 0;
}

.classbtn{
border-left:1px solid #bfcbd9;
border-right:1px solid #bfcbd9;
border-top:1px solid #bfcbd9;
border-bottom:1px solid #bfcbd9;
}
</style>
<script>
export
default {
    props: ['InsourceModuleId', 'IntargetModuleId'],
    data() {
        var validateName = (rule, value, callback) => {

            if (value === '') {
                callback(new Error('请输入名称'));
            } else {

                var goHeadUrl = "/cmdb/relatiomap/checkRelationName";
                $.ajax({
                    url: goHeadUrl,
                    type: "POST",
                    data: {
                        'name': value
                    },
                    dataType: "json",
                    async: false,
                    success: function (json, textStatus) {
                        debugger;
                        if (json.success == true) {} else {
                            callback(new Error('名称已存在'));
                        }
                    }.bind(this),
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        debugger;
                        callback(new Error('请求失败'));

                    }.bind(this)
                });

                callback();
            }
        };

        var validateDelName = (rule, value, callback) => {

            if (value === '') {
                callback(new Error('请输入非内置关系名称'));
            } else {

                var goHeadUrl = "/cmdb/relatiomap/checkRelationName";
                $.ajax({
                    url: goHeadUrl,
                    type: "POST",
                    data: {
                        'name': value,
                        'builtin': 'false'
                    },
                    dataType: "json",
                    async: false,
                    success: function (json, textStatus) {
                        debugger;
                        if (json.success == true) {
                            callback(new Error('名称不存在或者为内置关系'));
                        } else {

                        }
                    }.bind(this),
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        debugger;
                        callback(new Error('请求失败'));

                    }.bind(this)
                });

                callback();
            }
        };

        return {
            paginationData: {
                currentPage: 1,
                total: 0,
                pageSize: 12,
                selectPageSizes: [9, 12, 15, 18],
                sort: '',
                order: '',
            },
            ruleForm: {
                dec: '',
                restriction: "OneToOne",
                relationType: '',
                relationTypeList: [],
                checkedIds: [],
                moduleList: [],
                checkboxModules: [],
            },
            isShowAdd: false,
            isShowDel: false,
            rules: {
                checkboxModules: [{
                    type: 'array',
                    required: true,
                    message: '请至少选择一个模型',
                    trigger: 'change'
                }],
                restriction: [{
                    required: true,
                    message: '请选择限制条件',
                    trigger: 'blur'
                }],
                relationType: [{
                    required: true,
                    message: '请选择关系类型',
                    trigger: 'change'
                }],
            },

            addForm: {
                newType: '',
            },

            addRules: {
                newType: [{
                    validator: validateName,
                    trigger: 'blur'
                },   {
                        min: 1, max:4,
                        message: '长度不超过4个字符',
                        trigger: 'blur'
                    }],
            },
            delForm: {
                delType: '',
            },

            delRules: {
                delType: [{
                    validator: validateDelName,
                    trigger: 'blur'
                }],
            },
        };
    },
    watch: {
        ruleForm: {　　　　　　　　　 //注意：当观察的数据为对象或数组时，curVal和oldVal是相等的，因为这两个形参指向的是同一个数据对象
            　　　　　　　　　　
            handler(curVal, oldVal) {
                debugger;
                if (curVal.restriction == 'OneToOne') {
                    this.ruleForm.dec = '该模型下的1个配置可与其他模型关系下的1个配置建立关系';
                }
                if (curVal.restriction == 'OneToMany') {
                    this.ruleForm.dec = '该模型下的1个配置可与其他模型关系下的多个配置建立关系';
                }
                if (curVal.restriction == 'ManyToOne') {
                    this.ruleForm.dec = '该模型下的多个配置可与其他模型关系下的1个配置建立关系';
                }
                if (curVal.restriction == 'ManyToMany') {
                    this.ruleForm.dec = '该模型下的多个配置可与其他模型关系下的多个配置建立关系';
                }　　　
                
                for(var i = 0; i < this.ruleForm.relationTypeList.length; i++  ){
                      if(this.ruleForm.relationTypeList[i].id==this.ruleForm.relationType){
                      this.delForm.delType = this.ruleForm.relationTypeList[i].name;
                      }
                }　

                                                  　　　　　　
            }, 　　　　　　　　　　deep: true　　　　　　　　
        }
    },
    mounted: function () {
            this.initData();
        },

        methods: {
            //提交表单
            onSubmit() {
                    debugger;
                    this.$refs["ruleForm"].validate((valid) => {
                        if (valid) {
                            var paramData = {
                                "restriction": this.ruleForm.restriction,
                                "relationId": this.ruleForm.relationType,
                                "moduleIds": this.ruleForm.checkboxModules,
                                "targetModuleId": this.IntargetModuleId,
                                "sourceModuleId": this.InsourceModuleId,
                            }
                            debugger;
                            var submitUrl = "/cmdb/relatiomap/addModuleRelation";
                            jQuery.ajax({
                                url: submitUrl,
                                type: "POST",
                                cache: false,
                                async: false,
                                traditional: true,
                                data: paramData,
                                success: function (json, textStatus) {
                                    if (json.success == true) {
                                        this.$notify({
                                            title: '提示',
                                            message: '添加成功',
                                            type: 'success',
                                            duration: 3000
                                        });
                                        this.closeWin(json.dataList);

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
                                    this.$notify({
                                        title: '提示',
                                        message: '添加失败',
                                        type: 'error',
                                        duration: 3000
                                    });
                                }.bind(this),
                            });
                            debugger;

                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                },

                initData() {
                    debugger;
                    this.searchType();
                    this.modulePage();
                },

                closeWin: function (dataList) {
                    this.resetForm('ruleForm');
                    this.$emit("childBindAdd", dataList);
                },
                closeDialog() {

                        this.closeWin();

                },
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                    this.ruleForm.name = "";
                    this.ruleForm.code = ""
                    this.ruleForm.dec = "";
                },
                addType() {
                    this.$refs["addForm"].validate((valid) => {
                        if (valid) {
                            var paramData = {
                                "name": this.addForm.newType,
                                "builtin": "false",
                            }
                            debugger;
                            var submitUrl = "/cmdb/relatiomap/addRelatioType";
                            jQuery.ajax({
                                url: submitUrl,
                                type: "POST",
                                cache: false,
                                async: false,
                                traditional: true,
                                data: paramData,
                                success: function (json, textStatus) {
                                    if (json.success == true) {
                                        this.searchType();
                                        this.isShowAdd = false;
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
                                    this.$notify({
                                        title: '提示',
                                        message: '添加失败',
                                        type: 'error',
                                        duration: 3000
                                    });
                                }.bind(this),
                            });
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                },

                delType(name) {

                            var paramData = {
                                "name": name,
                                "builtin": "false",
                            }
                            debugger;
                            var submitUrl = "/cmdb/relatiomap/delRelatioType";
                            jQuery.ajax({
                                url: submitUrl,
                                type: "POST",
                                cache: false,
                                async: false,
                                traditional: true,
                                data: paramData,
                                success: function (json, textStatus) {
                                    if (json.success == true) {
                                        this.searchType();
                                        this.isShowDel = false;
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
                                    this.$notify({
                                        title: '提示',
                                        message: '删除失败',
                                        type: 'error',
                                        duration: 3000
                                    });
                                }.bind(this),
                            });

                },

                searchType() {
                    debugger
                    var sysCodeUrl = '/cmdb/relatiomap/getAllRelatioType';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: '',
                    }).done(function (data) {
                        debugger;
                        this.ruleForm.relationTypeList = data.dataList;
                    }.bind(this));
                },
                //选择每页显示记录数
                handleSizeChange(size) {
                    this.paginationData.pageSize = size;
                    var maxPage = Math.ceil(this.paginationData.total / size);
                    if (this.paginationData.currentPage > maxPage) {
                        this.paginationData.currentPage = maxPage;
                    } else {
                        this.modulePage();
                    }
                },
                //分页
                handleCurrentChange(val) {
                    this.paginationData.currentPage = val;
                    this.modulePage();
                },
                modulePage() {

                    debugger;
                    var query_data = {
                        "pageNumber": this.paginationData.currentPage,
                        "pageSize": this.paginationData.pageSize,
                        "sort": this.paginationData.sort,
                        "orderBy": this.paginationData.order,
                        "sourceModuleId": this.InsourceModuleId,
                        "targetModuleId": this.IntargetModuleId,
                    };
                    var sysCodeUrl = '/cmdb/relatiomap/getModuleByCondition';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: true,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.paginationData.total = data.total;
                        this.ruleForm.moduleList = data.dataList;
                    }.bind(this));

                },


        }
}
</script>
