<template>
  <div class="article-detail">
    <div v-if="loading" class="loading">正在加载文章...</div>
    <div v-else-if="!article.id" class="empty">文章不存在或已被删除</div>
    <template v-else>
      <h1>{{ article.title }}</h1>
      <p class="meta">
        作者：{{ article.author || '匿名' }} | 发布时间：{{ formatDate(article.createTime) }}
      </p>
      <div class="content">{{ article.content }}</div>
      <router-link to="/" class="back-btn">返回首页</router-link>
    </template>
  </div>
</template>

<script setup>
import { formatDate } from '../utils/date'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleById } from '../api/article'

const route = useRoute()
const article = ref({})
const loading = ref(false)

const loadArticle = async () => {
  const id = route.params.id
  if (!id) {
    console.error('无效的文章 id')
    return
  }
  loading.value = true
  try {
    const data = await getArticleById(id)
    article.value = data || {}
  } catch (error) {
    console.error('加载文章详情失败：', error)
    article.value = {}
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.article-detail h1 {
  margin-bottom: 1rem;
  color: #333;
}
.meta {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}
.content {
  line-height: 1.8;
  color: #444;
  margin: 2rem 0;
  white-space: pre-wrap;
}
.back-btn {
  display: inline-block;
  background: #42b983;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  text-decoration: none;
}
.loading, .empty {
  text-align: center;
  padding: 2rem;
  color: #666;
}
</style>
