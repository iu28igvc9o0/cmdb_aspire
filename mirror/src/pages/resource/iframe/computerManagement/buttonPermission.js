let myMixin = {
    data() {
        return {
            buttonPermission: {}, // 按钮的操作权限
        }
    },
    created() {
        this.initPermission()
    },
    methods: {
        // 初始化权限
        initPermission() {
            let permissions = sessionStorage.getItem('permissions')
            let buttonPermission = {
                create: false,
                update: false,
                delete: false,
                batchUpdate: false,
                import: false,
                export: false
            }
            if (permissions === '*') {
                buttonPermission = {
                    create: true,
                    update: true,
                    delete: true,
                    batchUpdate: true,
                    import: true,
                    export: true
                }
            } else {
                if (permissions && permissions.length > 0) {
                    permissions.split(',').forEach((item) => {
                        if (item.substr(0, 5) === 'cmdb:') {
                            buttonPermission[item.substr(5, item.length)] = true
                        }
                    })
                }
            }
            this.buttonPermission = buttonPermission

        },
    }
}
export default myMixin