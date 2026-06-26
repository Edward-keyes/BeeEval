import Link from 'next/link'
import { Button } from "@/components/ui/button"
import { ChevronLeft } from 'lucide-react'
import { motion } from "framer-motion"

export function BackButton() {
  return (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Link href="/vehicle-detail">
        <Button variant="ghost" size="sm" className="text-primary hover:text-primary/80">
          <ChevronLeft className="mr-2 h-4 w-4" />
          返回
        </Button>
      </Link>
    </motion.div>
  )
}

