const request = require('./request.js');

function login(username, password) {
  return request({
    url: '/login',
    method: 'POST',
    data: {
      username,
      password,
      uuid: ''
    }
  });
}

function getInfo() {
  return request({
    url: '/getInfo',
    method: 'GET'
  });
}

function logout() {
  return request({
    url: '/logout',
    method: 'POST'
  });
}

module.exports = {
  login,
  getInfo,
  logout
};