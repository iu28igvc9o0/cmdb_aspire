let permissions = sessionStorage.getItem('permissions')
let buttonPermission = {
    ip_check_operator_stop: false,// 暂不处理
    ip_check_operator_continue: false,// 继续处理
    ip_register_update: false,// IP登记
    ip_register_create: false// IP注册
}
if (permissions === '*') {
    buttonPermission = {
        ip_check_operator_stop: true,// 暂不处理
        ip_check_operator_continue: true,// 继续处理
        ip_register_update: true,// IP登记
        ip_register_create: true// IP注册
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
console.log(buttonPermission)
export default buttonPermission