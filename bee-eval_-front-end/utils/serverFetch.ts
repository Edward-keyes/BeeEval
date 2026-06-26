
interface RequestOptions extends RequestInit {
    timeout?: number,
    revalidate?: number,
}

interface ResponseData<T = any> {
    code: number
    data: T
    msg: string
}


export async function serverFetch<T>(url: string, options: RequestOptions = {}): Promise<ResponseData<T>> {
    const baseUrl = process.env.API_BASE_URL
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers,
    };
    try {
        var setData = {
            ...options,
            headers,
        }
        const response = await fetch(`${baseUrl}${url}`, setData);
        const data = await response.json();
        return data;
    } catch (error) {
        // console.error('API request failed:', error);
        throw error;
    }
};


// 封装常用方法1
export default {
    get<T>(url: string, options?: RequestOptions) {
        return serverFetch<T>(url, { ...options, method: 'GET' })
    },

    post<T>(url: string, data?: any, options?: RequestOptions) {
        return serverFetch<T>(url, {
            ...options,
            method: 'POST',
            body: JSON.stringify(data)
        })
    },

    put<T>(url: string, data?: any, options?: RequestOptions) {
        return serverFetch<T>(url, {
            ...options,
            method: 'PUT',
            body: JSON.stringify(data)
        })
    },

    delete<T>(url: string, options?: RequestOptions) {
        return serverFetch<T>(url, { ...options, method: 'DELETE' })
    }
}
