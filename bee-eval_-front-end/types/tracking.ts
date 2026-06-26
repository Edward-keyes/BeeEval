export interface UserBehavior {
    userId: string;
    userName: string;
    eventType: 'pageView' | 'videoView';
    targetId: string;  // 页面路径或视频ID
    targetName: string;  // 页面名称或视频名称
    startTime: number;
    endTime: number;
    duration: number;  // 停留时长（秒）
    deviceInfo: {
        userAgent: string;
        platform: string;
        language: string;
    };
}

export interface PageViewEvent {
    path: string;
    pageName: string;
    startTime: number;
}

export interface VideoViewEvent {
    videoId: string;
    videoName: string;
    startTime: number;
} 