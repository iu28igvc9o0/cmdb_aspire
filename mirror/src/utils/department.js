// 部门列表数据
export default {
    data() {
        return {
            // 数据

        }
    },
    methods: {
        // 获得uuid
        getUUid(data) {
            return this.rbHttp.sendRequest({
                method: 'POST',
                data: data,
                url: '/v1/department/queryList'
            }).then((res) => {
                return res[0].uuid
            })
        },

        // 获得部门
        getDepartment(data) {
            return this.rbHttp.sendRequest({
                method: 'POST',
                data: data,
                url: '/v1/department/queryList?'
            }).then((res) => {
                return res
            })
        },

        // 获得部门列表
        // async getDepartmentList(name = '') {
        //     let uuid = await this.getUUid({ name: name })
        //     return await this.getDepartment({ 'parent_id': uuid })
        // },
        async getDepartmentList() {
            let params = {
                'condicationCode': 'cond_department_list',
                'token': '5245ed1b-6345-11e',
                'pageSize': 99999,
                'currentPage': 1,
                'department_type': '1a658da2d85a497fb775cb695c51b35b'
            }
            return this.rbHttp.sendRequest({
                method: 'POST',
                data: params,
                url: '/v1/cmdb/restful/common/instance/list'
            }).then((res) => {
                if (res && res.data) {
                    return res.data
                } else {
                    return []
                }
            })
        }

    },


}
