'use client'

import { useState, useMemo, useRef, useEffect } from 'react'
import { motion } from 'framer-motion'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Check } from 'lucide-react'
import { Badge } from '@/components/ui/badge'
import { X } from 'lucide-react'
import { VideoPlayerModal } from '@/components/video-player-modal'
import { VideoImagePlayerModal } from '@/components/video-image-player-modal'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { cn } from '@/lib/utils'
import { useAuthStore } from '@/store/useAuthStore'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { toast } from 'react-hot-toast'
import tracker from '@/services/tracking'
import { Video } from '@/types/video'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import Image from 'next/image'
import { Button } from '@/components/ui/button'

import http from '@/utils/request'

// 定义域组类型
type MainDomain = string
type DomainGroups = {
    [key: string]: string[]
}

// 添加排序类型定义
type SortField = 'domain' | 'mainDomain' | 'count' | 'penetration'

interface SortConfig {
    field: SortField
    direction: 'asc' | 'desc'
}

// 添加车辆数据接口类型
interface VehicleInfo {
    id: string
    brandModel: string
    testDate: string
    vehicleVersion: string
    vehicleSystemVersion: string
    status: number  // 添加 status 字段
}

// 更新域数据类型
interface DomainData {
    domain: string
    mainDomain: MainDomain
    penetration: number
    count: number
}

interface BrandData {
    brand: string
    domains: {
        [key: string]: number // 域名到渗透率的映射
    }
}

// 添加评估类型定义
type EvaluationStatus = 'Good' | 'Avg' | 'Mod' | 'n.a.'

// 添加类型定义
interface VehicleTagRatio {
    brandModel: string
    vehicleId: string
    tagRatio: number
}

interface OneTagData {
    oneTagName: string
    oneId: string
    vehicleTagRatio: VehicleTagRatio[]
}

// 添加新的类型定义
interface VehicleFunctionGrade {
    vehicleName: string
    vehicleId: number
    functionList: string
}

interface ThreeFunctionGradeVo {
    threeTagName: string
    threeTagId: number
    vehiclefunctionGrade: VehicleFunctionGrade[]
}

interface TwoFunctionGradeResponse {
    twoTagName: string
    twoTagId: number
    threeFunctionGradeVos: ThreeFunctionGradeVo[]
}

interface FunctionGradeData {
    oneTagName: string
    oneTagId: string
    vehicleFunctionTwoGradeResponses: TwoFunctionGradeResponse[]
}

// 旧的FunctionGradeData类型定义 - 重命名为OldFunctionGradeData
interface OldFunctionGradeData {
    twoTagName: string
    twoTagId: number
    threeFunctionGradeVos: ThreeFunctionGradeVo[]
    // 扩展字段，用于存储一级标签信息
    oneTagName?: string
    oneTagId?: string
}

// 修改状态映射函数
const mapFunctionListToStatus = (functionList: string): EvaluationStatus => {
    switch (functionList) {
        case '1':
            return 'Good' // 1 对应 Good
        case '2':
            return 'Avg' // 2 对应 Avg
        case '3':
            return 'Mod' // 3 对应 Mod
        case '4':
            return 'n.a.' // 4 对应 n.a.
        default:
            return 'n.a.' // 其他情况返回 n.a.
    }
}

// 修改map值到评估状态的函数
const mapValueToStatus = (status: number): EvaluationStatus => {
    if (status === 1) return 'Good' // 1 对应 Good
    if (status === 2) return 'Avg' // 2 对应 Avg
    if (status === 3) return 'Mod' // 3 对应 Mod
    return 'n.a.' // 其他值都对应 n.a.
}

// 添加评估状态单元格组件
const EvaluationCell = ({
    evaluation,
    model,
    data,
    threeTagId,
    functionName
}: {
    evaluation: { status: EvaluationStatus; videoUrl?: string }
    model: string
    data: any
    threeTagId: string | number
    functionName?: string
}) => {
    const [isVideoModalOpen, setIsVideoModalOpen] = useState(false)
    const [videoUrl, setVideoUrl] = useState('')
    const [videoId, setVideoId] = useState('')
    const [pictureUrl, setPictureUrl] = useState('')
    const [type, setType] = useState('')
    const [isPictureModalOpen, setIsPictureModalOpen] = useState(false)
    const [currentImageIndex, setCurrentImageIndex] = useState(0)
    const [subtitleUrl, setSubtitleUrl] = useState<string | undefined>(undefined)
    const { language } = useLanguage()

    const handleNextImage = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex + 1) % (pictureUrl.length || 1))
    }

    const handlePrevImage = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex - 1 + (pictureUrl.length || 1)) % (pictureUrl.length || 1))
    }

    const getStatusColor = (status: EvaluationStatus) => {
        switch (status) {
            case 'Good':
                return 'bg-green-500/10 text-green-500 hover:bg-green-500/20'
            case 'Avg':
                return 'bg-yellow-500/10 text-yellow-500 hover:bg-yellow-500/20'
            case 'Mod':
                return 'bg-blue-500/10 text-blue-500 hover:bg-blue-500/20'
            default:
                return 'bg-gray-500/10 text-gray-500'
        }
    }
    async function open() {
        if (evaluation.status !== 'n.a.') {
            const response: any = await http.post<OneTagData[]>(
                '/ware/functiontree/queryVideoOrPictureByThreeTagIdAndVehicleId',
                {
                    threeTagIds: String(threeTagId),
                    vehicleIds: data.vehicleId,
                    language: language
                }
            )
            if (response.data.type == 'video') {
                setVideoUrl(response.data.videoUrl)
                setVideoId(response.data.videoId)
                setSubtitleUrl(response.data.srtUrl)
                setIsVideoModalOpen(true)
            } else {
                setPictureUrl(response.data.pictureUrl)
                setCurrentImageIndex(0) // Reset index when opening
                setIsPictureModalOpen(true)
            }
            setType(response.data.type)
        }
    }

    // 添加鼠标悬停处理函数
    const handleMouseEnter = (e: React.MouseEvent) => {
        if (evaluation.status === 'n.a.') return;

        // 添加高亮类到按钮
        e.currentTarget.classList.add('highlight-model-btn');

        // 找到包含按钮的单元格和行
        const buttonCell = e.currentTarget.closest('td');
        const row = buttonCell?.closest('tr');

        if (!row) return;

        // 添加高亮效果到当前行
        row.classList.add('row-highlight');
        row.classList.add('highlight-function');

        // 获取行的数据属性
        const domainGroup = row.getAttribute('data-domain-group');
        const functionGroup = row.getAttribute('data-function-group');

        // 高亮当前行的三级功能单元格（第三列）
        const tertiaryCell = row.querySelector('td:nth-child(3)');
        if (tertiaryCell) {
            tertiaryCell.classList.add('highlight-function');
        }

        // 高亮功能域单元格
        if (domainGroup) {
            document.querySelectorAll(`.domain-cell[data-domain-id="${domainGroup.replace('domain-group-', 'domain-')}"]`).forEach(cell => {
                cell.classList.add('highlight-domain');
            });
        }

        // 高亮二级功能单元格
        if (functionGroup) {
            document.querySelectorAll(`.secondary-function[data-function-id="${functionGroup.replace('function-group-', 'function-')}"]`).forEach(cell => {
                cell.classList.add('highlight-function');
            });
        }
    }

    const handleMouseLeave = (e: React.MouseEvent) => {
        // 移除按钮高亮
        e.currentTarget.classList.remove('highlight-model-btn');

        // 获取行元素
        const row = e.currentTarget.closest('tr');
        if (row) {
            row.classList.remove('row-highlight');
        }

        // 恢复所有高亮的单元格
        document.querySelectorAll('.highlight-domain').forEach(el => {
            el.classList.remove('highlight-domain');
        });
        document.querySelectorAll('.highlight-function').forEach(el => {
            el.classList.remove('highlight-function');
        });
    }

    return (
        <>
            <button
                onClick={open}
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}
                className={`evaluation-btn px-3 py-1 rounded-full text-sm font-medium transition-all duration-200 ${getStatusColor(
                    evaluation.status
                )} ${evaluation.status !== 'n.a.' ? 'hover:scale-105 cursor-pointer' : 'cursor-default'}`}
                title={evaluation.status !== 'n.a.' ? `查看 ${model} 的${functionName || '功能'}评估` : ''}
            >
                {evaluation.status}
            </button>
            {evaluation.status !== 'n.a.' && (
                <VideoPlayerModal
                    isOpen={isVideoModalOpen}
                    onClose={() => setIsVideoModalOpen(false)}
                    video={{
                        id: 1,
                        videoId: videoId, // 添加videoId字段用于跟踪
                        title: type == 'video' ? translations[language].evaluationVideo : translations[language].evaluationImage,
                        category: translations[language].evaluation,
                        videoUrl: type === 'video' ? videoUrl : undefined,
                        brandName: model, // 使用传入的当前车型
                        subtitleUrl: subtitleUrl, // 添加字幕URL属性
                    }}
                />
            )}
            {/* 图片模态框 */}
            <Dialog open={isPictureModalOpen} onOpenChange={setIsPictureModalOpen}>
                <DialogContent className="sm:max-w-4xl bg-[#1C2028] border-gray-800 p-6 rounded-lg overflow-hidden">
                    <DialogHeader>
                        <DialogTitle className="text-lg font-semibold text-white mb-4">功能图片</DialogTitle>
                    </DialogHeader>
                    {/* Image Content */}
                    <div className="relative aspect-video bg-black flex items-center justify-center rounded-md overflow-hidden">
                        {pictureUrl && pictureUrl.length > 0 ? (
                            <Image
                                src={pictureUrl[currentImageIndex]}
                                alt="Feature Analysis"
                                fill
                                className="object-contain"
                                unoptimized
                            />
                        ) : (
                            <div className="text-gray-500">图片加载失败或无图片</div>
                        )}
                        {/* Navigation Buttons */}
                        {pictureUrl && pictureUrl.length > 1 && (
                            <>
                                <button onClick={handlePrevImage} className="absolute left-0 p-2 text-white bg-black/50 hover:bg-black/70 rounded-full">
                                    &lt;
                                </button>
                                <button onClick={handleNextImage} className="absolute right-0 p-2 text-white bg-black/50 hover:bg-black/70 rounded-full">
                                    &gt;
                                </button>
                            </>
                        )}
                    </div>
                    {/* Image Indicators */}
                    {pictureUrl && pictureUrl.length > 1 && (
                        <div className="flex justify-center mt-4">
                            {pictureUrl.map((_, index) => (
                                <div
                                    key={index}
                                    className={`w-2 h-2 mx-1 rounded-full ${index === currentImageIndex ? 'bg-amber-500' : 'bg-gray-500'}`}
                                />
                            ))}
                        </div>
                    )}
                    {/* Footer */}
                    <div className="p-4 border-t border-gray-800 mt-4">
                        <Badge variant="outline" className="border-amber-500 text-amber-500">{model}</Badge>
                    </div>
                </DialogContent>
            </Dialog>
        </>
    )
}

interface PenetrationRateResponse {
    penetrationRateList: any[];
    vehicleCount: number;
}

const VideoPlayer = ({ video }: { video: Video }) => {
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

export default function FeatureAnalysisPage() {
    const { language } = useLanguage()
    const { token, accountStatus } = useAuthStore()
    const [selectedDomains, setSelectedDomains] = useState<string[]>([translations[language].allFunctionDomains])
    const [domainSelectOpen, setDomainSelectOpen] = useState(false)
    const [selectedBrands, setSelectedBrands] = useState<string[]>([translations[language].allModels])
    const [brandSelectOpen, setBrandSelectOpen] = useState(false)
    const [selectedEvalBrands, setSelectedEvalBrands] = useState<string[]>([])
    const [isEvalSelectOpen, setIsEvalSelectOpen] = useState(false)
    const [selectedEvalDomains, setSelectedEvalDomains] = useState<string[]>([translations[language].allFunctionDomains])
    const [evalDomainSelectOpen, setEvalDomainSelectOpen] = useState(false)
    const [selectedFirstLevel, setSelectedFirstLevel] = useState<string[]>([])
    const [selectedSecondLevel, setSelectedSecondLevel] = useState<string[]>([])
    const [selectedTime, setSelectedTime] = useState<string>('all')
    const [selectedVehicleTypes, setSelectedVehicleTypes] = useState<string[]>(['all'])
    const [selectedScoreRange, setSelectedScoreRange] = useState<string>('all')
    const [sortConfig, setSortConfig] = useState<SortConfig>({ field: 'penetration', direction: 'desc' })
    const [domainGroups, setDomainGroups] = useState<DomainGroups>({})
    const [domainOptions, setDomainOptions] = useState<string[]>([])
    const [domainData, setDomainData] = useState<DomainData[]>([])
    // Add refs for dropdown containers
    const domainSelectRef = useRef<HTMLDivElement>(null)
    const brandSelectRef = useRef<HTMLDivElement>(null)
    const evalDomainSelectRef = useRef<HTMLDivElement>(null)
    const evalBrandSelectRef = useRef<HTMLDivElement>(null)
    const [vehicleCount, setVehicleCount] = useState<number>(0)
    // 在组件内添加状态
    const [carOptions, setCarOptions] = useState<string[]>([])
    const [vehicleData, setVehicleData] = useState<VehicleInfo[]>([])

    // 添加状态
    const [functionGradeData, setFunctionGradeData] = useState<OldFunctionGradeData[]>([])
    const [heatmapData, setHeatmapData] = useState<BrandData[]>([])
    const [queryAllOneTag, setQueryAllOneTag] = useState<any[]>([])

    const [isLoading, setIsLoading] = useState(true)

    // 添加活动标签页状态
    const [activeTab, setActiveTab] = useState("heatmap");

    // 添加数据转换和获取的函数
    const fetchHeatmapData = async () => {
        try {
            setIsLoading(true)
            const response = await http.post<OneTagData[]>('/ware/functiontree/queryFirstLevelTagRatio', {
                ids: [],
                language: language
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

            // 1. 获取所有不重复的车型
            const allBrands = Array.from(
                new Set(response.data.flatMap((item) => item.vehicleTagRatio.map((ratio) => ratio.brandModel)))
            )

            // 2. 转换数据格式
            const formattedData: BrandData[] = allBrands.map((brand) => {
                const domains: { [key: string]: number } = {}

                response.data.forEach((tag) => {
                    const brandRatio = tag.vehicleTagRatio.find((ratio) => ratio.brandModel === brand)
                    domains[tag.oneTagName] = brandRatio?.tagRatio || 0
                })

                return {
                    brand,
                    domains
                }
            })

            setHeatmapData(formattedData)
            setSelectedBrands([translations[language].allModels])
        } catch (error) {
            // console.error('获取热力图数据失败:', error)
            toast.error('获取热力图数据失败')
        } finally {
            setIsLoading(false)
        }
    }

    // 修改获取评估数据的函数
    const fetchFunctionGradeData = async () => {
        try {
            setIsLoading(true)
            const response = await http.post<any>('/ware/functiontree/queryVehicleFunctionGrade', {
                oneTagIds: [],
                vehicleIds: [],
                language: language
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

            // 处理新的API响应格式
            const oldFormatData: OldFunctionGradeData[] = [];

            // 转换新格式数据为旧格式
            if (response.data && Array.isArray(response.data)) {
                response.data.forEach(oneTagData => {
                    // 记录一级标签名称，用于后续映射
                    const oneTagName = oneTagData.oneTagName;
                    const oneTagId = oneTagData.oneTagId;

                    // 遍历二级标签数据并转换
                    if (oneTagData.vehicleFunctionTwoGradeResponses && Array.isArray(oneTagData.vehicleFunctionTwoGradeResponses)) {
                        oneTagData.vehicleFunctionTwoGradeResponses.forEach(twoTagData => {
                            // 将二级标签数据添加到旧格式数组中
                            oldFormatData.push({
                                twoTagName: twoTagData.twoTagName,
                                twoTagId: twoTagData.twoTagId,
                                threeFunctionGradeVos: twoTagData.threeFunctionGradeVos,
                                // 添加一级标签信息到二级标签结构中
                                oneTagName: oneTagName,
                                oneTagId: oneTagId
                            });
                        });
                    }
                });
            }

            setFunctionGradeData(oldFormatData)
        } catch (error) {
            // console.error('获取功能等级数据失败:', error)
            toast.error('获取功能等级数据失败')
            setFunctionGradeData([])
        } finally {
            setIsLoading(false)
        }
    }

    // 修改函数签名，支持功能域参数（string或string[]）
    const queryVehicleFunctionGradetDate = async (ids: any, domains?: string | string[]) => {
        try {
            setIsLoading(true);

            // 在函数体内处理功能域逻辑
            // 如果提供了domains参数，则根据domains获取完整的功能域数据
            let finalIds = ids;

            // 如果提供了domains参数且ids为空，表示需要获取整个功能域的数据
            if (domains && (!ids || ids.length === 0)) {
                if (typeof domains === 'string' && domains !== translations[language].allFunctionDomains) {
                    // 处理单个域名，获取该域名下所有功能ID
                    finalIds = getIdsByTags([domains], queryAllOneTag, true);
                } else if (Array.isArray(domains) && domains.length > 0) {
                    // 处理多个域名，获取多个域名下所有功能ID
                    finalIds = getIdsByTags(domains, queryAllOneTag, true);
                }
            }

            // 继续原有的数据获取逻辑
            const response = await http.post<any>('/ware/functiontree/queryVehicleFunctionGrade', {
                oneTagIds: finalIds, // 使用处理后的ID
                vehicleIds: [],
                language: language
            });

            // 检查 token 是否失效
            if (response.code === 11012) {
                handleTokenExpired();
                return;
            }
            if (response.code === 11014) {
                handleTokenReplaced();
                return;
            }

            // 成功获取数据 - 需要转换成旧格式以保持兼容性
            const oldFormatData: OldFunctionGradeData[] = [];

            // 转换新格式数据为旧格式
            if (response.data && Array.isArray(response.data)) {
                response.data.forEach((oneTagData: any) => {
                    // 记录一级标签名称，用于后续映射
                    const oneTagName = oneTagData.oneTagName;
                    const oneTagId = oneTagData.oneTagId;

                    // 遍历二级标签数据并转换
                    if (oneTagData.vehicleFunctionTwoGradeResponses && Array.isArray(oneTagData.vehicleFunctionTwoGradeResponses)) {
                        oneTagData.vehicleFunctionTwoGradeResponses.forEach((twoTagData: any) => {
                            // 将二级标签数据添加到旧格式数组中
                            oldFormatData.push({
                                twoTagName: twoTagData.twoTagName,
                                twoTagId: twoTagData.twoTagId,
                                threeFunctionGradeVos: twoTagData.threeFunctionGradeVos,
                                // 添加一级标签信息到二级标签结构中
                                oneTagName: oneTagName,
                                oneTagId: oneTagId
                            });
                        });
                    }
                });
            }

            // 更新状态
            setFunctionGradeData(oldFormatData);
        } catch (error) {
            // 处理错误
            // console.error('获取车型功能评分数据失败:', error)
            setFunctionGradeData([]);
        } finally {
            setIsLoading(false);
        }
    }

    // 发起网络请求
    const fetchData = async () => {
        try {
            const response = await http.post<any[]>('/ware/functiononetag/queryAllOneTag', {
                language: language
            })

            // 转换数据格式
            const groups: DomainGroups = {}
            const options: string[] = []
            const optionsID: string[] = []
            response.data.forEach((group) => {
                // 添加到 domainOptions
                options.push(group.oneTagName)
                optionsID.push(group.oneId)
                // 转换三级标签数组
                groups[group.oneTagName] = group.threeDatas.map((item: any) => item.threeTagName)
            })

            setDomainGroups(groups)
            setDomainOptions(options)
            setQueryAllOneTag(response.data)

            // 更新选中的域（如果当前选中的域不在新数据中）
            setSelectedDomains((prev) => {
                if (prev.includes(translations[language].allFunctionDomains)) return prev
                const validDomains = prev.filter((domain) => options.includes(domain))
                return validDomains.length ? validDomains : [translations[language].allFunctionDomains]
            })
        } catch (error) {
            // console.error('Error fetching data:', error)
        }
    }

    const fetchPenetrationRate = async (ids: string[]) => {
        try {
            setIsLoading(true)
            const response = await http.post<PenetrationRateResponse>('/ware/functiontree/queryPenetrationRate', {
                ids,
                language: language
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

            const list = response.data.penetrationRateList
            // 使用从 API 获取的数据
            setDomainData(list)
            setVehicleCount(response.data.vehicleCount)
            setSelectedDomains([translations[language].allFunctionDomains])
        } catch (error) {
            // console.error('获取渗透率数据失败:', error)
            toast.error('获取渗透率数据失败')
        } finally {
            setIsLoading(false)
        }
    }

    // 所有车辆
    const fetchDataAllVehicle = async () => {
        try {
            const response = await http.post<{ data: VehicleInfo[] }>('/ware/baseinfo/allVehicle', {
                language: language
            })
            const vehicles: any = response.data
            setVehicleData(vehicles)

            // 根据账号类型和车辆状态过滤车型
            const filteredVehicles = vehicles.filter((vehicle: VehicleInfo) => {
                // status为2的车型永远不显示
                if (vehicle.status === 2) return false;

                // 如果是试用账号，只显示试用车型（status === 3）
                if (token && accountStatus === '1') {
                    return vehicle.status === 3;
                }

                // 如果是正式账号，显示status为1和3的车型
                return vehicle.status === 1 || vehicle.status === 3;
            });

            const vehicleNames = filteredVehicles.map((vehicle: VehicleInfo) => vehicle.brandModel)
            setCarOptions(vehicleNames)

            // 如果当前没有选中的车型，根据车型数量设置默认选择
            if (selectedEvalBrands.length === 0 && vehicleNames.length > 0) {
                if (vehicleNames.length >= 2) {
                    // 当有两辆或以上车型时，选中前两个
                    setSelectedEvalBrands([vehicleNames[2], vehicleNames[3]])
                } else {
                    // 当只有一辆车时，选中第一个
                    setSelectedEvalBrands([vehicleNames[2]])
                }
            }
        } catch (error) {
            // console.error('Error fetching data:', error)
        }
    }

    // 监听语言变化，重新获取数据
    useEffect(() => {
        // 页面首次加载和语言变化时获取数据
        fetchData()
        fetchHeatmapData()
        fetchFunctionGradeData()
        fetchDataAllVehicle()
        fetchPenetrationRate([])
    }, [language])

    // 初始数据加载和监听外部点击
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (domainSelectRef.current && !domainSelectRef.current.contains(event.target as Node)) {
                setDomainSelectOpen(false)
            }
            if (brandSelectRef.current && !brandSelectRef.current.contains(event.target as Node)) {
                setBrandSelectOpen(false)
            }
            if (evalDomainSelectRef.current && !evalDomainSelectRef.current.contains(event.target as Node)) {
                setEvalDomainSelectOpen(false)
            }
            if (evalBrandSelectRef.current && !evalBrandSelectRef.current.contains(event.target as Node)) {
                setIsEvalSelectOpen(false)
            }
        }

        document.addEventListener('mousedown', handleClickOutside)
        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
        }
    }, [])

    // 监听语言变化，仅当激活选项卡是evaluation(功能详情)时重新加载数据
    useEffect(() => {
        // 仅当在"功能详情"选项卡时才重新加载数据
        if (activeTab === 'evaluation') {
            // 重置选择的功能域和车型为默认值
            setSelectedEvalDomains([translations[language].allFunctionDomains]);

            // 如果有车型选项，选择前两个车型
            if (carOptions && carOptions.length > 0) {
                if (carOptions.length >= 2) {
                    // 当有两辆或以上车型时，选中前两个
                    setSelectedEvalBrands([carOptions[2], carOptions[3]]);
                } else {
                    // 当只有一辆车时，选中第一个
                    setSelectedEvalBrands([carOptions[2]]);
                }
            }

            // 重置其他筛选条件
            setSelectedFirstLevel([]);
            setSelectedSecondLevel([]);

            // 重新加载功能详情数据
            queryVehicleFunctionGradetDate([], translations[language].allFunctionDomains);
        }
    }, [language, activeTab, carOptions]);

    // 修改过滤评估数据的函数
    const filteredEvalData = useMemo(() => {
        // 首先检查是否有数据
        if (!functionGradeData || functionGradeData.length === 0) {
            return [];
        }

        // 创建一个映射，根据二级功能查找其对应的功能域
        const domainMap: Record<string, string> = {};

        try {
            // 从functionGradeData中提取二级标签到一级标签的映射
            functionGradeData.forEach(item => {
                if ((item as any).oneTagName && item.twoTagName) {
                    domainMap[item.twoTagName] = (item as any).oneTagName;
                }
            });

            // 如果domainMap为空，则尝试从queryAllOneTag中提取
            if (Object.keys(domainMap).length === 0) {
                // 从queryAllOneTag中提取twoTagId到oneTagName的映射
                // 首先遍历所有一级功能域
                queryAllOneTag.forEach(oneTag => {
                    if (oneTag.oneTagName) {
                        // 检查该功能域下的二级功能
                        if (oneTag.twoTags && Array.isArray(oneTag.twoTags)) {
                            oneTag.twoTags.forEach((twoTag: any) => {
                                if (twoTag.twoTagName) {
                                    domainMap[twoTag.twoTagName] = oneTag.oneTagName;
                                }
                            });
                        }

                        // 有些接口返回的字段名可能是twoDatas
                        if (oneTag.twoDatas && Array.isArray(oneTag.twoDatas)) {
                            oneTag.twoDatas.forEach((twoTag: any) => {
                                if (twoTag.twoTagName) {
                                    domainMap[twoTag.twoTagName] = oneTag.oneTagName;
                                }
                            });
                        }
                    }
                });
            }

            // 如果domainMap仍为空，则使用替代方案
            if (Object.keys(domainMap).length === 0) {
                // 从domainOptions中获取所有功能域
                const allDomains = domainOptions.filter(domain => domain !== translations[language].allFunctionDomains);

                // 将二级功能平均分配到这些功能域中
                if (allDomains.length > 0) {
                    functionGradeData.forEach((item, index) => {
                        const domainIndex = index % allDomains.length;
                        domainMap[item.twoTagName] = allDomains[domainIndex];
                    });
                }
            }
        } catch (error) {
            console.error("Error building domain map:", error);
        }

        // 先处理每个二级功能及其功能域映射
        const processedData = functionGradeData.map(item => {
            // 优先使用附加到项目上的oneTagName，其次从domainMap中查找
            const functionDomain = (item as any).oneTagName || domainMap[item.twoTagName] || translations[language].allFunctionDomains;
            return {
                domain: item.twoTagName,
                twoTagId: item.twoTagId,
                functionDomain: functionDomain,
                functions: item.threeFunctionGradeVos.map((threeItem, index) => ({
                    firstLevel: threeItem.threeTagName,
                    threeTagId: threeItem.threeTagId,
                    key: `${item.twoTagId}-${threeItem.threeTagId}-${index}`,
                    vehicleGrades: threeItem.vehiclefunctionGrade
                }))
            };
        });

        // 其余代码与原来相同
        // 按选定的功能域进行过滤
        let filteredData = processedData;
        if (!selectedEvalDomains.includes(translations[language].allFunctionDomains)) {
            filteredData = processedData.filter(item =>
                selectedEvalDomains.includes(item.functionDomain)
            );
        }

        // 对二级功能按功能域分组，保留分组结构
        const result: Record<string, any[]> = {};

        // 分组数据
        filteredData.forEach(item => {
            if (!result[item.functionDomain]) {
                result[item.functionDomain] = [];
            }

            // 确认这个二级功能是否已经存在于结果中
            const existingItemIndex = result[item.functionDomain].findIndex(
                existing => existing.domain === item.domain
            );

            if (existingItemIndex === -1) {
                // 如果不存在，添加新项
                result[item.functionDomain].push(item);
            } else {
                // 如果已存在，合并函数列表（这种情况不太可能发生，但为了安全）
                const existingItem = result[item.functionDomain][existingItemIndex];
                existingItem.functions = [...existingItem.functions, ...item.functions];
            }
        });

        // 将结果转换为需要的格式
        return Object.entries(result).map(([domain, items]) => ({
            functionDomain: domain,
            items: items
        }));
    }, [selectedEvalDomains, functionGradeData, queryAllOneTag, domainOptions, translations, language])
    // 过滤功能域数据
    const filteredDomainData = useMemo(() => {
        if (selectedDomains.includes(translations[language].allFunctionDomains)) {
            return domainData
        }
        return domainData.filter((item) => selectedDomains.some((domain) => domain === item.mainDomain))
    }, [selectedDomains])

    // 排序函数
    const getSortedData = useMemo(() => {
        if (!sortConfig) return filteredDomainData

        return [...filteredDomainData].sort((a, b) => {
            const direction = sortConfig.direction === 'asc' ? 1 : -1

            if (sortConfig.field === 'domain' || sortConfig.field === 'mainDomain') {
                return a[sortConfig.field].localeCompare(b[sortConfig.field]) * direction
            }

            return (a[sortConfig.field] - b[sortConfig.field]) * direction
        })
    }, [sortConfig, filteredDomainData])

    // 处理排序点击
    const handleSort = (field: SortField) => {
        setSortConfig((current) => {
            if (current.field === field) {
                return { field, direction: current.direction === 'asc' ? 'desc' : 'asc' }
            }
            return { field, direction: 'desc' }
        })
    }

    // 获取排序图标类名
    const getSortIndicator = (field: SortConfig['field']) => {
        if (sortConfig?.field !== field) return 'opacity-0'
        return sortConfig.direction === 'asc' ? 'rotate-180' : ''
    }

    function getIdsByTags(tagList: any, dataList: any, getFullDomain: boolean = false) {
        // 如果标签列表为空，返回空数组
        if (!tagList || tagList.length === 0) {
            return []
        }

        // 原有逻辑
        const result: any[] = []
        tagList.forEach((tag: any) => {
            dataList.forEach((item: any) => {
                if (item.name === tag) {
                    // 如果需要获取完整功能域数据，则添加所有子功能的ID
                    if (getFullDomain && item.functionOneTagList) {
                        item.functionOneTagList.forEach((oneTag: any) => {
                            result.push(oneTag.id)
                        })
                    } else {
                        // 原有逻辑
                        result.push(item.id)
                    }
                }
            })
        })
        return result
    }

    // 修改 filteredHeatmapData 的逻辑
    const filteredHeatmapData = useMemo(() => {
        // 如果选择了"全部车型"，直接返回所有数据
        if (selectedBrands.includes(translations[language].allModels)) {
            return heatmapData
        }
        // 否则返回选中的车型数据
        return heatmapData.filter((item) => selectedBrands.includes(item.brand))
    }, [selectedBrands, heatmapData])

    return (
        <main className="container mx-auto py-24 bg-[#0D1117]">
            <div className="container mx-auto px-4">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="space-y-9"
                >
                    {/* 页面标题 */}
                    <div className="mb-7">
                        <h1 className="text-3xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                            {translations[language].featureAnalysis}
                        </h1>
                        <p className="text-gray-400 mt-2">{translations[language].inDepthAnalysis}</p>
                    </div>

                    <Tabs defaultValue="heatmap" className="space-y-6" onValueChange={(value) => setActiveTab(value)}>
                        <TabsList className="w-full flex h-auto bg-[#1C2028] p-1 rounded-lg">
                            <TabsTrigger
                                value="overview"
                                className="flex-1 data-[state=active]:bg-amber-500 data-[state=active]:text-black text-gray-400 hover:bg-amber-500 hover:text-black transition-colors text-xl py-3"
                            >
                                {translations[language].bigModelFeatureAnalysis}
                            </TabsTrigger>
                            <TabsTrigger
                                value="heatmap"
                                className="flex-1 data-[state=active]:bg-amber-500 data-[state=active]:text-black text-gray-400 hover:bg-amber-500 hover:text-black transition-colors text-xl py-3"
                            >
                                {translations[language].penetrationRateHeatMap}
                            </TabsTrigger>
                            <TabsTrigger
                                value="evaluation"
                                className="flex-1 data-[state=active]:bg-amber-500 data-[state=active]:text-black text-gray-400 hover:bg-amber-500 hover:text-black transition-colors text-xl py-3"
                            >
                                {translations[language].functionEvaluationDetails}
                            </TabsTrigger>
                        </TabsList>

                        {/* 总览标签页 */}
                        <TabsContent value="overview" className="space-y-6">
                            {/* 筛选器卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardHeader>
                                    <div className="flex flex-col space-y-4 md:flex-row md:items-center md:justify-between md:space-y-0">
                                        <div>
                                            <CardTitle className="text-gray-200">{translations[language].bigModelFunctionDomain}</CardTitle>
                                            <p className="text-sm text-gray-400 mt-1">{translations[language].selectFunctionDomain}</p>
                                        </div>
                                        <div className="relative" ref={domainSelectRef}>
                                            <div
                                                className="flex flex-wrap items-center gap-2 min-w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg cursor-pointer"
                                                onClick={() => setDomainSelectOpen(!domainSelectOpen)}
                                            >
                                                <div className="flex-1 text-sm text-gray-400">
                                                    {selectedDomains.includes(translations[language].allFunctionDomains)
                                                        ? translations[language].allFunctionDomains
                                                        : `${translations[language].selected} ${selectedDomains.length} ${translations[language].selected1}`}
                                                </div>
                                                <svg
                                                    className="w-4 h-4 text-gray-500"
                                                    viewBox="0 0 24 24"
                                                    fill="none"
                                                    stroke="currentColor"
                                                    strokeWidth="2"
                                                >
                                                    <path d="M6 9l6 6 6-6" />
                                                </svg>
                                            </div>
                                            {domainSelectOpen && (
                                                <div className="absolute right-0 mt-2 w-64 max-h-96 overflow-y-auto bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg z-10">
                                                    <div className="p-2 border-b border-gray-800">
                                                        <div
                                                            className="flex items-center gap-2 px-3 py-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                            onClick={() => {
                                                                setSelectedDomains([translations[language].allFunctionDomains])
                                                                setDomainSelectOpen(false)
                                                            }}
                                                        >
                                                            <div
                                                                className={`w-4 h-4 border rounded flex items-center justify-center ${selectedDomains.includes(translations[language].allFunctionDomains)
                                                                    ? 'bg-amber-500 border-amber-500'
                                                                    : 'border-gray-600'
                                                                    }`}
                                                            >
                                                                {selectedDomains.includes(translations[language].allFunctionDomains) && <Check className="w-3 h-3 text-black" />}
                                                            </div>
                                                            <span className="text-sm text-gray-300">{translations[language].allFunctionDomains}</span>
                                                        </div>
                                                    </div>
                                                    <div className="p-2">
                                                        {domainOptions.map((mainDomain) => (
                                                            <div
                                                                key={mainDomain}
                                                                className="flex items-center gap-2 px-3 py-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                onClick={() => {
                                                                    if (selectedDomains.includes(translations[language].allFunctionDomains)) {
                                                                        setSelectedDomains([mainDomain])
                                                                    } else {
                                                                        const newSelection = selectedDomains.includes(mainDomain)
                                                                            ? selectedDomains.filter((d) => d !== mainDomain)
                                                                            : [...selectedDomains, mainDomain]
                                                                        setSelectedDomains(newSelection.length ? newSelection : [translations[language].allFunctionDomains])
                                                                    }
                                                                }}
                                                            >
                                                                <div
                                                                    className={`w-4 h-4 border rounded flex items-center justify-center ${selectedDomains.includes(mainDomain) && !selectedDomains.includes(translations[language].allFunctionDomains)
                                                                        ? 'bg-amber-500 border-amber-500'
                                                                        : 'border-gray-600'
                                                                        }`}
                                                                >
                                                                    {selectedDomains.includes(mainDomain) && !selectedDomains.includes(translations[language].allFunctionDomains) && (
                                                                        <Check className="w-3 h-3 text-black" />
                                                                    )}
                                                                </div>
                                                                <span className="text-sm text-gray-300">{mainDomain}</span>
                                                                <span className="text-xs text-gray-500 ml-1">
                                                                    ({domainGroups[mainDomain]?.length || 0})
                                                                </span>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </CardHeader>
                            </Card>

                            {/* 表格卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardContent className="pt-6">
                                    <div className="space-y-6">
                                        {/* 表格视图 */}
                                        <div className="bg-[#1C2028] rounded-lg shadow-sm overflow-hidden border border-gray-800/30">
                                            <Table>
                                                <TableHeader>
                                                    <TableRow className="bg-[#1C2028]/50 border-b border-gray-800/30">
                                                        <TableHead
                                                            className="font-medium text-lg text-gray-300 w-1/3 py-5 cursor-pointer group"
                                                            onClick={() => handleSort('domain')}
                                                        >
                                                            <div className="flex items-center gap-2 hover:bg-amber-500 hover:text-black px-3 py-1.5 rounded transition-colors">
                                                                {translations[language].function}
                                                                <div className={`transition-all duration-200 ${getSortIndicator('domain')}`}>
                                                                    <svg
                                                                        className="w-5 h-5 text-gray-500 group-hover:text-black"
                                                                        viewBox="0 0 24 24"
                                                                        fill="none"
                                                                        stroke="currentColor"
                                                                        strokeWidth="2"
                                                                    >
                                                                        <path d="M6 9l6 6 6-6" />
                                                                    </svg>
                                                                </div>
                                                            </div>
                                                        </TableHead>
                                                        <TableHead
                                                            className="font-medium text-lg text-gray-300 text-center w-32 cursor-pointer group py-5"
                                                            onClick={() => handleSort('count')}
                                                        >
                                                            <div className="flex items-center justify-center gap-2 hover:bg-amber-500 hover:text-black px-3 py-1.5 rounded transition-colors">
                                                                {translations[language].coveredModels}
                                                                <div className={`transition-all duration-200 ${getSortIndicator('count')}`}>
                                                                    <svg
                                                                        className="w-5 h-5 text-gray-500 group-hover:text-black"
                                                                        viewBox="0 0 24 24"
                                                                        fill="none"
                                                                        stroke="currentColor"
                                                                        strokeWidth="2"
                                                                    >
                                                                        <path d="M6 9l6 6 6-6" />
                                                                    </svg>
                                                                </div>
                                                            </div>
                                                        </TableHead>
                                                        <TableHead
                                                            className="font-medium text-lg text-gray-300 text-center w-[300px] pr-8 cursor-pointer group py-5"
                                                            onClick={() => handleSort('penetration')}
                                                        >
                                                            <div className="flex items-center justify-center gap-2 hover:bg-amber-500 hover:text-black px-3 py-1.5 rounded transition-colors">
                                                                {translations[language].penetrationRate}
                                                                <div className={`transition-all duration-200 ${getSortIndicator('penetration')}`}>
                                                                    <svg
                                                                        className="w-5 h-5 text-gray-500 group-hover:text-black"
                                                                        viewBox="0 0 24 24"
                                                                        fill="none"
                                                                        stroke="currentColor"
                                                                        strokeWidth="2"
                                                                    >
                                                                        <path d="M6 9l6 6 6-6" />
                                                                    </svg>
                                                                </div>
                                                            </div>
                                                        </TableHead>
                                                    </TableRow>
                                                </TableHeader>
                                                <TableBody>
                                                    {isLoading ? (
                                                        <TableRow>
                                                            <TableCell colSpan={7} className="text-center py-8">
                                                                <div className="flex items-center justify-center space-x-2">
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse"></div>
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse delay-100"></div>
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse delay-200"></div>
                                                                </div>
                                                            </TableCell>
                                                        </TableRow>
                                                    ) : (
                                                        getSortedData.map((item, index) => (
                                                            <TableRow key={index} className="border-b border-gray-800/30">
                                                                <TableCell className="py-5">
                                                                    <div className="flex items-center gap-3">
                                                                        <span className="text-gray-300 text-base">{item.domain}</span>
                                                                        <Badge variant="secondary" className="bg-gray-700/50 text-gray-400 text-sm px-3">
                                                                            {item.mainDomain}
                                                                        </Badge>
                                                                    </div>
                                                                </TableCell>
                                                                <TableCell className="text-center py-5">
                                                                    <span className="text-gray-300 text-base">
                                                                        {item.count}/{vehicleCount}
                                                                    </span>
                                                                </TableCell>
                                                                <TableCell className="pr-8 py-5">
                                                                    <div className="flex items-center justify-center">
                                                                        <div className="flex items-center gap-10">
                                                                            <div className="w-[300px] bg-gray-700/30 rounded-full h-3">
                                                                                <div
                                                                                    className="bg-amber-500 h-3 rounded-full"
                                                                                    style={{ width: `${item.penetration}%` }}
                                                                                ></div>
                                                                            </div>
                                                                            <span className="text-gray-300 text-base">{item.penetration}%</span>
                                                                        </div>
                                                                    </div>
                                                                </TableCell>
                                                            </TableRow>
                                                        ))
                                                    )}
                                                </TableBody>
                                            </Table>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </TabsContent>
                        {/* 热力图标签页 */}
                        <TabsContent value="heatmap" className="space-y-6">
                            {/* 筛选器卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardHeader>
                                    <div className="flex flex-col space-y-4 md:flex-row md:items-center md:justify-between md:space-y-0">
                                        <div>
                                            <CardTitle className="text-gray-200">{translations[language].modelPenetrationRate}</CardTitle>
                                            <p className="text-sm text-gray-400 mt-1">{translations[language].selectCarModel}</p>
                                        </div>
                                        <div className="relative" ref={brandSelectRef}>
                                            <div
                                                className="flex flex-wrap items-center gap-2 min-w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg cursor-pointer"
                                                onClick={() => setBrandSelectOpen(!brandSelectOpen)}
                                            >
                                                <div className="flex-1 text-sm text-gray-400">
                                                    {selectedBrands.includes(translations[language].allModels) ? translations[language].allModels : `${translations[language].selected} ${selectedBrands.length} ${translations[language].selected1}`}
                                                </div>
                                                <svg
                                                    className="w-4 h-4 text-gray-500"
                                                    viewBox="0 0 24 24"
                                                    fill="none"
                                                    stroke="currentColor"
                                                    strokeWidth="2"
                                                >
                                                    <path d="M6 9l6 6 6-6" />
                                                </svg>
                                            </div>
                                            {brandSelectOpen && (
                                                <div className="absolute right-0 mt-2 w-64 max-h-96 overflow-y-auto bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg z-10">
                                                    <div className="p-2 border-b border-gray-800">
                                                        <div
                                                            className="flex items-center gap-2 px-3 py-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                            onClick={() => {
                                                                setSelectedBrands([translations[language].allModels])
                                                                setBrandSelectOpen(false)
                                                            }}
                                                        >
                                                            <div
                                                                className={`w-4 h-4 border rounded flex items-center justify-center ${selectedBrands.includes(translations[language].allModels)
                                                                    ? 'bg-amber-500 border-amber-500'
                                                                    : 'border-gray-600'
                                                                    }`}
                                                            >
                                                                {selectedBrands.includes(translations[language].allModels) && <Check className="w-3 h-3 text-black" />}
                                                            </div>
                                                            <span className="text-sm text-gray-300">{translations[language].allModels}</span>
                                                        </div>
                                                    </div>
                                                    <div className="p-2">
                                                        {heatmapData.map((item) => (
                                                            <div
                                                                key={item.brand}
                                                                className="flex items-center gap-2 px-3 py-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                onClick={() => {
                                                                    if (selectedBrands.includes(translations[language].allModels)) {
                                                                        setSelectedBrands([item.brand])
                                                                    } else {
                                                                        const newSelection = selectedBrands.includes(item.brand)
                                                                            ? selectedBrands.filter((b) => b !== item.brand)
                                                                            : [...selectedBrands, item.brand]
                                                                        setSelectedBrands(newSelection.length ? newSelection : [translations[language].allModels])
                                                                    }
                                                                }}
                                                            >
                                                                <div
                                                                    className={`w-4 h-4 border rounded flex items-center justify-center ${selectedBrands.includes(item.brand) && !selectedBrands.includes(translations[language].allModels)
                                                                        ? 'bg-amber-500 border-amber-500'
                                                                        : 'border-gray-600'
                                                                        }`}
                                                                >
                                                                    {selectedBrands.includes(item.brand) && !selectedBrands.includes(translations[language].allModels) && (
                                                                        <Check className="w-3 h-3 text-black" />
                                                                    )}
                                                                </div>
                                                                <span className="text-sm text-gray-300">{item.brand}</span>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </CardHeader>
                            </Card>

                            {/* 表格卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardContent className="pt-6">
                                    <div className="space-y-6">
                                        {/* 表格视图 */}
                                        <div className="bg-[#1C2028] rounded-lg shadow-sm overflow-hidden border border-gray-800/30">
                                            <Table>
                                                <TableHeader>
                                                    <TableRow className="bg-[#1C2028]/50 border-b border-gray-800/30">
                                                        <TableHead className="font-medium text-base text-gray-300">{translations[language].brand}</TableHead>
                                                        {heatmapData && heatmapData.length > 0 ? (
                                                            Object.keys(heatmapData[0].domains).map((key) => (
                                                                <TableHead key={key} className="font-medium text-base text-gray-300 text-center">
                                                                    {key}
                                                                </TableHead>
                                                            ))
                                                        ) : (
                                                            <TableHead className="font-medium text-base text-gray-300 text-center">无数据</TableHead>
                                                        )}
                                                    </TableRow>
                                                </TableHeader>
                                                <TableBody>
                                                    {isLoading ? (
                                                        <TableRow>
                                                            <TableCell colSpan={7} className="text-center py-8">
                                                                <div className="flex items-center justify-center space-x-2">
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse"></div>
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse delay-100"></div>
                                                                    <div className="w-4 h-4 bg-amber-500 rounded-full animate-pulse delay-200"></div>
                                                                </div>
                                                            </TableCell>
                                                        </TableRow>
                                                    ) : (
                                                        filteredHeatmapData.map((item) => (
                                                            <TableRow key={item.brand} className="border-b border-gray-800/30">
                                                                <TableCell className="text-gray-300">{item.brand}</TableCell>
                                                                {Object.entries(item.domains).map(([domain, value]: [string, number]) => (
                                                                    <TableCell
                                                                        key={domain}
                                                                        className="text-center transition-colors"
                                                                        style={{
                                                                            backgroundColor:
                                                                                value === 100
                                                                                    ? 'rgba(245, 158, 11, 1.0)' // 深琥珀色
                                                                                    : value >= 90
                                                                                        ? 'rgba(245, 158, 11, 1.0)' // 琥珀色 100% 透明度
                                                                                        : value >= 80
                                                                                            ? 'rgba(245, 158, 11, 0.8)' // 琥珀色 80% 透明度
                                                                                            : value >= 60
                                                                                                ? 'rgba(245, 158, 11, 0.6)' // 琥珀色 60% 透明度
                                                                                                : value >= 40
                                                                                                    ? 'rgba(245, 158, 11, 0.4)' // 琥珀色 40% 透明度
                                                                                                    : value >= 20
                                                                                                        ? 'rgba(245, 158, 11, 0.1)' // 琥珀色 10% 透明度
                                                                                                        : 'transparent', // 小于20%: 透明背景
                                                                            color: value >= 80 ? '#000000' : '#FFFFFF' // 当背景色较深时使用黑色文字
                                                                        }}
                                                                    >
                                                                        {value}%
                                                                    </TableCell>
                                                                ))}
                                                            </TableRow>
                                                        ))
                                                    )}
                                                </TableBody>
                                            </Table>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </TabsContent>
                        {/* 功能评估详情标签页 */}
                        <TabsContent value="evaluation" className="space-y-6">
                            {/* 筛选器卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardHeader>
                                    <div className="flex flex-col space-y-4 md:flex-row md:items-center md:justify-between md:space-y-0">

                                        <div>
                                            <CardTitle className="text-gray-200">{translations[language].functionEvaluation}</CardTitle>
                                            <p className="text-sm text-gray-400 mt-1">
                                                {translations[language].selectBrandAndFunctionDomain}
                                            </p>
                                        </div>

                                        <div className="flex gap-4">

                                            {/* 功能域筛选器 */}
                                            <div className="relative" ref={evalDomainSelectRef}>
                                                <div
                                                    className="flex flex-wrap items-center gap-2 min-w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg cursor-pointer"
                                                    onClick={() => setEvalDomainSelectOpen(!evalDomainSelectOpen)}
                                                >
                                                    <div className="flex-1 text-sm text-gray-400">
                                                        {selectedEvalDomains.includes(translations[language].allFunctionDomains)
                                                            ? translations[language].allFunctionDomains
                                                            : `${translations[language].selected} ${selectedEvalDomains.length} ${translations[language].selected1}`}
                                                    </div>
                                                    <svg
                                                        className="w-4 h-4 text-gray-500"
                                                        viewBox="0 0 24 24"
                                                        fill="none"
                                                        stroke="currentColor"
                                                        strokeWidth="2"
                                                    >
                                                        <path d="M6 9l6 6 6-6" />
                                                    </svg>
                                                </div>

                                                {evalDomainSelectOpen && (
                                                    <div className="absolute z-10 w-full mt-1 bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg">
                                                        <div className="p-2 space-y-2">
                                                            <div
                                                                className="flex items-center gap-2 p-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                onClick={() => {
                                                                    setSelectedEvalDomains([translations[language].allFunctionDomains])
                                                                    setSelectedFirstLevel([])
                                                                    setSelectedSecondLevel([])
                                                                    setEvalDomainSelectOpen(false)
                                                                    // 获取所有功能域的数据
                                                                    queryVehicleFunctionGradetDate([], translations[language].allFunctionDomains)
                                                                }}
                                                            >
                                                                <div
                                                                    className={`w-4 h-4 border rounded flex items-center justify-center ${selectedEvalDomains.includes(translations[language].allFunctionDomains)
                                                                        ? 'bg-amber-500 border-amber-500'
                                                                        : 'border-gray-600'
                                                                        }`}
                                                                >
                                                                    {selectedEvalDomains.includes(translations[language].allFunctionDomains) && (
                                                                        <Check className="w-3 h-3 text-black" />
                                                                    )}
                                                                </div>
                                                                <span className="text-sm text-gray-300">{translations[language].allFunctionDomains}</span>
                                                            </div>
                                                            {domainOptions.map((domain) => (
                                                                <div
                                                                    key={domain}
                                                                    className="flex items-center gap-2 p-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                    onClick={() => {
                                                                        if (selectedEvalDomains.includes(translations[language].allFunctionDomains)) {
                                                                            setSelectedEvalDomains([domain])
                                                                            // 修改：传入空数组和域名
                                                                            queryVehicleFunctionGradetDate([], domain)
                                                                        } else {
                                                                            const newSelection = selectedEvalDomains.includes(domain)
                                                                                ? selectedEvalDomains.filter((d) => d !== domain)
                                                                                : [...selectedEvalDomains, domain]
                                                                            setSelectedEvalDomains(newSelection.length ? newSelection : [translations[language].allFunctionDomains])
                                                                            // 修改：传入空数组和域名数组
                                                                            if (newSelection.length === 0) {
                                                                                queryVehicleFunctionGradetDate([], translations[language].allFunctionDomains)
                                                                            } else {
                                                                                queryVehicleFunctionGradetDate([], newSelection)
                                                                            }
                                                                        }
                                                                        setSelectedFirstLevel([])
                                                                        setSelectedSecondLevel([])
                                                                        setEvalDomainSelectOpen(false)
                                                                    }}
                                                                >
                                                                    <div
                                                                        className={`w-4 h-4 border rounded flex items-center justify-center ${selectedEvalDomains.includes(domain) &&
                                                                            !selectedEvalDomains.includes(translations[language].allFunctionDomains)
                                                                            ? 'bg-amber-500 border-amber-500'
                                                                            : 'border-gray-600'
                                                                            }`}
                                                                    >
                                                                        {selectedEvalDomains.includes(domain) &&
                                                                            !selectedEvalDomains.includes(translations[language].allFunctionDomains) && (
                                                                                <Check className="w-3 h-3 text-black" />
                                                                            )}
                                                                    </div>
                                                                    <span className="text-sm text-gray-300">{domain}</span>
                                                                </div>
                                                            ))}
                                                        </div>
                                                    </div>
                                                )}
                                            </div>
                                            {/* 品牌筛选器 */}
                                            <div className="relative" ref={evalBrandSelectRef}>
                                                <div
                                                    className="flex flex-wrap items-center gap-2 min-w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg cursor-pointer"
                                                    onClick={() => setIsEvalSelectOpen(!isEvalSelectOpen)}
                                                >
                                                    <div className="flex-1 text-sm text-gray-400">{`${translations[language].selected} ${selectedEvalBrands.length} ${translations[language].selected1}`}</div>
                                                    <svg
                                                        className="w-4 h-4 text-gray-500"
                                                        viewBox="0 0 24 24"
                                                        fill="none"
                                                        stroke="currentColor"
                                                        strokeWidth="2"
                                                    >
                                                        <path d="M6 9l6 6 6-6" />
                                                    </svg>
                                                </div>
                                                {isEvalSelectOpen && (
                                                    <div className="absolute z-10 w-full mt-1 bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg">
                                                        <div className="p-2 space-y-2">
                                                            {carOptions.map((model) => (
                                                                <div
                                                                    key={model}
                                                                    className="flex items-center gap-2 p-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                    onClick={() => {
                                                                        const newSelection = selectedEvalBrands.includes(model)
                                                                            ? selectedEvalBrands.filter((b) => b !== model)
                                                                            : [...selectedEvalBrands, model]
                                                                        setSelectedEvalBrands(newSelection.length ? newSelection : [model])
                                                                    }}
                                                                >
                                                                    <div
                                                                        className={`w-4 h-4 border rounded flex items-center justify-center ${selectedEvalBrands.includes(model)
                                                                            ? 'bg-amber-500 border-amber-500'
                                                                            : 'border-gray-600'
                                                                            }`}
                                                                    >
                                                                        {selectedEvalBrands.includes(model) && <Check className="w-3 h-3 text-black" />}
                                                                    </div>
                                                                    <span className="text-sm text-gray-300">{model}</span>
                                                                </div>
                                                            ))}
                                                        </div>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                </CardHeader>
                            </Card>

                            {/* 表格卡片 */}
                            <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                <CardContent className="pt-6">
                                    <div className="overflow-x-auto">
                                        <style jsx global>{`
                                            .domain-cell:hover:after {
                                                content: "";
                                                display: none;
                                            }
                                            .secondary-function:hover:after {
                                                content: "";
                                                display: none;
                                            }
                                            .highlight-domain {
                                                background-color: rgba(245, 158, 11, 0.05) !important;
                                                border-left: 2px solid rgba(245, 158, 11, 0.4) !important;
                                            }
                                            .highlight-function {
                                                background-color: rgba(245, 158, 11, 0.08) !important;
                                                border-left: 2px solid rgba(245, 158, 11, 0.5) !important;
                                            }
                                            /* 添加车型按钮悬停相关样式 */
                                            td .highlight-model-btn {
                                                box-shadow: 0 0 8px rgba(245, 158, 11, 0.7);
                                                transform: scale(1.08);
                                                z-index: 5;
                                                position: relative;
                                            }
                                            tr.row-highlight td {
                                                background-color: rgba(245, 158, 11, 0.05) !important;
                                                transition: background-color 0.2s ease;
                                            }
                                            .evaluation-btn {
                                                transition: all 0.2s ease;
                                            }
                                        `}</style>
                                        <Table>
                                            <TableHeader>
                                                <TableRow className="border-b border-gray-800/30">
                                                    <TableHead className="w-[150px] text-gray-200">{translations[language].functionDomain || "功能域"}</TableHead>
                                                    <TableHead className="w-[220px] text-gray-200">{translations[language].secondaryFunction}</TableHead>
                                                    <TableHead className="w-[200px] text-gray-200">{translations[language].tertiaryFunction}</TableHead>
                                                    {selectedEvalBrands.map((model) => (
                                                        <TableHead key={model} className="text-center text-gray-200">
                                                            {model}
                                                        </TableHead>
                                                    ))}
                                                </TableRow>
                                            </TableHeader>
                                            <TableBody>
                                                {/* 按功能域分组显示 */}
                                                {
                                                    filteredEvalData.map((domainGroup, groupIndex) => (
                                                        domainGroup.items.map((domainItem, domainIndex) => {
                                                            // 对于每个二级功能组，计算其对应的功能函数数量
                                                            const totalFunctionsCount = domainItem.functions.length;

                                                            return domainItem.functions.map((functionItem, functionIndex) => (
                                                                <TableRow
                                                                    key={`${groupIndex}-${domainIndex}-${functionIndex}-${functionItem.key}`}
                                                                    className={cn(
                                                                        "border-b border-gray-800/30 group",
                                                                        groupIndex % 2 === 0 ? "bg-[#2A303C]" : "bg-[#1F242D]"
                                                                    )}
                                                                    data-domain-group={`domain-group-${groupIndex}`}
                                                                    data-function-group={`function-group-${groupIndex}-${domainIndex}`}
                                                                    data-function-parent={domainItem.domain}
                                                                    data-domain-parent={domainGroup.functionDomain}
                                                                >
                                                                    {/* 功能域列 - 仅在每个功能域的第一行显示 */}
                                                                    {domainIndex === 0 && functionIndex === 0 && (
                                                                        <TableCell
                                                                            rowSpan={domainGroup.items.reduce((sum, item) => sum + item.functions.length, 0)}
                                                                            className="text-gray-300 font-medium border-r border-gray-800/20 transition-colors duration-200 group-hover:bg-amber-500/10 hover:bg-amber-500/15 hover:text-amber-400 cursor-pointer relative domain-cell"
                                                                            data-domain-id={`domain-${groupIndex}`}
                                                                            data-function-domain={domainGroup.functionDomain}
                                                                            onMouseEnter={(e) => {
                                                                                // 只高亮自身
                                                                                e.currentTarget.classList.add('highlight-domain');
                                                                            }}
                                                                            onMouseLeave={(e) => {
                                                                                // 恢复自身的背景色
                                                                                e.currentTarget.classList.remove('highlight-domain');
                                                                            }}
                                                                        >
                                                                            <div className="flex items-center">
                                                                                <span>{domainGroup.functionDomain}</span>
                                                                            </div>
                                                                        </TableCell>
                                                                    )}

                                                                    {/* 二级功能列 - 仅在每个二级功能的第一行显示 */}
                                                                    {functionIndex === 0 && (
                                                                        <TableCell
                                                                            rowSpan={totalFunctionsCount}
                                                                            className="text-gray-300 font-medium transition-colors duration-200 group-hover:bg-amber-500/10 hover:bg-amber-500/15 hover:text-amber-400 cursor-pointer relative secondary-function"
                                                                            data-function-id={`function-${groupIndex}-${domainIndex}`}
                                                                            data-domain-group={`domain-group-${groupIndex}`}
                                                                            data-function-domain={domainGroup.functionDomain}
                                                                            onMouseEnter={(e) => {
                                                                                // 只高亮当前二级功能本身
                                                                                e.currentTarget.classList.add('highlight-function');

                                                                                // 高亮该二级功能下的所有三级功能，但不高亮其他二级功能
                                                                                document.querySelectorAll(`[data-function-group="function-group-${groupIndex}-${domainIndex}"]`).forEach(
                                                                                    el => el.classList.add('highlight-function')
                                                                                );

                                                                                // 只高亮对应的功能域单元格，而不是整个域
                                                                                document.querySelectorAll(`.domain-cell[data-domain-id="domain-${groupIndex}"]`).forEach(
                                                                                    cell => cell.classList.add('highlight-domain')
                                                                                );
                                                                            }}
                                                                            onMouseLeave={(e) => {
                                                                                // 恢复自身的背景色
                                                                                e.currentTarget.classList.remove('highlight-function');

                                                                                // 恢复原来的背景色
                                                                                document.querySelectorAll(`[data-function-group="function-group-${groupIndex}-${domainIndex}"]`).forEach(
                                                                                    el => el.classList.remove('highlight-function')
                                                                                );

                                                                                // 恢复功能域单元格
                                                                                document.querySelectorAll(`.domain-cell`).forEach(
                                                                                    cell => cell.classList.remove('highlight-domain')
                                                                                );
                                                                            }}
                                                                        >
                                                                            <div className="flex items-center">
                                                                                <span>{domainItem.domain}</span>
                                                                            </div>
                                                                        </TableCell>
                                                                    )}

                                                                    {/* 三级功能列 - 每行都显示 */}
                                                                    <TableCell
                                                                        className="text-gray-300 transition-colors duration-200 hover:bg-amber-500/20 group-hover:bg-amber-500/5 hover:text-amber-400 cursor-pointer tertiary-function"
                                                                        onMouseEnter={(e) => {
                                                                            // 获取相关数据属性
                                                                            const row = e.currentTarget.parentElement;
                                                                            const domainGroup = row?.getAttribute('data-domain-group');
                                                                            const functionGroup = row?.getAttribute('data-function-group');

                                                                            // 高亮当前行
                                                                            row?.classList.add('highlight-function');

                                                                            // 只高亮对应的功能域单元格
                                                                            if (domainGroup) {
                                                                                document.querySelectorAll(`.domain-cell[data-domain-id="${domainGroup.replace('domain-group-', 'domain-')}"]`).forEach(cell => {
                                                                                    cell.classList.add('highlight-domain');
                                                                                });
                                                                            }

                                                                            // 只高亮对应的二级功能单元格
                                                                            if (functionGroup) {
                                                                                document.querySelectorAll(`.secondary-function[data-function-id="${functionGroup.replace('function-group-', 'function-')}"]`).forEach(cell => {
                                                                                    cell.classList.add('highlight-function');
                                                                                });
                                                                            }
                                                                        }}
                                                                        onMouseLeave={(e) => {
                                                                            // 恢复当前行的高亮
                                                                            const row = e.currentTarget.parentElement;
                                                                            row?.classList.remove('highlight-function');

                                                                            // 恢复所有高亮的单元格
                                                                            document.querySelectorAll('.highlight-domain').forEach(el => {
                                                                                el.classList.remove('highlight-domain');
                                                                            });
                                                                            document.querySelectorAll('.highlight-function').forEach(el => {
                                                                                el.classList.remove('highlight-function');
                                                                            });
                                                                        }}
                                                                    >
                                                                        {functionItem.firstLevel}
                                                                    </TableCell>

                                                                    {/* 车型评估单元格 */}
                                                                    {selectedEvalBrands.map((model) => {
                                                                        const vehicleGrade = functionItem.vehicleGrades.find(
                                                                            (grade) => grade.vehicleName === model
                                                                        )
                                                                        return (
                                                                            <TableCell key={model} className="text-center">
                                                                                <EvaluationCell
                                                                                    evaluation={{
                                                                                        status: mapFunctionListToStatus(vehicleGrade?.functionList || '4')
                                                                                    }}
                                                                                    model={model}
                                                                                    data={vehicleGrade}
                                                                                    threeTagId={functionItem.threeTagId}
                                                                                    functionName={functionItem.firstLevel}
                                                                                />
                                                                            </TableCell>
                                                                        )
                                                                    })}
                                                                </TableRow>
                                                            ))
                                                        })
                                                    ))
                                                }
                                            </TableBody>
                                        </Table>
                                    </div>
                                </CardContent>
                            </Card>
                        </TabsContent>
                    </Tabs>
                </motion.div>
            </div>
        </main>
    )
}
