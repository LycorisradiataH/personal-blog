import request from '@/request'

export function listCategory () {
  return request({
    url: '/api/category',
    method: 'get'
  })
}
