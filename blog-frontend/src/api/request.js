// axios 基础配置与全局拦截器
// 1. 统一为需要登录的请求添加 Authorization 头
// 2. 统一解析后端返回的 Result 结构 { code, message, data }
// 3. 401 时调用 markTokenInvalid 同步状态，不弹"登录失效"提示
import axios from 'axios'
import { markTokenInvalid } from '../utils/auth'

const service = axios.create({
  baseURL: 'http://8.148.6.228:8080/api',
  timeout: 10000
})

// 请求拦截器：添加 JWT 令牌
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理 Result 结构与错误
service.interceptors.response.use(
  response => {
    const result = response.data
    // 后端统一返回 { code, message, data }
    if (result && typeof result === 'object' && 'code' in result) {
      if (result.code === 200) {
        // 请求成功，返回业务数据 data
        return result.data
      }
      // 业务错误：code != 200
      console.error('接口业务错误：', result.message)
      alert(result.message || '操作失败')
      return Promise.reject(new Error(result.message || '操作失败'))
    }
    // 兼容旧的非 Result 返回
    return result
  },
  error => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        // 401：token 过期或未登录。由 auth.js 统一处理状态，不打扰用户弹提示
        markTokenInvalid()
      } else if (status === 403) {
        alert('没有权限执行此操作')
      } else if (status === 500) {
        const msg = error.response.data && error.response.data.message
        alert(msg || '服务器内部错误，请稍后再试')
      } else {
        alert('请求失败：' + status)
      }
    } else if (error.code === 'ECONNABORTED' || (error.message && error.message.includes('timeout'))) {
      alert('请求超时，请检查网络或后端服务')
    } else {
      console.error('请求错误：', error)
      alert('请求失败，请检查后端是否启动')
    }
    return Promise.reject(error)
  }
)

export default service
