<template>
<div class="container">

    <el-form label-position="top"  class="demo-ruleForm" label-width="100px" style="padding-right:30px;">

	
	    <el-form-item align="center" label="请选择需要导出的配置类型"  style="width:100%; margin-left:auto;margin-right:auto;">
	              <el-select  placeholder="配置类型" v-model="moduleId">
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
            <el-button type="primary" @click="onSubmit" >导出</el-button>
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
        return {
            moduleId:'',
            selectData:[],
            isShowItem:false,
            fileList:[],
        };
    },

    mounted: function () {
            this.initData();
        },

        methods: {
                //提交表单
                onSubmit() {
                            debugger;
                            var submitUrl = "/cmdb/repertory/downloadData/"+this.moduleId;
                            window.open(submitUrl);
                            
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
                },
  
      uploadChange(file) {
      debugger;
        console.log(file);
      },

        }
}
</script>
