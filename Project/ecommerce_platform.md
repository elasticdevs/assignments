## UI Reference for E-commerce Platform
- [UI Reference](https://preview--shopper-supreme-frontend.lovable.app/login)
## Tech Stack  
- **Golang**  
- **Redis**  
- **PostgreSQL**  

## Libraries and Frameworks

### Gorilla Mux
- HTTP router and URL matcher  
- **[Gorilla Mux](https://www.gorillatoolkit.org/pkg/mux)**

### GORM
- ORM for Go (for working with databases)   
- **[GORM](https://gorm.io/)**

### Gin
- High-performance web framework  
- **[Gin](https://gin-gonic.com/)**

## Features  
- **Product Service:** CRUD operations for products.  
- **User Service:** Registration, login, and profile management.  
- **Cart Service:** Add/remove products from the cart.  
- **Order Service:** Place orders and track status.  
- **Redis:** Cache user sessions and frequently accessed products.  

---

## System Design

### Architecture Diagram Overview  
The architecture is divided into the following layers:  
- **Client Layer:** Web and/or Mobile clients  
- **Product Layer:** Product, User, Cart, Order services  
- **Data Layer:** PostgreSQL and Redis  
- **Monitoring & Logging:** ELK stack (Elasticsearch, Logstash, Kibana) (Optional)  

### System Design Breakdown  

#### 1. Client Layer (Frontend)  
- Web application (React/Angular) and Mobile app (Flutter/React Native).  
- Makes HTTP calls to the API Gateway.  

#### 2. Product Layer  

**a. Product Service**  
- CRUD Operations: Add, update, delete, and fetch product information.  
- Database: PostgreSQL (primary), Redis (caching).  
- Endpoints:  
  - `POST /api/products` - Add a product  
  - `GET /api/products/{id}` - Get product details  
  - `PUT /api/products/{id}` - Update product details  
  - `DELETE /api/products/{id}` - Delete product  

**b. User Service**  
- Authentication: JWT token-based authentication.  
- User Profile Management: Create, update, and view user profiles.  
- Session Caching: Store JWT tokens in Redis.  
- Database: PostgreSQL (user data), Redis (session data).  
- Endpoints:  
  - `POST /api/users/login` - User login  
  - `GET /api/users/profile` - Get user profile  
  - `PUT /api/users/{id}` - Update user details  
  - `DELETE /api/users/{id}` - Delete user profile  

**c. Cart Service**  
- Operations: Add, update, and remove items from the cart.  
- Cache: Store cart items in Redis for fast access.  
- Endpoints:  
  - `POST /api/cart/add` - Add product to cart  
  - `GET /api/cart/products` - View products in cart  
  - `DELETE /api/cart/remove/{id}` - Remove product  

**d. Order Service**  
- Operations: Place an order, update status, view order history.  
- External Integration: Uses Apache Camel to track shipments.  
- Database: PostgreSQL (orders), Redis (order status cache).  
- Endpoints:  
  - `POST /api/orders` - Place an order  
  - `POST /api/orders/{id}/cancel` - Cancel an order  
  - `GET /api/orders/history` - View order history  
  - `GET /api/orders/{id}/track` - Track order (updates time on each hit)  

#### 3. Data Layer  

**a. PostgreSQL**  
- Central database for user profiles, product catalog, and order history.  
- Implements data partitioning and replication for scalability.  

**b. Redis**  
- **Session Store:** Caches JWT tokens and active user sessions.  
- **Product Cache:** Stores product details for faster responses.  
- **Cart Cache:** Keeps track of items in the user's cart.  
- **Order Status Cache:** Quick lookup for order tracking data.  

#### 4. Communication Flow  

- **Product Addition:**  
  Admin adds a product -> Product Service -> Data stored in PostgreSQL.  

- **User Registration:**  
  User registers -> User Service -> Session stored in Redis.

- **User Login:**  
  User login -> User Service -> Session stored in Redis.  

- **Add to Cart:**  
  User adds item -> Cart Service -> Data stored in Redis.  

- **Place Order:**  
  Checkout -> Order Service -> Stored in PostgreSQL.  

#### 5. Basic Database schema
```
User Table:
uuid id
string email
string password
string name
Role role (enum: admin, user)

Product Table:
uuid id
string name
float64 price
string description
string imageUrl

Cart Table:
uuid id
uuid userId

Cart Item Table:
int id
uuid cart_id
uuid product_id
int product_quantity 

Order Table:
uuid orderId
uuid userId
uuid cartId
float64 total
```

#### 6. Project Structure

```plaintext
ecommerce-platform/
|            
├── configs/                      # App config 
│   └── db.go                       # DB connection & migrations
│   └── redis.go                    # Redis connection (caching, session)
│
├── core/
│   ├── product/
│   │   ├── add_product.go          # Product HTTP handlers
│   │   ├──          
│   │   ├──         
│   │        
│   ├── user/
│   │   ├── add_user.go          # User HTTP handlers
│   │   ├──          
│   │   ├──         
│   │       
│   ├── cart/
│   │   ├── add_item_to_cart.go          # Cart HTTP handlers
│   │   ├── 
│   │   ├── 
│   │         
│   ├── order/
│   │   ├── create_order.go          # Order HTTP handlers
│   │   ├── 
│   │   ├── 
│
├── utils/
│   ├── utils.go
│       
├── constants/
│   ├── constants.go
│
├── services/
│   ├── services.go
│ 
├── middleware/
│   └── auth.go              # JWT Auth, logging, etc.
│
├── models/
│   ├── user.go         
│   ├── product.go
│   ├── cart.go
│   └── order.go
│        
├── routes/
│   └── routes.go                # All routes grouping (product, user, cart, order)
│
├── main.go                     # App entry point
├── go.mod
└── go.sum
