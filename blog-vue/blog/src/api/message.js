import request from '@/request'

export function listMessage () {
  return request({
    url: '/api/message',
    method: 'get'
  })
}

export function addMessage (message) {
  return request({
    url: '/api/message',
    method: 'post',
    data: message
  })
}
