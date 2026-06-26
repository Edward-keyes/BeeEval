'use client'

import { ChevronRight } from 'lucide-react'
import { cn } from '@/lib/utils'
import { useEffect, useState } from 'react'
import http from '@/utils/request'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { useLanguage } from '@/constants/language'

// 定义接口类型
interface ThreeTag {
    id: number
    tagName: string
}

interface TwoTag {
    twoTagName: string
    functionThreeTagVOList: ThreeTag[]
}

interface OneTag {
    oneTagName: string
    functionTwoTagVOList: TwoTag[]
}

interface CapabilitiesSidebarProps {
    selectedCategory: string
    selectedSubcategory: string
    selectedItem: string
    onCategorySelect: (category: string) => void
    onSubcategorySelect: (subcategory: string) => void
    onItemSelect: (item: string, threeTagId: string) => void
}

export function CapabilitiesSidebar({
    selectedCategory,
    selectedSubcategory,
    selectedItem,
    onCategorySelect,
    onSubcategorySelect,
    onItemSelect
}: CapabilitiesSidebarProps) {
    const [expandedCategory, setExpandedCategory] = useState<string | null>(selectedCategory)
    const [expandedSubcategory, setExpandedSubcategory] = useState<string | null>(selectedSubcategory)
    const [categories, setCategories] = useState<OneTag[]>([])
    const { language } = useLanguage()
    // 发起网络请求
    const fetchData = async () => {
        try {
            const response = await http.post<OneTag[]>('/ware/functiononetag/queryAllFunctionTagTree', { language: language })

            // 检查 token 是否失效
            if (response.code === 11012) {
                handleTokenExpired()
                return
            }
            if (response.code === 11014) {
                handleTokenReplaced()
                return
            }

            setCategories(response.data)

            // 如果有数据，自动选中第一个节点
            if (response.data.length > 0) {
                const firstCategory = response.data[0]
                const firstSubcategory = firstCategory.functionTwoTagVOList[0]
                const firstItem = firstSubcategory.functionThreeTagVOList[0]

                setExpandedCategory(firstCategory.oneTagName)
                setExpandedSubcategory(firstSubcategory.twoTagName)

                onCategorySelect(firstCategory.oneTagName)
                onSubcategorySelect(firstSubcategory.twoTagName)
                onItemSelect(firstItem.tagName, firstItem.id.toString())
            }
        } catch (error) {
            // console.error('Error fetching data:', error)
        }
    }

    useEffect(() => {
        fetchData()
    }, [])

    // 监听语言变化，重新获取数据
    useEffect(() => {
        fetchData()
    }, [language])

    const handleCategoryClick = (categoryName: string) => {
        if (expandedCategory === categoryName) {
            setExpandedCategory(null)
            setExpandedSubcategory(null)
        } else {
            setExpandedCategory(categoryName)
            setExpandedSubcategory(null)
            onCategorySelect(categoryName)
        }
    }

    const handleSubcategoryClick = (categoryName: string, subcategoryName: string) => {
        if (expandedSubcategory === subcategoryName) {
            setExpandedSubcategory(null)
        } else {
            setExpandedCategory(categoryName)
            setExpandedSubcategory(subcategoryName)
            onCategorySelect(categoryName)
            onSubcategorySelect(subcategoryName)
        }
    }

    const handleItemClick = (categoryName: string, subcategoryName: string, itemName: string, threeTagId: number) => {
        setExpandedCategory(categoryName)
        setExpandedSubcategory(subcategoryName)
        onCategorySelect(categoryName)
        onSubcategorySelect(subcategoryName)
        onItemSelect(itemName, threeTagId.toString())
    }

    return (
        <nav className="h-[calc(100vh-16rem)] overflow-y-auto pr-2 custom-scrollbar">
            <style jsx global>{`
        .custom-scrollbar::-webkit-scrollbar {
          width: 4px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
          background: transparent;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
          background-color: #4b5563;
          border-radius: 2px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
          background-color: #6b7280;
        }
      `}</style>
            <div className="py-3 space-y-0.5">
                {categories.map((category) => (
                    <div key={category.oneTagName} className="mb-1.5">
                        {/* 一级菜单 */}
                        <button
                            onClick={() => handleCategoryClick(category.oneTagName)}
                            className={cn(
                                'w-full flex items-center justify-between px-4 py-2.5 transition-colors rounded-lg ml-2',
                                selectedCategory === category.oneTagName
                                    ? 'text-amber-500 font-semibold bg-amber-500/5'
                                    : 'text-gray-300 hover:text-gray-200 hover:bg-[#252933]',
                                expandedCategory === category.oneTagName && 'bg-[#252933]',
                                'text-[16px]'
                            )}
                        >
                            <div className="flex items-center gap-2.5">
                                <div
                                    className={cn(
                                        'w-1.5 h-1.5 rounded-full',
                                        selectedCategory === category.oneTagName ? 'bg-amber-500' : 'bg-gray-500'
                                    )}
                                />
                                <span>{category.oneTagName}</span>
                            </div>
                            <ChevronRight
                                className={cn(
                                    'h-4.5 w-4.5 transition-transform duration-200',
                                    expandedCategory === category.oneTagName && 'rotate-90'
                                )}
                            />
                        </button>

                        {/* 二级菜单 */}
                        <div
                            className={cn(
                                'overflow-hidden transition-all duration-200',
                                expandedCategory === category.oneTagName ? 'max-h-[1000px] opacity-100 mt-1' : 'max-h-0 opacity-0'
                            )}
                        >
                            {category.functionTwoTagVOList.map((subcategory) => (
                                <div key={subcategory.twoTagName} className="mb-1">
                                    <button
                                        onClick={() => handleSubcategoryClick(category.oneTagName, subcategory.twoTagName)}
                                        className={cn(
                                            'w-full flex items-center justify-between pl-7 pr-4 py-2 transition-colors rounded-lg',
                                            selectedSubcategory === subcategory.twoTagName
                                                ? 'text-amber-500 font-medium bg-amber-500/5'
                                                : 'text-gray-400 hover:text-gray-200 hover:bg-[#252933]',
                                            expandedSubcategory === subcategory.twoTagName && 'bg-[#252933]',
                                            'text-[15px]'
                                        )}
                                    >
                                        <div className="flex items-center gap-2.5">
                                            <div
                                                className={cn(
                                                    'w-4 h-[1px]',
                                                    selectedSubcategory === subcategory.twoTagName ? 'bg-amber-500' : 'bg-gray-500'
                                                )}
                                            />
                                            <span className="font-medium">{subcategory.twoTagName}</span>
                                        </div>
                                        <ChevronRight
                                            className={cn(
                                                'h-4 w-4 transition-transform duration-200',
                                                expandedSubcategory === subcategory.twoTagName && 'rotate-90'
                                            )}
                                        />
                                    </button>

                                    {/* 三级菜单 */}
                                    <div
                                        className={cn(
                                            'overflow-hidden transition-all duration-200',
                                            expandedSubcategory === subcategory.twoTagName
                                                ? 'max-h-[500px] opacity-100 mt-1'
                                                : 'max-h-0 opacity-0'
                                        )}
                                    >
                                        {subcategory.functionThreeTagVOList.map((item) => (
                                            <button
                                                key={item.id}
                                                onClick={() =>
                                                    handleItemClick(category.oneTagName, subcategory.twoTagName, item.tagName, item.id)
                                                }
                                                className={cn(
                                                    'w-full text-left pl-12 pr-4 py-2 transition-colors rounded-lg',
                                                    selectedItem === item.tagName
                                                        ? 'text-amber-500 bg-amber-500/5 font-medium'
                                                        : 'text-gray-400 hover:text-gray-200 hover:bg-[#252933]',
                                                    'text-[14px]'
                                                )}
                                            >
                                                <div className="flex items-center gap-2.5">
                                                    <div
                                                        className={cn(
                                                            'w-1 h-1 rounded-full transition-opacity',
                                                            selectedItem === item.tagName ? 'opacity-100' : 'opacity-0',
                                                            selectedItem === item.tagName ? 'bg-amber-500' : 'bg-gray-500'
                                                        )}
                                                    />
                                                    <span>{item.tagName}</span>
                                                </div>
                                            </button>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </nav>
    )
}
