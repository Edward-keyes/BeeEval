'use client'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import Image from "next/image"
import { Play, ChevronLeft, ChevronRight, Lock } from 'lucide-react'
import { useEffect, useState, useRef } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import { VideoPlayerModal } from '@/components/video-player-modal'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import http from '@/utils/request'
import { useToast } from "@/components/ui/use-toast"
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import tracker from '@/services/tracking'
import { Video } from '@/types/video'
import { useAuthStore } from '@/store/useAuthStore'
import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card"
import Hls from 'hls.js'

// 扩展Video接口以包含额外的标签属性
interface ExtendedVideo extends Video {
    functionalDomain?: string;
    threeTag?: string;
    taskType?: string;
    description?: string;
}

interface ApiResponse<T> {
    code: number;
    data: T;
    msg?: string;
}

interface UserInfo {
    vehicle?: string;
    [key: string]: any;
}

interface PerformanceVideosProps {
    currentVehicle: { name: string }
    vehicleId?: string
}

const VideoPlayer = ({ video }: { video: ExtendedVideo }) => {
    const videoRef = useRef<HTMLVideoElement>(null);
    const { handlePlay, handlePause } = tracker.recordVideoView(video.id.toString(), video.title || '未知视频');

    useEffect(() => {
        const videoElement = videoRef.current;
        if (videoElement) {
            videoElement.addEventListener('play', handlePlay);
            videoElement.addEventListener('pause', handlePause);
            videoElement.addEventListener('ended', handlePause);
        }

        return () => {
            if (videoElement) {
                videoElement.removeEventListener('play', handlePlay);
                videoElement.removeEventListener('pause', handlePause);
                videoElement.removeEventListener('ended', handlePause);
            }
        };
    }, [video]);

    return (
        <video
            ref={videoRef}
            src={video.videoUrl}
            controls
            className="w-full rounded-lg"
        />
    );
};

export function PerformanceVideos({ currentVehicle, vehicleId }: PerformanceVideosProps) {
    const [selectedVideo, setSelectedVideo] = useState<Video | null>(null)
    const [isVideoModalOpen, setIsVideoModalOpen] = useState(false)
    const [highScoreVideos, setHighScoreVideos] = useState<ExtendedVideo[]>([])
    const [highFrequencyVideos, setHighFrequencyVideos] = useState<ExtendedVideo[]>([])
    const [isLoading, setIsLoading] = useState(false)
    const [videoErrors, setVideoErrors] = useState<Set<number>>(new Set())

    // 分页状态
    const [currentPageHighScore, setCurrentPageHighScore] = useState(1)
    const [currentPageHighFrequency, setCurrentPageHighFrequency] = useState(1)
    const [isLoadingMoreHighScore, setIsLoadingMoreHighScore] = useState(false)
    const [isLoadingMoreHighFrequency, setIsLoadingMoreHighFrequency] = useState(false)
    const [hasMoreHighScore, setHasMoreHighScore] = useState(true)
    const [hasMoreHighFrequency, setHasMoreHighFrequency] = useState(true)
    const highScoreVideoRefs = useRef<{ [key: number]: HTMLVideoElement | null }>({})
    const highFrequencyVideoRefs = useRef<{ [key: number]: HTMLVideoElement | null }>({})
    const highScoreHlsInstances = useRef<{ [key: number]: Hls | null }>({})
    const highFrequencyHlsInstances = useRef<{ [key: number]: Hls | null }>({})
    const searchParams = useSearchParams()
    const id = vehicleId || searchParams.get('id')
    const highScoreScrollRef = useRef<HTMLDivElement>(null)
    const highFrequencyScrollRef = useRef<HTMLDivElement>(null)
    const { toast } = useToast()
    const [userVehicle, setUserVehicle] = useState<string>('')
    const { token, accountStatus } = useAuthStore()
    const { language } = useLanguage()

    const fetchData = async (pageHighScore = 1, pageHighFrequency = 1) => {
        try {
            setIsLoading(true)
            const response = await http.post<{
                highScoreFunctionalVideoList: Video[]
                highFrequencyFunctionalVideoList: Video[]
            }>('/vehicle/domaintree/functionalVideoList', {
                id: id,
                language: language,
                page: Math.max(pageHighScore, pageHighFrequency) // 使用最大页码作为请求参数
            })

            // 检查 token 是否失效
            if (response.code === 11012) {
                handleTokenExpired()
                return
            }
            if (response.code === 11014) {
                handleTokenReplaced()
                return
            }

            setHighScoreVideos(response.data.highScoreFunctionalVideoList)
            setHighFrequencyVideos(response.data.highFrequencyFunctionalVideoList)
            setVideoErrors(new Set()) // 重置视频错误状态

            // 重置分页状态
            setCurrentPageHighScore(1)
            setCurrentPageHighFrequency(1)
            setHasMoreHighScore(response.data.highScoreFunctionalVideoList.length >= 5)
            setHasMoreHighFrequency(response.data.highFrequencyFunctionalVideoList.length >= 5)
        } catch (error) {
            // console.error('Error fetching data:', error)
        } finally {
            setIsLoading(false)
        }
    }

    // 加载更多高分视频
    const loadMoreHighScoreVideos = async () => {
        if (!hasMoreHighScore || isLoadingMoreHighScore) return

        try {
            setIsLoadingMoreHighScore(true)
            const nextPage = currentPageHighScore + 1

            const response = await http.post<{
                highScoreFunctionalVideoList: Video[]
                highFrequencyFunctionalVideoList: Video[]
            }>('/vehicle/domaintree/functionalVideoList', {
                id: id,
                language: language,
                page: nextPage
            })

            if (response.code === 11012) {
                handleTokenExpired()
                return
            }
            if (response.code === 11014) {
                handleTokenReplaced()
                return
            }

            const newVideos = response.data.highScoreFunctionalVideoList
            if (newVideos.length > 0) {
                setHighScoreVideos(prev => [...prev, ...newVideos])
                setCurrentPageHighScore(nextPage)
                // 后续页返回2条，如果返回的条数少于2条说明没有更多数据了
                setHasMoreHighScore(newVideos.length >= 2)
            } else {
                setHasMoreHighScore(false)
            }
        } catch (error) {
            // console.error('Error loading more high score videos:', error)
            setHasMoreHighScore(false)
        } finally {
            setIsLoadingMoreHighScore(false)
        }
    }

    // 加载更多高频视频
    const loadMoreHighFrequencyVideos = async () => {
        if (!hasMoreHighFrequency || isLoadingMoreHighFrequency) return

        try {
            setIsLoadingMoreHighFrequency(true)
            const nextPage = currentPageHighFrequency + 1

            const response = await http.post<{
                highScoreFunctionalVideoList: Video[]
                highFrequencyFunctionalVideoList: Video[]
            }>('/vehicle/domaintree/functionalVideoList', {
                id: id,
                language: language,
                page: nextPage
            })

            if (response.code === 11012) {
                handleTokenExpired()
                return
            }
            if (response.code === 11014) {
                handleTokenReplaced()
                return
            }

            const newVideos = response.data.highFrequencyFunctionalVideoList
            if (newVideos.length > 0) {
                setHighFrequencyVideos(prev => [...prev, ...newVideos])
                setCurrentPageHighFrequency(nextPage)
                // 后续页返回2条，如果返回的条数少于2条说明没有更多数据了
                setHasMoreHighFrequency(newVideos.length >= 2)
            } else {
                setHasMoreHighFrequency(false)
            }
        } catch (error) {
            // console.error('Error loading more high frequency videos:', error)
            setHasMoreHighFrequency(false)
        } finally {
            setIsLoadingMoreHighFrequency(false)
        }
    }

    useEffect(() => {
        fetchData()
    }, [id, language])

    // 为高分表现视频设置首帧显示
    useEffect(() => {
        // 销毁未用的实例
        Object.keys(highScoreHlsInstances.current).forEach(id => {
            if (!highScoreVideos.find(v => v.id === Number(id))) {
                highScoreHlsInstances.current[Number(id)]?.destroy()
                delete highScoreHlsInstances.current[Number(id)]
            }
        })
        highScoreVideos.forEach((video) => {
            const videoElement = highScoreVideoRefs.current[video.id]
            if (!videoElement || !video.videoUrl) return

            // 如果已经初始化过该 video 的 HLS/原生处理，跳过
            if (video.id in highScoreHlsInstances.current) return

            if (Hls.isSupported()) {
                const hls = new Hls()
                hls.loadSource(video.videoUrl)
                hls.attachMedia(videoElement)
                hls.on(Hls.Events.MANIFEST_PARSED, () => {
                    try { videoElement.currentTime = 0 } catch (e) { }
                    videoElement.pause()
                })
                hls.on(Hls.Events.ERROR, () => {
                    setVideoErrors(prev => new Set(prev).add(video.id))
                })
                highScoreHlsInstances.current[video.id] = hls
            } else if (videoElement.canPlayType('application/vnd.apple.mpegurl')) {
                videoElement.src = video.videoUrl
                const onLoaded = () => {
                    try { videoElement.currentTime = 0 } catch (e) { }
                    videoElement.pause()
                }
                const onError = () => {
                    setVideoErrors(prev => new Set(prev).add(video.id))
                }
                videoElement.addEventListener('loadedmetadata', onLoaded, { once: true })
                videoElement.addEventListener('error', onError, { once: true })
                highScoreHlsInstances.current[video.id] = null
            }
        })
        // 组件卸载时销毁所有实例
        return () => {
            Object.values(highScoreHlsInstances.current).forEach(hls => hls?.destroy())
            highScoreHlsInstances.current = {}
        }
    }, [highScoreVideos])

    // 为高频表现视频设置首帧显示
    useEffect(() => {
        // 销毁未用的实例
        Object.keys(highFrequencyHlsInstances.current).forEach(id => {
            if (!highFrequencyVideos.find(v => v.id === Number(id))) {
                highFrequencyHlsInstances.current[Number(id)]?.destroy()
                delete highFrequencyHlsInstances.current[Number(id)]
            }
        })
        highFrequencyVideos.forEach((video) => {
            const videoElement = highFrequencyVideoRefs.current[video.id]
            if (!videoElement || !video.videoUrl) return

            // 如果已经初始化过该 video 的 HLS/原生处理，跳过
            if (video.id in highFrequencyHlsInstances.current) return

            if (Hls.isSupported()) {
                const hls = new Hls()
                hls.loadSource(video.videoUrl)
                hls.attachMedia(videoElement)
                hls.on(Hls.Events.MANIFEST_PARSED, () => {
                    try { videoElement.currentTime = 0 } catch (e) { }
                    videoElement.pause()
                })
                hls.on(Hls.Events.ERROR, () => {
                    setVideoErrors(prev => new Set(prev).add(video.id))
                })
                highFrequencyHlsInstances.current[video.id] = hls
            } else if (videoElement.canPlayType('application/vnd.apple.mpegurl')) {
                videoElement.src = video.videoUrl
                const onLoaded = () => {
                    try { videoElement.currentTime = 0 } catch (e) { }
                    videoElement.pause()
                }
                const onError = () => {
                    setVideoErrors(prev => new Set(prev).add(video.id))
                }
                videoElement.addEventListener('loadedmetadata', onLoaded, { once: true })
                videoElement.addEventListener('error', onError, { once: true })
                highFrequencyHlsInstances.current[video.id] = null
            }
        })
        // 组件卸载时销毁所有实例
        return () => {
            Object.values(highFrequencyHlsInstances.current).forEach(hls => hls?.destroy())
            highFrequencyHlsInstances.current = {}
        }
    }, [highFrequencyVideos])

    useEffect(() => {
        const fetchUserVehicle = async () => {
            try {
                const response = await http.post<ApiResponse<UserInfo>>('/vehicleuser/isLogin');
                if (response.code === 0 && response.data && 'vehicle' in response.data) {
                    setUserVehicle(response.data.vehicle as string);
                }
            } catch (error) {
                // console.error('获取用户信息失败:', error);
            }
        };
        fetchUserVehicle();
    }, []);

    const scroll = (ref: React.RefObject<HTMLDivElement>, direction: 'left' | 'right') => {
        if (ref.current) {
            const scrollAmount = 280 // 卡片宽度 + gap
            ref.current.scrollBy({
                left: direction === 'left' ? -scrollAmount : scrollAmount,
                behavior: 'smooth'
            })
        }
    }

    const handleVideoClick = (video: Video) => {
        // 如果未登录，提示登录
        if (!token) {
            toast({
                title: "请先登录",
                description: "查看视频需要登录账号",
                variant: "destructive",
                className: "bg-[#1C2028] border-gray-800 text-white border-l-4 border-l-red-500",
                duration: 3000,
            })
            return
        }

        // 检查是否是试用账号
        if (accountStatus === '1') {
            toast({
                title: "试用账号暂不支持",
                description: "试用账号无法查看高频问题视频",
                variant: "destructive",
                className: "bg-[#1C2028] border-gray-800 text-white border-l-4 border-l-red-500",
                duration: 3000,
            })
            return
        }

        // 检查是否是车企对应的视频
        if (userVehicle && video.vehicleName === userVehicle) {
            setSelectedVideo(video)
            setIsVideoModalOpen(true)
        } else {
            toast({
                title: "访问受限",
                description: "高频问题视频仅对应车企可见",
                variant: "destructive",
                className: "bg-[#1C2028] border-gray-800 text-white border-l-4 border-l-red-500",
                duration: 3000,
            })
        }
    }

    if (isLoading) {
        return (
            <div className="bg-[#0D1117] rounded-3xl shadow-md min-h-[400px]">
                <div className="overflow-hidden border-none shadow-xl bg-gradient-to-br from-[#0D1117] via-[#151922] to-[#1C2028] rounded-t-3xl">
                    <div className="bg-gradient-to-b from-black to-[#151922] py-6 px-6">
                        <h2 className="text-2xl font-bold text-[#F0A432]">{translations[language].featurePerformanceVideo}</h2>
                    </div>
                </div>
                <div className="flex items-center justify-center min-h-[300px]">
                    <div className="space-y-4 flex flex-col items-center">
                        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-amber-500"></div>
                        <p className="text-gray-400 text-sm">Loading...</p>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className="bg-[#0D1117] rounded-3xl shadow-md">
            <div className="overflow-hidden border-none shadow-xl bg-gradient-to-br from-[#0D1117] via-[#151922] to-[#1C2028] rounded-t-3xl">
                <div className="bg-gradient-to-b from-black to-[#151922] py-6 px-6">
                    <h2 className="text-2xl font-bold text-[#F0A432]">{translations[language].featurePerformanceVideo}</h2>
                </div>
            </div>
            <div className="p-6">
                <Card className="bg-[#1C2028] border-gray-800/50 rounded-2xl">
                    <CardContent className="space-y-8 p-6">
                        {/* 高分表现 */}
                        <div>
                            <h3 className="text-xl font-bold text-amber-500 mb-4">{translations[language].highScorePerformance}</h3>
                            <div className="relative">
                                <div className="overflow-x-hidden" ref={highScoreScrollRef}>
                                    <div className="flex gap-6">
                                        {highScoreVideos.map((video, index) => (
                                            <div key={video.id || `${video.videoUrl}-${index}`} className="w-[260px] flex-shrink-0">
                                                <HoverCard openDelay={100} closeDelay={100}>
                                                    <HoverCardTrigger asChild>
                                                        <div>
                                                            <Card
                                                                className="group bg-[#1C2028]/80 border border-gray-800/50 hover:border-amber-500 transition-colors rounded-xl overflow-hidden cursor-pointer"
                                                                onClick={() => {
                                                                    setSelectedVideo(video)
                                                                    setIsVideoModalOpen(true)
                                                                }}
                                                            >
                                                                <CardContent className="p-0">
                                                                    <div className="relative aspect-video">
                                                                        {video.videoCover ? (
                                                                            <Image
                                                                                src={video.videoCover}
                                                                                alt={video.videoName || '视频'}
                                                                                fill
                                                                                className="object-cover group-hover:scale-105 transition-transform duration-300"
                                                                            />
                                                                        ) : video.videoUrl && !videoErrors.has(video.id) ? (
                                                                            <video
                                                                                ref={(el) => {
                                                                                    if (el) {
                                                                                        highScoreVideoRefs.current[video.id] = el
                                                                                    }
                                                                                }}
                                                                                src={video.videoUrl}
                                                                                className="object-cover w-full h-full group-hover:scale-105 transition-transform duration-300"
                                                                                preload="metadata"
                                                                                muted
                                                                                playsInline
                                                                            />
                                                                        ) : (
                                                                            <div className="w-full h-full bg-gradient-to-br from-gray-800 to-gray-900 flex items-center justify-center">
                                                                                <div className="text-center">
                                                                                    <Play className="h-12 w-12 text-amber-500 mx-auto mb-3 opacity-50" />
                                                                                    <p className="text-gray-400 text-sm">视频预览</p>
                                                                                    <p className="text-gray-500 text-xs">点击播放</p>
                                                                                </div>
                                                                            </div>
                                                                        )}
                                                                        <div className="absolute inset-0 bg-black/30 opacity-0 group-hover:opacity-100 transition-all duration-300 flex items-center justify-center">
                                                                            <Button
                                                                                variant="ghost"
                                                                                size="icon"
                                                                                className="w-12 h-12 rounded-full bg-amber-500/80 hover:bg-amber-500 text-black scale-75 group-hover:scale-100 transition-transform duration-300"
                                                                            >
                                                                                <Play className="h-6 w-6" />
                                                                            </Button>
                                                                        </div>
                                                                    </div>
                                                                </CardContent>
                                                            </Card>
                                                            <div className="mt-3">
                                                                <h4 className="font-medium text-white mb-1">{video.videoName}</h4>
                                                            </div>
                                                            <div className="flex items-center gap-2 mt-1 flex-wrap">
                                                                {video.functionalDomain && (
                                                                    <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                        {video.functionalDomain}
                                                                    </div>
                                                                )}
                                                                {video.threeTag && (
                                                                    <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                        {video.threeTag}
                                                                    </div>
                                                                )}
                                                                {video.taskType && (
                                                                    <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                        {video.taskType}
                                                                    </div>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </HoverCardTrigger>
                                                    {/* <HoverCardContent
                                                        className="w-80 p-4 bg-[#1C2028] border-gray-800 text-white shadow-2xl"
                                                        side="top"
                                                        align="center"
                                                    >
                                                        <div className="space-y-2">
                                                            <h4 className="font-medium text-amber-500">{video.videoName}</h4>
                                                            <p className="text-sm text-gray-400">{video.videoDescription || '暂无描述'}</p>
                                                        </div>
                                                    </HoverCardContent> */}
                                                    <HoverCardContent
                                                        className="w-80 p-4 bg-[#1C2028] border-gray-800 text-white shadow-2xl"
                                                        side="top"
                                                        align="center"
                                                    >
                                                        <div className="space-y-2">
                                                            <h4 className="font-medium text-amber-500">{video.videoName}</h4>
                                                            <p className="text-sm text-gray-400">{video.description || '暂无描述'}</p>
                                                        </div>
                                                    </HoverCardContent>
                                                </HoverCard>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                                <Button
                                    variant="ghost"
                                    size="icon"
                                    onClick={() => scroll(highScoreScrollRef, 'left')}
                                    className="absolute -left-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg"
                                >
                                    <ChevronLeft className="h-4 w-4" />
                                </Button>
                                <Button
                                    variant="ghost"
                                    size="icon"
                                    onClick={async () => {
                                        // 先尝试滚动
                                        scroll(highScoreScrollRef, 'right')
                                        // 如果有更多数据且没有在加载中，则加载更多
                                        if (hasMoreHighScore && !isLoadingMoreHighScore) {
                                            await loadMoreHighScoreVideos()
                                        }
                                    }}
                                    disabled={isLoadingMoreHighScore}
                                    className="absolute -right-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    {isLoadingMoreHighScore ? (
                                        <div className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full" />
                                    ) : (
                                        <ChevronRight className="h-4 w-4" />
                                    )}
                                </Button>
                            </div>
                        </div>

                        {/* 高频问题 */}
                        <div>
                            <h3 className="text-xl font-bold text-amber-500 mb-4">{translations[language].highFrequencyIssues}</h3>
                            <div className="relative">
                                {accountStatus === '1' ? (
                                    <div className="flex items-center justify-center p-8 bg-[#1C2028]/80 border border-gray-800/50 rounded-xl">
                                        <p className="text-gray-400 text-center">
                                            开通正式账号即可查看高频问题视频
                                        </p>
                                    </div>
                                ) : (
                                    <div className="overflow-x-hidden" ref={highFrequencyScrollRef}>
                                        <div className="flex gap-6">
                                            {highFrequencyVideos.map((video, index) => (
                                                <div key={video.id || `${video.videoUrl}-${index}`} className="w-[260px] flex-shrink-0">
                                                    <HoverCard openDelay={100} closeDelay={100}>
                                                        <HoverCardTrigger asChild>
                                                            <div>
                                                                <Card
                                                                    className="group bg-[#1C2028]/80 border border-gray-800/50 hover:border-amber-500 cursor-pointer transition-colors rounded-xl overflow-hidden"
                                                                    onClick={() => {
                                                                        setSelectedVideo(video)
                                                                        setIsVideoModalOpen(true)
                                                                    }}
                                                                >
                                                                    <CardContent className="p-0">
                                                                        <div className="relative aspect-video">
                                                                            {video.videoCover ? (
                                                                                <Image
                                                                                    src={video.videoCover}
                                                                                    alt={video.videoName || '视频'}
                                                                                    fill
                                                                                    className="object-cover group-hover:scale-105 transition-transform duration-300"
                                                                                />
                                                                            ) : video.videoUrl && !videoErrors.has(video.id) ? (
                                                                                <video
                                                                                    ref={(el) => {
                                                                                        if (el) {
                                                                                            highFrequencyVideoRefs.current[video.id] = el
                                                                                        }
                                                                                    }}
                                                                                    src={video.videoUrl}
                                                                                    className="object-cover w-full h-full group-hover:scale-105 transition-transform duration-300"
                                                                                    preload="metadata"
                                                                                    muted
                                                                                    playsInline
                                                                                />
                                                                            ) : (
                                                                                <div className="w-full h-full bg-gradient-to-br from-gray-800 to-gray-900 flex items-center justify-center">
                                                                                    <div className="text-center">
                                                                                        <Play className="h-12 w-12 text-amber-500 mx-auto mb-3 opacity-50" />
                                                                                        <p className="text-gray-400 text-sm">视频预览</p>
                                                                                        <p className="text-gray-500 text-xs">点击播放</p>
                                                                                    </div>
                                                                                </div>
                                                                            )}
                                                                            <div className="absolute inset-0 bg-black/30 opacity-0 group-hover:opacity-100 transition-all duration-300 flex items-center justify-center">
                                                                                <Button
                                                                                    variant="ghost"
                                                                                    size="icon"
                                                                                    className="w-12 h-12 rounded-full bg-amber-500/80 hover:bg-amber-500 text-black scale-75 group-hover:scale-100 transition-transform duration-300"
                                                                                >
                                                                                    <Play className="h-6 w-6" />
                                                                                </Button>
                                                                            </div>
                                                                        </div>
                                                                    </CardContent>
                                                                </Card>
                                                                <div className="mt-3">
                                                                    <h4 className="font-medium text-white mb-1">{video.videoName}</h4>
                                                                </div>
                                                                <div className="flex items-center gap-2 mt-1 flex-wrap">
                                                                    {video.functionalDomain && (
                                                                        <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                            {video.functionalDomain}
                                                                        </div>
                                                                    )}
                                                                    {video.threeTag && (
                                                                        <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                            {video.threeTag}
                                                                        </div>
                                                                    )}
                                                                    {video.taskType && (
                                                                        <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 bg-[#1C2028]/50 border-amber-500/50 text-amber-500 whitespace-nowrap">
                                                                            {video.taskType}
                                                                        </div>
                                                                    )}
                                                                </div>
                                                            </div>
                                                        </HoverCardTrigger>
                                                        <HoverCardContent
                                                            className="w-80 p-4 bg-[#1C2028] border-gray-800 text-white shadow-2xl"
                                                            side="top"
                                                            align="center"
                                                        >
                                                            <div className="space-y-2">
                                                                <h4 className="font-medium text-amber-500">{video.videoName}</h4>
                                                                <p className="text-sm text-gray-400">{video.description || '暂无描述'}</p>
                                                            </div>
                                                        </HoverCardContent>
                                                    </HoverCard>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                                {accountStatus !== '1' && (
                                    <>
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            onClick={() => scroll(highFrequencyScrollRef, 'left')}
                                            className="absolute -left-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg"
                                        >
                                            <ChevronLeft className="h-4 w-4" />
                                        </Button>
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            onClick={async () => {
                                                // 先尝试滚动
                                                scroll(highFrequencyScrollRef, 'right')
                                                // 如果有更多数据且没有在加载中，则加载更多
                                                if (hasMoreHighFrequency && !isLoadingMoreHighFrequency) {
                                                    await loadMoreHighFrequencyVideos()
                                                }
                                            }}
                                            disabled={isLoadingMoreHighFrequency}
                                            className="absolute -right-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
                                        >
                                            {isLoadingMoreHighFrequency ? (
                                                <div className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full" />
                                            ) : (
                                                <ChevronRight className="h-4 w-4" />
                                            )}
                                        </Button>
                                    </>
                                )}
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>

            <VideoPlayerModal
                isOpen={isVideoModalOpen}
                onClose={() => setIsVideoModalOpen(false)}
                video={selectedVideo ? {
                    ...selectedVideo,
                    title: selectedVideo.videoName,
                    subtitleUrl: selectedVideo.srtUrl
                } : null}
            />
        </div>
    )
}
