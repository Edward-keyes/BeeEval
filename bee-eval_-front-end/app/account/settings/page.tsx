'use client'

import { useState } from 'react'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Separator } from "@/components/ui/separator"
import { Switch } from "@/components/ui/switch"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"

export default function SettingsPage() {
    const [settings, setSettings] = useState({
        reportNotification: true,
        systemNotification: true,
        securityNotification: true,
        dataAnalytics: true
    });

    const handleSettingChange = (key: keyof typeof settings) => {
        setSettings(prev => ({
            ...prev,
            [key]: !prev[key]
        }));
    };

    const handleReset = () => {
        setSettings({
            reportNotification: true,
            systemNotification: true,
            securityNotification: true,
            dataAnalytics: true
        });
    };

    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">设置</h3>
                <p className="text-sm text-muted-foreground">
                    管理您的应用偏好和通知设置
                </p>
            </div>
            <Separator />
            <div className="grid gap-6">
                <Card>
                    <CardHeader>
                        <CardTitle>通知设置</CardTitle>
                        <CardDescription>配置您希望接收的通知类型</CardDescription>
                    </CardHeader>
                    <CardContent className="grid gap-4">
                        <div className="flex items-center justify-between">
                            <div className="space-y-0.5">
                                <Label>报告通知</Label>
                                <p className="text-sm text-muted-foreground">
                                    当新的评测报告生成时通知我
                                </p>
                            </div>
                            <Switch
                                checked={settings.reportNotification}
                                onCheckedChange={() => handleSettingChange('reportNotification')}
                            />
                        </div>
                        <Separator />
                        <div className="flex items-center justify-between">
                            <div className="space-y-0.5">
                                <Label>系统通知</Label>
                                <p className="text-sm text-muted-foreground">
                                    接收系统更新和功能优化通知
                                </p>
                            </div>
                            <Switch
                                checked={settings.systemNotification}
                                onCheckedChange={() => handleSettingChange('systemNotification')}
                            />
                        </div>
                        <Separator />
                        <div className="flex items-center justify-between">
                            <div className="space-y-0.5">
                                <Label>安全提醒</Label>
                                <p className="text-sm text-muted-foreground">
                                    接收账号安全相关的提醒
                                </p>
                            </div>
                            <Switch
                                checked={settings.securityNotification}
                                onCheckedChange={() => handleSettingChange('securityNotification')}
                            />
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>数据管理</CardTitle>
                        <CardDescription>管理您的数据和隐私设置</CardDescription>
                    </CardHeader>
                    <CardContent className="grid gap-4">
                        <div className="flex items-center justify-between">
                            <div className="space-y-0.5">
                                <Label>数据分析</Label>
                                <p className="text-sm text-muted-foreground">
                                    允许收集使用数据以改进服务
                                </p>
                            </div>
                            <Switch
                                checked={settings.dataAnalytics}
                                onCheckedChange={() => handleSettingChange('dataAnalytics')}
                            />
                        </div>
                        <Separator />
                        <div className="flex items-center justify-between">
                            <div>
                                <Label>导出数据</Label>
                                <p className="text-sm text-muted-foreground">
                                    导出您的所有数据副本
                                </p>
                            </div>
                            <Button
                                variant="outline"
                                onClick={() => {
                                    // TODO: Implement data export functionality
                                    // console.log('Exporting data...');
                                }}
                            >
                                导出
                            </Button>
                        </div>
                    </CardContent>
                </Card>

                <div className="flex justify-end gap-4">
                    <Button
                        variant="outline"
                        onClick={handleReset}
                    >
                        重置设置
                    </Button>
                    <Button
                        onClick={() => {
                            // TODO: Implement save functionality
                            // console.log('Saving settings...');
                        }}
                    >
                        保存更改
                    </Button>
                </div>
            </div>
        </div>
    )
} 