import { create } from 'zustand'

interface AuthState {
  token: string | null
  userEmail: string | null
  accountStatus: string | null
  userInfo: any | null
  selectedModel: any | null
  isAuthenticated: boolean
  setAuth: (token: string, userEmail: string, accountStatus: string) => void
  clearAuth: () => void
  logout: () => void
}

// 从 localStorage 获取初始状态
const getInitialState = (): Partial<AuthState> => {
  if (typeof window === 'undefined') {
    return {
      token: null,
      userEmail: null,
      accountStatus: null,
      userInfo: null,
      selectedModel: null,
      isAuthenticated: false
    }
  }
  
  return {
    token: localStorage.getItem('token'),
    userEmail: localStorage.getItem('userEmail'),
    accountStatus: localStorage.getItem('accountStatus'),
    userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')!) : null,
    selectedModel: localStorage.getItem('selectedModel') ? JSON.parse(localStorage.getItem('selectedModel')!) : null,
    isAuthenticated: !!localStorage.getItem('token')
  }
}

export const useAuthStore = create<AuthState>((set) => ({
  token: null,
  userEmail: null,
  accountStatus: null,
  userInfo: null,
  selectedModel: null,
  isAuthenticated: false,
  ...getInitialState(),
  setAuth: (token: string, userEmail: string, accountStatus: string) => {
    set({ token, userEmail, accountStatus, isAuthenticated: true })
    localStorage.setItem('token', token)
    localStorage.setItem('userEmail', userEmail)
    localStorage.setItem('accountStatus', accountStatus)
  },
  clearAuth: () => {
    set({ token: null, userEmail: null, accountStatus: null, isAuthenticated: false })
    localStorage.removeItem('token')
    localStorage.removeItem('userEmail')
    localStorage.removeItem('accountStatus')
  },
  logout: () => {
    set({ 
      token: null, 
      accountStatus: null,
      userInfo: null,
      selectedModel: null,
      isAuthenticated: false 
    })
    localStorage.removeItem('token')
    localStorage.removeItem('accountStatus')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('selectedModel')
    localStorage.removeItem('userEmail')
  },
})) 