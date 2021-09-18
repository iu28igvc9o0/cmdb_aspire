import rbHttp from 'assets/js/utility/rb-http.factory'

const ProjectEndPoint = '/v1/cmdb/maintenance'
const ProduceEndPoint = '/v1/cmdb/maintenProduce'
const SoftwareEndPoint = '/v1/cmdb/maintensoft'
const SoftwareRecordEndPoint = '/v1/cmdb/maintenance/software/record'

export default class maintenanceService {
    // project
    // 查询维保项目
    static async listMaintenanceProject(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProjectEndPoint + '/project/list'
        }).then(function (data) {
            return data
        })
    }

    // produce
    // 分页查询软件维保数据
    static async listProduceByPage(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProduceEndPoint + '/listProduceByPage'
        }).then(function (data) {
            return data
        })
    }
    static async deleteProduceById(param) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            url: ProduceEndPoint + '/deleteProduce',
            params: param
        }).then((data) => {
            return data
        })
    }
    // 导出维保厂商
    static async exportProdceList(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: ProduceEndPoint + '/export'
        }).then((data) => {
            return data
        })
    }

    // software
    // 分页查询软件维保数据
    static async listMaintenanceByPage(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: SoftwareEndPoint + '/listMaintenSoftwareByPage'
        }).then(function (data) {
            return data
        })
    }

    // 插入新软件维保
    static async insertMaintenance(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: SoftwareEndPoint + '/insertMaintenSoftware'
        }).then(function (data) {
            return data
        })
    }

    // 根据id删除软件维保
    static async deleteMaintenanceById(data) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            params: data,
            url: SoftwareEndPoint + '/deleteMaintenSoftware'
        }).then(function (data) {
            return data
        })
    }

    // 查询软件维保根据软件名称
    static async selectMaintenSoftwareBySoftNmae(data) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: data,
            url: SoftwareEndPoint + '/selectMaintenSoftwareBySoftNmae'
        }).then(function (data) {
            return data
        })
    }
    // 导出软件维保数据
    static async exportTemp(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: SoftwareEndPoint + '/downloadTemplate'
        }).then(function (data) {
            return data
        })
    }

    // 导出软件维保
    static async downloadMaintenSoftware(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            binary: true,
            url: SoftwareEndPoint + '/downloadMaintenSoftware'
        }).then(function (data) {
            return data
        })
    }
    // softwareRecord
    // 分页查询软件维保数据
    static async listSoftwareRecordByPage(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: SoftwareRecordEndPoint + '/list'
        }).then(function (data) {
            return data
        })
    }
    // 新增或修改软件使用记录
    static async saveAndUpdateSoftwareRecord(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: SoftwareRecordEndPoint + '/saveAndUpdate'
        }).then(function (data) {
            return data
        })
    }

    // 删除软件维保使用
    static async deleteSoftwareRecord(data) {
        return rbHttp.sendRequest({
            method: 'DELETE',
            data: data,
            url: SoftwareRecordEndPoint + '/delete'
        }).then(function (data) {
            return data
        })
    }

    // 下载软件维保使用模板
    static async downloadSoftwareRecordTemplate() {
        return rbHttp.sendRequest({
            method: 'GET',
            binary: true,
            url: SoftwareRecordEndPoint + '/downloadTemplate'
        }).then(function (data) {
            return data
        })
    }
    // 导出软件维保使用数据
    static async exportSoftwareRecordData() {
        return rbHttp.sendRequest({
            method: 'POST',
            binary: true,
            url: SoftwareRecordEndPoint + '/exportData'
        }).then(function (data) {
            return data
        })
    }
    /**
   * 维保详情弹框
   */
    // 保存维保部件对象
    static async saveComponent(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProjectEndPoint + '/component/save'
        }).then(function (data) {
            return data
        })
    }
    // 修改维保部件对象
    static async updateComponent(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProjectEndPoint + '/component/update'
        }).then(function (data) {
            return data
        })
    }
    // 查询维保部件对象列表
    static async queryComponentsList(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProjectEndPoint + '/component/list'
        }).then(function (data) {
            return data
        })
    }
    // 删除维保部件对象
    static async deleteComponent(data) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: data,
            url: ProjectEndPoint + '/component/delete'
        }).then(function (data) {
            return data
        })
    }
    // 导出维保部件对象列表
    static async exportComponent(req) {
        return rbHttp.sendRequest({
            binary: true,
            method: 'POST',
            data: req,
            url: ProjectEndPoint + '/component/export'
        }).then(function (data) {
            return data
        })
    }
    // 查询单位列表
    static async getDictsByType(req) {
        return rbHttp.sendRequest({
            method: 'GET',
            params: req,
            url: '/v1/cmdb/configDict/getDictsByType',
        }).then(function (data) {
            return data
        })
    }
    /**
   * 维保存量对比统计
   */
    // 第一层查询
    static async getInventoryFirst() {
        return rbHttp.sendRequest({
            method: 'POST',
            url: '/v1/cmdb/maintenance/inventory/first',
        }).then(function (data) {
            return data
        })
    }
    // 第一层导出
    static async exportInventoryFirst() {
        return rbHttp.sendRequest({
            binary: true,
            method: 'POST',
            url: '/v1/cmdb/maintenance/inventory/export/first',
        }).then(function (data) {
            return data
        })
    }
    // 第二层查询
    static async getInventorySecond(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/cmdb/maintenance/inventory/second',
        }).then(function (data) {
            return data
        })
    }
    // 第二层导出
    static async exportInventorySecond(req) {
        return rbHttp.sendRequest({
            binary: true,
            method: 'POST',
            data: req,
            url: '/v1/cmdb/maintenance/inventory/export/second',
        }).then(function (data) {
            return data
        })
    }
    // 第三层查询
    static async getInventoryThird(req) {
        return rbHttp.sendRequest({
            method: 'POST',
            data: req,
            url: '/v1/cmdb/maintenance/inventory/third',
        }).then(function (data) {
            return data
        })
    }
    // 第三层导出
    static async exportInventoryThird(req) {
        return rbHttp.sendRequest({
            binary: true,
            method: 'POST',
            data: req,
            url: '/v1/cmdb/maintenance/inventory/export/third',
        }).then(function (data) {
            return data
        })
    }
}
