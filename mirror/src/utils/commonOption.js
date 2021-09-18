// 常用组件：提示、加载条、组件重置、导出等封装
export default {
    data() {
        return {
            // 全屏loading
            fullScreenLoading: false,
            // 局部loading(需要在页面v-if)
            loading: false,
            // 是否重置组件
            resetComponent: true,
            // cmdb字段转换
            cmdbColumnsConvert: {},

        }
    },
    computed: {
        // 按钮权限列表
        btnAuthority() {
            let obj = {
                // 角色
                permissions: '',
                // 权限
                btn: {
                    // create: false
                    // delete:false
                    // update:false
                    // save:false
                    // import:false
                    // export:false
                }
            }
            let permissions = sessionStorage.getItem('permissions')
            // permissions = 'cmdb:create'
            obj.permissions = permissions
            if (permissions && permissions !== '*') {
                permissions.split(',').forEach((item) => {
                    this.$set(obj.btn, item.substring(item.indexOf(':') ? item.indexOf(':') + 1 : 0), true)
                })
            }
            return obj
        }
    },
    methods: {
        // 打开全屏加载条
        showFullScreenLoading(obj) {
            this.fullScreenLoading = this.$loading({
                lock: true,
                text: obj && obj.text ? obj.text : '正在查询数据, 请稍等...',
                spinner: 'el-icon-loading'
            })
        },
        // 关闭全屏加载条
        closeFullScreenLoading() {
            this.fullScreenLoading.close()
        },
        // 打开局部加载条
        showLoading() {
            this.loading = true
        },
        // 关闭局部加载条
        closeLoading() {
            this.loading = false
        },

        // 更新组件
        updateComponent() {
            this.resetComponent = false
            this.$nextTick(() => {
                this.resetComponent = true
            })
        },
        // 获得cmdb动态配置项字段
        convertCmdb(item, attr) {
            let value = ''
            if (this.cmdbColumnsConvert[attr]) {
                value = this.cmdbColumnsConvert[attr]['type'] === 'ref' ? item[this.cmdbColumnsConvert[attr]['ref_name']] : item[attr]
            }

            return value
        },

        /** 文件导出
        * obj:{
        * data:数据源
        * fileType:文件类型,
        * fileName:文件名称
        * }
        */
        exportFiles(obj) {
            if (!obj) {
                return 0
            }
            let blob = new Blob([obj.data], {
                type: obj.fileType
            })
            let objectUrl = window.URL.createObjectURL(blob)
            // window.location.href = objectUrl
            let downLoadElement = document.createElement('a')
            downLoadElement.href = objectUrl
            downLoadElement.download = obj.fileName
            document.body.appendChild(downLoadElement)

            downLoadElement.click()
            document.body.removeChild(downLoadElement)
            URL.revokeObjectURL(objectUrl)
        },
        // 文件直接下载
        exportFilesByLink(obj = {}) {
            if (!obj.path) {
                return false
            }
            let downLoadElement = document.createElement('a')
            downLoadElement.href = obj.path
            downLoadElement.setAttribute('download', obj.fileName)
            document.body.appendChild(downLoadElement)
            downLoadElement.click()
            document.body.removeChild(downLoadElement)
        },


    },


}
