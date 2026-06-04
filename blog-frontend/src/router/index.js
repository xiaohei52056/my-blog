import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Article from '../views/Article.vue'
import Publish from '../views/Publish.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/article/:id', name: 'Article', component: Article },
  { path: '/publish', name: 'Publish', component: Publish },
  { path: '/users/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/users/register', name: 'Register', component: () => import('../views/Register.vue') }
]

const router = createRouter({
  history: createWebHistory('/my-blog/'),
  routes
})

export default router