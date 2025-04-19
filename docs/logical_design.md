# QM Inventory System - Logical Database Design

## Entities and Relationships

### User
- Primary entity for system users (administrators, staff)
- Attributes:
  - user_id (PK): Unique identifier
  - username: Unique login name
  - password: Encrypted password
  - email: Contact email
  - role: User role (ADMIN, STAFF)
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### Product
- Central entity for inventory items
- Attributes:
  - product_id (PK): Unique identifier
  - name: Product name
  - description: Detailed description
  - sku: Stock Keeping Unit
  - price: Current selling price
  - quantity: Current stock level
  - category_id (FK): Reference to Category
  - image_url: Product image location
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### Category
- Classification of products
- Attributes:
  - category_id (PK): Unique identifier
  - name: Category name
  - description: Category description
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### Supplier
- Entity for product suppliers
- Attributes:
  - supplier_id (PK): Unique identifier
  - name: Company name
  - contact_person: Primary contact
  - email: Contact email
  - phone: Contact phone
  - address: Physical address
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### PurchaseOrder
- Records of product orders from suppliers
- Attributes:
  - purchase_order_id (PK): Unique identifier
  - supplier_id (FK): Reference to Supplier
  - order_date: Date of order
  - status: Order status (PENDING, APPROVED, RECEIVED)
  - total_amount: Total order cost
  - created_by (FK): Reference to User
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### PurchaseOrderItem
- Individual items in purchase orders
- Attributes:
  - purchase_order_item_id (PK): Unique identifier
  - purchase_order_id (FK): Reference to PurchaseOrder
  - product_id (FK): Reference to Product
  - quantity: Order quantity
  - unit_price: Price per unit
  - total_price: Total item cost

### Customer
- Entity for storing customer information
- Attributes:
  - customer_id (PK): Unique identifier
  - name: Customer name
  - email: Contact email
  - phone: Contact phone
  - address: Shipping address
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### SalesOrder
- Records of product sales to customers
- Attributes:
  - sales_order_id (PK): Unique identifier
  - customer_id (FK): Reference to Customer
  - order_date: Date of order
  - status: Order status (PENDING, PROCESSING, COMPLETED, CANCELLED)
  - total_amount: Total order cost
  - payment_status: Payment status (UNPAID, PAID)
  - created_by (FK): Reference to User
  - created_at: Timestamp of creation
  - updated_at: Timestamp of last update

### SalesOrderItem
- Individual items in sales orders
- Attributes:
  - sales_order_item_id (PK): Unique identifier
  - sales_order_id (FK): Reference to SalesOrder
  - product_id (FK): Reference to Product
  - quantity: Order quantity
  - unit_price: Price per unit
  - total_price: Total item cost

### InventoryLog
- Tracks all inventory changes
- Attributes:
  - inventory_log_id (PK): Unique identifier
  - product_id (FK): Reference to Product
  - quantity_change: Integer
  - change_type: Type of change (PURCHASE, SALE, ADJUSTMENT, RETURN)
  - reference_type: Related entity type (PURCHASE_ORDER, SALES_ORDER, MANUAL)
  - reference_id: Related entity ID
  - notes: Additional information
  - created_by (FK): Reference to User
  - created_at: Timestamp of change

## Relationships

1. Product - Category (Many-to-One)
   - Each product belongs to one category
   - A category can have multiple products

2. PurchaseOrder - Supplier (Many-to-One)
   - Each purchase order is from one supplier
   - A supplier can have multiple purchase orders

3. PurchaseOrder - PurchaseOrderItem (One-to-Many)
   - Each purchase order contains multiple items
   - Each item belongs to one purchase order

4. Product - PurchaseOrderItem (One-to-Many)
   - Each product can be in multiple order items
   - Each order item references one product

5. SalesOrder - Customer (Many-to-One)
   - Each sales order belongs to one customer
   - A customer can have multiple sales orders

6. SalesOrder - SalesOrderItem (One-to-Many)
   - Each sales order contains multiple items
   - Each item belongs to one sales order

7. Product - SalesOrderItem (One-to-Many)
   - Each product can be in multiple sales items
   - Each sales item references one product

8. Product - InventoryLog (One-to-Many)
   - Each product has multiple inventory logs
   - Each log entry is for one product

9. User - PurchaseOrder (One-to-Many)
   - Each user can create multiple purchase orders
   - Each purchase order is created by one user

10. User - SalesOrder (One-to-Many)
    - Each user can create multiple sales orders
    - Each sales order is created by one user

11. User - InventoryLog (One-to-Many)
    - Each user can create multiple inventory logs
    - Each log entry is created by one user

## Constraints

1. Unique Constraints:
   - User.username
   - Product.sku
   - Category.name
   - Customer.email

2. Not Null Constraints:
   - All primary keys
   - User (username, password, role)
   - Product (name, sku, price, quantity)
   - Category (name)
   - Supplier (name)
   - Customer (name, email)
   - PurchaseOrder (supplier_id, order_date, status)
   - PurchaseOrderItem (all fields)
   - SalesOrder (customer_id, order_date, status)
   - SalesOrderItem (all fields)
   - InventoryLog (all fields except notes)

3. Foreign Key Constraints:
   - Product.category_id → Category.category_id
   - PurchaseOrder.supplier_id → Supplier.supplier_id
   - PurchaseOrder.created_by → User.user_id
   - PurchaseOrderItem.purchase_order_id → PurchaseOrder.purchase_order_id
   - PurchaseOrderItem.product_id → Product.product_id
   - SalesOrder.customer_id → Customer.customer_id
   - SalesOrder.created_by → User.user_id
   - SalesOrderItem.sales_order_id → SalesOrder.sales_order_id
   - SalesOrderItem.product_id → Product.product_id
   - InventoryLog.product_id → Product.product_id
   - InventoryLog.created_by → User.user_id

4. Check Constraints:
   - Product.price >= 0
   - Product.quantity >= 0
   - PurchaseOrderItem.quantity > 0
   - PurchaseOrderItem.unit_price >= 0
   - SalesOrderItem.quantity > 0
   - SalesOrderItem.unit_price >= 0
   - PurchaseOrder.status in ('PENDING', 'APPROVED', 'RECEIVED')
   - SalesOrder.status in ('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELLED')
   - SalesOrder.payment_status in ('UNPAID', 'PAID')
   - InventoryLog.change_type in ('PURCHASE', 'SALE', 'ADJUSTMENT', 'RETURN')
   - InventoryLog.reference_type in ('PURCHASE_ORDER', 'SALES_ORDER', 'MANUAL')