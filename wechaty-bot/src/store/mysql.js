const mysql = require('mysql')
const logger = require('../logger')
const config = require('../config')
const { promisify } = require("util");


const client = mysql.createConnection(config.mysql);
const query = promisify(client.query).bind(client)
const CURRENT_TIMESTAMP = { toSqlString: function() { return 'CURRENT_TIMESTAMP()' } }


if(config.mysql){
  client.connect(function(err) {
    if (err) {
      logger.error('error connecting: ' + err.stack);
      return;
    }
    logger.log('connected as id ' + client.threadId);
  });
}

const insert = ({
  message_id,
  to_id,
  from_id,
  origin_message_id,
  content,
  create_time,
  payload
}) => {
  let sql = mysql.format('insert into wechaty_message set ?' , {
    message_id,
    to_id,
    from_id,
    origin_message_id,
    content,
    create_time,
    payload
  })
  return query(sql)
}
const updateByMessageId = ( item, id )=> {
  let columns = [] , value = []
  for(let i in item) {
    columns.push(`set ${mysql.escapeId(i)} = ?`)
    value.push(item[i])
  }  
  return query(mysql.format(`update wechaty_message ${columns} where message_id = ? ` , [...value, id]))
}

const getMessageByMessageId = async ( id )=> {
  let records = await query(mysql.format(`select * from wechaty_message where message_id = ? ` , [id]))
  if(records && records.length) return records[0]
  else return
}
module.exports = {
  insert,
  updateByMessageId,
  getMessageByMessageId
}