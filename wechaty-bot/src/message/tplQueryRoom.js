const logger = require('../logger')
const util = require('util')
const keyword = "群信息"

module.exports = {
  exec: async (bot, msg) => {
      
      let room = await msg.room();
      logger.info({
        id: msg.id,
        action: '群信息',
        result: 'OK',
        message: room
      })
      return util.format(
        "当前群信息:\n群id: %s\n群名称: %s\n管理员id: %s\n成员数：%s", 
        room.id,
        room.payload.topic,
        room.payload.adminIdList,
        room.payload.memberIdList.length
      )
  },
  keyword
}