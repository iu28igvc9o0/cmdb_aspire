<!-- 导入平台信息 -->
<template>
    <div>
        <div class="config-wrap">
            <div class="config-wrap-top">
                <span>导入平台信息</span>
            </div>

            <div class="config-wrap-bottom">
                <el-form :inline="true">
                    <el-form-item>
                        <el-input clearable
                                  placeholder="请导入zip压缩文件"></el-input>
                        <el-upload class="upload-demo"
                                   name="multipartFile"
                                   :headers="headers"
                                   :action="actionUrl"
                                   :on-success="handleSuccess"
                                   accept=".zip"
                                   :limit="1">
                            <el-button>导入</el-button>
                        </el-upload>
                    </el-form-item>
                </el-form>

                <p>zip压缩文件包含平台LOGO、平台名称、版权信息</p>
            </div>
        </div>
    </div>
</template>

<script>
    // import req from "../request.js";
    // import rbHttp from 'assets/js/utility/rb-http.factory'

    export default {
        data() {
            return {
                actionUrl: '',
                headers: {
                    accept: 'application/json, text/plain, */*'
                }
            }
        },
        created() {
            // this.getList()
        },
        mounted() {
            this.actionUrl = '/sysVersion/v1/importSysVersion'
        },
        methods: {
            handleSuccess(response) {
                if (response.true === '导入平台信息成功！') {
                    this.$message({
                        showClose: true,
                        message: '上传成功',
                        type: 'success'
                    })
                }
            },

        }
    }
</script>
<style lang="scss" scoped>
    .config-wrap {
        width: 400px;
        height: 150px;
        border: 1px solid #c0c4cc;
        border-radius: 10px;
        margin: 20px auto;

        .config-wrap-top {
            padding: 10px;
            border-bottom: 1px solid #c0c4cc;
        }

        .config-wrap-bottom {
            height: 60%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            p {
                font-size: 10px;
            }

            /deep/ .el-form-item {
                margin-bottom: 0;
            }

            /deep/ .el-input__inner {
                width: 250px;
            }

            /deep/ .el-upload {
                display: block;
            }

            /deep/ .el-button {
                position: absolute;
                height: 22px;
                top: 10px;
                right: 4px;
                border: 0;
                border-radius: 0;
                border-left: 1px solid #c0c4cc;

                &:hover {
                    background-color: #fff;
                }

                &:active {
                    background-color: #fff;
                }
            }
        }
    }
</style>
