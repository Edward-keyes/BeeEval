'use client'

import { useEffect, useRef, useState } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { X, Play, Info, ChevronLeft, ChevronRight } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import Hls from 'hls.js'
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

interface ComparisonModalProps {
    isOpen: boolean
    onClose: () => void
    currentVehicle: {
        brandModel: string
        description?: string
        functionLabel?: string[]
        videoNumber?: string | null
        videoStr?: string | null
        caseDataList?: {
            videoNumber: string
            functionLabel?: string[]
            materialType: number // 0: 图片, 1: 视频
        }[] | null
    }
    competitor: {
        description: string
        functionLabel: string[]
        videoNumber: string | null
        brandModel: string
        videoStr?: string | null
        caseDataList?: {
            videoNumber: string
            functionLabel?: string[]
            materialType: number // 0: 图片, 1: 视频
        }[] | null
    } | null
    isLoading?: boolean
}

export function ComparisonModal({ isOpen, onClose, currentVehicle, competitor, isLoading }: ComparisonModalProps) {
    const modalRef = useRef<HTMLDivElement>(null)
    // 多车型多视频切换索引
    const [caseIndexes, setCaseIndexes] = useState([0, 0]); // [当前车型, 对比车型]
    const videoRefs = useRef<(HTMLVideoElement | null)[]>([null, null])
    const [playingStates, setPlayingStates] = useState<boolean[]>([false, false])
    const vttUrlsRef = useRef<(string | null)[]>([null, null])
    const { language } = useLanguage()

    const vehicles: any = [
        {
            brandModel: currentVehicle.brandModel || '',
            description: currentVehicle.description || '暂无描述',
            functionLabel: currentVehicle.functionLabel || [],
            videoNumber: currentVehicle.videoNumber || null,
            videoStr: currentVehicle.videoStr || null,
            caseDataList: currentVehicle.caseDataList || undefined,
        },
        competitor ? {
            ...competitor,
            caseDataList: competitor.caseDataList || undefined,
        } : null
    ].filter(Boolean)

    // 获取视频列表
    const getVideoList = (vehicle: any) => Array.isArray(vehicle?.caseDataList) && vehicle.caseDataList.length > 0
        ? vehicle.caseDataList
        : vehicle?.videoNumber
            ? [{ videoNumber: vehicle.videoNumber, materialType: 1 }] // 默认为视频类型
            : [];

    // 清理Blob URLs
    useEffect(() => {
        return () => {
            vttUrlsRef.current.forEach(url => {
                if (url) URL.revokeObjectURL(url);
            });
        };
    }, []);

    useEffect(() => {
        // 切换车型或弹窗时重置caseIndexes
        setCaseIndexes([0, 0]);
    }, [isOpen, currentVehicle, competitor]);

    useEffect(() => {
        vehicles.forEach(async (vehicle: any, index: number) => {
            const videoList = getVideoList(vehicle);
            const currentCaseIndex = caseIndexes[index] || 0;
            if (videoList.length > 0 && videoRefs.current[index] && !playingStates[index] && videoList[currentCaseIndex].materialType === 1) {
                const video = videoRefs.current[index];
                video.crossOrigin = "anonymous";

                // 处理字幕
                const addSubtitles = async () => {
                    if (!video || !vehicle.videoStr) return;

                    try {
                        // 清除已有的字幕轨道
                        const existingTracks = video.querySelectorAll('track');
                        existingTracks.forEach(track => track.remove());

                        let trackSrc = vehicle.videoStr;

                        // 如果是SRT格式，转换为VTT - 检查URL是否包含SRT，忽略查询参数
                        const urlPath = vehicle.videoStr.split('?')[0];
                        if (urlPath.toLowerCase().endsWith('.srt')) {
                            console.log(`对比视频 ${index + 1} 检测到SRT格式字幕，开始转换`);
                            const vttUrl = await loadAndConvertSubtitle(vehicle.videoStr);
                            vttUrlsRef.current[index] = vttUrl;
                            trackSrc = vttUrl;
                        }

                        // 创建字幕轨道
                        const track = document.createElement('track');
                        track.kind = 'subtitles';
                        track.src = trackSrc;
                        track.srclang = 'zh';
                        track.label = '中文';
                        track.default = true;

                        // 添加到视频
                        video.appendChild(track);

                        // 确保字幕显示
                        requestAnimationFrame(() => {
                            if (video.textTracks.length > 0) {
                                // 先禁用所有轨道
                                Array.from(video.textTracks).forEach(track => {
                                    track.mode = 'disabled';
                                });
                                // 启用我们的轨道
                                video.textTracks[0].mode = 'showing';
                                console.log(`对比视频 ${index + 1} 字幕已启用`);
                            }
                        });
                    } catch (error) {
                        console.error(`对比视频 ${index + 1} 字幕加载失败:`, error);
                    }
                };

                if (Hls.isSupported()) {
                    const hls = new Hls({
                        enableWorker: true,
                        lowLatencyMode: false
                    });
                    hls.loadSource(videoList[currentCaseIndex].videoNumber);
                    hls.attachMedia(video);

                    if (vehicle.videoStr) {
                        hls.on(Hls.Events.MANIFEST_PARSED, () => {
                            addSubtitles();
                        });
                    }
                } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
                    video.src = videoList[currentCaseIndex].videoNumber;

                    if (vehicle.videoStr) {
                        video.addEventListener('loadedmetadata', () => {
                            addSubtitles();
                        }, { once: true });
                    }
                }
            }
        });
    }, [vehicles, playingStates, caseIndexes]);

    const handlePlayClick = (index: number) => {
        const video = videoRefs.current[index];
        const videoList = getVideoList(vehicles[index]);
        const currentCaseIndex = caseIndexes[index] || 0;

        if (video && videoList[currentCaseIndex]?.materialType === 1) {
            video.muted = false;
            video.volume = 0.5;
            video.play();
            setPlayingStates((prevStates) => {
                const newStates = [...prevStates];
                newStates[index] = true;
                return newStates;
            });
        }
    };

    if (!competitor) return null

    return (
        <AnimatePresence>
            {isOpen && (
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
                >
                    <motion.div
                        ref={modalRef}
                        initial={{ scale: 0.9, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        exit={{ scale: 0.9, opacity: 0 }}
                        className="bg-background rounded-lg p-6 w-[90vw] max-w-7xl max-h-[90vh] overflow-y-auto"
                    >
                        <div className="flex justify-between items-center mb-6">
                            <h2 className="text-2xl font-bold">车型对比</h2>
                            <Button size="icon" onClick={onClose}>
                                <X className="h-6 w-6" />
                            </Button>
                        </div>
                        <div className="grid md:grid-cols-2 gap-6">
                            {vehicles.map((vehicle: any, index: number) => {
                                const videoList = getVideoList(vehicle);
                                const currentCaseIndex = caseIndexes[index] || 0;
                                const functionLabels = Array.isArray(vehicle?.caseDataList) && vehicle.caseDataList.length > 0
                                    ? (vehicle.caseDataList[currentCaseIndex]?.functionLabel || [])
                                    : (vehicle.functionLabel || []);
                                return (
                                    <div key={index} className="space-y-4">
                                        <div className="relative aspect-video">
                                            {isLoading ? (
                                                <div className="absolute inset-0 bg-black/50 flex items-center justify-center rounded-lg">
                                                    <div className="flex items-center space-x-2">
                                                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse"></div>
                                                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-150"></div>
                                                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-300"></div>
                                                    </div>
                                                </div>
                                            ) : videoList.length > 0 ? (
                                                <>
                                                    {videoList[currentCaseIndex].materialType === 1 ? (
                                                        <video
                                                            ref={(el) => {
                                                                videoRefs.current[index] = el;
                                                            }}
                                                            className="w-full h-full object-cover rounded-lg"
                                                            playsInline
                                                            controls
                                                            crossOrigin="anonymous"
                                                        />
                                                    ) : (
                                                        <img
                                                            src={videoList[currentCaseIndex].videoNumber}
                                                            alt="功能展示"
                                                            className="w-full h-full object-cover rounded-lg"
                                                        />
                                                    )}
                                                    {videoList.length > 1 && (
                                                        <>
                                                            <div className="absolute bottom-3 left-1/2 -translate-x-1/2 z-10 select-none">
                                                                <div className="px-3 py-1 rounded-full bg-black/60 text-white text-xs font-medium">
                                                                    {`${currentCaseIndex + 1}/${videoList.length}`}
                                                                </div>
                                                            </div>
                                                            <button
                                                                onClick={() => setCaseIndexes(idx => { const arr = [...idx]; arr[index] = Math.max(0, arr[index] - 1); return arr; })}
                                                                disabled={currentCaseIndex === 0}
                                                                className="absolute left-3 top-1/2 -translate-y-1/2 z-10 h-10 w-10 rounded-full bg-white/90 text-black shadow hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                                                                aria-label="Previous"
                                                            >
                                                                <ChevronLeft className="h-6 w-6" />
                                                            </button>
                                                            <button
                                                                onClick={() => setCaseIndexes(idx => { const arr = [...idx]; arr[index] = Math.min(videoList.length - 1, arr[index] + 1); return arr; })}
                                                                disabled={currentCaseIndex === videoList.length - 1}
                                                                className="absolute right-3 top-1/2 -translate-y-1/2 z-10 h-10 w-10 rounded-full bg-white/90 text-black shadow hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                                                                aria-label="Next"
                                                            >
                                                                <ChevronRight className="h-6 w-6" />
                                                            </button>
                                                        </>
                                                    )}
                                                    {videoList[currentCaseIndex].materialType === 1 && !playingStates[index] && (
                                                        <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                                                            <button
                                                                onClick={() => handlePlayClick(index)}
                                                                className="h-16 w-16 rounded-full bg-primary/90 flex items-center justify-center text-white hover:bg-primary transition-colors"
                                                            >
                                                                <Play className="h-8 w-8" />
                                                            </button>
                                                        </div>
                                                    )}
                                                </>
                                            ) : (
                                                <div className="absolute inset-0 bg-black/50 flex items-center justify-center rounded-lg">
                                                    <p className="text-white text-lg">{translations[language]?.noFunctionConfiguration || "该车型不具备此功能配置"}</p>
                                                </div>
                                            )}
                                        </div>
                                        <div className="space-y-4">
                                            <div className="flex items-start gap-4">
                                                <div className="flex-1">
                                                    <h3 className="text-lg font-semibold text-foreground mb-2">{vehicle.brandModel}</h3>
                                                    <ul className="list-disc pl-5 space-y-2">
                                                        {functionLabels.map((item: string, idx: number) => (
                                                            <li key={idx} className="text-sm text-gray-400">
                                                                {item}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                                <div className="flex items-center gap-2 text-muted-foreground">
                                                    <Info className="h-4 w-4" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                )
                            })}
                        </div>
                    </motion.div>
                </motion.div>
            )}
        </AnimatePresence>
    )
}