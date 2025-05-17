package routes

import (
	"fmt"

	"github.com/gin-gonic/gin"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()

	// r.Use(middleware.AuthMiddleware())

	
	api := r.Group("/api/v1")
	{
		// productRoutes := api.Group("/products")
		// {
		// 	productRoutes.GET("/", product.GetAllProducts)
		// 	productRoutes.POST("/", product.CreateProduct)
		// 	productRoutes.GET("/:id", product.GetProductByID)
		// 	productRoutes.PUT("/:id", product.UpdateProduct)
		// 	productRoutes.DELETE("/:id", product.DeleteProduct)
		// }

		// userRoutes := api.Group("/users")
		// {
		// 	userRoutes.POST("/register", user.RegisterUser)
		// 	userRoutes.POST("/login", user.LoginUser)
		// 	userRoutes.GET("/profile", user.GetUserProfile)
		// }

		// cartRoutes := api.Group("/cart")
		// {
		// 	cartRoutes.GET("/", cart.GetCartItems)
		// 	cartRoutes.POST("/add", cart.AddToCart)
		// 	cartRoutes.PUT("/update", cart.UpdateCartItem)
		// 	cartRoutes.DELETE("/remove/:id", cart.RemoveCartItem)
		// }

		// orderRoutes := api.Group("/orders")
		// {
		// 	orderRoutes.POST("/", order.PlaceOrder)
		// 	orderRoutes.GET("/", order.GetUserOrders)
		// 	orderRoutes.GET("/:id", order.GetOrderDetails)
		// }
	}

	fmt.Println(api)

	return r
}
