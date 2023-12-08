const express = require('express')
const app = express()
const port = 3000
const routes = require('./src/routes')

app.use(express.json())
app.use(routes)

app.listen(port, () => {
  console.log(`Server is running at port ${port}`)
})
