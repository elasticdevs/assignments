package models

import "github.com/google/uuid"

type Product struct {
	BaseModel

	UUID        uuid.UUID `json:"uuid" gorm:"type:uuid,primaryKey"`
	Name        string    `json:"name"`
	Price       float64   `json:"price"`
	Description string    `json:"description"`
	ImageUrl    string    `json:"image_url"`
	Rating      float32   `json:"rating"`
}
