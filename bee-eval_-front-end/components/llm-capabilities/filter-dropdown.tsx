'use client'

import * as React from "react"
import { motion, AnimatePresence } from "framer-motion"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"
import { cn } from "@/lib/utils"
import { Check, ChevronDown } from "lucide-react"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

interface FilterOption {
  id: string;
  label: string;
  description?: string;
}

interface FilterDropdownProps {
  label: string;
  options: FilterOption[];
  selectedOption: string;
  onSelect: (id: string) => void;
}

export function FilterDropdown({
  label,
  options,
  selectedOption,
  onSelect
}: FilterDropdownProps) {
  const [open, setOpen] = React.useState(false)
  const selectedItem = options.find(opt => opt.id === selectedOption)

  return (
    <DropdownMenu open={open} onOpenChange={setOpen}>
      <DropdownMenuTrigger asChild>
        <Button
          variant="outline"
          size="sm"
          className={cn(
            "h-8 px-4 text-sm font-medium rounded-md transition-all duration-200",
            "bg-black/20 text-gray-400 hover:bg-black/30 border-0",
            "data-[state=open]:bg-black/30"
          )}
        >
          <span className="truncate">{selectedItem?.label || label}</span>
          <motion.div
            animate={{ rotate: open ? 180 : 0 }}
            transition={{ duration: 0.2 }}
            className="ml-2"
          >
            <ChevronDown className="h-4 w-4 opacity-50" />
          </motion.div>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent
        align="start"
        className={cn(
          "w-[240px] p-1",
          "bg-[#1C2028] border-gray-800",
          "shadow-lg shadow-black/20"
        )}
      >
        <ScrollArea className="h-[var(--radix-dropdown-menu-content-available-height)] max-h-[300px]">
          <div className="space-y-1">
            {options.map((option) => (
              <DropdownMenuItem
                key={option.id}
                onSelect={() => onSelect(option.id)}
                className={cn(
                  "flex items-center gap-2 px-3 py-2 text-sm rounded-md cursor-pointer",
                  "text-gray-400 hover:text-white",
                  "hover:bg-black/20 focus:bg-black/20",
                  "data-[highlighted]:bg-black/20 data-[highlighted]:text-white",
                  selectedOption === option.id && "bg-amber-500/10 text-amber-500"
                )}
              >
                <div className="flex-1 flex flex-col gap-0.5">
                  <span className="font-medium">{option.label}</span>
                  {option.description && (
                    <span className="text-xs text-gray-500">{option.description}</span>
                  )}
                </div>
                {selectedOption === option.id && (
                  <Check className="h-4 w-4 text-amber-500" />
                )}
              </DropdownMenuItem>
            ))}
          </div>
        </ScrollArea>
      </DropdownMenuContent>
    </DropdownMenu>
  );
} 