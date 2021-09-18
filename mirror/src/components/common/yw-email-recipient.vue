<template>
    <!-- 邮箱收件人 -->
    <section>
        <div class="yw-email-recipient">
            <el-tag :key="tagIndex"
                    class="email-recipient-tag"
                    v-for="(tag,tagIndex) in dynamicTags"
                    closable
                    :type="tag.type"
                    :disable-transitions="false"
                    @close="handleClose(tag)">
                {{tag.showContent}}
            </el-tag>
            <el-input class="input-new-tag"
                      placeholder=""
                      v-model="inputValue"
                      ref="saveTagInput"
                      size="small"
                      @keyup.enter.native="handleInputConfirm"
                      @blur="handleInputConfirm">
            </el-input>
        </div>
        <div class="btn-wrap">
            <el-button type="text"
                       @click="handleCloseAll">清空收件人</el-button>
        </div>
    </section>

</template>

<script>
    import { isEmptyOrPhone } from 'src/utils/validate.js'
    export default {
        props: ['tagDatas'],
        components: {

        },
        data() {
            return {
                // 标签数据
                dynamicTags: [],
                // 输入框
                inputValue: ''
            }
        },


        methods: {
            // 删除所有标签
            handleCloseAll() {
                this.dynamicTags = []
                this.changeEmailRecipient()
            },
            // 删除标签
            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
                this.changeEmailRecipient()
            },
            // 新增标签
            handleInputConfirm() {
                let mobiles = this.dynamicTags.map((item) => {
                    return item.mobile
                })
                let type = ''
                isEmptyOrPhone('', this.inputValue, function (val) {
                    if (val) {
                        type = 'danger'
                    } else {
                        type = ''
                    }
                })

                let inputValue = `${this.inputValue}<${this.inputValue}>`
                if (this.inputValue && !mobiles.includes(this.inputValue)) {
                    this.dynamicTags.push({ showContent: inputValue, mobile: this.inputValue, type: type })
                }
                this.inputValue = ''
                this.changeEmailRecipient()
            },
            changeEmailRecipient() {
                this.$emit('changeEmailRecipient', this.dynamicTags)
            }
        },
        created() {
            if (this.tagDatas && this.tagDatas.length > 0) {
                this.dynamicTags = this.tagDatas
            }
        },

    }
</script>
<style lang="scss" scoped>
    .yw-email-recipient {
        max-height: 200px;
        overflow: auto;
        border: 1px solid #dcdfe6;

        .email-recipient-tag {
            &.el-tag {
                height: 26px;
                line-height: 22px;
                border-radius: 20px;
                margin: 0 0 5px 10px;
            }
        }
        /deep/.input-new-tag {
            width: 100px;
            &.el-input {
                .el-input__inner {
                    border: none;
                    padding: 0 10px;
                }
            }
        }
    }
</style>
