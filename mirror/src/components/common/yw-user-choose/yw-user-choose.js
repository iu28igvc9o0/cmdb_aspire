// 用户选择框常用方法
export default {
    data() {
        return {
            userList: [

            ]
        }
    },

    methods: {
        // 删除标签
        deleteUser(tagIndex) {
            this.userList.splice(tagIndex, 1)
        },
    },


}
