import request from '@/request'

export function login (user) {
  return request({
    url: '/api/login',
    method: 'post',
    params: user
  })
}

export function sendCode (username) {
  return request({
    url: '/api/user/code',
    method: 'get',
    params: {
      username
    }
  })
}

export function register (user) {
  return request({
    url: '/api/user/register',
    method: 'post',
    data: user
  })
}

export function forget (user) {
  return request({
    url: '/api/user/forget',
    method: 'put',
    data: {
      username: user.username,
      password: user.password,
      code: user.code
    }
  })
}

export function logout () {
  return request({
    url: '/api/logout',
    method: 'get'
  })
}
