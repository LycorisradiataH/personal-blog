import request from '@/request'

export function listArticle (current) {
  return request({
    url: '/api/home',
    method: 'get',
    params: {
      current
    }
  })
}

export function listArchive (current) {
  return request({
    url: '/api/article/archive',
    method: 'get',
    params: {
      current
    }
  })
}

export function listArticleByQueryParam (categoryId, tagId, current) {
  return request({
    url: '/api/article/queryParam',
    method: 'get',
    params: {
      categoryId: categoryId,
      tagId: tagId,
      current: current
    }
  })
}

export function getArticleById (path) {
  return request({
    url: '/api' + path,
    method: 'get'
  })
}

export function newArticle () {
  return request({
    url: '/api/article/new',
    method: 'get'
  })
}

export function likeArticle (id) {
  return request({
    url: '/api/article/like/' + id,
    method: 'post'
  })
}

export function searchArticle (keywords) {
  return request({
    url: '/api/article/search',
    method: 'get',
    params: {
      keywords
    }
  })
}
