<template>
  <div class="page">
    <nav>
      <span class="logo">corta<span class="dot">.</span></span>
      <div class="nav-right">
        <template v-if="auth.isLoggedIn">
          <span class="nav-email">{{ auth.email }}</span>
          <button class="btn-ghost" @click="auth.logout()">salir</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn-ghost">entrar</router-link>
          <router-link to="/register" class="btn-primary">registrarse</router-link>
        </template>
      </div>
    </nav>

    <main>
      <div class="hero">
        <h1>URLs más cortas,<br>al instante.</h1>
        <p class="subtitle">Pega tu enlace y obtén uno limpio y compartible.</p>
      </div>

      <div class="input-card">
        <div class="input-row">
          <input
            v-model="url"
            type="url"
            placeholder="https://tu-url-muy-larga.com/aqui"
            @keydown.enter="shorten"
            :disabled="loading"
            autofocus
          />
          <button class="btn-shorten" @click="shorten" :disabled="loading || !url">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Acortar</span>
          </button>
        </div>

        <div v-if="error" class="error-msg">{{ error }}</div>

        <transition name="slide">
          <div v-if="result" class="result">
            <div class="result-url">
              <span class="result-label">Tu enlace</span>
              <a :href="result.shortUrl" target="_blank" class="result-link">
                {{ result.shortUrl }}
              </a>
            </div>
            <div class="result-actions">
              <button class="btn-copy" @click="copy" :class="{ copied }">
                {{ copied ? '✓ copiado' : 'copiar' }}
              </button>
            </div>
            <div class="result-meta">
              <span>→ {{ result.originalUrl }}</span>
              <span>expira {{ formatDate(result.expiresAt) }}</span>
            </div>
          </div>
        </transition>
      </div>

      <div v-if="!auth.isLoggedIn" class="guest-note">
        <router-link to="/register">Crea una cuenta</router-link> para guardar tus enlaces y ver estadísticas.
      </div>
    </main>

    <footer>
      <span>URL Shortener · construido con Java + Vue</span>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../services/api'

const auth = useAuthStore()
const url = ref('')
const result = ref(null)
const error = ref('')
const loading = ref(false)
const copied = ref(false)

async function shorten() {
  if (!url.value || loading.value) return
  if (!auth.isLoggedIn) {
    error.value = 'Debes iniciar sesión para acortar URLs.'
    return
  }
  error.value = ''
  result.value = null
  loading.value = true
  try {
    const { data } = await api.post('/api/shorten', { url: url.value })
    result.value = data
  } catch (e) {
    error.value = e.response?.data?.detalle || 'Algo salió mal. Intenta de nuevo.'
  } finally {
    loading.value = false
  }
}

async function copy() {
  if (!result.value) return
  await navigator.clipboard.writeText(result.value.shortUrl)
  copied.value = true
  setTimeout(() => copied.value = false, 2000)
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('es-CL', { day: 'numeric', month: 'short', year: 'numeric' })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.25rem 2rem;
  border-bottom: 1px solid var(--border);
}

.logo {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.dot { color: var(--text-muted); }

.nav-right {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.nav-email {
  font-size: 13px;
  color: var(--text-muted);
}

.btn-ghost {
  font-size: 14px;
  color: var(--text-muted);
  padding: 0.4rem 0.75rem;
  border-radius: var(--radius);
  transition: color 0.15s, background 0.15s;
}

.btn-ghost:hover {
  color: var(--text);
  background: var(--surface);
}

.btn-primary {
  font-size: 14px;
  color: var(--bg);
  background: var(--text);
  padding: 0.4rem 0.9rem;
  border-radius: var(--radius);
  font-weight: 500;
  transition: opacity 0.15s;
}

.btn-primary:hover { opacity: 0.85; }

main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 1.5rem;
  gap: 2rem;
}

.hero {
  text-align: center;
}

.hero h1 {
  font-size: clamp(2rem, 5vw, 3.25rem);
  font-weight: 600;
  letter-spacing: -1.5px;
  line-height: 1.15;
  margin-bottom: 0.75rem;
}

.subtitle {
  font-size: 16px;
  color: var(--text-muted);
}

.input-card {
  width: 100%;
  max-width: 640px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input-row {
  display: flex;
  gap: 0.75rem;
}

input {
  flex: 1;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 0.65rem 1rem;
  font-size: 14px;
  color: var(--text);
  outline: none;
  transition: border-color 0.15s;
}

input:focus {
  border-color: var(--border-hover);
}

input::placeholder {
  color: var(--text-dim);
}

input:disabled {
  opacity: 0.5;
}

.btn-shorten {
  background: var(--text);
  color: var(--bg);
  font-size: 14px;
  font-weight: 500;
  padding: 0.65rem 1.25rem;
  border-radius: var(--radius);
  white-space: nowrap;
  transition: opacity 0.15s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 90px;
  justify-content: center;
}

.btn-shorten:hover:not(:disabled) { opacity: 0.85; }
.btn-shorten:disabled { opacity: 0.4; cursor: not-allowed; }

.spinner {
  width: 14px;
  height: 14px;
  border: 2px solid transparent;
  border-top-color: var(--bg);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

.error-msg {
  font-size: 13px;
  color: var(--error);
  padding: 0 0.25rem;
}

.result {
  border-top: 1px solid var(--border);
  padding-top: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.result-url {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.result-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-dim);
}

.result-link {
  font-family: var(--mono);
  font-size: 15px;
  color: var(--text);
  word-break: break-all;
  transition: color 0.15s;
}

.result-link:hover { color: var(--text-muted); }

.result-actions {
  display: flex;
}

.btn-copy {
  font-size: 13px;
  color: var(--text-muted);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 0.35rem 0.9rem;
  transition: all 0.15s;
}

.btn-copy:hover {
  color: var(--text);
  border-color: var(--border-hover);
}

.btn-copy.copied {
  color: var(--success);
  border-color: var(--success);
}

.result-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--text-dim);
  gap: 1rem;
}

.result-meta span:first-child {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.guest-note {
  font-size: 13px;
  color: var(--text-dim);
  text-align: center;
}

.guest-note a {
  color: var(--text-muted);
  text-decoration: underline;
  text-underline-offset: 3px;
}

.guest-note a:hover { color: var(--text); }

footer {
  padding: 1.5rem 2rem;
  text-align: center;
  font-size: 12px;
  color: var(--text-dim);
  border-top: 1px solid var(--border);
}

.slide-enter-active, .slide-leave-active {
  transition: all 0.2s ease;
}
.slide-enter-from {
  opacity: 0;
  transform: translateY(-8px);
}
.slide-leave-to {
  opacity: 0;
}
</style>
