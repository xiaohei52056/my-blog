// 路由守卫：
import router from './index'

// 需要登录才能访问发布页面
const authPages = ['/publish']
// 已登录用户不应该访问的页面（登录、注册页）
const guestPages = ['/users/login', '/users/register']

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 访问需登录但未登录
  if (authPages.includes(to.path) && !token) {
    alert('请先登录')
    next('/users/login')
    return
  }

  // 已登录用户尝试访问登录/注册页，直接跳转到首页
  if (guestPages.includes(to.path) && token) {
    next('/')
    return
  }

  next()
})
