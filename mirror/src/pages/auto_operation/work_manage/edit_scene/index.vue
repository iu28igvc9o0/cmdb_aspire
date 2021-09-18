<template>
    <div class="components-container yw-dashboard add-scene-box"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="yw-form is-required"
                 ref="addForm"
                 :model="addForm"
                 :rules="addFormRules"
                 label-width="100px"
                 :inline="false"
                 :show-message="false">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="场景名称"
                                  prop="scenesName">
                        <el-input v-model="addForm.scenesName"
                                  clearable></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="场景分组"
                                  prop="groupIdList">
                        <el-tag :key="tag.group_id"
                                style="margin-right: 5px;"
                                v-for="tag in groupTagids"
                                closable
                                :disable-transitions="false"
                                @close="handleClose(tag)"
                                size="small">
                            {{tag.group_name}}
                        </el-tag>
                        <el-popover placement="bottom-start"
                                    trigger="click">
                            <comtree :ref="treeName"
                                     :data="groupTreeData"
                                     :props="gruopTreeDefault"
                                     :exId="treeName"
                                     :ex-control="true"
                                     ex-show-search
                                     @node-click="handleTreeClick">
                            </comtree>
                            <el-button slot="reference"
                                       class="mod-btn"
                                       size="small">请选择
                            </el-button>
                        </el-popover>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <span class="must">*</span>
                <el-form-item label="选择图标"
                              prop="scenes_picture">
                    <!-- <el-form-item label="选择图标"> -->
                    <el-upload class="avatar-uploader"
                               :show-file-list="false"
                               action="/"
                               name="picture"
                               :multiple="false"
                               :before-upload="avatarUpload">
                        <!--<img v-if="addForm.scenes_picture" :src="`data:image/jpeg;base64,${addForm.scenes_picture}`"-->
                        <!--class="avatar">-->
                        <img v-if="imageUrl"
                             :src="imageUrl"
                             class="avatar">
                        <i v-else
                           class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="关联作业"
                              prop="pipelineId">
                    <el-select v-model="addForm.pipelineId"
                               placeholder="请选择"
                               filterable
                               clearable>
                        <el-option v-for="val in workList"
                                   :key="val.pipeline_id"
                                   :label="val.pipeline_name"
                                   :value="val.pipeline_id"></el-option>
                    </el-select>
                </el-form-item>
            </el-row>
            <section>
                <div class="fixed-bottom-box t-center">
                    <el-button @click="saveScene">保存</el-button>
                    <el-button type="primary"
                               @click="cancelSave">取消</el-button>
                </div>
            </section>
        </el-form>
    </div>
</template>

<script>
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import comtree from '../../components/tree.vue'

    export default {
        name: 'AutoOperationWorkManageAddScene',
        components: {
            comtree
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                sceneInfo: {},
                workList: [], // 作业列表
                groupTreeData: [], // 分组列表
                groupTagids: [], // tag 列表
                groupIdList: [], // 分组id列表
                // 分组树
                treeName: 'gtree',
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                // 表单数据
                addForm: {
                    pipelineScenesId: '',
                    scenesName: '',
                    groupIdList: '',
                    pipelineId: '',
                    scenes_picture: ''
                },
                addFormRules: {
                    pipelineId: [{ required: true, message: '请选择关联作业！' }],
                    scenesName: [{ required: true, message: '请填写场景名称！' }],
                    groupIdList: [{ required: true, message: '请选择场景分组！' }]
                },
                // 图片上传组件
                imageUrl: '',
                pictureData: null, // 传给接口的图片数据
                disabled: false,
                uploadDisabled: false
            }
        },
        // 生命周期 - 创建完成（访问当前this实例）
        created() {
            this.getWorkList()
            this.getGroupTree()
            // console.log("进入编辑场景");
        },
        // 生命周期 - 挂载完成（访问DOM元素）
        mounted() {
            this.initPageInfo()
        },
        methods: {
            // 初始化页面详情
            initPageInfo() {
                let query = this.$route.query
                if (query.pipelineSceneId) {
                    this.addForm.pipelineScenesId = query.pipelineSceneId
                    let req = { pipeline_scenes_id: query.pipelineSceneId }
                    this.getScenesById(req)
                }
            },
            // 获取场景详情信息
            getScenesById(req) {
                rbAutoOperationWorkServicesFactory
                    .pipelineScenesById(req)
                    .then(res => {
                        this.loading = false
                        this.sceneInfo = res
                        this.addForm.scenesName = res.scenes_name
                        this.addForm.groupIdList = res.group_id_list
                        this.addForm.pipelineId = res.pipeline_id
                        if (res.scenes_picture) {
                            this.imageUrl = `data:image/jpeg;base64,${res.scenes_picture}`
                        }
                        if (res.group_relation_list) {
                            this.groupTagids = res.group_relation_list
                        }
                        // console.log("初始化时获取到的场景数据");
                        // console.log(this.addForm);
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 获取作业列表信息
            getWorkList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList()
                    .then(res => {
                        this.loading = false
                        this.workList = res.dataList
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 获取场景分组树
            getGroupTree() {
                let params = {}
                rbAutoOperationWorkServicesFactory
                    .querGroupTree(params)
                    .then(res => {
                        this.loading = false
                        this.groupTreeData = res
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 图片上传框的处理事件
            avatarUpload(file) {
                this.addForm.scenes_picture = ''
                // console.log("图片上传框的处理事件");
                this.pictureData = file
                this.imageUrl = window.URL.createObjectURL(file)
            },
            // 树状图的点击事件
            handleTreeClick(data) {
                // console.log("树状图的点击事件");
                if (this.groupTagids.length > 0) {
                    this.$message({
                        message: '只能选择一个分组',
                        type: 'warning'
                    })
                    return
                }
                if (_.map(this.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.groupTagids.push({
                        group_name: data.group_name,
                        group_id: data.group_id
                    })
                }
                this.treeVisible = false
            },
            handleClose(tag) {
                this.groupTagids.splice(this.groupTagids.indexOf(tag), 1)
            },
            // 保存场景信息
            saveScene() {
                let groupId = this.groupTagids.map(item => {
                    return item.group_id
                })
                this.addForm.groupIdList = groupId[0]
                this.addForm.scenes_picture = null
                if (!this.imageUrl) {
                    this.$message({
                        message: '请先完善信息',
                        type: 'warning'
                    })
                    return
                }
                this.$refs.addForm.validate(valid => {
                    if (!valid) {
                        this.$message({
                            message: '请先完善信息',
                            type: 'warning'
                        })
                        return
                    }
                    rbAutoOperationWorkServicesFactory
                        .savePipelineScenes(this.addForm, this.pictureData)
                        .then(res => {
                            if (res.flag) {
                                this.$message({
                                    message: '保存成功',
                                    type: 'success'
                                })
                                // this.$refs["addForm"].resetFields()
                                this.$router.push('/auto_operation/work_manage/scene_manage')
                            } else {
                                this.$message.error(res.error_tip)
                            }
                            this.pageLoading = false
                        })
                        .catch(() => {
                            this.pageLoading = false
                            this.$message.error('提交失败！')
                        })
                })
            },
            cancelSave() {
                this.$router.push('/auto_operation/work_manage/scene_manage')
            }
        }
    }
</script>
<style lang="scss" scoped>
    .add-scene-box {
        width: 60%;
        margin: 0 auto;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100px;
        height: 100px;
        line-height: 100px;
        text-align: center;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
    }

    .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }

    .disabled .el-upload--picture-card {
        display: none !important;
    }
    .must {
        display: inline-block;
        position: absolute;
        color: #f56c6c !important;
        margin-right: 4px !important;
        top: 10px;
    }
</style>