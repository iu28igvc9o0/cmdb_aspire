import rbHttp from 'assets/js/utility/rb-http.factory'

const maintenStatistAnalysisEndPoint = '/v1/cmdb/mainten/statistic'
const dataDictEndPoint = '/v1/cmdb/configDict'
const maintenanceStatist = '/v1/cmdb/maintenance/statist'

export default class maintenStatistAnalysisService {

    // 维保周期分析
    static async maintenPeriodAnalysis(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/periodAnalysis',
        }).then(function (data) {
            return data
        })
    }

    // 周期导出
    static async maintenPeriodExport (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/export/period',
            binary: true
        }).then(function (data) {
            return data
        })
    }


    // 第一层导出
    static async firstLayerExport (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/export/first',
            binary: true
        }).then(function (data) {
            return data
        })
    }

    // 第二层导出
    static async secondLayerExport (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/export/second',
            binary: true
        }).then(function (data) {
            return data
        })
    }

    // 第三层导出
    static async thirdLayerExport (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/export/third',
            binary: true
        }).then(function (data) {
            return data
        })
    }

    // 第四层导出
    static async fourthLayerExport (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/export/fourth',
            binary: true
        }).then(function (data) {
            return data
        })
    }

    // 第一层统计查询接口
    static async firstLayerAPI (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/first'
        }).then(function (data) {
            return data
        })
    }

    // 第二层统计查询接口
    static async secondLayerAPI (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/second'
        }).then(function (data) {
            return data
        })
    }

    // 第三层统计查询接口
    static async thirdLayerAPI (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/third'
        }).then(function (data) {
            return data
        })
    }

    // 第四层统计查询接口
    static async fourthLayerAPI (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/fourth'
        }).then(function (data) {
            return data
        })
    }

    //  维保统计中的 按照类型获取字典数据
    static async getDictDataByType (param) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: param,
            url: dataDictEndPoint + '/getDictsByType'
        }).then(function (data) {
            return data
        })
    }

    // 维保状态信息统计
    static async maintenStatusStatist (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/statusInfo'
        }).then(function (data) {
            return data
        })
    }

    // 导出维保状态信息统计
    static async exportMaintenStatusStatist (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: maintenStatistAnalysisEndPoint + '/exportStatist'
        }).then(function (data) {
            return data
        })
    }

    // 维保状态信息统计 -> 第二层的维保项目列表
    static async getMaintenObjList (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenStatistAnalysisEndPoint + '/maintenList'
        }).then(function (data) {
            return data
        })
    }

    // "导出" 维保状态信息统计 -> 第二层的维保项目列表
    static async exportMaintenObjList (data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: maintenStatistAnalysisEndPoint + '/exportMaintenList'
        }).then(function (data) {
            return data
        })
    }

    // 按时间维度统计设备增量 接口
    static async statistDeviceIncreByTime(data){
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: maintenanceStatist + '/time'
        }).then(function (data) {
            return data
        })
    }
}