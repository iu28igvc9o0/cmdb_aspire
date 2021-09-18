
import personDataService from 'src/services/sys/person-services.js'
export default {
    data() {
        return {
        }
    },
    computed: {
    },
    mounted() {
    },
    methods: {
        // 懒加载部门子节点
        loadHandler (node, resolve) {
            let params = {
                parent_id: node.data.uuid
            }
            personDataService.getDepartmentTree(params).then((res) => {
                return resolve(res)
            })
        }
    }
}
