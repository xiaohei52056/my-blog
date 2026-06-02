// 轻量级登录状态管理：单一真相源（Single Source of Truth）
// 组件直接通过 useAuth() 读取 authState，登录/退出时同步更新全局响应式状态
// 不引入 Pinia，保持与项目原有风格一致
import { reactive, watch } from 'vue'
import { getCurrentUser as apiGetCurrentUser } from '../api/user'

const authState = reactive({
  token: localStorage.getItem('token') || '',
  userInfo: null,
  // 手动退出标志：为 true 时，request.js 的 401 拦截器不再弹出"登录失效"提示
  manualLogout: false
})

// 监听 token 变化，同步写入 localStorage
watch(
  () => authState.token,
  newVal => {
    if (newVal) {
      localStorage.setItem('token', newVal)
    } else {
      localStorage.removeItem('token')
    }
  }
)

// 通知其他组件（多标签页场景等）登录态已变化
function notifyAuthChange() {
  window.dispatchEvent(new CustomEvent('auth-change'))
}

// 登录成功：保存 token，并向后端拉取用户信息
export async function loginWithToken(token) {
  authState.token = token || ''
  authState.manualLogout = false
  try {
    if (authState.token) {
      const user = await apiGetCurrentUser()
      authState.userInfo = user
    } else {
      authState.userInfo = null
    }
  } catch (e) {
    // 获取用户信息失败也不影响登录态，token 仍有效；组件可再次触发
    console.error('获取登录用户信息失败', e)
    authState.userInfo = null
  }
  notifyAuthChange()
  return authState.userInfo
}

// 主动退出登录：清空本地态，不弹任何提示
export function logout() {
  authState.manualLogout = true
  authState.token = ''
  authState.userInfo = null
  notifyAuthChange()
}

// token 被动失效（401）：由 request.js 调用，同样不弹重复提示
export function markTokenInvalid() {
  if (authState.manualLogout) return
  authState.token = ''
  authState.userInfo = null
  notifyAuthChange()
}

// 判断是否已登录
export function isLoggedIn() {
  return !!authState.token
}

// 获取当前用户信息（响应式）
export function useAuth() {
  return authState
}

// 页面首次加载时同步一次用户信息（用于刷新后恢复状态）
export async function syncUserInfo() {
  if (!authState.token) {
    authState.userInfo = null
    return null
  }
  try {
    const user = await apiGetCurrentUser()
    authState.userInfo = user
    return user
  } catch (e) {
    authState.userInfo = null
    return null
  }
}
