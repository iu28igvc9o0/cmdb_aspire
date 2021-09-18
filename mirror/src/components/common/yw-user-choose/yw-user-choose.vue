<template>
    <!-- 用户选择框(多于5个，显示...) -->
    <section>
        <div class="user-choose-wrap"
             :class="{invalid:invalid}">
            <el-tag :key="tagIndex"
                    v-for="(tag,tagIndex) in userList.slice(0,5)"
                    style="margin-right:5px;"
                    closable
                    :disable-transitions="false"
                    @close="deleteUser(tagIndex)">
                {{tag.twUserName}}
            </el-tag>
            <span v-if="userList && userList.length>5">...</span>
            <i class="img el-icon-s-custom"
               @click="showDialog({data:userList,title:'作战人员'})"></i>

        </div>
        <div v-if="invalid"
             class="el-form-item__error">
            作战人员不能为空
        </div>
        <!-- dialog -->
        <DialogUserChoose v-if="dialogMsg.dialogVisible"
                          @closeDialog="closeDialog"
                          :dialogMsg="dialogMsg"></DialogUserChoose>
    </section>

</template>

<script>
    export default {
        props: ['userList', 'invalid', 'disabled'],
        components: {
            DialogUserChoose: () => import('src/components/common/yw-user-choose/dialog-user-choose.vue'),
        },
        data() {
            return {
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    // status: '',// add、edit
                    title: '',
                    data: {} // 数据
                },
            }
        },

        methods: {
            // 删除用户
            deleteUser(tagIndex) {
                if (this.disabled) {
                    return
                }
                this.$emit('deleteUser', tagIndex)
            },
            showDialog(obj = {}) {
                if (this.disabled) {
                    return
                }
                this.dialogMsg.title = obj.title
                this.dialogMsg.data = obj.data
                this.dialogMsg.dialogVisible = true
            },
            closeDialog(data) {
                this.dialogMsg.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作

                }

            },
        },

    }
</script>
<style lang="scss" scoped>
    .user-choose-wrap {
        position: relative;
        border: 1px solid #dcdfe6;
        border-radius: 6px;
        padding: 5px 30px 5px 5px;
        margin-top: 5px;
        height: 46px;
        overflow: hidden;
        &.invalid {
            border-color: #f56c6c;
        }
        .img {
            cursor: pointer;
            position: absolute;
            right: 5px;
            top: 10px;
            font-size: 22px;
        }
    }
</style>
