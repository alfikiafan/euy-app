const express = require('express');
const app = express();
const port = 3000;
const api = require('./src/routes/api')
const apiUser = require('./src/routes/apiUser')

app.use('/api',api)
app.use('/api', apiUser)

app.listen(port, () => {
    console.log(`Server is running at port ${port}`)
})