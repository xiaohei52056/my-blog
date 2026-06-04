<template>
  <nav class="navbar">
    <div class="container">
      <h1 class="logo">我的博客</h1>
      <div class="nav-links">
        <router-link to="/my-blog/">首页</router-link>
        <router-link to="/my-blog/publish">发布文章</router-link>

        <!-- 未登录状态 -->
        <template v-if="!authState.token">
          <router-link to="/my-blog/users/login">登录/注册</router-link>
        </template>

        <!-- 已登录状态 -->
        <template v-else>
          <span class="user-info">欢迎，{{ authState.userInfo && authState.userInfo.nickname ? authState.userInfo.nickname : '用户' }}</span>
          <button @click="handleLogout" class="logout-btn">退出登录</button>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth, logout, syncUserInfo } from '../utils/auth'

const router = useRouter()
const authState = useAuth()

// 退出登录
const handleLogout = () => {
  logout()
  router.push('/my-blog/')
}

// 页面加载时，如果有 token 就同步一次用户信息（用于刷新页面后恢复显示）
onMounted(async () => {
  if (authState.token && !authState.userInfo) {
    await syncUserInfo()
  }
})
</script>

<style scoped>
.navbar {
  background: #333;
  color: white;
  padding: 1rem 0;
  margin-bottom: 2rem;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
}
.logo {
  margin: 0;
  font-size: 1.5rem;
}
.nav-links a {
  color: white;
  margin-left: 2rem;
  text-decoration: none;
}
.nav-links a:hover {
  color: #42b983;
}
.user-info {
  color: #42b983;
  margin-left: 2rem;
}
.logout-btn {
  background: transparent;
  color: white;
  border: 1px solid white;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 1rem;
}
.logout-btn:hover {
  background: white;
  color: #333;
}
</style>
