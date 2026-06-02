<template>
  <div class="register">
    <div class="register-box">
      <h2>注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="username" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="password" placeholder="至少 6 位" required>
        </div>
        <button type="submit" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
        <p class="login-link">
          已有账号？<router-link to="/users/login">登录</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)

const handleRegister = async () => {
  if (!username.value.trim()) {
    alert('请输入用户名')
    return
  }
  if (!password.value || password.value.length < 6) {
    alert('密码至少 6 位')
    return
  }

  loading.value = true
  try {
    await register({
      username: username.value.trim(),
      password: password.value
    })
    alert('注册成功，请登录')
    router.push('/users/login')
  } catch (error) {
    console.error('注册失败：', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
.register-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}
.register-box h2 {
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
.login-link {
  text-align: center;
}
.login-link a {
  color: #42b983;
  text-decoration: none;
}
</style>
