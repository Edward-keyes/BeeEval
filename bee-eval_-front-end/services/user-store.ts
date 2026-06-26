import http from '@/utils/request'

interface UserInfo {
    userId: string;
    userName: string;
    token: string;
}

class UserStore {
    private static instance: UserStore;
    private userInfo: UserInfo | null = null;

    private constructor() {
        if (typeof window !== 'undefined') {
            // 只在浏览器环境中从 localStorage 获取用户信息
            const storedInfo = localStorage.getItem('userInfo');
            if (storedInfo) {
                try {
                    this.userInfo = JSON.parse(storedInfo);
                } catch (error) {
                    // console.error('Failed to parse stored user info:', error);
                }
            }
        }
    }

    static getInstance(): UserStore {
        if (!UserStore.instance) {
            UserStore.instance = new UserStore();
        }
        return UserStore.instance;
    }

    async checkLoginStatus(): Promise<boolean> {
        try {
            const response = await http.post<any>('/vehicleuser/isLogin');

            if (response.code === 0 && response.data === true) {
                // 如果已登录，但还没有用户信息，则获取用户信息
                if (!this.userInfo) {
                    // TODO: 调用获取用户信息的接口
                    // const userInfo = await this.fetchUserInfo();
                    // this.setUserInfo(userInfo);
                }
                return true;
            } else {
                // 未登录，清除用户信息
                this.clearUserInfo();
                return false;
            }
        } catch (error) {
            // console.error('检查登录状态失败:', error);
            return false;
        }
    }

    setUserInfo(info: UserInfo) {
        this.userInfo = info;
        if (typeof window !== 'undefined') {
            localStorage.setItem('userInfo', JSON.stringify(info));
            localStorage.setItem('token', info.token);
        }
    }

    getUserInfo(): UserInfo | null {
        return this.userInfo;
    }

    clearUserInfo() {
        this.userInfo = null;
        if (typeof window !== 'undefined') {
            localStorage.removeItem('userInfo');
            localStorage.removeItem('token');
        }
    }

    isLoggedIn(): boolean {
        return !!this.userInfo;
    }
}

export const userStore = UserStore.getInstance();
export default userStore; 