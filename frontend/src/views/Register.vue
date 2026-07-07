<template>
  <div class="auth-page">
    <nav>
      <router-link to="/" class="logo">corta<span class="dot">.</span></router-link>
    </nav>

    <main>
      <div class="auth-card">
        <h2>Crea tu cuenta</h2>
        <p class="auth-sub">Empieza a acortar y gestionar tus enlaces.</p>

        <div class="form">
          <div class="field">
            <label>Email</label>
            <input v-model="email" type="email" placeholder="tu@email.com" @keydown.enter="submit" />
          </div>
          <div class="field">
            <label>Contraseña</label>
            <input v-model="password" type="password" placeholder="mínimo 6 caracteres" @keydown.enter="submit" />
          </div>

          <div v-if="error" class="error-msg">{{ error }}</div>

          <button class="btn-submit" @click="submit" :disabled="loading">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Crear cuenta</span>
          </button>
        </div>

        <p class="switch-link">
          ¿Ya tienes cuenta? <router-link to="/login">Inicia sesión</router-link>
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function submit() {
  if (!email.value || !password.value) return
  if (password.value.length < 6) {
    error.value = 'La contraseña debe tener al menos 6 caracteres.'
    return
  }
  error.value = ''
  loading.value = true
  try {
    await auth.register(email.value, password.value)
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.detalle || 'No se pudo crear la cuenta.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

nav {
  padding: 1.25rem 2rem;
  border-bottom: 1px solid var(--border);
}

.logo {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.dot { color: var(--text-muted); }

main {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem 1.5rem;
}

.auth-card {
  width: 100%;
  max-width: 380px;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

h2 {
  font-size: 22px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.auth-sub {
  font-size: 14px;
  color: var(--text-muted);
  margin-top: -1rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

label {
  font-size: 13px;
  color: var(--text-muted);
}

input {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 0.65rem 1rem;
  font-size: 14px;
  color: var(--text);
  outline: none;
  transition: border-color 0.15s;
}

input:focus { border-color: var(--border-hover); }

.error-msg {
  font-size: 13px;
  color: var(--error);
}

.btn-submit {
  background: var(--text);
  color: var(--bg);
  font-size: 14px;
  font-weight: 500;
  padding: 0.7rem;
  border-radius: var(--radius);
  width: 100%;
  transition: opacity 0.15s;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 0.25rem;
}

.btn-submit:hover:not(:disabled) { opacity: 0.85; }
.btn-submit:disabled { opacity: 0.4; cursor: not-allowed; }

.spinner {
  width: 14px;
  height: 14px;
  border: 2px solid transparent;
  border-top-color: var(--bg);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

.switch-link {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
}

.switch-link a {
  color: var(--text);
  text-decoration: underline;
  text-underline-offset: 3px;
}
</style>
