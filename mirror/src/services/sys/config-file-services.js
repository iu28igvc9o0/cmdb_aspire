/*
* @author huangzhijie
* @date   19.2.25
* @description 系统管理-功能角色请求接口
*/
import rbHttp from 'assets/js/utility/rb-http.factory'

export default class configFileServices {
    // 上传配置文件
    static async uploadConfigFile (idcType, file, uploadInfo) {
        return rbHttp.sendRequest({
            method: 'POST',
            params: {
                'idcType': idcType,
                'file': file,
                'uploadInfo': uploadInfo
            },
            url: '/v1/config/uploadConfigFile'
        }).then(function (data) {
            return data
        })
    }
}
