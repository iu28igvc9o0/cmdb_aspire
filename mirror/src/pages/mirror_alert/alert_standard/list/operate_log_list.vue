<template>
    <!-- 操作日志显示拟态框 -->
    <el-dialog class="yw-dialog"
              title="操作日志列表"
              :visible.sync="logDialog.visiable"
              width="1100px"
              @close='close'
              destory-on-close = 'true'>
      <div class='yw-el-table-wrap'>
        <el-table
          class='yw-el-table'
          border
          :data='operateLogData'
          style='cursor: pointer;'
          stripe
          tooltip-effect='dark'
          height='calc(100vh - 350px)'
        >
          <el-table-column prop='operater' label='操作人'></el-table-column>
          <el-table-column prop='operateTypeDesc' label='操作类型'></el-table-column>
          <el-table-column prop='operateTime' label='操作时间' :formatter="timeFormatter" :show-overflow-tooltip='true'></el-table-column>
          <el-table-column prop='operateContent' label='操作内容' :show-overflow-tooltip='true'></el-table-column>
        </el-table>
      </div>
      <div class='yw-page-wrap'>
        <el-pagination
          @size-change='handleSizeChange'
          @current-change='handleCurrentChange'
          :current-page='currentPage'
          :page-sizes='pageSizes'
          :page-size='pageSize'
          :total='total'
          layout='total, sizes, prev, pager, next, jumper'
        ></el-pagination>
      </div>
    </el-dialog>
</template>

<script>
import alertOperatorLogService from '../../../../services/alert/rb-alert-isolate-service.factory'
import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
export default {
  name: 'MirrorASOperatorLogList',
  mixins: [rbAutoOperationMixin],
  props: ['logDialog'],
  data() {
    return {
      operateLogData: []
    }
  },
  mounted() {
    this.search(1)
  },
  watch: {
  },
  methods: {
    timeFormatter(timestamp){
      var date = new Date(timestamp.operateTime)
      var Y = date.getFullYear()
      var M = date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1
      var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      var s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      return Y+ '-' + M + '-' + D + ' ' + h + ':' + m + ':' + s
    },
    close(){
      // this.$emit('setOperateDialogClose', false)
    },
    search(pageNum){
      this.currentPage = pageNum ? pageNum : this.currentPage
      let req = {
        relationId: this.logDialog.id,
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        operateModel: 'alert_standard'
      }
      alertOperatorLogService.getOperateLogList(req).then((res) => {
        this.operateLogData = res.result
        this.total = res.count
      }).finally(() => {
      })
    }
  }
}
</script>

<style lang='scss' scoped>
</style>
