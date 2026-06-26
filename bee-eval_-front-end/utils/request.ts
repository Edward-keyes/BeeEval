import config from '@/config/api.config'
import { handleTokenExpired, handleTokenReplaced } from './auth'

interface RequestOptions extends RequestInit {
    timeout?: number
}

interface ResponseData<T = any> {
    code: number
    data: T
    msg: string
}

class RequestError extends Error {
    status: number
    constructor(message: string, status: number) {
        super(message)
        this.status = status
    }
}

export async function request<T>(url: string, options: RequestOptions = {}): Promise<ResponseData<T>> {
    const { timeout = config.timeout, ...fetchOptions } = options

    // 合并默认headers
    const headers = new Headers({
        ...config.headers,
        ...fetchOptions.headers
    })

    // 添加token
    const token = localStorage.getItem('token')
    if (token) {
        headers.append('token', token)
    }

    // 构建完整的请求URL
    const apiUrl = url.startsWith('http') ? url : `${config.baseURL}${url}`
    const fullUrl = apiUrl.replace(/\/+/g, '/') // 移除多余的斜杠

    // 超时控制
    const controller = new AbortController()
    const timeoutId = setTimeout(() => controller.abort(), timeout)

    try {
        const response = await fetch(fullUrl, {
            ...fetchOptions,
            headers,
            credentials: 'include',
            signal: controller.signal
        })

        clearTimeout(timeoutId)

        if (!response.ok) {
            throw new RequestError(response.statusText, response.status)
        }

        const data: ResponseData<T> = await response.json()

        // 检查token是否失效
        if (data.code === 11012) {
            handleTokenExpired()
        }
        if (data.code === 11014) {
            handleTokenReplaced()
        }

        return data
    } catch (error) {
        if (error instanceof RequestError) {
            throw error
        }
        if (error instanceof Error) {
            if (error.name === 'AbortError') {
                throw new RequestError('请求超时', 408)
            }
            throw new RequestError(error.message, 500)
        }
        throw new RequestError('未知错误', 500)
    }
}

// 封装常用方法
export default {
    get<T>(url: string, options?: RequestOptions) {
        return request<T>(url, { ...options, method: 'GET' })
    },

    post<T>(url: string, data?: any, options?: RequestOptions) {
        return request<T>(url, {
            ...options,
            method: 'POST',
            body: JSON.stringify(data)
        })
    },

    put<T>(url: string, data?: any, options?: RequestOptions) {
        return request<T>(url, {
            ...options,
            method: 'PUT',
            body: JSON.stringify(data)
        })
    },

    delete<T>(url: string, options?: RequestOptions) {
        return request<T>(url, { ...options, method: 'DELETE' })
    }
}
