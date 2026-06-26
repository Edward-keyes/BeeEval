'use client'

import * as React from "react"
import { motion } from "framer-motion"
import { Button } from "@/components/ui/button"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import { cn } from "@/lib/utils"

const brandLetters = ['不限', 'A', 'B', 'C', 'D', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'O', 'Q', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z']

export function BrandFilter() {
  const [activeBrand, setActiveBrand] = React.useState('不限')

  return (
    <div className="py-4">
      <div className="text-sm text-muted-foreground mb-2">品牌：</div>
      <ScrollArea className="w-full whitespace-nowrap">
        <div className="flex space-x-2">
          {brandLetters.map((letter) => (
            <motion.div
              key={letter}
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <Button
                variant="ghost"
                className={cn(
                  "h-8 px-4",
                  activeBrand === letter && "bg-primary/10 text-primary"
                )}
                onClick={() => setActiveBrand(letter)}
              >
                {letter}
              </Button>
            </motion.div>
          ))}
        </div>
        <ScrollBar orientation="horizontal" />
      </ScrollArea>
    </div>
  )
}

