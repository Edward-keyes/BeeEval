'use client'

import { useEffect, useRef } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { X, Play, Info, Download } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Portal } from '@/components/ui/portal'
import Hls from 'hls.js'
import trackingService from '@/services/tracking'
import { VideoProtection } from '@/components/video-protection'
import { useAuthStore } from '@/store/useAuthStore'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

// SRT转VTT格式的辅助函数
function convertSrtToVtt(srtContent: string): string {
    // 添加VTT标头
    let vttContent = 'WEBVTT\n\n';

    // 转换时间格式 (00:00:00,000 -> 00:00:00.000)
    vttContent += srtContent
        .replace(/(\d{2}:\d{2}:\d{2}),(\d{3})/g, '$1.$2')
        .replace(/^(\d+)$/gm, '');

    return vttContent;
}

// 从URL加载SRT文件并转换为VTT格式
async function loadAndConvertSubtitle(srtUrl: string): Promise<string> {
    try {
        console.log('正在加载字幕文件:', srtUrl);

        // 添加随机参数避免缓存问题
        const cacheBuster = `cache=${Date.now()}`;
        const url = srtUrl.includes('?') ? `${srtUrl}&${cacheBuster}` : `${srtUrl}?${cacheBuster}`;

        const response = await fetch(url, {
            credentials: 'omit',   // 避免发送cookies，减少跨域问题
            mode: 'cors',          // 启用CORS
            cache: 'no-cache',     // 避免缓存
            headers: {
                'Accept': 'text/plain, text/html, */*',  // 接受任何文本格式
            },
            // 添加更长的超时
            signal: AbortSignal.timeout(10000),  // 10秒超时
        });

        if (!response.ok) {
            throw new Error(`字幕加载失败: ${response.status} ${response.statusText}`);
        }

        const srtContent = await response.text();
        console.log('字幕文件加载成功，内容长度:', srtContent.length);

        if (!srtContent || srtContent.trim().length === 0) {
            throw new Error('字幕文件内容为空');
        }

        // 转换为VTT格式
        const vttContent = convertSrtToVtt(srtContent);
        console.log('SRT已转换为VTT格式，前50个字符:', vttContent.substring(0, 50));

        // 创建Blob URL
        const vttBlob = new Blob([vttContent], { type: 'text/vtt' });
        const vttUrl = URL.createObjectURL(vttBlob);
        console.log('已创建VTT Blob URL:', vttUrl);

        return vttUrl;
    } catch (error) {
        console.error('字幕处理失败:', error);
        // 返回一个空的VTT字幕(仅包含头部)，避免报错
        const emptyVtt = 'WEBVTT\n\n';
        const vttBlob = new Blob([emptyVtt], { type: 'text/vtt' });
        return URL.createObjectURL(vttBlob);
    }
}

interface VideoPlayerModalProps {
    isOpen: boolean
    onClose: () => void
    video: {
        id?: number
        videoId?: string
        title?: string
        videoName?: string
        duration?: string
        category?: string
        videoUrl?: string
        brandName?: string
        subtitleUrl?: string
        functionalDomain?: string
        threeTag?: string
        taskType?: string
        description?: string
    } | null
}

export function VideoPlayerModal({ isOpen, onClose, video }: VideoPlayerModalProps) {
    const modalRef = useRef<HTMLDivElement>(null)
    const videoRef = useRef<HTMLVideoElement>(null)
    const handlers = useRef<{ handlePlay?: () => void, handlePause?: () => void }>({})
    const isTrackingRef = useRef(false)
    const vttUrlRef = useRef<string | null>(null)
    const { accountStatus } = useAuthStore()
    const { language } = useLanguage()

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

    // 清理 Blob URL
    useEffect(() => {
        return () => {
            if (vttUrlRef.current) {
                URL.revokeObjectURL(vttUrlRef.current);
                vttUrlRef.current = null;
            }
        };
    }, []);

    // 初始化视频播放和字幕
    useEffect(() => {
        // 如果视频还未打开或没有视频URL，则退出
        if (!isOpen || !videoRef.current || !video?.videoUrl) return;

        // 确保视频元素有crossOrigin属性，以支持加载跨域字幕
        videoRef.current.crossOrigin = "anonymous";

        let hlsInstance: any = null;

        // 添加字幕处理函数
        const addSubtitles = async () => {
            if (!videoRef.current || !video?.subtitleUrl) return;

            try {
                console.log('开始处理字幕:', video.subtitleUrl);

                // 清除已有的字幕轨道
                const existingTracks = videoRef.current.querySelectorAll('track');
                existingTracks.forEach(track => track.remove());

                // 根据字幕URL和当前语言设置字幕语言
                // 检查字幕URL是否包含语言标识
                const urlPath = video.subtitleUrl.split('?')[0];
                const isEnglishSubtitle = urlPath.toLowerCase().includes('-en.') || language === 'en';
                const srclang = isEnglishSubtitle ? 'en' : 'zh';
                const label = isEnglishSubtitle ? 'English' : '中文';

                // 将SRT转换为VTT - 检查URL是否包含SRT，忽略查询参数
                if (urlPath.toLowerCase().endsWith('.srt')) {
                    console.log('检测到SRT格式字幕，开始转换');
                    const vttUrl = await loadAndConvertSubtitle(video.subtitleUrl);
                    vttUrlRef.current = vttUrl; // 保存引用以便清理

                    // 创建新的字幕轨道
                    const track = document.createElement('track');
                    track.kind = 'subtitles';
                    track.src = vttUrl;
                    track.srclang = srclang;
                    track.label = label;
                    track.default = true;

                    // 添加到视频
                    videoRef.current.appendChild(track);

                    // 强制显示字幕
                    requestAnimationFrame(() => {
                        if (videoRef.current && videoRef.current.textTracks.length > 0) {
                            // 先禁用所有轨道
                            Array.from(videoRef.current.textTracks).forEach(track => {
                                track.mode = 'disabled';
                            });
                            // 然后启用我们的轨道
                            videoRef.current.textTracks[0].mode = 'showing';
                            console.log(`字幕已设置为显示状态 (VTT - ${label})`);
                        }
                    });
                } else {
                    // 直接使用原始字幕（假设是VTT格式）
                    const track = document.createElement('track');
                    track.kind = 'subtitles';
                    track.src = video.subtitleUrl;
                    track.srclang = srclang;
                    track.label = label;
                    track.default = true;

                    videoRef.current.appendChild(track);

                    requestAnimationFrame(() => {
                        if (videoRef.current && videoRef.current.textTracks.length > 0) {
                            videoRef.current.textTracks[0].mode = 'showing';
                            console.log(`字幕已设置为显示状态 (原始 - ${label})`);
                        }
                    });
                }
            } catch (error) {
                console.error('字幕加载失败:', error);
            }
        };

        // HLS视频加载
        if (Hls.isSupported()) {
            console.log('使用HLS播放:', video.videoUrl);
            hlsInstance = new Hls({
                enableWorker: true,
                lowLatencyMode: false
            });
            hlsInstance.loadSource(video.videoUrl);
            hlsInstance.attachMedia(videoRef.current);

            hlsInstance.on(Hls.Events.MANIFEST_PARSED, () => {
                console.log('HLS流已加载完成');
                addSubtitles();
            });

            hlsInstance.on(Hls.Events.ERROR, (event: any, data: any) => {
                console.error('HLS错误:', data.type, data.details);
            });
        } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
            // Safari原生HLS支持
            console.log('使用原生HLS播放:', video.videoUrl);
            videoRef.current.src = video.videoUrl;
            videoRef.current.addEventListener('loadedmetadata', () => addSubtitles(), { once: true });
        }

        // 清理函数
        return () => {
            if (hlsInstance) {
                hlsInstance.destroy();
            }

            if (videoRef.current) {
                videoRef.current.removeEventListener('loadedmetadata', () => addSubtitles());
            }

            // 清理VTT URL
            if (vttUrlRef.current) {
                URL.revokeObjectURL(vttUrlRef.current);
                vttUrlRef.current = null;
            }
        };
    }, [isOpen, video?.videoUrl, video?.subtitleUrl]);

    // 使用recordVideoView进行视频行为跟踪
    useEffect(() => {
        if (!isOpen || !video || !videoRef.current) {
            return;
        }

        try {
            // 获取视频信息
            const videoId = video.videoId?.toString() || '';
            const videoTitle = video.title || video.videoName || '未知视频';
            // if (!videoId) {

            //     // console.warn('视频ID不存在，无法跟踪视频观看行为');
            //     return;
            // }

            // console.log("视频数据" + JSON.stringify(video, null, 2));

            // console.log('设置视频跟踪:', videoId, videoTitle);

            // 使用recordVideoView获取处理函数
            const trackingHandlers = trackingService.recordVideoView(videoId, videoTitle + " " + video.category);

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
    }, [isOpen, video]);

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

    const handleDownload = () => {
        if (video?.videoUrl) {
            const link = document.createElement('a')
            link.href = video.videoUrl
            link.download = `${video.title || 'video'}.mp4`
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        }
    }

    return (
        <Portal>
            <AnimatePresence>
                {isOpen && video && (
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
                                <div className="flex items-center gap-3 flex-1">
                                    <h3 className="text-lg font-semibold text-white">{video.title || video.videoName}</h3>
                                    <div className="flex items-center gap-2 flex-wrap">
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
                                <Button variant="ghost" size="icon" onClick={handleClose} className="text-gray-400 hover:text-white">
                                    <X className="h-5 w-5" />
                                </Button>
                            </div>

                            {/* Video Player */}
                            <div className="relative aspect-video bg-black">
                                <VideoProtection>
                                    {video.videoUrl ? (
                                        <video
                                            ref={videoRef}
                                            className="w-full h-full"
                                            controls
                                            autoPlay
                                            crossOrigin="anonymous" // 添加跨域支持，解决字幕可能的跨域问题
                                        >
                                            {video.subtitleUrl && (
                                                <track
                                                    kind="subtitles"
                                                    src={video.subtitleUrl}
                                                    srcLang="zh"
                                                    label="中文"
                                                    default
                                                />
                                            )}
                                        </video>
                                    ) : (
                                        <div className="absolute inset-0 flex items-center justify-center bg-[#1C2028]">
                                            <div className="flex flex-col items-center justify-center gap-4" style={{ marginTop: '500px' }}>
                                                <div className="w-20 h-20 rounded-full bg-amber-500/10 flex items-center justify-center">
                                                    <Play className="h-10 w-10 text-amber-500" />
                                                </div>
                                                <div className="text-center space-y-2">
                                                    <p className="text-xl font-semibold text-white">{translations[language]?.videoNotUploaded || "视频暂未上传"}</p>
                                                    <p className="text-gray-400">{translations[language]?.demoVideoInPreparation || "该功能的演示视频正在准备中"}</p>
                                                </div>
                                            </div>
                                        </div>
                                    )}
                                </VideoProtection>
                            </div>

                            {/* Footer */}
                            <div className="p-4 space-y-3">
                                <div className="flex justify-between items-center">
                                    <div className="flex items-center gap-2">
                                        {video.brandName && (
                                            <Badge variant="secondary" className="bg-amber-500/10 text-amber-500 hover:bg-amber-500/20">
                                                {video.brandName}
                                            </Badge>
                                        )}
                                        {video.description && (
                                            <p className="text-sm text-gray-300">{video.description}</p>
                                        )}
                                        {video.duration && (
                                            <span className="text-sm text-gray-400">{video.duration}</span>
                                        )}
                                    </div>
                                    {accountStatus !== '1' && accountStatus == '3' && video.videoUrl && (
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="text-amber-500 hover:text-amber-400 hover:bg-amber-500/10"
                                            onClick={handleDownload}
                                        >
                                            <Download className="h-4 w-4 mr-2" />
                                            下载视频
                                        </Button>
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
