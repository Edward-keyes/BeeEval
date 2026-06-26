'use client'

import { useEffect, useRef } from 'react'
import { usePathname } from 'next/navigation'
import trackingService from '@/services/tracking'
import userStore from '@/services/user-store'

// 获取页面名称
const getPageName = (path: string): string => {
    // 根据路径返回对应的页面名称
    const pageNames: Record<string, string> = {
        '/': '首页',
        '/rankings': '排行榜',
        '/faq': '常见问题',
        '/vehicle-detail': '车辆详情',
        '/domain-tree': '能力域树',
        '/compare': '对比分析',
        // 添加更多路由映射
    }

    // 如果路径是/vehicle-detail/xxx形式的，提取出xxx
    if (path.startsWith('/vehicle-detail/')) {
        return '车辆详情 - ' + path.split('/').pop();
    }

    return pageNames[path] || path
}

// 全局变量来保存当前跟踪的状态
let globalTrackingState: {
    pathname: string | null;
    startTime: number | null;
    cleanup: (() => void) | null;
} = {
    pathname: null,
    startTime: null,
    cleanup: null
};

export function UserTracker() {
    const pathname = usePathname()
    const instanceIdRef = useRef<string>(`tracker-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`)

    useEffect(() => {
        // console.log(`[UserTracker ${instanceIdRef.current}] 路径变化: ${globalTrackingState.pathname} -> ${pathname}`);

        // 如果路径相同，不需要记录
        if (globalTrackingState.pathname === pathname) {
            // console.log(`[UserTracker ${instanceIdRef.current}] 路径未变化，跳过记录`);
            return;
        }

        // 如果有上一个页面的记录，先执行清理函数
        if (globalTrackingState.cleanup) {
            // console.log(`[UserTracker ${instanceIdRef.current}] 结束上一个页面访问跟踪 for ${globalTrackingState.pathname}`);
            try {
                globalTrackingState.cleanup();
            } catch (error) {
                // console.error(`[UserTracker ${instanceIdRef.current}] 结束上一个页面跟踪时发生错误:`, error);
            }
        }

        const initializeTracking = async () => {
            try {
                // 检查用户登录状态
                // console.log(`[UserTracker ${instanceIdRef.current}] 开始检查登录状态`);
                const isLoggedIn = await userStore.checkLoginStatus();
                // console.log(`[UserTracker ${instanceIdRef.current}] 登录状态:`, isLoggedIn);

                if (!isLoggedIn) {
                    // console.log(`[UserTracker ${instanceIdRef.current}] 用户未登录，不进行行为跟踪`);
                    return;
                }

                // 开始页面访问跟踪 - 使用recordPageView方法
                const pageName = getPageName(pathname);
                // console.log(`[UserTracker ${instanceIdRef.current}] 开始页面访问跟踪:`, pageName);

                // 记录全局状态
                globalTrackingState.pathname = pathname;
                globalTrackingState.startTime = Date.now();
                globalTrackingState.cleanup = trackingService.recordPageView(pageName);
            } catch (error) {
                // console.error(`[UserTracker ${instanceIdRef.current}] 初始化页面跟踪时发生错误:`, error);
            }
        };

        // 初始化跟踪
        initializeTracking();

        // 组件卸载时，不在这里结束跟踪，而是在下一次路径变化时
        return () => {
            // 不在这里调用cleanup，而是在下一次路径变化时
            // console.log(`[UserTracker ${instanceIdRef.current}] 组件卸载，但跟踪将在下一次路径变化时结束`);
        };
    }, [pathname]);

    return null;
} 