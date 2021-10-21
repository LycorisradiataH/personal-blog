import request from '@/request'

export function updateUserInfo (user) {
  return request({
    url: '/api/user/updateUserInfo',
    method: 'put',
    data: user
  })
}

export function updateEmail (user) {
  return request({
    url: '/api/user/updateEmail',
    method: 'put',
    data: user
  })
}
