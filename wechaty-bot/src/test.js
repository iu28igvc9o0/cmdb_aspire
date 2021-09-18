
const mysqlStore = require('./store/mysql')

const msg = {
  payload: { "id": "1796143659748697337", "timestamp": 1594523377, "type": 7, "fromId": "wxid_arpozf6kqdpd21", "mentionIdList": ["wxid_35f4wy7zsd1w22"], "roomId": "22808783294@chatroom", "text": "@大德  【公告】2", "toId": "wxid_35f4wy7zsd1w22" }
}

const main = async () => {
  // console.log(await mysqlStore.insert({
  //   message_id: msg.payload.id,
  //   from_id: msg.payload.fromId,
    
  //   room_id: msg.payload.roomId,
  //   content: msg.payload.text,
  //   create_time: msg.payload.timestamp,
  // }))
  // console.log(await mysqlStore.updateByMessageId({
  //   status: 1
  // },'1796143659748697337'))
  let data = await mysqlStore.getMessageByMessageId('3161826998638071033');
  console.log(data[0].payload)
  console.log(1)
}
main()