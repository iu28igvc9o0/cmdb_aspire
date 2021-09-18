<template>
<div class="container">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules"  label-width="150px" class="demo-ruleForm"  style="white-space:nowrap;padding-right:30%;padding-left:25%;">
        
      <el-form-item label="认领的配置"    prop="instanceData" required>
				    <el-tag
					  v-for="tag in ruleForm.instanceData"
					  :key="tag.id"
					  :closable="true"
					  :close-transition="false"
                      @close="handleClose($index)"
					  type="gray">
					{{tag.name}}
					</el-tag>
	  </el-form-item>
	  
	    <el-form-item label="选择目标圈子/仓库"    prop="circleId" required>
              <el-select v-model="ruleForm.circleId" placeholder="请选择">
			    <el-option
			      v-for="item in ruleForm.selectData"
			      :key="item.id"
			      :label="item.name"
			      :value="item.id">
			    </el-option>
			  </el-select>

        </el-form-item>
        
         <el-alert style="padding:2px 0px 2px 0px;left:20%;width: 100px;text-align:right;" align="center"
		    title="配置认领后，归属权为目标圈子所有"
		    type="info"
		    :closable="false"
		    show-icon>
		  </el-alert>
  
        <el-form-item align="left" style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >确定</el-button>
            <el-button @click="closeDialog">取消</el-button>
        </el-form-item>
    </el-form>
    </div>
</template>
<style>
</style>
<script>
export
default {
    events: {
        'onEditnew': function (viewId) {
            debugger;
            this.viewId = viewId;
        }
    },
    props: ['multipleSelection'],
    data() {
        var validateName = (rule, value, callback) => {

            if (value.length <= 0) {
                this.closeWin();
            } else {
                callback();
            }
        };

        return {
            ruleForm: {
                instanceData: [],
                selectData: [],
                circleId: '',
            },
            rules: {
                instanceData: [{
                    type: 'array',
                    required: true,
                    message: '请至少选择一个实例',
                    trigger: 'change'
                }],
                circleId: [{
                    required: true,
                    message: '请选择目标圈子/仓库',
                    trigger: 'change'
                }]
            },

        };
    },

    mounted: function () {
            debugger;
            this.ruleForm.instanceData = this.multipleSelection.concat();
            this.initData();
        },

        methods: {
            handleClose(index) {
                    debugger;
                    this.ruleForm.instanceData.splice(this.ruleForm.instanceData.indexOf(index), 1);
                },
                //提交表单
                onSubmit() {
                    debugger;
                    this.$refs["ruleForm"].validate((valid) => {
                        if (valid) {
                            var instanceIds = [];
                            for (var i = 0; i < this.ruleForm.instanceData.length; i++) {
                                instanceIds.push(this.ruleForm.instanceData[i].id);
                            }
                            var paramData = {
                                "instanceIds": instanceIds,
                                "circleId": this.ruleForm.circleId,
                            }
                            debugger;
                            var submitUrl = "/cmdb/instance/handUpInstance";
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
                                            message: '认领成功',
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
                                        message: '认领失败',
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

                initData() {
                    var sysCodeUrl = '/cmdb/circle/getAllCircles';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: "",
                    }).done(function (data) {
                        this.ruleForm.selectData = data.dataList;
                    }.bind(this));
                },

                closeWin: function () {
                    this.resetForm('ruleForm');
                    this.$emit("childBindAdd");
                },
                closeDialog() {
                    this.$confirm('是否确认退出？', '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.closeWin();
                    }).catch(() => {

                    });
                },
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                },

        }
}
</script>
