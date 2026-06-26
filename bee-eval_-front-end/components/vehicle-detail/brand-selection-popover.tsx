'use client'

import { useState, useEffect, useRef } from 'react'
import { Button } from "@/components/ui/button"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Checkbox } from "@/components/ui/checkbox"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Settings2 } from 'lucide-react'
import { Label } from "@/components/ui/label"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { AlertCircle } from "lucide-react"
import { motion, AnimatePresence } from 'framer-motion'
import { useLanguage } from '@/constants/language'
import { vehicleNameMapping } from './capability-assessment-data'

interface BrandSelectionPopoverProps {
    children: React.ReactNode;
    selectedBrands: string[];
    onBrandsChange: (brands: string[]) => void;
    currentVehicle: { name: string };
}

const allBrands = [
    "极氪-007",
    "理想-L6",
    "小米-SU7",
    "乐道-L60",
    "蔚来-ES6",
    "问界-M9"
].sort((a, b) => a.localeCompare(b));

export function BrandSelectionPopover({
    children,
    selectedBrands,
    onBrandsChange,
    currentVehicle
}: BrandSelectionPopoverProps) {
    const [isOpen, setIsOpen] = useState(false);
    const { language } = useLanguage();
    const isEnglish = language === 'en';

    const getDisplayName = (name: string): string => {
        return isEnglish ? (vehicleNameMapping.en[name] || name) : name;
    };

    useEffect(() => {
        if (!selectedBrands.includes(currentVehicle.name)) {
            onBrandsChange([...selectedBrands, currentVehicle.name]);
        }
    }, [selectedBrands, currentVehicle.name, onBrandsChange]);

    const handleBrandToggle = (brand: string) => {
        if (brand === currentVehicle.name) {
            return;
        }

        if (selectedBrands.includes(brand)) {
            onBrandsChange(selectedBrands.filter((b) => b !== brand));
        } else {
            if (selectedBrands.length >= 3) {
                return;
            }
            onBrandsChange([...selectedBrands, brand]);
        }
    };

    return (
        <Popover open={isOpen} onOpenChange={setIsOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    size="sm"
                    className="h-10 px-6 text-sm font-medium rounded-md bg-black/20 text-gray-400 hover:bg-black/30 border-0 transition-all duration-200 flex items-center gap-2 group"
                >
                    <div className="flex items-center gap-2">
                        {selectedBrands.length > 1 ? (
                            <>
                                <motion.span layout>{getDisplayName(currentVehicle.name)}</motion.span>
                                <motion.span
                                    layout
                                    initial={{ scale: 0.8, opacity: 0 }}
                                    animate={{ scale: 1, opacity: 1 }}
                                    className="text-gray-500"
                                >
                                    +{selectedBrands.length - 1}
                                </motion.span>
                            </>
                        ) : (
                            <motion.span layout>{isEnglish ? "Compare Models" : "设置对比车型"}</motion.span>
                        )}
                    </div>
                </Button>
            </PopoverTrigger>
            <PopoverContent
                className="w-[200px] p-0 bg-[#1C2028] border-gray-800"
                align="end"
                onInteractOutside={() => setIsOpen(false)}
                onEscapeKeyDown={() => setIsOpen(false)}
            >
                <ScrollArea className="h-[300px]">
                    <motion.div
                        className="space-y-1 p-1"
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.2 }}
                    >
                        <motion.label
                            layout
                            key={currentVehicle.name}
                            className="flex items-center space-x-2 px-3 py-2 rounded-md bg-black/20 cursor-not-allowed"
                            initial={{ opacity: 0, x: -10 }}
                            animate={{ opacity: 1, x: 0 }}
                        >
                            <Checkbox
                                checked={true}
                                disabled={true}
                                className="border-gray-600 data-[state=checked]:bg-amber-500 data-[state=checked]:border-amber-500 transition-colors duration-200"
                            />
                            <span className="text-sm text-amber-500">{getDisplayName(currentVehicle.name)}</span>
                        </motion.label>
                        {allBrands
                            .filter(brand => brand !== currentVehicle.name)
                            .map((brand, index) => (
                                <motion.label
                                    layout
                                    key={brand}
                                    className="flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-black/20 cursor-pointer transition-colors duration-200"
                                    initial={{ opacity: 0, x: -10 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    transition={{ delay: index * 0.05 }}
                                >
                                    <Checkbox
                                        checked={selectedBrands.includes(brand)}
                                        onCheckedChange={() => handleBrandToggle(brand)}
                                        disabled={!selectedBrands.includes(brand) && selectedBrands.length >= 3}
                                        className="border-gray-600 data-[state=checked]:bg-amber-500 data-[state=checked]:border-amber-500 transition-colors duration-200"
                                    />
                                    <span className="text-sm text-gray-400">{getDisplayName(brand)}</span>
                                </motion.label>
                            ))}
                    </motion.div>
                </ScrollArea>
                <motion.div
                    className="p-3 border-t border-gray-800"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ delay: 0.2 }}
                >
                    <motion.div
                        className="text-xs text-gray-400 mb-3"
                        layout
                    >
                        {isEnglish ? "Selected " : "已选择 "}
                        <motion.span
                            key={selectedBrands.length}
                            initial={{ scale: 0.5, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            className="mx-1 text-amber-500 font-medium"
                        >
                            {selectedBrands.length}
                        </motion.span>
                        {isEnglish ? "/3 brands" : "/3 个品牌"}
                    </motion.div>
                </motion.div>
            </PopoverContent>
        </Popover>
    );
}

