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
- ### <a name="_czpvqcxywgaj"></a>Creating a platform to support the staff to interact with customers easier.
## <a name="_zbonwbblfjcc"></a>**2.3 Actor**

|**No.**|**Actor**|**Description**|
| :- | :- | :-: |
|1|Admin|Admin is a person who logged in the “Jewelry Sale System At The Store” System as “admin” role. Admin is the person who manages all managers activities and monitors the dashboard.|
|2|Manager|Manager is a person who logged in the “Jewelry Sale System At The Store” System as “manager” role. Manager is the person who manages all staff, products, and promotion.|
|3|Staff|User who interacting with customers|
## <a name="_u6aap33vz8g8"></a>**2.4 List of Feature:**
### <a name="_bukxuss52tqa"></a>**Admin**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Manager|Create a new account for manager role|
|2\.|Update Manager Info|Edit manager information, enable/disable manager account|
|3\.|Login|Log in into the system as an administrative role|
|4\.|View Dashboard|View dashboard statistics in an interval of time|
|5\.|View Manager List|View list of managers.|
|6\.|View Manager Info|View manager detailed info.|

### <a name="_q1ykqcnxjsj7"></a>**Manager**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Staff|Create a new account for manager role|
|2\.|Update Staff Info|Edit staff information, enable/disable staff account|
|3\.|Login|Log in into the system as a manager role|
|4\.|Add Product|Add new product into product list|
|5\.|Delete Product |Delete product in product list|
|6\.|Update Product|Edit product info in product list|
|7\.|View Product List|View list of product (name, price, stock) available in store|
|8\.|View Product Info|View product’s specified info such as name, price, stock, weight,...|
|9\.|View Counter Info|View the revenue and staff info of the counter under the management of the manager|
|10\.|Display Product|Display product info on external screen|
|11\.|Create Counter|Assign staff to a new counter.|
|12\.|Update Counter|Edit staff information in the counter.|
|13\.|Add Promotion|Add promotion code + price for deduction + started date + ended date|
|14\.|Delete Promotion|Delete promotion code|
|15\.|View Promotion List|View list of valid/invalid promotion code|
|16\.|View Promotion Request|View the list of promotions request .|
|17\.|Accept/Refuse Promotion Request|Accept/Refuse promotion to apply for the invoice|
### <a name="_ri3rlguyx5e8"></a>**Staff**

|**No.**|**Use case**|**Description**|
| :- | :-: | :-: |
|1\.|Add Item|Add a new item in the cart by scanning barcode or by inputting product code directly|
|2\.|Delete Item|Remove item from the cart|
|3\.|Update Cart|Update item’s quantity in the cart|
|4\.|Login|Log in into the system as a staff role|
|5\.|View Cart|<p>View the product in the cart and total price. </p><p>- ***Price of product*** = historical cost \*  gold price ratio.</p><p>- ***Historical cost*** = (Gold price at current time \* product weight) + manufacture price + stone price.</p>|
|6\.|Print Receipt|Print receipt for customer after payment|
|7\.|Print Warranty|Print warranty for each accessory that customers has purchased|
|8\.|Check out|Confirm the payment and change the stock|
|9\.|Refund|Reclaim the product that customer had bought from the store before. |
|10\.|View Product Info|View product information such as name, price, stock, weight,...|
|11\. |View Order List|View list of order|
|12\.|View Order Info|View order’s properties|
|13\.|Search Order|Search order based on customer’s phone number and order ID.|
|14\.|Update Order|Edit order info (status, delivery date).|
|15\.|Request Promotion|The staff sent promotions request to the manager.|
|16\.|View Customer Info|View customer info + their point|
|17\.|Search Customer|Search customer info base on phone number|
## <a name="_iybf9vkh6owx"></a>**2.5 Statistics**
- ### <a name="_5ug0a6x246xj"></a>**Dashboard Statistics**
- #### <a name="_btr78ysm3c8j"></a>Total Revenue.
- ### <a name="_n5jjyx9dwkvz"></a>**Accessory Statistic**
  - #### <a name="_6tco4dynj5cl"></a>Product ID
  - #### <a name="_iktey6otpcci"></a>Product Name.
  - Price.
  - Stock.
  - Description.(Weight, Main Stone, Sub Stone, Number of Main Stone, Number of Sub Stone, Brand, Cut)
  - Gender.
- ### <a name="_e4q3kbu77li"></a>**Staff/Manager/Customer Info**
  - User ID.
  - Full Name.
  - Address.
  - Role.
  - Phone Number.
  - Email.
  - Date of Birth.
  - Gender.
  - Point. (Customer only)
- ### <a name="_houm7xmm2mdn"></a>**Order Info:**
  - Order ID.
  - Customer Name.
  - Phone Number.
  - Gender.
  - Delivery Address.
  - Payment Method.
  - Payment Status.(Paid/Unpaid)
  - Order Date Time.
  - Delivery Date Time.
  - Delivery Status.
  - List of purchased products:
+ Name.
+ Quantity.
+ Price. (Individual Price \* Quantity)
- Delivery Cost. (Based on delivery policy)
- Sale Promotion. (optional)
- Total Cost. (Sum of Product’s Price)
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
- Step 3 (Optional): Customers want to deliver to a specific address.
+ Input delivery address.
- #### <a name="_z59el25dbj0l"></a>**Create order through phone:**
  - Step 1: Check product stock.
- Step 2: Input Customer Information.
  + Name.(Required)
  + Phone Number.(Required)
  + Gender.
- Step 3: Choose the receive method.
+ Delivery.
+ At the store. (Choose branch address)
- Step 4: Delivery Method.
+ ` `Input delivery address
+ Note.(optional)
+ Issue company invoice.(optional)
- Step 4.5: At the store Method.
+ Choose a city/province and district based on the given list.
+ Choose a branch.
+ If not any branch available on the location then show the statement “No stores are available on this location.”
+ Note.(optional)
+ Issue company invoice.(optional)
- Step 5: Check out. (Only delivery method)
+ COD. (cash on delivery)
- ### <a name="_8nch2bsz9psj"></a>**Note:**
  - Staff can only update Delivery Date and Order Status.
  - When Order Status changes to Finished Delivery, payment method must update to Paid.
## <a name="_dywnadfm98r1"></a>**2.7 Delivery Management**
- ### <a name="_7tqjamujqkrq"></a>**There are 5 stages of product delivery process:**
  - Stage 1: Contact Delivery Unit.
  - Stage 2: Prepare Product.
  - Stage 3: Transfer Product To Delivery Unit.
  - Stage 4: Deliver Product.
  - Stage 5: Finished Delivery.
  - Stage 5.5: Delivered Failed.
- If stage 4 fails, the product will be restocked and the order will be changed to “Delivered Failed” status. 
## <a name="_q7fi7kbl5rz"></a>**2.8. Staff Management**
- 1 manager manages 3 counters.

# <a name="_peiwz1qxl442"></a>**3.Policy:**
## <a name="_q6hg64meqm2a"></a>**3.1 Points Policy**
- The payment will be turned into points, with every 10 million vnd = 1 point.
- Points can be used in various activities such as discounts.
- The point will be printed within the receipt in a form of code. (User will input the code into their account gift code feature or in web gift code page)
## <a name="_c225aics878e"></a>**3.2 Discount Policy**
- 1 point = 100.000 vnd discount
- ### <a name="_uocvu7v7jcv2"></a>**Discount = Discount during promotion (optional) + Discount policy (optional)**
**Note:** For customers who can be affected by discount policy need approval of the manager before applying the discount into invoice.  
## <a name="_teq3mxl6k6qv"></a>**3.2 Purchasing Policy**
- For women’s accessory + normal stone, Shop will only buy the real gold part. 
- Reclaim the jewels which customers had bought with 70% sold price.
- The gold price will be based on a real-time price chart. 

## <a name="_uda3l5cdstlb"></a>**3.3 Warranty Policy**
- Every accessory can be returned to the shop within 7 days under these condition:
+ The accessory must remain intact and not have any sign of being used.
- Return procedure:
+ Step 1: Contact the staff to request returning product. (To facilitate the process, customer should provide warranty of the returning product)
+ Step 2: We will check the product’s warranty and product’s integrity then respond to the customer.
+ Step 3: We will perform a refund/replace procedure. (100% sold price)
## <a name="_5qfy5eirmo3o"></a>**3.4 Delivery Policy**

|**Distance**|**Expected Delivery Time**|**Delivery Charge**|
| :-: | :-: | :-: |
|Below 20 km in HCM|Same day or next day|<p>First 2 km: 18.000 VND</p><p>Above 2 km: 5.000 VND/ km</p>|
|Below 20 km in other locations|From 1-3 days|<p>First 2 km: 20.000 VND</p><p>Above 2 km: 5.000 VND/ km</p>|
|Above 20 km|From 2-7 days|5\.000 VND/km|


- The distance calculated from the nearest store branch where the product is available.
- Time may vary depending on the location.
- In case the order costs above 20 million VND, delivery charge will be free but customers need to pay in deposit.

  |**Order Cost**|**Deposit Rule**||
  | :-: | :-: | :- |
  |Between 20 million VND - 50 million VND |30%||
  |From 50 million VND - 100 million VND|50%||
  |Above 100 million VND|70%||

- If customers have any complaints in any processes, please call the number 1800 XX XX XX in time between 08:00 to 21:00 (holiday included).
- Time solving the complaints will be from 2-7 working days.


