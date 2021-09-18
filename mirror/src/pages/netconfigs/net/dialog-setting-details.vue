<template>
    <!-- 配置文件详情 -->
    <el-dialog class="yw-dialog"
               width="600px"
               title="配置信息"
               :visible.sync="dialogMsg.dialogVisible">
        <div class="yw-content space-pre">
            <ul class="infinite-list"
                v-infinite-scroll="load"
                infinite-scroll-distance="50"
                style="overflow:auto">
                <li v-for="(item, i) in result"
                    :key="i"
                    class="infinite-list-item">{{ item }}</li>
            </ul>
        </div>
        <!-- <div class="yw-content space-pre"
             v-html="result"></div> -->
    </el-dialog>

</template>
<script>
    export default {
        props: ['dialogMsg'],
        watch: {
            'dialogMsg.dialogVisible': {
                handler(val) {
                    if (val) {
                        this.query()
                    }

                },
                deep: true
            }
        },
        data() {
            return {
                // dialogMsg: {
                //   id: '',//每个弹窗数据的唯一标识
                //   dialogVisible: false,
                // }
                result: [],
                storeResult: []
            }
        },
        methods: {
            query() {
                if (!this.dialogMsg.data.massage) {
                    return
                }
                this.result = this.dialogMsg.data.massage
                this.result = this.result.replace(/\n/g, '<br />').split('<br />')
                this.storeResult = this.$utils.deepClone(this.result)
                this.result.splice(50)
            },
            load() {
                if (this.result.length < this.storeResult.length) {
                    if (this.result.length + 50 < this.storeResult.length) {
                        this.result = this.storeResult.splice(0, this.result.length + 50)
                    } else {
                        this.result = this.storeResult
                    }
                }
            }

        }
    }

</script>
<style lang="scss" scoped>
    .yw-dialog {
      /deep/ .yw-form {
        max-height: 400px;
        overflow-y: auto;
        .el-form-item {
          display: block;
          border-bottom: 1px dashed #eee;
        }
        .el-form-item__label {
          font-weight: bold;
          min-width: 82px;
          text-align: left;
        }
        .el-form-item__content {
          width: 80%;
        }
      }

      .yw-content {
        min-height: 200px;
        max-height: 400px;
        overflow-y: auto;
      }
      .infinite-list {
        height: 300px;
        padding: 0;
        margin: 0;
        list-style: none;
      }
    }
</style>
