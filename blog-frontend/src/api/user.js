import request from './request'

// 用户注册
export function register(data) {
  return request.post('/users/register', data)
}

// 用户登录
export function login(data) {
  return request.post('/users/login', data)
}

// 获取当前登录用户信息（需登录）
export function getCurrentUser() {
  return request.get('/users/current')
}
