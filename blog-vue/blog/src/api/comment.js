import request from '@/request'

export function addComment (comment) {
  return request({
    url: '/api/comment/insertComment',
    method: 'post',
    data: comment
  })
}

export function listReply (commentId, current, size) {
  return request({
    url: '/api/comment/listReply/' + commentId,
    method: 'get',
    params: {
      current: current,
      size: size
    }
  })
}

export function listComment (articleId, current) {
  return request({
    url: '/api/comment/' + articleId,
    method: 'get',
    params: {
      current: current
    }
  })
}

export function like (id) {
  return request({
    url: '/api/comment/like/' + id,
    method: 'post'
  })
}
