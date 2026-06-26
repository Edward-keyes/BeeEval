'use client'

import { useEffect, useRef } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { X, Play, Info } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Portal } from '@/components/ui/portal'
import Hls from 'hls.js'
import Slider from 'react-slick'
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import './video-image-player-modal.css';
import trackingService from '@/services/tracking'

interface VideoPlayerModalProps {
    isOpen: boolean
    onClose: () => void
    data: {
        id: string | number,
        title: string
        category: string,
        videoUrl: string
        brandName: string
        duration: string,
        type: 'video' | 'picture',
        pictureUrl: Array<string>
    } | any
}

export function VideoImagePlayerModal({ isOpen, onClose, data }: VideoPlayerModalProps) {
    const modalRef = useRef<HTMLDivElement>(null)
    const videoRef = useRef<HTMLVideoElement>(null)
    const handlers = useRef<{ handlePlay?: () => void, handlePause?: () => void }>({})
    const isTrackingRef = useRef(false)

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (modalRef.current && !modalRef.current.contains(event.target as Node)) {
                handleClose();
            }
        }

        const handleEscape = (event: KeyboardEvent) => {
            if (event.key === 'Escape') {
                handleClose();
            }
        }

        if (isOpen) {
            document.addEventListener('mousedown', handleClickOutside)
            document.addEventListener('keydown', handleEscape)
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
            document.removeEventListener('keydown', handleEscape)
        }
    }, [isOpen, onClose])

    // 当模态框关闭时暂停视频
    useEffect(() => {
        if (!isOpen && videoRef.current) {
            videoRef.current.pause()
        }
    }, [isOpen])

    // 修复HLS视频加载逻辑，避免自动播放导致的报错
    useEffect(() => {
        let hls: Hls | null = null;

        if (isOpen && videoRef.current && data?.videoUrl && data?.type === 'video') {
            if (Hls.isSupported()) {
                hls = new Hls();
                hls.loadSource(data?.videoUrl || '');
                hls.attachMedia(videoRef.current);

                // 不在这里自动播放，避免AbortError
                // hls.on(Hls.Events.MANIFEST_PARSED, () => {
                //     videoRef.current?.play()
                // })
            } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
                // 对于 Safari 的原生 HLS 支持
                videoRef.current.src = data?.videoUrl || '';
            }
        }

        return () => {
            if (hls) {
                hls.destroy();
            }
        }
    }, [data?.videoUrl, isOpen, data?.type])

    // 使用recordVideoView进行视频行为跟踪，与VideoPlayerModal保持一致
    useEffect(() => {
        if (!isOpen || !data || data.type !== 'video' || !videoRef.current) {
            return;
        }

        try {
            // 获取视频信息
            const videoId = data.id?.toString() || '';
            const videoTitle = data.title || '未知视频';

            if (!videoId) {
                // console.warn('视频ID不存在，无法跟踪视频观看行为');
                return;
            }

            // console.log('设置视频跟踪:', videoId, videoTitle);

            // 使用recordVideoView获取处理函数，与VideoPlayerModal保持一致
            const trackingHandlers = trackingService.recordVideoView(videoId, videoTitle);

            // 保存到ref中以便在关闭时使用
            handlers.current.handlePlay = () => {
                // console.log('视频开始播放，开始跟踪');
                isTrackingRef.current = true;
                trackingHandlers.handlePlay();
            };

            handlers.current.handlePause = () => {
                // console.log('视频暂停或结束，结束跟踪');
                isTrackingRef.current = false;
                trackingHandlers.handlePause();
            };

            const videoElement = videoRef.current;

            // 添加事件监听器
            videoElement.addEventListener('play', handlers.current.handlePlay);
            videoElement.addEventListener('pause', handlers.current.handlePause);
            videoElement.addEventListener('ended', handlers.current.handlePause);

        } catch (error) {
            // console.error('设置视频跟踪时发生错误:', error);
        }

        // 清理函数
        return () => {
            if (videoRef.current && handlers.current.handlePlay && handlers.current.handlePause) {
                // console.log('清理视频跟踪事件监听器');

                try {
                    // 无论视频是否正在播放，都记录结束时间，确保数据被保存
                    // console.log('模态框关闭，记录视频观看结束');
                    // 只有之前触发过播放才需要记录结束
                    if (isTrackingRef.current) {
                        handlers.current.handlePause();
                    }
                } catch (e) {
                    // console.error('模态框关闭时记录视频观看结束失败:', e);
                }

                // 移除事件监听器
                videoRef.current.removeEventListener('play', handlers.current.handlePlay);
                videoRef.current.removeEventListener('pause', handlers.current.handlePause);
                videoRef.current.removeEventListener('ended', handlers.current.handlePause);

                // 清空处理函数
                handlers.current = {};
                isTrackingRef.current = false;
            }
        };
    }, [isOpen, data]);

    // 处理模态框关闭
    const handleClose = () => {
        // 如果正在跟踪视频，确保记录结束
        if (isTrackingRef.current && handlers.current.handlePause) {
            // console.log('关闭按钮点击，手动结束视频跟踪');
            handlers.current.handlePause();
            isTrackingRef.current = false;
        }

        onClose();
    };

    const settings = {
        dots: true,
        infinite: false,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1
    };

    return (
        <Portal>
            <AnimatePresence>
                {isOpen && data && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50 p-4"
                    >
                        <motion.div
                            ref={modalRef}
                            initial={{ scale: 0.95, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            exit={{ scale: 0.95, opacity: 0 }}
                            className="bg-[#1C2028] rounded-lg overflow-hidden w-full max-w-4xl"
                        >
                            {/* Header */}
                            <div className="flex justify-between items-center p-4 border-b border-gray-800">
                                <h3 className="text-lg font-semibold text-white">{data.title}</h3>
                                <Button variant="ghost" size="icon" onClick={handleClose} className="text-gray-400 hover:text-white">
                                    <X className="h-5 w-5" />
                                </Button>
                            </div>

                            {/* Video Player or Image Slider */}
                            <div className="relative aspect-video bg-black">
                                {data.type === 'video' ? (
                                    <video ref={videoRef} src={data.videoUrl} className="w-full h-full" controls autoPlay />
                                ) : (
                                    <div style={{ padding: '10px 0px 30px 0' }}>
                                        <Slider {...settings}>
                                            {data.pictureUrl.map((url: string, index: number) => (
                                                <div key={index} className="flex justify-center">
                                                    <img src={url} alt={`Slide ${index}`} style={{ margin: 'auto' }} />
                                                </div>
                                            ))}
                                        </Slider>
                                    </div>
                                )}
                            </div>

                            {/* Footer */}
                            <div className="p-4 flex justify-between items-center">
                                <div className="flex items-center gap-2">
                                    <Badge variant="secondary" className="bg-amber-500/10 text-amber-500 hover:bg-amber-500/20">
                                        {data.brandName}
                                    </Badge>
                                    {data.type == 'video' && (
                                        <span className="text-sm text-gray-400">{data.duration}</span>
                                    )}
                                </div>
                            </div>
                        </motion.div>
                    </motion.div>
                )}
            </AnimatePresence>
        </Portal>
    )
}
