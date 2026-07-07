import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const email = ref(localStorage.getItem('email') || null)

  const isLoggedIn = computed(() => !!token.value)

  async function login(emailVal, password) {
    const { data } = await api.post('/api/auth/login', { email: emailVal, password })
    token.value = data.token
    email.value = data.email
    localStorage.setItem('token', data.token)
    localStorage.setItem('email', data.email)
  }

  async function register(emailVal, password) {
    const { data } = await api.post('/api/auth/register', { email: emailVal, password })
    token.value = data.token
    email.value = data.email
    localStorage.setItem('token', data.token)
    localStorage.setItem('email', data.email)
  }

  function logout() {
    token.value = null
    email.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('email')
  }

  return { token, email, isLoggedIn, login, register, logout }
})
