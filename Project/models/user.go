package models

import "github.com/google/uuid"

type Role string

const (
	Admin    Role = "ADMIN"
	Customer Role = "CUSTOMER"
)

type User struct {
	BaseModel
	UUID     uuid.UUID `json:"uuid" gorm:"type:uuid;primaryKey"`
	Email    string    `json:"email" gorm:"required,unique"`
	Password string    `json:"password"`
	Name     string    `json:"name"`
	Role     Role      `json:"role"`
}
