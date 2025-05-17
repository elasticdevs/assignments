package models

import "github.com/google/uuid"

type Status string

const (
	Active   Status = "active"
	InActive Status = "inactive"
)

type CartItem struct {
	BaseModel

	CartID          uuid.UUID `json:"cart_id" gorm:"type:uuid;not null;index"`
	Cart            Cart      `json:"cart" gorm:"foreignKey:CartID;references:UUID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
	ProductID       uuid.UUID `json:"product_id" gorm:"type:uuid;not null"`
	Product         Product   `json:"product" gorm:"foreignKey:ProductID;references:UUID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
	ProductQuantity uint      `json:"product_quantity"`
}

type Cart struct {
	BaseModel

	UUID      uuid.UUID  `json:"uuid" gorm:"type:uuid;uniqueIndex"`
	UserID    uuid.UUID  `json:"user_id" gorm:"type:uuid;not null"`
	User      User       `json:"user" gorm:"foreignKey:UserID;references:UUID;constraint:OnUpdate:CASCADE,OnDelete:CASCADE"`
	Status    Status     `json:"status" gorm:"type:varchar(20);not null;check:status IN ('active','inactive')"`
	CartItems []CartItem `json:"cart_items" gorm:"foreignKey:CartID"`
}
