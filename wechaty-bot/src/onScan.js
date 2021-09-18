/*
 * @Author: isboyjc
 * @Date: 2020-02-18 16:26:41
 * @LastEditors: isboyjc
 * @LastEditTime: 2020-02-18 16:26:42
 * @Description: 机器人需要扫描二维码时监听
 */
const Qrterminal = require("qrcode-terminal")

module.exports = function onScan(qrcode, status) {

    if (status === 2) {
        // 二维码在log中无法扫描，隐藏掉
        // Qrterminal.generate(qrcode, { small: true })
        // console.log('qrcode===', { qrcode }, 'status===', status);

        // show qrcode on console
        const qrcodeImageUrl = ['https://api.qrserver.com/v1/create-qr-code/?data=', encodeURIComponent(qrcode)].join('')

        console.log(
            `
            =======================================================================================================
            登录二维码：
            ${qrcodeImageUrl}
            =======================================================================================================
            复制以上链接到浏览器打开，使用机器人微信号扫描登录`)

    } else {
        console.log('二维码已过期，status===5，', status);
    }

}