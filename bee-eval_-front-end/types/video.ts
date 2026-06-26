export interface Video {
    id: number;
    title?: string;
    videoName?: string;
    videoUrl: string;
    duration?: string;
    category?: string;
    brandName?: string;
    status?: number;  // 1: 正常, 2: 敬请期待, 3: 试用车型
    videoCover?: string;
    vehicleName?: string;
    videoId?: string;
    srtUrl?: string;
    videoDescription?: string;  // 视频描述
} 