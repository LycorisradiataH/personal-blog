import request from '@/request'

export function listTag () {
  return request({
    url: '/api/tag',
    method: 'get'
  })
}
