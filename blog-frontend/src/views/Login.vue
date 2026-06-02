<template>
  <div class="login">
    <div class="login-box">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="username" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="password" placeholder="请输入密码" required>
        </div>
        <button type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
        <p class="register-link">
          还没有账号？<router-link to="/users/register">注册</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'
import { loginWithToken } from '../utils/auth'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value.trim()) {
    alert('请输入用户名')
    return
  }
  if (!password.value) {
    alert('请输入密码')
    return
  }

  loading.value = true
  try {
    const token = await login({
      username: username.value.trim(),
      password: password.value
    })
    // 登录成功：保存 token，并同步拉取用户信息到全局状态，导航栏可立即响应
    await loginWithToken(token)
    alert('登录成功')
    router.push('/')
  } catch (error) {
    console.error('登录失败：', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}
.login-box h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}
.form-group {
  margin-bottom: 1rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}
.form-group input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}
button[type='submit'] {
  width: 100%;
  background: #42b983;
  color: white;
  border: none;
  padding: 0.8rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-bottom: 1rem;
}
button[type='submit']:disabled {
  background: #95d5b2;
  cursor: not-allowed;
}
.register-link {
  text-align: center;
}
.register-link a {
  color: #42b983;
  text-decoration: none;
}
</style>
