import request from '@/request'

export function about () {
  return request({
    url: '/api/about',
    mthod: 'get'
  })
}
