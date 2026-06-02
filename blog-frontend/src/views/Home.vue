<template>
  <div class="home">
    <h2>最新文章</h2>
    <div v-if="loading" class="loading">正在加载文章...</div>
    <div v-else-if="!articles || articles.length === 0" class="empty">暂无文章</div>
    <div v-else class="articles-list">
      <div class="article-card" v-for="article in articles" :key="article.id">
        <h3>{{ article.title }}</h3>
        <p class="meta">
          作者：{{ article.author || '匿名' }} | 发布时间：{{ formatDate(article.createTime) }}
        </p>
        <p class="summary">{{ (article.content || '').substring(0, 100) + '...' }}</p>
        <router-link :to="`/article/${article.id}`" class="read-more">阅读全文</router-link>
        <!-- 只有已登录且是当前作者的文章才显示删除按钮 -->
        <button
          v-if="authState.token && authState.userInfo && article.userId === authState.userInfo.id"
          @click="handleDelete(article.id)"
          class="delete-btn"
          :disabled="deletingId === article.id"
        >
          {{ deletingId === article.id ? '删除中...' : '删除' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { formatDate } from '../utils/date'
import { ref, onMounted } from 'vue'
import { getArticles, deleteArticle } from '../api/article'
import { useAuth } from '../utils/auth'

const articles = ref([])
const loading = ref(false)
const deletingId = ref(null)
const authState = useAuth()

const loadArticles = async () => {
  loading.value = true
  try {
    const data = await getArticles()
    articles.value = Array.isArray(data) ? data : []
  } catch (error) {
    console.error('加载文章列表失败：', error)
    articles.value = []
  } finally {
    loading.value = false
  }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这篇文章吗？')) return
  deletingId.value = id
  try {
    await deleteArticle(id)
    alert('删除成功')
    loadArticles()
  } catch (error) {
    console.error('删除失败：', error)
  } finally {
    deletingId.value = null
  }
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.loading, .empty {
  text-align: center;
  padding: 2rem;
  color: #666;
}
.articles-list {
  margin-top: 1rem;
}
.article-card {
  background: white;
  padding: 1.5rem;
  margin-bottom: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.article-card h3 {
  margin-bottom: 0.5rem;
  color: #333;
}
.meta {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 1rem;
}
.summary {
  color: #444;
  margin-bottom: 1rem;
}
.read-more {
  color: #42b983;
  text-decoration: none;
  margin-right: 1rem;
}
.delete-btn {
  background: #ff4444;
  color: white;
  border: none;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
}
.delete-btn:disabled {
  background: #ffb3b3;
  cursor: not-allowed;
}
</style>
