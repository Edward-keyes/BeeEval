'use client'

import { useState, useRef, useEffect } from 'react'
import { motion } from 'framer-motion'
import { ChevronDown, ChevronUp, Check, Heart, Share2, Bookmark, ThumbsUp } from 'lucide-react'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Badge } from '@/components/ui/badge'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Button } from '@/components/ui/button'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { PageLayout } from '@/components/layouts/page-layout'
import { Card, CardHeader, CardTitle } from '@/components/ui/card'
import { cn } from '@/lib/utils'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import http from '@/utils/request'
import { RankingDataItem, SortConfig, baseRankingData, SortField, industryAverageData } from './constants'
import { useAuthStore } from '@/store/useAuthStore'
import { toast } from 'react-hot-toast'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'

const brandOptions = [
    { value: 'all', label: '全部品牌' },
    ...Array.from(new Set(baseRankingData.map((item) => item.brand)))
        .sort()
        .map((brand) => ({ value: brand, label: brand }))
]

export default function RankingsPage() {
    const { token, accountStatus } = useAuthStore()
    const [selectedTime, setSelectedTime] = useState<string>('all')
    const [selectedBrands, setSelectedBrands] = useState<string[]>(['all'])
    const [selectedScoreRange, setSelectedScoreRange] = useState<string>('all')
    const [isSelectOpen, setIsSelectOpen] = useState(false)
    const [selectedCategory, setSelectedCategory] = useState('basic')
    const [isLoading, setIsLoading] = useState(false)
    const [likes, setLikes] = useState(128)
    const [isLiked, setIsLiked] = useState(false)
    const [isBookmarked, setIsBookmarked] = useState(false)
    const [timeSelectOpen, setTimeSelectOpen] = useState(false)
    const [scoreSelectOpen, setScoreSelectOpen] = useState(false)
    const [brandSelectOpen, setBrandSelectOpen] = useState(false)
    const { language } = useLanguage()

    // Add refs for dropdown containers
    const timeSelectRef = useRef<HTMLDivElement>(null)
    const scoreSelectRef = useRef<HTMLDivElement>(null)
    const brandSelectRef = useRef<HTMLDivElement>(null)
    const [vehicleTypeOptions, setVehicleTypeOptions] = useState<Array<{ value: string; label: string; status?: number }>>([
        { value: 'all', label: `${translations[language].allModels}` }
    ])

    // 添加新的状态
    const [domainCategories, setDomainCategories] = useState<any[]>([])
    const [catData, setCatData] = useState<any>({
        headerList: [],
        tableList: []
    })
    const [sortConfig, setSortConfig] = useState<{ field: string; direction: 'asc' | 'desc' }>({
        field: '',
        direction: 'asc'
    })
    // 修改 queryDomainTree 函数
    const fetchDomainCategories = async () => {
        try {
            const res = await http.post<any[]>('/vehicle/domaintree/queryDomainTree', {
                language: language
            })
            if (res.code === 11012) {
                handleTokenExpired()
                return
            }
            if (res.code === 11014) {
                handleTokenReplaced()
                return
            }
            const apiCategories = res.data.map((item) => ({
                id: item.id.toString(),
                name: item.functionalDomainName,
                description: `${item.functionalDomainName} ${translations[language].relevantFunctionScore}`
            }))

            // 合并固定分类和API返回的分类
            const allCategories = [

                // {
                //   id: 'total',
                //   name: '总分排行',
                //   description: '智能汽车系统综合评分排行'
                // },

                {
                    id: 'basic',
                    name: translations[language].basicAbilityRanking,
                    description: translations[language].voiceRecognitionAndNLU
                },
                ...apiCategories
            ]
            setDomainCategories(allCategories)
        } catch (error) {
            // console.error('获取领域分类失败:', error)
            toast.error('获取领域分类失败')
        }
    }

    // 获取车辆数据
    const fetchAllVehicleData = async () => {
        try {
            const res = await http.post<any[]>('/ware/baseinfo/allVehicle', {
                language: language
            })

            const options = res.data.map((item) => ({
                value: item.id,
                label: `${item.brandModel}`,
                status: item.status
            }))

            setVehicleTypeOptions([{ value: 'all', label: translations[language].allModels }, ...options])
        } catch (error) {
            // console.error('获取车辆列表失败:', error)
        }
    }

    // 添加一个新的函数，使用指定的分类 ID 获取数据
    const fetchCategoryDataWithId = async (categoryId: string) => {
        try {
            setIsLoading(true)
            let res;

            if (categoryId === 'basic') {
                // 基础能力排行使用 baseDomainIndexScoreSort 接口
                res = await http.post<any[]>('/vehicle/domaintree/baseDomainIndexScoreSort', {
                    brandBaseInfoId: selectedBrands.includes('all') ? [] : selectedBrands,
                    language: language
                })
            } else {
                // 其他领域分类使用 queryIndexScoreRank 接口
                res = await http.post<any[]>('/vehicle/domaintree/queryIndexScoreRank', {
                    brandBaseInfoId: selectedBrands.includes('all') ? [] : selectedBrands,
                    functionalDomain: categoryId,
                    language: language
                })
            }

            if (res.code === 11012) {
                handleTokenExpired()
                return
            }
            if (res.code === 11014) {
                handleTokenReplaced()
                return
            }

            setCatData(res.data)
        } catch (error) {
            // console.error('获取排行数据失败:', error)
            toast.error('获取排行数据失败')
        } finally {
            setIsLoading(false)
        }
    }

    // 修改原有的 fetchCategoryData 函数
    const fetchCategoryData = async () => {
        await fetchCategoryDataWithId(selectedCategory)
    }

    const changeSelect = (data: any, change: any) => {
        if (change) {
            setSelectedCategory(data)
            // 直接使用传入的 data 作为当前选中的分类
            fetchCategoryDataWithId(data)
        }
    }

    // 监听语言变化，重新获取数据
    useEffect(() => {
        fetchAllVehicleData()
        fetchDomainCategories()
        // 重新获取当前分类的数据
        fetchCategoryDataWithId(selectedCategory)
    }, [language])

    // 初始数据加载
    useEffect(() => {
        fetchAllVehicleData()
        fetchDomainCategories()
        // 初始化时加载基础能力排行数据
        fetchCategoryData()
    }, [])

    // Add click outside handler
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (timeSelectRef.current && !timeSelectRef.current.contains(event.target as Node)) {
                setTimeSelectOpen(false)
            }
            if (scoreSelectRef.current && !scoreSelectRef.current.contains(event.target as Node)) {
                setScoreSelectOpen(false)
            }
            if (brandSelectRef.current && !brandSelectRef.current.contains(event.target as Node)) {
                setBrandSelectOpen(false)
            }
        }

        document.addEventListener('mousedown', handleClickOutside)
        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
        }
    }, [])

    const sortedTableList = [...catData.tableList].sort((a, b) => {
        if (!sortConfig.field) return 0 // 默认不排序

        const aValue = a[sortConfig.field]
        const bValue = b[sortConfig.field]

        if (typeof aValue === 'number' && typeof bValue === 'number') {
            return sortConfig.direction === 'asc' ? aValue - bValue : bValue - aValue
        } else if (typeof aValue === 'string' && typeof bValue === 'string') {
            return sortConfig.direction === 'asc' ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue)
        }

        return 0
    })

    const handleSort = (field: string) => {
        setSortConfig((prev) => ({
            field,
            direction: prev.field === field && prev.direction === 'asc' ? 'desc' : 'asc'
        }))
    }

    const renderSortIcon = (sortConfig: any) => {
        return sortConfig === 'asc' ? (
            <ChevronUp className="w-3 h-3 text-amber-500 group-hover:text-black" />
        ) : (
            <ChevronDown className="w-3 h-3 text-amber-500 group-hover:text-black" />
        )
    }

    // 更新日期格式化函数
    const formatDate = (dateString: string) => {
        if (!dateString) return '';
        try {
            // 如果已经是 YYYY-MM-DD 格式，直接返回
            if (/^\d{4}-\d{2}-\d{2}$/.test(dateString)) {
                return dateString;
            }

            // 尝试解析日期字符串
            const date = new Date(dateString);
            if (isNaN(date.getTime())) {
                return dateString; // 如果无法解析，返回原始字符串
            }

            // 格式化日期为 YYYY-MM-DD
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        } catch (error) {
            // console.error('Date formatting error:', error);
            return dateString; // 发生错误时返回原始字符串
        }
    }

    useEffect(() => {
        if (!brandSelectOpen) {
            fetchCategoryData()
        }
    }, [brandSelectOpen])

    // 检查是否是试用车型
    const isTrialVehicle = (row: any) => {
        return row.status === 3
    }

    // 检查是否应该限制访问
    const shouldRestrictAccess = (row: any) => {
        // 使用硬编码的字符串而非translations引用
        const industryAverageText = language === 'en' ? 'Industry Average' : '行业均值';
        return (token && accountStatus === '1' && !isTrialVehicle(row) && row.vehicleInfoName !== industryAverageText);
    }

    // 修改为更通用的列单位处理函数
    const getColumnUnitInfo = (headerId: string, headerName: string) => {
        // 检查列标题是否包含特定关键词
        const percentageColumns = ['任务完成率', '拒识有效性', '免唤醒准确率', '正确拒识率', '跨域任务完成率', '拒识准确率'];

        if (percentageColumns.some(keyword => headerName.includes(keyword))) {
            return {
                needsUnit: true,
                unitZh: '%',
                unitEn: '%',
                processingType: 'percentage'
            };
        }

        if (headerName.includes('图像生成速度')) {
            return {
                needsUnit: true,
                unitZh: 's/张',
                unitEn: 's/image',
                processingType: 'divideBy1000'
            };
        }

        if (headerName.includes('拒识准确率')) {
            return {
                needsUnit: true,
                unitZh: '%',
                unitEn: '%',
                processingType: 'percentage'
            };
        }

        if (headerName.includes('文本生成速度')) {
            return {
                needsUnit: true,
                unitZh: '词/秒',
                unitEn: 'words/s',
                processingType: 'none'
            };
        }

        if (headerName.includes('首字响应时长')) {
            return {
                needsUnit: true,
                unitZh: 's',
                unitEn: 's',
                processingType: 'divideBy1000'
            };
        }

        if (headerName.includes('跨域写作能力')) {
            return {
                needsUnit: true,
                unitZh: '分',
                unitEn: 'points',
                processingType: 'none'
            };
        }

        return {
            needsUnit: false,
            unitZh: '',
            unitEn: '',
            processingType: 'none'
        };
    };

    // 修改为更通用的数据格式化函数
    const formatDataWithUnit = (value: any, headerId: string, headerName: string) => {
        if (value === null || value === undefined || value === '' || value === '-') {
            return '-';
        }

        // 获取列的单位信息
        const unitInfo = getColumnUnitInfo(headerId, headerName);

        // 将值转换为数字
        let numValue = Number(value);

        // 根据不同列类型处理数值
        if (unitInfo.processingType === 'percentage') {
            numValue = numValue * 100;
        } else if (unitInfo.processingType === 'divideBy1000') {
            numValue = numValue / 1000;
        }

        // 对数字进行格式化，保留一位小数
        const formattedValue = numValue.toFixed(1);

        // 添加适当的单位
        if (unitInfo.needsUnit) {
            const unit = language === 'en' ? unitInfo.unitEn : unitInfo.unitZh;
            return `${formattedValue}${unit}`;
        }

        return formattedValue;
    };

    // 为表头添加单位显示
    const getColumnHeaderWithUnit = (headerName: string, headerId: string) => {
        const unitInfo = getColumnUnitInfo(headerId, headerName);

        if (unitInfo.needsUnit) {
            const unit = language === 'en' ? unitInfo.unitEn : unitInfo.unitZh;
            // 对于百分比，使用 (%) 样式，其他单位使用括号
            if (unitInfo.processingType === 'percentage') {
                return `${headerName} (%)`;
            } else {
                return `${headerName} (${unit})`;
            }
        }

        return headerName;
    };

    return (
        <PageLayout activeItem="rankings" darkMode>
            <div className="container mx-auto px-4 py-24">
                {/* 标题部分 */}
                <div className="mb-6">
                    <h1 className="text-2xl md:text-3xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                        {translations[language].smartCarSystemRanking}
                    </h1>
                    <p className="mt-2 text-sm text-gray-400">{translations[language].basedOnBeeEvalData}</p>
                </div>

                {/* 主要内容区域 */}
                <div className="space-y-6">
                    <Tabs
                        defaultValue="total"
                        value={selectedCategory}
                        onValueChange={(v) => changeSelect(v, true)}
                        className="space-y-6"
                    >
                        <TabsList className="w-full flex flex-wrap h-auto bg-[#1C2028] p-1 rounded-lg">
                            {domainCategories.map((category) => (
                                <TabsTrigger
                                    key={category.id}
                                    value={category.id}
                                    className="flex-1 data-[state=active]:bg-amber-500 data-[state=active]:text-black text-gray-400 hover:bg-amber-500 hover:text-black transition-colors text-xl py-3"
                                >
                                    {category.name}
                                </TabsTrigger>
                            ))}
                        </TabsList>

                        {domainCategories.map((category) => (
                            <TabsContent key={category.id} value={category.id} className="space-y-6">
                                <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                    <CardHeader>
                                        <div className="flex flex-col space-y-4 md:flex-row md:items-center md:justify-between md:space-y-0">
                                            <div>
                                                <CardTitle className="text-gray-200">{category.name}</CardTitle>
                                                <p className="text-sm text-gray-400 mt-1">{category.description}</p>
                                            </div>

                                            {/* 车型筛选器 */}
                                            <div className="relative" ref={brandSelectRef}>
                                                <div
                                                    className="flex flex-wrap items-center gap-2 min-w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg cursor-pointer"
                                                    onClick={() => setBrandSelectOpen(!brandSelectOpen)}
                                                >
                                                    <div className="flex-1 text-sm text-gray-400">
                                                        {selectedBrands.includes('all') ? `${translations[language].allModels}` : `${translations[language].selected} ${selectedBrands.length} ${translations[language].selected1}`}
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
                                                                    setSelectedBrands(['all'])
                                                                    setBrandSelectOpen(false)
                                                                    // 使用新的选择值进行查询
                                                                    if (selectedCategory === 'basic') {
                                                                        fetchCategoryData()
                                                                    } else if (selectedCategory === 'total') {
                                                                        fetchCategoryData()
                                                                    } else {
                                                                        fetchCategoryData()
                                                                    }
                                                                }}
                                                            >
                                                                <div
                                                                    className={`w-4 h-4 border rounded flex items-center justify-center ${selectedBrands.includes('all') ? 'bg-amber-500 border-amber-500' : 'border-gray-600'
                                                                        }`}
                                                                >
                                                                    {selectedBrands.includes('all') && <Check className="w-3 h-3 text-black" />}
                                                                </div>
                                                                <span className="text-sm text-gray-300">{translations[language].allModels}</span>
                                                            </div>
                                                        </div>
                                                        <div className="p-2">
                                                            {vehicleTypeOptions
                                                                .slice(1)
                                                                .filter(option => {
                                                                    // 如果是试用账号，只显示试用车型（status === 3）
                                                                    if (token && accountStatus === '1') {
                                                                        return option.status === 3;
                                                                    }
                                                                    // 如果是正式账号，显示所有非"敬请期待"状态的车型
                                                                    return option.status !== 2;
                                                                })
                                                                .map((option) => (
                                                                    <div
                                                                        key={option.value}
                                                                        className="flex items-center gap-2 px-3 py-2 hover:bg-amber-500 hover:text-black rounded cursor-pointer"
                                                                        onClick={() => {
                                                                            // 如果是试用账号且不是试用车型，显示提示
                                                                            if (token && accountStatus === '1' && option.status !== 3) {
                                                                                toast.error('当前账号为试用账号，仅支持查看试用车型')
                                                                                return;
                                                                            }

                                                                            if (selectedBrands.includes('all')) {
                                                                                const newSelection: string[] = [option.value];
                                                                                setSelectedBrands(newSelection);
                                                                                // 使用新的选择值进行查询
                                                                                if (selectedCategory === 'basic') {
                                                                                    fetchCategoryData()
                                                                                } else if (selectedCategory === 'total') {
                                                                                    fetchCategoryData()
                                                                                } else {
                                                                                    fetchCategoryData()
                                                                                }
                                                                            } else {
                                                                                const newSelection = selectedBrands.includes(option.value)
                                                                                    ? selectedBrands.filter((b) => b !== option.value)
                                                                                    : [...selectedBrands, option.value]
                                                                                const finalSelection = newSelection.length ? newSelection : ['all']
                                                                                setSelectedBrands(finalSelection)
                                                                                // 使用新的选择值进行查询
                                                                                const params = finalSelection.includes('all') ? [] : finalSelection
                                                                                if (selectedCategory === 'basic') {
                                                                                    fetchCategoryData()
                                                                                } else if (selectedCategory === 'total') {
                                                                                    fetchCategoryData()
                                                                                } else {
                                                                                    fetchCategoryData()
                                                                                }
                                                                            }
                                                                        }}
                                                                    >
                                                                        <div
                                                                            className={`w-4 h-4 border rounded flex items-center justify-center ${selectedBrands.includes(option.value) && !selectedBrands.includes('all')
                                                                                ? 'bg-amber-500 border-amber-500'
                                                                                : 'border-gray-600'
                                                                                }`}
                                                                        >
                                                                            {selectedBrands.includes(option.value) && !selectedBrands.includes('all') && (
                                                                                <Check className="w-3 h-3 text-black" />
                                                                            )}
                                                                        </div>
                                                                        <span className="text-sm text-gray-300">{option.label}</span>
                                                                    </div>
                                                                ))}
                                                        </div>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </CardHeader>
                                </Card>

                                {/* 表格部分 */}
                                <Card className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border-gray-800/30">
                                    <div className="relative overflow-x-auto">
                                        {isLoading ? (
                                            <div className="flex items-center justify-center min-h-[200px]">
                                                <div className="flex flex-col items-center gap-2">
                                                    <div className="w-8 h-8 border-4 border-amber-500 border-t-transparent rounded-full animate-spin"></div>
                                                    <span className="text-sm text-gray-400">Loading...</span>
                                                </div>
                                            </div>
                                        ) : (
                                            <table className="w-full border-collapse">
                                                <thead>
                                                    <tr className="border-b border-gray-800/30 bg-[#1C2028]/50">
                                                        {catData.headerList.map((item: any, index: number) => (
                                                            <TableHead
                                                                key={index}
                                                                className={cn(
                                                                    "whitespace-nowrap py-3 text-gray-300 cursor-pointer hover:bg-gray-700",
                                                                    // 为固定列添加特殊样式
                                                                    (item.headerId.toLowerCase().includes('vehicle') ||
                                                                        item.headerId.toLowerCase().includes('version') ||
                                                                        item.headerId.toLowerCase().includes('date')) &&
                                                                    "sticky bg-[#1C2028] min-w-[120px]",
                                                                    // 为每个固定列设置不同的left值和z-index，确保层级正确
                                                                    item.headerId.toLowerCase().includes('vehicle') && "left-0 z-30",
                                                                    item.headerId.toLowerCase().includes('version') && "left-[120px] z-20",
                                                                    item.headerId.toLowerCase().includes('date') && "left-[240px] z-10",
                                                                    // 为非固定列设置最小宽度
                                                                    !item.headerId.toLowerCase().includes('vehicle') &&
                                                                    !item.headerId.toLowerCase().includes('version') &&
                                                                    !item.headerId.toLowerCase().includes('date') &&
                                                                    "min-w-[100px] px-4"
                                                                )}
                                                                onClick={() => handleSort(item.headerId)}
                                                            >
                                                                <div className="group flex items-center justify-center gap-0.5 cursor-pointer hover:bg-amber-500 hover:text-black px-2 py-1 rounded transition-colors">
                                                                    {getColumnHeaderWithUnit(item.headerName, item.headerId)}
                                                                    {sortConfig.field === item.headerId && (
                                                                        <span className="ml-2">{renderSortIcon(sortConfig.direction)}</span>
                                                                    )}
                                                                </div>
                                                            </TableHead>
                                                        ))}
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {sortedTableList.map((row: any, rowIndex: number) => (
                                                        <TableRow
                                                            key={rowIndex}
                                                            className={cn(
                                                                'hover:bg-muted/50 cursor-pointer group border-b border-gray-800/30',
                                                                (() => {
                                                                    // 使用硬编码的字符串而非translations引用
                                                                    const industryAverageText = language === 'en' ? 'Industry Average' : '行业均值';
                                                                    return row.vehicleInfoName === industryAverageText && 'bg-amber-500/5 hover:bg-amber-500/10';
                                                                })()
                                                            )}
                                                        >
                                                            {catData.headerList.map((col: any, colIndex: number) => (
                                                                <TableCell
                                                                    key={colIndex}
                                                                    className={cn(
                                                                        // 为固定列添加特殊样式
                                                                        (col.headerId.toLowerCase().includes('vehicle') ||
                                                                            col.headerId.toLowerCase().includes('version') ||
                                                                            col.headerId.toLowerCase().includes('date')) &&
                                                                        "sticky bg-[#2A303C] min-w-[120px] whitespace-nowrap text-left",
                                                                        // 为每个固定列设置不同的left值和z-index，确保层级正确
                                                                        col.headerId.toLowerCase().includes('vehicle') && "left-0 z-30 after:absolute after:top-0 after:right-0 after:bottom-0 after:w-[1px] after:bg-[#1F242D] after:content-['']",
                                                                        col.headerId.toLowerCase().includes('version') && "left-[120px] z-20 after:absolute after:top-0 after:right-0 after:bottom-0 after:w-[1px] after:bg-[#1F242D] after:content-['']",
                                                                        col.headerId.toLowerCase().includes('date') && "left-[240px] z-10 after:absolute after:top-0 after:right-0 after:bottom-0 after:w-[1px] after:bg-[#1F242D] after:content-['']",
                                                                        // 非固定列右对齐
                                                                        !col.headerId.toLowerCase().includes('vehicle') &&
                                                                        !col.headerId.toLowerCase().includes('version') &&
                                                                        !col.headerId.toLowerCase().includes('date') &&
                                                                        "text-right",
                                                                        // 添加相对定位，用于遮罩层定位
                                                                        shouldRestrictAccess(row) && !col.headerId.toLowerCase().includes('vehicle') &&
                                                                        !col.headerId.toLowerCase().includes('version') &&
                                                                        !col.headerId.toLowerCase().includes('date') &&
                                                                        "relative"
                                                                    )}
                                                                >
                                                                    <div className={cn(
                                                                        shouldRestrictAccess(row) && !col.headerId.toLowerCase().includes('vehicle') &&
                                                                        !col.headerId.toLowerCase().includes('version') &&
                                                                        !col.headerId.toLowerCase().includes('date') &&
                                                                        "blur-sm"
                                                                    )}>
                                                                        {col.headerId.toLowerCase().includes('date')
                                                                            ? formatDate(row[col.headerId])
                                                                            : !col.headerId.toLowerCase().includes('vehicle') &&
                                                                                !col.headerId.toLowerCase().includes('version') &&
                                                                                !col.headerId.toLowerCase().includes('date')
                                                                                ? formatDataWithUnit(row[col.headerId], col.headerId, col.headerName)
                                                                                : row[col.headerId]}
                                                                    </div>
                                                                    {shouldRestrictAccess(row) && !col.headerId.toLowerCase().includes('vehicle') &&
                                                                        !col.headerId.toLowerCase().includes('version') &&
                                                                        !col.headerId.toLowerCase().includes('date') && (
                                                                            <div className="absolute inset-0 flex items-center justify-center bg-black/20">
                                                                                <span className="text-xs text-gray-300"></span>
                                                                            </div>
                                                                        )}
                                                                </TableCell>
                                                            ))}
                                                        </TableRow>
                                                    ))}
                                                </tbody>
                                            </table>
                                        )}
                                    </div>
                                </Card>
                            </TabsContent>
                        ))}
                    </Tabs>
                </div>
            </div>
        </PageLayout>
    )
}
