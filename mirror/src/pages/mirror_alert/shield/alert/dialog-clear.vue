<template>
  <!-- 清除 -->
  <el-dialog
    class="yw-dialog"
    :visible.sync="dialogMsg.dialogVisible"
    width="446px"
    title="清除"
  >
    <section class="yw-dialog-main">
      <el-form class="yw-form" :model="formdata" :rules="rules" ref="clearform" label-width="70px">
        <el-form-item label="告警清除">
          <span> <i class="el-icon-info"></i>告警数据将移至历史告警看板 </span>
        </el-form-item>
        <el-form-item 
            label="清除原因" 
            prop="content">
          <el-input type="textarea" :rows="5" v-model="formdata.content"></el-input>
        </el-form-item>
      </el-form>
    </section>
    <section class="btn-wrap">
      <el-button type="primary" size="small" @click="submit('clearform')">确定</el-button>
      <el-button size="small" @click="cancel()">取消</el-button>
    </section>
  </el-dialog>
</template>

<script>
import CommonOption from 'src/utils/commonOption.js'
import rbAlertService from 'src/services/alert/rb-alert-services.factory.js'
export default {
  props: ['dialogMsg'],
  mixins: [CommonOption],
  components: {},
  data() {
    return {
      orderType: 1,
      rules: {
          content: [
              { required: true, message: '请输入清除原因', trigger: '' }
          ]
      },
      formdata: {
          content: ''
      }
    }
  },
  mounted() {},
  methods: {
    submit(formName) {
      this.$refs[formName].validate(valid => {
          if (valid) {
            this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
            let params = {
              content: this.formdata.content,
              alert_ids: this.dialogMsg.data
                ? this.dialogMsg.data
                    .map((item) => {
                      return item['alert_id']
                    })
                    .join()
                : '',
            }
            rbAlertService
              .remove(params)
              .then((data) => {
                if (data.status === 'success') {
                  this.$message.success('清除成功')
                  this.$emit('closeDialog', 'update')
                } else {
                  this.$message.error(data.message)
                }
              })
              .finally(() => {
                this.closeFullScreenLoading()
              })
          } else {
              return false
          }
      })
    },
    cancel() {
      this.$emit('closeDialog', '')
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
