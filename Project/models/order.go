package models

import (
	"github.com/google/uuid"
)

type OrderStatus string

const (
	InProgress OrderStatus = "in_progress"
	Completed  OrderStatus = "completed"
	Cancelled  OrderStatus = "cancelled"
)

type Order struct {
	BaseModel

	OrderID uuid.UUID   `json:"order_id" gorm:"type:uuid;uniqueIndex"`
	UserID  uuid.UUID   `json:"user_id" gorm:"type:uuid;not null"`
	User    User        `json:"user" gorm:"foreignKey:UserID;references:UUID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
	CartID  uuid.UUID   `json:"cart_id" gorm:"type:uuid;not null"`
	Cart    Cart        `json:"cart" gorm:"foreignKey:CartID;references:UUID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
	Total   float64     `json:"total" gorm:"not null"`
	Status  OrderStatus `json:"status" gorm:"type:varchar(20);not null;check:status IN ('in_progress','completed','cancelled')"`
}
