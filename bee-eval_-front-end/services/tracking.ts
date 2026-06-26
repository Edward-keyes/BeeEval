import http from '@/utils/request'
import userStore from './user-store'

// 用户行为类型
export type EventType = 'pageView' | 'videoView'

// 用户行为数据接口
export interface UserBehavior {
    user_id?: number
    user_name?: string
    eventType: EventType
    targetId?: string
    targetName?: string
    startTime?: number
    endTime?: number
    duration?: number
    device_info: {
        userAgent: string
        platform: string
        language: string
        screenResolution: string
    }
}

interface TrackingData {
    eventType: string;
    targetId: string;
    targetName: string;
    startTime: number;
    endTime?: number;
    duration?: number;
    deviceInfo: {
        userAgent: string;
        platform: string;
        screenSize: string;
    }
}

class TrackingService {
    private static instance: TrackingService;
    private currentTracking: TrackingData | null = null;
    private userStore = userStore;

    private constructor() { }

    static getInstance(): TrackingService {
        if (!TrackingService.instance) {
            TrackingService.instance = new TrackingService();
        }
        return TrackingService.instance;
    }

    startPageTracking(path: string, pageName: string) {
        // console.log('开始页面跟踪:', path, pageName);
        this.currentTracking = {
            eventType: 'pageView',
            targetId: path,
            targetName: pageName,
            startTime: Date.now(),
            deviceInfo: {
                userAgent: navigator.userAgent,
                platform: navigator.platform,
                screenSize: `${window.innerWidth}x${window.innerHeight}`
            }
        };
        // console.log('当前跟踪状态:', this.currentTracking);
    }

    async endPageTracking() {
        // console.log('结束页面跟踪, 当前跟踪数据:', this.currentTracking);
        if (!this.currentTracking) {
            // console.log('没有找到当前跟踪数据，跳过保存');
            return;
        }

        const trackingData = {
            ...this.currentTracking,
            endTime: Date.now(),
            duration: Math.floor((Date.now() - this.currentTracking.startTime) / 1000)
        };
        // console.log('已完成的跟踪数据:', trackingData);

        try {
            await this.saveTrackingData(trackingData);
            // console.log('成功保存页面跟踪数据');
        } catch (error) {
            // console.error('保存页面访问数据失败:', error);
        } finally {
            this.currentTracking = null;
            // console.log('重置当前跟踪数据');
        }
    }

    private async saveTrackingData(data: TrackingData) {
        try {
            // console.log('发送跟踪数据:', data);
            const trackingPayload = {
                event_type: data.eventType,
                targetId: data.targetId,
                targetName: data.targetName,
                startTime: data.startTime,
                endTime: data.endTime,
                duration: data.duration,
                device_info: {
                    userAgent: navigator.userAgent,
                    platform: navigator.platform,
                    language: navigator.language,
                    screenResolution: `${window.screen.width}x${window.screen.height}`
                }
            };

            // console.log('跟踪数据Payload:', trackingPayload);
            const response = await http.post('/vehicleusermonitor/save', trackingPayload);
            // console.log('保存跟踪数据响应:', response);

            if (response.code !== 0) {
                throw new Error(`保存跟踪数据失败: ${response.msg || '未知错误'}`);
            }
        } catch (error) {
            // console.error('保存跟踪数据时发生错误:', error);
            throw error;
        }
    }

    // 获取设备信息
    private getDeviceInfo() {
        return {
            userAgent: navigator.userAgent,
            platform: navigator.platform,
            language: navigator.language,
            screenResolution: `${window.screen.width}x${window.screen.height}`
        }
    }

    // 保存行为数据
    private async saveBehavior(data: UserBehavior) {
        try {
            // console.log('保存行为数据:', data);
            // 检查用户是否登录
            const loginResponse = await http.post<any>('/vehicleuser/isLogin');
            // console.log('用户登录检查响应:', loginResponse);

            if (loginResponse.code === 0) {
                // 用户已登录，添加用户信息
                const behaviorData = {
                    ...data,
                    device_info: this.getDeviceInfo()
                };
                // console.log('行为数据完整Payload:', behaviorData);
                const response = await http.post('/vehicleusermonitor/save', behaviorData);
                // console.log('保存行为数据响应:', response);
                return response;
            } else {
                // console.log('用户未登录，不保存行为数据');
            }
        } catch (error) {
            // console.error('保存行为数据失败:', error);
        }
    }

    // 记录页面访问
    public recordPageView = (pageName: string) => {
        const startTime = Date.now();

        // 记录页面访问开始
        this.saveBehavior({
            eventType: 'pageView',
            targetId: window.location.pathname,
            targetName: pageName,
            startTime: startTime,
            device_info: this.getDeviceInfo()
        });

        // 返回清理函数
        return () => {
            const endTime = Date.now();
            const duration = endTime - startTime;

            this.saveBehavior({
                eventType: 'pageView',
                targetId: window.location.pathname,
                targetName: pageName,
                startTime: startTime,
                endTime: endTime,
                duration,
                device_info: this.getDeviceInfo()
            });
        };
    };

    // 记录视频观看
    public recordVideoView = (videoId: string, videoName: string) => {
        let startTime = Date.now();
        let isPlaying = false;

        const handlePlay = () => {
            if (!isPlaying) {
                startTime = Date.now();
                isPlaying = true;
                // console.log('视频开始播放:', videoName);
                this.saveBehavior({
                    eventType: 'videoView',
                    targetId: videoId,
                    targetName: videoName,
                    startTime: startTime,
                    device_info: this.getDeviceInfo()
                });
            }
        };

        const handlePause = () => {
            if (isPlaying) {
                const endTime = Date.now();
                const duration = endTime - startTime;
                isPlaying = false;
                // console.log('视频结束播放:', videoName, '持续时间:', duration);
                this.saveBehavior({
                    eventType: 'videoView',
                    targetId: videoId,
                    targetName: videoName,
                    startTime: startTime,
                    endTime: endTime,
                    duration,
                    device_info: this.getDeviceInfo()
                });
            }
        };

        return {
            handlePlay,
            handlePause
        };
    };
}

const trackingService = TrackingService.getInstance();
export default trackingService; 