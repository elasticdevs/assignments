package main

import (
	"fmt"
	"server/config.go"
	"server/routes"
)

func main() {

	config.InitDB()

	router := routes.SetupRouter()

	router.Run(":8080")
	fmt.Println("Server is running on pert :8080")
}
