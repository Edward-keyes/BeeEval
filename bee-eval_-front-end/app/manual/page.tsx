'use client'

import { SidebarProvider, SidebarContent, SidebarGroup, SidebarGroupLabel } from '@/components/ui/sidebar';
import { useState, useEffect, useRef } from 'react';
import { Header } from '@/components/header';
import { Footer } from '@/components/footer';
import Image from 'next/image';
import { useLanguage } from '@/constants/language';

const HEADER_HEIGHT = 64; // 头部高度，按主站Header高度调整

const guideMenu = [
    {
        id: 'section-1',
        label: {
            zh: '1. 顶部导航栏/底部导航栏',
            en: '1. Top / Bottom Navigation Bar'
        },
        children: [
            {
                id: 'section-1-1',
                label: {
                    zh: '顶部导航栏/底部导航栏',
                    en: 'Top / Bottom Navigation Bar'
                },
                image: '/websitebook/网站说明书(顶部底部导航栏)1.png'
            },
        ]
    },
    {
        id: 'section-2',
        label: {
            zh: '2. 首页',
            en: '2. Home Page'
        },
        children: [
            {
                id: 'section-2-1',
                label: {
                    zh: '基础信息区域',
                    en: 'Basic Information Section'
                },
                image: '/websitebook/网站说明书(基础信息区域)2.png'
            },
            {
                id: 'section-2-2',
                label: {
                    zh: '亮点场景推荐',
                    en: 'Function Highlights'
                },
                image: '/websitebook/网站说明书(亮点场景推荐)3.png'
            },
        ]
    },
    {
        id: 'section-3',
        label: {
            zh: '3. 车型库',
            en: '3. All Models'
        },
        children: [
            {
                id: 'section-3-1',
                label: {
                    zh: '车型库',
                    en: 'All Models'
                },
                image: '/websitebook/网站说明书(车型库)4.png'
            },
        ]
    },
    {
        id: 'section-4',
        label: {
            zh: '4. 大模型能力评测',
            en: '4. Large Model Capability Evaluation'
        },
        children: [
            {
                id: 'section-4-1',
                label: {
                    zh: '基础能力',
                    en: 'Basic Capability'
                },
                image: '/websitebook/网站说明书(基础能力)5.png'
            },
            {
                id: 'section-4-2',
                label: {
                    zh: '功能域表现',
                    en: 'Domain Performance'
                },
                image: '/websitebook/网站说明书(功能域表现)6.png'
            },
            {
                id: 'section-4-3',
                label: {
                    zh: '车格特征',
                    en: 'Carsona'
                },
                image: '/websitebook/网站说明书(车格特征)7.png'
            },
        ]
    },
    {
        id: 'section-5',
        label: {
            zh: '5. 能力测评视频',
            en: '5. Feature Performance Video'
        },
        children: [
            {
                id: 'section-5-1',
                label: {
                    zh: '能力测评视频',
                    en: 'Feature Performance Video'
                },
                image: '/websitebook/网站说明书(能力测评视频).png'
            },
        ]
    },
    {
        id: 'section-6',
        label: {
            zh: '6. 大模型功能树/竞品视频对比',
            en: '6. Large Model Function Evaluation/Competitive Product Video Comparison'
        },
        children: [
            {
                id: 'section-6-1',
                label: {
                    zh: '大模型功能树',
                    en: 'Large Model Function Evaluation'
                },
                image: '/websitebook/网站说明书(大模型功能树)9.png'
            },
            {
                id: 'section-6-2',
                label: {
                    zh: '竞品视频对比',
                    en: 'Competitive Product Video Comparison'
                },
                image: '/websitebook/网站说明书(竞品视频对比)10.png'
            },
        ]
    },
    {
        id: 'section-7',
        label: {
            zh: '7. 排行榜',
            en: '7. Rankings'
        },
        children: [
            {
                id: 'section-7-1',
                label: {
                    zh: '排行榜',
                    en: 'Rankings'
                },
                image: '/websitebook/网站说明书(排行榜)11.png'
            },
        ]
    },
    {
        id: 'section-8',
        label: {
            zh: '8. 功能分析',
            en: '8. Feature Analysis'
        },
        children: [
            {
                id: 'section-8-1',
                label: {
                    zh: '大模型功能普及率/功能渗透率',
                    en: 'Large Model Function Popularity/Function Penetration Rate'
                },
                image: '/websitebook/网站说明书(大模型功能普及率功能渗透率)12.png'
            },
            {
                id: 'section-8-2',
                label: {
                    zh: '功能详情',
                    en: 'Feature Details'
                },
                image: '/websitebook/网站说明书(功能详情)13.png'
            },
        ]
    },
];

// 定义类型
interface MenuItem {
    id: string;
    label: {
        zh: string;
        en: string;
    };
    image?: string;
    parentId: string | null;
    children?: ChildMenuItem[];
}

interface ChildMenuItem {
    id: string;
    label: {
        zh: string;
        en: string;
    };
    image?: string;
}

// 扁平化菜单，方便后续索引操作（只包含子菜单项）
const flattenMenu = () => {
    const result: MenuItem[] = [];

    guideMenu.forEach(item => {
        if (item.children) {
            item.children.forEach(child => {
                result.push({
                    id: child.id,
                    label: child.label,
                    image: child.image,
                    parentId: item.id
                });
            });
        }
    });

    return result;
};

export default function ManualPage() {
    const { language } = useLanguage();
    const isEnglish = language === 'en';
    const flatMenu = flattenMenu();
    const [activeSection, setActiveSection] = useState(flatMenu[0]?.id || '');
    const contentRef = useRef<HTMLDivElement>(null);
    const sectionRefs = useRef<{ [key: string]: HTMLDivElement | null }>({});

    // 修改图片路径处理函数
    const getImagePath = (basePath: string) => {
        if (isEnglish) {
            // 在文件名和扩展名之间插入 " ENG"
            const lastDotIndex = basePath.lastIndexOf('.');
            if (lastDotIndex !== -1) {
                return `${basePath.slice(0, lastDotIndex)} ENG${basePath.slice(lastDotIndex)}`;
            }
        }
        return basePath;
    };

    // 修复section引用的设置方法
    const setSectionRef = (id: string) => (el: HTMLDivElement | null) => {
        sectionRefs.current[id] = el;
    };

    // 监听滚动，更新活动菜单
    useEffect(() => {
        const handleScroll = () => {
            if (!contentRef.current) return;

            // 获取当前滚动位置
            const scrollPosition = contentRef.current.scrollTop;

            // 找出当前可见的部分
            let currentSection = flatMenu[0]?.id || '';

            // 计算每个部分的位置
            flatMenu.forEach(item => {
                const section = sectionRefs.current[item.id];
                if (section) {
                    const sectionTop = section.offsetTop;

                    // 向下滚动30px以上才算进入该部分
                    if (scrollPosition >= sectionTop - 100) {
                        currentSection = item.id;
                    }
                }
            });

            setActiveSection(currentSection);
        };

        const contentElement = contentRef.current;
        if (contentElement) {
            contentElement.addEventListener('scroll', handleScroll);
            // 初始加载时也检查一次
            handleScroll();
        }

        return () => {
            if (contentElement) {
                contentElement.removeEventListener('scroll', handleScroll);
            }
        };
    }, [flatMenu]);

    // 滚动到指定部分
    const scrollToSection = (sectionId: string) => {
        const section = sectionRefs.current[sectionId];
        if (section && contentRef.current) {
            contentRef.current.scrollTo({
                top: section.offsetTop - 90,
                behavior: 'smooth'
            });
        }
    };

    // 判断一级菜单是否激活
    const isParentActive = (itemId: string) => {
        // 查找当前活动项目
        const current = flatMenu.find(item => item.id === activeSection);
        // 如果找到了当前活动项目，并且它的父ID是这个一级菜单ID，那么这个一级菜单就是激活的
        return current?.parentId === itemId;
    };

    // 判断子项是否激活
    const isChildActive = (childId: string) => {
        return childId === activeSection;
    };

    return (
        <div className="flex flex-col min-h-screen bg-[#171717] font-sans">
            <Header />
            <div className="flex flex-1 w-full">
                <SidebarProvider>
                    <aside
                        className="z-20"
                        style={{
                            position: 'sticky',
                            top: HEADER_HEIGHT,
                            height: `calc(100vh - ${HEADER_HEIGHT}px)`,
                            minWidth: '16rem',
                            maxWidth: '18rem',
                            background: '#181C23',
                            color: 'white',
                            borderRight: '1px solid #23272f',
                            boxShadow: '0 2px 8px 0 rgba(0,0,0,0.04)',
                            overflowY: 'auto',
                            paddingBottom: '1rem',
                        }}
                    >
                        <SidebarContent>
                            <SidebarGroup>
                                <SidebarGroupLabel className="text-lg font-bold mb-4 tracking-wide pl-2 pt-4 pb-2">
                                    {isEnglish ? 'Website Manual' : '网站使用手册'}
                                </SidebarGroupLabel>
                                <nav className="flex flex-col gap-1">
                                    {guideMenu.map((item) => (
                                        <div key={item.id}>
                                            <div
                                                className={`px-4 py-2 rounded-lg text-base font-medium transition-colors select-none flex items-center gap-2
                                                    ${isParentActive(item.id) ? 'bg-amber-500/90 text-black shadow' : 'text-gray-100'}`}
                                            >
                                                {isEnglish ? item.label.en : item.label.zh}
                                            </div>
                                            {item.children && (
                                                <div className={`ml-4 flex flex-col gap-1 border-l border-gray-700 pl-2 transition-all duration-300`}>
                                                    {item.children.map((child) => (
                                                        <div
                                                            style={{ marginTop: 3 }}
                                                            key={child.id}
                                                            className={`px-3 py-1.5 rounded-lg cursor-pointer text-sm transition-colors select-none
                                                                ${isChildActive(child.id) ? 'bg-amber-500/90 text-black shadow' : 'hover:bg-amber-500/10 text-gray-300'}`}
                                                            onClick={() => scrollToSection(child.id)}
                                                        >
                                                            {isEnglish ? child.label.en : child.label.zh}
                                                        </div>
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </nav>
                            </SidebarGroup>
                        </SidebarContent>
                    </aside>

                    <main
                        ref={contentRef}
                        className="flex-1 overflow-y-auto bg-gradient-to-br from-[#181C23] to-[#23272f]"
                        style={{
                            height: `calc(100vh - ${HEADER_HEIGHT}px)`,
                            scrollBehavior: 'smooth'
                        }}
                    >
                        <div className="flex flex-col items-center py-8 px-4">
                            {/* 初始页图片 */}
                            <div className="w-full max-w-[1500px] mb-16">
                                <div style={{ marginTop: 60, marginBottom: 60 }} className=" rounded-2xl overflow-hidden">
                                    <div className="relative" style={{ height: '793px' }}>
                                        <Image
                                            src={getImagePath("/websitebook/网站说明书-初始页.png")}
                                            alt={isEnglish ? "Website Manual" : "网站使用手册"}
                                            fill
                                            className="object-contain p-4"
                                            priority
                                        />
                                    </div>
                                </div>
                            </div>

                            {/* 所有部分的内容（只渲染子菜单） */}
                            {flatMenu.map((item) => (
                                <div
                                    key={item.id}
                                    id={item.id}
                                    ref={setSectionRef(item.id)}
                                    style={{ marginBottom: 100 }}
                                    className="w-full max-w-[1500px] mb-16 scroll-mt-8"
                                >
                                    <div className="bg-white rounded-2xl shadow-2xl border border-gray-200 overflow-hidden">
                                        <div className="border-b border-gray-100 bg-gradient-to-r from-amber-50 to-white px-6 py-4">
                                            <h2 className="text-2xl font-bold text-gray-800">
                                                {isEnglish ?
                                                    guideMenu.find(m => m.id === item.parentId)?.label.en :
                                                    guideMenu.find(m => m.id === item.parentId)?.label.zh
                                                }
                                            </h2>
                                            {item.parentId && (
                                                <p className="text-sm text-gray-500 mt-1">
                                                    {isEnglish ? item.label.en : item.label.zh}
                                                </p>
                                            )}
                                        </div>

                                        <div className="relative" style={{ height: '793px' }}>
                                            {item.image && (
                                                <Image
                                                    src={getImagePath(item.image)}
                                                    alt={isEnglish ? item.label.en : item.label.zh}
                                                    fill
                                                    className="object-contain p-4"
                                                    priority
                                                />
                                            )}
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </main>
                </SidebarProvider>
            </div>
            <Footer />
        </div>
    );
} 