<template>
  <div class="publish">
    <h2>发布新文章</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>文章标题</label>
        <input type="text" v-model="title" placeholder="请输入标题（不超过 200 字）" maxlength="200" required>
      </div>
      <div class="form-group">
        <label>文章内容</label>
        <textarea v-model="content" rows="12" placeholder="请输入正文" required></textarea>
      </div>
      <button type="submit" class="submit-btn" :disabled="loading">
        {{ loading ? '发布中...' : '发布文章' }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { publishArticle } from '../api/article'
import { useAuth } from '../utils/auth'

const router = useRouter()
const loading = ref(false)
const authState = useAuth()

const title = ref('')
const content = ref('')

const submitForm = async () => {
  if (!title.value.trim()) {
    alert('标题不能为空')
    return
  }
  if (title.value.trim().length > 200) {
    alert('标题不能超过 200 字')
    return
  }
  if (!content.value.trim()) {
    alert('内容不能为空')
    return
  }

  // 未登录或用户信息获取失败，跳登录
  if (!authState.token || !authState.userInfo || !authState.userInfo.id) {
    alert('请先登录')
    router.push('/users/login')
    return
  }

  loading.value = true
  try {
    await publishArticle({
      title: title.value.trim(),
      content: content.value.trim(),
      author: authState.userInfo.nickname || authState.userInfo.username,
      userId: authState.userInfo.id
    })
    alert('发布成功')
    router.push('/')
  } catch (error) {
    console.error('发布失败：', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.publish {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: 0 auto;
}
.form-group {
  margin-bottom: 1.5rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}
.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  box-sizing: border-box;
  font-family: inherit;
}
.submit-btn {
  background: #42b983;
  color: white;
  border: none;
  padding: 0.8rem 2rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}
.submit-btn:disabled {
  background: #95d5b2;
  cursor: not-allowed;
}
</style>
