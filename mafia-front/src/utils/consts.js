export const phoneRegex = /^\+?\d+$/;

export const taskTypeOptions = [
  {value: "CALL", label: "Call"},
  {value: "SUPPLIES", label: "Supplies"},
  {value: "PERMANENT_GUARDING", label: "Permanent guarding"}
]

export const taskStatusOptions = [
  {value: "NEW", label: "New"},
  {value: "IN_PROGRESS", label: "In progress"},
  {value: "FINISHED", label: "Finished"},
  {value: "CANCELED", label: "Canceled"}
]

export const statusColor = {
  "NEW": "primary",
  "IN_PROGRESS": "info",
  "FINISHED": "success",
  "CANCELED": "danger"
}