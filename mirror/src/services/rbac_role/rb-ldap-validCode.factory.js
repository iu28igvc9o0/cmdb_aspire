import rbHttp from 'src/assets/js/utility/rb-http.factory.js'
const ENDPOINT_ROLE = '/v1/ldap'
export default class rbLdapValidCodeFactory {
    static getValidCode () {
        return rbHttp.sendRequest({
            method: 'GET',
            url: `${ENDPOINT_ROLE}/validCode`,
            cache: false
        }).catch(() => [])
    }
}
