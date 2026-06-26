import { toast } from "sonner"
// 登录已过期
export const handleTokenExpired = () => {
    // 清除本地存储的用户信息和token
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')

    // 显示提示
    toast.error('登录已过期，请重新登录', {
        duration: 3000,
    })

    // 延迟跳转，确保 toast 消息能够显示
    setTimeout(() => {
        window.location.href = '/login'
    }, 1000)
}

// 登录被顶替
export const handleTokenReplaced = () => {
    // 清除本地存储的用户信息和token
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')

    // 显示提示
    toast.error('登录被顶替，请重新登录', {
        duration: 3000,
    })

    // 延迟跳转，确保 toast 消息能够显示
    setTimeout(() => {
        window.location.href = '/login'
    }, 1000)
} 