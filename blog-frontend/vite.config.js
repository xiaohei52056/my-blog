import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  base: '/my-blog/', // 必须添加，对应服务器的子路径
  server: {
    port: 5173
  }
})
