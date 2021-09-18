<template>
    <!-- 模型配置字段设置 -->
    <div>
        <el-form-item class="filed-box"
                      v-for="(item, index) in codeByDetails"
                      :key="index">
            <el-form :model="codeByDetails[index]">
                <el-row class="box-header"
                        type="flex"
                        justify="space-between">
                    <el-col style="width: calc(100% - 20px)">
                        <el-form-item>
                            <el-input v-model="item.groupName"
                                      :disabled="!limit.close"
                                      placeholder="请输入区域名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <i style="color: #909399;line-height: 40px;margin-right: 10px;cursor:pointer;"
                       v-if="limit.close"
                       class="el-icon-close"
                       @click="handleAreaDelete(index)"></i>
                </el-row>
                <p style="color:red;font-size:12px;margin-left:10px;"
                   v-if="!(item.groupName.trim())">区域名称不能为空</p>
                <el-row class="box-body">
                    <draggable class="code-list"
                               style="min-height: 40px;"
                               v-model="item.codeList"
                               :options="dragOptions"
                               @add="onAdd">
                        <el-col class="code-box"
                                v-for="(code, codeIndex) in item.codeList"
                                :key="codeIndex"
                                :span="code.displayStyle ? Math.floor(24/Number(code.displayStyle)) : 6">
                            <el-col style="width: calc(100% - 30px)">
                                <el-form :model="code"
                                         label-width="80px"
                                         @click.native="showCodeInfo(code)">
                                    <el-form-item :label="code.filedName"
                                                  :title="code.filedName"
                                                  v-if="code.filedName">
                                        <el-select v-model="code.defaultValue"
                                                   v-if="code.controlType.controlCode === 'listSel'
                                                       || code.controlType.controlCode === 'cascader'"
                                                   @click.native="showCodeInfo(code)"
                                                   disabled
                                                   size="mini"
                                                   style="width:178px;"
                                                   placeholder="">
                                        </el-select>
                                        <el-date-picker v-else-if="code.controlType.controlCode === 'dateTime'"
                                                        style="width:178px;"
                                                        disabled></el-date-picker>
                                        <el-input v-else
                                                  style="width:178px;"
                                                  disabled></el-input>
                                        <i style="color: #909399;line-height: 40px;margin-left: 10px;cursor:pointer;"
                                           class="el-icon-close"
                                           v-if="limit.close && code.filedName"
                                           @click="handleDelete(code, index, codeIndex)"></i>
                                    </el-form-item>
                                </el-form>
                            </el-col>
                            <!-- <i style="color: #909399;line-height: 40px;float: right;margin-right: 10px;cursor:pointer;"
                               class="el-icon-close"
                               v-if="limit.close && code.filedName"
                               @click="handleDelete(code, index, codeIndex)"></i> -->
                        </el-col>
                    </draggable>
                </el-row>
            </el-form>
        </el-form-item>

        <!-- dialog 字段详细信息配置 -->
        <DialogFieldSet :dialogMsg="dialogFieldSet"
                        @closeDialog="closeDialog"
                        v-if="dialogFieldSet.dialogVisible"></DialogFieldSet>
        <!-- dialog -->
    </div>
</template>

<script>
    import draggable from 'vuedraggable'
    export default {
        props: ['codeByDetails', 'dragOptions', 'limit'],// 数据，配置项，权限
        components: {
            draggable,
            DialogFieldSet: () => import('./dialog-fieldSet.vue'),
        },
        data() {
            return {
                // dialog字段详细信息配置
                dialogFieldSet: {
                    dialogVisible: false,
                    id: '',// 预留：每个弹窗数据的唯一标识
                    data: {} // 预留：数据
                },
            }
        },
        computed: {

        },
        methods: {
            // 字段点击
            showCodeInfo() {
                // 打开字段配置弹窗
                // this.openDialogFieldSet();
            },
            // 打开字段配置弹窗
            openDialogFieldSet() {
                this.dialogFieldSet.dialogVisible = true
            },
            // 拖拽回调
            onAdd(val) {
                if (this.limit.emitEvents) {
                    this.$emit('emitEvents', 'onAdd', [val])
                }

            },
            // 删除区域
            handleAreaDelete(index) {
                if (this.limit.emitEvents) {
                    this.$emit('emitEvents', 'handleAreaDelete', [index])
                }

            },
            // 删除字段
            handleDelete(code, areaIndex, codeIndex) {
                if (this.limit.emitEvents) {
                    this.$emit('emitEvents', 'handleDelete', [code, areaIndex, codeIndex])
                }
            },
        },

    }
</script>
<style lang="scss" scoped>
    /deep/.filed-box {
        .el-row {
            margin-bottom: 0;
        }
        .box-header {
            background-color: #f5f7fa;
            border-radius: 4px 4px 0 0;
            .el-form-item {
                margin-bottom: 0;
            }
            .el-input__inner {
                font-size: 14px;
                font-weight: 500;
                border: none;
                background-color: #f5f7fa;
            }
        }
        .box-body {
            min-height: 40px;
            /*border: 1px solid #E4E7ED;*/
            .code-box {
                .el-col {
                    line-height: 30px;
                    border-radius: 0;
                    padding: 0 5px;
                }
                .el-form-item {
                    margin-bottom: 0;
                    .el-select {
                        width: 100%;
                    }
                    .el-date-editor.el-input,
                    .el-date-editor.el-input__inner {
                        width: 100%;
                    }
                    .el-input--prefix .el-input__inner {
                        padding-left: 0;
                    }
                    .is-disabled .el-input__inner {
                        cursor: default !important;
                    }
                    .el-input.is-disabled .el-input__icon {
                        cursor: default !important;
                    }
                    .el-form-item__label {
                        text-align: left;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        overflow: hidden;
                    }
                }
            }
        }
    }
</style>
