import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
import CommonOption from 'src/utils/commonOption.js'
export default {
    mixins: [CommonOption],
    // 导出(大数据量类导出)
    data() {
        return {
            isExportShow: false,
        }
    },
    methods: {
        // 设置导出弹窗是否显示
        setExportDisplay(val) {
            this.isExportShow = val
        },
        // 导出功能
        exportInstance() {
            let resdata = this.$utils.deepClone(this.queryParams)
            resdata.pageSize = null
            resdata.currentPage = null
            let _t = this
            rbConfigDictServiceFactory.getDictsByType({ type: 'export_type' }).then((res) => {
                if (res[0] && res[0].value === 'ftp') {
                    _t.exportParams = resdata
                    _t.setExportDisplay(true)
                } else {
                    this.showFullScreenLoading({ text: '正在导出数据,由于数据量较大请耐心等待...' })
                    rbCmdbServiceFactory.exportInstanceListStream(resdata, _t.moduleTypeDefault).then((data) => {
                        let blob = new Blob([data], { type: 'application/msword' })
                        // 创建下载链接
                        let objectUrl = URL.createObjectURL(blob)
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = objectUrl
                        downLoadElement.download = '资源列表.xlsx'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(objectUrl)
                    }).catch((e) => {
                        _t.pageLoading = false
                        _t.$message.error('导出失败!' + '导出数量过大，请重新选择')
                    }).finally(() => {
                        _t.closeFullScreenLoading()
                    })
                }
            })
        },
    },


}
