export default class CookieService {
    static getCookie (name) {
        const nameEQ = name + '='
        const ca = document.cookie.split(';')
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i]
            while (c.charAt(0) === ' ') {
                c = c.substring(1, c.length)
            }
            if (c.indexOf(nameEQ) === 0) {
                return decodeURIComponent(c.substring(nameEQ.length, c.length))
            }
        }
        return ''
    }

    static setCookie (k, v) {
        document.cookie = `${k}=${decodeURIComponent(v)}; path=/`
    }
}
