'use client'

import { motion, MotionProps } from 'framer-motion'
import { ReactNode } from 'react'

interface AnimatedElementProps extends MotionProps {
  children: ReactNode
}

export function AnimatedElement({ children, ...motionProps }: AnimatedElementProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      {...motionProps}
    >
      {children}
    </motion.div>
  )
}

