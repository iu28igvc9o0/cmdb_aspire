<template>   
<div class="container">     
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules"  label-width="120px" class="demo-ruleForm" style="white-space:nowrap;">
        <el-form-item label="视图名称"    prop="name" required>
            <el-input v-model="ruleForm.name" placeholder="请输入视图名" size="small"></el-input> 

        </el-form-item>
        
      <el-form-item label="过滤条件"    prop="moduleIds" required>
	              <el-select  multiple placeholder="过滤条件" v-model="ruleForm.moduleIds">
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
        
        <el-form-item align="center" style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >{{ruleForm.submitCon}}</el-button>
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
    props: ['circleId'],
    data() {
        var validateName = (rule, value, callback) => {

            if (value === '') {
                callback(new Error('请输入名称'));
            } else {

                var goHeadUrl = "/cmdb/circle/checkViewName";
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

        return {
            selectData: [],
            ruleForm: {
                name: '',
                moduleIds: [],
                submitCon: '立即创建'
            },
            rules: {
                name: [{
                    validator: validateName,
                    trigger: 'blur'
                }, {
                    min: 2,max:40,
                    message: '长度大于2小于40',
                    trigger: 'blur'
                }],
                moduleIds: [{
                    type: 'array',
                    required: true,
                    message: '请选择过滤条件',
                    trigger: 'blur'
                }]
            },

        };
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
                                "name": this.ruleForm.name,
                                "moduleIds": this.ruleForm.moduleIds,
                                "circleId": this.circleId,
                                "defaultView": "false",
                            }
                            debugger;
                            var submitUrl = "/cmdb/circle/addMaintainView";
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

                initData() {
                    var sysCodeUrl = '/cmdb/circle/getModuleTree';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: true,
                        dataType: "json",
                        data: "",
                    }).done(function (data) {
                        debugger;
                        this.selectData = data;
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
                        this.$notify({
                            title: '提示',
                            message: '已取消退出',
                            type: 'info',
                            duration: 3000
                        });
                    });
                },
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                    this.ruleForm.name = "";
                    this.ruleForm.code = ""
                    this.ruleForm.dec = "";
                },
        }
}
</script>
