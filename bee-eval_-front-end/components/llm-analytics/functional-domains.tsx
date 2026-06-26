import { motion } from "framer-motion"

const domains = [
  "出行域", "车书域", "车控域", "闲聊域", "娱乐域", "创作域"
];

interface FunctionalDomainsProps {
  selectedDomain: string;
  onSelectDomain: (domain: string) => void;
}

export function FunctionalDomains({ selectedDomain, onSelectDomain }: FunctionalDomainsProps) {
  return (
    <>
      {domains.map((domain, index) => (
        <motion.div key={domain}>
          <div className="text-lg">
            {""}
          </div>
        </motion.div>
      ))}
    </>
  )
}

