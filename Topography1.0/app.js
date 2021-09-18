const express = require('express')
// const rewrite = require('express-urlrewrite')

const app = express()

app.all("*",function(req,res,next){
  //设置允许跨域的域名，*代表允许任意域名跨域
  res.header("Access-Control-Allow-Origin","http://localhost:8080");
  //允许的header类型
  res.header("Access-Control-Allow-Headers","content-type");
  //跨域允许的请求方式
  res.header("Access-Control-Allow-Methods","DELETE,PUT,POST,GET,OPTIONS");
  if (req.method.toLowerCase() == 'options')
    res.send(200);  // 让options尝试请求快速结束
  else
    next();
})

// const fs = require('fs')
const path = require('path')

app.use(express.static(path.join(__dirname, './')))
app.get('/', function (req, res) {
  // res.sendFile('index.html')
  res.sendFile(path.join(__dirname, './', 'index.html'));
})

app.get('/viewer', function (req, res) {
  // res.sendFile('index.html')
  res.sendFile(path.join(__dirname, './', 'viewer.html'));
})

const port = process.env.PORT || 9876
module.exports = app.listen(port, () => {
  console.log(`Server listening on http://localhost:${port}, Ctrl+C to stop`)
})
