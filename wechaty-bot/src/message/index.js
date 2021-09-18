const tplQuery = require("./tplQuery")
const tplResolve = require("./tplResolve")
const tplNotice = require("./tplNotice")
const tplQueryRoom = require("./tplQueryRoom")
const mysqlStore =  require('../store/mysql')
const logger = require('../logger')
const tplMap = {
  [tplQuery.keyword]: tplQuery.exec,
  [tplResolve.keyword]: tplResolve.exec,
  [tplNotice.keyword]: tplNotice.exec,
  [tplQueryRoom.keyword]: tplQueryRoom.exec,
}
/*
{
  '咨询' : function(),
  '回复' : function,
  '群消息' : 

}
*/
const exec = async (bot,msg) => {
  //  console.log(msg)
  let self = await msg.to()
  self = "@" + self.name()
  // 获取消息内容，拿到整个消息文本，去掉 @+名字
  let sendText = msg.text().replace(self, "").trim()
  let keyword = /【(.+)】/mi.exec(sendText);
  if(keyword && keyword.length == 2) {
    if(tplMap[keyword[1]]) {
      logger.info({
        id: msg.id,
        action: keyword[1],
        message:msg.payload
      })
      mysqlStore.insert({
        message_id: msg.payload.id,
        to_id: msg.payload.toId || 0,
        from_id: msg.payload.fromId,
        room_id: msg.payload.roomId,
        content: msg.payload.text,
        create_time: msg.payload.timestamp,
        payload: JSON.stringify(msg.payload)
      })
      return tplMap[keyword[1]](bot,msg)
    } 
  }
  return
}
module.exports = {
 exec
}