interface ApiConfig {
    baseURL: string;
    timeout: number;
    headers: Record<string, string>;
}

const devConfig: ApiConfig = {
    baseURL: '/xai-vehicledata',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
}

const testConfig: ApiConfig = {
    baseURL: '/xai-vehicledata',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
}

const prodConfig: ApiConfig = {
    baseURL: '/xai-vehicledata',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
}

// 根据环境变量选择配置
const getConfig = (): ApiConfig => {
    const env = process.env.NODE_ENV
    switch (env) {
        case 'production':
            return prodConfig
        case 'test':
            return testConfig
        default:
            return devConfig
    }
}

const config = getConfig()

export default config
