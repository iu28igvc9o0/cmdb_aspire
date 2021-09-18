<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <div class="relative">
            <section>
                <el-row>
                    <el-button v-if="isEditNonactivated"
                               type="primary"
                               size="medium"
                               @click="editScene">编辑</el-button>
                    <el-button v-if="!isEditNonactivated"
                               type="primary"
                               size="medium"
                               @click="modifyScene">修改</el-button>
                    <el-button type="primary"
                               size="medium"
                               @click="addScene">新增</el-button>
                    <el-button v-if="!isEditNonactivated"
                               type="primary"
                               size="medium"
                               @click="delteScene">删除</el-button>
                </el-row>
            </section>
            <el-collapse class="mtop10"
                         v-model="activeNames"
                         v-for="(pipeline,i) in pipelineList"
                         :key="i">
                <el-collapse-item :name="i+1">
                    <template slot="title">
                        <span class="mleft10">
                            {{pipeline.group_name}}
                        </span>
                    </template>
                    <div class="sceneList-warpper mtop10 mleft10">
                        <div class="sceneList"
                             v-for="(scene,i1) in pipeline.scenes_list"
                             :key="i1">
                            <div class="image-wrapper"
                                 @dblclick.stop="runWork(scene)">
                                <el-image style="width: 80px; height: 80px"
                                          :src="`data:image/jpeg;base64,${scene.scenes_picture}`">
                                </el-image>
                                <div class="sceneDesc">
                                    <el-radio v-model="radio"
                                              :disabled="isEditNonactivated"
                                              :label="' ' + scene.scenes_name"
                                              @change="getSceneInfo(scene)"></el-radio>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-collapse-item>
            </el-collapse>
        </div>
        <!-- 执行作业 -->
        <run-work-dialog></run-work-dialog>
    </div>
</template>

<script>
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import runWorkDialog from '../task/run-work-dialog.vue'

    export default {
        name: 'AutoOperationWorkManageSceneManage',
        components: {
            runWorkDialog
        },
        data () {
            return {
                pageLoading: true,
                loading_text: '请稍候...',
                pipelineList: [], // 分组列表
                isEditNonactivated: true,
                // 单选框
                radio: '',
                sceneInfo: {} // 单选框选中时的场景信息
            }
        },
        computed: {
            activeNames () {
                return this.pipelineList.map((item, index) => {
                    return index + 1
                })
            }
        },
        // 生命周期 - 创建完成（访问当前this实例）
        created () {
            this.getPipelineList()
        },
        // 生命周期 - 挂载完成（访问DOM元素）
        mounted () {
        },
        methods: {
            // 请求场景列表数据
            getPipelineList () {
                this.pipelineList = []
                this.pageLoading = true
                rbAutoOperationWorkServicesFactory
                    .pipelineScenesAllList()
                    .then(res => {
                        this.pipelineList = res
                        this.pageLoading = false
                        // console.log(this.pipelineList);
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.$message({
                            message: error,
                            type: 'warning'
                        })
                    })
            },
            // 单选框事件
            editScene () {
                this.isEditNonactivated = !this.isEditNonactivated
                this.radio = ''
            },
            getSceneInfo (scene) {
                this.sceneInfo = scene
            },
            // 场景的新增，删除，编辑事件
            addScene () {
                this.$router.push('/auto_operation/work_manage/add_scene')
            },
            modifyScene () {
                if (Object.keys(this.sceneInfo).length === 0) {
                    this.$message({
                        message: '请先选中要修改的场景！',
                        type: 'warning'
                    })
                    return
                }
                let sceneId = this.sceneInfo.pipeline_scenes_id
                this.$router.push({
                    path: '/auto_operation/work_manage/edit_scene',
                    query: {
                        pipelineSceneId: sceneId
                    }
                })
            },
            delteScene () {
                let req = { scenes_ids: this.sceneInfo.pipeline_scenes_id }
                if (Object.keys(this.sceneInfo).length === 0) {
                    this.$message({
                        message: '请先选中要删除的场景！',
                        type: 'warning'
                    })
                    return
                }
                this.$confirm('确认删除？').then(() => {
                    rbAutoOperationWorkServicesFactory
                        .deletePipelineScenes(req)
                        .then(res => {
                            if (res.flag) {
                                this.$message({
                                    message: '删除成功!',
                                    type: 'success'
                                })
                                this.getPipelineList()
                                this.radio = ''
                                this.sceneInfo = {}
                            } else {
                                this.$message({
                                    message: '删除失败！',
                                    type: 'error'
                                })
                            }
                        })
                        .catch(error => {
                            this.$message(error)
                        })
                })
            },
            // 执行作业
            runWork (scene) {
                // console.log(scene);
                // console.log(scene.scenes_picture);
                let pipelineId = scene.pipeline_id
                this.$confirm('确认执行作业吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                })
                    .then(() => {
                        // this.$router.push({
                        //   path: "/auto_operation/code_manage/run",
                        //   query: {
                        //     scriptId: pipelineId
                        //   }
                        // });
                        this.$bus.emit('showRunWork', pipelineId)
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消执行'
                        })
                    })
            }
        }
    }
</script>
<style lang="scss" scoped>
    .btn-wrap {
      position: absolute;
      top: 40px;
      right: 80px;
    }
    .el-tag {
      color: black !important;
      border: 1px solid black !important;
    }
    .sceneList-warpper {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;

      .sceneList {
        display: flex;
        align-items: center;
        min-width: 124px;
        margin-right: 10px;
        margin-bottom: 10px;
        .el-radio {
          margin-right: 0;
        }
        .image-wrapper {
          width: 160px;
          overflow: hidden;
          border: 1px solid $color-border;
          border-radius: 4px;
          padding: 10px;
          margin-right: 10px;
          float: left;
          text-align: center;
          /deep/ .sceneDesc .el-radio__label {
            white-space: initial;
            font-size: 12px;
          }
        }
      }
    }
</style>