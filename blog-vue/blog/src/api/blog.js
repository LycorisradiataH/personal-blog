import request from '@/request'

export function getBlogInfo () {
  return request({
    url: '/api',
    method: 'get'
  })
}

export function about () {
  return request({
    url: '/api/about',
    mthod: 'get'
  })
}
