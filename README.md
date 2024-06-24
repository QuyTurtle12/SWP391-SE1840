# <a name="_ehqt08w09yqu"></a>	**Jewelry Sales System At The Store**
<a name="_5eoyzn572g8y"></a>Software Requirement Specification

# <a name="_fkqr85w4d2ra"></a>**1.Team Member:**
- ## <a name="_w68qof78tzz7"></a>Nguyễn Ngọc Quy SE171134
- ## <a name="_bg37llco4uwn"></a>Phan Phạm Hòa SE171730
- ## <a name="_fj393ygqd98m"></a>Huỳnh Thái Tài SE171320
- ## <a name="_x6nr5sldp5qf"></a>Trần Trọng Nhân SE171973
# <a name="_kex60s1c7drv"></a>**2.Requirement:**
## <a name="_srcmrxiyu3np"></a>**2.1 Name of The System:**
### <a name="_w5fmk68csmvr"></a>**Jewelry Sales System At The Store**
### <a name="_s9krq3x2ti5"></a>(Phần mềm bán hàng trang sức tại cửa hàng)

## <a name="_1edxs1g18lnh"></a>**2.2 Purpose of The System:**
This project's goal is to make a system that can help shops whose main category is jewelry or accessories to be able to sell products, and manage staff.
## <a name="_zbonwbblfjcc"></a>**2.3 Actor**

|**No.**|**Actor**|**Description**|
| :- | :- | :-: |
|0\.|Registered User|Registered User is a person who has registered into the system. A Registered User is the person who can log in the system to get a role and work according to that role.|
|1|Admin|Admin is a person who logged in the “Jewelry Sale System At The Store” System as “admin” role. Admin is the person who manages all managers activities and monitors the dashboard.|
|2|Manager|Manager is a person who logged in the “Jewelry Sale System At The Store” System as “manager” role. Manager is the person who manages all staff, products, and promotion.|
|3|Staff|User who interacting with customers|
## <a name="_u6aap33vz8g8"></a>**2.4 List of Feature:**
### <a name="_eomx89kv76oz"></a>**Registered User**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Login|Log in into the system as a specific role by email, password|
|2\.|Logout|Log out the system|

### <a name="_bukxuss52tqa"></a>**Admin**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Manager|Create a new account for manager role|
|2\.|Update Manager Info|Edit manager information|
|3\.|Change Manager Status|enable/disable manager account status|
|4\.|View Dashboard|View dashboard statistics in an interval of time|
|5\.|View Manager List|View list of managers.|
|6\.|View Manager Info|View manager detailed info.|

### <a name="_q1ykqcnxjsj7"></a>**Manager**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Staff|Create a new account for manager role|
|2\.|Update Staff Info|Edit staff information, enable/disable staff account|
|3\.|Add Product|Add new product into product list|
|4\.|Delete Product |Delete product in product list|
|5\.|Update Product|Edit product info in product list|
|6\.|View Product List|View list of product (name, price, stock) available in store|
|7\.|View Product Info|View product’s specified info such as name, price, stock, weight,...|
|8\.|View Counter Info|View the revenue and staff info of the counter under the management of the manager|
|9\.|Display Gold Price Chart|Display product info on external screen|
|10\.|Update Counter|Edit staff information in the counter.|
|11\.|Add Promotion|Add promotion code + price for deduction + started date + ended date|
|12\.|Delete Promotion|Delete promotion code|
|13\.|View Promotion List|View list of valid/invalid promotion code|
|14\.|View Promotion Request|View the list of promotions request|
|15\.|Enable/Disable Promotion Request|Enable/Disable promotion to apply for the invoice|
|16\.|View Staff List|View list of staff|
|17\.|View Staff Info|View staff detail info|
|18\.|Search Staff|Search staff info by phone number |
|19\.|Search Product|Search product by product ID, name,...|
### <a name="_ri3rlguyx5e8"></a>**Staff**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Item|Add a new item in the cart by scanning barcode or by inputting product code directly|
|2\.|Delete Item|Remove item from the cart|
|3\.|Update Cart|Update item’s quantity in the cart|
|4\.|View Cart|<p>View the product in the cart and total price. </p><p>- ***Price of product*** = historical cost \*  gold price ratio.</p><p>- ***Historical cost*** = (Gold price at current time \* product weight) + manufacture price + stone price.</p>|
|5\.|Print Receipt|Print receipt for customer after payment|
|6\.|Print Warranty|Print warranty for each accessory that customers has purchased|
|7\.|Check out|Confirm the payment and change the stock|
|8\.|Refund|Reclaim the product that customer had bought from the store before. |
|9\.|View Product Info|View product information such as name, price, stock, weight,...|
|10\. |View Order List|View list of order|
|11\.|View Order Info|View order’s properties|
|12\.|Search Order|Search order based on customer’s phone number and order ID.|
|13\.|Update Order|Edit order info (status, delivery date).|
|14\.|View Customer Info|View customer info + their point|
|15\.|Search Customer|Search customer info base on phone number|
|16\.|View Customer List|View List of Customer|
|17\.|View Product List|View list of Product.|
|18\.|Change Counter Status|Announce to customers that this counter is unoccupied/occupied. |
|19\.|Search Product|Search product by product ID, name,...|
## <a name="_iybf9vkh6owx"></a>**2.5 Statistics**
- ### <a name="_5ug0a6x246xj"></a>**Dashboard Statistics**
- #### <a name="_btr78ysm3c8j"></a>Total Revenue.
- Amount of Order.
- ### <a name="_n5jjyx9dwkvz"></a>**Product Statistic**
  - #### <a name="_6tco4dynj5cl"></a>Product ID
  - #### <a name="_iktey6otpcci"></a>Product Name.
  - Price.
  - Refund Price.
  - Stock.
  - Description.(Weight, Main Stone, Sub Stone, Number of Main Stone, Number of Sub Stone, Brand, Cut)
  - Barcode.
  - Labor Cost.
  - Stone Cost.
  - Promotion ID.
  - Gender.
  - Category.
  - Status.
- ### <a name="_e4q3kbu77li"></a>**Staff/Manager/Customer Info**
  - User ID.
  - Full Name.
  - Address.(Staff/Manager only)
  - Role. 
  - Phone Number.
  - Email.
  - Date of Birth.
  - Gender.
  - Point. (Customer only)
- ### <a name="_houm7xmm2mdn"></a>**Order/Refund Info:**
  - Order ID.
  - Customer ID.
  - Customer Name.
  - Phone Number.
  - Gender.
  - Order Date Time.
  - List of purchased products:
+ Name.
+ Quantity.
+ Price. (Individual Price \* Quantity)
- Sale Promotion. (optional)
- Total Cost. (Sum of Product’s Price)
- ### <a name="_w2701h3dc4aq"></a>**Promotion Info:**
  - Promotion ID.
  - Description.
  - Discount Rate.
  - Status.
## <a name="_g8a9n8m112do"></a>**2.6 Order Management**
- ### <a name="_mimgxbjzj5ae"></a>**Order Process:**
- #### <a name="_hy2o9g397qwx"></a>**Create order at store:**
  - Step 1: Input Customer Information.
  + Name.(Required)
  + Phone Number.(Required)
  + Gender.
- Step 2: Check out
+ Choose payment method: cash, momo.
+ Issue company invoice.(optional)
+ Perform payment.
## <a name="_q7fi7kbl5rz"></a>**2.7. Staff Management**
- 1 manager manages 3 counters.
- Manager can check the revenue of each counter and each staff member.

# <a name="_peiwz1qxl442"></a>**3.Policy:**
## <a name="_q6hg64meqm2a"></a>**3.1 Points Policy**
- The payment will be turned into points, with every 10 million vnd = 1 point.
- Points can be used in various activities such as discounts.
- The point will be printed within the receipt in a form of code. (User will input the code into their account gift code feature or in web gift code page)
## <a name="_c225aics878e"></a>**3.2 Discount Policy**
- ### <a name="_uocvu7v7jcv2"></a>**Discount Money = Discount during sale promotion (optional) + Discount for customer(optional) + Discount money per point  (optional)**
- 2 types of discount: 
+ Discount during sale promotion.
+ Discount for customers.
- 1 point = 100.000 vnd discount.
## <a name="_teq3mxl6k6qv"></a>**3.2 Purchasing Policy**
- For women’s accessory + normal stone, Shop will only buy the real gold part. 
- Reclaim the jewels which customers had bought with a percentage of sold price.
- The gold price will be based on a real-time price chart. 

## <a name="_uda3l5cdstlb"></a>**3.3 Warranty Policy**
- Every accessory can be returned to the shop within 7 days under these condition:
+ The accessory must remain intact and not have any sign of being used.
- Return procedure:
+ Step 1: Contact the staff to request returning product. (To facilitate the process, customer should provide warranty of the returning product)
+ Step 2: We will check the product’s warranty and product’s integrity then respond to the customer.
+ Step 3: We will perform a refund/replace procedure. (100% sold price)



