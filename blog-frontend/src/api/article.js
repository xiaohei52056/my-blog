import request from './request'

// 获取文章列表
export function getArticles() {
  return request.get('/articles')
}

// 根据 ID 获取文章详情
export function getArticleById(id) {
  return request.get(`/articles/${id}`)
}

// 发布文章（需登录
export function publishArticle(data) {
  return request.post('/articles', data)
}

// 编辑文章（需登录，且仅作者可调用
export function updateArticle(data) {
  return request.put('/articles', data)
}

// 删除文章（需登录，且仅作者可调用
export function deleteArticle(id) {
  return request.delete(`/articles/${id}`)
}
