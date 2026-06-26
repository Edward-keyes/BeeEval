import Image, { ImageProps } from 'next/image'

export function LazyImage(props: ImageProps) {
  return (
    <Image
      {...props}
      loading="lazy"
      placeholder="blur"
      blurDataURL="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mN8/+F9PQAI/wM6hgAAAABJRU5ErkJggg=="
    />
  )
}

