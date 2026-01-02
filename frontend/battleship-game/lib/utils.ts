import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import { ShipName } from "@/lib/types";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatShipName(name?: ShipName | null) {
  if (!name) return "";

  return name
      .toLowerCase()
      .split("_")
      .map(word => word.toUpperCase())
      .join(" ");
}
