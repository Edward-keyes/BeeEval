'use client'

import { useState, useEffect, useRef } from 'react'
import { Button } from '@/components/ui/button'
import { ScrollArea, ScrollBar } from '@/components/ui/scroll-area'
import Image from 'next/image'
import {
    Car,
    Settings,
    Phone,
    Music,
    User,
    Power,
    Navigation,
    Camera,
    Bell,
    Smartphone,
    Monitor,
    Eye,
    Map,
    Download,
    Play,
    ChevronLeft,
    ChevronRight,
    Book,
    Settings2,
    MessageSquare,
    PenTool,
    Lock
} from 'lucide-react'
import { motion } from 'framer-motion'
import { cn } from '@/lib/utils'
import { LazyImage } from '@/components/lazy-image'
import { VideoPlayerModal } from './video-player-modal'
import { Badge } from '@/components/ui/badge'
import http from '@/utils/request'
import Hls from 'hls.js'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { useRouter } from 'next/navigation'
import { toast } from 'sonner'
import { useAuthStore } from '@/store/useAuthStore'
import tracker from '@/services/tracking'
import { Video } from '@/types/video'

interface MenuItem {
    icon: string
    label: any
}

const formatVideoTitle = (title: string) => {
    // 使用正则表达式匹配并移除车型和版本信息
    return title.replace(/^[^-]+-[^-]+-\d+\.\d+\.*\d*-/, '')
}

// 试用账号可查看的车型配置
const TRIAL_VEHICLES = {
    description: '当前账号为试用账号，仅支持查看试用车型'
}

export function FeaturedFunctions(data: any) {
    const { language } = useLanguage()
    const router = useRouter()
    const [selectedPrimary, setSelectedPrimary] = useState<any>(null)
    const [selectedSecondary, setSelectedSecondary] = useState('')
    const [currentPage, setCurrentPage] = useState(1)
    const [selectedVideo, setSelectedVideo] = useState<Video | null>(null)
    const [isVideoModalOpen, setIsVideoModalOpen] = useState(false)
    const videosPerPage = 8
    const videoRefs = useRef<{ [key: number]: HTMLVideoElement | null }>({})
    const [primaryMenuItems, setPrimaryMenuItems] = useState<MenuItem[]>([])
    const [secondaryMenuItems, setSecondaryMenuItems] = useState<Record<string, string[]>>({})
    const [featuredVideos, setFeaturedVideos] = useState<Record<any, Video[]>>({})
    const [isRestrictedModalOpen, setIsRestrictedModalOpen] = useState(false)
    const { token, accountStatus } = useAuthStore()

    // 重置状态并重新获取数据的函数
    const resetAndFetch = () => {
        // 清除状态
        setPrimaryMenuItems([])
        setSelectedPrimary(null)
        setSelectedSecondary('')
        setSecondaryMenuItems({})
        setFeaturedVideos({})
        // 重新调用接口刷新数据
        fetchData()
    }

    // 监听语言变化，重新获取数据
    useEffect(() => {
        resetAndFetch()
    }, [language])

    // 数据获取函数
    const fetchData = async () => {
        try {
            const response: any = await http.post('/ware/functiononetag/queryAllHighlightFunction', {
                language: language
            })

            const fetchedMenuItems: any = response.data.map((item: any) => ({
                label: item.oneTagName,
                icon: item.iconUrl
            }))
            setPrimaryMenuItems(fetchedMenuItems)
            setSelectedPrimary(fetchedMenuItems[0].label)

            const fetchedSecondaryMenuItems: Record<string, string[]> = {}
            const fetchedFeaturedVideos: Record<string, Video[]> = {}

            let oneLabel = ''
            response.data.forEach((item: any, index: number) => {
                if (index === 0) {
                    oneLabel = item.oneTagName
                }
                fetchedSecondaryMenuItems[item.oneTagName] = []

                item.twoTagHighlightRequests.forEach((item1: any) => {
                    fetchedSecondaryMenuItems[item.oneTagName].push(item1.twoTagName)

                    item1.threeTagHighlightRequests.forEach((item2: any, index2: any) => {
                        const video: Video = {
                            id: index2,
                            title: item2.threeTagName,
                            category: item1.twoTagName,
                            videoUrl: item2.videoUrl,
                            brandName: item2.brandName,
                            status: item2.status,
                            srtUrl: item2.srtUrl
                        }
                        if (!fetchedFeaturedVideos[item.oneTagName]) {
                            fetchedFeaturedVideos[item.oneTagName] = []
                        }
                        fetchedFeaturedVideos[item.oneTagName].push(video)
                    })
                })
            })
            setSecondaryMenuItems(fetchedSecondaryMenuItems)
            setFeaturedVideos(fetchedFeaturedVideos)
            setSelectedSecondary(fetchedSecondaryMenuItems[oneLabel][0])
        } catch (error) {
            // console.error('Error fetching data:', error)
        }
    }

    // 初始数据加载
    useEffect(() => {
        fetchData()
    }, [])

    const filteredVideos =
        featuredVideos[selectedPrimary]?.filter(
            (video) => selectedSecondary === '全部' || video.category === selectedSecondary
        ) || []

    const totalPages = Math.ceil(filteredVideos.length / videosPerPage)
    const paginatedVideos = filteredVideos.slice((currentPage - 1) * videosPerPage, currentPage * videosPerPage)
    useEffect(() => {
        // 为每个视频设置第一帧
        paginatedVideos.forEach((video) => {
            const videoElement = videoRefs.current[video.id]
            if (videoElement) {
                if (Hls.isSupported() && video?.videoUrl) {
                    const hls = new Hls()
                    hls.loadSource(video?.videoUrl)
                    hls.attachMedia(videoElement)
                    hls.on(Hls.Events.MANIFEST_PARSED, () => {
                        videoElement.currentTime = 0
                        videoElement.pause()
                    })
                } else if (videoElement.canPlayType('application/vnd.apple.mpegurl')) {
                    videoElement.src = video.videoUrl
                    videoElement.addEventListener('loadedmetadata', () => {
                        videoElement.currentTime = 0
                        videoElement.pause()
                    })
                }
            }
        })
    }, [currentPage, selectedPrimary, selectedSecondary])

    // 检查视频是否属于试用车型
    const isTrialVideo = (video: Video) => {
        return video.status === 3
    }

    // 检查是否应该限制访问
    const shouldRestrictAccess = (video: Video) => {
        // 如果用户未登录或者是试用账号但视频不属于试用车型，则限制访问
        return (token && accountStatus === '1' && !isTrialVideo(video))
    }

    const handleVideoClick = (video: Video) => {
        // 如果未登录，提示登录
        if (!token) {
            toast('请先登录', {
                description: '查看视频需要登录账号',
                duration: 3000,
                style: {
                    background: '#FFC107',
                    color: '#000',
                },
            })
            return
        }

        // 检查是否是受限视频（试用账号但不是试用车型的视频）
        if (shouldRestrictAccess(video)) {
            toast('试用账号暂不支持', {
                description: TRIAL_VEHICLES.description,
                duration: 3000,
                style: {
                    background: '#FFC107',
                    color: '#000',
                },
            })
            return
        }

        // 正常播放视频
        setSelectedVideo(video)
        setIsVideoModalOpen(true)
    }

    const VideoPlayer = ({ video }: { video: Video }) => {
        const videoRef = useRef<HTMLVideoElement>(null);
        const { handlePlay, handlePause } = tracker.recordVideoView(video.id.toString(), video.title || video.videoName || '未知视频');

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

    return (
        <div className="py-12 bg-[#151922] rounded-3xl shadow-md mb-12">
            <div className="container mx-auto px-4">
                <motion.h2
                    initial={{ opacity: 0, y: 20 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    className="text-2xl font-bold mb-8 text-white"
                >
                    {translations[language].featuredFunctions}
                </motion.h2>

                <div className="space-y-6">
                    {/* Primary Menu */}
                    <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4 mb-6">
                        {primaryMenuItems.map((item, index) => (
                            <motion.div
                                key={item.label}
                                initial={{ opacity: 0, scale: 0.9 }}
                                whileInView={{ opacity: 1, scale: 1 }}
                                viewport={{ once: true }}
                                transition={{ delay: index * 0.05 }}
                                whileHover={{ scale: 1.05 }}
                                whileTap={{ scale: 0.95 }}
                                onClick={() => {
                                    setSelectedPrimary(item.label)
                                    setSelectedSecondary(secondaryMenuItems[item.label][0])
                                    setCurrentPage(1)
                                }}
                                className={cn(
                                    'flex flex-col items-center justify-center p-4 rounded-xl transition-all duration-200 cursor-pointer',
                                    selectedPrimary === item.label
                                        ? 'bg-amber-500 text-black'
                                        : 'bg-[#1C2028] border border-gray-700/50 hover:border-amber-500 text-gray-400 hover:text-amber-500'
                                )}
                            >
                                {item.icon && (
                                    <img
                                        src={item.icon}
                                        alt={item.label}
                                        className={cn('w-6 h-6 mb-2', selectedPrimary === item.label ? 'text-black' : 'text-amber-500')}
                                    />
                                )}
                                <span className="text-sm font-medium">{item.label}</span>
                            </motion.div>
                        ))}
                    </div>

                    {/* Secondary Menu */}
                    <ScrollArea className="w-full">
                        <div className="flex space-x-2 pb-4">
                            {selectedPrimary &&
                                secondaryMenuItems[selectedPrimary]?.map((item: string, index: number) => (
                                    <motion.div
                                        key={item}
                                        initial={{ opacity: 0, y: 20 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        transition={{ duration: 0.5, delay: index * 0.1 }}
                                    >
                                        <Button
                                            variant={item === selectedSecondary ? 'default' : 'outline'}
                                            onClick={() => {
                                                setSelectedSecondary(item)
                                                setCurrentPage(1)
                                            }}
                                            className={cn(
                                                'transition-all duration-200',
                                                item === selectedSecondary
                                                    ? 'bg-amber-500 text-black'
                                                    : 'border-gray-700 hover:border-amber-500 text-gray-400 hover:text-amber-500'
                                            )}
                                        >
                                            {item}
                                        </Button>
                                    </motion.div>
                                ))}
                        </div>
                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>

                    {/* Video Grid */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mt-8">
                        {paginatedVideos.map((video: Video) => (
                            <motion.div
                                key={video.id}
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: video.id * 0.1 }}
                            >
                                <div className="relative aspect-video bg-[#1C2028] border border-gray-700/50 rounded-lg overflow-hidden group">
                                    <video
                                        ref={(el) => {
                                            if (el) {
                                                videoRefs.current[video.id] = el
                                            }
                                        }}
                                        src={video.videoUrl}
                                        className="object-cover w-full h-full"
                                        preload="metadata"
                                        muted
                                        playsInline
                                    />
                                    {/* 添加试用车型标签 */}
                                    {token && accountStatus === '1' && video.status === 3 && (
                                        <Badge
                                            variant="secondary"
                                            className="text-[11px] px-1.5 py-0.5 absolute top-2 right-2 bg-amber-500/80 text-black z-10"
                                        >
                                            {translations[language].trialVehicle}
                                        </Badge>
                                    )}
                                    <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 transition-opacity">
                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="text-white hover:text-amber-500"
                                            onClick={() => handleVideoClick(video)}
                                        >
                                            {shouldRestrictAccess(video) ? (
                                                <Lock className="h-12 w-12" />
                                            ) : (
                                                <Play className="h-12 w-12" />
                                            )}
                                        </Button>
                                    </div>
                                </div>
                                <div className="mt-2 flex justify-between items-center">
                                    <h4 className="font-semibold text-white flex-1">{formatVideoTitle(video.title)}</h4>
                                    <p className="text-sm text-gray-400 ml-2">{video.duration}</p>
                                </div>
                                <div className="flex items-center gap-2 mt-1">
                                    {video.brandName && (
                                        <Badge
                                            variant="outline"
                                            className={cn(
                                                "bg-[#1C2028]/50 border-amber-500/50",
                                                shouldRestrictAccess(video) ? "text-gray-400" : "text-amber-500"
                                            )}
                                        >
                                            {video.brandName}
                                            {shouldRestrictAccess(video) && (
                                                <Lock className="ml-1 h-3 w-3" />
                                            )}
                                        </Badge>
                                    )}
                                </div>
                            </motion.div>
                        ))}
                    </div>

                    {/* Pagination */}
                    {totalPages > 1 && (
                        <div className="flex items-center justify-center gap-2 mt-8">
                            <Button
                                variant="ghost"
                                size="icon"
                                onClick={() => setCurrentPage((page) => Math.max(1, page - 1))}
                                disabled={currentPage === 1}
                                className="text-gray-400 hover:text-amber-500"
                            >
                                <ChevronLeft className="h-4 w-4" />
                            </Button>
                            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                                <Button
                                    key={page}
                                    variant={currentPage === page ? 'default' : 'ghost'}
                                    className={cn(
                                        'w-10 h-10 p-0 rounded-full',
                                        currentPage === page ? 'bg-amber-500 text-black' : 'text-gray-400 hover:text-amber-500'
                                    )}
                                    onClick={() => setCurrentPage(page)}
                                >
                                    {page}
                                </Button>
                            ))}
                            <Button
                                variant="ghost"
                                size="icon"
                                onClick={() => setCurrentPage((page) => Math.min(totalPages, page + 1))}
                                disabled={currentPage === totalPages}
                                className="text-gray-400 hover:text-amber-500"
                            >
                                <ChevronRight className="h-4 w-4" />
                            </Button>
                        </div>
                    )}
                </div>
            </div>

            <VideoPlayerModal
                isOpen={isVideoModalOpen}
                onClose={() => setIsVideoModalOpen(false)}
                video={selectedVideo ? (function () {
                    const videoForModal = {
                        id: selectedVideo.id,
                        title: selectedVideo.title || selectedVideo.videoName,
                        videoUrl: selectedVideo.videoUrl,
                        brandName: selectedVideo.brandName,
                        duration: selectedVideo.duration,
                        category: selectedVideo.category
                    };

                    // 确保srtUrl是有效的字符串再添加
                    if (selectedVideo.srtUrl && typeof selectedVideo.srtUrl === 'string') {
                        // @ts-ignore - 运行时添加属性
                        videoForModal.subtitleUrl = selectedVideo.srtUrl;
                    }

                    return videoForModal;
                })() : null}
            />
        </div>
    )
}
