<template>

<div class="container">

        <div style="padding:20px 0px 20px 0px;text-align:center;" v-if="!isOnlyRead"><h4>{{instanceName}}</h4></div>

    <el-form :model="validateForm" ref="validateForm" :rules="rules"  label-position="right" class="demo-ruleForm" label-width="100px" style="padding-right:1px;padding-left:50px;">
 <div id="ss"  v-if="isInit" >

	    <div v-for="(item, index) in dataList">
	    
	     <el-form-item  :label="item.form.name+':'"  prop="name"  v-if="item.form.type=='singleRowText' && item.form.code == 'Y_name'"  required >
	                     <div  style="width:calc(100% - 87px);float:left;" align="left">
	                                <el-input v-model="validateForm.name" :disabled="isOnlyRead"></el-input>
	                       </div>           
	    </el-form-item>


	    <el-form-item  :label="item.form.name+':'"  v-if="item.form.hidden == 'false'  && item.form.code != 'Y_name'"   :prop="'key'+item.form.id"    :required=" item.form.required == 'true' ? true : false ">
	     <div  style="width:calc(100% - 87px);float:left;" align="left">
	                                <!--单行-->
	                                <el-input v-if="item.form.type=='singleRowText' " v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead"></el-input>
	                                <!-- 多行-->
	                                <el-input v-if="item.form.type=='multiRowText' "  type="textarea"  :rows="2"  v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead"></el-input>

	                                <!--下拉-->
									<el-select v-if="item.form.type=='listSel' " v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead">
								    <el-option  v-for="op in item.formOptions" :key="op.value"  :label="op.name" :value="op.value">
								    </el-option>
								    </el-select>
								   <!--单选-->
								      <el-radio-group v-if="item.form.type=='singleSel' " v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead">
									    <el-radio v-for="op in item.formOptions" :label="op.value" :key="op.value">{{op.name}}</el-radio>
									  </el-radio-group>
									<!--多选  -->
								      <el-checkbox-group v-if="item.form.type=='multiSel' " v-model="validateForm['key'+item.form.id]" >
									    <el-checkbox v-for="op in item.formOptions" :label="op.value" :key="op.value" :disabled="isOnlyRead">{{op.name}}</el-checkbox>
									  </el-checkbox-group>
									<!-- 小数-->
									 <div   v-if="item.form.type=='double' "><el-input-number v-model="validateForm['key'+item.form.id]" :step="0.01" :disabled="isOnlyRead"></el-input-number>{{item.form.unit}}</div>
									<!--整数-->
									<div   v-if="item.form.type=='int' " > <el-input-number v-model="validateForm['key'+item.form.id]" :step="1" :disabled="isOnlyRead"></el-input-number>{{item.form.unit}}</div>
								   <!--附件-->
								    <div v-if="item.form.type=='file' " >
										<el-upload
										  class="upload-demo"
										  :action="'/cmdb/instance/uploadIcon/'+item.form.id"
										  :on-success="handleSuccess"
										  :on-remove="handleRemove" :disabled="isOnlyRead"
										   :file-list="validateForm['key'+item.form.id]">
										  <el-button :disabled="isOnlyRead" size="small" type="primary">点击上传</el-button>
										</el-upload>
					                </div>
					                <!--日期-->
					                <el-date-picker v-if="item.form.type=='dateTime' "
								      v-model="validateForm['key'+item.form.id]"
								      type="datetime"
								      placeholder="选择日期时间" :disabled="isOnlyRead">
								    </el-date-picker>
								    <!--表格-->
								     <el-table v-if="item.form.type=='table'" :data="validateForm['key'+item.form.id]" highlight-current-row @current-change="handleCurrentChange" >
						                <el-table-column v-for="it in item.formFields" :key="it.id" inline-template :label="it.name">
						                    <div>
						                        <el-input :value="row.name" size="small" v-model="row[it.key]" :disabled="isOnlyRead">
						                        </el-input>
						                    </div>
						                </el-table-column>
						                <el-table-column inline-template prop="address" label="操作" width="80" v-if="!isOnlyRead">
						                    <div>
						                        <el-dropdown @command="handleCommand" trigger="click" menu-align="start">
						                            <i class="fa fa-cog"></i>
						                            <el-dropdown-menu slot="dropdown">
						                                <el-dropdown-item command="add_row">添加行</el-dropdown-item>
						                                <el-dropdown-item command="del_row">删除行</el-dropdown-item>
						                            </el-dropdown-menu>
						                        </el-dropdown>
						                    </div>
						                </el-table-column>
						            </el-table>

								    <!--引用-->
	                                <el-input v-if="item.form.type=='reference' " v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead"></el-input>
	                                   <!--富文本-->
	                                  <vue-editor  v-if="item.form.type=='richText' "  v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead"></vue-editor>
	                                 <!-- 级联菜单 -->
						            <el-cascader v-if="item.form.type=='cascader'"  :options="item.casoptions[item.form.id]" v-model="validateForm['key'+item.form.id]" :disabled="isOnlyRead">
						            </el-cascader>

						            <!--图标-->
								    <div v-if="item.form.type=='image' " >
											 <el-upload
											  class="upload-demo"
											  :action="'/cmdb/instance/uploadIcon/'+item.form.id"
											  :on-success="handleSuccess"
											  :on-remove="handleRemove"
											  :file-list="validateForm['key'+item.form.id]" :disabled="isOnlyRead"
											  list-type="picture">
											  <el-button size="small" :disabled="isOnlyRead" type="primary">点击上传</el-button>
											</el-upload>
					                </div>
	
						             <!-- 属性分组 -->
						            <div v-if="item.form.type=='groupLine'">
						                <div style="height:35px;background:#20a0ff;margin-left:-100px;margin-right:-70px;color:#fff;line-height:35px;border-color:#20a0ff;border-radius:4px">
						                    <span style="margin-left:20px;">{{item.form.name}}</span>
						                    <i class="el-icon-caret-bottom" style="float:right;margin-right:15px;line-height:35px;"></i>
						                </div>
						            </div>
  </div>

	    </el-form-item>
	    </div>
     </div> 
        <el-form-item v-if="!isOnlyRead" align="center" style="margin-top:15px;padding-right:150px;padding-left:0px;">
            <el-button  type="primary" @click="onSubmit" >保存</el-button>
            <el-button  @click="closeDialog">取消</el-button>
        </el-form-item>
    </el-form>


</div>
</template>
<style>

  #test-hot {
    width: 800px;
    height: 800px;
    overflow: hidden;
  }

.btnIcon {
    width: 60px;
    height: 60px;
    border: 1px dashed #d1dbe5;
    font-size: 12px;
    color: #48576a;
    line-height: 60px;
    text-align: center;
    border-radius: 2px;
    cursor: pointer;
}

.iconDialog .el-dialog__body {}

.iconDialog .el-tabs {}

.sysicon {
    background-color: #1d6aa7;
    border-radius: 5px;
    cursor: pointer;
}

.sysicon li {
    padding: 10px;
}

.sysicon li:hover {
    border-radius: 10px;
    background: #666;
}

.iconlist {
    float: left;
    list-style-type: none;
    margin: 10px 10px 0px 10px;
}
</style>
<script>
import {downloadUrl}   from '../assets/js/constants';
import router from '../router';
import {
  VueEditor
} from 'vue2-editor';

  export
  default {
      components: {
          VueEditor
      },
      props: ['instanceId','instanceName','moduleId','isOnlyRead'],
      data() {
          var validateAll = (rule, value, callback) => {

          };

          return {
              ruleForm: {
                  id: "",
                  name: "",
                  formValues: [],
              },

              rules: {},
              isInit: false,
              validateForm: {},
              dataList: [],
              contextMenu: { //自定义右键菜单，可汉化，默认布尔值
                  items: {
                      "row_above": {
                          name: '上方插入一行'
                      },
                      "row_below": {
                          name: '下方插入一行'
                      },
                      "hsep1": "---------", //提供分隔线
                      "remove_row": {
                          name: '删除行',
                      },

                      "hsep2": "---------",
                  }
              }, //右键效果
              formRules:[],
          }

      },

      created: function () {


          },
          mounted: function () {

              this.initData();
              this.readonlyDatePicker();
          },

          methods: {
             readonlyDatePicker(){
                var explorer = window.navigator.userAgent;
                if ((!$.support.style) && (!$.support.opacity)) {
                    //alert('IE');
                    function f(){
                       $(".el-date-picker__time-header input").attr("readonly","readonly");
                        setTimeout(f,1000);
                    }
                    setTimeout(f,1000);
                    }
            },
              //提交表单
              onSubmit() {
                      this.$refs["validateForm"].validate((valid) => {
                          if (valid) {
                          debugger;
                              var validator = require('validator.tool');
                              var v = new validator();
                              for (var i = 0; i < this.dataList.length; i++) {
                                for (var j = 0; j < this.dataList[i].formParams.length; j++) {
                                      //邮箱,ip验证
                                      if (typeof (this.dataList[i].formParams[j].key) != "undefined" && this.dataList[i].formParams[j].key == 'validation') {
                                        for(var jjj = 0; jjj < this.formRules.length; jjj++){ 
                                          if (typeof (this.dataList[i].formParams[j].value) != "undefined" && this.dataList[i].formParams[j].value == this.formRules[jjj].id &&  this.formRules[jjj].code=='IP' && !v.isIp(this.validateForm['key' + this.dataList[i].form.id])) {
                                              var nameS = this.dataList[i].form.name;
                                              this.$notify({
                                                  title: '提示',
                                                  message: nameS + ':IP验证失败！',
                                                  type: 'error',
                                                  duration: 3000
                                              });
                                              return false;
                                          }
                                          if (typeof (this.dataList[i].formParams[j].value) != "undefined" && this.dataList[i].formParams[j].value == this.formRules[jjj].id &&  this.formRules[jjj].code == 'EMAIL' && !v.isEmail(this.validateForm['key' + this.dataList[i].form.id])) {
                                              var nameS = this.dataList[i].form.name;
                                              this.$notify({
                                                  title: '提示',
                                                  message: nameS + ':EMAIL验证失败！',
                                                  type: 'error',
                                                  duration: 3000
                                              });
                                              return false;
                                          }
                                         } 
                                      }
                                  }
                              
                              
                                  if (this.dataList[i].form.type == 'table' || this.dataList[i].form.type == 'multiSel' ||this.dataList[i].form.type == 'cascader' || this.dataList[i].form.type == 'file') {
                                      this.dataList[i].formValue = JSON.stringify(this.validateForm['key' + this.dataList[i].form.id]);
                                  } else if (this.dataList[i].form.type == 'dateTime') {
                                       if(this.validateForm['key' + this.dataList[i].form.id] !== null && this.validateForm['key' + this.dataList[i].form.id]  !== undefined && this.validateForm['key' + this.dataList[i].form.id]  !== ''){
                                            this.dataList[i].formValue = this.validateForm['key' + this.dataList[i].form.id].format("yyyy-MM-dd HH:mm:ss");
                                      }else{
                                          this.dataList[i].formValue = "";
                                      }
                                    
                                  }else if (this.dataList[i].form.type == 'image') {
                                       if( this.validateForm['key' + this.dataList[i].form.id].length > 0){
                                           for (var kk = 0; kk < this.validateForm['key' + this.dataList[i].form.id].length; kk++) {
                                              var arrObj = this.validateForm['key' + this.dataList[i].form.id][kk].url.split("//");
                                              var start = arrObj[1].indexOf("/");
                                              this.validateForm['key' + this.dataList[i].form.id][kk].url = arrObj[1].substring(start);
                                           }
                                       }
                                      this.dataList[i].formValue = JSON.stringify(this.validateForm['key' + this.dataList[i].form.id]);
                                  }  else {
                                      this.dataList[i].formValue = this.validateForm['key' + this.dataList[i].form.id];
                                  }
                              }

                              //组装数据
                              this.ruleForm.id = this.instanceId;
                              this.ruleForm.name = this.validateForm.name;
                              this.ruleForm.formValues = this.dataList;

                              debugger;
                              var submitUrl = "/cmdb/instance/updateFormValues";
                              jQuery.ajax({
                                  url: submitUrl,
                                  type: "POST",
                                  contentType: 'application/json;charset=utf-8', //设置请求头信息
                                  cache: false,
                                  async: false,
                                  traditional: true,
                                  data: JSON.stringify(this.ruleForm),
                                  dataType: "json",
                                  success: function (json, textStatus) {
                                      if (json.success == true) {
                                          this.$notify({
                                              title: '提示',
                                              message: '修改成功',
                                              type: 'success',
                                              duration: 3000
                                          });
                                          this.closeWin();

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
                                          message: '修改失败',
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
                  closeWin: function () {
                               this.$emit("childBindAdd");
                  },
                  closeDialog() {
                     this.$emit("childBindAdd");
                  },


                  initData() {
                      this.isInit = false;
                      var paramData = {
                          "instanceId": this.instanceId,
                           "moduleId": this.moduleId,
                      }
                      var sysCodeUrl = '/cmdb/instance/getFormValues';
                      $.ajax({
                          url: sysCodeUrl,
                          type: "POST",
                          cache: false,
                          async: true,
                          dataType: "json",
                          data: paramData,
                      }).done(function (data) {
                          debugger;
                          this.isInit = true;
                          this.formRules = data.formRules;
                          this.rules = JSON.parse(data.rule);
                          this.validateForm = JSON.parse(data.validateForm);

                          for (var i = 0; i < data.dataList.length; i++) {
                           if (data.dataList[i].form.type == "cascader") {
                                   this.deleteEmptyProperty(data.dataList[i].casoptions[data.dataList[i].form.id]);
                              }
                          
                          if(data.dataList[i].formValue !== null && data.dataList[i].formValue !== undefined){
                              //数组
                              if (data.dataList[i].form.type == "multiSel" || data.dataList[i].form.type == "table" || data.dataList[i].form.type == "file") {

                                  this.validateForm['key' + data.dataList[i].form.id] = eval(data.dataList[i].formValue);
                              } else if (data.dataList[i].form.type == "dateTime") {
                                    if(data.dataList[i].formValue == '' || data.dataList[i].formValue == null || data.dataList[i].formValue == undefined){
                                          this.validateForm['key' + data.dataList[i].form.id] = null;
                                    }else{
                                    this.validateForm['key' + data.dataList[i].form.id] = new Date(Date.parse(data.dataList[i].formValue.replace(/-/g, "/")));
                                        
                                    }
                              } else if (data.dataList[i].form.type == "cascader") {
                               debugger;
                                   this.validateForm['key' + data.dataList[i].form.id] = eval(data.dataList[i].formValue);
                              } else if (data.dataList[i].form.type == "image") {
                                      this.validateForm['key' + data.dataList[i].form.id] = eval(data.dataList[i].formValue);
                                      if( this.validateForm['key' + data.dataList[i].form.id].length > 0){
                                           for (var kk = 0; kk < this.validateForm['key' + data.dataList[i].form.id].length; kk++) {
                                           this.validateForm['key' + data.dataList[i].form.id][kk].url = downloadUrl +this.validateForm['key' + data.dataList[i].form.id][kk].url;
                                           }
                                       }
                              } else {

                                  this.validateForm['key' + data.dataList[i].form.id] = data.dataList[i].formValue;
                              }
                            }
                          }

                          debugger;
                          var duplicateInstanceName = (rule, value, callback) => {
                              debugger;
                              if (value === '') {
                                  callback(new Error('请输入名称'));
                              } else {

                                  var goHeadUrl = "/cmdb/instance/checkInstancenName";
                                  $.ajax({
                                      url: goHeadUrl,
                                      type: "POST",
                                      data: {
                                          'name': value,
                                          'moduleId': this.moduleId
                                      },
                                      dataType: "json",
                                      async: false,
                                      success: function (json, textStatus) {
                                          debugger;
                                          if (json.success == true) {} else {
                                              if (value != this.instanceName) {
                                                  callback(new Error('名称已存在'));
                                              }
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
                          var cc = this.rules.name;
                          cc[0].validator = duplicateInstanceName;
                          this.validateForm.name = this.instanceName;



                          this.dataList = data.dataList;

                          console.log( this.validateForm);

                      }.bind(this));
                  },
                  
                    handleCurrentChange(row) {
			            console.log("当前行：" + JSON.stringify(row));
			            this.currentRow = row;
			        },                 
        handleCommand(val) {
        debugger;
            if (val == 'add_row') { //增加行
                let tmp = {
                    id: this.uuid(),
                    formId: this.currentRow.formId,
                }
                this.validateForm['key'+this.currentRow.formId].push(tmp);
            } else { //删除行
                console.log("当前删除行:" + this.currentRow.id);
                console.log(this.validateForm['key'+this.currentRow.formId]);
                for (var i = 0; i < this.validateForm['key'+this.currentRow.formId].length; i++) {
                    if (this.validateForm['key'+this.currentRow.formId][i].id == this.currentRow.id) {
                        this.validateForm['key'+this.currentRow.formId].splice(i, 1);
                        break;
                    }
                }
            }
        },
        //生成唯一的UUID
        uuid() {
            function S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            }
            return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
        },
       //删除对象中空对象，空属性
        deleteEmptyProperty(object) {
        debugger;
            for (var i in object) {
                var value = object[i];
                // sodino.com
                // console.log('typeof object[' + i + ']', (typeof value));
                if (typeof value === 'object') {
                    if (Array.isArray(value)) {
                        if (value.length == 0) {
                            delete object[i];
                            console.log('delete Array', i);
                            continue;
                        }
                    }
                    this.deleteEmptyProperty(value);
                    if (this.isEmpty(value)) {
                        console.log('isEmpty true', i, value);
                        delete object[i];
                        console.log('delete a empty object');
                    }
                } else {
                    if (value === '' || value === null || value === undefined) {
                        delete object[i];
                        console.log('delete ', i);
                    } else {
                        console.log('check ', i, value);
                    }
                }
            }
        },
       isEmpty(object) {
            for (var name in object) {
                return false;
            }
            return true;
        },  
        
    handleSuccess(response, file, fileList) {
         debugger;
     for(var i = 0; i < fileList.length; i++){
  
       if(response.success== true ){
              var formId = response.formId;
             if(file.uid == fileList[i].uid){
		       fileList[i].url = downloadUrl+response.url;
		       fileList[i].formId = formId;
		       
		       this.validateForm['key'+formId] = fileList;
		       }
       }else{
       var formId = response.formId;
               if(file.uid == fileList[i].uid) {
			      fileList.splice(i, 1);
			    }
			    this.validateForm['key'+formId] = fileList;
			    
       }
     }  
      console.log(this.validateForm);

        },
    handleRemove(file, fileList){
    debugger;
           var formId = file.formId;
           for(var i = 0; i < fileList.length; i++){
               if(file.uid == fileList[i].uid) {
			      fileList.splice(i, 1);
			    }
           }
           this.validateForm['key'+formId] = fileList;
            console.log(this.validateForm);
    },  
   closeBack() {
              this.$emit("childBindAdd");
    },

          }
  }
</script>
